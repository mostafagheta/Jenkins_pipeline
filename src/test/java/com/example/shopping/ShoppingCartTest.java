package com.example.shopping;

import com.example.shopping.model.Product;
import com.example.shopping.model.ShoppingCart;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShoppingCartTest {

    @Test
    void totalIsCalculatedCorrectly() {
        ShoppingCart cart = new ShoppingCart();
        Product apple = new Product("P1", "Apple", new BigDecimal("0.50"));
        Product milk = new Product("P2", "Milk", new BigDecimal("1.20"));

        cart.addProduct(apple, 3);
        cart.addProduct(milk, 2);

        assertEquals(new BigDecimal("3.90"), cart.getTotal());
    }

    @Test
    void addingSameProductIncrementsQuantity() {
        ShoppingCart cart = new ShoppingCart();
        Product apple = new Product("P1", "Apple", new BigDecimal("0.50"));

        cart.addProduct(apple, 1);
        cart.addProduct(apple, 2);

        assertEquals(new BigDecimal("1.50"), cart.getTotal());
    }
}
