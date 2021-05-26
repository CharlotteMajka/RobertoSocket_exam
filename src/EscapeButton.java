import java.io.IOException;

import lejos.hardware.Brick;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.Sound;
import lejos.robotics.subsumption.Behavior;

public class EscapeButton implements Behavior {
	 private boolean suppressed = false;
	 private socket_singleton socket;
	 private  boolean Takecontrol = false;
	 
	public EscapeButton(socket_singleton _socket) {
		socket = _socket;
		
		
		Button.ESCAPE.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(Key k) {
			Takecontrol=true;
			}
			
			@Override
			public void keyPressed(Key k) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	public boolean takeControl() {
		if(Takecontrol == true)
			return Takecontrol;
		else return false;
		
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
	public void action() {
		suppressed = false;
		Sound.twoBeeps();
		sendInfo("SHUTDOWN");
		System.exit(0);
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}
