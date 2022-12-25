;**
;**	Logics for room 12 -- Hollywood
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 25, 1988
;**


(script#				12)

(public
	rm12				0
)


(local
	aStreet
	aFreeway1
	aFreeway2
)


(instance rm12 of Room
	(properties
		picture:		12
		horizon:		5
		south:		16
		east:			13
		west:			11
	)

	(method (init)
		(Load VIEW vHollywood)

		(super init:)

		((View new:)							;**	hollywood sign
			view: 			vHollywood
			loop: 			0
			cel: 				0
			posn: 			152 30
			setPri: 			0
			ignoreActors:
			addToPic:
		)

		((= aStreet (Prop new:))			;**	street 1
			view:				vHollywood
			loop: 			1
			cel: 				0
			posn: 			123 108
			setCycle:		Forward
			cycleSpeed:		4
			isExtra:			TRUE
			init:
		)
		((= aFreeway1 (Prop new:))			;**	freeway 1
			view:				vHollywood
			loop: 			2
			cel: 				1
			posn: 			156 94
			setCycle:		Forward
			cycleSpeed:		1
			isExtra:			TRUE
			init:
		)
		((= aFreeway2 (Prop new:))			;**	freeway 2
			view:				vHollywood
			loop: 			3
			posn: 			220 74
			setCycle:		Forward
			cycleSpeed:		2
			isExtra:			TRUE
			init:
		)

		(cond
			((== prevRoomNum	16)	(ego posn: 230 188))
			((== prevRoomNum	0)	(ego posn: 230 188))
		)
		(NormalEgo)
		(ego					init:)

		(self
			setRegions:		rgLA
			setScript:		rm12Script
		)
	)
)


(instance rm12Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/hill')
				(Print "The Hollywood Hills are almost hidden by that smog.")
			)

			(if (Said '/freeway, car')
				(Print "Traffic on the Hollywood Freeway is medium to moderate,
					but surface streets offer a reasonable alternative
					for commuters.")
			)

			(if (Said '/building')
				(Print "Busy show-biz execs chase secretaries on every floor.")
			)

			(if (Said '[/city, sign, wall, fence, room]')
				(Print "You wonder what part of Los Angeles you are in now.")
			)
		)
	)
)

