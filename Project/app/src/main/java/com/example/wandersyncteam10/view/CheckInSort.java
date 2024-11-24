package com.example.wandersyncteam10.view;

//import com.example.wandersyncteam10.Model.AccommodationsLog;
import com.example.wandersyncteam10.Model.AccommodationsLog;
import com.example.wandersyncteam10.Model.SortingStrategy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class CheckInSort implements SortingStrategy {
    @Override // Make sure this annotation is present
    public void sort(List<AccommodationsLog> logs) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        Collections.sort(logs, (log1, log2) -> {
            LocalDate checkinDate1 = LocalDate.parse(log1.getCheckin(), formatter);
            LocalDate checkinDate2 = LocalDate.parse(log2.getCheckin(), formatter);
            return checkinDate1.compareTo(checkinDate2);
        });
    }
}
