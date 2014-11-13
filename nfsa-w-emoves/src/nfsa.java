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
@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class nfsa {
	
	/**
	 * The number of states in this machine
	 */
	public int numStates;
	
	/**
	 * A 2d array that represents the transitions in this machine.
	 * The size of transitions is number_of_states x number_of_chars_in_alphabet.
	 */
	private Set<Integer> transitions[][];
	
	private Set<Integer> nfsa[][];
	
	private Set<Integer> eclosure[];
	
	private ArrayList<Set[]> dfsa;
	
	/**
	 * An array whose indices refer to a particular state in this machine.
	 * If the element at index i is true, then it is a final state.
	 * If the element at index i is false, then it is not a final state.
	 */
	private boolean finalStates[];
	
	/**
	 * An array that contains all valid characters of this machine.
	 */
	private char[] alphabet;
	
	private int alphabetSize;
	
	private boolean hasEmoves = false;
	
	/**
	 * Constructs a new machine, initializing all states as NOT final.
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
	 * Prints the final states of this nfsa machine.
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
		alphabet = new char[temp.length+1]; // where the first index represents a column for emoves
		alphabet[0] = 'e';
		for(int i = 1; i < alphabet.length; i++) { // for actual alphabet characters
			alphabet[i] = temp[i-1].charAt(0); // copy actual alphabet values into alphabet array
		}
		alphabetSize = alphabet.length-1;
	}
	
	/**
	 * A convenient way to print all valid characters of this machine.
	 */
	public void printAlpha() {
		for(int i = 1; i < alphabet.length; i++) {
			System.out.print(alphabet[i] + " ");
		}
		System.out.println();
	}
	
	/**
	 * A convenient way to print the elements within a Set.
	 */
	private void printSet(Set<Integer> s) {
		if(s != null) {
			for(int i : s) { System.out.print(i + " "); }
		}
	}
	
	
	private void getEclosure() {
		eclosure = new HashSet[numStates];
		int counter = 0;
		for(int i = 0; i < getRows(transitions)-1; i++) {
			Set<Integer> tmp = new HashSet();
			tmp.add(i);
			if(transitions[i][0] != null) {
				tmp.addAll(transitions[i][0]);
			}
			int setSize;
			do {
				setSize = tmp.size();
				Set<Integer> tmp2 = new HashSet();
				for(int s : tmp) {
					if(transitions[s][0] != null) {
						tmp2.addAll(transitions[s][0]);
					}
				}
				tmp.addAll(tmp2);
			} while(tmp.size() > setSize);
			eclosure[i] = tmp;
		}
	}	
	
	public void setNfsaNoEmove() {
		getEclosure();
		nfsa = new HashSet[numStates][alphabetSize];	
		
		for(int i = 0; i < getRows(nfsa); i++) {
			for(int j = 0; j < getCols(nfsa); j++) {
				Set<Integer> eclosureAt_i = eclosure[i];
				Set<Integer> toAdd = new HashSet();
				int setSize;
				for(int element : eclosureAt_i) {
					if(transitions[element][j+1] != null) { 
						toAdd.addAll(transitions[element][j+1]); 
					}
				}
				Set<Integer> toAdd2 = new HashSet();
				for(int element : toAdd) {
					toAdd2.addAll(eclosure[element]);
				}
				toAdd.addAll(toAdd2);
				nfsa[i][j] = toAdd;
			}
			
		}
		
	}
	
	public int getDfsaNumStates() {
		getDfsa();
		return dfsa.size();
	}
	
	private void getDfsa() {
		if(hasEmoves == true) {
			setNfsaNoEmove();
		}
		else {
			nfsa = transitions;
		}
		
		dfsa = new ArrayList<Set[]>();
		ArrayList<Set> dfsaStatesList = new ArrayList<Set>();
		
		Set<Integer> tmpSet = new HashSet<Integer>();
		Set<Integer>[] tmpArray = new HashSet[alphabetSize];
		
		tmpSet.add(0);
		dfsaStatesList.add(tmpSet);
		tmpArray = getFirstRow();
		dfsa.add(tmpArray);
		int counter = 1;
		
		for(Set<Integer> s : tmpArray) {
			if( !dfsaStatesList.contains(s) ) {
				dfsaStatesList.add(s);
			}
		}
		do {
			Set<Integer> dfsaSet = dfsaStatesList.get(counter);
			Set<Integer>[] toAdd = new HashSet[alphabet.length-1];
			for(int j = 0; j < alphabet.length-1; j++) {
				Set<Integer> newSet = new HashSet();
				for(int s : dfsaSet) {
					if(nfsa[s][j] != null) {
						newSet.addAll(nfsa[s][j]);
					}
				}
				toAdd[j] = newSet;
				if( !dfsaStatesList.contains(newSet) && newSet.size() > 0 ) {
					dfsaStatesList.add(newSet);
				}
			}
			dfsa.add(counter,toAdd);
			counter += 1;
		} while(counter < dfsaStatesList.size());	
	}	
	
	private Set<Integer>[] getFirstRow() {
		Set<Integer>[] tmpArray = new HashSet[alphabetSize];
		if(hasEmoves == true) {
			tmpArray = nfsa[0];
		}
		else {
			for(int i = 0; i < tmpArray.length; i++) {
				tmpArray[i] = nfsa[0][i+1];
			}
		}
		return tmpArray;
	}
	
	/*
	public void getDFSA() {
		ArrayList<Set[]> dfsaTransitionsList = new ArrayList<Set[]>();
		ArrayList<Set> dfsaStatesList = new ArrayList<Set>();
		
		Set<Integer> tmpSet = new HashSet<Integer>();
		Set<Integer>[] tmpArray = new HashSet[alphabet.length];
		
		for(int i = 0; i < getRows(transitions); i++) {
			tmpSet.add(i);
			if( !dfsaStatesList.contains(tmpSet) ) {
				dfsaStatesList.add(tmpSet);
			}
			tmpSet.clear();		
			for(int j = 0; j < getCols(transitions); j++) {
				tmpArray[j] = transitions[i][j];
				printSet(transitions[i][j]);
				if(!dfsaTransitionsList.contains(tmpArray[j])) {
					dfsaStatesList.add(tmpArray[j]);
				}
			}
			dfsaTransitionsList.add(tmpArray);
			System.out.println();
		}
		
		System.out.println("Printing dfsa: ");
		for(Set[] sA : dfsa) {
			for(Set i : sA) {
				printSet(i);
			}
		}
		
	} */
	
	/**
	 * Sets the transition matrix of this machine, which tells the machine where
	 * to go next.
	 * @param trans
	 */
	public void setTrans(ArrayList<String> trans) {
		transitions = new Set[numStates+1][alphabet.length]; // +1 represents the trap state
		hasEmoves = false;
		String[] temp;
		for(int i = 0; i < trans.size(); i++) {
			temp = trans.get(i).split(" ");
			int p = Integer.parseInt(temp[0]); // the characters in the transition array can be characters
			
			int a = translate(temp[1].charAt(0));
			if(a == 0) { hasEmoves = true; }
			Set<Integer> tmp = new HashSet<Integer>();
			for(int j = 2; j < temp.length; j++) {
				int q = Integer.parseInt(temp[j]);
				tmp.add(q);
			}
			transitions[p][a] = tmp;
		}
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
	
	public void printDfsa() {
		for(int i = 0; i < getRows(dfsa); i++) {
			for(int j = 0; j < getCols(dfsa); j++) {
				System.out.print("{ ");
				printSet(dfsa.get(i)[j]);
				System.out.print(" }");
			}
			System.out.println();
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
	private int getRows(ArrayList<Set[]> r) {
		return r.size();
	}
	
	/**
	 * Returns the number of columns in a 2d matrix
	 * @param r
	 * @return the number of columns
	 */
	private int getCols(Set[][] r) {
		return r[0].length;
	}
	private int getCols(ArrayList<Set[]> r) {
		return r.get(0).length;
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
