#!/bin/bash

docker build . -t clinicaltrialsapp
docker run -it -d -p 8080:8080 clinicaltrialsapp:latest