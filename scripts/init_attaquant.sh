#!/bin/bash

ROBONEWBIE_DIR=../RoboNewbie_1.0
ROBONEWBIE_JAR=dist/RoboNewbie_1.0.jar

cd $ROBONEWBIE_DIR

for i in {1..5}
do
	posx=$((-$i))
	posy=$((0))
	
	(java -cp $ROBONEWBIE_JAR gaming.AgentAttaquant Test $i $posx $posy &) 
	sleep 1
done
