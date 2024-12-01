import java.awt.*;
import java.util.*;
import javax.management.RuntimeErrorException;

public class Particle {
	private String _name;
	private double _x, _y;
	private double _vx, _vy;
	private double _radius;
	private double _lastUpdateTime;

	/**
	 * Helper method to parse a string into a Particle.
	 * DO NOT MODIFY THIS METHOD
	 * @param str the string to parse
	 * @return the parsed Particle
	 */
	public static Particle build (String str) {
		String[] tokens = str.split("\\s+");
		double[] nums = Arrays.stream(Arrays.copyOfRange(tokens, 1, tokens.length))
				      .mapToDouble(Double::parseDouble)
				      .toArray();
		return new Particle(tokens[0], nums[0], nums[1], nums[2], nums[3], nums[4]);
	}

	/**
	 * @name name of the particle (useful for debugging)
	 * @param x x-coordinate of the particle
	 * @param y y-coordinate of the particle
	 * @param vx x-velocity of the particle
	 * @param vy y-velocity of the particle
	 * @param radius radius of the particle
	 */
	Particle (String name, double x, double y, double vx, double vy, double radius) {
		_name = name;
		_x = x;
		_y = y;
		_vx = vx;
		_vy = vy;
		_radius = radius;
	}

	/**
	 * Draws the particle as a filled circle.
	 * DO NOT MODIFY THIS METHOD
	 */
	void draw (Graphics g) {
		g.fillOval((int) (_x - _radius), (int) (_y - _radius), (int) (2 * _radius), (int) (2 * _radius));
	}

	/**
	 * Useful for debugging.
	 */
	public String toString () {
		return (_name.equals("") ? "" : _name + " ") + _x + "  " + _y + " " + _vx + " " + _vy + " " + _radius;
	}

	/**
	 * Updates the position of the particle after an elapsed amount of time, delta, using
	 * the particle's current velocity.
	 * @param delta the elapsed time since the last particle update
	 */
	public void update (double delta, int width) {
		double newX = _x + delta * _vx;
		double newY = _y + delta * _vy;
		//System.out.println("newX: "+newX);
		//System.out.println("newY: "+newY);
		//System.out.println("oohh"+(width-_radius));

		_x = newX;
		_y = newY;
		/* 
		if(Double.compare(newX,(width-_radius))>=0)
			_x=width-_radius;
		else if (Double.compare(newX,_radius)<=0)
			_x = _radius;
		else 
			_x = newX;

		
		if(Double.compare(newY,(width-_radius))>=0)
			_y=width-_radius;
		else if (Double.compare(newY,_radius)<=0){
			_y = _radius;
		}
		else 
			_y = newY;*/
		//System.out.println("x: "+_x);
		//System.out.println("y: "+_y);
	}

	/**
	 * Returns the horizontal velocity
	 * @return the horizontal velocity
	 */
	public double getVX(){
		return _vx;
	}

	/**
	 * Returns the vertical velocity
	 * @return the vertical velocity
	 */
	public double getVY(){
		return _vy;
	}

	/**
	 * Returns the name
	 * @return the name
	 */
	public String getName(){
		return _name;
	}

	/**
	 * Returns the last update time 
	 * @return the vertical last update time 
	 */
	public double getLastUpdate(){
		return _lastUpdateTime;
	}

	/**
	 * 
	 */
	private double wallCollisionTime( double width, double velocity, double currentPos){
		double timeToCollision;
		if (Double.compare(velocity,0)>0)
			timeToCollision = ((width-_radius)-currentPos)/velocity;
		else if (Double.compare(velocity,0)<0)
			timeToCollision = ((_radius)-currentPos)/velocity;
		else
			timeToCollision = Double.POSITIVE_INFINITY;
		return timeToCollision;
	}


