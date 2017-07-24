package com.mcmo.mcmo3d.gl.math.matrix;

import com.mcmo.mcmo3d.gl.math.vector.Vector3;

/**
 * column-major 4*4 matrix
 * <P>order:</P>
 * <pre>
 *     m[offset+0],m[offset+4],m[offset+8],m[offset+12]
 *     m[offset+1],m[offset+5],m[offset+9],m[offset+13]
 *     m[offset+2],m[offset+6],m[offset+10],m[offset+14]
 *     m[offset+3],m[offset+7],m[offset+11],m[offset+15]
 * </pre>
 * <p>
 * Created by ZhangWei on 2017/1/17.
 */

public final class Matrix {
    private final static float[] sTemp = new float[32];

    public static void setIdentity(float[] matrix, int offset) {
        if ((offset + 16) > matrix.length) {
            throw new IllegalArgumentException("Specified offset would overflow the matrix");
        }
        matrix[0 + offset] = 1;
        matrix[1 + offset] = 0;
        matrix[2 + offset] = 0;
        matrix[3 + offset] = 0;//
        matrix[4 + offset] = 0;
        matrix[5 + offset] = 1;
        matrix[6 + offset] = 0;
        matrix[7 + offset] = 0;//
        matrix[8 + offset] = 0;
        matrix[9 + offset] = 0;
        matrix[10 + offset] = 1;
        matrix[11 + offset] = 0;//
        matrix[12 + offset] = 0;
        matrix[13 + offset] = 0;
        matrix[14 + offset] = 0;
        matrix[15 + offset] = 1;//
    }

    /**
     * <pre>
     *     2/(r-l)     0       0        -(r+l)/(r-l)
     *       0      2/(t-b)    0        -(t+b)/(t-b)
     *       0         0     -2/(f-n)   -(f+n)/(f-n)
     *       0         0       0              1
     * </pre>
     *
     * @param m
     * @param mOffset
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near
     * @param far
     */
    public static void orthoM(float[] m, int mOffset, float left, float right, float bottom, float top, float near, float far) {
        if (left == right) {
            throw new IllegalArgumentException("left == right");
        }
        if (bottom == top) {
            throw new IllegalArgumentException("bottom == top");
        }
        if (near == far) {
            throw new IllegalArgumentException("near == far");
        }
        final float r_width = 1.0f / (right - left);
        final float r_height = 1.0f / (top - bottom);
        final float r_depth = 1.0f / (far - near);
        float x = 2.0f * r_width;
        float y = 2.0f * r_height;
        float z = -2.0f * r_depth;
        float tx = (right + left) * r_width;
        float ty = (top + bottom) * r_height;
        float tz = (far + near) * r_depth;
        m[0 + mOffset] = x;
        m[1 + mOffset] = 0;
        m[2 + mOffset] = 0;
        m[3 + mOffset] = 0;
        m[4 + mOffset] = 0;
        m[5 + mOffset] = y;
        m[6 + mOffset] = 0;
        m[7 + mOffset] = 0;
        m[8 + mOffset] = 0;
        m[9 + mOffset] = 0;
        m[10 + mOffset] = z;
        m[11 + mOffset] = 0;
        m[12 + mOffset] = tx;
        m[13 + mOffset] = ty;
        m[14 + mOffset] = tz;
        m[15 + mOffset] = 1;
    }

    /**
     * <pre>
     *     2n/(r-l)       0       (r+l)/(r-l)     0
     *        0        2n/(t-b)   (t+b)/(t-b)     0
     *        0           0      -(f+n)/(f-n) -2fn/(f-n)
     *        0           0            0          1
     * </pre>
     *
     * @param m
     * @param offset
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near
     * @param far
     */
    public static void frustumM(float[] m, int offset, float left, float right, float bottom, float top, float near, float far) {
        if (left == right) {
            throw new IllegalArgumentException("left == right");
        }
        if (top == bottom) {
            throw new IllegalArgumentException("top == bottom");
        }
        if (near == far) {
            throw new IllegalArgumentException("near == far");
        }
        if (near <= 0.0f) {
            throw new IllegalArgumentException("near <= 0.0f");
        }
        if (far <= 0.0f) {
            throw new IllegalArgumentException("far <= 0.0f");
        }
        final float r_width = 1.0f / (right - left);
        final float r_height = 1.0f / (top - bottom);
        final float r_depth = 1.0f / (near - far);
        final float x = 2.0f * near * r_width;
        final float y = 2.0f * near * r_height;
        final float A = (right + left) * r_width;
        final float B = (top + bottom) * r_height;
        final float C = (far + near) * r_depth;
        final float D = 2.0f * far * near * r_depth;
        m[offset + 0] = x;
        m[offset + 5] = y;
        m[offset + 8] = A;
        m[offset + 9] = B;
        m[offset + 10] = C;
        m[offset + 14] = D;
        m[offset + 1] = 0.0f;
        m[offset + 2] = 0.0f;
        m[offset + 3] = 0.0f;
        m[offset + 4] = 0.0f;
        m[offset + 6] = 0.0f;
        m[offset + 7] = 0.0f;
        m[offset + 11] = -1.0f;
        m[offset + 12] = 0.0f;
        m[offset + 13] = 0.0f;
        m[offset + 15] = 0.0f;
    }

