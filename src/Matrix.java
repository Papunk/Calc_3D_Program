import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.Arrays;

public class Matrix {

    private int height, width;
    protected double[][] matrix;

    // Constructors

    public Matrix(int height, int width) {
        this.height = height;
        this.width = width;
        this.matrix = new double[height][width];
        for (double[] row: this.matrix) {
            Arrays.fill(row, 0);
        }
    }

    public Matrix(double[][] matrix) {
        if (matrix.length > 0 && matrix[0].length > 0){
            this.height = matrix.length;
            this.width = matrix[0].length;
            this.matrix = matrix;
        }
        //TODO throw error
    }

    public Matrix(Vector[] vectorList) throws VectorTypeMismatch {
        boolean type = vectorList[0].isColumnVector();

        this.height = vectorList.length;
        this.width = vectorList[0].getDimension();

        this.matrix = new double[height][width];

        int i = 0;
        for (Vector v: vectorList) {
            if (v.isColumnVector() != type || v.getDimension() != width) { // if vectors in the list are of different type
                throw new VectorTypeMismatch();
            }
            matrix[i] = v.asList();
            i++;
        }
    }

    // Methods

    public Boolean isSquare() {
        return height == width;
    }

    public Boolean isDiagonal() {
        if (!this.isSquare()) // only a square matrix can be diagonal
            return false;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == j && matrix[i][j] == 0) { // values within the diagonal must not be zero
                    return false;
                }
                if (i != j && matrix[i][j] != 0){ // values outside the diagonal must be zero
                    return false;
                }
            }
        }
        return true;
    }

    public void multiplyByScalar(double scalar) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] *= scalar;
            }
        }
    }

    // Static Methods

    /**
     * calculates the transverse of the given matrix
     * @param A matrix to be transversed
     * @return the transverse matrix
     */
    public static Matrix transverse(Matrix A) {
        Matrix T = new Matrix(A.getHeight(), A.getWidth());
        for (int i = 0; i < T.getHeight(); i++) {
            for (int j = 0; j < T.getWidth(); j++) {
                T.setValueAt(i, j, A.matrix[j][i]);
            }
        }
        return T;
    }

    /**
     * @param m1 first matrix
     * @param m2 second matrix
     * @return a matrix containing the added elements of m1 and m2
     * @throws MatrixDimensionMismatch when the matrices are not of equaldimension
     */
    public static Matrix add(Matrix m1, Matrix m2) throws MatrixDimensionMismatch{
        if (Matrix.sameDimensions(m1, m2)) {
            Matrix m = new Matrix(m1.getHeight(), m1.getWidth());
            for (int i = 0; i < m.getHeight(); i++) {
                for (int j = 0; j < m.getWidth(); j++) {
                    m.setValueAt(i, j, m1.getValueAt(i, j) * m2.getValueAt(i, j));
                }
            }
            return m;
        } else {
            throw new MatrixDimensionMismatch("Matrix Addition");
        }
    }

//    public static Matrix multiply(Matrix m1, Matrix m2) {
//        if (m1.getWidth() == m2.getHeight()) {
//            Matrix m = new Matrix(m1.getHeight(), m2.getWidth());
//        }
//        return null;
//    }

    public static boolean sameDimensions(Matrix m1, Matrix m2) {
        return m1.getHeight() == m2.getHeight() && m1.getWidth() == m2.getWidth();
    }


    /**
     * @return the rows of the matrix as a list of row vectors
     */
    public Vector[] asVectorList() {
        Vector[] vectorList = new Vector[getHeight()];
        for (int i = 0; i < getHeight(); i++) {
            vectorList[i] = new Vector(matrix[i], false);
        }
        return vectorList;
    }

    // Setters and Getters

    public void setValueAt(int row, int col, double newValue) {
        matrix[row][col] = newValue;
    }

    public double getValueAt(int row, int col) {
        return matrix[row][col];
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    // Override methods

    @Override
    public String toString() {
        StringBuilder matrixString = new StringBuilder();
        for (double[] row: this.matrix) matrixString.append(Arrays.toString(row)).append("\n");
        return matrixString.toString();
    }


    // Internal classes

    public static class MatrixDimensionMismatch extends Exception {
        public MatrixDimensionMismatch(String operation) {
            super("Matrices are not of compatible size for this operation: " + operation);
        }
    }

    public static class VectorTypeMismatch extends Exception {
        public VectorTypeMismatch() {
            super("Cannot mix column and row vectors");
        }
    }
}
