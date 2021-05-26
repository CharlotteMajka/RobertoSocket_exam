import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RangeFinderAdapter;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class ServerBrick {

	private static final double TRACK_WIDTH = 12.0;
	private static final double WHEEL_DIAMETER = 5.6;
	
	public static void main(String[] args) {
		System.out.println("Waiting for client");
		Brick brick = BrickFinder.getDefault();
		try 
		(/*ServerSocket ss = new ServerSocket(5000);
		 Socket s = ss.accept();
				
		 DataInputStream dataIn = new DataInputStream(s.getInputStream());	
		 DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());
		 */
		 socket_singleton socket = socket_singleton.getSocketInstance();
		 EV3LargeRegulatedMotor left = new EV3LargeRegulatedMotor(brick.getPort("B"));
		 EV3LargeRegulatedMotor right = new EV3LargeRegulatedMotor(brick.getPort("D"));
		 EV3UltrasonicSensor Usensor = new EV3UltrasonicSensor(brick.getPort("S2"));
		 EV3UltrasonicSensor UsensorBack = new EV3UltrasonicSensor(brick.getPort("S3"));
		 EV3MediumRegulatedMotor headMotor = new EV3MediumRegulatedMotor(brick.getPort("A"));
		
				
		)
		{
			System.out.println("Client connected ");
			
			//Wheel lWheel = WheeledChassis.modelWheel(left, WHEEL_DIAMETER).offset(TRACK_WIDTH/2);
			//Wheel rWheel = WheeledChassis.modelWheel(right, WHEEL_DIAMETER).offset(-TRACK_WIDTH/2);
			
			//WheeledChassis chassis = new WheeledChassis(new Wheel[] {lWheel, rWheel}, WheeledChassis.TYPE_DIFFERENTIAL);
			//MovePilot pilot = new MovePilot(chassis);
			RangeFinderAdapter rfa = new RangeFinderAdapter(Usensor.getDistanceMode());
			RangeFinderAdapter rfaBack = new RangeFinderAdapter(UsensorBack.getDistanceMode());
			DifferentialPilot pilot = new DifferentialPilot(WHEEL_DIAMETER, TRACK_WIDTH, left, right);
			
			Behavior b1 = new RemoteControl(pilot, socket, rfa);
			Behavior b2 = new DetectWithSensor(socket, pilot, rfa, rfaBack, headMotor);
			Behavior b3 = new EscapeButton(socket);
			Behavior [] bArray = {b1, b2, b3};
			Arbitrator arbi = new Arbitrator(bArray);
			arbi.go();
				
			System.out.println("Shutting program down");
		}
		catch ( Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