    public static void setLookAtM(float[] rm, int rmOffset, float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
        // See the OpenGL GLUT documentation for gluLookAt for a description
        // of the algorithm. We implement it in a straightforward way:

        float fx = centerX - eyeX;
        float fy = centerY - eyeY;
        float fz = centerZ - eyeZ;

        // Normalize f
        float rlf = 1.0f / length(fx, fy, fz);
        fx *= rlf;
        fy *= rlf;
        fz *= rlf;

        // compute s = f x up (x means "cross product")
        float sx = fy * upZ - fz * upY;
        float sy = fz * upX - fx * upZ;
        float sz = fx * upY - fy * upX;

        // and normalize s
        float rls = 1.0f / android.opengl.Matrix.length(sx, sy, sz);
        sx *= rls;
        sy *= rls;
        sz *= rls;

        // compute u = s x f
        float ux = sy * fz - sz * fy;
        float uy = sz * fx - sx * fz;
        float uz = sx * fy - sy * fx;

        rm[rmOffset + 0] = sx;
        rm[rmOffset + 1] = ux;
        rm[rmOffset + 2] = -fx;
        rm[rmOffset + 3] = 0.0f;

        rm[rmOffset + 4] = sy;
        rm[rmOffset + 5] = uy;
        rm[rmOffset + 6] = -fy;
        rm[rmOffset + 7] = 0.0f;

        rm[rmOffset + 8] = sz;
        rm[rmOffset + 9] = uz;
        rm[rmOffset + 10] = -fz;
        rm[rmOffset + 11] = 0.0f;

        rm[rmOffset + 12] = 0.0f;
        rm[rmOffset + 13] = 0.0f;
        rm[rmOffset + 14] = 0.0f;
        rm[rmOffset + 15] = 1.0f;

        translateM(rm, rmOffset, -eyeX, -eyeY, -eyeZ);
    }

    public static void perspectiveM(double[] m, int offset,
                                    double fovy, double aspect, double zNear, double zFar) {
        double f = 1.0 / Math.tan(fovy * (Math.PI / 360.0));
        double rangeReciprocal = 1.0 / (zNear - zFar);

        m[offset + 0] = f / aspect;
        m[offset + 1] = 0.0;
        m[offset + 2] = 0.0;
        m[offset + 3] = 0.0;

        m[offset + 4] = 0.0;
        m[offset + 5] = f;
        m[offset + 6] = 0.0;
        m[offset + 7] = 0.0;

        m[offset + 8] = 0.0;
        m[offset + 9] = 0.0;
        m[offset + 10] = (zFar + zNear) * rangeReciprocal;
        m[offset + 11] = -1.0;

        m[offset + 12] = 0.0;
        m[offset + 13] = 0.0;
        m[offset + 14] = 2.0 * zFar * zNear * rangeReciprocal;
        m[offset + 15] = 0.0;
    }

