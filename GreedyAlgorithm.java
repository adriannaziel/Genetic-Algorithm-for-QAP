import java.util.ArrayList;

public class GreedyAlgorithm {



    public short[] greedyAlg ( int size, short[][]flow_matrix, short [][] distance_matrix){

        short[] result = new short[size];
        ArrayList<Short> already_chosen = new ArrayList<Short>(size);

        short loc1=0;
        short loc2=0;

        int best_cost_initial = Integer.MAX_VALUE;
        int current_cost_initial =0;

        for (short i=0; i<size; i++){
            for(short j=0; j<size;j++){
                if(i!=j) {
                    current_cost_initial = flow_matrix[0][1] * distance_matrix[i][j];
                    if(current_cost_initial<best_cost_initial){
                        loc1=i;
                        loc2=j;
                    }
                }
            }
        }
        System.out.println(loc1 +  " " + loc2);

        result[0]=loc1;
        result[1]=loc2;

        already_chosen.add(loc1);
        already_chosen.add(loc2);


        for(int i =2; i<size; i++){

            int best_cost = Integer.MAX_VALUE;
            int curr_cost;
            for(short j=0; j<size; j++){
                if(!already_chosen.contains(j)) {

                    curr_cost = (flow_matrix[i - 1][i]) *( distance_matrix[result[i-1]][j]);

                    if (curr_cost < best_cost) {
                        best_cost = curr_cost;
                        result[i]=j;
                    }

                }// if(!already_chosen.contains(j)) {

            }//  for(int j=0; j<size; j++)
            already_chosen.add(result[i]);

        }

        return result;
    }

    public void printArray(short[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ", ");
        }
        System.out.println();
    }


    private int countFitnessFunction(short[] individual, short[][] distance_matrix, short[][] flow_matrix) {
        int fitness_val = 0;


        for (int i = 0; i < individual.length; i++) {
            for (int j = 0; j < individual.length; j++) {
                if (i != j) {
                    fitness_val += distance_matrix[individual[i]][individual[j]] * flow_matrix[i][j];
                }
            }
        }
        return fitness_val;
    }

    public static void main(String[] args){
        GreedyAlgorithm ga = new GreedyAlgorithm();
        short[][] flow = new MatrixLoad().readMatrix("had20_flow.txt");
        short[][] dst = new MatrixLoad().readMatrix("had20_dst.txt");
        short[] res1 = ga.greedyAlg(20, flow, dst);
        ga.printArray( res1);
        System.out.println(ga.countFitnessFunction(res1, flow,dst));

    }


}


