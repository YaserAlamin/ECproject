package GEP;

import java.util.ArrayList;

class GEP_Configuration{
	/**1st - N - number of elements (must be a multiple of s)
	 * 2nd - l - lenght of the string representation
	 * 3rd - s - tournament size
	 * 4th - Pm - probability of mutation
	 * 5th - Pc - probability of crossover
	 * 6th - r - fraction of the worst elements from the 
		   old population that is going to be replaced.
		   The actual number of individuals should be round up.
	 * 7th - g - number of generations
			
	 * 8th - SEL_method
	 * 9th - CO_method
	**/
	private int N;
	private int l;
	private int s;
	private double Pm;
	private double Pc;
	private double r;
	private int g;
	private int SEL_method;
	private int CO_method;
	
	public int getN() {
		return N;
	}
	public void setN(int n) {
		N = n;
	}
	public int getL() {
		return l;
	}
	public void setL(int l) {
		this.l = l;
	}
	public int getS() {
		return s;
	}
	public void setS(int s) {
		this.s = s;
	}
	public double getPm() {
		return Pm;
	}
	public void setPm(double pm) {
		Pm = pm;
	}
	public double getPc() {
		return Pc;
	}
	public void setPc(double pc) {
		Pc = pc;
	}
	public double getR() {
		return r;
	}
	public void setR(double r) {
		this.r = r;
	}
	public int getG() {
		return g;
	}
	public void setG(int g) {
		this.g = g;
	}
	public int getSEL_method() {
		return SEL_method;
	}
	public void setSEL_method(int SEL_method) {
		this.SEL_method = SEL_method;
	}
	public String getSEL_method_name(){
		// SEL_method
		/**
		 * 0 -> Roulette Wheel
		 * 1 -> SUS
		 * 2 -> Tournament with s = 2
		 * 3 -> Tournament with s = 4
		 * 4 -> Truncation with 10% cutoff
		 * 5 -> Truncation with 50% cutoff
		 **/
		switch (getSEL_method()){
			case 0: return "Roulette Wheel";
			case 1: return "SUS";
			case 2: return "Tournament with s = 2";
			case 3: return "Tournament with s = 4";
			case 4: return "Truncation with 10% cutoff";
			case 5: return "Truncation with 50% cutoff";
			default: return "not specified";
		}
	}
	public int getCO_method() {
		return CO_method;
	}
	public void setCO_method(int CO_method) {
		this.CO_method = CO_method;
	}
	public String getCO_method_name(){
		// CO_method
		/**
		 * 0 -> Uniform Crossover
		 * 1 -> One-point Crossover
		 * 2 -> Two-point Crossover
		 **/
		switch (getCO_method()){
			case 0: return "Uniform Crossover";
			case 1: return "One-point Crossover";
			case 2: return "Two-point Crossover";
			default: return "not specified";
		}
	}



}

class GeneExpressionAlghorithm{
	
