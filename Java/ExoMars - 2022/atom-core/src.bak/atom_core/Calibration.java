package atom_core;

import lejos.hardware.Button;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;

class Calibration {	
	public static float[] lettura(EV3ColorSensor sensore) {					//legge i valori e li ritorna in un array di valori float
		SampleProvider lettura;
		
		lettura = sensore.getRGBMode();
		float valori_grezzi[] = new float[lettura.sampleSize()];
		lettura.fetchSample(valori_grezzi, 0);
		
		return valori_grezzi;
	}
	
	public static String[] conversione(float valori_grezzi[]) {				//converte i valori float in string e li ritorna
		String valori_finali[] = new String[3];
		
		for(int i = 0; i<3; i++) {
			valori_finali[i] = Float.toString(valori_grezzi[i]);
		}
		
		return valori_finali;
	}
	
	public static void printValori(String[] valori, String nome) {			//scrive i valori di un sensore, nome serve solo per capire di che sensore si tratta
		LCD.drawString("valori di " + nome, 0, 0);
		LCD.drawString("RED-GREEN-BLUE", 0, 1);
		LCD.drawString("--------------", 0, 2);
		for(int i = 0; i<3; i++) {
			LCD.drawString(valori[i], 0, 3+i);
		}
	}
	
	public static void play() {												//funzione principale della classe
		String[] valoriFinaliDx;		//creazione array per i valori
		String[] valoriFinaliSx;
		
		while(Button.getButtons() != Button.ID_ESCAPE) {
			valoriFinaliDx = conversione(lettura(Menu.sensoreDx));
			valoriFinaliSx = conversione(lettura(Menu.sensoreSx));
			printValori(valoriFinaliDx, "P1");							//stampa dei valori
			Button.waitForAnyPress();
			LCD.clear();
			printValori(valoriFinaliSx, "P4");
			Button.waitForAnyPress();
			LCD.clear();
		}
		
		LCD.drawString("Premere un tasto qualsiasi", 0, 4);					//attesa per nuova calibrazione
		Button.waitForAnyPress();
		LCD.clear();
		
		LCD.drawString("USCITA IN CORSO", 0, 4);
		System.exit(0);
	}
}
