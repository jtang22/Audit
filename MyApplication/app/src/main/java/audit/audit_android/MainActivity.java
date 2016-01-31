package audit.audit_android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Button;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.Parse;
import com.parse.SignUpCallback;
import com.parse.ParseException;


public class MainActivity extends AppCompatActivity {
    public EditText usernameLabel;
    public EditText passwordLabel;
    public EditText firstNameLabel;
    public EditText lastNameLabel;
    public EditText emailLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [Optional] Power your app with Local Datastore. For more info, go to
        // https://parse.com/docs/android/guide#local-datastore


        usernameLabel = (EditText)findViewById(R.id.username);
        passwordLabel = (EditText)findViewById(R.id.userPassword);
        firstNameLabel = (EditText)findViewById(R.id.userFirstName);
        lastNameLabel = (EditText)findViewById(R.id.userLastName);
        emailLabel = (EditText)findViewById(R.id.userEmail);


        final Button button = (Button) findViewById(R.id.submitButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signUp();
                usernameLabel.getText().clear();
                passwordLabel.getText().clear();
                firstNameLabel.getText().clear();
                lastNameLabel.getText().clear();
                emailLabel.getText().clear();
            }
        });

        Button displayDataButton = (Button) findViewById(R.id.displayDataButton);
        displayDataButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DisplayData.class);
                startActivity(intent);
            }
        });

    }

    private void signUp() {
        Parse.enableLocalDatastore(this);

        Parse.initialize(this);

        String username = usernameLabel.getText().toString().trim();
        String password = passwordLabel.getText().toString().trim();
        String firstName = firstNameLabel.getText().toString().trim();
        String lastName = lastNameLabel.getText().toString().trim();
        String email = emailLabel.getText().toString().trim();

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
        }
        validationErrorMessage.append("ERROR");
        if (validationError) {
            Toast.makeText(this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Set up a new Parse user
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.put("firstName", firstName);
        user.put("lastName", lastName);

        // Call the Parse signup method
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    // Show the error message
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                } else {
                    // Start an intent for the dispatch activity
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
