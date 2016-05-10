package edu.calpoly.mjzhao.audit;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Myron on 5/9/2016.
 */
public class HomeScreen extends AppCompatActivity {

    private List<ParseObject> m_companyList;

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
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        android.widget.SearchView searchView = (android.widget.SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
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
                        Toast.makeText(HomeScreen.this, queriedItem + " not found!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("Companies", "Error: " + e.getMessage());
                }
            }
        });
    }

}
