package Automata;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import Automata.Estado;

/**
 * Una clase para representar automatas finitos
 * @version 3.1, 12/04/2016
 * @author Jacobo Tapia
 * @author Ketzia Dante
 */


public class Automata{

	private ArrayList<Estado> automata; //Arraylist con todos los estados
	private ArrayList<Integer> estadosFinales; //ArrayList con los esstados finales;
	private String nombreAutomata; //Nombre del archivo del que se cargo el automata
	private int initialState; //Posicion del estado inicial
	private char[] alfabeto; //Lista de todos los simbolos del alfabeto
	private String megaResultado; //Cadena para imprimir los resultados del procesamiento de una cadena

	/** 
     * Crea un automata.
     */
	public Automata(){
		/*Se crea un nuevo arraylist de estados*/
		automata=new ArrayList<Estado>();
		/*Se inicializa la lista de estado finales*/
		estadosFinales=new ArrayList<Integer>();
		/*
		* Se pone la posicion 0 como estado final
		* Al hacer la carga de el autómata el método setEstadoInicial cambiara este valor
		*/
		initialState=0;
	}

	/** 
     * Inserta un estado al autómata.
     * @param e String con el nombre del estado a insertar
     * @return True si se inserto el estado, False en caso contrario
     */
	public boolean insertEstado(String e){
		/*Se crea un nuevo estado con el nombre*/
		Estado estado=new Estado(e);
		/*Se verifica que el automata no contenga un estado con el mismo nombre*/
		if(!automata.contains(estado)){
			/*Si no lo contiene se inserta el estado*/
			automata.add(estado);

			return true;
		}else{
			return false;
		}
	}

	/** 
     * Aniade a un estado del automata, un estado de llegada tras procesar un simbolo
     * @param estadoI nombre del estado al que se le añadira un estado adyacente
     * @param simbolo simbolo que lleva a otro estado
     * @param estadoF nombre del estado al que se llega al procesar en estadoI el simbolo
     * @see Estado, metodo addAdyacente
     * @see Automata, metodo contiene
     */
	public void addAdyacente(String estadoI,String simbolo,String estadoF){
		/*
		* Se crea un objeto estado con los nombres estadoI y estadoF
		*/
		Estado estadoA=new Estado(estadoI), estadoB=new Estado(estadoF);
		int indiceEstadoI,indiceEstadoF;
		/*
		* Se llama al metodo contiene
		* Se evalua si este autómata contiene un estado
		*/
		if(contiene(estadoA) && contiene(estadoB)){
			/*
			* Si se contienen ambos estados
			* Se guarda la posicion que tienen en este automata
			*/
			indiceEstadoI=automata.indexOf(estadoA);
			indiceEstadoF=automata.indexOf(estadoB);
			/*
			* Se obtiene el estadoI en este automata
			* Se llama al estado addAdyacente de Estado
			* Se le pasa simbolo como la llave del HashMap
			* indiceEstadoF, se envia la posición en la que esta el estado adyacente en este automata
			*/
			automata.get(indiceEstadoI).addAdyacente(indiceEstadoF,simbolo);
			/*
			* Se manda int, pues, al procesar cadenas
			* Es mas facil encontrar con posiciones
			* En lugar de crear un objeto estado con la lista de nombres que se regresen
			* Y luego buscar su posicion
			*/
		}
	}

	/** 
     * Aniade a la lista de estados finales, un valor numerico, que
     * representa la posicion en este automata donde hay un estado final
     * @param i posicion en este automata donde hay un estado final
     */
	public void addFinalState(int i){
		estadosFinales.add(i);
		/*
		* Se tiene una lista de estados finales
		* Que permita hacer la insterseccion de resultados
		* Y ver si la cadena es aceptada
		*/
	}

	/** 
     * Para cada simbolo, se genera un ArrayList con los estados a los que se llegara
     * @param index valor numerico para decir el elemento que deseamos de este automata
     * @return objeto de la clase Estado en la posicion index de este automata
     */
	public Estado get(int index){
		return automata.get(index);
	}

	/** 
     * Regresa la posicion de un estado al pasar como parametro su nombre
     * @param estado , cadena con el nombre del estado a buscar
     * @return indice donde se encuentra el Estado con nombre 'estado', regresa -1 si no se encuentra
     */
	public int indexOf(String estado){
		return automata.indexOf(new Estado(estado));
	}

