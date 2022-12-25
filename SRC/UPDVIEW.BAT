@echo off
echo	Put VIEW disks in drives A & B.
pause
cd\ll2\view
ds en
md a:\view
xcopy view.* a:\view
md b:\view
xcopy view.* b:\view
cd\ll2
