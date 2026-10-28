@echo off
setlocal
set "FX_LIB=C:\javafx-sdk-21\lib"
if not exist "%FX_LIB%" (
  echo JavaFX SDK lib folder not found: %FX_LIB%
  echo Edit FX_LIB in compile.bat to match your JavaFX SDK path.
  exit /b 1
)
if not exist out mkdir out
dir /b /s src\main\java\*.java > sources.txt
javac --module-path "%FX_LIB%" --add-modules javafx.controls -encoding UTF-8 -d out @sources.txt
if errorlevel 1 (
  echo.
  echo Compilation failed. Check JDK 21 and JavaFX SDK paths.
  exit /b 1
)
echo.
echo Compilation successful. Run run.bat next.
endlocal
