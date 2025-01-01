@rem product mode
call 0-make-jar.cmd
@IF %ERRORLEVEL% GTR 0 goto L_ERR_1
setlocal
set remote_debug=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:46753
start java %remote_debug% -Djava.io.tmpdir=tmpFolder -jar target/supply-warehouse-j8-sb2-0.0.1-SNAPSHOT.jar
endlocal
goto L_END

:L_ERR_1
echo compile error. need fixup
goto L_END

:L_END
