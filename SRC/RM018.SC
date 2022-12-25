;**
;**	Logics for room 18 -- drug store exterior
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 23, 1988
;**


(script#				18)

(public
	rm18				0
)


;(define	vDrugExt						227)
;(define	vDrugExtSign				240)


(local
	aDoor
)


(instance rm18 of Room
	(properties
		picture:		18
		horizon:		113
		north:		14
		south:		22
		east:			22
		west:			17
	)

	(method (init)
		(Load VIEW vDrugExt)
		(Load VIEW vDrugExtSign)

		(super init:)

		((View new:)
			view: 			vDrugExtSign
			loop: 			0
			cel: 				0
			posn: 			60 32
			setPri: 			1
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vDrugExtSign
			loop: 			1
			cel: 				0
			posn: 			196 32
			setPri: 			1
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vDrugExt
			loop: 			1
			cel: 				2
			posn: 			170 83
			setPri: 			4
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vDrugExt
			loop: 			1
			cel: 				1
			posn: 			233 125
			setPri: 			11
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vDrugExt
			loop: 			1
			cel: 				0
			posn: 			98 138
			setPri: 			11
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vDrugExt
			loop: 			1
			cel: 				3
			posn: 			20 137
			setPri: 			11
			ignoreActors:
			addToPic:
		)

		(cond
			((== prevRoomNum	 0)	(ego posn: 253 120))
			((== prevRoomNum	14)	(ego posn: 253 120))
			((== prevRoomNum 118)	(ego posn: 172 147))
		)
		(NormalEgo)
		((= aDoor (AutoDoor new:))
			view:				vDrugExt
			setLoop:			0
			posn:				172 151
			setPri:			11
			entranceTo:		118
			locked:			FALSE
			msgLook:			"Through this door, you see a store."
			init:
		)
		(ego					init:)

		(self
			setRegions:		rgLA
			setScript:		rm18Script
		)
	)
)


(instance rm18Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/sign, window')
				(Print "Even in Hollywood, they're having trouble moving
					New Age gear!")
			)

			(if (Said '[/building, shop, room]')
				(Print "Swabs Drug Store has everything a guy like
					you might need.")
			)
		)
	)
)

