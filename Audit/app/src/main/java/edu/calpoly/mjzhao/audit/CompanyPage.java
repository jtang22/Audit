package edu.calpoly.mjzhao.audit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Myron on 5/10/2016.
 */
public class CompanyPage extends AppCompatActivity{

    protected ArrayList<ParseObject> m_reviewList;
    protected ReviewListAdapter m_reviewAdapter;
    protected ListView m_vwReviewLayout;
    protected ParseObject m_review;
    protected String m_company;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle getBundle = getIntent().getExtras();
        m_company = getBundle.getString("company");

        m_reviewList = new ArrayList<>();
        m_reviewAdapter = new ReviewListAdapter(this, m_reviewList);
        initReviewList();
        initLayout();
    }

    protected void initLayout() {
        setContentView(R.layout.company_view);
        m_vwReviewLayout = (ListView) findViewById(R.id.reviewListViewGroup);
        m_vwReviewLayout.setAdapter(m_reviewAdapter);
    }

    protected void initReviewList () {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Review");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> reviewList, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < reviewList.size(); i++) {
                        if (reviewList.get(i).getString("companyId").equals(m_company)) {
                            Log.d("REVIEW", reviewList.get(i).getString("comment"));
                            m_reviewList.add(reviewList.get(i));
                            m_reviewAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Log.d("Companies", "Error: " + e.getMessage());
                }
            }
        });
    }
}
