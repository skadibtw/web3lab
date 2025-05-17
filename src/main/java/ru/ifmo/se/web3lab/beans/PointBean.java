package ru.ifmo.se.web3lab.beans;

import java.io.Serializable;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import ru.ifmo.se.web3lab.managers.DBManager;
import ru.ifmo.se.web3lab.mbeans.ClickInterval;
import ru.ifmo.se.web3lab.mbeans.PointCounter;
import ru.ifmo.se.web3lab.models.Point;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("pointBean")
@SessionScoped
public class PointBean implements Serializable {
    private double x;
    private double y;
    private double r;
    private ArrayList<Point> pointsList;
    protected DBManager dbManager;

    // MBean related fields
    private PointCounter pointCounter;
    private ClickInterval clickInterval;
    private MBeanServer mBeanServer;
    private ObjectName pointCounterName;
    private ObjectName clickIntervalName;

    @Inject
    private HttpSession session; // Inject HttpSession to get session ID

    @PostConstruct
    public void init() {
        x = 0;
        y = 0;
        r = 3;
        dbManager = DBManager.getInstance();
        pointsList = dbManager.getAll();
        if (pointsList == null) {
            pointsList = new ArrayList<>();
        }

        try {
            mBeanServer = ManagementFactory.getPlatformMBeanServer();
            String sessionId = session.getId();

            // Point Counter MBean
            pointCounter = new PointCounter();
            pointCounterName = new ObjectName("ru.ifmo.se.web3lab:type=PointCounter,name=SessionPointCounter-" + sessionId);
            if (!mBeanServer.isRegistered(pointCounterName)) {
                mBeanServer.registerMBean(pointCounter, pointCounterName);
            }

            // Click Interval MBean
            clickInterval = new ClickInterval();
            clickIntervalName = new ObjectName("ru.ifmo.se.web3lab:type=ClickInterval,name=SessionClickInterval-" + sessionId);
            if (!mBeanServer.isRegistered(clickIntervalName)) {
                mBeanServer.registerMBean(clickInterval, clickIntervalName);
            }

        } catch (MalformedObjectNameException | InstanceAlreadyExistsException |
                 MBeanRegistrationException | NotCompliantMBeanException e) {
        }
    }

    public void reset() {
        dbManager.clearAll();
        pointsList.clear();
    }

    public String calc() {
        long clickTimestamp = System.currentTimeMillis(); // Record time before calculation
        Point point = new Point(x, y, r);
        point.calc();
        pointsList.add(point);
        dbManager.send(point);

        // Update MBeans
        if (pointCounter != null) {
            pointCounter.incrementPoints(point.isInsideArea());
        }
        if (clickInterval != null) {
            clickInterval.recordClick(clickTimestamp);
        }

        return "update";
    }

    public void setX(double x) {
        this.x = ((Long) Math.round(x * 1000)).doubleValue() / 1000;
    }

    public void setY(double y) {
        this.y = ((Long) Math.round(y * 1000)).doubleValue() / 1000;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public ArrayList<Point> getBigList() {
        return pointsList;
    }

    public void setBigList(ArrayList<Point> bigList) {
        this.pointsList = bigList;
    }

    @PreDestroy
    public void cleanup() {
        // Unregister MBeans when session ends
        try {
            if (mBeanServer != null) {
                if (pointCounterName != null && mBeanServer.isRegistered(pointCounterName)) {
                    mBeanServer.unregisterMBean(pointCounterName);
                }
                if (clickIntervalName != null && mBeanServer.isRegistered(clickIntervalName)) {
                    mBeanServer.unregisterMBean(clickIntervalName);
                }
            }
        } catch (InstanceNotFoundException | MBeanRegistrationException e) {
        }
    }
}

