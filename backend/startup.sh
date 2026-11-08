#!/bin/sh

./wait-for-it.sh postgres:5432 -s -t 60
./wait-for-it.sh redis:6379 -s -t 60

/javaruntime/bin/java -jar /app/backend.jar