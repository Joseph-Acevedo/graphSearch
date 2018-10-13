package mathUtilities;

import java.awt.Point;

public class MathUtilities 
{
	
	
	/**
	 * Multiples 2 2-dimensional matrices together given their dimensions are compatible
	 * @param mat_a A 2-Dimensional matrix to be multiplied
	 * @param mat_b A 2-Dimensional matrix to be multiplied
	 * @return		The product of the two matrices if compatible dimensions, null otherwise
	 */
	public static double[][] matrixMultiply2D(double[][] mat_a, double[][] mat_b)
	{
		int aRows = mat_a.length;
        int aColumns = mat_a[0].length;
        int bRows = mat_b.length;
        int bColumns = mat_b[0].length;

        if (aColumns != bRows)
            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");

        double[][] mat_result = new double[aRows][bColumns];
        for (int i = 0; i < aRows; i++) {
        	for (int j = 0; j < bColumns; j++) {
        		mat_result[i][j] = 0.00000;
        	}
        }

        for (int i = 0; i < aRows; i++) { // aRow
        	for (int j = 0; j < bColumns; j++) { // bColumn
        		for (int k = 0; k < aColumns; k++) { // aColumn
        			mat_result[i][j] += mat_a[i][k] * mat_b[k][j];
                }
            }
        }
        return mat_result;
	}

	
	/**
	 * 
	 * @param mat The matrix to transpose
	 * @return	  The transposed matrix
	 */
	public static double[][] matrixTranspose2D(double[][] mat)
	{
		double[][] result = new double[mat[0].length][mat.length];
		
		for(int x = 0; x < mat.length; x++)
		{
			for(int y = 0; y < mat[0].length; y++)
			{
				result[y][x] = mat[x][y];
			}
		}
		return result;
	}
	
	
	/**
	 * 
	 * @param a Point a
	 * @param b Point b
	 * @return  The absolute value of the euclidean distance between point a and b
	 */
	public static double euclideanDistance(Point a, Point b)
	{
		return Math.abs(Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2)));
	}
	
	
	public static void main(String[] args)
	{
		double[][] mat_a = {{1,2},{3,4},{5,6}};
		double[][] mat_b = {{7,8,9},{10,11,12}};
		double[][] mat_c = matrixTranspose2D(mat_a);
		
		for(int x = 0; x < mat_c.length; x++)
		{
			for(int y = 0; y < mat_c[0].length; y++)
			{
				System.out.print(mat_c[x][y] + " ");
			}
			System.out.println();
		}
	}

}
