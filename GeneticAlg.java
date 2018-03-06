import java.io.*;
import java.util.*;

public class GeneticAlg {

    int pop_size;
    int generations;
    double pm;
    double px;
    int amount;


    short[][] distance_matrix;
    short[][] flow_matrix;

    public GeneticAlg(int pop_size, int generations, double pm, double px, int amount) {
        this.pop_size = pop_size;
        this.generations = generations;
        this.pm = pm;
        this.px = px;
        this.amount = amount;

        fillFlowMx("had12_flow.txt");
        fillDstMx("had12_dst.txt");

    }


    public void fillFlowMx(String filename) {

        flow_matrix = new MatrixLoad().readMatrix(filename);
//           flow_matrix = new short[][]{
//                   {0, 1, 2, 2, 3, 4, 4, 5, 3, 5, 6, 7},
//                   {1, 0, 1, 1, 2, 3, 3, 4, 2, 4, 5, 6},7
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

//        distance_matrix = new short[][]{
//                {0,22,53,53},
//                {22,0,40,62},
//                {53,40,0,55},
//                {53,62,55,0}
//        };
    }

    public void fillDstMx(String filename) {
        distance_matrix = new MatrixLoad().readMatrix(filename);
//            distance_matrix = new short[][]
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

//        flow_matrix = new short[][]{
//                {0,3,0,2},
//                {3,0,0,1},
//                {0,0,0,4},
//                {2,1,4,0}
//        };
    }


    public Population generateFirstPopulation(int pop_size) {
        Population start_pop = new Population(pop_size);

        for (int j = 0; j < pop_size; j++) {
            ArrayList<Short> list = new ArrayList<>(amount);
            for (short i = 0; i < amount; i++) {
                list.add(i);
            }
            Collections.shuffle(list);

            short[] new_genes = new short[amount];
            for (int k = 0; k < amount; k++) {
                new_genes[k] = list.get(k);
            }
            start_pop.addIndividual(new Individual(new_genes));
        }

        return start_pop;

    }


    public int countFitnessFunction(Individual individual) { // ?????
        int fitness_val = 0;

        for (int i = 0; i < amount; i++) {
            for (int j = 0; j < amount; j++) {
                if (i != j) { // opt czy potrzbne?
                    //System.out.println(i+ " "+ j  );
                    fitness_val += distance_matrix[individual.getGene(i)][individual.getGene(j)] * flow_matrix[i][j];
                }
            }
        }
        return fitness_val;
    }


    public Individual crossover(Individual individual1, Individual individual2) { // tak to losowe dziecko?
        Individual child_individual = new Individual(amount);

        if (new Random().nextDouble() <= 0.5) { //random child

            for (int i = 0; i < amount; i += 2) {
                child_individual.setGene(i, individual1.getGene(i));
            }
            for (int i = 1; i < amount; i += 2) {
                child_individual.setGene(i, individual2.getGene(i));
            }
        } else {

            for (int i = 0; i < amount; i += 2) {
                child_individual.setGene(i, individual2.getGene(i));
            }
            for (int i = 1; i < amount; i += 2) {
                child_individual.setGene(i, individual1.getGene(i));
            }

        }

        return fixIndividual(child_individual); // return fixed
    }


    public Individual mutate(Individual individual) { //todo better
        Random random = new Random();
        double mutation_nuber;

        for (int i = 0; i < individual.getGenes().length; i++) {
            mutation_nuber = random.nextDouble();
            if (mutation_nuber <= pm) { // swap
                int swap_place = random.nextInt(individual.getGenes().length - 1);
                // printArray(individual);

                //swapping:
                short tmp = individual.getGene(i);
                individual.setGene(i, individual.getGene(swap_place));
                individual.setGene(swap_place, tmp);

                //printArray(individual);
            }
        }
        return individual;
        //return fixIndividual(individual); // jak bez swapa tylko inaczej mutacja
    }


