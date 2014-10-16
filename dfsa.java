package proj1;

import java.util.ArrayList;
import java.util.Arrays;

public class dfsa {
	
	int numStates;
	int transitions[][];
	boolean finalStates[];
	char[] alphabet;
	ArrayList testStrings;
	
	public dfsa(int states) {
		numStates = states;
		finalStates = new boolean[numStates+1]; // +1 is a trap state
		Arrays.fill(finalStates, Boolean.FALSE);
	}
	
	public void setFinal(String line) {
		String[] input = line.split(" ");
		for(String state : input) {
			finalStates[Integer.parseInt(state)] = true;
		}
	}
	
	public void printFinalStates() {
		for(int i = 0; i < finalStates.length; ++i) {
			if (finalStates[i] == true) { System.out.print(i + " "); }
		}
		System.out.println();
	}
	
	public void setAlpha(String line) {
		String[] temp = line.split(" ");
		alphabet = new char[temp.length];
		for(int i = 0; i < alphabet.length; i++) {
			alphabet[i] = temp[i].charAt(0);
		}
	}
	
	public void printAlpha() {
		int commas = 0;
		for(char a : alphabet) {
			System.out.print(a);
			if(commas < alphabet.length-1) { System.out.print(", "); }
			commas++;
		}
		System.out.println();
	}
	
	// Transitions array is an array of integers, where each int
	// represents an input either numerical or any other symbol, i.e. 10 = a
	public void setTrans(ArrayList<String> trans) {
		transitions = new int[numStates+1][alphabet.length]; // +1 represents the trap state
		initialize2d(transitions);
		String[] temp;
		for(int i = 0; i < trans.size(); i++) {
			
			temp = trans.get(i).split(" ");
			for(String s : temp) {
				System.out.print(s + " ");
			}
			
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
	
	private void initialize2d(int[][] x) {
		for(int i = 0; i < getRows(x); i++) {
			for(int j = 0; j < getCols(x); j++) {
				x[i][j] = getRows(x)-1;
			}
		}
	}
	
	public void printTransitions() {
		for(int i = 0; i < getRows(transitions); i++) {
			for(int j = 0; j < getCols(transitions); j++) {
				if(transitions[i][j] != getRows(transitions)+1) { // do not print out trap states
					System.out.println("\t" + i + " " + alphabet[j] + " " + transitions[i][j]);
				}
			}
		}
	}
	
	private int translate(char ch) {
		for(int i = 0; i < alphabet.length; i++) {
			if(alphabet[i] == ch) { return i; }
		}
		return numStates+1;
	}
	
	private int getRows(int[][] r) {
		return r.length;
	}
	
	private int getCols(int[][] r) {
		return r[0].length;
	}
	
	private void print2d(int[][] x) {
		for (int i = 0; i < getRows(x); i++){
		    for (int j = 0; j < getCols(x); j++){
		        System.out.print(x[i][j] + " ");
		    }
		    System.out.println();
		}
	}
	
	public void test(String str) {
		int initialState = 0; 
		int currentState = initialState;
		
		for(int i = 0; i < str.length(); i++) {
			if(!inAlphabet(str.charAt(i))) { //if it ISN'T in the alphabet
				if(str.charAt(i) == ' ' && i == 0) { System.out.print("accept"); }
				else { 
					System.out.print("reject"); 
					break; 
				}				
			}
			else if(i == str.length()-1) { //if it IS the last character of the string
				
				//System.out.print("current state: " + currentState);
				//System.out.print(" -- consume: " + translate(str.charAt(i)));
				currentState = transitions[currentState][translate(str.charAt(i))];
				//System.out.println(" -- last state: " + currentState);
				
				if(finalStates[currentState] == true) { System.out.print("accept"); }
				else { System.out.print("reject"); }
			}
			else {
				//System.out.print("current state: " + currentState);
				//System.out.print(" -- consume: " + translate(str.charAt(i)));
				currentState = transitions[currentState][translate(str.charAt(i))];
				//System.out.println(" -- new state: " + currentState);
			}
			
		}
		
		System.out.println();

	}
	
	private boolean inAlphabet(char c) {
		for(int i = 0; i < alphabet.length; i++) {
			if(c == alphabet[i]) {
				return true;
			}
		}
		return false;
	}
	
}