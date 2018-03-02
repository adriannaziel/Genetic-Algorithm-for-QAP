import java.util.ArrayList;

public class Population {

    private ArrayList<Individual> individuals ;
    int pop_size;

    public Population(int size){
        pop_size = size;
        individuals = new ArrayList<>(pop_size);
    }

    public ArrayList<Individual> getIndividuals() {
        return individuals;
    }

    public void addIndividual(Individual individual){
        individuals.add(individual);
    }

}
