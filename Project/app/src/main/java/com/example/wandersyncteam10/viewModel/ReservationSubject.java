package com.example.wandersyncteam10.viewModel;

import com.example.wandersyncteam10.Model.Reservation;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject class to manage observers and notify them of reservation updates.
 */
public class ReservationSubject {

    private List<ReservationObserver> observers = new ArrayList<>();

    /**
     * Adds an observer to the list.
     *
     * @param observer The observer to add.
     */
    public void addObserver(ReservationObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the list.
     *
     * @param observer The observer to remove.
     */
    public void removeObserver(ReservationObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers of a reservation update.
     *
     * @param reservations The updated list of reservations.
     */
    public void notifyObservers(List<Reservation> reservations) {
        for (ReservationObserver observer : observers) {
            observer.onReservationUpdated(reservations);
        }
    }
}
