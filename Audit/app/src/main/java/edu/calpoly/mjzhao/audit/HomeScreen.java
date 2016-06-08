package edu.calpoly.mjzhao.audit;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Myron on 5/9/2016.
 */
public class HomeScreen extends AppCompatActivity {

    private List<ParseObject> m_companyList;
    private int SIZE_OF_OVERFLOW = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        if (m_companyList == null) {
            m_companyList = new ArrayList<>();
        }
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (ParseUser.getCurrentUser() != null) {
            // Inflate the options menu from XML
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.options_menu, menu);

            getSupportActionBar().setDisplayShowTitleEnabled(false);

            // Get the SearchView and set the searchable configuration
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            android.widget.SearchView searchView = (android.widget.SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setMaxWidth(getWindowManager().getDefaultDisplay().getWidth() - SIZE_OF_OVERFLOW);
            searchView.setQueryHint(getResources().getString(R.string.search));



            // Assumes current activity is the searchable activity
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

            return true;
        } else {
            // Inflate the options menu from XML
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.search_only, menu);

            getSupportActionBar().setDisplayShowTitleEnabled(false);

            // Get the SearchView and set the searchable configuration
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            android.widget.SearchView searchView = (android.widget.SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setQueryHint(getResources().getString(R.string.search));

            // Assumes current activity is the searchable activity
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

            return true;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.viewProfile:
                Bundle bundle = new Bundle();
                bundle.putString("user", ParseUser.getCurrentUser().getUsername().toString());
                intent = new Intent(HomeScreen.this, UserProfile.class);
                intent.putExtras(bundle);
                HomeScreen.this.startActivity(intent);
                return true;

            case R.id.logout:
                if (ParseUser.getCurrentUser() != null) {
                    ParseUser.getCurrentUser().logOutInBackground();
                }
                intent = new Intent(HomeScreen.this, Startup.class);
                HomeScreen.this.startActivity(intent);
                finish();
                return true;

            default:
                return true;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            parseQuery(query);
        }
    }

    private void parseQuery (final String queriedItem) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Company");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> companyList, ParseException e) {
                Bundle bundle;
                Intent intent;
                Boolean found = false;

                if (e == null) {
                    for (int i = 0; i < companyList.size(); i++) {
                        if (companyList.get(i).getString("Name").toLowerCase().equals(queriedItem.toLowerCase())) {
                            bundle = new Bundle();
                            bundle.putString("company", companyList.get(i).getString("Name"));
                            intent = new Intent(HomeScreen.this, CompanyPage.class);
                            intent.putExtras(bundle);
                            found = true;
                            startActivity(intent);
                            break;
                        }
                    }

                    if (found) {
                        Toast.makeText(HomeScreen.this, queriedItem + " found!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (ParseUser.getCurrentUser() == null) {
                            Toast.makeText(HomeScreen.this, queriedItem + " not found!", Toast.LENGTH_SHORT).show();
                        } else {
                            String title = queriedItem + " " + getResources().getString(R.string.new_company);

                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
                            builder.setMessage(title)
                                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            dialog.cancel();
                                        }
                                    })
                                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("company", queriedItem);
                                            bundle.putString("new", "new");
                                            Intent intent = new Intent(HomeScreen.this, AddReview.class);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                            dialog.dismiss();
                                        }
                                    }).create().show();
                        }
                    }

                } else {
                    Log.d("Companies", "Error: " + e.getMessage());
                }
            }
        });
    }

}
