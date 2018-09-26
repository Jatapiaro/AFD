package Automata;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;

/**
 * Una clase para representar los estados contenidos
 * en un automata.
 * @version 1.1, 12/04/2016
 * @author Jacobo Tapia
 * @author Ketzia Dante
 */

public class Estado{
	
	private String name; //Nombre del estado
	private boolean isInitial,isFinal; //Describe si es estado final y/o incial
	private HashMap<String,ArrayList<Integer>> tablaTransicion; //Transiciones del estado, HashMap, con ArrayList

	 /** 
     * Crea un estado del autómata.
     * @param name Nombre del estado.
     * @param alfabeto Alfabeto del autómata
     * @see crearTablaTransicion
     */
	public Estado(String name,String[] alfabeto){
		this.name=name;
		this.tablaTransicion=crearTablaTransicion(alfabeto);
		isInitial=isFinal=false;
	}


	/** 
     * Crea un estado del autómata.
     * @param name Nombre del estado, únicamente para el método equals.
     */
	public Estado(String name){
		this.name=name;
		isInitial=isFinal=false;
	}

	/** 
     * Marca el estado como inicial.
     * @param isInitial, valor bool, que describe si el estado es inicial.
     */
	public void setInitial(boolean isInitial){
		this.isInitial=isInitial;
	}


	/** 
     * Marca el estado como final.
     * @param isFinal, valor bool, que describe si el estado es final.
     */
	public void setFinal(boolean isFinal){
		this.isFinal=isFinal;
	}

	/** 
     * Crea la lista de transiciones de el estado con cada simbolo del alfabeto.
     * @param alfabeto, arreglo de strings con cada simbolo del alfabeto.
     * @see crearTablaTransicion
     */
	public void setAlfabeto(String[] alfabeto){
		/*Se manda a llamar el método crearTablaTransicion()*/
		this.tablaTransicion=crearTablaTransicion(alfabeto);
	}

	/** 
     * Se aniade un estado de llegada al procesar un simbolo
     * @param estado Valor int de la posicion de un estado en el aútomata
     * @param simbolo Simbolo que al procesar lleva al "estado"
     */
	public void addAdyacente(int estado,String simbolo){
		//this.tablaTransicion.get(simbolo).add(estado);
		if(!this.tablaTransicion.get(simbolo).contains(estado)){
			this.tablaTransicion.get(simbolo).add(estado);
		}
	}


	/** 
     * Regresa todos los estados a los que se llega al procesar un simbolo
     * @param simbolo parametro para encontrar los estados a los que se llega al ser procesado
     * @return Regresa la lista de estados a los que se llega si el HashMap contiene el simbolo, en caso contrario regresa una lista vacia
     */
	public ArrayList<Integer> funcionDeTransicion(String simbolo){

		/*Si se recibe lambda, llega como 'ª', por lo que se convierte esa cadena a 'lmd'*/
		simbolo=(simbolo.equals("ª"))? "lmd" : simbolo;
		/*Si la tabla contiene el simbolo*/
		if(tablaTransicion.containsKey(simbolo)){
			/*Regresa la lista de estados a los que se llega*/
			return this.tablaTransicion.get(simbolo);
		}else{
			/*Regresa una lista vacia*/
			return new ArrayList<Integer>();
		}
	}

	/** 
     * Sobreeescritura del metodo equals
     * @param other Objeto que se evalua si tiene el mismo nombreq que este estado
     * @return Regresa true si un estado es igual a otro
     */
	@Override
	public boolean equals(Object other){
		return this.name.equals(((Estado)other).toString())? true:false;
	}

	/** 
     * Sobreeescritura del metodo toString
     * @return Nombre del estado
     */
	public String toString(){
		return this.name;
	}



	/** 
     * Crea el HashMap inicial
     * Los simbolos son la llave
     * Para cada simbolo, se genera un ArrayList con los estados a los que se llegara
     * @param alfabeto Lista de todos los elementos del alfabeto
     * @return Regresa el HashMap con los simbolos del alfabeto como llaves
     */
	private static HashMap<String,ArrayList<Integer>> crearTablaTransicion(String[] alfabeto){
		HashMap tabla=new HashMap<String,ArrayList<Integer>>();
		/* Se pone por defecto, el símbolo lambda */
		tabla.put("lmd",new ArrayList<Estado>());
		for (String simbolo : alfabeto) {
			tabla.put(simbolo,new ArrayList<Estado>());
		}
		return tabla;
	}
}