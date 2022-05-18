package GEP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


class Replication{
	
	public static ArrayList<Karva> replace (ArrayList<Karva> population_old, ArrayList<Karva>population_new, double replacement_factor){
		
		ArrayList<Karva> population_final = new ArrayList<Karva>();
		
		int keepers = population_old.size()-(int)(replacement_factor*population_old.size()); 
		
		Collections.sort(population_old);
		//Collections.sort(population_new);
		
		population_final.addAll(population_old.subList(0, keepers));
		population_final.addAll(population_new.subList(0, population_old.size()-keepers));	
			
		return population_final;
	}

}


class Mutation{
	
	// Computes the mutation of a binaryString bit by bit given the probability of mutation for all the bits and a random value for each bit 
	public static String mutate(Karva kExpression, double pMut){
						
		String mutated = "";
		
		Random dice = new Random();
		int index;
		
		// for all the bits in the string, check if the corresponding random value is less than the probability of mutation, if so mutate the bit.
		for(int i=0; i<Karva.getHeadSize();i++){
			if(RandomGenerator.generate()<pMut){
				index = dice.nextInt(Karva.getFunctionSet().size()+Karva.getTerminalSet().size());
				if(index<Karva.getFunctionSet().size()){
					mutated += kExpression.getRepresentation().substring(0, i-1);
					mutated += Karva.getFunctionSet().get(index).getSimbol();
					mutated += kExpression.getRepresentation().substring(i+1, kExpression.getRepresentation().length());
				}else{
					mutated += kExpression.getRepresentation().substring(0, i-1);
					mutated += Karva.getTerminalSet().get(index-Karva.getFunctionSet().size()).getSimbol();
					mutated += kExpression.getRepresentation().substring(i+1, kExpression.getRepresentation().length());
				}
			}
		}
						
		return mutated;
		
	}
}
