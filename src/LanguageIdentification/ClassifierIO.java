package LanguageIdentification;
/*

 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author in dia
 */
public class ClassifierIO {
    public static void save(Object obj, String path) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeUnshared(obj);
            oos.close();
        } catch (IOException ex) {
        	System.out.println("error");
            Logger.getLogger(ClassifierIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Object read(String path) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
            Object obj = ois.readObject();
            ois.close();
            return obj;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClassifierIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClassifierIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
