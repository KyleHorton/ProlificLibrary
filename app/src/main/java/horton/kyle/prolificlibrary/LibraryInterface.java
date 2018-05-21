package horton.kyle.prolificlibrary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Kyleh on 5/20/2018.
 */

public interface LibraryInterface {

    @GET("books")
    Call<List<BookDetails>> getBooks();

    @POST("books")
    Call<BookDetails> addBook(@Body BookDetails bookDetails);


}
