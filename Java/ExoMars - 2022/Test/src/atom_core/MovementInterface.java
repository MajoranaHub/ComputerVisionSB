//PROGRAMMA DI TEST
package atom_core;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

//Movements need to be calibrated with a standard motor speed
public class MovementInterface 
	extends Setup
{	
	static void rotate(int degrees, String mode, EV3LargeRegulatedMotor RMotor, EV3LargeRegulatedMotor LMotor)
	  { 
		//Works if RMotor.getSpeed() == LMotor.getSpeed()
		//SE NON FUNZIONA RISETTARE IL VALORE DI TIMECONSTANT A 23!
		final int timeConstant = 26;
		
		if(mode != "cw" && mode != "ccw")
		{
			LCD.drawString("Rotation argument is wrong!", 2, 0);
			Sound.buzz();
		}
		
		switch (mode)
		{
	  	case "ccw": 
		  LMotor.backward();
		  RMotor.forward();
		  break;
		  
	  	case "cw": 
		  LMotor.forward();
		  RMotor.backward();
		  break;
	    }
	    
	    Delay.msDelay(degrees * timeConstant);
	    RMotor.flt(true);
	    LMotor.flt(true);
	  }
	  
	  static void move(int mm, String direction, EV3LargeRegulatedMotor RMotor, EV3LargeRegulatedMotor LMotor)
	  {    
	    final int timeConstant = 26;//value changed 18
	    
	    if(direction != "forw" && direction != "back")
	    {
	    	LCD.drawString("Movement argument is wrong!", 2, 0);
	    	Sound.buzz();
	    }
	    
	    switch (direction)
	    {
	    case "forw": 
	      RMotor.forward();
	      LMotor.forward();
	      break;
	    case "back": 
	      RMotor.backward();
	      LMotor.backward();
	    }
	    
	    Delay.msDelay(mm * timeConstant);
	    RMotor.flt(true);
	    LMotor.flt(true);
	  }
}
