package edu.calpoly.mjzhao.audit;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Myron on 5/9/2016.
 */
public class ParseApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);

    }
}
