package com.example.termi.macnewsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {


    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the USGS dataset and return a list of {@link Article} objects.
     */
    public static List<Article> fetchArticleData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
            Log.e(LOG_TAG, "Connecting  HTTP request.");

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link article}s
        List<Article> articles = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link article}s
        return articles;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
            Log.e(LOG_TAG, "Building URL");
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000); // milliseconds
            urlConnection.setConnectTimeout(15000); // milliseconds
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the article JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Article} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Article> extractFeatureFromJson(String articleJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding articles to
        List<Article> articles = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(articleJSON);

            JSONObject articleResponse = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or articles).
            JSONArray articleArray = articleResponse.getJSONArray("results");

            // For each article in the articleArray, create an {@link article} object
            for (int i = 0; i < articleArray.length(); i++) {

                // Get a single article at position i within the list of articles
                JSONObject currentArticle = articleArray.getJSONObject(i);

                // Extract the value for the key called "sectionName"
                String sectionName = currentArticle.optString("sectionName");

                // Extract the value for the key called "webTitle"
                String articleTitle = currentArticle.optString("webTitle");

                // Extract the value for the key called "webPublication"
                //The "webPublicationDateAndTime" will hold the full date and time from the webPublicationDate of the article
                String webPublicationDateAndTime = currentArticle.getString("webPublicationDate");

                String[] webPublicationDateOnly;
                webPublicationDateOnly = webPublicationDateAndTime.split("T");

                String webArticleDate = webPublicationDateOnly[0];

                // Extract the value for the key called "webTitle"
                String url = currentArticle.optString("webUrl");

                String articleAuthor = " ";
                if (currentArticle.has("tags")) {
                    //Check if the JSONArray has the key "tags." Extract the value for the key called "webTitle."
                    JSONArray tagsArray = currentArticle.getJSONArray("tags");
                    if (tagsArray != null && tagsArray.length() > 0) {
                        JSONObject authorTag = (JSONObject) tagsArray.get(0);
                        articleAuthor = authorTag.getString("webTitle");
                        articleAuthor = "Article by " + articleAuthor;
                    }

                    // Create a new {@link article} object with the magnitude, location, time,
                    // and url from the JSON response.
                    Article article = new Article(articleTitle, articleAuthor, sectionName, webArticleDate, url);
                    // Add the article to the list of articles.
                    articles.add(article);
                }

            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing article JSON results", e);
        }

        // Return the list of articles.
        return articles;
    }
}