@echo off
if "%1" == "" goto doitNothing
:doit
if not "%1" == "0" goto doit1
b rm00?.sc -mll2keys
quit
:doit1
if not "%1" == "1" goto doit2
b rm01?.sc rm02?.sc rm03?.sc rm04?.sc -mll2keys
quit
:doit2
if not "%1" == "2" goto doith
b rm05?.sc rm06?.sc rm07?.sc rm08?.sc rm09?.sc -mll2keys
quit
:doith
if not "%1" == "h" goto doitb
b rm1??.sc -mll2keys
quit
:doitb
if not "%1" == "b" goto doitd
b rm007.sc rm125.sc rm037.sc rm045.sc rm151.sc rm178.sc -mll2keys
quit
:doitd
if not "%1" == "d" goto doitr
b rm011.* rm015.* rm016.* rm018.* rm025.* rm032.* rm033.* rm050.* rm053.* rm054.* rm057.* rm083.* rm101.* rm102.* -mll2keys
quit
:doitr
if not "%1" == "r" goto doits
b rm?00.sc rm?01.sc -mll2keys
quit
:doitr
if not "%1" == "s" goto doitNothing
b rm048.* rm178.* rm071.* rm152.* rm079.* rm082.* rm086.* rm093.* rm095.* rm103.* rm104.* rm181.* rm032.sc -mll2keys
quit
:doitNothing
echo Use: editall 0 1 2 h r b d s
echo Where 0 loads globals; 1 loads 11-48;   2 loads 50-96; H loads 101-178; 
echo       R loads regions; B loads barbers; D loads doors; S loads sounds;
