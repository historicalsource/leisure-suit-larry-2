pause Ready to copy 720K floppy disks.  Insert Disk 1 in B: now.
copy \system\sciv.exe    b:sierra.exe
copy \system\install.exe b:
copy lsl2.bat            b:
copy instgame.bat        b:
copy \system\exists.com  b:
copy \system\godir.com   b:
copy \system\space.com   b:
call \system\drvcopy     b:
cd   720
copy __insth.bat         b:
copy resource.map        b:
copy resource.001        b:
volabel /l b:LSL2 Disk 1
pause   Insert Disk 2 now.
copy resource.002        b:
volabel /l b:LSL2 Disk 2
pause   Insert Disk 3 now.
copy resource.003        b:
volabel /l b:LSL2 Disk 3
cd   ..
echo All done.
