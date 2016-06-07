package edu.calpoly.mjzhao.audit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

/**
 * Created by Myron on 5/31/2016.
 */

public class AddReview extends AppCompatActivity {

    protected String m_company;
    protected String m_addCompany;
    protected TextView m_companyName;
    protected RatingBar m_qualityRating;
    protected RatingBar m_serviceRating;
    protected RatingBar m_shippingRating;
    protected EditText m_commentText;
    protected Button m_submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_review);

        Bundle getBundle = getIntent().getExtras();
        m_company = getBundle.getString("company");
        m_addCompany = getBundle.getString("new");

        initViews();

        m_submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_addCompany != null) {
                    ParseObject company = new ParseObject("Company");
                    company.put("Name", m_company);
                    company.put("Url", getResources().getString(R.string.none_available));

                    Drawable placeholder = ResourcesCompat.getDrawable(getResources(), R.drawable.placeholder_company, null);
                    Bitmap bmp = ((BitmapDrawable)placeholder).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] bitmapdata = stream.toByteArray();
                    ParseFile upload = new ParseFile(bitmapdata, "jpeg");

                    company.put("Image", upload);
                    company.saveInBackground();
                }

                ParseObject review = new ParseObject("Review");
                review.put("companyId", m_company);
                review.put("userId", ParseUser.getCurrentUser().getUsername());
                review.put("serviceRating", (int)m_qualityRating.getRating());
                review.put("shippingRating", (int)m_shippingRating.getRating());
                review.put("qualityRating", (int)m_serviceRating.getRating());
                review.put("comment", m_commentText.getText().toString());
                review.put("flagged", false);
                review.put("score", 0);
                review.saveInBackground();

                ParseUser.getCurrentUser().put("numReviews", ParseUser.getCurrentUser().getInt("numReviews") + 1);
                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (m_addCompany != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString("company", m_company);
                            Intent intent = new Intent(AddReview.this, CompanyPage.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

                if (m_addCompany == null) {
                    finish();
                }
            }
        });

    }

    protected void initViews () {
        String reviewForName;

        m_companyName = (TextView) findViewById(R.id.review_for);
        m_qualityRating = (RatingBar) findViewById(R.id.qualityRating);
        m_serviceRating = (RatingBar) findViewById(R.id.serviceRating);
        m_shippingRating = (RatingBar) findViewById(R.id.shippingRating);
        m_commentText = (EditText) findViewById(R.id.add_review_comment);
        m_submitButton = (Button) findViewById(R.id.submit);

        reviewForName = getResources().getString(R.string.reviewFor)+ " " + m_company;
        m_companyName.setText(reviewForName);
    }
}
