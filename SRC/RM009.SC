;**
;**	Logics for room 9 -- the Boss Key
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	August 31, 1988
;**


(script#				9)

(public
	rm9				0
)

(instance rm9 of Room
	(properties
		picture:		178
	)

	(method (init)
		(super init:)
		(TheMenuBar hide:)
		(self setScript: rm9Script)
		(ego
			posn: 			99 1099
			setMotion:		FALSE
		)
		(User
			canControl: 	FALSE
			canInput:		TRUE
		)
		(Animate 0)

(Display "Classified Advertising Dept."	
												p_at: 0	20 p_font:GENEVA7 p_color:green)
(Display "Ball Street Journal" 		
												p_at: 0	28 p_font:GENEVA7 p_color:green)
(Display "10001 Broadway" 				
												p_at: 0	36 p_font:GENEVA7 p_color:green)
(Display "New York, NY 10001" 		
												p_at: 0	44 p_font:GENEVA7 p_color:green)

(Display "Gentlemen" 					
												p_at: 0	60 p_font:GENEVA7 p_color:green)

(Display "Enclosed please find my personal check in the amount of $57.30 to"
												p_at: 0	76 p_font:GENEVA7 p_color:green)
(Display "cover three insertions of the following classified advertisement"
												p_at: 0	84 p_font:GENEVA7 p_color:green)
(Display "in your \"Seeking Position\" department:"
												p_at: 0	92 p_font:GENEVA7 p_color:green)

(Display "Mature, experienced software analyst available."
												p_at:50 108 p_font:GENEVA7 p_color:green)
(Display "Bug installation a proven speciality. \"No version"
												p_at:50 116 p_font:GENEVA7 p_color:green)
(Display "too clean.\" Familiar with DOS 1.0, AGI and SCI."
												p_at:50 124 p_font:GENEVA7 p_color:green)
	)
)


(instance rm9Script of Script
	(method (doit)
		(super doit:)
	)

	(method (handleEvent event)
		(if (or (== (event type?) mouseUp) (event claimed?)) (return))

		(Print "Sorry, but from here, all you can do is Restart or Restore a
			saved game.")
		(Print "(Hope you didn't get caught!)" LOW)
		(event claimed: TRUE)
	)
)

