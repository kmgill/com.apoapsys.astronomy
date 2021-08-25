package com.apoapsys.astronomy.math;


public class Quaternion
{
	public double q0;
	public double q1;
	public double q2;
	public double q3;

	public Quaternion()
	{
		init();
	}

	public Quaternion(Quaternion q)
	{
		set(q);
	}

	public Quaternion(Vector axis, double angle)
	{
		set(axis, angle);
	}

	public Quaternion(Euler e)
	{
		setFromEuler(e);
	}
	
	protected void init()
	{
		q0 = 1;
		q1 = q2 = q3 = 0;
	}

	public void set(Vector axis, double angle)
	{
		double halfTheta = angle / 2.0f;

		q0 = MathExt.cos(halfTheta);

		double sinHalfTheta = MathExt.sin(halfTheta);

		Vector realAxis = new Vector(axis);

		realAxis.normalize();

		q1 = realAxis.x * sinHalfTheta;
		q2 = realAxis.y * sinHalfTheta;
		q3 = realAxis.z * sinHalfTheta;
	}

	public void set(Quaternion q)
	{
		this.q0 = q.q0;
		this.q1 = q.q1;
		this.q2 = q.q2;
		this.q3 = q.q3;
	}
	
	public void set(double q0, double q1, double q2, double q3)
	{
		this.q0 = q0;
		this.q1 = q1;
		this.q2 = q2;
		this.q3 = q3;
	}
	
	public void setFromEuler(Euler euler) {
		// From Three.js
		
		// http://www.mathworks.com/matlabcentral/fileexchange/
		// 	20696-function-to-convert-between-dcm-euler-angles-quaternions-and-euler-vectors/
		//	content/SpinCalc.m

		double c1 = Math.cos( euler.x / 2 );
		double c2 = Math.cos( euler.y / 2 );
		double c3 = Math.cos( euler.z / 2 );
		double s1 = Math.sin( euler.x / 2 );
		double s2 = Math.sin( euler.y / 2 );
		double s3 = Math.sin( euler.z / 2 );
		
		if ( euler.order.equals("XYZ") ) {

			this.q0 = s1 * c2 * c3 + c1 * s2 * s3;
			this.q1 = c1 * s2 * c3 - s1 * c2 * s3;
			this.q2 = c1 * c2 * s3 + s1 * s2 * c3;
			this.q3 = c1 * c2 * c3 - s1 * s2 * s3;

		} else if ( euler.order.equals("YXZ") ) {

			this.q0 = s1 * c2 * c3 + c1 * s2 * s3;
			this.q1 = c1 * s2 * c3 - s1 * c2 * s3;
			this.q2 = c1 * c2 * s3 - s1 * s2 * c3;
			this.q3 = c1 * c2 * c3 + s1 * s2 * s3;

		} else if ( euler.order.equals("ZXY") ) {

			this.q0 = s1 * c2 * c3 - c1 * s2 * s3;
			this.q1 = c1 * s2 * c3 + s1 * c2 * s3;
			this.q2 = c1 * c2 * s3 + s1 * s2 * c3;
			this.q3 = c1 * c2 * c3 - s1 * s2 * s3;

		} else if ( euler.order.equals("ZYX")) {

			this.q0 = s1 * c2 * c3 - c1 * s2 * s3;
			this.q1 = c1 * s2 * c3 + s1 * c2 * s3;
			this.q2 = c1 * c2 * s3 - s1 * s2 * c3;
			this.q3 = c1 * c2 * c3 + s1 * s2 * s3;

		} else if ( euler.order.equals("YZX") ) {

			this.q0 = s1 * c2 * c3 + c1 * s2 * s3;
			this.q1 = c1 * s2 * c3 + s1 * c2 * s3;
			this.q2 = c1 * c2 * s3 - s1 * s2 * c3;
			this.q3 = c1 * c2 * c3 - s1 * s2 * s3;

		} else if ( euler.order.equals("XZY") ) {

			this.q0 = s1 * c2 * c3 - c1 * s2 * s3;
			this.q1 = c1 * s2 * c3 - s1 * c2 * s3;
			this.q2 = c1 * c2 * s3 + s1 * s2 * c3;
			this.q3 = c1 * c2 * c3 + s1 * s2 * s3;

		}
	}
	
	public boolean withinEpsilon(Quaternion arg, double epsilon)
	{
		return ((MathExt.abs(q0 - arg.q0) < epsilon)
				&& (MathExt.abs(q1 - arg.q1) < epsilon)
				&& (MathExt.abs(q2 - arg.q2) < epsilon)
				&& (MathExt.abs(q3 - arg.q3) < epsilon));
	}

	public double get(Vector axis)
	{

		double retval = (2.0f * MathExt.acos(q0));
		axis.set(q1, q2, q3);
		double len = axis.length();
		if (len == 0.0f) {
			axis.set(0, 0, 1);
		} else {
			axis.scale(1.0f / len);
		}
		return retval;
	}

	public Quaternion getInverse()
	{
		Quaternion tmp = new Quaternion(this);
		tmp.invert();
		return tmp;
	}

	public void invert()
	{
		q1 = -q1;
		q2 = -q2;
		q3 = -q3;
	}

	public double lengthSquared()
	{
		return (q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3);
	}

	public double length()
	{
		return (double) MathExt.sqrt(lengthSquared());
	}

