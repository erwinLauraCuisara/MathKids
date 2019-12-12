/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package complementos;

import javax.swing.*;
import dimensionesEscala.*;
import java.awt.*;
import java.awt.event.*;
import ventanaPrincipal.ventanaMenu;
import j2logica.ventanaLogica;
import j1ordenamiento.ventanaOrdenarNumeros;
import j3completaoperacion.ventanaCompletaOperaciones;
import j4cartaspares.ventanaCartasPares;
import java.util.ArrayList;

/**
 *
 * @author Gustavo
 */
public class ventanaPerdedor extends JFrame implements ActionListener {
    
    private DimensionesPantalla dimPan;
    private JLabel labelLastima, labelPerdedor, labelEmo1;
    private ImageIcon imagen, propiedades;
    private JButton botonVolverHome, botonReiniciar;
    private char IDJuego;
    private int nivelMultijugador;
    
    //Ventanas
    private ventanaMenu home;
    private ventanaOrdenarNumeros ordenaNumeros;
    private ventanaLogica logica;
    private ventanaCompletaOperaciones completaOperaciones;
    private ventanaCartasPares pares;
    private audioEfectos audio;
    private String nombreJugador;
    private java.util.List copiaLista;
    // private ventanaCartasPares ventanaCartasPares;
    
    public ventanaPerdedor(char IDJuego, java.util.List listaJugadores, int nivelMultijugador){
        
        copiaLista = new ArrayList();
        for (int i = 0; i < listaJugadores.size(); i++) {
            copiaLista.add(listaJugadores.get(i));
        }
        
        this.IDJuego = IDJuego;
        if(listaJugadores.size() <= 1){ //Si es un solo jugador
            this.nombreJugador = (String) listaJugadores.get(0);
        
        } else this.nombreJugador = "Multijugador";
        
        this.nivelMultijugador = nivelMultijugador;
        
        setUndecorated(true);
        setLayout(null);
        
        getContentPane().setBackground(Color.RED);
        
        audio = new audioEfectos();
        audio.efectoDeSonido("perdedor");
        
        dimPan = new DimensionesPantalla();
        labelPerdedor = new JLabel("PERDISTE");
        labelPerdedor.setBounds(0,dimPan.PenY(5),dimPan.PenX(100), dimPan.PenY(30));
        labelPerdedor.setFont(new Font("New Times Roman", 1, 200));
        labelPerdedor.setForeground(Color.YELLOW);
        labelPerdedor.setHorizontalAlignment(SwingConstants.CENTER);
        add(labelPerdedor);
        
        //Cambio de fondo de pantalla
        labelLastima = new JLabel();
        labelLastima.setBounds(dimPan.PenX(0),dimPan.PenY(0),dimPan.horizontal(), dimPan.vertical());
        imagen = new ImageIcon(getClass().getResource("images/burlaDeHomero.gif"));
        propiedades = new ImageIcon(imagen.getImage().getScaledInstance(labelLastima.getWidth(), labelLastima.getHeight(), Image.SCALE_DEFAULT));
        labelLastima.setIcon(propiedades);
        setBounds(0, 0, new DimensionesPantalla().PenX(100F), new DimensionesPantalla().PenY(100F));
        setExtendedState(Frame.MAXIMIZED_BOTH);
        add(labelLastima);
        
         //Emoticos
        labelEmo1 = new JLabel();
        labelEmo1.setBounds(dimPan.PenX(5), dimPan.PenY(80), dimPan.PenX(10), dimPan.PenY(9));
        imagen = new ImageIcon(getClass().getResource("images/que paso....gif"));
        propiedades = new ImageIcon(imagen.getImage().getScaledInstance(labelEmo1.getWidth(), labelEmo1.getHeight(), Image.SCALE_DEFAULT));
        labelEmo1.setIcon(propiedades);
        setBounds(0, 0, new DimensionesPantalla().PenX(100F), new DimensionesPantalla().PenY(100F));
        labelLastima.add(labelEmo1);
        
        botonVolverHome = new JButton("VOLVER AL MENU PRINCIPAL");
        botonVolverHome.setBounds(dimPan.PenX(1),dimPan.PenY(95),dimPan.PenX(20), dimPan.PenY(5));
        botonVolverHome.addActionListener(this);
        botonVolverHome.setForeground(Color.GREEN);
        botonVolverHome.setFont(new Font("Arial", 1, 15));
        botonVolverHome.setHorizontalAlignment(SwingConstants.LEFT);
        botonVolverHome.setOpaque(false);
        botonVolverHome.setContentAreaFilled(false);
        labelLastima.add(botonVolverHome);
        /*
        botonReiniciar = new JButton("VOLVER A JUGAR");
        botonReiniciar.setBounds(dimPan.PenX(1), dimPan.PenY(90), dimPan.PenX(20), dimPan.PenY(5));
        botonReiniciar.addActionListener(this);
        botonReiniciar.setForeground(Color.GREEN);
        botonReiniciar.setHorizontalAlignment(SwingConstants.LEFT);
        botonReiniciar.setFont(new Font("Arial", 1, 15));
        botonReiniciar.setOpaque(false);
        botonReiniciar.setContentAreaFilled(false);
        labelLastima.add(botonReiniciar);*/
        
        //AWTUtilities.setWindowOpaque(this, false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
         if (e.getSource() == botonVolverHome) {
             this.setVisible(false);
            /*
            home = new ventanaMenu(copiaLista);
            home.setVisible(true);*/
        } else {

            if (IDJuego=='1') {
                ordenaNumeros = new ventanaOrdenarNumeros(copiaLista, "reiniciar", nivelMultijugador);
                ordenaNumeros.setVisible(true);
                this.setVisible(false);
            }
            if (IDJuego=='2') {
                logica = new ventanaLogica(copiaLista, "reiniciar", nivelMultijugador);
                logica.setVisible(true);
                this.setVisible(false);
            }
            
            if (IDJuego=='3') {
                completaOperaciones = new ventanaCompletaOperaciones(copiaLista, "reiniciar", nivelMultijugador);
                completaOperaciones.setVisible(true);
                this.setVisible(false);
            }
            if (IDJuego=='4') {
                pares = new ventanaCartasPares(copiaLista, "reiniciar", nivelMultijugador);
                pares.setVisible(true);
                this.setVisible(false);
            }
        }
    }
}