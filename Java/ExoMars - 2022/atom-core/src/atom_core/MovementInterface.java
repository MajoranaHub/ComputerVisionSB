package atom_core;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

//Movements need to be calibrated with a standard motor speed
public class MovementInterface 
	extends Setup
{	
	static void rotate(int degrees, String mode, int timeConstant, EV3LargeRegulatedMotor RMotor, EV3LargeRegulatedMotor LMotor)
	  { 
		
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
		
		default :
		  LCD.drawString("Rotation argument is wrong!", 2, 0);
		  Sound.buzz();
	      Button.waitForAnyPress();
		  LCD.clear();
		  break;
	    }
	    
		//Qui i gradi di rotazione sono convertiti in millisecondi
	    Delay.msDelay(degrees * timeConstant);
	    //Alla fine dei movimenti i motori sono lasciati in folle
	    RMotor.flt(true);
	    LMotor.flt(true);
	  }
	  
	  static void move(int mm, String direction, int timeConstant, EV3LargeRegulatedMotor RMotor, EV3LargeRegulatedMotor LMotor)
	  { 
		//Questa funzione serve ad effettuare i movimenti lineari
		//Il concetto dietro questa costante ? lo stesso presente nella funzione rotate sopra

	    switch (direction)
	    {
	    case "forw": 
	      RMotor.forward();
	      LMotor.forward();
	      break;
	      
	    case "back": 
	      RMotor.backward();
	      LMotor.backward();
	      break;
	      
	    default :
	      LCD.drawString("Move argument is wrong!", 2, 0);
		  Sound.buzz();
		  Button.waitForAnyPress();
		  LCD.clear();
	      break;
	    }
	    
	    Delay.msDelay(mm * timeConstant);
	    RMotor.flt(true);
	    LMotor.flt(true);
	  }
}
