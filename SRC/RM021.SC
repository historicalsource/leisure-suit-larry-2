;**
;**	Logics for room 21 -- Century City
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	August 31, 1988
;**


(script#				21)

(public
	rm21				0
)


(local
	aSign
)


(instance rm21 of Room
	(properties
		picture:		21
		horizon:		146
		north:		17
		south:		25
		east:			22
		west:			20
	)

	(method (init)
		(Load VIEW vLABarExt)

		(super init:)

		((View new:)							;**	graffiti
			view: 			vLABarExt
			loop: 			1
			cel: 				0
			posn: 			296 139
			setPri: 			10
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	more graffiti
			view: 			vLABarExt
			loop: 			1
			cel: 				1
			posn: 			259 135
			setPri: 			9
			ignoreActors:
			addToPic:
		)

		((= aSign (Prop new:))
			view: 			vLABarExt
			setLoop:			0
			setPri:			5
			posn:				302 86
			setCycle:		Forward
			cycleSpeed:		3
			init:
		)

		(cond
			((== prevRoomNum	25)			(ego posn: 222 188))
			((or	(== prevRoomNum	0)
					(== prevRoomNum	17))	(ego posn: 264 150))
			((== prevRoomNum 115)			(ego loop: facingEast
															posn: 178 178))
		)
		(NormalEgo)
		(ego					init:)

		(self
			setRegions:		rgLA
			setScript:		rm21Script
		)
	)
)


(instance rm21Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/litter, ground, street')
				(Print "This part of town is not as well-maintained as
					it should be.")
			)

			(if (Said '/bar')
				(Print "It looks too rough for a guy like you, Larry.
					Better stay out!")
			)

			(if (Said '/sign')
				(Print "\"BAR\"")
			)

			(if (Said '/graffiti')
				(Print "It says lots of dirty stuff, all of which you
					already know and therefore don't need to read here,
					in a family-oriented product such as this!")
			)

			(if (Said '[/building, room]')
				(Print "You are now in a slightly seedy section of the city.")
			)
		)
	)
)

