;**
;**	Logics for room 134 -- underwater at the Ship's swimming pool
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 14, 1988
;**


(script#				134)

(public
	rm134				0
)


(define	nearBikini	60 130 105 150)

;(define	vEgoPoolBottom				136)
	(define	diveDown			0)
	(define	diveUp			1)

;(define	vSwimmersUnderwater		314)
	(define	lFatAss			0)
	(define	lBikini			1)
	(define	lSkimmer			2)
	(define	lFart				3)
	(define	lPiss				4)


(local
	aSkimmer
	aBra
	aMan
	aFart
	aPiss
	egoPissing
	passInRoom
)


(enum												;**	fartScript state values
	fartInit
	startFart
	fartDone
)

(enum												;**	rmScript state values
	intro
	coast
	newDir
)


(instance rm134 of Room
	(properties
		picture:		134
		horizon:		5
		north:		34
	)

	(method (init)
		(Load VIEW vEgoPoolBottom)
		(Load VIEW vSwimmersUnderwater)

		(super init:)

		((= aSkimmer (Prop new:))
			view:				vSwimmersUnderwater
			setLoop:			2
			setPri:			2
			posn:				116 28
			setCycle:		Forward
			cycleSpeed:		3
			ignoreActors:
			isExtra:			TRUE
			init:
		)

		((= aMan (Actor new:))
			view:				vSwimmersUnderwater
			setLoop:			0
			setPri:			3
			setCycle:		Forward
			moveSpeed:		3
			cycleSpeed:		3
			setCycle:		Forward
			setStep:			1 1
			;**	everything BUT ltBlue is illegal!
			illegalBits:	( & $FFFF ( ~ cLtBlue))
			posn:				200 20
			init:
			setMotion:		Wander
		)
		((= aFart (Actor new:))
			view:				vSwimmersUnderwater
			setLoop:			3
			setPri:			5
			ignoreActors:
			illegalBits:	0
			init:
			hide:
			setScript:		fartScript
		)
		((= aPiss (Actor new:))				;**	ego pissing in pool
			view:				vSwimmersUnderwater
			setLoop:			lPiss
			setPri:			10					;**	1 less than ego, below.
			illegalBits:	0
			ignoreActors:
			posn:				999 999
			init:
		)

		(if ((inventory at: iBikiniTop) ownedBy: curRoomNum)
			((= aBra (View new:))
				view:				vSwimmersUnderwater
				setLoop:			1
				setPri:			2
				posn:				84 136
				ignoreActors:
				stopUpd:
				init:
			)
		)

		(ego
			view:				vEgoPoolBottom
			setLoop:			diveDown
			cel:				0
			illegalBits:	cLtWhite
			observeControl:cLtBlue
			posn:				88 6
			setPri:			11
			setMotion:		0
			init:
		)

		(User
			canControl:		TRUE
			canInput:		TRUE
		)

		(= currentStatus DIVING)
		(self
			setRegions:		rgShip
			setScript:		rm134Script
		)
	)
)


(instance rm134Script of Script
	(method (doit)
		(super doit:)

		(if (== (++ passInRoom) 200)
			(Print "Exactly how long do you think you can hold your breath?")
		)

		(if (and (== passInRoom 400)
					(!= currentStatus DEAD))
			(= currentStatus DEAD)
			(ego hide:)
			(Print "You feel your lungs explode as you think to yourself,
				\"Gee, I wish I could hold my breath longer, 
				or I owned a faster computer!\"" draw:)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/drain, bottom')
				(if ((inventory at: iBikiniTop) ownedBy: curRoomNum)
					(Print "There is a bikini top caught in the pool's drain!")
				else
					(Print "You can remember when there was a bikini top caught
						in that drain!")
				)
			)

			(if (Said '[/pool, tube, water, man, room]')
				(Print "The water is clear enough to see too much
					of that guy in the inner tube!")
				(if ((inventory at: iBikiniTop) ownedBy: curRoomNum)
					(Print "There is a bikini top caught in the pool's drain!")
				)
			)
		)

		(if (Said 'get/tube, man')
			(Print "No way!")
		)

		(if (Said '/drain')
			(Print "Why do you have such a fascination with plumbing?")
		)

		(if (Said 'swim, dive, (climb<out)')
			(Print "Just move wherever you want to go.")
		)

		(if (Said 'get/top, bikini, swimsuit, (top<bikini)')
			(cond
				((not ((inventory at: iBikiniTop) ownedBy: curRoomNum))
					(Print "Where?")
				)
				((not (ego inRect: nearBikini))
					(NotClose)
				)
				(else
					(Ok)
					(aBra dispose:)
					(ego get: iBikiniTop)
					(theGame changeScore: 7)
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(if (and	(not egoPissing)
							(== 5 (Random 1 22)))
					(= egoPissing TRUE)
					(aPiss
						cel:				0
						posn:				( + (ego x?)  7)
											( - (ego y?) 41)
						setCycle:		EndLoop
					)
				)
				(ego
					setCycle:	EndLoop self
				)
			)

			(coast
				(= cycles 4)
				(if (== egoPissing TRUE)
					(= egoPissing 255)
					(aPiss posn: 999 999)
					(Print "Oops.")
				)
			)

			(newDir
				(if (and ( > (ego heading?)  90)
							( < (ego heading?) 270))
					(ego setLoop: diveDown)
				)
				(if (or	( < (ego heading?)  90)
							( > (ego heading?) 270))
					(ego setLoop: diveUp)
				)
				(self changeState: intro)
			)
		)
	)
)


(instance fartScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(fartInit
				(= seconds (Random 22 44))
			)

			(startFart
				(aFart
					posn:				(+ (aMan x?) 8) (- (aMan y?) 4)
					cel:				0
					setCycle:		EndLoop self
					show:
				)
			)

			(fartDone
				(aFart				hide:)
				(self changeState: intro)
			)
		)
	)
)

