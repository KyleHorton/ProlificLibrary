package horton.kyle.prolificlibrary;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Prolific Library Application
 * Author: Kyle Horton
 * 5/22/2018
 *
 * This class creates an instance of the API using retrofit. It is useful as a class since multiple calls will be made.
 */

public class RetrofitInstance {

    private LibraryInterface libraryInterface;

    public LibraryInterface getInterface(){

        if (libraryInterface == null){

            // creates retrofit instance
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("http://prolific-interview.herokuapp.com/5afda8ff9d343a0009d21dad/")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();

            libraryInterface = retrofit.create(LibraryInterface.class);
        }
        return libraryInterface;
    }

}
