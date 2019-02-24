package com.lauper.undertowwebserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtility{
    List<String> fileList;
    private String source;
	
    ZipUtility(String source){
	fileList = new ArrayList<String>();
        this.source = source;
        generateFileList(new File(source));
        
    }
    /**
     * Zip it
     * @param zipFile output ZIP file location
     */
    public void zipIt(String zipFile){
        byte[] buffer = new byte[1024];
        try{
           FileOutputStream fos = new FileOutputStream(zipFile);
           ZipOutputStream zos = new ZipOutputStream(fos);

           System.out.println("Output to Zip : " + zipFile);

           for(String file : this.fileList){
                System.out.println("File Added : " + file);
                ZipEntry ze= new ZipEntry(file);
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(source + File.separator + file);
                int len;
                while ((len = in.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                }
                in.close();
           }
           zos.closeEntry();
           zos.close();
       }catch(IOException ex){ ex.printStackTrace(); }
   }
    
    /**
     * Traverse a directory and get all files,
     * and add the file into fileList  
     * @param node file or directory
     */
    public void generateFileList(File node){
    	//add file only
	if(node.isFile()){
            fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
	}
	if(node.isDirectory()){
            String[] subNote = node.list();
            for(String filename : subNote){
                generateFileList(new File(node, filename));
            }
	}
    }
    /**
     * Format the file path for zip
     * @param file file path
     * @return Formatted file path
     */
    private String generateZipEntry(String file){
    	return file.substring(source.length()+1, file.length());
    }
    
    public void zipItToStream(OutputStream ops){
        byte[] buffer = new byte[1024];
        try{
           //FileOutputStream fos = new FileOutputStream(zipFile);
           ZipOutputStream zos = new ZipOutputStream(ops);

           //System.out.println("Output to Zip : " + zipFile);

           for(String file : this.fileList){
                System.out.println("File Added : " + file);
                ZipEntry ze= new ZipEntry(file);
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(source + File.separator + file);
                int len;
                while ((len = in.read(buffer)) > 0) {
                        ops.write(buffer, 0, len);
                }
                in.close();
           }
           zos.closeEntry();
           zos.close();
       }catch(IOException ex){ ex.printStackTrace(); }
   }
}
/*
                OutputStream ops = exchange.getOutputStream();
                byte[] buf = new byte[8192];
                is = new FileInputStream(image);
                int c = 0;
                while ((c = is.read(buf, 0, buf.length)) > 0) {
                    ops.write(buf, 0, c);
                    ops.flush();
                }   ops.close();
                is.close();
*/