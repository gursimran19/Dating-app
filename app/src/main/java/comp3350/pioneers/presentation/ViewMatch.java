package comp3350.pioneers.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import comp3350.pioneers.R;
import comp3350.pioneers.business.Services;
import comp3350.pioneers.objects.Match;
import comp3350.pioneers.objects.User;

public class ViewMatch extends AppCompatActivity {

    private String matchUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_match);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        matchUsername = getIntent().getStringExtra("username");


        //this is for the back button click listener
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish(); //close the current activity and return to the previous one
        });

        // Chat button click listener
        Button chatButton = findViewById(R.id.chatButton);
        chatButton.setOnClickListener(v -> {
            // Create intent to start chat page
            Intent intent = new Intent(ViewMatch.this, ChatPage.class);

            // Pass the match username to chat page
            intent.putExtra("matchUsername", matchUsername);

            // Start the chat activity
            startActivity(intent);
        });

        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView bioTextView = findViewById(R.id.bioTextView);
        TextView interest1TextView = findViewById(R.id.interest1TextView);
        TextView interest2TextView = findViewById(R.id.interest2TextView);
        TextView interest3TextView = findViewById(R.id.interest3TextView);
        TextView interest4TextView = findViewById(R.id.interest4TextView);
        // Retrieve passed match name
        String matchUsername = getIntent().getStringExtra("username");

        // Get user and match info from DSO
        User currentUser = Services.getUserDSO();
        if (currentUser != null && matchUsername != null) {
            Match match = null;
            for (Match m : currentUser.getMatches()) {
                if (matchUsername.equals(m.getUserName())) {
                    match = m;
                    break;
                }
            }

            if (match != null) {
                nameTextView.setText(match.getUserName());
                Log.d("ViewMatch", "Bio for " + match.getUserName() + ": " + match.getBio());

                bioTextView.setText(match.getBio());
                interest1TextView.setText(match.getInterest1());
                interest2TextView.setText(match.getInterest2());
                interest3TextView.setText(match.getInterest3());
                interest4TextView.setText(match.getInterest4());
            } else {
                nameTextView.setText("Match Not Found");
                Log.e("ViewMatch", "Match not found for user: " + matchUsername);
            }
        } else {
            nameTextView.setText("No User Logged In");
            Log.e("ViewMatch", "No user or username passed");
        }
    }
}