import java.util.Scanner;

import tools.Reader;
import tridiagonalMatrix.Matrix;
import tridiagonalMatrix.ThomasAlgorithm;

public class TridiagonalMatrix {

	public static void main(String[] args) {
		
		System.out.println("Enter file name which contains tridiagonal matrix");
		System.out.print("File name: ");
		
		Scanner s = new Scanner(System.in);

		Reader fileReader = new Reader();
		if (fileReader.read(s.next())) {
			System.out.println();
			
			Matrix matrix = new Matrix();
			matrix.fillData(fileReader.getLine(1), fileReader.getLine(2), fileReader.getLine(3));
			
			if (matrix.validate()) {
				System.out.println("Matrix from the file");
				matrix.print();
				
				ThomasAlgorithm method = new ThomasAlgorithm(matrix, fileReader.getLine(4));
				
				if (method.isSolvable()) {
					method.calculate();
				} else {
					System.out.println("The equation is not solvable with Thomas algorithm");
				}
			} else {
				System.out.println("Matrix was not filled corectly");
			}	
		}
	}
}
