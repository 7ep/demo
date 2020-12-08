package com.coveros.training.mathematics;

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
     * ordinals.  For example: 0 returns "zero"
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

    /**
     * Add two pairs.
     */
    public static Pair<Integer, Integer> add(Pair<Integer, Integer> pair1, Pair<Integer, Integer> pair2) {
        int newLeftValue = pair1.getLeft() + pair2.getLeft();
        int newRightValue = pair1.getRight() + pair2.getRight();
        return Pair.of(newLeftValue, newRightValue);
    }

    /**
     * Used for teaching
     * testing stubs.
     */
    public static int calculateAndMore(int a, int b, iFoo foo, iBar bar) {
        int c = foo.doComplexThing(a);
        int d = bar.doOtherComplexThing(c);
        return a + b + c + d;
    }

    /**
     * Used for teaching
     * testing stubs.
     */
    public int calculateAndMorePart2(int a) {
        int b = baz.doThirdPartyThing(a);
        return a + b;
    }

    /**
     * Used for teaching
     * testing mocks.
     */
    public void calculateAndMorePart3(int a) {
        baz.doThirdPartyThing(a);
    }

    public interface iFoo {
        int doComplexThing(int a);
    }

    /**
     * An artificial class needed as a dependency
     */
    public static class Foo {
        public int doComplexThing(int a) {
            return a + 1;
        }
    }


    public interface iBar {
        int doOtherComplexThing(int c);
    }

    /**
     * An artificial class needed as a dependency
     */
    public static class Bar {
        public int doOtherComplexThing(int c) {
            return c - 1;
        }
    }

    /**
     * An artificial class needed as a dependency
     */
    public class Baz {

        public int doThirdPartyThing(int a) {
            return 42;
        }
    }
}
