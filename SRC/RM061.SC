;**
;**	Logics for room 61 -- airplane first class
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 2, 1988
;**


(script#				61)

(public
	rm61				0
)


(define	nearGalley		0 0 106 92)

;(define	vStewardess					600)
	(define	lTalk					4)

;(define	vAirplaneSeatsFront		661)
;(define	vAirplaneFront				603)
	(define	lCockpitDoor		0)
	(define	lMainDoor			1)


(local
	aStewardess
	aCockDoor
	aKnitting
	aNewspaper
	aHand
)


(enum
	intro

	fromJetway
	startMoving
	sheTalks1
	atStewardess
	sheTalks2
	gag1
	sheTalks3
	gag2
	ohYeah
	pauseInAisle
	itsNotSmall

	openCockDoor
	cockDoorDone
	closeCockDoor
	say1
	say2
	shakeTheScreen
	UBdead
)


(instance rm61 of Room
	(properties
		picture:		61
	)

	(method (init)
		(Load VIEW vEgo)
		(Load VIEW vAirplaneSeatsFront)
		(Load VIEW vAirplaneFront)

		(super init:)

		(NormalEgo)

((View new:) view:661 loop:0 cel:2 posn:254	76 setPri: 4 ignoreActors: addToPic:)
((View new:) view:661 loop:0 cel:3 posn:205	76 setPri: 4 ignoreActors: addToPic:)
((View new:) view:661 loop:0 cel:4 posn:151	77 setPri: 4 ignoreActors: addToPic:)
((View new:) view:661 loop:0 cel:1 posn:206	91 setPri: 5 ignoreActors: addToPic:)
((View new:) view:661 loop:0 cel:4 posn:261	92 setPri: 5 ignoreActors: addToPic:)
((View new:) view:661 loop:0 cel:0 posn:150	92 setPri: 5 ignoreActors: addToPic:)
((View new:) view:661 loop:1 cel:4 posn:157 120 setPri: 8 ignoreActors: addToPic:)
((View new:) view:661 loop:1 cel:5 posn:286 120 setPri: 8 ignoreActors: addToPic:)
((View new:) view:661 loop:1 cel:3 posn:223 121 setPri: 8 ignoreActors: addToPic:)
((View new:) view:661 loop:1 cel:0 posn:152 138 setPri:10 ignoreActors: addToPic:)
((View new:) view:661 loop:1 cel:2 posn:288 138 setPri:10 ignoreActors: addToPic:)
((View new:) view:661 loop:1 cel:1 posn:226 139 setPri:10 ignoreActors: addToPic:)
((View new:) view:661 loop:2 cel:1 posn:189	78 setPri: 4 ignoreActors: addToPic:)
((View new:) view:661 loop:2 cel:2 posn:239	94 setPri: 5 ignoreActors: addToPic:)
((View new:) view:661 loop:2 cel:0 posn:188 111 setPri: 7 ignoreActors: addToPic:)
((View new:) view:661 loop:2 cel:1 posn:255 111 setPri: 7 ignoreActors: addToPic:)
((View new:) view:661 loop:2 cel:2 posn:279 130 setPri: 9 ignoreActors: addToPic:)

		((= aNewspaper (Extra new:))
			view: 			vAirplaneSeatsFront
			loop: 			3
			posn: 			146 75
			setPri: 			6
			pauseCel: 		LastCel
			minPause:		3
			maxPause:		8
			init:
		)
		((= aHand (Extra new:))
			view: 			vAirplaneSeatsFront
			loop: 			4
			posn: 			279 109
			setPri: 			9
			pauseCel: 		LastCel
			minPause:		3
			maxPause:		8
			minCycles:		5
			maxCycles:		11
			init:
		)
		((= aKnitting (Extra new:))
			view: 			vAirplaneSeatsFront
			loop: 			5
			posn: 			149 60
			setPri: 			5
			pauseCel: 		LastCel
			minPause:		3
			maxPause:		11
			minCycles:		15
			maxCycles:		31
			init:
		)

		(self
			setRegions:		rgAirplane
			setScript:		rm61Script
		)

		((= aCockDoor (Prop new:))
			view:				vAirplaneFront
			ignoreActors:
			setLoop:			lCockpitDoor
			posn:				41 104
			setPri:			5
			setCel:			0
			stopUpd:
			init:
		)

		(if (== prevRoomNum 62)				;**	From middle of plane
			(ego
				observeControl:cYellow
				loop: 			facingWest
				posn: 			300 102
			)
			((View new:)						;**	aMainDoor
				view:				vAirplaneFront
				loop:				lMainDoor
				cel:				0
				posn:				38 157
				setPri:			15
				ignoreActors:
				addToPic:
			)
		else										;**	from jetway
			(Load VIEW iAirlineTicket)
			((= aStewardess (Prop new:))
				view:				vStewardess
				setLoop:			lTalk
				posn: 			55 101
				stopUpd:
				init:
			)
			(ego
				loop: 			facingNorth
				posn:				42 144
			)
			(HandsOff)
			(= currentStatus GOING_TO_SEAT)
			(rm61Script changeState: fromJetway)
		)
		(ego			init:)
	)
)


(instance rm61Script of Script
	(method (doit)
		(super doit:)

		(if (== state shakeTheScreen)
			(ShakeScreen 1 (Random 1 3))
		)

		(if (& (ego onControl:) cGreen)
			(curRoom newRoom: 62)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/galley, curtain, stewardess')
				(if (ego inRect: nearGalley)
					(Print "There's nothing behind the curtain that you need.")
				else
					(NotClose)
				)
			)

			(if (Said '/door')
				(Print "There are two doors here,
					one leading to the outside and the other to the cockpit.")
			)

			(if (Said '[/people, man, woman, plane, room]')
				(Print "First Class is certainly more comfortable than
					\"Cattle Class!\"")
				(Print "The door to the cockpit is at the front of
					the airplane.")
			)
		)

		(if (Said 'talk/man, woman, people')
			(Print "\"Is there any one here who would like to trade seats?\" you
				ask.")
			(Print "\"NO!\" they yell back in unison.")
		)

		(if (Said 'open/door')
			(cond
				((!= currentStatus NORMAL)
					(NotNow)
				)
				((& (ego onControl:) cRed)
					(Print "This door was locked from the outside before
						takeoff and there's no way you're ever going to open it!")
				)
				((not (& (ego onControl:) cCyan))
					(NotClose)
				)
				(else
					(self changeState: openCockDoor)
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(intro)


			;**	This path takes ego into the plane, and back to his seat.

			(fromJetway
				(= seconds 2)
			)

			(startMoving						; ego in door
				(ego setMotion: MoveTo 53 117 self)
			)

			(sheTalks1
				(aStewardess
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(atStewardess							; go to center of plane
				(aStewardess
					setCel:			0
				)
				(Print "\"Welcome aboard, sir!\"
					says the stewardess with her best plastic smile.
					\"May I see your ticket, please?\"" draw:)
				(Print "You give it to her."
					icon:	iAirlineTicket 0 0
				)
				(= seconds 3)
			)

			(sheTalks2
				(aStewardess
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(gag1
				(aStewardess
					setCel:			0
				)
				(Print "\"Your seat is on your left,
					in the second compartment Mr. uh, Laffer\"
					she says, keeping your ticket." draw:)
				(ego put: iAirlineTicket LIMBO)
				(Print "\"Say! Haven't I seen you on TV?
					Weren't you the big winner of the
					Lucky Life Lottery?\" she asks.")
				(Print "\"Yep, that's me, baby,\"
					you grin, and hand her a $100 bill.
					\"And here's a little something for you.\"")
				(= seconds 3)
			)

			(sheTalks3
				(aStewardess
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(gag2
				(aStewardess
					setCel:			0
					stopUpd:
				)
				(Print "\"It appears that what I've heard is true,\"
					she says." draw:)
				(Print "\"About me, gorgeous?\" you respond.")
				(Print "\"No, about the nouveau riche.
					They ARE incredibly crude!\"")
				(Print (Format @string
					"She gives you her best phony smile.
					\"%s\"" tritePhrase))
				(= seconds 2)
			)

			(ohYeah
				(ego setMotion: MoveTo 103 102 self)
			)

			(pauseInAisle
				(= seconds 3)
			)

			(itsNotSmall
				(Print "\"Why, this plane isn't so small!\"
					you think. \"Look at all the room in here.\"")
				(ego setMotion: MoveTo 326 102)
			)


			;**	This path is only used if he was dumb enough to open the
			;**	cockpit door.

			(openCockDoor
				(HandsOff)
				(= currentStatus NEAR_DEATH)
				(Ok)
				(aCockDoor
					setCycle:		EndLoop self
				)
			)

			(cockDoorDone
				(ego
					illegalBits:	0
					setMotion: 		MoveTo 0 102 self
				)
			)

			(closeCockDoor
				(aCockDoor
					setCycle:		BegLoop self
				)
			)

			(say1
				(Print "\"Carrumba!\" somebody screams,
					\"It's one of those wacko foreign terrorists!\"")
				(= seconds 3)
			)

			(say2
				(Print "\"Grab the fire ax!\"")
				(Print "\"Got him, el Capitan!\"")
				(= seconds 3)
			)

			(shakeTheScreen
				(= seconds 3)
			)

			(UBdead
				(Print "What a waste, Larry!
					To come so far and then try to hijack an airliner!")
				(= currentStatus DEAD)
			)
		)
	)
)

