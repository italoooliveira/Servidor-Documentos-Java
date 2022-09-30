package com.feevale.servidordocumentos.application;

import com.feevale.servidordocumentos.utils.FileHelper;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Italo
 */
public class DocumentRootServer {
    Socket socket;
    ServerSocket server;
    BufferedReader inputStreamReader;
    DataOutputStream outputStreamWriter;  
    FileInputStream fileInputStream;
    boolean itsHtmlFile = false;
    
    public DocumentRootServer(){
        this.setup();
    }
    
    public void listenRequests() throws InterruptedException{
        while(true){
            this.waitClient();
            String fileName = this.receiveRequest();
            try {
                this.sendResponse(fileName);
            } catch (IOException e) {
               e.printStackTrace();
            }
        }
    }
    
    private void setup(){
        try {
            server = new ServerSocket(8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void waitClient(){
        try {            
            socket = server.accept();
            inputStreamReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStreamWriter = new DataOutputStream(socket.getOutputStream()); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String receiveRequest(){
        String line = "";
        List<String> linesFromRequest = new ArrayList<String>();
        
        try {
            while((line = inputStreamReader.readLine()).length() > 0){
                System.out.println(line);
                linesFromRequest.add(line);
            }
            //System.out.println("-------");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return getFileName(linesFromRequest);
    }
    
    private String getFileName(List<String> linesFromRequest){
        String fileName = linesFromRequest.get(0)
                .replace("GET", "")
                .replace("HTTP/1.1", "")
                .replace("/ ", "")
                .trim();
        
        if(!fileName.equals(""))
        {
            if(fileName.contains(".html"))
                itsHtmlFile = true;
            
            return fileName;
        } 
        else 
        {
            return "index.html";
        }
    }
    
    private void sendResponse(String fileName) throws IOException{
        try {
           processFile(fileName);
        } catch (IOException e) {
           if(itsHtmlFile)
            processFile("404.html");
           e.printStackTrace();
        }
        
        outputStreamWriter.close();
        socket.close();
    }
    
    private void processFile(String fileName) throws IOException{
        int bytes = 0;
        File file = FileHelper.getFileFromServer(fileName);
        fileInputStream = new FileInputStream(file);

        outputStreamWriter.writeBytes("HTTP/1.1 200 OK\r\n");
        outputStreamWriter.writeBytes("Content-Type:"+Files.probeContentType(file.toPath())+"\r\n");
        outputStreamWriter.writeBytes("\r\n");

        byte[] buffer = new byte[1024];
        while ((bytes = fileInputStream.read(buffer)) > 0){
            outputStreamWriter.write(buffer,0,bytes);
        }
        outputStreamWriter.flush();

        fileInputStream.close();
    }
}
