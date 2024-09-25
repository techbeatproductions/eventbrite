package com.example.eventbrite.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventbrite.AllFragmentsActivity;
import com.example.eventbrite.Models.Event;
import com.example.eventbrite.R;

import java.util.ArrayList;
import java.util.List;

public class EventItemAdapter extends RecyclerView.Adapter<EventItemAdapter.EventItemViewHolder> {
    private final ArrayList<Event> eventsList;
    private Context context;

    public EventItemAdapter(ArrayList<Event> eventsList, Context context) {
        this.eventsList = eventsList;
        this.context = context;
    }

    @NonNull
    @Override
    public EventItemAdapter.EventItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_item_layout, parent, false);
        return new EventItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventItemAdapter.EventItemViewHolder holder, int position) {
        Event event = eventsList.get(position);

        //Set Text views
        holder.eventItemName.setText(event.getEvent_name());
        holder.eventItemDate.setText(event.getEvent_date());
        holder.eventItemLocation.setText(event.getEvent_location());

        //Set Images
        Glide.with(context)
                .load(event.getEvent_image())
                .into(holder.eventItemImage);

        //Set onclick listeners
        holder.eventCard.setOnClickListener(v -> navigateToEvent(event));
        holder.eventItemBookMark.setOnClickListener(v -> bookMarkEvent());


    }

    private void bookMarkEvent() {
    }

    private void navigateToEvent(Event event) {
        Intent intent = new Intent(context, AllFragmentsActivity.class);
        intent.putExtra("fetchedEventList", eventsList); // Pass the entire list
        intent.putExtra("fragmentToLoad", "SpecificEvent"); // Specify the fragment to load
        intent.putExtra("selectedEvent", event); // Pass the selected event

        context.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public static class EventItemViewHolder extends RecyclerView.ViewHolder{
        ImageView eventItemImage;
        ImageView eventItemBookMark;
        TextView eventItemDate;
        TextView eventItemName;
        TextView eventItemLocation;
        CardView eventCard;


        public EventItemViewHolder(@NonNull View itemView) {
            super(itemView);

            eventItemImage = itemView.findViewById(R.id.eventItemImageIV);
            eventItemBookMark = itemView.findViewById(R.id.eventItemBookMarkIV);
            eventItemDate = itemView.findViewById(R.id.eventItemDateTV);
            eventItemName = itemView.findViewById(R.id.eventItemNameTV);
            eventItemLocation = itemView.findViewById(R.id.eventItemLocationTV);
            eventCard = itemView.findViewById(R.id.eventItemCardView);
        }
    }
}
