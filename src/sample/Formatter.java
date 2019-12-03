
package sample;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Formatter{
    protected int line_length = 80;
    protected int align = 0;  // left = 0, right, centered, equal
    protected int b = 0; //blankspaces:0 = off, 1 = on; 
    protected boolean wrap = false;
    protected boolean double_spaced = false;
    protected boolean two_column = false;

    public String formatWrap(String inputString){
        String output = "";
        String[] lines = inputString.split("\\r?\\n");
        for(String line : lines){
            if(line.startsWith("-w+")){
                wrap = true;
            }
            else if(line.startsWith("-w-")){
                wrap = false;
            }
            else{
                if(!line.startsWith("-")){
                    if(wrap){
                        output = output + line + " ";
                    }
                    else{
                    	if(!output.endsWith("\n")){
                    		output = output + "\n";
						}
                        output = output + line + "\n";
                    }
                }
                else{
                    if(wrap){
                        output = "\n" + line + "\n";
                    }
                    else{
						if(!output.endsWith("\n")){
							output = output + "\n";
						}
                        output = output + line + "\n";
                    }
                }

            }
        }
		while(output.length() > 0 && output.charAt(output.length() - 1) == '\n') output = output.substring(0, output.length() - 1);
        return output;
    }

    public String formatTitle(String inputString){
        String output = "";
        boolean titleToken = false;
        int titleLength = 0;
        String underline = "";
        String[] lines = inputString.split("\\r?\\n");
        for(String line : lines){
            if(line.startsWith("-t")){
                titleToken = true;
            }
            else if(!line.startsWith("-") && titleToken == true){
                titleLength = line.trim().length();
                for(int i = 0; i < titleLength; i++){
                    underline += "-";
                }
                line = centerJust(line, line.length()) + "\n" + centerJust(underline, line.length());
                //line = centerJust(line, line_length);
                titleToken = false;
                output = output + line + "\n";
            }
            else{
                output = output + line + "\n";
            }
        }
        output = output.substring(0,output.length()-2);
        return output;
    }

    public static String lineLength(String inputString)
    {
        int max = 80;
        
        String output = "";
        for(String line: inputString.split("\\r?\\n"))
        {
                String out = "";
				
				if(line.length() > 2 && line.trim().substring(0,3).equals("-a2"))
                {
                        max = 35;
                }
				if(line.length() > 2 && line.trim().substring(0,3).equals("-a1"))
                {
                        max = 80;
                }

                if(line.length() > 2 && line.trim().substring(0,2).equals("-n"))
                {
                        max = Integer.parseInt(line.trim().substring(2));
                }
                else{
                    String[] words = line.split(" ");
                    int index = 0;
                    int currentSize = 0;

                    while(index < words.length)
                    {
                        if(words[index].equals(""))
                        {
                            index++;
                        }
                        else if(words[index].length() + currentSize <= max && currentSize == 0)
                        {
                                out += words[index];
                                currentSize += words[index].length();
                                index++;

                        }
                        else if(words[index].length() + currentSize + 1 <= max)
                        {
                                out += " " + words[index];
                                currentSize += words[index].length() + 1;
                                index++;
                        }
                        else{
                            if(words[index].length() > max && currentSize == 0)
                            {
                                out += words[index].substring(0, max);
                                words[index] = words[index].substring(max);
                            }
                            currentSize = 0;

                            int startOfCurrentLine = 0 > out.lastIndexOf("\n") ? 0 : out.lastIndexOf("\n") + 1;
                            while(out.substring(startOfCurrentLine).length() < max) out += " ";

                            out += "\n";                    
                        }
                    }
                    if(out.length() > 0 && out.charAt(out.length() - 1) != '\n'){
                        int startOfCurrentLine = 0 > out.lastIndexOf("\n") ? 0 : out.lastIndexOf("\n") + 1;
                        while(out.substring(startOfCurrentLine).length() < max) out += " ";
                        out += "\n";
                    }
                }
            output += out;
        }

        while(output.length() > 0 && output.charAt(output.length() - 1) == '\n') output = output.substring(0, output.length() - 1);

        return output;
    }

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
	public String leftJust(String input, int lineLength) {		//this is the only method that assumes that input is longer than line length.
		input = input.trim();
		String output = "";
		char[] temp = input.toCharArray();
		int i = 0;
		while( i < input.length()) {
  			output = output + temp[i];
  			i++;
  			if(i == lineLength) {
  				output = output + "\n";
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
					output = output + leftJust(line, line.length()) + "\n";
					
				} else if(align == 1) {		//right justified
					output = output + rightJust(line, line.length()) + "\n";
					
				} else if(align == 2) {		//center align
					output = output + centerJust(line, line.length()) + "\n";
					
				} else if(align == 3) {		//equal spacing
					output = output + equalSpacing(input, line.length()) + "\n";
				}
			}
			
		}
		while(output.length() > 0 && output.charAt(output.length() - 1) == '\n') output = output.substring(0, output.length() - 1);
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
		while(output.length() > 0 && output.charAt(output.length() - 1) == '\n') output = output.substring(0, output.length() - 1);
		return output;
	}
	
	public String doubleSpaces(String input) {
		String output = "";
		String[] lines = input.split("\\r?\\n");
		for(String line: lines) {
			if(line.startsWith("-d")) {
				double_spaced = true;
			} else if(line.startsWith("-s")) {
				double_spaced = false;
			} else if(!line.startsWith("-")) {
				if(double_spaced == true) {		//double spaced on.
					output = output + doubleSpace(line) + "\n";
				} else if (double_spaced == false) {	//single spaced on.
					output = output + line + "\n";
				}
			}
		}
		while(output.length() > 0 && output.charAt(output.length() - 1) == '\n') output = output.substring(0, output.length() - 1);
		return output;
	}
	
	public String blankSpaces(String input) {
		String output = "";
		String[] lines = input.split("\\r?\\n");
		int number = 0; 
		 
		for(String line: lines) {
			if(line.startsWith("-b")) {
				number = Integer.parseInt(line.trim().substring(2));
				b = 1;
			} else if(!line.startsWith("-")) {
				if(b == 1) {
					output = output + blankLines(number) + "\n";
					b = 0;
				} else if(b == 0) {
					output = output + line + "\n";
				}
			}
		}
		while(output.length() > 0 && output.charAt(output.length() - 1) == '\n') output = output.substring(0, output.length() - 1);
		return output;
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
}


