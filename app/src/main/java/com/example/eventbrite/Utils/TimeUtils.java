package com.example.eventbrite.Utils;

import java.util.concurrent.TimeUnit;

public class TimeUtils {
    public static String getTimeAgo(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - timestamp;

        if (timeDifference < 0) {
            return "Just now"; // If the timestamp is in the future
        }

        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDifference);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference);
        long hours = TimeUnit.MILLISECONDS.toHours(timeDifference);
        long days = TimeUnit.MILLISECONDS.toDays(timeDifference);
        long weeks = days / 7;
        long months = days / 30; // Approximation
        long years = days / 365; // Approximation

        if (seconds < 60) {
            return (seconds == 1) ? "1 second ago" : seconds + " seconds ago";
        } else if (minutes < 60) {
            return (minutes == 1) ? "1 minute ago" : minutes + " minutes ago";
        } else if (hours < 24) {
            return (hours == 1) ? "1 hour ago" : hours + " hours ago";
        } else if (days < 30) {
            return (days == 1) ? "1 day ago" : days + " days ago";
        } else if (days < 365) {
            if (weeks < 5) {
                return (weeks == 1) ? "1 week ago" : weeks + " weeks ago";
            } else {
                return (months == 1) ? "1 month ago" : months + " months ago";
            }
        } else {
            return (years == 1) ? "1 year ago" : years + " years ago";
        }
    }
}