	public void normalize()
	{
		double len = length();
		q0 /= len;
		q1 /= len;
		q2 /= len;
		q3 /= len;
	}

	public Quaternion multiply(Quaternion b)
	{
		Quaternion tmp = new Quaternion();
		tmp.multiply(this, b);
		
		q0 = tmp.q0;
		q1 = tmp.q1;
		q2 = tmp.q2;
		q3 = tmp.q3;
		
		return this;
	}

	public void multiply(Quaternion a, Quaternion b)
	{
		double qax = a.q0, qay = a.q1, qaz = a.q2, qaw = a.q3;
		double qbx = b.q0, qby = b.q1, qbz = b.q2, qbw = b.q3;

		this.q0 = qax * qbw + qaw * qbx + qay * qbz - qaz * qby;
		this.q1 = qay * qbw + qaw * qby + qaz * qbx - qax * qbz;
		this.q2 = qaz * qbw + qaw * qbz + qax * qby - qay * qbx;
		this.q3 = qaw * qbw - qax * qbx - qay * qby - qaz * qbz;
	}

	public void toMatrix(Matrix mat)
	{
		double q00 = q0 * q0;
		double q11 = q1 * q1;
		double q22 = q2 * q2;
		double q33 = q3 * q3;
		// Diagonal elements
		mat.set(0, 0, q00 + q11 - q22 - q33);
		mat.set(1, 1, q00 - q11 + q22 - q33);
		mat.set(2, 2, q00 - q11 - q22 + q33);
		// 0,1 and 1,0 elements
		double q03 = q0 * q3;
		double q12 = q1 * q2;
		mat.set(1, 0, 2.0f * (q12 - q03));
		mat.set(0, 1, 2.0f * (q03 + q12));
		// 0,2 and 2,0 elements
		double q02 = q0 * q2;
		double q13 = q1 * q3;
		mat.set(2, 0, 2.0f * (q02 + q13));
		mat.set(0, 2, 2.0f * (q13 - q02));
		// 1,2 and 2,1 elements
		double q01 = q0 * q1;
		double q23 = q2 * q3;
		mat.set(2, 1, 2.0f * (q23 - q01));
		mat.set(1, 2, 2.0f * (q01 + q23));
	}

	public void fromMatrix(Matrix mat)
	{
		// FIXME: Should reimplement to follow Horn's advice of using
		// eigenvector decomposition to handle roundoff error in given
		// matrix.

		double tr, s;
		int i, j, k;

		tr = mat.get(0, 0) + mat.get(1, 1) + mat.get(2, 2);
		if (tr > 0.0) {
			s = Math.sqrt(tr + 1.0f);
			q0 = s * 0.5f;
			s = 0.5f / s;
			q1 = (mat.get(2, 1) - mat.get(1, 2)) * s;
			q2 = (mat.get(0, 2) - mat.get(2, 0)) * s;
			q3 = (mat.get(1, 0) - mat.get(0, 1)) * s;
		} else {
			i = 0;
			if (mat.get(1, 1) > mat.get(0, 0))
				i = 1;
			if (mat.get(2, 2) > mat.get(i, i))
				i = 2;
			j = (i + 1) % 3;
			k = (j + 1) % 3;
			s = Math.sqrt((mat.get(i, i) - (mat.get(j, j) + mat.get(k, k))) + 1.0f);
			setQ(i + 1, s * 0.5f);
			s = 0.5f / s;
			q0 = (mat.get(k, j) - mat.get(j, k)) * s;
			setQ(j + 1, (mat.get(j, i) + mat.get(i, j)) * s);
			setQ(k + 1, (mat.get(k, i) + mat.get(i, k)) * s);
		}
	}

	public void rotateVector(Vector src, Vector dest)
	{
		// NOTE: uncomment these to illustrate compiler bug with line numbers
		// Vector qCrossX = new Vector();
		// Vector qCrossXCrossQ = new Vector();
		Vector qVec = new Vector(q1, q2, q3);

		Vector qCrossX = qVec.crossProduct(src);
		Vector qCrossXCrossQ = qCrossX.crossProduct(qVec);
		qCrossX.scale(2.0f * q0);
		qCrossXCrossQ.scale(-2.0f);
		dest.add(src, qCrossX);
		dest.add(dest, qCrossXCrossQ);
	}

	public Vector rotateVector(Vector src)
	{
		Vector tmp = new Vector();
		rotateVector(src, tmp);
		return tmp;
	}
	
	public Quaternion times(Quaternion b) {
		Quaternion tmp = new Quaternion();
		tmp.multiply(this, b);
		return tmp;
	}
	
	public String toString()
	{
		return "(" + q0 + ", " + q1 + ", " + q2 + ", " + q3 + ")";
	}

	public void setQ(int i, double val)
	{
		switch (i) {
		case 0:
			q0 = val;
			break;
		case 1:
			q1 = val;
			break;
		case 2:
			q2 = val;
			break;
		case 3:
			q3 = val;
			break;
		default:
			throw new IndexOutOfBoundsException();
		}
	}
	
	public double getQ(int i)
	{
		switch(i) {
		case 0:
			return q0;
		case 1:
			return q1;
		case 2:
			return q2;
		case 3:
			return q3;
		default:
			throw new IndexOutOfBoundsException();
		}
	}
}
