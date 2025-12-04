package CSC_466_Group_project;


import java.util.ArrayList;

public class ProjectMatrix {
    private ArrayList<ArrayList<Double>> twoD_list;
    public ProjectMatrix trainingSet;
    public ProjectMatrix testingSet;

    public ProjectMatrix(){
        twoD_list = new ArrayList<>();
    }

    public ProjectMatrix(double[][] twod_list){
        twoD_list = new ArrayList<>();

        // manually converts an int[][] to
        // the arraylist version by copying over
        // every value
        ArrayList<Double> listRow;
        for(double[] row : twod_list){
            listRow = new ArrayList<>();
            for(double val : row){
                listRow.add(val);
            }
            twoD_list.add(listRow);
        }
    }

    public void addRow(ArrayList<Double> row){
        if(twoD_list.isEmpty()){
            twoD_list.add(row);
        }else if(row.size() != getRowSize()){
            System.out.println("Row is not the correct size for the matrix");
        }else{
            twoD_list.add(row);
        }

    }

    public int getSize(){
        return twoD_list.size();
    }
    public int getRowSize(){
        return twoD_list.get(0).size();
    }
    public ArrayList<Double> getRow(int row){
        return twoD_list.get(row);
    }

    public double getVal(int row, int attribute){
        return twoD_list.get(row).get(attribute);
    }

    public double setVal(int row, int attribute, double val){
        return twoD_list.get(row).set(attribute, val);
    }

    //splits titanicData into training and testing sets
    // training = 80%, testing = 20%
    public void splitData(){

        int testSize, totalSize = getSize(), row;

        testSize = (int) Math.floor(0.8 * totalSize) + 1;

        trainingSet = new ProjectMatrix();
        testingSet = new ProjectMatrix();

        for(row = 0; row < testSize;row++){
            trainingSet.addRow(getRow(row));
        }

        for(row = testSize; row < totalSize; row++){
            testingSet.addRow(getRow(row));
        }

        System.out.printf("train size: %d\n", trainingSet.getSize());
        System.out.printf("test size: %d\n", testingSet.getSize());
        System.out.printf("total size: %d\n", getSize());
    }
    @Override
    public String toString(){
        return twoD_list.toString();
    }
}

