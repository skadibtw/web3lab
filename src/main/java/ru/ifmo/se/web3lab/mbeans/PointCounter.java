package ru.ifmo.se.web3lab.mbeans;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public class PointCounter extends NotificationBroadcasterSupport implements PointCounterMBean {
    private long totalPoints = 0;
    private long hitPoints = 0;
    private long sequenceNumber = 1;

    @Override
    public long getTotalPoints() {
        return totalPoints;
    }

    @Override
    public long getHitPoints() {
        return hitPoints;
    }

    @Override
    public synchronized void incrementPoints(boolean hit) {
        totalPoints++;
        if (hit) {
            hitPoints++;
        }

        if (totalPoints % 5 == 0) {
            Notification n = new AttributeChangeNotification(this,
                    sequenceNumber++, System.currentTimeMillis(),
                    "Total points is a multiple of 5", "TotalPoints", "long",
                    totalPoints -1, totalPoints); // Provide old and new values
            sendNotification(n);
        }
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[]{
                AttributeChangeNotification.ATTRIBUTE_CHANGE
        };
        String name = AttributeChangeNotification.class.getName();
        String description = "Total points count reached a multiple of 5";
        MBeanNotificationInfo info = new MBeanNotificationInfo(types, name, description);
        return new MBeanNotificationInfo[]{info};
    }
}
