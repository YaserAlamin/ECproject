package GEP;

import java.util.ArrayList;

class CrossOver{
	// Given two String Expressions of size l and a random number, returns the results of onePointCrossover for those String Expressions (which is another pair of String Expressions)
	public static ArrayList<String> onePointCrossOver (String stringX, String stringY){
		
		// Generates pattern r
		String r = onePointPatternGenerator(stringX.length());
		
		return childs(stringX,stringY,r);
		
	}
	
	// Given two Strings of size l and a random number, returns the results of twoPointCrossover for those Strings (which is another pair of Strings)
	public static ArrayList<String> twoPointCrossOver (String stringX, String stringY){

		// Generates pattern r
		String r = twoPointPatternGenerator(stringX.length());
		
		//System.out.println();
		//System.out.println(r);
		return childs(stringX,stringY,r);

	}

	// Return a random onePoint pattern with the given length
	private static String twoPointPatternGenerator(int l) {
		
		// calculates the pattern splitting point
		int point_a = RandomGenerator.generateInRange_int(0,l-1);
		int point_b = RandomGenerator.generateInRange_int(0,l-1);
		while(point_b == point_a)
			point_b = RandomGenerator.generateInRange_int(0,l-1);
		
		
		// bits before the smallest and after the largest of the two splitting points get a 1,
		// bits between the splitting points get a 0
		String pattern = "";
		for(int i=0; i<=Math.min(point_a,point_b);i++ )
			pattern += "1";
		for(int i=Math.min(point_a,point_b); i<Math.max(point_a,point_b);i++ )
			pattern += "0";
		for(int i=Math.max(point_a,point_b); i<l-1;i++ )
			pattern += "1";
		
		// return the pattern
		return pattern;
	}
	
	// Return a random onePoint pattern with the given length
	private static String onePointPatternGenerator(int l) {
		
		// calculates the pattern splitting point
		int onePoint = RandomGenerator.generateInRange_int(0,l-1);
		
		// bits before the splitting point get a 1, bits at the splitting point and after get a 0 
		String pattern = "";
		for(int i=0; i<=onePoint;i++ )
			pattern += "1";
		for(int i=onePoint; i<l-1;i++ )
			pattern += "0";
		
		// return the pattern
		return pattern;
	}
	
	// Returns 
	private static ArrayList<String> childs (String stringX, String stringY, String r){
		// Initialize childs
		String stringX_ ="";
		String stringY_ = "";
		
		// In positions where pattern r has the value 0, keep bits the same as corresponding parent
		// 						where r has the value 1, swap the bit with the other parent
		for(int i=0; i<r.length(); i++)
			if(r.charAt(i)=='1'){
				//swap bits
				stringX_ += stringY.charAt(i);
				stringY_ += stringX.charAt(i);
			}else{
				//maintain bits
				stringX_ += stringX.charAt(i);
				stringY_ += stringY.charAt(i);
			}
		
		// Store and return the resulting children from the crossover
		ArrayList<String> childs = new ArrayList<String>();
		childs.add(stringY_);
		childs.add(stringX_);
		
		return childs;
	}
}
