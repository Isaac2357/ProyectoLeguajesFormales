import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Interface extends JFrame implements ActionListener{
		private Container contenedor;
		JLabel labelTitulo;/*declaramos el objeto Label*/
		JTextArea areaDeTexto;
		JButton botonAbrir;/*declaramos el objeto Boton*/
		JButton botonGuardar;/*declaramos el objeto Boton*/
		JScrollPane scrollPaneArea;
		JFileChooser fileChooser; /*Declaramos el objeto fileChooser*/
		String texto;
    
		
		public Interface()//constructor
		{
			contenedor=getContentPane();
			contenedor.setLayout(null);
			
			/*Creamos el objeto*/
			fileChooser=new JFileChooser();
			
			/*Propiedades del Label, lo instanciamos, posicionamos y
			 * activamos los eventos*/
			labelTitulo= new JLabel();
			labelTitulo.setText("Texto");
			labelTitulo.setBounds(110, 20, 180, 23);
			
			areaDeTexto = new JTextArea();
			//para que el texto se ajuste al area
			areaDeTexto.setLineWrap(true);
			//permite que no queden palabras incompletas al hacer el salto de linea
			areaDeTexto.setWrapStyleWord(true);
		   	scrollPaneArea = new JScrollPane();
			scrollPaneArea.setBounds(20, 50, 350, 270);
	        scrollPaneArea.setViewportView(areaDeTexto);
	       	
			/*Propiedades del boton, lo instanciamos, posicionamos y
			 * activamos los eventos*/
			botonAbrir= new JButton();
			botonAbrir.setText("Cargar ER");
			botonAbrir.setBounds(100, 330, 120, 23);
			botonAbrir.addActionListener(this);
			
			botonGuardar= new JButton();
			botonGuardar.setText("Cargar Texto");
			botonGuardar.setBounds(220, 330, 120, 23);
			botonGuardar.addActionListener(this);
			
			/*Agregamos los componentes al Contenedor*/
			contenedor.add(labelTitulo);
			contenedor.add(scrollPaneArea);
			contenedor.add(botonAbrir);
			contenedor.add(botonGuardar);
       		//Asigna un titulo a la barra de titulo
			setTitle("Lenguajes Formales");
			//tama�o de la ventana
			setSize(400,400);
			//pone la ventana en el Centro de la pantalla
			setLocationRelativeTo(null);
			
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}

		@Override
		public void actionPerformed(ActionEvent evento) {
			if (evento.getSource()==botonAbrir)
			{
				String archivo=abrirArchivo();
				areaDeTexto.setText(archivo);
			}
			
			if (evento.getSource()==botonGuardar)
			{
				guardarArchivo();
			}
		}

		/**
		 * Permite mostrar la ventana que carga 
		 * archivos en el area de texto
		 * @return
		 */
		private String abrirArchivo() {
					
			String aux=""; 		
	 		texto="";
		
	 		try
			{
	 			/*llamamos el metodo que permite cargar la ventana*/
	    		fileChooser.showOpenDialog(this);
	    		/*abrimos el archivo seleccionado*/
	 			File abre=fileChooser.getSelectedFile();

	 			/*recorremos el archivo, lo leemos para plasmarlo
	 			 *en el area de texto*/
	 			if(abre!=null)
	 			{ 				
	 				FileReader archivos=new FileReader(abre);
	 				BufferedReader lee=new BufferedReader(archivos);
	 				while((aux=lee.readLine())!=null)
	 					{
	 					 texto+= aux+ "\n";
	 					}

	 		  		lee.close();
	 			} 			
	 		}
	 		catch(IOException ex)
			{
			  JOptionPane.showMessageDialog(null,ex+"" +
			  		"\nNo se ha encontrado el archivo",
			  		"ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
			}
				return texto;
		}
		
		/**
		 * Guardamos el archivo del area 
		 * de texto en el equipo
		 */
		private void guardarArchivo() {

	 		try
	 		{
				String nombre="";
				JFileChooser file=new JFileChooser();
				file.showSaveDialog(this);
				File guarda =file.getSelectedFile();
		
				if(guarda !=null)
				{
		 			nombre=file.getSelectedFile().getName();
		 			/*guardamos el archivo y le damos el formato directamente,
		 			 * si queremos que se guarde en formato doc lo definimos como .doc*/
		 			FileWriter  save=new FileWriter(guarda+".txt");
		 			save.write(areaDeTexto.getText());
		 			save.close();
		 			JOptionPane.showMessageDialog(null,
		 					"El archivo se a guardado Exitosamente",
		 					"Informaci�n",JOptionPane.INFORMATION_MESSAGE);
			    }
	 		 }
	 	   catch(IOException ex)
		   {
			 JOptionPane.showMessageDialog(null,
					 "Su archivo no se ha guardado",
					 "Advertencia",JOptionPane.WARNING_MESSAGE);
		   }
		}

}