package com.example.shopping;

import com.example.shopping.model.CartItem;
import com.example.shopping.model.Product;
import com.example.shopping.model.ShoppingCart;

import java.math.BigDecimal;

public class ShoppingApp {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();

        Product apple = new Product("P1", "Apple", new BigDecimal("0.50"));
        Product milk = new Product("P2", "Milk", new BigDecimal("1.20"));

        cart.addProduct(apple, 3);
        cart.addProduct(milk, 2);

        System.out.println("Items in cart:");
        for (CartItem item : cart.getItems()) {
            System.out.printf("- %s x%d = %s%n",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getTotalPrice());
        }

        System.out.println("----------------------");
        System.out.println("Total: " + cart.getTotal());
    }
}
