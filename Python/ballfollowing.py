import socket
import time
import cv2
import imutils
from PIL import Image
import numpy as np

evaquationCompleted = True

s = socket.socket()
s.bind(('10.0.1.4', 1500))

def checkcolour(x,y,frame):
    color_coverted = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    pil_image=Image.fromarray(color_coverted)
    r, g, b = pil_image.getpixel((x, y))
    return(b,g,r)

def trackobject(oggetto):
    (grabbed, frame) = camera.read()
    x = -1
    y = -1
    frame = imutils.resize(frame, width=640)
    copyframe = frame.copy()
    # blurred = cv2.GaussianBlur(frame, (11, 11), 0)
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
    if (oggetto == 'ball'):
        edges = cv2.Canny(frame,70,110)
        mask = cv2.dilate(edges, None, iterations=1)

    elif (oggetto == 'zone'):
        blackLower = (12, 0, 0) #da cambiare
        blackUpper = (119, 85, 255) #da cambiare
        mask = cv2.inRange(hsv, blackLower, blackUpper)
        erodeKernel = np.ones((10,10), np.uint8)
        dilateKernel = np.ones((8,8), np.uint8)
        mask = cv2.erode(mask, erodeKernel, iterations=2)
        mask = cv2.dilate(mask, dilateKernel, iterations=3)

    elif (oggetto == 'line'):
        greenLower = (0, 150, 40) #da cambiare
        greenUpper = (119, 85, 95) #da cambiare
        mask = cv2.inRange(hsv, greenLower, greenUpper)
        erodeKernel = np.ones((10,10), np.uint8)
        dilateKernel = np.ones((8,8), np.uint8)
        mask = cv2.erode(mask, erodeKernel, iterations=2)
        mask = cv2.dilate(mask, dilateKernel, iterations=3)

    # construct a mask for the color "green", then perform
    # a series of dilations and erosions to remove any small
    # blobs left in the mask

    # find contours in the mask and initialize the current
    # (x, y) center of the ball
    cnts = cv2.findContours(mask.copy(), cv2.RETR_EXTERNAL,
        cv2.CHAIN_APPROX_SIMPLE)[-2]
    center = None
    
    # only proceed if at least one contour was found
    if len(cnts) > 0:
        # find the largest contour in the mask, then use
        # it to compute the minimum enclosing circle and
        # centre

        c = max(cnts, key=cv2.contourArea)
        ((x, y), radius) = cv2.minEnclosingCircle(c)
        color = checkcolour(int(x),int(y),copyframe)
        M = cv2.moments(c)
        center = (int(M["m10"] / M["m00"]), int(M["m01"] / M["m00"]))

        

        # only proceed if the radius meets a minimum size
        #if radius > 10:
            # draw the circle and centroid on the frame,
            # then update the list of tracked points
            #cv2.circle(frame, (int(x), int(y)), int(radius),
                #(0, 255, 255), 2)

        cv2.circle(frame, center, 5, (0, 0, 255), -1)
        cv2.putText(frame,str(int(x))+" "+str(int(y)),center,cv2.FONT_HERSHEY_SIMPLEX,1,color,2,cv2.LINE_AA)

    #cv2.imshow("frame", frame)
    #cv2.imshow("edges", edges)
    #key = cv2.waitKey(2) & 0xFF
    
    return x, y


s.listen(5)

while True:
    clientsocket, address = s.accept()
    print(f"Connection to {address} has been established!")

    camera = cv2.VideoCapture(0)
    startTime = int(time.time())
    
    while True:
        ack = clientsocket.recv(2)
        print(ack)
        if ack == b'ok' :
            
            if time.time() - startTime < 60:
                x, y = trackobject('ball')
                print(f"x:{x}, y:{y}")
                
                if x > -1 and y > -1:
                    print('going to ball')
                    if x < 220:
                        msg = 'ccw\n'
                        clientsocket.send(msg.encode('utf-8'))
                    elif x > 420:
                        msg = 'cw\n'
                        clientsocket.send(msg.encode('utf-8'))
                    elif y < 350:
                        msg = 'forw\n'
                        clientsocket.send(msg.encode('utf-8'))
                    else:
                        msg = 'grab\n'
                        clientsocket.send(msg.encode('utf-8'))
                        #ballCounter += 1
                else: print('noxy')
			
                
            elif not(evaquationCompleted):
                #cerca zona di evaquazione
                x, y = trackobject('zone')
                print(f"x:{x}, y:{y}")

                if x != -1 and y != -1:
                    print('going to zone')
                    if x < 220:
                        msg = 'ccw\n'
                        clientsocket.send(msg.encode('utf-8'))
                    elif x > 420:
                        msg = 'cw\n'
                        clientsocket.send(msg.encode('utf-8'))
                    elif y < 350:
                        msg = 'forw\n'
                        clientsocket.send(msg.encode('utf-8'))
                    else:
                        msg = 'drop\n'
                        clientsocket.send(msg.encode('utf-8'))
                        evaquationCompleted = False
                else: print('noxy')

            else:
                #go to green line
                x, y = trackobject('line')
                print(f"x:{x}, y:{y}")

                if x != -1 and y != -1:
                    print('going to line')
                    if x < 220:
                        msg = 'ccw\n'
                        clientsocket.send(msg.encode('utf-8'))
                    elif x > 420:
                        msg = 'cw\n'
                        clientsocket.send(msg.encode('utf-8'))
                    elif y < 350:
                        msg = 'forw\n'
                        clientsocket.send(msg.encode('utf-8'))
                    else:
                        msg = 'exit\n'
                        clientsocket.send(msg.encode('utf-8'))
                else: print('noxy')