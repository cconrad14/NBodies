import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;



public class Body {

	private double radius = 1;
	private double mass = 5000.0;
	private UUID _id = UUID.randomUUID();
	
	private final static int DIMENSION = 3;
	private double[] deltaAccel = new double[DIMENSION];
	public double[] velocity = new double[DIMENSION];
	
	//take the assumption that the position is the center of the ball
	private double[] position = new double[DIMENSION];
	public double[] prevPosition = new double[DIMENSION];
	public int numCollisions = 0;
	private Stack<Body> _stackCollision = new Stack<Body>();
	private ArrayList<Body> _stillIntersecting = new ArrayList<Body>();
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
	 * @throws JSONException 
	 */
	public JSONObject toJson() throws JSONException
	{
		JSONObject obj = new JSONObject();
		
		obj.put("x", position[0]);
		obj.put("y", position[1]);
		obj.put("z", position[2]);
		obj.put("radius", radius);
		obj.put("id", _id.toString());
		
		return obj;
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
			DecomposeVelocityCenter(hisVCenter, hisCenter, hisAngles[1], hisAngles[2], DIMENSION);
			
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
	 
	 // another take at computing the collisions
	 public void CcomputeCollision(Body b){
		 double distance = distance(b);
		 double massratio = b.mass/this.mass;
		 double[] dists = new double[3];
		 double[] diffV = new double[3];
		 double[] rotV = new double[3];
		 dists[0] = b.getXPosition() - this.getXPosition();
		 dists[1] = b.getYPosition() - this.getYPosition();
		 dists[2] = b.getZPosition() - this.getZPosition();
		 diffV[0] = b.getXVelocity() - this.getXVelocity();
		 diffV[1] = b.getYVelocity() - this.getYVelocity();
		 diffV[2] = b.getZvelocity() - this.getZvelocity();
		 
		 
		 // but "this" at origin, and boost so that b is resting
		 double relativeV = 0;
		 for(int i = 0; i < DIMENSION; i++){
		 	relativeV += (diffV[i] * diffV[i]);
		 	diffV[i] = velocity[i] - diffV[i];
		 }
		 
		 // do a rotation for "this" at the origin with  polar coordinates
		 relativeV = Math.sqrt(relativeV);
		 double phiV, thetaV, Zangle, theta2, phi2, sintheta, costheta, sinphi, cosphi;
		 theta2 = Math.acos(dists[2] / distance);
		 if(dists[0] == 0 && dists[1] == 0)
			 phi2 = 0.0;
		 else 
			 phi2 = Math.atan(dists[1] / dists[0]);
		 sintheta = Math.sin(theta2);
		 costheta = Math.cos(theta2);
		 sinphi = Math.sin(phi2);
		 cosphi = Math.cos(phi2);
		 
		 rotV[0] = costheta*sinphi*diffV[0] + costheta*sinphi*diffV[1] - sintheta*diffV[2];
		 rotV[1] = cosphi*diffV[1] - sinphi*diffV[0];
		 rotV[2] = sintheta*cosphi*diffV[0] + sinphi*diffV[1] + costheta*diffV[2];
		 
		 Zangle = rotV[2] / relativeV;
		 if(Zangle > 1)
			 Zangle = 1;
		 else if(Zangle < -1)
			 Zangle = -1;
		
		 thetaV = Math.acos(Zangle);
		 double centerV = distance * Math.sin(thetaV) / (this.radius + b.radius);
		 
		 if(rotV[0] == 0 && rotV[1] == 0)
			 phiV = 0;
		 else
			 phiV = Math.atan(rotV[1] / rotV[0]);
		 
		 
		 double alpha, beta, sinbeta, cosbeta, a, dvz2;
		 double[] mytemp = new double[3];
		 double[] btemp = new double[3];
		 alpha = Math.asin(-centerV);
		 a = Math.tan(thetaV+alpha);
		 beta = phiV;
		 sinbeta = Math.sin(beta);
		 cosbeta = Math.cos(beta);
		 dvz2 = 2*(rotV[2] + a*(cosbeta * rotV[0] + sinbeta*rotV[1])) / ((1+a*a) * (1+massratio));
		 
		 //update velocities
		 btemp[2] = dvz2;
		 btemp[0] = a *cosbeta*dvz2;
		 btemp[1] = a*sinbeta*dvz2;
		 mytemp[2] = rotV[2] - massratio*btemp[2];
		 mytemp[0] = rotV[0] - massratio*btemp[0];
		 mytemp[1] = rotV[1] - massratio*btemp[1];
		 
		 // rotate back to euclidean
		 velocity[0] = costheta*cosphi*mytemp[0] - sinphi*mytemp[1] + sintheta*cosphi*mytemp[2] + b.velocity[0];
		 velocity[1] = costheta*cosphi*mytemp[0] + cosphi*mytemp[1] + sintheta*sinphi*mytemp[2] + b.velocity[1];
		 velocity[2] = costheta*mytemp[2] - sintheta*mytemp[0] + b.velocity[2];
		 
		 b.velocity[0] = costheta*cosphi*btemp[0] - sinphi*btemp[1] + sintheta*cosphi*btemp[2] + b.velocity[0];
		 b.velocity[1] = costheta*cosphi*btemp[0] + cosphi*btemp[1] + sintheta*sinphi*btemp[2] + b.velocity[1];
		 b.velocity[2] = costheta*btemp[2] - sintheta*btemp[0] + b.velocity[2];
		 
		 
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
		if(denominator < numerator)
			denominator *= denominator;
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
	
	public void RemoveFromStillIntersecting(Body b) {
		if(_stillIntersecting.contains(b))
			_stillIntersecting.remove(b);
	}
	
	public void Collide(Body b)
	{
		if(_stillIntersecting.contains(b) || b._stillIntersecting.contains(this))
			return;
		
		// 1) Find C
		double dx = b.position[0] - position[0];
		double dy = b.position[1] - position[1];;
		double dz = b.position[2] - position[2];;
		
		// 2) Normalize C
		double mag = Math.pow(Math.sqrt(dx*dx + dy*dy + dz*dz), -1);
		dx *= mag;
		dy *= mag;
		dz *= mag;
		
		// 3) Project this.v onto c in coord frame where b.v = 0
		// create boost vector
		double boostX = velocity[0] - b.velocity[0];
		double boostY = velocity[1] - b.velocity[1];
		double boostZ = velocity[2] - b.velocity[2];
		
		// project boost onto c
		double projX = boostX * dx;
		double projY = boostY * dy;
		double projZ = boostZ * dz;
		
		// 4) do final assignment
		velocity[0] = boostX - projX + b.velocity[0];
		velocity[1] = boostY - projY + b.velocity[1];
		velocity[2] = boostZ - projZ + b.velocity[2];
		b.velocity[0] = projX + b.velocity[0];
		b.velocity[1] = projY + b.velocity[1];
		b.velocity[2] = projZ + b.velocity[2];
		
		_stillIntersecting.add(b);
		b._stillIntersecting.add(this);
	}
	
}
