package com.example.termi.macnewsapp;

public class Article {

    private String mTitle, mAuthor, mSectionTitle, mUrl, mTimeInMilliseconds;


    public Article(String mTitle, String mAuthor, String mSectionTitle, String mTimeInMilliseconds, String mUrl) {
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mSectionTitle = mSectionTitle;
        this.mTimeInMilliseconds = mTimeInMilliseconds;
        this.mUrl = mUrl;
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

    public String getmTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    public String getmUrl() {
        return mUrl;
    }
}