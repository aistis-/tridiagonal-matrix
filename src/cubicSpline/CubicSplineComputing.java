package cubicSpline;

import java.util.Arrays;

import tridiagonalMatrix.Matrix;
import tridiagonalMatrix.TransposeMethod;

public class CubicSplineComputing {
	
	private float[] x = new float[0];
	private float[] y = new float[0];
	private float a;
	private float b;
	private int i;
	
	public CubicSplineComputing(String intervals, String xLine, String yLine) {
		String[] numbers = intervals.split(" ");
		
		this.a = Float.parseFloat(numbers[0]);
		this.b = Float.parseFloat(numbers[1]);
		this.i = Integer.parseInt(numbers[2]);

		for (int index = 0; index < numbers.length; index++) {
			this.x = this.addNumber(this.x, Float.parseFloat(numbers[index]));
		}
		
		numbers = yLine.split(" ");

		for (int index = 0; index < numbers.length; index++) {
			this.y = this.addNumber(this.y, Float.parseFloat(numbers[index]));
		}
	}
	
	public void compute() {
		float intervalDelta = (this.b - this.a) / this.i;
		
		float[] x = new float[this.i + 1];
		float[] y = new float[this.i + 1];
		float[] h = new float[this.i];
		
		/* Count x and y by i*/
		for (int i = 0; i <= this.i; i++) {
			x[i] = this.a + intervalDelta * i;
			y[i] = (float) this.aproximatingFunction(x[i]);
		}
		
		/* Count h by i*/
		for (int i = 0; i < this.i; i++) {
			h[i] = x[i + 1] - x[i];
		}
		
		/* Fill matrix with data */
		float[] diagonalTop = new float[this.i];
		float[] diagonalMiddle = new float[this.i + 1];
		float[] diagonalBottom = new float[this.i];
		
		for (int i = 0; i <= this.i - 1; i++) {
			if (i < this.i && i != 0) {
				diagonalTop[i] = h[i] / (h[i-1] + h[i]);
				diagonalBottom[i-1] = h[i-1] / (h[i-1] + h[i]);
			}
			
			diagonalMiddle[i] = 2;
		}
		
		diagonalMiddle[0] = 1;
		diagonalMiddle[this.i] = 1;
		
		diagonalTop[0] = 0;
		diagonalBottom[this.i - 1] = 0;
		
		Matrix matrix = new Matrix();
		matrix.setDiagonalTop(diagonalTop);
		matrix.setDiagonalMiddle(diagonalMiddle);
		matrix.setDiagonalBottom(diagonalBottom);
		
		/* Compute the matrix */
		float[] result = new float[this.i + 1];
		
		for (int i = 0; i < this.i; i++) {
			result[i] = (y[i + 1] - y[i]) / h[i];
		}
		
		result[0] = 0;
		result[this.i] = 0;
		
		TransposeMethod matrixComputing = new TransposeMethod(matrix, result);
		if (matrixComputing.isSolvable()) {
			matrixComputing.calculate();
			float[] g = matrixComputing.getUnknows();
			
			g[0] = 0;
			g[this.i] = 0;
			
			/* Compute e, G and H by i*/
			float[] e = new float[this.i];
			float[] G = new float[this.i];
			float[] H = new float[this.i];
			
			for (int i = 0; i < this.i; i++) {
				e[i] = (y[i + 1] - y[i]) / h[i] - g[i + 1] * h[i] / 6 - g[i] * h[i] / 3;
				
				G[i] = g[i] / 2;
				
				H[i] = (g[i + 1] - g[i]) / (6 * h[i]);
			}
			
			/* Print spline coefficients */
			System.out.println();
			
			for (int i = 0; i < this.i; i++) {
				System.out.print("S[" + (i + 1) + "] = ");
				System.out.print(H[i] + ", ");
				System.out.print(G[i] + ", ");
				System.out.print(e[i] + ", ");
				System.out.println(y[i]);
			}
			
		} else {
			System.out.println("Can't solve");
		}
	}

	private double aproximatingFunction(double x) {
		return Math.sqrt(x) * Math.cos(2 * x);
	}
	
	private float[] addNumber(float[] diagonal, float number) {
		float[] result = Arrays.copyOf(diagonal, diagonal.length + 1);
	    result[diagonal.length] = number;
	    return result;
	}
}
