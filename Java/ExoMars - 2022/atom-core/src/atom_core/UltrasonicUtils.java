package atom_core;

import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class UltrasonicUtils
	extends Setup
{
	//Lettura della distanza del sensore ultrasuoni
	public static float getValue(EV3UltrasonicSensor sensor)
	{
	  //I valori di tutti i sensori EV3 sono rappresentatii dal tipo sample provider che rappresenta la versione "codificata" dei valori
	  SampleProvider reading = sensor.getDistanceMode();
	  float[] rawValues = new float[reading.sampleSize()];
	  
	  /*I valori sotto forma di SampleProvider sono convertiti in un arrray di float con un numero di elementi pari alla
	   *quantità di valori rilevati con una lettura*/
	  reading.fetchSample(rawValues, 0);
	    
	  return rawValues[0];
	}
}
