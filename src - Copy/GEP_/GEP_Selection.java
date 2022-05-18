package GEP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

class Truncation{
	
	// Returns a list of N elements from the specified percentage of the fittest elements of the list
	public static ArrayList<Karva> selectNfromFittestPercentage(ArrayList<Karva> elements, int N, double percentage){
		
		ArrayList<Karva> fittest = selectFittestPercentage(elements, percentage);
		ArrayList<Karva> selected = selectNfromFittestPercentage(fittest, N);
		
		return selected;
	}
	
	private static ArrayList<Karva> selectNfromFittestPercentage(ArrayList<Karva> fittestElements, int N){
		
		ArrayList<Karva> selected = new ArrayList<Karva>();
		
		//iterate through the selected elements
		Iterator<Karva> iterator = fittestElements.iterator();
		
		for(int i=0; i<N; i++){
			//if there is no more selected elements, re-assign the iterator
			if(!iterator.hasNext())
				iterator = fittestElements.iterator();
			
			selected.add(iterator.next());
		}
		
		assert selected.size() == N;
		
		return selected;
		
		
	}
	
	// Returns the specified percentage of the fittest elements of the list
	private static ArrayList<Karva> selectFittestPercentage(ArrayList<Karva> elements, double percentage){
		
		Collections.sort(elements);
		
		ArrayList<Karva> result = new ArrayList<Karva>();
		result.addAll(elements.subList(0, (int)Math.floor(elements.size()*percentage/100)));
		
		return result;
		
	}
	
}

//Stochastic Universal Sampling
class SUS{
		
	// u is a random number and numberElements is the number of elements to keep
	public static ArrayList<Karva> selectN(ArrayList<Karva> elements, int numberElements){
		
		// List containing the selected elements
		ArrayList<Karva> selected = new ArrayList<Karva>();
		
		// Wheel Distribution list
		ArrayList<Double> wheelDistribution = generateWheelDistribution(elements);
		
		// Calculates population total fitness
		double totalFitness = calculateTotalFitness(elements);
		
		// Calculates the distance between the pointers (equal distance between all points) 
		double pointerDistance = totalFitness/numberElements;
		
		// Chooses the starting point of the random points
		double startingPointer = RandomGenerator.generateInRange(0,pointerDistance);
		
		//scales the pointerDistance and startingPointer into 0 to 1 values
		double pointerDistanceScaled = pointerDistance/totalFitness;
		double startingPointerScaled = startingPointer/totalFitness;
		
		// Searches the wheel distribution for the element corresponding to each point
		Iterator<Double> iterator = wheelDistribution.iterator();
		
		int pointerNumber = 0;
		double currentPointer = startingPointerScaled;
		int index = 0;
		
		double currentWheel = iterator.next();
		
		while(pointerNumber < numberElements ){
			// add the element corresponding to the currrent slice if the random number is bigger than the current slice limit, but wasn't bigger than the previous
			if(currentPointer<=currentWheel){
				 selected.add(elements.get(index));
				 pointerNumber++;
				 currentPointer += pointerDistanceScaled; 
			}else{
				currentWheel = iterator.next();
				index++;
			}
		}
				
		return selected;
	}

	// Calculates the sum of the fitness of all elements (basic accumulation function)
	private static Double calculateTotalFitness (ArrayList<Karva> elements){
		double totalFitness = 0;
		Iterator<Karva> iterator = elements.iterator();
		while(iterator.hasNext()){
			totalFitness += iterator.next().getFitness();
		}

		return totalFitness;
	}

	// Given a list of Chromossomes (with corresponding fitnesses), generates the ordered list with the corresponding wheel "slice" for each element. 
	// Offset taken into consideration so, each element corresponds to the maximum value between 0 and 1 that belongs to the indexed "slice"
	// Ex: if element 1 has p=0.15 and element 2 has p=0.20... The list will have at least 3 elements, being the first two 0.15 and 0.35. Where 0.35 = 0.15 + 0.20.
	private static ArrayList<Double> generateWheelDistribution (ArrayList<Karva> elements){

		// Stores each slice value (slice probability + all the previous slices probabilities)
		ArrayList<Double> wheelDistribution = new ArrayList<Double>();

		// Calculates Total Fitness
		double totalFitness = calculateTotalFitness(elements);

		// Offset accumulator
		double offset = 0;

		// Computes the wheelDistribution considering each element
		Iterator<Karva> iterator = elements.iterator();
		while(iterator.hasNext()){
			offset += iterator.next().getFitness() / totalFitness;
			wheelDistribution.add(offset);
		}

		// The offset value has to be exactly 1 uppon accumulating all the elements.
		assert(offset==1);

		return wheelDistribution;
	}

}

