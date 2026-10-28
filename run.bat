@echo off
setlocal
set "FX_LIB=C:\javafx-sdk-21\lib"
if not exist "%FX_LIB%" (
  echo JavaFX SDK lib folder not found: %FX_LIB%
  echo Edit FX_LIB in run.bat to match your JavaFX SDK path.
  exit /b 1
)
if not exist out\lifelink\LifeLink.class (
  echo Compiled classes not found. Run compile.bat first.
  exit /b 1
)
java --module-path "%FX_LIB%" --add-modules javafx.controls -cp out lifelink.LifeLink
endlocal
