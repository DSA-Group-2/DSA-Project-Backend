package edu.upc.dsa.services;


import edu.upc.dsa.domain.models.Credentials;
import edu.upc.dsa.domain.models.Order;
import edu.upc.dsa.domain.models.Product;
import edu.upc.dsa.domain.models.User;
import edu.upc.dsa.domain.repository.Products.ProductManager;
import edu.upc.dsa.domain.repository.Products.ProductManagerImpl;
import edu.upc.dsa.exceptions.EmptyQueueException;
import edu.upc.dsa.exceptions.UserNotFoundException;
import edu.upc.dsa.util.RandomUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;

@Api(value = "/user", description = "Endpoint to User Service")
@Path("/user")

public class UserService {

    private ProductManager pm;

    public UserService()
    {
        this.pm = ProductManagerImpl.getInstance();

    }

    //LOGIN
    @POST
    @ApiOperation(value = "login", notes = "Comprobar usuario y contraseña")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Login correcto"),
            @ApiResponse(code = 401, message = "Credenciales incorrectas")
    })
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(Credentials credentials) {
        if (this.pm.checkLogin(credentials.getName(), credentials.getPassword())) {
            return Response.status(200).build();
        }
        return Response.status(401).build();
    }

    //REGISTER
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(User user) {
        if (this.pm.getUser(user.getNif()) != null) // ya existe ese nif, por tanto ya existe el usuario
        {
            return Response.status(409).build();
        }
        this.pm.addUser(user.getNif(), user.getName(), user.getSurname(),
                user.getMail(), user.getPassword(), user.getDinero());
        return Response.status(201).build();
    }

    @GET
    @ApiOperation(value = "get all Users", notes = "Retorna todos los usuarios")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Product.class, responseContainer="List"),
    })
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)

    public Response getProducts() {

        HashMap<String, User> users = this.pm.findAllUsers();

        GenericEntity<HashMap<String, User>> entity = new GenericEntity<HashMap<String, User>>(users) {};
        return Response.status(201).entity(entity).build()  ;

    }

    @GET
    @ApiOperation(value = "get a User", notes = "Devuelve un usuario")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Product.class),
            @ApiResponse(code = 404, message = "Producto not found")
    })
    @Path("/{nif}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("nif") String nif) {
        User t = this.pm.getUser(nif);
        if (t == null) return Response.status(404).build();
        else  return Response.status(201).entity(t).build();
    }



    @GET
    @ApiOperation(value = "get executed orders of a user", notes = "Pedidos ya servidos de un usuario")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Order.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @Path("/orders/{nif}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExecutedOrdersbyUser(@PathParam("nif") String nif) {

        User user = this.pm.getUser(nif);
        if (user == null) return Response.status(404).build();

        List<Order> orders = user.getUserOrders();

        GenericEntity<List<Order>> entity = new GenericEntity<List<Order>>(orders) {};
        return Response.status(201).entity(entity).build();
    }


    @DELETE
    @ApiOperation(value = "delete a User", notes = "Eliminar un usuario")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "Producto not found")
    })
    @Path("/{nif}")
    public Response deleteUser(@PathParam("nif") String nif) {
        User t = this.pm.getUser(nif);
        if (t == null) return Response.status(404).build();
        else this.pm.deleteUser(nif);
        return Response.status(201).build();
    }

    @PUT
    @ApiOperation(value = "update a User", notes = "Actualiza usuario")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @Path("/")
    public Response updateUser(User user) {

        User t = this.pm.updateUser(user);

        if (t == null) return Response.status(404).build();

        return Response.status(201).build();
    }



    @POST
    @ApiOperation(value = "create a new User", notes = "Crear nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=Product.class),
            @ApiResponse(code = 500, message = "Validation Error")

    })

    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newUser(User user) {

        //if (product.getId()==null || product.getName()==null || product.getPrice()==0.0)

        if (user.getName()==null || user.getPassword()== null)

        {
            return Response.status(500).entity(user).build();
        }


        this.pm.addUser(user.getNif(), user.getName(), user.getSurname(), user.getMail(), user.getPassword(), user.getDinero());
        return Response.status(201).entity(user).build();
    }


    @POST
    @ApiOperation(value = "make an order", notes = "Un usuario identificado realiza un pedido")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Order.class),
            @ApiResponse(code = 404, message = "User or product not found"),
            @ApiResponse(code = 500, message = "Validation Error")
    })
    @Path("/order")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response makeOrder(Order order) {

        if (order == null || order.getDni() == null
                || order.getProductList() == null || order.getProductList().isEmpty())
            return Response.status(500).build();

        for (Integer cantidad : order.getProductList().values())
        {
            if (cantidad == null || cantidad <= 0)
                return Response.status(500).build();
        }

        if (this.pm.getUser(order.getDni()) == null)
            return Response.status(404).build();

        for (String idProduct : order.getProductList().keySet()) {
            if (this.pm.getProduct(idProduct) == null)
                return Response.status(404).build();
        }

        this.pm.addOrder(order);
        return Response.status(201).entity(order).build();
    }

    @POST
    @ApiOperation(value = "serve an order", notes = "Sirve el siguiente pedido de la cola (orden de llegada)")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Order.class),
            @ApiResponse(code = 404, message = "Empty queue or user not found")
    })
    @Path("/order/serve")
    @Produces(MediaType.APPLICATION_JSON)
    public Response serveOrder() {
        try {
            Order order = this.pm.deliverOrder();
            return Response.status(201).entity(order).build();
        } catch (EmptyQueueException e) {
            return Response.status(404).entity(e.getMessage()).build();
        } catch (UserNotFoundException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }


}