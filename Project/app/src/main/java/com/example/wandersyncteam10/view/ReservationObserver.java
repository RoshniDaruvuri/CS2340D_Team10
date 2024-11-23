package com.example.wandersyncteam10.view;

import java.util.List;

/**
 * Observer interface for monitoring reservation updates.
 */
public interface ReservationObserver {

    /**
     * Called when reservations are updated.
     *
     * @param reservations The updated list of reservations.
     */
    void onReservationUpdated(List<Reservation> reservations);
}
