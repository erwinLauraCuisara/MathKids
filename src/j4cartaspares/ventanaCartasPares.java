/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package j4cartaspares;

/**
 * Esta clase es la ventana del juego de cartas (j4)
 * @Autores:  
 * Abasto Argote Gustavo, 
 * Laura Cuisara Erwin Harnaldo, 
 * Cayo Huaylla Ruddy, 
 * Ancari Iñiguez Abel
 * 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import dimensionesEscala.*;
import complementos.*;
import ventanaPrincipal.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 * 
 * 
 */
public class ventanaCartasPares extends JInternalFrame implements MouseListener, Runnable, Serializable, ActionListener {

    private int puntosAuxiliar;  ////borrarrrrrrrrrrrrr
    private ImageIcon fondo, propiedadesFondo;
    private JLabel cartas[];
    private int numeros[], listaAux[], listaAux2[];
    private JLabel labelFondo, labelTitulo, labelVidas, labelCronometro, labelNivel, labelNombreJugador, labelJugadorTurno,
            labelAciertos, labelCronometroMultijugador, labelPausa;
    private JButton botonHome, botonGuardar;
    private DimensionesPantalla dimPan;
    private JPanel Panel1, panelCartas;
    private int nivel, numeroDigitos, Contador, c, nCartas, numero1, contador2, puntos1, vidas, segundos;

    //private puntosJugador puntaje;
    private cartasPares logicaJuego;

    private ventanaPerdedor perdedor;
    //private ventanaMenu home;

    private Thread hilo1;
    private audioEfectos sonido, sonidoFaltan5Minutos;
    private java.util.List copiaLista;
    private gestionArchivos archivo;

    //Carga datos
    private String nombreJugador;
    //private String ruta;

    //Multijugador
    private int tiempoMultijugador, segundosRestantes, jugador, aciertosMultijugador;
    private String datosJugadores[][];
    private ventanaResultadosMultijugador resultadosMultijugador;
    public boolean CartasVoltedas[];

    //pausar
    private boolean pausado;
    private JButton botonPausa;
    