    /**
     * @param r      用来填充结果的数组，0 fov, 1 aspect ,2 near 3 far
     * @param matrix perspective 矩阵
     * @return true 解析成功，false 解析失败
     */
    public static boolean perspectiveParse(float[] r, float[] matrix, int offset) {
        if(r.length<4){
            throw new RuntimeException("this array r length must more than 4");
        }
        float fov, aspect, near, far;
        float tanHalfFovy = 1.0f / matrix[offset + 5];
        fov = (float) ((float) Math.atan(tanHalfFovy) * 360.0 / Math.PI);
        if (fov < 0) fov = -fov;
        aspect = matrix[offset + 5] / matrix[offset + 0];
        if (matrix[offset + 10] == 1.0f) {
            far = 0.0f;
            near = 0.0f;
        } else if (matrix[offset + 10] == -1.0f) {
            near = 0.0f;
            far = Float.POSITIVE_INFINITY;
        } else {
            near = matrix[offset + 14] / (matrix[offset + 10] - 1);
            far = matrix[offset + 14] / (matrix[offset + 10] + 1);
        }
        if (matrix[offset + 0] == 0.0f || matrix[offset + 5] == 0.0f | matrix[offset + 10] == 0.0f) {
            return false;
        }
        if (matrix[offset + 4] != 0.0f || matrix[offset + 12] != 0.0f
                || matrix[offset + 1] != 0.0f || matrix[offset + 13] != 0.0f
                || matrix[offset + 2] != 0.0f || matrix[offset + 6] != 0.0f
                || matrix[offset + 3] != 0.0f || matrix[offset + 7] != 0.0f || matrix[offset + 15] != 0.0f) {
            return false;
        }
        if(matrix[offset+11]!=-1.0f){
            return false;
        }
        r[0]=fov;
        r[1]=aspect;
        r[2]=near;
        r[3]=far;
        return true;
    }

    public static void multiplyMV(float[] rm, int resultOffset, float[] lhs, int lhsOffset, float[] rhs, int rhsOffset) {
        String message = null;
        if (rm == null) {
            message = "result array can not be null";
        } else if (lhs == null) {
            message = "left hand side array can not be null";
        } else if (rhs == null) {
            message = "right hand side array can not be null";
        } else if ((resultOffset + 16) > rm.length) {
            message = "Specified result offset would overflow the passed result matrix.";
        } else if ((lhsOffset + 16) > lhs.length) {
            message = "Specified left hand side offset would overflow the passed lhs matrix.";
        } else if ((rhsOffset + 16) > rhs.length) {
            message = "Specified right hand side offset would overflow the passed rhs matrix.";
        }
        if (message != null) {
            throw new IllegalArgumentException(message);
        }
        float sum = 0;
        for (int i = 0; i < 4; i++) {//column
            for (int j = 0; j < 4; j++) {//row
                sum = 0;
                for (int k = 0; k < 4; k++) {
                    sum += lhs[i + k * 4 + lhsOffset] * rhs[j * 4 + k + rhsOffset];
                }
                rm[i + j * 4 + resultOffset] = sum;
            }
        }
    }

    public static void translateM(float[] m, int mOffset, float x, float y, float z) {
        for (int i = 0; i < 4; i++) {
            int mi = mOffset + i;
            m[12 + mi] += m[mi] * x + m[mi + 4] * y + m[mi + 8] * z;
//            m[12 + mi] = m[mi] * x + m[mi + 4] * y + m[mi + 8] * z + m[mi + 12] * 1;
        }
    }

    public static void translateM(float[] mTran, int mTranOffset, float[] m, int mOffset, float x, float y, float z) {
        for (int i = 0; i < 12; i++) {
            mTran[i + mTranOffset] = m[i + mOffset];
        }
        for (int i = 0; i < 4; i++) {
            int mi = i + mOffset;
            mTran[12 + mTranOffset + i] = m[mi] * x + m[mi + 4] * y + m[mi + 8] * z + m[mi + 12];
        }
    }

    public static void transposeM(float[] mTran, int mTranOffset, float[] m, int mOffset) {
        for (int i = 0; i < 4; i++) {//column
            int baseIndex = i * 4 + mTranOffset;
            mTran[baseIndex] = m[i + mOffset];
            mTran[baseIndex + 1] = m[i + mOffset + 4];
            mTran[baseIndex + 2] = m[i + mOffset + 8];
            mTran[baseIndex + 3] = m[i + mOffset + 12];
        }
    }

    public static void scaleM(float[] m, int mOffset, float x, float y, float z) {
//        for (int i = 0; i < 4; i++) {
//            int mi = mOffset + i;
//            m[mi] *= x;
//            m[mi + 4] *= y;
//            m[mi + 8] *= z;
//        }
        m[mOffset+0]*=x;
        m[mOffset+5]*=y;
        m[mOffset+10]*=z;

    }

