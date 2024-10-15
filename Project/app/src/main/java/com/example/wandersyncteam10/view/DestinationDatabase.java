package com.example.wandersyncteam10.view;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class DestinationDatabase {
    private static DestinationDatabase instance;
    private final List<TravelLog> travelLogs;

    private DestinationDatabase() {
        travelLogs = new ArrayList<>();
        // Prepopulate with 2 entries
        travelLogs.add(new TravelLog("New York", "2024-01-10", "2024-01-20"));
        travelLogs.add(new TravelLog("Tokyo", "2024-05-01", "2024-05-15"));
    }

    public static synchronized DestinationDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new DestinationDatabase();
        }
        return instance;
    }

    public void addTravelLog(String location, String startDate, String endDate) {
        travelLogs.add(new TravelLog(location, startDate, endDate));
    }

    public List<TravelLog> getTravelLogs() {
        return travelLogs;
    }
}
