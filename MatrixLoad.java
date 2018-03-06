import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class MatrixLoad {

    public short [][] readMatrix(String filename) {

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(filename)))) {
            int matrix_size = scanner.nextShort();
            short[][] martix;
            martix = new short[matrix_size][matrix_size];
            System.out.println(matrix_size);
            while (scanner.hasNextShort()) {
                for (int i = 0; i < matrix_size; i++) {
                    for (int j = 0; j < matrix_size; j++) {
                        martix[i][j] = scanner.nextShort();
                    }
                }
            }
            return martix;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }


    public void printMx(short [][] matrix, int amount) {
        for (int i = 0; i < amount; i++) {
            for (int j = 0; j < amount; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

//    public static void main (String[] args){
//        int amount = 0;
//        MatrixLoad ml = new MatrixLoad();
//       ml.printMx( ml.readMatrix("macierz.txt"),12);
//
//    }
}
