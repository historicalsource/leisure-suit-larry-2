tm start                > redo.err
copy \system\classdef
copy \system\selector
call vc
copy vocab.000  script >> redo.err
sc @redo1.lst -oScript >> redo.err
sc @redo2.lst -oScript >> redo.err
sc @redo3.lst -oScript >> redo.err
sc rm043 	  -oScript >> redo.err
sc rm084 	  -oScript >> redo.err
call incver            >> redo.err
del script\script.002  >> redo.err
tm stop                >> redo.err
b redo.err -mll2keys
