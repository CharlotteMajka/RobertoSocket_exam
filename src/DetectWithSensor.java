import lejos.hardware.Sound;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RangeFinderAdapter;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class DetectWithSensor  implements Behavior {
	private DifferentialPilot pilot;
	private EV3UltrasonicSensor USensor;
	private RangeFinderAdapter rfa;
	private boolean suppressed = false;
	private double min = 45;
	private double max = 315;
	
	public DetectWithSensor(DifferentialPilot _pilot, RangeFinderAdapter _rfa) {
		pilot = _pilot;
		rfa = _rfa;
	}
	
	

	@Override
	public boolean takeControl() {
		boolean takeCon = false; 
		if(rfa.getRange() < 40  && rfa.getRange() > 20)
			takeCon = true;
		
		return takeCon;
		//return rfa.getRange() < 40;
		
	}

	@Override
	public void action() {
		suppressed = false;
		
		System.out.println("vi er i detect");
		Sound.beepSequence();
		pilot.setLinearAcceleration(50);
		pilot.setLinearSpeed(30);
		pilot.setAngularAcceleration(75);
		pilot.setAngularSpeed(50);
		while(!suppressed) {
		if(rfa.getRange() < 30)
			pilot.setLinearSpeed(15);
		else if(rfa.getRange()< 10)
			pilot.stop();
		else 
			while(pilot.isMoving() && !suppressed)
				Thread.yield();
		}
		
	
	}

	@Override
	public void suppress() {
		suppressed = true;
		
	}
}
