#!/bin/bash

export SBT_OPTS="-XX:MaxMetaspaceSize=1G"
sbt clean coverage test it:test component:test coverageOff coverageReport
python dependencyReport.py api-example-microservice
