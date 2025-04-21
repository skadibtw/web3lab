package ru.ifmo.se;

import org.junit.Assert;
import org.junit.Test;
import ru.ifmo.se.web3lab.models.Point;

public class PointTest {

    @Test
    public void testInsideArea() {
        Point p = new Point(0, 0, 3); // центр координат
        p.calc();
        Assert.assertTrue(p.isInsideArea());
    }

    @Test
    public void testOutsideArea() {
        Point p = new Point(5, 5, 3); // вне границ
        p.calc();
        Assert.assertFalse(p.isInsideArea());
    }

    @Test
    public void testExecutionTimeAndTimestamp() {
        Point p = new Point(1, 1, 2);
        p.calc();
        Assert.assertNotNull(p.getTimestamp());
        Assert.assertTrue(p.getExecutionTime() > 0);
    }
}

