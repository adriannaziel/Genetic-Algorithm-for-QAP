import java.util.ArrayList;
import java.util.Random;

public class GreedyAlgorithm {

    public Individual greedyAlg ( int size, short[][]flow_matrix, short [][] distance_matrix){
        Individual result_individual = new Individual(size);
        ArrayList<Short> already_chosen = new ArrayList<Short>(size);
        Random random = new Random();

        short random_localization = (short)random.nextInt(size);
        result_individual.setGene(0, random_localization);
        already_chosen.add(random_localization);

        for(int i =1; i<size; i++){


            int best_cost = Integer.MAX_VALUE;
            int curr_cost;
            for(short j=0; j<size; j++){
                if(!already_chosen.contains(j)) {

                    curr_cost = (flow_matrix[i - 1][i]) *( distance_matrix[result_individual.getGene(i-1)][j]);

                   // System.out.println(i + " " + j + " " +best_cost+ " "  + curr_cost  );
                    if (curr_cost < best_cost) {
                        best_cost = curr_cost;
                        result_individual.setGene(i,j);
                    }

                }// if(!already_chosen.contains(j)) {

            }//  for(int j=0; j<size; j++)
            already_chosen.add(result_individual.getGene(i));

        }
       // printArray(result.getGenes());
       // System.out.println(countFitnessFunction(result));

        return result_individual;
    }
}
