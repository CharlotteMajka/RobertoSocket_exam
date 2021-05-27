import java.io.IOException;

import javax.swing.JOptionPane;

import lejos.hardware.Power;
import lejos.robotics.RangeFinderAdapter;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class RemoteControl implements Behavior {
	private DifferentialPilot pilot;
	private socket_singleton socket;
	private RangeFinderAdapter rfa;
	private Power battery;
	
	private float battery_needcharge = (float) 6.8;
	private float battery_low = (float) 7.5;
	private float battery_full = (float) 8.5;
	
	private boolean suppressed = false;
	private boolean done = false;
	private int SLOWDOWN = 25;
	private int ACCELERATIONDOWN = 30;
	private int SPEEDUP = 50;
	private int ACCELERATIONUP = 75;
	private int SPEED;
	private int ACCELERATION;
	private int NEAR_SOMETHING = 50;
	
	public RemoteControl(DifferentialPilot _pilot, socket_singleton _socket, RangeFinderAdapter _rfa, Power _battery) {
		pilot = _pilot;
		socket = _socket;
		rfa = _rfa;
		battery = _battery;
	}
	
	public void doCommands() throws Exception  {	
		pilot.setLinearAcceleration(ACCELERATION);
		pilot.setLinearSpeed(SPEED);

		String command = socket.dataIn.readUTF();
		System.out.println(command);

		switch (command) {
		case "forward":
			pilot.forward();
			break;
		case "turnleft":
			pilot.rotate(1000, true);
			break;
		case "turnright":
			pilot.rotate(-1000, true);
			break;
		case "backward":
			pilot.backward();
			break;
		case "stop":
			pilot.stop();
			break;
		case "shutdown":
			pilot.stop();
			System.exit(0);
		}
		
	

}
	public void slowDown() {
		
		if ( rfa.getRange() <= NEAR_SOMETHING )
		{System.out.println("slow down");
			SPEED = SLOWDOWN;
			ACCELERATION = ACCELERATIONDOWN;
		}
		
	}
	public void speedUp() {
		
		if ( rfa.getRange() > NEAR_SOMETHING )
		{System.out.println("Speed up");
			SPEED = SPEEDUP;
			ACCELERATION = ACCELERATIONUP;
		}
		
	}

	public void batteryLevel() {
		float batterylevel = battery.getVoltage();
		if (batterylevel <= battery_needcharge)
		{
			sendInfo("Batteri: ▯▯▯ - Oplad nu! " + batterylevel);
		}
		if (batterylevel > battery_needcharge && batterylevel <= battery_low)
		{
			sendInfo("Batteri: ▮▯▯ " + batterylevel);
		}
		if (batterylevel > battery_low && batterylevel <= battery_full)
		{
			sendInfo("Batteri: ▮▮▯ "  + batterylevel);
		}
		if (batterylevel >= battery_full)
		{
			sendInfo("Batteri: ▮▮▮ "  + batterylevel);
		}

	}
	
	private void sendInfo(String level) {
		try {
			socket.dataOut.writeUTF(level);
			socket.dataOut.flush();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	
	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		suppressed = false;
		
		while(!suppressed)
			try {
				batteryLevel();
				slowDown();
				speedUp();
				doCommands();
				
			} catch (Exception e) {
			
				e.printStackTrace();
			}
		
	}

	
	
	@Override
	public void suppress() {

		suppressed = true; 
		
	}
	


}
