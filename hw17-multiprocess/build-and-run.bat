
call ../gradlew.bat message-server:bootJar
call ../gradlew.bat db-server:bootJar
call ../gradlew.bat frontend:bootJar

start java -jar ../build/message-server/message-server.jar

set app=message-server.jar
:LOOPSTART
for /f "tokens=2" %%x in ('jps -m ^| find ^"%app%^"')  DO IF %%x == %app% goto FOUND
TIMEOUT /T 3
goto LOOPSTART

:FOUND

echo await server to start
TIMEOUT /T 15

start java -jar ../build/db-server/db-server.jar

start java -jar ../build/frontend/frontend.jar  --spring.config.location=application-front1.yaml
start java -jar ../build/frontend/frontend.jar  --spring.config.location=application-front12.yaml
start java -jar ../build/frontend/frontend.jar  --spring.config.location=application-front2.yaml



