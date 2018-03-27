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


    private Individual mutate(Individual individual) {
        Random random = new Random();
        double mutation_nuber;

        for (int i = 0; i < individual.getGenes().length; i++) {
            mutation_nuber = random.nextDouble();
            if (mutation_nuber <= pm) { // swap
                int swap_place = random.nextInt(individual.getGenes().length - 1);

                //swapping:
                short tmp = individual.getGene(i);
                individual.setGene(i, individual.getGene(swap_place));
                individual.setGene(swap_place, tmp);
                }
        }
        return individual;
    }


    private Individual fixIndividual(Individual individual) {
        ArrayList<Short> lack_genes = new ArrayList<>();
        for (Short s = 0; s < individual.getGenes().length; s++) { //dodaj wszystkie geny
            lack_genes.add(s);
        }

        for (int i = 0; i < individual.getGenes().length; i++) { //usuwa te co sa w osobniku
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
            int position = random.nextInt(pop_size);
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




    Individual selectByRoulette(Population population){

        double[] fitness_reversed_array = new double[pop_size];

        for (int i = 0; i < pop_size; i++){
            double cost = countFitnessFunction(population.getIndividuals().get(i));
            fitness_reversed_array[i] = 1/cost;
        }

        double sum = 0;

        for (int i=0; i < fitness_reversed_array.length; i++){
            sum += fitness_reversed_array[i];
        }

        double[] weights = new double [fitness_reversed_array.length];
        double last_weight = 0;

        for (int i=0; i < weights.length; i++){
            weights[i] = last_weight + fitness_reversed_array[i] / sum;
            last_weight = weights[i];
        }

        double random = Math.random();

        for(int i = 0; i < weights.length; i++) {
            if(random - weights[i] < 0){
                return population.getIndividuals().get(i);
            }
        }
        return population.getIndividuals().get(weights.length - 1);
    }




    public ArrayList<Tuple<Integer,Integer,Integer, Double>> runAlgorithmWithTournament(int tournament_size) {
        Population new_population = generateFirstPopulation(pop_size);
        Population old_population;

        Random random = new Random();

        ArrayList<Tuple<Integer,Integer,Integer, Double>> results = new ArrayList<>(generations);

            for (int generation_nr = 0; generation_nr < generations; generation_nr++) {

                results.add(populationResults(new_population));

                old_population = new_population;
                new_population = new Population(pop_size);


                for (int ind_nr = 0; ind_nr < pop_size; ind_nr++) {

                    Individual current_ind = selectByTournament(old_population, tournament_size);

                    double cross = Math.random();

                    if (cross <= px) {//crossover
                        Individual partner = old_population.getIndividuals().get(random.nextInt(pop_size - 1));
                        Individual child = crossover(current_ind, partner);
                        new_population.addIndividual(child);
                    }
                    else {//no crossover
                        Individual ind_to_new_pop = mutate(current_ind.clone());
                        new_population.addIndividual(ind_to_new_pop);
                    }
                }
            }
            return results;

    }



    public ArrayList<Tuple<Integer,Integer,Integer, Double>> runAlgorithmWithRoulette() {
        Population new_population = generateFirstPopulation(pop_size);
        Population old_population;

        Random random = new Random();

        ArrayList<Tuple<Integer,Integer,Integer, Double>> results = new ArrayList<>(generations);

        for (int generation_nr = 0; generation_nr < generations; generation_nr++) {

            results.add(populationResults(new_population));

            old_population = new_population;
            new_population = new Population(pop_size);


            for (int ind_nr = 0; ind_nr < pop_size; ind_nr++) {

                Individual current_ind = selectByRoulette(old_population);

                double cross = Math.random();

                if (cross <= px) {//crossover
                    Individual partner = old_population.getIndividuals().get(random.nextInt(pop_size - 1));
                    Individual child = crossover(current_ind, partner);
                    new_population.addIndividual(child);
                }
                else {//no crossover
                    Individual ind_to_new_pop = mutate(current_ind.clone());
                    new_population.addIndividual(ind_to_new_pop);
                }
            }
        }
        return results;

    }





    private Tuple<Integer,Integer,Integer, Double> populationResults(Population population)  {
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


        double standdev =0;
        for (Individual ind : population.getIndividuals()) {
            int fitness_val = countFitnessFunction(ind);
            standdev += (fitness_val-avg_value) * (fitness_val-avg_value) ;
        }

        standdev /= pop_size;
        standdev = Math.sqrt(standdev);

        return new Tuple(best_value,avg_value,worst_value, (standdev));
    }


}

