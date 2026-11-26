package edu.upc.dsa.domain.models;


import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;

public class User {

    private String nif;
    private String name;
    private String surname;
    private String mail;
    private List<Order> ordersUser;
    private String password;
    private double dinero;
    private HashMap<String, Integer> inventario = new HashMap<>();

    public User(String nif, String name, String surname, String mail, String password, double dinero)
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
    public double getDinero() {return dinero;}
    public void setDinero(double dinero) {this.dinero = dinero;}
    public HashMap<String, Integer> getInventario() {return inventario;}

    public void addProductoInventario(String idProducto, int cantidad) {

        if (inventario.containsKey(idProducto)) {
            inventario.put(idProducto, inventario.get(idProducto) + cantidad);
        } else {
            inventario.put(idProducto, cantidad);
        }
    }


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
