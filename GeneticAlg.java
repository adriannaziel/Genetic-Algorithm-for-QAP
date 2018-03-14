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

        fillFlowMx("had"+amount+"_flow.txt");
        fillDstMx("had"+amount+"_dst.txt");

    }


    public void fillFlowMx(String filename) {
        flow_matrix = new MatrixLoad().readMatrix(filename);
    }

    public void fillDstMx(String filename) {
        distance_matrix = new MatrixLoad().readMatrix(filename);
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


    private int countFitnessFunction(Individual individual) {
        int fitness_val = 0;

        for (int i = 0; i < amount; i++) {
            for (int j = 0; j < amount; j++) {
                if (i != j) {
                    fitness_val += distance_matrix[individual.getGene(i)][individual.getGene(j)] * flow_matrix[i][j];
                }
            }
        }
        return fitness_val;
    }


    private Individual crossover(Individual individual1, Individual individual2) {
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
        return fixIndividual(child_individual);
    }


    private Individual mutate(Individual individual) { //todo better
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


    private Individual fixIndividual(Individual individual) {
        ArrayList<Short> lack_genes = new ArrayList<>();
        for (Short s = 0; s < individual.getGenes().length; s++) { //dodaj wszystkie geny
            lack_genes.add(s);
        }

        for (int i = 0; i < individual.getGenes().length; i++) { //rusuwa te co sa w osobniku
            Short s = individual.getGene(i);
            if (lack_genes.contains(s)) {
                lack_genes.remove(s);
            }
        }

        for (int i = 0; i < individual.getGenes().length; i++) {  // jak sie powtarza to bierze z tych ktorych nie bylo
            for (int j = 0; j < individual.getGenes().length; j++) {
                if (i != j && individual.getGene(i) == individual.getGene(j)) {
                    Short tmp = lack_genes.get(lack_genes.size() - 1);
                    individual.setGene(j, tmp);
                    lack_genes.remove(tmp);
                }
            }
        }
        return individual;
    }


    private Individual selectByTournament(Population pop, int tournament_size) {

        Population tournament_pop = new Population(tournament_size);
        Random random = new Random();
        int i = 0;

        while (i <= tournament_size){
            int position = random.nextInt(tournament_size);
            tournament_pop.addIndividual(pop.getIndividuals().get(position));
            i += 1;
        }


        Individual best_individual = tournament_pop.getIndividuals().get(0);
        int best_fitness = countFitnessFunction(best_individual);

        for (Individual ind : tournament_pop.getIndividuals()) {
            int fitness = countFitnessFunction(ind);

            if (fitness < best_fitness) {
                best_fitness = fitness;
                best_individual = ind;
            }
        }
        return best_individual;
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

                    if (cross <= px) {//crossover
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

    public ArrayList<Tuple<Integer,Integer,Integer>> runAlgorithmWithTournamentTuples(int tournament_size) { //params
        Population new_population = generateFirstPopulation(pop_size);
        Population old_population;

        Random random = new Random();

        ArrayList<Tuple<Integer,Integer,Integer>> results = new ArrayList<>(generations);

            for (int generation_nr = 0; generation_nr < generations; generation_nr++) {//DLA KAZDEJ POPULACJI
                //best worse avg

                results.add(populationResultsTuple(new_population));

                old_population = new_population;
                new_population = new Population(pop_size);


                for (int ind_nr = 0; ind_nr < pop_size; ind_nr++) {//tyle ile ma byc osobnikow w nowej

                    Individual current_ind = selectByTournament(old_population, tournament_size);

                    double cross = Math.random();//random.nextDouble(); //random nr that determines wheather individual will be crossed

                    if (cross <= px) {//crossover
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
            return results;

    }

    public void runAlgorithmWithRoulette() {
        Population new_population = generateFirstPopulation(pop_size);
        Population old_population;

        Random random = new Random();

        try (FileWriter writer = new FileWriter("Results_roulette.csv")) {


            for (int generation_nr = 0; generation_nr < generations; generation_nr++) {//DLA KAZDEJ POPULACJI

                populationResults(new_population, writer);

                old_population = new_population;
                new_population = new Population(pop_size);


                for (int ind_nr = 0; ind_nr < pop_size; ind_nr++) {//tyle ile ma byc osobnikow w nowej

                    Individual current_ind = rouletteSelect(old_population);//selectByRoulette(old_population);

                    double cross = random.nextDouble(); //czy bedzie krzyzowany

                    if (cross <= px) {//crossover
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

    private Individual rouletteSelect(Population pop ) {

        int total_fitness = 0;
        for(Individual ind : pop.getIndividuals()) {
            total_fitness += countFitnessFunction(ind);
        }

        ArrayList<Double> ind_fitness = new ArrayList<>();

        for(Individual ind : pop.getIndividuals()) {
            ind_fitness.add( (double)countFitnessFunction(ind)/total_fitness);
        }

        double rand_value =  new Random().nextDouble();
        int c=0;
        for(Individual ind : pop.getIndividuals()) {
            rand_value-= 1/countFitnessFunction(ind);
            if(rand_value<0) return ind;
        }
        return pop.getIndividuals().get(pop.pop_size-1);
    }


    private void populationResults(Population population, FileWriter writer) throws IOException {
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


       // System.out.println("best:" + best_value + " worst:" + worst_value + " avg:" + avg_value);

        try {
            CSVUtils.writeLine(writer, Arrays.asList(String.valueOf(worst_value), String.valueOf(avg_value), String.valueOf(best_value)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        printArray(bi.getGenes());
    }


    private Tuple<Integer,Integer,Integer> populationResultsTuple(Population population)  {
        int best_value = countFitnessFunction(population.getIndividuals().get(0));
        int worst_value = countFitnessFunction(population.getIndividuals().get(0));
        int avg_value = 0;
        int fitness_sum = 0;



        for (Individual ind : population.getIndividuals()) {
            int fitness_val = countFitnessFunction(ind);

            if (fitness_val < best_value) {
                best_value = fitness_val;
            }

            if (fitness_val > worst_value) {
                worst_value = fitness_val;
            }

            fitness_sum += fitness_val;
        }

        avg_value = fitness_sum / pop_size;


        //System.out.println("best:" + best_value + " worst:" + worst_value + " avg:" + avg_value);

        return new Tuple(best_value,avg_value,worst_value);
    }


    public static void main(String[] args) {
    }
}

