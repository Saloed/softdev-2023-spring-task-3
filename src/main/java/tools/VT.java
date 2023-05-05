package tools;

public class VT {
    double u;
    double v;
    public VT(){};
    public VT(double u, double v){
        this.u = u;
        this.v = v;
    }
    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }
        if (o == null || o.getClass() != getClass()){
            return false;
        }
        VT vt = (VT) o;
        return this.v == vt.v && this.u == vt.u;
    }
}
