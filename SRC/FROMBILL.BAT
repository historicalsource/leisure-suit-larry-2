@echo off
ds en a:/s
dir a:\pic   > tmp.tmp
dir a:\view >> tmp.tmp
dir a:\file >> tmp.tmp
type tmp.tmp | more
call clean 
call d:\%game%\clean 
upd d:\%game%		 from \%game% 		 -f
upd d:\%game%\pic  from \%game%\pic  -f
upd d:\%game%\view from \%game%\view -f
copy a:file
copy a:pic pic
copy a:view view
slash /d
echo Send tmp file to printer?  (y/n):
slash /d
query
if not errorlevel 1 quit
copy tmp.tmp lpt1
cat file.* -n > lpt1
copy \epson\formfeed lpt1
