package com.coveros.training.expenses;

public class AlcoholResult {
    private final Double foodPrice;
    private final Double alcoholPrice;
    private final Double foodRatio;

    public AlcoholResult(Double foodPrice, Double alcoholPrice, Double foodRatio) {
        this.foodPrice = foodPrice;
        this.alcoholPrice = alcoholPrice;
        this.foodRatio = foodRatio;
    }

    public static AlcoholResult returnEmpty() {
        return new AlcoholResult(0d,0d,0d);
    }
}
