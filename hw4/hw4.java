import edu.princeton.cs.algs4.GrahamScan;
import edu.princeton.cs.algs4.Point2D;

import java.util.List;
import java.util.ArrayList;
import java.lang.Math;


class Airport {
    private double crossLength(Point2D v1, Point2D v2) {
        return v1.x()*v2.y()-v2.x()*v1.y();
    }
    
    private double sumDistance(Point2D point, Point2D postPoint, Point2D[] points) {
        Point2D dirVector = new Point2D(point.x()-postPoint.x(), point.y()-postPoint.y());
        double dirVecLength = Math.pow(Math.pow(dirVector.x(),2)+Math.pow(dirVector.y(),2), 0.5);
        double distance = 0;

        for (int j=0; j < points.length; j++) {
            Point2D vector = new Point2D(points[j].x()-point.x(), points[j].y()-point.y());
            distance += Math.abs(crossLength(vector, dirVector))/dirVecLength;
        }

        return distance;
    }
    
    // Output the smallest average distance with optimal selection of airport location.
    public double airport(List<int[]> houses) {
        double avgDistance = Double.POSITIVE_INFINITY;
        int numPoint = houses.size();
        Point2D[] points = new Point2D[numPoint];

        for (int i = 0; i < numPoint; i++) {
            Point2D pointNow = new Point2D(houses.get(i)[0], houses.get(i)[1]);
            points[i] = pointNow;
        }

        GrahamScan graham = new GrahamScan(points);

        ArrayList<Point2D> transitionPoints = new ArrayList<>();

        for (Point2D p : graham.hull())
            transitionPoints.add(p);

        Object[] convexPoints = transitionPoints.toArray();

        for (int i = 0; i < convexPoints.length; i++) {
            double distance;
            if (i+1 != convexPoints.length) distance = sumDistance((Point2D) convexPoints[i], (Point2D) convexPoints[i + 1], points);
            else distance = sumDistance((Point2D) convexPoints[i], (Point2D) convexPoints[0], points);
            if (avgDistance > distance/numPoint) avgDistance = distance/numPoint;
        }

        return avgDistance;
    }
}
