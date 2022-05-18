package GEP;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class SortProgram {

	public static void runSort(String expression) {
		
		//Example of working expression
		//String expression = "dobl(0,e1m(len),dobl(0,e1m(len),swap(wismaller(e1p(index),index),index)))";
		
		//The actual expression
		//String expression = "dobl(0,e1m(len),dobl(0,e1m(len),swap(wismaller(e1p(index),index),index)))";
		
		int len = 10;
		
		//create a random sequence
		Sequence sequenceA = new Sequence(len, true);
		Sequence sequenceB = new Sequence(len, false);
		
		sequenceB.copy(sequenceA);
		
		//setup the algorithm using
		expressionHandler.setSequence(sequenceB);
		expressionHandler.setIndex(0);
		expressionHandler.setLen(len);
		
		//run the algorithm using the expression
		TreeNode root = expressionHandler.expressionToTree(expression);
		expressionHandler.runExp(root);
		
		//print the resulting sequence
		sequenceA.print();
		sequenceB.print();
		System.out.println(sequenceA.compareChanges(sequenceB));
	}
	
	public static void runSort(String expression, Sequence sequence) {
		
		//Example of working expression
		//String expression = "dobl(0,e1m(len),dobl(0,e1m(len),swap(wismaller(e1p(index),index),index)))";
		
		//The actual expression
		//String expression = "dobl(0,e1m(len),dobl(0,e1m(len),swap(wismaller(e1p(index),index),index)))";
		
		int len = sequence.getSequence().length;
		
		//create a random sequence
		Sequence sequence_sorted = new Sequence(len, false);
		
		sequence_sorted.copy(sequence);
		
		//setup the algorithm using
		expressionHandler.setSequence(sequence_sorted);
		expressionHandler.setIndex(0);
		expressionHandler.setLen(len);
		
		//run the algorithm using the expression
		TreeNode root = expressionHandler.expressionToTree(expression);
		expressionHandler.runExp(root);
		
		//print the resulting sequence
		sequence.print();
		sequence_sorted.print();
		System.out.println(sequence.compareChanges(sequence_sorted));
	}


	public static void runSort(String expression, ArrayList<Sequence> tests) {

		//Example of working expression
		//String expression = "dobl(0,e1m(len),dobl(0,e1m(len),swap(wismaller(e1p(index),index),index)))";

		//The actual expression
		//String expression = "dobl(0,e1m(len),dobl(0,e1m(len),swap(wismaller(e1p(index),index),index)))";

		for (int i=0; i<tests.size();i++){
			
		
			int len = tests.get(i).getSequence().length;
	
			//create a random sequence
			Sequence sequence_sorted = new Sequence(len, false);
	
			sequence_sorted.copy(tests.get(i));
	
			//setup the algorithm using
			expressionHandler.setSequence(sequence_sorted);
			expressionHandler.setIndex(0);
			expressionHandler.setLen(len);
	
			//run the algorithm using the expression
			TreeNode root = expressionHandler.expressionToTree(expression);
			expressionHandler.runExp(root);
	
			//print the resulting sequence
			tests.get(i).print();
			sequence_sorted.print();
			int defaultFitness = tests.get(i).sortFitness();
			double defaultFitnessScaled = tests.get(i).sortFitnessScaled();
			System.out.println(sequence_sorted.sortFitness() - defaultFitness);
			System.out.println(sequence_sorted.sortFitnessScaled() - defaultFitnessScaled);
			System.out.println(sequence_sorted.compareChanges(tests.get(i)));
			System.out.println();
		}
	}
	

	public static int runSort_Evaluate(String expression, ArrayList<Sequence> tests) {

		//Example of working expression
		//String expression = "dobl(0,e1m(len),dobl(0,e1m(len),swap(wismaller(e1p(index),index),index)))";

		//The actual expression
		//String expression = "dobl(0,e1m(len),dobl(0,e1m(len),swap(wismaller(e1p(index),index),index)))";

		int fitness = 0;

		for (int i=0; i<tests.size();i++){


			int len = tests.get(i).getSequence().length;

			//create a random sequence
			Sequence sequence_sorted = new Sequence(len, false);

			sequence_sorted.copy(tests.get(i));

			//setup the algorithm using
			expressionHandler.setSequence(sequence_sorted);
			expressionHandler.setIndex(0);
			expressionHandler.setLen(len);

			//run the algorithm using the expression
			TreeNode root = expressionHandler.expressionToTree(expression);
			expressionHandler.runExp(root);

			//print the resulting sequence
			tests.get(i).print();
			sequence_sorted.print();
			
			int defaultFitness = tests.get(i).sortFitness();
			double defaultFitnessScaled = tests.get(i).sortFitnessScaled();
			
			//fitness += sequence_sorted.sortFitness() - defaultFitness;
			fitness += sequence_sorted.sortFitnessScaled() - defaultFitnessScaled;
			fitness += sequence_sorted.compareChanges(tests.get(i));
		}
		
		return fitness;
		
	}

}

