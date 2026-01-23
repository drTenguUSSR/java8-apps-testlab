@rem product mode
call 0-make-jar.cmd
set CUST_ERR=%ERRORLEVEL%
echo cust_err %CUST_ERR%
@pushd ..
@IF %CUST_ERR% GTR 0 goto L_ERR_1
setlocal
echo running ...
@rem -Dlog4j2.configurationFile=config/log4j2.xml -Dvar123=viaCmdBuildRun
set remote_debug=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:46753
java %remote_debug% -Djava.io.tmpdir=tmpFolder -jar build\libs\25C_kotlin-tech-0.0.1-SNAPSHOT.jar
endlocal
goto L_END

:L_ERR_1
echo compile error. need fixup
goto L_END

:L_END
@popd