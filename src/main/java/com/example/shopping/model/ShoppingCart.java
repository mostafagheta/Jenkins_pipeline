package com.example.shopping.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShoppingCart {
    private final Map<String, CartItem> items = new LinkedHashMap<>();

    public void addProduct(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than zero");
        }
        CartItem existing = items.get(product.getId());
        if (existing == null) {
            items.put(product.getId(), new CartItem(product, quantity));
        } else {
            existing.increaseQuantity(quantity);
        }
    }

    public void removeProduct(String productId) {
        items.remove(productId);
    }

    public Collection<CartItem> getItems() {
        return Collections.unmodifiableCollection(items.values());
    }

    public BigDecimal getTotal() {
        return items.values().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void clear() {
        items.clear();
    }
}
