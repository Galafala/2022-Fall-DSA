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
            int left = i*2;
            int right= 2*length-2*i-1;

           if (rightSide.empty()) {
               ans[right] = length-1;
               rightSide.push(rightChallenger);
           }
           else {
               while (true) {
                   if (rightSide.peek().Strength < rightChallenger.Strength) rightSide.pop();
                   else {
                       ans[right] = rightSide.peek().Index-1;
                       rightSide.push(rightChallenger);
                       break;
                   }
                   if (!rightSide.empty()) continue;
                   ans[right] = length-1;
                   rightSide.push(rightChallenger);
                   break;
               }
           }
           ans[right] = Math.min(ans[right], rightChallenger.Index+rightChallenger.Range);


           if (leftSide.empty()) {
               ans[left] = 0;
               leftSide.push(leftChallenger);
           }
           else {
               while (true) {
                   if (leftSide.peek().Strength < leftChallenger.Strength) leftSide.pop();
                   else {
                       ans[left] = leftSide.peek().Index+1;
                       leftSide.push(leftChallenger);
                       break;
                   }
                   if (!leftSide.empty()) continue;
                   ans[left] = 0;
                   leftSide.push(leftChallenger);
                   break;
               }
           }
           ans[left] = Math.max(ans[left], leftChallenger.Index-leftChallenger.Range);
        
        }

        return ans;
    }
}