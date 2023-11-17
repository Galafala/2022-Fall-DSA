import edu.princeton.cs.algs4.Point2D;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class Point implements Comparable<Point>{
    Point2D point;
    int centre;

    Point(Point2D point, int centre) {
        this.point = point;
        this.centre = centre;
    }

    public int compareTo(Point that) {
        int cmp = (int) (this.point.x()-that.point.x());
        if (cmp != 0) return cmp;
        return (int) (this.point.y()-that.point.y());
    }
}

class Cluster {
    public List<double[]> cluster(List<int[]> points, int cluster_num) {
        ArrayList<Point> p = new ArrayList<>();
        for(int[] i: points) {
            p.add(new Point(new Point2D(i[0], i[1]), 1));
        }


        while (p.size() > cluster_num) {
            double d = 5e19;
            Point[] pair = new Point[2];

            for (int i = 0; i < p.size(); i++) {
                for (int j = i+1; j < p.size(); j++) {
                    double dist = p.get(j).point.distanceTo(p.get(i).point);
                    if (d > dist) {
                        d = dist;
                        pair[0] = p.get(i);
                        pair[1] = p.get(j);
                    }
                }
            }

            double newC = (pair[0].centre+pair[1].centre);
            double newX = (pair[0].point.x()*pair[0].centre + pair[1].point.x()*pair[1].centre)/newC;
            double newY = (pair[0].point.y()*pair[0].centre + pair[1].point.y()*pair[1].centre)/newC;

            Point newPoint = new Point(new Point2D(newX, newY), (int) newC);
            p.add(newPoint);
            p.remove(pair[0]);
            p.remove(pair[1]);
        }

        ArrayList<double[]> ans = new ArrayList<>();
        for (Point point : p) {
            double[] peko = new double[2];
            peko[0] = point.point.x();
            peko[1] = point.point.y();
            ans.add(peko);
        }

        Collections.sort(ans, (point1, point2) -> {
            double cmp = point1[0]-point2[0];

            if (cmp != 0) {
                if (cmp > 0) cmp = 1;
                else cmp = -1;
                return (int) cmp;
            }

            cmp = (point1[1]-point2[1]);

            if (cmp > 0) cmp = 1;
            else if (cmp < 0) cmp = -1;
            return (int) cmp;
        });
        return ans;
    }
}


