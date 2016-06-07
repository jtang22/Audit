package edu.calpoly.mjzhao.audit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

/**
 * Created by Myron on 6/3/2016.
 */
public class UserProfileView  extends LinearLayout {

    private ImageButton m_image;
    private TextView m_company;
    private RatingBar m_service;
    private RatingBar m_shipping;
    private RatingBar m_quality;
    private TextView m_comment;

    public UserProfileView (Context context, ParseObject review) {
        super(context);
        final Context m_context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.review_view, this, true);

        m_image = (ImageButton) findViewById(R.id.reviewerImage);
        m_company = (TextView) findViewById(R.id.reviewerName);
        m_service = (RatingBar) findViewById(R.id.serviceRating);
        m_shipping = (RatingBar) findViewById(R.id.shippingRating);
        m_quality = (RatingBar) findViewById(R.id.qualityRating);
        m_comment = (TextView) findViewById(R.id.comment);

        setReview(review);
        m_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ReviewView", "onClick: click received");
                Bundle bundle = new Bundle();
                bundle.putString("company", m_company.getText().toString());
                Intent intent = new Intent(m_context, CompanyPage.class);
                intent.putExtras(bundle);
                m_context.startActivity(intent);
            }

        });
    }

    public UserProfileView (Context context, ParseObject review, String userPic) {
        super(context);
        final Context m_context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.review_view, this, true);

        m_image = (ImageButton) findViewById(R.id.reviewerImage);
        m_company = (TextView) findViewById(R.id.reviewerName);
        m_service = (RatingBar) findViewById(R.id.serviceRating);
        m_shipping = (RatingBar) findViewById(R.id.shippingRating);
        m_quality = (RatingBar) findViewById(R.id.qualityRating);
        m_comment = (TextView) findViewById(R.id.comment);

        setReview(review, userPic);
        m_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("company", m_company.getText().toString());
                Intent intent = new Intent(m_context, CompanyPage.class);
                intent.putExtras(bundle);
                m_context.startActivity(intent);
            }

        });
    }

    public void setReview (ParseObject review) {
        m_company.setText(review.getString("companyId"));
        m_service.setRating(review.getInt("serviceRating"));
        m_shipping.setRating(review.getInt("shippingRating"));
        m_quality.setRating(review.getInt("qualityRating"));
        m_comment.setText(review.getString("comment"));
        requestLayout();
    }

    public void setReview (ParseObject review, String userPic) {
        m_company.setText(review.getString("companyId"));
        m_service.setRating(review.getInt("serviceRating"));
        m_shipping.setRating(review.getInt("shippingRating"));
        m_quality.setRating(review.getInt("qualityRating"));
        m_comment.setText(review.getString("comment"));
        Picasso.with(getContext()).load(userPic).resize(160, 160).into(m_image);
        requestLayout();
    }
}
