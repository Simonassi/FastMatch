package FastMatch;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
/**
 *
 * @author Rafael
 */
public class Serializa {

    /**
     * @param args the command line arguments
     */
    
    // por padrao, os arquivos tem extensao .ser
    public static void serializa(Object objeto,String arquivo){
        try
      {
         FileOutputStream fileOut = new FileOutputStream(arquivo);
         ObjectOutputStream out = new ObjectOutputStream(fileOut);
         out.writeObject(objeto);
         out.close();
          fileOut.close();
      }catch(IOException e)
      {
          System.out.println("Erro ao serializar o objeto.");
      }
    }
    
    public static Object deserializa(String arquivo) {
        Object objeto = null;
        try
        {
            FileInputStream fileIn = new FileInputStream(arquivo);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            objeto =  (ConcurrentHashMap<String, Objeto>) in.readObject();
            in.close();
            fileIn.close();
        }catch(IOException i){
            System.out.println("Erro ao abrir o arquivo.");
        }catch(ClassNotFoundException c){
            System.out.println("Erro ao deserializar o objeto.");
        }
        return objeto;
    }
    
}
