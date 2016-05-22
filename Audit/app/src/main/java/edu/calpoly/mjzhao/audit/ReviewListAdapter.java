package edu.calpoly.mjzhao.audit;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Myron on 5/10/2016.
 */
public class ReviewListAdapter extends BaseAdapter{

    private Context m_context;
    private List<ParseObject> m_reviewList;
    private List<String> m_userPicList;

    public ReviewListAdapter(Context context, List<ParseObject> reviewList, List<String> userPicList) {
        m_context = context;
        m_reviewList = reviewList;
        m_userPicList = userPicList;
    }

    @Override
    public int getCount() {
        return m_reviewList.size();
    }

    @Override
    public Object getItem(int position) {
        return m_reviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            if (m_userPicList.size() != m_reviewList.size()) {
                convertView = new ReviewView(m_context, m_reviewList.get(position));
            } else {
                convertView = new ReviewView(m_context, m_reviewList.get(position), m_userPicList.get(position));
            }
        } else {
            if (m_userPicList.size() != m_reviewList.size()) {
                ((ReviewView)convertView).setReview(m_reviewList.get(position));
            } else {
                ((ReviewView)convertView).setReview(m_reviewList.get(position), m_userPicList.get(position));
            }
        }

        return convertView;
    }
}
