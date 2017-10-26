package multilayerperceptron;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.jfr.events.FileReadEvent;

/**
 *
 * @author Rafael Del Lama
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        System.out.println("MultiLayerPerceptron");
        int layer[] = {4 ,3};
        MultiLayerPerceptron mlp = new MultiLayerPerceptron(4, layer);
              
        double in[][] = new double[150][4];
        double out[][] = new double[150][3];
       
        try {
            //Ler dados do arquivo
            FileReader dados = new FileReader("C:\\Users\\Rafael\\Documents\\dados_perceptron.txt");
            BufferedReader lerArq = new BufferedReader(dados);
            for(int i = 0; i < 150; i++){
                String linha[] = lerArq.readLine().split(" ");
                for(int j = 0; j < 7; j++){
                    if(j < 4)
                        in[i][j] = Double.valueOf(linha[j]);
                    else
                        out[i][j-4] = Integer.parseInt(linha[j]);
                }
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }        
        mlp.learn(in, out);      
    }
}