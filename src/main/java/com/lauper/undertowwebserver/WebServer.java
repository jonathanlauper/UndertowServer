package com.lauper.undertowwebserver;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

/**
 * @author Stuart Douglas
 */
public class WebServer{
    
    static String startPath = "/mnt/imageServer";//D:/code/testFolder/";
    private static final HttpHandler ROUTES = new RoutingHandler()
        .post("/upload", WebServer::uploadHandler)
        .get("/images", WebServer::getImage)
        .get("/source", WebServer::getSource)
        .get("/shutdown", WebServer::shutdown)
        .get("/", RoutingHandlers.constantStringHandler("GET - My Homepage"))
        .post("/", WebServer::postAccessDeniedHandler)
        .get("/myRoute", RoutingHandlers.constantStringHandler("GET - My Route"))
        .post("/myRoute", RoutingHandlers.constantStringHandler("POST - My Route"))
        .get("/myOtherRoute", RoutingHandlers.constantStringHandler("GET - My Other Route"))
        // Wildcards and RoutingHandler had some bugs before version 1.4.8.Final
        .get("/myRoutePrefix*", RoutingHandlers.constantStringHandler("GET - My Prefixed Route"))
            
        // Pass a handler as a method reference.
        .setFallbackHandler(RoutingHandlers::notFoundHandler);


    public static void main(final String[] args){

        SimpleServer server = SimpleServer.simpleServer(ROUTES);
        // See we have access to it here!
        Undertow.Builder undertow = server.getUndertow();
        server.start();
    }
    
    
    public static void uploadHandler(HttpServerExchange exchange){
        System.out.println(exchange.getRequestURL());
        if (exchange.isInIoThread()){
            exchange.dispatch(new UploadHandler());
        }
    }
    public static void getImage(HttpServerExchange exchange){  System.out.println(exchange.getRequestURL());
        exchange.dispatch(new DownloadHandler());
    }
    
    public static void getSource(HttpServerExchange exchange) { System.out.println(exchange.getRequestURL());
        try {
            String path = exchange.getQueryParameters().get("setpath").getFirst();
            String fileSeperator = Pattern.quote(System.getProperty("file.separator"));
            startPath = path.replace("/", fileSeperator);
        } catch (Exception e) {
        }
        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send(startPath + " is the location of the fileServer");
    }
    public static void shutdown(HttpServerExchange exchange) {
        try {
            String pw = exchange.getQueryParameters().get("pw").getFirst();
            if(pw.equals("1234")){
                exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "text/plain");
                exchange.getResponseSender().send("Shutting server down");
                System.exit(0);
            }
            exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send("Wrong password");
        }catch(Exception e){
        }
    }
    //Optional.ofNullable(exchange.getQueryParameters().get(name)).map(Deque::getFirst)
    /*
    * Creating HttpHandlers as a method and passing it as a method reference is pretty clean.
    * This also helps reduce accidentally adding state to handlers.
    */
    public static void helloWorldHandler(HttpServerExchange exchange) {
        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send("Hello World!");
    }

    public static void notFoundHandler(HttpServerExchange exchange) {
        exchange.setStatusCode(404);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send("Page Not Found!!");
     }
    public static void postAccessDeniedHandler(HttpServerExchange exchange) {
        exchange.setStatusCode(404);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send("You don't have the rights to post stuff");
     }
}

/*
        int port = 8080;
        /*
         *  "localhost" will ONLY listen on local host.
         *  If you want the server reachable from the outside you need to set "0.0.0.0"
         
        String host = "localhost";

        /*
         * This web server has a single handler with no routing.
         * ALL urls are handled by the helloWorldHandler.
         
        Undertow server = Undertow.builder()
            // Add the helloWorldHandler as a method reference.
            .addHttpListener(port, host, WebServer::helloWorldHandler)
            .build();
        server.start();
*/
/*
    public static void main(final String[] args) {
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                        exchange.getResponseSender().send("Hello World");
                    }
                }).build();
        server.start();
    }
*/

            /*
            try {
            File out = new File("D:/code/testFolder");
            FileWriter o = new FileWriter(out);
            
            FileInputStream fis = new FileInputStream(is);
            
            int content;
            while ((content = fis.read()) != -1) {
            // convert to char and display it
            System.out.print((char) content);
            }
            }catch (IOException ex){}
            */