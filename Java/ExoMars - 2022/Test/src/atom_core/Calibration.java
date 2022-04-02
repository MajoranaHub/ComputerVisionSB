//PROGRAMMA DI TEST
package atom_core;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

class Calibration
  extends Setup
{  
  public static String[] stringArray(float[] rawValues)
  {
    String[] finalValues = new String[3];
    for (int i = 0; i < 3; i++) {
      finalValues[i] = Float.toString(rawValues[i]);
    }
    return finalValues;
  }
  
  public static void printValues(String[] values, String nome)
  {
    LCD.drawString("Values of " + nome, 0, 0);
    LCD.drawString("RED-GREEN-BLUE", 0, 1);
    LCD.drawString("--------------", 0, 2);
    for (int i = 0; i < 3; i++) {
      LCD.drawString(values[i], 0, 3 + i);
    }
  }
  
  public static void play()
  {
    while (Button.getButtons() != 32)
    {
      String[] finalValuesR = stringArray(ColorUtils.getValues(RColor));
      String[] finalValuesL = stringArray(ColorUtils.getValues(LColor));
      
      printValues(finalValuesR, "Port1");
      Button.waitForAnyPress();
      LCD.clear();
      
      printValues(finalValuesL, "Port4");
      Button.waitForAnyPress();
      LCD.clear();
      
      LCD.drawString("Distance sensor", 0, 2);
      LCD.drawString(Float.toString(UltrasonicUtils.getValue(Distance)), 0, 4);
      Button.waitForAnyPress();
      LCD.clear();
    }
  }
}