import socket
import pickle
import cv2
import imutils
from PIL import Image

HSIZE = 10

ballCounter = 0
evaquationCompleted = False

def checkcolour(x,y,frame):
    color_coverted = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    pil_image=Image.fromarray(color_coverted)
    r, g, b = pil_image.getpixel((x, y))
    return(b,g,r)

def trackobject(frame, object):
    (grabbed, frame) = camera.read()
    x, y = -1
    frame = imutils.resize(frame, width=600)
    copyframe = frame.copy()
    # blurred = cv2.GaussianBlur(frame, (11, 11), 0)
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
    if (object == 'ball'):
        edges = cv2.Canny(frame,70,110)
        mask = cv2.dilate(edges, None, iterations=1)

    elif (object == 'zone'):
        blackLower = (12, 0, 0) #da cambiare
        blackUpper = (119, 85, 255) #da cambiare
        mask = cv2.inRange(hsv, blackLower, blackUpper)
        mask = cv2.erode(mask, None, iterations=2)
        mask = cv2.dilate(edges, None, iterations=1)

    elif (object == 'line'):
        greenLower = (12, 0, 0)
        greenUpper = (119, 85, 255)
        mask = cv2.inRange(hsv, greenLower, greenUpper)
        mask = cv2.erode(mask, None, iterations=2)
        mask = cv2.dilate(edges, None, iterations=1)

    # construct a mask for the color "green", then perform
    # a series of dilations and erosions to remove any small
    # blobs left in the mask
    #mask = cv2.inRange(hsv, greenLower, greenUpper)
    #mask = cv2.erode(mask, None, iterations=2)
    #mask = cv2.dilate(edges, None, iterations=1)

    # find contours in the mask and initialize the current
    # (x, y) center of the ball
    cnts = cv2.findContours(mask.copy(), cv2.RETR_EXTERNAL,
        cv2.CHAIN_APPROX_SIMPLE)[-2]
    center = None

    # only proceed if at least one contour was found
    if len(cnts) > 0:
        # find the largest contour in the mask, then use
        # it to compute the minimum enclosing circle and
        # centroid
        c = max(cnts, key=cv2.contourArea)
        ((x, y), radius) = cv2.minEnclosingCircle(c)
    
    return x, y

s = socket.socket()
s.bind(('127.0.0.1', 1500))
s.listen(5)

while True:
    clientsocket, address = s.accept()
    print(f"Connection to {address} has been established!")

    camera = cv2.VideoCapture(0)
    
    while True:
        ack = clientsocket.recv(1024).decode('utf-8')         
        if ack == 'ok' :

            if ballCounter < 4:
                x, y = trackobject('ball')

                if x != -1 and y != -1:
                    if x < 250:
                        msg = b'left'
                        clientsocket.send(msg)
                    elif x > 350:
                        msg = b'right'
                        clientsocket.send(msg)
                    elif y < 250:
                        msg = b'forw'
                        clientsocket.send(msg)
                    else:
                        msg = b'grab'
                        clientsocket.send(msg)
                        ballCounter += 1
                else: clientsocket.send(b'noxy')
                
            elif not(evaquationCompleted):
                #cerca zona di evaquazione
                x, y = trackobject('zone')
                if x != -1 and y != -1:
                    if x < 250:
                        msg = b'left'
                        clientsocket.send(msg)
                    elif x > 350:
                        msg = b'right'
                        clientsocket.send(msg)
                    elif y < 250:
                        msg = b'forw'
                        clientsocket.send(msg)
                    else:
                        msg = b'drop'
                        clientsocket.send(msg)
                else: clientsocket.send(b'noxy')
            else:
                #go to green line
                x, y = trackobject('line')
                if x != -1 and y != -1:
                    if x < 250:
                        msg = b'left'
                        clientsocket.send(msg)
                    elif x > 350:
                        msg = b'right'
                        clientsocket.send(msg)
                    elif y < 250:
                        msg = b'forw'
                        clientsocket.send(msg)
                    else:
                        msg = b'exit'
                        clientsocket.send(msg)
                else: clientsocket.send(b'noxy')
            