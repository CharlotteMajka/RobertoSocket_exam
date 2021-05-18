import java.io.IOException;

import lejos.robotics.RangeFinderAdapter;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class RemoteControl implements Behavior {
	private DifferentialPilot pilot;
	private socket_singleton socket;
	private RangeFinderAdapter rfa;
	
	private boolean suppressed = false;
	private boolean done = false;
	private int SLOWDOWN = 25;
	private int NEAR_SOMETHING = 50;
	
	public RemoteControl(DifferentialPilot _pilot, socket_singleton _socket, RangeFinderAdapter _rfa) {
		pilot = _pilot;
		socket = _socket;
		rfa = _rfa;
	}
	
	public void doCommands() throws Exception  {	
		pilot.setLinearAcceleration(75);
		pilot.setLinearSpeed(50);

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
			pilot.setLinearSpeed(SLOWDOWN);
			pilot.setLinearAcceleration(SLOWDOWN);
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
				doCommands();
				slowDown();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	@Override
	public void suppress() {

		suppressed = true; 
		
	}
	


}
