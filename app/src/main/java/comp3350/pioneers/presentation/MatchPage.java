package comp3350.pioneers.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import comp3350.pioneers.R;
import comp3350.pioneers.business.MatchManager;
import comp3350.pioneers.business.Services;
import comp3350.pioneers.objects.Match;

public class MatchPage extends AppCompatActivity {
    private Match randomMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_match_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView bioTextView = findViewById(R.id.bioTextView);
        TextView interest1TextView = findViewById(R.id.interest1TextView);
        TextView interest2TextView = findViewById(R.id.interest2TextView);
        TextView interest3TextView = findViewById(R.id.interest3TextView);
        TextView interest4TextView = findViewById(R.id.interest4TextView);
        Button yesButton = findViewById(R.id.yesButton);
        Button noButton = findViewById(R.id.noButton);

        MatchManager matchManager = new MatchManager();
        String currentUser = Services.getUserDSO().getUsername();

        loadingNewMatch(matchManager, currentUser, nameTextView, bioTextView,
                interest1TextView, interest2TextView, interest3TextView, interest4TextView, yesButton, noButton);

        Button backButton = findViewById(R.id.backButtonExplore);
        backButton.setOnClickListener(v -> {
            finish();
        });

    }

    private void loadingNewMatch(MatchManager matchManager, String currentUser,
                                 TextView nameTextView, TextView bioTextView, TextView interest1TextView,
                                 TextView interest2TextView, TextView interest3TextView, TextView interest4TextView,
                                 Button yesButton, Button noButton) {

        randomMatch = matchManager.getRandomMatch(currentUser);

        while (randomMatch != null && (matchManager.isMatched(currentUser, randomMatch.getUserName())
                || matchManager.isRejected(currentUser, randomMatch.getUserName()))) {
            randomMatch = matchManager.getRandomMatch(currentUser);
        }

        if (randomMatch != null) {
            nameTextView.setText(randomMatch.getUserName());
            bioTextView.setText(randomMatch.getBio());
            interest1TextView.setText(randomMatch.getInterest1());
            interest2TextView.setText(randomMatch.getInterest2());
            interest3TextView.setText(randomMatch.getInterest3());
            interest4TextView.setText(randomMatch.getInterest4());
            yesButton.setEnabled(true);
            noButton.setEnabled(true);

            yesButton.setOnClickListener(v -> {
                matchManager.createMatch(currentUser, randomMatch.getUserName());
                loadingNewMatch(matchManager, currentUser, nameTextView, bioTextView, interest1TextView,
                        interest2TextView, interest3TextView, interest4TextView, yesButton, noButton);
            });
            //updates user dso after clicking yes
            List<Match> updatedMatches = matchManager.getUserMatches(currentUser);
            Services.getUserDSO().setMatches(updatedMatches);


            noButton.setOnClickListener(v -> {
                matchManager.rejectUser(currentUser, randomMatch.getUserName());
                loadingNewMatch(matchManager, currentUser, nameTextView, bioTextView, interest1TextView,
                        interest2TextView, interest3TextView, interest4TextView, yesButton, noButton);
            });

        } else {
            nameTextView.setText("\n\n\n\nYou have reached the end :) \n Check back later");
            bioTextView.setVisibility(View.GONE);
            interest1TextView.setVisibility(View.GONE);
            interest2TextView.setVisibility(View.GONE);
            interest3TextView.setVisibility(View.GONE);
            interest4TextView.setVisibility(View.GONE);
            yesButton.setEnabled(false);
            noButton.setEnabled(false);

        }
    }
}
