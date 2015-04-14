import java.util.Stack;




public class Body {

	private double radius = 1;
	private double mass = 50000000000.0;
	
	private final static int DIMENSION = 3;
	private double[] deltaAccel = new double[DIMENSION];
	private double[] velocity = new double[DIMENSION];
	
	//take the assumption that the position is the center of the ball
	private double[] position = new double[DIMENSION];
	public double[] prevPosition = new double[DIMENSION];
	public int numCollisions = 0;
	private Stack<Body> _stackCollision = new Stack<Body>();
	private float _mySmallestFloatTime = 0;
	private int _mySmallestIntTime = 0;
	private double[] normalVector;
	private double[] tangentVector;
	public boolean _hasCollided = false;
	
	
	
	public Body(double radius,
			double velocity_x,
			double velocity_y,
			double zVelocity,
			double xPosition,
			double yP,
			double zP)
	{
		this.radius = radius;
		velocity[0] = velocity_x;
		velocity[1] = velocity_y;
		velocity[2] = zVelocity;
		position[0] = xPosition;
		position[1] = yP;
		position[2] = zP;
	}
	
	/**
	 * Returns this Body's current state in JSON representation
	 * @return
	 */
	public String toJson()
	{
		String ret = "{";
		
		ret += "position:";
		ret += "[";
		for(int i = 0; i < DIMENSION; i++)
			ret += String.valueOf(velocity[i]) + ",";
		ret += "],";
		
		ret += "radius:";
		ret += String.valueOf(radius);
		
		return ret + "}";
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

	public static double Magnitude(
		double[] vector,
		int dimension)
	{
		double sum = 0;
		for(int i = 0; i < dimension; i++)
			sum += vector[i] * vector[i];

		return Math.sqrt(sum);
	}
	
	public void updateTimestepAccel(Body other) {
		double d = distance(other);
		
		for(int i = 0; i < 3; i++) {
			double a = Constants.G * other.mass * (other.position[i] - position[i]);
			deltaAccel[i] += a / (d * d * d);
		}
	}
	
	public void move(double timeStep){
		for(int i = 0; i < 3; i++){
			prevPosition[i] = position[i];
			position[i] = position[i] + velocity[i] * timeStep + deltaAccel[i] * timeStep * timeStep * .5;
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
		float collide = 0; //?????
				
		
		return collide;
	}

	 public void computeNormal( Body b){
		 double magnitude = distance(b);
		 normalVector[0] = (this.getXPosition() - b.getXPosition()) / magnitude;
		 normalVector[1] = (this.getYPosition() - b.getYPosition()) / magnitude;
		 normalVector[2] = (this.getZPosition() - b.getZPosition()) / magnitude;
		 
	 }
	 
	 
	 // switches axis to make the x-axis along the normal vector between the two objects.
	 // only works for objects having same mass for now
	 public void computeCollision(Body other){
		numCollisions++;
		double[] myAngles = CalculateAngles(other);
		double[] hisAngles = other.CalculateAngles(this);
		double myVCenter = CalculateVelocityCenterMagnitude(myAngles[0]);
		double hisVCenter = CalculateVelocityCenterMagnitude(hisAngles[0]);
		
		double[] myCenter = new double[3];
		double[] hisCenter = new double[3];
		try {
			DecomposeVelocityCenter(myVCenter, myCenter, myAngles[1], myAngles[2], DIMENSION);
			DecomposeVelocityCenter(myVCenter, myCenter, myAngles[1], myAngles[2], DIMENSION);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// compute the normal vectors
		double temp = 0;
		double[] myNormal = new double[3];
		double[] hisNormal = new double[3];
		for(int i = 0; i < 3; i++){
			myNormal[i] = velocity[i] - myCenter[i];
			hisNormal[i] = other.velocity[i] - hisCenter[i];
		}
		// swap the center vectors
		for(int i = 0; i < 3; i++){
			temp = myCenter[i];
			myCenter[i] = hisCenter[i];
			hisCenter[i] = temp;
		}
		
		// compute the new normal vectors
		for(int i = 0; i < 3; i++){
			myNormal[i] = velocity[i] - myCenter[i];
			hisNormal[i] = other.velocity[i] - hisCenter[i];
		}
		
		// compute the new velocity vectors
		// this section needs to be synchronized
		// else we dont change the velocity of the other guy
		for(int i = 0; i < 3; i++){
			velocity[i] = myCenter[i] + myNormal[i];
			other.velocity[i] = hisCenter[i] + hisNormal[i];
		}
		
	}

	
	// returns double array 
	// index 0 is the 3D angle with other.
	// index 1 is the XZ angle
	// index 2 is the XY angle 
	public double[] CalculateAngles(
		Body other)
	{
		double toRet[] = new double[3];
		double distX = other.getXPosition() - this.getXPosition();
		double distY = other.getYPosition() - this.getYPosition();
		double distZ = other.getZPosition() - this.getZPosition();

		double numerator = 0.0;
		numerator += distX * velocity[0];
		numerator += distY * velocity[1];
		numerator += distZ * velocity[2];

		double denominator = 0.0;
		denominator += (distX + distY + distZ);
		denominator *= (velocity[0] + velocity[1] + velocity[2]);
		denominator = Math.sqrt(Math.abs(denominator));

		toRet[0] = Math.acos(numerator / denominator);
		toRet[1] = Math.asin(velocity[2] / velocity[0]);
		toRet[2] = Math.asin(velocity[1] / velocity[0]);
		
		
		return toRet;
	}

	public double CalculateVelocityCenterMagnitude(
		double xyzAngle)
	{
		return Magnitude(velocity, 3) * Math.cos(xyzAngle);
	}

	public static void DecomposeVelocityCenter(
		double velocityCenterMagnitude,
		double[] velocityCenterComponents,
		double XZAngle,
		double XYAngle,
		int dimension) throws Exception
	{
		if(dimension != DIMENSION)
			throw new Exception("dimension must match!");

		// x
		velocityCenterComponents[0] = velocityCenterMagnitude * Math.sin(XZAngle) * Math.cos(XYAngle);
		// y
		velocityCenterComponents[1] = velocityCenterMagnitude * Math.sin(XZAngle) * Math.sin(XYAngle);
		// z
		velocityCenterComponents[2] = velocityCenterMagnitude * Math.cos(XZAngle);
	}

	public void ComputeVelocityNormal(
		double[] velocityCenterComponents,
		double[] velocityNormalComponents,
		int dimension)
	{
		if(dimension != DIMENSION)
			try {
				throw new Exception("dimension must match!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		for(int i = 0; i < dimension; i++)
		velocityNormalComponents[i] = velocity[i] - velocityCenterComponents[i];
	}
	
	
	
}
