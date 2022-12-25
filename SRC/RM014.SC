;**
;**	Logics for room 14 -- quickie mart
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 23, 1988
;**


(script#				14)

(public
	rm14				0
)


;(define	vQuikiExt					243)


(local
	aSign
)


(instance rm14 of Room
	(properties
		picture:		14
		horizon:		50
		south:		18
		west:			13
	)

	(method (init)
		(Load VIEW vQuikiExt)

		(super init:)

		((View new:)							;**	quikie sign
			view: 			vQuikiExt
			loop: 			0
			cel: 				1
			posn: 			96 53
			setPri: 			2
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	pepsi sign
			view: 			vQuikiExt
			loop: 			0
			cel: 				0
			posn: 			17 58
			setPri: 			2
			addToPic:
		)
		((View new:)							;**	pepsi sign
			view: 			vQuikiExt
			loop: 			0
			cel: 				0
			posn: 			175 58
			setPri: 			2
			addToPic:
		)
		((View new:)							;**	beer sign
			view: 			vQuikiExt
			loop: 			1
			cel: 				1
			posn: 			99 74
			setPri: 			12
			addToPic:
		)
		((View new:)							;**	wine sign
			view: 			vQuikiExt
			loop: 			1
			cel: 				2
			posn: 			166 82
			setPri: 			12
			addToPic:
		)
		((View new:)							;**	lotto sign
			view: 			vQuikiExt
			loop: 			1
			cel: 				0
			posn: 			105 135
			setPri: 			9
			addToPic:
		)

		((= aSign (Prop new:))				;**	24hours sign
			view: 			vQuikiExt
			loop: 			2
			cel: 				0
			posn: 			34 105
			setPri: 			12
			setCycle:		Forward
			cycleSpeed:		10
			isExtra:			TRUE
			init:
		)

		(cond
			((== prevRoomNum	 0)		(ego posn: 312 188))
			((== prevRoomNum	18)		(ego posn: 312 188))
			((== prevRoomNum 114)		(ego posn:	36 153))
		)
		(NormalEgo)
		(ego					init:)

		(self
			setRegions:		rgLA
			setScript:		rm14Script
		)
	)
)


(instance rm14Script of Script
	(method (doit)
		(super doit:)

		(if (& (ego onControl: origin) cBlue)
			(curRoom newRoom: 114)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/ground')
				(Print "The sidewalk here is littered with junk food wrappers
					and old paper cups.")
			)

			(if (Said '/window, sign')
				(Print "From the looks of this window,
					they sell things inside.")
			)

			(if (Said '[/store, convenience, building, room]')
				(Print "Only in Southern California would you find a
					convenience store without a door.
					\"Gosh,\" you think, \"I wonder if they're open?\""
				)
			)
		)

		(if (Said 'open/door')
			(Print "What door?")
		)
	)
)

