class LongJump {
    BST<Integer> players;

    public LongJump(int[] playerList){
        players = new BST<>(playerList);
    }

    // Add new player in the competition with different distance
    public void addPlayer(int distance) {
        players.put(distance);
    }
    // return the winners total distance in range[from, to]

    public int winnerDistances(int from, int to) {
        return players.findAns(from, to);
    }
}

class BST<Integer> {
    private Node root;             // root of BST
    private Node newRoot;

    private int maxJunk;
    private int minJunk;

    private class Node {
        private int key;           // sorted by key
        private Node left, right;  // left and right subtrees
        private int size;          // number of nodes in subtree
        public Node(int key, int size) {
            this.key = key;
            this.size = size;
        }
    }

    public BST(int[] elements) {
        for (int element: elements) {
            put(element);
        }
    }

    public int size() {
        return size(newRoot);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }

    private void findRoot(int from, int to) {
        Node node = root;
        while (node != null) {
            if (from > node.key) {
                node = node.right;
            }
            else if (to < node.key) {
                node = node.left;
            }
            else {
                newRoot = node;
                break;
            }
        }
    }

    public int findAns(int from, int to) {
        maxJunk = 0;
        minJunk = 0;
        findRoot(from, to);
        floor(newRoot, from);
        ceiling(newRoot, to);
        return size()-maxJunk-minJunk;
    }

    public void put(int key) {
        root = put(root, key);
    }

    private Node put(Node x, int key) {
        if (x == null) return new Node(key, key);
        int cmp = key-x.key;
        if      (cmp < 0) x.left  = put(x.left,  key);
        else if (cmp > 0) x.right = put(x.right, key);
        else              x.key   = key;
        x.size = x.key + size(x.left) + size(x.right);
        return x;
    }

    private Node floor(Node x, int key) { //subtract left
        if (x == null) return null;
        int cmp = key-x.key;
        if (cmp == 0) { // include self
            minJunk += size(x.left);
            return x;
        }
        if (cmp <  0) return floor(x.left, key);
        Node t = floor(x.right, key);
        minJunk += (x.size-size(x.right));
        if (t != null) return t;
        else return x;
    }

    private Node ceiling(Node x, int key) { //subtract right
        if (x == null) {
            return null;
        }
        int cmp = key - x.key;
        if (cmp == 0) {
            maxJunk += size(x.right);
            return x;
        }
        if (cmp < 0) {
            Node t = ceiling(x.left, key);
            maxJunk += (x.size-size(x.left));
            if (t != null) return t;
            else return x;
        }
        return ceiling(x.right, key);
    }
}