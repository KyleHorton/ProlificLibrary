package horton.kyle.prolificlibrary;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.ShareActionProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Prolific Library Application
 * Author: Kyle Horton
 * 5/22/2018
 *
 * This class displays the details on a selected book.
 */
public class DisplayDetailActivity extends AppCompatActivity {

    private Button checkout, update;
    private TextView title, author, publisher, lastChecked, lastCheckedBy, categories;
    private ShareActionProvider shareActionProvider;
    private int id;
    private RetrofitInstance retrofitInstance;
    private BookDetails book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_detail);

        checkout = (Button) findViewById(R.id.checkout);
        title = (TextView) findViewById(R.id.detail_title);
        author = (TextView) findViewById(R.id.detail_author);
        publisher = (TextView) findViewById(R.id.detail_publisher);
        categories = (TextView) findViewById(R.id.detail_categories);
        lastChecked = (TextView) findViewById(R.id.detail_last_checked);
        lastCheckedBy = (TextView) findViewById(R.id.detail_last_checked_by);
        update = (Button) findViewById(R.id.updateBook);

        id = getIntent().getIntExtra("id", -1);
        book = new BookDetails();

        //adds toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Details");
        setSupportActionBar(toolbar);

        // creates back navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (id == -1) {
            // creates alert
            AlertDialog.Builder builder = new AlertDialog.Builder(DisplayDetailActivity.this);
            builder.setTitle("ERROR");
            builder.setMessage("Book ID cannot be found. Returning to library.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // if yes, close alert, bring user back to library
                            dialog.cancel();
                            startActivity(new Intent(DisplayDetailActivity.this, BrowseBooksActivity.class));
                            finish();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();

        } else {

            recallBook(id);
            book.setId(id);

        }

        //when checkout is clicked
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // builds dialog to allow for user to input name
                AlertDialog.Builder builder = new AlertDialog.Builder(DisplayDetailActivity.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_checkin, null);
                final EditText name = (EditText) view.findViewById(R.id.nameCheckout);

                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!name.getText().toString().isEmpty()) {
                            Date date = Calendar.getInstance().getTime();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
                            final String strDate = dateFormat.format(date);
                            Log.d("Date", "onClick: " + strDate);
                            book.setLastCheckedOutBy(name.getText().toString());
                            book.setLastCheckedOut(strDate);
                            updateCheckout(book);
                            dialog.cancel();
                        } else {
                            // creates alert if user tries submitting blank name
                            AlertDialog.Builder builder = new AlertDialog.Builder(DisplayDetailActivity.this);
                            builder.setTitle("ERROR");
                            builder.setMessage("Please enter name to complete checkout.")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            // if yes, close alert, delete all books
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert = builder.create();
                            alert.show();

                        }
                    }
                });

                builder.setView(view);
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        // when update is clicked, bring user to activity to update book info
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DisplayDetailActivity.this, UpdateActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();

            }
        });
    }

    // updates checkout by taking a book as the body and using it's id to update
    public void updateCheckout(BookDetails bookDetails) {
        retrofitInstance = new RetrofitInstance();
        LibraryInterface libraryInterface = retrofitInstance.getInterface();
        Call<BookDetails> call = libraryInterface.updateBook(book.getBookId(), bookDetails);
        call.enqueue(new Callback<BookDetails>() {
            @Override
            public void onResponse(Call<BookDetails> call, Response<BookDetails> response) {
                Toast.makeText(DisplayDetailActivity.this, "Checkout successful.", Toast.LENGTH_SHORT).show();
                recreate();
            }

            @Override
            public void onFailure(Call<BookDetails> call, Throwable t) {
                Toast.makeText(DisplayDetailActivity.this, "Checkout unsuccessful.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // adds menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        return super.onCreateOptionsMenu(menu);
    }


    // sends the book details using an intent
    private void setShareActionIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);

    }

    // tells you which item from the toolbar is clicked on
    // each item has a specific id
    // each item has a specific action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, BrowseBooksActivity.class));
            finish();
            return true;
        }

        if (item.getItemId() == R.id.share) {
            setShareActionIntent("Checkout out the book " + title.getText().toString() + ". " +
                    "It was written by " + author.getText().toString() + " and published by " + book.getPublisher() + ".");
            Log.d("Share Option", "shared");
        }

        if (item.getItemId() == R.id.deleteBook) {
            // creates alert
            AlertDialog.Builder builder = new AlertDialog.Builder(DisplayDetailActivity.this);
            builder.setTitle("Warning");
            builder.setMessage("Are you sure you would like to delete this book from the current library?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // if yes, close alert, delete all books
                            dialog.cancel();
                            LibraryInterface libraryInterface = retrofitInstance.getInterface();
                            Call<Void> call = libraryInterface.deleteBook(id);
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, final Response<Void> response) {

                                    Toast.makeText(getApplicationContext(), "Book successfully deleted.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(DisplayDetailActivity.this, BrowseBooksActivity.class));
                                    finish();

                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                    Toast.makeText(DisplayDetailActivity.this, "ERROR: Book could not be deleted.", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // if no, just close alert and resume activity
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        }


        return super.onOptionsItemSelected(item);
    }

    // uses the book's id to bring all book details to the screen
    public void recallBook(int id) {

        retrofitInstance = new RetrofitInstance();
        LibraryInterface libraryInterface = retrofitInstance.getInterface();
        Call<BookDetails> call = libraryInterface.getBookById(id);

        // returns responses based on the interfaces call
        call.enqueue(new Callback<BookDetails>() {
            @Override
            public void onResponse(Call<BookDetails> call, Response<BookDetails> response) {

                book = response.body();

                title.setText(book.getBookTitle());
                author.setText(book.getAuthor());
                publisher.setText("Publisher: " + book.getPublisher());
                categories.setText("Categories: " + book.getCategories());
                lastChecked.setText("Last Checked Out: ");

                if (book.getLastCheckedOutBy() == null){
                    lastCheckedBy.setText("");

                } else {
                    lastCheckedBy.setText(book.getLastCheckedOutBy() + " @ " + book.getLastCheckedOut());
                    Log.d("time", "onResponse: " + book.getLastCheckedOut());
                }

            }

            @Override
            public void onFailure(Call<BookDetails> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
