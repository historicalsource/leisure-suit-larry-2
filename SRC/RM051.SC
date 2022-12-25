;**
;**	Logics for room 51 -- Airport Barber Shop exterior
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 23, 1988
;**


(script#				51)

(public
	rm51				0
)


;(define	vBarberPole					230)
;(define	vWestHall					506)
	(define	cChairsWest		0)
	(define	cSign				1)
	(define	cChairNorth		2)

;(define	vAirportHenchette			523)
;(define	vAirportHenchetteSitting 524)
	(define	lReading			0)
	(define	lGesture			1)
	(define	lTalking			2)


(local
	work
	goto95
	henchInvited
	aBarberPole
	aPlane
	aHench
	LarrySaidYes 
)


(enum												;**	henchScript state values
	henchInit
	henchGestures
	henchTalking
	henchHello
	henchTalksAgain
	comeON
	henchReadsAgain

	henchLeaving
	henchTurning2
	henchOffScreen
	henchAllGone
)


(instance rm51 of Room
	(properties
		picture:		51
		horizon:		1
		north:		151
	)

	(method (init)
		(Load VIEW vWestHall)
		(Load VIEW vBarberPole)
		(Load VIEW vAirplane)

		(super init:)

		((View new:)							;**	chairs1
			view: 			vWestHall
			cel: 				cChairsWest
			posn: 			59 157
			setPri: 			10
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	chairs2
			view: 			vWestHall
			cel: 				cChairsWest
			posn: 			19 183
			setPri: 			11
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	barberShopSign
			view: 			vWestHall
			cel: 				cSign
			posn: 			152 75
			setPri: 			4
			ignoreActors:
			addToPic:
		)

		((= aBarberPole (Prop new:))
			view:				vBarberPole
			setPri:			4
			setCycle:		Forward
			posn:				236 75
			isExtra:			TRUE
			init:
		)

		((= aPlane (Airplane new:))
			view:				vAirplane
			setPri:			1
			startX:			-20
			startY:			18
			endX:				111
			endY:				11
			init:
		)

		((View new:)							;**	North chair1
			view: 			vWestHall
			cel: 				cChairNorth
			posn: 			266 104
			setPri: 			5
			ignoreActors:
			addToPic:
		)
		(if (or	(!= 1 (Random 1 3))
					(!= currentEgoView vEgo))
			((View new:)						;**	North chair2
				view: 			vWestHall
				cel: 				cChairNorth
				posn: 			240 104
				setPri: 			5
				ignoreActors:
				addToPic:
			)
		else
			(self setRegions: rgHenchette)
			(= henchOnScreen TRUE)
			(= currentHenchView vAirportHenchette)
			(Load VIEW currentHenchView)
			(Load VIEW vAirportHenchetteSitting)
			((= aHench (Actor new:))
				view:				vAirportHenchetteSitting
				ignoreActors:
				illegalBits:	0
				setLoop:			lReading
				posn:				240 104
				setPri: 			5
				init:
				cycleSpeed:		2
				setCycle:		Forward
				setScript:		henchScript
			)
			(Notify rgHenchette 1)			;**	play music 1x
		)

		(if (or	(== prevRoomNum 151)
					(== prevRoomNum	0))
			(ego	posn: 	210 101)
		)
		(NormalEgo)
		(ego					init:)

		(self
			setRegions:		rgAirport
			setScript: 		rm51Script
		)
	)
)


(instance rm51Script of Script
	(method (doit)
		(super doit:)

		(if (& (ego onControl:) cBlue)
			(curRoom newRoom: 151)
		)

		(if (== EAST (ego edgeHit?))
			(if (== goto95 FALSE)
				(curRoom newRoom: 52)
			else
				(Print "The two of you head for the tarmac where she
					introduces herself as an interior decorator and
					professional hair styling consultant.
					You enter her \"corporate helicopter,\"
					which bears a large logo consisting of just
					a circle and a slash.
					\"Oh, that,\" she explains, \"that means 'no plain areas.'\"
					A short, romantic, low-altitude chopper ride across
					the South Pacific is followed by..." WIDE)
				(= currentStatus HEADING_FOR_DEATH_SCENE)
				(curRoom newRoom: 95)
			)
		)

		(if (and henchOnScreen
					henchInvited
					( > (ego x?) 300))
			(= henchInvited FALSE)
			(= goto95 TRUE)
			(curRoom east: 95)				;**	Too bad, Larry.
			(Print "\"Hey, baby,\" you cry, \"wait for me!!\"")
			(HandsOff)
			(ego
				setMotion:		MoveTo 333 (ego y?) self
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/chair, girl')
				(if (and	henchOnScreen
							( < (henchScript state?) henchLeaving))
					(Print "She's beautiful!")
				else
					(Print "These chairs have a beautiful, plastic covering.")
				)
			)

			(if (Said '/picture')
				(Print "Jim Whistler is about as modern as you can stand.")
			)

			(if (Said '[/shop, room]')
				(Print "This end of the airport contains some chairs,
					a window looking out on the runway,
					and the entrance to the airport barber shop.")
				(if henchOnScreen
					(Print "There is a gorgeous woman reading a paper.")
				)
			)
		)

		(if (and	henchOnScreen
					( < (henchScript state?) henchLeaving)
					(not LarrySaidYes)
					(or	(Said 'hump')
							(Said 'talk/girl')
							(Said 'yes')
							(Said 'sit[/down, chair]')
							(Said 'kiss')))
			(= LarrySaidYes TRUE)
			(Print "\"Hi, baby,\" you charm her, you charmer.
				\"How's about if I sit here beside you, and you
				let me see that photo spread?\"")
			(Print "\"Ok,\" she responds, \"but not here.
				I'd rather do it at my place! Follow me!!\"")
			(henchScript changeState: henchLeaving)
		)

		(if (Said 'sit[/down, chair]')
			(Print "Nah. You don't have time to sit here!")
		)
	)
)


(instance henchScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(henchInit
				(= seconds (Random 5 10))
			)

			(henchGestures
				(aHench
					cycleSpeed:	1
					setLoop:		lGesture
					cel:			0
					setCycle:	EndLoop self
				)
			)

			(henchTalking
				(aHench
					cycleSpeed:	0
					setLoop:		lTalking
					setCycle:	Forward
				)
				(= seconds 3)
			)

			(henchHello
				(aHench
					setLoop:		lGesture
					setCel:		0
				)
				(Print "\"Hello, sweetie,\"
					says the beautiful woman with the magazine,
					\"wanna look through my layouts?\"" draw:)
				(Print "(What an opportunity!
					You've never met a woman from the magazine industry before!)"
					LOW)
				(= seconds 5)
			)

			(henchTalksAgain
				(aHench
					setLoop:		lTalking
					setCycle:	Forward
				)
				(= seconds 3)
			)

			(comeON
				(Print "\"Come on, I just can't wait to see you sans serif!\"")
				(= seconds 3)
			)

			(henchReadsAgain
				(Print "\"Well, ok. Maybe next month!\" she gives up.")
				(aHench
					setLoop:		lReading
					cel:			0
					setCycle:	Forward
					cycleSpeed:	2
				)
				(= seconds (Random 10 40))
				(= state henchInit)
			)


			;**	If Larry says yes, do this:

			(henchLeaving
				(= seconds 0)
				(= cycles 0)
				((View new:)						;**	addtopic her now empty chair
					view: 			vWestHall
					cel: 				cChairNorth
					posn: 			240 104
					setPri: 			5
					ignoreActors:
					addToPic:
				)
				(aHench
					view:			currentHenchView
					ignoreActors:FALSE
					loop:			facingEast
					cycleSpeed:	0
					posn:			237 109
					setStep:		3 2
					setLoop:		RELEASE
					setAvoider: (Avoider new:)
					setCycle:	Walk
					setMotion:	MoveTo 305 118 self
				)
			)

			(henchTurning2
				(Print "\"Come on to my place and I'll show you my galleys!\"")
				(aHench
					setMotion:	MoveTo 333 118 self
				)
				(= henchInvited TRUE)
			)

			(henchOffScreen
				(= seconds 10)
			)

			(henchAllGone
				(aHench		dispose:)
				(= currentHenchView FALSE)
				(= henchOnScreen FALSE)
				(= henchInvited FALSE)
			)
		)
	)
)

