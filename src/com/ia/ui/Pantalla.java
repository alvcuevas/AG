package com.ia.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;

public class Pantalla {
	
	public static final String RUTA_PROYECTO = System.getProperty("user.dir");
	
	static String rutaConfiguracion = null;
	static String rutaPuntos = null;
	
	public static void main(String[] args) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		
		JTextField frase = new JTextField(15);
		JTextField log = new JTextField(RUTA_PROYECTO + "log_a_genetico_" + sdf.format(new Date()) + ".txt");
		
		JFrame pantalla = new JFrame("Algoritmo Genetico (IAII)");
		pantalla.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel gui = new JPanel(new BorderLayout(3,3));
		gui.setBorder(new EmptyBorder(5,5,5,5));
		gui.setBackground(Color.WHITE);
		pantalla.setContentPane(gui);
		
		UIManager.put("InternalFrame.activeTitleBackground", new ColorUIResource(Color.black ));
		UIManager.put("InternalFrame.activeTitleForeground", new ColorUIResource(Color.WHITE));
		UIManager.put("InternalFrame.titleFont", new Font("Dialog", Font.PLAIN, 11));
		
		
		JLabel mensaje = new JLabel("Introduce una frase para empezar!");
		
		JPanel labels = new JPanel(new GridLayout(0,1));
		labels.setBackground(Color.WHITE);
        JPanel controls = new JPanel(new GridLayout(0,1));
        controls.setBackground(Color.WHITE);
		JPanel messageBox = new JPanel(new GridLayout(1,1));
		
		
		gui.add(labels, BorderLayout.WEST);
        gui.add(controls, BorderLayout.CENTER);
        
        labels.add(new JLabel("Frase: "));
        controls.add(frase);
        labels.add(new JLabel("Log: "));
        controls.add(log);
        
		messageBox.add(mensaje);
		gui.add(messageBox, BorderLayout.PAGE_END);
		
        pantalla.pack();
        pantalla.setVisible(true);
		
		
	}
}