// Can be improved, generating the Wheel Distribution can be done only once for all the population,
// it's not necessary to generate it every time the wheel is executed
class RouletteWheel{

	// O(3*n)
	/*public static ArrayList<Karva> selectN(ArrayList<Karva> elements, int N){

		// Wheel Distribution list
		ArrayList<Double> wheelDistribution = generateWheelDistribution(elements);

		// List with the selected elements
		ArrayList<Karva> selected = new ArrayList<Karva>();

		for(int i=0; i<N; i++){
			// Searches for the slice corresponding to the given random number;
			int index = 0;
			Iterator<Double> iterator = wheelDistribution.iterator();

			double random = RandomGenerator.generate();
			while(iterator.hasNext())
				// return the element corresponding to the currrent slice if the random number is bigger than the current slice limit, but wasn't bigger than the previous
				if(random<iterator.next()){
					selected.add(elements.get(index));
					break;
				}
				else
					index++;
		}


		return selected;

	}*/


	public static ArrayList<Karva> selectN(ArrayList<Karva> elements, int N){
		ArrayList<Karva> selected = new ArrayList<Karva>();
		for(int i=0; i<N; i++)
			selected.add(selectOne(elements));

		return selected;
	}

	// O(3*n)
	public static Karva selectOne(ArrayList<Karva> elements){

		// Wheel Distribution list
		ArrayList<Double> wheelDistribution = generateWheelDistribution(elements);

		// Searches for the slice corresponding to the given random number;
		int index = 0;
		Iterator<Double> iterator = wheelDistribution.iterator();

		double random = RandomGenerator.generate();

		while(iterator.hasNext())
			// return the element corresponding to the currrent slice if the random number is bigger than the current slice limit, but wasn't bigger than the previous
			if(random<iterator.next())
				return elements.get(index);
			else
				index++;

		return null;

	}

	// Given a list of BinaryStrings (with corresponding fitnesses), generates the ordered list with the corresponding wheel "slice" for each element. 
	// Offset taken into consideration so, each element corresponds to the maximum value between 0 and 1 that belongs to the indexed "slice"
	// Ex: if element 1 has p=0.15 and element 2 has p=0.20... The list will have at least 3 elements, being the first two 0.15 and 0.35. Where 0.35 = 0.15 + 0.20.
	private static ArrayList<Double> generateWheelDistribution (ArrayList<Karva> elements){

		// Stores each slice value (slice probability + all the previous slices probabilities)
		ArrayList<Double> wheelDistribution = new ArrayList<Double>();

		// Calculates Total Fitness
		double totalFitness = calculateTotalFitness(elements);

		// Offset accumulator
		double offset = 0;

		// Computes the wheelDistribution considering each element
		Iterator<Karva> iterator = elements.iterator();
		while(iterator.hasNext()){
			offset += iterator.next().getFitness() / totalFitness;
			wheelDistribution.add(offset);
		}

		// The offset value has to be exactly 1 uppon accumulating all the elements.
		assert(offset==1);

		return wheelDistribution;
	}

	// Calculates the sum of the fitness of all elements (basic accumulation function)
	private static Double calculateTotalFitness (ArrayList<Karva> elements){
		double totalFitness = 0;
		Iterator<Karva> iterator = elements.iterator();
		while(iterator.hasNext()){
			totalFitness += iterator.next().getFitness();
		}

		return totalFitness;
	}

}

class TournamentWithoutReplacement {
	// N/s tournaments
	// s -> number of chosen elements
	// s * (N - 1) random numbers
	
	// Runs tournament on a list of Chromossomes N times, randomly chooses s winner elements each time
	// From these s winner elements choose the fittest as the ultimate winner of one from those N tournaments
	public ArrayList<Karva> runTournaments(int N, int s, ArrayList<Karva> elements){
		
		// List of winners
		ArrayList<Karva> winners = new ArrayList<Karva>();
		
		// You will	need to generate a random permutation of the population s times
		ArrayList<Permutation> permutations = generatePermutations(N,s);
		assert(permutations.size()==s); 
		
		// For each permutation
		for(int i=0; i<s;i++){
			
			// Perform a N/s group of selections, with N/s elements
			ArrayList<ArrayList<Integer>> selections = permutationTournamentSelections(N,s,permutations.get(i));
			
			for(int j = 0; j<N/s; j++){
				ArrayList<Karva> tournamentSelected = selectedBinaryStringFromIndexes(elements, selections.get(j));
				Karva winner = selectFittestElement(tournamentSelected);
				winners.add(winner);
			}
		}
		
		return winners;
			
			
	}
	
