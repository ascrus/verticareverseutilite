#! /bin/sh

IZPACK_JAVA_HOME=%JAVA_HOME

if [ -d "$JAVA_HOME" -a -x "$JAVA_HOME/bin/java" ]; then
	JAVACMD="$JAVA_HOME/bin/java"
elif [ -d "$IZPACK_JAVA_HOME" -a -x "$IZPACK_JAVA_HOME/bin/java" ]; then
	JAVACMD="$IZPACK_JAVA_HOME/bin/java"
else
	JAVACMD=java
fi

$JAVACMD -splash:splash.gif -jar SQLReversingForVertica-1.0.0.jar
