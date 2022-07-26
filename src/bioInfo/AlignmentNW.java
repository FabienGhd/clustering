package bioInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AlignmentNW {
	private String s1;
	private String s2;
	private int scoreMatch = 5;
	private int scoreMismatch = -4;
	private int scoreIndel = -3;
	private int[][] matrix;
	private int alignmentScore; //best path value
	
	//initializes the attributes and the alignment matrix
	public AlignmentNW(String s1, String s2, int match, int mismatch, int indel) { 
		this.s1 = s1;
	    this.s2 = s2;
	    this.scoreMatch = match;
	    this.scoreMismatch = mismatch;
	    this.scoreIndel = indel;
	    this.alignmentScore = 0;
	    
	    //when initiating the matrix, we add two dashes before each sequence and an empty cell at the position[0][0], hence the +2
	    matrix = new int[s1.length()+2][s2.length()+2]; 
	    matrix[1][1] = 0; //we conventionally add a 0 in the top left corner
	    matrix[1][0] = 0; //we set zeros instead of the dashes
	    matrix[0][1] = 0;
	    
	    
	    for(int i = 0; i < s1.length(); i++) { //fills the first row
	    	matrix[0][i+2] = s1.charAt(i);
	    }
	    for(int i = 0; i < s2.length(); i++) { //fills the first column
	    	matrix[i+2][0] = s2.charAt(i); 
	    }
	    for(int i = 0; i < s1.length(); i++) { //fills the second row
	    	matrix[1][i+2] = (i+1)*indel;
	    }
	    for(int i = 0; i < s2.length(); i++) { //fills the second columns
	    	matrix[i+2][1] = (i+1)*indel;
	    }
	    
	    //fills all the rows and columns
	    for(int i = 2; i < matrix.length; i++) {
	    	for(int j = 2; j < matrix[i].length; j++) {
	    		int choice = 0;
	    		if(s1.charAt(i-2) == s2.charAt(j-2)) { //checks if there is a match
	    			choice = scoreMatch;
	    		} else {choice = scoreMismatch;}
	    		matrix[i][j] = maxOf3(matrix[i-1][j-1] + choice, //left diagonal
	    				matrix[i-1][j] + scoreIndel, //up
	    				matrix[i][j-1] + scoreIndel //left
	    				);
	    		if(matrix[i][j] > alignmentScore) { //the best path value is stored
	    			alignmentScore = matrix[i][j];
	    		}
	    	}
	    }
	}
	
	private int maxOf3(int x, int y, int z) {
		if(z > y && z > x) {
			return z;
		} else if(y > x) {
			return y;
		} else {
			return x;
		}
		
	}
	
	
	public void printMatrix() { // pretty prints the alignment matrix, simply called 'matrix'
		for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
            	if(i == 0 && j >= 2) {
            		char ch = (char) matrix[i][j];
            		System.out.print(ch);
            		 if(j<matrix[0].length-1)
    					 System.out.print("   "); //spaces between the first row
            		 
            	} else if(i >= 2 && j == 0) {
            		char ch = (char) matrix[i][j];
            		System.out.print(ch);
            		 if(j < matrix[0].length-1)
    					 System.out.print("   "); //spaces in front of the first column
            		 
            	} else {
            		if(matrix[i][j] < 0 && matrix[i][j] > -10) {
            			System.out.print(matrix[i][j] + "  ");
            		} else if(matrix[i][j] <= -10) {
            			System.out.print(matrix[i][j] + " ");
            		}
            		else {
            		System.out.print(matrix[i][j] + "   ");
            		}
            	}
            }	
            System.out.println(); //new line
		}
	}
	
	 public  int getScoreMax() {
		 return scoreMatch * Math.min(s1.length(), s2.length());
	 }
	 
	 public  int getScoreMin() {
		 return scoreIndel * (s1.length() + s2.length());
	 }
	 
	 public int getScore() {
		 return this.alignmentScore;
	 }
	 
	 public double getDistance() {
		 return( (getScoreMax() - getScore()) / (getScoreMax() - getScoreMin()));
	 }
	


	public static void main(String[] args) {
		int match = 5, mismatch = -4, indel = -3;
		String myseq1 = "ATGGCAA";
		String myseq2 = "ATCGGAG";
		AlignmentNW al1 = new AlignmentNW(myseq1, myseq2, match, mismatch, indel);
		//we can check the results with this application: https://bioboot.github.io/bimm143_W20/class-material/nw/
		//al1.printMatrix();
		
		String seq1 = "ATTACG";
		String seq2 = "ATATCG";
		String seq3 = "ACCCCG";
		String seq4 = "GGGGAA";
		String seq5 = "TTTACG";
		AlignmentNW alignment1 = new AlignmentNW(seq1,seq2, match, mismatch, indel);
	    AlignmentNW alignment2 = new AlignmentNW(seq1,seq3, match, mismatch, indel);
	    AlignmentNW alignment3 = new AlignmentNW(seq2,seq3, match, mismatch, indel);
		alignment1.printMatrix();
		System.out.println();
		System.out.println("alignment score matrix 1 : " + alignment1.alignmentScore);
		System.out.println("score max matrix 1 : " + alignment1.getScoreMax());
		System.out.println("score min matrix 1 : " + alignment1.getScoreMin());
		System.out.println("distance : " + alignment1.getDistance());
		System.out.println();
		alignment2.printMatrix();
		System.out.println();
		System.out.println("alignment score matrix 2 : " + alignment2.alignmentScore);
		System.out.println("score max matrix 2 : " + alignment2.getScoreMax());
		System.out.println("score min matrix 2 : " + alignment2.getScoreMin());
		System.out.println();
		alignment3.printMatrix();
		System.out.println();
		System.out.println("alignment score matrix 3 : " + alignment3.alignmentScore);
		System.out.println("score max matrix 3 : " + alignment3.getScoreMax());
		System.out.println("score min matrix 3 : " + alignment3.getScoreMin());
		System.out.println();
		
		
  	
	}

}
