package horton.kyle.prolificlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Prolific Library Application
 * Author: Kyle Horton
 * 5/19/2018
 *
 * This class displays the home screen.
 */
public class HomeScreenActivity extends AppCompatActivity {

    private Button browse, credits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        browse = (Button) findViewById(R.id.browse);
        credits = (Button) findViewById(R.id.credits);

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, BrowseBooksActivity.class));
                finish();
            }
        });

        credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, CreditsActivity.class));
                finish();
            }
        });
    }
}
