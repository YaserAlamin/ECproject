package GEP;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.*;

public class Main {

	public static void main(String[] args) {
		
	Function dobl = new Function("dobl( , , )", 3, 'd');
	Function swap = new Function("swap( , )", 2, 's');
	Function e1p = new Function("e1p( )", 1, 'E');
	Function e1m = new Function("e1m( )", 1, 'e');
	Function wismaller = new Function("wismaller( , )", 2, 'w');
	Function wibigger = new Function("wibigger( , )", 2, 'W');
	Function em = new Function("em( )", 1, 'x');
	
	ArrayList<Function> functions = new ArrayList<Function>();
	functions.add(dobl);
	functions.add(swap);
	functions.add(e1p);
	functions.add(e1m);
	functions.add(wismaller);
	functions.add(wibigger);
	functions.add(em);
	

	Terminal index = new Terminal("index", 'i');
	Terminal len = new Terminal("len", 'l');
	
	ArrayList<Terminal> terminals = new ArrayList<Terminal>();
	terminals.add(index);
	terminals.add(len);
	
	final int headSize = 10; 	
	final int populationSize = 2000;
	
	Karva.languageSetUp(functions, terminals, headSize);
	
	//Karva expression1 = Karva.generate();
	
	ArrayList<Karva> population = Karva.generatePopulation(populationSize);
	
	//ArrayList<Karva> selected = Tournament.selectN(population, 4, 4);
	
	//Karva.printListOnePerLine(population);
	//System.out.println();
	//Karva.printListOnePerLine(selected);
	
	//System.out.println(expression1.getRepresentation());

	//System.out.println(population.get(0).writeProgram());
	
	//Generate a list of  
	ArrayList<Sequence> testingSequences = Sequence.generateList(15, 30); //15 Sequences, Length from 1 to 30
	
	for(int i=0;i<populationSize;i++){
		
		String expression = rep2pro.makePro(population.get(i).getRepresentation());
		System.out.println(expression);
		System.out.println(SortProgram.runSort_Evaluate(expression, testingSequences));
		System.out.println();
		
	}
	

}

class Experiments{
	public void run(){
		Scanner sc = new Scanner(System.in);
		
		// Read input constants
		int N = 320;					// N - number of elements (must be a multiple of s)
		int l = 100;					// l - lenght of the string representation
		int s = 2;						// s - tournament size
		double pm = (double)0.2;		// Pm - probability of mutation
		double pc = 0.5;				// Pc - probability of crossover
		double r = 0.5;					// r - fraction of the worst elements from the 
										// old population that is going to be replaced.
										// The actual number of individuals should be round up.
		int g = 100;					// g - number of generations
		int experiments = 100;	
		
		ArrayList<Integer> Ns = new ArrayList<Integer>();
		Ns.add(10);
		Ns.add(40);
		Ns.add(320);
		Iterator<Integer> iterator_Ns = Ns.iterator();
		
		ArrayList<Double> rs = new ArrayList<Double>();
		rs.add(0.5);
		rs.add(1.0);
		Iterator<Double> iterator_rs = rs.iterator();
		
		ArrayList<Double> pms = new ArrayList<Double>();
		pms.add(0.0);
		pms.add(1.0/l);
		pms.add(10.0/l);
		Iterator<Double> iterator_pms = pms.iterator();
		
		ArrayList<Double> pcs = new ArrayList<Double>();
		pcs.add(0.0);
		pcs.add(0.5);
		pcs.add(1.0);
		Iterator<Double> iterator_pcs = pcs.iterator();
		
		/* SEL_method
		 * 0 -> Roulette Wheel
		 * 1 -> SUS
		 * 2 -> Tournament with s = 2
		 * 3 -> Tournament with s = 4
		 * 4 -> Truncation with 10% cutoff
		 * 5 -> Truncation with 50% cutoff
		 */
		
		/* CO_method
		 * 0 -> Uniform Crossover
		 * 1 -> One-point Crossover
		 * 2 -> Two-point Crossover
		 */
		DecimalFormat df = new DecimalFormat("#0.000");
		
		int total_iterations = Ns.size()*rs.size()*pms.size()*pcs.size()*6*3*experiments;
		int total_iterations_so_far = 0;
		
		try {

			// BufferedWriter to write on the constraint file
			File file =  new File("D:\\Universidade Mestrado\\Evolutionary Computing\\Lab4_results.txt");
			FileOutputStream fileOS = new FileOutputStream(file);
			OutputStreamWriter oSW = new OutputStreamWriter(fileOS);
			BufferedWriter out = new BufferedWriter(oSW);

			out.append("Elements with length of " + l + " on the ONEMAX problem." + System.getProperty("line.separator") +
					g + " generations per experience." + System.getProperty("line.separator") + 
					experiments + " experiments with 3 pre-runs." + System.getProperty("line.separator") + 
					"Total number of experiments: " + total_iterations + System.getProperty("line.separator")
					);
			
			out.append("Selection_Method " +
					"CrossOver_method " +
					"N " +
					//multirun_Data.getConfiguration().getL() + " " +
					//multirun_Data.getConfiguration().getS() + " " +
					"Pm " +
					"Pc " +
					"R " +

					"Avg_max " +
					"Avg_avg " +
					"Avg_min " +
					"Avg_stdDeviation " +
					"Max_max " +
					"Min_min " + 
			
					"Total_time_in_seconds " +
					"Avg_time_in_seconds " +
					"Min_time_in_seconds " +
					"Max_time_in_seconds " +
					"StdDeviation_time_in_seconds " +
			
					"Total_number_of_convergence " +
					"Average_number_of_convergence " +
					"Average_convergence_percentage " +
					"Min_iterations_to_converge " +
					"Avg_iterations_to_converge " +
					"Max_iterations_to_converge" + 	
					System.getProperty("line.separator")
					);

			
			while(iterator_Ns.hasNext()){
				N = iterator_Ns.next();
				while(iterator_rs.hasNext()){
					r = iterator_rs.next();
					while(iterator_pms.hasNext()){
						pm = iterator_pms.next();
						while(iterator_pcs.hasNext()){
							pc = iterator_pcs.next();
							for(int sE=0;sE<=5;sE++)
								for(int cO=0;cO<=2;cO++){
									//show how many iterations are 
									int iterations = 0;

									ArrayList<GA_Data> multirun_list = new ArrayList<GA_Data>();
									for(int i=0; i<experiments+2;i++){
										if(i<2)
											System.out.print("pre-caching..");
										else
											System.out.print(++total_iterations_so_far + " out of " + total_iterations + ". Locally " + ++iterations + "...");
										GA_Data data = new GA_Data();
										//data = CustomGeneticAlghorithm.geneticONEMAX(sE, cO, N, l, s, pm, pc, r, g);
										if(i<2)
											continue;
										//data.print(false);
										multirun_list.add(data);

										System.out.println("done");
									}

									GA_multirun_Data multirun_Data = new GA_multirun_Data();
									multirun_Data.calculate_data(multirun_list);
									//multirun_Data.print(true);



									out.append(
											"\"" + multirun_Data.getConfiguration().getSEL_method_name() + "\" " +
													"\"" + multirun_Data.getConfiguration().getCO_method_name() + "\" " +
													multirun_Data.getConfiguration().getN() + " " +
													//multirun_Data.getConfiguration().getL() + " " +
													//multirun_Data.getConfiguration().getS() + " " +
													multirun_Data.getConfiguration().getPm() + " " +
													multirun_Data.getConfiguration().getPc() + " " +
													multirun_Data.getConfiguration().getR() + " " +

													df.format(multirun_Data.getAvg_max()) + " " +
													df.format(multirun_Data.getAvg_avg()) + " " +
													df.format(multirun_Data.getAvg_min()) + " " +
													df.format(multirun_Data.getAvg_stdDeviation()) + " " +
													df.format(multirun_Data.getMax_max()) + " " +
													df.format(multirun_Data.getMin_min()) + " " + 

													df.format(multirun_Data.getTotal_time_in_seconds()) + " " +
													df.format(multirun_Data.getAvg_time_in_seconds()) + " " +
													df.format(multirun_Data.getMin_time_in_seconds()) + " " +
													df.format(multirun_Data.getMax_time_in_seconds()) + " " +
													df.format(multirun_Data.getStdDeviation_time_in_seconds()) + " " +

													df.format(multirun_Data.getTotal_number_of_convergence()) + " " +
													df.format(multirun_Data.getAverage_number_of_convergence()) + " " +
													df.format(multirun_Data.getAverage_convergence_percentage()) + " " +
													df.format(multirun_Data.getMin_iterations_to_converge()) + " " +
													df.format(multirun_Data.getAvg_iterations_to_converge()) + " " +
													df.format(multirun_Data.getMax_iterations_to_converge())		
											);
									out.append(System.getProperty("line.separator"));
									out.flush();
								}	
							}
						iterator_pcs = pcs.iterator();
						}
					iterator_pms = pms.iterator();
					}
				iterator_rs = rs.iterator();
				}

	
			out.flush();
			out.close();				

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	}
}
class GA_multirun_Data{
	//experiment identification
	private GA_Configuration configuration;
	private int number_iterations;
	
