package proj1;

import java.util.ArrayList;
import java.util.Arrays;

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
public class dfsa {
	
	/**
	 * The number of states in this machine
	 */
	int numStates;
	
	/**
	 * A 2d array that represents the transitions in this machine.
	 * The size of transitions is number_of_states x number_of_chars_in_alphabet.
	 */
	int transitions[][];
	
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
	public dfsa(int states) {
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
		transitions = new int[numStates+1][alphabet.length]; // +1 represents the trap state
		initialize2d(transitions);
		String[] temp;
		for(int i = 0; i < trans.size(); i++) {
			
			temp = trans.get(i).split(" ");
			
			int p = Integer.parseInt(temp[0]); // the characters in the transition array can be characters
			int a = translate(temp[1].charAt(0));
			int q = Integer.parseInt(temp[2]);
			
			transitions[p][a] = q;
			//System.out.print("p:" + p );
			//System.out.print(" a:" + a );
			//System.out.println(" q:" + q );
		}	
		//print2d(transitions);
	}
	
	/**
	 * Initializes a 2d array with the value of its last index. 
	 * Why? This last index represents a trap state.
	 * @param x
	 */
	private void initialize2d(int[][] x) {
		for(int i = 0; i < getRows(x); i++) {
			for(int j = 0; j < getCols(x); j++) {
				x[i][j] = getRows(x)-1;
			}
		}
	}
	
	/**
	 * A convenient way to view the transitions of this machine.
	 */
	public void printTransitions() {
		for(int i = 0; i < getRows(transitions); i++) {
			for(int j = 0; j < getCols(transitions); j++) {
				if(transitions[i][j] != getRows(transitions)+1) { // do not print out trap states
					System.out.println("\t" + i + " " + alphabet[j] + " " + transitions[i][j]);
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
	private int getRows(int[][] r) {
		return r.length;
	}
	
	/**
	 * Returns the number of columns in a 2d matrix
	 * @param r
	 * @return the number of columns
	 */
	private int getCols(int[][] r) {
		return r[0].length;
	}
	
	/**
	 * A convenient way to print out any 2d matrix.
	 * @param x - a 2d matrix
	 */
	private void print2d(int[][] x) {
		for (int i = 0; i < getRows(x); i++){
		    for (int j = 0; j < getCols(x); j++){
		        System.out.print(x[i][j] + " ");
		    }
		    System.out.println();
		}
	}
	
	/**
	 * Tests whether a string will be accepted by this dfsa or not. 
	 * If the string is accepted, then it is in the language.
	 * If the string is rejected, then it is not in the language. 
	 * @param str - the string in question
	 */
	public void test(String str) {
		int initialState = 0; 
		int currentState = initialState;
		
		for(int i = 0; i < str.length(); i++) {
			if(!inAlphabet(str.charAt(i))) { //if it ISN'T in the alphabet
				if(str.charAt(i) == ' ' && i == 0) { System.out.printf("%-15s %15s %n", "\t"+str, "accept"); }
				else { 
					System.out.printf("%-15s %15s %n", "\t"+str, "reject"); 
					break; 
				}				
			}
			else if(i == str.length()-1) { //if it IS the last character of the string
				
				//System.out.print("current state: " + currentState);
				//System.out.print(" -- consume: " + translate(str.charAt(i)));
				currentState = transitions[currentState][translate(str.charAt(i))];
				//System.out.println(" -- last state: " + currentState);
				
				if(finalStates[currentState] == true) { System.out.printf("%-15s %15s %n", "\t"+str, "accept"); }
				else { System.out.printf("%-15s %15s %n", "\t"+str, "reject"); }
			}
			else {
				//System.out.print("current state: " + currentState);
				//System.out.print(" -- consume: " + translate(str.charAt(i)));
				currentState = transitions[currentState][translate(str.charAt(i))];
				//System.out.println(" -- new state: " + currentState);
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
