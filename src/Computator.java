
public class Computator {
	//how to exponent in java?

		double G = .0000000000667; // units and things!
		
		//the force will be directly toward each other
	public double calcForce(Body b1, Body b2){
		
		double radius = calcDistance(b1, b2);
		double force = G * b1.getMass() * b2.getMass() / (radius * radius);
		return force;
	}
	
	public double calcDistance(Body b1, Body b2){
		
		double distance = Math.sqrt((b1.getPosition().x - b2.getPosition().x) ^ 2 + (b1.getPosition().y - b2.getPosition().y));
		
		return distance;
	}
	
	
	// method will take in two bodies that are colliding and change the resulting velocities of each body
	// treat the center of the plane as the 0,0 point to give us an indication of negative velocities and positions
	public void twoBodyCollision(Body b1, Body b2){
		//following conservation of momentum we will use p1i + p2i = p1f + p2f
		//TODO are we adding the feature of giving different masses?
		
		double zSum = b1.getZvelocity() + b2.getZvelocity();
		double xSum = b1.getXVelocity() + b2.getXVelocity();
		double ySum = b1.getYVelocity() + b2.getYVelocity();
	}
	
}
