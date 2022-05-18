package project;

/* change the chromosome to program string  
 * test the funtction \/
 * {
    String program;
	program = rep2pro.makePro(expression1.getRepresentation());
	System.out.println(program);
	} 
	*/

public class rep2pro {
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
