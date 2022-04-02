//ho inserito il caso "Affronta ostacolo"(proabilmente errato per colpa dei valori)

package atom_core;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.Button;
//librerie importate

class Core {
	//static EV3UltrasonicSensor sensoreDist = new EV3UltrasonicSensor(SensorPort.S2);
	
	static void impostazioni() {							//qui ci sono tutte le impostazioni dei valori
		Menu.mDx.setSpeed(900);									//impostazioni velocità motori
		Menu.mSx.setSpeed(900);
	}
	
	static int potenza(int base, int esp) {					//funzione per la potenza dato che quella di java da problemi di conversione di tipo
		for(int i = 1; i<esp; i++) {
			base*=base;
		}
		
		return base;
	}
	
	static float[] arrotonda(float valori[], int posizioni) {	//metodo per arrotondare elementi di un array a n cifre doppo la virgola
		int provvisorio;
		float[] finale = {0,0,0};								//Inizializzazione per non generare un falso errore da eclipse
		
		for(int i = 0; i<3; i++) {
			provvisorio = Math.round(valori[i] * potenza(10, posizioni));
			finale[i] = provvisorio/posizioni;
		}
		
		return finale;
	}
	
	static void muovi(EV3LargeRegulatedMotor m1, EV3LargeRegulatedMotor m2, int gradiM1, int gradiM2) {
		m1.rotate(gradiM1, true);
		m2.rotate(gradiM2);
	}
	
	static void ostacolo() {
		muovi(Menu.mDx, Menu.mSx, 100, 50);
		muovi(Menu.mDx, Menu.mSx, 100, 100);
		muovi(Menu.mDx, Menu.mSx, 25, 25);
		muovi(Menu.mDx, Menu.mSx, 50, 100);
		muovi(Menu.mDx, Menu.mSx, 100, 100);	
	}
	
	static boolean confronto(float[] array1, float[] array2) {
		if(array1[0] == array2[0] && array1[1] == array2[1] && array1[2] == array2[2]) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void play() {								//funzione principale della classe		
		boolean premuto = false;							//variabile per iniziare il programma
		float[] bianco = {(float)0.0, (float)0.0 ,(float)0.0};		//<--valori casuali da cambiare e calibrare
		float[] verde = {0, 0 ,0};		///////
		float[] nero = {1, 4 ,6};		///////
		
		float[] distanza;
		
		//sensoreDist.getDistanceMode();
		impostazioni();										//caricamento impostazioni
		
		LCD.drawString("INVIO PER INIZIARE", 0, 4);
		
		while(!premuto) {									//attesa per l'avvio del programma
			if(Button.getButtons() == Button.ID_ENTER) {
				premuto = true;
				LCD.clear();
			}
		}
		
		while(premuto) {									//esecuzione programma
			LCD.drawString("RUNNING...", 0, 4);	
			Button.waitForAnyPress();
			LCD.clear();
			
			LCD.drawString(Calibration.conversione(arrotonda(Calibration.lettura(Menu.sensoreSx), 1))[0], 0, 2);
			LCD.drawString(Calibration.conversione(arrotonda(Calibration.lettura(Menu.sensoreSx), 1))[0], 0, 3);
			LCD.drawString(Calibration.conversione(arrotonda(Calibration.lettura(Menu.sensoreSx), 1))[0], 0, 4);
			
			Button.waitForAnyPress();
			LCD.clear();
			
			if(confronto(arrotonda(Calibration.lettura(Menu.sensoreDx), 1),bianco) && confronto(arrotonda(Calibration.lettura(Menu.sensoreSx), 1), bianco))			
			{		//I VALORI DI CONFRONTO SONO FITTIZI
				muovi(Menu.mDx, Menu.mSx, 100, 100);		//vai dritto
			}
			LCD.drawString("PRIMA FASE", 0, 3);
			if(arrotonda(Calibration.lettura(Menu.sensoreDx), 1) == bianco && arrotonda(Calibration.lettura(Menu.sensoreSx), 1) == verde) 
			{
				muovi(Menu.mDx, Menu.mSx, 100, 100);		//vai dritto
			}
			else if(arrotonda(Calibration.lettura(Menu.sensoreDx), 1) == bianco && arrotonda(Calibration.lettura(Menu.sensoreSx), 1) == nero) 
			{
				muovi(Menu.mDx, Menu.mSx, 100, 100);		//vai dritto
			}
			else if(arrotonda(Calibration.lettura(Menu.sensoreDx), 1) == verde && arrotonda(Calibration.lettura(Menu.sensoreSx), 1) == bianco) 
			{
				muovi(Menu.mDx, Menu.mSx, 100, 100);		//vai dritto
			}
			else if(arrotonda(Calibration.lettura(Menu.sensoreDx), 1) == nero && arrotonda(Calibration.lettura(Menu.sensoreSx), 1) == bianco)
			{
				muovi(Menu.mDx, Menu.mSx, 100, 100);		//vai dritto
			}
			else if(arrotonda(Calibration.lettura(Menu.sensoreDx), 1) == nero && arrotonda(Calibration.lettura(Menu.sensoreSx), 1) == nero)
			{
				muovi(Menu.mDx, Menu.mSx, 100, 100);		//vai dritto
			}
			else {
				LCD.drawString("ERRORE COMBINAZIONE COLORI",0 , 4);
				Button.waitForAnyPress();
			}
			
			LCD.clear();

			//distanza[0] = 
			
			if(Button.getButtons() == Button.ID_ESCAPE) {break;}	//chiusura istanza
		}
	}
}
