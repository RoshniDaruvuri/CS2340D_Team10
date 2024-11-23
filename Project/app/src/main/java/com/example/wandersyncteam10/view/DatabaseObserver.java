package com.example.wandersyncteam10.view;

import java.util.List;

/**
 * Interface for observing database changes.
 *
 * @param <T> the type of data being observed
 */
public interface DatabaseObserver<T> {

    /**
     * Called when the database data is updated.
     *
     * @param data the updated list of data
     */
    void onDataUpdated(List<T> data);

    /**
     * Called when an error occurs in the database operation.
     *
     * @param errorMessage the error message describing the issue
     */
    void onError(String errorMessage);
}
