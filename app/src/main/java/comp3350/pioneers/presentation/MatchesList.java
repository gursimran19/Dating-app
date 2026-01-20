package comp3350.pioneers.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import comp3350.pioneers.R;
import comp3350.pioneers.business.MatchManager;
import comp3350.pioneers.business.Services;
import comp3350.pioneers.business.UserManager;
import comp3350.pioneers.objects.Match;
import comp3350.pioneers.objects.User;


public class MatchesList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView emptyMatchesText;
    private MatchAdapter adapter;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_matches_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //initialize database and user manager

        MatchManager matchManager = new MatchManager();
        userManager = new UserManager(matchManager);
        Log.d("DEBUG", "UserManager initialized!");

        List<Match> matches = Services.getUserDSO().getMatches();
        for (Match match : matches) {
            Log.d("DEBUG", "Found match: " + match);
        }




        //initialize UI components
        recyclerView = findViewById(R.id.matchesRecyclerView);
        emptyMatchesText = findViewById(R.id.emptyMatchesText);
        Button backButton = findViewById(R.id.backButton);

        //set up RecyclerView for the list of maches
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //get the current user's matches
        List<String> matchNames = getMatchedUsers();

        //show empty state or matches list
        if (matchNames.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyMatchesText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyMatchesText.setVisibility(View.GONE);

            //set up adapter and attach to RecyclerView
            adapter = new MatchAdapter(matchNames);
            recyclerView.setAdapter(adapter);
        }

        //set up back button
        backButton.setOnClickListener(v -> {
            finish();
        });
    }

    // Gets a list of users, this is just for testing
    private List<String> getMatchedUsers() {
        List<String> matches = new ArrayList<>();

        User currUser = Services.getUserDSO();
        if (currUser == null) {
            Log.e("MatchesList", "User not logged in");
            return matches;
        }

        List<Match> matchList = currUser.getMatches();
        Log.d("MatchesList", "Matches for user: " + matchList.size());

        if (matchList == null || matchList.isEmpty()) {
            Log.d("MatchesList", "No matches stored in User object");
            return matches;
        }

        for (Match m : matchList) {
            matches.add(m.getUserName());
            Log.d("MatchesList", "Showing match: " + m.getUserName());
        }

        return matches;
    }

    //t his is for recyclerView, to display matches

    private class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

        private List<String> matchNames;

        public MatchAdapter(List<String> matchNames) {
            this.matchNames = matchNames;
        }

        @Override
        public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.match_item, parent, false);
            return new MatchViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MatchViewHolder holder, int position) {
            String matchName = matchNames.get(position);
            holder.matchNameTextView.setText(matchName);

            //set click listener for the view profile button
            holder.viewProfileButton.setOnClickListener(v -> {
                Intent intent = new Intent(MatchesList.this, ViewMatch.class);
                intent.putExtra("username", matchName);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return matchNames.size();
        }

        class MatchViewHolder extends RecyclerView.ViewHolder {
            TextView matchNameTextView;
            Button viewProfileButton;

            MatchViewHolder(View itemView) {
                super(itemView);
                matchNameTextView = itemView.findViewById(R.id.matchNameTextView);
                viewProfileButton = itemView.findViewById(R.id.viewProfileButton);
            }
        }
    }
}