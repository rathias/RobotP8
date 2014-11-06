#!/bin/bash

ROBONEWBIE_DIR=../RoboNewbie_1.0
ROBONEWBIE_JAR=$ROBONEWBIE_DIR/dist/RoboNewbie_1.0.jar

for i in {1..5}
do
	posx=$((-$i))
	posy=$((0))
	(java -cp $ROBONEWBIE_JAR gaming.Agent_BasicStructure AlphaTeam $i $posx $posy &) 
	sleep 1
done
