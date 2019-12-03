
package sample;
import java.util.ArrayList;

class Formatter{
    protected int line_length;
    protected int align;  // left = 0, right, centered, equal
    protected boolean wrap;
    protected boolean double_spaced;
    protected boolean title;  // switched off after first text line
    protected int p; // = 0 after insert
    protected int b; // = 0 after insert
    protected boolean two_column;

    protected Errors E = new Errors();

    public static String formatColumns(String inputString){
        ArrayList<ColumnBlock> blocks = new ArrayList<ColumnBlock>();
        ColumnBlock current = new ColumnBlock(1);
        String[] lines = inputString.split("\\r?\\n");

        for(String line : lines){
            if(line.equals("-a2")){
                if(current.isSingleColumn()){
                    blocks.add(current);
                    current = new ColumnBlock(2);
                }
            }
            else if(line.equals("-a1")){
                if(!current.isSingleColumn()){
                    blocks.add(current);
                    current = new ColumnBlock(1);
                }
            }
            else{
                current.addLine(line);
            }
        }

        blocks.add(current);

        String out = "";
        for(ColumnBlock a: blocks){
            out = out + a.format();
        }

        return out;
    }
}

class ColumnBlock{
    private int columns;
    String text;

    public ColumnBlock(int columns){
        this.columns = columns;
        text = "";
    }

    public boolean isSingleColumn(){
        return columns == 1;
    }

    public void addLine(String text){
        this.text += text + "\n";
    }
//<<<<<<< HEAD
//
//    // This assumes that lines are at most 35 characters long if columns = 2w
//=======
//
//        // This assumes that lines are at most 35 characters long if columns = 2
//        >>>>>>> ccdea80eb77ab76f6fb79ba72290bc558c0f7aec
    public String format(){
        if(columns == 1){
            return text;
        }
        else{
            String[] lines = text.split("\\r?\\n");
            String out = "";

            int diff = lines.length / 2 + 1;

            for(int i = 0; i < diff; i++){
                while(lines[i].length() < 35){
                    lines[i] = lines[i] + " ";
                }
                if(i + diff < lines.length){
                    while(lines[i + diff].length() < 35){
                        lines[i + diff] = lines[i + diff] + " ";
                    }
                    out = out + lines[i] + "          " + lines[i + diff] + "\n";
                }
                else{
                    out = out + lines[i] + "                                             \n";
                }
            }

            return out;
        }
    }
    
    	public String leftJust(String input, int lineLength) {		//this is the only method that assumes that input is longer than line length.
		input = input.trim();
		String output = "";
		char[] temp = input.toCharArray();
		int i = 0;
			while( i < input.length()) {
  				System.out.print(temp[i]);
  				i++;
  				if(i == 79) {
  					System.out.print("\n");
  				}
  			}
		
		return output;
	}

	public String rightJust(String input, int lineLength) {
		input = input.trim();
		char[] temp = input.toCharArray();
		String output = "";
		int counter = 0;
		int section = 0;
		
		while( counter <= lineLength) {
			if(input.length() > lineLength) {		//if input length is more than line length;
				
				if(counter == 79) {
					String temp2 = new String(temp, section, counter);
					output = output + temp2 + "\n";
					section = section + 80;
					System.out.println("Iteration was hit.");
				}
				
				//String temp3 = new String(temp, section, 145);
				//output = output + temp3 + "\n";
				counter++;
				
			} else {		//if input length is less than line length;
				int spaces = lineLength - input.length();
				
				for(int i = 0; i <= spaces; i++) {
					output = output + " ";
				}
				output = output + input;
				return output;
			}
		}
		
		
		return output;
	}
		 


