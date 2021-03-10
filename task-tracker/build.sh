#!/usr/bin/env bash

../gradlew build

docker build -t uberpopug/task-tracker .