	/** 
     * @return cadena con todos los estados que conforman este automata
     */	
	public String toString(){
		return automata.toString();
	}

	/** 
     * @param e Objeto de la clase Estado queue se desea saber si se contiene en este automata
     * @return true si este automata contiene el Estado e, false si no lo tiene
     */	
	public boolean contiene(Estado e){
		return automata.contains(e);
	}

	/** 
     * @return Numero de estados que contiene este automata
     */	
	public int size(){
		return automata.size();
	}

	/** 
	 * Indica el indice donde esta el estado inicial de este automata 
     * @param intialState valor numerico con la posicion en la que se encuentra el estado inicial de este automata
     */	
	public void setInitialState(int initialState){
		this.initialState=initialState;
	}

	/** 
	 * Regresa una cadena con los resultados de procesar una cadena
	 * asi como del procesamiento simbolo por simbolo
	 * y los resultados finales
     * @param cadena La cadena con simbolos a procesar
     * @see append
     * @see getModifiedString
     * @see procesarSimbolo
     * @see Estado, funcionDeTransicion
     */	
	public String funcionTransicionExtendida(String cadena){
		megaResultado+=""; //Variable para guardar todo el resultado
		/*
		* Se indica el procesamiento de una cadena
		* Se elimina ª que es nuestro equivalente de lambda
		*/
		megaResultado="Procesamiento de: "+(cadena.replaceAll("ª","lmd"))+" en el automata: "+nombreAutomata+"\n\n";
		char[] simbolos=cadena.toCharArray();
		int simbolPos=0;


		megaResultado+=automata.get(initialState).toString()+"=>lmd : {";
		ArrayList<Integer> results=new ArrayList<Integer>();

		/*
		*Del estado inicial se buscan sus transiciones lambda
		*/
		ArrayList<Integer> listaEstados=automata.get(
			initialState).funcionDeTransicion("lmd");
		/*
		* De cada listaDeEstados se evalua si es posible llegar a un más lejos con lambda
		*/

		for(int i=0;!listaEstados.isEmpty() && i<listaEstados.size();i++){
			listaEstados=append(listaEstados,automata.get(listaEstados.get(i)).funcionDeTransicion("lmd"));
		}

		/*
		* De sus resultados se pegan al megaResultado
		*/
		for (int i=0;!listaEstados.isEmpty() && i<listaEstados.size();i++) {
			if(i==listaEstados.size()-1){
				megaResultado+=automata.get(listaEstados.get(i)).toString()+"}\n\n";
			}else{
				megaResultado+=automata.get(listaEstados.get(i)).toString()+",";
			}
		}

		/*
		* Se recorre simbolo por simbolo
		* por cada simbolos se hace el proceso del conjunto de estados
		* todo se va añadiendo al megaResultado
		*/
		for(int i=0;i<simbolos.length;i++){
			megaResultado+="Proceso de: "+(getModifiedString(simbolos,i).replaceAll("ª","lmd"));
			String aux="";
			aux+=simbolos[i];
			ArrayList<Integer> al=procesarSimbolo(listaEstados,aux);
			listaEstados=al;
		}


		boolean aceptada=false;
		/*
		* Se evalua si el conjunto de estado resultantes
		* Tiene al menos un estado final 
		*/
		for (int i=0;i<listaEstados.size();i++) {
			if(estadosFinales.contains(listaEstados.get(i))){
				aceptada=true;
				break;
			}
		}


		if(aceptada){
			megaResultado+="\nLa cadena: "+(cadena.replaceAll("ª","lmd"))+" es aceptada";
		}else{
			megaResultado+="\nLa cadena: "+(cadena.replaceAll("ª","lmd"))+" no es aceptada";
		}
		return megaResultado;
	}

	/** 
	 * Regresa una cadena modificada
	 * se remarca el simbolo en el que esta el proceso actual
	 * Si se esta procesando el segundo simbolo de aab
	 * regresaria a[a]b
	 * y los resultados finales
     * @param simbolos Lista de simbolos que conforman la cadena
     * @param pos Posicion actual en la que se evalua una cadena
     * @return cadena modificada
     */	
	private String getModifiedString(char[] simbolos, int pos){
		String str="";
		int counter=0;
		for (char c : simbolos) {
			if(counter==pos){
				String aux="";
				aux+=c;
				aux.replaceAll("ª","lmd");
				str+=("["+aux+"]");
			}else{
				str+=c;
			}
			counter++;
		}
		return str+"\n";
	}