    public static void scaleM(float[] sm, int sOffset, float[] m, int mOffset, float x, float y, float z) {
        for (int i = 0; i < 4; i++) {
            int mi = mOffset + i;
            int si = sOffset + i;
            sm[si] = m[mi] * x;
            sm[si + 4] = m[mi + 4] * y;
            sm[si + 8] = m[mi + 8] * z;
            sm[si + 12] = m[mi + 12];
        }
    }

    public static void setRotateM(float[] rm, int rmOffset, float a, float x, float y, float z) {
        rm[rmOffset + 3] = 0;
        rm[rmOffset + 7] = 0;
        rm[rmOffset + 11] = 0;
        rm[rmOffset + 12] = 0;
        rm[rmOffset + 13] = 0;
        rm[rmOffset + 14] = 0;
        rm[rmOffset + 15] = 1;
        a *= (float) (Math.PI / 180.0f);
        float s = (float) Math.sin(a);
        float c = (float) Math.cos(a);
        if (1.0f == x && 0.0f == y && 0.0f == z) {
            rm[rmOffset + 5] = c;
            rm[rmOffset + 10] = c;
            rm[rmOffset + 6] = s;
            rm[rmOffset + 9] = -s;
            rm[rmOffset + 1] = 0;
            rm[rmOffset + 2] = 0;
            rm[rmOffset + 4] = 0;
            rm[rmOffset + 8] = 0;
            rm[rmOffset + 0] = 1;
        } else if (0.0f == x && 1.0f == y && 0.0f == z) {
            rm[rmOffset + 0] = c;
            rm[rmOffset + 10] = c;
            rm[rmOffset + 8] = s;
            rm[rmOffset + 2] = -s;
            rm[rmOffset + 1] = 0;
            rm[rmOffset + 4] = 0;
            rm[rmOffset + 6] = 0;
            rm[rmOffset + 9] = 0;
            rm[rmOffset + 5] = 1;
        } else if (0.0f == x && 0.0f == y && 1.0f == z) {
            rm[rmOffset + 0] = c;
            rm[rmOffset + 5] = c;
            rm[rmOffset + 1] = s;
            rm[rmOffset + 4] = -s;
            rm[rmOffset + 2] = 0;
            rm[rmOffset + 6] = 0;
            rm[rmOffset + 8] = 0;
            rm[rmOffset + 9] = 0;
            rm[rmOffset + 10] = 1;
        } else {
            float len = length(x, y, z);
            if (1.0f != len) {
                float recipLen = 1.0f / len;
                x *= recipLen;
                y *= recipLen;
                z *= recipLen;
            }
            float nc = 1.0f - c;
            float xy = x * y;
            float yz = y * z;
            float zx = z * x;
            float xs = x * s;
            float ys = y * s;
            float zs = z * s;
            rm[rmOffset + 0] = x * x * nc + c;
            rm[rmOffset + 4] = xy * nc - zs;
            rm[rmOffset + 8] = zx * nc + ys;
            rm[rmOffset + 1] = xy * nc + zs;
            rm[rmOffset + 5] = y * y * nc + c;
            rm[rmOffset + 9] = yz * nc - xs;
            rm[rmOffset + 2] = zx * nc - ys;
            rm[rmOffset + 6] = yz * nc + xs;
            rm[rmOffset + 10] = z * z * nc + c;
        }
    }

    public static void rotateM(float[] m, int rmOffset, float a, float x, float y, float z) {
        synchronized (sTemp) {
            setRotateM(sTemp, 0, a, x, y, z);
            multiplyMV(sTemp, 16, m, rmOffset, sTemp, 0);
            System.arraycopy(sTemp, 16, m, rmOffset, 16);
        }
    }

    public static void rotateM(float[] rm, int rmOffset, float[] m, int mOffset, float a, float x, float y, float z) {
        synchronized (sTemp) {
            setRotateM(sTemp, 0, a, x, y, z);
            multiplyMV(rm, rmOffset, m, mOffset, sTemp, 0);
        }
    }

