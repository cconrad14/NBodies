import java.util.Stack;




public class Body {

	private double radius = 1;
	private double mass = 1;
	
	private double[] deltaAccel = new double[3];
	private double[] velocity = new double[3];
	
	//take the assumption that the position is the center of the ball
	private double[] position;
	public double[] nextPosition = new double[3];
	
	private Stack<Body> _stackCollision = new Stack<Body>();
	
	public Body(double radius,
			double velocity_x,
			double velocity_y,
			double zVelocity,
			double[] position )
	{
		this.radius = radius;
		velocity[0] = velocity_x;
		velocity[1] = velocity_y;
		velocity[2] = zVelocity;
		this.position = position;
		
	}
	
	
	public double[] getAccel(){
		return deltaAccel;
	}

	public void setZVelocity(double z){
		velocity[2] = z;
	}
	
	public double getZvelocity(){
		return velocity[2];
	}
	
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getXVelocity() {
		return velocity[0];
	}

	public void setXVelocity(double velocity_x) {
		this.velocity[0] = velocity_x;
	}

	public double getYVelocity() {
		return velocity[1];
	}

	public void setYVelocity(double velocity_y) {
		this.velocity[1] = velocity_y;
	}

	public double getXPosition() {
		return position[0];
	}

	public void setXPosition(double p) {
		position[0] = p;
	}
	
	public double getYPosition() {
		return position[1];
	}

	public void setYPosition(double p) {
		position[1] = p;
	}
	
	public double getZPosition() {
		return position[2];
	}

	public void setZPosition(double p) {
		position[2] = p;
	}
	
	public double distance(Body other) {
		double xDiff = position[0] - other.position[0];
		double yDiff = position[1] - other.position[1];
		double zDiff = position[2] - other.position[2];		
		return Math.sqrt(xDiff*xDiff + yDiff*yDiff + zDiff*zDiff);
	}
	
	public void updateTimestepAccel(Body other) {
		double d = distance(other);
		
		for(int i = 0; i < 3; i++) {
			double a = Constants.G * other.mass * (position[i] - other.position[i]);
			deltaAccel[i] += a / (d * d * d);
		}
	}
	
	public void move(){
		for(int i = 0; i < 3; i++){
			nextPosition[i] = position[i] + velocity[i] + deltaAccel[i] / 2;
			velocity[i] += deltaAccel[i];
		}
	}
	
	public void pushBodyOnCollisionStack(Body other) {
		_stackCollision.push(other);
	}
	
	public Body popBodyOffCollisionStack() {
		return _stackCollision.pop();
	}
	
	public float ComputeCollisionTime(
			Body b1,
			Body b2) {
		float collide;
		
		
		
		return collide;
	}
}
