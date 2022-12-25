@echo off
echo	Put SOURCE CODE disks in drives A & B.
pause
cd\ll2
ds en/s
call clean
md a:\src
xcopy *.* a:src
md b:\src
md b:\inc
xcopy *.* b:src
xcopy inc b:\inc
