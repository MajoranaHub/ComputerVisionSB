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
	
	
	public static void grab() {
		Sound.beepSequenceUp();
		MovementInterface.rotate(10, "cw", 26, RMotor, LMotor);
		MovementInterface.move(10, "back", 26, RMotor, LMotor);
		MovementInterface.rotate(180, "cw", 26, RMotor, LMotor);
		ShovelMotor.rotateTo(200);
		ShovelMotor.rotateTo(-200);
		MovementInterface.rotate(90, "ccw", 26, RMotor, LMotor);
	}
	public static void drop() {
		
	}
	public static void exit() {
		
	}
	
	public static void start() throws IOException, InterruptedException{
		Socket serverSocket = new Socket("10.0.1.4", 1500);
		//System.out.println("Connesso a " + serverSocket);
		String lastMove = "ccw";
		String inMsg;
		while(true) {
			//TimeUnit.MILLISECONDS.sleep(30);
			//Delay.msDelay(10);
			LCD.clear();
			
			if(lastMove.equals("forw")) {
				//System.out.println(lastMove);
				LCD.drawString(lastMove, 0, 3);
				MovementInterface.move(1, lastMove, 8, RMotor, LMotor);
			}
			else if(lastMove.equals("cw") || lastMove.equals("ccw")) {
				//System.out.println(lastMove);
				LCD.drawString(lastMove, 0, 3);
				MovementInterface.rotate(1, lastMove, 8, RMotor, LMotor);
			}
			
			//lastMove = "ccw";
			
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
				else lastMove = inMsg.toString();
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
