import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class ServerBrick {

	private static final double TRACK_WIDTH = 12.0;
	private static final double WHEEL_DIAMETER = 5.6;
	
	public static void main(String[] args) {
		System.out.println("Waiting for client");
		Brick brick = BrickFinder.getDefault();
		try 
		(ServerSocket ss = new ServerSocket(5000);
		 Socket s = ss.accept();
				
		 DataInputStream dataIn = new DataInputStream(s.getInputStream());	
		 DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());		
		 EV3LargeRegulatedMotor left = new EV3LargeRegulatedMotor(brick.getPort("B"));
		 EV3LargeRegulatedMotor right = new EV3LargeRegulatedMotor(brick.getPort("D"));
			
		)
		{
			System.out.println("Client connected ");
			
			Wheel lWheel = WheeledChassis.modelWheel(left, WHEEL_DIAMETER).offset(TRACK_WIDTH/2);
			Wheel rWheel = WheeledChassis.modelWheel(right, WHEEL_DIAMETER).offset(-TRACK_WIDTH/2);
			
			WheeledChassis chassis = new WheeledChassis(new Wheel[] {lWheel, rWheel}, WheeledChassis.TYPE_DIFFERENTIAL);
			
			MovePilot pilot = new MovePilot(chassis);
			
			boolean done = false; 
			while (!done)
			{
				String command = dataIn.readUTF();
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
					done = true;
				}
				
				
				}
				
			System.out.println("Shutting program down");
		}
		catch ( Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