	/**
	 *  Iteratively Simulates one generation of the Gene Expression Programming 
	 *  algorithm operating on the sort problem.
	 *  
	 * 	@param N -  number of elements
	   	@param l -  lenght of the string representation
	   	@param s -  tournament size
		@param pm - probability of mutation
		@param pc - probability of crossover
		@param r -  fraction of the worst elements from the 
		    		old population that is going to be replaced.
					The actual number of individuals should be round up.
		@param M - the amount of random numbers given. Uniformly generated in [0..1)
		@param u - list with random numbers	
	 * @return 
	 * @return 
	*/
	public static GEP_Data geneticONEMAX(int SEL_method, int CO_method, int N, int l, int s, double pm, double pc, double r, int g){
		
		//DecimalFormat df = new DecimalFormat("#0.00");
		
		boolean convergence = false;
		int iterations_to_converge = -1;
		
		//Stores the configuration
		GEP_Configuration config = new GEP_Configuration();
		config.setN(N);
		config.setL(l);
		config.setS(s);
		config.setPm(pm);
		config.setPc(pc);
		config.setPm(pm);
		config.setR(r);
		config.setG(g);
		config.setSEL_method(SEL_method);
		config.setCO_method(CO_method);
				
		long startTime = System.nanoTime();
		/** BEGINNING OF THE ALGHORITHM **/
		
		// INITIAL POPULATION GENERATION	
		// generates the population 
		ArrayList<Karva> population = Karva.generatePopulation(N);
		
		// generates testing Sequences and use them to calculate the fitness for each element
		ArrayList<Sequence> testingSequences = Sequence.generateList(15, 30);
		Karva.populationFitness(population, testingSequences);
		

		// prints the status of the initial population
		double max = BinaryString.fitness_max(populationBS).getFitness();
		double avg;// = BinaryString.fitness_average(populationBS);
		double min;// = BinaryString.fitness_min(populationBS).getFitness();
		double stdDeviation;// = BinaryString.fitness_stdDeviation(populationBS);

		
		//System.out.println("0: " + df.format(max) + " " + df.format(avg) + " " + df.format(min));
		
		for(int i=0; i<g;i++){
			populationBS = geneticONEMAX_iteration(SEL_method,CO_method,populationBS, N, l, s, pm, pc, r);
			
			max = BinaryString.fitness_max(populationBS).getFitness();
			
			if(max == l){
				convergence = true;
				iterations_to_converge = i;
				break;
			}
			//System.out.println(i+1 + ": " + df.format(max) + " " + df.format(avg) + " " + df.format(min));
		}
		
		/** END OF THE ALGHORITHM **/
		
		//Stores the alghorithm iteration data
		GA_Data data = new GA_Data();
		
		//Stores configuration
		data.setConfiguration(config);
		
		//Stores running time
		long estimatedTime = System.nanoTime() - startTime;
		double elapsedTime = (double)estimatedTime/1000000000;
		//System.out.println("Time:" + elapsedTime);
		data.setTime_in_seconds(elapsedTime);
		
		//Store min, max and avg
		max = BinaryString.fitness_max(populationBS).getFitness();
		avg = BinaryString.fitness_average(populationBS);
		min = BinaryString.fitness_min(populationBS).getFitness();
		stdDeviation = BinaryString.fitness_stdDeviation(populationBS);
		
		data.setMin(min);
		data.setMax(max);
		data.setAvg(avg);
		data.setStdDeviation(stdDeviation);
		
		//Store convergence confirmation and the number of iterations necessary (stores -1 if convergence was not achieved)
		data.setConvergence(convergence);
		data.setIterations_to_converge(iterations_to_converge);
		
		return data;
	}
	
}

	private static ArrayList<BinaryString> geneticONEMAX_iteration(int SEL_method, int CO_method, ArrayList<BinaryString> population, int N, int l, int s, double pm, double pc, double r){
		
		//SELECTION	
		/*
		 * 0 -> Roulette Wheel
		 * 1 -> SUS
		 * 2 -> Tournament with s = 2
		 * 3 -> Tournament with s = 4
		 * 4 -> Truncation with 10% cutoff
		 * 5 -> Truncation with 50% cutoff
		 */
		ArrayList<BinaryString> selected;
		
		switch (SEL_method) {
        case 0:	selected = RouletteWheel.selectN(population, N);
        	//System.out.println("using: Roulette Wheel");
    		break;
        case 1: selected = SUS.selectN(population, N);
        	//System.out.println("using: SUS");
        	break;
        case 2: selected = Tournament.selectN(population, 2, N);
        	//System.out.println("using: Tournament with s = 2");
        	break;
        case 3: selected = Tournament.selectN(population, 4, N);
        	//System.out.println("using: Tournament with s = 4");
    		break;
        case 4: selected = Truncation.selectNfromFittestPercentage(population, N, 10);
        	//System.out.println("using: Truncation with 10% cutoff");
    		break;
        case 5: selected = Truncation.selectNfromFittestPercentage(population, N, 50);
        	//System.out.println("using: Truncation with 50% cutoff");
    		break;
        default: selected = Truncation.selectNfromFittestPercentage(population, N, 50);
        	//System.out.println("using: Truncation with 50% cutoff");
    		break;	
		}
		
		//CROSS-OVER
		// CO_method
		/*
		 * 0 -> Uniform Crossover
		 * 1 -> One-point Crossover
		 * 2 -> Two-point Crossover
		 */
		//apply One Point Crossover to every two elements of the selected list
		ArrayList<String> population_new = new ArrayList<String>();
		
		// for every 2 elements of the selected list, according to crossover probability
		// cross over and add to the new population
		for(int i=0; i<selected.size()/2; i++){
			if(RandomGenerator.generate() < pc){
				ArrayList<String> offspring = new ArrayList<String>();
				switch (CO_method) {
		        case 0:	offspring = CrossOver.uniformCrossOver(selected.get(i*2).getBinaryString(), selected.get(i*2+1).getBinaryString());
		    		break;
		        case 1: offspring = CrossOver.onePointCrossOver(selected.get(i*2).getBinaryString(), selected.get(i*2+1).getBinaryString());
		        	break;
		        case 2: offspring = CrossOver.twoPointCrossOver(selected.get(i*2).getBinaryString(), selected.get(i*2+1).getBinaryString());
		        	break;
		        default: offspring = CrossOver.uniformCrossOver(selected.get(i*2).getBinaryString(), selected.get(i*2+1).getBinaryString());
		    		break;	
				}
				
				population_new.addAll(offspring);
			}
			else{
				population_new.add(selected.get(i*2).getBinaryString());
				population_new.add(selected.get(i*2+1).getBinaryString());
			}
		}
		
		//MUTATION
		//apply mutation to every new element
		for(int i=0;i<population_new.size();i++)				
			population_new.set(i, Mutation.binaryStringMutation(population_new.get(i), pm));
		
		// REPLACE		
		ArrayList<BinaryString> populationBS_new = BinaryString.onemaxPopulationFitness(population_new);
		ArrayList<BinaryString> populationBS_final = Replace.replace(population, populationBS_new, r);
		
				
		return populationBS_final;
		
	}
	
	
	private static ArrayList<Karva>
}









