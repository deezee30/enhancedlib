/*
 * MaulssLib
 * 
 * Created on 03 January 2015 at 3:05 AM.
 */

package com.maulss.enhancedlib.math;

import java.io.Serializable;
import java.util.Random;

/**
 * Represents a mutable vector.  Because the components of Vectors are mutable,
 * storing Vectors long term may be dangerous if passing code modifies the
 * Vector later.  If you want to keep around a Vector, it may be wise to call
 * {@link #clone()} in order to get a copy.
 */
public class Vector implements Cloneable, Serializable {

	private static final long serialVersionUID = -2657651106777219169L;

	private static final Random random = new Random();

	/**
	 * Threshold for fuzzy equals().
	 */
	private static final double epsilon = 0.000001;

	/**
	 * Point or vector with all three coordinates set to 0.
	 */
	public static final Vector ZERO = new Vector(0, 0, 0);

	/**
	 * The x component.
	 */
	private double x;

	/**
	 * The x component.
	 *
	 * @return the x component
	 */
	public double getX() {
		return x;
	}

	/**
	 * The y component.
	 */
	private double y;

	/**
	 * The y component.
	 *
	 * @return the y component
	 */
	public double getY() {
		return y;
	}

	/**
	 * The z component.
	 */
	private double z;

	/**
	 * The z component.
	 *
	 * @return the z component
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Construct the vector with provided double components.
	 *
	 * @param x X component
	 * @param y Y component
	 * @param z Z component
	 */
	public Vector(double x,
				  double y,
				  double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Adds a vector to this one.
	 *
	 * @param   vec The other vector
	 *
	 * @return  the same vector
	 */
	public Vector add(Vector vec) {
		x += vec.x;
		y += vec.y;
		z += vec.z;
		return this;
	}

	/**
	 * Performs scalar addition, adding
	 * all components with a scalar.
	 *
	 * @param   scalar The factor
	 *
	 * @return  the same vector
	 */
	public Vector add(double scalar) {
		x += scalar;
		y += scalar;
		z += scalar;
		return this;
	}

	/**
	 * Subtracts a vector from this one.
	 *
	 * @param   vec The other vector
	 *
	 * @return  the same vector
	 */
	public Vector subtract(Vector vec) {
		x -= vec.x;
		y -= vec.y;
		z -= vec.z;
		return this;
	}

	/**
	 * Performs scalar subtraction, subtracting
	 * all components with a scalar.
	 *
	 * @param   scalar The factor
	 *
	 * @return  the same vector
	 */
	public Vector subtract(double scalar) {
		x -= scalar;
		y -= scalar;
		z -= scalar;
		return this;
	}

	/**
	 * Multiplies the vector by another.
	 *
	 * @param   vec The other vector
	 *
	 * @return  the same vector
	 */
	public Vector multiply(Vector vec) {
		x *= vec.x;
		y *= vec.y;
		z *= vec.z;
		return this;
	}

	/**
	 * Performs scalar multiplication, multiplying
	 * all components with a scalar.
	 *
	 * @param   scalar The factor
	 *
	 * @return  the same vector
	 */
	public Vector multiply(double scalar) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
		return this;
	}

	/**
	 * Divides the vector by another.
	 *
	 * @param   vec The other vector
	 *
	 * @return  the same vector
	 */
	public Vector divide(Vector vec) {
		x /= vec.x;
		y /= vec.y;
		z /= vec.z;
		return this;
	}

	/**
	 * Performs scalar division, dividing
	 * all components by a scalar.
	 *
	 * @param   scalar The factor
	 *
	 * @return  the same vector
	 */
	public Vector divide(double scalar) {
		x /= scalar;
		y /= scalar;
		z /= scalar;
		return this;
	}

	/**
	 * Copies another vector.
	 *
	 * @param   vec The other vector
	 *
	 * @return  the same vector
	 */
	public Vector copy(Vector vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
		return this;
	}

	/**
	 * Gets the magnitude of the vector, defined as sqrt(x^2+y^2+z^2).
	 *
	 * The value of this method is not cached and uses a costly square-root
	 * function, so do not repeatedly call this method to get the vector's
	 * magnitude.  {@code NaN} will be returned if the inner result of the
	 * {@code sqrt()} function overflows, which will be caused if the length
	 * is too long.
	 *
	 * @return the magnitude
	 */
	public double length() {
		return Math.sqrt(lengthSquared());
	}

	/**
	 * Gets the magnitude of the vector squared.
	 *
	 * @return the magnitude
	 */
	public double lengthSquared() {
		return x * x + y * y + z * z;
	}

