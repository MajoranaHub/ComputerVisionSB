package atom_core;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;

public class Menu {
	static EV3LargeRegulatedMotor mDx = new EV3LargeRegulatedMotor(MotorPort.A);	//ATTENZIONE ALLO STATIC
	static EV3LargeRegulatedMotor mSx = new EV3LargeRegulatedMotor(MotorPort.D);
	static EV3ColorSensor sensoreDx = new EV3ColorSensor(SensorPort.S1);	//LO STATIC POTREBBE DARE PROBLEMI
	static EV3ColorSensor sensoreSx = new EV3ColorSensor(SensorPort.S4);
	
	public static void main(String[] args) {
		LCD.drawString("MAIN INIZIATO", 0, 4);
		String[] opzioni;
		opzioni = new String[] {"inizia", "calibra"};
		int scelta = 0;
		
		LCD.drawString("PRE-LOOP", 0, 4);
		while(true) {																//sistema di menu basilare
			LCD.clear();
			LCD.drawString(opzioni[scelta], 0, 4);
			
			Button.waitForAnyPress();
			if(Button.getButtons() == Button.ID_DOWN && scelta == 0) {
				scelta++;
			} 
			else if(Button.getButtons() == Button.ID_UP && scelta == 1)
				{
				scelta--;
			}
			
			if(Button.getButtons() == Button.ID_ENTER) 
			{
				LCD.clear();
				if(scelta == 0) {Core.play();}
				else {Calibration.play();}
			}
			else if(Button.getButtons() == Button.ID_ESCAPE) {System.exit(0);}
			LCD.drawString("FINE ITERAZIONE", 0, 4);
		}
	}
}
