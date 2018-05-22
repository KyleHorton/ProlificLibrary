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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Prolific Library Application
 * Author: Kyle Horton
 * 5/19/2018
 *
 * This class displays the books currently in the library. It uses Retrofit to retrieve data from the API.
 */
public class BrowseBooksActivity extends AppCompatActivity {

    private ListView booksList;
    private Button deleteAll;
    private RetrofitInstance retrofitInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_books);

        booksList = (ListView) findViewById(R.id.list_books);

        //adds toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Library");
        setSupportActionBar(toolbar);

        // creates back navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        retrofitInstance = new RetrofitInstance();
        LibraryInterface libraryInterface = retrofitInstance.getInterface();
        Call<List<BookDetails>> call = libraryInterface.getBooks();

        // returns responses based on the interfaces call
        call.enqueue(new Callback<List<BookDetails>>() {
            @Override
            public void onResponse(Call<List<BookDetails>> call, Response<List<BookDetails>> response) {
                List<BookDetails> bookDetails = response.body();
                booksList.setAdapter(new LibraryAdapter(BrowseBooksActivity.this, bookDetails));

            }

            @Override
            public void onFailure(Call<List<BookDetails>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    // adds menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_browse, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // tells you which item from the toolbar is clicked on
    // each item has a specific id
    // each item has a specific action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, HomeScreenActivity.class));
            finish();
            return true;
        }

        if (item.getItemId() == R.id.add_book) {
            startActivity(new Intent(this, AddBookActivity.class));
            finish();

        }

        if (item.getItemId() == R.id.deleteAll){

            // creates alert
            AlertDialog.Builder builder = new AlertDialog.Builder(BrowseBooksActivity.this);
            builder.setTitle("Warning");
            builder.setMessage("Are you sure you would like to delete all books in the current library?.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // if yes, close alert, delete all books
                            dialog.cancel();
                            LibraryInterface libraryInterface = retrofitInstance.getInterface();
                            Call<Void> call = libraryInterface.deleteAll();
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, final Response<Void> response) {
                                    Toast.makeText(BrowseBooksActivity.this, "Books successfully deleted.", Toast.LENGTH_SHORT).show();
                                    recreate();

                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                    Toast.makeText(BrowseBooksActivity.this, "ERROR: Books could not be deleted.", Toast.LENGTH_SHORT).show();

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
}
