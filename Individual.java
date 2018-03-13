public class Individual{

    private short[] genes;

    public Individual(short[] genes_array){
        genes = genes_array;
    }

    public Individual(int amount) {
        genes = new short[amount];
    }

    public void setGenes(short[] genes_array){
        genes = genes_array;
    }

    public short [] getGenes(){
       return genes;
    }

    public short getGene(int position){
        return genes[position];
    }

    public void setGene(int position, short value){
        genes[position] = value;
    }

    public Individual clone(){
        return  new Individual(this.genes.clone());
    }


}