	/**
	 * Get the distance between this vector and another.
	 *
	 * The value of this method is not cached and uses a costly square-root
	 * function, so do not repeatedly call this method to get the vector's
	 * magnitude.  {@code NaN} will be returned if the inner result of the
	 * {@code sqrt()} function overflows, which will be caused if the distance
	 * is too long.
	 *
	 * @param   o The other vector
	 *
	 * @return  the distance
	 */
	public double distance(Vector o) {
		return Math.sqrt(distanceSquared(o));
	}

	/**
	 * Get the squared distance between this vector and another.
	 *
	 * @param   o The other vector
	 *
	 * @return  the distance
	 */
	public double distanceSquared(Vector o) {
		return Math.pow(x - o.x, 2) + Math.pow(y - o.y, 2) + Math.pow(z - o.z, 2);
	}

	/**
	 * Computes the angle (in degrees) between the vector represented
	 * by this point and the specified vector.
	 *
	 * @param   x the X magnitude of the other vector
	 * @param   y the Y magnitude of the other vector
	 * @param   z the Z magnitude of the other vector
	 *
	 * @return  the angle between the two vectors measured in degrees
	 */
	public double angle(double x,
						double y,
						double z) {
		final double ax = getX();
		final double ay = getY();
		final double az = getZ();

		final double delta = (ax * x + ay * y + az * z) / Math.sqrt((ax * ax + ay * ay + az * az) * (x * x + y * y + z * z));

		if (delta > 1.0) {
			return 0.0;
		}
		if (delta < -1.0) {
			return 180.0;
		}

		return Math.toDegrees(Math.acos(delta));
	}

	/**
	 * Computes the angle (in degrees) between the vector represented
	 * by this point and the vector represented by the specified point.
	 *
	 * @param   point the other vector
	 *
	 * @return  the angle between the two vectors measured in degrees,
	 *          {@code NaN} if any of the two vectors is a zero vector
	 * @throws  NullPointerException
	 *          if the specified {@code point} is null
	 */
	public double angle(Vector point) {
		return angle(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Computes the angle (in degrees) between the three points with this point
	 * as a vertex.
	 *
	 * @param   p1 one point
	 * @param   p2 other point
	 *
	 * @return  angle between the vectors (this, p1) and (this, p2) measured
	 *          in degrees, {@code NaN} if the three points are not different
	 *          from one another
	 * @throws  NullPointerException
	 *          if the {@code p1} or {@code p2} is null
	 */
	public double angle(Vector p1, Vector p2) {
		final double x = getX();
		final double y = getY();
		final double z = getZ();

		final double ax = p1.getX() - x;
		final double ay = p1.getY() - y;
		final double az = p1.getZ() - z;
		final double bx = p2.getX() - x;
		final double by = p2.getY() - y;
		final double bz = p2.getZ() - z;

		final double delta = (ax * bx + ay * by + az * bz) / Math.sqrt((ax * ax + ay * ay + az * az) * (bx * bx + by * by + bz * bz));

		if (delta > 1.0) {
			return 0.0;
		}
		if (delta < -1.0) {
			return 180.0;
		}

		return Math.toDegrees(Math.acos(delta));
	}

	/**
	 * Gets a new midpoint vector between this vector and another.
	 *
	 * @param   other The other vector
	 *
	 * @return  a new midpoint vector
	 */
	public Vector getMidpoint(Vector other) {
		double x = (this.x + other.x) / 2;
		double y = (this.y + other.y) / 2;
		double z = (this.z + other.z) / 2;
		return new Vector(x, y, z);
	}

	/**
	 * Calculates the dot product of this vector with another.  The dot product
	 * is defined as x1*x2+y1*y2+z1*z2.  The returned value is a scalar.
	 *
	 * @param   other The other vector
	 *
	 * @return  dot product
	 */
	public double dot(Vector other) {
		return x * other.x + y * other.y + z * other.z;
	}

	/**
	 * Calculates the cross product of this vector with another.  The cross
	 * product is defined as:
	 * <ul>
	 * <li>x = y1 * z2 - y2 * z1
	 * <li>y = z1 * x2 - z2 * x1
	 * <li>z = x1 * y2 - x2 * y1
	 * </ul>
	 *
	 * @param   o The other vector
	 *
	 * @return  the same vector
	 */
	public Vector crossProduct(Vector o) {
		double newX = y * o.z - o.y * z;
		double newY = z * o.x - o.z * x;
		double newZ = x * o.y - o.x * y;

		x = newX;
		y = newY;
		z = newZ;
		return this;
	}

	/**
	 * Converts this vector to a unit vector (a vector with length of 1).
	 *
	 * @return the same vector
	 */
	public Vector normalize() {
		double length = length();

		x /= length;
		y /= length;
		z /= length;

		return this;
	}

	/**
	 * Gets the floored value of the X component, indicating the voxel that
	 * this vector is contained with.
	 *
	 * @return floor of X
	 */
	public int getFloorX() {
		return MathUtil.floor(x);
	}

	/**
	 * Gets the floored value of the Y component, indicating the voxel that
	 * this vector is contained with.
	 *
	 * @return floor of y
	 */
	public int getFloorY() {
		return MathUtil.floor(y);
	}

	/**
	 * Gets the floored value of the Z component, indicating the voxel that
	 * this vector is contained with.
	 *
	 * @return floor of z
	 */
	public int getFloorZ() {
		return MathUtil.floor(z);
	}

	/**
	 * Checks to see if two objects are equal.
	 *
	 * Only two Vectors can ever return true. This method uses a fuzzy match
	 * to account for floating point errors. The epsilon can be retrieved
	 * with epsilon.
	 */
	@Override
	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof Vector)) {
			return false;
		}

