;**
;**	Logics for room 15 -- Ethnomusicology Shoppe Exterior
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 20, 1988
;**


(script#				15)

(public
	rm15				0
)


;(define	vMusicExt					220)
;(define	vMusicHench					223)	;**	KBG guy, black trench coat


(local
	storeOpen
	thisIsIt
	lclWindow
	aCar
	aSignal
	aHench
	aDoorWest
	aDoorEast
)


(enum												;**	trafficSignalScript state values
	TSinit
	TSyellow
	TSred
	TSgreen
)

(enum												;**	rmScript state values
	intro
	storeIsOpen

	kgbInit
	moveLarry
	lookAtThatCar
	continueWalking
	dontSee
	kgbFollow
	kgbGone
	LarryYouLookLikeANewMan
	badLarryAtDoor
)


(instance rm15 of Room
	(properties
		picture:		15
		horizon:		77
		north:		11
		south:		19
		east:			16
	)

	(method (init)
		(Load VIEW vMusicExt)
		(Load VIEW vMusicHench)

		(super init:)

		(NormalEgo)

		(self
			setRegions:		rgLA
			setScript:		rm15Script
		)

		((View new:)							;**	Ye Olde Shoppe sign
			view:				vMusicExt
			loop: 			2
			cel: 				0
			posn: 			116 93
			setPri: 			8
			addToPic:
		)

		;**	This could be right, but who knows?!

		(if (and	(== LAhenchAfterEgo FALSE)
					(ego has: iOnklunk))
			(= thisIsIt TRUE)
		)

		(if (and	LAhaircut
					(not (ego has: iOnklunk)))
			(= storeOpen TRUE)
		)

		(if (or thisIsIt storeOpen)
			((= aDoorWest (AutoDoor new:)) ;**	Real west door
				view:				vMusicExt
				setLoop:			0
				posn:				43 115
				entranceTo:		(if (!= prevRoomNum 115) 115)
				msgLook:			"A sign in the window says,
										\"Now OPEN for business. Please come in!\""
				msgCloser:		"The entrance door is to the left.
										Just walk near it."
				init:
			)
		else
			((View new:)						;**	Fake west door.
				view:				vMusicExt
				ignoreActors:
				posn: 			43 115
				addToPic:
			)
			(ego
				observeControl:cYellow
			)
		)

		(if (== prevRoomNum 115)
			((= aDoorEast (Prop new:))		;**	Prop east door
				view:				vMusicExt
				setLoop:			1
				setCel:			LastCel
				posn:				262 81
				cycleSpeed:		2
				init:
				setCycle:		BegLoop
			)
		else
			((View new:)						;**	Fake east door
				view:				vMusicExt
				ignoreActors:
				setLoop:			1
				posn:				262 81
				addToPic:
			)
		)

		(if thisIsIt
			(= LAhenchAfterEgo TRUE)
			(= currentStatus WALKING)
			(rm15Script changeState: kgbInit)	;**	choetime!
			(Load VIEW vMusicHench)

			(curRoom
				east:				0				;**	So Larry will walk off-screen.
			)
			((View new:)						;**	traffic signal now just addtopic
				view:				vMusicExt
				loop: 			3
				cel: 				0
				posn: 			27 64
				setPri: 			15
				addToPic:
			)
			((= aCar (Extra new:))
				view:				vMusicExt
				setLoop:			4
				setPri:			8
				posn:				20 172
				cycleSpeed:		0
				init:
			)
			((= aHench (Actor new:))
				view:				vMusicHench
				loop:				facingEast
				posn:				10 174
				setCycle:		Walk
				stopUpd:
				init:
			)
			(ego
				posn:				253 89
				init:
			)
			(HandsOff)
		else
			((= aSignal (Prop new:))		;**	traffic signal cycles
				view:				vMusicExt
				loop: 			3
				setCel: 			0
				posn: 			27 64
				setPri: 			15
				stopUpd:
				init:
				setScript:		trafficSignalScript
			)

			(cond
				((== prevRoomNum	 0)	(ego posn: 302	81))
				((== prevRoomNum	11)	(ego posn: 302	81))
				((== prevRoomNum	19)	(ego posn:	8 188))
				((== prevRoomNum 115)	(ego posn: 253	89))
			)
			(ego					init:)
		)
	)
)


(instance rm15Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (event claimed?)						(return))

		(if (and	lclWindow
					(== state lookAtThatCar))
			(event claimed:TRUE)
			(= lclWindow FALSE)
			(cls)
			(self cue:)
		)

		(if (!= (event type?) saidEvent)		(return))

		(if (Said '/door, sign')
			(cond
				(storeOpen
					(Print "A sign in the window says \"GRAND OPENING -- TODAY!\"")
				)
				((ego has: iOnklunk)
					(Print "It appears that both doors are locked, since there
						is a sign that says \"WATCH FOR OUR BIG 
						GOING OUT OF BUSINESS SALE!\"")
				)
				(else
					(Print "It appears that both doors are locked, since there
						is a sign that says \"WATCH FOR OUR GRAND OPENING --
							REAL SOON NOW!\"")
				)
			)
			(if ( < (ego x?) 155)
				(Print "The door nearest you is labeled \"ENTER.\"")
			else
				(Print "The door nearest you is labeled \"EXIT.\"")
			)
		)

		(if (Said '/pole, light, (light<stop), (sign<stop)')
			(Print "The stoplight seems unnecessary here,
				since there aren't any cars here!")
		)

		(if (Said 'look>')
			(if (Said '[/store, (store<music), building, sign, room]')
				(Print "You are outside the famous LA landmark
					\"Ye Olde Ethno- Musicology Shoppe.\"
					You are familiar with them from their many advertisements
					on late-night cable television.")
				(Print "Does this town have specialized stores or what!?")
				(if storeOpen
					(Print "A sign in the window says \"GRAND OPENING -- TODAY!\"")
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(= cycles 30)
			)

			(storeIsOpen
				(if (and storeOpen
							(!= prevRoomNum 115))
					(Print "Say, look! This store is finally open for business.")
				)
				(if (== prevRoomNum 115)
					(aDoorWest
						entranceTo:		115
					)
				)
			)


			;**	This is the route after getting the onklunk...

			(kgbInit
				(= cycles	0)
				(= seconds 3)
			)

			(moveLarry
				(ego
					setMotion:	MoveTo 262 140 self
				)
			)

			(lookAtThatCar
				(= lclWindow (Print "\"Say, what an interesting car,\"
					you think to yourself.
					\"How does he make it do that?\"" HIGH dispose:))
			)

			(continueWalking
				(ego
					setMotion:	MoveTo 262 170 self
				)
				(aDoorEast			stopUpd:)
			)

			(dontSee
				(aDoorWest locked: FALSE)
				(Print "You are so distracted by the low-rider that you
					fail to notice the unusual little man lurking beside the car.")
				(Print "He, however, DOESN'T fail to notice you!")
				(ego
					setMotion:	MoveTo 350 170 self
				)
			)

			(kgbFollow
				(Print "The little guy in the trench coat
					gives you a short head-start then follows you.
					Larry, be careful!
					This guy's up to no good!")
				(aHench
					setMotion:	MoveTo 333 (aHench y?) self
				)
			)

			(kgbGone
				(aHench dispose:)
				(= seconds 4)
			)

			(LarryYouLookLikeANewMan
				(= currentStatus EGO_IS_HENCH)
				(aDoorWest
					entranceTo:		115
				)
				(ego
					posn:				-30 126
					setMotion:		MoveTo 49 119 self
					illegalBits:	0
				)
			)

			(badLarryAtDoor
				(Print "Wait a minute! What's this?!")
				(Print "How can you be at the door to the music store,
					when you just walked off screen to the right?")
				(Print "That must be someone who just looks like you!
					Something strange is going on here.
					Let's follow him into the music shop
					to see what we can learn!")
				(ego
					setMotion:		MoveTo 78 105 self
				)
			)
		)
	)
)


(instance trafficSignalScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(TSinit
				(= seconds 6)
			)

			(TSyellow
				(aSignal	setCel:		1)
				(= seconds 4)
			)

			(TSred
				(aSignal setCel:		2)
				(= seconds 13)
			)

			(TSgreen
				(aSignal setCel:		0)
				(= seconds 13)
				(= state TSinit)
			)
		)
	)
)

