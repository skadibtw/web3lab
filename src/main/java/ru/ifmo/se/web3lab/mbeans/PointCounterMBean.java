package ru.ifmo.se.web3lab.mbeans;

import javax.management.NotificationBroadcaster;

public interface PointCounterMBean extends NotificationBroadcaster {
    long getTotalPoints();
    long getHitPoints();
    void incrementPoints(boolean hit);
}
