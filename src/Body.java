


public class Body {

	private double radius;
	private double[] velocity = new double[3];	
	//take the assumption that the position is the center of the ball
	private double[] position = new double[3];
	
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
