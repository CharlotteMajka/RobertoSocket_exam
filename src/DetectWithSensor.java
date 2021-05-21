import java.io.IOException;

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
	private socket_singleton socket;
	private boolean suppressed = false;
	private double min = 45;
	private double max = 315;
	
	public DetectWithSensor(socket_singleton _socket, DifferentialPilot _pilot, RangeFinderAdapter _rfa,  RangeFinderAdapter _rfaBack, EV3MediumRegulatedMotor _headmotor) {
		pilot = _pilot;
		rfa = _rfa;
		rfaBack = _rfaBack;
		headMotor = _headmotor;
		socket = _socket;
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

		left = rfa.getRange();
		headMotor.rotate(180);

		right = rfa.getRange();
		headMotor.rotate(-90);

		
		float max  = Math.max(back, Math.max(right, left));
		
			
		if (left < 50 && right < 50 )
		{

			float backwards;
			backwards = rfaBack.getRange();
			if(backwards > 35) {
				sendInfo("der er plads bag ved");
				System.out.println("der er plads bagved");
				suppress();
			}

		} else if 
			( left > right)
			{
			    sendInfo("go left");
				System.out.println("go left");
				suppress();
			}
			else {
				sendInfo("go right");
				System.out.println("go right");
				suppress();
			}
		 
		}
		
		
	private void sendInfo(String info) {
		try {
			socket.dataOut.writeUTF(info);
			socket.dataOut.flush();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	

	@Override
	public void suppress() {
		suppressed = true;
		
	}
}
