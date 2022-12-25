cd 720
tm start               > ..\720.err
del resource.*
makevols -F..\720.src >> ..\720.err
tm stop               >> ..\720.err
cd ..
echo All done.
