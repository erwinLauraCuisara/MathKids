/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package j3completaoperacion;
/**
 * Esta clase es la ventana del juego de completar la operacion (j3)
 * @Autores:  
 * Abasto Argote Gustavo, 
 * Laura Cuisara Erwin Harnaldo, 
 * Cayo Huaylla Ruddy, 
 * Ancari Iñiguez Abel
 * 
 */
//import com.sun.org.apache.bcel.internal.generic.SWITCH;
import complementos.*;
import dimensionesEscala.DimensionesPantalla;
import javax.swing.*;
import ventanaPrincipal.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ventanaCompletaOperaciones extends JInternalFrame implements ActionListener, Runnable {

    private JButton boton1, boton2, boton3, boton4, boton5, botonNum1, botonNum2, botonNum3, botonNum4, botonNum5, botonNum6, botonNum7, botonNum8, botonNum9, 
            botonNum10, botonNum11, botonNum12, botonNum13, botonMas, botonMenos, botonHome, botonYa, botonGuardar,botonMultiplicacion, botonDivision;
    private JLabel labelTitulo, labelPuntos, labelVidas, labelNivel, labelFondo, labelFondoGif, labelGif, labelBuenoMalo, labelIgual, 
            labelRespuesta, labelFondoBotones, labelPausa,
            labelCronometro, labelNombreJugador, labelJugadorTurno, labelAciertos, labelCronometroMultijugador;
    private int nivel, puntos, intentos, nivelMaximo;
    private byte vidas;
    
    private completaOperacion compOp;
    private puntosJugador puntosJugador;
    private nivelYVidas nivelVidas;

    private String copiaSigno = "?", copiaNumero = "?";
    private boolean acertoRespuesta;
    private DimensionesPantalla dimPan;

    //Ventanas
    private ventanaPerdedor perdedor;
    private ventanaCampeon ganador;

    private final int alturaBotonesCompletar;
    private final int alturaBotonesPropuesta;
    private final int ubicacionX1;
    private final int ubicacionX2;
    private int tamanioLetra, tamanioNumeros, tamLetraSuperior;

    private int segundos;
    private Thread hilo1;

    private ImageIcon imagenFondo, propiedadesFondo;
    private audioEfectos sonido, sonidoFaltan5Minutos;
    private JPanel panelNumeros;

    //carga de archivo
    private gestionArchivos archivo;

    //carga datos
    private String nombreJugador;
    //private String ruta;
    private java.util.List copiaLista;

    //Mitijugador
    private String[][] datosJugadores;
    private int tiempoMultijugador, segundosRestantes, jugador, aciertosMultijugador;
    private ventanaResultadosMultijugador resultadosMultijugador;
    //Pausa
    private JButton botonPausa;
    private boolean pausado;

    /**
     * 
     * @param listaJugadores Recibe la cantidad de jugadores
     * @param rutaArchivo Recibe la ruta de un archivo (En caso de que se cargue el archivo)
     * @param nivelMultijugador Recibe el nivel para el juego multijugador (desde el comboBox del la ventana Menu)
     */
    public ventanaCompletaOperaciones(java.util.List listaJugadores, String rutaArchivo, int nivelMultijugador) {

        copiaLista = new ArrayList();
        for (int i = 0; i < listaJugadores.size(); i++) {
            copiaLista.add(listaJugadores.get(i));
        }
        if (listaJugadores.size() <= 1) {

            nombreJugador = (String) listaJugadores.get(0);

        } else {
            nombreJugador = listaJugadores.get(0).toString();
        }
        
        dimPan = new DimensionesPantalla();
        this.setIconifiable(true);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(0, 0, dimPan.horizontal(), dimPan.vertical());
        setTitle("Completa la Operación");
        getContentPane().setBackground(Color.yellow);

        //FONDO DE PANTALLA
        labelFondo = new JLabel();
        labelFondo.setBounds(0, 0, dimPan.horizontal(), dimPan.vertical());
        imagenFondo = new ImageIcon(getClass().getResource("images/oscuridad.jpg"));
        propiedadesFondo = new ImageIcon(imagenFondo.getImage().getScaledInstance(labelFondo.getWidth(), labelFondo.getHeight(), Image.SCALE_DEFAULT));
        labelFondo.setIcon(propiedadesFondo);
        add(labelFondo);
        
        botonGuardar = new JButton("Guardar");
        botonGuardar.setBounds(0, 0, dimPan.PenX(5), dimPan.PenY(2));
        botonGuardar.addActionListener(this);
        botonGuardar.setContentAreaFilled(false);
        botonGuardar.setForeground(Color.WHITE);
        labelFondo.add(botonGuardar);
        
        panelNumeros = new JPanel();
        panelNumeros.setBounds(0, 0, dimPan.horizontal(), dimPan.vertical());
        panelNumeros.setLayout(null);
        panelNumeros.setOpaque(false);
        labelFondo.add(panelNumeros);

        labelFondoGif = new JLabel();
        labelFondoGif.setBounds(0, 0, dimPan.horizontal(), dimPan.vertical());
        imagenFondo = new ImageIcon(getClass().getResource("images/lluvia (1).gif"));
        propiedadesFondo = new ImageIcon(imagenFondo.getImage().getScaledInstance(labelFondoGif.getWidth(), labelFondoGif.getHeight(), Image.SCALE_DEFAULT));
        labelFondoGif.setIcon(propiedadesFondo);
        labelFondo.add(labelFondoGif);

        labelGif = new JLabel();
        labelGif.setBounds(dimPan.PenX(90), dimPan.PenY(71), dimPan.PenX(7), dimPan.PenY(15));
        imagenFondo = new ImageIcon(getClass().getResource("images/nick01GIF.gif"));
        propiedadesFondo = new ImageIcon(imagenFondo.getImage().getScaledInstance(labelGif.getWidth(), labelGif.getHeight(), Image.SCALE_DEFAULT));
        labelGif.setIcon(propiedadesFondo);
        labelFondo.add(labelGif);
        
        
        //Cargar archivo
        nivelMaximo = 5;
        //ruta = "C:\\Users\\Gustavo\\Documents\\NetBeansProjects\\Juego\\src\\complementos\\BD\\juego3\\";
        if (copiaLista.size() <= 1) { // Si no es multijugador
                archivo = new gestionArchivos();
                if (!rutaArchivo.equals("")) {//Vacio es que no contiene ninguna ruta
                    
                    try {
                        nivel = Integer.parseInt(archivo.recuperaDatos(rutaArchivo)[0]); // int
                        puntos = Integer.parseInt(archivo.recuperaDatos(rutaArchivo)[1]); // int
                        vidas = Byte.parseByte(archivo.recuperaDatos(rutaArchivo)[2]); //Byte
                        intentos = Integer.parseInt(archivo.recuperaDatos(rutaArchivo)[3]);// si es un juego guardado debe ser mayor a 0 (int)
                    } catch (Exception e) {
                       JOptionPane.showMessageDialog(null, "El archivo no es compatible");
                        nivel = 1;
                        puntos = 0; // int
                        vidas = 3; //Byte
                        intentos = 0;// si es un juego guardado debe ser mayor a 0 (int)
                    }
                } else {
                    nivel = 1;
                    puntos = 0; // int
                    vidas = 3; //Byte
                    intentos = 0;// si es un juego guardado debe ser mayor a 0 (int)
                }
            
            segundos = 44; //Ojo
        } else { // Si es MULTIJUGADOR
            tiempoMultijugador = 5; //Establecemos los tiempos
            segundosRestantes = tiempoMultijugador;
            jugador = 0;
            nivel = nivelMultijugador;
            puntos = puntos = (nivelMultijugador - 1) * 800;
            vidas = 1;

            datosJugadores = new String[listaJugadores.size()][3];// cada fila tiene nombreJugador - puntos - tiempo
            for (int i = 0; i < copiaLista.size(); i++) {
                datosJugadores[i][0] = (String) copiaLista.get(i);//NombreJugador
                datosJugadores[i][1] = "" + 0; // Aciertos jugador
                datosJugadores[i][2] = "" + tiempoMultijugador; // tiempoJugador
            }

            labelJugadorTurno = new JLabel("TURNO DE: " + datosJugadores[0][0]); // Se carga al label el nombre del primer jugador
            labelJugadorTurno.setBounds(0, dimPan.PenY(1.5F), dimPan.PenX(33.333F), dimPan.PenY(10F));
            labelJugadorTurno.setFont(new Font("Courier New", 1, dimPan.tamanioLetra(30)));
            labelJugadorTurno.setHorizontalAlignment(SwingConstants.CENTER);
            labelJugadorTurno.setForeground(Color.ORANGE);
            labelFondo.add(labelJugadorTurno);

            labelAciertos = new JLabel("ACIERTOS: " + datosJugadores[0][1]); // Se carga al label el nombre del primer jugador
            labelAciertos.setBounds(dimPan.PenX(66.666F), dimPan.PenY(1.5F), dimPan.PenX(33.333F), dimPan.PenY(10F));
            labelAciertos.setFont(new Font("Courier New", 1, dimPan.tamanioLetra(30)));
            labelAciertos.setHorizontalAlignment(SwingConstants.CENTER);
            labelAciertos.setForeground(Color.orange);
            labelFondo.add(labelAciertos);

            labelCronometroMultijugador = new JLabel("Atento...");
            labelCronometroMultijugador.setBounds(dimPan.PenX(0), dimPan.PenY(97), dimPan.horizontal(), dimPan.PenY(10));
            labelCronometroMultijugador.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(90)));
            labelCronometroMultijugador.setHorizontalAlignment(SwingConstants.CENTER);
            labelCronometroMultijugador.setForeground(Color.WHITE);
            labelFondo.add(labelCronometroMultijugador);

            aciertosMultijugador = 0;

        }
        
        sonido = new audioEfectos();
        sonidoFaltan5Minutos = new audioEfectos();
        compOp = new completaOperacion(nivel);
        tamanioLetra = dimPan.tamanioLetra(70);
        tamanioNumeros = dimPan.tamanioLetra(80);
        tamLetraSuperior = dimPan.tamanioLetra(30);
        
        alturaBotonesCompletar = dimPan.PenY(30); // Botones juego
        alturaBotonesPropuesta = dimPan.PenY(60);

        if (nivel <= 3) {
            ubicacionX1 = dimPan.PenX(29.5F);
        } else {
            ubicacionX1 = dimPan.PenX(15.5F);
        }
        ubicacionX2 = dimPan.PenX(15.5F);

        labelTitulo = new JLabel("COMPLETA LA OPERACION");
        labelTitulo.setBounds(dimPan.PenX(0), dimPan.PenY(15), dimPan.PenX(100), dimPan.PenY(10));
        labelTitulo.setFont(new Font("Andale Mono", 1, 120));
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitulo.setForeground(Color.PINK);
        labelFondo.add(labelTitulo);

        labelVidas = new JLabel("VIDAS: " + vidas);
        labelVidas.setBounds(0, dimPan.PenY(1.5F), dimPan.PenX(33.333F), dimPan.PenY(10F));
        labelVidas.setFont(new Font("Courier New", 1, tamLetraSuperior));
        labelVidas.setHorizontalAlignment(SwingConstants.CENTER);
        labelVidas.setForeground(Color.orange);
        labelFondo.add(labelVidas);

        labelNivel = new JLabel("NIVEL: " + nivel);
        labelNivel.setBounds(dimPan.PenX(33.333F), dimPan.PenY(1.5F), dimPan.PenX(33.333F), dimPan.PenY(10F));
        labelNivel.setFont(new Font("Courier New", 1, tamLetraSuperior));
        labelNivel.setHorizontalAlignment(SwingConstants.CENTER);
        labelNivel.setForeground(Color.orange);
        labelFondo.add(labelNivel);

        labelPuntos = new JLabel("PUNTOS: " + puntos);
        labelPuntos.setBounds(dimPan.PenX(66.666F), dimPan.PenY(1.5F), dimPan.PenX(33.333F), dimPan.PenY(10F));
        labelPuntos.setFont(new Font("Courier New", 1, tamLetraSuperior));
        labelPuntos.setHorizontalAlignment(SwingConstants.CENTER);
        labelPuntos.setForeground(Color.orange);
        labelFondo.add(labelPuntos);

        labelNombreJugador = new JLabel(nombreJugador);
        labelNombreJugador.setBounds(dimPan.PenX(85F), dimPan.PenY(82.7F), dimPan.PenX(12), dimPan.PenY(5F));
        labelNombreJugador.setFont(new Font("Courier New", 1, dimPan.tamanioLetra(30)));
        labelNombreJugador.setHorizontalAlignment(SwingConstants.CENTER);
        labelNombreJugador.setForeground(Color.orange);
        labelFondo.add(labelNombreJugador);

        labelBuenoMalo = new JLabel("DALE!!!");
        labelBuenoMalo.setBounds(dimPan.PenX(0), dimPan.PenY(40), dimPan.PenX(100), dimPan.PenY(20));
        labelBuenoMalo.setFont(new Font("Andale Mono", 1, tamanioLetra));
        labelBuenoMalo.setForeground(Color.GREEN);
        labelBuenoMalo.setHorizontalAlignment(SwingConstants.CENTER);
        labelFondo.add(labelBuenoMalo);

        boton1 = new JButton("?");
        boton1.setBounds(ubicacionX1 + dimPan.PenX(0), alturaBotonesCompletar, dimPan.PenX(10), dimPan.PenY(10));
        boton1.addActionListener(this);
        boton1.setForeground(Color.white);
        boton1.setContentAreaFilled(false);
        panelNumeros.add(boton1);

        boton2 = new JButton("?");
        boton2.setBounds(ubicacionX1 + dimPan.PenX(10), alturaBotonesCompletar, dimPan.PenX(10), dimPan.PenY(10));
        boton2.addActionListener(this);
        boton2.setForeground(Color.GREEN);
        boton2.setContentAreaFilled(false);
        panelNumeros.add(boton2);

        boton3 = new JButton("?");
        boton3.setBounds(ubicacionX1 + dimPan.PenX(20), alturaBotonesCompletar, dimPan.PenX(10), dimPan.PenY(10));
        boton3.addActionListener(this);
        boton3.setForeground(Color.white);
        boton3.setContentAreaFilled(false);
        panelNumeros.add(boton3);

        boton4 = new JButton("?");
        boton4.setBounds(ubicacionX1 + dimPan.PenX(30), alturaBotonesCompletar, dimPan.PenX(10), dimPan.PenY(10));
        boton4.addActionListener(this);
        boton4.setForeground(Color.GREEN);
        boton4.setContentAreaFilled(false);
        panelNumeros.add(boton4);

        boton5 = new JButton("?");
        boton5.setBounds(ubicacionX1 + dimPan.PenX(40), alturaBotonesCompletar, dimPan.PenX(10), dimPan.PenY(10));
        boton5.addActionListener(this);
        boton5.setForeground(Color.white);
        boton5.setContentAreaFilled(false);
        panelNumeros.add(boton5);

        labelIgual = new JLabel("=");
        labelIgual.setFont(new Font("Andale Mono", 1, 70));
        labelIgual.setHorizontalAlignment(SwingConstants.CENTER);
        labelIgual.setForeground(Color.white);
        panelNumeros.add(labelIgual);

        labelRespuesta = new JLabel();
        labelRespuesta.setFont(new Font("Andale Mono", 1, 70));
        labelRespuesta.setHorizontalAlignment(SwingConstants.CENTER);
        labelRespuesta.setForeground(Color.white);
        panelNumeros.add(labelRespuesta);

        botonNum1 = new JButton();
        botonNum1.setFont(new Font("Andale mono", 1, tamanioNumeros));
        botonNum1.setBounds(ubicacionX2 + dimPan.PenX(0), alturaBotonesPropuesta, dimPan.PenX(10), dimPan.PenY(10));
        botonNum1.addActionListener(this);
        botonNum1.setForeground(Color.WHITE);
        botonNum1.setContentAreaFilled(false);
        labelFondo.add(botonNum1);

        botonNum2 = new JButton();
        botonNum2.setFont(new Font("Andale mono", 1, tamanioNumeros));
        botonNum2.setBounds(ubicacionX2 + dimPan.PenX(10), alturaBotonesPropuesta, dimPan.PenX(10), dimPan.PenY(10));
        botonNum2.addActionListener(this);
        botonNum2.setForeground(Color.WHITE);
        botonNum2.setContentAreaFilled(false);
        labelFondo.add(botonNum2);

        botonNum3 = new JButton();
        botonNum3.setFont(new Font("Andale mono", 1, tamanioNumeros));
        botonNum3.setBounds(ubicacionX2 + dimPan.PenX(20), alturaBotonesPropuesta, dimPan.PenX(10), dimPan.PenY(10));
        botonNum3.addActionListener(this);
        botonNum3.setForeground(Color.WHITE);
        botonNum3.setContentAreaFilled(false);
        labelFondo.add(botonNum3);

        botonNum4 = new JButton();
        botonNum4.setFont(new Font("Andale mono", 1, tamanioNumeros));
        botonNum4.setBounds(ubicacionX2 + dimPan.PenX(30), alturaBotonesPropuesta, dimPan.PenX(10), dimPan.PenY(10));
        botonNum4.addActionListener(this);
        botonNum4.setForeground(Color.WHITE);
        botonNum4.setContentAreaFilled(false);
        labelFondo.add(botonNum4);

        botonNum5 = new JButton();
        botonNum5.setFont(new Font("Andale mono", 1, tamanioNumeros));
        botonNum5.setBounds(ubicacionX2 + dimPan.PenX(40), alturaBotonesPropuesta, dimPan.PenX(10), dimPan.PenY(10));
        botonNum5.addActionListener(this);
        botonNum5.setForeground(Color.WHITE);
        botonNum5.setContentAreaFilled(false);
        labelFondo.add(botonNum5);

        botonNum6 = new JButton("?");
        botonNum6.setFont(new Font("Andale mono", 1, tamanioNumeros));
        botonNum6.setBounds(ubicacionX2 + dimPan.PenX(50), alturaBotonesPropuesta, dimPan.PenX(10), dimPan.PenY(10));
        botonNum6.addActionListener(this);
        botonNum6.setForeground(Color.WHITE);
        botonNum6.setContentAreaFilled(false);
        labelFondo.add(botonNum6);

        botonNum7 = new JButton("?");
        botonNum7.setFont(new Font("Andale mono", 1, tamanioNumeros));
        botonNum7.setBounds(ubicacionX2 + dimPan.PenX(60), alturaBotonesPropuesta, dimPan.PenX(10), dimPan.PenY(10));
        botonNum7.addActionListener(this);
        botonNum7.setForeground(Color.WHITE);
        botonNum7.setContentAreaFilled(false);
        labelFondo.add(botonNum7);

        //SEGUNDA FILA DE NUMEROS
        botonMas = new JButton("+");
        botonMas.setFont(new Font("Andale mono", 1, tamanioNumeros));
        botonMas.setBounds(ubicacionX2 + dimPan.PenX(65), alturaBotonesPropuesta + dimPan.PenY(10), dimPan.PenX(10), dimPan.PenY(10));
        botonMas.addActionListener(this);
        botonMas.setForeground(Color.GREEN);
        botonMas.setContentAreaFilled(false);
        labelFondo.add(botonMas);

        botonNum8 = new JButton("?");
        botonNum8.setFont(new Font("Andale mono", 1, tamanioNumeros));
        botonNum8.setBounds(ubicacionX2 + dimPan.PenX(5), alturaBotonesPropuesta + dimPan.PenY(10), dimPan.PenX(10), dimPan.PenY(10));
        botonNum8.addActionListener(this);
        botonNum8.setForeground(Color.WHITE);
        botonNum8.setContentAreaFilled(false);
        labelFondo.add(botonNum8);

        botonNum9 = new JButton("?");
        botonNum9.setFont(new Font("Andale mono", 1, tamanioNumeros));
        botonNum9.setBounds(ubicacionX2 + dimPan.PenX(15), alturaBotonesPropuesta + dimPan.PenY(10), dimPan.PenX(10), dimPan.PenY(10));
        botonNum9.addActionListener(this);
        botonNum9.setForeground(Color.WHITE);
        botonNum9.setContentAreaFilled(false);
        labelFondo.add(botonNum9);

        botonNum10 = new JButton("?");
        botonNum10.setFont(new Font("Andale mono", 1, tamanioNumeros));
        botonNum10.setBounds(ubicacionX2 + dimPan.PenX(25), alturaBotonesPropuesta + dimPan.PenY(10), dimPan.PenX(10), dimPan.PenY(10));
        botonNum10.addActionListener(this);
        botonNum10.setForeground(Color.WHITE);
        botonNum10.setContentAreaFilled(false);
        labelFondo.add(botonNum10);

        botonNum11 = new JButton("?");
        botonNum11.setFont(new Font("Andale mono", 1, tamanioNumeros));
        botonNum11.setBounds(ubicacionX2 + dimPan.PenX(35), alturaBotonesPropuesta + dimPan.PenY(10), dimPan.PenX(10), dimPan.PenY(10));
        botonNum11.addActionListener(this);
        botonNum11.setForeground(Color.WHITE);
        botonNum11.setContentAreaFilled(false);
        labelFondo.add(botonNum11);

        botonNum12 = new JButton("?");
        botonNum12.setFont(new Font("Andale mono", 1, tamanioNumeros));
        botonNum12.setBounds(ubicacionX2 + dimPan.PenX(45), alturaBotonesPropuesta + dimPan.PenY(10), dimPan.PenX(10), dimPan.PenY(10));
        botonNum12.addActionListener(this);
        botonNum12.setForeground(Color.WHITE);
        botonNum12.setContentAreaFilled(false);
        labelFondo.add(botonNum12);

        botonNum13 = new JButton("?");
        botonNum13.setFont(new Font("Andale mono", 1, tamanioNumeros));
        botonNum13.setBounds(ubicacionX2 + dimPan.PenX(55), alturaBotonesPropuesta + dimPan.PenY(10), dimPan.PenX(10), dimPan.PenY(10));
        botonNum13.addActionListener(this);
        botonNum13.setForeground(Color.WHITE);
        botonNum13.setContentAreaFilled(false);
        labelFondo.add(botonNum13);

        botonMenos = new JButton("-");
        botonMenos.setFont(new Font("Andale mono", 1, tamanioNumeros));
        botonMenos.setBounds(ubicacionX2 + dimPan.PenX(-5), alturaBotonesPropuesta + dimPan.PenY(10), dimPan.PenX(10), dimPan.PenY(10));
        botonMenos.addActionListener(this);
        botonMenos.setForeground(Color.GREEN);
        botonMenos.setContentAreaFilled(false);
        labelFondo.add(botonMenos);

        botonHome = new JButton("HOME");
        botonHome.setBounds(dimPan.PenX(5), dimPan.PenY(87), dimPan.PenX(12), dimPan.PenY(7));
        botonHome.addActionListener(this);
        botonHome.setForeground(Color.YELLOW);
        botonHome.setBackground(Color.red);
        labelFondo.add(botonHome);

        botonYa = new JButton("YA!");
        botonYa.setBounds(dimPan.PenX(85), dimPan.PenY(87), dimPan.PenX(12), dimPan.PenY(7));
        botonYa.addActionListener(this);
        botonYa.setForeground(Color.YELLOW);
        botonYa.setBackground(Color.red);
        labelFondo.add(botonYa);

        //tamanio letras
        boton1.setFont(new Font("Andale Mono", 1, tamanioNumeros));
        boton2.setFont(new Font("Andale Mono", 1, tamanioNumeros));
        boton3.setFont(new Font("Andale Mono", 1, tamanioNumeros));
        boton4.setFont(new Font("Andale Mono", 1, tamanioNumeros));
        boton5.setFont(new Font("Andale Mono", 1, tamanioNumeros));
        botonYa.setFont(new Font("Andale Mono", 1, tamanioNumeros - dimPan.tamanioLetra(30)));
        botonHome.setFont(new Font("Andale Mono", 1, tamanioNumeros - dimPan.tamanioLetra(30)));

        labelFondoBotones = new JLabel();
        labelFondoBotones.setBounds(dimPan.PenX(10), dimPan.PenY(0), dimPan.PenX(7), dimPan.PenY(5));
        labelFondoBotones.setHorizontalAlignment(SwingConstants.CENTER);
        labelFondo.add(labelFondoBotones);

        labelCronometro = new JLabel();
        labelCronometro.setBounds(dimPan.PenX(0), dimPan.PenY(87), dimPan.horizontal(), dimPan.PenY(10));
        labelCronometro.setHorizontalAlignment(SwingConstants.CENTER);
        labelCronometro.setForeground(Color.WHITE);
        labelCronometro.setFont(new Font("Andale Mono", 1, tamanioLetra));
        labelFondo.add(labelCronometro);

        ponerElementosJuego(nivel);

        hilo1 = new Thread(this);
        hilo1.start();
        
        if(copiaLista.size()>1){ //Ocultar elmentos si es muntijugador
            labelPuntos.setVisible(false);
            labelVidas.setVisible(false);
        }
        
        //Boton pausar
        pausado = false;
        botonPausa = new JButton("Pausar");
        botonPausa.setBounds(dimPan.PenX(5), dimPan.PenY(83), dimPan.PenX(12), dimPan.PenY(5));
        botonPausa.addActionListener(this);
        botonPausa.setForeground(Color.WHITE);
        botonPausa.setContentAreaFilled(false);
        botonPausa.setFont(new Font("Andale Mono", 2, dimPan.tamanioLetra(20)));
        labelFondo.add(botonPausa);
        
        labelPausa = new JLabel("PAUSA");
        labelPausa.setBounds(0, dimPan.PenY(35), dimPan.horizontal(), dimPan.PenY(25));
        labelPausa.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(250)));
        labelPausa.setForeground(Color.WHITE);
        labelFondo.add(labelPausa);
        labelPausa.setHorizontalAlignment(SwingConstants.CENTER);
        labelPausa.setVisible(false);
        labelFondo.add(botonPausa);
        
        botonMultiplicacion = new JButton("×");
        botonMultiplicacion.setFont(new Font("Arial",1,90));
        botonMultiplicacion.setBounds(ubicacionX2 + dimPan.PenX(-10), alturaBotonesPropuesta, dimPan.PenX(10), dimPan.PenY(10));
        botonMultiplicacion.addActionListener(this);
        botonMultiplicacion.setForeground(Color.GREEN);
        botonMultiplicacion.setContentAreaFilled(false);
        labelFondo.add(botonMultiplicacion);
        
        botonDivision = new JButton("÷");
        botonDivision.setFont(new Font("Arial", 1, 90));
        botonDivision.setBounds(ubicacionX2 + dimPan.PenX(70), alturaBotonesPropuesta, dimPan.PenX(10), dimPan.PenY(10));
        botonDivision.addActionListener(this);
        botonDivision.setForeground(Color.GREEN);
        botonDivision.setContentAreaFilled(false);
        labelFondo.add(botonDivision);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
         //inicio pausar
        if(e.getSource()==botonPausa && pausado == false){
            sonidoFaltan5Minutos.detenerAudio();
            hilo1.stop();
            botonYa.setEnabled(false);
            pausado = true;
            botonPausa.setText("Reanudar");
            panelNumeros.setVisible(false);
            labelPausa.setVisible(true);
            labelBuenoMalo.setVisible(false);
        }else if(e.getSource()==botonPausa && pausado == true){
            if(segundos<=5){
                sonidoFaltan5Minutos.reproduceFondo("faltan 5 segundos");
            }
            hilo1 = new Thread(this);
            hilo1.start();
            panelNumeros.setVisible(true);
            botonYa.setEnabled(true);
            pausado = false;
            botonPausa.setText("Pausar");
            labelPausa.setVisible(false);
            labelBuenoMalo.setVisible(true);
        } // fin boton pausar

        if (e.getSource() == botonHome) {
            if (segundos <= 5) {
                sonidoFaltan5Minutos.detenerAudio();
            }
            sonido.efectoDeSonido("risa1");
            //home = new ventanaMenu(copiaLista);
            //home.setVisible(true);
            setVisible(false);
            hilo1.stop();

            //Guardar archivo
            /*
            if(copiaLista.size()<=1 && !nombreJugador.equalsIgnoreCase("Entrenamiento")){
                archivo.serializarDatos(ruta + nombreJugador, (new String[]{"" + nivel, "" + puntos, "" + vidas, "" + intentos}));
            }*/
            
        }
        // COPIA SIGNO
        if (e.getSource() == botonMas) {
            sonido.efectoDeSonido("burbuja");
            copiaSigno = "+";
        }
        if (e.getSource() == botonMenos) {
            sonido.efectoDeSonido("burbuja");
            copiaSigno = "-";
        }
        if (e.getSource() == botonMultiplicacion) {
            sonido.efectoDeSonido("burbuja");
            copiaSigno = "*";
        }
        if (e.getSource() == botonDivision) {
            sonido.efectoDeSonido("burbuja");
            copiaSigno = "/";
        }
        if (e.getSource() == boton2) {
            sonido.efectoDeSonido("burbuja");
            boton2.setText(copiaSigno);
            copiaSigno = "?";

        }
        if (e.getSource() == boton4) {
            sonido.efectoDeSonido("burbuja");
            boton4.setText(copiaSigno);
            copiaSigno = "?";
        }
        //COPIA NUMEROS
        if (e.getSource() == botonNum1) {
            sonido.efectoDeSonido("burbuja");
            copiaNumero = botonNum1.getText();
        }
        if (e.getSource() == botonNum2) {
            sonido.efectoDeSonido("burbuja");
            copiaNumero = botonNum2.getText();
        }
        if (e.getSource() == botonNum3) {
            sonido.efectoDeSonido("burbuja");
            copiaNumero = botonNum3.getText();
        }
        if (e.getSource() == botonNum4) {
            sonido.efectoDeSonido("burbuja");
            copiaNumero = botonNum4.getText();
        }
        if (e.getSource() == botonNum5) {
            sonido.efectoDeSonido("burbuja");
            copiaNumero = botonNum5.getText();
        }
        if (e.getSource() == botonNum6) {
            sonido.efectoDeSonido("burbuja");
            copiaNumero = botonNum6.getText();
        }
        if (e.getSource() == botonNum7) {
            sonido.efectoDeSonido("burbuja");
            copiaNumero = botonNum7.getText();
        }
        if (e.getSource() == botonNum8) {
            sonido.efectoDeSonido("burbuja");
            copiaNumero = botonNum8.getText();
        }
        if (e.getSource() == botonNum9) {
            sonido.efectoDeSonido("burbuja");
            copiaNumero = botonNum9.getText();
        }
        if (e.getSource() == botonNum10) {
            sonido.efectoDeSonido("burbuja");
            copiaNumero = botonNum10.getText();
        }
        if (e.getSource() == botonNum11) {
            sonido.efectoDeSonido("burbuja");
            copiaNumero = botonNum11.getText();
        }
        if (e.getSource() == botonNum12) {
            sonido.efectoDeSonido("burbuja");
            copiaNumero = botonNum12.getText();
        }
        if (e.getSource() == botonNum13) {
            sonido.efectoDeSonido("burbuja");
            copiaNumero = botonNum13.getText();
        }

        if (e.getSource() == boton1) {
            sonido.efectoDeSonido("burbuja");
            boton1.setText(copiaNumero);
            copiaNumero = "?";
        }
        if (e.getSource() == boton3) {
            sonido.efectoDeSonido("burbuja");
            boton3.setText(copiaNumero);
            copiaNumero = "?";
        }
        if (e.getSource() == boton5) {
            sonido.efectoDeSonido("burbuja");
            boton5.setText(copiaNumero);
            copiaNumero = "?";
        }

        if (e.getSource() == botonYa) {
            sonido.efectoDeSonido("comedy-bubble-pop");
            
                if (segundos <= 5) {
                    sonidoFaltan5Minutos.detenerAudio();
                }
                int indice = 0;
                String arregloLlenado[] = new String[compOp.tamanioArreglo()];
                while (indice < compOp.tamanioArreglo()) {
                    if (indice == 0) {
                        arregloLlenado[indice] = boton1.getText();
                    }
                    if (indice == 1) {
                        arregloLlenado[indice] = boton2.getText();
                    }
                    if (indice == 2) {
                        arregloLlenado[indice] = boton3.getText();
                    }
                    if (indice == 3) {
                        arregloLlenado[indice] = boton4.getText();
                    }
                    if (indice == 4) {
                        arregloLlenado[indice] = boton5.getText();
                    }
                    indice++;
                }
                //INTERACION DE LAS VIDAS, PUNTOS Y NIVELES

                intentos++;
                acertoRespuesta = compOp.acertoRespuesta(arregloLlenado);//Mandamos el arreglo formado por el jugador
                if (copiaLista.size() <= 1) { //individual
                    puntosJugador = new puntosJugador(puntos);
                    puntos = puntosJugador.puntos(acertoRespuesta);
                    nivelVidas = new nivelYVidas(puntos, vidas, acertoRespuesta, intentos);
                    vidas = nivelVidas.vidas();
                    nivel = nivelVidas.nivel();

                    labelPuntos.setText("PUNTOS: " + puntos);
                    labelNivel.setText("NIVEL: " + nivel);
                    labelVidas.setText("VIDAS: " + vidas);
                }

                if (acertoRespuesta) {
                    labelBuenoMalo.setForeground(Color.CYAN);
                    labelBuenoMalo.setText("CORRECTO! :)");
                    if (copiaLista.size() > 1) {
                        aciertosMultijugador++;
                        labelAciertos.setText("ACIERTOS: " + aciertosMultijugador);
                    }

                    if (nivel >= nivelMaximo + 1 && copiaLista.size() <= 1) { //VentanaGanador
                        ganador = new ventanaCampeon('3', copiaLista, nivel);
                        ganador.setVisible(true);
                        this.setVisible(false);
                        hilo1.stop();
                    }

                } else {
                    labelBuenoMalo.setForeground(Color.RED);
                    labelBuenoMalo.setText("INCORRECTO :(");
                    if (vidas <= 0 && copiaLista.size() <= 1) {//Ventana perdedor
                        perdedor = new ventanaPerdedor('3', copiaLista, nivel);
                        perdedor.setVisible(true);
                        this.setVisible(false);
                        hilo1.stop();
                    }
                }
                boton1.setText("?");
                boton2.setText("?");
                boton3.setText("?");
                boton4.setText("?");
                boton5.setText("?");

                compOp = new completaOperacion(nivel);
                ponerElementosJuego(nivel);
                if(copiaLista.size()<=1){
                    segundos = nivelVidas.segundosDeTiempoJ3();
                }
                
            
        } // Fin boton Ya
        
        if(e.getSource()==botonGuardar){
            if(botonPausa.getText().equalsIgnoreCase("Pausar")){
            botonPausa.doClick();
            }
            salvarArchivo("j3");
        }
    }

    private void ponerElementosJuego(int nivel) {

        String arregloRecibido[] = compOp.getArreglo();

        if (nivel <= 3) {
            boton4.setVisible(false);
            boton5.setVisible(false);
        } else {
            boton4.setVisible(true);
            boton5.setVisible(true);
        }

        labelIgual.setFont(new Font("Andale Mono", 1, 70));
        if (nivel <= 3) {

            labelIgual.setBounds(ubicacionX1 + dimPan.PenX(30), alturaBotonesCompletar, dimPan.PenX(10), dimPan.PenY(10));

        } else {
            labelIgual.setBounds(ubicacionX1 + dimPan.PenX(50), alturaBotonesCompletar, dimPan.PenX(10), dimPan.PenY(10));
        }
        labelIgual.setHorizontalAlignment(SwingConstants.CENTER);
        panelNumeros.add(labelIgual);

        labelRespuesta.setText("" + compOp.calculoRespuesta());
        labelRespuesta.setFont(new Font("Andale Mono", 1, 70));
        if (nivel <= 3) {

            labelRespuesta.setBounds(ubicacionX1 + dimPan.PenX(40), alturaBotonesCompletar, dimPan.PenX(10), dimPan.PenY(10));

        } else {
            labelRespuesta.setBounds(ubicacionX1 + dimPan.PenX(60), alturaBotonesCompletar, dimPan.PenX(10), dimPan.PenY(10));
        }
        labelRespuesta.setHorizontalAlignment(SwingConstants.CENTER);
        panelNumeros.add(labelRespuesta);

        int indice = 0;// PARA LLENAR LOS BOTONES DE LA OPERACION
        String numerosMesclados[] = compOp.numerosPropuestos();
        String numerosMesclados2[] = compOp.numerosPropuestos2();
        int indiceOculto = Integer.parseInt(numerosMesclados[numerosMesclados.length - 1]); // Extrae el indice obtenido
        while (indice < arregloRecibido.length) {// ESTE CAMBIARA SI QUEREMOS IMPLEMENTAR ALEATORIAMENTE LOS...

            if (indice == 0 && indice != indiceOculto) {
                boton1.setText(arregloRecibido[indice]);
            }
            if (indice == 2 && indice != indiceOculto) {
                boton3.setText(arregloRecibido[indice]);
            }
            if (indice == 4 && indice != indiceOculto) {
                boton5.setText(arregloRecibido[indice]);
            }
            indice += 2;
        }

        indice = 0;
        while (indice < numerosMesclados.length - 1) { //llena los botones con el azar de numeros (primera fila)
            if (indice == 0) {
                botonNum1.setText(numerosMesclados[indice]);
            }
            if (indice == 1) {
                botonNum2.setText(numerosMesclados[indice]);
            }
            if (indice == 2) {
                botonNum3.setText(numerosMesclados[indice]);
            }
            if (indice == 3) {
                botonNum4.setText(numerosMesclados[indice]);
            }
            if (indice == 4) {
                botonNum5.setText(numerosMesclados[indice]);
            }
            if (indice == 5) {
                botonNum6.setText(numerosMesclados[indice]);
            }
            if (indice == 6) {
                botonNum7.setText(numerosMesclados[indice]);
            }

            indice++;
        }

        indice = 0;
        while (indice < numerosMesclados2.length) {
            if (indice == 0) {
                botonNum8.setText(numerosMesclados2[indice]);
            }
            if (indice == 1) {
                botonNum9.setText(numerosMesclados2[indice]);
            }
            if (indice == 2) {
                botonNum10.setText(numerosMesclados2[indice]);
            }
            if (indice == 3) {
                botonNum11.setText(numerosMesclados2[indice]);
            }
            if (indice == 4) {
                botonNum12.setText(numerosMesclados2[indice]);
            }
            if (indice == 5) {
                botonNum13.setText(numerosMesclados2[indice]);
            }

            indice++;
        }
        compOp.mostrarArreglo();
    }

    @Override
    public void run() {
        if (copiaLista.size() <= 1) { //Si no es multijugador
            while (segundos > 0) {

                try {
                    labelCronometro.setText("Quedan " + segundos + " segundos");
                    Thread.sleep(1000);
                    segundos--;
                    if (segundos <= 0) {
                        botonYa.doClick();
                    }

                } catch (InterruptedException ex) {
                }

                if (segundos == 5) {
                    sonidoFaltan5Minutos.reproduceFondo("faltan 5 segundos");
                }
            }
        } else {//Si es multijugador
            try {
                //segundosRestantes = tiempoMultijugador;
                //Aquí inicializamos al primer jugador
                while (segundosRestantes > 0 && jugador < copiaLista.size()) {
                    if (segundosRestantes == tiempoMultijugador) {
                        botonYa.setEnabled(false);
                        boton1.setEnabled(false);
                        boton2.setEnabled(false);
                        boton3.setEnabled(false);
                        boton4.setEnabled(false);
                        boton5.setEnabled(false);
                        labelCronometroMultijugador.setText("Atento...!!!");
                        Thread.sleep(3000);
                        botonYa.setEnabled(true);
                        boton1.setEnabled(true);
                        boton2.setEnabled(true);
                        boton3.setEnabled(true);
                        boton4.setEnabled(true);
                        boton5.setEnabled(true);

                        labelCronometroMultijugador.setText("Ya!");
                    }

                    Thread.sleep(1000);
                    labelCronometroMultijugador.setText("" + segundosRestantes);
                    segundosRestantes--;
                    if (segundosRestantes <= 0 && jugador < copiaLista.size() - 1) {
                        //Guardamos los datos del jugador (Temporal)
                        jugador++;
                        botonYa.doClick();
                        datosJugadores[jugador - 1][1] = "" + aciertosMultijugador;
                        datosJugadores[jugador - 1][2] = "" + segundosRestantes;
                        aciertosMultijugador = 0;
                        segundosRestantes = tiempoMultijugador;
                        
                        nombreJugador = datosJugadores[jugador][0];
                        labelJugadorTurno.setText("TURNO DE: " + nombreJugador);
                        labelCronometroMultijugador.setText("" + segundosRestantes);
                        labelAciertos.setText("ACIERTOS: " + aciertosMultijugador);
                        
                        labelNombreJugador.setText(nombreJugador);
                        segundosRestantes = tiempoMultijugador;
                    }
                }
                //Guarda los datos del ultimo jugador
                botonYa.doClick();
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
    
    private void salvarArchivo(String extension){
        archivo = new gestionArchivos();
        javax.swing.JFileChooser jF1 = new javax.swing.JFileChooser();
        String ruta = "";
        FileNameExtensionFilter filtro = new FileNameExtensionFilter(extension, extension);
        jF1.setFileFilter(filtro);
        try {
            if (jF1.showSaveDialog(null) == jF1.APPROVE_OPTION) {
                ruta = jF1.getSelectedFile().getAbsolutePath();
                System.out.println("Se guardó en: "+ruta);
                archivo.serializarDatos(ruta +"."+extension, (new String[]{"" + nivel, "" + puntos, "" + vidas, "" + intentos}));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        botonPausa.doClick();
    }
}