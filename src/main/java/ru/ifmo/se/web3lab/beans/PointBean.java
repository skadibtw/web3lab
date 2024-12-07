package ru.ifmo.se.web3lab.beans;

import java.io.Serializable;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import ru.ifmo.se.web3lab.managers.DBManager;
import ru.ifmo.se.web3lab.models.Point;
import java.util.ArrayList;


@Named("pointBean")
@SessionScoped
public class PointBean implements Serializable {
    private double x;
    private double y;
    private double r;
    private ArrayList<Point> pointsList;
    private DBManager dbManager;

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
    }

    public void reset() {
        dbManager.clearAll();
        pointsList.clear();
    }

    public String calc() {
        Point point = new Point(x, y, r);
        point.calc();
        pointsList.add(point);
        dbManager.send(point);
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
}

