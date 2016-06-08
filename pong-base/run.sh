#!/bin/bash 

cd "$(dirname "$0")"

java -cp $(echo lib/*.jar | tr ' ' ':') -Dserver-id=$(hostname) com.example.Pong