    /**
     * 
     * @param listaJugadores Recibe la cantidad de jugadores
     * @param rutaArchivo Recibe la ruta de un archivo (En caso de que se cargue el archivo)
     * @param nivelMultijugador Recibe el nivel para el juego multijugador (desde el comboBox del la ventana Menu)
     */
    public ventanaCartasPares(java.util.List listaJugadores, String rutaArchivo, int nivelMultijugador) {
        puntosAuxiliar = 0;
        copiaLista = new ArrayList();
        CartasVoltedas = new boolean[28];
        voltearCartas();
        for (int i = 0; i < listaJugadores.size(); i++) {
            copiaLista.add(listaJugadores.get(i));
        }
        if (listaJugadores.size() <= 1) {

            nombreJugador = (String) listaJugadores.get(0);

        } else {
            nombreJugador = "Multijugador";
        }

        dimPan = new DimensionesPantalla();
        this.setIconifiable(true);
        this.setBounds(0, 0, dimPan.horizontal(), dimPan.vertical());
        this.setTitle("Cartas pares");

        panelCartas = new JPanel();
        panelCartas.setBounds(0, 0, dimPan.PenX(72), dimPan.vertical());
        this.add(panelCartas);

        labelFondo = new JLabel();
        labelFondo.setBounds(0, 0, dimPan.PenX(72), dimPan.vertical());
        labelFondo.setForeground(Color.WHITE);
        ImageIcon fot = new ImageIcon("src\\j4cartaspares\\images\\online casino GIF by South Park -source.gif");
        Icon icono = new ImageIcon(fot.getImage().getScaledInstance(labelFondo.getWidth(), labelFondo.getHeight(), Image.SCALE_DEFAULT));
        labelFondo.setIcon(icono);
        this.repaint();
        panelCartas.add(labelFondo);

        archivo = new gestionArchivos();
        //ruta = "C:\\Users\\user1\\Desktop\\juego v5\\Juego\\src\\complementos\\BD\\juego4\\";

        sonido = new audioEfectos();
        sonidoFaltan5Minutos = new audioEfectos();

        if (copiaLista.size() <= 1) { // Si no es multijugador
            archivo = new gestionArchivos();
            if (!rutaArchivo.equals("")) {//Vacio es que no contiene ninguna ruta

                try {
                    nivel = Integer.parseInt(archivo.recuperaDatos(rutaArchivo)[0]);
                    vidas = Byte.parseByte(archivo.recuperaDatos(rutaArchivo)[1]);
                    numeroDigitos = Integer.parseInt(archivo.recuperaDatos(rutaArchivo)[2]);
                    c = Integer.parseInt(archivo.recuperaDatos(rutaArchivo)[3]);
                    segundos = 15;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "El archivo no es compatible");
                    nivel = 1;
                    vidas = 5; //Byte
                    c = 1;
                    numeroDigitos = 10;
                    segundos = 15;
                }
            } else {
                nivel = 1;
                vidas = 5; //Byte
                c = 1;
                numeroDigitos = 10;
                segundos = 15;
            }
        } else { //multijugador

            c = 1;
            numeroDigitos = 10;

            tiempoMultijugador = 10; //(Nunca cambia) Tiempo total en el que se ejecutará el juego
            segundosRestantes = tiempoMultijugador; //Cronometro cambiante
            jugador = 0; //Posicion del jugador
            this.nivel = nivelMultijugador;
            vidas = 1;
            //jugadores y resultados (inserta a datos de los jugadores)
            datosJugadores = new String[listaJugadores.size()][3];// cada fila tiene nombreJugador - puntos - tiempo
            for (int i = 0; i < copiaLista.size(); i++) {
                datosJugadores[i][0] = (String) copiaLista.get(i);
                datosJugadores[i][1] = "" + 0;
                datosJugadores[i][2] = "" + tiempoMultijugador;
            }

            labelJugadorTurno = new JLabel("Turno de: " + datosJugadores[0][0]); // Se carga al label el nombre del primer jugador
            labelJugadorTurno.setBounds(dimPan.PenX(15), dimPan.PenY(85), dimPan.PenX(33), dimPan.PenY(5));
            labelJugadorTurno.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(30)));
            labelJugadorTurno.setForeground(Color.ORANGE);
            //labelJugadorTurno.setHorizontalAlignment(SwingConstants.CENTER);
            labelFondo.add(labelJugadorTurno);

            labelAciertos = new JLabel("Aciertos: " + datosJugadores[0][1]); // Se carga al label el nombre del primer jugador
            labelAciertos.setBounds(dimPan.PenX(15), dimPan.PenY(90), dimPan.PenX(33), dimPan.PenY(5));
            labelAciertos.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(30)));
            labelAciertos.setForeground(Color.ORANGE);
            //labelAciertos.setHorizontalAlignment(SwingConstants.CENTER);
            labelFondo.add(labelAciertos);

            labelCronometroMultijugador = new JLabel("Atento...");
            labelCronometroMultijugador.setBounds(dimPan.PenX(0), dimPan.PenY(82), dimPan.PenX(100), dimPan.PenY(10));
            labelCronometroMultijugador.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(110)));
            labelCronometroMultijugador.setHorizontalAlignment(SwingConstants.CENTER);
            labelCronometroMultijugador.setForeground(Color.WHITE);
            labelFondo.add(labelCronometroMultijugador);

            aciertosMultijugador = 0;

        }

        //numeroDigitos = 10;
        //c = 1;
        cartas = new JLabel[28];
        numeros = new int[28];
        listaAux = new int[3];
        listaAux2 = new int[3];

        logicaJuego = new cartasPares();
        setLayout(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.green);

        Panel1 = new JPanel();
        Panel1.setBounds(dimPan.PenX(70), 0, dimPan.PenX(30), dimPan.vertical());
        Panel1.setBackground(new Color(139, 0, 0));
        Panel1.setLayout(null);

        labelCronometro = new JLabel();
        labelCronometro.setBounds(dimPan.PenX(16), dimPan.PenY(20), dimPan.PenX(10), dimPan.PenY(10));
        labelCronometro.setText("" + segundos);
        labelCronometro.setFont(new Font("Forte", 4, dimPan.tamanioLetra(60)));
        labelCronometro.setForeground(Color.WHITE);
        labelCronometro.setHorizontalAlignment(SwingConstants.CENTER);
        Panel1.add(labelCronometro);

        // aqui label fondo
        labelTitulo = new JLabel();
        labelTitulo.setBounds(dimPan.PenX(0), dimPan.PenY(5), dimPan.PenX(30), dimPan.PenY(10));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setText("  JUEGO DE PARES");
        labelTitulo.setFont(new Font("Forte", 4, 30));
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        Panel1.add(labelTitulo);

        labelVidas = new JLabel();
        labelVidas.setBounds(dimPan.PenX(6), dimPan.PenY(36), dimPan.PenX(20), dimPan.PenY(8));
        labelVidas.setForeground(Color.WHITE);
        labelVidas.setText("VIDAS: " + vidas);
        labelVidas.setFont(new Font("Forte", 5, 30));
        Panel1.add(labelVidas);

        labelNivel = new JLabel();
        labelNivel.setBounds(dimPan.PenX(6), dimPan.PenY(55), dimPan.PenX(20), dimPan.PenY(8));
        labelNivel.setText("NIVEL: " + nivel + "      :)");
        labelNivel.setForeground(Color.WHITE);
        labelNivel.setFont(new Font("Forte", 5, 30));
        Panel1.add(labelNivel);

        labelNombreJugador = new JLabel(this.nombreJugador);
        labelNombreJugador.setBounds(dimPan.PenX(2), dimPan.PenY(85), dimPan.PenX(17), dimPan.PenY(10));
        labelNombreJugador.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(40)));
        labelNombreJugador.setForeground(Color.WHITE);
        labelNombreJugador.setHorizontalAlignment(SwingConstants.CENTER);
        Panel1.add(labelNombreJugador);

        this.add(Panel1);
        //instanciando las  cartas
        for (int i = 0; i < 28; i++) {
            cartas[i] = new JLabel();
            cartas[i].setIcon(new ImageIcon("src\\j4cartaspares\\images\\227670523_3.jpg"));
            cartas[i].addMouseListener(this);
            labelFondo.add(cartas[i]);
        }
        /////
        if (listaJugadores.size() > 1) {
            if (nivelMultijugador == 1) {
                this.nivel = 2;    //las lineas 230 231 y 232    no sirven(borrar) solo lo usamos para presentar.
            }
        }
        
        botonGuardar = new JButton("Guardar");
        botonGuardar.setBounds(dimPan.PenX(15), 0, dimPan.PenX(5), dimPan.PenY(2));
        botonGuardar.addActionListener(this);
        botonGuardar.setContentAreaFilled(false);
        botonGuardar.setForeground(Color.WHITE);
        Panel1.add(botonGuardar);
        
        botonHome = new JButton();
        botonHome.setBounds(dimPan.PenX(20), dimPan.PenY(87), dimPan.PenX(8), dimPan.PenY(5));
        botonHome.setText("HOME");
        botonHome.setBackground(new Color(165, 42, 42));
        botonHome.setForeground(Color.WHITE);
        botonHome.setFont(new Font("Forte", 5, 20));
        botonHome.addMouseListener(this);
        Panel1.add(botonHome);

        pausado = false;
        botonPausa = new JButton("Pausar");
        botonPausa.setBounds(dimPan.PenX(20), dimPan.PenY(82), dimPan.PenX(8), dimPan.PenY(5));
        botonPausa.addActionListener(this);
        botonPausa.setContentAreaFilled(false);
        botonPausa.setForeground(Color.WHITE);
        botonPausa.setFont(new Font("Andale Mono", 2, dimPan.tamanioLetra(20)));
        Panel1.add(botonPausa);

        labelPausa = new JLabel("PAUSA       ");
        labelPausa.setBounds(0, dimPan.PenY(40), dimPan.horizontal(), dimPan.PenY(25));
        labelPausa.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(250)));
        labelPausa.setForeground(Color.WHITE);
        this.add(labelPausa);
        labelPausa.setHorizontalAlignment(SwingConstants.CENTER);
        labelPausa.setVisible(false);

        // siempre inicia con el nivel n Ojo, arreglar
        if (nivel == 1) {
            nivel1();
        }
        if (nivel == 2) {
            nivel2();
        }
        if (nivel == 3) {
            nivel3();
        }
        if (nivel == 4) {
            nivel4();
        }
        if (nivel == 5) {
            nivel5();
        }
        if (nivel == 6) {
            nivel6();
        }
        if (nivel == 7) {
            nivel7();
        }
        if (nivel == 8) {
            nivel8();
        }
        if (nivel == 9) {
            nivel9();
        }
        if (nivel == 10) {
            nivel10();
        }
        if (nivel == 11) {
            nivel11();
        }
        if (nivel == 12) {
            nivel12();
        }
        if (nivel == 13) {
            nivel13();
        }
            if (nivel == 1) {
                logicaJuego.GenerarNumerosAleatorios(4, numeroDigitos);
            }
            if (nivel == 2) {
                logicaJuego.GenerarNumerosAleatorios(6, numeroDigitos);
            }
            if (nivel == 3) {
                logicaJuego.GenerarNumerosAleatorios(8, numeroDigitos);
            }
            if (nivel == 4) {
                logicaJuego.GenerarNumerosAleatorios(10, numeroDigitos);
            }
            if (nivel == 5) {
                logicaJuego.GenerarNumerosAleatorios(12, numeroDigitos);
            }
        
        numeros = logicaJuego.getLista();
        if (copiaLista.size() <= 1) {
            nivel++;
        }
        
        if (copiaLista.size() > 1) {
            labelVidas.setVisible(false);
            labelCronometro.setVisible(false);
        }

        hilo1 = new Thread(this);
        hilo1.start();

    } // FIN DEL CONTRUCTOR
    
    private void GuardarImg() {
        for (int i = 0; i < 28; i++) {
            cartas[i].setIcon(new ImageIcon("src\\j4cartaspares\\images\\227670523_3.jpg"));
            //cartas[i].addMouseListener(null);
            labelFondo.add(cartas[i]);
        }
    }

    private void nivel1() {
        cartas[0].setBounds(dimPan.PenX(10), dimPan.PenY(30), 150, 200);
        cartas[1].setBounds(dimPan.PenX(22), dimPan.PenY(30), 150, 200);
        cartas[2].setBounds(dimPan.PenX(34), dimPan.PenY(30), 150, 200);
        cartas[3].setBounds(dimPan.PenX(46), dimPan.PenY(30), 150, 200);
        nCartas = 4;
        voltearCartas();
    }

    private void nivel2() {
        cartas[0].setBounds(dimPan.PenX(10), dimPan.PenY(40), 150, 200);
        cartas[1].setBounds(dimPan.PenX(22), dimPan.PenY(30), 150, 200);
        cartas[2].setBounds(dimPan.PenX(34), dimPan.PenY(30), 150, 200);
        cartas[3].setBounds(dimPan.PenX(46), dimPan.PenY(40), 150, 200);
        cartas[4].setBounds(dimPan.PenX(22), dimPan.PenY(50), 150, 200);
        cartas[5].setBounds(dimPan.PenX(34), dimPan.PenY(50), 150, 200);
        nCartas = 6;
        voltearCartas();

    }

    private void nivel3() {
        cartas[0].setBounds(dimPan.PenX(10), dimPan.PenY(30), 150, 200);
        cartas[1].setBounds(dimPan.PenX(22), dimPan.PenY(30), 150, 200);
        cartas[2].setBounds(dimPan.PenX(34), dimPan.PenY(30), 150, 200);
        cartas[3].setBounds(dimPan.PenX(46), dimPan.PenY(30), 150, 200);
        cartas[4].setBounds(dimPan.PenX(10), dimPan.PenY(50), 150, 200);
        cartas[5].setBounds(dimPan.PenX(22), dimPan.PenY(50), 150, 200);
        cartas[6].setBounds(dimPan.PenX(34), dimPan.PenY(50), 150, 200);
        cartas[7].setBounds(dimPan.PenX(46), dimPan.PenY(50), 150, 200);
        nCartas = 8;
        voltearCartas();
    }

    private void nivel4() {
        cartas[0].setBounds(dimPan.PenX(5), dimPan.PenY(32), 150, 200);
        cartas[1].setBounds(dimPan.PenX(17), dimPan.PenY(32), 150, 200);
        cartas[2].setBounds(dimPan.PenX(29), dimPan.PenY(32), 150, 200);
        cartas[3].setBounds(dimPan.PenX(41), dimPan.PenY(32), 150, 200);
        cartas[4].setBounds(dimPan.PenX(53), dimPan.PenY(32), 150, 200);

        cartas[5].setBounds(dimPan.PenX(5), dimPan.PenY(52), 150, 200);
        cartas[6].setBounds(dimPan.PenX(17), dimPan.PenY(52), 150, 200);
        cartas[7].setBounds(dimPan.PenX(29), dimPan.PenY(52), 150, 200);
        cartas[8].setBounds(dimPan.PenX(41), dimPan.PenY(52), 150, 200);
        cartas[9].setBounds(dimPan.PenX(53), dimPan.PenY(52), 150, 200);
        nCartas = 10;
        voltearCartas();
    }

    private void nivel5() {
        cartas[0].setBounds(dimPan.PenX(29), dimPan.PenY(8), 150, 200);
        cartas[1].setBounds(dimPan.PenX(5), dimPan.PenY(28), 150, 200);
        cartas[2].setBounds(dimPan.PenX(17), dimPan.PenY(28), 150, 200);
        cartas[3].setBounds(dimPan.PenX(29), dimPan.PenY(28), 150, 200);
        cartas[4].setBounds(dimPan.PenX(41), dimPan.PenY(28), 150, 200);
        cartas[5].setBounds(dimPan.PenX(53), dimPan.PenY(28), 150, 200);

        cartas[6].setBounds(dimPan.PenX(5), dimPan.PenY(48), 150, 200);
        cartas[7].setBounds(dimPan.PenX(17), dimPan.PenY(48), 150, 200);
        cartas[8].setBounds(dimPan.PenX(29), dimPan.PenY(48), 150, 200);
        cartas[9].setBounds(dimPan.PenX(41), dimPan.PenY(48), 150, 200);
        cartas[10].setBounds(dimPan.PenX(53), dimPan.PenY(48), 150, 200);
        cartas[11].setBounds(dimPan.PenX(29), dimPan.PenY(68), 150, 200);
        nCartas = 12;
        voltearCartas();
    }

    private void nivel6() {
        cartas[0].setBounds(dimPan.PenX(23), dimPan.PenY(8), 150, 200);
        cartas[1].setBounds(dimPan.PenX(35), dimPan.PenY(8), 150, 200);
        cartas[2].setBounds(dimPan.PenX(5), dimPan.PenY(28), 150, 200);
        cartas[3].setBounds(dimPan.PenX(17), dimPan.PenY(28), 150, 200);
        cartas[4].setBounds(dimPan.PenX(29), dimPan.PenY(28), 150, 200);
        cartas[5].setBounds(dimPan.PenX(41), dimPan.PenY(28), 150, 200);
        cartas[6].setBounds(dimPan.PenX(53), dimPan.PenY(28), 150, 200);

        cartas[7].setBounds(dimPan.PenX(5), dimPan.PenY(48), 150, 200);
        cartas[8].setBounds(dimPan.PenX(17), dimPan.PenY(48), 150, 200);
        cartas[9].setBounds(dimPan.PenX(29), dimPan.PenY(48), 150, 200);
        cartas[10].setBounds(dimPan.PenX(41), dimPan.PenY(48), 150, 200);
        cartas[11].setBounds(dimPan.PenX(53), dimPan.PenY(48), 150, 200);
        cartas[12].setBounds(dimPan.PenX(23), dimPan.PenY(68), 150, 200);
        cartas[13].setBounds(dimPan.PenX(35), dimPan.PenY(68), 150, 200);
        nCartas = 14;
        voltearCartas();
    }

    private void nivel7() {
        cartas[0].setBounds(dimPan.PenX(23), dimPan.PenY(8), 150, 200);
        cartas[1].setBounds(dimPan.PenX(35), dimPan.PenY(8), 150, 200);
        cartas[2].setBounds(dimPan.PenX(47), dimPan.PenY(8), 150, 200);
        cartas[3].setBounds(dimPan.PenX(5), dimPan.PenY(28), 150, 200);
        cartas[4].setBounds(dimPan.PenX(17), dimPan.PenY(28), 150, 200);
        cartas[5].setBounds(dimPan.PenX(29), dimPan.PenY(28), 150, 200);
        cartas[6].setBounds(dimPan.PenX(41), dimPan.PenY(28), 150, 200);
        cartas[7].setBounds(dimPan.PenX(53), dimPan.PenY(28), 150, 200);

        cartas[8].setBounds(dimPan.PenX(5), dimPan.PenY(48), 150, 200);
        cartas[9].setBounds(dimPan.PenX(17), dimPan.PenY(48), 150, 200);
        cartas[10].setBounds(dimPan.PenX(29), dimPan.PenY(48), 150, 200);
        cartas[11].setBounds(dimPan.PenX(41), dimPan.PenY(48), 150, 200);
        cartas[12].setBounds(dimPan.PenX(53), dimPan.PenY(48), 150, 200);
        cartas[13].setBounds(dimPan.PenX(23), dimPan.PenY(68), 150, 200);
        cartas[14].setBounds(dimPan.PenX(35), dimPan.PenY(68), 150, 200);
        cartas[15].setBounds(dimPan.PenX(47), dimPan.PenY(68), 150, 200);
        nCartas = 16;
        voltearCartas();
    }

    private void nivel8() {
        cartas[0].setBounds(dimPan.PenX(11), dimPan.PenY(8), 150, 200);
        cartas[1].setBounds(dimPan.PenX(23), dimPan.PenY(8), 150, 200);
        cartas[2].setBounds(dimPan.PenX(35), dimPan.PenY(8), 150, 200);
        cartas[3].setBounds(dimPan.PenX(47), dimPan.PenY(8), 150, 200);
        cartas[4].setBounds(dimPan.PenX(5), dimPan.PenY(28), 150, 200);
        cartas[5].setBounds(dimPan.PenX(17), dimPan.PenY(28), 150, 200);
        cartas[6].setBounds(dimPan.PenX(29), dimPan.PenY(28), 150, 200);
        cartas[7].setBounds(dimPan.PenX(41), dimPan.PenY(28), 150, 200);
        cartas[8].setBounds(dimPan.PenX(53), dimPan.PenY(28), 150, 200);

        cartas[9].setBounds(dimPan.PenX(5), dimPan.PenY(48), 150, 200);
        cartas[10].setBounds(dimPan.PenX(17), dimPan.PenY(48), 150, 200);
        cartas[11].setBounds(dimPan.PenX(29), dimPan.PenY(48), 150, 200);
        cartas[12].setBounds(dimPan.PenX(41), dimPan.PenY(48), 150, 200);
        cartas[13].setBounds(dimPan.PenX(53), dimPan.PenY(48), 150, 200);
        cartas[14].setBounds(dimPan.PenX(11), dimPan.PenY(68), 150, 200);
        cartas[15].setBounds(dimPan.PenX(23), dimPan.PenY(68), 150, 200);
        cartas[16].setBounds(dimPan.PenX(35), dimPan.PenY(68), 150, 200);
        cartas[17].setBounds(dimPan.PenX(47), dimPan.PenY(68), 150, 200);
        nCartas = 18;
        voltearCartas();
    }

    private void nivel9() {
        cartas[0].setBounds(dimPan.PenX(5), dimPan.PenY(8), 150, 200);
        cartas[1].setBounds(dimPan.PenX(17), dimPan.PenY(8), 150, 200);
        cartas[2].setBounds(dimPan.PenX(29), dimPan.PenY(8), 150, 200);
        cartas[3].setBounds(dimPan.PenX(41), dimPan.PenY(8), 150, 200);
        cartas[4].setBounds(dimPan.PenX(53), dimPan.PenY(8), 150, 200);
        cartas[5].setBounds(dimPan.PenX(5), dimPan.PenY(28), 150, 200);
        cartas[6].setBounds(dimPan.PenX(17), dimPan.PenY(28), 150, 200);
        cartas[7].setBounds(dimPan.PenX(29), dimPan.PenY(28), 150, 200);
        cartas[8].setBounds(dimPan.PenX(41), dimPan.PenY(28), 150, 200);
        cartas[9].setBounds(dimPan.PenX(53), dimPan.PenY(28), 150, 200);

        cartas[10].setBounds(dimPan.PenX(5), dimPan.PenY(48), 150, 200);
        cartas[11].setBounds(dimPan.PenX(17), dimPan.PenY(48), 150, 200);
        cartas[12].setBounds(dimPan.PenX(29), dimPan.PenY(48), 150, 200);
        cartas[13].setBounds(dimPan.PenX(41), dimPan.PenY(48), 150, 200);
        cartas[14].setBounds(dimPan.PenX(53), dimPan.PenY(48), 150, 200);
        cartas[15].setBounds(dimPan.PenX(5), dimPan.PenY(68), 150, 200);
        cartas[16].setBounds(dimPan.PenX(17), dimPan.PenY(68), 150, 200);
        cartas[17].setBounds(dimPan.PenX(29), dimPan.PenY(68), 150, 200);
        cartas[18].setBounds(dimPan.PenX(41), dimPan.PenY(68), 150, 200);
        cartas[19].setBounds(dimPan.PenX(53), dimPan.PenY(68), 150, 200);
        nCartas = 20;
        voltearCartas();
    }

    public void nivel10() {
        cartas[0].setBounds(dimPan.PenX(2), dimPan.PenY(8), 125, 175);
        cartas[1].setBounds(dimPan.PenX(11), dimPan.PenY(8), 125, 175);
        cartas[2].setBounds(dimPan.PenX(20), dimPan.PenY(8), 125, 175);
        cartas[3].setBounds(dimPan.PenX(29), dimPan.PenY(8), 125, 175);
        cartas[4].setBounds(dimPan.PenX(38), dimPan.PenY(8), 125, 175);
        cartas[5].setBounds(dimPan.PenX(47), dimPan.PenY(18), 125, 175);
        cartas[6].setBounds(dimPan.PenX(2), dimPan.PenY(28), 125, 175);
        cartas[7].setBounds(dimPan.PenX(11), dimPan.PenY(28), 125, 175);
        cartas[8].setBounds(dimPan.PenX(20), dimPan.PenY(28), 125, 175);
        cartas[9].setBounds(dimPan.PenX(29), dimPan.PenY(28), 125, 175);
        cartas[10].setBounds(dimPan.PenX(38), dimPan.PenY(28), 125, 175);

        cartas[11].setBounds(dimPan.PenX(2), dimPan.PenY(48), 125, 175);
        cartas[12].setBounds(dimPan.PenX(11), dimPan.PenY(48), 125, 175);
        cartas[13].setBounds(dimPan.PenX(20), dimPan.PenY(48), 125, 175);
        cartas[14].setBounds(dimPan.PenX(29), dimPan.PenY(48), 125, 175);
        cartas[15].setBounds(dimPan.PenX(38), dimPan.PenY(48), 125, 175);
        cartas[16].setBounds(dimPan.PenX(47), dimPan.PenY(58), 125, 175);
        cartas[17].setBounds(dimPan.PenX(2), dimPan.PenY(68), 125, 175);
        cartas[18].setBounds(dimPan.PenX(11), dimPan.PenY(68), 125, 175);
        cartas[19].setBounds(dimPan.PenX(20), dimPan.PenY(68), 125, 175);
        cartas[20].setBounds(dimPan.PenX(29), dimPan.PenY(68), 125, 175);
        cartas[21].setBounds(dimPan.PenX(38), dimPan.PenY(68), 125, 175);
    }

    private void nivel11() {
        cartas[0].setBounds(dimPan.PenX(2), dimPan.PenY(8), 125, 175);
        cartas[1].setBounds(dimPan.PenX(11), dimPan.PenY(8), 125, 175);
        cartas[2].setBounds(dimPan.PenX(20), dimPan.PenY(8), 125, 175);
        cartas[3].setBounds(dimPan.PenX(29), dimPan.PenY(8), 125, 175);
        cartas[4].setBounds(dimPan.PenX(38), dimPan.PenY(8), 125, 175);
        cartas[5].setBounds(dimPan.PenX(47), dimPan.PenY(8), 125, 175);
        cartas[6].setBounds(dimPan.PenX(2), dimPan.PenY(28), 125, 175);
        cartas[7].setBounds(dimPan.PenX(11), dimPan.PenY(28), 125, 175);
        cartas[8].setBounds(dimPan.PenX(20), dimPan.PenY(28), 125, 175);
        cartas[9].setBounds(dimPan.PenX(29), dimPan.PenY(28), 125, 175);
        cartas[10].setBounds(dimPan.PenX(38), dimPan.PenY(28), 125, 175);
        cartas[11].setBounds(dimPan.PenX(47), dimPan.PenY(28), 125, 175);

        cartas[12].setBounds(dimPan.PenX(2), dimPan.PenY(48), 125, 175);
        cartas[13].setBounds(dimPan.PenX(11), dimPan.PenY(48), 125, 175);
        cartas[14].setBounds(dimPan.PenX(20), dimPan.PenY(48), 125, 175);
        cartas[15].setBounds(dimPan.PenX(29), dimPan.PenY(48), 125, 175);
        cartas[16].setBounds(dimPan.PenX(38), dimPan.PenY(48), 125, 175);
        cartas[17].setBounds(dimPan.PenX(47), dimPan.PenY(48), 125, 175);
        cartas[18].setBounds(dimPan.PenX(2), dimPan.PenY(68), 125, 175);
        cartas[19].setBounds(dimPan.PenX(11), dimPan.PenY(68), 125, 175);
        cartas[20].setBounds(dimPan.PenX(20), dimPan.PenY(68), 125, 175);
        cartas[21].setBounds(dimPan.PenX(29), dimPan.PenY(68), 125, 175);
        cartas[22].setBounds(dimPan.PenX(38), dimPan.PenY(68), 125, 175);
        cartas[23].setBounds(dimPan.PenX(47), dimPan.PenY(68), 125, 175);
    }

    private void nivel12() {
        cartas[0].setBounds(dimPan.PenX(2), dimPan.PenY(8), 125, 175);
        cartas[1].setBounds(dimPan.PenX(11), dimPan.PenY(8), 125, 175);
        cartas[2].setBounds(dimPan.PenX(20), dimPan.PenY(8), 125, 175);
        cartas[3].setBounds(dimPan.PenX(29), dimPan.PenY(8), 125, 175);
        cartas[4].setBounds(dimPan.PenX(38), dimPan.PenY(8), 125, 175);
        cartas[5].setBounds(dimPan.PenX(47), dimPan.PenY(8), 125, 175);
        cartas[6].setBounds(dimPan.PenX(56), dimPan.PenY(8), 125, 175);
        cartas[7].setBounds(dimPan.PenX(2), dimPan.PenY(28), 125, 175);
        cartas[8].setBounds(dimPan.PenX(11), dimPan.PenY(28), 125, 175);
        cartas[9].setBounds(dimPan.PenX(20), dimPan.PenY(28), 125, 175);
        cartas[10].setBounds(dimPan.PenX(29), dimPan.PenY(28), 125, 175);
        cartas[11].setBounds(dimPan.PenX(38), dimPan.PenY(28), 125, 175);
        cartas[12].setBounds(dimPan.PenX(47), dimPan.PenY(28), 125, 175);
        cartas[13].setBounds(dimPan.PenX(56), dimPan.PenY(28), 125, 175);

        cartas[14].setBounds(dimPan.PenX(2), dimPan.PenY(48), 125, 175);
        cartas[15].setBounds(dimPan.PenX(11), dimPan.PenY(48), 125, 175);
        cartas[16].setBounds(dimPan.PenX(20), dimPan.PenY(48), 125, 175);
        cartas[17].setBounds(dimPan.PenX(29), dimPan.PenY(48), 125, 175);
        cartas[18].setBounds(dimPan.PenX(38), dimPan.PenY(48), 125, 175);
        cartas[19].setBounds(dimPan.PenX(47), dimPan.PenY(48), 125, 175);
        cartas[20].setBounds(dimPan.PenX(2), dimPan.PenY(68), 125, 175);
        cartas[21].setBounds(dimPan.PenX(11), dimPan.PenY(68), 125, 175);
        cartas[22].setBounds(dimPan.PenX(20), dimPan.PenY(68), 125, 175);
        cartas[23].setBounds(dimPan.PenX(29), dimPan.PenY(68), 125, 175);
        cartas[24].setBounds(dimPan.PenX(38), dimPan.PenY(68), 125, 175);
        cartas[25].setBounds(dimPan.PenX(47), dimPan.PenY(68), 125, 175);
    }

    private void nivel13() {
        cartas[0].setBounds(dimPan.PenX(2), dimPan.PenY(8), 125, 175);
        cartas[1].setBounds(dimPan.PenX(11), dimPan.PenY(8), 125, 175);
        cartas[2].setBounds(dimPan.PenX(20), dimPan.PenY(8), 125, 175);
        cartas[3].setBounds(dimPan.PenX(29), dimPan.PenY(8), 125, 175);
        cartas[4].setBounds(dimPan.PenX(38), dimPan.PenY(8), 125, 175);
        cartas[5].setBounds(dimPan.PenX(47), dimPan.PenY(8), 125, 175);
        cartas[6].setBounds(dimPan.PenX(56), dimPan.PenY(8), 125, 175);
        cartas[7].setBounds(dimPan.PenX(2), dimPan.PenY(28), 125, 175);
        cartas[8].setBounds(dimPan.PenX(11), dimPan.PenY(28), 125, 175);
        cartas[9].setBounds(dimPan.PenX(20), dimPan.PenY(28), 125, 175);
        cartas[10].setBounds(dimPan.PenX(29), dimPan.PenY(28), 125, 175);
        cartas[11].setBounds(dimPan.PenX(38), dimPan.PenY(28), 125, 175);
        cartas[12].setBounds(dimPan.PenX(47), dimPan.PenY(28), 125, 175);
        cartas[13].setBounds(dimPan.PenX(56), dimPan.PenY(28), 125, 175);

        cartas[14].setBounds(dimPan.PenX(2), dimPan.PenY(48), 125, 175);
        cartas[15].setBounds(dimPan.PenX(11), dimPan.PenY(48), 125, 175);
        cartas[16].setBounds(dimPan.PenX(20), dimPan.PenY(48), 125, 175);
        cartas[17].setBounds(dimPan.PenX(29), dimPan.PenY(48), 125, 175);
        cartas[18].setBounds(dimPan.PenX(38), dimPan.PenY(48), 125, 175);
        cartas[19].setBounds(dimPan.PenX(47), dimPan.PenY(48), 125, 175);
        cartas[20].setBounds(dimPan.PenX(56), dimPan.PenY(48), 125, 175);
        cartas[21].setBounds(dimPan.PenX(2), dimPan.PenY(68), 125, 175);
        cartas[22].setBounds(dimPan.PenX(11), dimPan.PenY(68), 125, 175);
        cartas[23].setBounds(dimPan.PenX(20), dimPan.PenY(68), 125, 175);
        cartas[24].setBounds(dimPan.PenX(29), dimPan.PenY(68), 125, 175);
        cartas[25].setBounds(dimPan.PenX(38), dimPan.PenY(68), 125, 175);
        cartas[26].setBounds(dimPan.PenX(47), dimPan.PenY(68), 125, 175);
        cartas[27].setBounds(dimPan.PenX(56), dimPan.PenY(68), 125, 175);

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == botonHome) {
            hilo1.stop();
            if (segundos <= 5) {
                sonidoFaltan5Minutos.detenerAudio();
            }
            sonido.efectoDeSonido("risa1");
            //guargar
            /*
            if (copiaLista.size() <= 1 && !nombreJugador.equalsIgnoreCase("Entrenamiento")) {
                archivo.serializarDatos(ruta + nombreJugador, (new String[]{"" + nivel, "" + vidas, "" + numeroDigitos, "" + c}));
            }*/
            this.setVisible(false);
        }
        for (int i = 0; i < cartas.length; i++) {
            if (e.getSource() == cartas[i]) {
                if (CartasVoltedas[i] != true) {
                    if (contador2 != 2) {
                        cartas[i].setIcon(null);
                        cartas[i].setFont(new Font("Forte", 8, 68));
                        cartas[i].setForeground(Color.WHITE);
                        cartas[i].setText("" + numeros[i]);
                        numero1 = Integer.parseUnsignedInt(cartas[i].getText());
                        listaAux[contador2] = numero1;
                        //System.out.println("j4cartaspares.ventanaCartasPares.mousePressed()");
                        //CartasVoltedas[i] = true;

                        listaAux2[contador2] = i;
                        contador2++;
                    }
                }
            }
        }
    }

    private void PasarDeNivel() {
        if (copiaLista.size() > 1) {
            aumentarPuntos();
        }
        if (Contador == nCartas / 2) {
            if (segundos <= 5) {
                sonidoFaltan5Minutos.detenerAudio();
            }
            if (nivel == 2) {
                switch (c) {
                    case 1: {
                        nivel2();
                        GuardarImg();
                        labelNivel.setText("NIVEL: " + nivel);
                        logicaJuego.GenerarNumerosAleatorios(6, 10);
                        numeros = logicaJuego.getLista();
                        segundos = 15;
                        break;
                    }
                    case 2: {
                        nivel2();
                        GuardarImg();

                        logicaJuego.GenerarNumerosAleatorios(6, 100);
                        numeros = logicaJuego.getLista();
                        segundos = 15;
                        break;
                    }
                    default: {
                        c = 0;
                        if (copiaLista.size() <= 1) {
                            nivel++;
                        } else {
                            aciertosMultijugador++;
                            labelAciertos.setText("Aciertos: " + aciertosMultijugador);
                        }
                        break;
                    }
                }

            }
            if (nivel == 3) {
                switch (c) {
                    case 1: {
                        nivel3();
                        GuardarImg();
                        labelNivel.setText("NIVEL: " + nivel);
                        logicaJuego.GenerarNumerosAleatorios(8, 10);
                        numeros = logicaJuego.getLista();
                        segundos = 15;
                        break;
                    }
                    case 2: {
                        nivel3();
                        GuardarImg();
                        logicaJuego.GenerarNumerosAleatorios(8, 100);
                        numeros = logicaJuego.getLista();
                        segundos = 15;
                        break;
                    }
                    default: {
                        c = 1;
                        if (copiaLista.size() <= 1) {
                            nivel++;
                        } else {
                            aciertosMultijugador++;
                            labelAciertos.setText("Aciertos: " + aciertosMultijugador);
                        }
                        break;
                    }
                }
            }
            if (nivel == 4) {
                switch (c) {
                    case 1: {
                        nivel4();
                        GuardarImg();
                        labelNivel.setText("NIVEL: " + nivel);
                        logicaJuego.GenerarNumerosAleatorios(10, 10);
                        numeros = logicaJuego.getLista();
                        segundos = 15;
                        break;
                    }
                    case 2: {
                        nivel4();
                        GuardarImg();
                        logicaJuego.GenerarNumerosAleatorios(10, 1000);
                        numeros = logicaJuego.getLista();
                        segundos = 15;
                        break;
                    }
                    default: {
                        c = 1;
                        if (copiaLista.size() <= 1) {
                            nivel++;
                        } else {
                            aciertosMultijugador++;
                            labelAciertos.setText("Aciertos: " + aciertosMultijugador);
                        }
                        break;
                    }
                }
            }
            if (nivel == 5) {
                switch (c) {
                    case 1: {
                        nivel5();
                        GuardarImg();
                        labelNivel.setText("NIVEL: " + nivel);
                        logicaJuego.GenerarNumerosAleatorios(12, 100);
                        numeros = logicaJuego.getLista();
                        segundos = 15;
                        break;
                    }
                    case 2: {
                        nivel5();
                        GuardarImg();
                        logicaJuego.GenerarNumerosAleatorios(12, 1000);
                        numeros = logicaJuego.getLista();
                        segundos = 15;
                        break;
                    }
                    default: {
                        c = 1;
                        if (copiaLista.size() <= 1) {
                            nivel++;
                        } else {
                            aciertosMultijugador++;
                            labelAciertos.setText("Aciertos: " + aciertosMultijugador);
                        }
                        break;
                    }
                }
            }
            if (nivel == 6) {
                switch (c) {
                    case 1: {
                        nivel6();
                        GuardarImg();
                        labelNivel.setText("NIVEL: " + nivel);
                        logicaJuego.GenerarNumerosAleatorios(14, 100);
                        numeros = logicaJuego.getLista();
                        segundos = 15;
                        break;
                    }
                    case 2: {
                        nivel6();
                        GuardarImg();
                        logicaJuego.GenerarNumerosAleatorios(14, 1000);
                        numeros = logicaJuego.getLista();
                        segundos = 15;
                        break;
                    }
                    default: {
                        c = 1;
                        if (copiaLista.size() <= 1) {
                            nivel++;
                        } else {
                            aciertosMultijugador++;
                            labelAciertos.setText("Aciertos: " + aciertosMultijugador);
                        }
                        break;
                    }
                }
            }
            if (nivel == 7) {
                switch (c) {
                    case 1: {
                        nivel7();
                        GuardarImg();
                        labelNivel.setText("NIVEL: " + nivel);
                        logicaJuego.GenerarNumerosAleatorios(16, 100);
                        numeros = logicaJuego.getLista();
                        segundos = 15;
                        break;
                    }
                    case 2: {
                        nivel7();
                        GuardarImg();
                        logicaJuego.GenerarNumerosAleatorios(16, 1000);
                        numeros = logicaJuego.getLista();
                        segundos = 15;
                        break;
                    }
                    default: {
                        c = 1;
                        if (copiaLista.size() <= 1) {
                            nivel++;
                        } else {
                            aciertosMultijugador++;
                            labelAciertos.setText("Aciertos: " + aciertosMultijugador);
                        }
                        break;
                    }
                }
            }
            if (nivel == 8) {
                switch (c) {
                    case 1: {
                        nivel8();
                        GuardarImg();
                        labelNivel.setText("NIVEL: " + nivel);
                        logicaJuego.GenerarNumerosAleatorios(18, 100);
                        numeros = logicaJuego.getLista();
                        segundos = 15;
                        break;
                    }
                    case 2: {
                        nivel8();
                        GuardarImg();
                        logicaJuego.GenerarNumerosAleatorios(18, 1000);
                        numeros = logicaJuego.getLista();
                        segundos = 15;
                        break;
                    }
                    default: {
                        c = 1;
                        if (copiaLista.size() <= 1) {
                            nivel++;
                        } else {
                            aciertosMultijugador++;
                            labelAciertos.setText("Aciertos: " + aciertosMultijugador);
                        }
                        break;
                    }
                }
            }
            if (nivel == 9) {
                switch (c) {
                    case 1: {
                        nivel9();
                        GuardarImg();
                        labelNivel.setText("NIVEL: " + nivel);
                        logicaJuego.GenerarNumerosAleatorios(20, 100);
                        numeros = logicaJuego.getLista();
                        segundos = 15;
                        break;
                    }
                    case 2: {
                        nivel9();
                        GuardarImg();
                        logicaJuego.GenerarNumerosAleatorios(20, 1000);
                        numeros = logicaJuego.getLista();
                        segundos = 15;
                        break;
                    }
                    default: {
                        c = 1;
                        if (copiaLista.size() <= 1) {
                            nivel++;
                        } else {
                            aciertosMultijugador++;
                            labelAciertos.setText("Acierots: " + aciertosMultijugador);
                        }
                        ventanaCampeon c1 = new ventanaCampeon('4', copiaLista, nivel);

                        c1.setVisible(true);
                        break;
                    }
                }
            }
            c++;
            Contador = 0;
        }
    }

    private void voltearCartas() {
        for (int y = 0; y < CartasVoltedas.length; y++) {
            CartasVoltedas[y] = false;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        sonido.efectoDeSonido("carta");
        if (contador2 == 2) {
            if (listaAux2[0] != listaAux2[1]) {

                if ((listaAux[0] == listaAux[1])) {
                    Contador++;
                    contador2 = 0;

                    CartasVoltedas[listaAux2[0]] = true;
                    CartasVoltedas[listaAux2[1]] = true;
                    System.out.println("volteado");
                }
                PasarDeNivel();

            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
///System.out.println("j4cartaspares.ventanaCartasPares.mouseExited()888");
        for (int i = 0; i < cartas.length; i++) {
            if (e.getSource() == cartas[i]) {
                if (contador2 == 2) {
                    if (CartasVoltedas[listaAux2[0]] == false && CartasVoltedas[listaAux2[1]] == false) {
                        if (listaAux[0] != listaAux[1]) {
                            cartas[listaAux2[0]].setIcon(new ImageIcon("src\\j4cartaspares\\images\\227670523_3.jpg"));
                            cartas[listaAux2[1]].setIcon(new ImageIcon("src\\j4cartaspares\\images\\227670523_3.jpg"));

                            contador2 = 0;
                        } else {
                            contador2 = 0;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        if (copiaLista.size() <= 1) {
            while (segundos > 0) {

                try {
                    labelCronometro.setText("" + segundos);

                    Thread.sleep(1000);
                    segundos--;

                    if (segundos < 1) {
                        vidas--;
                        labelVidas.setText("VIDAS: " + vidas);
                        segundos = 15;
                        sonidoFaltan5Minutos.detenerAudio();
                    }
                    if (vidas < 1) {
                        perdedor = new ventanaPerdedor('4', copiaLista, nivel);
                        perdedor.setVisible(true);
                        this.setVisible(false);
                        sonidoFaltan5Minutos.detenerAudio();
                        hilo1.stop();
                    }
                    //guargar
                    /*
                    if (copiaLista.size() <= 1 && !nombreJugador.equalsIgnoreCase("Entrenamiento")) {
                        archivo.serializarDatos(ruta + nombreJugador, (new String[]{"" + nivel, "" + vidas, "" + numeroDigitos, "" + c}));
                    }*/

                } catch (InterruptedException ex) {

                }
                if (segundos == 5) {
                    sonidoFaltan5Minutos.reproduceFondo("faltan 5 segundos");
                }
            }
        } else {
            try {
                //segundosRestantes = tiempoMultijugador;
                //Aquí inicializamos al primer jugador
                while (segundosRestantes > 0 && jugador < copiaLista.size()) {
                    if (segundosRestantes == tiempoMultijugador) {
                        //botonSi.setEnabled(false);
                        //botonNo.setEnabled(false);
                        labelCronometroMultijugador.setText("Atento...!!!");
                        Thread.sleep(3000);
                        //botonSi.setEnabled(true);
                        //botonNo.setEnabled(true);

                        labelCronometroMultijugador.setText("Ya!");
                    }

                    Thread.sleep(1000);
                    labelCronometroMultijugador.setText("" + segundosRestantes);
                    segundosRestantes--;
                    if (segundosRestantes <= 0 && jugador < copiaLista.size() - 1) {
                        //Guardamos los datos del jugador (Temporal)
                        jugador++;
                        //botonYa.doClick();

                        datosJugadores[jugador - 1][1] = "" + aciertosMultijugador;
                        datosJugadores[jugador - 1][2] = "" + segundosRestantes;
                        aciertosMultijugador = 0;
                        segundosRestantes = tiempoMultijugador;

                        nombreJugador = datosJugadores[jugador][0];
                        labelJugadorTurno.setText("Turno de: " + nombreJugador);
                        labelCronometroMultijugador.setText("" + segundosRestantes);
                        labelAciertos.setText("ACIERTOS: " + aciertosMultijugador);

                        labelNombreJugador.setText(nombreJugador);
                        segundosRestantes = tiempoMultijugador;
                    }
                }
                datosJugadores[jugador][1] = "" + aciertosMultijugador;
                datosJugadores[jugador][2] = "" + segundosRestantes;

                resultadosMultijugador = new ventanaResultadosMultijugador(datosJugadores);
                resultadosMultijugador.setVisible(true);
                this.setVisible(false);
                labelCronometroMultijugador.setText("FIN");

            } catch (Exception e) {
                System.out.println("Se produjo un error " + e);
            }
        }
    }

    private void aumentarPuntos() {
        puntosAuxiliar = puntosAuxiliar + 100;
        labelAciertos.setText(puntosAuxiliar + "");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //inicio pausar
        if (e.getSource() == botonPausa && pausado == false) {
            sonidoFaltan5Minutos.detenerAudio();
            hilo1.stop();
            pausado = true;
            botonPausa.setText("Reanudar");
            panelCartas.setVisible(false);
            labelPausa.setVisible(true);

        } else if (e.getSource() == botonPausa && pausado == true) {
            if (segundos <= 5) {
                sonidoFaltan5Minutos.reproduceFondo("faltan 5 segundos");
            }
            hilo1 = new Thread(this);
            hilo1.start();
            pausado = false;
            botonPausa.setText("Pusar");
            panelCartas.setVisible(true);
            labelPausa.setVisible(false);
        } // fin boton pausar
        if (e.getSource() == botonGuardar) {
            if (botonPausa.getText().equalsIgnoreCase("Pausar")) {
                botonPausa.doClick();
            }
            salvarArchivo("j4");
        }
    }

    private void salvarArchivo(String extension) {//Este metodo guarda la partida
        archivo = new gestionArchivos();
        javax.swing.JFileChooser jF1 = new javax.swing.JFileChooser();
        String ruta = "";
        FileNameExtensionFilter filtro = new FileNameExtensionFilter(extension, extension);
        jF1.setFileFilter(filtro);
        try {
            if (jF1.showSaveDialog(null) == jF1.APPROVE_OPTION) {
                ruta = jF1.getSelectedFile().getAbsolutePath();
                System.out.println("Se guardó en: " + ruta);
                //archivo.serializarDatos(ruta + "." + extension, (new String[]{"" + nivel, "" + vidas, "" + numeroDigitos, "" + c}));
                archivo.serializarDatos(ruta + "." + extension, (new String[]{"" + nivel, "" + vidas, "" + numeroDigitos, "" + c}));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        botonPausa.doClick();
    }
}
