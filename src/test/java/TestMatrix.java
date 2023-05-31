import org.junit.jupiter.api.*;
import tools.Matrix;

public class TestMatrix {

    @Test
    public void additiontest(){
        double [][] m = { {0.0, 1.0}, {2.0, 0.0} };
        Matrix a = new Matrix(m);
        double [][] n = {{4.0, 3.0}, {1.0, 5.0}};
        Matrix b = new Matrix(n);

        double [][] r = {{4.0, 4.0}, {3.0, 5.0}};
        Matrix res = new Matrix(r);

        Assertions.assertArrayEquals(a.add(b).m, r);
    }
    @Test
    public void multyplytest(){
        double [][] m1 = {{1}, {2}, {3}};
        Matrix a = new Matrix(m1);
        double [][] m2 = {{0, 1, 0}, {1, 0, 1}, {0, 1, 0}};
        Matrix b = new Matrix(m2);

        double[][] res = {{2}, {4}, {2}};

        Assertions.assertArrayEquals(b.multiply(a).m, res);
    }
}
