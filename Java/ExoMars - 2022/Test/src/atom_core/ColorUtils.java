//PROGRAMMA DI TEST
package atom_core;

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ColorUtils
  extends Setup 
{
	static boolean black(float[] color)
	{
	  for(int i = 0; i < 3; i++)
	  {
		  if(color[i] > 0.08) return false;
	  }
	  
	  return true;
	}
	
	static boolean green(float[] color)
	{
		if(color[1] / color[0] > 1.5 && color[1] / color[2] > 1.5)
		{
			return true;
		}
		
		return false;
	}
	
	static boolean silver(float[] color)
	{
		float sum = 0;
		sum = color[0] + color[1] + color[2];
		
		if (sum / 3 > 0.4) return true;
		return false;
	}
	
	static boolean white(float[] color)
	{
		if(!green(color) && !black(color) && !silver(color)) return true;
		return false;
	}
	
	static float[] getValues(EV3ColorSensor sensor)
	{
	    SampleProvider reading = sensor.getRGBMode();
	    float[] rawValues = new float[reading.sampleSize()];
	    reading.fetchSample(rawValues, 0);
	    
	    return rawValues;
	}
}
