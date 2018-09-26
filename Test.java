import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.JOptionPane;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.util.ArrayList;
import Automata.Automata;

/**
 * Clase principal
 * Permite cargar un automata y evaluar cadenas
 * @version 2.0, 20/03/2016
 * @author Jacobo Tapia
 * @author Ketzia Dante
 */

public class Test{

	/** 
     * Metodo main
     * @see menuProceso
     * @see verInstrucciones
     */	
	public static void main(String[] args) {

		/*Variable opcion, permite controlar la interfaz de menu*/
		int option=-1;

		/*
		* Mientras 'opcion' sea diferente de 3
		* Se mantiene el programa corriento
		* Si se ingresa el numero 1, se pedira el nombre de un automata a carga
		* Con el numero 2 se despliegan algunas instrucciones 
		* El 3 termina la ejecucion
		*/
		while(option!=3){
			option=Integer.parseInt(JOptionPane.showInputDialog(null,
				"1.Ingresa un autómata desde archivo\n2.Instrucciones\n3.Salir",
				"Menú de aútomatas",JOptionPane.PLAIN_MESSAGE));
			switch(option){
				case 1:
					/*
					* Se crea un nuevo automata que se carga con el nombre
					* del archivo
					*/
					String fileName=JOptionPane.showInputDialog(
						null,"Ingresa el nombre del archivo de tu automata: ",
                    	"Carga de Automata desde archivo: ",
                    	JOptionPane.PLAIN_MESSAGE);
					menuProceso(cargaAutomata(fileName),fileName);
				break;

				case 2:
					verInstrucciones();
				break;

				case 3:
				break;

				default:
					JOptionPane.showMessageDialog(null,"Opción no valida","Error",
						JOptionPane.PLAIN_MESSAGE);
				break;
			}
		}

	}

	/** 
     * Abre la interfaz que permite procesar cadenas en un automata
     * Previamente cargado
     * @see Automata, metodo funcionDeTransicionExtendida
     */	
	public static void menuProceso(Automata automata,String fileName){
		int option=-1;
		/*
		* Mientras 'opcion' sea diferente de 2
		* Se repite este menu
		* Si se ingresa 1, permitira ingresar una cadena para ver si es aceptada
		* Si se ingresa 2, se manda al menu anterior para cerrar el programa o usar otro automata.
		*/
		while(option!=2){
			option=Integer.parseInt(JOptionPane.showInputDialog(
				null,"1.Procesamiento de cadenas\n2.Regresar al menu anterior",
                "Menú de Proceso de cadenas",
                JOptionPane.PLAIN_MESSAGE));
			switch(option){
				case 1:
					/*
					* Se quitan las cadenas "lmd" de la cadena a procesar
					* Son sustituidas por ª
					* Para facilitar el manejo del procesamiento
					*/
					String cadena=JOptionPane.showInputDialog(
						null,"Ingresa la cadena que deseeas procesar: ",
                    	"Proceso de cadenas",
                    	JOptionPane.PLAIN_MESSAGE).replaceAll("lmd","ª");

					/*
					* Se crea un textArea, que permitira
					* desplegar con mayor facilidad los resultados
					* del procesamiento de la cadena
					*/
					JTextArea textArea=new JTextArea(automata.funcionTransicionExtendida(cadena));
					JScrollPane scrollPane=new JScrollPane(textArea);
					textArea.setLineWrap(true);
					scrollPane.setPreferredSize(new Dimension( 500, 500 ));
					JOptionPane.showMessageDialog(null, 
						scrollPane,
						"Evaluación de "+cadena+" en automata: "+fileName,
		   				JOptionPane.YES_NO_OPTION);
				break;

				case 2:

				break;

				default:
					JOptionPane.showMessageDialog(null,"Opción no valida","Error",
						JOptionPane.PLAIN_MESSAGE);
				break;
			}
		}
	}


