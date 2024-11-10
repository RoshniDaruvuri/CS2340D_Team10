package com.example.wandersyncteam10.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wandersyncteam10.R;
import com.example.wandersyncteam10.view.Reservation;

import java.util.ArrayList;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {
    private final ArrayList<Reservation> reservations;

    public ReservationAdapter(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);
        holder.textViewLocation.setText(reservation.getLocation());
        holder.textViewWebsite.setText(reservation.getWebsite());
        holder.textViewTime.setText(reservation.getTime());
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLocation, textViewWebsite, textViewTime;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            textViewWebsite = itemView.findViewById(R.id.textViewWebsite);
            textViewTime = itemView.findViewById(R.id.textViewTime);
        }
    }
}