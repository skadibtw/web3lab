package ru.ifmo.se.web3lab.mbeans;

public class ClickInterval implements ClickIntervalMBean {
    private long lastClickTime = 0;
    private long totalIntervalSum = 0;
    private int clickCount = 0;

    @Override
    public synchronized double getAverageIntervalMillis() {
        if (clickCount < 1) { 
            return 0.0;
        }
        return (double) totalIntervalSum / clickCount;
    }

    @Override
    public synchronized void recordClick(long timestamp) {
        if (lastClickTime != 0) {
            long interval = timestamp - lastClickTime;
            totalIntervalSum += interval;
            clickCount++;
        }
        lastClickTime = timestamp;
    }
}
