package edu.upc.dsa.domain.models;


import java.util.LinkedList;
import java.util.List;

public class User {

    private String nif;
    private String name;
    private String surname;
    private String mail;

    private List<Order> ordersUser;

    public User(String nif, String name, String surname, String mail)
    {
        this.nif = nif;
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.ordersUser = new LinkedList<>();

    }

    public User(){};

    public String getNif()
    {
        return nif;
    }
    public String  getName() {return name;}
    public String getSurname() {return surname;}
    public String getMail() {return mail;}


    public List<Order> getUserOrders()
    {
        return this.ordersUser;
    }

    public int numOrders()
    {
        return ordersUser.size();
    }

    public void addOrder(Order order)
    {
        ordersUser.add(order);
    }


    @Override
    public String toString()
    {
        return "User[nif=" + nif + ", name=" + name + ", surname=" + surname + ", mail=" + mail + ", pedidos=" + ordersUser.size() + "]";
    }


}
