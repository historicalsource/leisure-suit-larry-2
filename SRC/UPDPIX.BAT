@echo off
echo Put PICTURE and SOUND disks in drives A & B.
pause
cd\ll2
ds en/s
md a:\pic
md a:\sound
xcopy pic a:\pic
xcopy sound a:\sound
md b:\pic
md b:\sound
xcopy pic b:\pic
xcopy sound b:\sound
cd\ll2
