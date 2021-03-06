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

    public static TupleReal BASIC_WATCH_POINT = new TupleReal(10, 5, 10);
}

// describe a surface, (implemented by ArrayList of TupleReal)
class Surface {
    ArrayList<TupleReal> points = new ArrayList<>();
    public Surface() {

    }
    void add(TupleReal np) {
        points.add(np);
    }
    // write a toArray() function by myself
    TupleReal[] toArray() {
        TupleReal[] ans = new TupleReal[points.size()];
        for(int i = 0; i < points.size(); i ++) {
            ans[i] = points.get(i);
        }
        return ans;
    }
}

public class MathTransform implements Constants{
    //! size of the chessboard (lenX, lenY) will be set when game begins
    static int lenX = 5, lenY = 5; 

    // watch line is the norm vec for the watch surface
    protected static TupleReal watchPoint = TupleReal.BASIC_WATCH_POINT;

    public static TupleReal getMidPoint() {
        return new TupleReal(0.5 * lenX, 0.5 * lenY, 0);
    }

    public static TupleReal getWatchLine() {
        return getMidPoint().sub(watchPoint).unit().mul(-1);
    }

    // set the length of a game
    static void setLength(int lenX, int lenY) {
        MathTransform.lenX = lenX;
        MathTransform.lenY = lenY;

        //! need argument here
        watchPoint = new TupleReal(lenX + 1, 0.5 * lenY, 0.8 * Math.max(lenX, lenY) + 1);
        basicLength = -1;
    }

    // set the watch point
    static void setWatchPoint(TupleReal newWatchPoint) {
        watchPoint = newWatchPoint;
    }

    static void rotateWatchPoint(double theta) {
        watchPoint = rotate(watchPoint, getMidPoint(), TupleReal.VECZ, theta);
    }

    // basicLength is the count of pixel for len 1
    // use getBasicLength() to fit different chessboard size
    static int basicLength = -1;
    static int getBasicLength() {
        // basiclength = -1 means this is the first time to config this length
        if(basicLength == -1) {
            TupleReal[] corners = new TupleReal[4];
            for(int i = 0; i < 4; i ++) {
                boolean dx = ((i >> 1) & 1) != 0; 
                boolean dy = ((i >> 0) & 1) != 0;
                double x = dx ? lenX : 0;
                double y = dy ? lenY : 0;
                corners[i] = new TupleReal(x, y, 0);
            }
            PairReal[] p2d = new PairReal[4];
            for(int i = 0; i < 4; i ++) {
                try {
                    p2d[i] = getPositionOnWatchScreen(corners[i]);
                } catch (Exception e) {
                    p2d[i] = new PairReal(0, 0);
                    e.printStackTrace();
                }
            }
            double minX = p2d[0].x, minY = p2d[0].y, maxX = p2d[0].x, maxY = p2d[0].y;
            for(int i = 1; i < 4; i ++) {
                minX = Math.min(minX, p2d[i].x);
                minY = Math.min(minY, p2d[i].y);
                maxX = Math.max(maxX, p2d[i].x);
                maxY = Math.max(maxY, p2d[i].y);
            }
            int basicLenX = (int)(0.8 * UI_WIDTH  / (maxX - minX));
            int basicLenY = (int)(0.8 * UI_HEIGHT / (maxY - minY));
            basicLength = basicLenX < basicLenY ? basicLenX : basicLenY;
        }
        return basicLength;
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

    // get the center point on the watchSurface
    public static TupleReal getWatchSurfacePoint() {
        return watchPoint.sub(getWatchLine());
    }

    // get the Z axis for the watch surface
    public static TupleReal getWatchSurfaceZ() {
        return getWatchLine().unit();
    }

    // get the Y axis for the watch surface
    public static TupleReal getWatchSurfaceY() {
        double t = TupleReal.VECZ.dot(getWatchLine()) / getWatchLine().dot(getWatchLine());
        return TupleReal.VECZ.sub(getWatchLine().mul(t)).unit();
    }

    // get the X axis for the watch surface
    public static TupleReal getWatchSurfaceX() {
        TupleReal axisY = getWatchSurfaceY();
        TupleReal axisZ = getWatchLine().unit();
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
            // regard this suface as a integral of many trangles
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
                }
            }
        }
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
    @Deprecated
    public static PairInt transformOld(TupleReal pos3d) {
        int LENGTH     = getBasicLength();
        int SUB_LENGTH = getSubBasicLength();
        PairReal VECTOR_Y = new PairReal(LENGTH, 0);
        PairReal VECTOR_Z = new PairReal(0, -LENGTH);
        PairReal VECTOR_X = new PairReal(-SUB_LENGTH, SUB_LENGTH);
        PairReal VECTOR_O = new PairReal(Utils.getOx(), Utils.getOy());
        return new PairInt(VECTOR_O.add(VECTOR_X.mul(pos3d.x).add(VECTOR_Y.mul(pos3d.y)).add(VECTOR_Z.mul(pos3d.z))));
    }

    // transform a pos3d to pos2d new
    public static PairInt transform(TupleReal pos3d) throws Exception {
        PairReal newPos = getPositionOnWatchScreen(pos3d).mul(getBasicLength());
        PairReal VECTOR_O = new PairReal(Utils.getOx(), - Utils.getOy());
        PairReal vtmp = VECTOR_O.add(newPos);
        return new PairInt((int)vtmp.x, - (int)vtmp.y - 150);
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

    public static void returnToMainAngle() {
        MathTransform.setLength(DrawPanel.gameNow.getLenX(), DrawPanel.gameNow.getLenY());
    }
}