	/**
	 * returns the time a particle will collide with a wall
	 * @param width of the box
	 * @return the time a particle will collide with a wall
	 */
	public double wallXCollisionTime(double now, double width) throws RuntimeErrorException{
		double timeX;
		double SMALL = 1e-6;
		/* 
		if (Double.compare(_vx, 0) > 0) {
			timeX = (width-(_x + _radius)) / _vx;
		}
		else if (Double.compare(_vx, 0) == 0){
			timeX = Double.POSITIVE_INFINITY;
		}
		else {
			timeX = -(_x - _radius) / _vx; // gives negative value when radius greater than distance to the wall
		}*/

		timeX = wallCollisionTime(width, _vx, _x);

		if (Double.compare(timeX, 0) >= 0){
			return timeX + now;
		} else if (Double.compare(timeX, 0) < 0)
			return now;
		throw new RuntimeException();
	}

	
	/**
	 * returns the time a particle will collide with a wall
	 * @param width of the box
	 * @return the time a particle will collide with a wall
	 */
	public double wallYCollisionTime(double now, double width)  throws RuntimeErrorException {
		double timeY;
		double SMALL = 1e-6;
		//double threshhold = -0.0000000
		//double negativeZero = -0.0;
		//double positiveZero = 0.0;
		/* 
		if (Double.compare(_vy, 0) > 0) {
			timeY = (width - _y - _radius) / _vy;
		} else if (Double.compare(_vy, 0) == 0) {
			timeY = Double.POSITIVE_INFINITY;
		}else {
			timeY = -(_y - _radius) / _vy;
		}*/

		timeY = wallCollisionTime(width, _vy, _y);
		//System.out.println(timeY);
		if (Double.compare(timeY, 0) >= 0) {
			return timeY + now;
		} else if (Double.compare(timeY, 0) < 0) {
			return now;}
	throw new RuntimeException();
	}



	/**
	 * returns the time a particle will collide with a wall
	 * @param width of the box
	 * @return the time a particle will collide with a wall
	 */
	public void updateWallCollision(double now, double width){
		// takes in the times where the collisions with walls should happen
		double timeX = wallXCollisionTime(now, width);
		double timeY = wallYCollisionTime(now, width);

		//negates the velocities if the particle is currently hitting a walll
		if(Double.compare(timeX, now) == 0) {
			_vx *= -1;
		} 
		if (Double.compare(timeY, now) == 0) {
			_vy *= -1;
		}
		
		//updates when the particle was last altered
		_lastUpdateTime = now;
	}


	/**
	 * Updates both this particle's and another particle's velocities after a collision between them.
	 * DO NOT CHANGE THE MATH IN THIS METHOD
	 * @param now the current time in the simulation
	 * @param other the particle that this one collided with
	 */
	public void updateAfterCollision (double now, Particle other) {
		double vxPrime, vyPrime;
		double otherVxPrime, otherVyPrime;
		double common = ((_vx - other._vx) * (_x - other._x) + 
				 (_vy - other._vy) * (_y - other._y)) /
			     (Math.pow(_x - other._x, 2) + Math.pow(_y - other._y, 2));
		vxPrime = _vx - common * (_x - other._x);
		vyPrime = _vy - common * (_y - other._y);
		otherVxPrime = other._vx - common * (other._x - _x);
		otherVyPrime = other._vy - common * (other._y - _y);

		_vx = vxPrime;
		_vy = vyPrime;
		other._vx = otherVxPrime;
		other._vy = otherVyPrime;

		_lastUpdateTime = now;
		other._lastUpdateTime = now;
	}

	/**
	 * Computes and returns the time when (if ever) this particle will collide with another particle,
	 * or infinity if the two particles will never collide given their current velocities.
	 * DO NOT CHANGE THE MATH IN THIS METHOD
	 * @param other the other particle to consider
	 * @return the time with the particles will collide, or infinity if they will never collide
	 */
	public double getCollisionTime (Particle other) {
		// See https://en.wikipedia.org/wiki/Elastic_collision#Two-dimensional_collision_with_two_moving_objects
		double a = _vx - other._vx;
		double b = _x - other._x;
		double c = _vy - other._vy;
		double d = _y - other._y;
		double r = _radius;

		double A = (a * a) + (c * c);
		double B = 2 * ((a * b) + (c * d));
		double C = (b * b) + (d * d) - (4 * r * r);

		// Numerically more stable solution to QE.
		// https://people.csail.mit.edu/bkph/articles/Quadratics.pdf
		double t1, t2;
		if (B >= 0) {
			t1 = (-B - Math.sqrt((B * B) - (4 * A * C))) / (2 * A);
			t2 = 2*C / (-B - Math.sqrt((B * B) - (4 * A * C)));
		} else {
			t1 = 2*C / (-B + Math.sqrt((B * B) - (4 * A * C)));
			t2 = (-B + Math.sqrt((B * B) - (4 * A * C))) / (2 * A);
		}

		// Require that the collision time be slightly larger than 0 to avoid
		// numerical issues.
		double SMALL = 1e-6;
		double t;
		if (t1 > SMALL && t2 > SMALL) {
			t = Math.min(t1, t2);
		} else if (t1 > SMALL) {
			t = t1;
		} else if (t2 > SMALL) {
			t = t2;
		} else {
			// no collision
			t = Double.POSITIVE_INFINITY;
		}

		return t;
	}
}
