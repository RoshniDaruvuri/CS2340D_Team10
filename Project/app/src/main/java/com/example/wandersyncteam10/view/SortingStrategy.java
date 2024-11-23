package com.example.wandersyncteam10.view;

import java.util.List;

/**
 * Defines a strategy for sorting accommodation logs.
 */
public interface SortingStrategy {

    /**
     * Sorts a list of accommodation logs according to a specific strategy.
     *
     * @param logs The list of accommodation logs to be sorted.
     */
    void sort(List<AccommodationsLog> logs); // Method signature must match exactly
}
