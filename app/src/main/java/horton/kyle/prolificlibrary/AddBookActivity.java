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
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Prolific Library Application
 * Author: Kyle Horton
 * 5/19/2018
 * <p>
 * This class allows a user to add a book to the library. It uses Retrofit to add books to the API.
 */

public class AddBookActivity extends AppCompatActivity {
    private EditText bookTitle, author, publisher, categories;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        bookTitle = (EditText) findViewById(R.id.book_title);
        author = (EditText) findViewById(R.id.book_author);
        publisher = (EditText) findViewById(R.id.book_publisher);
        categories = (EditText) findViewById(R.id.book_categories);
        submit = (Button) findViewById(R.id.submit);

        //adds toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detail");
        setSupportActionBar(toolbar);

        // creates back navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // when clicked
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bookTitle.getText().toString().equals("") ||
                        author.getText().toString().equals("") ||
                        publisher.getText().toString().equals("") ||
                        categories.getText().toString().equals("")) {

                    Log.d("ERROR", "all empty ");

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddBookActivity.this);
                    builder.setMessage("Please fill in all required information before submitting a book!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.setTitle("ERROR!");
                    alert.show();

                } else {

                    // creates new instance
                    BookDetails bookDetails = new BookDetails(author.getText().toString(),
                            categories.getText().toString(),
                            null,
                            null,
                            publisher.getText().toString(),
                            bookTitle.getText().toString());

                    // adds instance to the API
                    addBookToLibrary(bookDetails);
                }

            }
        });
    }

    // adds a book to the API
    private void addBookToLibrary(BookDetails details) {

        //retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://prolific-interview.herokuapp.com/5afda8ff9d343a0009d21dad/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        LibraryInterface library = retrofit.create(LibraryInterface.class);
        Call<BookDetails> call = library.addBook(details);

        call.enqueue(new Callback<BookDetails>() {
            @Override
            public void onResponse(Call<BookDetails> call, Response<BookDetails> response) {
                Toast.makeText(AddBookActivity.this, "Book successfully added to library!.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BookDetails> call, Throwable t) {
                Toast.makeText(AddBookActivity.this, "ERROR: Failed to add book.", Toast.LENGTH_SHORT).show();

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
            if (!bookTitle.getText().toString().equals("") ||
                    !author.getText().toString().equals("") ||
                    !publisher.getText().toString().equals("") ||
                    !categories.getText().toString().equals("")) {

                Log.d("back button", "not empty ");

                // creates alert
                AlertDialog.Builder builder = new AlertDialog.Builder(AddBookActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("Are you sure you would like to leave the screen? All unsaved changes will be lost.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // if yes, close alert, bring user back to library
                                dialog.cancel();
                                startActivity(new Intent(AddBookActivity.this, BrowseBooksActivity.class));
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