	public String centerJust(String input, int lineLength) {
		String output = "";
		input = input.trim();
		int spaces = lineLength - input.length();
		if(spaces%2 == 1) {
			output = output + " ";
		}
		for(int i = 1; i <= (spaces/2); i++) {	//adds spaces in front of input to make it center aligned.
			output = output + " ";
		}
		
		output = output + input;
		
		for(int i = 1; i <= (spaces/2); i++) {	//adds spaces in the back of sentence to make it equal to line length.
			output = output + " ";
		}
		return output;
	
	
	}


	public String equalSpacing(String input, int lineLength) {
		String output = "";
		input = input.trim();
		char[] temp = input.toCharArray();
		int count = 0, spaces = 0, remainder = 0;
		int extra = lineLength - input.length();
		
		while(count < input.length()) {
			if(temp[count] == ' ') {		//counts the number of spaces.
				spaces++;
//				System.out.printf("Spaces: %d Line: %d\n", spaces, count);
			}
			count++;
		}
		
		String[] lines = input.split(" ");		//makes an array of all words in input.
		remainder = extra%spaces;
		spaces = extra/spaces;		//how many spaces to add between each word.
		count = 0;
//		System.out.println(remainder);
//		System.out.println(spaces);
		
		for(int i = 0; i < lines.length - 1; i++) {
			lines[i].trim();
			for(int ii = 0; ii <= spaces; ii++) {		//adds spaces to each index of a word but not the last one.
				lines[i] = lines[i] + " ";
			}
		}
		
		for(int ii = 0; ii <= remainder; ii++) {
			lines[ii] = lines[ii] + " ";
		}
		
		for(int i = 0; i < lines.length - 1; i++) {
			output = output + lines[i];
		}
		output = output + lines[lines.length-1];	//adds last word into output.
		
		
		return output;
		
	}


	public String doubleSpace(String input) {		//adds new line to the end output.
		input = input.trim();
		String output = input + "\n";
		return output;
	}
	
	public String indent(String input, int spaces) {
		input = input.trim();
		String output = "";
		for(int i = 0; i < spaces; i++) {
			output = output + " ";
		}
		
		return (output + input);
	}
	
	public String blankLines(int numLines) {
		String output = "";
		for(int i = 0; i < numLines; i++) {
			output = output + "\n";
		}
				
		return output;
	}
	
	public String justification(String input) {
		String output = "";
		String[] lines = input.split("\\r?\\n");
		 Formatter just = new Formatter();
		for(String line: lines) {
			if(line.startsWith("-l")) {			//left justified
				align = 0;
			} else if(line.startsWith("-r")){		//right justified
				align = 1;
			} else if(line.startsWith("-c")) {		//center aligned
				align = 2;
			} else if(line.startsWith("-e")) {		//equal spacing
				align = 3;
			} else if(!line.startsWith("-")) {
				
				if(align == 0) {		//left justified
					output = output + just.leftJust(line, line.length());
					
				} else if(align == 1) {		//right justified
					output = output + just.rightJust(line, line.length());
					
				} else if(align == 2) {		//center align
					output = output + just.centerJust(line, line.length());
					
				} else if(align == 3) {		//equal spacing
					output = output + just.equalSpacing(input, line.length());
				}
			}
			
		}
			
		return output;
	}
	
	public String indentation(String input) {
		String output = "";
		String[] lines = input.split("\\r?\\n");
		 Formatter just = new Formatter();
		for(String line: lines) {
			if(line.startsWith("-p")) {
				
			}
		}
		return output;
	}
	
	public String doubleSpaces(String input) {
		String output = "";
		String[] lines = input.split("\\r?\\n");
		 Formatter just = new Formatter();
		for(String line: lines) {
			if(line.startsWith("-d")) {
				double_spaced = 1;
			} else if(line.startsWith("-s")) {
				double_spaced = 0;
			} else if(!line.startsWith("-")) {
				if(double_spaced == 1) {		//double spaced on.
					output = output + just.doubleSpace(line);
				} else if (double_spaced == 0) {	//single spaced on.
					output = output + line;
				}
			}
		}
		return output;
	}
}
