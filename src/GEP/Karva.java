package project2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;


class Function {
	
	private String name;
	private int cardinality;
	private char simbol;
	
	//Constructors
	public Function(String name, int cardinality, char simbol){
		this.name = name;
		this.cardinality = cardinality;
		this.simbol = simbol;
		
	}
	
	//Setters & Getters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCardinality() {
		return cardinality;
	}
	public void setCardinality(int cardinality) {
		this.cardinality = cardinality;
	}
	public char getSimbol() {
		return simbol;
	}
	public void setSimbol(char simbol) {
		this.simbol = simbol;
	}
	
}
class Terminal {
	
	private String name;
	private char simbol;
	
	//Constructors
	public Terminal(String name, char simbol){
		this.name = name;
		this.simbol = simbol;
	}
	
	//Setters & Getters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public char getSimbol() {
		return simbol;
	}
	public void setSimbol(char simbol) {
		this.simbol = simbol;
	}

}


class rep2pro {
	// Given a Karva representation, converts it into a program string
	public static String makePro (String repres){
		String prog = " ";
		int Ep;

		for(int index=0 ; index < repres.length();index++){

			Ep = Emptypos(prog);
			if (Ep != -1)
				for(int f_index=0; f_index < Karva.getFunctionSet().size(); f_index ++ )
					if (repres.charAt(index)==Karva.getFunctionSet().get(f_index).getSimbol()){
						if (Ep == 0)
							prog = ""+Karva.getFunctionSet().get(f_index).getName();
						else
							prog = prog.substring(0, Ep) + Karva.getFunctionSet().get(f_index).getName() + prog.substring(Ep+1, prog.length());

					}

			if (Ep != -1)
				for(int f_index=0; f_index < Karva.getTerminalSet().size(); f_index ++ )
					if (repres.charAt(index)==Karva.getTerminalSet().get(f_index).getSimbol()){
						prog = prog.substring(0, Ep) + Karva.getTerminalSet().get(f_index).getName() + prog.substring(Ep+1, prog.length());

					}
		}

		//prog = prog +';';
		return prog;
	}
	// Calculate where is the empty place 
	public static int Emptypos (String iprog){

		for (int i=0 ; i < iprog.length() ; i++){			
			if(iprog.charAt(i)==' '){

				return i;  // return the position of Empty place

			}
		}	
		return -1;

	}

}

interface Chromossome extends Comparator<Karva>, Comparable<Karva> {

    // constant declarations
	String representation ="";
	double fitness = 0;
 
    // method signatures
	public void setFitness(double fitness);
	public double getFitness();
}

class Karva implements Chromossome { 
	
	private static ArrayList<Function> functionSet;
	private static ArrayList<Terminal> terminalSet;
	
	private static int headSize;
	private static int tailSize;
	
	private String representation;
	private double fitness;
	
	/* CONSTRUCTORS */
	public Karva (String representation){
		this.representation = representation;
	}
	
	/* SET UP */
	public static void languageSetUp(ArrayList<Function> functions, ArrayList<Terminal> terminals, int h){
		
		functionSet = functions;
		terminalSet = terminals;
		headSize = h;
		
		int n = maxCardinality(functionSet);
		
		//System.out.print(n);
		tailSize = h*(n-1) + 1;
	}
	
	/* GETTERS & SETTERS */
	public static ArrayList<Function> getFunctionSet() {
		return functionSet;
	}
	public static void setFunctionSet(ArrayList<Function> functionSet) {
		Karva.functionSet = functionSet;
	}
	public static ArrayList<Terminal> getTerminalSet() {
		return terminalSet;
	}
	public static void setTerminalSet(ArrayList<Terminal> terminalSet) {
		Karva.terminalSet = terminalSet;
	}
	public static int getHeadSize() {
		return headSize;
	}
	public static void setHeadSize(int headSize) {
		Karva.headSize = headSize;
				
		int n = maxCardinality(functionSet);
		
		tailSize = headSize*(n-1) + 1;
		
	}
	public static int getTailSize() {
		return tailSize;
	}
	public static void setTailSize(int tailSize) {
		Karva.tailSize = tailSize;
	}
	public String getRepresentation() {
		return representation;
	}
	public void setRepresentation(String representation) {
		this.representation = representation;
	}
	public double getFitness() {
		return fitness;
	}
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public String readHeader(){

		try {
			return new String(Files.readAllBytes(Paths.get("C:\\_test\\prog2\\header")));
		} catch (IOException e) {	e.printStackTrace();	}

		return null;
	}
	public String readFooter(){
		
		try {
			return new String(Files.readAllBytes(Paths.get("C:\\_test\\prog2\\footer")));
		} catch (IOException e) {	e.printStackTrace();	}

		return null;

	}
	
	
	
	// COMPARATOR (based on fitness)
	@Override
	public int compareTo(Karva kExpression) {
		if(this.getFitness()>kExpression.getFitness())
			return -1;
		else if(this.getFitness()<kExpression.getFitness())
			return 1;
		else
			return 0;
	}
	@Override
	public int compare(Karva kExpression0, Karva kExpression1) {
		if(kExpression0.getFitness()>kExpression1.getFitness())
			return -1;
		else if(kExpression0.getFitness()<kExpression1.getFitness())
			return 1;
		else
			return 0;
	}
	
	
	/* METHODS */
	// Find max Cardinality from a set of Functions
	private static int maxCardinality(ArrayList<Function> functions){
		int maxCardinality = 0;
		
		for(int i=0; i< functions.size(); i++)
			if(functions.get(i).getCardinality()>maxCardinality)
				maxCardinality = functions.get(i).getCardinality();

		return maxCardinality;
	}
	
