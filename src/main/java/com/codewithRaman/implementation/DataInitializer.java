package com.codeWithRaman.implementation;

import com.codeWithRaman.implementation.model.Beverage;
import com.codeWithRaman.implementation.model.Order;
import com.codeWithRaman.implementation.model.OrderItem;
import com.codeWithRaman.implementation.model.Bottle;  // Import Bottle
import com.codeWithRaman.implementation.model.Crate; //Import Crate
import com.codeWithRaman.implementation.service.BeverageService;
import com.codeWithRaman.implementation.service.OrderItemService;
import com.codeWithRaman.implementation.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BeverageService beverageService;

    @Autowired
    private OrderItemService orderItemService;

    @Bean
    public CommandLineRunner demoData() {
        return (args) -> {
            // Create and save demo beverages (using Bottle as an example)
            // Non-Alcoholic Beverages
            Bottle coke = new Bottle();
            coke.setName("Coke");
            coke.setPrice(1.50);
            coke.setVolume(1.5);
            coke.setAlcoholic(false);
            coke.setInStock(100);
            coke.setSupplier("Coca-Cola");
            coke.setBeveragePic("https://i.imgur.com/T8reNs4.jpeg");
            beverageService.saveBeverage(coke);

            Bottle pepsi = new Bottle();
            pepsi.setName("Pepsi");
            pepsi.setPrice(1.50);
            pepsi.setVolume(2.5);
            pepsi.setAlcoholic(false);
            pepsi.setInStock(50);
            pepsi.setSupplier("PepsiCo");
            pepsi.setBeveragePic("https://i.imgur.com/OwnV0BP.png");
            beverageService.saveBeverage(pepsi);

            Bottle sprite = new Bottle();
            sprite.setName("Sprite");
            sprite.setPrice(1.20);
            sprite.setVolume(1.5);
            sprite.setAlcoholic(false);
            sprite.setInStock(70);
            sprite.setSupplier("Coca-Cola");
            sprite.setBeveragePic("https://i.imgur.com/DLHVWwB.png");
            beverageService.saveBeverage(sprite);

            Bottle fanta = new Bottle();
            fanta.setName("Fanta");
            fanta.setPrice(1.20);
            fanta.setVolume(1.5);
            fanta.setAlcoholic(false);
            fanta.setInStock(80);
            fanta.setSupplier("Coca-Cola");
            fanta.setBeveragePic("https://i.imgur.com/8VhBjeg.jpeg");
            beverageService.saveBeverage(fanta);

            // Alcoholic Beverages
            Bottle beer = new Bottle();
            beer.setName("Beer");
            beer.setPrice(2.50);
            beer.setVolume(0.5);
            beer.setAlcoholic(true);
            beer.setInStock(120);
            beer.setSupplier("Heineken");
            beer.setBeveragePic("https://i.imgur.com/jwfUkeQ.jpeg");
            beverageService.saveBeverage(beer);

            Bottle redWine = new Bottle();
            redWine.setName("Red Wine");
            redWine.setPrice(12.00);
            redWine.setVolume(0.75);
            redWine.setAlcoholic(true);
            redWine.setInStock(30);
            redWine.setSupplier("Bordeaux");
            redWine.setBeveragePic("https://i.imgur.com/WYmNrFl.jpeg");
            beverageService.saveBeverage(redWine);

            Bottle whiskey = new Bottle();
            whiskey.setName("Whiskey");
            whiskey.setPrice(45.00);
            whiskey.setVolume(1.0);
            whiskey.setAlcoholic(true);
            whiskey.setInStock(20);
            whiskey.setSupplier("Jameson");
            whiskey.setBeveragePic("https://i.imgur.com/JjC8g9f.jpeg");
            beverageService.saveBeverage(whiskey);

            Bottle vodka = new Bottle();
            vodka.setName("Vodka");
            vodka.setPrice(25.00);
            vodka.setVolume(0.75);
            vodka.setAlcoholic(true);
            vodka.setInStock(50);
            vodka.setSupplier("Absolut");
            vodka.setBeveragePic("https://i.imgur.com/IPUFffR.jpeg");
            beverageService.saveBeverage(vodka);

            /*
            // Create an initial order with some items
            Order order = new Order();
            order.setPrice(3.00); // Total price will update dynamically later
            orderService.saveOrder(order);
            */

            /*
            // Create order items and associate them with the order
            OrderItem item1 = new OrderItem();
            item1.setBeverage(coke);
            item1.setPrice(coke.getPrice());
            item1.setOrder(order);
            orderItemService.saveOrderItem(item1);

            OrderItem item2 = new OrderItem();
            item2.setBeverage(pepsi);
            item2.setPrice(pepsi.getPrice());
            item2.setOrder(order);
            orderItemService.saveOrderItem(item2);
            */

            // Example crates
            Crate cokeCrate = new Crate();
            cokeCrate.setName("Coke Crate");
            cokeCrate.setPrice(12.00);
            cokeCrate.setNoOfBottles(6);
            cokeCrate.setCratesInStock(20);
            cokeCrate.setBeveragePic("https://i.imgur.com/T8reNs4.jpeg");
            beverageService.saveBeverage(cokeCrate);

            Crate beerCrate = new Crate();
            beerCrate.setName("Beer Crate");
            beerCrate.setPrice(36.00);
            beerCrate.setNoOfBottles(12);
            beerCrate.setCratesInStock(15);
            beerCrate.setBeveragePic("https://i.imgur.com/jwfUkeQ.jpeg");
            beverageService.saveBeverage(beerCrate);
        };
    }
}
