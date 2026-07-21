@echo off
set MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6-bin\bin
if not exist "%MAVEN_HOME%\mvn.cmd" (
    echo Downloading Maven...
    mkdir "%MAVEN_HOME%" 2>nul
    curl -o maven.zip https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip
    tar -xf maven.zip -C "%MAVEN_HOME%\.."
    del maven.zip
)
"%MAVEN_HOME%\mvn.cmd" %*