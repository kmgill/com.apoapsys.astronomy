package com.apoapsys.astronomy.math;

public class Matrix
{
	public static final double[] ZERO_ARRAY = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

	public static final double[] IDENTITY_ARRAY = { 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 };

	public double[] matrix = new double[16];

	public Matrix(boolean loadIdentity)
	{
		if (loadIdentity) {
			this.loadIdentity();
		} else {
			this.fill(0);
		}
	}

	public Matrix(double v)
	{
		this.fill(0);
	}

	public Matrix(double v00, double v01, double v02, double v03, double v10, double v11, double v12, double v13, double v20, double v21, double v22, double v23, double v30, double v31, double v32, double v33)
	{
		matrix[0] = v00;
		matrix[1] = v01;
		matrix[2] = v02;
		matrix[3] = v03;

		matrix[4] = v10;
		matrix[5] = v11;
		matrix[6] = v12;
		matrix[7] = v13;

		matrix[8] = v20;
		matrix[9] = v21;
		matrix[10] = v22;
		matrix[11] = v23;

		matrix[12] = v30;
		matrix[13] = v31;
		matrix[14] = v32;
		matrix[15] = v33;

	}

	public Matrix(double[] values)
	{
		if (values != null && values.length >= 16) {
			for (int i = 0; i < 16; i++) {
				this.matrix[i] = values[i];
			}
		} else {
			fill(0);
		}
	}

	public Matrix()
	{
		this.fill(0);
	}

	public Matrix(float[] values)
	{
		if (values != null && values.length >= 16) {
			for (int i = 0; i < 16; i++) {
				this.matrix[i] = values[i];
			}
		} else {
			fill(0);
		}
	}

	public void fill(double v)
	{
		for (int i = 0; i < 16; i++) {
			this.matrix[i] = v;
		}
	}

	public void loadIdentity()
	{
		for (int i = 0; i < 16; i++) {
			this.matrix[i] = Matrix.IDENTITY_ARRAY[i];
		}
	}

	protected static int index(int x, int y)
	{
		return (y * 4) + x;
	}

