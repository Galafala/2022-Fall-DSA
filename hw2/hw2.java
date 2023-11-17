import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

class Percolation {
    private static class Node {
        private Point2D site;
        private Node next;
    }

    private static class LinkList {
        private int n;         // number of elements on queue
        private Node first;    // beginning of queue
        private Node last;     // end of queue
        boolean top;
        boolean bottom;

        public LinkList() {
            first = null;
            last = null;
            n = 0;
        }

        public boolean isEmpty(){
            return first == null;
        }

        public int size() {
            return n;
        }

        public void addNode(Node item) {
            Node oldLast = last;
            last = new Node();
            last.site = item.site;
            last.next = null;
            if (isEmpty()) first = last;
            else oldLast.next = last;
            n++;
        }

        public void combineLinkList(LinkList list) {
            Node oldLast = last;
            last = list.last;
            oldLast.next = list.first;
            n += list.n;
            if (list.top) top = true;
            if (list.bottom) bottom = true;
        }
    }

    private int w;
    private int check;
    WeightedQuickUnionUF full;
    LinkList[] allList;
    boolean percolateCheckPoint = false;
    Point2D[] percolateArray;

    public Percolation(int N) {
        w = N+2;
        full = new WeightedQuickUnionUF(w*w);
        allList = new LinkList[w*w];
        check = 0;
    }          // create N-by-N grid, with all sites blocked

    public void open(int x, int y) {
        int coordinateOnMap = (x+1)*w + (y+1);
        int[][] checkCoordinates = { {x-1, y}, {x+1, y}, {x, y+1}, {x, y-1} }; // store the up, down, left, right coordinates

        int[] keySet = new int[5];
        keySet[0] = full.find(coordinateOnMap);

        LinkList tempList = new LinkList();
        Node tempNode = new Node();
        tempNode.site = new Point2D(x, y);
        tempList.addNode(tempNode);
        allList[keySet[0]] = tempList;

        if (x==0) tempList.top = true;
        if (x==w-3) tempList.bottom = true;

        for (int i=0; i < checkCoordinates.length; i++) {
            boolean repeat = false;
            int xCoordinate = checkCoordinates[i][0];
            int yCoordinate = checkCoordinates[i][1];
            int checkCoordinate = w*(xCoordinate+1) + yCoordinate+1;

            if (!isOpen(xCoordinate,yCoordinate)) continue;

            int tempRoot = full.find(checkCoordinate);
            for (int key : keySet) {
                if (key == tempRoot) {
                    repeat = true;
                    break;
                }
            }
            if (!repeat) {
                keySet[i+1] = full.find(checkCoordinate);
            }
            full.union(checkCoordinate, coordinateOnMap);
        }

        int parent = full.find(coordinateOnMap);


        for (int key : keySet) { // merge the parents
            if (key == 0) continue;
            if (key == parent) continue;
            allList[parent].combineLinkList(allList[key]);
            if (allList[key].top) allList[parent].top = true;
            if (allList[key].bottom) allList[parent].bottom = true;
        }

        if (allList[parent].top && allList[parent].bottom) percolateCheckPoint = true;

        if (check != 1) {
            if (percolates() && percolateArray == null) {
                LinkList percolateList = allList[parent];
                percolateArray = new Point2D[percolateList.size()];
                int index = 0;
                while (percolateList.first != null) {
                    percolateArray[index] = percolateList.first.site;
                    percolateList.first = percolateList.first.next;
                    index++;
                }
                check++;
            }
        }
    }          // open site (row i, column j) if it is not open already

    public boolean isOpen(int i, int j) {
        int coordinateOnMap = w*(i+1)+j+1;
        return allList[coordinateOnMap] != null;
    }    // is site (row i, column j) open?

    public boolean isFull(int i, int j) {
        int coordinateOnMap = w*(i+1)+j+1;
        int parent = full.find(coordinateOnMap);
        return allList[parent].top;
    }    // is site (row i, column j) full?

    public boolean percolates() {
        return percolateCheckPoint;
    }           // does the system percolate?

    public Point2D[] PercolatedRegion() {
        if (check==1) Merge.sort(percolateArray);
        return percolateArray;
    }    // return the array of the sites of the percolated region in order (using Point2D default compare.to)
}