;**
;**	Logics for room 58 -- the jetway from the Airport to the Airplane
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 15, 1988
;**


(script#				58)

(public
	rm58				0
)


(local
	work
	seenMessage
	aPlane
)


(enum												;**	rmScript state values
	intro
)


(instance blockWest of Block
	(properties
		top				129
		bottom			136
		left				-20
		right				-10
	)
)


(instance rm58 of Room
	(properties
		picture:		58
		horizon:		1
	)

	(method (init)
		(Load VIEW vAirplane)
		(Load VIEW vStewardess)

		(super init:)

		((View new:)							;**	stewardess
			view: 			vStewardess
			loop: 			facingWest
			cel: 				4
			posn: 			311 133
			addToPic:
		)

		((= aPlane (Airplane new:))	;**	how's this for minimalist programming?
			startY:			8
			endY:				1
			delayMin:		2
			delayMax:		3
			init:
		)

		(self
			setRegions:		rgAirport
			setScript: 		rm58Script
		)

		(blockWest 			init:)

		(NormalEgo facingEast)
		(ego
			posn:				10 133
			observeBlocks:	blockWest
			init:
		)
	)
)


(instance rm58Script of Script
	(method (doit)
		(super doit:)

		(if (& (ego onControl:) cBlue)
			(curRoom newRoom: 61)
		)

		(if (& (ego onControl:) cGreen)
			(if (not seenMessage)
				(= seenMessage TRUE)
				(Print "You have no reason to return there.
					Hurry, Larry; you don't want to miss your plane!")
			)
		else
			(= seenMessage FALSE)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '[/room, airplane]')
				(Print "This jetway stretches between the airport waiting room
					and your flight to freedom.")
				(Print "(But, where are you going?)" LOW)
			)
		)
	)
)

