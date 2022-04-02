package atom_core;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.port.MotorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.utility.Delay;

public class Test 
extends Setup{
	static int timeConstant = 15;
	static int timeConstantMove = 110;
	static float ultrasonicInput = UltrasonicUtils.getValue(DistanceUP);
	static double distance;
	static int cicli;
	static float[] colorR; 
	static float[] colorL;
	static boolean direction = true;
	 
	static int gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
	static int gyroscopeInputCheck;
	static String lato = "";
	static boolean evacuationZonefound = false;
	
	public static void play()
	{
		NewEvacuation.play();
	}
	
}