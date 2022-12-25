;**
;**	Logics for room 74 -- Larry Goes Swinging Across The Piranha River
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 22, 1988
;**


(script#				74)

(public
	rm74				0
)


(define	nearVine			 59  91	78	 98)
(define	nearTHEvine		149 100 195 129)
(define	changeRooms		261  72 321	 77)

;(define	vBigEgo						110)
;(define	vEgoWading					176)
;(define	vEgoHalfEaten				177)
	(define	lBElooksDown		4)
	(define	lBEgroan				5)

;(define	vEgoSwingingJungle		178)
	(define	lVine1				0)
	(define	lVine2and3			1)
	(define	lToWater				2)
	(define	lToGround			3)
	(define	lSplash				4)

;(define	vRm74							729)


(local
	vineHere 
	seenCreekMessage 
	seenMessage
	grabbedVine
	aBigEgo
	aBigEgoFace
	aRapids
	aVine1
	aVine2
	aVine3
	aTHEvine
)


(enum												;**	rmScript state values
	intro

	touchedWater
	thatTickles
	emerge
	doTake
	holdTake
	takeDone
	groan
	removeBigFace
	UBdying

	doVine1
	didn'tGrab2
	splash1
	walkOut1

	doVine2
	didn'tGrab3
	splash2
	walkOut2

	doVine3
	released3
	leave3
	splash3
	walkOut3

	doJump
	jumpDone
	heMadeIt
)


(instance rm74 of Room
	(properties
		picture:		74
		horizon:		66
		west:			73
		east:			75
	)

	(method (init)
		(Load VIEW vEgoSwingingJungle)
		(Load VIEW vRm74)

		(super init:)

		(if ((inventory at: iVine) ownedBy: curRoomNum)
			(= vineHere TRUE)
			((= aTHEvine (View new:))
				view: 			vEgoSwingingJungle
				loop: 			5
				posn: 			184 38
				setPri: 			9
				ignoreActors:
				stopUpd:
				init:
			)
		)

		((View new:)							;**	vine 1
			view: 			vEgoSwingingJungle
			loop: 			5
			cel: 				1
			posn: 			18 41
			setPri: 			12
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	vine 2
			view: 			vEgoSwingingJungle
			loop: 			5
			cel: 				0
			posn: 			37 56
			setPri: 			12
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	vine 3
			view: 			vEgoSwingingJungle
			loop: 			5
			cel: 				0
			posn: 			223 28
			setPri: 			12
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	vine 4
			view: 			vEgoSwingingJungle
			loop: 			5
			cel: 				1
			posn: 			202 11
			setPri: 			12
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	vine 5
			view: 			vEgoSwingingJungle
			loop: 			5
			cel: 				1
			posn: 			44 41
			setPri: 			12
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	vine 6
			view: 			vEgoSwingingJungle
			loop: 			5
			cel: 				0
			posn: 			192 33
			setPri: 			12
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	vine 7
			view: 			vEgoSwingingJungle
			loop: 			5
			posn: 			23 52
			setPri: 			7
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	vine 8
			view: 			vEgoSwingingJungle
			loop: 			1
			posn: 			11 43
			setPri: 			7
			ignoreActors:
			addToPic:
		)

		((= aRapids (Prop new:))
			view:				vRm74
			setLoop:			0
			setCycle:		Forward
			posn:				124 68
			init:
		)

		((= aVine1 (Prop new:))
			view:				vEgoSwingingJungle
			ignoreActors:
			setLoop:			0
			setPri:			8
			posn:				85 45
			stopUpd:
			init:
		)
		((= aVine2 (Prop new:))
			view:				vEgoSwingingJungle
			ignoreActors:
			setLoop:			1
			setPri:			8
			posn:				111 33
			stopUpd:
			init:
		)
		((= aVine3 (Prop new:))
			view:				vEgoSwingingJungle
			ignoreActors:
			setLoop:			1
			setPri:			8
			posn:				136 21
			stopUpd:
			init:
		)

		(if (and	(== pastPiranha FALSE)
					(!= prevRoomNum 75))
			(Load VIEW vEgoHalfEaten)
			(Load VIEW vEgoWading)
			(Load VIEW vBigEgo)
			((= aBigEgo (View new:))		;**	His empty head.
				view:				vBigEgo
				ignoreActors:
				setPri:			14
				posn:				243 1099
				init:
			)
			((= aBigEgoFace (Prop new:))	;**	facial details
				view:				vEgoHalfEaten
				setLoop:			lBElooksDown
				ignoreActors:
				setPri:			15
				cycleSpeed:		1
				posn:				243 1099
				init:
			)
		)

		(NormalEgo)
		(if (== prevRoomNum 75)
			(ego
				posn:				288 77
				observeControl:cBlue cRed
			)
		else
			(ego posn:			2 77)
		)
		(ego					init:)

		(self
			setRegions:		rgIsland
			setScript: 		rm74Script
		)
	)
)


(instance rm74Script of Script
	(method (doit)
		(super doit:)

		(cond
			((and	(ego inRect: changeRooms)
					(== currentStatus NORMAL))
				(curRoom newRoom: 75)
			)
			((and	(== currentStatus WADING)
					(& (ego onControl:) cRed))
				(self changeState: emerge)
			)
			((and	(== currentStatus WADING)
					(& (ego onControl:) cLtGreen))
				(if (== seenMessage FALSE)
					(= seenMessage TRUE)
					(Print "The bank here is too steep and slippery for you
						to climb out.")
				)
			)
			((and	(== currentStatus WADING)
					(& (ego onControl:) cLtMagenta))
				(if (== seenCreekMessage FALSE)
					(= seenCreekMessage TRUE)
					(Print "Don't go that way, Larry!
						There's no game over there!!")
				)
			)
			((and	(== currentStatus NORMAL)
					(== (ego onControl:) cBlue))
				(self changeState: touchedWater)
			)
			(else
				(= seenCreekMessage FALSE)
				(= seenMessage FALSE)
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/ocean, beach')
				(Print "The ocean appears to be just beyond that clump of
					trees near the beach.")
			)

			(if (Said '/river, water')
				(Print "A swift river cascades down from the island's
					volcano behind the jungle,
					splashing its way over some extremely dangerous rapids
					before becoming more placid and tranquil here in the jungle.")
			)

			(if (Said '/fish')
				(if (!= currentStatus WADING)
					(Print "You can only see a few vague shapes from here.")
				else
					(Print "They are presently feasting.
						Looks like dinner is on you, Larry!")
				)
			)

			(if (Said '/tree, vine, bush')
				(Print "Many vines hang down from the jungle canopy overhead.")
				(if (== vineHere TRUE)
					(Print "One in particular attracts your eye:
						although it appears sturdy,
						it has been weakened near its top by excessive swinging!")
				)
			)

			(if (Said '/rock, waterfall')
				(Print "The slippery rocks are far too treacherous for you
					to traverse.")
			)

			(if (Said '[/room, jungle]')
				(Print "A quiet brook babbles nearby.
					Vines hang from the jungle canopy, which thins as it
					reaches the beach just visible ahead.")
			)
		)

		(if (Said 'grab, pull, get/vine')
			(cond
				((!= currentStatus NORMAL)
					(NotNow)
				)
				((not vineHere)
					(Print "All the other vines are out of your reach.")
				)
				((ego inRect: nearVine)
					(Print "You tug as hard as you can,
						but this vine is securely attached to that big branch
						above you.")
				)
				((not (ego inRect: nearTHEvine))
					(NotClose)
				)
				(else
					(= vineHere FALSE)
					(ego get: iVine)
					(theGame changeScore: 4)
					(aTHEvine dispose:)
					(Print "You reach up, grasp the vine firmly,
						and give it a sharp jerk,
						yanking it from the tree branch above you!" draw:)
					(Print "You carefully coil it up and insert it into your
						left front pants pocket.")
				)
			)
		)

		(if (Said 'swim')
			(Print "Not in that water!")
		)

		(if (Said 'anyword/rock')
			(if (== currentStatus WADING)
				(Print "The bank is too steep and too slippery here.
					Keep looking for a spot.")
			else
				(Print "The rocks are too slippery and steep for you to climb.")
			)
		)

		(if (or	(Said 'jump, (let<go)')
					(Said 'release, use, swing, change/vine')
					(Said 'swing/river, water')
					(Said 'swing<on/vine'))
			(cond
				((and	(>= currentStatus SWINGING1)
						(<= currentStatus SWINGING3))
					(= grabbedVine TRUE)
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				((not (ego inRect: nearVine))
					(NotClose)
				)
				(else
					(self changeState: doVine1)
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(intro)

			(touchedWater
				(= currentStatus WADING)
				(User
					canControl: 	TRUE
					canInput: 		TRUE
				)
				(ego
					view:				vEgoWading
					observeControl:cBlack cLtWhite	;**	stay off the grass
					setLoop:			RELEASE
					setStep:			3 2
					setCycle:		Walk
					setPri:			RELEASE
				)
				(= cycles 8)
			)

			(thatTickles
				(Print "You feel a tickling sensation around your toes.
					Boy, this adventuring life is sure fun!" draw:)
			)

			(emerge
				(Print "This seems a safe place to climb out.")
				(ego
					view:				vEgoHalfEaten
					setPri:			RELEASE
					illegalBits:	cLtWhite
					observeControl:cRed cBlue cYellow
				)
				(User
					canInput: 		FALSE
				)
				(= currentStatus NEAR_DEATH)
				(curRoom east: FALSE)
				(= cycles	0)
				(= seconds 6)
			)

			(doTake
				(User
					canControl: 	FALSE
				)
				(ego
					setMotion:		FALSE
					setLoop:			facingSouth
				)
				(aBigEgo
					posn:				(ego x?) 88
					stopUpd:
				)
				(aBigEgoFace
					posn:				(ego x?) 88
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(holdTake
				(= cycles 7)
			)

			(takeDone
				(aBigEgoFace
					setCycle:		BegLoop self
				)
			)

			(groan
				(aBigEgoFace
					loop:				lBEgroan
					setCycle:		EndLoop
				)
				(= seconds 3)
			)

			(removeBigFace
				(Print "Gee. Those piranha really work fast!" HIGH)
										 ;**	piranha spell-checked September 29, 1988
				(aBigEgoFace		dispose:)
				(aBigEgo				dispose:)
				(= seconds 3)
			)

			(UBdying
				(Print "For some reason,
					your heart just isn't in the game any more...")
				(Print "...not to mention several other organs!")
				(= currentStatus DEAD)
			)


			;**	This swings Tarzonego across the river.

			(doVine1
				(Ok)
				(ego					hide:)
				(User canControl: FALSE)
				(= currentStatus SWINGING1)
				(aVine1
					cel:				1
					setCycle:		CycleTo 7 FORWARD self
				)
			)

			(didn'tGrab2
				(if grabbedVine
					(= grabbedVine FALSE)
					(self changeState: doVine2)
				else
					(aVine1
						cel:				8
						setCycle:		EndLoop
					)
					(ego
						view:				vEgoSwingingJungle
						illegalBits:	0
						ignoreActors:
						ignoreHorizon:
						setLoop:			lToWater
						setPri:			8
						setStep:			3 4
						cel:				0
						posn:				106 86
						show:
						setCycle:		EndLoop
						setMotion:		MoveTo 106 102 self
					)
				)
			)

			(splash1
				(ego
					setLoop:		lSplash
					posn:			106 104
					cel:			0
					setCycle:	EndLoop self
				)
			)

			(walkOut1
				(Print "Next time, you may want to consider using
					the next vine as well!" HIGH draw:)
				(self changeState: touchedWater)	;**	Soon you'll be dead, ego.
			)

			(doVine2
				(aVine1
					setCel:		8
					setCycle:	EndLoop
				)
				(aVine2
					setCel:		1
					setCycle:	CycleTo 6 FORWARD self
				)
			)

			(didn'tGrab3
				(if grabbedVine
					(= grabbedVine FALSE)
					(self changeState: doVine3)
				else
					(aVine2
						cel:				7
						setCycle:		EndLoop
					)
					(ego
						view:				vEgoSwingingJungle
						illegalBits:	0
						ignoreActors:
						ignoreHorizon:
						setLoop:			lToWater
						setPri:			8
						setStep:			3 7
						cel:				0
						posn:				131 74
						show:
						setCycle:		EndLoop
						setMotion:		MoveTo 131 102 self
					)
				)
			)

			(splash2
				(ego
					setLoop:		lSplash
					posn:			131 104
					cel:			0
					setCycle:	EndLoop self
				)
			)

			(walkOut2
				(Print "Next time, you may want to grab that third vine!"
					HIGH draw:)
				(self changeState: touchedWater)	;**	Soon you'll be dead, ego.
			)

			(doVine3
				(aVine1				stopUpd:)
				(aVine2
					setCel:			7
					setCycle:		EndLoop
				)
				(aVine3
					cel:				1
					setCycle:		CycleTo 6 FORWARD self
				)
			)

			(released3
				(aVine2				stopUpd:)
				(if grabbedVine				;**	actually "released vine"
					(= grabbedVine FALSE)
					(aVine3
						setCel:		6
					)
					(self changeState: doJump)
				else
					(aVine3 setCycle: BegLoop self)
				)
			)

			(leave3
				(ego
					view:				vEgoSwingingJungle
					illegalBits:	0
					ignoreActors:
					ignoreHorizon:
					setLoop:			lToWater
					setPri:			8
					setStep:			3 7
					cel:				0
					posn:				131 74
					show:
					setCycle:		EndLoop
					setMotion:		MoveTo 131 102 self
				)
			)

			(splash3
				(ego
					setLoop:			lSplash
					posn:				131 104
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(walkOut3
				(Print "Next time, you may want to release the third vine
					when you believe your trajectory will carry you 
					safely over land!"
					HIGH draw:)
				(self changeState: touchedWater)	;**	Soon you'll be dead, ego.
			)

			(doJump
				(aVine3
					setCel:			7
					setCycle:		EndLoop
				)
				(ego
					illegalBits:	0
					ignoreActors:
					ignoreHorizon:
					view:				vEgoSwingingJungle
					setLoop:			lToGround
					setPri:			ltBlue
					setCel: 			0
					posn: 			158 62
					show:
					setCycle:		CycleTo 5 FORWARD
					setStep:			1 10
					setMotion:		MoveTo 167 122 self
				)
			)

			(jumpDone
				(aVine3				stopUpd:)
				(ego setCycle:		EndLoop self)
			)

			(heMadeIt
				(NormalEgo facingEast)
				(if (== pastPiranha FALSE)
					(= pastPiranha TRUE)
					(theGame changeScore: 6)
					(Print "(You knew all along you were a swinger!)" LOW draw:)
				)
			)
		)
	)
)

