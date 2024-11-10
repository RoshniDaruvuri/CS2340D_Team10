package com.example.wandersyncteam10.view;

import com.example.wandersyncteam10.view.AccommodationsLog;
import java.util.List;

public interface SortingStrategy {
    void sort(List<AccommodationsLog> logs); // Method signature must match exactly
}
