package edu.calpoly.mjzhao.audit;

import android.content.Intent;
import android.net.ParseException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.Parse;
import com.parse.SignUpCallback;

public class Signup extends AppCompatActivity {

    protected Button m_CancelButton;
    protected Button m_Signup;

    protected EditText usernameLabel;
    protected EditText passwordLabel;
    protected EditText confirmPassLabel;
    protected EditText firstNameLabel;
    protected EditText lastNameLabel;
    protected EditText emailLabel;

    protected Menu m_vwMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        getLabels();
        initButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.mainmenu, menu);
        m_vwMenu = menu;
        return true;
    }

    void getLabels () {
        usernameLabel = (EditText)findViewById(R.id.signupUsername);
        passwordLabel = (EditText)findViewById(R.id.signupPassword);
        confirmPassLabel = (EditText)findViewById(R.id.reenterPassword);
        firstNameLabel = (EditText)findViewById(R.id.firstName);
        lastNameLabel = (EditText)findViewById(R.id.lastName);
        emailLabel = (EditText)findViewById(R.id.email);
    }

    void initButtons() {
        m_Signup = (Button) findViewById(R.id.submit);
        m_Signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                signUp();
                usernameLabel.getText().clear();
                passwordLabel.getText().clear();
                confirmPassLabel.getText().clear();
                firstNameLabel.getText().clear();
                lastNameLabel.getText().clear();
                emailLabel.getText().clear();
            }
        });

        m_CancelButton = (Button) findViewById(R.id.cancel2);
        m_CancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void signUp() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();

        String username = usernameLabel.getText().toString().trim();
        String password = passwordLabel.getText().toString().trim();
        String firstName = firstNameLabel.getText().toString().trim();
        String lastName = lastNameLabel.getText().toString().trim();
        String email = emailLabel.getText().toString().trim();
        String confirm = confirmPassLabel.getText().toString().trim();

        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder("Error");
        if (username.length() == 0) {
            validationError = true;
            validationErrorMessage.append("Error: Blank Username");
        }
        if (password.length() == 0) {
            if (validationError) {
                validationErrorMessage.append("Error");
            }
            validationError = true;
            validationErrorMessage.append("Error: Blank Password");
        } else if (!password.equals(confirm)) {
            if (validationError) {
                validationErrorMessage.append("Error");
            }
            validationError = true;
            validationErrorMessage.append("Error: Passwords do not match");
        }
        validationErrorMessage.append("ERROR");
        if (validationError) {
            Toast.makeText(this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
            return;
        }

        // Set up a new Parse user
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.put("firstname", firstName);
        user.put("lastname", lastName);

        // Call the Parse signup method
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e != null) {
                    // Show the error message
                    Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_LONG).show();

                } else {
                    // Start an intent for the dispatch activity
                    Toast.makeText(Signup.this, "New account created!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Signup.this, HomeScreen.class);
                    startActivity(intent);
                }
            }
        });
    }
}
