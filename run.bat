@echo off
setlocal

rem Check if java is already in PATH
where java >nul 2>nul
if %ERRORLEVEL% equ 0 goto RUN_JAVA

rem Check installed user JDK
if exist "%LOCALAPPDATA%\Programs\Java\jdk17.0.20_12\bin\java.exe" (
    set "JAVA_HOME=%LOCALAPPDATA%\Programs\Java\jdk17.0.20_12"
    set "PATH=%LOCALAPPDATA%\Programs\Java\jdk17.0.20_12\bin;%PATH%"
    goto RUN_JAVA
)

rem Check other common Java locations
if exist "%JAVA_HOME%\bin\java.exe" (
    set "PATH=%JAVA_HOME%\bin;%PATH%"
    goto RUN_JAVA
)

echo [ERROR] Java could not be found. Please ensure JDK 17 is installed.
pause
exit /b 1

:RUN_JAVA
echo ========================================================
echo Starting Student Management System Prototype...
echo ========================================================

cd /d "%~dp0prototype"
javac *.java
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Compilation failed.
    pause
    exit /b %ERRORLEVEL%
)

start javaw LoginPrototype
exit /b 0
