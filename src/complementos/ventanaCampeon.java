/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complementos;

//import com.sun.awt.AWTUtilities;//transparencia de la ventana
import javax.swing.*;
import dimensionesEscala.*;
import java.awt.*;
import java.awt.event.*;
import ventanaPrincipal.ventanaMenu;
import j1ordenamiento.ventanaOrdenarNumeros;
import j2logica.ventanaLogica;
import j3completaoperacion.ventanaCompletaOperaciones;
import j4cartaspares.ventanaCartasPares;
import java.util.ArrayList;

/**
 *
 * @author Gustavo
 */
public class ventanaCampeon extends JFrame implements ActionListener {

    private DimensionesPantalla dimPan;
    private char IDJuego;
    private JLabel labelFelicidades, labelCampeon, labelEmo1;
    private ImageIcon imagen, propiedades;
    private JButton botonVolverHome, botonReiniciar;

    //Ventanas 
    private ventanaMenu home;
    private ventanaOrdenarNumeros ordenarNumeros;
    private ventanaLogica logica;
    private ventanaCompletaOperaciones completaOperaciones;
    private ventanaCartasPares cartasPares;
    private audioEfectos sonido, sonidoFaltan5Minutos;
    private String nombreJugador;
    // private ventanaPares pares;
    private java.util.List copiaLista;
    private int nivelMultijugador;

    public ventanaCampeon(char IDJuego, java.util.List listaJugadores, int nivelMultijugador) {

        this.nombreJugador = "";
        copiaLista = new ArrayList();
        for (int i = 0; i < listaJugadores.size(); i++) {
            copiaLista.add(listaJugadores.get(i));
        }

        this.nombreJugador = nombreJugador;
        this.nivelMultijugador = nivelMultijugador;
        this.IDJuego = IDJuego;
        setUndecorated(true);
        setLayout(null);

        sonido = new audioEfectos();

        getContentPane().setBackground(Color.BLACK);

        sonido.reproduceFondo("Campeon");

        dimPan = new DimensionesPantalla();
        labelCampeon = new JLabel("CAMPEON!!!");
        labelCampeon.setBounds(0, dimPan.PenY(5), dimPan.PenX(100), dimPan.PenY(30));
        labelCampeon.setFont(new Font("New Times Roman", 1, 200));
        labelCampeon.setForeground(Color.YELLOW);
        labelCampeon.setHorizontalAlignment(SwingConstants.CENTER);
        add(labelCampeon);

        //IMAGEN DE GANADOR
        labelFelicidades = new JLabel();
        labelFelicidades.setBounds(dimPan.PenX(20), dimPan.PenY(30), dimPan.PenX(60), dimPan.PenY(60));
        imagen = new ImageIcon(getClass().getResource("images/homeroAsustado.gif"));
        propiedades = new ImageIcon(imagen.getImage().getScaledInstance(labelFelicidades.getWidth(), labelFelicidades.getHeight(), Image.SCALE_DEFAULT));
        labelFelicidades.setIcon(propiedades);
        setBounds(0, 0, new DimensionesPantalla().PenX(100F), new DimensionesPantalla().PenY(100F));
        setExtendedState(Frame.MAXIMIZED_BOTH);
        add(labelFelicidades);

        //Emoticos
        labelEmo1 = new JLabel();
        labelEmo1.setBounds(dimPan.PenX(5), dimPan.PenY(80), dimPan.PenX(10), dimPan.PenY(10));
        imagen = new ImageIcon(getClass().getResource("images/reverencia.gif"));
        propiedades = new ImageIcon(imagen.getImage().getScaledInstance(labelEmo1.getWidth(), labelEmo1.getHeight(), Image.SCALE_DEFAULT));
        labelEmo1.setIcon(propiedades);
        setBounds(0, 0, new DimensionesPantalla().PenX(100F), new DimensionesPantalla().PenY(100F));
        add(labelEmo1);

        botonVolverHome = new JButton("VOLVER AL MENU PRINCIPAL");
        botonVolverHome.setBounds(dimPan.PenX(1), dimPan.PenY(95), dimPan.PenX(20), dimPan.PenY(5));
        botonVolverHome.addActionListener(this);
        botonVolverHome.setForeground(Color.GREEN);
        botonVolverHome.setFont(new Font("Arial", 1, 15));
        botonVolverHome.setHorizontalAlignment(SwingConstants.LEFT);
        botonVolverHome.setOpaque(false);
        botonVolverHome.setContentAreaFilled(false);
        add(botonVolverHome);
        /*
        botonReiniciar = new JButton("VOLVER A JUGAR");
        botonReiniciar.setBounds(dimPan.PenX(1), dimPan.PenY(90), dimPan.PenX(20), dimPan.PenY(5));
        botonReiniciar.addActionListener(this);
        botonReiniciar.setForeground(Color.GREEN);
        botonReiniciar.setFont(new Font("Arial", 1, 15));
        botonReiniciar.setHorizontalAlignment(SwingConstants.LEFT);
        botonReiniciar.setOpaque(false);
        botonReiniciar.setContentAreaFilled(false);
        add(botonReiniciar);*/

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonVolverHome) {
            /*
            home = new ventanaMenu(copiaLista);
            home.setVisible(true);*/
            this.setVisible(false);
            sonido.detenerAudio();
            sonido.efectoDeSonido("burbuja");
        } else {
            sonido.efectoDeSonido("burbuja");
            if (IDJuego == '1') {
                ordenarNumeros = new ventanaOrdenarNumeros(copiaLista, "reiniciar", nivelMultijugador);
                ordenarNumeros.setVisible(true);
                this.setVisible(false);
                sonido.detenerAudio();
            }

            if (IDJuego == '2') {
                logica = new ventanaLogica(copiaLista, "reiniciar", nivelMultijugador);
                logica.setVisible(true);
                this.setVisible(false);
                sonido.detenerAudio();
            }

            if (IDJuego == '3') {
                completaOperaciones = new ventanaCompletaOperaciones(copiaLista, "reiniciar", nivelMultijugador);
                completaOperaciones.setVisible(true);
                this.setVisible(false);
                sonido.detenerAudio();
            }
            if (IDJuego == '4') {
                cartasPares = new ventanaCartasPares(copiaLista, "reiniciar", nivelMultijugador);
                cartasPares.setVisible(true);
                this.setVisible(false);
                sonido.detenerAudio();
            }

        }

    }
}
