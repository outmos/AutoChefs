#!/bin/sh
mysql -u root -p < resources/database/database.sql
java -jar dist/g06-iteration-4.jar

