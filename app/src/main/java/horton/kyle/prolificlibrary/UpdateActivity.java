package horton.kyle.prolificlibrary;

import android.content.DialogInterface;
import android.content.Intent;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Prolific Library Application
 * Author: Kyle Horton
 * 5/22/2018
 *
 * This class is responsible for updating a book's information.
 */
public class UpdateActivity extends AppCompatActivity {

    private EditText title, author, publisher, categories;
    private TextView leftText, leftAuthor, leftPublisher, leftCategories;
    private Button update;
    private int id;
    private RetrofitInstance retrofitInstance;
    private BookDetails book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        title = (EditText) findViewById(R.id.update_title);
        author = (EditText) findViewById(R.id.update_author);
        publisher = (EditText) findViewById(R.id.update_publisher);
        categories = (EditText) findViewById(R.id.update_categories);
        update = (Button) findViewById(R.id.updateDone);
        id = getIntent().getIntExtra("id", -1);
        book = new BookDetails();

        //adds toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detail");
        setSupportActionBar(toolbar);

        // creates back navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (id == -1){
            Toast.makeText(UpdateActivity.this, "ERROR: Book cannot be updated", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UpdateActivity.this, DisplayDetailActivity.class));
            finish();
        } else {
            recallBook(id); // retrieve books information
        }

        // set book's new information
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book.setId(id);
                book.setTitle(title.getText().toString());
                book.setAuthor(author.getText().toString());
                book.setPublisher(publisher.getText().toString());
                book.setCategories(categories.getText().toString());
                updateBook(book);
            }
        });
    }

    // send book object to database for updating
    public void updateBook(BookDetails bookDetails){
        retrofitInstance = new RetrofitInstance();
        LibraryInterface libraryInterface = retrofitInstance.getInterface();
        Call<BookDetails> call = libraryInterface.updateBook(book.getBookId(), bookDetails);
        call.enqueue(new Callback<BookDetails>() {
            @Override
            public void onResponse(Call<BookDetails> call, Response<BookDetails> response) {
                Toast.makeText(UpdateActivity.this, "Update successful.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateActivity.this, BrowseBooksActivity.class));
            }

            @Override
            public void onFailure(Call<BookDetails> call, Throwable t) {
                Toast.makeText(UpdateActivity.this, "Update unsuccessful.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    // method that retrieves the book's info based on its id
    public void recallBook(int bookID){

        retrofitInstance = new RetrofitInstance();
        LibraryInterface libraryInterface = retrofitInstance.getInterface();
        Call<BookDetails> call = libraryInterface.getBookById(id);

        // returns responses based on the interfaces call
        call.enqueue(new Callback<BookDetails>() {
            @Override
            public void onResponse(Call<BookDetails> call, Response<BookDetails> response) {

                BookDetails bookDetails = response.body();

                title.setText(bookDetails.getBookTitle());
                author.setText(bookDetails.getAuthor());
                publisher.setText(bookDetails.getPublisher());
                categories.setText(bookDetails.getCategories());
            }

            @Override
            public void onFailure(Call<BookDetails> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    // adds menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_book, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // tells you which item from the toolbar is clicked on
    // each item has a specific id
    // each item has a specific action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home || item.getItemId() == R.id.add_done) {

            // conditional to check text fields
            // if they are not all empty
            if (!title.getText().toString().equals("") ||
                    !author.getText().toString().equals("") ||
                    !publisher.getText().toString().equals("") ||
                    !categories.getText().toString().equals("")) {

                // creates alert
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("Are you sure you would like to leave the screen? All unsaved changes will be lost.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // if yes, close alert, bring user back to library
                                dialog.cancel();
                                startActivity(new Intent(UpdateActivity.this, BrowseBooksActivity.class));
                                finish();
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

            } else {
                // if all text fields are empty
                startActivity(new Intent(this, BrowseBooksActivity.class));
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
