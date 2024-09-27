package com.example.eventbrite.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventbrite.Models.Notification;
import com.example.eventbrite.R;
import com.example.eventbrite.Utils.TimeUtils;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notifications;

    // Constructor
    public NotificationAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the notification item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        // Get the current notification
        Notification notification = notifications.get(position);

        // Bind the data to the views
        holder.messageTextView.setText(notification.getMessage());
        holder.timeTextView.setText(TimeUtils.getTimeAgo(notification.getTimestamp()));

        // Optionally set the profile picture or any other view
        // holder.profilePicImageView.setImageResource(R.drawable.placeholder_image); // Example
    }

    @Override
    public int getItemCount() {
        return notifications != null ? notifications.size() : 0;
    }

    // ViewHolder class for holding the views
    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView timeTextView;
        ImageView profilePicImageView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.notificationItemMessageTv);
            timeTextView = itemView.findViewById(R.id.notificationItemTimeTV);
            profilePicImageView = itemView.findViewById(R.id.notificationItemProfilePicIV);
        }
    }

    // Method to update the list of notifications
    public void updateNotifications(List<Notification> newNotifications) {
        this.notifications.clear();
        this.notifications.addAll(newNotifications);
        notifyDataSetChanged();
    }
}
