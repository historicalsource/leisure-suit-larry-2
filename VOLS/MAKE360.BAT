cd 360
tm start               > ..\360.err
del resource.*
makevols -F..\360.src >> ..\360.err
tm stop               >> ..\360.err
cd ..
echo All done.
