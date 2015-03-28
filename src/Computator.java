import java.util.ArrayList;

public class Computator extends Thread {
	// how to exponent in java?

	static ArrayList<Body> _bodies;
	
	static {
		_bodies = new ArrayList<Body>();
	}

	private int timeSteps;
	private int currentStep = 0;
	private int myID;
	private int numWorkers;
	private boolean collision = false;
	private static TimeKeeper time = new TimeKeeper();

	public Computator(int timeSteps, int id, int workers) {
		this.timeSteps = timeSteps;
		myID = id;
		numWorkers = workers;
	}

	// the force will be directly toward each other
	public double calcForce(Body b1, Body b2) {

		double radius = calcDistance(b1, b2);
		double force = Constants.G / (radius * radius);
		
		return force;
	}

	public void run() {

		for (; currentStep < timeSteps; currentStep++) {
			update();
			checkForCollision();
			barrier();
			while(collision) {
				
			}
			
		}
	}

	public double calcDistance(Body b1, Body b2) {
		double xDiff = b1.getXPosition() - b2.getXPosition();
		xDiff = xDiff * xDiff;
		double yDiff = b1.getYPosition() - b2.getYPosition();
		yDiff = yDiff * yDiff;
		double zDiff = b1.getZPosition() - b2.getZPosition();
		zDiff = zDiff * zDiff;
		double distance = xDiff + yDiff + zDiff;
		distance = Math.sqrt(distance);

		return distance;
	}

	private void update() {
		int mine = _bodies.size() / numWorkers;
		int lowerBody, upperBody;
		lowerBody = mine * myID;

		if (myID == numWorkers - 1) {
			upperBody = _bodies.size();
		} else {
			upperBody = lowerBody + mine;
		}
		for (int i = lowerBody; i < upperBody; i++) {
			
		}

	}
	
	public void findCollisionLocation(){
		
	}
	
	public void calcCollision(){
		
	}

	
	// checks for a collision with the closest object to it
	// if true, then calls
	public boolean checkForCollision() {

		return false;
	}

	public void calculatePosition(Body b) {

	}

	private void barrier() {

	}

	// method will take in two bodies that are colliding and change the
	// resulting velocities of each body
	// treat the center of the plane as the 0,0 point to give us an indication
	// of negative velocities and positions
	public void twoBodyCollision(Body b1, Body b2) {
		// following conservation of momentum we will use p1i + p2i = p1f + p2f
		// TODO are we adding the feature of giving different masses?

		double zSum = b1.getZvelocity() + b2.getZvelocity();
		double xSum = b1.getXVelocity() + b2.getXVelocity();
		double ySum = b1.getYVelocity() + b2.getYVelocity();
	}

}
