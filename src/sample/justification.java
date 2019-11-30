/*
 * Author: Brian Dang
 * Class: CSE360 (85141)
 * Date: 11/25/19
 * Text Formatter
 * 
 * Description: It will take in an input .txt file. Depending on the base of a command system will produced a
 * specifically formatted text output.
 */

package cse360GroupProject;

import java.io.*;
//import java.io.File; 
//import java.util.Scanner; 

public class justification {

	public static void main(String[] args)throws Exception {
	  File file = new File("C:\\Users\\bball\\OneDrive\\Desktop\\ASU School Files\\Fall2019\\CSE360\\TesterFile.txt"); 
	  BufferedReader br = new BufferedReader(new FileReader(file)); 
	  justification stuff = new justification();
	  
	  String st; 
	  int max = 79;
	  int i = 0;
	  int buffNum = 0;
	  int strNum = 0;
	  char[] str = new char[80];
	  char[] buffer = new char[80];

	  while ((st = br.readLine()) != null) {
		  str = st.toCharArray();
		  System.out.println(str);

		  switch(st) {
		  		case "-l":		//Once this point is reached change Format Class to make the rest of the text left justified.
		  			System.out.println("Command: Left Justification");
		  			i = 0;
		  			st = br.readLine();
		  			str = st.toCharArray();
		  				
		  			while( i < str.length) {
		  				System.out.print(str[i]);
		  				i++;
		  				if(i == 79) {
		  					System.out.print("\n");
		  				}
		  			}
		  			
		  			System.out.print("\n");
		  			break;
		  		
		  		case "-r":
		  			System.out.println("Command: Right Justification");
		  			i = 0;
		  			st = br.readLine();
		  			str = st.toCharArray();
		  				st.
		  			while( i < str.length) {
		  				if(str[79] == ' ') {	//if sentence ends in a space delete the space so last letter is on the right.
		  					max = 78;
		  				}
		  				System.out.print(str[i]);
		  				i++;
		  				if(i == max) {
		  					System.out.print("\n");
		  				}
		  			}
//	  				System.out.println(stuff.addSpaces(st, 20));
		  			
		  			break;
		  		
		  		case "-c":
		  			System.out.println("Command: Center Justification");
		  			break;
		  		
		  		case "-e":
		  			System.out.println("Command: Equal Spacing");
		  			break;
		  			
		  		default:
		  			break;
		  }
		  
	  }
	  
	} 
	
	public char[] addSpaces(char[] input, int numSpaces) {	//add spaces in front of the text so the output is formatted correctly.
		char[] temp = new char [input.length + numSpaces];
		int i = 0;
		while(i < numSpaces) {
			temp[i] = ' ';
			i++;	
		}
		i = 0;
		for(int ii = numSpaces; ii < input.length; ii++) {
			temp[ii] = input[i];
			i++;
		}
		
		return (temp);
	}
		 
}
