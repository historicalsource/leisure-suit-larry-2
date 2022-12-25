;**
;**	Logics for room 80 -- Before the Glacier
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	September 29, 1988
;**


(script#				80)

(public
	rm80				0
)


;(define	vRm80							728)
	(define	lWaterfall		0)
	(define	lPlant			1)



(local
	aWaterfall
	seenMessage
)


(instance rm80 of Room
	(properties
		picture:		80
		horizon:		5
	)

	(method (init)
		(Load VIEW vRm80)

		(super init:)

		((View new:)
			view: 			vRm80
			loop: 			lPlant
			cel: 				0
			posn: 			175 154
			setPri: 			11
			addToPic:
		)
		((View new:)
			view: 			vRm80
			loop: 			lPlant
			cel: 				1
			posn: 			154 139
			setPri: 			10
			addToPic:
		)

		((= aWaterfall (Prop new:))
			view:				vRm80
			ignoreActors:
			setLoop:			lWaterfall
			posn:				127 77
			setCycle:		Forward
			cycleSpeed:		2
			init:
		)

		(self
			setRegions:		rgIsland
			setScript: 		rm80Script
		)

		(NormalEgo)
		(cond
			((== prevRoomNum	81)
				(ego
					posn: 	114 125
					loop:		facingWest
				)
			)
			(else
				(ego
					posn: 	265 185
					loop:		facingNorth
				)
			)
		)
		(ego					init:)
	)
)


(instance rm80Script of Script
	(method (doit)
		(super doit:)

		(cond
			((& (ego onControl:) cGreen)
				(if (== seenMessage FALSE)
					(= seenMessage TRUE)
					(Print "There's no need to return that way ever again, Larry.")
				)
			)
			((& (ego onControl:) cBlue)
				(curRoom newRoom: 81)
			)
			(else
				(= seenMessage FALSE)
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/fern, leaves, plant')
				(Print "It's a member of the fern family
					and one of the first true trees.
					Its species is over 300-million years old.
					Since most of the jokes in this game come from the same era,
					it seems appropriate.")
			)

			(if (Said '[/room, island, mountain]')
				(Print "What lies before you?")
			)
		)
	)
)

