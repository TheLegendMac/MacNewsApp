package com.example.termi.macnewsapp;

public class Article {

    private String mTitle, mAuthor, mSectionTitle, mUrl;

    private long mTimeInMilliseconds;

    public Article(String mTitle, String mAuthor, String mSectionTitle, long mTimeInMilliseconds, String mUrl) {
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mSectionTitle = mSectionTitle;
        this.mTimeInMilliseconds = mTimeInMilliseconds;
        this.mUrl = mUrl;
    }

    public Article(String sectionName, String webTitle, String webPublicationDate, String url) {
        super();
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmSectionTitle() {
        return mSectionTitle;
    }

    public long getmTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    public String getmUrl() {
        return mUrl;
    }
}
