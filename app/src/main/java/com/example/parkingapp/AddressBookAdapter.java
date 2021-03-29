package com.example.parkingapp;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AddressBookAdapter extends RecyclerView.Adapter<AddressBookAdapter.ViewHolder> {

    private final List<ParkingPlace> parkingBook;

    public AddressBookAdapter(List<ParkingPlace> items) {
        parkingBook = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.address.setText(parkingBook.get(position).getAddress());
        String available_places = "Доступно мест: ";
        holder.available.setText(new StringBuilder().append(available_places)
                .append(parkingBook.get(position).getNumberFreePlaces()).toString());
    }

    @Override
    public int getItemCount() {
        return parkingBook.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView address;
        public final TextView available;
        public Long parkingId;

        public ViewHolder(View view) {
            super(view);
            this.address = (TextView) view.findViewById(R.id.item_address);
            this.available = (TextView) view.findViewById(R.id.item_available);
        }
    }
}