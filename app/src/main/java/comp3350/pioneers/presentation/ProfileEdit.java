/**
 *
 * This file handles the profile editing functionality.
 * user can add or update the bio,interests and match type(romance or friendship)
 * This class interacts with the logic layer to get the details from the database and update them in the gui.
 *
 */
package comp3350.pioneers.presentation;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import comp3350.pioneers.business.MatchManager;
import comp3350.pioneers.business.Services;
import comp3350.pioneers.business.UserManager;
import comp3350.pioneers.objects.User;


import comp3350.pioneers.R;

public class ProfileEdit extends AppCompatActivity {
    /*GUI elements */
    private EditText bioEditText;
    private Spinner interest1Spinner, interest2Spinner, interest3Spinner, interest4Spinner;
    private CheckBox romanceCheckbox, friendshipCheckbox;
    private UserManager userManager;
    //interest options for spinners
    private String[] interestOptions;
    private String updateSuccessMsg;
    private String updateErrorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_edit);
        getInterestsFromAssets();
        getMessagesFromAssets();

        /*Initializing UserManager*/

        MatchManager matchManager = new MatchManager();
        userManager = new UserManager(matchManager);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //find spinner elements
        bioEditText = findViewById(R.id.bioEditText);
        interest1Spinner = findViewById(R.id.interest1Spinner);
        interest2Spinner = findViewById(R.id.interest2Spinner);
        interest3Spinner = findViewById(R.id.interest3Spinner);
        interest4Spinner = findViewById(R.id.interest4Spinner);
        romanceCheckbox = findViewById(R.id.romanceCheckbox);
        friendshipCheckbox = findViewById(R.id.friendshipCheckbox);
        Button saveButton = findViewById(R.id.saveButton);
        Button goBackButton = findViewById(R.id.goBackButton);
        //set up spinners with interest options
        setupSpinners();
        /*Get user Info*/


        String username = Services.getUserDSO().getUsername();
        User currentUser = Services.getUserDSO();

        if (currentUser != null) {
            // Auto Fill previous bio
            String userBioData = Services.getUserDSO().getUserBio();
            String[] userInterests = Services.getUserDSO().getUserInterests();



            bioEditText.setText(userBioData);
            Log.d("UserProfile", "Bio: " + userBioData);
            //set previously selected interests in spinners
            interest1Spinner.setSelection(getSpinnerIndex(interest1Spinner, userInterests[0]));
            interest2Spinner.setSelection(getSpinnerIndex(interest2Spinner, userInterests[1]));
            interest3Spinner.setSelection(getSpinnerIndex(interest3Spinner, userInterests[2]));
            interest4Spinner.setSelection(getSpinnerIndex(interest4Spinner, userInterests[3]));
            romanceCheckbox.setChecked(Services.getUserDSO().getUserMatchType());
            friendshipCheckbox.setChecked(Services.getUserDSO().getUserMatchType());


        }
        /*Save button*/
        saveButton.setOnClickListener(v -> {
            if (currentUser != null) {
                String bio = bioEditText.getText().toString();
                String interest1 = interest1Spinner.getSelectedItem().toString();
                String interest2 = interest2Spinner.getSelectedItem().toString();
                String interest3 = interest3Spinner.getSelectedItem().toString();
                String interest4 = interest4Spinner.getSelectedItem().toString();
                boolean lookingForRomance = romanceCheckbox.isChecked();
                boolean lookingForFriendship = friendshipCheckbox.isChecked();

                userManager.updateUserBio(username, bio, interest1, interest2, interest3, interest4);
                userManager.updateUserMatchingType(username, lookingForRomance);



                Toast.makeText(ProfileEdit.this, updateSuccessMsg, Toast.LENGTH_SHORT).show();



            } else {
                Toast.makeText(ProfileEdit.this, updateErrorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        /*Go back Button*/
        goBackButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileEdit.this, MainPage.class);
            startActivity(intent);
            finish();
        });
    }

    /*Method to set spinner with each interest option*/
    private void setupSpinners() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, interestOptions);

        interest1Spinner.setAdapter(adapter);
        interest2Spinner.setAdapter(adapter);
        interest3Spinner.setAdapter(adapter);
        interest4Spinner.setAdapter(adapter);
    }
    private int getSpinnerIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }


    private void getInterestsFromAssets(){

        List<String> lines = new ArrayList<>();
        AssetManager assetManager = getAssets();
        String line;
        try{
            InputStream input = assetManager.open("textAssets/ProfileEditText.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            while((line = reader.readLine()) != null){
                lines.add(line);
            }
            interestOptions = new String[lines.size()];

            for( int i = 0 ; i < lines.size() ; i++ ){
                interestOptions[i] = lines.get(i);
            }


        } catch (IOException e){
            System.out.println("Error populating interests in" + e.getMessage());
        }

    }

    private void getMessagesFromAssets(){
        AssetManager assetManager = getAssets();

        try{
            InputStream input = assetManager.open("textAssets/ProfileEditToastMsgs.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            updateSuccessMsg = reader.readLine();
            updateErrorMsg = reader.readLine();

        } catch (IOException e){
            System.out.println("Error populating messages in profile edit" + e.getMessage());
        }

    }
}