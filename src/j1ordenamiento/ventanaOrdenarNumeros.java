package j1ordenamiento;

import complementos.ventanaCampeon;
import complementos.*;
import ventanaPrincipal.*;
import dimensionesEscala.DimensionesPantalla;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Esta clase es la ventana del juego de ordenamiento (j1)
 * @Autores:  
 * Abasto Argote Gustavo, 
 * Laura Cuisara Erwin Harnaldo, 
 * Cayo Huaylla Ruddy, 
 * Ancari Iñiguez Abel
 * 
 */
public class ventanaOrdenarNumeros extends JInternalFrame implements ActionListener, Runnable, Serializable {

    //Icono
    private Image icono;
    private Toolkit pantalla;
    
    private JButton botonHome, botonYa, boton1, boton2, boton3, boton4, boton5, boton6, boton7, boton8, boton9, boton11, boton12, boton13,
            boton14, boton15, boton16, boton17, boton18, boton19, botonPausa, botonGuardar;
    private JPanel panelArreglos;
    private JLabel labelTitulo, labelPuntos, labelLetraPuntos, labelGanaste, labelLetraNivel, labelNivel, labelLetraVidas,
            labelVidas, labelTipoDeOrden, labelVerificacion, labelLlenadoJugador, labelLlenadoEsperado, labelCronometro, labelCronometroMultijugador,
            labelAciertos, labelJugadorTurno, labelPausa;
    private int ubiXfield, ubiYfield, tamXField, tamYfield; // Incluye la posicion de los botones
    private int tamLetraNums, tamLetraSup, tamLetraPuntos, tamanioLetraVerificación, tamLetraBotHomYa;
    private String tipoLetra;
    private DimensionesPantalla dimPan;

    //VENTANAS
    private ventanaCampeon ganador;
    private ventanaPerdedor perdedor;
    private ventanaResultadosMultijugador resultadosMultijugador;
    
    private int numeroDeNumerosAleatorios;
    private ordenarNumeros ordenaNumeros;
    
    private int nivel, nivelMaximo, puntos, intentos;
    private byte vidas;
    private boolean bienHecho;
    
    private JLabel labelFondo, labelMono;
    private ImageIcon fondo, propiedadesFondo;
    
    private int[] arregloResuelto;
    
    private String copiaCadena;
    
    private int segundos; // BORRAR
    private Thread hilo1;
    //Audio