class SimpleGeneticAlghorithm{

/**
 * Simulate one generation of a genetic algorithm operating on the onemax problem.
 *  
 * 	@param N -  number of elements
   	@param l -  lenght of the string representation
   	@param s -  tournament size
	@param pm - probability of mutation
	@param pc - probability of crossover
	@param r -  fraction of the worst elements from the 
	    		old population that is going to be replaced.
				The actual number of individuals should be round up.
	@param M - the amount of random numbers given. Uniformly generated in [0..1)
	@param u - list with random numbers	
*/

public void oneGenerationONEMAX(int N, int l, int s, double pm, double pc, double r){
	
	DecimalFormat df = new DecimalFormat("#0.00");

	
	// INITIAL POPULATION GENERATION	
	// generates the population and calculates the fitness for each element
	ArrayList<String> population = BinaryString.generatePopulation(N, l);
	ArrayList<BinaryString> populationBS = BinaryString.onemaxPopulationFitness(population);		
	
	// prints the status of the initial population
	double max = BinaryString.fitness_max(populationBS).getFitness();
	double avg = BinaryString.fitness_average(populationBS);
	double min = BinaryString.fitness_min(populationBS).getFitness();
	
	System.out.println("0: " + df.format(max) + " " + df.format(avg) + " " + df.format(min));
	
	//SELECTION		
	TournamentWithoutReplacement twr = new TournamentWithoutReplacement();
	ArrayList<BinaryString> selected =  twr.runTournaments(N, s, populationBS);
	
	//CROSS-OVER
	//apply One Point Crossover to every two elements of the selected list
	ArrayList<String> population_new = new ArrayList<String>();
	
	// for every 2 elements of the selected list, according to crossover probability
	// cross over and add to the new population
	for(int i=0; i<selected.size()/2; i++)
		if(RandomGenerator.generate() < pc)
			population_new.addAll(
					CrossOver.onePointCrossOver(
							selected.get(i*2).getBinaryString(),
							selected.get(i*2+1).getBinaryString()));
		else{
			population_new.add(selected.get(i*2).getBinaryString());
			population_new.add(selected.get(i*2+1).getBinaryString());
		}

	//MUTATION
	//apply mutation to every new element
	for(int i=0;i<population_new.size();i++){	
		population_new.set(i, Mutation.binaryStringMutation(population_new.get(i), pm));
	}		
	
	
	// REPLACE	
	ArrayList<BinaryString> populationBS_new = BinaryString.onemaxPopulationFitness(population_new);
	ArrayList<BinaryString> populationBS_final = Replace.replace(populationBS, populationBS_new, r);
			
	// prints the status of the new population
	max = BinaryString.fitness_max(populationBS_final).getFitness();
	avg = BinaryString.fitness_average(populationBS_final);
	min = BinaryString.fitness_min(populationBS_final).getFitness();

	System.out.println("1: " + df.format(max) + " " + df.format(avg) + " " + df.format(min));
	
}

/**
 *  Iteratively Simulates one generation of a genetic algorithm operating on the onemax problem.
 *  
 * 	@param N -  number of elements
   	@param l -  lenght of the string representation
   	@param s -  tournament size
	@param pm - probability of mutation
	@param pc - probability of crossover
	@param r -  fraction of the worst elements from the 
	    		old population that is going to be replaced.
				The actual number of individuals should be round up.
	@param M - the amount of random numbers given. Uniformly generated in [0..1)
	@param u - list with random numbers	
 * @return 
*/
public static void runGEP(int N, int l, int s, double pm, double pc, double r, double g){
	
	// INITIAL POPULATION GENERATION	
	// generates the population and calculates the fitness for each element
	ArrayList<String> population = BinaryString.generatePopulation(N, l);
	ArrayList<BinaryString> populationBS = BinaryString.onemaxPopulationFitness(population);

	// prints the status of the initial population
	double max = BinaryString.fitness_max(populationBS).getFitness();
	double avg = BinaryString.fitness_average(populationBS);
	double min = BinaryString.fitness_min(populationBS).getFitness();

	DecimalFormat df = new DecimalFormat("#0.00");
	System.out.println("0: " + df.format(max) + " " + df.format(avg) + " " + df.format(min));
	
	for(int i=0; i<g;i++){
		populationBS = geneticONEMAX_iteration(populationBS, N, l, s, pm, pc, r);
		
		// prints the status of the initial population
		max = BinaryString.fitness_max(populationBS).getFitness();
		avg = BinaryString.fitness_average(populationBS);
		min = BinaryString.fitness_min(populationBS).getFitness();

		System.out.println(i+1 + ": " + df.format(max) + " " + df.format(avg) + " " + df.format(min));
	}
}

private static ArrayList<BinaryString> geneticONEMAX_iteration(ArrayList<BinaryString> population, int N, int l, int s, double pm, double pc, double r){
	
	//SELECTION	
	TournamentWithoutReplacement twr = new TournamentWithoutReplacement();
	ArrayList<BinaryString> selected =  twr.runTournaments(N, s, population);
	
	//CROSS-OVER
	//apply One Point Crossover to every two elements of the selected list
	ArrayList<String> population_new = new ArrayList<String>();
	
	// for every 2 elements of the selected list, according to crossover probability
	// cross over and add to the new population
	for(int i=0; i<selected.size()/2; i++)
		if(RandomGenerator.generate() < pc)
			population_new.addAll(
					CrossOver.onePointCrossOver(
							selected.get(i*2).getBinaryString(),
							selected.get(i*2+1).getBinaryString()));
		else{
			population_new.add(selected.get(i*2).getBinaryString());
			population_new.add(selected.get(i*2+1).getBinaryString());
		}
	
	//MUTATION
	//apply mutation to every new element
	for(int i=0;i<population_new.size();i++)				
		population_new.set(i, Mutation.binaryStringMutation(population_new.get(i), pm));
	
	// REPLACE		
	ArrayList<BinaryString> populationBS_new = BinaryString.onemaxPopulationFitness(population_new);
	ArrayList<BinaryString> populationBS_final = Replace.replace(population, populationBS_new, r);
	
			
	return populationBS_final;
}

}