    public static void setRotateElurM(float[] rm, int rmOffset, float x, float y, float z) {
        x *= (float) (Math.PI / 180.0f);
        y *= (float) (Math.PI / 180.0f);
        z *= (float) (Math.PI / 180.0f);
        float cx = (float) Math.cos(x);
        float sx = (float) Math.sin(x);
        float cy = (float) Math.cos(y);
        float sy = (float) Math.sin(y);
        float cz = (float) Math.cos(z);
        float sz = (float) Math.sin(z);
        float cxsy = cx * sy;
        float sxsy = sx * sy;

        rm[rmOffset + 0] = cy * cz;
        rm[rmOffset + 1] = -cy * sz;
        rm[rmOffset + 2] = sy;
        rm[rmOffset + 3] = 0.0f;

        rm[rmOffset + 4] = cxsy * cz + cx * sz;
        rm[rmOffset + 5] = -cxsy * sz + cx * cz;
        rm[rmOffset + 6] = -sx * cy;
        rm[rmOffset + 7] = 0.0f;

        rm[rmOffset + 8] = -sxsy * cz + sx * sz;
        rm[rmOffset + 9] = sxsy * sz + sx * cz;
        rm[rmOffset + 10] = cx * cy;
        rm[rmOffset + 11] = 0.0f;

        rm[rmOffset + 12] = 0.0f;
        rm[rmOffset + 13] = 0.0f;
        rm[rmOffset + 14] = 0.0f;
        rm[rmOffset + 15] = 1.0f;
    }

    public static void invertM(float[] mInv, int mInvOffset, float[] m, int mOffset) {

    }

    public static float length(float x, float y, float z) {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public static String toString(float[] matrix, int offset) {
        return matrix[0 + offset] + ", " + matrix[4 + offset] + ", " + matrix[8 + offset] + ", " + matrix[12 + offset] + "\n" +
                matrix[1 + offset] + ", " + matrix[5 + offset] + ", " + matrix[9 + offset] + ", " + matrix[13 + offset] + "\n" +
                matrix[2 + offset] + ", " + matrix[6 + offset] + ", " + matrix[10 + offset] + ", " + matrix[14 + offset] + "\n" +
                matrix[3 + offset] + ", " + matrix[7 + offset] + ", " + matrix[11 + offset] + ", " + matrix[15 + offset] + "\n";
    }

    public static Vector3 multiplyMV(Vector3 vec, float[] matrix) {
        Vector3 rv = new Vector3();
        rv.x = matrix[0] * vec.x + matrix[4] * vec.y + matrix[8] * vec.z;// + matrix[12];
        rv.y = matrix[1] * vec.x + matrix[5] * vec.y + matrix[9] * vec.z;// + matrix[13];
        rv.z = matrix[2] * vec.x + matrix[6] * vec.y + matrix[10] * vec.z;// + matrix[14];
//        float w = matrix[3] * vec.x + matrix[7] * vec.y + matrix[11] * vec.z + matrix[15];
//        rv.dot(w);
        return rv;
    }

    /*  此为行矩阵，opengl为列矩阵
    cos θ cos φ, sin ψ sin θ cos φ − cos ψ sin φ, cos ψ sin θ cos φ + sin ψ sin φ
    cos θ sin φ, sin ψ sin θ sin φ + cos ψ cos φ, cos ψ sin θ sin φ − sin ψ cos φ
      − sin θ,                    sin ψ cos θ ,                    cos ψ cos θ
    | R11,R12,R13|
    | R21,R22,R23|
    | R31,R32,R33|
    */
    public static Vector3 getEulerAngle(float[] matrix) {
        double degree = 180.0 / Math.PI;
        int R31 = 8, R32 = 9, R33 = 10, R21 = 4, R11 = 0, R12 = 1, R13 = 2;
        double x1, x2, y1, y2, z1, z2;
        if (matrix[R31] != 1 && matrix[R31] != -1) {
            y1 = -Math.asin(matrix[R31]);
            y2 = Math.PI - y1;
            x1 = Math.atan2(matrix[R32] / Math.cos(y1), matrix[R33] / Math.cos(y1));
            x2 = Math.atan2(matrix[R32] / Math.cos(y2), matrix[R33] / Math.cos(y2));
            z1 = Math.atan2(matrix[R21] / Math.cos(y1), matrix[R11] / Math.cos(y1));
            z2 = Math.atan2(matrix[R21] / Math.cos(y2), matrix[R11] / Math.cos(y2));
        } else {
            z1 = 0;
            if (matrix[R31] == -1) {
                y1 = Math.PI / 2;
                x1 = z1 + Math.atan2(matrix[R12], matrix[R13]);
            } else {
                y1 = -Math.PI / 2;
                x1 = -z1 + Math.atan2(-matrix[R12], -matrix[R13]);
            }
        }
        return new Vector3((float) (x1 * degree), (float) (y1 * degree), (float) (z1 * degree));
    }
}
