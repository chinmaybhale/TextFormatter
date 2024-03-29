/**
 * This is the main class for formatting the document
 * its methods are called in Controller one by one in order
 * of precedence to format the document
 *
 * @author Chinmay Bhale, Brian Dang, Henrique Mello, An Ngo
 */
package sample;

import java.lang.String;
import java.util.ArrayList;


class Formatter{
    protected int max = 80;
    protected static int align = 0;  // left = 0, right, centered, equal
    protected boolean wrap = false;
    protected boolean double_spaced = false;
    protected boolean two_column = false;

	/**
	 * This method formats the text to wrap or unwrap it by checking
	 * for -w+ or -w-
	 *
	 * @param inputString which is the string which has to be formatted
	 * @return output which is the formatted string
	 */
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
                        output = output + "\n" + line + "\n";
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

	/**
	 * This method formats the the line below the title (-t) tag to
	 * output it with "-" below it as underline
	 *
	 * @param inputString which is the string to format
	 * @return output which is the formatted string
	 */
	public String formatTitle(String inputString){
        String output = "";
        boolean titleToken = false;
        int titleLength;
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
                titleToken = false;
                output = output + line + "\n";
				underline = "";
                //System.out.println(line);
            }
            else{
                output = output + line + "\n";
            }
        }
        //output = output.substring(0,output.length()-1);
		while(output.length() > 0 && output.charAt(output.length() - 1) == '\n') output = output.substring(0, output.length() - 1);
        return output;
    }

	/**
	 * This method formats the text to fit into a certain line length
	 * it will add extra spaces at the end if the line length is shorter than max
	 * It gets the line length from the -n# command where # is the max number of characters
	 * in one line
	 *
	 * @param inputString which is the string to format
	 * @return output which is the formatted string
	 */
	public String lineLength(String inputString)
    {
        //int max = 80;
        
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

	/**
	 * This method formats the text into 2 columns if it encounters
	 * the -a2 command. It also resets the line length to 80, and splits the columns
	 * based on a 35/10/35 format.
	 *
	 * @param inputString which is the string to be formatted
	 * @return output which is the formatted string
	 */
	public static String formatColumns(String inputString){
        ArrayList<ColumnBlock> blocks = new ArrayList<ColumnBlock>();
        ColumnBlock current = new ColumnBlock(1);
        String[] lines = inputString.split("\\r?\\n");

        for(String line : lines){
            if(line.startsWith("-a2")){
                if(current.isSingleColumn()){
                    blocks.add(current);
                    current = new ColumnBlock(2);
                }
            }
            else if(line.startsWith("-a1")){
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

	/**
	 * This command is called by justification to left justify the text
	 * this is the default condition for the program
	 *
	 * @param input is the string to be formatted
	 * @param lineLength is the max length of the line
	 * @return output is the formatted string
	 */
	public String leftJust(String input, int lineLength) {		//this is the only method that assumes that input is longer than line length.
		//input = input.trim();
		String output = "";
		char[] temp = input.toCharArray();
		int i = 0;
		while( i < input.length()) {
  			output = output + temp[i];
  			i++;
  			if(i > lineLength) {
  				output = output + "\n";
  			}
  		}
	
		return output;
	}

	/**
	 * This command is called by justification to right justify the text
	 *
	 * @param input is the string to be formatted
	 * @param lineLength is the max length of the line
	 * @return output is the formatted string
	 */
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
		 

	/**
	 * This method is called by justification to center justify the text
	 *
	 * @param input is the string to be formatted
	 * @param lineLength is the max line length
	 * @return output is the formatted string
	 */
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

	/**
	 * This method is called by justification for equal justification
	 *
	 * @param input is the string to be formatted
	 * @param lineLength is the max line length
	 * @return output is the formatted string
	 */
	public String equalSpacing(String input, int lineLength) {
		String output = "";
		//input = input.trim();
		String[] words = input.split(" ");

		if(words.length == 1)
			return input;

		int spaceCount = words.length - 1;

		int freeSpaces = lineLength - input.replace(" ","").length();

		int dividedSpaces = freeSpaces / spaceCount;

		int remainder = freeSpaces % spaceCount;

		String space1 = "";

		String space2 = "";

		for(int i = 0; i < dividedSpaces; i++){
			space1 += " ";
			space2 += " ";
		}


		space2 += " ";


		for(int i = 0; i < words.length; i++){
			output += words[i];
			if(i < remainder){
				output += space2;
			}
			else{
				output += space1;
			}
		}

		return output.trim();
	}


	/**
	 * This is a helper method for doubleSpaces. It adds an extra line between every line
	 *
	 * @param input is the string to be formatted
	 * @return output is the formatted string
	 */
	private String doubleSpace(String input) {		//adds new line to the end output.
		//input = input.trim();
		String output = input + "\n";
		return output;
	}

	/**
	 *
	 * @param numLines
	 * @return
	 */
//	private String blankLines(int numLines) {
//		String output = "";
//		for(int i = 0; i < numLines; i++) {
//			output = output + "\n";
//		}
//
//		return output;
//	}

	/**
	 * This is the method which parses the program and justifies the text
	 * according to the commands. The default is left justified
	 *
	 * @param input is the string to be formatted
	 * @return output is the formatted string
	 */
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
			} else if(!line.startsWith("-") || line.contains("--")) {
				
				if(align == 0) {		//left justified
					output = output + leftJust(line, line.length()) + "\n";
					
				} else if(align == 1) {		//right justified
					output = output + rightJust(line, line.length()) + "\n";
					
				} else if(align == 2) {		//center align
					output = output + centerJust(line, line.length()) + "\n";
					
				} else if(align == 3) {		//equal spacing
					output = output + equalSpacing(line, line.length()) + "\n";
				}				
			} else if(line.startsWith("-")) {
				output = output + line + "\n";
			}
			
		}
		while(output.length() > 0 && output.charAt(output.length() - 1) == '\n') output = output.substring(0, output.length() - 1);
		return output;
	}

	/**
	 * This method adds extra spaces before the start of a line to indent it
	 * it is called by using the -p# command where # is the number of spaces the user
	 * wants before the start of the line
	 *
	 * @param input is the string to be formatted
	 * @return output is the formatted string
	 */
	public String indentation(String input) {
        String[] lines = input.split("\\r?\\n");
        String output = "";
        int indentLen = 0;

        for(String line: lines) {
            if(line.startsWith("-p")){
                indentLen = Integer.parseInt(line.trim().substring(2));
            }
			else if(line.startsWith(" ")){
				output += line + "\n";
			}
            else if(indentLen > 0){
                String out = "";
                while(indentLen > 0){
                    indentLen--;
                    out = " " + out;
                }
                String[] words = line.split(" ");
                int index = 0;
                int currentSize = out.length();

                while (index < words.length) {
                    if (words[index].equals("")) {
                        index++;
                    } else if (words[index].length() + currentSize <= max && currentSize == 0) {
                        out += words[index];
                        currentSize += words[index].length();
                        index++;

                    } else if (words[index].length() + currentSize + 1 <= max) {
                        out += " " + words[index];
                        currentSize += words[index].length() + 1;
                        index++;
                    } else {
                        if (words[index].length() > max && currentSize == 0) {
                            out += words[index].substring(0, max);
                            words[index] = words[index].substring(max);
                        }
                        currentSize = 0;

                        int startOfCurrentLine = 0 > out.lastIndexOf("\n") ? 0 : out.lastIndexOf("\n") + 1;
                        while (out.substring(startOfCurrentLine).length() < max) out += " ";

                        out += "\n";
                    }
                }
                if (out.length() > 0 && out.charAt(out.length() - 1) != '\n') {
                    int startOfCurrentLine = 0 > out.lastIndexOf("\n") ? 0 : out.lastIndexOf("\n") + 1;
                    while (out.substring(startOfCurrentLine).length() < max) out += " ";
                    out += "\n";
                }
                output += out;
            }
			else{
				output += line + "\n";
			}
        }
        return output;
	}

	/**
	 * This method is called for double spacing the text. It uses the private method
	 * doubleSpace for formatting the input.
	 * It is called by the -d command
	 *
	 * @param input is the string to be formatted
	 * @return output is the formatted string
	 */
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
			} else if(line.startsWith("-")) {
				output = output + line + "\n";
			}
		}
		while(output.length() > 0 && output.charAt(output.length() - 1) == '\n') output = output.substring(0, output.length() - 1);
		return output;
	}

	/**
	 * This method add blocks of blank lines between 2 lines. It is called by the -b# command
	 * where # is the number of blank lines to be inserted
	 *
	 * @param input is the string to be formatted
	 * @return output is the formatted string
	 */
	public static String blankSpaces(String input) {
		String out = "";
		for(String line : input.split("\\r?\\n")){
			if(line.startsWith("-b"))
			{
				for(int i = 0; i < Integer.parseInt(line.trim().substring(2)); i++)
				{
					out += "\n";
				}
			} else if(line.startsWith("-")) {
				out = out + line;
			}
			else{
				out += line + "\n";
			}
		}

		String output = out;
		
		while(output.length() > 0 && output.charAt(output.length() - 1) == '\n') output = output.substring(0, output.length() - 1);
		
		return output;
	}	
}

/**
 * This class is used for formatting the text to make columns. It is used by the formatColumns method
 */
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


