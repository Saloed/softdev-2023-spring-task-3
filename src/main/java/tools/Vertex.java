package tools;


import com.mokiat.data.front.parser.OBJTexCoord;
import com.mokiat.data.front.parser.OBJVertex;

public class Vertex extends OBJVertex {
    double x; double y; double z;

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }

    public double getY() {
        return y;
    }

    public Vertex() {}
    public Vertex(OBJTexCoord texCoord){
        this.x = texCoord.u;
        this.y = texCoord.v;
        this.z = texCoord.w;
    }
    public Vertex(OBJVertex vertex){
        this.x = vertex.x;
        this.y = vertex.y;
        this.z = vertex.z;
    }

    public Vertex(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vertex(Matrix m){
        if (m.rows > 3 || m.colomns > 1) {
            throw new IllegalArgumentException("неккоректая матрица");
        } else {
            this.x = m.getValue(0, 0);
            this.y = m.getValue(1, 0);
            this.z = m.getValue(2, 0);
        }
    }

    public Matrix getMatrix(){
        Matrix res = new Matrix(3, 1);
        res.addValue(0, 0, this.x);
        res.addValue(1, 0, this.y);
        res.addValue(2, 0, this.z);
        return res;
    }


    public Vertex transformationXY(Vertex oldVer, double angle){
        double ang = Math.toRadians(angle);
        double sinus = Math.sin(ang);
        double cosines = Math.cos(ang);
        double[][] array = {{cosines, -sinus, 0.0}, {sinus, cosines, 0.0}, {0.0, 0.0, 1.0}};
        Matrix transformation = new Matrix(array);
        Matrix res = oldVer.getMatrix().multiply(transformation);
        return new Vertex(res);

    }
    public Vertex transformationXZ(Vertex oldVer, double angle){
        double ang = Math.toRadians(angle);
        double sinus = Math.sin(ang);
        double cosines = Math.cos(ang);
        double[][] array = {{cosines, 0, -sinus}, {0, 1.0, 0}, {sinus, 0, cosines}};
        Matrix transformation = new Matrix(array);
        Matrix res = oldVer.getMatrix().multiply(transformation);
        return new Vertex(res);
    }

    public Vertex transformationYZ(Vertex oldVer, double angle){
        double ang = Math.toRadians(angle);
        double sinus = Math.sin(ang);
        double cosines = Math.cos(ang);
        double[][] array = {{1, 0, 0}, {0, cosines, sinus}, {0, -sinus, cosines}};
        Matrix transformation = new Matrix(array);
        Matrix res = oldVer.getMatrix().multiply(transformation);
        return new Vertex(res);
    }

    public static Vertex transform(Vertex old, double a, double b, double c){
        double sinA = Math.sin(Math.toRadians(a));
        double sinB = Math.sin(Math.toRadians(b));
        double sinC = Math.sin(Math.toRadians(c));
        double cosA = Math.cos(Math.toRadians(a));
        double cosB = Math.cos(Math.toRadians(b));
        double cosC = Math.cos(Math.toRadians(c));
        double[][] array = {{cosB*cosC, -sinC*cosB, sinB},
                {sinA*sinB*cosC + sinC * cosA, -sinA*sinB*sinC + cosA * cosC, -sinA*cosB},
                {sinA*sinC - sinB*cosA*cosC, sinA*cosC + sinB *sinC*cosA, cosA*cosB}};
        Matrix matrix = new Matrix(array);
        Matrix res = matrix.multiply(old.getMatrix());
        Vertex result = new Vertex(res);
        return result;
    }

    public static Vertex transformOBJ(OBJVertex old, double a, double b, double c){
        Vertex oldVer = new Vertex(old);
        double sinA = Math.sin(Math.toRadians(a));
        double sinB = Math.sin(Math.toRadians(b));
        double sinC = Math.sin(Math.toRadians(c));
        double cosA = Math.cos(Math.toRadians(a));
        double cosB = Math.cos(Math.toRadians(b));
        double cosC = Math.cos(Math.toRadians(c));
        double[][] array = {{cosB*cosC, -sinC*cosB, sinB},
                {sinA*sinB*cosC + sinC * cosA, -sinA*sinB*sinC + cosA * cosC, -sinA*cosB},
                {sinA*sinC - sinB*cosA*cosC, sinA*cosC + sinB *sinC*cosA, cosA*cosB}};
        Matrix matrix = new Matrix(array);
        Matrix res = matrix.multiply(oldVer.getMatrix());
        Vertex result = new Vertex(res);
        return result;
    }
    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Vertex vertex = (Vertex) o;
        return this.x == vertex.x && this.y == vertex.y && this.z == vertex.z;
    }
    @Override
    public int hashCode() {
        return (int) (31 * x + 31* y + 31 * z);
    }
}
