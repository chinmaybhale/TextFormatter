
package sample;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Formatter{
    protected int line_length = 80;
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
                        output = output+ " " + line;
                    }
                    else{
                        output = output + "\n" + line;
                    }
                }
                else{
                    if(wrap){
                        output = "\n" + line + "\n";
                    }
                    else{
                        output = "\n" + line;
                    }
                }

            }
        }
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
                titleLength = line.length();
                for(int i = 0; i < titleLength; i++){
                    underline += "-";
                }
                line = line + "\n" + underline;
                line = centerJust(line);
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
