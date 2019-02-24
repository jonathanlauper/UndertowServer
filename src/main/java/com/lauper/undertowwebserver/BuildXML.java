package com.lauper.undertowwebserver;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class BuildXML{
    
    Element root = null;
    
    public BuildXML(){
    }
    
    public String getXML(){
        Document doc = new Document();
        XMLOutputter out = new XMLOutputter();
        out.setFormat(Format.getPrettyFormat());
        
        doc.setRootElement(root);
        //out.output(doc, new FileWriter(new File("myxml.xml")));
        
        return out.outputString(doc);
    }
    
    public void addEndNode(String path){

        boolean isFile = !path.endsWith("/"); 
        String[] nodes = null;
        String rt = "";
        if(!isFile){
            String t = path.substring(0, path.length()-1);//if it's a directory we need to remove the last char (itself a fileseperator) first
            if(t.contains("/"))
                nodes = t.split("/");
            else
                rt = t;//root dir
        }else
            nodes = path.split("/");
        
        if(!rt.equals("")){
            root = new Element(rt);
            return;
        }
        Element current = root;
        Element next = null; 
        Element ch;
        for(int i = 1; i<nodes.length; i++){
            if(nodes.length-1 == i && isFile){
                Element file = new Element("file");
                file.addContent(nodes[i]);
                current.addContent(file);
                return;
            }else if(current.getChild(nodes[i]) == null){//ie it doesnt exist yet, so create it and make it current
                Element child = new Element(nodes[i]);
                current.addContent(child);
                current = child;
            }else if((ch = current.getChild(nodes[i])) != null){//it already exist, make it current
                current = ch;
            }       
        }
    }
}
/*

lement root=new Element("CONFIGURATION");
Document doc=new Document();

Element child1=new Element("BROWSER");
child1.addContent("chrome");
Element child2=new Element("BASE");
child1.addContent("http:fut");
Element child3=new Element("EMPLOYEE");
child3.addContent(new Element("EMP_NAME").addContent("Anhorn, Irene"));

root.addContent(child1);
root.addContent(child2);
root.addContent(child3);

doc.setRootElement(root);

XMLOutputter outter=new XMLOutputter();
outter.setFormat(Format.getPrettyFormat());
outter.output(doc, new FileWriter(new File("myxml.xml")));



 public void addEndNode(String path){
        String fileSeperator = Pattern.quote(System.getProperty("file.separator"));
        String see = File.separator;
        boolean isFile = !(path.endsWith(File.separator) ||path.endsWith(fileSeperator) || path.endsWith("/")); 
        String[] nodes = null;
        String rt = "";
        if(!isFile){
            String t = path.substring(0, path.length()-1);//if it's a directory we need to remove the last char (itself a fileseperator) first
            if(t.contains(fileSeperator))
                nodes = t.split(fileSeperator);
            else
                rt = t;//root dir
        }else
            nodes = path.split(fileSeperator);
        
        if(!rt.equals("")){
            root = new Element(rt);
            return;
        }
        Element current = root;
        Element next = null; 
        Element ch;
        for(int i = 1; i<nodes.length; i++){
            if(nodes.length-1 == i && isFile){
                Element file = new Element("file");
                file.addContent(nodes[i]);
                current.addContent(file);
                return;
            }else if(current.getChild(nodes[i]) == null){//ie it doesnt exist yet, so create it and make it current
                Element child = new Element(nodes[i]);
                current.addContent(child);
                current = child;
            }else if((ch = current.getChild(nodes[i])) != null){//it already exist, make it current
                current = ch;
            }       
        }
    }
}
*/