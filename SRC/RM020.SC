;**
;**	Logics for room 20 -- LA Bonaventure Hotel
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 16, 1988
;**


(script#				20)

(public
	rm20				0
)


;(define	vBonaventure				251)
 	(define	lJogger				0)
	(define	lAirplane			1)


(local
	aPlane
	aJogger
)


(enum												;**	rmScript state values
	intro
	planeFlying
	planeGone
	joggerOn
	joggerJogging
	joggerDone
)


(instance rm20 of Room
	(properties
		picture:		20
		horizon:		1
		south:		24
		east:			21
		west:			19
	)

	(method (init)
		(Load VIEW vBonaventure)

		(super init:)

		((= aJogger (Actor new:))			;**	little guy walking on path
			view: 			vBonaventure
			setLoop: 		lJogger
			setPri: 			4
			moveSpeed:		3
			setStep:			1 1
			illegalBits:	0
			ignoreActors:
			init:
			hide:
			setCycle:		Walk
		)
		((= aPlane (Actor new:))
			view: 			vBonaventure
			setLoop: 		lAirplane
			posn: 			274 8
			setPri: 			0
			illegalBits:	0
			ignoreActors:
			init:
			hide:
		)

		(if (or	(== prevRoomNum	 0)	
					(== prevRoomNum	16))
			(ego posn: 309 133)
		)
		(NormalEgo)
		(ego					init:)

		(self
			setRegions:		rgLA
			setScript:		rm20Script
		)
	)
)


(instance rm20Script of Script
	(method (doit)
		(super doit:)

		(if (& (ego onControl: origin) cBlue)
			(curRoom newRoom: 16)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/airplane')
				(Print "Stand here long enough, and you get the feeling he's
					practicing \"touch-and-goes.\"")
			)

			(if (Said '/jogger, man, woman')
				(if (or	(== state joggerOn)
							(== state joggerJogging))				
					(if (not scoredJogger)
						(= scoredJogger TRUE)
						(theGame changeScore: 1)
					)
					(Print "You make a vow to get more exercise.")
					(Print "(Right after you back up your hard disk!)" LOW)
				else
					(Print "Where?")
				)
			)

			(if (Said '/hotel, building')
				(Print "Inside that large hotel
					hundreds of people are being kept awake by that one airplane.")
			)

			(if (Said '[/city, room]')
				(Print "This place looks like it belongs in \"Space Quest IV,
					(The Coarsegold Encounter).\"")
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(= seconds (Random 2 14))
			)

			(planeFlying
				(aPlane
					posn:			287 8
					show:
					setMotion:	MoveTo -30 8 self
				)
			)

			(planeGone
				(aPlane hide:)
				(= seconds (Random 2 9))
			)

			(joggerOn
				(aJogger
					posn: 			61 121
					show:
					setMotion:		MoveTo 112 116 self
				)
			)

			(joggerJogging
				(aJogger setMotion:		MoveTo 150 125 self)
			)

			(joggerDone
				(aJogger hide:)
				(self changeState: intro)
			)
		)
	)
)