// Sequence is basically and array with expressions in a relevant order, it's used to create  
class Sequence{
	
	private int[] sequence;
	
	// CONSTRUCTOR //
	public Sequence(int n){
		sequence = new int[n];
	}
	public Sequence(int n, boolean randomInitialization){
		sequence = new int[n];
		if(randomInitialization){
			Random dice = new Random();
			for(int i = 0; i<n; i++)
				sequence[i]=dice.nextInt(10);
		}
	}
	
	// GETTERS AND SETTERS //
	public int[] getSequence() {
		return sequence;
	}
	public void setSequence(int[] sequence) {
		this.sequence = sequence;
	}
	
	// METHODS //

	//return the element on position "index" 
	public int get(int index){
		return sequence[index];
	}
	//replace the element on position index with "value"
	public void set(int index, int value){
		sequence[index] = value;
	}
	//replace elements on positions index1 and index2
	public void swap(int index1, int index2){
		int aux = sequence[index1];
		sequence[index1] = sequence[index2];
		sequence[index2] = aux;
	}
	//prints all the elements of the sequence separated by " "
	public void print(){
		for(int i=0; i<sequence.length;i++)
			System.out.print(sequence[i] + " ");
		System.out.println();
	}
	public void copy(Sequence sequenceB){		
		int[] a = this.getSequence();
		int[] b = sequenceB.getSequence();
		
		assert(a.length == b.length);
		
		for(int i=0; i<a.length;i++)
			a[i] = b[i];

		
	}
	public int compareChanges(Sequence sequenceB){		
		int[] a = this.getSequence();
		int[] b = sequenceB.getSequence();
		
		assert(a.length == b.length);
		
		int changes = 0;
		
		for(int i=0; i<a.length;i++)
			if(a[i] != b[i])
				changes++;
		
		return changes;
		
	}
	
	//checks neighbors to see if the first neighbor is smaller, equal or bigger than the following 
	public int sortFitness(){
		
		int fitness=0;
		
		for(int i=0; i<this.getSequence().length-1; i++)
			if(this.getSequence()[i] > this.getSequence()[i+1])
				fitness++;
			else if(this.getSequence()[i] < this.getSequence()[i+1])
				fitness--;
		
		return fitness;
		
	}
	
	// checks neighbors to see if the first neighbor is smaller, equal or bigger than the following 
	// Results are scaled so that sequence size is indifferent
	public double sortFitnessScaled(){
		
		int fitness=0;
		
		for(int i=0; i<this.getSequence().length-1; i++)
			if(this.getSequence()[i] > this.getSequence()[i+1])
				fitness++;
			else if(this.getSequence()[i] < this.getSequence()[i+1])
				fitness--;
		
		return (double)fitness/(double)this.getSequence().length;
		
	}
	
	
	//generates a random list of sequences with "numberSequences" elements, each element having a maximum size of "maximumLength"
	public static ArrayList<Sequence> generateList(int numberSequences, int maximumLength){
		
		ArrayList<Sequence> list = new ArrayList<Sequence>();
		
		Random dice = new Random();
		
		for(int i=0; i<numberSequences; i++){
			//At least 2 elements in a sequence, and a maximum of "maximumLength"
			int size = 2 + dice.nextInt(maximumLength-2);
			Sequence sequence = new Sequence(size, true);
			list.add(sequence);
		}
		
		return list;
	}

}

