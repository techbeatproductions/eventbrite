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

public class EventCardSearchViewItemAdapter extends RecyclerView.Adapter<EventCardSearchViewItemAdapter.EventCardSearchViewItemViewHolder> {
    private final ArrayList<Event> eventCardSearchViewEventList;
    private final Context context;

    public EventCardSearchViewItemAdapter(ArrayList<Event> eventCardSearchViewEventList,Context context) {
        this.eventCardSearchViewEventList = eventCardSearchViewEventList;
        this.context =context;
    }

    @NonNull
    @Override
    public EventCardSearchViewItemAdapter.EventCardSearchViewItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_for_search_view_item, parent, false);
        return new EventCardSearchViewItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventCardSearchViewItemAdapter.EventCardSearchViewItemViewHolder holder, int position) {
        Event event = eventCardSearchViewEventList.get(position);
        String concatenateEventDateTime = event.getEvent_date();


        //Set text views
        holder.eventTitleInEventCardSearchView.setText(event.getEvent_name());
        holder.eventDateTimeInEventCardSearchView.setText(concatenateEventDateTime);

        //Set onclick listeners
        holder.eventCardInEventCardSearchView.setOnClickListener(v -> navigateToEvent(event));


        //Set image
        Glide.with(context)
                .load(event.getEvent_image())
                .into(holder.eventImageInEventCardSearchView);

    }

    private void navigateToEvent(Event event) {
        Intent intent = new Intent(context, AllFragmentsActivity.class);
        intent.putExtra("fetchedEventList", eventCardSearchViewEventList); // Pass the entire list
        intent.putExtra("fragmentToLoad", "SpecificEvent"); // Specify the fragment to load
        intent.putExtra("selectedEvent", event); // Pass the selected event

        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return eventCardSearchViewEventList.size();
    }

    public static class EventCardSearchViewItemViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImageInEventCardSearchView;
        TextView eventDateTimeInEventCardSearchView, eventTitleInEventCardSearchView;
        CardView eventCardInEventCardSearchView;


        public EventCardSearchViewItemViewHolder(@NonNull View itemView) {
            super(itemView);
            eventDateTimeInEventCardSearchView = (TextView) itemView.findViewById(R.id.eventDateTimeInSearchViewItemTV);
            eventTitleInEventCardSearchView = (TextView)  itemView.findViewById(R.id.eventTitleInSearchViewItemTV);
            eventImageInEventCardSearchView = (ImageView) itemView.findViewById(R.id.eventImgInSearchViewItemIV);
            eventCardInEventCardSearchView = (CardView) itemView.findViewById(R.id.eventCardViewInSearchViewItem);

        }
    }
}
