import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A universal finite state automaton is a string-consuming mechanism that allows
 * for easy finite state automata (fsa) design and modification. It is a general
 * program that can consume multiple fsa designs and test strings for each fsa
 * machine. 
 * 
 * I have only tested this program using deterministic fsa machines. 
 * I wrote and compiled this program in Eclipse. The source code is packaged in
 * 'proj1'. The input file must be in the parent project folder in order for it 
 * to run. 
 * 
 * @author Khamille Sarmiento 2014
 *
 */
public class Main {

	public static void main(String[] args) throws IOException {
		ArrayList<String> temptrans;
		BufferedReader in = new BufferedReader(new FileReader("input.txt"));
		int counter = 1;
		while(in.ready()) {
			
			String temp = in.readLine();
			if(temp.compareTo(".") != 0) {
			
				// nfsa constructor
				nfsa machine = new nfsa(Integer.parseInt(temp));
				System.out.println("Finite State Automaton #" + counter + ". \n(1) number of states: " + machine.numStates);
				
				// Read in final states
				machine.setFinal(in.readLine());
				System.out.print("(2) final states: ");
				machine.printFinalStates();
				
				// Read in valid chars
				machine.setAlpha(in.readLine());
				System.out.print("(3) alphabet: ");
				machine.printAlpha();
				
				// Read in transitions 
				temptrans = new ArrayList<String>();
				temp = in.readLine();
				while(temp.charAt(0) == '(') {
					temptrans.add(temp.substring(1,temp.length()-1));
					temp = in.readLine();
				}	// when this loop exits, temp contains 1st string to test.
				machine.setTrans(temptrans);
				temptrans.clear();
				System.out.println("(4) transitions: ");
				machine.printTransitions();
				System.out.println();
				
				System.out.print("The equivalent DFSA by subset construction:\n1) number of states: ");
				machine.getDfsaNumStates();
				machine.printDfsa();
				System.out.println();
				
				
			}
			System.out.println();
			counter++;
			
		}
		
	}

}
