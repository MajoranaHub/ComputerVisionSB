package atom_core;

import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.port.MotorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.device.NXTCam;

public class Setup
{
  //Dichiarazioni variabili per l'interfaccia con i motori ed i sensori
  static EV3LargeRegulatedMotor RMotor = new EV3LargeRegulatedMotor(MotorPort.D);
  static EV3LargeRegulatedMotor LMotor = new EV3LargeRegulatedMotor(MotorPort.A);
  static EV3LargeRegulatedMotor ShovelMotor = new EV3LargeRegulatedMotor(MotorPort.B);
  static EV3MediumRegulatedMotor DropMotor = new EV3MediumRegulatedMotor(MotorPort.C); // --> gara classica
  //static EV3MediumRegulatedMotor colorSensorRotator = new EV3MediumRegulatedMotor(MotorPort.C); // --> gara in "DAD"
  
  //static NXTCam Pixi = new NXTCam(SensorPort.S2);
  static EV3GyroSensor Gyroscope = new EV3GyroSensor(SensorPort.S3);
  static EV3UltrasonicSensor DistanceUP;
  static EV3ColorSensor RColor = new EV3ColorSensor(SensorPort.S4);
  static EV3ColorSensor LColor = new EV3ColorSensor(SensorPort.S1);
  
  public static void main(String[] args)
  {
	//Ciclo di inizializzazione forzata del sensore ultrasuoni per evitare il bug in cui non viene rilevato
	do {
		try {
			DistanceUP = new EV3UltrasonicSensor(SensorPort.S2);
			break;
		}
		catch (IllegalArgumentException i)
		{
			LCD.drawString("Ultrasonic loop Sensor Up", 0, 3);
		}
	} while(true);
	
	LCD.clear();
	  
	Button.LEDPattern(3);
    String[] options = {"Inizia", "Mezzo", "Sinistra", "Calibra", "Test", "Client" };
    int option = 0;
    
    //Configurazione iniziale dei motori
    RMotor.setSpeed(250);
    LMotor.setSpeed(250);
    DropMotor.setSpeed(50); // --> motore gara classica
    //colorSensorRotator.setSpeed(50); // --> motore per gara del 2021
    ShovelMotor.setSpeed(300);
    String[] pippo = {"1"};
    
    //Ciclo del menu principale
    while(true)
    {
    	LCD.clear();
    	LCD.drawString(options[option], 0, 4);
    	
    	//Struttura del menu
    	switch(Button.waitForAnyPress())
    	{
    	case Button.ID_UP :
    		if(option == 0) option = 5;
    		else option--;
    		break;
    		
    	case Button.ID_DOWN :
    		if(option == 5) option = 0;
    		else option++;
    		break;
    		
    	case Button.ID_ENTER :
    		LCD.clear();
    		switch(option)
    		{
	    		case 0 :
	    			Core.play("destra");
	    			break;
	    			
	    		case 1 :
	    			Core.play("mezzo");
	    			break;
	    			
	    		case 2 :
	    			Core.play("sinistra");
	    			break;
	    			
	    		case 3 :
	    			Calibration.play();
	    			break;
	    			
	    		case 4 :
	    			Test.play();
	    			break;
	    			
	    		case 5 :
	    			try {
						Client.start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						LCD.clear();
						LCD.drawString(e.toString(), 0, 3);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	    			break;
    		} 
    		
    	
    	case Button.ID_ESCAPE :
    		System.exit(0);
    		break;
    	}
    }
  }
}