// Implementation of a tree data structure, each element is a node. 
class TreeNode{
	
	private String value;
	private TreeNode parent;
	private ArrayList<TreeNode> children = new ArrayList<TreeNode>();
	
	//GETTERS AND SETTERS
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	//if null, the node is the root of the tree
	public TreeNode getParent() {
		return parent;
	}
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}
	public ArrayList<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<TreeNode> children) {
		this.children = children;
	}
	
	// METHODS //
	//adds a child node to the children pool, sets the children father as this node
	public void addChild(TreeNode t){
		t.setParent(this);
		this.children.add(t);
	}
	//Returns true if there are no children
	public boolean isTerminal(){
		if(getChildren().isEmpty())
			return true;
		return false;
	}
	
	//Returns true is a node is a root node (has no parent)
	public boolean isRoot(){
		if(this.parent == null)
			return true;
		return false;
	}
}

//Class with the necessary methods and properties to handle and run expressions
class expressionHandler{
	
	private static int index;
	private static int len;
	private static Sequence sequence;
	
	// GETTERS AND SETTERS
	public static int getIndex() {
		return index;
	}
	public static void setIndex(int index) {
		expressionHandler.index = index;
	}
	public static int getLen() {
		return len;
	}
	public static void setLen(int len) {
		expressionHandler.len = len;
	}
	public static Sequence getSequence() {
		return sequence;
	}
	public static void setSequence(Sequence sequence) {
		expressionHandler.sequence = sequence;
	}

	
	// METHODS //
	
