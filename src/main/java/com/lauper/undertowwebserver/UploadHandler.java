package com.lauper.undertowwebserver;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UploadHandler  implements HttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.startBlocking();
        InputStream is = exchange.getInputStream();
        String xmlPathResult = "error";
        try {
            xmlPathResult = UnzipUtility.unzipFromStream(is, WebServer.startPath);
            exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send(xmlPathResult);
            
        } catch (IOException ex) {
            Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
            exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send(WebServer.class.getName()+" "+ex);
        }     
        

    }
    
    
}
