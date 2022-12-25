;**
;**	Script 4 -- BASS_SETTER
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	May 11, 1988
;**


(script#			BASS_SETTER)


(class BassSetter of Code
	(properties
		radii		7								;**	default to 1/2-normal ego width
	)

	(method (doit whom)
		(whom brBottom:	(+ (whom y?) 1 ))
		(whom brTop:		(- (whom brBottom?) (whom yStep?)))
		(whom brLeft:		(- (whom x?) radii))
		(whom brRight:		(+ (whom x?) radii))
	)
)

