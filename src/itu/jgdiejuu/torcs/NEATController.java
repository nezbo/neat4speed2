package itu.jgdiejuu.torcs;

import com.anji.integration.Activator;

public class NEATController extends Controller{
	
	private static final int MAX_JUMP = 100;
	
	private Activator activator;
	private double fitness = 0.0;
	private double lastLaps = 0.0;
	
	private double lastDiff = -1.0;
	private double lastDist = 0.0;
	private int lastTick = 0;
	private int tick = 0;
	private static final int TICKS_PER_SAVE = 125;

	public NEATController(Activator acti){
		this.activator = acti;
	}
	
	public void reset() {
		//System.out.println("Restarting the race!");
		
	}

	public void shutdown() {
		//System.out.println("Bye bye!");	
	}

	@Override
	public Action control(SensorModel sensors) {
		double dist = sensors.getDistanceFromStartLine();
		updateDiff(dist);
		storeFitness(dist);
		return convertOutput(activator.next(covertInput(sensors)));
	}

	private void updateDiff(double dist) {
		if(tick == lastTick + TICKS_PER_SAVE){
			lastTick = tick;
			lastDiff = Math.abs(dist - lastDist);
			lastDist = dist;
		}
		tick++;
	}

	private double[] covertInput(SensorModel sensors) {
		double[] result = new double[26];
		result[0] = normalizeAngle(sensors.getAngleToTrackAxis());
		// not used curLapTime
		// not used damage
		// not used distFromStart
		// not used distRaced
		// not used focus
		// not used fuel
		result[1] = normalizedGear(sensors.getGear());
		//not used lastLapTime
		double[] opp = normalizeOpponents(sensors.getOpponentSensors());
		result[2] = opp[0];
		result[3] = opp[1];
		result[4] = opp[2];
		result[5] = opp[3];
		result[6] = opp[4];
		result[7] = opp[5];
		result[8] = opp[6];
		result[9] = opp[7];
		// not used racePos
		result[10] = normalizeRPM(sensors.getRPM());
		result[11] = normalizeSpeed(sensors.getSpeed()); // speedX
		result[12] = normalizeSpeed(sensors.getLateralSpeed()); // speedY
		result[13] = normalizeSpeed(sensors.getZSpeed()); // speedZ
		double[] track = normalizeTrack(sensors.getTrackEdgeSensors());
		result[14] = track[0];
		result[15] = track[1];
		result[16] = track[2];
		result[17] = track[3];
		result[18] = track[4];
		result[19] = track[5];
		result[20] = track[6];
		result[21] = track[7];
		result[22] = track[8];
		result[23] = track[9];
		result[24] = normalizeTrackPos(sensors.getTrackPosition());
		// not used wheelSpinVel
		// not used z
		
		result[25] = 1.0;
		
		return result;
	}

	private double normalizeTrackPos(double pos) {
		return (pos + 1.0)/2.0;
	}

	private double[] normalizeTrack(double[] track) {
		double[] result = new double[10];
		for(int i = 0; i < result.length; i++){
			result[i] = track[i*2]/200.0;
		}
		return result;
	}

	private double normalizeSpeed(double speed) {
		return (speed+200)/400.0;
	}

	private double normalizeRPM(double rpm) {
		return (rpm-2000)/5000.0;
	}

	private double[] normalizeOpponents(double[] opp) {
		double[] result = new double[8];
		for(int i = 0; i < result.length; i++){
			result[i] = opp[i*4]/200.0;
		}
		return result;
	}

	private double normalizedGear(int gear) {
		return (gear+1)/8.0;
	}

	private double normalizeAngle(double angle) {
		return (angle+Math.PI)/(2*Math.PI);
	}

	private Action convertOutput(double[] output) {
		Action result = new Action();
		result.accelerate = clamp(output[0],0,1);
		result.brake = clamp(output[1],0,1);
		result.clutch = clamp(output[2],0,1);
		result.gear = getGear(output[3]);
		result.steering = normalizeSteering(output[4]);
		// not used focus
		// not used meta
		return result;
	}
	
	private double normalizeSteering(double steer) {
		return clamp(steer*2.0-1.0,-1,1);
	}

	private int getGear(double gear) {
		int result = (int)(gear*9.0-1.0);
		return result == 8 ? 7 : result;
	}

	private double clamp(double value, double min, double max){
		return Math.max(min, Math.min(max, value));
	}
	
	private void storeFitness(double newFit) {
		if(newFit > fitness && fitness + MAX_JUMP > newFit){ // legal progress
			fitness = newFit;
		}else if(newFit < MAX_JUMP && fitness > 5*MAX_JUMP){ // new lap
			lastLaps += fitness;
			fitness = newFit;
		}
	}

	public int getFitness() {
		System.out.println(">> Get fitness: "+(fitness+lastLaps));
		return (int)(fitness + lastLaps);
	}
	
	// Meters moved last 5 seconds.
	public double getDeltaFive(){
		return lastDiff;
	}
}
