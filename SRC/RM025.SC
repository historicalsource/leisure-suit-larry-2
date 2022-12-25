;**
;**	Logics for room 25 -- LA barber Hairy Reams
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 23, 1988
;**


(script#				25)

(public
	rm25				0
)


;(define	vBarberPole					230)
;(define	vLAbarberExt				231)


(local
	aDoor
	aPole
	aWave
)


(enum												;**	rmScript state values
	waveInit
	loopDone
	delayDone
)


(instance rm25 of Room
	(properties
		picture:		25
		horizon:		1
		east:			26
		west:			24
	)

	(method (init)
		(Load VIEW vLAbarberExt)
		(Load VIEW vBarberPole)

		(super init:)

		((View new:)							;**	hairy reams
			view: 			vLAbarberExt
			loop: 			1
			cel: 				1
			posn: 			266 84
			setPri: 			12
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	wrought iron
			view: 			vLAbarberExt
			loop: 			1
			cel: 				0
			posn: 			248 107
			setPri: 			12
			ignoreActors:
			addToPic:
		)

		((= aWave (Prop new:))
			view:				vLAbarberExt
			setLoop:			2
			setCel:			0
			setPri:			0
			posn: 			319 96
			setCycle:		Forward
			cycleSpeed:		3
			isExtra:			TRUE
			init:
		)
		((= aPole (Prop new:))
			view: 			vBarberPole
			setPri:			12
			posn:				176 139
			setCycle:		Forward
			cycleSpeed:		1
			isExtra:			TRUE
			init:
		)

		(NormalEgo)

		((= aDoor (AutoDoor new:))
			view: 			vLAbarberExt
			setLoop:			0
			posn:				185 85
			setPri:			4
			entranceTo:		125
			msgLook:			"A small, discreetly lettered sign on the door says:
									\"No hair loss too great.\""
			msgFunny:		"\"Come in. It's open!\""
			msgCloser:		"Just walk closer."
			init:
		)

		(cond
			((== prevRoomNum	24)	(ego posn:	 2 169))
			((== prevRoomNum	26)	(ego posn: 318 135))
			((== prevRoomNum 125)	(ego posn: 186 125))
			(else							(ego posn: 245 121))	;**	from rm21, 22, 0
		)
		(ego					init:)

		(self
			setRegions:		rgLA
			setScript:		rm25Script
		)
	)
)


(instance rm25Script of Script
	(method (doit)
		(super doit:)

		(if (& (ego onControl:) cCyan)
			(curRoom newRoom: 21)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/ocean, beach, water, wave')
				(Print "The ocean waves, and the dock \"piers\" back at you.")
			)

			(if (Said '[/building, (shop<barber), shop, room]')
				(Print "Gee, isn't Hairy Reams that famous Hollywood
					barber?")
				(Print "Maybe he could help you with your hair problem.")
				(Print "What do you mean, \"what problem?!\"")
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(waveInit
				(aWave
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(loopDone
				(= cycles (Random 6 10))
			)

			(delayDone
				(self changeState: waveInit)
			)
		)
	)
)

