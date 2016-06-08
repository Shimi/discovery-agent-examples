#!/bin/bash 

cd "$(dirname "$0")"

java -cp $(echo lib/*.jar | tr ' ' ':') com.example.Ping