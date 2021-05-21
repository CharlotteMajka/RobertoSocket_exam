import lejos.hardware.Sound;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RangeFinderAdapter;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class DetectWithSensor  implements Behavior {
	private DifferentialPilot pilot;
	private EV3UltrasonicSensor USensor;
	private RangeFinderAdapter rfa;
	private RangeFinderAdapter rfaBack;
	private EV3MediumRegulatedMotor headMotor;
	private boolean suppressed = false;
	private double min = 45;
	private double max = 315;
	
	public DetectWithSensor(DifferentialPilot _pilot, RangeFinderAdapter _rfa,  RangeFinderAdapter _rfaBack, EV3MediumRegulatedMotor _headmotor) {
		pilot = _pilot;
		rfa = _rfa;
		rfaBack = _rfaBack;
		headMotor = _headmotor;
	}
	
	

	@Override
	public boolean takeControl() {
		boolean takecon = false;
		if(rfa.getRange() < 35)
			takecon = true;
			suppressed = false;
		
		return takecon;

		
	}

	@Override
	public void action() {
		suppressed = false;
		float left;
		float right;
		float back;
		System.out.println("vi er i detect");
		Sound.beepSequence();
			//look around 
		back = rfaBack.getRange();
		if(back > 20)
		{pilot.travel(-15);}
		headMotor.setAcceleration(100);
		headMotor.setSpeed(100);
		headMotor.rotate(-90);
		/*while(pilot.isMoving() && !suppressed)
		{   
		    Thread.yield();

		}*/
		left = rfa.getRange();
		headMotor.rotate(180);
		/*while(pilot.isMoving() && !suppressed)
		{   
		    Thread.yield();
		}*/
		right = rfa.getRange();
		headMotor.rotate(-90);
		/*while(pilot.isMoving() && !suppressed)
			Thread.yield();*/
		if (left < 50 && right < 50 )
		{
		/*while(pilot.isMoving() && !suppressed)
		{   
		    Thread.yield();

		}*/
			float backwards;
			backwards = rfaBack.getRange();
			if(backwards > 50) {
				System.out.println("der er plads bagved");
			}
			/*while(pilot.isMoving() && !suppressed)
			{   
			    Thread.yield();

			}*/
		} else if 
			( left > right)
			{
				System.out.println("go left");
				suppress();
			}
			else {
				System.out.println("go right");
				suppress();
			}
		 
		}
		
		
	
	

	@Override
	public void suppress() {
		suppressed = true;
		
	}
}
