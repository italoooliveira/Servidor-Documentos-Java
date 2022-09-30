package com.feevale.servidordocumentos.application;

/**
 *
 * @author Italo
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        try{
            DocumentRootServer server = new DocumentRootServer();
            server.listenRequests();   
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
