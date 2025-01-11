@rem set remote_debug=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:46753
@rem java %remote_debug% -Djava.io.tmpdir=tmpFolder -Dlog4j2.configurationFile=config/log4j2.xml -Dvar123=plain123 -jar build\libs\24C-AspectJ.jar
setlocal
set GRADLE_OPTS=-Dvar123=441
gradle run
endlocal