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

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import dimensionesEscala.DimensionesPantalla;
import java.util.ArrayList;
import ventanaPrincipal.*;


public class ventanaResultadosMultijugador extends JFrame implements ActionListener{
    private DimensionesPantalla dimPan;
    private java.util.List copiaLista;
    private ventanaMenu Menu;
    
    private JLabel labelTituloResultados;
    private JScrollPane scrollPuntos;
    private JButton botonHome;
    private JLabel labelResultados;
    public ventanaResultadosMultijugador(String [] [] resultadoJugadores){
    
        setLayout(null);
        setUndecorated(true);
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.darkGray);
        dimPan = new DimensionesPantalla();
        copiaLista = new ArrayList();
        for(int i = 0; i < resultadoJugadores.length;i++){
        
           copiaLista.add(resultadoJugadores[i][0]);
            
        }
        
        labelTituloResultados = new JLabel("RESULTADOS");
        labelTituloResultados.setBounds(dimPan.PenX(0), dimPan.PenY(5), dimPan.horizontal(), dimPan.PenY(25));
        labelTituloResultados.setForeground(new Color(255,100,255));
        labelTituloResultados.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(150)));
        labelTituloResultados.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(labelTituloResultados);
        
        //Armado del texto resultados
        String texto = "<html>◙  ";
        for(int i = 0; i< copiaLista.size();i++ ){
            texto += resultadoJugadores[i][0] + ": "+resultadoJugadores [i][1]+"<p>◙  ";
        }
        texto += " ☺</html>";
        labelResultados = new JLabel(texto);
        scrollPuntos = new JScrollPane(labelResultados);
        scrollPuntos.setBounds(dimPan.PenX(25), dimPan.PenY(30), dimPan.PenX(50), dimPan.PenY(50));
        labelResultados.setForeground(Color.darkGray);
        labelResultados.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(50)));
        labelResultados.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(scrollPuntos);
        
        botonHome = new JButton("HOME");
        botonHome.setBounds(dimPan.PenX(2), dimPan.PenY(90), dimPan.PenX(13), dimPan.PenY(8));
        botonHome.setForeground(Color.red);
        botonHome.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(50)));
        botonHome.addActionListener(this);
        botonHome.setBackground(Color.darkGray);
        this.add(botonHome);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==botonHome){
            this.setVisible(false);
        }
    }
    
}
