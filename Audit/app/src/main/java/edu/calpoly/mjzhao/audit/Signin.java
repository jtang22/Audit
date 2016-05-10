package edu.calpoly.mjzhao.audit;

import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseUser;

public class
Signin extends AppCompatActivity {

    protected Button m_SigninButton;
    protected Button m_CancelButton;

    protected EditText m_usernameField;
    protected EditText m_passwordField;

    protected Menu m_vwMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initEditText();
        initButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.mainmenu, menu);
        m_vwMenu = menu;
        return true;
    }

    void initButtons() {
        m_CancelButton = (Button) findViewById(R.id.cancel);
        m_CancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        m_SigninButton = (Button) findViewById(R.id.signin);
        m_SigninButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ParseUser.logInInBackground(m_usernameField.getText().toString(),
                        m_passwordField.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, com.parse.ParseException e) {
                        if (user != null) {
                            Toast.makeText(Signin.this, "Login successful!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Signin.this, HomeScreen.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Signin.this, "Login failed!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    void initEditText () {
        m_usernameField = (EditText) findViewById(R.id.usernameField);
        m_usernameField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.equals(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
                        || keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(m_usernameField.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        m_passwordField = (EditText) findViewById(R.id.passwordField);
        m_passwordField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.equals(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
                        || keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(m_usernameField.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }
}
