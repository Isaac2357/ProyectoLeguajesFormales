import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Interface extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;	
	private Container container;
	private JLabel titleLabel;
	private JTextArea resultTextArea;
	private JButton loadERButton;
	private JButton loadTextButton;
	private JButton executeButton;
	private JScrollPane scrollPaneArea;
	private JFileChooser fileChooser; 
	private String regularExpression =  "";
	private String text = "";

	private InterfaceHandler interfaceHandler = null;
	
    
	//Refactoring using F2
	public Interface(){

		interfaceHandler = new InterfaceHandler();
		container = getContentPane();
		container.setLayout(null);
		fileChooser = new JFileChooser();
			

		titleLabel = new JLabel();
		titleLabel.setBounds(110, 20, 180, 23);
			
		resultTextArea = new JTextArea();
		//para que el texto se ajuste al area
		resultTextArea.setLineWrap(true);
		//permite que no queden palabras incompletas al hacer el salto de linea
		resultTextArea.setWrapStyleWord(true);
		scrollPaneArea = new JScrollPane();
		scrollPaneArea.setBounds(20, 50, 550, 270);
	    scrollPaneArea.setViewportView(resultTextArea);
	       	
		/*Propiedades del boton, lo instanciamos, posicionamos y activamos los eventos*/
		loadERButton = new JButton();
		loadERButton.setText("Cargar ER");
		loadERButton.setBounds(100, 330, 120, 23);
		loadERButton.addActionListener(this);
			
		loadTextButton = new JButton();
		loadTextButton.setText("Cargar Texto");
		loadTextButton.setBounds(220, 330, 120, 23);
		loadTextButton.addActionListener(this);

		executeButton = new JButton();
		executeButton.setText("Ejecutar");
		executeButton.setBounds(100, 330, 120, 23);
		executeButton.addActionListener(this);
			
		/*Agregamos los componentes al Contenedor*/
		container.add(titleLabel);
		container.add(scrollPaneArea);
		container.add(loadERButton);
		container.add(loadTextButton);
		container.add(executeButton);

       	//Asigna un titulo a la barra de titulo
		setTitle("Lenguajes Formales");
		//tama�o de la ventana
		setSize(600,400);
		//pone la ventana en el Centro de la pantalla
		setLocationRelativeTo(null);
			
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent evento) {
		if (evento.getSource() == loadERButton){
			loadRegularExpression();
			String txt = this.resultTextArea.getText();
			txt += "Regular Expression: \n" + this.interfaceHandler.getRegularExpression()  + "\n\n";
			resultTextArea.setText(txt);
		}else if (evento.getSource()==loadTextButton){
			loadText();
			String txt = this.resultTextArea.getText();
			txt += "Text: \n" + this.interfaceHandler.getText()  + "\n\n";
			resultTextArea.setText(txt);
		}else if(evento.getSource() == executeButton){
			interfaceHandler.compute();
		}
	}

	private void loadRegularExpression() {
		//Code to load the ER
		try{

		fileChooser.showOpenDialog(this);
		File fileRegularExpression = fileChooser.getSelectedFile();	
		List<String> myRegularExpression = Files.readAllLines(Paths.get(fileRegularExpression.getAbsolutePath()));
		for(String er : myRegularExpression){
			this.regularExpression += er;
		}

		interfaceHandler.setRegularExpression(this.regularExpression);
		System.out.println(interfaceHandler.getRegularExpression());
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, 
										  e +  "\nNo se ha encontrado el archivo",
										  "ADVERTENCIA!!!",
									      JOptionPane.WARNING_MESSAGE);
		}
	}

	private void loadText() {
		//Code to load Text
		try{

			fileChooser.showOpenDialog(this);
			File fileText = fileChooser.getSelectedFile();	
			List<String> myText = Files.readAllLines(Paths.get(fileText.getAbsolutePath()));
			for(String txt : myText){
				this.text += (txt + "\n");
			}
	
			interfaceHandler.setText(this.text);
			System.out.println(interfaceHandler.getText());
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, 
											  e +  "\nNo se ha encontrado el archivo",
											  "ADVERTENCIA!!!",
											  JOptionPane.WARNING_MESSAGE);
			}
	}

}