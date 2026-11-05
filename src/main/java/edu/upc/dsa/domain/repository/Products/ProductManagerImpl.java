package edu.upc.dsa.domain.repository.Products;

import edu.upc.dsa.domain.models.Order;
import edu.upc.dsa.domain.models.Product;
import edu.upc.dsa.domain.models.User;

import edu.upc.dsa.domain.models.*;
import edu.upc.dsa.exceptions.EmptyQueueException;
import edu.upc.dsa.exceptions.UserNotFoundException;
import org.apache.log4j.Logger;


import java.util.*;

public class ProductManagerImpl implements ProductManager {

    private static ProductManager instance;

    private List<Product> productList;
    private Queue<Order> orderQueue;
    private HashMap<String, User> users;

    final static Logger logger = Logger.getLogger(ProductManagerImpl.class);



    private ProductManagerImpl()
    {
        productList = new ArrayList<>();
        orderQueue = new LinkedList<>();
        users = new HashMap<>();
    }

    public static ProductManager getInstance() //Implementació Singleton
    {
        if (instance == null) instance = new ProductManagerImpl();
        return instance;
    }

    @Override
    public void clear() {
        this.productList.clear();
        this.orderQueue.clear();
        this.users.clear();
    }


    @Override
    public void addProduct(String id, String name, double price)
    {

        productList.add(new Product(id, name, price));
    }

    @Override
    public List<Product> getProductsByPriceDescendiente()
    {

        productList.sort(Comparator.comparing(Product::getPrice).reversed());

        return productList;
    }

    @Override
    public List<Product> getProductsByPriceAscendiente()
    {
        List<Product> copia = new ArrayList<>(productList);
        copia.sort(Comparator.comparing(Product::getPrice));

        return copia;
    }

    @Override
    public List<Product> getProductsBySalesDescendiente()
    {
        List<Product> copia = new ArrayList<>(productList);
        copia.sort(Comparator.comparing(Product::getSales).reversed());

        return  copia;

    }

    @Override
    public List<Product> getProductsBySalesAscendiente()
    {
        List<Product> copia = new ArrayList<>(productList);
        copia.sort(Comparator.comparing(Product::getSales));

        return  copia;

    }

    @Override
    public void addOrder(Order order)
    {
        logger.info("addOrder(order=" + order + ")");

        if (order == null)
        {
            logger.fatal("addOrder() -> el pedido recibido es null, no se añade");
            return;
        }

        orderQueue.add(order);
        logger.info("addOrder() -> pedido en cola, pedidos pendientes: " + orderQueue.size());

    }

    @Override
    public int numOrders()
    {
        if(orderQueue.isEmpty())
            return 0;

        return orderQueue.size();

    }

    @Override
    public int numUsers()
    {
        if (users.isEmpty())
            return 0;
        else
            return users.size();
    }

    @Override
    public Order deliverOrder() throws UserNotFoundException, EmptyQueueException {

        Order order = orderQueue.poll();

        if(order != null)
        {
            User user = users.get(order.getDni());

            if(user != null)
            {
                order.setUser(user);
                user.addOrder(order);

                HashMap<String, Integer> listaProductosMap = order.getProductList();

                for (String idProduct : order.getProductList().keySet())
                {
                    Integer cantidad = order.getProductList().get(idProduct);
                    Product product = getProduct(idProduct);

                    if (product == null)
                    {
                        logger.error("deliverOrder() -> el producto " + idProduct + " ya no existe");
                        continue;
                    }
                    if (cantidad == null || cantidad <= 0)
                    {
                        logger.error("deliverOrder() -> cantidad no válida (" + cantidad + ") para el producto " + idProduct + ", se ignora esa línea");
                        continue;
                    }

                    product.setSales(product.getSales() + cantidad);
                }
            }

            else
            {
                throw  new UserNotFoundException("User not found");
            }
            return order;

        }

        else
        {
            throw  new EmptyQueueException("Empty queue");
        }


    }


    public Product getProduct(String id) {
        /*
        for (Product product : productsList) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
        */

        return productList.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElse(null);

    }

    @Override
    public User getUser(String number)
    {
        if (users.containsKey(number))
        {
            return users.get(number);
        }

        return null;
    }

    @Override
    public int numProducts() {
        return productList.size();
    }

    @Override
    public void addUser(String nif, String name, String surname, String mail)
    {
        users.put(nif, new User(nif, name, surname, mail));

    }

    @Override
    public List<Product> findAllProducts()
    {
        return productList ;
    }

    @Override
    public HashMap<String, User> findAllUsers()
    {
        return users ;
    }



    @Override
    public void deleteProduct(String id) {

        Product t = this.getProduct(id);
        if (t==null) {
            logger.warn("not found " + t);
        }
        else logger.info(t+" deleted ");

        this.productList.remove(t);

    }

    @Override
    public Product updateProduct(Product p) {
        Product t = this.getProduct(p.getId());

        if (t!=null) {
            logger.info(p+" rebut!!!! ");

            t.setName(p.getName());
            t.setPrice(p.getPrice());
            t.setSales(p.getSales());
            t.setId(p.getId());

            logger.info(t+" updated ");
        }
        else {
            logger.warn("not found "+p);
        }

        return t;
    }


}