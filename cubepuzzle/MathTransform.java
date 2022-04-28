package cubepuzzle;

import java.util.Scanner;

// PairInt defines the pixel position on the screen (unfixed)
class PairReal {
    final double x, y;
    public PairReal(double nx, double ny) {
        x = nx;
        y = ny;
    }
    PairReal add(PairReal rhs) {
        return new PairReal(x + rhs.x, y + rhs.y);
    }
    PairReal rev() {
        return new PairReal(-x, -y);
    }
    PairReal mul(double len) {
        return new PairReal(x*len, y*len);
    }
}

class PairInt {
    final int x, y;
    public PairInt(PairReal pairReal) {
        x = (int)(pairReal.x + 0.5);
        y = (int)(pairReal.y + 0.5);
    }
}

// Tuple Real is the abstract 3D grid position
//! this will be a 3D engine made by myself one day
class TupleReal {
    final double x, y, z;

    // input from Scanner
    static TupleReal input(Scanner cin) {
        double x, y, z;
        x = cin.nextDouble();
        y = cin.nextDouble();
        z = cin.nextDouble();
        return new TupleReal(x, y, z);
    }

    // output to stdout
    void output() {
        System.out.println("x: " + x + ", y: " + y + ", z:" + z);
    }
    void output(String name) {
        System.out.print(name + ":\t");
        System.out.println("x: " + x + ", y: " + y + ", z:" + z);
    }

    // point constants which are often used
    public static final TupleReal ZERO = new TupleReal(0, 0, 0);
    public static final TupleReal VECX = new TupleReal(1, 0, 0);
    public static final TupleReal VECY = new TupleReal(0, 1, 0);
    public static final TupleReal VECZ = new TupleReal(0, 0, 1);

    TupleReal(double nx, double ny, double nz) {
        x = nx;
        y = ny;
        z = nz;
    }
    TupleReal setx(double nx) {
        return new TupleReal(nx, y, z);
    }
    TupleReal sety(double ny) {
        return new TupleReal(x, ny, z);
    }
    TupleReal setz(double nz) {
        return new TupleReal(x, y, nz);
    }

    // the following of this are the vector calculations
    TupleReal add(TupleReal rhs) {
        return new TupleReal(x + rhs.x, y + rhs.y, z + rhs.z);
    }
    TupleReal sub(TupleReal rhs) {
        return new TupleReal(x - rhs.x, y - rhs.y, z - rhs.z);
    }
    double len() {
        return Math.sqrt(x*x + y*y + z*z);
    }
    double dot(TupleReal rhs) {
        return x*rhs.x + y*rhs.y + z*rhs.z;
    }
    TupleReal mul(double p) {
        return new TupleReal(x*p, y*p, z*p);
    }
    TupleReal unit() { // get a unit vector with the sme direction
        if(this.len() == 0) {
            throw new ArithmeticException("div 0");
        }
        return this.mul(1.0 / this.len());
    }
    TupleReal cross(TupleReal rhs) {
        return new TupleReal(y*rhs.z - z*rhs.y, z*rhs.x - x*rhs.z, x*rhs.y - y*rhs.x);
    }
}

public class MathTransform implements Constants{
    static int lenX = 5, lenY = 5;

    // set the length of a game
    static void setLength(int lenX, int lenY) {
        MathTransform.lenX = lenX;
        MathTransform.lenY = lenY;
    }

    // basicLength is the count of pixel for len 1
    static int getBasicLength() {
        int len = lenX > lenY ? lenX : lenY;
        return (int)(1.0 * MATH_MAX_WIDTH / len + 0.5);
    }

    // oblique secondary auxiliary drawing (side edge)
    static int getSubBasicLength() {
        return (int)(getBasicLength() * Math.sqrt(0.5) / 2 + 0.5);
    }

    // rotate a point by theta rounding a poll
    static TupleReal rotate(TupleReal q, TupleReal p, TupleReal v, double theta) {
        // q is the point to rotate
        // p, v is the poll line
        double t = v.dot(q.sub(p)) / (v.len() * v.len());
        TupleReal u = p.add(v.mul(t));
        TupleReal z = v.unit();
        if(q.sub(u).len() < Constants.MATH_EPS) { // q is on the line of p, v
            return q;
        }
        TupleReal x = q.sub(u).unit();
        TupleReal y = z.cross(x);
        TupleReal n = x.mul(Math.cos(theta)).add(y.mul(Math.sin(theta)));
        return u.add(n.mul(q.sub(u).len()));
    }

    // rotate all the node in a cube
    static TupleReal[] rotate(TupleReal[] q, TupleReal p, TupleReal v, double theta) {
        TupleReal[] ansArr = new TupleReal[q.length];
        for(int i = 0; i < q.length; i ++) {
            ansArr[i] = rotate(q[i], p, v, theta);
        }
        return ansArr;
    }

    // ture p3d list into p2d list
    public static PairInt[] getPairIntArrayForTupleRealArray(TupleReal[] p3d) {
        PairInt[] p2d = new PairInt[p3d.length];
        for(int i = 0; i < p3d.length; i ++) {
            p2d[i] = MathTransform.transform(p3d[i]);
        }
        return p2d;
    }

    // transform a pos3d to pos2d
    static PairInt transform(TupleReal pos3d) {
        int LENGTH     = getBasicLength();
        int SUB_LENGTH = getSubBasicLength();
        PairReal VECTOR_Y = new PairReal(LENGTH, 0);
        PairReal VECTOR_Z = new PairReal(0, -LENGTH);
        PairReal VECTOR_X = new PairReal(-SUB_LENGTH, SUB_LENGTH);
        PairReal VECTOR_O = new PairReal(MATH_O_X, MATH_O_Y);
        return new PairInt(VECTOR_O.add(VECTOR_X.mul(pos3d.x).add(VECTOR_Y.mul(pos3d.y)).add(VECTOR_Z.mul(pos3d.z))));
    }

    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        TupleReal q = TupleReal.input(cin);
        TupleReal p = TupleReal.input(cin);
        TupleReal v = TupleReal.input(cin);
        rotate(q, p, v, Math.PI * 2 / 3).output();
    }
}
