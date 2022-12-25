;**
;**	Logics for room 22 -- Queen Larry, the Loose Goose, Scurvy Dog Saloon
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 20, 1988
;**


(script#				22)

(public
	rm22				0
)


;(define	vEgoDrinkingBrownBag		102)
	(define	lReachingEast		0)
	(define	lReachingWest		1)
	(define	lDrinkingEast		2)
	(define	lDrinkingWest		3)
	(define	lFallingEast		4)
	(define	lFallingWest		5)
	(define	lStars				6)

;**	This guy walks around, and hands bottle to ego
;(define	vWharfHench1				234)	;**	handing jug loops are +2 walking

;**	two guys lying in doorways are in loops 0 and 1
;(define	vWharfHench2				235)

;(define	vScurvyDog					252)


(local
	work
	henchHere
	aSpudsSign1
	aSpudsSign2
	aSpudsSign3
	aBoat
	aHench
	aStars
)


(enum												;**	boatScript state values
	boatInit
	startBoat
	boatOffScreen
)

(enum												;**	rmScript state values
	intro

	waitingForHench
	henchAppears
	henchSpoke
	goingToHench
	larryAtHench
	larryLiftingBottle
	larryDrinking
	ahhh
	lowerBottle
	raiseBottle
	maybe1more
	ohoh
	ohohoh
	seeStars
	UBdying
)


(instance rm22 of Room
	(properties
		picture:		22
		horizon:		127
		north:		18
		south:		25
		west:			21
	)

	(method (init)
		(Load VIEW vScurvyDog)
		(Load VIEW vWharfHench2)			;**	lying in doorway

		(super init:)

		((View new:)							;**	QueenLarry
			view: 			vScurvyDog
			loop: 			1
			cel: 				1
			posn: 			287 77
			setPri: 			4
			addToPic:
		)
		((View new:)							;**	leftDoor
			view: 			vScurvyDog
			loop: 			1
			cel: 				2
			posn: 			64 143
			setPri: 			10
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	rightDoorway
			view: 			vScurvyDog
			loop: 			1
			cel: 				3
			posn: 			188 152
			setPri: 			0
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	boats
			view: 			vScurvyDog
			loop: 			1
			posn: 			284 89
			setPri: 			5
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	wino west
			view: 			vWharfHench2
			posn: 			63 149
			setPri: 			11
			addToPic:
		)
		((View new:)							;**	wino east
			view: 			vWharfHench2
			loop: 			1
			posn: 			182 157
			setPri: 			11
			addToPic:
		)
		((View new:)							;**	Spuds' head
			view: 			vScurvyDog
			loop: 			2
			posn: 			54 85
			setPri: 			12
			addToPic:
		)
		((= aSpudsSign1 (Prop new:))		;**	"Scurvy"
			view:				vScurvyDog
			setLoop:			3
			setPri:			12
			posn:				32 84
			cycleSpeed:		4
			setCycle:		Forward
			init:
		)
		((= aSpudsSign2 (Prop new:))		;**	"dog"
			view:				vScurvyDog
			setLoop:			4
			setPri:			12
			posn:				77 84
			cycleSpeed:		4
			setCycle:		Forward
			init:
		)
		((= aSpudsSign3 (Prop new:))		;**	"saloon"
			view:				vScurvyDog
			setLoop:			5
			setPri:			12
			posn:				54 94
			cycleSpeed:		4
			setCycle:		Forward
			init:
		)

		((= aBoat (Actor new:))
			view:				vScurvyDog
			setLoop:			0
			setPri:			7
			illegalBits:	0
			ignoreActors:
			ignoreHorizon:
			posn:				249 107
			setCycle:		Forward
			init:
			moveSpeed:		1
			cycleSpeed:		1
			setStep:			1 1
			setScript:		boatScript
		)

		(self
			setRegions:		rgLA
			setScript: 		rm22Script
		)

		(if (ego has: iOnklunk)
			(= henchHere TRUE)
			(rm22Script changeState: waitingForHench)
			(Load VIEW vEgoDrinkingBrownBag)
			(Load VIEW vWharfHench1)
			(Load SOUND mHench)

			(theSound init:)

			((= aHench (Actor new:))
				view:				vWharfHench1
				posn:				-11 175
				illegalBits:	cLtWhite
				setCycle:		Walk
				init:
				hide:
			)
			((= aStars (Prop new:))
				view:				vEgoDrinkingBrownBag
				setLoop:			lStars
				setCycle:		Forward
				setPri:			ltMagenta
				ignoreActors:
				init:
				hide:
			)
		)

		(cond
			((== prevRoomNum	0)		(ego posn: 302 188))
			((== prevRoomNum	18)	(ego posn: 258 129))
			((== prevRoomNum	26)	(ego posn: 302 188))
		)
		(NormalEgo)
		(ego					init:)
	)
)


(instance rm22Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (== state henchSpoke)
			(if (or 	(Said 'yes')
				 		(Said 'take/drink'))
				(if ( < (ego y?) 162)
					(NotClose)
				else
					(Print "\"Sure,\" you reply,
						\"always time for a little nip with some
						seedy-looking stranger!\"")
					(self changeState: goingToHench)
				)
			)

			(if (or	(Said 'no')
						(Said 'refuse/drink'))
				(Print "\"Thanks anyway, buster, but I'm on the wagon.\"")
			)

			(if (Said 'talk/man')
				(if (Random 0 1)
					(Print "\"That's some breath you're packing, buddy,\" you say.")
				else
					(Print "\"Do you have a license for that breath of yours?\" 
						you ask him.")
				)
				(Print "He ignores you, \"How 'bout a little nip?\"")
			)
		)

		(if (Said 'knock, open/door')
			(Print "The doors are barred shut, as this is a seedy part of town.
				In fact, you probably shouldn't be walking around here!")
		)

		(if (Said 'look>')
			(if (Said '/man')
				(if (not henchHere)
					(Print "They're just a couple of guys who can't say no.")
				else
					(Print "He looks friendly enough, for a scum-sucking
						gutter snipe!")
				)
			)

			(if (Said '/door')
				(Print "This place will remain closed until this game goes into
					night mode.")
			)

			(if (Said '/litter, ground, street')
				(Print "This part of town is not as well-maintained as
					it should be.")
			)

			(if (Said '/window')
				(Print "There's nothing inside.")
			)

			(if (Said '/ocean, beach, water, wave')
				(Print "The ocean waves, and the dock \"piers\" back at you.")
			)

			(if (Said '/ship, boat')
				(Print "Yep. That's what's out there!")
			)

			(if (Said '/sign')
				(Print "Aren't you tired of seeing that dog everywhere!")
			)

			(if (Said '[/building, store, room]')
				(Print "You are now in a very seedy portion of the city.")
				(Print "In the distance you can see those famous L._A. landmarks:
					the \"Queen Larry\" and the \"Loose Goose.\"")
			)
		)

		(if (Said '/door, bar')
			(Print "This place will remain closed until this game goes into
				night mode.")
		)

		(if (Said '/man>')
			(cond
				((Said 'talk/')
					(Print "There's no need. 
						These guys are too gone to hear you!")
				)

				((Said '(step<over), move, wake/')
					(Print "Why bother? This bar is off limits.")
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(intro)

			;**	This path is after Larry has the onklunk, and makes the hench
			;**	offer him a drink.

			(waitingForHench
				(= seconds (Random 3 5))
			)

			(henchAppears
				(aHench
					show:
					setMotion:		MoveTo 127 175 self
				)
				(theSound play:)
			)

			(henchSpoke
				(aHench
					setLoop:			(if ( < (aHench x?) (ego x?))
											facingEast
										else
											facingWest
										)
				)
				(Print "\"Hey, sonny,\" says the old man,
					\"wouldja like a hit ah the 'bird?\"" draw:)
			)


			;**	Here we wait for him to say yes...	If he does,

			(goingToHench
				(HandsOff)
				(= currentStatus NEAR_DEATH)
				(if ( < (aHench x?) (ego x?))
					(ego
						setAvoider:	Avoider
						ignoreActors:
						setMotion:	MoveTo (+ (aHench x?) 20) (+ 1 (aHench y?)) self
					)
				else
					(ego
						setAvoider:	Avoider 
						ignoreActors:
						setMotion:	MoveTo (- (aHench x?) 20) (+ 1 (aHench y?)) self
					)
				)
			)

			(larryAtHench
				(ego
					view:				vEgoDrinkingBrownBag
					setLoop:			(if ( < (aHench x?) (ego x?))
											facingWest
										else
											facingEast
										)
					cel:				0
					setCycle:		EndLoop self
				)
				(= work	(if ( < (aHench x?) (ego x?))
									facingEast
							else
									facingWest
							)
				)
				(aHench
					setLoop:			( + 2 work)
					setCycle:		EndLoop
				)
			)

			(larryLiftingBottle
				(aHench				stopUpd:)
				(ego
					setLoop:			( + 2 (ego loop?))	;**	bump curLoop 2
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(larryDrinking
				(= seconds 3)
			)

			(ahhh
				(Print "Say! This really hits the spot.
					Nothing like a good, hard, snort to perk up an
					adventurer's spirits.")
				(= seconds 3)
			)

			(lowerBottle
				(ego
					setCycle:		CycleTo 3 REVERSE self
				)
			)

			(raiseBottle
				(ego
					setCycle:		EndLoop self
				)
			)

			(maybe1more
				(Print "A little more couldn't hurt...")
				(= seconds 3)
			)

			(ohoh
				(ego
					setLoop:			( + 2 (ego loop?))	;**	bump curLoop 2 more
					cel:				0
					cycleSpeed:		1
					setCycle:		EndLoop self
				)
			)

			(ohohoh
				(Print "(THUMP!)" at: -1 123)
				(= seconds 3)
			)

			(seeStars
				(if (== (ego loop?) lFallingEast)
					(= work (- (ego x?) 3))
				else
					(= work (+ (ego x?) 4))
				)
				(aStars
					posn:				work (- (ego y?) 19)
					show:
				)
				(= seconds 5)
			)

			(UBdying
				(Print "\"Gotcha, you scurvy dog!\"
					cries the cleverly disguised KGB agent.
					\"Now, let's go to my place (and not for a drink)!\"")
				(Print "These guys are playing for keeps, Larry.
					You'd better be more careful
					if you're going to hang onto your onklunk!")
				(= currentStatus HEADING_FOR_DEATH_SCENE)
				(curRoom newRoom: 96)
			)
		)
	)
)


(instance boatScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(boatInit
				(= seconds (Random 3 15))
			)

			(startBoat
				(aBoat setMotion:	MoveTo 333 107 self)
			)

			(boatOffScreen
				(aBoat hide:)
			)
		)
	)
)

(instance theSound of Sound
	(properties
	 	number:	mHench
		loop:		1
	)
)


