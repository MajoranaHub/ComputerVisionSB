package atom_core;

import java.io.IOException;
import java.util.concurrent.Delayed;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

class Core
  extends Setup
{ 
	//Variabili utilizzate per contenere i valori rilevati dalla lettura dei sensori
	static float[] colorInputR;
	static float[] colorInputL;
	
	 //Variabile per errori curve
	 static boolean error = true;
	 
	 //Variabile per l'ostacolo
	 static String obstacleDirection = "ccw";
	
	 //timeConstant
	 static int timeConstant = 26;
	 static int timeConstantMove = 26;
	 
	 //Variabile per fixare il gap
	 static String lastMoveGap = "start";
	 
	//f-r-l forward, right, left
	static String lastMove = "";
	
	//Contatore per cicli bianco-nero prima del nero-nero
	static int BWCounter = 0;
	 
  public static void play(String obstacleArg)
  {
	 Sound.playTone(550, 500);
	 LCD.drawString("---Running---", 0, 3);
	 
	 
	 //Variabili utilizzate per contenere i valori rilevati dalla lettura dei sensori
	 float ultrasonicUPInput;
	 int gyroValue;
	 
	 //Variabili utilizzate per fixare il gap
	 int whiteCounter = 0;
	 int searchCounter = 0;
	 String gapRotation;
	 lastMoveGap = "start";
	 
	 //Controllo green-green
	 boolean greenCheck = true;
	 
	 //Contatore cicli silver-random
	 int silverCount = 0;  
	 
	 //Costante relativa alla velocit? da utilizzare nelle curve
	 final int steeringSpeed = 180;//da diminuire
	 
	 /*Variabile utilizzata per memorizzare l'ultima mossa effettuata, aiuta a risolvere le ambiguit? generate
	  *dal caso NERO-NERO*/
	 
	 //f-r-l forward, right, left
	 lastMove = "";
	 
	 boolean ostacolo = true;
	 
	 //Loop principale di esecuzione, si ripete finch? non viene premuto il tasto di uscita
	 while(Button.getButtons() != Button.ID_ESCAPE)
	 {
		 //Lettura dei valori dei sensori tramite le "librerie" ColorUtils e UltrasonicUtils
		 colorInputR = ColorUtils.getValues(RColor);
		 colorInputL = ColorUtils.getValues(LColor);
		 ultrasonicUPInput = UltrasonicUtils.getValue(DistanceUP);
		 greenCheck = true;
		 
		 //In questa parte sono gestiti tutti i casi possibili in relazione ai valori rilevati dai sensori
		 
		 //Ostacolo, viene superato girando alla sua destra, caso attivato quando la distanza del sensore ultrasuoni
		 //? minore di 5.5cm
		 
		  if(ultrasonicUPInput < 0.06 && ostacolo)
		 {
			 ostacolo = false;
			 Sound.beepSequence();
			 
			 LCD.drawString("ostacolo", 0, 5);
			 		
			 
			 Delay.msDelay(500);
			 if(obstacleArg == "destra") obstacleDirection = "ccw";
			 else if(obstacleArg == "sinistra") obstacleDirection = "cw";
			 else if(obstacleArg == "mezzo") obstacleDirection = "cw";
			 
			 if(obstacleDirection == "ccw") MovementInterface.rotate(80, "cw", timeConstant, RMotor, LMotor);
			 else MovementInterface.rotate(80, "ccw", timeConstant, RMotor, LMotor);
			 /*ultrasonicUPInput = UltrasonicUtils.getValue(DistanceUP);
			 if (ultrasonicUPInput > 0 .29) obstacleDirection = "ccw";
			 else{
				 MovementInterface.rotate(170, "ccw", timeConstant, RMotor, LMotor);
				 obstacleDirection = "cw";
			 }*/
			 MovementInterface.move(90, "forw", timeConstantMove, RMotor, LMotor);
			 MovementInterface.rotate(85, obstacleDirection, timeConstant, RMotor, LMotor);
			 
			 if(obstacleArg == "mezzo") {
				 MovementInterface.move(5, "forw", timeConstantMove, RMotor, LMotor);
			 }
			 else {
				 MovementInterface.move(120, "forw", timeConstantMove, RMotor, LMotor);
				 MovementInterface.rotate(65, obstacleDirection, timeConstant, RMotor, LMotor);
			 }
			 
			 do {
				 MovementInterface.move(1, "forw", timeConstantMove, RMotor, LMotor);
				 colorInputR = ColorUtils.getValues(RColor);
			 } while(ColorUtils.white(colorInputR));
			 
			 MovementInterface.move(30, "forw", timeConstantMove, RMotor, LMotor);
			 
			 if(obstacleDirection == "ccw") obstacleDirection = "cw";
			 else obstacleDirection = "ccw";
			 
			 do {
				 MovementInterface.rotate(1, obstacleDirection, timeConstant, RMotor, LMotor);
				 colorInputL = ColorUtils.getValues(LColor);
			 } while(ColorUtils.white(colorInputL));
			 
			 MovementInterface.move(10, "back", timeConstantMove, RMotor, LMotor);
			 
			 LCD.clear(5);
			 
			 Sound.beepSequenceUp();
		 } 
		 
		 
		 //White-White
		 else if(ColorUtils.white(colorInputR) && ColorUtils.white(colorInputL))
		 {		
			 BWCounter = 0;
			 whiteCounter++;
			 LCD.drawString(Float.toString(whiteCounter), 0, 3);
			 LCD.drawString("white-white", 0, 4);
			 timeConstantMove = 15;
			 MovementInterface.move(1, "forw", timeConstantMove, RMotor, LMotor);
			 timeConstantMove = 26;
			 lastMove = "f";
			 LCD.clear();
			 
			 if (whiteCounter > 115)
				{
				 do{
					if (lastMoveGap == "start") break;
					else if (lastMoveGap == "l") gapRotation = "ccw";
					else gapRotation = "cw";
					
					Gyroscope.reset();
					do{
						MovementInterface.rotate(1, gapRotation, timeConstant, RMotor, LMotor);
						colorInputR = ColorUtils.getValues(RColor);
						colorInputL = ColorUtils.getValues(LColor);
						if (ColorUtils.black(colorInputR) || ColorUtils.black(colorInputL)) break;
						gyroValue = GyroscopeUtils.getValue(Gyroscope);
						if (gyroValue < 0) gyroValue *= -1;
					}while(gyroValue < 45 && !(ColorUtils.black(colorInputR) || ColorUtils.black(colorInputL)));
					
					if (!(ColorUtils.black(colorInputR) || ColorUtils.black(colorInputL))){
						do {
							MovementInterface.move(1, "forw", timeConstantMove, RMotor, LMotor);
							colorInputR = ColorUtils.getValues(RColor);
							colorInputL = ColorUtils.getValues(LColor);
							searchCounter++;
						} while(ColorUtils.white(colorInputR) &&  ColorUtils.white(colorInputL) && searchCounter < 70);
					}
					
					if (searchCounter == 70)
					{
						if (gapRotation == "cw") gapRotation = "ccw";
						else gapRotation = "cw";
						
						Gyroscope.reset();
						do{
							MovementInterface.rotate(1, gapRotation, timeConstant, RMotor, LMotor);
							colorInputR = ColorUtils.getValues(RColor);
							colorInputL = ColorUtils.getValues(LColor);
							if (ColorUtils.black(colorInputR) || ColorUtils.black(colorInputL)) break;
							gyroValue = GyroscopeUtils.getValue(Gyroscope);
							if (gyroValue < 0) gyroValue *= -1;
						}while(gyroValue < 80);
						
						do {
							MovementInterface.move(1, "forw", timeConstantMove, RMotor, LMotor);
							colorInputR = ColorUtils.getValues(RColor);
							colorInputL = ColorUtils.getValues(LColor);
						} while(ColorUtils.white(colorInputR) &&  ColorUtils.white(colorInputL));
					}
					
					searchCounter = 0;
					
				  }while(false);
				}
		 }
		 
		 //White-Black
		 else if(ColorUtils.white(colorInputR) && ColorUtils.black(colorInputL))
		 {
			 //Qui viene effettuato il cambio di velocit? per le curve
			 RMotor.setSpeed(steeringSpeed);
			 LMotor.setSpeed(steeringSpeed);

			 LCD.drawString("white-black", 0, 4);
			 timeConstant = 33;
			 MovementInterface.rotate(1, "ccw", timeConstant, RMotor, LMotor);
			 timeConstant = 26;
			 
			//Dopo aver eseguito la curva reimposta la velocit? dei motori al valore originale
			 RMotor.setSpeed(250);
			 LMotor.setSpeed(250);
			 
			 /*Nei casi con un nero ed un bianco la nuova azione viene memorizzata solo se quella precedente ? stata quella
			  *di andare avanti, questo per ridurre i casi in cui un ambiguit? di tipo NERO-NERO venga risolta andando in
			  *direzione opposta a quella corretta*/
			 if(lastMove == "f") lastMove = "l";
			 if(whiteCounter > 10){
				 lastMoveGap = "l";
			 }
			 whiteCounter = 0;
			 
			 BWCounter += 1;
			 if(BWCounter == 9) DoubleBlack();
			 LCD.clear();
		 }
		 
		 //Black-White
		 else if(ColorUtils.black(colorInputR) && ColorUtils.white(colorInputL))
		 {
			RMotor.setSpeed(steeringSpeed);
			LMotor.setSpeed(steeringSpeed);
			
			LCD.drawString("black-white", 0, 4);
			timeConstant = 33;
			MovementInterface.rotate(1, "cw", timeConstant, RMotor, LMotor);
			timeConstant = 26;
			
			RMotor.setSpeed(250);
			LMotor.setSpeed(250);
			
			if(lastMove == "f") lastMove = "r";
			if(whiteCounter > 10){
				 lastMoveGap = "r";  
			 }		
			whiteCounter = 0; 
			
			BWCounter += 1;
			if(BWCounter == 9) DoubleBlack();
		 }
		 
		 //Black-Black
		 else if(ColorUtils.black(colorInputR) && ColorUtils.black(colorInputL))
		 {
			 DoubleBlack();
		 }
		 
		//Silver-Silver 
		 else if(ColorUtils.silver(colorInputL) || ColorUtils.silver(colorInputR)){
			 //Se l'argento viene rilevato da entrambe i sensori passa direttamente alla zona di evacuazione
			 
			 colorInputL = ColorUtils.getValues(LColor);
			 colorInputR = ColorUtils.getValues(RColor);
			 if(ColorUtils.silver(colorInputL) && ColorUtils.silver(colorInputR))
			 {
				 Sound.twoBeeps();
			 }
			 //Se solo uno dei due sensori rileva l'argento allora significa che la linea ? stata raggiunta di sbiego e quindi riaddrizza
			 else if(ColorUtils.silver(colorInputL)) 
			 {
				 Sound.beepSequenceUp();
				 do {
					 MovementInterface.rotate(1, "ccw", timeConstant, RMotor, LMotor);
					 colorInputR = ColorUtils.getValues(RColor);
					 silverCount += 1;
				 } while(!ColorUtils.silver(colorInputR) && silverCount <= 5);
			 }
			 else if(ColorUtils.silver(colorInputR))
			 {
				 Sound.beepSequence();
				 
				 do {
					 MovementInterface.rotate(1, "cw", timeConstant, RMotor, LMotor);
					 colorInputL = ColorUtils.getValues(LColor);
					 silverCount += 1;
				 } while(!ColorUtils.silver(colorInputL) && silverCount <= 5);
			 }
			 if (silverCount != 6)
			 {			 
				 silverCount = 0;
				 //EvacuationZone.play();
				 try {
					Client.start();
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 silverCount = 0;
		 }
		 
		//Random-Green
		 else if(ColorUtils.green(colorInputR) && !ColorUtils.green(colorInputL))
		 {	
			BWCounter = 0;
			LCD.drawString("random-green", 0, 4);
			/*LCD.drawString(Float.toString(colorInputR[0]), 0, 1);
			LCD.drawString(Float.toString(colorInputR[1]), 0, 2);
			LCD.drawString(Float.toString(colorInputR[2]), 0, 3);
			Button.waitForAnyPress();*/
			
			//Controllo green-green
			MovementInterface.move(2, "forw", timeConstantMove, RMotor, LMotor);
			Delay.msDelay(200);
			colorInputL = ColorUtils.getValues(LColor);
			if (ColorUtils.green(colorInputL)) greenCheck = false;
			MovementInterface.rotate(4, "cw", timeConstant, RMotor, LMotor);
			Delay.msDelay(200);
			colorInputL = ColorUtils.getValues(LColor);
			if (ColorUtils.green(colorInputL)) greenCheck = false;
			MovementInterface.rotate(8, "ccw", timeConstant, RMotor, LMotor);
			Delay.msDelay(200);
			colorInputL = ColorUtils.getValues(LColor);
			if (ColorUtils.green(colorInputL)) greenCheck = false;
			MovementInterface.rotate(5, "cw", timeConstant, RMotor, LMotor);
			
			if(greenCheck)
			{
				//Prima della curva viene fatto un lieve spostamento in avanti per adattarsi a tutti i tipi di curva con verde
				MovementInterface.move(25, "forw", timeConstantMove, RMotor, LMotor);
				MovementInterface.rotate(6, "cw", timeConstant, RMotor, LMotor);
				do {
					MovementInterface.rotate(2, "cw", timeConstant, RMotor, LMotor);
					
					colorInputR = ColorUtils.getValues(RColor);
				} while(!ColorUtils.black(colorInputR));
				lastMoveGap = "r";
				LCD.clear();
			}
			else DoubleGreen();
		 }
		 
		 //Green-Random
		 else if(ColorUtils.green(colorInputL) && !ColorUtils.green(colorInputR))
		 {
			 BWCounter = 0;
			 LCD.drawString("green-random", 0, 4);
				/*LCD.drawString(Float.toString(colorInputL[0]), 0, 1);
				LCD.drawString(Float.toString(colorInputL[1]), 0, 2);
				LCD.drawString(Float.toString(colorInputL[2]), 0, 3);
				Button.waitForAnyPress();*/ 
			 
			//Controllo green-green
			MovementInterface.move(2, "forw", timeConstantMove, RMotor, LMotor);
			Delay.msDelay(200);
			colorInputR = ColorUtils.getValues(RColor);
			if (ColorUtils.green(colorInputR)) greenCheck = false;
			MovementInterface.rotate(4, "cw", timeConstant, RMotor, LMotor);
			Delay.msDelay(200);
			colorInputR = ColorUtils.getValues(RColor);
			if (ColorUtils.green(colorInputR)) greenCheck = false;
			MovementInterface.rotate(8, "ccw", timeConstant, RMotor, LMotor);
			Delay.msDelay(200);
			colorInputR = ColorUtils.getValues(RColor);
			if (ColorUtils.green(colorInputR)) greenCheck = false;
			MovementInterface.rotate(5, "cw", timeConstant, RMotor, LMotor);
			
			if(greenCheck)
			{
				MovementInterface.move(25, "forw", timeConstantMove, RMotor, LMotor);
				MovementInterface.rotate(6, "ccw", timeConstant, RMotor, LMotor);			 
				do {
					MovementInterface.rotate(2, "ccw", timeConstant, RMotor, LMotor);
					
					colorInputL = ColorUtils.getValues(LColor);
				} while(!ColorUtils.black(colorInputL));
				lastMoveGap = "l";
				LCD.clear();
			}
			else DoubleGreen();	
		 }
		 
		 //Green-Green
		 else if(ColorUtils.green(colorInputL) && ColorUtils.green(colorInputR)){
			 DoubleGreen();
		 }
			 
		 
		 
	 }//Instruction loop
	 
	 LCD.clear();
	 Sound.playTone(330, 500);
  }
  
  public static void DoubleGreen(){
	  Sound.playTone(500, 400);
	  BWCounter = 0;
	  LCD.drawString("green-green", 0, 4);
		 /*Viene effettuata una rotazione iniziale per evitare di incorrere nella linea nera perpendicolare a quella su cui
		  *si trova il robot*/
	  	 MovementInterface.move(65, "forw", 26, RMotor, LMotor); //prima era 40
 		 MovementInterface.rotate(100, "ccw", 26, RMotor, LMotor); //prima era cw e 90
		 
		 //Successivamente continua la rotazione in maniera dinamica, cio? fermandosi quando incontra la linea nera da cui ? arrivato
		 do{
			 MovementInterface.rotate(2, "ccw", 26, RMotor, LMotor); //prima era cw
			 colorInputL = ColorUtils.getValues(LColor); //prima era R
		 }while(ColorUtils.white(colorInputL)); //prima era R
		 
		 MovementInterface.rotate(2, "ccw", 26, RMotor, LMotor);
		 
		 LCD.clear(); 
  }
  
  
  public static void DoubleBlack(){
	  /*Il caso NERO-NERO ? gestito come un'ambiguit? in quanto potrebbe essere generato da un incrocio oppure da
		  *una curva stretta. L'ambiguit? viene risolta tramite l'utilizzo della variabile "lastMove" che contiene
		  *l'ultima azione eseguita*/
		 LCD.drawString("black-black", 0, 4);
		 
		 if (BWCounter == 9)
		 {
			 LCD.drawString("Counter Black", 1, 0);
		 }
		 
		 //Nel caso in cui l'azione precedente sia stata quella di girare ad una curva, si gira in direzione di quella curva
		 if(lastMove == "r" || lastMove == "f")
		 {
			 /*Prima di intraprendere la curva, c'? una breve rotazione nella direzione opposta. Questa azione funge da
			  *ulteriore sistema di controllo su eventuali fraintendimenti in quanto, cos? facendo, si evitano casi in cui
			  *un incrocio preso male venga frainteso per una curva in quanto l'ipotetica linea dritta viene rilevata*/
			 Gyroscope.reset();
			 LCD.drawString("Curva a destra", 0, 5);
			 MovementInterface.move(20, "forw", timeConstantMove, RMotor, LMotor);
			 colorInputL = ColorUtils.getValues(LColor);
			 colorInputR = ColorUtils.getValues(RColor);
			 if(!(ColorUtils.white(colorInputL) || ColorUtils.white(colorInputR))){
				 MovementInterface.move(60, "forw", timeConstantMove, RMotor, LMotor);
			 }
			 MovementInterface.rotate(23, "ccw", timeConstant, RMotor, LMotor); 
			 do {
				 MovementInterface.rotate(1, "cw", 30, RMotor, LMotor);
				 colorInputR = ColorUtils.getValues(RColor);
				 colorInputL = ColorUtils.getValues(LColor);
				 
				 /*
				 
				 if(ColorUtils.black(colorInputR))
				 {
					LCD.drawString(Float.toString(colorInputR[0]), 0, 1);
					LCD.drawString(Float.toString(colorInputR[1]), 0, 2);
					LCD.drawString(Float.toString(colorInputR[2]), 0, 3);
					Button.waitForAnyPress();
				 }
				 if(ColorUtils.black(colorInputL))
				 {
					LCD.drawString(Float.toString(colorInputL[0]), 0, 1);
					LCD.drawString(Float.toString(colorInputL[1]), 0, 2);
					LCD.drawString(Float.toString(colorInputL[2]), 0, 3);
					Button.waitForAnyPress();
				 }
				 
				 */
				 
				 if (GyroscopeUtils.getValue(Gyroscope) < -117) error = false;
			 } while((!ColorUtils.black(colorInputR) && !ColorUtils.black(colorInputL)) && error);
			 lastMoveGap = "r";
			 if (!error)
			 {
				 error = true;
				 do {
					 MovementInterface.rotate(1, "ccw", 30, RMotor, LMotor);
					 colorInputR = ColorUtils.getValues(RColor);
					 colorInputL = ColorUtils.getValues(LColor);
					 if (GyroscopeUtils.getValue(Gyroscope) > 90) 
						 {
						 error = false;
						 }
				 } while((!ColorUtils.black(colorInputR) && !ColorUtils.black(colorInputL)) && error);
				 lastMoveGap = "l";
			 }
			 if (!error)
			 {
				 do {
					 MovementInterface.rotate(1, "cw", 30, RMotor, LMotor);
					 colorInputR = ColorUtils.getValues(RColor);
					 colorInputL = ColorUtils.getValues(LColor);
				 } while((!ColorUtils.black(colorInputR) && !ColorUtils.black(colorInputL)));
				 lastMoveGap = "l";
				 error = true;
			 }
			 LCD.clear(5);
		 }
		 
		 else if(lastMove == "l")
		 {
			 Gyroscope.reset();
			 LCD.drawString("Curva a sinistra", 0, 5);
			 MovementInterface.move(20, "forw", timeConstantMove, RMotor, LMotor);
			 colorInputL = ColorUtils.getValues(LColor);
			 colorInputR = ColorUtils.getValues(RColor);
			 if(!(ColorUtils.white(colorInputL) || ColorUtils.white(colorInputR))){
				 MovementInterface.move(60, "forw", timeConstantMove, RMotor, LMotor);
			 }
			 MovementInterface.rotate(23, "cw", timeConstant, RMotor, LMotor);
			 
			 do {
				 MovementInterface.rotate(1, "ccw", 30, RMotor, LMotor);
				 colorInputL = ColorUtils.getValues(LColor);
				 colorInputR = ColorUtils.getValues(RColor);
				 
				 /*
				 
				 if(ColorUtils.black(colorInputR))
				 {
					LCD.drawString(Float.toString(colorInputR[0]), 0, 1);
					LCD.drawString(Float.toString(colorInputR[1]), 0, 2);
					LCD.drawString(Float.toString(colorInputR[2]), 0, 3);
					Button.waitForAnyPress();
				 }
				 if(ColorUtils.black(colorInputL))
				 {
					LCD.drawString(Float.toString(colorInputL[0]), 0, 1);
					LCD.drawString(Float.toString(colorInputL[1]), 0, 2);
					LCD.drawString(Float.toString(colorInputL[2]), 0, 3);
					Button.waitForAnyPress();
				 }
				 
				 */
				 
				 if (GyroscopeUtils.getValue(Gyroscope) > 117) error = false;
			 } while((!ColorUtils.black(colorInputL) && !ColorUtils.black(colorInputR)) && error);
			 lastMoveGap = "l";
			 if (!error)
			 {
				 error = true;
				 do {
					 MovementInterface.rotate(1, "cw", 30, RMotor, LMotor);
					 colorInputR = ColorUtils.getValues(RColor);
					 colorInputL = ColorUtils.getValues(LColor);
					 if (GyroscopeUtils.getValue(Gyroscope) < -90) 
					 {
					 error = false;
					 }
				 } while((!ColorUtils.black(colorInputR) && !ColorUtils.black(colorInputL)) && error);
				 lastMoveGap = "r";
			 }
			 
			 if (!error)
			 {
				 do {
					 MovementInterface.rotate(1, "ccw", 30, RMotor, LMotor);
					 colorInputR = ColorUtils.getValues(RColor);
					 colorInputL = ColorUtils.getValues(LColor);
				 } while((!ColorUtils.black(colorInputR) && !ColorUtils.black(colorInputL)));
				 lastMoveGap = "l";
				 error = true;
			 }
			 LCD.clear(5);
		 }
		 
		 LCD.clear();
		 BWCounter = 0;
  }
  
  public static void goToEvacuation(){
	  float distance;
	  int timeConstantMoveCM = 120;
	  while(Button.getButtons() != Button.ID_ESCAPE) 
	  {
		  distance = UltrasonicUtils.getValue(DistanceUP);
		  
		  MovementInterface.rotate(10, "cw", timeConstant, RMotor, LMotor);
		  
		  /*if(distance < 0.30)
		  {
			  LCD.drawString("Ostacolo", 0, 2);
			  			  
			  //il robot continua ad avanzare fino a trovare l'ostacolo
			  while(distance > 0.06)
			  {
				  MovementInterface.move(1, "forw", timeConstantMoveCM, RMotor, LMotor);
				  distance = UltrasonicUtils.getValue(DistanceUP);
				  if(Button.getButtons() == Button.ID_ESCAPE) return;
			  }
			  
			  //il robot oltrepassa l'ostacolo a destra
			  MovementInterface.rotate(85, "cw", timeConstant, RMotor, LMotor);
			  MovementInterface.move(90, "forw", timeConstantMove, RMotor, LMotor);
			  MovementInterface.rotate(85, "ccw", timeConstant, RMotor, LMotor);
			  MovementInterface.move(150, "forw", timeConstant, RMotor, LMotor);
			  MovementInterface.rotate(85, "ccw", timeConstant, RMotor, LMotor);
			  MovementInterface.move(90, "forw", timeConstantMove, RMotor, LMotor);
			  MovementInterface.rotate(85, "cw", timeConstant, RMotor, LMotor);
			  
			  //procede fino allo start
			  MovementInterface.move(5, "forw", timeConstant, RMotor, LMotor);
			  MovementInterface.rotate(85, "cw", timeConstant, RMotor, LMotor);
			  MovementInterface.move(60, "forw", timeConstantMoveCM, RMotor, LMotor);
			  MovementInterface.rotate(85, "ccw", timeConstant, RMotor, LMotor);
			  
			  //cerca il grigio
			  
			  do{
				  LCD.drawString("cerco argento", 0, 2);
				  colorInputR = ColorUtils.getValues(RColor);
				  colorInputL = ColorUtils.getValues(LColor);
				  MovementInterface.move(1, "forw", timeConstant, RMotor, LMotor);
				  if(Button.getButtons() == Button.ID_ESCAPE) return;
				  
			  }while(!ColorUtils.silver(colorInputR) && !ColorUtils.silver(colorInputL));
			  
			  //si posiziona sullo START
			  
			  MovementInterface.move(30, "forw", timeConstantMoveCM, RMotor, LMotor);
			  MovementInterface.rotate(170, "cw", timeConstant, RMotor, LMotor);
			  
			  do{
				  colorInputR = ColorUtils.getValues(RColor);
				  colorInputL = ColorUtils.getValues(LColor);
				  MovementInterface.move(1, "forw", timeConstant, RMotor, LMotor);
				  if(Button.getButtons() == Button.ID_ESCAPE) return;
				  
			  }while(!ColorUtils.silver(colorInputR) && !ColorUtils.silver(colorInputL));
			  
			  NewEvacuation.play();
		  }
		  else*/
		  //{
			  //procede fino alla piastrella 10
			  MovementInterface.move(60, "forw", timeConstantMoveCM, RMotor, LMotor);
			  Delay.msDelay(500);
			  
			  LCD.drawString("vado a start", 0, 2);
			  
			  //si gira e va fino allo START
			  
			  //aggiornamento:  uso la ricerca dinamica della linea nera
			  MovementInterface.rotate(45, "cw", timeConstant, RMotor, LMotor);
			  do {
				  MovementInterface.rotate(1, "cw", timeConstant, RMotor, LMotor);
				  colorInputL = ColorUtils.getValues(LColor);
				  if(Button.getButtons() == Button.ID_ESCAPE) return;
			  }while(ColorUtils.white(colorInputL));
			  
			  do {
				  MovementInterface.rotate(1, "cw", timeConstant, RMotor, LMotor);
				  colorInputL = ColorUtils.getValues(LColor);
				  colorInputR = ColorUtils.getValues(RColor);
				  if(Button.getButtons() == Button.ID_ESCAPE) return;
			  }while(!ColorUtils.white(colorInputL) && !ColorUtils.white(colorInputR));
			  
			  MovementInterface.rotate(10, "ccw", timeConstant, RMotor, LMotor);
			  
			  do{
				  LCD.drawString("cerco ross", 0, 2);
				  colorInputR = ColorUtils.getValues(RColor);
				  colorInputL = ColorUtils.getValues(LColor);
				  MovementInterface.move(1, "forw", timeConstantMoveCM, RMotor, LMotor);
				  if(Button.getButtons() == Button.ID_ESCAPE) return;
				  
			  }while(!ColorUtils.red(colorInputR) && !ColorUtils.red(colorInputL));
			  MovementInterface.move(5, "back", timeConstantMoveCM, RMotor, LMotor);
			  
			  MovementInterface.rotate(85, "ccw", timeConstant, RMotor, LMotor);
			  
			  /*
			  MovementInterface.rotate(90, "cw", timeConstant, RMotor, LMotor);
			  MovementInterface.move(55, "forw", timeConstantMoveCM, RMotor, LMotor);
			  MovementInterface.rotate(85, "ccw", timeConstant, RMotor, LMotor);
			  */
			  
			  //cerca il grigio
			  
			  do{
				  LCD.drawString("cerco argento", 0, 2);
				  colorInputR = ColorUtils.getValues(RColor);
				  colorInputL = ColorUtils.getValues(LColor);
				  MovementInterface.move(1, "forw", timeConstantMoveCM, RMotor, LMotor);
				  if(Button.getButtons() == Button.ID_ESCAPE) return;
				  
			  }while(!ColorUtils.silver(colorInputR) && !ColorUtils.silver(colorInputL));
			  
			  LCD.clear();
			  LCD.drawString("silver found", 0, 2);
			  
			  //si posiziona sullo START
			  
			  MovementInterface.move(25, "forw", timeConstantMoveCM, RMotor, LMotor);
			  MovementInterface.rotate(170, "ccw", timeConstant, RMotor, LMotor);
			  MovementInterface.move(5, "back", timeConstantMoveCM, RMotor, LMotor);
			  
			  do{
				  colorInputR = ColorUtils.getValues(RColor);
				  colorInputL = ColorUtils.getValues(LColor);
				  MovementInterface.move(1, "forw", timeConstantMoveCM, RMotor, LMotor);
				  if(Button.getButtons() == Button.ID_ESCAPE) return;
				  
			  }while(!ColorUtils.silver(colorInputR) && !ColorUtils.silver(colorInputL));
			  
			  NewEvacuation.play();
		  //}
	  }
  }
  
  
}
