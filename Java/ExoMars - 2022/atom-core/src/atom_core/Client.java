package atom_core;

import java.net.*;
import java.util.*;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

import java.nio.charset.StandardCharsets;
import java.io.*;


public class Client extends Setup{
	public static void grab() {
		
	}
	public static void drop() {
		
	}
	public static void exit() {
		
	}
	
	public static void start() throws IOException{
		Socket serverSocket = new Socket("10.0.1.2", 1500);
		//System.out.println("Connesso a " + serverSocket);
		String lastMove = "left";
		String inMsg;
		while(true) {
			LCD.clear();
			
			if(lastMove == "forw" || lastMove == "back")
				MovementInterface.move(1, lastMove, 26, RMotor, LMotor);
			else if(lastMove == "right" || lastMove == "left")
				MovementInterface.rotate(1, lastMove, 26, RMotor, LMotor);
			
			OutputStream outputStream = serverSocket.getOutputStream();
			DataOutputStream dout = new DataOutputStream(outputStream);
			
			dout.writeUTF("ok");
			dout.flush();
			
			BufferedReader inBuffer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream(), StandardCharsets.UTF_8));
			
			if (inBuffer.ready()) {
				inMsg = inBuffer.readLine();
				LCD.clear();
				LCD.drawString(inMsg, 0, 3);
				//Button.waitForAnyPress();
				if (inMsg == "grab") grab();
				else if (inMsg == "drop") drop();
				else if (inMsg == "exit") exit();
				else lastMove = inMsg;
			}
			
			//LCD.drawString("moving forw", 0, 3);
			
			if(Button.getButtons() == Button.ID_ESCAPE){
				serverSocket.close();
				break;
			}
			
			//inBuffer.close();

			//clientSocket.close();
		}
		
		//System.out.println("so uscito");
	}
}