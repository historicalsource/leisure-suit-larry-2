@echo off
copy script\input.log+script\note.log+fixes tmp
del fixes
sort < tmp > fixes
del tmp
del script\input.log
del script\note.log
