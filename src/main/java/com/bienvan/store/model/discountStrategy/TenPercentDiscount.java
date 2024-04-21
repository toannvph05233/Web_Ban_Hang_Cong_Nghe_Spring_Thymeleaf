package com.bienvan.store.model.discountStrategy;

public class TenPercentDiscount implements DiscountStrategy {
    @Override
    public double applyDiscount(double amount) {
        return amount * 0.1; // 10% discount
    }
}