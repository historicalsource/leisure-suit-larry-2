;**
;**	Logics for room 63 -- airplane rear
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 15, 1988
;**


(script#				63)

(public
	rm63				0
)


(define	nearMensRoom			206   0 236	 92)
(define	nearLadiesRoom			242  90 254 102)
(define	nearEmergencyExit		  0 127 320 190)

;(define	vEgoOutPlane				161)
;(define	vAirplaneRear				605)
;(define	vPassenger					608)


(local
	aSmoker1
	aSmoker2
	aSmoker3
	aJohnLight
	aJohnDoor
	aJohnUser1
	aJohnUser2
	aEmergencyExit
)


(enum												;**	johnScript state values
	johnInit
	doDoor
	johnDoorOpen
	walk1
	walk2
	johnDoorClosed
	doItAgain
)

(enum												;**	smokerScript state values
	smokerInit
	smoker1puff
	wait1
	smoker2puff
	wait2
	smoker3puff
	wait3
)

(enum												;**	ED means "Emergency Door"
	EDlocked
	EDunlocked
	EDhandleTurned
	EDopen
)

(enum 											;**	rm63Script script states
	intro
	EDshut
	EDopened
	EDgone
	egoOut
	egoGone
)


(instance rm63 of Room
	(properties
		picture:		63
		west:			62
	)

	(method (init)
		(Load VIEW currentEgoView)
		(Load VIEW vAirplaneRear)
		(Load VIEW vEgoOutPlane)
		(Load VIEW vPassenger)

		(super init:)

		((View new:)							;**	Unused john door
			view: 			vAirplaneRear
			loop: 			4
			cel: 				0
			posn:				253 93
			setPri: 			4
			ignoreActors:
			addToPic:
		)

		((= aSmoker1 (Prop new:))
			view:				vAirplaneRear
			loop:				2
			cel:				0
			posn:				113 84
			setPri:			5
			cycleSpeed:		3
			setCycle:		Forward
			stopUpd:
			init:
			setScript:		smokerScript
		)
		((= aSmoker2 (Prop new:))
			view:				vAirplaneRear
			loop:				2
			cel:				0
			posn:				62 114
			setPri:			8
			ignoreActors:
			cycleSpeed:		2
			setCycle:		Forward
			stopUpd:
			init:
		)
		((= aSmoker3 (Prop new:))
			view:				vAirplaneRear
			loop:				2
			cel:				0
			posn:				155 106
			setPri:			7
			ignoreActors:
			cycleSpeed:		3
			setCycle:		Forward
			stopUpd:
			init:
		)

		((= aJohnLight (Prop new:))		;**	active john light
			view:				vAirplaneRear
			loop:				3
			cel:				1
			posn:				203 60
			setPri:			3
			stopUpd:
			init:
		)
		((= aJohnDoor (Prop new:))			;**	active john door
			view:				vAirplaneRear
			loop:				1
			cel:				0
			posn: 			232 83
			setPri:			5
			stopUpd:
			ignoreActors:
			init:
			setScript:		johnScript
		)

		((= aJohnUser1 (Actor new:))		;**	The two guys cycling the can.
			view:				vPassenger
			posn:				222 80
			setPri:			0
			ignoreActors:
			illegalBits:	0
			setCycle:		Walk
			init:
			hide:
		)
		((= aJohnUser2 (Actor new:))
			view:				vPassenger
			posn:				0 0
			setPri:			0
			ignoreActors:
			illegalBits:	0
			setCycle:		Walk
			init:
			hide:
		)
		((= aEmergencyExit (Actor new:))	;**	airplane's emergency exit
			view:				vAirplaneRear
			illegalBits:	0
			ignoreActors:
			setLoop:			0
			cel:				0
			posn:				274 146
			setPri:			15
			setStep:			5 5
			stopUpd:
			init:
		)

		(NormalEgo facingEast)
		(ego
			posn:				37 105
			init:
			observeControl:cYellow
		)

		(self
			setRegions:		rgAirplane
			setScript:		rm63Script
		)
	)
)


(instance rm63Script of Script
	(method (doit)
		(super doit:)
		(if (and (== currentStatus NORMAL)
					(& (ego onControl:) cBlue))
			(curRoom newRoom: 62)
		)
	)

	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look, enter, use, open/(room<rest), (room<bath), restroom')
			(cond
				((ego inRect: nearMensRoom)
					(Print "The red light indicates it's currently
						\"Occupado.\"")
				)
				((ego inRect: nearLadiesRoom)
					(Print "You are not desperate enough to use
						the Women's restroom.
						Besides, some gal has been in there for hours!")
				)
				(else
					(Print "You're not near a restroom.")
				)
			)
		)

		(if (Said 'knock/door')
			(Print "\"Knock, knock.\"")
			(Print "\"Who's there?\"")
			(Print "\"Larry; Larry Laffer.\"")
			(Print "(You never were good at jokes!)" LOW)
		)

		(if (Said 'talk')
			(Print (Format @string
				"\"Hello,\" you say to the men in the smoking area,
				%s" laffer))
			(Print "\"Hack.\"")
			(Print "\"Cough.\"")
			(Print "\"Wheeze.\"")
		)

		(if (Said '(blow<up), blow/handle')
			(Print "Puff, puff.")
		)

		(if (Said 'look>')
			(if (Said '/door')
				(cond
					((== airplaneDoorStatus EDhandleTurned)
						(Print "It's still securely closed,
							although the safety handle is in the open position.")
					)
					((== airplaneDoorStatus EDunlocked)
						(Print "Even though the padlock is now off,
							the large red handle is still in the closed position.")
					)
					(else
						(Print "There are three doors here.
							Two are labeled \"Restroom.\"
							One is labeled \"DANGER -- EMERGENCY EXIT!\"")
					)
				)
			)

			(if (Said '/floor, ceiling')
				(Print "It's difficult to see with all the smoke back here,
					but you think you detect three doors at the rear of the
					plane!")
			)

			(if (Said '/light')
				(Print "When the light near the restroom is lit, 
					the restroom is currently occupied.")
			)

			(if (Said '[/people, man, woman, plane, room]')
				(Print "The smell of stale cigarette smoke makes you
					fondly recall your days as a lounge lizard!")
				(Print "Available at your local software dealer
					as \"Leisure Suit Larry in the Land of the Lounge Lizards!\"")
				(Print "Pick up a couple of copies today!")
			)
		)

		(if (Said 'smell')
			(Print "The smell of stale cigarette smoke makes you
				fondly recall your days as a lounge lizard!")
			(Print "Remember, those wonderful days of yesteryear are available
				now, at your local software dealer
				as \"Leisure Suit Larry in the Land of the Lounge Lizards!\"")
			(Print "Be sure to pick up several copies today!")
		)

		(if (Said '(put<on), wear, fasten, strap, tie, use/parachute')
			(cond
				(wearingParachute
					(ItIs)
				)
				((not (ego has: iParachute))
					(DontHave)
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				(else
					(Ok)
					(Print "The other passengers stare at you with suspicious
						expressions, so you slide the parachute under your coat.")
					(theGame changeScore: 4)
					(= wearingParachute TRUE)
				)
			)
		)

		(if (and wearingParachute
					(Said 'change, (take<off), remove/parachute'))
			(Ok)
			(= wearingParachute FALSE)
		)

		(if (Said 'pour, use, put/tonic/lock, door, handle')
			(cond
				((not (ego has: iHairRejuvenator))
					(DontHave)
				)
				((not (ego inRect: nearEmergencyExit))
					(NotClose)
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				(else
					(Print "Ok.
						You carefully pour your bottle of highly flammable
						hair rejuvenator on the emergency exit's padlock.
						It makes the look look really new!")
					(ego put: iHairRejuvenator LIMBO)
					(theGame changeScore: -5)
				)
			)
		)

		(if (Said '/knife/lock, door, handle')
			(cond
				((not (ego has: iKnife))
					(DontHave)
				)
				((not (ego inRect: nearEmergencyExit))
					(NotClose)
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				(else
					(Print "You try your best to jimmy the lock with the dull
						cheese knife, but for naught.")
				)
			)
		)

		(if (or	(Said 'use/pin')
					(Said 'unlock, open/lock, handle/pin')
					(Said 'pick/lock'))
			(cond
				((not (ego inRect: nearEmergencyExit))
					(NotClose)
				)
				((not (ego has: iBobbyPin))
					(Print "You don't have the proper equipment to do that.")
					(Print "(You've heard that line before!)" LOW)
				)
				(else
					(theGame changeScore: 5)
					(Print "Cleverly,
						you insert the bobby pin into the safety lock.
						(Wouldn't you guess this is against some Federal
						regulation?)")
					(Print "The pin works! The door is unlocked.")
					(= airplaneDoorStatus EDunlocked)
					(ego put: iBobbyPin LIMBO)
				)
			)
		)

		(if (Said 'move, use, shove, turn/lever, handle, knob')
			(cond
				((not (ego inRect: nearEmergencyExit))
					(NotClose)
				)
				((== airplaneDoorStatus EDunlocked)
					(Print "With great difficulty, you push the
						large red handle until you feel something tear.
						Crossing your legs,
						you shove the handle a quarter-turn to the left,
						until it is near the word \"DANGER.\"")
					(= airplaneDoorStatus EDhandleTurned)
				)
				(( < airplaneDoorStatus EDunlocked)
					(Print "A heavy, locked padlock on the handle
						prevents you from doing that.")
				)
				(else
					(Print "It IS!")
				)
			)
		)

		(if (Said 'push, open/door')
			(cond
				((ego inRect: nearMensRoom)
					(Print "The red light indicates it's currently \"Occupado.\"")
				)
				((ego inRect: nearLadiesRoom)
					(Print "Some gal has been in that one for hours!")
				)
				((not (ego inRect: nearEmergencyExit))
					(NotClose)
				)
				(else
					(cond
						((== airplaneDoorStatus EDhandleTurned)
							(= airplaneDoorStatus EDopen)
							(self changeState: EDshut)
						)
						(( < airplaneDoorStatus EDhandleTurned)
							(Print "There's a large red handle preventing that.")
						)
						(else
							(Print "It IS!")
						)
					)
				)
			)
		)

		(if (Said 'bum, (ask<for)/cigarette')
			(Print "\"Hey, buddy,\" you shout,
				\"how's 'bout givin' me one of them coffin nails?\"")
			(Print "(Why, Larry! You don't even smoke!)" LOW)
		)

	)


	(method (changeState newState)
		(switch (= state newState)

			;**	(intro)

			(EDshut
				(= cycles	0)
				(= seconds	0)
				(HandsOff)
				(ego
					posn: 			250 145
				)
				(Print "You give the door a firm shove..." draw:)
				(theGame changeScore: 6)
				(aEmergencyExit
					cycleSpeed:		3
					setCycle:		EndLoop self
				)
			)

			(EDopened
				(aEmergencyExit
					setMotion:		MoveTo 324 (aEmergencyExit y?) self
				)
				(Print "...and the blast of the airstream tears it from its
					hinges!" draw:)
			)

			(EDgone
				(Print "You feel yourself being sucked...")
				(Print "...out, unfortunately!")
				(ego
					ignoreActors:
					illegalBits:	0
					view: 			vEgoOutPlane
					posn: 			261 135
					cycleSpeed:		1
					setLoop:			0
					setCel:			0
					setCycle:		EndLoop self
					setPri:			15
				)
			)

			(egoOut
				(ego
					setCel:
					setStep:			5 5
					setMotion:		MoveTo 333 135 self
				)
			)

			(egoGone
				(curRoom newRoom: 64)
			)
		)
	)
)


(instance smokerScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(smokerInit
				(= seconds (Random 1 5))
			)

			(smoker1puff
				(aSmoker1
					startUpd:
					setCel:		0
					setCycle:	EndLoop self
				)
			)

			(wait1
				(aSmoker1
					stopUpd:
				)
				(= seconds (Random 1 5))
			)

			(smoker2puff
				(aSmoker2
					startUpd:
					setCel:		0
					setCycle:	EndLoop self
				)
			)

			(wait2
				(aSmoker2
					stopUpd:
				)
				(= seconds (Random 1 5))
			)

			(smoker3puff
				(aSmoker3
					startUpd:
					setCel:		0
					setCycle:	EndLoop self
				)
			)

			(wait3
				(aSmoker3
					stopUpd:
				)
				(= seconds (Random 1 5))
				(= state smokerInit)
			)
		)
	)
)


(instance johnScript of Script
	(method (doit)
		(super doit:)

		(if (and
				(& (ego onControl:) cLtRed)
				(== state johnInit)
			)
			(johnScript changeState: doDoor)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(doDoor
				(= cycles	0)
				(= seconds	0)
				(aJohnDoor
					startUpd:
					setCycle:	EndLoop self
				)
				(aJohnLight
					setCel:		0
					forceUpd:
				)
			)

			(johnDoorOpen
				(aJohnDoor
					stopUpd:
				)
				(aJohnUser1						; in front of johnDoor
					loop: 		facingWest
					posn: 		219 86
					setPri: 		5
					startUpd:
					show:
					setCycle:	Walk
					setMotion:	MoveTo 192 98 self
				)
				(aJohnUser2						; user2 starts here
					loop: 		facingEast
					posn: 		174 104
					setPri: 		6
					startUpd:
					show:
					setCycle:	Walk
					setMotion:	MoveTo 210 104
				)
			)

			(walk1
				(aJohnUser1 setMotion:	MoveTo 174 98)
				(aJohnUser2	setMotion:	MoveTo 222 86 self)
			)

			(walk2
				(aJohnUser1 hide:)
				(aJohnUser2 hide:)
				(aJohnDoor
					startUpd:
					setCycle:	BegLoop self
				)
			)

			(johnDoorClosed
				(aJohnDoor
					stopUpd:
				)
				(aJohnLight
					setCel:		1
					forceUpd:
				)
				(= seconds (Random 11 33))
			)

			(doItAgain
				(= state johnInit)
			)
		)
	)
)

