@echo off
if [%JDK8_HOME%] NEQ [] (
	echo found jdk: %JDK8_HOME%
	set "PATH=%JDK8_HOME%\bin;%PATH%"
) else (
	echo env 'JDK8_HOME' not found. define for use special java
)
echo ===compiler
javac -version
echo ===runtime
java -version