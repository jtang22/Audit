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

    public ReviewListAdapter(Context context, List<ParseObject> reviewList) {
        m_context = context;
        m_reviewList = reviewList;
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
            convertView = new ReviewView(m_context, m_reviewList.get(position));
        } else {
            ((ReviewView)convertView).setReview(m_reviewList.get(position));
        }

        return convertView;
    }
}
