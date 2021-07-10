package com.example.instagramclient.classes;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject implements Serializable {

    private static final String KEY_USER = "user";
    private static final String KEY_IMAGE = "Image";
    private static final String KEY_DESC = "description";
    private static final String KEY_LIKES = "likes";
    private static final String KEY_LIST = "userList";

    private List<String> userList = new ArrayList<>();

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public String getDesc() {
        return getString(KEY_DESC);
    }

    public void setDesc(String description) {
        put(KEY_DESC, description);
    }

    public int getLikes() {
        return getInt(KEY_LIKES);
    }

    public void setLikes(int likes) {
        put(KEY_LIKES, likes);
    }

    // returns the formatted list of the ids of user who liked the post
    public List<String> getList() {

        JSONArray jsonArray = getJSONArray(KEY_LIST);

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    userList.add(jsonArray.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return userList;
    }

    public void setList(List<String> newList) {
        put(KEY_LIST, newList);
    }

    // returns the timestamp as a string
    public String getTimeStamp() {
        return calculateTimeAgo(this.getCreatedAt());
    }

    // converts date object to String timestamp
    public static String calculateTimeAgo(Date createdAt) {

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m ago";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h ago";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d ago";
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }

        return "";
    }

    // adds user to post like array
    public void addUser(ParseUser user) {
        userList.add(user.getObjectId());
        setList(userList);
    }

    // removes user to post like array
    public void removeUser(ParseUser user) {

        for (String id : userList) {
            if (user.getObjectId().equals(id))
                userList.remove(id);
        }
        setList(userList);
    }

    // checks if the given user liked the post
    public boolean isLiked(ParseUser user) {
        return userList.contains(user.getObjectId());
    }

    public static String getKeyUser() {
        return KEY_USER;
    }

    public static String getKeyImage() {
        return KEY_IMAGE;
    }

    public static String getKeyDesc() {
        return KEY_DESC;
    }
}



