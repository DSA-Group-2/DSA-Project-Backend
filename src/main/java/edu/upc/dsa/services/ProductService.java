package edu.upc.dsa.services;


import edu.upc.dsa.domain.models.Product;
import edu.upc.dsa.domain.models.Order;
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
import java.util.List;

@Api(value = "/product", description = "Endpoint to Product Service")
@Path("/product")

public class ProductService {

    private ProductManager pm;

    public ProductService()
    {
        this.pm = ProductManagerImpl.getInstance();

    }

    @GET
    @ApiOperation(value = "get all Product", notes = "Retorna todos los productos")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Product.class, responseContainer="List"),
    })
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)

    public Response getProducts() {

        List<Product> products = this.pm.findAllProducts();

        GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(products) {};
        return Response.status(201).entity(entity).build()  ;

    }

    @GET
    @ApiOperation(value = "get a Product", notes = "Devuelve un producto")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Product.class),
            @ApiResponse(code = 404, message = "Producto not found")
    })
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProduct(@PathParam("id") String id) {
        Product t = this.pm.getProduct(id);
        if (t == null) return Response.status(404).build();
        else  return Response.status(201).entity(t).build();
    }

    @GET
    @ApiOperation(value = "getOrderedProductsbyPrice", notes = "Productos ordenados por precios de forma ascendiente.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Product.class),
    })

    @Path("/byPrice")
    @Produces(MediaType.APPLICATION_JSON)

    public Response getOrderedProductsbyPrice() {

        List<Product> products = this.pm.getProductsByPriceAscendiente();

        GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(products) {};
        return Response.status(201).entity(entity).build()  ;
    }

    @GET
    @ApiOperation(value = "getOrderedProductsbySales", notes = "Productos ordenados por ventas de forma descendiente.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Product.class),
    })

    @Path("/bySales")
    @Produces(MediaType.APPLICATION_JSON)

    public Response getOrderedProductsbySale() {

        List<Product> products = this.pm.getProductsBySalesDescendiente();

        GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(products) {};
        return Response.status(201).entity(entity).build()  ;
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
    @ApiOperation(value = "delete a Product", notes = "Eliminar un producto")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "Producto not found")
    })
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") String id) {
        Product t = this.pm.getProduct(id);
        if (t == null) return Response.status(404).build();
        else this.pm.deleteProduct(id);
        return Response.status(201).build();
    }

    @PUT
    @ApiOperation(value = "update a Product", notes = "Actualiza producto")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "Producto not found")
    })
    @Path("/")
    public Response updateProduct(Product product) {

        Product t = this.pm.updateProduct(product);

        if (t == null) return Response.status(404).build();

        return Response.status(201).build();
    }



    @POST
    @ApiOperation(value = "create a new Product", notes = "Crear nuevo Producto")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=Product.class),
            @ApiResponse(code = 500, message = "Validation Error")

    })

    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newProduct(Product product) {

        //if (product.getId()==null || product.getName()==null || product.getPrice()==0.0)

        if (product.getName()==null || product.getPrice()==0.0)

        {
            return Response.status(500).entity(product).build();
        }

        if (product.getId() == null || product.getId().trim().isEmpty())
        {
            product.setId(RandomUtils.getId());
        }


        if (this.pm.getProduct(product.getId()) != null)
        {
            return Response.status(500).entity(product).build();
        }

        this.pm.addProduct(product.getId(), product.getName(), product.getPrice(), product.getImage_url());
        return Response.status(201).entity(product).build();
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