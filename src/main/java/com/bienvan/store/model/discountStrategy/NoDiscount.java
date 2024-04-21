package com.bienvan.store.model.discountStrategy;

public class NoDiscount implements DiscountStrategy {
    @Override
    public double applyDiscount(double amount) {
        return 0;
    }
}
