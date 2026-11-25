package edu.upc.dsa;

import edu.upc.dsa.domain.repository.Products.ProductManager;
import edu.upc.dsa.domain.repository.Products.ProductManagerImpl;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jersey.listing.ApiListingResourceJSON;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import org.apache.log4j.Logger;

/**
 * Main class.
 *
 */



public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/dsaApp/";
    final static Logger logger = Logger.getLogger(Main.class);
    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in edu.upc.dsa package
        final ResourceConfig rc = new ResourceConfig().packages("edu.upc.dsa.services");

        rc.register(io.swagger.jaxrs.listing.ApiListingResource.class);
        rc.register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
        rc.register(com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider.class);
        BeanConfig beanConfig = new BeanConfig();

        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/dsaApp");
        beanConfig.setContact("support@example.com");
        beanConfig.setDescription("REST API for Tracks Manager");
        beanConfig.setLicenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html");
        beanConfig.setResourcePackage("edu.upc.dsa.services");
        beanConfig.setTermsOfServiceUrl("http://www.example.com/resources/eula");
        beanConfig.setTitle("REST API");
        beanConfig.setVersion("1.0.0");
        beanConfig.setScan(true);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void loadInitialData() {
        ProductManager pm = ProductManagerImpl.getInstance();

        if (pm.numProducts()==0) {
            pm.addProduct("C1", "Espada", 2, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTBPOUe7I-v5GXcX5CAtebKhDT_MfWhIybJV-9v6_6V0g&s=10");
            pm.addProduct("C2", "Cargador de pistola", 2.5, "https://en.specshop.pl/hpeciai/1076c6e2f5a26d10565d17d875660c63/eng_pm_Glock-Pistol-Magazine-for-Glock-9x19-mm-Luger-Parabellum-31-2-rounds-4450-41776_2.jpg");
            pm.addProduct("B1", "Botiquin", 3, "https://static.wikia.nocookie.net/fortnite/images/b/b1/Medkit_-_Item_-_Fortnite.png/revision/latest/thumbnail/width/360/height/360?cb=20180917023707");
            pm.addProduct("B2", "Granada", 3.5, "https://static.wikia.nocookie.net/fortnite/images/4/4d/Grenade_%28v11.00%29_-_Item_-_Fortnite.png/revision/latest?cb=20200312041058");
            pm.addProduct("D1", "Mejora de linterna", 3, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdb9C4yPYRkoytdaqhCf2Mydt3Lm_-Nhe0lBgdzoKhhQ&s=10");
            pm.addProduct("D2", "Pistola láser", 3.5, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS3XjobCqzBmqUQJJl0_MBpm4SL_74W2Hw4vIDrIm6kpCEehYRKA_yyHbI&s=10");
            //pm.addProduct("CF1", "Cafe", 2);
            //pm.addProduct("CF2", "Tallat", 2.25);

            pm.addUser("NIF1", "El Chaval", "de la Peca", "chaval.peca@gmail.com", null, 500);
            pm.addUser("NIF2", "Manolo", "Vinilo", "manolo.vinilo@gmail.com", "123", 300);
            pm.addUser("NIF3", "Paco", "Synth", "paco.synth@gmail.com", null, 60);
            pm.addUser("NIF4", "Rafa", "Theremín", "rafa.theremin@gmail.com", null, 40);
            pm.addUser("NIF5", "Lola", "Neón", "lola.neon@gmail.com", null, 100);
            pm.addUser("NIF6", "Toni", "Cassette", "toni.cassette@gmail.com", null, 220);
            pm.addUser("NIF7", "Juanjo", "Kazoo", "juanjo.kazoo@gmail.com", null, 140);
            pm.addUser("NIF8", "Santi", "Prog", "santi.prog@gmail.com", null, 330);
            pm.addUser("NIF9", "Marta", "Moog", "marta.moog@gmail.com", null, 55);
            pm.addUser("NIF10", "Carlos", "Distorsión", "carlos.distorsion@gmail.com", null, 85);
            pm.addUser("NIF11", "Nuria", "Vinilo", "nuria.vinilo@gmail.com", null, 90);
            pm.addUser("NIF12", "Óscar", "Synthwave", "oscar.synthwave@gmail.com", null, 80);
            pm.addUser("NIF13", "David", "Frikipop", "david.frikipop@gmail.com", null, 440);
            pm.addUser("NIF14", "Miguel", "Guitarra", "miguel.guitarra@gmail.com", null, 105);
            pm.addUser("NIF15", "Ferran", "Rockabilly", "ferran.rockabilly@gmail.com", null, 170);
        }
    }


    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        loadInitialData();
        final HttpServer server = startServer();

        StaticHttpHandler staticHttpHandler = new StaticHttpHandler("./public/");
        server.getServerConfiguration().addHttpHandler(staticHttpHandler, "/");


        logger.info(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));

        System.in.read();
        server.stop();
    }
}

