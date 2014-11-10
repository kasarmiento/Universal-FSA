import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
public class nfsa {
	
	/**
	 * The number of states in this machine
	 */
	int numStates;
	
	/**
	 * A 2d array that represents the transitions in this machine.
	 * The size of transitions is number_of_states x number_of_chars_in_alphabet.
	 */
	Set<Integer> transitions[][];
	
	ArrayList<String> baloney = new ArrayList<String>();
	
	
	
	
	/**
	 * An array whose indices refer to a particular state in this machine.
	 * If the element at index i is true, then it is a final state.
	 * If the element at index i is false, then it is not a final state.
	 */
	boolean finalStates[];
	
	/**
	 * An array that contains all valid characters of this machine.
	 */
	char[] alphabet;
	
	/**
	 * Constructs a new dfsa machine, initializing all states as NOT final.
	 * The boolean array called finalStates has a size of total_states + 1 
	 * where the additional 1 represents a trap state.
	 * @param states - The total number of states in this machine
	 */
	public nfsa(int states) {
		numStates = states;
		finalStates = new boolean[numStates+1]; // +1 is a trap state
		Arrays.fill(finalStates, Boolean.FALSE);
	}
	
	/**
	 * Sets the final states of this dfsa machine.
	 * @param line - a string containing the final states of this machine separated by a white space
	 */
	public void setFinal(String line) {
		String[] input = line.split(" ");
		for(String state : input) {
			finalStates[Integer.parseInt(state)] = true;
		}
	}
	
	/**
	 * Prints the final states of this dfsa machine.
	 */
	public void printFinalStates() {
		for(int i = 0; i < finalStates.length; ++i) {
			if (finalStates[i] == true) { System.out.print(i + " "); }
		}
		System.out.println();
	}
	
	/**
	 * Defines the alphabet for this dfsa machine.
	 * @param line - a string containing all valid characters of this machine separated by a white space
	 */
	public void setAlpha(String line) {
		String[] temp = line.split(" ");
		alphabet = new char[temp.length];
		for(int i = 0; i < alphabet.length; i++) {
			alphabet[i] = temp[i].charAt(0);
		}
	}
	
	/**
	 * A convenient way to print all valid characters of this machine.
	 */
	public void printAlpha() {
		int commas = 0;
		for(char a : alphabet) {
			System.out.print(a);
			if(commas < alphabet.length-1) { System.out.print(", "); }
			commas++;
		}
		System.out.println();
	}
	
	/**
	 * Sets the transition matrix of this machine, which tells the machine where
	 * to go next.
	 * @param trans
	 */
	public void setTrans(ArrayList<String> trans) {
		transitions = new Set[numStates+1][alphabet.length]; // +1 represents the trap state
		
		String[] temp;
		for(int i = 0; i < trans.size(); i++) {
			
			temp = trans.get(i).split(" ");
			
			int p = Integer.parseInt(temp[0]); // the characters in the transition array can be characters
			int a = translate(temp[1].charAt(0));
			
			Set<Integer> tmp = new HashSet<Integer>();
			for(int j = 2; j < temp.length; j++) {
				int q = Integer.parseInt(temp[j]);
				tmp.add(q);
			}
			transitions[p][a] = tmp;
			
		}	
		System.out.println("Transitions:");
		printTransitions();
		
	}
	
	/**
	 * A convenient way to view the transitions of this machine.
	 */
	public void printTransitions() {
		for(int i = 0; i < getRows(transitions); i++) {
			for(int j = 0; j < getCols(transitions); j++) {
				if(transitions[i][j] != null) { // do not print out trap states
					System.out.print("\t" + i + " " + alphabet[j] + " ");
					for (int s : transitions[i][j]) {
			    	    System.out.print(s + " ");
			    	}
					System.out.println();
				}	
			}
		}
	}
	
	/**
	 * Translates a character of this machine's alphabet into a corresponding number
	 * @param ch - the character to translate
	 * @return the character's corresponding number according to its location in the alphabet matrix
	 */
	private int translate(char ch) {
		for(int i = 0; i < alphabet.length; i++) {
			if(alphabet[i] == ch) { return i; }
		}
		return numStates+1;
	}
	
	/**
	 * Returns the number of rows in a 2d matrix
	 * @param r
	 * @return the number of rows
	 */
	private int getRows(Set[][] r) {
		return r.length;
	}
	
	/**
	 * Returns the number of columns in a 2d matrix
	 * @param r
	 * @return the number of columns
	 */
	private int getCols(Set[][] r) {
		return r[0].length;
	}
	
	/**
	 * A convenient way to print out a 2d matrix of 
	 * Integer Sets. x[i][j] contains a Set of Integers.
	 * x[i][j] = {0}, or even x[i][j] = {0,1} for example
	 * @param x - a 2d matrix of Integer Sets
	 */
	private void print2d(Set<Integer>[][] x) {
		boolean format = false; // This boolean is simply for white space between transitions. 
		for (int i = 0; i < getRows(x); i++){
		    for (int j = 0; j < getCols(x); j++){
		    	format = false;
		    	if(x[i][j] != null) {
			    	for (int s : x[i][j]) {
			    	    System.out.print(s + " ");
			    	    format = true;
			    	}
		    	}
		    }
		    if(format == true) {
		    	System.out.println();
		    }
		}
	}
	
	/**
	 * Checks whether a certain character is in this dfsa's alphabet
	 * @param c - the character in question
	 * @return true if the character is in the alphabet, false otherwise
	 */
	private boolean inAlphabet(char c) {
		for(int i = 0; i < alphabet.length; i++) {
			if(c == alphabet[i]) {
				return true;
			}
		}
		return false;
	}
	
}