		Vector other = (Vector) obj;

		return Math.abs(x - other.x) < epsilon
				&& Math.abs(y - other.y) < epsilon
				&& Math.abs(z - other.z) < epsilon
				&& getClass().equals(obj.getClass());
	}

	/**
	 * Returns a hash code for this vector.
	 *
	 * @return hash code
	 */
	@Override
	public int hashCode() {
		int hash = 7;

		hash = 79 * hash + (int) (Double.doubleToLongBits(x) ^ (Double.doubleToLongBits(x) >>> 32));
		hash = 79 * hash + (int) (Double.doubleToLongBits(y) ^ (Double.doubleToLongBits(y) >>> 32));
		hash = 79 * hash + (int) (Double.doubleToLongBits(z) ^ (Double.doubleToLongBits(z) >>> 32));
		return hash;
	}

	/**
	 * Get a new vector.
	 *
	 * @return vector
	 */
	@Override
	public Vector clone() {
		return new Vector(x, y, z);
	}

	/**
	 * Returns a string representation of this {@code Vector}.
	 *
	 * This method is intended to be used only for informational
	 * purposes.  The content and format of the returned string
	 * might vary between implementations.  The returned string
	 * might be empty but cannot be {@code null}.
	 */
	@Override
	public String toString() {
		return "Vector[x=" + x + ",y=" + y + ",z=" + z + "]";
	}

	/**
	 * Get the threshold used for {@link #equals(java.lang.Object)}.
	 *
	 * @return The epsilon
	 */
	public static double getEpsilon() {
		return epsilon;
	}

	/**
	 * Gets the minimum components of two vectors.
	 *
	 * @param   v1 The first vector
	 * @param   v2 The second vector
	 *
	 * @return  minimum
	 */
	public static Vector getMinimum(Vector v1, Vector v2) {
		return new Vector(Math.min(v1.x, v2.x), Math.min(v1.y, v2.y), Math.min(v1.z, v2.z));
	}

	/**
	 * Gets the maximum components of two vectors.
	 *
	 * @param   v1 The first vector
	 * @param   v2 The second vector
	 *
	 * @return  maximum
	 */
	public static Vector getMaximum(Vector v1, Vector v2) {
		return new Vector(Math.max(v1.x, v2.x), Math.max(v1.y, v2.y), Math.max(v1.z, v2.z));
	}

	/**
	 * Gets a random vector with components having a
	 * random value between {@code 0} and {@code 1}.
	 *
	 * @return A random vector
	 */
	public static Vector getRandom() {
		return new Vector(random.nextDouble(), random.nextDouble(), random.nextDouble());
	}

	public static Vector get2DCentroid(Vector... vectors) {
		double centroidX = 0, centroidZ = 0;

		for (Vector vector : vectors) {
			centroidX += vector.x;
			centroidZ += vector.z;
		}

		int len = vectors.length;
		return new Vector(centroidX / len, 0, centroidZ / len);
	}

	public static Vector get2DCentroid(int y, Vector... vectors) {
		double centroidX = 0, centroidZ = 0;

		for (Vector vector : vectors) {
			centroidX += vector.x;
			centroidZ += vector.z;
		}

		int len = vectors.length;
		return new Vector(
				centroidX / len,
				y,
				centroidZ / len
		);
	}

	public static Vector get3DCentroid(Vector... vectors) {
		double centroidX = 0, centroidY = 0, centroidZ = 0;

		for (Vector vector : vectors) {
			centroidX += vector.x;
			centroidY += vector.y;
			centroidZ += vector.z;
		}

		int len = vectors.length;
		return new Vector(
				centroidX / len,
				centroidY / len,
				centroidZ / len
		);
	}
}