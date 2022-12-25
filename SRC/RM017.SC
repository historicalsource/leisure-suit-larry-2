;**
;**	Logics for room 17 -- Brown Derby
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 6, 1988
;**


(script#				17)

(public
	rm17				0
)


(instance rm17 of Room
	(properties
		picture:		17
		horizon:		5
		north:		13
		south:		21
		east:			18
		west:			16
	)

	(method (init)
		(Load VIEW vBrownDerby)

		(super init:)

		((View new:)							;**	drugstore sign
			view: 			vBrownDerby
			loop: 			0
			cel: 				0
			posn: 			311 129
			setPri: 			9
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	left window
			view: 			vBrownDerby
			loop: 			0
			cel: 				1
			posn: 			18 111
			setPri: 			7
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	derby sign
			view: 			vBrownDerby
			loop: 			0
			cel: 				2
			posn: 			92 73
			setPri: 			9
			ignoreActors:
			addToPic:
		)

		(cond
			((== prevRoomNum	0)		(ego posn: 260 188))
			((== prevRoomNum	13)		(ego posn: 194	77))
			((== prevRoomNum	21)		(ego posn: 260 188))
		)
		(NormalEgo)
		(ego					init:)

		(self
			setRegions:		rgLA
			setScript:		rm17Script
		)
	)
)


(instance rm17Script of Script
	(method (doit)
		(super doit:)

		(if (& (ego onControl:) cBlue)
			(curRoom newRoom: 13)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (or	(Said 'look<inside')
					(Said 'get, look/junk'))
			(Print "There is nothing inside of interest to you.")
		)

		(if (Said 'open/door')
			(Print "You can't. It's boarded up.")
		)

		(if (Said 'knock')
			(Print "They're closed.")
		)

		(if (Said 'look>')
			(if (Said '/window')
				(Print "Peering through the dirty windows,
					you see an interior filled with memories of a bygone
					day, and junk.")
			)

			(if (Said '/sign')
				(Print "\"The Brown Derby\"")
				(Print "\"Closed for reblocking\""
					font:		smallFont)
			)

			(if (Said '/door')
				(Print "It's boarded up.")
			)

			(if (Said '/building<behind')
				(Print "It's an empty parking lot.")
			)

			(if (Said '[/restaurant, shop, building, room]')
				(Print "This place has been closed for years.")
				(Print "It's in desperate need of blocking.")
			)
		)
	)
)

