package comp3350.pioneers.presentation;

//import static android.os.Build.VERSION_CODES.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import comp3350.pioneers.R;
import comp3350.pioneers.business.MatchManager;
import comp3350.pioneers.business.Services;
import comp3350.pioneers.business.UserManager;
import comp3350.pioneers.objects.Match;


public class ChatPage extends AppCompatActivity {

    private TextView chatTitleTextView;
    private TextView chatMessagesTextView;
    private EditText messageEditText;
    private Button sendButton;
    private Button backButton;


    private Button reportUser;
    private UserManager userManager;
    private MatchManager matchManager;
    private Match currentMatch;
    private String currentUsername;
    private String matchUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //initialize UI components
        chatTitleTextView = findViewById(R.id.chatTitleTextView);
        chatMessagesTextView = findViewById(R.id.chatMessagesTextView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        backButton = findViewById(R.id.backButton);
        reportUser = findViewById(R.id.reportUser);

        //make the chat messages scrollable
        chatMessagesTextView.setMovementMethod(new ScrollingMovementMethod());

        //initialize Managers

        matchManager = new MatchManager();
        userManager = new UserManager(matchManager);

        //get the current user
        currentUsername = Services.getUserDSO().getUsername();

        //get the match username from the intent
        matchUsername = getIntent().getStringExtra("matchUsername");

        if (matchUsername == null || matchUsername.isEmpty()) {
            Toast.makeText(this, "Error: No match information provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //set the chat title
        chatTitleTextView.setText("Chat with " + matchUsername);

        //try to find the match in the user's matches
        currentMatch = matchManager.getMatchInfo(currentUsername, matchUsername);


        //if not found, create a new Match object
        if (currentMatch == null) {
            currentMatch = new Match(matchUsername);
        }

        //load existing chat messages
        loadChatMessages();

        //set up send button click listener
        sendButton.setOnClickListener(v -> sendMessage());

        //set up back button click listener
        backButton.setOnClickListener(v -> finish());

        reportUser.setOnClickListener(v -> {
            boolean reported = matchManager.reportMatch(currentUsername, matchUsername);
            if (reported) {
                // update memory to reload the match list again
                Services.getUserDSO().setMatches(matchManager.getUserMatches(currentUsername));

                Toast.makeText(ChatPage.this, "User reported and removed from matches.", Toast.LENGTH_SHORT).show();
            } else {
                // update memory to reload the match list again
                Services.getUserDSO().setMatches(matchManager.getUserMatches(currentUsername));

                Toast.makeText(ChatPage.this, "Match Removed it was one-sided.", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(ChatPage.this ,MainPage.class );
            startActivity(intent);
            finish();
        });

    }

    //find a match by username in the user's matches

    private Match findMatchByUsername(String username) {
        for (Match match : Services.getUserDSO().getMatches()) {
            if (match.getUserName().equals(username)) {
                return match;
            }
        }
        return null;
    }

    //loads existing chat messages


    // using bussiness layer instead
   private void loadChatMessages() {
       // Use MatchManager to get chat from logic layer
       String chatHistory = matchManager.getMatchChat(currentMatch);


       if (chatHistory == null || chatHistory.isEmpty()) {
           chatMessagesTextView.setText("No messages yet. Start the conversation!");
       } else {
           chatMessagesTextView.setText(chatHistory);
       }

       // Debug log
       android.util.Log.d("ChatPage", "Loaded chat: " + chatHistory);
   }


    //sends a new message
    private void sendMessage() {
        String message = messageEditText.getText().toString().trim();

        if (message.isEmpty()) {
            return;
        }

        boolean success = matchManager.addChatMessage(currentMatch, currentUsername, message);
        if(success){
            chatMessagesTextView.setText(matchManager.getMatchChat(currentMatch));
            System.out.println("Message updated in the box " + currentUsername + " to " + currentMatch);
        //clear the message input
             messageEditText.setText("");

            //scroll to the bottom
            final int scrollAmount = chatMessagesTextView.getLayout().getLineTop(chatMessagesTextView.getLineCount()) - chatMessagesTextView.getHeight();
            if (scrollAmount > 0) {
                chatMessagesTextView.scrollTo(0, scrollAmount);
            } else {
                chatMessagesTextView.scrollTo(0, 0);
            }
        }
   }
}