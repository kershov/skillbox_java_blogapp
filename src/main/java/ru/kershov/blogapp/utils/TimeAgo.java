package ru.kershov.blogapp.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeAgo {
    // TODO: To be refactored. Specify if is it needed to treat dates as "*** days ago, HH:mm"
    //       or it is possible to show date in some other format like "15th of Jan, 2020, HH:mm"
    private static final List<Long> times = Arrays.asList(
            TimeUnit.DAYS.toMillis(365),
            TimeUnit.DAYS.toMillis(30),
            TimeUnit.DAYS.toMillis(1),
            TimeUnit.HOURS.toMillis(1),
            TimeUnit.MINUTES.toMillis(1),
            TimeUnit.SECONDS.toMillis(1));

    private static final List<String> timesString = Arrays.asList(
            "year", "month", "day", "hour", "minute", "second"
    );

    private static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm")
            .withZone(ZoneId.systemDefault());

    public static String toDuration(Instant time) {

        long duration = Instant.now().toEpochMilli() - time.toEpochMilli();

        StringBuilder res = new StringBuilder();

        for (int i = 0; i < TimeAgo.times.size(); i++) {
            Long current = TimeAgo.times.get(i);
            long temp = duration / current;
            if (temp > 0) {
                res.append(temp)
                        .append(" ")
                        .append(TimeAgo.timesString.get(i))
                        .append(temp != 1 ? "s" : "")
                        .append(" ago, ")
                        .append(DATE_TIME_FORMATTER.format(time));
                break;
            }
        }
        if ("".equals(res.toString()))
            return "0 seconds ago";
        else
            return res.toString();
    }
}
