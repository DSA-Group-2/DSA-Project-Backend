package edu.upc.dsa.domain.repository.Products;


import edu.upc.dsa.domain.models.Order;
import edu.upc.dsa.domain.models.Product;
import edu.upc.dsa.domain.models.User;
import edu.upc.dsa.exceptions.EmptyQueueException;
import edu.upc.dsa.exceptions.UserNotFoundException;

import java.util.HashMap;
import java.util.List;

public interface ProductManager {

    void addUser(String nif, String name, String surname, String mail, String password, int dinero);
    void addProduct(String id, String name, double price, String image_url);
    void addOrder(Order order);

    boolean checkLogin(String name, String password);

    List<Product> getProductsByPriceDescendiente();
    List<Product> getProductsByPriceAscendiente();

    List<Product> getProductsBySalesDescendiente();
    List<Product> getProductsBySalesAscendiente();

    Order deliverOrder() throws UserNotFoundException, EmptyQueueException;

    Product getProduct(String id);
    User getUser(String number);

    int numOrders();
    int numUsers();
    int numProducts();

    void clear();

    void deleteProduct(String id);
    List<Product> findAllProducts();
    Product updateProduct(Product product);

    HashMap<String, User> findAllUsers();
    void deleteUser(String nif);
    User updateUser(User user);


}
