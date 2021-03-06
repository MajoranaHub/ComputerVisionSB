package atom_core;

import java.net.*;
import java.util.*;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

import java.util.concurrent.TimeUnit;
import java.nio.charset.StandardCharsets;
import java.io.*;


public class Client extends Setup{
	
	public static void exit2() {
		float distance;
		int angle;
		while (true) {
			Gyroscope.reset();
			while ( ( distance = UltrasonicUtils.getValue(DistanceUP) ) > 0.1) {
				MovementInterface.move(1, "forw", 26, RMotor, LMotor);
				if ( (angle = GyroscopeUtils.getValue(Gyroscope)) > 0) {
					MovementInterface.rotate(angle, "ccw", 26, RMotor, LMotor);
				}
				else if ( (angle = GyroscopeUtils.getValue(Gyroscope)) < 0) {
					MovementInterface.rotate(-angle, "ccw", 26, RMotor, LMotor);
				}
				if(ColorUtils.green(ColorUtils.getValues(LColor)) && ColorUtils.green(ColorUtils.getValues(RColor))) {
					MovementInterface.move(15, "forw", 26, RMotor, LMotor);
					Core.play("destra");
				}
			}
			MovementInterface.rotate(90, "cw", 26, RMotor, LMotor);
			distance = UltrasonicUtils.getValue(DistanceUP);
			if (distance > 0.3) {
				MovementInterface.move(15, "forw", 26, RMotor, LMotor);
				Core.play("destra");
			}
			else {
				MovementInterface.rotate(180, "cw", 26, RMotor, LMotor);
			}
		}
		
	}
	
	public static void grab() {
		Sound.beepSequenceUp();
		MovementInterface.rotate(10, "cw", 26, RMotor, LMotor);
		MovementInterface.move(10, "back", 26, RMotor, LMotor);
		MovementInterface.rotate(180, "cw", 26, RMotor, LMotor);
		ShovelMotor.rotateTo(200);
		ShovelMotor.rotateTo(-200);
		MovementInterface.rotate(180, "ccw", 26, RMotor, LMotor);
	}
	
	public static void drop() {
		Sound.beepSequence();
		MovementInterface.move(10, "back", 26, RMotor, LMotor);
		MovementInterface.rotate(90, "ccw", 26, RMotor, LMotor);
		ShovelMotor.rotateTo(100);
		DropMotor.rotateTo(30);
		Delay.msDelay(100);
		DropMotor.rotateTo(-30);
		ShovelMotor.rotateTo(-100);
		MovementInterface.rotate(90, "ccw", 26, RMotor, LMotor);
		MovementInterface.move(15, "forw", 26, RMotor, LMotor);
		MovementInterface.rotate(45, "cw", 26, RMotor, LMotor);
		MovementInterface.move(15, "forw", 26, RMotor, LMotor);
	}
	
	public static void exit() {
		Sound.buzz();
		MovementInterface.move(15, "forw", 26, RMotor, LMotor);
		Core.play("destra");
	}
	
	public static void start() throws IOException, InterruptedException{
		Socket serverSocket = new Socket("10.0.1.4", 1500);
		//System.out.println("Connesso a " + serverSocket);
		String lastMove = "ccw";
		String inMsg;
		while(true) {
			
			LCD.clear();
			
			if(lastMove.equals("forw")) {
				//System.out.println(lastMove);
				LCD.drawString(lastMove, 0, 3);
				MovementInterface.move(1, lastMove, 8, RMotor, LMotor);
			}
			else {
				//System.out.println(lastMove);
				LCD.drawString(lastMove, 0, 3);
				MovementInterface.rotate(1, lastMove, 8, RMotor, LMotor);
			}
			
			lastMove = "ccw";
			
			OutputStream outputStream = serverSocket.getOutputStream();
			DataOutputStream dout = new DataOutputStream(outputStream);
			
			dout.write("ok".getBytes());
			dout.flush();
			
			BufferedReader inBuffer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream(), StandardCharsets.UTF_8));
			
			if (inBuffer.ready()) {
				inMsg = inBuffer.readLine();
				LCD.clear();
				if (inMsg.equals("grab")) grab();
				else if (inMsg.equals("drop")) drop();
				else if (inMsg.equals("exit")) exit();
				else lastMove = inMsg;
			}
			
			
			if(Button.getButtons() == Button.ID_ESCAPE){
				dout.close();
				inBuffer.close();
				serverSocket.close();
				break;
			}
			

		}
		
	}
}
