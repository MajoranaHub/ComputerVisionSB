//backup dell'iniziale funzione core!

/*package atom_core;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.Button;

public class Core2 {
	public static void main(String[] args) {
		boolean premuto = false;
		EV3LargeRegulatedMotor m1 = new EV3LargeRegulatedMotor(MotorPort.A);
		EV3LargeRegulatedMotor m2 = new EV3LargeRegulatedMotor(MotorPort.B);
		LCD.drawString("PREMI INVIO", 0, 4);
		
		while(!premuto) {
			if(Button.getButtons() == Button.ID_ENTER) {
				premuto = true;
				LCD.clear();
			}
		}
		
		while(premuto) {
			LCD.drawString("ATOM TEAM", 0, 4);		//Test avvio programma			
			m1.setSpeed(900);
			m2.setSpeed(900);
			
			m1.rotate(2000, true);
			m2.rotate(2000, true);
			
			Button.waitForAnyPress();
			
			if(Button.getButtons() == Button.ID_ESCAPE) {
				LCD.drawString("FIGOOO", 0, 4);
				m1.close();			//Termina l'uso della risorsa
				m2.close();
				System.exit(0);
			}
		}
	}
}
*/