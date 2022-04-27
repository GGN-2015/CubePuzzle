package cubepuzzle;

// PairInt defines the pixel position on the screen
class PairInt {
    final int x, y;
    PairInt(int nx, int ny) {
        x = nx;
        y = ny;
    }
    PairInt add(PairInt rhs) {
        return new PairInt(x + rhs.x, y + rhs.y);
    }
    PairInt rev() {
        return new PairInt(-x, -y);
    }
    PairInt mul(double len) {
        return new PairInt((int)(x*len + 0.5), (int)(y*len + 0.5));
    }
}

// Tuple Real is the abstract 3D grid position
class TupleReal {
    final double x, y, z;
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
    TupleReal add(TupleReal rhs) {
        return new TupleReal(x + rhs.x, y + rhs.y, z + rhs.z);
    }
}

public class MathTransform extends Constants{
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


    // transform a pos3d to pos2d
    static PairInt transform(TupleReal pos3d) {
        int LENGTH     = getBasicLength();
        int SUB_LENGTH = getSubBasicLength();
        PairInt VECTOR_Y = new PairInt(LENGTH, 0);
        PairInt VECTOR_Z = new PairInt(0, -LENGTH);
        PairInt VECTOR_X = new PairInt(-SUB_LENGTH, SUB_LENGTH);
        PairInt VECTOR_O = new PairInt(MATH_O_X, MATH_O_Y);
        return VECTOR_O.add(VECTOR_X.mul(pos3d.x).add(VECTOR_Y.mul(pos3d.y)).add(VECTOR_Z.mul(pos3d.z)));
    }
}