    //Gestion de archivos
    //private String ruta; // Quedó obsoleto al reemplazar el tipo de guardado por ruta dinámica
    private String nombreJugador;
    private gestionArchivos archivo;
    //Multijugador
    private java.util.List copiaLista;
    private int tiempoMultijugador, segundosRestantes;
    private String[][] datosJugadores;
    private int aciertosMultijugador;
    private int jugador;
    private boolean pausado;
    private audioEfectos sonido, sonidoFaltan5Minutos;
    /**
     * 
     * @param listaJugadores Recibe la cantidad de jugadores
     * @param rutaArchivo Recibe la ruta de un archivo (En caso de que se cargue el archivo)
     * @param nivelMultijugador Recibe el nivel para el juego multijugador (desde el comboBox del la ventana Menu)
     */
    public ventanaOrdenarNumeros(java.util.List listaJugadores, String rutaArchivo, int nivelMultijugador) {
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        pausado = false;
        
        setTitle("ORDENAR NUMEROS");
        
        dimPan = new DimensionesPantalla();
        //this.setClosable(true);
        //this.setMaximizable(true);
        this.setIconifiable(true);
        //Cambio de fondo de pantalla
        labelFondo = new JLabel();
        labelFondo.setSize(dimPan.horizontal(), dimPan.vertical());
        fondo = new ImageIcon(getClass().getResource("images/238.jpg"));
        propiedadesFondo = new ImageIcon(fondo.getImage().getScaledInstance(labelFondo.getWidth(), labelFondo.getHeight(), Image.SCALE_DEFAULT));
        labelFondo.setIcon(propiedadesFondo);
        setBounds(0, 0, dimPan.PenX(100F), dimPan.PenY(100F));
        //setExtendedState(Frame.MAXIMIZED_BOTH);
        add(labelFondo);
        
        botonGuardar = new JButton("Guardar");
        botonGuardar.setBounds(0, 0, dimPan.PenX(5), dimPan.PenY(2));
        botonGuardar.addActionListener(this);
        botonGuardar.setContentAreaFilled(false);
        labelFondo.add(botonGuardar);

        //Contiene los numeros
        panelArreglos = new JPanel();
        panelArreglos.setBounds(0, 0, dimPan.horizontal(), dimPan.vertical());
        panelArreglos.setOpaque(false);
        panelArreglos.setLayout(null);
        labelFondo.add(panelArreglos);
        //Lista de jugadores
        this.nombreJugador = "";
        copiaLista = new ArrayList();
        for (int i = 0; i < listaJugadores.size(); i++) {
            copiaLista.add(listaJugadores.get(i));
        }
        if (listaJugadores.size() <= 1) {
            this.nombreJugador = (String) listaJugadores.get(0);
        } else {
            this.nombreJugador = "Multijugador";
        }

        //setUndecorated(true);//Elimina la barra de título
        //ruta = "C:\\Users\\Gustavo\\Documents\\NetBeansProjects\\Juego\\src\\complementos\\BD\\juego1\\";

        //Carga datos
        nivelMaximo = 5; //se debe superar

        if (copiaLista.size() <= 1) { // Si no es multijugador
            archivo = new gestionArchivos();
            if (!rutaArchivo.equals("")) {//Vacio es que no contiene ninguna ruta

                try {
                    nivel = Integer.parseInt(archivo.recuperaDatos(rutaArchivo)[0]); // int
                    puntos = Integer.parseInt(archivo.recuperaDatos(rutaArchivo)[1]); // int
                    vidas = Byte.parseByte(archivo.recuperaDatos(rutaArchivo)[2]); //Byte
                    intentos = Integer.parseInt(archivo.recuperaDatos(rutaArchivo)[3]);// si es un juego guardado debe ser mayor a 0 (int)
                    numeroDeNumerosAleatorios = Integer.parseInt(archivo.recuperaDatos(rutaArchivo)[4]); // int
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "El archivo no es compatible");
                    nivel = 1;
                    puntos = 0; // int
                    vidas = 3; //Byte
                    intentos = 0;// si es un juego guardado debe ser mayor a 0 (int)
                    numeroDeNumerosAleatorios = 2; // int
                }
            } else {
                nivel = 1;
                puntos = 0; // int
                vidas = 3; //Byte
                intentos = 0;// si es un juego guardado debe ser mayor a 0 (int)
                numeroDeNumerosAleatorios = 2; // int
            }
            
        } else { // Si es multijugador
            tiempoMultijugador = 30; //(Nunca cambia) Tiempo total en el que se ejecutará el juego
            segundosRestantes = tiempoMultijugador; //Cronometro cambiante
            jugador = 0; //Posicion del jugador
            this.nivel = nivelMultijugador;
            puntos = (nivelMultijugador - 1) * 800;
            numeroDeNumerosAleatorios = 2;
            vidas = 1;

            //jugadores y resultados (inserta a datos de los jugadores)
            datosJugadores = new String[listaJugadores.size()][3];// cada fila tiene nombreJugador - puntos - tiempo
            for (int i = 0; i < copiaLista.size(); i++) {
                datosJugadores[i][0] = (String) copiaLista.get(i);
                datosJugadores[i][1] = "" + 0;
                datosJugadores[i][2] = "" + tiempoMultijugador;
            }
            
            labelJugadorTurno = new JLabel("Turno de: " + datosJugadores[0][0]); // Se carga al label el nombre del primer jugador
            labelJugadorTurno.setBounds(dimPan.PenX(1.5F), dimPan.PenY(13), dimPan.PenX(100), dimPan.PenY(20));
            labelJugadorTurno.setFont(new Font("Courier New", 1, dimPan.tamanioLetra(35)));
            labelJugadorTurno.setForeground(Color.ORANGE);
            labelFondo.add(labelJugadorTurno);
            
            labelAciertos = new JLabel("ACIERTOS: " + datosJugadores[0][1]); // Se carga al label el nombre del primer jugador
            labelAciertos.setBounds(dimPan.PenX(60F), dimPan.PenY(0F), dimPan.PenX(20F), dimPan.PenY(5F));
            labelAciertos.setFont(new Font("Courier New", 1, dimPan.tamanioLetra(30)));
            labelAciertos.setForeground(new Color(131, 85, 12));
            labelFondo.add(labelAciertos);
            
            labelCronometroMultijugador = new JLabel("Atento...");
            labelCronometroMultijugador.setBounds(dimPan.PenX(0), dimPan.PenY(90), dimPan.PenX(100), dimPan.PenY(8));
            labelCronometroMultijugador.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(90)));
            labelCronometroMultijugador.setHorizontalAlignment(SwingConstants.CENTER);
            labelCronometroMultijugador.setForeground(Color.WHITE);
            labelFondo.add(labelCronometroMultijugador);
            
            aciertosMultijugador = 0;
        }
        //Nota, para guardar tambien se reguiere del tamaño del arreglo
        bienHecho = false;

        //cambio de icono
        pantalla = Toolkit.getDefaultToolkit();
        icono = pantalla.getImage("src/ordenamiento/images/disney.png");
        //setIconImage(icono);
        //Sonido
        sonido = new audioEfectos();
        sonidoFaltan5Minutos = new audioEfectos();

        //mono
        labelMono = new JLabel();
        labelMono.setBounds(dimPan.PenX(77.5F), dimPan.PenY(72.5F), dimPan.PenX(15F), dimPan.PenY(15F));
        fondo = new ImageIcon(getClass().getResource("images/mono.gif"));
        propiedadesFondo = new ImageIcon(fondo.getImage().getScaledInstance(labelMono.getWidth(), labelMono.getHeight(), Image.SCALE_DEFAULT));
        labelMono.setIcon(propiedadesFondo);
        labelFondo.add(labelMono);
        
        ubiXfield = dimPan.PenX(2F);// tamanio de los botones en X
        tamYfield = dimPan.PenY(10F); //tamanio de los botones en Y
        ubiYfield = dimPan.PenY(25F);// Altura de los botones 25
        tamXField = dimPan.PenX(10F);//Posicion de los botones en X 10

        //Tamanio de letras
        tamLetraNums = dimPan.tamanioLetra(55);
        tamLetraSup = dimPan.tamanioLetra(30);
        tamLetraPuntos = dimPan.tamanioLetra(40);
        tamanioLetraVerificación = dimPan.tamanioLetra(14);
        tamLetraBotHomYa = dimPan.tamanioLetra(40);
        tipoLetra = "Andale Mono";
        
        labelLetraNivel = new JLabel("NIVEL:");
        labelLetraNivel.setForeground(new Color(131, 85, 12));
        labelLetraNivel.setFont(new Font("Courier New", 1, tamLetraSup));
        labelLetraNivel.setBounds(dimPan.PenX(30F), dimPan.PenY(0F), dimPan.PenX(10F), dimPan.PenY(5F));
        labelFondo.add(labelLetraNivel);
        
        labelNivel = new JLabel("" + nivel);
        labelNivel.setForeground(Color.ORANGE);
        labelNivel.setFont(new Font("Courier New", 1, tamLetraSup));
        labelNivel.setBounds(dimPan.PenX(37F), dimPan.PenY(0F), dimPan.PenX(10F), dimPan.PenY(5F));
        labelFondo.add(labelNivel);
        
        labelLetraVidas = new JLabel("VIDAS:");
        labelLetraVidas.setForeground(new Color(131, 85, 12));
        labelLetraVidas.setFont(new Font("Courier New", 1, tamLetraSup));
        labelLetraVidas.setBounds(dimPan.PenX(60F), dimPan.PenY(0F), dimPan.PenX(10F), dimPan.PenY(5F));
        labelFondo.add(labelLetraVidas);
        
        labelVidas = new JLabel("" + vidas);
        labelVidas.setForeground(Color.ORANGE);
        labelVidas.setFont(new Font("Courier New", 1, tamLetraSup));
        labelVidas.setBounds(dimPan.PenX(67F), dimPan.PenY(0F), dimPan.PenX(10F), dimPan.PenY(5F));
        labelFondo.add(labelVidas);
        
        if (copiaLista.size() > 1) {
            labelLetraVidas.setVisible(false);
            labelVidas.setVisible(false);
        }
        
        labelLetraPuntos = new JLabel("PUNTAJE:");
        labelLetraPuntos.setBounds(dimPan.PenX(77F), dimPan.PenY(43.5F), dimPan.PenX(15F), dimPan.PenY(10F));
        labelLetraPuntos.setFont(new Font(tipoLetra, 1, tamLetraPuntos));
        labelLetraPuntos.setForeground(Color.WHITE);
        labelFondo.add(labelLetraPuntos);
        
        labelPuntos = new JLabel("" + puntos);
        labelPuntos.setBounds(dimPan.PenX(90F), dimPan.PenY(43.5F), dimPan.PenX(10F), dimPan.PenY(10F));
        labelPuntos.setFont(new Font(tipoLetra, 1, tamLetraPuntos));
        labelPuntos.setForeground(Color.ORANGE);
        labelFondo.add(labelPuntos);

        //NO HAY MEJORAS: será modificado por una imagen
        labelTitulo = new JLabel("ORDENA LOS NUMEROS");
        labelTitulo.setBounds(dimPan.PenX(5F), dimPan.PenY(10F), dimPan.PenX(100), dimPan.PenY(10));
        labelTitulo.setFont(new Font(tipoLetra, 1, 100));
        labelTitulo.setForeground(Color.ORANGE);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelFondo.add(labelTitulo);
        
        labelGanaste = new JLabel(";)");
        labelGanaste.setForeground(Color.YELLOW);
        labelGanaste.setFont(new Font(tipoLetra, 1, dimPan.tamanioLetra(80))); //tamanio de letra 80
        labelGanaste.setBounds(dimPan.PenX(0F), dimPan.PenY(40F), dimPan.PenX(100F), dimPan.PenY(15F));
        labelGanaste.setHorizontalAlignment(SwingConstants.CENTER);
        labelFondo.add(labelGanaste);
        
        boton11 = new JButton();
        boton11.setBounds(ubiXfield, ubiYfield, tamXField, tamYfield);
        boton11.setFont(new Font(tipoLetra, 1, tamLetraNums));
        boton11.setForeground(Color.MAGENTA);
        boton11.addActionListener(this);
        boton11.setBackground(new Color(212, 244, 191));
        //boton11.setCursor(new Cursor(HAND_CURSOR));
        panelArreglos.add(boton11);
        
        boton12 = new JButton();
        boton12.setBounds(ubiXfield + dimPan.PenX(10.4F), ubiYfield, tamXField, tamYfield);
        boton12.setFont(new Font(tipoLetra, 1, tamLetraNums));
        boton12.setForeground(Color.GREEN);
        boton12.addActionListener(this);
        boton12.setBackground(new Color(212, 244, 191));
        //boton12.setCursor(new Cursor(HAND_CURSOR));
        panelArreglos.add(boton12);
        
        boton13 = new JButton();
        boton13.setBounds(ubiXfield + dimPan.PenX(20.9F), ubiYfield, tamXField, tamYfield);
        boton13.setFont(new Font(tipoLetra, 1, tamLetraNums));
        boton13.setForeground(Color.MAGENTA);
        boton13.addActionListener(this);
        boton13.setBackground(new Color(212, 244, 191));
        //boton13.setCursor(new Cursor(HAND_CURSOR));
        panelArreglos.add(boton13);
        
        boton14 = new JButton();
        boton14.setBounds(ubiXfield + dimPan.PenX(31.4F), ubiYfield, tamXField, tamYfield);
        boton14.setFont(new Font(tipoLetra, 1, tamLetraNums));
        boton14.setForeground(Color.GREEN);
        boton14.addActionListener(this);
        boton14.setBackground(new Color(212, 244, 191));
        //boton14.setCursor(new Cursor(HAND_CURSOR));
        panelArreglos.add(boton14);
        
        boton15 = new JButton();
        boton15.setBounds(ubiXfield + dimPan.PenX(41.9F), ubiYfield, tamXField, tamYfield);
        boton15.setFont(new Font(tipoLetra, 1, tamLetraNums));
        boton15.setForeground(Color.MAGENTA);
        boton15.addActionListener(this);
        boton15.setBackground(new Color(212, 244, 191));
        //boton15.setCursor(new Cursor(HAND_CURSOR));
        panelArreglos.add(boton15);
        
        boton16 = new JButton();
        boton16.setBounds(ubiXfield + dimPan.PenX(52.4F), ubiYfield, tamXField, tamYfield);
        boton16.setFont(new Font(tipoLetra, 1, tamLetraNums));
        boton16.setForeground(Color.GREEN);
        boton16.addActionListener(this);
        boton16.setBackground(new Color(212, 244, 191));
        //boton16.setCursor(new Cursor(HAND_CURSOR));
        panelArreglos.add(boton16);
        
        boton17 = new JButton();
        boton17.setBounds(ubiXfield + dimPan.PenX(62.9F), ubiYfield, tamXField, tamYfield);
        boton17.setFont(new Font(tipoLetra, 1, tamLetraNums));
        boton17.setForeground(Color.MAGENTA);
        boton17.addActionListener(this);
        boton17.setBackground(new Color(212, 244, 191));
        //boton17.setCursor(new Cursor(HAND_CURSOR));
        panelArreglos.add(boton17);
        
        boton18 = new JButton();
        boton18.setBounds(ubiXfield + dimPan.PenX(73.4F), ubiYfield, tamXField, tamYfield);
        boton18.setFont(new Font(tipoLetra, 1, tamLetraNums));
        boton18.setForeground(Color.GREEN);
        boton18.addActionListener(this);
        boton18.setBackground(new Color(212, 244, 191));
        //boton18.setCursor(new Cursor(HAND_CURSOR));
        panelArreglos.add(boton18);
        
        boton19 = new JButton();
        boton19.setBounds(ubiXfield + dimPan.PenX(83.9F), ubiYfield, tamXField, tamYfield);
        boton19.setFont(new Font(tipoLetra, 1, tamLetraNums));
        boton19.setForeground(Color.MAGENTA);
        boton19.addActionListener(this);
        boton19.setBackground(new Color(212, 244, 191));
        //boton19.setCursor(new Cursor(HAND_CURSOR));
        panelArreglos.add(boton19);

        //ARREGLO DADO
        boton1 = new JButton();
        boton1.setBounds(ubiXfield, ubiYfield + dimPan.PenY(41F), tamXField, tamYfield);
        boton1.setFont(new Font(tipoLetra, 1, tamLetraNums));
        boton1.setForeground(Color.GREEN);
        boton1.addActionListener(this);
        boton1.setBackground(new Color(128, 98, 12));
        //boton1.setCursor(new Cursor(HAND_CURSOR));
        panelArreglos.add(boton1);
        
        boton2 = new JButton();
        boton2.setBounds(ubiXfield + dimPan.PenX(10.4F), ubiYfield + dimPan.PenY(41F), tamXField, tamYfield);
        boton2.setFont(new Font(tipoLetra, 1, tamLetraNums));
        boton2.setForeground(Color.MAGENTA);
        boton2.setBackground(null);
        boton2.addActionListener(this);
        boton2.setBackground(new Color(128, 98, 12));
        //boton2.setCursor(new Cursor(HAND_CURSOR));
        panelArreglos.add(boton2);
        
        boton3 = new JButton();
        boton3.setBounds(ubiXfield + dimPan.PenX(20.9F), ubiYfield + dimPan.PenY(41F), tamXField, tamYfield);
        boton3.setFont(new Font(tipoLetra, 1, tamLetraNums));
        boton3.setForeground(Color.GREEN);
        boton3.setBackground(null);
        boton3.addActionListener(this);
        boton3.setBackground(new Color(128, 98, 12));
        //boton3.setCursor(new Cursor(HAND_CURSOR));
        panelArreglos.add(boton3);
        
        boton4 = new JButton();
        boton4.setBounds(ubiXfield + dimPan.PenX(31.4F), ubiYfield + dimPan.PenY(41F), tamXField, tamYfield);
        boton4.setFont(new Font(tipoLetra, 1, tamLetraNums));
        boton4.setForeground(Color.MAGENTA);
        boton4.setBackground(null);
        boton4.addActionListener(this);
        boton4.setBackground(new Color(128, 98, 12));
        //boton4.setCursor(new Cursor(HAND_CURSOR));
        panelArreglos.add(boton4);
        
        boton5 = new JButton();
        boton5.setBounds(ubiXfield + dimPan.PenX(41.9F), ubiYfield + dimPan.PenY(41F), tamXField, tamYfield);
        boton5.setFont(new Font(tipoLetra, 1, tamLetraNums));
        boton5.setForeground(Color.GREEN);
        boton5.setBackground(null);
        boton5.addActionListener(this);
        boton5.setBackground(new Color(128, 98, 12));
        //boton5.setCursor(new Cursor(HAND_CURSOR));
        panelArreglos.add(boton5);
        
        boton6 = new JButton();
        boton6.setBounds(ubiXfield + dimPan.PenX(52.4F), ubiYfield + dimPan.PenY(41F), tamXField, tamYfield);
        boton6.setFont(new Font(tipoLetra, 1, tamLetraNums));
        boton6.setForeground(Color.MAGENTA);
        boton6.setBackground(null);
        boton6.addActionListener(this);
        boton6.setBackground(new Color(128, 98, 12));
        //boton6.setCursor(new Cursor(HAND_CURSOR));
        panelArreglos.add(boton6);
        
        boton7 = new JButton();
        boton7.setBounds(ubiXfield + dimPan.PenX(62.9F), ubiYfield + dimPan.PenY(41F), tamXField, tamYfield);
        boton7.setFont(new Font(tipoLetra, 1, tamLetraNums));
        boton7.setForeground(Color.GREEN);
        boton7.setBackground(null);
        boton7.addActionListener(this);
        boton7.setBackground(new Color(128, 98, 12));
        panelArreglos.add(boton7);
        
        boton8 = new JButton();
        boton8.setBounds(ubiXfield + dimPan.PenX(73.4F), ubiYfield + dimPan.PenY(41F), tamXField, tamYfield);
        boton8.setFont(new Font(tipoLetra, 1, tamLetraNums));
        boton8.setForeground(Color.MAGENTA);
        boton8.setBackground(null);
        boton8.addActionListener(this);
        boton8.setBackground(new Color(128, 98, 12));
        panelArreglos.add(boton8);
        
        boton9 = new JButton();
        boton9.setBounds(ubiXfield + dimPan.PenX(83.9F), ubiYfield + dimPan.PenY(41F), tamXField, tamYfield);
        boton9.setFont(new Font(tipoLetra, 1, tamLetraNums));
        boton9.setForeground(Color.GREEN);
        boton9.setBackground(null);
        boton9.addActionListener(this);
        boton9.setBackground(new Color(128, 98, 12));
        panelArreglos.add(boton9);

        //tamanio de letra de los 3 labels = 15
        labelVerificacion = new JLabel();
        labelVerificacion.setForeground(Color.GREEN);
        labelVerificacion.setFont(new Font(tipoLetra, 1, tamanioLetraVerificación));
        labelVerificacion.setBounds(dimPan.PenX(0F), dimPan.PenY(35F), dimPan.PenX(100F), dimPan.PenY(5F));
        labelFondo.add(labelVerificacion);
        
        labelLlenadoEsperado = new JLabel();
        labelLlenadoEsperado.setForeground(Color.PINK);
        labelLlenadoEsperado.setFont(new Font(tipoLetra, 1, tamanioLetraVerificación));
        labelLlenadoEsperado.setBounds(dimPan.PenX(0F), dimPan.PenY(38F), dimPan.PenX(100F), dimPan.PenY(5F));
        labelFondo.add(labelLlenadoEsperado);
        
        labelLlenadoJugador = new JLabel("");
        labelLlenadoJugador.setForeground(Color.GREEN);
        labelLlenadoJugador.setFont(new Font(tipoLetra, 1, tamanioLetraVerificación));
        labelLlenadoJugador.setBounds(dimPan.PenX(0F), dimPan.PenY(41F), dimPan.PenX(100F), dimPan.PenY(5F));
        labelFondo.add(labelLlenadoJugador);

        // tamanio de letras botones  = 50
        botonHome = new JButton("HOME");
        botonHome.setForeground(new Color(241, 196, 15));
        botonHome.setFont(new Font(tipoLetra, 1, tamLetraBotHomYa));
        botonHome.setBounds(dimPan.PenX(10F), dimPan.PenY(85F), dimPan.PenX(10F), dimPan.PenY(10F));
        botonHome.setBackground(new Color(148, 49, 38));
        botonHome.addActionListener(this);
        labelFondo.add(botonHome);
        
        botonYa = new JButton("YA!");
        botonYa.addActionListener(this);
        botonYa.setFont(new Font(tipoLetra, 1, tamLetraBotHomYa));
        botonYa.setForeground(new Color(241, 196, 15));
        botonYa.setBackground(new Color(131, 85, 12));
        botonYa.setBounds(dimPan.PenX(80F), dimPan.PenY(85F), dimPan.PenX(10F), dimPan.PenY(10F));
        labelFondo.add(botonYa);
        
        copiaDatos();
        
        labelTipoDeOrden = new JLabel(listaJugadores.get(jugador) + ", " + ordenaNumeros.TipoDeOrden());
        labelTipoDeOrden.setBounds(dimPan.PenX(15F), dimPan.PenY(53F), dimPan.PenX(50F), dimPan.PenY(20F));
        labelTipoDeOrden.setForeground(Color.WHITE);
        labelTipoDeOrden.setFont(new Font(tipoLetra, 2, dimPan.tamanioLetra(35))); // Tamanio de letra = 35
        labelFondo.add(labelTipoDeOrden);
        
        labelCronometro = new JLabel();
        labelCronometro.setBounds(dimPan.PenX(1.5F), dimPan.PenY(13), dimPan.PenX(100), dimPan.PenY(20));
        labelCronometro.setForeground(Color.cyan);
        labelCronometro.setFont(new Font(tipoLetra, 1, dimPan.tamanioLetra(40))); // 40
        labelFondo.add(labelCronometro);
        
        segundos = new nivelYVidas(puntos, vidas, bienHecho, intentos, numeroDeNumerosAleatorios).segundos();
        hilo1 = new Thread(this);
        hilo1.start();
        
        botonPausa = new JButton("Pausar");
        botonPausa.setBounds(dimPan.PenX(10F), dimPan.PenY(80F), dimPan.PenX(10F), dimPan.PenY(5F));;
        botonPausa.setContentAreaFilled(false); //Hace al boton transparente
        botonPausa.setForeground(Color.WHITE);
        botonPausa.addActionListener(this);
        botonPausa.setFont(new Font("Andale Mono", 2, dimPan.tamanioLetra(20)));
        labelFondo.add(botonPausa);
        
        labelPausa = new JLabel("PAUSA");
        labelPausa.setBounds(0, dimPan.PenY(40), dimPan.horizontal(), dimPan.PenY(25));
        labelPausa.setFont(new Font("Andale Mono", 1, dimPan.tamanioLetra(250)));
        labelPausa.setForeground(Color.WHITE);
        labelFondo.add(labelPausa);
        labelPausa.setHorizontalAlignment(SwingConstants.CENTER);
        labelPausa.setVisible(false);
        
    }
    
    private void copiaDatos() {  // ESTE MÉTODO SIRVE PARA COPIAR LOS DATOS  EN LA INTERFAZ GRÁFICA
        //ASIGNACION A LOS NUMEROS ALEATORIOS A LOS BOTONES
        int indice = 0;
        
        ordenaNumeros = new ordenarNumeros(numeroDeNumerosAleatorios, nivel, nivelMaximo); // le pasamos los parametros de numero de numeros aleatorios y prob de que sea negativo

        int[] arregloConAleatorios = ordenaNumeros.numerosAleatorios(); //Copia los datos del arreglo recibido en una arreglo interno
        while (indice < numeroDeNumerosAleatorios) {
            
            if (indice == 0) {
                boton1.setText("" + arregloConAleatorios[indice]);
            }
            if (indice == 1) {
                boton2.setText("" + arregloConAleatorios[indice]);
            }
            if (indice == 2) {
                boton3.setText("" + arregloConAleatorios[indice]);
            }
            if (indice == 3) {
                boton4.setText("" + arregloConAleatorios[indice]);
            }
            if (indice == 4) {
                boton5.setText("" + arregloConAleatorios[indice]);
            }
            if (indice == 5) {
                boton6.setText("" + arregloConAleatorios[indice]);
            }
            if (indice == 6) {
                boton7.setText("" + arregloConAleatorios[indice]);
            }
            if (indice == 7) {
                boton8.setText("" + arregloConAleatorios[indice]);
            }
            if (indice == 8) {
                boton9.setText("" + arregloConAleatorios[indice]);
            }
            
            indice++;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //Pausar hilo
        if (e.getSource() == botonPausa && pausado == false) {
            sonidoFaltan5Minutos.detenerAudio();
            hilo1.stop();
            botonYa.setEnabled(false);
            botonPausa.setText("Reanudar");
            pausado = true;
            panelArreglos.setVisible(false);
            labelPausa.setVisible(true);
            labelCronometro.setVisible(false);
            labelTipoDeOrden.setVisible(false);
            labelGanaste.setVisible(false);
            
        } else if (e.getSource() == botonPausa && pausado == true) {
            if (segundos <= 5) {
                sonidoFaltan5Minutos.reproduceFondo("faltan 5 segundos");
            }
            hilo1 = new Thread(this);
            hilo1.start();
            botonYa.setEnabled(true);
            pausado = false;
            botonPausa.setText("Pausar");
            panelArreglos.setVisible(true);
            labelPausa.setVisible(false);
            labelCronometro.setVisible(true);
            labelTipoDeOrden.setVisible(true);
            labelGanaste.setVisible(true);
        } // Fin hilo pausar juego

        if (e.getSource() == botonYa) {
            if (segundos <= 5) {
                sonidoFaltan5Minutos.detenerAudio();
            }
            sonido.efectoDeSonido("elec-beep-chirp1");
            int indice = 0;
            boolean bienHecho;
            arregloResuelto = new int[numeroDeNumerosAleatorios];//int[]arregloResuelto = new int[numeroDeNumerosAleatorios];
            while (indice < numeroDeNumerosAleatorios) {
                if (indice == 0) {
                    try {
                        arregloResuelto[indice] = Integer.parseInt(boton11.getText());
                    } catch (Exception ex) {
                        arregloResuelto[indice] = -1000000000; //-10000000
                    }
                }
                if (indice == 1) {
                    try {
                        arregloResuelto[indice] = Integer.parseInt(boton12.getText());
                    } catch (Exception ex) {
                        arregloResuelto[indice] = -1000000000;
                    }
                }
                if (indice == 2) {
                    try {
                        arregloResuelto[indice] = Integer.parseInt(boton13.getText());
                    } catch (Exception ex) {
                        arregloResuelto[indice] = -1000000000;
                    }
                }
                if (indice == 3) {
                    try {
                        arregloResuelto[indice] = Integer.parseInt(boton14.getText());
                    } catch (Exception ex) {
                        arregloResuelto[indice] = -1000000000;
                    }
                }
                if (indice == 4) {
                    try {
                        arregloResuelto[indice] = Integer.parseInt(boton15.getText());
                    } catch (Exception ex) {
                        arregloResuelto[indice] = -1000000000;
                    }
                }
                if (indice == 5) {
                    try {
                        arregloResuelto[indice] = Integer.parseInt(boton16.getText());
                    } catch (Exception ex) {
                        arregloResuelto[indice] = -1000000000;
                    }
                }
                if (indice == 6) {
                    try {
                        arregloResuelto[indice] = Integer.parseInt(boton17.getText());
                    } catch (Exception ex) {
                        arregloResuelto[indice] = -1000000000;
                    }
                }
                if (indice == 7) {
                    try {
                        arregloResuelto[indice] = Integer.parseInt(boton18.getText());
                    } catch (Exception ex) {
                        arregloResuelto[indice] = -1000000000;
                    }
                }
                if (indice == 8) {
                    try {
                        arregloResuelto[indice] = Integer.parseInt(boton19.getText());
                    } catch (Exception ex) {
                        arregloResuelto[indice] = -1000000000;
                    }
                }
                indice++;
            }
            // Prueba de verificacion/////////////////////////////////////////////////////////////////////////////////////////////////
            indice = 0;
            String verifica = "Enviado: [";
            String llenadoCorrecto = "Esperado: [";
            int arregloEsperado[] = ordenaNumeros.numerosOrdenados();
            
            while (indice < arregloResuelto.length) {
                
                if (indice == arregloResuelto.length - 1) {
                    if (arregloResuelto[indice] == -1000000000) {
                        verifica += ("Vacío" + "]");
                        llenadoCorrecto += (arregloEsperado[indice] + "]");
                    } else {
                        verifica += (arregloResuelto[indice] + "]");
                        llenadoCorrecto += (arregloEsperado[indice] + "]");
                    }
                } else {
                    
                    if (arregloResuelto[indice] == -1000000000) {
                        verifica += ("Vacío" + ", ");
                        llenadoCorrecto += (arregloEsperado[indice] + ", ");
                    } else {
                        verifica += (arregloResuelto[indice] + ", ");
                        llenadoCorrecto += (arregloEsperado[indice] + ", ");
                    }
                }
                indice++;
            }
            labelVerificacion.setText("Verificacion: " + labelTipoDeOrden.getText() + "...");
            labelLlenadoEsperado.setText(llenadoCorrecto);
            labelLlenadoJugador.setText(verifica);
            // Fin del proceso de verificacion

            intentos++;
            bienHecho = ordenaNumeros.esCorrectoElOrden(arregloResuelto);
            if (!nombreJugador.equalsIgnoreCase("Multijugador")) {//Solo se ejecuta si NO es multijugador
                puntos = new puntosJugador(puntos).puntos(bienHecho);
                nivel = new nivelYVidas(puntos, vidas, bienHecho, intentos, numeroDeNumerosAleatorios).nivel();
                vidas = new nivelYVidas(puntos, vidas, bienHecho, intentos, numeroDeNumerosAleatorios).vidas();
            }
            
            if (bienHecho) { // Si se acierta en la respuesta
                labelGanaste.setText("BIEN HECHO...  :)");
                labelLlenadoJugador.setForeground(Color.GREEN);
                //PUNTOS (+)
                labelGanaste.setForeground(Color.YELLOW);
                labelPuntos.setText("" + puntos);
                labelNivel.setText("" + nivel);
                
                if (nombreJugador.equalsIgnoreCase("Multijugador") && segundosRestantes <= 0) {
                    numeroDeNumerosAleatorios = 2;
                } else {
                    if (numeroDeNumerosAleatorios == 9) { // CONTROLA LAS CASILLAS O EL TAMAÑO DEL ARREGLO, trasladar a la parte logica
                        numeroDeNumerosAleatorios = 2;
                    } else {
                        numeroDeNumerosAleatorios++;
                    }
                }
                if (nivel >= nivelMaximo + 1) {//Campeon (Nunca se ejecuta en  modo multijugador)
                    //Guardar datos
                    /*
                    if (!nombreJugador.equalsIgnoreCase("Entrenamiento") && copiaLista.size() <= 1) {//Solo si es distinto de entrenamiento
                        archivo.serializarDatos(ruta + nombreJugador, (new String[]{"" + nivel, "" + puntos, "" + vidas, "" + intentos, "" + numeroDeNumerosAleatorios, "" + nivelMaximo}));
                    }*/
                    ganador = new ventanaCampeon('1', copiaLista, nivel);
                    ganador.setVisible(true);
                    this.setVisible(false);
                    hilo1.stop();
                }
                if (nombreJugador.equalsIgnoreCase("Multijugador")) {
                    aciertosMultijugador++; // Incrementa los aciertos del jugador x
                    labelAciertos.setText("ACIERTOS: " + aciertosMultijugador);
                }
                
            } else {
                labelGanaste.setForeground(Color.RED);
                labelGanaste.setText("MAL...  :(");
                labelVidas.setText("" + vidas);
                labelLlenadoJugador.setForeground(Color.RED);
                sonido.efectoDeSonido("La risa de Homero");
                
                if (nombreJugador.equalsIgnoreCase("Multijugador") && segundosRestantes <= 0) {
                    numeroDeNumerosAleatorios = 2;
                }
            }
            if (vidas == 0) { // Nuna se ejecuta en modo multijugador
                labelTipoDeOrden.setText("PERDISTE");
                botonYa.setEnabled(false);
                
                perdedor = new ventanaPerdedor('1', copiaLista, nivel);
                perdedor.setVisible(true);
                this.setVisible(false);
                hilo1.stop();
            } else {
                //Primero limpia
                boton1.setText("");
                boton2.setText("");
                boton3.setText("");
                boton4.setText("");
                boton5.setText("");
                boton6.setText("");
                boton7.setText("");
                boton8.setText("");
                boton9.setText("");
                copiaDatos();
                
                if (copiaLista.size() > 1) {
                    labelTipoDeOrden.setText(copiaLista.get(jugador) + ", " + ordenaNumeros.TipoDeOrden());
                } else {
                    labelTipoDeOrden.setText(nombreJugador + ", " + ordenaNumeros.TipoDeOrden());
                }
                
                boton11.setText("");
                boton12.setText("");
                boton13.setText("");
                boton14.setText("");
                boton15.setText("");
                boton16.setText("");
                boton17.setText("");
                boton18.setText("");
                boton19.setText("");
            }
            //LIMPIA LOS CAMPOS
            segundos = new nivelYVidas(puntos, vidas, bienHecho, intentos, numeroDeNumerosAleatorios).segundos();
            
        } //FIN DEL BOTONYA

        if (e.getSource() == botonHome) {
            if (segundos <= 5) {
                sonidoFaltan5Minutos.detenerAudio();
            }

            //home = new ventanaMenu(copiaLista);
            //home.setVisible(true);
            this.dispose();
            
            sonido.efectoDeSonido("risa1");
            hilo1.stop();

            //Guardar datos
            /*
            if (!nombreJugador.equalsIgnoreCase("Entrenamiento") && copiaLista.size() <= 1) {
                archivo.serializarDatos(ruta + nombreJugador, (new String[]{"" + nivel, "" + puntos, "" + vidas, "" + intentos, "" + numeroDeNumerosAleatorios}));
            }*/
        }
        //copia de informacion de los botones a la cadena copiaCadena
        if (e.getSource() == boton1) {
            sonido.efectoDeSonido("click-slide1");
            copiaCadena = boton1.getText();
        }
        if (e.getSource() == boton2) {
            sonido.efectoDeSonido("click-slide1");
            copiaCadena = boton2.getText();
        }
        if (e.getSource() == boton3) {
            sonido.efectoDeSonido("click-slide1");
            copiaCadena = boton3.getText();
        }
        if (e.getSource() == boton4) {
            sonido.efectoDeSonido("click-slide1");
            copiaCadena = boton4.getText();
        }
        if (e.getSource() == boton5) {
            sonido.efectoDeSonido("click-slide1");
            copiaCadena = boton5.getText();
        }
        if (e.getSource() == boton6) {
            sonido.efectoDeSonido("click-slide1");
            copiaCadena = boton6.getText();
        }
        if (e.getSource() == boton7) {
            sonido.efectoDeSonido("click-slide1");
            copiaCadena = boton7.getText();
        }
        if (e.getSource() == boton8) {
            sonido.efectoDeSonido("click-slide1");
            copiaCadena = boton8.getText();
        }
        if (e.getSource() == boton9) {
            sonido.efectoDeSonido("click-slide1");
            copiaCadena = boton9.getText();
        }

        //Peda de informacion en los votones correspondientes
        if (e.getSource() == boton11) {
            sonido.efectoDeSonido("comedy-bubble-pop");
            boton11.setText(copiaCadena);
            copiaCadena = "";
        }
        if (e.getSource() == boton12) {
            sonido.efectoDeSonido("comedy-bubble-pop");
            boton12.setText(copiaCadena);
            copiaCadena = "";
        }
        if (e.getSource() == boton13) {
            sonido.efectoDeSonido("comedy-bubble-pop");
            boton13.setText(copiaCadena);
            copiaCadena = "";
        }
        if (e.getSource() == boton14) {
            sonido.efectoDeSonido("comedy-bubble-pop");
            boton14.setText(copiaCadena);
            copiaCadena = "";
        }
        if (e.getSource() == boton15) {
            sonido.efectoDeSonido("comedy-bubble-pop");
            boton15.setText(copiaCadena);
            copiaCadena = "";
        }
        if (e.getSource() == boton16) {
            sonido.efectoDeSonido("comedy-bubble-pop");
            boton16.setText(copiaCadena);
            copiaCadena = "";
        }
        if (e.getSource() == boton17) {
            sonido.efectoDeSonido("comedy-bubble-pop");
            boton17.setText(copiaCadena);
            copiaCadena = "";
        }
        if (e.getSource() == boton18) {
            sonido.efectoDeSonido("comedy-bubble-pop");
            boton18.setText(copiaCadena);
            copiaCadena = "";
        }
        if (e.getSource() == boton19) {
            sonido.efectoDeSonido("comedy-bubble-pop");
            boton19.setText(copiaCadena);
            copiaCadena = "";
        }

        //Boton guardar como...
        if (e.getSource() == botonGuardar) {
            if (!botonPausa.getText().equalsIgnoreCase("Reanudar")) {
                botonPausa.doClick();
            }
            salvarArchivo();
        }
    }

    //@Override
    public void run() {
        
        if (copiaLista.size() <= 1) { //un jugador
            while (!Thread.interrupted() && segundos > 0) {
                try {
                    if (segundos <= 5) { // Solo es para repitaar los numeros
                        if (segundos % 2 == 0) {
                            labelCronometro.setForeground(Color.cyan);
                        } else {
                            labelCronometro.setForeground(Color.red);
                        }
                        
                        botonYa.setForeground(Color.red);
                        
                    } else {
                        labelCronometro.setForeground(Color.cyan);
                    }
                    labelCronometro.setText("Te quedan " + segundos + " segundos!");
                    Thread.sleep(1000);
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(ventanaOrdenarNumeros.class.getName()).log(Level.SEVERE, null, ex);
                }
                segundos--;
                if (segundos == 0) {
                    botonYa.doClick();
                    sonidoFaltan5Minutos.detenerAudio();
                }
                botonYa.setForeground(Color.orange);
                if (segundos == 5) {
                    sonidoFaltan5Minutos.reproduceFondo("faltan 5 segundos");
                }
            }
        } else { // MULTIJUGADOR
            try {
                //segundosRestantes = tiempoMultijugador;
                //Aquí inicializamos al primer jugador
                while (segundosRestantes > 0 && jugador < copiaLista.size()) {
                    if (segundosRestantes == tiempoMultijugador) {
                        botonYa.setEnabled(false);
                        boton1.setEnabled(false);
                        boton2.setEnabled(false);
                        labelCronometroMultijugador.setText("Atento...!!!");
                        Thread.sleep(3000);
                        botonYa.setEnabled(true);
                        boton1.setEnabled(true);
                        boton2.setEnabled(true);
                        
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
                        
                        labelJugadorTurno.setText("Turno de: " + datosJugadores[jugador][0]);
                        labelCronometroMultijugador.setText("" + segundosRestantes);
                        labelAciertos.setText("ACIERTOS: " + aciertosMultijugador);
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
    
    private void salvarArchivo() {
        archivo = new gestionArchivos();
        javax.swing.JFileChooser jF1 = new javax.swing.JFileChooser();
        String ruta = "";
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("j1", "j1");
        jF1.setFileFilter(filtro);
        try {
            if (jF1.showSaveDialog(null) == jF1.APPROVE_OPTION) {
                ruta = jF1.getSelectedFile().getAbsolutePath();
                System.out.println("Se guardó en: " + ruta);
                archivo.serializarDatos(ruta + ".j1", (new String[]{"" + nivel, "" + puntos, "" + vidas, "" + intentos, "" + numeroDeNumerosAleatorios}));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        botonPausa.doClick();
    }
}