	//Gets a String with an expression an converts it to a tree
	public static TreeNode expressionToTree(String expression){
		TreeNode root = new TreeNode();
		TreeNode currentNode = root; 
		root.setParent(null);
		
		String word = "";
		char currentChar=' ';
		char previousChar;
		
		//Searches entire expression
		for(int i = 0; i<expression.length();i++){
			previousChar=currentChar;
			currentChar=expression.charAt(i);
			
			//If the actual char is different from '(' , ')' and ',' then we're in a middle of a word
			if(currentChar!='(' && currentChar!=')' && currentChar!=','){
				word+=expression.charAt(i);//keep storing the word
			
				//the word is over, we save it on the value of the current node and reset its variable

			}
			// if currentChar is a '(', the next word is an argument to the current word
			// so create a node for it and put it in a children nodes to the currentNode
			else if(currentChar=='('){
				currentNode.setValue(word);
				word="";
				TreeNode newNode = new TreeNode();
				currentNode.addChild(newNode);
				currentNode = newNode;
			}
			//if currentChar is a ',', the next word is a brother argument to the current argument
			// so we create a node and put it in the children nodes of the parent of the currentNode
			else if(currentChar==',' && previousChar!=')'){
				currentNode.setValue(word);
				word="";
				TreeNode newNode = new TreeNode();
				currentNode.getParent().addChild(newNode);
				currentNode = newNode; 
			}
			else if(currentChar==',' && previousChar==')'){
				TreeNode newNode = new TreeNode();
				currentNode.getParent().addChild(newNode);
				currentNode = newNode; 
			}
			//if currentChar is a ')' we have finished all the brother arguments of the current node
			// so we set the currentNode as the parent of the actual currentNode;
			else if(currentChar==')' && previousChar!=')'){
				currentNode.setValue(word);
				word="";
				currentNode = currentNode.getParent();
			}
			else if(currentChar==')' && previousChar==')'){
				currentNode = currentNode.getParent();
			}
				
		}
		return root;
		
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
	
	// REGARDING THE SORTING ALGORITHM //

	//Gets a treeNode containing an expression, recursively runs the expression
	public static int runExp(TreeNode t){
		Iterator<TreeNode> iterator = t.getChildren().iterator();
		
		if(t.getValue().equals("index")){ return index(); }
		else if(t.getValue().equals("len")){ return len(); }
		else if(t.getValue().equals("e1p")){ return e1p(t, iterator.next());	}	
		else if(t.getValue().equals("e1m")){ return e1m(t, iterator.next());	}
		else if(t.getValue().equals("en")){ return en(t, iterator.next()); }
		else if(t.getValue().equals("swap")){ return swap(t, iterator.next(),iterator.next()); }
		else if(t.getValue().equals("wismaller")){ return wismaller(t, iterator.next(), iterator.next()); }
		else if(t.getValue().equals("wibigger")){ return wibigger(t, iterator.next(), iterator.next()); }
		else if(t.getValue().equals("dobl")){ return dobl(t, iterator.next(), iterator.next(), iterator.next()); }
		else
			return 0;
	}

	//Literals
	private static int index(){return expressionHandler.getIndex(); }
	private static int len(){return expressionHandler.getLen(); }
	
	//Functions
	private static int e1p(TreeNode t,TreeNode arg0){
		//System.out.println("e1p");
		//if(!arg0.isTerminal()){
			//return 0;
		int x = runExp(arg0);
		return x + 1;
	}
	private static int e1m(TreeNode t,TreeNode arg0){
		//System.out.println("e1p");
		//if(!arg0.isTerminal()){
			//return 0;
		int x = runExp(arg0);
		return x - 1;
	}
	private static int en(TreeNode t,TreeNode arg0){
		//System.out.println("e1p");
		//if(!arg0.isTerminal()){
			//return 0;
		int x = runExp(arg0);
		return x * (-1);
	}
	private static int swap(TreeNode t,TreeNode arg0, TreeNode arg1){
		//System.out.println("swap");
		//if(!arg0.isTerminal() || !arg1.isTerminal())
			//return 0;
		
		int x = runExp(arg0);
		int y = runExp(arg1);
		
		//if either of the arguments is < 0 or >=len, return 0
		if(x < 0 || x >= len || y < 0 || y >= len)
			return 0;
		
		expressionHandler.getSequence().swap(x,y);
		return x;
	}
	private static int wismaller(TreeNode t, TreeNode arg0, TreeNode arg1){
		//System.out.println("wismaller");
		//if(!arg0.isTerminal() || !arg1.isTerminal())
			//return 0;
		
		int x = runExp(arg0);
		int y = runExp(arg1);
		
		//if either of the arguments is < 0 or >=len, return 0
		if(x < 0 || x >= len || y < 0 || y >= len)
			return 0;
		
		if(expressionHandler.getSequence().get(x) < expressionHandler.getSequence().get(y))
			return x;
		else
			return y;
		
	}
	private static int wibigger(TreeNode t, TreeNode arg0, TreeNode arg1){
		//System.out.println("wibigger");
		//if(!arg0.isTerminal() || !arg1.isTerminal())
			//return 0;
		
		int x = runExp(arg0);
		int y = runExp(arg1);
		
		//if either of the arguments is < 0 or >=len, return 0
		if(x < 0 || x >= len || y < 0 || y >= len)
			return 0;
		
		if(expressionHandler.getSequence().get(x) >= expressionHandler.getSequence().get(y))
			return x;
		else
			return y;
		
	}
	private static int dobl(TreeNode t, TreeNode arg0, TreeNode arg1, TreeNode arg2){
		//System.out.println("dobl");
		//if(!arg0.isTerminal() || !arg1.isTerminal())
			//return 0;
		
		int start = runExp(arg0);
		int end = runExp(arg1);
		TreeNode work = arg2;
		
		//Store the value of pre_existing index
		int indexStorer = expressionHandler.index;
		
		//Execute work from start until either len-1 or end-1 is reached
		for(expressionHandler.index=start;expressionHandler.index<len-1 || expressionHandler.index<end; expressionHandler.index++){
			runExp(work);
		}
		
		//Restore the previous value of the index
		expressionHandler.index = indexStorer;
		
		//returns (min end and len)
		return Math.min(end, len);
	}
	

}











