package com.example.eventbrite.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventbrite.Models.Event;
import com.example.eventbrite.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class EventButtonTagAdapter extends RecyclerView.Adapter<EventButtonTagAdapter.EventButtonTagViewHolder> {
    private final List<String> uniqueTagsList = new ArrayList<>(); // Initialize as empty list
    private final OnTagClickListener onTagClickListener;

    // Define an interface for tag click listener
    public interface OnTagClickListener {
        void onTagClick(String tag);
    }

    // Define a set of colors to choose from
    private final int[] colors = {

            Color.parseColor("#F0635A"), // Custom color
            Color.parseColor("#F59762"), // Custom color
            Color.parseColor("#29D697"), // Custom color
            Color.parseColor("#46CDFB")  // Custom color
    };

    private int lastUsedColor = -1;

    public EventButtonTagAdapter(OnTagClickListener listener) {
        this.onTagClickListener = listener;
    }

    @NonNull
    @Override
    public EventButtonTagAdapter.EventButtonTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buttons_recycler_view_layout, parent, false);
        return new EventButtonTagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventButtonTagAdapter.EventButtonTagViewHolder holder, int position) {
        String tag = uniqueTagsList.get(position);
        holder.tagButton.setText(tag);

        // Set a random color from the predefined color array
        holder.tagButton.setBackgroundColor(getRandomColorFromSet());

        // On click listener for the button
        holder.tagButton.setOnClickListener(v -> eventTagButtonClicked(tag));
    }

    private void eventTagButtonClicked(String tag) {
        if (onTagClickListener != null) {
            onTagClickListener.onTagClick(tag); // Pass the tag back to the activity/fragment
        }
    }

    @Override
    public int getItemCount() {
        return uniqueTagsList.size();
    }

    // Method to update the list of unique tags
    public void updateEventList(List<Event> eventList) {
        Set<String> uniqueTagsSet = new HashSet<>();
        for (Event event : eventList) {
            // Normalize the tag by trimming whitespace and converting to lowercase
            String normalizedTag = event.getEvent_tag().trim();
            uniqueTagsSet.add(normalizedTag);  // Add only unique tags
        }
        this.uniqueTagsList.clear();
        this.uniqueTagsList.addAll(uniqueTagsSet); // Update unique tags
        notifyDataSetChanged(); // Notify the adapter of data change
    }

    // Method to get a random color from the predefined set
    private int getRandomColorFromSet() {
        Random random = new Random();
        int newColor;
        do {
            newColor = colors[random.nextInt(colors.length)]; // Select a random color from the array
        } while (newColor == lastUsedColor); // Ensure it's not the same as the last color

        lastUsedColor = newColor; // Update the last used color
        return newColor; // Return the new color
    }

    public static class EventButtonTagViewHolder extends RecyclerView.ViewHolder {
        Button tagButton;

        public EventButtonTagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagButton = itemView.findViewById(R.id.eventTagButtonRecyclerView);
        }
    }
}
