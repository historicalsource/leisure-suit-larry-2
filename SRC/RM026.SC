;**
;**	Logics for room 26 -- the Dock
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 15, 1988
;**


(script#				26)

(public
	rm26				0
)


(define	nearPurser 175 144 211 158)
(define	splatville 103 151 226 167)

;(define	vShipDock					236)
	(define	lWave				0)
	(define	lBarricade		1)
	(define	lTakeOff			2)
	(define	lFlyingWest		3)
	(define	lFlyingEast		4)
	(define	lPoop				5)

;(define	iPassport					007)
	(define	lPassportPhoto	1)

;(define	vPurser						237)


(local
	work
	aPurser
	aGate
	shipHere
	aWave
	aBird
	aPoop
	aPoop2
)


(enum												;**	birdScript state values
	birdInit
	takeOff
	startFlight
	bird1Done
	bird2Init
	bird2Shits
	poop2Plops
	keepBirdMoving
	bird2Done
	bird3Init
	bird3Shits
	poop3Plops
	bird3Done
)

(enum												;**	rmScript state values
	intro

	request
	cutCrap
	noPassport
	sorry
	youAgain
	hasPassport
	openGate
	gateOpen
	closeGate
	toRamp
	upYourRamp
	changeRooms
)


(instance rm26 of Room
	(properties
		horizon:		5
		north:		22
		east:			27
		west:			25
	)

	(method (init)
		(if (and	(ego has: iCruiseTicket)
					(ego has: iOnklunk))
			(= shipHere TRUE)
			(curRoom drawPic: 126)
		else
			(curRoom drawPic:	26)
		)

		(Load VIEW vShipDock)

		(super init:)

		(if shipHere
			(Load VIEW vPurser)
			(Load VIEW  iPassport)
			(Load VIEW  iCruiseTicket)
			((= aPurser (Prop new:))
				view:				vPurser
				ignoreActors:
				setCel:			0
				setPri:			ltBlue
				posn:				196 147
				init:
				stopUpd:
			)
			((= aBird (Actor new:))
				view:				vShipDock
				setLoop:			lTakeOff
				setCel:			0
				setPri:			15
				setStep:			8 6
				illegalBits:	0
				posn:				192 49
				ignoreActors:
				init:
				setScript:		birdScript
			)
			((= aPoop (Actor new:))
				view: 			vShipDock
				setLoop: 		lPoop
				setCel: 			0
				setPri: 			12
				setStep:			4 4
				illegalBits:	0
				ignoreActors:
				posn:				999 999
				init:
				hide:
			)
			((= aPoop2 (Actor new:))
				view: 			vShipDock
				setLoop: 		lPoop
				setCel: 			0
				setPri: 			12
				setStep:			4 4
				illegalBits:	0
				ignoreActors:
				posn:				999 999
				init:
				hide:
			)
			((= aWave (Prop new:))
				view:				vShipDock
				setLoop:			lWave
				setPri:			10
				posn:				296 162
				setCycle:		Forward
				cycleSpeed:		1
				isExtra:			TRUE
				init:
			)
		)

		((= aGate (Actor new:))
			view:				vShipDock
			setLoop:			lBarricade
			illegalBits:	0
			ignoreActors:
			setPri:			ltGreen
			posn:				188 147
			setStep:			1 1
			stopUpd:
			init:
		)

		(NormalEgo)
		(ego
			posn:				1 151
			init:
		)

		(self
			setRegions:		rgLA
			setScript:		rm26Script
		)
	)
)


(instance rm26Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if shipHere
			(cond
				((or	(Said 'show, hand, give/passport, ticket')
						(Said 'show, hand, give/man, clerk/passport, ticket')
						(Said 'show, hand, give/passport, ticket/man, clerk'))
					(cond
						((not (ego inRect: nearPurser))
							(NotClose)
						)
						((ego has: iPassport)
							(self changeState: youAgain)
						)
						(else
							(self changeState: request)
						)
					)
				)

				((Said '/ship, boat')
					(Print "It's so big that from this angle it is difficult
						to see it all.
						Perhaps you should \"board her!\"")
				)

				((Said '/gangplank')
					(Print "It reaches from here all the way to the ship.")
					(Print "(If it didn't, wouldn't it be a pain to board?)" LOW)
				)

				((Said '/chain')
					(Print "Big links, eh?")
				)

				((Said 'look/gate')
					(Print "The man controls the electric gate.")
				)

				((Said 'look/man')
					(Print "He's either a member of the crew of that large
						ocean-going vessel parked immediately behind him,
						or dressed for an unusual party!")
				)

				((Said 'talk/man')
					(Print "\"If you're going with us, you'd better get moving,\"
						says the man in the uniform.
						\"Are you going to show me your ticket,
						or did you walk over here just to kabitz?\"")
				)
			)
		)

		(if (or	(Said '//gate')
					(Said '/gate'))
			(Print "This is an automatic opening, electric, remote-controlled
				gate that is presently locked.")
			(Print "(And you can't open it.)" LOW)
		)

		(if (Said 'look>')
			(if (Said '/bird')
				(Print "It would be best not to look up at THAT bird!")
			)

			(if (Said '/ocean, beach, water, wave')
				(Print "The ocean waves, and the dock \"piers\" back at you.")
			)

			(if (Said '/gate')
				(Print "The electric gate is closed and locked.
					It is not under your control.")
			)

			(if (Said '[/room]')
				(Print "The aroma of dead fish and diesel fumes makes
					you realize you're at the harbor.")
				(if shipHere
					(Print "There is a man standing here.")
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			;**	(intro)

			(request
				(HandsOff)
				(Print "You snap to attention.
					\"Sir! Request permission to come aboard, Sir!!\"")
				(aPurser
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(cutCrap
				(Print "\"Cut the crap, son,\" he replies with disgust,
					\"I'm no Captain, I'm the purser.
					May I have your ticket and your passport, please?\"")
				(aPurser
					setCel:		0
					stopUpd:
				)
				(= seconds 3)
			)

			(noPassport
				(Print "\"Uh, I don't have a passport,\" you respond.
					\"They didn't say anything on the tv show about a passport!\"")
				(aPurser
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(sorry
				(Print "\"I'm sorry, young man, but I can not allow any 
					passengers aboard without a passport!\" he says, returning
					your ticket to you.
					\"Come back when you get yours, but you'd better hurry:
					we're due to sail any minute now!\"")
				(aPurser
					setCel:		0
					stopUpd:
				)
				(User
					canControl:		TRUE
					canInput:		TRUE
				)
			)


			;**	This route will be successful...

			(youAgain
				(HandsOff)
				(Print "\"Hi!\" you tell the purser.
					\"Here's my ticket and my passport!\"")
				(aPurser
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(hasPassport
				(Print "\"Ok, you just made it!
					Let's take a look at that passport first.\"")
				(Print "\"Whew, nice photo,\" he says sarcastically."
					icon: iPassport lPassportPhoto 0)
				(Print "\"Now let's see if your ticket is in order...\"")
				(Print "\"...ah, geesh, another freebie!
					When are those suits in marketing gonna realize
					these boats don't sail on wind power any more!
					Oops, I'm sorry; it's not your fault.\""
					icon: iCruiseTicket 0 0)
				(Print "Regaining his composure, he continues,
					\"your cabin will be on Deck F, cabin number 1.
					That'll be all the way below decks, and to the rear.
					This will give you an excellent opportunity to hear
					all the subtle inflections of our diesels.\"")
				(Print "\"You may enter now,\" he concludes.")
				(theGame changeScore: 9)
				(aPurser
					setCel:		0
					stopUpd:
				)
				(ego
					put:			iCruiseTicket LIMBO
					illegalBits:0
					setMotion:	MoveTo 181 155 self
				)
			)

			(openGate
				(aGate setMotion:	MoveTo 218 147 self)
			)

			(gateOpen
				(ego
					setPri:			white
					setMotion:		MoveTo 182 144 self
				)
			)

			(closeGate
				(aGate setMotion:	MoveTo 188 147)
				(ego	setMotion:	MoveTo 179 121 self)
			)

			(toRamp
				(ego	setMotion:	MoveTo 254 121 self)
			)

			(upYourRamp
				(ego	setMotion:	MoveTo 318 94 self)
			)

			(changeRooms
				(curRoom newRoom: 27)
			)
		)
	)
)


(instance birdScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(birdInit
				(= seconds (Random 2 3))
			)

			(takeOff
				(aBird
					setCycle:		EndLoop self
				)
			)

			(startFlight
				(aBird
					setLoop:			lFlyingWest
					setCycle:		Forward
					setMotion:		MoveTo -12 49 self
				)
			)

			(bird1Done
				(= seconds (Random 4 8))
			)

			(bird2Init
				(if (not (ego inRect: splatville))
					(= seconds 3)
					(-- state)
				else
					(aBird
						posn:				333 66
						setLoop:			lFlyingWest
						setCycle:		Forward
						setMotion:		MoveTo (+ (ego x?) 20) 66 self
					)
				)
			)

			(bird2Shits
				(aBird
					setMotion:		MoveTo -12 66
				)
				(aPoop
					posn:				(aBird x?) (aBird y?)
					setCel:			0
					show:
					setMotion:		MoveTo (- (aBird x?) 24) (+ (ego y?) 8) self
				)
			)

			(poop2Plops
				(aPoop
					setCycle:		EndLoop self
				)
			)

			(keepBirdMoving
				(aBird
					setMotion:		MoveTo -12 66 self
				)
			)

			(bird2Done
				(= seconds 3)
			)

			(bird3Init
				(if (not (ego inRect: splatville))
					(= seconds 3)
					(-- state)
				else
					(aBird
						posn:				-12 22
						setLoop:			lFlyingEast
						setCycle:		Forward
						setMotion:		MoveTo (- (ego x?) 20) 22 self
					)
				)
			)

			(bird3Shits
				(aBird
					setMotion:		MoveTo 333 22 self
				)
				(aPoop2
					posn:				(aBird x?) (aBird y?)
					show:
					setMotion:		MoveTo ( + 14 (aBird x?)) (+ (ego y?) 3) self
				)
			)

			(poop3Plops
				(aPoop2
					setCycle:		EndLoop
				)
			)

			(bird3Done
				(aBird				dispose:)
			)
		)
	)
)

