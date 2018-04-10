@echo off

set IZPACK_JAVA="%JAVA_HOME%"
 
if exist "%IZPACK_JAVA%\bin\javaw.exe" (
  set LOCAL_JAVA=%IZPACK_JAVA%\bin\javaw.exe
) else (
  set LOCAL_JAVA=javaw.exe
)

echo Using java: %LOCAL_JAVA%

start "SQLReversingForVertica" /B "%LOCAL_JAVA%" -splash:splash.gif -jar SQLReversingForVertica-1.0.1.jar