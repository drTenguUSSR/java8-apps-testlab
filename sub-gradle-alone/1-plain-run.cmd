setlocal
set remote_debug=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:46753
java %remote_debug% -Djava.io.tmpdir=tmpFolder -Dlog4j2.configurationFile=log4j2.xml -jar build\libs\sub-gradle-alone.jar 
endlocal