package horton.kyle.prolificlibrary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Prolific Library Application
 * Author: Kyle Horton
 * 5/222/2018
 *
 * This is the interface for the library API. It contains all GET, POST, and PUT methods.
 */

public interface LibraryInterface {

    @GET("books")
    Call<List<BookDetails>> getBooks();

    @GET("books/{id}/")
    Call<BookDetails> getBookById(@Path("id") int id);

    @POST("books")
    Call<BookDetails> addBook(@Body BookDetails bookDetails);

    @DELETE("clean/")
    Call<Void> deleteAll();

    @DELETE("books/{id}/")
    Call<Void> deleteBook(@Path("id") int id);

    @PUT("books/{id}/")
    Call<BookDetails> updateBook(@Path("id") int id, @Body BookDetails bookDetails);


}
