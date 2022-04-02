package atom_core;

import lejos.hardware.device.NXTCam;
import lejos.robotics.geometry.Rectangle2D;

public class Pixy
	extends Setup
{
	static Rectangle2D prova;
	public static Rectangle2D getNumber(NXTCam sensor, int obj)
	{
		prova = sensor.getRectangle(obj);
		return prova;
	}
	
}