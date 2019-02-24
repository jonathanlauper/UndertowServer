package com.lauper.undertowwebserver;

import static com.lauper.undertowwebserver.WebServer.startPath;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DownloadHandler  implements HttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.startBlocking();
        InputStream is = null;
        String path = exchange.getQueryParameters().get("path").getFirst();
        String zipIt = "";
        try {
            zipIt = exchange.getQueryParameters().get("zip").getFirst();
        } catch (Exception e) {
        }
        System.out.println(zipIt);
        if(!zipIt.equals("true")){
            try {
                File image = new File(startPath+path);

                OutputStream ops = exchange.getOutputStream();
                byte[] buf = new byte[8192];
                is = new FileInputStream(image);
                int c = 0;
                while ((c = is.read(buf, 0, buf.length)) > 0) {
                    ops.write(buf, 0, c);
                    ops.flush();
                }   ops.close();
                is.close();

            } catch (FileNotFoundException ex) {
                Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(zipIt.equals("true")){
            ZipUtility zip = new ZipUtility(startPath+path);
            zip.zipItToStream(exchange.getOutputStream());
        }
    }
    
    
}