	// Generate s random permutations of size N-1 
	private ArrayList<Permutation> generatePermutations (int N, int s){
		
		// Generate a random permutation of the population s times
		ArrayList<Permutation> permutations = new ArrayList<Permutation>();
		for(int i = 0; i<s; i++){
			
			// Generate the permutation and add it to the list
			Permutation permutation = Permutation.generateRandom(N);
			permutations.add(permutation);
		}
		return permutations;
	}

	// Given a permutation and the number of tournaments it represents, return an array with each group of selected elements
	private ArrayList<ArrayList<Integer>> permutationTournamentSelections(int N, int s, Permutation permutation){
		
		ArrayList<ArrayList<Integer>> tournamentSelections = new ArrayList<ArrayList<Integer>>();
		//int t = N/s;
		
		// selects a group of elements
		for(int j=0; j<N/s; j++){

			// select the ith group of N/s values from the permutation
			ArrayList<Integer> selected = new ArrayList<Integer>();
			int from = j*(s);
			int to = (j+1)*(s);
			selected.addAll(permutation.getPermutation().subList(from, to));
			tournamentSelections.add(selected);
		}

		return tournamentSelections;
	}

	// Given a list of binaryStrings and a list of indexes with the positions of selected binaryStrings, return a list of the indexed binaryStrings
	private ArrayList<Karva> selectedBinaryStringFromIndexes(ArrayList<Karva> elements, ArrayList<Integer> selected){
		
		//The list wich will store the elements to return
		ArrayList<Karva> tournamentSelected = new ArrayList<Karva>();
		
		//for each index element, select the corresponding binaryString
		for(int i = 0 ; i<selected.size();i++)
			tournamentSelected.add(elements.get(selected.get(i)));
		
		return tournamentSelected;
	}

	// Receives a non-empty list of BinaryString and returns the fittest element
	private Karva selectFittestElement(ArrayList<Karva> elements){
		Iterator<Karva> iterator = elements.iterator();

		// If the list is not empty search for the fittest element
		if(!elements.isEmpty()){
			Karva fittest, current;
			fittest = current = iterator.next();

			// while there is elementes on the list, update the fittest
			while(iterator.hasNext()){
				current = iterator.next();
				if(current.getFitness() > fittest.getFitness())
					fittest = current;
			}

			return fittest;


		}else // if the list is empty return null
			return null;
	}

}

class Tournament{
	
	// Runs tournament on a list of Chromossomes N times, randomly chooses s winner elements each time
	// From these s winner elements choose the fittest as the ultimate winner of one from those N tournaments
	public static ArrayList<Karva> selectN(ArrayList<Karva> elements, int s, int N){
		ArrayList<Karva> selected = new ArrayList<Karva>();
		for(int i=0; i<N; i++)
			selected.add(selectOne(elements,s));
		
		return selected;
	}
	
	// Runs tournament on a list of Chromossomes, randomly chooses s winner elements
	// From these s winner elements choose the fittest as the ultimate winner
	public static Karva selectOne(ArrayList<Karva> elements, int s){
		
		ArrayList<Karva> randomElements = new ArrayList<Karva>();
		
		// Select s random elements
		for(int i = 0; i<s; i++){
			randomElements.add(selectRandomElement(elements));
		}
		
		return selectFittestElement(randomElements);
		
	}
		
	// Receives a list of Chromossomes and returns a random element from the list
	private static Karva selectRandomElement(ArrayList<Karva> elements){
		int index = RandomGenerator.generateInRange_int(1,elements.size());
		return elements.get(index-1);
	}
	
	// Receives a non-empty list of Chromossomes and returns the fittest element
	private static Karva selectFittestElement(ArrayList<Karva> elements){
		Iterator<Karva> iterator = elements.iterator();

		// If the list is not empty search for the fittest element
		if(!elements.isEmpty()){
			Karva fittest, current;
			fittest = current = iterator.next();
			
			// while there is elementes on the list, update the fittest
			while(iterator.hasNext()){
				current = iterator.next();
				if(current.getFitness() > fittest.getFitness())
					fittest = current;
			}
			
			return fittest;

			
		}else // if the list is empty return null
			return null;
	}

}
