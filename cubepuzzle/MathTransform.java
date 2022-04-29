package cubepuzzle;

import java.util.ArrayList;
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
    public PairInt(int x, int y) {
        this.x = x;
        this.y = y;
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

// describe a surface
class Surface {
    ArrayList<TupleReal> points = new ArrayList<>();
    public Surface() {

    }
    void add(TupleReal np) {
        points.add(np);
    }
    TupleReal[] toArray() {
        TupleReal[] ans = new TupleReal[points.size()];
        for(int i = 0; i < points.size(); i ++) {
            ans[i] = points.get(i);
        }
        return ans;
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
    public static TupleReal[] rotate(TupleReal[] q, TupleReal p, TupleReal v, double theta) {
        TupleReal[] ansArr = new TupleReal[q.length];
        for(int i = 0; i < q.length; i ++) {
            ansArr[i] = rotate(q[i], p, v, theta);
        }
        return ansArr;
    }

    // solve the inersection point of a surface and a line
    // where (p, v) give the surface, (q, u) give the line
    public static TupleReal IntersectionSurfaceAndLine(TupleReal p, TupleReal v, TupleReal q, TupleReal u) {
        double project = u.dot(v);
        //if(Math.abs(project) < Constants.MATH_EPS) {
        //    throw new Exception("line and surface are parallel with each other");
        //}
        double t = p.sub(q).dot(v) / project;
        return q.add(u.mul(t));
    }

    // transform p3d list into p2d list
    public static PairInt[] getPairIntArrayForTupleRealArray(TupleReal[] p3d) {
        PairInt[] p2d = new PairInt[p3d.length];
        for(int i = 0; i < p3d.length; i ++) {
            try {
                p2d[i] = MathTransform.transform(p3d[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return p2d;
    }

    // watch line is the norm vec for the watch surface
    protected static TupleReal watchPoint = new TupleReal(10, 5, 10);
    protected static TupleReal watchLine  = new TupleReal(2, 1, 2);

    // get the center point on the watchSurface
    public static TupleReal getWatchSurfacePoint() {
        return watchPoint.sub(watchLine);
    }

    // get the Z axis for the watch surface
    public static TupleReal getWatchSurfaceZ() {
        return watchLine.unit();
    }

    // get the Y axis for the watch surface
    public static TupleReal getWatchSurfaceY() {
        double t = TupleReal.VECZ.dot(watchLine) / watchLine.dot(watchLine);
        return TupleReal.VECZ.sub(watchLine.mul(t)).unit();
    }

    // get the X axis for the watch surface
    public static TupleReal getWatchSurfaceX() {
        TupleReal axisY = getWatchSurfaceY();
        TupleReal axisZ = watchLine.unit();
        return axisY.cross(axisZ);
    }

    // get the midpoint for many points
    public static TupleReal getMidPoint(TupleReal[] pArr) {
        double x = 0, y = 0, z = 0;
        double sum = 0;
        for(int i = 0; i < pArr.length; i ++) {
            double rate = Math.random();
            sum += rate;
            x += pArr[i].x * rate;
            y += pArr[i].y * rate;
            z += pArr[i].z * rate;
        }
        x /= sum;
        y /= sum;
        z /= sum;
        if(sum == 0) return getMidPoint(pArr);
        else {
            return new TupleReal(x, y, z);
        }
    }

    public static TupleReal[] getMidPointList(TupleReal[] pArr, int cnt) {
        TupleReal[] midPointArr = new TupleReal[cnt];
        for(int i = 0; i < cnt; i ++) {
            midPointArr[i] = getMidPoint(pArr);
        }
        return midPointArr;
    }

    // check if Ar if between AB and AC
    public static boolean InAngle(TupleReal r, TupleReal A, TupleReal B, TupleReal C) {
        TupleReal axisX = B.sub(A);
        TupleReal axisY = C.sub(A);
        TupleReal posR  = r.sub(A);
        return axisX.cross(posR).dot(axisY.cross(posR)) <= 0;
    }

    // check if r is in trangle ABC
    public static boolean InTrangle(TupleReal r, TupleReal A, TupleReal B, TupleReal C) {
        return InAngle(r, A, B, C) && InAngle(r, B, A, C) && InAngle(r, C, B, A);
    }

    // check if the trangle ABC can hide point p
    public static boolean hidePoint(TupleReal p, TupleReal A, TupleReal B, TupleReal C) {
        TupleReal n = A.sub(C).cross(A.sub(B));
        // now (A, n) is the surface
        TupleReal u = watchPoint.sub(A);
        TupleReal r;
        try {
            r = IntersectionSurfaceAndLine(A, n, p, u);
        }
        catch(Exception exp) {
            r = p;
            exp.printStackTrace();
        }
        return r.sub(watchPoint).len() <= p.sub(watchPoint).len() && InTrangle(r, A, B, C);
    }

    // check if a surface can hide a point on the watchSurface
    public static boolean hidePoint(TupleReal p, TupleReal[] surface) {
        boolean ans = false;
        for(int i = 1; i + 1 < surface.length; i ++) {
            if(hidePoint(p, surface[0], surface[i], surface[i+1])) {
                ans = true;
                break;
            }
        }
        return ans;
    }

    public static boolean hidePoints(TupleReal[] p, TupleReal[] surface) {
        for(int i = 0; i < p.length; i ++) {
            if(hidePoint(p[i], surface)) return true;
        }
        return false;
    }

    // find which surface is going to show
    public static boolean[] checkHide(Surface[] surfaceArr) {
        boolean[] boolArr = new boolean[surfaceArr.length];
        for(int i = 0; i < surfaceArr.length; i ++) {
            boolArr[i] = false;
            for(int j = 0; j < surfaceArr.length; j ++) {
                if(i == j) continue; // i != j
                if(hidePoints(getMidPointList(surfaceArr[i].toArray(), MATH_RNDCNT), surfaceArr[j].toArray())) {
                    boolArr[i] = true;

                    //! debug
                    //System.out.println(j + " hide " + i);
                }
            }
        }
        System.out.println();
        return boolArr;
    }

    // get position on the watch screen
    // q is a point in the 3d space
    public static PairReal getPositionOnWatchScreen(TupleReal q) throws Exception {
        TupleReal u = watchPoint.sub(q);
        TupleReal wp = getWatchSurfacePoint(); // watch point
        TupleReal newPos = IntersectionSurfaceAndLine(wp, getWatchSurfaceZ(), q, u).sub(wp);
        return new PairReal(newPos.dot(getWatchSurfaceX()), newPos.dot(getWatchSurfaceY()));
    }

    //// transform a pos3d to pos2d
    // this is the older algorithm for point transfomation
    public static PairInt transformOld(TupleReal pos3d) {
        int LENGTH     = getBasicLength();
        int SUB_LENGTH = getSubBasicLength();
        PairReal VECTOR_Y = new PairReal(LENGTH, 0);
        PairReal VECTOR_Z = new PairReal(0, -LENGTH);
        PairReal VECTOR_X = new PairReal(-SUB_LENGTH, SUB_LENGTH);
        PairReal VECTOR_O = new PairReal(MATH_O_X, MATH_O_Y);
        return new PairInt(VECTOR_O.add(VECTOR_X.mul(pos3d.x).add(VECTOR_Y.mul(pos3d.y)).add(VECTOR_Z.mul(pos3d.z))));
    }

    // transform a pos3d to pos2d new
    public static PairInt transform(TupleReal pos3d) throws Exception {
        PairReal newPos = getPositionOnWatchScreen(pos3d).mul(getBasicLength());
        return new PairInt((int)(newPos.x + MATH_O_X + 0.5), (int)(MATH_O_Y - newPos.y + 0.5));
    }

    public static void main(String[] args) {
        //Scanner cin = new Scanner(System.in);
        TupleReal A = new TupleReal(1, 0, 0);
        TupleReal B = new TupleReal(0, 1, 0);
        TupleReal C = new TupleReal(2, 2, 0);
        int cnt = 0;
        int MAXN = 10000000;
        for(int i = 0; i < MAXN; i ++) {
            TupleReal npoint = new TupleReal(Math.random() * 2, Math.random() * 2, 0);
            if(InTrangle(npoint, A, B, C)) {
                cnt ++;
            }
        }
        System.out.println("cnt: " + 1.0 * cnt / MAXN);
    }
}
