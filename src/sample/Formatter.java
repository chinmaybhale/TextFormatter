
package sample;
import java.util.ArrayList;

class Formatter{
    private int line_length;
    private int align;  // left = 0, right, centered, equal
    private boolean wrap;
    private boolean double_spaced;
    private boolean title;  // switched off after first text line
    private int p; // = 0 after insert
    private int b; // = 0 after insert
    private boolean two_column;

    // sanitize: mark all commands causing errors and throws errors
    //           then delete error commands and return cleaned input
    public String sanitize(String inputStrings){
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
                        wrap = 1;
                        break;
                    case "-w-": // wrap off
                        wrap = 0;
                        break;
                    case "-s":  // single spaced
                        double_spaced = false;
                        break;
                    case "-d":  // double spaced
                        double_spaced = true;
                        break;
                    case "-t":  // title
                        //if title fits with line length
                        // HOW DO I CHECK THIS BC I HAVE TO
                        // LOOK AHEAD?? :((
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
                                if(two_column){
                                    try{
                                        line_length = Integer.parseInt(lines[i].substring(2));
                                    }
                                    catch (NumberFormatException e){
                                        //throw invalid line length error
                                    }
                                }
                                catch (Error e){
                                    //throw cannot change line length bc of column error
                                }
                                break;
                            case "-p": // insert spaces
                                if(align != 0) {
                                    try {
                                        Integer.parseInt(lines[i].substring(2))
                                    } catch (NumberFormatException e) {
                                        //throw invalid num option
                                    }
                                }
                                else{
                                    //throw can't insert space bc of alignment error
                                }
                                break;
                            case "-b": //insert blank lines
                                try {
                                    Integer.parseInt(lines[i].substring(2))
                                }
                                catch (NumerFormatException e){
                                    //throw invalid num option
                                }
                                break;
                            default:
                        }

                }
            }
        }
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
