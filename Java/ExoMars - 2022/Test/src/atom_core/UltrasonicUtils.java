//PROGRAMMA DI TEST
package atom_core;

import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class UltrasonicUtils
	extends Setup
{
	  public static float getValue(EV3UltrasonicSensor sensor)
	  {
	    SampleProvider reading = sensor.getDistanceMode();
	    float[] rawValues = new float[reading.sampleSize()];
	    reading.fetchSample(rawValues, 0);
	    
	    return rawValues[0];
	  }
}
