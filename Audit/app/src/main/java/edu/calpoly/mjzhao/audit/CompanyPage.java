package edu.calpoly.mjzhao.audit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by Myron on 5/10/2016.
 */
public class CompanyPage extends AppCompatActivity{

    protected ArrayList<ParseObject> m_reviewList;
    protected ReviewListAdapter m_reviewAdapter;
    protected ListView m_vwReviewLayout;
    protected ParseObject m_review;
    protected String m_company;
    protected ArrayList<String> m_userPicList;

    protected Semaphore semaphore = new Semaphore(1);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle getBundle = getIntent().getExtras();
        m_company = getBundle.getString("company");

        m_reviewList = new ArrayList<>();
        m_userPicList = new ArrayList<>();

        m_reviewAdapter = new ReviewListAdapter(this, m_reviewList, m_userPicList);
        initReviewList();
        initLayout();
        m_reviewAdapter.notifyDataSetChanged();
    }

    protected void initLayout() {
        setContentView(R.layout.company_view);
        m_vwReviewLayout = (ListView) findViewById(R.id.reviewListViewGroup);
        m_vwReviewLayout.setAdapter(m_reviewAdapter);
    }

    protected void initReviewList () {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Review");
        query.whereEqualTo("companyId", m_company);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> reviewList, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < reviewList.size(); i++) {
                        Log.i("REVIEW", reviewList.get(i).getString("comment"));
                        m_reviewList.add(reviewList.get(i));
                        initUserPicList(m_reviewList.get(i).get("userId").toString());
                        m_reviewAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("Companies", "Error: " + e.getMessage());
                }
            }
        });
    }

    protected void initUserPicList (String username) {
        final String m_username = username;

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, ParseException e) {
            if (e == null) {
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getUsername().equals(m_username)) {
                        Log.i("USERS", userList.get(i).getParseFile("picture").getUrl());
                        m_userPicList.add(userList.get(i).getParseFile("picture").getUrl());
                        m_reviewAdapter.notifyDataSetChanged();
                    }
                }
            } else {
                Log.e("Companies", "Error: " + e.getMessage());
            }
            }
        });

    }
}
