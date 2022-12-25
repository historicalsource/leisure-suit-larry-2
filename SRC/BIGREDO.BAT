tm start                            > \ll2\redo.err
cd\system
call redo                          >> \ll2\redo.err
cd\ll2
copy \system\classdef
copy \system\selector
vc                                 >> \ll2\redo.err
copy vocab.000  script             >> \ll2\redo.err
sc @redo1.lst -oScript             >> \ll2\redo.err
sc @redo2.lst -oScript             >> \ll2\redo.err
sc @redo3.lst -oScript             >> \ll2\redo.err
sc rm043 	  -oScript             >> \ll2\redo.err
incver script\script.997 -vVersion >> \ll2\redo.err
del script\script.002              >> \ll2\redo.err
cd \ll2\vols
call makedisk                      >> \ll2\redo.err
cd\ll2
tm stop                            >> \ll2\redo.err
b redo.err -mll2keys
