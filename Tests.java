import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Tests {


    public void runNtimes(int n, int popsize, int gens, double pm, double px, int amount, int ts) throws IOException {

        List<ArrayList<Tuple<Integer,Integer,Integer>>> list = new ArrayList<ArrayList<Tuple<Integer, Integer, Integer>>>();

        double start = System.currentTimeMillis();
        for(int i=0;i<n;i++){
            GeneticAlg ga = new GeneticAlg(popsize,gens,pm,px,amount);
            list.add(ga.runAlgorithmWithTournamentTuples(ts));
        }
        double end = System.currentTimeMillis();

        double time = (end-start)/n;
        System.out.println("czas sredni: " + time );

        ArrayList<Tuple<Integer,Integer,Integer>> avgs = new ArrayList<Tuple<Integer, Integer, Integer>>();
            for(int i =0 ; i< list.get(0).size(); i++){
                int best=0;
                int avg=0;
                int worst=0;

                for(int j=0; j<n; j++){
                    Tuple<Integer,Integer,Integer> tuple =  list.get(j).get(i);
                    best+=tuple.x;
                    avg += tuple.y;
                    worst += tuple.z;
                }


                avgs.add(new Tuple(best/n,avg/n,worst/n));

            }








            //.........................................

        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new FileWriter("Results_tournament_tuple.csv"));
            writer.write("Najlepszy, Sredni, Najgorszy");
            writer.newLine();
        }
        catch (IOException e){}

        for(int i=0; i<avgs.size(); i++){
            Tuple<Integer,Integer,Integer> tuple = avgs.get(i);
            try {
                writer.write(tuple.x + "," + tuple.y + "," + tuple.z);
                writer.newLine();

            }
            catch (IOException e){}
        }
        try{
            if (writer != null) writer.close();
        }
        catch (IOException ex) {}
    }


    public static void main(String[] args) throws IOException {
//        GeneticAlg ga = new GeneticAlg(100,1000,0.04, 0.7,12);
//
//
//        System.out.println(ga.runAlgorithmWithTournamentTuples(60).get(999).x);
        // ga.runAlgorithmWithRoulette();

        Tests t = new Tests();
        try (FileWriter writer = new FileWriter("Results_tournament.csv")) {
            t.runNtimes(10,200,500,0.01, 0.7,16,200);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
