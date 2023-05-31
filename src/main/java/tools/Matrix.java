package tools;

public class Matrix {


    int rows;
    int colomns;

    public double[][] m;

    public Matrix(){};

    //матрица из двумерного массива
    public Matrix(double[][] m) {
        this.rows = m.length;
        this.colomns = m[0].length;
        this.m = new double[rows][colomns];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < colomns; ++j) {
                this.m[i][j] = m[i][j];
            }
        }
    }

    public Matrix(int rows, int colomns) {
        this.rows = rows;
        this.colomns = colomns;
        this.m = new double[rows][colomns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colomns; j++) {
                m[i][j] = 0;
            }
        }
    }

    public double getValue(int i, int j) {
        return m[i][j];
    }

    public void addValue(int i, int j, double element) {
        m[i][j] += element;
    }

    public Matrix add(Matrix other) {
        if (!(this.rows == other.rows && this.colomns == other.colomns)) {
            throw new ArithmeticException("матрицы разных размеров");
        }
        Matrix res = new Matrix(this.rows, this.colomns);
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < other.colomns; ++j) {
                double sum = this.getValue(i, j) + other.getValue(i, j);
                res.addValue(i, j, sum);
            }
        }
        return res;
    }

    public Matrix multiply(Matrix other) {
        double[][] resM = new double[this.rows][other.colomns];
        if (this.colomns != other.rows) {
            throw new ArithmeticException("матрицы не возможно перемножить");
        }
        for (int i = 0; i < this.rows; ++i ) {
            for (int j = 0; j < other.colomns; ++j ) {
                resM[i][j] = 0;
                for (int k = 0; k < this.rows; ++k){
                    resM[i][j] += this.m[i][k] * other.m[k][j];
                }
            }
        }
        Matrix res = new Matrix(resM);
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (((Matrix) o).colomns != this.colomns || ((Matrix) o).rows != this.rows) {
            return false;
        }

        Matrix matrix = (Matrix) o;

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.colomns; j++) {
                if (this.m[i][j] != matrix.m[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}