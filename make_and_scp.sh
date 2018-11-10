#!/usr/bin/env bash
mvn clean package
scp target/P2-1.0.jar mcdomingo@stargate.cs.usfca.edu:~/workplace/cs677/p2/
