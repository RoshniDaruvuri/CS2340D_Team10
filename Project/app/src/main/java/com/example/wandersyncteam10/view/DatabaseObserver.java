package com.example.wandersyncteam10.view;

import java.util.List;

public interface DatabaseObserver<T> {
    void onDataUpdated(List<T> data);
    void onError(String errorMessage);
}

