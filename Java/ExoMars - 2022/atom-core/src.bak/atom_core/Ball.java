//Metodo raccolta palle (Incompleto)
/*
Tutti i valori sono approsimati, tranne glia angoli calcolati in gradi
le distanze dovrebbero in cm, manca il metodo per raggiungere la zona di evacuazione.
Controllare le graffe con lettere in cerca di errori sintattici.
*/
package atom_core;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.hardware.Button;
import lejos.utility.Delay;

class Ball{
	
	static EV3UltrasonicSensor Central = new EV3UltrasonicSensor(SensorPort.S1);
	static EV3LargeRegulatedMotor mDx = new EV3LargeRegulatedMotor(MotorPort.A);
	static EV3LargeRegulatedMotor mSx = new EV3LargeRegulatedMotor(MotorPort.D);
	static EV3LargeRegulatedMotor mBc = new EV3LargeRegulatedMotor(MotorPort.C);
	static EV3LargeRegulatedMotor mCc = new EV3LargeRegulatedMotor(MotorPort.B);
	
	void impostazioni() {
		mDx.setSpeed(720);	//Impostazioni velocità dei motori di movimento
		mSx.setSpeed(720);
		mBc.setSpeed(450);//Velocità del braccio prendi palline
		mCc.setSpeed(450);//Velocità cancello per scarico delle palle
	}
	
	static SampleProvider distanza() {
		SampleProvider distanza_grezza = Central.getDistanceMode(); //Metodo inutilizzato usato in caso di calibrazione del sensore di distanza
		return distanza_grezza;
	}
	
	static float[] conversione(SampleProvider valori_grezzi) {
		SampleProvider distanza_grezza = valori_grezzi;
		float distanza[] = {0};
		distanza_grezza.fetchSample(distanza, 0);				//Acquisizione distanza per calibrazione, l'errore è uin falso allarme
		
		return distanza;
	}
	
	
	static void movimentoRaccolta(){
		LCD.drawString("Raccolta Palle",0 , 4);
		mDx.backward(); //movimenti per raccolta palline
		mSx.backward(); 
		mDx.stop();
		mSx.stop();
		mBc.rotate(145);	//Valori puramente casuali
		mDx.forward();		//Da settare
		mSx.forward();	
		mBc.rotate(-160);	//Valori puramente casuali
		mBc.rotate(15);		//Valori puramente casuali	
	}
	
	static void RicercaPalline(){
		
		
		mDx.forward();		//Il robot si muove quando va addosso a un muro lo stallo viene registrato
		mSx.forward();
		if(mDx.isStalled() == true && mSx.isStalled() == true){//e Inizia la procedura per il movimento del robot
			while(ac!=40){// Con questo ciclo il robot si muoverà a spirale fino a quando la distanza non sarà 40
				LCD.drawString("Sta girando...",0 , 4);
				mDx.rotate(-1080); //Il robot torna indietro di 3 giri
				mSx.rotate(-1080);
				do{									
					mDx.forward();  //Ci si muove fino a quando la distanza è minore di ac 
					mSx.backward();
				}while(distanza[0]<ac); 
				mDx.forward(); // si aumenta la distanza in modo che il movimento sprale abbia inizio
				mSx.forward();
				ac+=5;
				
			}
			if(distanza[0]<40 && (mDx.isStalled() == false && mSx.isStalled() == false)){
				LCD.drawString("Riavvio ricerca",0 , 4);
					mDx.forward();
					mSx.forward();	
			}
		}//ee
		
	}
	
	public static int ac=10;//Variabile per l'aumento della distanza spiegazione sottostante
	public static float distanza[] = conversione(distanza());
	
	static void ricercaZona(){
		
		//Va Inserito il metodo per raggiungere il punto di evacuazione
		
		do{	//Metodo per farla attaccare alla zona di evacuazione di retromarcia
		mDx.backward();
		mSx.backward();
		}while(mDx.isStalled() == false && mSx.isStalled() == false);
		if(mDx.isStalled() == true && mSx.isStalled() == true){//Scarico palle
			
			
			mCc.rotate(100);
			Delay.msDelay(6000);					 //Attesa per il perfetto scaricamento delle palle (
			mCc.rotateTo(0);						//Ritorna in posizione iniziale
			
		}
	}
	
	public static void play() {
		
		while(true)	{										//a
		int i=0;											//Contatore per raccolta di due palle la volta
			while(i<3){										//b Inizializzo il ciclo raccogli palle
				if(distanza[0]<10){						//c Movimenti per la raccolta della pallina		
					movimentoRaccolta();
					i++;
						}//cc
						else{//d Inizo ricerca palline								
							RicercaPalline();
							}//dd
							if(i==2){//w Metodo per inserire le palle nella zona di evacuazione
								ricercaZona();
							}//ww
						}//bb
					if(Button.getButtons() == Button.ID_ESCAPE) {System.exit(0); break;}	//Chiusura programma
					}//aa
				}
}