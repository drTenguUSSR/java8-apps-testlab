setlocal
set remote_debug=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:46753
java %remote_debug% -Djava.io.tmpdir=tmpFolder -jar target/sub-prj-animals-by-classes-1.0-SNAPSHOT.jar
endlocal