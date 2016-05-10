package edu.calpoly.mjzhao.audit;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

/**
 * Created by Myron on 5/10/2016.
 */
public class ReviewView  extends LinearLayout {

    private ImageView m_image;
    private TextView m_reviewer;
    private RatingBar m_service;
    private RatingBar m_shipping;
    private RatingBar m_quality;
    private TextView m_comment;

    public ReviewView (Context context, ParseObject review) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.review_view, this, true);

        m_image = (ImageView) findViewById(R.id.reviewerImage);
        m_reviewer = (TextView) findViewById(R.id.reviewerName);
        m_service = (RatingBar) findViewById(R.id.serviceRating);
        m_shipping = (RatingBar) findViewById(R.id.shippingRating);
        m_quality = (RatingBar) findViewById(R.id.qualityRating);
        m_comment = (TextView) findViewById(R.id.comment);

        setReview(review);
    }

    public void setReview (ParseObject review) {
        m_reviewer.setText(review.getString("userId"));
        m_service.setRating(review.getInt("serviceRating"));
        m_shipping.setRating(review.getInt("shippingRating"));
        m_quality.setRating(review.getInt("qualityRating"));
        m_comment.setText(review.getString("comment"));
        Picasso.with(getContext()).load(R.drawable.placeholder_user).into(m_image);
        requestLayout();
    }
}
