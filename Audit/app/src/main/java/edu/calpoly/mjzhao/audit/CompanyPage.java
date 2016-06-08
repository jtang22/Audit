package edu.calpoly.mjzhao.audit;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

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
    protected View m_header;
    protected String m_company;
    protected ArrayList<String> m_userPicList;
    protected Menu m_vwMenu;

    protected TextView m_companyNameView;
    protected TextView m_companyURLView;
    protected ImageView m_companyImageView;
    protected Button m_writeReviewButton;
    protected RatingBar m_overallRatingView;
    protected RatingBar m_qualityRatingView;
    protected RatingBar m_serviceRatingView;
    protected RatingBar m_shippingRatingView;

    protected int numReviews = 0;
    protected float qualityRating = 0;
    protected float serviceRating = 0;
    protected float shippingRating = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_view);

        Bundle getBundle = getIntent().getExtras();
        m_company = getBundle.getString("company");

        LayoutInflater inflater = (LayoutInflater)this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        m_header = inflater.inflate(R.layout.company_view_header, null, false);

        m_reviewList = new ArrayList<>();
        m_userPicList = new ArrayList<>();

        m_reviewAdapter = new ReviewListAdapter(this, m_reviewList, m_userPicList);

        initAdapter();
        initViews();
        initHeader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (ParseUser.getCurrentUser() != null) {
            MenuInflater menuInflater = new MenuInflater(this);
            menuInflater.inflate(R.menu.mainmenu, menu);
            m_vwMenu = menu;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.viewProfile:
                Bundle bundle = new Bundle();
                bundle.putString("user", ParseUser.getCurrentUser().getUsername().toString());
                intent = new Intent(CompanyPage.this, UserProfile.class);
                intent.putExtras(bundle);
                CompanyPage.this.startActivity(intent);
                finish();
                return true;

            case R.id.logout:
                if (ParseUser.getCurrentUser() != null) {
                    ParseUser.getCurrentUser().logOutInBackground();
                }
                intent = new Intent(CompanyPage.this, Startup.class);
                CompanyPage.this.startActivity(intent);
                finish();
                return true;

            default:
                return true;
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        initReviewList();
        m_reviewAdapter.notifyDataSetChanged();
        m_reviewAdapter.notifyDataSetInvalidated();
    }

    protected void initViews() {
        m_companyNameView = (TextView) findViewById(R.id.company_name);
        m_companyURLView = (TextView) findViewById(R.id.company_url);
        m_companyImageView = (ImageView) findViewById(R.id.company_image);
        m_writeReviewButton = (Button) findViewById(R.id.add_review);
        m_overallRatingView = (RatingBar) findViewById(R.id.overallRating);
        m_qualityRatingView = (RatingBar) findViewById(R.id.qualityRating);
        m_serviceRatingView = (RatingBar) findViewById(R.id.serviceRating);
        m_shippingRatingView = (RatingBar) findViewById(R.id.shippingRating);

        if (ParseUser.getCurrentUser() == null) {
            m_writeReviewButton.setVisibility(View.GONE);
        }

        m_companyNameView.setText(m_company);
        m_writeReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("company", m_company);
                Intent intent = new Intent(CompanyPage.this, AddReview.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    protected void initAdapter() {
        m_vwReviewLayout = (ListView) findViewById(R.id.reviewListViewGroup);
        m_vwReviewLayout.addHeaderView(m_header);
        m_vwReviewLayout.setAdapter(m_reviewAdapter);
    }

    protected void initHeader() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Company");
        query.whereEqualTo("Name", m_company);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> companyList, ParseException e) {
                if (e == null) {
                    if (companyList.get(0).getParseFile("Image") == null) {
                        Picasso.with(CompanyPage.this).load(getResources().getString(R.string.placeholder_company_url)).into(m_companyImageView);
                    } else {
                        Picasso.with(CompanyPage.this).load(companyList.get(0).getParseFile("Image").getUrl()).into(m_companyImageView);
                    }
                    m_companyURLView.setText(companyList.get(0).getString("Url"));
                } else {
                    Log.e("Companies", "Error: " + e.getMessage());
                }
            }
        });
    }

    protected void initReviewList () {
        m_reviewList.clear();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Review");
        query.whereEqualTo("companyId", m_company);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> reviewList, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < reviewList.size(); i++) {
                        m_reviewList.add(reviewList.get(i));
                        m_reviewAdapter.notifyDataSetChanged();

                        numReviews++;
                        qualityRating += reviewList.get(i).getInt("qualityRating");
                        serviceRating += reviewList.get(i).getInt("serviceRating");
                        shippingRating += reviewList.get(i).getInt("shippingRating");
                    }
                    m_overallRatingView.setRating((qualityRating + serviceRating + shippingRating)/(3 * numReviews));
                    m_qualityRatingView.setRating(qualityRating/numReviews);
                    m_serviceRatingView.setRating(serviceRating/numReviews);
                    m_shippingRatingView.setRating(shippingRating/numReviews);
                } else {
                    Log.e("Companies", "Error: " + e.getMessage());
                }
                initUserPicList();
            }
        });
    }

    protected void initUserPicList () {
        m_userPicList.clear();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, ParseException e) {
            if (e == null) {
                for (int i = 0; i < m_reviewList.size(); i++) {
                    for (int j = 0; j < userList.size(); j++) {
                        if (m_reviewList.get(i).getString("userId").equals(userList.get(j).getUsername())) {
                            if (userList.get(j).getParseFile("picture") == null) {
                                m_userPicList.add(getResources().getString(R.string.placeholder_user_url));
                            } else {
                                m_userPicList.add(userList.get(j).getParseFile("picture").getUrl());
                            }
                            break;
                        }
                    }
                }
                m_reviewAdapter.notifyDataSetChanged();
            } else {
                Log.e("Companies", "Error: " + e.getMessage());
            }
            }
        });

    }
}
