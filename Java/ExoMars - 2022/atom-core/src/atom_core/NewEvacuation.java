package atom_core;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.utility.Delay;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class NewEvacuation
extends Setup
{
	static int timeConstant = 26;
	static int timeConstantMove = 120;
	static float[] colorInputR;
	static float[] colorInputL;
	static float distance = UltrasonicUtils.getValue(DistanceUP);
	//static int gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
	static boolean bianca = false, nera = false, blackInStart = false;
	static int redCounter;
	static int lastAngle = GyroscopeUtils.getValue(Gyroscope);
	static String lastMove;
	
	static boolean viola(float[] color)
	{
		if(color[0] <= 0.009 && color[1] <= 0.016 && color[2] <= 0.009) return true;
		return false;
	}
	
	public static void play() {
		
		//quando il robot si trova sullo start si posiziona nella prima colonna e inizia la serpentina
		Sound.playTone(300, 400);
		releaseResqueKit();
		Button.waitForAnyPress();
		Sound.playTone(300, 300);
		if(Button.getButtons() == Button.ID_ESCAPE) return;
		
		colorSensorRotator.rotateTo(0);
		colorSensorRotator.rotateTo(60);
		redCounter = 1;
		MovementInterface.move(33, "forw", timeConstantMove, RMotor, LMotor);
		MovementInterface.rotate(85, "cw", timeConstant, RMotor, LMotor);
		lilSnake();
		
	}
	
	public static void releaseResqueKit() {
		MovementInterface.move(2, "back", timeConstantMove, RMotor, LMotor);
		MovementInterface.rotate(85, "ccw", timeConstant, RMotor, LMotor);
		MovementInterface.move(45, "back", timeConstantMove, RMotor, LMotor);
		ShovelMotor.rotateTo(250);
		MovementInterface.move(45, "forw", timeConstantMove, RMotor, LMotor);
		MovementInterface.rotate(85, "cw", timeConstant, RMotor, LMotor);
		MovementInterface.move(5, "back", timeConstantMove, RMotor, LMotor);
		do {
			MovementInterface.move(1, "forw", timeConstantMove, RMotor, LMotor);
			colorInputR = ColorUtils.getValues(RColor);
			colorInputL = ColorUtils.getValues(LColor);
			if(Button.getButtons() == Button.ID_ESCAPE) return;
		}while(!ColorUtils.silver(colorInputL) && !ColorUtils.silver(colorInputR));
	}
	
	static void lilSnake() {
		while(Button.getButtons() != Button.ID_ESCAPE) {
			Gyroscope.reset();
			
			colorInputR = ColorUtils.getValues(RColor);
			colorInputL = ColorUtils.getValues(LColor);
			
			//radar con il sensore ultrasuoni
			LCD.drawString("radar", 0, 2);
			
			for(int i=0; i < 30; i++) {
				MovementInterface.rotate(1, "cw", timeConstant, RMotor, LMotor);
				distance = UltrasonicUtils.getValue(DistanceUP);
				if(distance <= 0.25) takeBall();
				if(Button.getButtons() == Button.ID_ESCAPE) return;
			}
			
			MovementInterface.rotate(30, "ccw", timeConstant, RMotor, LMotor);
			
			for(int i=0; i < 30; i++) {
				MovementInterface.rotate(1, "ccw", timeConstant, RMotor, LMotor);
				distance = UltrasonicUtils.getValue(DistanceUP);
				if(distance <= 0.25) takeBall();
				if(Button.getButtons() == Button.ID_ESCAPE) return;
			}
			
			MovementInterface.rotate(30, "cw", timeConstant, RMotor, LMotor);
			
			for(int i=0; i < 10; i++) {
				MovementInterface.move(1, "forw", timeConstantMove, RMotor, LMotor);
				colorInputR = ColorUtils.getValues(RColor);
				colorInputL = ColorUtils.getValues(LColor);
				if(ColorUtils.red(colorInputL) || ColorUtils.red(colorInputR)) gira();
				if(Button.getButtons() == Button.ID_ESCAPE) return;
			}
		}
	}
	
	static void gira() {
		if(redCounter % 2 == 0) {
			MovementInterface.move(10, "back", timeConstantMove, RMotor, LMotor);
			MovementInterface.rotate(85, "cw", timeConstant, RMotor, LMotor);
			MovementInterface.move(35, "forw", timeConstantMove, RMotor, LMotor);
			MovementInterface.rotate(85, "cw", timeConstant, RMotor, LMotor);
			redCounter++;
		}
		else {
			MovementInterface.move(10, "back", timeConstantMove, RMotor, LMotor);
			MovementInterface.rotate(85, "ccw", timeConstant, RMotor, LMotor);
			MovementInterface.move(30, "forw", timeConstantMove, RMotor, LMotor);
			MovementInterface.rotate(85, "ccw", timeConstant, RMotor, LMotor);
			redCounter++;
		}
	}

	static void takeBall() {
		float lastDistance;
		while(true){
			Delay.msDelay(100);
			lastDistance = UltrasonicUtils.getValue(DistanceUP);
			MovementInterface.rotate(1, "ccw", timeConstant, RMotor, LMotor);
			Delay.msDelay(100);
			distance = UltrasonicUtils.getValue(DistanceUP);
			if(lastDistance < distance) break;
			if(Button.getButtons() == Button.ID_ESCAPE) return;
		}
		
		do {
			MovementInterface.rotate(1, "cw", timeConstant, RMotor, LMotor);
			Delay.msDelay(100);
			distance = UltrasonicUtils.getValue(DistanceUP);
			if(Button.getButtons() == Button.ID_ESCAPE) return;
		}while(distance != lastDistance);
		
		//MovementInterface.rotate(2, "ccw", timeConstant, RMotor, LMotor);
		
		//il robot si avvicina alla pallina
		while(distance > 0.07) {
			MovementInterface.move(1, "forw", timeConstantMove, RMotor, LMotor);
			distance = UltrasonicUtils.getValue(DistanceUP);
			if(Button.getButtons() == Button.ID_ESCAPE) return;
		}
		
		MovementInterface.rotate(20, "ccw", timeConstant, RMotor, LMotor);
		lastAngle = GyroscopeUtils.getValue(Gyroscope); //prende l'angolo
		
		//colorSensorRotator.rotateTo(70); //da provare
		LCD.clear();
		Delay.msDelay(100);
		MovementInterface.move(5, "forw", timeConstantMove, RMotor, LMotor);
		/*
		if(ColorUtils.black(colorInputR)) LCD.drawString("Nero", 0, 3);
	    else if(ColorUtils.green(colorInputR)) LCD.drawString("Verde", 0, 3);
	    else if(ColorUtils.silver(colorInputR)) LCD.drawString("Argento", 0, 3);
	    else if(ColorUtils.red(colorInputR)) LCD.drawString("Rosso", 0, 3);
	    else if(ColorUtils.white(colorInputR)) LCD.drawString("Bianco", 0, 3);*/
		
		for(int i=0; i < 3; i++) {
			Delay.msDelay(200);
			colorInputR = ColorUtils.getValues(RColor);
			if(ColorUtils.black(colorInputR)) LCD.drawString("Nero", 0, 3+i);
		    else if(ColorUtils.green(colorInputR)) LCD.drawString("Verde", 0, 3+i);
		    else if(ColorUtils.silver(colorInputR)) LCD.drawString("Argento", 0, 3+i);
		    else if(ColorUtils.red(colorInputR)) LCD.drawString("Rosso", 0, 3+i);
		    else if(viola(colorInputR)) LCD.drawString("Viola", 0, 7);
		    else if(ColorUtils.white(colorInputR)) LCD.drawString("Bianco", 0, 3+i);
			MovementInterface.move(1, "forw", timeConstant, RMotor, LMotor);
			Delay.msDelay(200);
		}
		
		
		if(viola(colorInputR) && bianca && !nera) {
			captureBall();
			nera = true;
			placeBall("black");
		}
		else if(viola(colorInputR) && !bianca && !nera) {
			captureBall();
			bringToStart();
		}
		else{
			captureBall();
			bianca = true;
			placeBall("silver");	
		}
	}
	
	static void captureBall() {
		ShovelMotor.rotateTo(200);
		MovementInterface.move(5, "back", timeConstantMove, RMotor, LMotor);
		MovementInterface.rotate(175, "cw", timeConstant, RMotor, LMotor);
		MovementInterface.move(10, "back", timeConstantMove, RMotor, LMotor);
		ShovelMotor.rotateTo(-250);
		MovementInterface.rotate(175, "cw", timeConstant, RMotor, LMotor);
		
		//con l'ausilio del giroscopio la macchina si rimette dritta
		if(lastAngle < 0) MovementInterface.rotate(lastAngle, "cw", timeConstant, RMotor, LMotor);
		else MovementInterface.rotate(lastAngle, "cw", timeConstant, RMotor, LMotor);
		
		Delay.msDelay(400);
	}
	
	static void bringToStart() {
		if(redCounter % 2 == 1)
		{
			MovementInterface.rotate(170, "ccw", timeConstant, RMotor, LMotor);
			
			do {
				MovementInterface.move(1, "forw", timeConstantMove, RMotor, LMotor);
				colorInputR = ColorUtils.getValues(RColor);
				colorInputL = ColorUtils.getValues(LColor);
				if(Button.getButtons() == Button.ID_ESCAPE) return;
			}while(!ColorUtils.red(colorInputL) && !ColorUtils.red(colorInputR));
			
			MovementInterface.move(15, "back", timeConstantMove, RMotor, LMotor); //da provare   (5)
			MovementInterface.rotate(85, "ccw", timeConstant, RMotor, LMotor);
			
			do {
				MovementInterface.move(1, "forw", timeConstantMove, RMotor, LMotor);
				colorInputR = ColorUtils.getValues(RColor);
				colorInputL = ColorUtils.getValues(LColor);
				if(Button.getButtons() == Button.ID_ESCAPE) return;
			}while(!ColorUtils.silver(colorInputL) && !ColorUtils.silver(colorInputR));
			
			//rilascia pallina
			MovementInterface.move(10, "forw", timeConstantMove, RMotor, LMotor);
			MovementInterface.rotate(170, "ccw", timeConstant, RMotor, LMotor);
			MovementInterface.rotate(25, "cw", timeConstant, RMotor, LMotor);
			MovementInterface.move(15, "back", timeConstantMove, RMotor, LMotor);
			ShovelMotor.rotateTo(200);
			
			MovementInterface.rotate(20, "ccw", timeConstant, RMotor, LMotor);
			MovementInterface.move(25, "forw", timeConstantMove, RMotor, LMotor);
			
		}
		else if(redCounter % 2 == 0)
		{
			MovementInterface.rotate(170, "ccw", timeConstant, RMotor, LMotor);
			
			do {
				MovementInterface.move(1, "forw", timeConstantMove, RMotor, LMotor);
				colorInputR = ColorUtils.getValues(RColor);
				colorInputL = ColorUtils.getValues(LColor);
				if(Button.getButtons() == Button.ID_ESCAPE) return;
			}while(!ColorUtils.red(colorInputL) && !ColorUtils.red(colorInputR));
			
			MovementInterface.rotate(85, "cw", timeConstant, RMotor, LMotor);
			
			if(redCounter == 2) MovementInterface.move(30, "forw", timeConstantMove, RMotor, LMotor);
			else if(redCounter == 4) MovementInterface.move(90, "forw", timeConstantMove, RMotor, LMotor);
			
			MovementInterface.rotate(85, "cw", timeConstant, RMotor, LMotor);
			
			//la macchina va verso lo start
			
			do {
				MovementInterface.move(1, "forw", timeConstantMove, RMotor, LMotor);
				colorInputR = ColorUtils.getValues(RColor);
				colorInputL = ColorUtils.getValues(LColor);
				if(Button.getButtons() == Button.ID_ESCAPE) return;
			}while(!ColorUtils.red(colorInputL) && !ColorUtils.red(colorInputR));
			
			MovementInterface.move(5, "back", timeConstantMove, RMotor, LMotor);
			MovementInterface.rotate(85, "ccw", timeConstant, RMotor, LMotor);
			
			do {
				MovementInterface.move(1, "forw", timeConstantMove, RMotor, LMotor);
				colorInputR = ColorUtils.getValues(RColor);
				colorInputL = ColorUtils.getValues(LColor);
				if(Button.getButtons() == Button.ID_ESCAPE) return;
			}while(!ColorUtils.silver(colorInputL) && !ColorUtils.silver(colorInputR));
			
			//rilascia la pallina
			MovementInterface.move(10, "forw", timeConstantMove, RMotor, LMotor);
			MovementInterface.rotate(170, "ccw", timeConstant, RMotor, LMotor);
			MovementInterface.rotate(25, "cw", timeConstant, RMotor, LMotor);
			MovementInterface.move(2, "forw", timeConstantMove, RMotor, LMotor);
			ShovelMotor.rotateTo(200);
			
			MovementInterface.rotate(20, "ccw", timeConstant, RMotor, LMotor);
			MovementInterface.move(20, "forw", timeConstantMove, RMotor, LMotor);
		}
		
		blackInStart = true;
		getBacktoSnake();
		
	}


	static void placeBall(String ballColor) {
		
		if(redCounter % 2 == 1) MovementInterface.rotate(170, "cw", timeConstant, RMotor, LMotor);
		
		if(redCounter > 1) {
			MovementInterface.rotate(85, "ccw", timeConstant, RMotor, LMotor);
			MovementInterface.move(30, "forw", timeConstantMove, RMotor, LMotor);
			MovementInterface.rotate(85, "cw", timeConstant, RMotor, LMotor);
		}
		
		//cerca il bordo rosso
		do {
			MovementInterface.move(1, "forw", timeConstantMove, RMotor, LMotor);
			colorInputR = ColorUtils.getValues(RColor);
			colorInputL = ColorUtils.getValues(LColor);
			if(Button.getButtons() == Button.ID_ESCAPE) return;
		}while(!ColorUtils.red(colorInputL) && !ColorUtils.red(colorInputR));
		
		MovementInterface.move(5, "back", timeConstantMove, RMotor, LMotor);
		MovementInterface.rotate(85, "ccw", timeConstant, RMotor, LMotor);
		
		//cerca il grigio
		do {
			MovementInterface.move(1, "forw", timeConstantMove, RMotor, LMotor);
			colorInputR = ColorUtils.getValues(RColor);
			colorInputL = ColorUtils.getValues(LColor);
			if(Button.getButtons() == Button.ID_ESCAPE) return;
		}while(!ColorUtils.silver(colorInputL) && !ColorUtils.silver(colorInputR));
		
		MovementInterface.move(25, "forw", timeConstantMove, RMotor, LMotor);
		MovementInterface.rotate(85, "ccw", timeConstant, RMotor, LMotor);
		
		if(ballColor == "silver")
		{	
			//rilascia pallina argentata
			MovementInterface.move(45, "forw", timeConstantMove, RMotor, LMotor);
			MovementInterface.rotate(170, "cw", timeConstant, RMotor, LMotor);
			ShovelMotor.rotateTo(200);
			if(blackInStart) {
				placeBlack();
			}
		}
		else if(ballColor == "black")
		{
			//rilascia pallina nera
			MovementInterface.move(20, "forw", timeConstantMove, RMotor, LMotor);
			MovementInterface.rotate(170, "cw", timeConstant, RMotor, LMotor);
			ShovelMotor.rotateTo(200);
		}
		
		//il robot torna allo start 
		do {
			MovementInterface.move(1, "forw", timeConstantMove, RMotor, LMotor);
			colorInputR = ColorUtils.getValues(RColor);
			colorInputL = ColorUtils.getValues(LColor);
			if(Button.getButtons() == Button.ID_ESCAPE) return;
		}while(!ColorUtils.red(colorInputL) && !ColorUtils.red(colorInputR));
		
		MovementInterface.move(5, "back", timeConstantMove, RMotor, LMotor);
		MovementInterface.rotate(85, "cw", timeConstant, RMotor, LMotor);
		MovementInterface.move(30, "forw", timeConstantMove, RMotor, LMotor);
		
		getBacktoSnake();
	}
	
	//metodo che piazza la pallina nera che si trova nello start
	static void placeBlack() {
		MovementInterface.rotate(170, "cw", timeConstant, RMotor, LMotor);
		MovementInterface.move(30, "back", timeConstantMove, RMotor, LMotor);
		MovementInterface.rotate(30, "ccw", timeConstant, RMotor, LMotor);
		MovementInterface.move(10, "back", timeConstantMove, RMotor, LMotor);
		ShovelMotor.rotateTo(-250);
		MovementInterface.move(10, "forw", timeConstantMove, RMotor, LMotor);
		MovementInterface.rotate(30, "cw", timeConstant, RMotor, LMotor);
		MovementInterface.move(20, "forw", timeConstantMove, RMotor, LMotor);
		MovementInterface.rotate(170, "cw", timeConstant, RMotor, LMotor);
		ShovelMotor.rotateTo(200);
		Button.waitForAnyPress();
	}
	
	static void getBacktoSnake() {

		if(redCounter == 1) 
		{
			MovementInterface.rotate(85, "cw", timeConstant, RMotor, LMotor);
		}
		else if(redCounter == 2)
		{
			MovementInterface.rotate(85, "cw", timeConstant, RMotor, LMotor);
			do {
				MovementInterface.move(1, "forw", timeConstantMove, RMotor, LMotor);
				colorInputR = ColorUtils.getValues(RColor);
				colorInputL = ColorUtils.getValues(LColor);
				if(Button.getButtons() == Button.ID_ESCAPE) return;
			}while(!ColorUtils.red(colorInputL) && !ColorUtils.red(colorInputR));
			MovementInterface.rotate(85, "ccw", timeConstant, RMotor, LMotor);
			MovementInterface.move(30, "forw", timeConstantMove, RMotor, LMotor);
			MovementInterface.rotate(85, "ccw", timeConstant, RMotor, LMotor);
		}
		else if(redCounter == 3)
		{
			MovementInterface.move(60, "forw", timeConstantMove, RMotor, LMotor);
			MovementInterface.rotate(85, "cw", timeConstant, RMotor, LMotor);
		}
		else if(redCounter == 4)
		{
			MovementInterface.rotate(85, "cw", timeConstant, RMotor, LMotor);
			do {
				MovementInterface.move(1, "forw", timeConstantMove, RMotor, LMotor);
				colorInputR = ColorUtils.getValues(RColor);
				colorInputL = ColorUtils.getValues(LColor);
				if(Button.getButtons() == Button.ID_ESCAPE) return;
			}while(!ColorUtils.red(colorInputL) && !ColorUtils.red(colorInputR));
			MovementInterface.rotate(85, "ccw", timeConstant, RMotor, LMotor);
			MovementInterface.move(90, "forw", timeConstantMove, RMotor, LMotor);
			MovementInterface.rotate(85, "ccw", timeConstant, RMotor, LMotor);
		}
		
		lilSnake();
	}
}
