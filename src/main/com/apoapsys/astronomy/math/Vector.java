package com.apoapsys.astronomy.math;

public class Vector
{

	public static final Vector X_AXIS_VECTOR = new Vector(1, 0, 0);
	public static final Vector Y_AXIS_VECTOR = new Vector(0, 1, 0);
	public static final Vector Z_AXIS_VECTOR = new Vector(0, 0, 1);

	public double x = 0;
	public double y = 0;
	public double z = 0;
	public double w = 1.0;

	public Vector()
	{

	}
	
	public void reset() {
		x = y = z = 0.0;
	}

	public Vector(Vector copy)
	{
		this.x = copy.x;
		this.y = copy.y;
		this.z = copy.z;
		this.w = copy.w;
	}

	public Vector(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector(double x, double y, double z, double w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public void copyTo(Vector v)
	{
		v.x = this.x;
		v.y = this.y;
		v.z = this.z;
	}

	public void set(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector divideScalar(double s) {
		this.x /= s;
		this.y /= s;
		this.z /= s;
		return this;
	}
	
	public Vector multiplyScalar(double s) {
		this.x *= s;
		this.y *= s;
		this.z *= s;
		return this;
	}
	
	public double length()
	{
		return length(x, y, z);
	}

	public static double length(double x, double y, double z)
	{
		return MathExt.sqrt(MathExt.sqr(x) + MathExt.sqr(y) + MathExt.sqr(z));
	}
	
	public Vector multiply(Vector v) {
		this.x *= v.x;
		this.y *= v.y;
		this.z *= v.z;
		return this;
	}
	
	public Vector divide(Vector v) {
		this.x /= v.x;
		this.y /= v.y;
		this.z /= v.z;
		return this;
	}
	
	public void scale(double f)
	{
		this.x *= f;
		this.y *= f;
		this.z *= f;
	}

	public double getDistanceTo(Vector other)
	{
		double _x = this.x - other.x;
		double _y = this.y - other.y;
		double _z = this.z - other.z;
		
		return length(_x, _y, _z);
	}

	public Vector getUnitVector()
	{
		double len = this.length();
		if (len <= 0.0) {
			return new Vector(0, 0, 0);
		} else {
			return new Vector(x / len, y / len, z / len, w / len);
		}
	}

	
	public Vector normalize(Vector v) {
		double len = v.length();
		if (len == 0.0) {
			len = 1.0;
		}
		v.x /= len;
		v.y /= len;
		v.z /= len;
		return v;
	}
	
	public Vector getNormalized()
	{
		Vector v = this.clone();
		normalize(v);
		return v;
	}

	public Vector normalize()
	{
		return normalize(this);
	}

	public Vector getInversed()
	{
		return new Vector(x * -1, y * -1, z * -1);
	}
	
	public Vector inverse()
	{
		this.x *= -1.0;
		this.y *= -1.0;
		this.z *= -1.0;
		return this;
	}
	
	
	public double dotProduct(Vector other)
	{
		return dotProduct(other, true);
	}

	public double dotProduct(Vector other, boolean normalize)
	{
		Vector v0 = (normalize) ? this.getNormalized() : this;
		Vector v1 = (normalize) ? other.getNormalized() : other;
		return v0.x * v1.x + v0.y * v1.y + v0.z * v1.z;
	}
	
	public double dotProduct4(Vector other)
	{
		Vector v0 = this.getNormalized();
		Vector v1 = other.getNormalized();

		return v0.x * v1.x + v0.y * v1.y + v0.z * v1.z + v0.w * v1.w;
	}

	public Vector crossProduct(Vector other)
	{
		return new Vector(this.y * other.z - other.y * this.z
						, this.z * other.x - other.z * this.x
						, this.x * other.y - other.x * this.y);
	}

	public double angle(Vector other)
	{
		double dot = this.dotProduct(other);// / (this.length() * other.length());
		return MathExt.acos(MathExt.clamp(dot, -1.0, 1.0));
	}
	
	public Vector subtract(double other)
	{
		this.x -= other;
		this.y -= other;
		this.z -= other;
		return this;
	}
	
	public Vector subtract(Vector other)
	{
		this.x -= other.x;
		this.y -= other.y;
		this.z -= other.z;
		return this;
	}
	
	public Vector add(double other)
	{
		this.x += other;
		this.y += other;
		this.z += other;
		return this;
	}
	
	public Vector add(Vector b)
	{
		add(this, b);
		return this;
	}

	/** this = a + b */
	public Vector add(Vector a, Vector b)
	{
		x = a.x + b.x;
		y = a.y + b.y;
		z = a.z + b.z;
		return this;
	}

	public Vector getDirectionTo(Vector other)
	{
		return other.subtract(this).getNormalized();
	}

	public double intersectDistance(Plane plane, Vector direction)
	{
		double ldotv = plane.getPlaneVector().dotProduct(direction);
		if (ldotv == 0) {
			return 0; // If I wasn't being lazy then I should be checking for
						// infinity instead
		}
		return -plane.getPlaneVector().dotProduct4(this) / ldotv;
	}

	public Vector intersectPoint(Vector direction, double intersectDistance)
	{
		if (intersectDistance == 0) {
			return null;
		}
		Vector intersect = new Vector(this.x + (direction.x * intersectDistance)
									, this.y + (direction.y * intersectDistance)
									, this.z + (direction.z * intersectDistance));
		return intersect;
	}
	
	public void rotate(double x, double y, double z)
	{
		Vectors.rotate(x, y, z, this);
	}
	
	public void rotate(double angle, int axis)
	{
		if (axis == Vectors.X_AXIS) {
			Vectors.rotateX(angle, this);
		} else if (axis == Vectors.Y_AXIS) {
			Vectors.rotateY(angle, this);
		} else if (axis == Vectors.Z_AXIS) {
			Vectors.rotateZ(angle, this);
		}
	}
	
	public void applyEuler(Euler e) {
		Quaternion q = new Quaternion(e);
		applyQuaternion(q);
	}
	
	public void applyQuaternion(Quaternion q) {
		// From Three.js
		double x = this.x;
		double y = this.y;
		double z = this.z;

		double qx = q.q0;
		double qy = q.q1;
		double qz = q.q2;
		double qw = q.q3;

		// calculate quat * vector

		double ix =  qw * x + qy * z - qz * y;
		double iy =  qw * y + qz * x - qx * z;
		double iz =  qw * z + qx * y - qy * x;
		double iw = -qx * x - qy * y - qz * z;

		// calculate result * inverse quat

		this.x = ix * qw + iw * -qx + iy * -qz - iz * -qy;
		this.y = iy * qw + iw * -qy + iz * -qx - ix * -qz;
		this.z = iz * qw + iw * -qz + ix * -qy - iy * -qx;


	}
	
	
	public void applyMatrix(Matrix m) {
		Vector v = new Vector();
		m.multiply(this, v);
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = v.w;
	}

	
	public Vector clone()
	{
		return new Vector(x, y, z);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Vector)) {
			return false;
		}
		
		Vector other = (Vector) o;
		return this.x == other.x && this.y == other.y && this.z == other.z && this.w == other.w;
	}
}
