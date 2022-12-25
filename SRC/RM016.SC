;**
;**	Logics for room 16 -- outside of Rodeo Drive clothing store
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 23, 1988
;**


(script#				16)

(public
	rm16				0
)


;(define	vClothingExt				224)


(local
	work
	aDoor
)


(instance rm16 of Room
	(properties
		picture:		16
		horizon:		111
		south:		20
		east:			17
		west:			15
	)

	(method (init)
		(Load VIEW vClothingExt)

		(super init:)

		((View new:)							;**	entrance sign
			view: 			vClothingExt
			loop: 			1
			cel: 				1
			posn: 			150 141
			setPri: 			10
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	canvas sign
			view: 			vClothingExt
			loop: 			1
			cel: 				0
			posn: 			150 84
			setPri: 			15
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	open sign
			view: 			vClothingExt
			loop: 			1
			cel: 				2
			posn: 			113 118
			setPri: 			8
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	rodeo sign
			view: 			vClothingExt
			loop: 			1
			cel: 				3
			posn: 			299 93
			setPri: 			13
			ignoreActors:
			addToPic:
		)

		(cond
			((== prevRoomNum	 0)	(ego posn: 248 134))
			((== prevRoomNum	12)	(ego posn: 252 136))
			((== prevRoomNum	20)	(ego posn:	73 188))
			((== prevRoomNum 116)	(ego posn: 150 131))
		)
		(NormalEgo)
		(ego					init:)

		((= aDoor (AutoDoor new:))
			view: 			vClothingExt
			setLoop:			0
			posn:				136 135
			setPri:			9
			entranceTo:		116
			msgLook:			"Through the door you see an exclusive mens clothing
									store. (And, a beautiful female clerk!) A sign
									says they accept any form of U. S. currency."
			msgFunny:		"Everytime you try to knock, it opens itself!"
			init:
		)

		(self
			setRegions:		rgLA
			setScript:		rm16Script
		)
	)
)


(instance rm16Script of Script
	(method (doit)
		(super doit:)

		(if (& (ego onControl:) cCyan)
			(curRoom newRoom: 12)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/pole, (sign<street)')
				(Print "\"Rodeo Drive\"")
			)

			(if (Said '/window, sign')
				(Print "\"OPEN\"")
			)

			(if (Said '/flower')
				(Print "Yep. They're flowers all right!")
			)

			(if (Said '/ground, (mat<door)')
				(Print "Embedded in the marble sidewalk is a gold-plated
					medallion reading \"Rejectus Fiscus Universum.\"")
				(Print "(That's Latin for \"Give us all your money!\")" LOW)
			)

			(if (Said '[/shop, building, room]')
				(Print "You are in front of the world renowned Rodeo Drive
					Italian clothing purveyor, \"Molto Lira.\"")
				(if (ego has: iMillion)
					(Print "Perhaps they could cash a million-dollar bill.")
				)
				(if (not (ego has: iMoney))
					(Print "You've never been able to afford anything in there.")
				)
			)
		)
	)
)

