package com.coveros.training;

import java.util.Stack;

public class Ackermann {

    /**
     * Ackerman function.  See https://en.wikipedia.org/wiki/Ackermann%27s_formula
     *
     * This less recursive version found at https://stackoverflow.com/a/45412879/713809
     */
    public static int calculate(int m, int n){
        Stack<Integer> s = new Stack<>();
        s.add(m);
        while(!s.isEmpty()){
            m=s.pop();
            if(m==0) { n+=m+1; }
            else if(n==0)
            {
                n += 1;
                s.add(--m);
            }
            else{
                s.add(--m);
                s.add(++m);
                n--;
            }
        }
        return n;
    }
}
