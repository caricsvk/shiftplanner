#!/bin/bash

mvn install

sudo docker build -t shiftplanner .
sudo docker run -ti -p 8080:8080 -d shiftplanner

