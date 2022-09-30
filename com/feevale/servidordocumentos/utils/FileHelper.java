package com.feevale.servidordocumentos.utils;

import java.io.File;
import java.nio.file.Paths;

/**
 *
 * @author Italo
 */
public class FileHelper {
    public static File getFileFromServer(String filePath){
        return new File(getAbsolutePathOfFile(filePath));
    }
    
    private static String getAbsolutePathOfFile(String fileName){
        return Paths.get("").toAbsolutePath().toString()+ "/src/main/java/www/" + fileName;
    }
}