	/** 
	 * Regresa la lista de estados finales de este automata
     * @return lista de estados finales como un string
     */	
	public String getFinalStates(){
		return estadosFinales.toString();
	}

	/** 
	 * Regresa una cadena modificada
	 * se remarca el simbolo en el que esta el proceso actual
	 * Si se esta procesando el segundo simbolo de aab
	 * regresaria a[a]b
	 * y los resultados finales
     * @param listaEstados Conjunto de estados a ser procesados
     * @param simbolo Simbolo con el que se evaluaran los estados anteriores
     * @see Auomata append
     * @see Estado funcionDeTransicion
     * @return conjunto de estados resultantes del proceso
     */	
	public ArrayList<Integer> procesarSimbolo(
		ArrayList<Integer> listaEstados, String simbolo){

		/*
		* Se inicializa variable para guardar resultados
		*/
		ArrayList<Integer> results=new ArrayList<Integer>();

		/*
		* Para cada estado, se evalua el simbolo y se gurada en la 
		* variable results
		*/
		for (int i=0;i<listaEstados.size();i++) {

			megaResultado+=automata.get(listaEstados.get(i)).toString()+"=>"+simbolo.replaceAll("ª","lmd")+": ";

			ArrayList<Integer> dataAux=automata.get(listaEstados.get(i)).funcionDeTransicion(simbolo);

			results=append(results,automata.get(
				listaEstados.get(i)).funcionDeTransicion(simbolo));

			if(dataAux.isEmpty()){
				megaResultado+="{ }\n";
			}else{
				megaResultado+="{ ";
				for(int k=0;k<dataAux.size();k++){
					if(k==dataAux.size()-1){
						megaResultado+=automata.get(dataAux.get(k)).toString()+" }\n";
					}else{
						megaResultado+=automata.get(dataAux.get(k)).toString()+" , ";
					}
				}
			}
		}

		megaResultado+="lmd( { ";
		for(int i=0;i<results.size();i++){
			if(i==results.size()-1){
				megaResultado+=automata.get(results.get(i)).toString();
			}else{
				megaResultado+=automata.get(results.get(i)).toString()+" , ";
			}
		}
		megaResultado+=" } )";

		/*
		*Por cada resultado
		*Se obtiene a donde se llega con lambda
		*/
		ArrayList<String> resultAux=new ArrayList<String>();
		ArrayList<Integer> lambdaResults=new ArrayList<Integer>();
		for (int i=0;!results.isEmpty() && i<results.size();i++) {
			//lambdaResults=append(lambdaResults,automata.get(results.get(i)).funcionDeTransicion("lmd"));
			results=append(results,automata.get(results.get(i)).funcionDeTransicion("lmd"));
		}


		megaResultado+=" = { ";
		for(int i=0;i<results.size();i++){
			if(i==results.size()-1){
				megaResultado+=automata.get(results.get(i)).toString();
			}else{
				megaResultado+=automata.get(results.get(i)).toString()+" , ";
			}
		}
		megaResultado+=" }\n\n";


		return results;

		
	}

	/** 
     * Une dos ArrayList
     * @param a ArrayList al que se le aniadiran los datos de b
     * @param b ArrayList con datos
     * @return Lista con la union de a y b
     */
	public ArrayList append(
		ArrayList<Integer> a, ArrayList<Integer> b){

		/*Se recorren todos los elementos de b*/
		for(int i=0;i<b.size();i++){
			/*
			* Si a no contiene el enesimo elemento de b, se aniade dicho elemento
			* En caso contrario, se ignora el elemento para evitar repetidos
			*/
			if(a.contains(b.get(i))==false){
				a.add(b.get(i));	
			}
		}
		return a;
	}

	/** 
     * Para cada elemento de este automata
     * Se indica que cada estado procesando lambda llega a el mismo estado
     * @see Estado, metodo addAdyacente
     */	
	public void setSelfLamda(){
		for (int i=0;i<size();i++) {
			automata.get(i).addAdyacente(i,"lmd");
		}
	}

	/** 
     * Se pone un nombre al automata, el nombre es el mismo nombre que 
     * el del archivo del que fue cargado
     * @param nombreAutomata
     */
	public void setNombreAutomata(String nombreAutomata){
		this.nombreAutomata=nombreAutomata;
	}

}