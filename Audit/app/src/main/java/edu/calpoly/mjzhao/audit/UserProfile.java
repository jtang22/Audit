package edu.calpoly.mjzhao.audit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Myron on 6/3/2016.
 */
public class UserProfile extends AppCompatActivity {

    protected ArrayList<ParseObject> m_reviewList;
    protected ArrayList<String> m_companyPicList;
    protected UserProfileAdapter m_reviewAdapter;
    protected ListView m_vwReviewLayout;
    protected View m_header;
    protected String m_user;
    protected Menu m_vwMenu;

    protected ImageView m_profilePic;
    protected TextView m_username;
    protected TextView m_profileName;
    protected TextView m_memberSince;
    protected TextView m_numReviews;
    protected Button m_changePic;

    protected Uri imageURI;
    protected Bitmap bitmap;

    private String TAG = "UserProfile";
    protected static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_company);

        Bundle getBundle = getIntent().getExtras();
        m_user = getBundle.getString("user");

        LayoutInflater inflater = (LayoutInflater)this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        m_header = inflater.inflate(R.layout.user_profile_header, null, false);

        m_reviewList = new ArrayList<>();
        m_companyPicList = new ArrayList<>();

        m_reviewAdapter = new UserProfileAdapter(this, m_reviewList, m_companyPicList);

        initAdapter();
        initViews();
        initHeader();
        initReviewList();
        m_reviewAdapter.notifyDataSetChanged();
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
                intent = new Intent(UserProfile.this, UserProfile.class);
                intent.putExtras(bundle);
                UserProfile.this.startActivity(intent);
                finish();
                return true;

            case R.id.logout:
                if (ParseUser.getCurrentUser() != null) {
                    ParseUser.getCurrentUser().logOutInBackground();
                }
                intent = new Intent(UserProfile.this, Startup.class);
                UserProfile.this.startActivity(intent);
                finish();
                return true;

            default:
                return true;
        }
    }

    protected void initViews() {
        m_profilePic = (ImageView) findViewById(R.id.user_profile_image);
        m_profileName = (TextView) findViewById(R.id.user_profile_name);
        m_username = (TextView) findViewById(R.id.user_profile_username);
        m_memberSince = (TextView) findViewById(R.id.member_since);
        m_numReviews = (TextView) findViewById(R.id.number_reviews);
        m_changePic = (Button) findViewById(R.id.change_picture);

        if (ParseUser.getCurrentUser() == null || !ParseUser.getCurrentUser().getUsername().equals(m_user)) {
            m_changePic.setVisibility(View.GONE);
        }

        m_username.setText(m_user);

        m_changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(UserProfile.this, "Error: No picture selected.", Toast.LENGTH_SHORT).show();
            } else {
                imageURI = data.getData();

                InputStream fileInputStream;
                try {
                    fileInputStream = UserProfile.this.getContentResolver().openInputStream(imageURI);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(UserProfile.this, "File upload failed!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bitmap bmp = BitmapFactory.decodeStream(fileInputStream);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imageData = stream.toByteArray();

                ParseFile uploadImage = new ParseFile(imageData, "jpeg");
                ParseUser.getCurrentUser().put("picture", uploadImage);
                ParseUser.getCurrentUser().saveInBackground();
                Picasso.with(UserProfile.this).load(imageURI).resize(160, 160).into(m_profilePic);
            }
        }
    }

    protected void initHeader() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", m_user);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, ParseException e) {
                if (e == null) {
                    ParseUser user = userList.get(0);
                    String fullName = "Name: " + user.getString("firstname") + " " + user.getString("lastname");
                    String numReviews = "Number of Reviews: " + user.getInt("numReviews");
                    String date[] = user.getCreatedAt().toString().split(" ");
                    String memberSince = "Member Since: " + date[1] + " " + date[2] + ", " + date[5];

                    m_profileName.setText(fullName);
                    m_memberSince.setText(memberSince);
                    m_numReviews.setText(numReviews);
                    Picasso.with(UserProfile.this).load(user.getParseFile("picture").getUrl()).resize(160, 160).into(m_profilePic);
                } else {
                    Log.e("User", "Error: " + e.getMessage());
                }
            }
        });

    }

    protected void initAdapter() {
        m_vwReviewLayout = (ListView) findViewById(R.id.companyListViewGroup);
        m_vwReviewLayout.addHeaderView(m_header);
        m_vwReviewLayout.setAdapter(m_reviewAdapter);
    }

    protected void initReviewList () {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Review");
        query.whereEqualTo("userId", m_user);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> reviewList, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < reviewList.size(); i++) {
                        Log.i("REVIEW", reviewList.get(i).getString("comment"));
                        m_reviewList.add(reviewList.get(i));
                        m_reviewAdapter.notifyDataSetChanged();
                    }
                    initCompanyPicList();
                } else {
                    Log.e("Companies", "Error: " + e.getMessage());
                }
            }
        });
    }

    protected void initCompanyPicList () {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Company");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> companyList, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < m_reviewList.size(); i++) {
                        for (int j = 0; j < companyList.size(); j++) {
                            if (m_reviewList.get(i).getString("companyId").equals(companyList.get(j).getString("Name"))) {
                                m_companyPicList.add(companyList.get(j).getParseFile("Image").getUrl());
                                m_reviewAdapter.notifyDataSetChanged();
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
