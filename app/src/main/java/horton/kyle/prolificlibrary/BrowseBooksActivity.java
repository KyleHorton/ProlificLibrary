package horton.kyle.prolificlibrary;

import android.content.Intent;
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
    private String bookid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_books);

        booksList = (ListView) findViewById(R.id.list_books);

        //adds toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Books");
        setSupportActionBar(toolbar);

        // creates back navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // creates retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://prolific-interview.herokuapp.com/5afda8ff9d343a0009d21dad/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        LibraryInterface library = retrofit.create(LibraryInterface.class);
        Call<List<BookDetails>> call = library.getBooks();

        // returns responses based on the interfaces call
        call.enqueue(new Callback<List<BookDetails>>() {
            @Override
            public void onResponse(Call<List<BookDetails>> call, Response<List<BookDetails>> response) {
                List<BookDetails> bookDetails = response.body();

                for(BookDetails b: bookDetails){
                    Log.d("title", b.getBookTitle());
                    Log.d("author", b.getAuthor());
                }

                String[] book = new String[bookDetails.size()];

                for (int i = 0; i < bookDetails.size(); i++){
                    book[i] = "Title: " + bookDetails.get(i).getBookTitle() + "\n" + "Author(s): " + bookDetails.get(i).getAuthor();

                }


                booksList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, book));

            }

            @Override
            public void onFailure(Call<List<BookDetails>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        booksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                

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

        return super.onOptionsItemSelected(item);
    }
}
