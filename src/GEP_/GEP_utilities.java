package GEP;

import java.util.ArrayList;
import java.util.Iterator;

class Permutation {
	
	private ArrayList<Integer> permutation;
	
	/* CONSTRUCTORS */
	public Permutation(){
		this.permutation = new ArrayList<Integer>();
	}
	public Permutation(ArrayList<Integer> permutation){
		this.permutation = permutation;
	}

	/* GETTERS & SETTERS */
	public ArrayList<Integer> getPermutation() {
		return permutation;
	}
	public void setPermutation(ArrayList<Integer> permutation) {
		this.permutation = permutation;
	}
	
	/* METHODS */
	// prints one element of the permutation per line
	public void print_1perLine(){
		//
		for(int i=0;i<permutation.size();i++)
			System.out.println(permutation.get(i));
	}
	
	// swaps the elements of the referred positions in the permutation list
	public void swap(int a, int b){
		int aux = permutation.get(a);
		permutation.set(a, permutation.get(b));
		permutation.set(b,aux);
	}

	// generates a random permutation of size N using N-1 random numbers
	public static Permutation generateRandom(int N){

		//The permutation
		ArrayList<Integer> permutation_ = new ArrayList<Integer>();

		//Generate the permutation ordered 
		//for( i=0; i<N; i++)
		for(int i=0; i<N;i++)
			// v[i] = i;
			permutation_.add(i);

		// Instantiate the permutation object
		Permutation permutation = new Permutation(permutation_);

		// Swap the elements to generate a random permutation
		//for(i=0;i<N-1;i++)
		for(int i=0; i<N-1;i++){
			// r = obtain a uniformly generated random integer in [i..N-1];
			int mappedPosition = randomMap(i, N-1, RandomGenerator.generate());

			// exchange the contents of v[i] with the contents of v[r]
			permutation.swap(i, mappedPosition);
		}

		//returns the random permutation
		return permutation;
	}

	// Receives a random number and the smaller and larger limits. Maps the random number to an integer between those limits
	private static int randomMap(double a, double b, double u){
		return (int)(a + (Math.floor(u * (double)(b - a + 1))));
	}
}

class RandomGenerator{
	
	public static double generate(){
		return Math.random();
	}

	// Generates an Integer random number inside the given limits (using floor)
	public static int generateInRange_int(int min, int max){
		return (int)(min + (Math.floor(RandomGenerator.generate() * (double)(max - min + 1))));
	}
	
	// Generates a random number inside the given limits
	public static double generateInRange(double min, double max){
		return generate() * (double)(max - min);
	}
}

class Statistics{
	
	//Returns the average value of a list of values
	public static double average(ArrayList<Double> list){
		//accumulator
		double accumulator = 0;

		Iterator<Double> iterator = list.iterator();

		//calculate sum of all elements
		while(iterator.hasNext())
			accumulator += iterator.next();
		
		//return average
		return (double)accumulator/(double)list.size();
	}
	
	public static double standardDeviation(ArrayList<Double> list){
		double average = average(list);
		
		//accumulator
		double variance_sum = 0;

		Iterator<Double> iterator = list.iterator();

		//calculate and sum the difference of each data point from the mean, and square the result of each:
		while(iterator.hasNext())
			variance_sum += Math.pow((double)(iterator.next()-average),2);
		
		double variance_mean = (double)variance_sum/(double)list.size();
		
		return Math.sqrt(variance_mean);

	}
}