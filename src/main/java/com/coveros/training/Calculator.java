package com.coveros.training;

import org.apache.commons.lang3.tuple.Pair;

/**
 * A simple class to do simple things.
 */
public class Calculator {

    private final Baz baz;

    public Calculator() {
        this.baz = new Baz();
    }

    public Calculator(Baz baz) {
        this.baz = baz;
    }


    /**
     * Simply add two integers
     */
    public static int add(int a, int b) {
        return a + b;
    }

    /**
     * Simply add two doubles.
     */
    public static double add(double a, double b) {
        return a + b;
    }

    /**
     * This method converts integers 0 to 10 into their
     * ordinals.  For example: 0 -> "zero"
     */
    public static String toStringZeroToTen(int i) {
        switch (i) {
            case 0: return "zero";
            case 1: return "one";
            case 2: return "two";
            case 3: return "three";
            case 4: return "four";
            case 5: return "five";
            case 6: return "six";
            case 7: return "seven";
            case 8: return "eight";
            case 9: return "nine";
            case 10: return "ten";
            default: return "dunno";
        }
    }

    public static int calculateAndMore(int a, int b, Foo foo, Bar bar) {
        int c = foo.doComplexThing(a);
        int d = bar.doOtherComplexThing(c);
        return a + b + c + d;
    }

    public int calculateAndMorePart2(int a) {
        int b = baz.doThirdPartyThing(a);
        return a + b;
    }

    /**
     * Add two pairs.
     */
    public static Pair<Integer, Integer> add(Pair<Integer, Integer> pair1, Pair<Integer, Integer> pair2) {
        int newLeftValue = pair1.getLeft() + pair2.getLeft();
        int newRightValue = pair1.getRight() + pair2.getRight();
        return Pair.of(newLeftValue, newRightValue);
    }

    public static class Foo {
        public int doComplexThing(int a) {
            return a + 1;
        }
    }

    public static class Bar {
        public int doOtherComplexThing(int c) {
            return c - 1;
        }
    }

    public class Baz {

        public int doThirdPartyThing(int a) {
            return 42;
        }
    }
}
