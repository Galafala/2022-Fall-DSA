import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

class King{
    int Strength;
    int Range;
    int Index;
    boolean isKing;

    King(int str,int rng, int i){
        Strength=str;
        Range=rng;
        Index=i;
        isKing=true;
    }
}

class Kings {
    ArrayList<King> realKings = new ArrayList<King>();
    public Kings(int[] strength, int[] range){
        int length = range.length;
        King[] kings = new King[length];
        Stack<King> leftStack = new Stack<King>();
        Stack<King> rightStack = new Stack<King>();

        for (int i=0; i < length; i++) {
            int left = i;
            King leftChallenger = new King(strength[left], range[left], left);

            if (kings[left]==null) {
                kings[left] = leftChallenger;
            }

            int leftChallengerRange = Math.max(0, leftChallenger.Index-leftChallenger.Range);
            while(!leftStack.empty() && leftStack.peek().Strength < leftChallenger.Strength) {
                if (leftChallengerRange <= leftStack.peek().Index) {
                    kings[leftStack.peek().Index].isKing = false;
                }
                leftStack.pop();
            }
            leftStack.push(leftChallenger);


            int right = length-i-1;
            King rightChallenger = new King(strength[right], range[right], right);

            if (kings[right]==null) {
                kings[right] = rightChallenger;
            }

            int rightChallengerRange = Math.min(length-1, rightChallenger.Index+rightChallenger.Range);
            while(!rightStack.empty() && rightStack.peek().Strength < rightChallenger.Strength) {
                if (rightChallengerRange >= rightStack.peek().Index) {
                    kings[rightStack.peek().Index].isKing = false;
                }
                rightStack.pop();
            }
            rightStack.push(rightChallenger);
        }

        for (int i=0; i < length; i++) {
            if (kings[i].isKing) {
                realKings.add(kings[i]);
            }
        }
    }

    public int[] topKKings(int N){
        realKings.sort(new Comparator<King>() {
            @Override
            public int compare(King o1, King o2) {
                return o2.Strength - o1.Strength;
            }
        });

        int[] ans;
        if (N >= realKings.size()) {
            ans = new int[realKings.size()];
        }
        else {
            ans = new int[N];
        }

        for (int i=0; i < ans.length; i++) {
            ans[i] = realKings.get(i).Index;
        }

        return ans;
    }
}

