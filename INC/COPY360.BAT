pause Ready to copy 360K floppy disks.  Insert Disk 1 in A: now.
copy \system\sciv.exe    a:sierra.exe
copy \system\install.exe a:
copy lsl2.bat            a:
copy instgame.bat        a:
copy \system\exists.com  a:
copy \system\godir.com   a:
copy \system\space.com   a:
call \system\drvcopy     a:
cd   360
copy __insth.bat         a:
copy resource.map        a:
copy resource.001        a:
volabel /l a:LSL2 Disk 1
pause   Insert Disk 2 now.
copy resource.002        a:
volabel /l a:LSL2 Disk 2
pause   Insert Disk 3 now.
copy resource.003        a:
volabel /l a:LSL2 Disk 3
pause   Insert Disk 4 now.
copy resource.004        a:
volabel /l a:LSL2 Disk 4
pause   Insert Disk 5 now.
copy resource.005        a:
volabel /l a:LSL2 Disk 5
pause   Insert Disk 6 now.
copy resource.006        a:
volabel /l a:LSL2 Disk 6
cd   ..
echo All done.
