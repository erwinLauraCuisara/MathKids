/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complementos;

/**
 *
 * @author Gustavo
 */
import java.io.*;

public class gestionArchivos {

    private File directorio;
    private String rutaEspecifica;
    
    //Para crear un archivo y guardarlo
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public gestionArchivos() {
        
    }

    public void eliminarArchivo(String Archivo){ // Este método elimina un archivo
        
        directorio = new File(Archivo);
        if (!directorio.exists()) {
            System.out.println("El archivo no existe.");
        } else {
            directorio.delete();
            System.out.println("El archivo fue eliminado.");
        }
    }
    
    public void serializarDatos(String nombreArchivo, String [] objeto){ // Escribe el archivo

        System.out.println("Guardando cambios...");
        try {
            oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo));
            oos.writeObject(objeto);
            oos.close();
            System.out.println("Se guardó correctamente");
        } catch (Exception e) {
            System.out.println("No se pudieron guardar los cambios");
        }
    }
    
    public String [] recuperaDatos(String nombreArchivo){ //Lee el archivo
        
               
        System.out.println("Leyendo archivo...");
        try {
            ois = new ObjectInputStream(new FileInputStream(nombreArchivo));
            System.out.println("Lectura terminada");
            return (String[]) ois.readObject();
        } catch (Exception e) {
            System.out.println("Ocurrio un error durante la lectura!");
            return new String [] {"Error"};
        }
    }
}