    public Individual fixIndividual(Individual individual) {
        // printArray(individual);


        ArrayList<Short> lack_genes = new ArrayList<>();
        for (Short s = 0; s < individual.getGenes().length; s++) { //adds all genes to array
            lack_genes.add(s);
        }

        for (int i = 0; i < individual.getGenes().length; i++) { //removes genes that are in individual
            Short s = individual.getGene(i);
            if (lack_genes.contains(s)) {
                lack_genes.remove(s);
            }
        }

        for (int i = 0; i < individual.getGenes().length; i++) {  // if a gene is repeated, replaces it with a gene from lac_genes array
            for (int j = 0; j < individual.getGenes().length; j++) {
                if (i != j && individual.getGene(i) == individual.getGene(j)) {
                    Short tmp = lack_genes.get(lack_genes.size() - 1);
                    individual.setGene(j, tmp);
                    lack_genes.remove(tmp);
                }
            }
        }

        //printArray(individual);
        return individual;
    }

    public Individual selectByTournament(Population pop, int tournament_size) {  //todo : dopracowac dla ts=0
        Population tournament_pop = new Population(tournament_size);
        Random random = new Random();


        int i = 0;
        do {
            int position = random.nextInt(tournament_size);   // moze wybrac te same... przekazywac kopie pop i usuwac tutaj z niej wybrane?
            tournament_pop.addIndividual(pop.getIndividuals().get(position));

            //System .out.println(i + " " + position );

            i += 1;
        }
        while (i <= tournament_size);

        Individual best_individual = tournament_pop.getIndividuals().get(0);
        int best_fitness = countFitnessFunction(best_individual);

        for (Individual ind : tournament_pop.getIndividuals()) { //bzsnsu bo 2 razy liczy dla 0
            int fitness = countFitnessFunction(ind);

            // printArray(ind.getGenes());
            //System.out.println(fitness);

            if (fitness < best_fitness) {
                best_fitness = fitness;
                best_individual = ind;
            }
        }
        return best_individual;
    }

    public Individual selectByRoulette(Population pop) {
        Random random = new Random();
        int total_fitness = 0;

        for (Individual ind : pop.getIndividuals()) {
            total_fitness += countFitnessFunction(ind);
        }
        int value = random.nextInt(total_fitness);
        // locate the random value based on the weights
        for (int i = 0; i < pop.getIndividuals().size(); i++) {
            value -= countFitnessFunction(pop.getIndividuals().get(i));
            if (value < 0) return pop.getIndividuals().get(i);
        }
        // when rounding errors occur, we return the last item's index
        return pop.getIndividuals().get(pop.getIndividuals().size() - 1);
    }

    public Individual selectByRoulette2(Population pop) {
        Random random = new Random();
        int total_fitness = 0;

        for (Individual ind : pop.getIndividuals()) {
            total_fitness += countFitnessFunction(ind);
        }

        int randomNumber = random.nextInt(total_fitness + 1);
        int runningSum = 0;
        int index = 0;
        while (runningSum < randomNumber) {
            runningSum += countFitnessFunction(pop.getIndividuals().get(index));
            index++;
        }

        return pop.getIndividuals().get(index - 1);
    }

