@rem product mode
call 0-make-jar.cmd
@IF %ERRORLEVEL% GTR 0 goto L_ERR_1
setlocal
set remote_debug=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:46753
java %remote_debug% -Djava.io.tmpdir=tmpFolder -Dlog4j2.configurationFile=log4j2.xml -jar target/sub-uber.jar
endlocal
goto L_END

:L_ERR_1
echo compile error. need fixup
goto L_END

:L_END
