;**
;**	Logics for room 81 -- Glacier Pointless
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 3, 1988
;**


(script#				81)

(public
	rm81				0
)


(define	nearIcePath		127 130 202 154)
(define	pissHere			34 161	37 165)

(define	coilX				153)
(define	coil1y			119)
(define	coil2y			103)
(define	coil3y			87)
(define	coil4y			71)

(define	egoCoilX			156)
(define	egoCoil1y		134)
(define	egoCoil2y		118)
(define	egoCoil3y		102)
(define	egoCoil4y		86)
(define	egoTopY			70)

;(define	vEgoSliding					181)
	(define	lBalancing			0)
	(define	lSliding				1)
	(define	lSitting				2)
	(define	lStanding			3)

;(define	vEgoAshing					182)
;(define	vEgoClimbingCoils			183)

;(define	vGlacier						726)
	(define	lYellowName			0)
	(define	lCoil					1)


(local
	readMessage
	spreading
	hasPissed
	aCoil
	aYellowName
	localBS										;**	bassSetter
)


(enum												;**	rmScript state values
	intro
	startSlipping
	startSliding
	startSitting
	ouch
	riseUp
	slideDone
	slippingDone

	spread'em
	doCoil1
	howdyDoit
	addCoil1
	doCoil2
	addCoil2
	doCoil3
	addCoil3
	doCoil4
	addCoil4
	coilDone
	changeRooms

	startPissing
	pissDone
)


(instance rm81 of Room
	(properties
		picture:		81
		horizon:		1
		south:		80
	)

	(method (init)
		(Load VIEW vGlacier)
		(Load VIEW vEgoSliding)
		(Load VIEW vEgoAshing)
		(Load VIEW vEgoClimbingCoils)
		(Load VIEW iSand)
		(Load VIEW iAshes)

		(super init:)

		((= aCoil (Prop new:))
			view:				vGlacier
			setLoop:			lCoil
			posn:				151 123
			cycleSpeed:		10
			ignoreActors:
			init:
			hide:
		)

		((= aYellowName (Prop new:))
			view:				vGlacier
			setLoop:			lYellowName
			setCel:			0
			setPri:			11
			posn: 			53 137
			setCycle:		Forward
			ignoreActors:
			cycleSpeed:		2
			init:
			hide:
		)

		(NormalEgo)
		(ego
			posn: 			155 186
			init:
		)

		(self
			setRegions:		rgIsland
			setScript: 		rm81Script
		)
	)
)


(instance rm81Script of Script
	(method (doit)
		(super doit:)

		(cond
			((and	(& (ego onControl: origin) cBlue)
					(== spreading FALSE)
					(== currentStatus NORMAL))
				(= currentStatus SLIPPING)
				(self changeState: startSlipping)
			)
			((and	(& (ego onControl: origin) cGreen)
					(== spreading FALSE)
					(== currentStatus SLIPPING))
				(= currentStatus SLIDING)
				(self changeState: startSliding)
			)
			((and	(& (ego onControl: origin) cRed)
					(== spreading FALSE)
					(== currentStatus SLIPPING))
				(= currentStatus SLIDING)
				(self changeState: slippingDone)
			)
			((and (== hasPissed FALSE)
					(== facingEast (ego loop?))
					(ego inRect: pissHere))
				(= hasPissed TRUE)
				(self changeState: startPissing)
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/snow, ice')
				(Print "The ice is absolutely impenetrable.
					There's no way to crack, move, or break it.")
			)

			(if (Said '/ground, floor')
				(Print "The ramp-like path before you leads to the top of
					the volcano.
					It is very steep and very slippery!")
			)

			(if (Said '[/room, island, mountain, ice, glacier]')
				(Print "Ice, ice, everywhere. (And, not a drop to drink!)")
				(Print "Isn't it amazing?
					With that hot, tropical sun overhead
					and the temperature somewhere in the nineties,
					this glacier remains frozen solid.")
				(Print "Somewhere, off in the far distance,
					you think you hear a low-pitched humming noise,
					like a large electric motor.
					How silly.
					There's no electricity on an uncivilized,
					isolated, tropical island like this!")
			)
		)

		(if (and (ego has: iHairRejuvenator)
					(Said 'put, pour, use, drop/bottle, tonic'))
			(Print "Ok. You dump the bottle of hair rejuvenator on the ice.")
			(Print "Nothing happens.")
			(ego put: iHairRejuvenator LIMBO)
			(theGame changeScore: -5)
		)

		(if (Said 'run, climb')
			(Print "Better watch out. This area is slippery!")
		)

		(if (Said 'spread, use/ash')
			(cond
				((not (ego has: iAshes))
					(DontHave)
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				((not (ego inRect: nearIcePath))
					(Print "Move a little closer to the center of
						the ice-laden rampway leading up and out of this
						refrigerated hellhole!")
				)
				(else
					(theGame changeScore: 10)
					(Ok)
					(= spreading iAshes)
					(self changeState: spread'em)
				)
			)
		)
		(if (Said 'spread, use/sand')
			(cond
				((not (ego has: iSand))
					(DontHave)
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				((not (ego inRect: nearIcePath))
					(Print "Move a little closer to the center of
						the ice-laden rampway leading up and out of this
						refrigerated hellhole!")
				)
				(else
					(theGame changeScore: 10)
					(Ok)
					(= spreading iSand)
					(self changeState: spread'em)
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(intro)

			(startSlipping
				(Print "Hey! This ice is slippery!!" HIGH)
				(ego
					view:				vEgoSliding
					setLoop:			lBalancing
					setCycle:		Forward
					setStep:			1 1
					baseSetter:		(= localBS (BassSetter new:))
				)
			)

			(startSliding
				(Print "Look out!" HIGH)
				(HandsOff)
				(ego
					setStep:			1 5
					setLoop:			lSliding
					cel:				0
					setCycle:		EndLoop
					setMotion:		MoveTo 160 132 self
				)
			)

			(startSitting
				(ego
					view:				vEgoSliding
					setLoop:			lSitting
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(ouch
				(Print "Ow!" draw:)
				(= cycles 25)
			)

			(riseUp
				(ego
					setLoop:			lStanding
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(slideDone
				(NormalEgo facingEast)
				(self changeState: slippingDone)
			)


			;**	This is if he walks down off the ice on his own...

			(slippingDone
				(ego	baseSetter:	0)
				(NormalEgo)
				(if localBS (localBS dispose:))
			)


			;**	This handles Larry spreading Bob Heitman's memorial ashes
			;**	on the snowbank.

			(spread'em
				(= currentStatus SPREADING)
				(HandsOff)
				(ego
					illegalBits:	0
					ignoreActors:
					setMotion:		MoveTo egoCoilX egoCoil1y self
				)
			)

			(doCoil1
				(aCoil
					posn:				coilX coil1y
					cel:				0
					setCycle:		EndLoop self
					show:
				)
				(ego
					view:				vEgoAshing
					setLoop:			0
					setCycle:		Forward
				)
				(= cycles 10)
			)

			(howdyDoit
				(if (== spreading iSand)
					(ego put: iSand LIMBO)
					(Print "The broiling tropical sun strikes the 
						sand you took from the beach 
						and quickly melts the snow, revealing..." 
						icon: iSand 0 0
						HIGH
					)
				else
					(ego put: iAshes LIMBO)
					(Print "The broiling tropical sun strikes the 
						black ashes you took from the village campfire 
						and quickly melts the snow, revealing..." 
						icon: iAshes 0 0
						HIGH
					)
				)
			)

			(addCoil1
				(Print "...refrigeration coils!\n\n
					You immediately realize the meaning: Dr. Nonookee has
					created a glacier on the side of a volcano in the middle
					of the tropics to prevent the island natives from disturbing
					his evil doings!"	BIG draw:)
				(Print "What power!" HIGH)
				(Print "What force!" HIGH)
				(if ( > filthLevel DIRTY)
					(Print "(What an asshole!)" LOW)
				)
				(aCoil				hide:)
				((View new:)					;**	addToPic coil #1
					view: 			vGlacier
					loop: 			lCoil
					cel: 				4
					posn: 			coilX coil1y
					ignoreActors:
					addToPic:
				)
				(ego
					view:				vEgoClimbingCoils
					cel:				0
					posn:				egoCoilX egoCoil2y
					setCycle:		EndLoop self
				)
			)

			(doCoil2
				(aCoil
					posn:				coilX coil2y
					cel:				0
					setCycle:		EndLoop self
					show:
				)
				(ego
					view:				vEgoAshing
					setCycle:		Forward
				)
			)

			(addCoil2
				(aCoil				hide:)
				((View new:)					;**	addToPic coil #2
					view: 			vGlacier
					loop: 			lCoil
					cel: 				4
					posn: 			coilX coil2y
					ignoreActors:
					addToPic:
				)
				(ego
					view:				vEgoClimbingCoils
					cel:				0
					posn:				egoCoilX egoCoil3y
					setCycle:		EndLoop self
				)
			)

			(doCoil3
				(aCoil
					posn:				coilX coil3y
					cel:				0
					setCycle:		EndLoop self
					show:
				)
				(ego
					view:				vEgoAshing
					setCycle:		Forward
				)
			)

			(addCoil3
				(aCoil				hide:)
				((View new:)					;**	addToPic coil #3
					view: 			vGlacier
					loop: 			lCoil
					cel: 				4
					posn: 			coilX coil3y
					ignoreActors:
					addToPic:
				)
				(ego
					view:				vEgoClimbingCoils
					cel:				0
					posn:				egoCoilX egoCoil4y
					setCycle:		EndLoop self
				)
			)

			(doCoil4
				(aCoil
					posn:				coilX coil4y
					cel:				0
					setCycle:		EndLoop self
					show:
				)
				(ego
					view:				vEgoAshing
					setCycle:		Forward
				)
			)

			(addCoil4
				(aCoil				hide:)
				((View new:)					;**	addToPic coil #4
					view: 			vGlacier
					loop: 			lCoil
					cel: 				4
					posn: 			coilX coil4y
					ignoreActors:
					addToPic:
				)
				(ego
					view:				vEgoClimbingCoils
					cel:				0
					posn:				egoCoilX egoTopY
					setCycle:		EndLoop self
				)
			)

			(coilDone
				(Print "You made it!" draw:)
				(Print "(If you keep this up, you're going to be a hero yet,
					Larry!)" LOW)
				(Print "Now, all you have to do is scale the sheer, vertical,
					basaltic cliffs of the volcano,
					reach its crater, and attempt to discover an entrance
					to the evil Dr. Nonookee's \"impenetrable\" fortress
					so you can learn what lies inside.")
					;**	"impenetrable" spelling checked, July 9, 1988
				(ego
					view:				vEgo
					setLoop:			facingNorth
					setCycle:		Walk
					setMotion:		MoveTo egoCoilX 2 self
				)
			)

			(changeRooms
				(NormalEgo)
				(curRoom newRoom: 181)
			)


			;**	This handles writing Larry's name in the snow...

			(startPissing
				(= hasPissed TRUE)
				(ego
					setMotion:		FALSE
				)
				(HandsOff)
				(aYellowName
					show:
					setCycle:		EndLoop self
				)
			)

			(pissDone
				(NormalEgo)
				(aYellowName		hide:)
			)
		)
	)
)

