# importa le librerie
from collections import deque
from PIL import Image
import argparse
import imutils
import cv2
import numpy as np

#controlla il colore di un pixel specifico
#(si usa per controllare il colore del cerchio trovato)
def checkcolour(x,y,frame):
    color_coverted = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    pil_image=Image.fromarray(color_coverted)
    r, g, b = pil_image.getpixel((x, y))
    return(b,g,r)
                                 
#prende video dalla webcam
camera = cv2.VideoCapture(0)

# loop principale
while True:
    # prendi il frame corrente
    (grabbed, frame) = camera.read()

    #ritaglia il frame, copialo, convertilo in hsv e trasformal o in canny
    frame = imutils.resize(frame, width=600)
    copyframe = frame.copy()
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
    edges = cv2.Canny(frame,120,60)

    #toglie blob dall immagine in canny
    mask = cv2.dilate(edges, None, iterations=1)

    # trova i contorni della palla e ne trova il centro
    cnts = cv2.findContours(mask.copy(), cv2.RETR_EXTERNAL,
        cv2.CHAIN_APPROX_SIMPLE)[-2]
    center = None

    # procedi solo se vedi piu di 0 contorni
    if len(cnts) > 0:
        
            
       #trova il contorno piu ghrande e il minimo cerchio e centro
        c = max(cnts, key=cv2.contourArea)
        ((x, y), radius) = cv2.minEnclosingCircle(c)
        
        print(str(int(x))+" "+str(int(y)))
        
        color=checkcolour(int(x),int(y),copyframe)
        
        M = cv2.moments(c)
        center = (int(M["m10"] / M["m00"]), int(M["m01"] / M["m00"]))

        # procedi solo seil raggio e piu grande di
        if radius > 10:
            # disegna il cerchioe il centro
            # aggiorna la list dei punti trackati
            cv2.circle(frame, center, 5, (0, 0, 255), -1)
            cv2.putText(frame,str(int(x))+" "+str(int(y)),center,cv2.FONT_HERSHEY_SIMPLEX,1,color,2,cv2.LINE_AA)

    # mostra il frame 
    cv2.imshow("Frame", frame)
    cv2.imshow("edges",edges)
    key = cv2.waitKey(1) & 0xFF

    # se si preme il tasto q il loop si ferma
    if key == ord("q"):
        break

# pulisci la camera e chiudi le finestre
camera.release() 
cv2.destroyAllWindows()
