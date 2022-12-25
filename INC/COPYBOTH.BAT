rem Ready to copy both 360K and 720K floppy disks.  
rem Insert 360K Disk 1 in A: AND 720K disk in B: now.
pause
copy \system\sciv.exe    a:sierra.exe
copy \system\install.exe a:
copy lsl2.bat            a:
copy instgame.bat        a:
copy \system\exists.com  a:
copy \system\godir.com   a:
copy \system\space.com   a:
call \system\drvcopy     a:
copy 360\__insth.bat     a:
copy 360\resource.map    a:
copy 360\resource.001    a:
volabel /l a:LSL2 Disk 1
cls
rem 




                   Change Drive A: to Disk #2.
copy \system\sciv.exe    b:sierra.exe
copy \system\install.exe b:
copy lsl2.bat            b:
copy instgame.bat        b:
copy \system\exists.com  b:
copy \system\godir.com   b:
copy \system\space.com   b:
call \system\drvcopy     b:
copy 720\__insth.bat     b:
copy 720\resource.map    b:
copy 720\resource.001    b:
volabel /l b:LSL2 Disk 1
cls
rem 




                   Change Drive B: to Disk #2.
copy 360\resource.002    a:
volabel /l a:LSL2 Disk 2

cls
rem 




                   Change Drive A: to Disk #3.
copy 720\resource.002    b:
volabel /l b:LSL2 Disk 2
cls
rem 




                   Change Drive B: to Disk #3.

copy 360\resource.003    a:
volabel /l a:LSL2 Disk 3
cls
rem 




                   Change Drive A: to Disk #4.

copy 720\resource.003    b:
volabel /l b:LSL2 Disk 3
cls
rem 




                   All done with Drive B:

copy 360\resource.004    a:
volabel /l a:LSL2 Disk 4
rem 




                   Change Drive A: to Disk #5.
pause
copy 360\resource.005    a:
volabel /l a:LSL2 Disk 5
rem 




                   Change Drive A: to Disk #6.
pause
copy 360\resource.006    a:
volabel /l a:LSL2 Disk 6
cd   ..
rem All done.
