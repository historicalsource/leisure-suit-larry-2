pause Ready to copy 360K floppy disks.  Insert Disk 1 in A: now.
copy \system\sciv.exe    a:sierra.exe
copy \system\install.exe a:
copy lsl2.bat            a:
copy instgame.bat        a:
copy *.com               a:
call drvcopy             a:
cd   360
copy __insth.bat         a:
copy resource.map        a:
copy resource.001        a:
copy \ll2\doco\debug.doc a:
pause   Insert Disk 2 now.
copy resource.002        a:
pause   Insert Disk 3 now.
copy resource.003        a:
pause   Insert Disk 4 now.
copy resource.004        a:
pause   Insert Disk 5 now.
copy resource.005        a:
pause   Insert Disk 6 now.
copy resource.006        a:
cd   ..
echo All done.
