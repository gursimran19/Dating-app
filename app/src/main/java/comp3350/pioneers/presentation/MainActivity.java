/*
This file is responsible for handling user interactions, such as entering a username and password,
clicking login or sign-up buttons, and displaying messages based on success or failure.

Author : Abhijeet Singh
*/
package comp3350.pioneers.presentation;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import comp3350.pioneers.R;
import comp3350.pioneers.business.LogicManager;
import comp3350.pioneers.business.MatchManager;
import comp3350.pioneers.business.UserManager;
import comp3350.pioneers.business.Services;


public class MainActivity extends AppCompatActivity {
    private LogicManager logicManager;
    private UserManager userManager;
    private static boolean databaseInitialized = false;

    //Error messages
    private String loginSuccessMsg;
    private String errorLoadingMsg;
    private String invalidCredsMsg;
    private String pleaseEnterMsg;
    private String regSuccessMsg;
    private String alreadyExitsMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Initializes the database and sets a flag so it does not get reinitialized every time User returns to MainActivity
        if(!databaseInitialized){
            Services.initializeDb(getApplicationContext(),"/SC");
            databaseInitialized = true;
            populateMessages();
        } else {
            System.out.println("The database was not re initialized");
        }

        populateMessages();
        setContentView(R.layout.main_activity);


        MatchManager matchManager = new MatchManager();
        logicManager = new LogicManager();
        userManager = new UserManager(matchManager);


        EditText Username = findViewById(R.id.editTextUsername);
        EditText Password = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.loginButton);
        Button signupButton = findViewById(R.id.buttonSignup);

        //login button
        loginButton.setOnClickListener(v -> {
            String username = Username.getText().toString();
            String password = Password.getText().toString();

            if (logicManager.validateUser(username, password)) {
                //build the user object after successful login
                if (userManager.buildUserDSO(username, password)) {
                    Toast.makeText(MainActivity.this, loginSuccessMsg, Toast.LENGTH_SHORT).show();
                    // Navigate to MainPage
                    Intent intent = new Intent(MainActivity.this, MainPage.class);
                    startActivity(intent);
                    finish(); // Close login activity after success
                } else {
                    Toast.makeText(MainActivity.this, errorLoadingMsg, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, invalidCredsMsg, Toast.LENGTH_SHORT).show();
            }
        });

        //sign-up button
        signupButton.setOnClickListener(v -> {
            String username = Username.getText().toString();
            String password = Password.getText().toString();

            //check if username and password are not empty
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, pleaseEnterMsg, Toast.LENGTH_SHORT).show();
                return;
            }

            if (logicManager.registerUser(username, password)) {
                Toast.makeText(MainActivity.this, regSuccessMsg, Toast.LENGTH_SHORT).show();

                // Auto-login after registration
                if (userManager.buildUserDSO(username, password)) {
                    Intent intent = new Intent(MainActivity.this, MainPage.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(MainActivity.this, alreadyExitsMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateMessages(){
        AssetManager assetManager = getAssets();

        try{
            InputStream input = assetManager.open("textAssets/ToastMessages.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            loginSuccessMsg = reader.readLine();

            errorLoadingMsg = reader.readLine();

            invalidCredsMsg = reader.readLine();

            pleaseEnterMsg = reader.readLine();

            regSuccessMsg = reader.readLine();

            alreadyExitsMsg = reader.readLine();

        } catch (IOException e){
            System.out.println("Error populating messages in Main Activity" + e.getMessage());
        }
    }

}