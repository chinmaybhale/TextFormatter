package sample;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//class marks all lines with errors, throws errors, and deletes error lines at end to return clean input
public class Sanitize extends Formatter {
    private int line_length = max;

    private ArrayList<Integer> errorLines = new ArrayList<Integer>();

    public String sanitize(String inputStrings){
        boolean titleFound = false; //for title error
        int j;  //for title error

        String[] lines = inputStrings.split("\\r?\\n");
        for(int i = 0; i < lines.length; i++) {
            if(lines[i].startsWith("-")){
                switch(lines[i].trim()){
                    case "-l":  // left-justified
                        align = 0;
                        break;
                    case "-r":  // right-justified
                        align = 1;
                        break;
                    case "-c":  // centered
                        align = 2;
                        break;
                    case "-e":  // equally spaced
                        align = 3;
                        break;
                    case "-w+": // wrap on
                        wrap = true;
                        break;
                    case "-w-": // wrap off
                        wrap = false;
                        break;
                    case "-s":  // single spaced
                        double_spaced = false;
                        break;
                    case "-d":  // double spaced
                        double_spaced = true;
                        break;
                    case "-t":  // title
                        j = i;
                        titleFound = false;
                        while(titleFound == false){
                            if(!lines[j].startsWith("-")) {
                                titleFound = true;
                                if (lines[j].trim().length() > line_length) {
                                    //throw title length overflow error
                                    Errors.TitleLengthOverflowError("Title is longer than line length.", i+1);
                                    errorLines.add(i);
                                }
                            }
                            j++;
                        }
                        break;
                    case "-a1":  // 1 column
                        two_column = false;
                        break;
                    case "-a2": // 2 columns
                        two_column = true;
                        break;
                    default:
                        //check if -n, -p, or -b w/ valid command
                        switch(lines[i].substring(0,2)){
                            case "-n":
                                try{
                                    line_length = Integer.parseInt(lines[i].substring(2));
                                    if(two_column){
                                        //cannot change line_length bc two col is on
                                        Errors.InvalidCommandError("Cannot change line length because two column is switched on.",i+1);
                                        errorLines.add(i);
                                    }
                                }
                                catch (NumberFormatException e){
                                    //throw invalid line length error
                                    Errors.InvalidCommandError("Invalid number following -n.", i+1);
                                    errorLines.add(i);
                                }
                            break;
                            case "-p": // insert spaces
                                for(int k = i + 1; k < lines.length; k++)
                                    sanitize(lines[k]);
                                if(align == 0) {
                                    try {
                                        Integer.parseInt(lines[i].substring(2));
                                    } catch (NumberFormatException e) {
                                        //throw invalid num option
                                        Errors.InvalidCommandError("Invalid number following -p.", i+1);
                                        errorLines.add(i);
                                    }
                                }
                                else{
                                    //throw can't insert space bc of alignment error
                                    Errors.InvalidCommandError("Cannot insert space with -r, -c, or -e on.", i+1);
                                    errorLines.add(i);
                                }
                                break;
                            case "-b": //insert blank lines
                                try {
                                    Integer.parseInt(lines[i].substring(2));
                                }
                                catch (NumberFormatException e){
                                    //throw invalid num option
                                    Errors.InvalidCommandError("Invalid number following -b.", i+1);
                                    errorLines.add(i);
                                }
                                break;
                            default:
                                Errors.UnrecognizedCommandError("Unrecognized Command.", i+1);
                                errorLines.add(i);
                        }
                }
            }
        }
        List<String> inputLines = new ArrayList<>(Arrays.asList(lines));
        for(int k = inputLines.size()-1; k > -1; k--) {
            if (errorLines.contains(k)){
                inputLines.remove(k);
            }
        }
        return String.join("\n",inputLines);
    }
}
