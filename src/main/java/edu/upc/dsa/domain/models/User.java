package edu.upc.dsa.domain.models;


import java.util.LinkedList;
import java.util.List;

public class User {

    private String nif;
    private String name;
    private String surname;
    private String mail;
    private List<Order> ordersUser;
    private String password;
    private int dinero;

    public User(String nif, String name, String surname, String mail, String password, int dinero)
    {
        this.nif = nif;
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.ordersUser = new LinkedList<>();
        this.password = password;
        this.dinero = dinero;
    }

    public User(){};

    public String getNif()
    {
        return nif;
    }
    public String  getName() {return name;}
    public String getSurname() {return surname;}
    public String getMail() {return mail;}
    public String getPassword() {return password;}
    public int getDinero() {return dinero;}



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
