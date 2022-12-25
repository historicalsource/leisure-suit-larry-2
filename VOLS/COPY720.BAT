pause Ready to copy 720K floppy disks.  Insert Disk 1 in B: now.
copy \system\sciv.exe    b:sierra.exe
copy \system\install.exe b:
copy lsl2.bat            b:
copy instgame.bat        b:
copy *.com               b:
call drvcopy             b:
cd   720
copy __insth.bat         b:
copy resource.map        b:
copy resource.001        b:
copy \ll2\doco\debug.doc b:
pause   Insert Disk 2 now.
copy resource.002        b:
pause   Insert Disk 3 now.
copy resource.003        b:
cd   ..
echo All done.
