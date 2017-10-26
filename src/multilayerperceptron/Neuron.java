package multilayerperceptron;

/**
 *
 * @author Rafael Del Lama
 */
public class Neuron {
    protected double bias;
    protected double weights[];
    protected double sumInput;
    protected double output;
    protected double delta;

    /**
     * 
     * @param inputSize quantidade de entradas 
     */
    public Neuron(int inputSize) {
        weights = new double[inputSize];  
        
        bias = Math.random() - 0.5;
       
        for(int i = 0; i < inputSize; i++)
            weights[i] = Math.random() - 0.5;   
    }//Neuron   
}// class Neuron