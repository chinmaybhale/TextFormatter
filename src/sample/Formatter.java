
package sample;
import java.lang.String;
import java.util.ArrayList;


class Formatter{
    protected int max = 80;
    protected int align = 0;  // left = 0, right, centered, equal
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


	public String doubleSpace(String input) {		//adds new line to the end output.
		//input = input.trim();
		String output = input + "\n";
		return output;
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
					output = output + equalSpacing(line, line.length()) + "\n";
				}
			} else if(line.startsWith("-")) {
				output = output + line + "\n";
			}
			
		}
		while(output.length() > 0 && output.charAt(output.length() - 1) == '\n') output = output.substring(0, output.length() - 1);
		return output;
	}

	public String indentation(String input) {
        String[] lines = input.split("\\r?\\n");
        String output = "";
        int indentLen = 0;

        for(String line: lines) {
            if(line.startsWith("-p")){
                indentLen = Integer.parseInt(line.trim().substring(2));
            }
            else {
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
        }
        return output;
//        String[] lines = input.split("\\r?\\n");
//        int length = 80, indentLen = 0;
//        String out = "";
//
//        for(String line: lines){
//            length = line.length();
//            if(line.startsWith("-p")){
//                indentLen = Integer.parseInt(line.trim().substring(2));
//            }
//            else if(line.trim().length() > 0){
//                if(indentLen > 0){
//                    for(int i = 0; i < indentLen && i < length; i++) {
//                        out += " ";
//                    }
//
//                    while(line.trim().length() > 0){
//                        int i = indentLen;
//                        if(line.split(" ")[0].length() > length){
//                            String temp = line.substring(0, line.length());
//                            out +=  temp + "\n";
//                            i = 0;
//                            line = line.substring(temp.length());
//                        }
//                        else if(line.indexOf(" ") < 0){
//                            out += line + "\n";
//                            line = "";
//                            i = 0;
//                        }else{
//                            out += line.substring(0, line.indexOf(" ")) + " ";
//                            i+= line.substring(0, line.indexOf(" ")).length();
//                            line = line.substring(line.indexOf(" ") + 1);
//                            if(line.indexOf(" ") > 0 && line.indexOf(" ") + i > length){
//                                while(out.length() > 0 && out.charAt(out.length() - 1) == ' ') out = out.substring(0, out.length() - 1);
//                                out += "\n";
//                                i = 0;
//                            }
//                        }
//                    }
//                    indentLen = 0;
//                }
//                else{
//                    out += line + "\n";
//                }
//            }
//        }
//        return out;
//    	boolean p = false;
//    	int length = 80;
//		String output = "";
//		String[] lines = input.split("\\r?\\n");
//		int line1 = 0;
//		int number = 0;
//		String[] words;
//		String firstLine = "";
//		String secondLine = "";
//
//		for(String line: lines) {
//			length = line.length();
//		    //if see -p command, store indentation number;
//			if(line.startsWith("-n")){
//                length = Integer.parseInt(line.trim().substring(2));
//            }
//            else if(line.startsWith("-p")) {
//				number = Integer.parseInt(line.trim().substring(2));
//				p = true;
//			}
//			else if (!line.startsWith("-")) {
//				if(p){
//				    p = false;
//					String indent = "";
//					for(int j = 0; j < number && j < length; j++)
//					{
//						indent += " ";
//					}
//
//					output += indent;
//					words = line.trim().split(" ");
//
//					int currentSize = indent.length();
//					int max = length;
//
//					for(int index = 0; index < words.length; index = index) {
//						if (words[index].length() + currentSize <= max && (currentSize == 0 || index == 0)) {
//							output += words[index];
//							currentSize += words[index].length();
//							index++;
//
//						} else if (words[index].length() + currentSize + 1 <= max) {
//							output += " " + words[index];
//							currentSize += words[index].length() + 1;
//							index++;
//						} else {
//							if (words[index].length() > max && currentSize == 0) {
//								output += words[index].substring(0, max);
//								words[index] = words[index].substring(max);
//							}
//							currentSize = 0;
//
//							int startOfCurrentLine = 0 > output.lastIndexOf("\n") ? 0 : output.lastIndexOf("\n") + 1;
//							while (output.substring(startOfCurrentLine).length() < max) output += " ";
//
//							output += "\n";
//						}
//					}
//
////					words = line.trim().split(" ");
////					line1 = number;
////					//adds indentations
////					for(int j = 1; j < number; j++){
////						firstLine += " ";
////					}
////					//make sure not longer than line length
////					for(int i = 0; i < words.length; i++){
////						if((firstLine.length() + words[i].length()) <= length) {
////							firstLine = firstLine + " " + words[i];
////						}
////						else{
////								secondLine = " " + words[i];
////						}
////					}
////					line = firstLine;
////					while(line.length() < length) line += " ";
////					firstLine = "";
////					if(secondLine.trim().length() != 0){
////							line = line + "\n" + secondLine.trim();
////                    }
////					secondLine = "";
////					output = output + line + " ";
////					p = false;
////				} else {
////					words = line.trim().split(" ");
////					//make sure not longer than line length
////					for(int i = 0; i < words.length; i++){
////						int len = 0;
////						if(len + words[i].length())
////						if((firstLine.length() + words[i].length()) <= max) {
////							firstLine = firstLine + words[i] + " ";
////						}
////						else{
////							if(!words[i].equals("\n"))
////								secondLine = words[i] + " ";
////						}
////					}
//				}
//                else {
//                    output = output + line + "\n";
//                }
//			}
//            else {
//                output = output + line + "\n";
//            }
//		}
//        if(output.length() > 0 && output.charAt(output.length() - 1) != '\n') output += "\n";
//
//		lines = output.split("\\r?\\n");
//		output = "";
//		for(int i = 0; i < lines.length; i++)
//		{
//			while(lines[i].length() < length) lines[i] += " ";
//			output += lines[i] + "\n";
//		}
//		while(output.length() > 0 && output.charAt(output.length() - 1) == '\n') output = output.substring(0, output.length() - 1);
//		return output;
	}
	
	public String doubleSpaces(String input){
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


