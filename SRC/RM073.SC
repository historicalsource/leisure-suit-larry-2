;**
;**	Logics for room 73 -- Jungle Quicksand
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	September 29, 1988
;**


(script#				73)

(public
	rm73				0
)


;(define	vMonkey						722)
;(define	vEgoQuicksand				175)
	(define	lSinkingSouth	4)
	(define	lSinkingNorth	5)
	(define	lGurgle			6)


(local
	work
	aMonkey
)


(enum												;**	rmScript state values
	intro
	runOff
	monkeyGone

	startSinking
	inAllTheWay
	UBdying
)


(instance rm73 of Room
	(properties
		picture:		73
		horizon:		50
		west:			72
		east:			74
		south:		74
	)

	(method (init)
		(Load VIEW vEgoQuicksand)

		(super init:)

		(cond
			((== prevRoomNum 74)		(ego posn: 318 188))
			(else
				(ego posn:	2 153)

				((= aMonkey (Actor new:))
					view:				vMonkey
					illegalBits:	0
					posn:				221 169
					init:
				)
			)
		)
		(NormalEgo)
		(ego
			view:				vEgoQuicksand
			setStep:			2 1
			init:
		)

		(self
			setRegions:		rgIsland
			setScript: 		rm73Script
		)
	)
)


(instance rm73Script of Script
	(method (doit)

		(cond
			((and 	(== pastQuicksand FALSE)
					(or	(== (ego edgeHit?) EAST)
							(== (ego edgeHit?) SOUTH)))
				(= pastQuicksand TRUE)
				(theGame changeScore: 5)
				(Print "You made it!")
			)

			((and (== currentStatus NORMAL)
					(& (ego onControl:) cBrown))
				(self changeState: startSinking)
			)
		)

		(super doit:)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said '/vine')
			(Print "The vines here are too high for you to reach.")
		)

		(if (Said 'crawl, jump')
			(Print "That won't help you here.")
		)

		(if (Said 'stand, step, walk, climb, use/rock')
			(Print "The rocks are not the path to your success.")
		)

		(if (and (cast contains: aMonkey)
					(Said '/monkey'))
			(Print "Funny. He has no trouble whatsoever walking across this
				area.")
		)

		(if (Said '/tree')
			(Print "The trees here can't help you.
				You're going to have to plod through this one on your own.")
		)

		(if (Said 'look>')
			(if (Said '/pattern')
				(Print "The patterned area looks different from the rest of
					the ground here.")
			)

			(if (Said '/quicksand')
				(Print "It looks prepared to suck.")
			)

			(if (Said '[/jungle, ground, room]')
				(Print "The ground here has a strange pattern to it.")
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(if (!= prevRoomNum 74)
					(aMonkey
						setStep:		1 1
						setMotion:	MoveTo 223 181
						setCycle:	EndLoop self
					)
				)
			)

			(runOff
				(aMonkey
					setStep:			4 3
					setLoop:			1
					setCycle:		Walk
					setMotion:		MoveTo 334 189 self
				)
			)

			(monkeyGone
				(aMonkey				dispose:)
				(Print "The jungle floor is soft and sticky here.")
				(Print "(You fondly recall your first car's seat covers.)" LOW)
			)


			;**	We take this path if he steps off the path.

			(startSinking
				(= currentStatus NEAR_DEATH)
				(HandsOff)
				(ego
					setLoop:			(if (and ( > (ego heading?)	89)
													( < (ego heading?) 271))
											lSinkingSouth
										else
											lSinkingNorth
										)
					cycleSpeed:		3
					cel:				0
					setCycle:		EndLoop self
				)
				(Print "At least this quicksand is nice and soft." draw:)
			)

			(inAllTheWay
				(ego
					setLoop:			lGurgle
					setCycle:		Forward
					cycleSpeed:		1
				)
				(= seconds 5)
			)

			(UBdying
				(Print "Once again, you're in over your head, Larry!")
				(Print "In the future, you must be more observant!")
				(= currentStatus DEAD)
			)
		)
	)
)

