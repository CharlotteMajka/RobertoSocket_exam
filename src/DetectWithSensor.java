import java.io.IOException;

import lejos.hardware.Sound;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RangeFinderAdapter;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class DetectWithSensor implements Behavior {
	private DifferentialPilot pilot;
	private EV3UltrasonicSensor USensor;
	private RangeFinderAdapter rfa;
	private RangeFinderAdapter rfaBack;
	private EV3MediumRegulatedMotor headMotor;
	private socket_singleton socket;
	private boolean suppressed = false;
	private double min = 45;
	private double max = 315;
	private int TURN_LEFT = -90;
	private int TURN_RIGHT = 90;
	private int TURN_BACK = 180;
	
	
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
		sendInfo("forhindring forud, finder ud af hvad jeg kan gøre");
		Sound.beepSequence();
			
		//look with back sensor
		back = rfaBack.getRange();
		if(back > 20)
		{pilot.travel(-15);}
		headMotor.setAcceleration(100);
		headMotor.setSpeed(100);
		
		// look Left
		turnHead(TURN_LEFT);
		left = rfa.getRange();
		turnHead(0);
		
		// look Right
		turnHead(TURN_RIGHT);
		right = rfa.getRange();
		turnHead(0);
		//look forward


		
		// find where there is most space
		float max  = Math.max(back, Math.max(right, left));
		if (max > 39) {
		if (max == back)
		{
				sendInfo("mest plads bagved, vender mig om");
				pilot.rotate(TURN_BACK);
				suppress();

		} else if 
			( max == left )
			{
			    sendInfo("mest plads til venstre, rotere til venstre");
				pilot.rotate(TURN_LEFT);
				suppress();
			}
			else if (max == right) {
				sendInfo("mest plads til højre, rotere til højre");
				pilot.rotate(TURN_RIGHT);
				suppress();
			}
		}
		else {
			sendInfo("vi er fucked");
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

	public void turnHead(int angle) {
		headMotor.rotateTo(angle, true);
		while(headMotor.isMoving() && !suppressed)
			Thread.yield();
		if(suppressed)
			{headMotor.rotateTo(0, true);
		return;}
	}
	
	@Override
	public void suppress() {
		suppressed = true;
		
	}
}
