package edu.upc.dsa.domain.models;


import javax.xml.bind.annotation.XmlTransient;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;

public class Order {

    private String dni;
    private HashMap<String, Integer> productList;
    private User user;

    public Order(String dni)
    {
        this.productList = new HashMap<>();
        this.dni = dni;

    }

    public Order(){};

    public String getDni()
    {
        return dni;
    }

    @XmlTransient
    @ApiModelProperty(hidden = true)
    public User getUser() {return user;}

    public HashMap<String, Integer> getProductList()
    {
        return productList;
    }

    public void setDni(String dni)
    {
        this.dni = dni;
    }
    public void setUser(User user) {this.user = user;}
    public void setProductList(HashMap<String, Integer> productList) {this.productList = productList;}


    public void addLP(int q, String p)
    {
        productList.merge(p, q, Integer::sum);

    }

    @Override
    public String toString()
    {
        // No se imprime el user para evitar recursión
        return "Order[dni=" + dni + ", productList=" + productList + "]";
    }




}