    public void printArray(short[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ", ");
        }
        System.out.println();
    }

    public void printMx(short[][] matrix) {
        for (int i = 0; i < amount; i++) {
            for (int j = 0; j < amount; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

    }


    public void runAlgorithmWithTournament(int tournament_size) { //params
        Population new_population = generateFirstPopulation(pop_size);
        Population old_population;

        Random random = new Random();

        try (FileWriter writer = new FileWriter("Results_tournament.csv")) {


            for (int generation_nr = 0; generation_nr < generations; generation_nr++) {//DLA KAZDEJ POPULACJI
                //best worse avg

                populationResults(new_population, writer);

                old_population = new_population;
                new_population = new Population(pop_size);


                for (int ind_nr = 0; ind_nr < pop_size; ind_nr++) {//tyle ile ma byc osobnikow w nowej

                    Individual current_ind = selectByTournament(old_population, tournament_size);

                    double cross = random.nextDouble(); //random nr that determines wheather individual will be crossed

                    if (cross <= px) {//crossover   //todo:słabo bo może wybrac siebie do krzyzowania
                        Individual partner = old_population.getIndividuals().get(random.nextInt(pop_size - 1)); //random partner for crossover
                        Individual child = crossover(current_ind, partner);
                        new_population.addIndividual(child);
                    }//if(cross<=px)
                    else {//no crossover
                        Individual ind_to_new_pop = mutate(current_ind.clone());//MUTOWAĆ KOPIĘ!!!
                        new_population.addIndividual(ind_to_new_pop);
                    }// else if(cross<=px){

                }//for(int ind_nr=0; ind_nr <pop_size;i++){


            }//for(generation_nr...)

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runAlgorithmWithRoulette() { //params
        Population new_population = generateFirstPopulation(pop_size);
        Population old_population;

        Random random = new Random();

        try (FileWriter writer = new FileWriter("Results_roulette.csv")) {


            for (int generation_nr = 0; generation_nr < generations; generation_nr++) {//DLA KAZDEJ POPULACJI

                populationResults(new_population, writer);

                old_population = new_population;
                new_population = new Population(pop_size);


                for (int ind_nr = 0; ind_nr < pop_size; ind_nr++) {//tyle ile ma byc osobnikow w nowej

                    Individual current_ind = selectByRoulette(old_population);

                    double cross = random.nextDouble(); //random nr that determines wheather individual will be crossed

                    if (cross <= px) {//crossover   //todo:słabo bo może wybrac siebie do krzyzowania
                        Individual partner = old_population.getIndividuals().get(random.nextInt(pop_size - 1)); //random partner for crossover
                        Individual child = crossover(current_ind, partner);
                        new_population.addIndividual(child);
                    }//if(cross<=px)
                    else {//no crossover
                        Individual ind_to_new_pop = mutate(current_ind.clone());//MUTOWAĆ KOPIĘ!!!
                        new_population.addIndividual(ind_to_new_pop);
                    }// else if(cross<=px){

                }//for(int ind_nr=0; ind_nr <pop_size;i++){


            }//for(generation_nr...)

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void populationResults(Population population, FileWriter writer) throws IOException { //0?  //todo: writing to csv file
        int best_value = countFitnessFunction(population.getIndividuals().get(0));
        int worst_value = countFitnessFunction(population.getIndividuals().get(0));
        int avg_value = 0;
        int fitness_sum = 0;


        Individual bi = population.getIndividuals().get(0);

        for (Individual ind : population.getIndividuals()) {
            int fitness_val = countFitnessFunction(ind);

            if (fitness_val < best_value) {
                best_value = fitness_val;
                bi = ind;
            }

            if (fitness_val > worst_value) {
                worst_value = fitness_val;
            }

            fitness_sum += fitness_val;
        }

        avg_value = fitness_sum / pop_size;


        System.out.println("best:" + best_value + " worst:" + worst_value + " avg:" + avg_value);

        //String csvFile = "/Users/mkyong/csv/abc.csv";

        try {
            CSVUtils.writeLine(writer, Arrays.asList(String.valueOf(worst_value), String.valueOf(avg_value), String.valueOf(best_value)));
        } catch (IOException e) {
            e.printStackTrace();
        }


        printArray(bi.getGenes());
    }


//
//    public void readMatrixes(String filename) {
//
//        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(filename)))) {
//            amount = scanner.nextShort();
//            System.out.println(amount);
//            ArrayList<Short> elems = new ArrayList<>();
//
//            while (scanner.hasNextShort()) {
//
//                //System.out.println(scanner.nextShort());
//                elems.add(scanner.nextShort());
//
//            }
//            System.out.println(elems.size());
////            System.out.println(elems.toString());
//
//            //                // short[][] distance = new short[amount][amount];
////                for (int i = 0; i < amount; i++) {
////                    for (int j = 0; j < amount; j++) {
////                        flow_matrix[i][j] = elems.get(i+j);
////                    }
////                }
//
//            for (int i = 0; i < amount; i++){
//                flow_matrix[i/amount][i%amount] = elems.get(i);
//            }
//
//                //flow_matrix = d1istance;
//
////                //short[][] flow = new short[amount][amount];
////                for (int i = 0; i < amount; i++) {
////                    for (int j = 0; j < amount; j++) {
////                        distance_matrix[i][j] = elems.get(amount+i+j);
////                    }
//               // }
//        } catch (FileNotFoundException e) {
//        }
//    }


    public static void main(String[] args) {
//        GeneticAlg ga = new GeneticAlg();
//        ga.fillDstMx();
//        ga.fillFlowMx();
//
//        for(Individual ind : ga.generateFirstPopulation(10).getIndividuals()){
//            ga.printArray(ind.getGenes());
//        }


        // ga.fixIndividual(new short[]{1, 2, 0, 3});



    }
}

