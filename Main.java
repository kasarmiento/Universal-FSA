package proj1;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Khamille S. 2014
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
			
				// dfsa constructor
				dfsa machine = new dfsa(Integer.parseInt(temp));
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
				
				// Read in and test strings
				System.out.println("(5) strings: ");
				while(temp.substring(0,1).compareTo(".") != 0) { // while we have not reached 0s 
					machine.test(temp);
					temp = in.readLine();
				}
				
				
			}
			System.out.println();
			counter++;
			
		}
		
	}

}
