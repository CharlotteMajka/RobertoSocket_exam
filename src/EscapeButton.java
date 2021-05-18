import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.robotics.subsumption.Behavior;

public class EscapeButton implements Behavior {
	 private boolean suppressed = false;
	 
	public EscapeButton() {
	}
	
	@Override
	public boolean takeControl() {
		return Button.ESCAPE.isDown();
	}

	@Override
	public void action() {
		suppressed = false;
		Sound.twoBeeps();
		System.exit(0);
		
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}
