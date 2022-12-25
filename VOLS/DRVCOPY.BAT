@echo off
echo Copying video drivers
copy \system\ega320.drv   %1
copy \system\mcga320.drv  %1
copy \system\tandy320.drv %1
copy \system\cga320bw.drv %1
copy \system\cga320c.drv  %1
echo Copying sound drivers
copy \system\std.drv      %1
copy \system\jr.drv       %1
copy \system\imf.drv      %1
copy \system\mt32.drv     %1
copy \system\adl.drv      %1
echo Copying keyboard drivers
copy \system\ibmkbd.drv   %1
copy \system\tandykbd.drv %1
echo Copying input drivers
copy \system\joystick.drv %1
echo on
