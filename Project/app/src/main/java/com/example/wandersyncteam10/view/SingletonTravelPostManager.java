package com.example.wandersyncteam10.view;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class to manage Travel Posts throughout the app.
 */
public class SingletonTravelPostManager {

    private static SingletonTravelPostManager instance; // Single instance of this class
    private final List<TravelPost> travelPosts; // List to store travel posts

    /**
     * Private constructor to prevent external instantiation.
     */
    private SingletonTravelPostManager() {
        travelPosts = new ArrayList<>();
    }

    public void clearPosts() {
        travelPosts.clear(); // Assuming travelPosts is the list storing the posts
    }


    /**
     * Provides the single instance of SingletonTravelPostManager.
     *
     * @return the singleton instance.
     */
    public static synchronized SingletonTravelPostManager getInstance() {
        if (instance == null) {
            instance = new SingletonTravelPostManager();
        }
        return instance;
    }

    /**
     * Adds a new travel post to the list.
     *
     * @param post The TravelPost object to be added.
     */
    public void addTravelPost(TravelPost post) {
        travelPosts.add(post);
        Log.d("SingletonTravelPostManager", "Added post: " + post.getDestination());
    }

    /**
     * Retrieves all travel posts.
     *
     * @return the list of travel posts.
     */
    public List<TravelPost> getTravelPosts() {
        return new ArrayList<>(travelPosts); // Return a copy to prevent external modification
    }
}
