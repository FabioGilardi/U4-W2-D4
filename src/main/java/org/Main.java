package org;

import com.github.javafaker.Faker;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

//      FAKER
        Faker faker = new Faker();

//        SUPPLIERS
        Supplier<Product> productSupplier = () -> {
            Random rndm = new Random();
            long randomId = rndm.nextLong(10000, 100000);
            double randomPrice = rndm.nextDouble(1.00, 500.00);
            int randomSelectorCategory = rndm.nextInt(0, 4);
            String randomName = faker.leagueOfLegends().rank();
            List<String> categoryList = new ArrayList<>();
            categoryList.add("Baby");
            categoryList.add("Books");
            categoryList.add("Boys");
            categoryList.add("Gaming");
            return new Product(randomId, randomName, categoryList.get(randomSelectorCategory), randomPrice);
        };

        Supplier<Customer> customerSupplier = () -> {
            Random rndm = new Random();
            long randomId = rndm.nextLong(1, 2);
            int randomTier = rndm.nextInt(1, 2);
            int randomName = rndm.nextInt(0, 4);
            List<String> nameList = new ArrayList<>();
            nameList.add("Fabio");
            nameList.add("Bobby");
            nameList.add("Jhonny");
            nameList.add("Jimmy");
            return new Customer(randomId, nameList.get(randomName), randomTier);
        };

        Supplier<Order> orderSupplier = () -> {
            Random rndm = new Random();
            long randomId = rndm.nextLong(10000, 100000);
            int randomProducts = rndm.nextInt(2, 5);
            int randomStatus = rndm.nextInt(0, 3);
            int randomDate = rndm.nextInt(0, 2);
            List<LocalDate> dateList = new ArrayList<>();
            dateList.add(LocalDate.now());
            dateList.add(LocalDate.parse("2024-02-04"));
            List<String> status = new ArrayList<>();
            status.add("Shipped");
            status.add("Processed");
            status.add("Not taken in charge yet");
            Customer randomCustomer = customerSupplier.get();
            List<Product> randomProductsList = new ArrayList<>();
            for (int i = 0; i < randomProducts; i++) {
                randomProductsList.add(productSupplier.get());
            }
            return new Order(randomId, status.get(randomStatus), dateList.get(randomDate), randomProductsList, randomCustomer);
        };

//        ESERCIZIO 1
        List<Order> orderList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            orderList.add(orderSupplier.get());
        }
        Map<Customer, List<Order>> ordersByCustomer = orderList
                .stream()
                .collect(Collectors.groupingBy(Order::getCustomer));
        ordersByCustomer.forEach((customer, orders) -> System.out.println("Customer " + customer.getName() + " ordered this :" + orders));

//        ESERCIZIO 2
        Map<Customer, Double> customerTotalSpent = orderList.stream().collect(Collectors.groupingBy(Order::getCustomer, Collectors.summingDouble(order -> order.getProducts().stream().mapToDouble(Product::getPrice).sum())));

        customerTotalSpent.forEach((customer, aDouble) -> System.out.println("Customer " + customer.getName() + " spent " + aDouble + "€"));

//        ESERCIZIO 3
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            productList.add(productSupplier.get());
        }

        List<Product> mostExpensive5Products = productList.stream().sorted(Comparator.comparingDouble(Product::getPrice).reversed()).limit(5).toList();
        mostExpensive5Products.forEach(product -> System.out.println(product.getName() + " " + product.getPrice()));

//        ESERCIZIO 4
        double averageOrdersPrice = orderList.stream().mapToDouble(order -> order.getProducts().stream().mapToDouble(Product::getPrice).sum()).average().getAsDouble();

        System.out.println("The average price of each order is: " + averageOrdersPrice);

//        ESERCIZIO 5
        Map<String, Double> totalByCategory = productList.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.summingDouble(Product::getPrice)));

        totalByCategory.forEach((s, aDouble) -> System.out.println("The category " + s + " has a total cost of " + aDouble));

//        ESERCIZIO 6
        salvaProdottiSuDisco(productList);

//        ESERCIZIO 7

    }

    public static void salvaProdottiSuDisco(List<Product> list) {
        String productString = list.stream().map(product -> product.getName() + "@" + product.getCategory() + "@" + product.getPrice()).collect(Collectors.joining("#"));
        System.out.println(productString);

        File file = new File("src/Esercizio6.txt");
        try {
            FileUtils.writeStringToFile(file, productString);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
