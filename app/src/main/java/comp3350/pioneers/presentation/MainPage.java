/**
 *
 * This class represents the main page GUI.
 * It provides navigation options for users to edit their profile or log out.
 *
 */
package comp3350.pioneers.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import comp3350.pioneers.R;
import comp3350.pioneers.business.Services;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Button editProfileButton = findViewById(R.id.editProfileButton);
        editProfileButton.setOnClickListener(v -> {

            Intent intent = new Intent(MainPage.this, ProfileEdit.class);
            startActivity(intent);
        });

        Button logOutButton = findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(v -> {
            Services.addUserDSO(null);

            Intent intent = new Intent(MainPage.this ,MainActivity.class );
            startActivity(intent);

        });

        Button exploreButton = findViewById(R.id.exploreButton);
        exploreButton.setOnClickListener(v -> {

            Intent intent = new Intent(MainPage.this , MatchPage.class );
            startActivity(intent);
        });

        Button viewMatchesButton = findViewById(R.id.viewMatchesButton);
        viewMatchesButton.setOnClickListener(v -> {

            Intent intent = new Intent(MainPage.this ,MatchesList.class );
            startActivity(intent);
        });





    }
}