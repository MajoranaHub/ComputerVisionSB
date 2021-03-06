package atom_core;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

class Calibration
  extends Setup
{  
  //Converte un array di float in un array di string
  public static String[] stringArray(float[] rawValues)
  {
    String[] finalValues = new String[3];
    for (int i = 0; i < 3; i++) {
      finalValues[i] = Float.toString(rawValues[i]);
    }
    return finalValues;
  }
  
  //Stampa i valori di un array di float in maniera formattata
  public static void printValues(String[] values, String nome, float[] originalValues)
  {
    LCD.drawString("Values of " + nome, 0, 0);
    LCD.drawString("RED-GREEN-BLUE", 0, 1);
    LCD.drawString("--------------", 0, 2);
    for (int i = 0; i < 3; i++) {
      LCD.drawString(values[i], 0, 3 + i);
    }
    
    if(ColorUtils.black(originalValues)) LCD.drawString("Nero", 0, 7);
    else if(ColorUtils.green(originalValues)) LCD.drawString("Verde", 0, 7);
    else if(ColorUtils.silver(originalValues)) LCD.drawString("Argento", 0, 7);
    else if(ColorUtils.red(originalValues)) LCD.drawString("Rosso", 0, 7);
    else if(ColorUtils.white(originalValues)) LCD.drawString("Bianco", 0, 7);
  }
  
  public static void play()
  {
    while (Button.getButtons() != Button.ID_ESCAPE)
    {
      //Legge e converte i valori dei sensori di colore
      float[] originalValuesR = ColorUtils.getValues(RColor);
      float[] originalValuesL = ColorUtils.getValues(LColor);
      String[] finalValuesR = stringArray(originalValuesR);
      String[] finalValuesL = stringArray(originalValuesL);
      
      printValues(finalValuesR, "Port1", originalValuesR);
      Button.waitForAnyPress();
      LCD.clear();
      
      printValues(finalValuesL, "Port4", originalValuesL);
      Button.waitForAnyPress();
      LCD.clear();
      
      //Legge e stampa i valori dell'ultrasuoni
      LCD.drawString("Distance sensor UP", 0, 2);
      LCD.drawString(Float.toString(UltrasonicUtils.getValue(DistanceUP)), 0, 3);
      Button.waitForAnyPress();
      LCD.clear();
    }
  }
}