package ru.ifmo.se.web3lab.mbeans;

public interface ClickIntervalMBean {
    double getAverageIntervalMillis();
    void recordClick(long timestamp);
}