	//fitness related data
	private double avg_max;
	private double avg_avg;
	private double avg_min;
	private double avg_stdDeviation;
	
	private double max_max;
	private double min_min;
	
	//alghorithm performance and execution time
	private double total_time_in_seconds;
	private double avg_time_in_seconds;
	private double min_time_in_seconds;
	private double max_time_in_seconds;
	private double stdDeviation_time_in_seconds;
	
	//alghorithm convergence
	private double total_number_of_convergence;
	private double average_number_of_convergence;
	private double average_convergence_percentage;
	
	private double min_iterations_to_converge;
	private double avg_iterations_to_converge;
	private double max_iterations_to_converge;
	
	public void calculate_data(ArrayList<GA_Data> data){

		this.configuration = data.get(0).getConfiguration();
		this.number_iterations = data.size();
		
		double MAXNUM = 9999999;
		
		//fitness statistics
		double avg_max=0;
		double avg_avg=0;
		double avg_min=0;
		double avg_stdDeviation=0;

		double max_max=0;
		double min_min=MAXNUM;

		//alghorithm performance and execution time
		double total_time_in_seconds=0;
		double avg_time_in_seconds=0;
		double min_time_in_seconds=MAXNUM;
		double max_time_in_seconds=0;
		double stdDeviation_time_in_seconds;
		ArrayList<Double> stdDeviation_time_in_seconds_aux = new ArrayList<Double>();

		//alghorithm convergence
		double total_number_of_convergence=0;
		double average_number_of_convergence=0;
		double average_convergence_percentage;

		double min_iterations_to_converge=MAXNUM;
		double avg_iterations_to_converge=0;
		double max_iterations_to_converge=0;


		Iterator<GA_Data> iterator = data.iterator();
		GA_Data elem;
		while(iterator.hasNext()){
			
			elem = iterator.next();
			
			//fitness statistics		
			avg_max += elem.getMax();
			avg_avg += elem.getAvg();
			avg_min += elem.getMin();
			avg_stdDeviation += elem.getStdDeviation();
			
			if(elem.getMax() > max_max)
				max_max = elem.getMax();
			if(elem.getMin() < min_min)
				min_min = elem.getMin();
			
			//alghorithm performance and execution time
			total_time_in_seconds += elem.getTime_in_seconds();
			
			if(elem.getTime_in_seconds() < min_time_in_seconds)
				min_time_in_seconds = elem.getTime_in_seconds();
			if(elem.getTime_in_seconds() > max_time_in_seconds)
				max_time_in_seconds = elem.getTime_in_seconds();
			
			stdDeviation_time_in_seconds_aux.add(elem.getTime_in_seconds());
			
			//alghorithm convergence
			if(elem.isConvergence()){
				total_number_of_convergence++;
				if(elem.getIterations_to_converge() < min_iterations_to_converge)
					min_iterations_to_converge = elem.getIterations_to_converge();
				if(elem.getIterations_to_converge() > max_iterations_to_converge)
					max_iterations_to_converge = elem.getIterations_to_converge();
				avg_iterations_to_converge += elem.getIterations_to_converge();
			}	
		}
		
		//fitness statistics
		avg_max /= data.size();
		avg_avg /= data.size();
		avg_min /= data.size();
		avg_stdDeviation /= data.size();
		
		//alghorithm performance and execution time
		avg_time_in_seconds = total_time_in_seconds / data.size();
		stdDeviation_time_in_seconds = Statistics.standardDeviation(stdDeviation_time_in_seconds_aux);
		
		//alghorithm convergence
		average_number_of_convergence = total_number_of_convergence/data.size();
		average_convergence_percentage = 100*total_number_of_convergence/data.size();
		avg_iterations_to_converge /= total_number_of_convergence;
		
		// FINAL ATTRIBUTION
		this.avg_max=avg_max;
		this.avg_avg=avg_avg;
		this.avg_min=avg_min;
		this.avg_stdDeviation=avg_stdDeviation;

		this.max_max=max_max;
		this.min_min=min_min;

		//alghorithm performance and execution time
		this.total_time_in_seconds=total_time_in_seconds;
		this.avg_time_in_seconds=avg_time_in_seconds;
		this.min_time_in_seconds=min_time_in_seconds;
		this.max_time_in_seconds=max_time_in_seconds;
		this.stdDeviation_time_in_seconds=stdDeviation_time_in_seconds;

		//alghorithm convergence
		this.total_number_of_convergence=total_number_of_convergence;
		this.average_number_of_convergence=average_number_of_convergence;
		this.average_convergence_percentage=average_convergence_percentage;

		this.min_iterations_to_converge=min_iterations_to_converge;
		this.avg_iterations_to_converge=avg_iterations_to_converge;
		this.max_iterations_to_converge=max_iterations_to_converge;
	}
	
