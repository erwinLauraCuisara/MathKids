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
//import java.applet.AudioClip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
public class audioEfectos {
    private Clip clip;
    private Clip efectoRapido;
    private String ruta= "/complementos/audio/";
    
    public void reproduceFondo(String archivo){ //El fondo de sonido es repetitivo
        
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(getClass().getResourceAsStream(ruta+archivo+".wav")));
            clip.loop(10);
            
        } catch (Exception e) {
            System.out.println("No fue posible reproducir");
        }
    }
    
    public void efectoDeSonido(String archivo){
        
        try {
            efectoRapido = AudioSystem.getClip();
            efectoRapido.open(AudioSystem.getAudioInputStream(getClass().getResourceAsStream(ruta+archivo+".wav")));
            efectoRapido.start();
            
        } catch (Exception e) {
            System.out.println("No fue posible reproducir");
        }
        
    }
    
    public void detenerAudio(){
        try {
            clip.stop();
        } catch (Exception e) {
        }
    }
}
