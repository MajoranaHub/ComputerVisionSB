//PROGRAMMA DI TEST
package atom_core;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

class Core
  extends Setup
{ 
  public static void play()
  {
	 Button.waitForAnyPress();
	 int zoneRotation = 79;
	 
	 while(Button.getButtons() != Button.ID_ESCAPE)
	 {
		 float distance;
		 float wallDistance = 0.2f;
		 String direction;
		 
		 MovementInterface.rotate(zoneRotation, "ccw", RMotor, LMotor);
		distance = UltrasonicUtils.getValue(Distance);
		
		if(distance < wallDistance)
		{
			direction = "cw";
			MovementInterface.rotate(zoneRotation, "cw", RMotor, LMotor);
		}
		else direction = "ccw";
		
		//Loop serpentellall
		boolean ripeti = true;
		boolean uscita = false;
		
		Sound.buzz();
		
		while(ripeti)
		{
			//Ruota nella direzione e leggi se c'è il muro
			MovementInterface.move(35, "forw", RMotor, LMotor);
			MovementInterface.rotate(zoneRotation, direction, RMotor, LMotor);
			
			//Controllo fine pista
			distance = UltrasonicUtils.getValue(Distance);
			if(distance < wallDistance)
			{
				ripeti = false;
				continue;
			}
			
			MovementInterface.move(100, "back", RMotor, LMotor);
			MovementInterface.rotate(zoneRotation, direction, RMotor, LMotor);

			if(Button.getButtons() == Button.ID_ESCAPE) break;
			
			do {
				MovementInterface.move(20, "back", RMotor, LMotor);
				
				distance = UltrasonicUtils.getValue(Distance);
				uscita = Button.getButtons() == Button.ID_ESCAPE;
				if(uscita) break;
			} while(distance > wallDistance);
			
			if(uscita) break;
			
			if(direction == "cw") direction = "ccw";
			else direction = "cw";
		}
	 }
  }
}
