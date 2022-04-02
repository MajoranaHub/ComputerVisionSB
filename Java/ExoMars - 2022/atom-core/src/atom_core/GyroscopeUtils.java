package atom_core;

import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;

public class GyroscopeUtils
	extends Setup
{
	
	//CON ROTAZIONI CCW IL VALORE IN GRADI E' CRESCENTE, CON ROTAZIONI CW E' DECRESCENTE
	public static int getValue(EV3GyroSensor sensor)
	{
		final SampleProvider sp = sensor.getAngleMode();
		int value = 0;
	  	float [] sample = new float[sp.sampleSize()];
      	sp.fetchSample(sample, 0);
      	value = (int)sample[0];
      	return value;
	}
}