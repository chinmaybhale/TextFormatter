
package sample;

import java.util.ArrayList;

class Formatter{

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
