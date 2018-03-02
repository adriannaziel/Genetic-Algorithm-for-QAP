import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GeneticAlg {
    int amount = 4 ;
    int pop_size = 100;
    double pm = 0.01;
    double px = 0.7;
    int generations = 10;

    short[][] distance_matrix;
    short[][] flow_matrix;


    ////////////////////

    short[] testo = {2,9,10,1,11,4,5,6,7,0,3,8};
    /////////////////


    public void fillDstMx(){
//           distance_matrix = new short[][]{
//                   {0, 1, 2, 2, 3, 4, 4, 5, 3, 5, 6, 7},
//                   {1, 0, 1, 1, 2, 3, 3, 4, 2, 4, 5, 6},
//                   {2, 1, 0, 2, 1, 2, 2, 3, 1, 3, 4, 5},
//                   {2, 1, 2, 0, 1, 2, 2, 3, 3, 3, 4, 5},
//                   {3, 2, 1, 1, 0, 1, 1, 2, 2, 2, 3, 4},
//                   {4, 3, 2, 2, 1, 0, 2, 3, 3, 1, 2, 3},
//                   {4, 3, 2, 2, 1, 2, 0, 1, 3, 1, 2, 3},
//                   {5, 4, 3, 3, 2, 3, 1, 0, 4, 2, 1, 2},
//                   {3, 2, 1, 3, 2, 3, 3, 4, 0, 4, 5, 6},
//                   {5, 4, 3, 3, 2, 1, 1, 2, 4, 0, 1, 2},
//                   {6, 5, 4, 4, 3, 2, 2, 1, 5, 1, 0, 1},
//                   {7, 6, 5, 5, 4, 3, 3, 2, 6, 2, 1, 0}
//           };

        distance_matrix = new short[][]{
                {0,22,53,53},
                {22,0,40,62},
                {53,40,0,55},
                {53,62,55,0}
        };
    }

    public void fillFlowMx(){
//            flow_matrix = new short[][]
//                    {
//                            {0, 3, 4, 6, 8, 5, 6, 6, 5, 1, 4, 6},
//                            {3, 0, 6, 3, 7, 9, 9, 2, 2, 7, 4, 7},
//                            {4, 6, 0, 2, 6, 4, 4, 4, 2, 6, 3, 6},
//                            {6, 3, 2, 0, 5, 5, 3, 3, 9, 4, 3, 6},
//                            {8, 7, 6, 5, 0, 4, 3, 4, 5, 7, 6, 7},
//                            {5, 9, 4, 5, 4, 0, 8, 5, 5, 5, 7, 5},
//                            {6, 9, 4, 3, 3, 8, 0, 6, 8, 4, 6, 7},
//                            {6, 2, 4, 3, 4, 5, 6, 0, 1, 5, 5, 3},
//                            {5, 2, 2, 9, 5, 5, 8, 1, 0, 4, 5, 2},
//                            {1, 7, 6, 4, 7, 5, 4, 5, 4, 0, 7, 7},
//                            {4, 4, 3, 3, 6, 7, 6, 5, 5, 7, 0, 9},
//                            {6, 7, 6, 6, 7, 5, 7, 3, 2, 7, 9, 0}
//                    };

        flow_matrix = new short[][]{
                {0,3,0,2},
                {3,0,0,1},
                {0,0,0,4},
                {2,1,4,0}
        };
    }




//        public ArrayList<short[]> generateFirstPopulation(int pop_size){
//            ArrayList<short[]> start_pop = new ArrayList<>(pop_size);
//
//            for(int j=0; j<pop_size; j++){ //przeniesc do metody?
//                ArrayList<Short> list = new ArrayList<>(amount);
//                for (short i = 0; i < amount; i++) {
//                    list.add(i);
//                }
//                Collections.shuffle(list);
//
//                short[] individual = new short[amount];
//                for(int k =0; k<amount;k++){
//                    individual[k]=list.get(k);
//                }
//                start_pop.add(individual);
//
//                printArray(individual);//tst
//            }
//
//            return start_pop;
//        }

    public Population generateFirstPopulation(int pop_size){
        Population start_pop = new Population(pop_size);

        for(int j=0; j<pop_size; j++){ //przeniesc do metody?
            ArrayList<Short> list = new ArrayList<>(amount);
            for (short i = 0; i < amount; i++) {
                list.add(i);
            }
            Collections.shuffle(list);

            short[] new_genes = new short[amount];
            for(int k =0; k<amount;k++){
                new_genes[k]=list.get(k);
            }
            start_pop.addIndividual(new Individual(new_genes));

            //printArray(individual);//tst
        }

        return start_pop;
    }

//        public int countFitnessFunction(short[] individual){ // ?????
////            int fitness_val = 0;
////
////            for (int i=0; i < amount; i++){
////                for(int j=0; j<amount; j++){
////                    if(i!=j) { // opt czy potrzbne?
////                        //System.out.println(i+ " "+ j  );
////                        fitness_val += distance_matrix[individual[i]][individual[j]] * flow_matrix[i][j];
////                    }
////                }
////            }
////            return fitness_val;
////        }

    public int countFitnessFunction(Individual individual){ // ?????
        int fitness_val = 0;

        for (int i=0; i < amount; i++){
            for(int j=0; j<amount; j++){
                if(i!=j) { // opt czy potrzbne?
                    //System.out.println(i+ " "+ j  );
                    fitness_val += distance_matrix[individual.getGene(i)][individual.getGene(j)] * flow_matrix[i][j];
                }
            }
        }
        return fitness_val;
    }

//         public short[] crossover(short [] individual1, short[] individual2){
//            short [] child_individual = new short[amount];
//
//            for(int i = 0 ; i<amount ; i+=2){
//                child_individual[i] = individual1[i];
//             }
//             for(int i = 1; i<amount ; i+=2){
//                 child_individual[i] = individual2[i];
//             }
//
//            return fixIndividual(child_individual); // return fixed
//         }


    public Individual crossover(Individual individual1, Individual individual2){ // tak to losowe dziecko?
        Individual child_individual = new Individual(amount);

        if(new Random().nextDouble()<=0.5) { //random child

            for (int i = 0; i < amount; i += 2) {
                child_individual.setGene(i, individual1.getGene(i));
            }
            for (int i = 1; i < amount; i += 2) {
                child_individual.setGene(i, individual2.getGene(i));
            }
        }

        else {

            for (int i = 0; i < amount; i += 2) {
                child_individual.setGene(i, individual1.getGene(i));
            }
            for (int i = 1; i < amount; i += 2) {
                child_individual.setGene(i, individual2.getGene(i));
            }

        }

        return fixIndividual(child_individual); // return fixed
    }


//         public  short[] mutate(short [] individual){ //todo better
//            Random random = new Random();
//            double mutation_nuber;
//
//             for(int i=0; i<individual.length; i++){
//                 mutation_nuber = random.nextDouble();
//                 if(mutation_nuber<=pm){ // swap
//                    int swap_place = random.nextInt(individual.length-1);
//                    // printArray(individual);
//
//                    //swapping:
//                     short tmp = individual[i];
//                     individual[i]=individual[swap_place];
//                     individual[swap_place] = tmp;
//
//                     //printArray(individual);
//                 }
//             }
//             return individual;
//             //return fixIndividual(individual); // jak bez swapa tylko inaczej mutacja
//         }


    public  Individual mutate(Individual individual){ //todo better
        Random random = new Random();
        double mutation_nuber;

        for(int i=0; i<individual.getGenes().length; i++){
            mutation_nuber = random.nextDouble();
            if(mutation_nuber<=pm){ // swap
                int swap_place = random.nextInt(individual.getGenes().length-1);
                // printArray(individual);

                //swapping:
                short tmp = individual.getGene(i);
                individual.setGene(i,individual.getGene(swap_place));
                individual.setGene(swap_place,  tmp);

                //printArray(individual);
            }
        }
        return individual;
        //return fixIndividual(individual); // jak bez swapa tylko inaczej mutacja
    }


//         public short[] fixIndividual(short[] individual) {
//             printArray(individual);
//
//
//             ArrayList<Short> lack_genes = new ArrayList<>();
//             for(Short s=0; s<individual.length; s++){ //adds all genes to array
//                 lack_genes.add(s);
//             }
//
//             for(int i=0; i<individual.length; i++){ //removes genes that are in individual
//                 Short s = individual[i];
//                 if(lack_genes.contains(s)){
//                     lack_genes.remove(s);
//                 }
//             }
//
//             for(int i=0; i<individual.length; i++){  // if a gene is repeated, replaces it with a gene from lac_genes array
//                 for(int j=0; j<individual.length; j++){
//                     if(i!=j && individual[i] == individual[j]){
//                         Short tmp =lack_genes.get(lack_genes.size()-1);
//                         individual[j]=tmp;
//                         lack_genes.remove(tmp);
//                     }
//                 }
//             }
//
//            printArray(individual);
//             return individual;
//        }


    public Individual fixIndividual(Individual individual) {
        // printArray(individual);


        ArrayList<Short> lack_genes = new ArrayList<>();
        for(Short s=0; s<individual.getGenes().length; s++){ //adds all genes to array
            lack_genes.add(s);
        }

        for(int i=0; i<individual.getGenes().length; i++){ //removes genes that are in individual
            Short s = individual.getGene(i);
            if(lack_genes.contains(s)){
                lack_genes.remove(s);
            }
        }

        for(int i=0; i<individual.getGenes().length; i++){  // if a gene is repeated, replaces it with a gene from lac_genes array
            for(int j=0; j<individual.getGenes().length; j++){
                if(i!=j && individual.getGene(i) == individual.getGene(j)){
                    Short tmp =lack_genes.get(lack_genes.size()-1);
                    individual.setGene(j,tmp);
                    lack_genes.remove(tmp);
                }
            }
        }

        //printArray(individual);
        return individual;
    }

//    public short[] selectByTournament(Population pop, int tournament_size){ //param? populacja?
//
//
//
//    }
//
//        public short[] selectByRoulette(Population pop){ //param? populacja?
//
//        }

    public void printArray(short[] arr){
        for (int i =0; i<arr.length;i++){
            System.out.print(arr[i] + ", ");
        }
        System.out.println();
    }
    public void printMx(short [][] matrix){
        for (int i =0 ; i< amount;i++){
            for(int j = 0; j<amount; j++){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

    }


    public static void main(String[] args) {
        GeneticAlg_1 ga = new GeneticAlg_1();
        ga.fillDstMx();
        ga.fillFlowMx();
//        ga.printMx(ga.distance_matrix);
//        ga.printMx(ga.flow_matrix);

        //System.out.println(ga.countFitnessFunction(new short[]{3, 0, 1, 2}));
        //ga.printArray(ga.crossover(new short[]{3, 1, 0, 2}, new short[]{1, 3, 1, 2}));  // działa

        // ga.fixIndividual(new short[]{1, 2, 0, 3});

    }
}