	// Returns the size of each chromossome
	public static int getChromossomeSize(){
		return headSize + tailSize;
	}

	// Generates a random K-expression with the given length
	public static Karva generate(){

		Random dice = new Random();
		
		// the default kExpression
		String kExpression = "";
		int index;
		
		
		// Get the first simbol from the function set (we can't start with a terminal..)
		kExpression += functionSet.get(dice.nextInt(functionSet.size())).getSimbol();
		
		// Fill the rest of the head with either functions or terminals
		for(int i_h=1; i_h<getHeadSize(); i_h++){
			index = dice.nextInt(functionSet.size()+terminalSet.size());
			if(index<functionSet.size())
				kExpression += functionSet.get(index).getSimbol();
			else
				kExpression += terminalSet.get(index-functionSet.size()).getSimbol();
		}
		// Fill the tail with terminals
		for(int i_h=0; i_h<getTailSize(); i_h++){
			index = dice.nextInt(terminalSet.size());
			kExpression += terminalSet.get(index).getSimbol();
		}
		
		Karva expression = new Karva(kExpression);
		// return the Karva object with the expression
		return expression;		
	}

	// Generates a population of N random kExpressions
	public static ArrayList<Karva> generatePopulation (int N){

		// List that will store the generated population of elements
		ArrayList<Karva> population = new ArrayList<Karva>();

		//Generate N elements
		for(int i=0; i<N; i++)
			population.add(generate());

		// Return the generated population
		return population;
	}

	// Print a list of kExpressions, one per line
	public static void printListOnePerLine(ArrayList<Karva> kExpressions){

		Iterator<Karva> iterator = kExpressions.iterator();

		while(iterator.hasNext())
			System.out.println(iterator.next().getRepresentation());
	}
	
	// Given a list of Karva expressions, calculate each individual fitness and fills the fitness parameter
	public static void populationFitness (ArrayList<Karva> population, ArrayList<Sequence> testingSequences){
		
		Iterator<Karva> iterator = population.iterator();
		
		while(iterator.hasNext()){
			
			Karva element = iterator.next();
			
			//generate expression from representation
			String expression = rep2pro.makePro(element.getRepresentation());
			
			//calculates fitness using the testing sequences
			int fitness = SortProgram.runSort_Evaluate(expression, testingSequences);
			
			//Assigns the fitness to the Karva expression
			element.setFitness(fitness);
		}
			
		
	}
	
	
	// Given a list with binaryString representations, calculate each individual fitness and generate a	
	// list with all the population as BinaryStrings
	/*public static ArrayList<Karva> populationFitness (ArrayList<String> population){

		// ArrayList that will store the population of binaryStrings
		ArrayList<BinaryString> populationBS = new ArrayList<BinaryString>();

		// For every representation
		Iterator<String> iterator = population.iterator();
		while(iterator.hasNext()){

			// get the string element
			String stringElement = iterator.next();

			//create the binaryString element and set its representation and fitness
			BinaryString bsElement = new BinaryString();
			bsElement.setBinaryString(stringElement);
			bsElement.setFitness(onemax(stringElement, stringElement.length()));	// fitness is given by onemax function

			// add the BinaryString element to the BinaryString population
			populationBS.add(bsElement);
		}

		// return the population of BinaryStrings
		return populationBS;
	}*/

	// Search for the fittest element
	public static Karva fitness_max(ArrayList<Karva> kExpressions){
		//the fittest so far
		Karva fittest = kExpressions.get(0);
		Karva current;
		
		Iterator<Karva> iterator = kExpressions.iterator();
		while(iterator.hasNext()){
			current = iterator.next();
			if(current.getFitness()>fittest.getFitness())
				fittest = current;
		}
		return fittest;
		
	}
	
	// Search for the unfittest element
	public static Karva fitness_min(ArrayList<Karva> kExpressions){
		//the most unsuited so far
		Karva unsuited = kExpressions.get(0);
		Karva current;

		Iterator<Karva> iterator = kExpressions.iterator();
		while(iterator.hasNext()){
			current = iterator.next();
			if(current.getFitness()<unsuited.getFitness())
				unsuited = current;
		}

		return unsuited;

	}
	
	// Calculate the average fitness for a list of kExpressions
	public static Double fitness_average(ArrayList<Karva> kExpressions){
		//the most unsuited so far
		double accumulator = 0;

		Iterator<Karva> iterator = kExpressions.iterator();
		while(iterator.hasNext()){
			accumulator += iterator.next().getFitness();
		}

		return (double)accumulator/(double)kExpressions.size();

	}
	
	// Calculate the standard deviation fitness for a list of binaryStrings
	public static Double fitness_stdDeviation(ArrayList<Karva> kExpressions){
		double average = fitness_average(kExpressions);
		
		//accumulator
		double variance_sum = 0;

		Iterator<Karva> iterator = kExpressions.iterator();

		//calculate and sum the difference of each data point from the mean, and square the result of each:
		while(iterator.hasNext())
			variance_sum += Math.pow((double)(iterator.next().getFitness()-average),2);
		
		double variance_mean = (double)variance_sum/(double)kExpressions.size();
		
		return Math.sqrt(variance_mean);
	}

}	