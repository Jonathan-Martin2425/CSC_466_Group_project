package CSC_466_Group_project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TitanicProject {
    public static ProjectMatrix titanicData;
    public static final ArrayList<Integer> omittedColumns = new ArrayList<>();
    public static void main(String[] args){

        // I recommend either setting up environments in Java, or creating a class
        // with the same name and putting the variable there.
        process(TempEnv.DATAPATH);

        System.out.println(titanicData);
    }

    public static void process(String filename){
        String curLine;
        String[] parsedNums;
        ArrayList<Double> curRow;

        titanicData = new ProjectMatrix();


        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

            // read 2nd line before looping
            reader.readLine();
            curLine = reader.readLine();

            // parses line into a double[] where each attribute goes in order as follows,
            // survived, class, sex, age(young, middle, old?), num_siblings, num_parents, fare(cost of ticket)

            // skips indexes 0, 3, 4, and 9 as they act as id's rather than information
            omittedColumns.add(0);
            omittedColumns.add(3);
            omittedColumns.add(4);
            omittedColumns.add(9);


            // all parsed as their given numbers, or converted into numbers, until we decided
            // to do it differently

            while (curLine != null) {
                parsedNums = curLine.split(",");

                curRow = new ArrayList<>();

                // only worried about 1st 10 columns
                for(int i = 0; i < 11; i++){
                    if(!omittedColumns.contains(i)){
                        if (i == 5){
                            // sex: male == 0, female == 1
                            if(parsedNums[i].equals("male")){
                                curRow.add(0.0);
                            }else{
                                curRow.add(1.0);
                            }
                        }else{
                            if (!parsedNums[i].isEmpty()){
                                curRow.add(Double.parseDouble(parsedNums[i]));
                            }else{
                                curRow.add(-1.0);
                            }
                        }
                    }

                }

                titanicData.addRow(curRow);

                curLine = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Bad file name error");
        }
    }
}
