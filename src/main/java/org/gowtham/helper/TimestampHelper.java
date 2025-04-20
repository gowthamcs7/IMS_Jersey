package org.gowtham.helper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimestampHelper {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Convert Timestamp to formatted string
    public static String formatTimestamp(Timestamp timestamp) {
        return (timestamp != null) ? dateFormat.format(timestamp) : null;
    }
}
