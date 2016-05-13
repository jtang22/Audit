package edu.calpoly.mjzhao.audit;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import com.plattysoft.leonids.ParticleSystem;
import com.squareup.picasso.Picasso;

public class Startup extends AppCompatActivity {

    protected Button m_SigninButton;
    protected Button m_SignupButton;
    protected Button m_GuestButton;

    protected Menu m_vwMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_startup);
        initButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.mainmenu, menu);
        m_vwMenu = menu;
        return true;
    }

    void initButtons () {
        m_SigninButton = (Button) findViewById(R.id.signin);
        m_SigninButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Startup.this, Signin.class);
                startActivity(intent);
            }
        });

        m_SignupButton = (Button) findViewById(R.id.signup);
        m_SignupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Startup.this, Signup.class);
                startActivity(intent);
            }
        });

        m_GuestButton = (Button) findViewById(R.id.guest);
        m_GuestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Startup.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        Button koala = (Button) findViewById(R.id.koala_mode);
        koala.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                koalaModeActivate();
            }
        });
    }

    private void koalaModeActivate () {
        new ParticleSystem(this, 80, R.drawable.koala, 10000)
                .setSpeedModuleAndAngleRange(0f, 0.3f, 45, 0)
                .setRotationSpeed(256)
                .setAcceleration(0.00005f, 90)
                .emit(findViewById(R.id.emitter_top_left), 8);
        new ParticleSystem(this, 80, R.drawable.koala, 10000)
                .setSpeedModuleAndAngleRange(0f, 0.3f, 135, 180)
                .setRotationSpeed(256)
                .setAcceleration(0.00005f, 90)
                .emit(findViewById(R.id.emitter_top_right), 8);
    }
}
