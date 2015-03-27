import java.awt.Point;


public class Body {

	private double mass;
	private double xVelocity;
	private double yVelocity;
	private double zVelocity;
	private Point position;
	// should be a tuple, not sure how to make that happen w/o google
	private Point force;
	static double G;
	
	//TODO is it worthwhile to have
	//a storage of forces and distnaces from one body to all the others?
	public Body(double mass,
			double velocity_x,
			double velocity_y,
			double zVelocity,
			Point position )
	{
		this.mass = mass;
		this.zVelocity = zVelocity;
		this.xVelocity= velocity_x;
		this.yVelocity = velocity_y;
		this.position = position;
		
	}

	public void setZVelocity(double z){
		this.zVelocity = z;
	}
	
	public double getZvelocity(){
		return zVelocity;
	}
	
	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public double getXVelocity() {
		return xVelocity;
	}

	public void setXVelocity(double velocity_x) {
		this.xVelocity = velocity_x;
	}

	public double getYVelocity() {
		return yVelocity;
	}

	public void setYVelocity(double velocity_y) {
		this.yVelocity = velocity_y;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public Point getForce() {
		return force;
	}

	public void setForce(Point force) {
		this.force = force;
	}

	
	
	
}
