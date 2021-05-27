import java.io.IOException;

import lejos.hardware.Power;
import lejos.robotics.subsumption.Behavior;

public class BatteryLevel implements Behavior {
	private socket_singleton socket;
	private Power battery;
	private boolean suppressed = false;
	private float battery_needcharge = (float) 6.8;
	private float battery_low = (float) 7.2;
	private float battery_okay = (float) 7.8;
	private float battery_full = (float) 8.5;
	
	public BatteryLevel(socket_singleton _socket, Power _battery) {
		socket = _socket;
		battery = _battery;
	}
	
	@Override
	public boolean takeControl() {
		boolean takecon = false;
		float batterylevel = battery.getVoltage();
		if (batterylevel <= battery_needcharge)
		{
			sendInfo("Batteri niveau: Oplad nu!");
		}
		if (batterylevel <= battery_low)
		{
			sendInfo("Batteri niveau: Lavt");
		}
		if (batterylevel >= battery_okay)
		{
			sendInfo("Batteri niveau: Okay");
		}
		if (batterylevel >= battery_full)
		{
			sendInfo("Batteri niveau: Fuld");
		}
		return takecon;
	}

	@Override
	public void action() {

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
	public void suppress() {
		suppressed = true;
		
	}

}
