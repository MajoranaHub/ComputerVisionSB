package atom_core;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.utility.Delay;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class EvacuationZone
	extends Setup
{	
	 static int timeConstant = 15;
	 static int timeConstantMove = 26;
	 static float ultrasonicInput = UltrasonicUtils.getValue(DistanceUP);
	 static double distance;
	 static int cicli;
	 static float[] colorR;
	 static float[] colorL;
	 static boolean direction = true;
	 
	 static int gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
	 static String lato = "";
	 static boolean evacuationZonefound = false;
	 
	public static void play()
	{
		//Ingresso
		while(Button.getButtons() != Button.ID_ESCAPE)
		 {
			RMotor.setSpeed(300);
		    LMotor.setSpeed(300);
			ShovelMotor.setSpeed(100);
			DropMotor.setSpeed(100);
			DropMotor.rotateTo(0);
			MovementInterface.move(135, "forw", timeConstantMove, RMotor, LMotor); //57
			if(Button.getButtons() == Button.ID_ESCAPE) return;
			ZoneFinder();
			if(Button.getButtons() == Button.ID_ESCAPE) return;
			lilSnake();
			if(Button.getButtons() == Button.ID_ESCAPE) return;
			ReturnToZone();
			if(Button.getButtons() == Button.ID_ESCAPE) return;
		 }
	}
	
	//Funzione principale di esecuzione della serpentina
	public static void ZoneFinder(){
		Gyroscope.reset();
		/*
		 * Gira di 90° al lato A variabile 0				V
		 * Se non vede il muro gira di 180° variabile 1		V
		 * Variabile 0 lato A variabile 1 lato B			V
		 * Va dritta fino a una certa distanza				V
		 * Scattino indietro							 	V
		 * Alza la pala										V
		 * Va avanti fino a poco dal muro					V
		 * Gira di 90° 										V
		 * Controllo distanza								V						
		 * Va dritta di x									V
		 * Gira di 90° 										V
		 */
			do{
				if(ultrasonicInput <1.0 && ultrasonicInput > 0.7){
					//Lato Lungo
					LCD.drawString("Lato Lungo", 0, 3);
					LCD.clear();
					do{		
						MovementInterface.move(10, "forw", timeConstantMove, RMotor, LMotor);	
						colorR = ColorUtils.getValues(RColor);
						colorL = ColorUtils.getValues(LColor);
						if(ColorUtils.black(colorR)) evacuationZonefound = true;
					ultrasonicInput = UltrasonicUtils.getValue(DistanceUP); 
					if(Button.getButtons() == Button.ID_ESCAPE) return;
					}while(ultrasonicInput > 0.05 && !ColorUtils.black(colorR) && ((!ColorUtils.silver(colorL) && !ColorUtils.silver(colorL))));
					if(Button.getButtons() == Button.ID_ESCAPE) return;
					Delay.msDelay(500);
					colorR = ColorUtils.getValues(RColor);
					if (ColorUtils.black(colorR)) evacuationZonefound = true;
					if (evacuationZonefound) break;
					MovementInterface.move(13, "back", timeConstantMove, RMotor, LMotor);	
					Gyroscope.reset();
					do{
						MovementInterface.rotate(1, "ccw", timeConstant, RMotor, LMotor);
						gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
						if(Button.getButtons() == Button.ID_ESCAPE) return;
				    }while(gyroscopeInput < 85);
					if(Button.getButtons() == Button.ID_ESCAPE) return;
					do{			
						MovementInterface.move(10, "forw", timeConstantMove, RMotor, LMotor);
						colorR = ColorUtils.getValues(RColor);
						colorL = ColorUtils.getValues(LColor);
						if(ColorUtils.black(colorR)) evacuationZonefound = true;
						ultrasonicInput = UltrasonicUtils.getValue(DistanceUP);
						if(Button.getButtons() == Button.ID_ESCAPE) return;
					}while(ultrasonicInput > 0.05 && !ColorUtils.black(colorR) && ((!ColorUtils.silver(colorL) && !ColorUtils.silver(colorL))));
					if(Button.getButtons() == Button.ID_ESCAPE) return;
					Delay.msDelay(500);
					colorR = ColorUtils.getValues(RColor);
					if (ColorUtils.black(colorR)) evacuationZonefound = true;
					if (evacuationZonefound) break;
				    Gyroscope.reset();
					MovementInterface.move(13, "back", timeConstantMove, RMotor, LMotor);		
					do{
						MovementInterface.rotate(1, "ccw", timeConstant, RMotor, LMotor);
						gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
						if(Button.getButtons() == Button.ID_ESCAPE) return;
				    }while(gyroscopeInput < 85);
					if(Button.getButtons() == Button.ID_ESCAPE) return;
					
				}else{
					//lato corto
					LCD.drawString("Lato Corto", 0, 3);
					LCD.clear();
					do{	
						MovementInterface.move(10, "forw", timeConstantMove, RMotor, LMotor);
						colorR = ColorUtils.getValues(RColor);
						colorL = ColorUtils.getValues(LColor);
						if(ColorUtils.black(colorR)) evacuationZonefound = true;
						ultrasonicInput = UltrasonicUtils.getValue(DistanceUP);
						if(Button.getButtons() == Button.ID_ESCAPE) return;
					}while(ultrasonicInput > 0.05 && !ColorUtils.black(colorR) && ((!ColorUtils.silver(colorL) && !ColorUtils.silver(colorL))));
					if(Button.getButtons() == Button.ID_ESCAPE) return;
					Delay.msDelay(500);
					colorR = ColorUtils.getValues(RColor);
					if (ColorUtils.black(colorR)) evacuationZonefound = true;
					if (evacuationZonefound) break;
					MovementInterface.move(13, "back", timeConstantMove, RMotor, LMotor);		
					Gyroscope.reset();
					do{
						MovementInterface.rotate(1, "ccw", timeConstant, RMotor, LMotor);
						gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
						if(Button.getButtons() == Button.ID_ESCAPE) return;
				    }while(gyroscopeInput < 85);
					if(Button.getButtons() == Button.ID_ESCAPE) return;
					do{			
						MovementInterface.move(10, "forw", timeConstantMove,  RMotor, LMotor);
						colorR = ColorUtils.getValues(RColor);
						colorL = ColorUtils.getValues(LColor);
						if(ColorUtils.black(colorR)) evacuationZonefound = true;
						ultrasonicInput = UltrasonicUtils.getValue(DistanceUP);
						if(Button.getButtons() == Button.ID_ESCAPE) return;
					}while(ultrasonicInput > 0.05 && !ColorUtils.black(colorR) && ((!ColorUtils.silver(colorL) && !ColorUtils.silver(colorL))));
					if(Button.getButtons() == Button.ID_ESCAPE) return;
					Delay.msDelay(500);
					colorR = ColorUtils.getValues(RColor);
					if (ColorUtils.black(colorR)) evacuationZonefound = true;
					if (evacuationZonefound) break;
					MovementInterface.move(13, "back", timeConstantMove, RMotor, LMotor);
					Gyroscope.reset();
					do{
						MovementInterface.rotate(1, "ccw", timeConstant, RMotor, LMotor);
						gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
						if(Button.getButtons() == Button.ID_ESCAPE) return;
				    }while(gyroscopeInput < 85);
					if(Button.getButtons() == Button.ID_ESCAPE) return;
				
				}
				}while(!evacuationZonefound  && Button.getButtons() != Button.ID_ESCAPE);
				LCD.drawString("Finito ciclo zona", 0 , 3);
				LCD.clear();
				MovementInterface.move(30, "back", timeConstantMove, RMotor, LMotor);
				Gyroscope.reset();
				do{
					MovementInterface.rotate(1, "ccw", timeConstant, RMotor, LMotor);
					gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
					if(Button.getButtons() == Button.ID_ESCAPE) return;
			    }while(gyroscopeInput < 41);
				if(Button.getButtons() == Button.ID_ESCAPE) return;
				MovementInterface.move(84, "forw", timeConstantMove, RMotor, LMotor);
				Gyroscope.reset();
				do{
					MovementInterface.rotate(1, "ccw", timeConstant, RMotor, LMotor);
					gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
					if(Button.getButtons() == Button.ID_ESCAPE) return;
			    }while(gyroscopeInput < 41);
				if(Button.getButtons() == Button.ID_ESCAPE) return;
				ultrasonicInput = UltrasonicUtils.getValue(DistanceUP);
				Delay.msDelay(500);
				if(ultrasonicInput > 0.55){
					Gyroscope.reset();
					do{
						MovementInterface.rotate(1, "cw", timeConstant, RMotor, LMotor);
						gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
						if(Button.getButtons() == Button.ID_ESCAPE) return;
				    }while(gyroscopeInput > -83);
					if(Button.getButtons() == Button.ID_ESCAPE) return;
				}else{
					Gyroscope.reset();
					do{
						MovementInterface.rotate(1, "cw", timeConstant, RMotor, LMotor); 
						gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
						if(Button.getButtons() == Button.ID_ESCAPE) return;
				    }while(gyroscopeInput > -175);
					if(Button.getButtons() == Button.ID_ESCAPE) return;
					direction = !direction;
					}			
		}
			
			
	public static void lilSnake(){
		distance = 0.50;
		do{
			cicli = 0;
			MovementInterface.move(65, "back", timeConstantMove, RMotor, LMotor);
			MovementInterface.move(65, "forw", timeConstantMove, RMotor, LMotor);
			ShovelMotor.rotateTo(-198);
			RMotor.setSpeed(600);
		    LMotor.setSpeed(600);
			do {
				MovementInterface.move(10, "back", timeConstantMove, RMotor, LMotor);
				ultrasonicInput = UltrasonicUtils.getValue(DistanceUP);
				cicli++;
				if(Button.getButtons() == Button.ID_ESCAPE) return;
			}while(ultrasonicInput < distance || cicli <= 10);
			if(Button.getButtons() == Button.ID_ESCAPE) return;
			RMotor.setSpeed(300);
		    LMotor.setSpeed(300);
		    MovementInterface.move(30, "back", timeConstantMove, RMotor, LMotor);
			MovementInterface.move(35, "forw", timeConstantMove, RMotor, LMotor);
			ShovelMotor.rotateTo(0);
			MovementInterface.move(45, "back", timeConstantMove, RMotor, LMotor);
			if(Button.getButtons() == Button.ID_ESCAPE) return;
			if (direction == false)
			{
				Gyroscope.reset();
				do{
					MovementInterface.rotate(1, "ccw", timeConstant, RMotor, LMotor);
					gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
			    }while(gyroscopeInput < 82);
			}
			else {
				Gyroscope.reset();
				do{
					MovementInterface.rotate(1, "cw", timeConstant, RMotor, LMotor);
					gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
			    }while(gyroscopeInput > -82);
			}
			Delay.msDelay(1000);
			ultrasonicInput = UltrasonicUtils.getValue(DistanceUP);
				if (ultrasonicInput > 0.8) break;
			MovementInterface.move(50, "back", timeConstantMove, RMotor, LMotor);
			if(Button.getButtons() == Button.ID_ESCAPE) return;
			if (direction == false) 
			{
				do{
					MovementInterface.rotate(1, "ccw", timeConstant, RMotor, LMotor);
					gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
			    }while(gyroscopeInput < 175);
			}
			else {
				do{
					MovementInterface.rotate(1, "cw", timeConstant, RMotor, LMotor);
					gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
			    }while(gyroscopeInput > -165);
			}
			direction = !direction;
		}while(Button.getButtons() != Button.ID_ESCAPE);
	}
	
	public static void ReturnToZone(){ 
		//caso 1 dietro, scarica
		//caso 2 180°, dritto, destra, dritto, scarica
		LCD.drawString("Ciclo scarico", 0, 3);
		Delay.msDelay(500);
		LCD.clear();
		
			do {
				MovementInterface.move(30, "forw", timeConstantMove, RMotor, LMotor);
				ultrasonicInput = UltrasonicUtils.getValue(DistanceUP);
			}while(ultrasonicInput > 0.06  && ((!ColorUtils.silver(colorL) && !ColorUtils.silver(colorL))));
			MovementInterface.move(10, "forw", timeConstantMove, RMotor, LMotor);
			LCD.drawString("Primo muro", 0, 3);
			 
			LCD.clear();
			colorR = ColorUtils.getValues(RColor);
			colorL = ColorUtils.getValues(LColor);
			if(ColorUtils.black(colorL) || ColorUtils.black(colorR)){
				LCD.drawString("Nero a dx o sx", 0, 3);
				 
				LCD.clear();
				do{
					MovementInterface.move(3, "back", timeConstantMove, RMotor, LMotor);
					colorR = ColorUtils.getValues(RColor);
					colorL = ColorUtils.getValues(LColor);
				}while(!(ColorUtils.white(colorL) || ColorUtils.white(colorR)));
				LCD.drawString("Bianco a dx o sx", 0, 3);
				 
				LCD.clear();
				colorL = ColorUtils.getValues(LColor);
				if(ColorUtils.white(colorL)){
					//mosse di scarico zona a destra 1
					release(1);
				}else{
					//mosse di scarico zona a sinistra 2
					release(2);
					
				}
			}
			else {
				Gyroscope.reset();
				do{
					MovementInterface.rotate(1, "ccw", timeConstant, RMotor, LMotor);
					gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
			    }while(gyroscopeInput < 84);
				Delay.msDelay(500);
				ultrasonicInput = UltrasonicUtils.getValue(DistanceUP);
				if(ultrasonicInput <0.2){
					Gyroscope.reset();
					do{
						MovementInterface.rotate(1, "ccw", timeConstant, RMotor, LMotor); 
						gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
				    }while(gyroscopeInput < 180);
					LCD.drawString("secondo muro", 0, 3);
					 
					LCD.clear();
				}
				do{
					MovementInterface.move(3, "forw", timeConstantMove, RMotor, LMotor);
					colorR = ColorUtils.getValues(RColor);
					colorL = ColorUtils.getValues(LColor);
				}while(!(ColorUtils.black(colorL) || ColorUtils.black(colorR)));
				LCD.drawString("Nero a dx o sx after", 0, 3);
				 
				LCD.clear();
				do{
					MovementInterface.move(1, "back", timeConstantMove, RMotor, LMotor);
					colorR = ColorUtils.getValues(RColor);
					colorL = ColorUtils.getValues(LColor);
				}while(!(ColorUtils.white(colorL) || ColorUtils.white(colorR)));
				LCD.drawString("Bianco a dx o sx", 0, 3);
				 
				LCD.clear();
				colorL = ColorUtils.getValues(LColor);
				if(ColorUtils.white(colorL)){
					//mosse di scarico zona a destra 1
					release(1);
				}else{
					//mosse di scarico zona a sinistra 2
					release(2);
					
				}
				
			}
		
	}
	
	
	//Svuota il carrello
	public static void release(int position){
			if(position == 1){
				LCD.drawString("Scarico", 0, 3);
				 
				LCD.clear();
				Gyroscope.reset();
				do{
					MovementInterface.rotate(1, "ccw", timeConstant, RMotor, LMotor);
					gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
			    }while(gyroscopeInput < 41);
				MovementInterface.move(45, "forw", timeConstantMove, RMotor, LMotor);
				Gyroscope.reset();
				do{
					MovementInterface.rotate(1, "ccw", timeConstant, RMotor, LMotor); 
					gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
			    }while(gyroscopeInput < 83);
				MovementInterface.move(33, "back", timeConstantMove, RMotor, LMotor);
				DropMotor.rotateTo(120);
				MovementInterface.move(10, "forw", timeConstantMove,  RMotor, LMotor);
				ShovelMotor.rotateTo(-100);
				Delay.msDelay(3000);
				ShovelMotor.rotateTo(0);
				Delay.msDelay(1500);
				DropMotor.rotateTo(0);
				
			}else{
				LCD.drawString("Scarico", 0, 3);

				LCD.clear();
				Gyroscope.reset();
				do{
					MovementInterface.rotate(1, "cw", timeConstant, RMotor, LMotor);
					gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
			    }while(gyroscopeInput > -41);
				MovementInterface.move(45, "forw", timeConstantMove, RMotor, LMotor);
				Gyroscope.reset();
				do{
					MovementInterface.rotate(1, "cw", timeConstant, RMotor, LMotor);
					gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
			    }while(gyroscopeInput > -83);
				MovementInterface.move(33, "back", timeConstantMove, RMotor, LMotor);
				DropMotor.rotateTo(120);
				MovementInterface.move(10, "forw", timeConstantMove, RMotor, LMotor);
				ShovelMotor.rotateTo(-100);
				Delay.msDelay(3000);
				ShovelMotor.rotateTo(0);
				Delay.msDelay(1500);
				DropMotor.rotateTo(0);
				
			}
		    Gyroscope.reset();
		    do {
		    	MovementInterface.rotate(1, "cw", timeConstant, RMotor, LMotor);
		    	gyroscopeInput = GyroscopeUtils.getValue(Gyroscope);
		    } while (gyroscopeInput > -83);
	} 
}