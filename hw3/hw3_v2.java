import java.util.Stack;

class Warrior {
    int Strength;
    int Range;
    int Index;
    
    Warrior(int str, int rng, int i){
        Strength=str;
        Range=rng;
        Index=i;
    }
}

class Warriors {
    public int[] warriors(int[] strengths, int[] ranges) {
        int length = ranges.length;
        int[] ans = new int[length*2];
        Stack<Warrior> leftSide = new Stack<>();
        Stack<Warrior> rightSide= new Stack<>();

        for (int i=0; i < length; i++) {
            Warrior leftChallenger = new Warrior(strengths[i], ranges[i], i);
            Warrior rightChallenger= new Warrior(strengths[length-i-1], ranges[length-i-1], length-i-1);
            int left = 2*i;
            int right= 2*(length-i)-1;

            while (!rightSide.empty() && rightSide.peek().Strength < rightChallenger.Strength) rightSide.pop();

            if (rightSide.empty()) ans[right] = length-1;
            else ans[right] = rightSide.peek().Index-1;

            rightSide.push(rightChallenger);
            ans[right] = Math.min(ans[right], rightChallenger.Index+rightChallenger.Range);


            while (!leftSide.empty() && leftSide.peek().Strength < leftChallenger.Strength) leftSide.pop();

            if (leftSide.empty()) ans[left] = 0;
            else ans[left] = leftSide.peek().Index+1;

            leftSide.push(leftChallenger);
            ans[left] = Math.max(ans[left], leftChallenger.Index-leftChallenger.Range);
        }

        return ans;
    }
}