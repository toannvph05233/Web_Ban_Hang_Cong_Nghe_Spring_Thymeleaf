package com.bienvan.store.model;

import java.util.HashMap;
import java.util.Map;
import com.bienvan.store.model.discountStrategy.DiscountStr;
import com.bienvan.store.model.discountStrategy.NoDiscount;
import com.bienvan.store.model.discountStrategy.TenPercentDiscount;

public class Cart {
    private Map<Long, Product> products = new HashMap<>();

    public void addItem(Product item) {
        Long itemId = item.getId();
        if (products.containsKey(itemId)) {
            Product existingItem = products.get(itemId);
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
        } else {
            products.put(itemId, item);
        }
    }

    public void removeItem(Long itemId) {
        products.remove(itemId);
    }

    public void clearCart() {
        products.clear();
    }

    public Map<Long, Product> getCartItems() {
        return products;
    }

    public Product getItemById(Long itemId) {
        return products.get(itemId);
    }

    public double calculateTotalPrice() {
        double total = 0;
        for (Map.Entry<Long, Product> entry : products.entrySet()) {
            Product product = entry.getValue();
            double productTotal = product.getPrice() * product.getQuantity();
            total += productTotal;
        }
        return total;
    }

    public double calculateDiscountPrice(double total) {
        DiscountStr discount = new DiscountStr();
        int count = 0;
        double discountAmount = 0;
        for (Map.Entry<Long, Product> entry : products.entrySet()) {
            Product product = entry.getValue();
            count += product.getQuantity();
        }
        if(count == 1) {
            discount.setDiscountStrategy(new NoDiscount());
            discountAmount = discount.calculateDiscount(total);
        }
        if(count >1) {
            discount.setDiscountStrategy(new TenPercentDiscount());
            discountAmount = discount.calculateDiscount(total);
        }
        return discountAmount;
    }
}
