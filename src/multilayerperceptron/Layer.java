package multilayerperceptron;

/**
 *
 * @author Rafael Del Lama
 */
public class Layer {
    protected Neuron neurons[];
    protected int layerSize;
    
    /**
     * @param layerSize quantidade de neuronios da camada
     * @param inputSize quantidade de entradas desta camada
     */
    public Layer(int layerSize, int inputSize){
        this.layerSize = layerSize;
        neurons = new Neuron[layerSize];
        
        for(int i = 0; i < layerSize; i++)
            neurons[i] = new Neuron(inputSize);
    }// Layer
}// class Layer
