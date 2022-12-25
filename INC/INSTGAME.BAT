echo off
rem 	Parameters are
rem 		%1:	drive on which to install the program
rem 		%2:	drive from which installation is taking place

rem	Check to see that there is enough space for the installation.
space %1: 1750kb
if errorlevel 1 goto NoSpace

rem	Create the drivers subdirectory and copy the drivers.
godir %1:\sierra\LSL2
if errorlevel 1 goto CantCreate

rem	Copy over the necessary files.
copy %2:install.exe > NUL
copy %2:*.drv       > NUL
copy %2:exists.com  > NUL
copy %2:LSL2.bat .. > NUL
copy %2:__insth.bat > NUL

rem	Copy all the resources over.
__insth %1 %2

:NoSpace
echo There is not enough space on %1: to install 
echo "Leisure Suit Larry Goes Looking for Love (in Several Wrong Places)."
echo 1.75 megabytes of disk space are necessary.
pause
goto Exit

:CantCreate
echo Unable to create directory %1:\Sierra\LSL2.
pause
goto Exit

:Exit