	public void set(int x, int y, double v)
	{
		int i = index(x, y);
		if (i >= 0 && i < 16) {
			this.matrix[i] = v;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	public double get(int x, int y)
	{
		int i = index(x, y);
		if (i >= 0 && i < 16) {
			return this.matrix[i];
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	public void copyTo(Matrix copy)
	{
		for (int i = 0; i < 16; i++) {
			copy.matrix[i] = this.matrix[i];
		}
	}

	public Matrix copy()
	{
		Matrix m = new Matrix();
		this.copyTo(m);
		return m;
	}

	public double[] toArray(double[] arr, int offset, boolean rowMajor)
	{
		if (arr != null && arr.length < offset + 16) {
			// throw new InvalidParameterException();
		}
		if (arr == null) {
			arr = new double[offset + 16];
		}

		if (rowMajor) {
			arr[0 + offset] = this.matrix[0];
			arr[1 + offset] = this.matrix[1];
			arr[2 + offset] = this.matrix[2];
			arr[3 + offset] = this.matrix[3];

			arr[4 + offset] = this.matrix[4];
			arr[5 + offset] = this.matrix[5];
			arr[6 + offset] = this.matrix[6];
			arr[7 + offset] = this.matrix[7];

			arr[8 + offset] = this.matrix[8];
			arr[9 + offset] = this.matrix[9];
			arr[10 + offset] = this.matrix[10];
			arr[11 + offset] = this.matrix[11];

			arr[12 + offset] = this.matrix[12];
			arr[13 + offset] = this.matrix[13];
			arr[14 + offset] = this.matrix[14];
			arr[15 + offset] = this.matrix[15];

		} else {
			arr[0 + offset] = this.matrix[0];
			arr[4 + offset] = this.matrix[1];
			arr[8 + offset] = this.matrix[2];
			arr[12 + offset] = this.matrix[3];

			arr[1 + offset] = this.matrix[4];
			arr[5 + offset] = this.matrix[5];
			arr[9 + offset] = this.matrix[6];
			arr[13 + offset] = this.matrix[7];

			arr[2 + offset] = this.matrix[8];
			arr[6 + offset] = this.matrix[9];
			arr[10 + offset] = this.matrix[10];
			arr[14 + offset] = this.matrix[11];

			arr[3 + offset] = this.matrix[12];
			arr[7 + offset] = this.matrix[13];
			arr[11 + offset] = this.matrix[14];
			arr[15 + offset] = this.matrix[15];
		}

		return arr;
	}

	/*
	 * Code borrowed from Mesa3d /src/mesa/math/m_matrix.c
	 */
	public static void matmul4(double[] product, double[] a, double[] b)
	{

		for (int row = 0; row < 4; row++) {
			double ai0 = a[(0 << 2) + row];
			double ai1 = a[(1 << 2) + row];
			double ai2 = a[(2 << 2) + row];
			double ai3 = a[(3 << 2) + row];

			product[(0 << 2) + row] = ai0 * b[(0 << 2) + 0] + ai1 * b[(0 << 2) + 1] + ai2 * b[(0 << 2) + 2] + ai3 * b[(0 << 2) + 3];
			product[(1 << 2) + row] = ai0 * b[(1 << 2) + 0] + ai1 * b[(1 << 2) + 1] + ai2 * b[(1 << 2) + 2] + ai3 * b[(1 << 2) + 3];
			product[(2 << 2) + row] = ai0 * b[(2 << 2) + 0] + ai1 * b[(2 << 2) + 1] + ai2 * b[(2 << 2) + 2] + ai3 * b[(2 << 2) + 3];
			product[(3 << 2) + row] = ai0 * b[(3 << 2) + 0] + ai1 * b[(3 << 2) + 1] + ai2 * b[(3 << 2) + 2] + ai3 * b[(3 << 2) + 3];

		}

	}

	public void multiply(Matrix other)
	{
		matmul4(this.matrix, this.matrix, other.matrix);
	}

	public void multiply(Matrix in, Matrix out)
	{
		matmul4(out.matrix, this.matrix, in.matrix);
	}

	public void multiply(Vector in, Vector out)
	{

		out.x = in.x * this.matrix[0 * 4 + 0] + in.y * this.matrix[1 * 4 + 0] + in.z * this.matrix[2 * 4 + 0] + in.w * this.matrix[3 * 4 + 0];

		out.y = in.x * this.matrix[0 * 4 + 1] + in.y * this.matrix[1 * 4 + 1] + in.z * this.matrix[2 * 4 + 1] + in.w * this.matrix[3 * 4 + 1];

		out.z = in.x * this.matrix[0 * 4 + 2] + in.y * this.matrix[1 * 4 + 2] + in.z * this.matrix[2 * 4 + 2] + in.w * this.matrix[3 * 4 + 2];

		out.w = in.x * this.matrix[0 * 4 + 3] + in.y * this.matrix[1 * 4 + 3] + in.z * this.matrix[2 * 4 + 3] + in.w * this.matrix[3 * 4 + 3];

	}

	
	

	public static Matrix makeRotationX(double theta) {

		double c = Math.cos( theta );
		double s = Math.sin( theta );
		
		Matrix m = new Matrix(1, 0,  0, 0,
								0, c, s, 0,
								0,-s,  c, 0,
								0, 0,  0, 1
							);

		return m;
	}

	public static Matrix makeRotationY(double theta ) {

		double c = Math.cos( theta );
		double s = Math.sin( theta );

		Matrix m = new Matrix(
						 c, 0,-s, 0,
						 0, 1, 0, 0,
						 s, 0, c, 0,
						 0, 0, 0, 1
					);

		return m;

	}

	public static Matrix makeRotationZ(double theta ) {

		double c = Math.cos( theta );
		double s = Math.sin( theta );

		Matrix m = new Matrix(
						c, s, 0, 0,
						-s,  c, 0, 0,
						0,  0, 1, 0,
						0,  0, 0, 1
					);

		return m;

	}
	
	public void multiply(double[] in, double[] out)
	{
		for (int i = 0; i < 4; i++) {
			out[i] = in[0] * this.matrix[0 * 4 + i] + in[1] * this.matrix[1 * 4 + i] + in[2] * this.matrix[2 * 4 + i] + in[3] * this.matrix[3 * 4 + i];
		}
	}

	public void scale(double x, double y, double z)
	{
		this.matrix[0] *= x;
		this.matrix[4] *= y;
		this.matrix[8] *= z;
		this.matrix[1] *= x;
		this.matrix[5] *= y;
		this.matrix[9] *= z;
		this.matrix[2] *= x;
		this.matrix[6] *= y;
		this.matrix[10] *= z;
		this.matrix[3] *= x;
		this.matrix[7] *= y;
		this.matrix[11] *= z;
	}

	public void translate(double x, double y, double z)
	{
		this.matrix[12] = this.matrix[0] * x + this.matrix[4] * y + this.matrix[8] * z + this.matrix[12];
		this.matrix[13] = this.matrix[1] * x + this.matrix[5] * y + this.matrix[9] * z + this.matrix[13];
		this.matrix[14] = this.matrix[2] * x + this.matrix[6] * y + this.matrix[10] * z + this.matrix[14];
		this.matrix[15] = this.matrix[3] * x + this.matrix[7] * y + this.matrix[11] * z + this.matrix[15];
	}

	public void transposeRotation()
	{
		Matrix tmp = new Matrix(this.matrix);
		this.matrix[1] = tmp.matrix[4];
		this.matrix[4] = tmp.matrix[1];

		this.matrix[2] = tmp.matrix[8];
		this.matrix[8] = tmp.matrix[2];

		// this.matrix[3] = tmp.matrix[12];
		// this.matrix[12] = tmp.matrix[3];

		this.matrix[6] = tmp.matrix[9];
		this.matrix[9] = tmp.matrix[6];
	}

	public void rotate(double angle, int x, int y, int z)
	{
		Matrix m = new Matrix(true);

		if (y == 1) {
			angle *= -1.0;
		}

		double _a = angle;
		double c = MathExt.cos(_a);
		double s = MathExt.sin(_a);

		if (x == 1) {
			m.set(1, 1, c);
			m.set(2, 2, c);

			m.set(2, 1, -s);
			m.set(1, 2, s);
		} else if (y == 1) {
			m.set(0, 0, c);
			m.set(2, 2, c);

			m.set(2, 0, s);
			m.set(0, 2, -s);
		} else if (z == 1) {
			m.set(0, 0, c);
			m.set(1, 1, c);

			m.set(0, 1, s);
			m.set(1, 0, -s);
		}

		this.multiply(m);

	}
}
