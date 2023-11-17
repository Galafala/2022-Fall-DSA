import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.*;

class BoardGame {
    int[] board; // store the stones
    int width, height;
    WeightedQuickUnionUF connected;
    HashMap<Integer, Set<Integer>> group = new HashMap<>(); // store parents as key and children with the same parent as value to lower the time of finding the connected stone.

    public BoardGame(int h, int w) {
        h += 2;
        w += 2;
        width = h;
        height =  w;
        int[] temp = new int[h*w]; // create board with padding
        
        for (int i = 0; i < h*w; i++) {
            temp[i] = -1;
        }

        board = temp;
        connected = new WeightedQuickUnionUF((h)*(w));
    }// create a board of size h*w

    private void checkConnect(int x, int y) {
        Set<Integer> keySet = new HashSet();
        int coordinateOnMap = height*x+y;
        int[] checkCoordinates = { coordinateOnMap-height, coordinateOnMap+height, coordinateOnMap-1, coordinateOnMap+1 }; // store the up, down, left, right coordinates
        Set<Integer> temp = new HashSet<Integer>();
        int parent = connected.find(coordinateOnMap); // old parent

        temp.add(coordinateOnMap);

        for (int i = 0; i < checkCoordinates.length; i++) {
            if (board[coordinateOnMap] != board[checkCoordinates[i]]) continue;
            keySet.add(connected.find((checkCoordinates[i])));
            connected.union(checkCoordinates[i], coordinateOnMap);
            temp.add(checkCoordinates[i]);
        }

        parent = connected.find(coordinateOnMap); // new parent
        if (!group.containsKey(parent)) group.put(parent, temp);
        else {
            group.get(parent).addAll(temp);
            Object[] keySetArray = keySet.toArray();
            for (int i = 0; i < keySetArray.length; i++) { // merge the parents
                int key = (int)keySetArray[i];
                if (key==0 || key==parent) continue;
                group.get(parent).addAll(group.get(key)); // merge the old parent and its children into new parent's children
                group.remove(key); // remove the not existed parent
            }
        }
    }
    
    public void putStone(int[] x, int[] y, char stoneType) {
        int stone = stoneType=='O' ? 0 : 5; //transfer O to 0, X to 5

        for (int i = 0; i < x.length; i++) {
            int xCoordinate = x[i]+1;
            int yCoordinate = y[i]+1;
            int coordinateOnMap = height*xCoordinate+yCoordinate;

            if (board[coordinateOnMap] != -1) continue;
            board[coordinateOnMap] = stone;
            checkConnect(xCoordinate, yCoordinate);
        }
    }// put stones of the specified type on the board according to the coordinates
    
    private boolean checkSurrounded(int coordinateOnMap) {
        int[] checkCoordinates = { coordinateOnMap-height, coordinateOnMap+height, coordinateOnMap-1, coordinateOnMap+1 };

        for (int coordinate : checkCoordinates) if (board[coordinate] == -1) return false;

        return true;
    }

    public boolean surrounded(int x, int y) {
        x += 1;
        y += 1;
        int coordinateOnMap = height*x + y;
        int parent = connected.find(coordinateOnMap);

        Object[] temp = group.get(parent).toArray();
        for (Object o : temp) {
            if (!checkSurrounded((Integer) o)) return false;
        }

        return true;
    } // Answer if the stone and its connected stones are surrounded by another type of stones

    public int countConnectedRegions() {
        return group.keySet().size();
    }// Get the number of connected regions in the board, including both types of the stones

}