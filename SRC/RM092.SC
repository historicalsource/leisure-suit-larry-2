;**
;**	Logics for room 92 -- Nontoonyt long shot opening cartoon
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 20, 1988
;**


(script#				92)

(public
	rm92				0
)


;(define	vRm92							806)
	(define	lWaterfall			0)
	(define	lFogIn				1)
	(define	lWaterStops			2)
	(define	lDoor					3)
	(define	lWaterStarts		4)

;(define	vTinyCopter					821)
	(define	lCopter				0)
	(define	lCopterWest			1)

;(define	mIsland						102)
;(define	sCopter						17)


(local
	endOfGame
	aWaterfall
	aFog
	aDoor
	aCopter
)


(enum												;**	fogScript state values
	fogInit
	fogStart
	fogMiddle
	fogDone
)

(enum 											;**	islandScript state values
	islandInit
	emitFog
	fogOut
	stopWaterfall
	waterfallStopped
	openHangerDoor
	doorIsOpen
	closeHangerDoor
	doorIsClosed
	startWaterfall
	waterFlowing
	retractFog
	fogIn
)

(enum												;**	rmScript state values
	intro

	openingCartoon
	setup
	natives
	afterFog
	nonookee
	turnOffTheWater
	doNothing
	heliInPlace
	afterDoor
	shutTheDoor
	doorMustBeShut
	removeTheFog
	allNormal
	meanwhile
	sceneDone

	beachToHanger
	cueTheFog
	headIn
	circlingNontoonyt
	comeOnIn
	afterDoor2
	climbInLarry

	hangerToBeach
	aren'tYou
	turnAndLand
	noBiggie
	turnCorner
	nextRoom
)


(instance theSound of Sound
	(properties
		loop:				-1
		priority:		30
	)
)


(instance rm92 of Room
	(properties
		picture:		92
		horizon:		5
		style:		(+ IRISOUT BLACK)
	)

	(method (init)
		(Load VIEW vRm92)
		(Load VIEW vTinyCopter)

		(super init:)

		((= aCopter (Actor new:))
			view:				vTinyCopter
			ignoreActors:
			illegalBits:	0
			setPri:			6
			setCycle:		Forward
			setStep:			2 1
			setScript:		islandScript
		)

		(self
			setScript: 		rm92Script
		)

		(cond
			((== islandStatus copterComing)
				(Load SOUND sCopter)
				(theSound 
					number:	sCopter
					play:
				)
				(aCopter posn: 103 123)
				(rm92Script changeState: beachToHanger)
			)
			((== islandStatus leavingVolcano)
				(Load SOUND sCopter)
				(theSound 
					number:	sCopter
					play:
				)
				(aCopter	posn: 223 93)
				(rm92Script changeState: hangerToBeach)
			)
			(else
				(Load SOUND mIsland)
				(theSound 
					number:	mIsland
					play:
				)
				(aCopter posn: -7 93)
				(rm92Script changeState: openingCartoon)
			)
		)

		(if (!= islandStatus leavingVolcano)	;**	Leaving uses no props.
			((= aWaterfall (Prop new:))
				view:				vRm92
				ignoreActors:
				loop:				lWaterfall
				posn:				203 88
				setPri:			10
				setCycle:		Forward
				cycleSpeed:		1
				init:
			)
			((= aFog (Prop new:))
				view:				vRm92
				ignoreActors:
				loop:				lFogIn
				cel:				0
				posn:				206 109
				cycleSpeed:		(if (== islandStatus copterComing) 1 else 5)
				setPri:			14
				init:
				hide:
			)
			((= aDoor (Prop new:))
				view:				vRm92
				ignoreActors:
				loop:				lDoor
				cel:				0
				posn:				205 94
				cycleSpeed:		5
				setPri:			4
				init:
			)
		)

		(aCopter				init:)
		(HandsOff)
		(= currentStatus CLOSING_CARTOON)
	)
)


(instance rm92Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (and	(== islandStatus FALSE)	;**	It's the opening cartoon show!
					(not (event claimed?)))
			(theGame restart:)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(intro)

			(openingCartoon
				(= cycles 30)
			)

			(setup
				(Print "Meanwhile, on the beautiful tropical island paradise
					of Nontoonyt Island, located somewhere in the South Pacific,
					a formerly strange weather phenomenon occurs once again."
					BIG time: 12)
				(islandScript changeState: emitFog)
			)

			(natives
				(Print "The island's native tribe has grown accustomed to
					these recent occurrences of dense fog,
					even though it appears (and dissipates) quite rapidly."
					BIG time: 12 draw:)
				(= cycles 20)
			)

			(afterFog
				(aCopter
					setMotion:		MoveTo 186 93 self
				)
				(= cycles 40)
			)

			(nonookee
				(Print "What the natives don't know, however, is that the
					fog is just a smokescreen to cover the activities of
					an evil force so sinister, so sly, so slick,
					that the mere mention of his name brings fear to the
					heart of the staunchest man..." BIG time: 14 draw:)
				(Print "\n\n\ \ \ \ NONOOKEE!\ \ \ \ \ \n\n\n"
					font: bigFont time: 5)
				(= cycles 40)
			)

			(turnOffTheWater
				(islandScript changeState: stopWaterfall)
			)

			;**	(doNothing)		just wait for helicopter to arrive

			(heliInPlace
				(islandScript changeState: openHangerDoor)
			)

			(afterDoor
				(aCopter
					setMotion:	MoveTo 223 93 self
				)
			)

			(shutTheDoor
				(islandScript changeState: closeHangerDoor)
			)

			(doorMustBeShut
				(islandScript changeState: startWaterfall)
			)

			(removeTheFog
				(islandScript changeState: retractFog)
			)

			(allNormal
				(= cycles 20)
			)

			(meanwhile
				(Print "Inside his volcanic mountain fortress,
					the dirty Doctor is designing the most disgusting of his
					dastardly deeds." BIG time: 8 draw:)
				(= seconds 3)
			)

			(sceneDone
				(curRoom newRoom: 93)
			)


			;**	This portion handles the SI chopper coming to Larry's rescue:

			(beachToHanger
				(= cycles 30)
			)

			(cueTheFog
				(aFog
					show:
					setScript: 		fogScript
				)
				(aCopter
					setMotion:		MoveTo 98 93 self
				)
			)

			(headIn
				(islandScript changeState: stopWaterfall)
			)

			(circlingNontoonyt
				(aCopter
					setMotion:		MoveTo 186 93 self
				)
			)

			(comeOnIn
				(islandScript changeState: openHangerDoor)
			)

			(afterDoor2
				(aCopter
					setMotion:	MoveTo 223 93 self
				)
			)

			(climbInLarry
				(curRoom newRoom: 85)
			)


			;**	This is the return from fortress to beach...

			(hangerToBeach
				(aCopter
					setMotion:	MoveTo 149 93
				)
				(= cycles 20)
			)

			(aren'tYou
				(Print "\"Say, aren't you that guy that won that
					big lottery prize?\" asks the pilot.")
					(Print (Format @string
					"\"Yep, that's me,\" you reply proudly.
						%s" laffer))

				(= cycles 20)
			)

			(turnAndLand
				(Print "\"Well, Larry, I've got a little surprise for you.
					I just heard on the radio:
					they just went bankrupt. Kaput. Busted.
					I hope you saved some of that money.\"")
				(= cycles 10)
			)

			(noBiggie
				(Print "\"It doesn't matter,\" you assert,
					\"I have the love of a beautiful woman
					and we can live here on this wonderful island for
					the rest of our lives.
					Why do I need money?
					I'll give up my leisure suit for a muumuu!\"")
				(= cycles 20)
			)

			(turnCorner
				(aCopter
					setMotion:	MoveTo 246 120 self
				)
			)

			(nextRoom
				(= islandStatus onBeach)
				(curRoom newRoom: 75)
			)
		)
	)
)


(instance islandScript of Script
	(method (changeState newState)
		(switch (= state newState)

;**				(islandInit)

			(emitFog
				(aFog
					show:
					setCycle:	EndLoop self
				)
			)
			(fogOut
				(aFog
					stopUpd:
				)
				(rm92Script 	cue:)
			)


			(stopWaterfall
				(aWaterfall
					cycleSpeed:	2
					setLoop:		lWaterStops
					setCycle:	EndLoop self
				)
			)
			(waterfallStopped
				(aWaterfall		hide:)
				(rm92Script 	cue:)
			)


			(openHangerDoor
				(aDoor
					setCycle:	EndLoop self
				)
			)
			(doorIsOpen
				(rm92Script 	cue:)
			)


			(closeHangerDoor
				(aDoor
					setCycle:	BegLoop self
				)
			)
			(doorIsClosed
				(rm92Script 	cue:)
			)


			(startWaterfall
				(aWaterfall
					show:
					setLoop:		lWaterStarts
					cel:			0
					setCycle:	EndLoop self
				)
			)
			(waterFlowing
				(aWaterfall
					setLoop:		lWaterfall
					cycleSpeed:	0
					setCycle:	Forward
				)
				(rm92Script 	cue:)
			)


			(retractFog
				(aFog
					show:
					setCycle:	BegLoop self
				)
			)
			(fogIn
				(aFog				hide:)
				(rm92Script 	cue:)
			)
		)
	)
)

(instance fogScript of Script
	(method (changeState newState &tmp fogX fogY)
		(switch (= state newState)

			(fogInit
				(= cycles 5)
			)

			(fogStart
				(aFog setCycle: EndLoop self)
			)

			(fogMiddle
				(aFog setCycle: BegLoop self)
			)

			(fogDone
				(self changeState: fogInit)
			)
		)
	)
)