	/** 
     * Genera un objeto de la Clase Automata
     * con los datos de un archivo
     * @param fileName nombre del archivo del que se quieren cargar los datos
     * @see Automata, metodo setSelfLambda
     * @see Automata, metodo addAdyacente
     * @see Automata, metodo setNombreAutomata
     * @see Automata, metodo insertEstado
     * @see Automata, metodo setInitialState
     * @see Automata, metodo addFinalState
     * @see Estado, metodo setAlfabeto
     * @see Estado, metodo listaDeTransicion
     * @return se rregresa un automata con lo especificado en el archivo con nombre 'fileName'
     */
	public static Automata cargaAutomata(String fileName){

		Automata automata=new Automata(); //Se instancia un automata
		int linea=0; //Variable auxiliar para saber en que linea se encuentra de lectura


		try{


			/*
			* Se genera un lector para el archivo 'fileName'
			*/

			BufferedReader lector=new BufferedReader(
				new FileReader(fileName)); 

			/*
			* Mientras en el lector haya datos
			* Se continua con la lectura del mismo
			*/
			while(lector.ready()){

				/*
				* De cada linea del archivo
				* Se separa cada que sea encontrada una ','
				*/
				String data[]=lector.readLine().split(",");

				/*
				* Si la linea es igual a 0, se espera leer: 
				* Conjunto de estados del aútomata, separados por comas
				*/
				if(linea==0){

					for (String nombreEstado : data) {
						automata.insertEstado(nombreEstado);
					}
					linea++;

				/*
				* Si la linea es igual a 1, se espera leer: 
				* Simbolos del alfabeto, separados por comas
				*/

				}else if(linea==1){

					for(int i=0;i<automata.size();i++){
						automata.get(i).setAlfabeto(data);
					}
					linea++;

				/*
				* Si la linea es igual a 2, se espera leer: 
				* Estado Inicial
				*/

				}else if(linea==2){

					int i=automata.indexOf(data[0]);
					automata.get(i).setInitial(true);
					automata.setInitialState(i);
					linea++;

				/*
				* Si la linea es igual a 3, se espera leer: 
				* Estados finales, separados por comas
				*/

				}else if(linea==3){

					int i=-1;
					for (String estado : data) {
						i=automata.indexOf(estado);
						automata.get(i).setFinal(true);
						automata.addFinalState(i);
					}
					linea++;

				/*
				* Si la linea es mayor a 3, se espera leer: 
				* Transiciones por estado
				*/

				}else if(linea>3){


					/*
					* Debido a que en la variable 'data', se divide por comas las cadenas
					* Se vuelve a unir la cadena para hacer las operaciones y obtener
					* las transiciones por estados de manera correcta
					*/
					String lectura="";

					for(int i=0;i<data.length;i++){
						if(i==data.length-1){
							lectura+=data[i];
						}else{
							lectura+=data[i]+",";
						}
					}

					String specialData[]=lectura.replaceFirst(",",";").split(";");
					String estadoOrigen=specialData[0];
					String specialData2[]=specialData[1].split("=>");
					String simbolo=specialData2[0];
					String[] estados=specialData2[1].split(",");

					/*
					* Para cada estado en estados (adyacentes)
					* Se aniade a un estado, el conjunto de estados
					* tras procesar un simbolo
					*/
					for (String e : estados) {
						automata.addAdyacente(estadoOrigen,simbolo,e);
					}


					linea++;
				}

			}

			/*
			* A todos los estados del automata se 
			* les asigna que al procesar lambda llegan a si
			*  mismos
			*/
			automata.setSelfLamda();

			/*
			* Nombramos el automata
			*/
			automata.setNombreAutomata(fileName);


		}catch(Exception ex){
			/*
			* Si no se encuentra el archivo
			* Se detiene la ejecución del programa.
			*/
			JOptionPane.showMessageDialog(null,
				"Ups :S, el archivo no existe");
			throw new RuntimeException("Falla en carga de archivo");
		}
		return automata;
	}



	/** 
     * Imprime la lista de instrucciones
     * del programa
     */
	public static void verInstrucciones(){
		String instrucciones="";
		instrucciones+="1.Ingresa el nombre del archivo para cargar tu aútomata";
		instrucciones+="\n1.1. Si creaste tu archivo y el programa no lo reconoce:\n";
		instrucciones+="Copia y pega el contenido, elimina el archivo y crea uno nuevo\n";
		instrucciones+="Vuelve a compilar tu programa, debería funcionar\n\n";
		instrucciones+="2.Los resultados apareceran de la siguiente manera:";
		instrucciones+="\nSi tu cadena es aab, se mostrara a[a]b\n";
		instrucciones+="Entre los corechetes, quedará el simbolo que se esta procesando";
		instrucciones+="\nDebajo, vendra el resultado de procesar cada estado con dicho simbolo";
		instrucciones+="\nY al final, el resultado de aplicar cerradura lambda al conjunto de estado resultantes.";
		instrucciones+="\n\n3.En la carpeta del proyecto, vienen algunos automatas de ejemplo";
		JTextArea textArea=new JTextArea(instrucciones);
		JScrollPane scrollPane=new JScrollPane(textArea);
		textArea.setLineWrap(true);
		scrollPane.setPreferredSize(new Dimension( 500, 500 ));
		JOptionPane.showMessageDialog(null, 
			scrollPane,
			"Instrucciones",
		   	JOptionPane.YES_NO_OPTION);	
	}

}