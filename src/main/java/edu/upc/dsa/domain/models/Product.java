package edu.upc.dsa.domain.models;


import edu.upc.dsa.util.RandomUtils;

public class Product {

    private  String id;
    private  String name;
    private double price;
    private int numSales;
    private String image_url;


    /*public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;

    }*/

    //Para tener id random si no se pone uno

    public Product() {
        this.id = RandomUtils.getId();
    }

    public Product(String id, String name, double price, String image_url) {
        this();
        if (id != null) this.id = id;       // solo si te pasan uno
        this.name = name;
        this.price = price;
        this.image_url = image_url;
    }


    //public Product() {};

    public double getPrice()
    {
        return price;
    }


    public void setSales(int sales)
    {
        this.numSales = sales;
    }

    public int getSales()
    {
        return numSales;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setImage_url(String image_url) {}

    public String getImage_url()
    {
        return image_url;
    }

    @Override
    public String toString()
    {
        return "Product[id=" + id + ", name=" + name + ", price=" + price + ", sales=" + numSales + "]";
    }


}