	public void print(boolean showConfiguration){	
		
		if(showConfiguration){
			System.out.println();
			System.out.println("N  (population size): " + configuration.getN());
			System.out.println("l  (element length): " + configuration.getL());
			//System.out.print("s (tournament selection): " + configuration.getS());
			System.out.println("Pm (mutation probability): " + configuration.getPm());
			System.out.println("Pc (crossover probability): " + configuration.getPc());
			System.out.println("r  (replacement factor: " + configuration.getR());
			System.out.println("Selection method: " + configuration.getSEL_method_name());
			System.out.println("CrossOver method: " + configuration.getCO_method_name());
			System.out.println();
			
		}
		
		System.out.println("EXPERIMENT IDENTIFICATION");
		System.out.println("- configuration: ");
		System.out.println("- # iterations: " + number_iterations);
		System.out.println();
		
		System.out.println("FITNESS RELATED DATA");
		System.out.println("- avg_Max: " + getAvg_max());
		System.out.println("- avg_Avg: " + getAvg_avg());
		System.out.println("- avg_Min: " + getAvg_min());
		System.out.println("- avg_stdDeviation: " + getAvg_stdDeviation());
		System.out.println();
		System.out.println("- max_Max: " + getMax_max());
		System.out.println("- min_Min: " + getMin_min());
		System.out.println();
		
		System.out.println("ALGORITHM PERFORMANCE AND EXECUTION TIME");
		System.out.println("- total_time_in_seconds: " + this.getTotal_time_in_seconds());
		System.out.println("- avg_time_in_seconds: " + getAvg_time_in_seconds());
		System.out.println("- min_time_in_seconds: " + getMin_time_in_seconds());
		System.out.println("- max_time_in_seconds: " + getMax_time_in_seconds());
		System.out.println("- stdDeviation_time_in_seconds: " + getStdDeviation_time_in_seconds());
		System.out.println();
		
		System.out.println("ALGORITHM CONVERGENCE ");
		System.out.println("- total_number_of_convergence: " + getTotal_number_of_convergence());
		System.out.println("- average_number_of_convergence: " + getAverage_number_of_convergence());
		System.out.println("- average_convergence_percentage: " + getAverage_convergence_percentage());
		
		System.out.println("- min_iterations_to_converge: " + getMin_iterations_to_converge());
		System.out.println("- avg_iterations_to_converge: " + getAvg_iterations_to_converge());
		System.out.println("- max_iterations_to_converge: " + getMax_iterations_to_converge());
		
	}
	public GA_Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(GA_Configuration configuration) {
		this.configuration = configuration;
	}
	public int getNumber_iterations() {
		return number_iterations;
	}
	public void setNumber_iterations(int number_iterations) {
		this.number_iterations = number_iterations;
	}
	public double getAvg_max() {
		return avg_max;
	}
	public void setAvg_max(double avg_max) {
		this.avg_max = avg_max;
	}
	public double getAvg_avg() {
		return avg_avg;
	}
	public void setAvg_avg(double avg_avg) {
		this.avg_avg = avg_avg;
	}
	public double getAvg_min() {
		return avg_min;
	}
	public void setAvg_min(double avg_min) {
		this.avg_min = avg_min;
	}
	public double getAvg_stdDeviation() {
		return avg_stdDeviation;
	}
	public void setAvg_stdDeviation(double avg_stdDeviation) {
		this.avg_stdDeviation = avg_stdDeviation;
	}
	public double getMax_max() {
		return max_max;
	}
	public void setMax_max(double max_max) {
		this.max_max = max_max;
	}
	public double getMin_min() {
		return min_min;
	}
	public void setMin_min(double min_min) {
		this.min_min = min_min;
	}
	public double getTotal_time_in_seconds() {
		return total_time_in_seconds;
	}
	public void setTotal_time_in_seconds(double total_time_in_seconds) {
		this.total_time_in_seconds = total_time_in_seconds;
	}
	public double getAvg_time_in_seconds() {
		return avg_time_in_seconds;
	}
	public void setAvg_time_in_seconds(double avg_time_in_seconds) {
		this.avg_time_in_seconds = avg_time_in_seconds;
	}
	public double getMin_time_in_seconds() {
		return min_time_in_seconds;
	}
	public void setMin_time_in_seconds(double min_time_in_seconds) {
		this.min_time_in_seconds = min_time_in_seconds;
	}
	public double getMax_time_in_seconds() {
		return max_time_in_seconds;
	}
	public void setMax_time_in_seconds(double max_time_in_seconds) {
		this.max_time_in_seconds = max_time_in_seconds;
	}
	public double getStdDeviation_time_in_seconds() {
		return stdDeviation_time_in_seconds;
	}
	public void setStdDeviation_time_in_seconds(double stdDeviation_time_in_seconds) {
		this.stdDeviation_time_in_seconds = stdDeviation_time_in_seconds;
	}
	public double getTotal_number_of_convergence() {
		return total_number_of_convergence;
	}
	public void setTotal_number_of_convergence(double total_number_of_convergence) {
		this.total_number_of_convergence = total_number_of_convergence;
	}
	public double getAverage_number_of_convergence() {
		return average_number_of_convergence;
	}
	public void setAverage_number_of_convergence(
			double average_number_of_convergence) {
		this.average_number_of_convergence = average_number_of_convergence;
	}
	public double getAverage_convergence_percentage() {
		return average_convergence_percentage;
	}
	public void setAverage_convergence_percentage(
			double average_convergence_percentage) {
		this.average_convergence_percentage = average_convergence_percentage;
	}
	public double getMin_iterations_to_converge() {
		return min_iterations_to_converge;
	}
	public void setMin_iterations_to_converge(double min_iterations_to_converge) {
		this.min_iterations_to_converge = min_iterations_to_converge;
	}
	public double getAvg_iterations_to_converge() {
		return avg_iterations_to_converge;
	}
	public void setAvg_iterations_to_converge(double avg_iterations_to_converge) {
		this.avg_iterations_to_converge = avg_iterations_to_converge;
	}
	public double getMax_iterations_to_converge() {
		return max_iterations_to_converge;
	}
	public void setMax_iterations_to_converge(double max_iterations_to_converge) {
		this.max_iterations_to_converge = max_iterations_to_converge;
	}
	
}
class GA_Data{
	//Configuration used, each element represents one initialization parameter:
	private GA_Configuration configuration;
	
