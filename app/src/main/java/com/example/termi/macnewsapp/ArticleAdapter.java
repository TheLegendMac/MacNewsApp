package com.example.termi.macnewsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article> {

    /**
     * The part of the location string from the USGS service that we use to determine
     * whether or not there is a location offset present ("5km N of Cairo, Egypt").
     */
    private static final String LOCATION_SEPARATOR = " of ";

    /**
     * Constructs a new {@link ArticleAdapter}.
     *
     * @param context     of the app
     * @param articles is the list of articles, which is the data source of the adapter
     */
    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    /**
     * Returns a list item view that displays information about the article at the given position
     * in the list of articles.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_list_item, parent, false);
        }

        // Find the article at the given position in the list of articles
        Article currentArticle = getItem(position);

        // Find the TextView with view ID time
        TextView sectionTitle = (TextView) listItemView.findViewById(R.id.section_name);
        // Display the time of the current article in that TextView
        sectionTitle.setText(currentArticle.getmSectionTitle());

        // Find the TextView with view ID location offset
        TextView articleTitle = (TextView) listItemView.findViewById(R.id.article_title);
        // Display the location offset of the current article in that TextView
        articleTitle.setText(currentArticle.getmTitle());

        // Find the TextView with view ID location
        TextView authorName = (TextView) listItemView.findViewById(R.id.author_name);
        // Display the Author of the current article in that TextView
        authorName.setText(currentArticle.getmAuthor());

        // Find the TextView with view ID date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        dateView.setText(currentArticle.getmTimeInMilliseconds());


        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

}
