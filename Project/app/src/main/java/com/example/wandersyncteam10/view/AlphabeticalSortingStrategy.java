package com.example.wandersyncteam10.view;

import com.example.wandersyncteam10.Model.AccommodationsLog;
import com.example.wandersyncteam10.Model.SortingStrategy;

import java.util.Collections;
import java.util.List;

public class AlphabeticalSortingStrategy implements SortingStrategy {
    @Override
    public void sort(List<AccommodationsLog> logs) {
        // Sort alphabetically by location in AccommodationsLog
        Collections.sort(logs, (log1, log2) -> {
            String location1 = log1.getLocation(); // Use getLocation() method
            String location2 = log2.getLocation();
            return location1.compareToIgnoreCase(location2);
        });
    }
}
