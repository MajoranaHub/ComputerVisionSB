//PROGRAMMA DI TEST
package atom_core;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.port.MotorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class Setup
{
  static EV3LargeRegulatedMotor RMotor = new EV3LargeRegulatedMotor(MotorPort.A);
  static EV3LargeRegulatedMotor LMotor = new EV3LargeRegulatedMotor(MotorPort.D);
  static EV3MediumRegulatedMotor UltrasonicMotor = new EV3MediumRegulatedMotor(MotorPort.B);
  
  static EV3ColorSensor RColor = new EV3ColorSensor(SensorPort.S1);
  static EV3UltrasonicSensor Distance = new EV3UltrasonicSensor(SensorPort.S3);
  static EV3ColorSensor LColor = new EV3ColorSensor(SensorPort.S4);
  
  public static void main(String[] args)
  {		  
	Button.LEDPattern(3);
    String[] options = { "Inizia", "Calibra", "Muovi" };
    int option = 0;
    
    RMotor.setSpeed(300);//value changed 200
    LMotor.setSpeed(300);
    UltrasonicMotor.setSpeed(600);
    UltrasonicMotor.stop();
    
    while(true)
    {
    	LCD.clear();
    	LCD.drawString(options[option], 0, 4);
    	
    	switch(Button.waitForAnyPress())
    	{
    	case Button.ID_UP :
    		if(option == 0) option = 1;
    		else option--;
    		break;
    		
    	case Button.ID_DOWN :
    		if(option == 1) option = 0;
    		else option++;
    		break;
    		
    	case Button.ID_ENTER :
    		LCD.clear();
    		switch(option)
    		{
    		case 0 :
    			Core.play();
    			break;
    			
    		case 1 :
    			Calibration.play();
    			break;
    		} 
    		break;
    	
    	case Button.ID_ESCAPE :
    		System.exit(0);
    		break;
    	}
    }
  }
}
