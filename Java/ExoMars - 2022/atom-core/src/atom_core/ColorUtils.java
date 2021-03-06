package atom_core;

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ColorUtils
  extends Setup 
{
	//Viene considerato nero se tutti e tre i valori sono minori di 0.08
	static boolean black(float[] color)
	{
	  for(int i = 0; i < 3; i++)
	  {
		  if(color[i] > 0.08 || color[i] < 0.013) return false;
	  }
	  
	  return true;
	}
	
	//Considerato verde tutto se i rapporti verde/rosso e verde/blu sono maggiori di 1.5 e se il rapporto verde/rosso < 0.5
	static boolean green(float[] color)
	{
		if(((color[1] / color[0] > 2.3 && color[1] / color[2] > 1.5 && ((color[0] + color[1]) / 2) < 0.7) && color[0] < 0.06 && color[1] > 0.13) || (color[1] > 0.081 && color[1] < 0.13 && color[0] < 0.037))
		{
			return true;
		}
		return false;
	}
	
	//viene preso rosso se il valore red ? maggiore di 10 e gli altri sono minori di 0.1
	static boolean red(float[] color) 
	{
		if(color[0] >= 0.20 && color[1] < 0.1 && color[2] < 0.1) return true;
		return false;
	}
	
	//Viene preso per bianco se le funzioni green, black e silver ritornano false con il color passato come argomento
	static boolean white(float[] color)
	{
		if(!green(color) && !black(color) && !silver(color) && !red(color)) return true;
		return false;
	}
	
	//Considerato silver se la media di rosso e verde ? maggiore di 0.45
	static boolean silver(float[] color)
	{
		if ((color[0] + color[1]) / 2 > 0.5) return true;
		return false;
	} 
	
	
	
	//Funzione per la lettura dei sensori di colore con lo stesso metodo utilizzato in UltrasonicUtils.getValue()
	static float[] getValues(EV3ColorSensor sensor)
	{
	    SampleProvider reading = sensor.getRGBMode();
	    float[] rawValues = new float[reading.sampleSize()];
	    reading.fetchSample(rawValues, 0);
	    
	    return rawValues;
	}
}
