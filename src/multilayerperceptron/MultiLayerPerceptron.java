package multilayerperceptron;

import java.util.Random;

/**
 *
 * @author Rafael Del Lama
 */
public class MultiLayerPerceptron {
    private double learnRate = 0.1;
    private Layer layers[];
    private int inputSize; // número de neurônios de entrada

    public MultiLayerPerceptron(int inputSize, int layerSize[]) {
        this.inputSize = inputSize;
        
        layers = new Layer[layerSize.length];
        
        for(int i = 0; i < layers.length; i++){
            if(i == 0)
                layers[i] = new Layer(layerSize[i], inputSize);
            else
                layers[i] = new Layer(layerSize[i], layerSize[i-1]);
        }
    }//MultiLayerPerceptron

    /**
     * Responsável por gerar a saída da rede
     * @param in conjunto de dadps de entrada
     * @return cojunto contendo a saída da rede
     */
    public double[] execute (double in[]){
        double output[] = new double[layers[layers.length-1].layerSize];
        double sum;
        
        //Calcular a saída da primeira camada escondida
        for(int i = 0; i < layers[0].layerSize; i++){
            sum = layers[0].neurons[i].bias;
            for(int j = 0; j < inputSize; j++)
                sum += layers[0].neurons[i].weights[j] * in[j];
            layers[0].neurons[i].output = funcAtivacao(sum);
            layers[0].neurons[i].sumInput = sum;
        }
        
        //Calcula a propagação do sinal nas camadas escondidas
        for(int i = 1; i < layers.length; i++ ){ //camada
            for(int j = 0; j < layers[i].layerSize; j++){ // neuronios de entrada
                sum = layers[i].neurons[j].bias;
                for(int k = 0; k < layers[i - 1].layerSize; k++) //entradas do neuronio - saída da camada anterior
                    sum += layers[i].neurons[j].weights[k] * layers[i - 1].neurons[k].output;
                layers[i].neurons[j].output = funcAtivacao(sum);
                layers[i].neurons[j].sumInput = sum;
            }
        }
            
        //Calcula a saída
        for(int i = 0; i < layers[layers.length-1].layerSize; i++){
            output[i] = Math.round(layers[layers.length - 1].neurons[i].output);
        }
        return output;
    }//execute
    
     /**
     * Função logistica como função de ativação do neuronio
     * @param x valor de entrada para a função
     * @return saída da função
     */
    private double funcAtivacao (double x){
        return (1.0 /(1.0 + Math.exp(-x)));
    }// funcAtivacao
    
    /**
     * Função que calcula o valor da derivada da função de ativação
     * @param x valor da entrada da função
     * @return valor da derivada
     */
    private double derivateFuncAtivacao(double x){
        return Math.exp(-x) / Math.pow((1 + Math.exp(-x)),2);
    }
    
    /**
     * Função responsável pelo treinamento da rede
     * @param input conjunto de dados de entradas 
     * @param desiredOutput conjunto de saídas esperadas
     */
    public void learn(double input[][], double desiredOutput[][]) {
        double error = 1.0;
        int epoca = 0;
              
        while (error > 0.01) {
            int v[] = seqAleatoria(input.length);     
            error = 0;
            for (int i = 0; i < input.length; i++) {
                int seq = v[i];
                double in[] = new double[input[0].length];
                for (int j = 0; j < input[0].length; j++) {
                    in[j] = input[seq][j];
                }

                double desiredOut[] = new double[desiredOutput[0].length];
                for (int j = 0; j < desiredOutput[0].length; j++) {
                    desiredOut[j] = desiredOutput[seq][j];
                }

                double out[] = execute(in);
                error += backpropagation(out, desiredOut, in);
            }
            epoca++;
            System.out.println(epoca + " " + error);
        }
        System.out.println(epoca + " " + error);
    }//learn
    
     /**
     * Responsável por ajustar os pesos
     * @param out saída da rede
     * @param desiredOutput saída esperada
     */
    private double backpropagation(double out[], double desiredOutput[], double input[]){
        double error;
        double y; 

        //Gradiente do erro da saída
        for(int i = 0; i < layers[layers.length-1].layerSize; i++){
            error = desiredOutput[i] - out[i];
            y = layers[layers.length - 1].neurons[i].sumInput;
            layers[layers.length-1].neurons[i].delta = error * derivateFuncAtivacao(y);
        }
        
        for(int i = layers.length - 2; i >= 0; i--){
            //Gradiente do erro da(s) camada(s) escondida(s)
            for(int j = 0; j < layers[i].layerSize; j++){
                error = 0.0;
                for(int k = 0; k < layers[i + 1].layerSize; k++)
                    error += layers[i + 1].neurons[k].delta * layers[i + 1].neurons[k].weights[j];
                y = layers[i].neurons[j].sumInput;
                layers[i].neurons[j].delta = derivateFuncAtivacao(y) * error;
            }
            
            //Corrige os pesos da camada oculta e da saída
            for(int j = 0; j < layers[i + 1].layerSize; j++) {
                for(int k = 0; k < layers[i].layerSize; k++){
                    layers[i + 1].neurons[j].weights[k] += learnRate * layers[i + 1].neurons[j].delta * 
                            layers[i].neurons[k].output;     
                }
                layers[i + 1].neurons[j].bias += learnRate * layers[i + 1].neurons[j].delta;
            }
        }
        //System.out.println();
        
        //Corrige os pesos da entrada
        for (int j = 0; j < layers[0].layerSize; j++) {
            for (int k = 0; k < inputSize; k++){
                layers[0].neurons[j].weights[k] += learnRate * layers[0].neurons[j].delta
                        * input[k];
            }
            layers[0].neurons[j].bias += learnRate * layers[0].neurons[j].delta;
        }
    
        error = 0.0;
        for(int i = 0; i < out.length; i++) 
            error += Math.abs(out[i] - desiredOutput[i]);

        error = error / out.length;
  
        return error;
    }//backpropagation
    
    public static int[] seqAleatoria(int n){
        int v[] = new int[n];
        for(int i = 0; i < v.length; i++)
            v[i] = i;
        
        Random rand = new Random();
        for(int i = 0; i < v.length; i++){
            int j = Math.abs(rand.nextInt() % v.length);
            int temp = v[i];
            v[i] = v[j];
            v[j] = temp;
        }
        return v;
    }//seqAleatoria
}// class MultiLayerPerceptron