	//min, max and avg fitness of the population
	private double max;
	private double avg;
	private double min;
	private double stdDeviation;
	
	//time executing the iterations until convergeance or until iteration limit
	private double time_in_seconds;
	
	//Did the alghorithm converged before reaching iteration limit?
	private boolean convergence;
	
	//If the alghorithm converged, how many iterations did it took?
	private int iterations_to_converge;

	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public double getAvg() {
		return avg;
	}
	public void setAvg(double avg) {
		this.avg = avg;
	}
	public double getTime_in_seconds() {
		return time_in_seconds;
	}
	public void setTime_in_seconds(double time_in_seconds) {
		this.time_in_seconds = time_in_seconds;
	}
	public boolean isConvergence() {
		return convergence;
	}
	public void setConvergence(boolean convergence) {
		this.convergence = convergence;
	}
	public int getIterations_to_converge() {
		return iterations_to_converge;
	}
	public void setIterations_to_converge(int iterations_to_converge) {
		this.iterations_to_converge = iterations_to_converge;
	}
	public GA_Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(GA_Configuration configuration) {
		this.configuration = configuration;
	}
	public double getStdDeviation() {
		return stdDeviation;
	}
	public void setStdDeviation(double stdDeviation) {
		this.stdDeviation = stdDeviation;
	}
	
	public void print(boolean showConfiguration){	
		System.out.println("Max: " + getMax());
		System.out.println("Avg: " + getAvg());
		System.out.println("Min: " + getMin());
		System.out.println("StdDeviation: " + getStdDeviation());
		System.out.println();
		System.out.println("Time: " + getTime_in_seconds());
		System.out.println("Convergence: " + (isConvergence()==true ? "Yes!" : "Nop... stopped at iteration: " + getConfiguration().getG()));
		if(isConvergence())
			System.out.println("Iterations to Converge:" + getIterations_to_converge());
	
		if(showConfiguration){
			System.out.println();
			System.out.println("N  (population size): " + configuration.getN());
			System.out.println("l  (element length): " + configuration.getL());
			//System.out.print("s (tournament selection): " + configuration.getS());
			System.out.println("Pm (mutation probability): " + configuration.getPm());
			System.out.println("Pc (crossover probability): " + configuration.getPc());
			System.out.println("r  (replacement factor: " + configuration.getR());
			System.out.println("Selection method: " + configuration.getSEL_method_name());
			System.out.println("CrossOver method: " + configuration.getCO_method_name());
			
		}
	}
	
}

