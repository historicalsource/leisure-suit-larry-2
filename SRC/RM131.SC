;**
;**	Logics for room 131 -- ship map, Larry escapes & sails away in lifeboat
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 25, 1988
;**


(script#				131)

(public
	rm131				0
)


;(define	vShipMap					300)
	(define	lBar				0)
	(define	lFlag				1)
	(define	lWake				2)
	(define	lClouds			3)				;**	cels 0-8
	(define	lFlagLimp		4)

;(define	vLifeboatsLowering	301)
	(define	lOneBoat			0)
	(define	lEgoRope			1)
	(define	lEgoBoat			2)


(local
	aBoat0
	aBoat1
	aBoat2
	aBoat3
	aBoat4
	aBoat5
	aEgoBoat
)


(enum
	intro
	deadInWater
	justMadeIt
	lowerBoats
	boatsDown
	egoBoatAway
	egoBoatOffScreen
)


(instance rm131 of Room
	(properties
		picture:		31
		horizon:		5
	)

	(method (init)
		(Load VIEW vShipMap)
		(Load VIEW vLifeboatsLowering)

		(super init:)

		((View new:)							; non-revolvingBar
			view:				vShipMap
			loop:				lBar
			priority:		15
			posn:				216 45
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	limp flag
			view:				vShipMap
			loop:				lFlagLimp
			priority:		15
			posn:				99 23
			ignoreActors:
			addToPic:
		)

		((= aBoat0 (Prop new:))
			view: 			vLifeboatsLowering
			setLoop:			lOneBoat
			setPri:			14
			posn:				159 76
			cycleSpeed:		5
			init:
		)
		((= aBoat1 (Prop new:))
			view: 			vLifeboatsLowering
			setLoop:			lOneBoat
			setPri:			14
			posn:				199 76
			cycleSpeed:		5
			init:
		)
		((= aBoat2 (Prop new:))
			view: 			vLifeboatsLowering
			setLoop:			lOneBoat
			setPri:			14
			posn:				217 76
			cycleSpeed:		5
			init:
		)
		((= aBoat3 (Prop new:))
			view: 			vLifeboatsLowering
			setLoop:			lOneBoat
			setPri:			14
			posn:				234 76
			cycleSpeed:		5
			init:
		)
		((= aBoat4 (Prop new:))
			view: 			vLifeboatsLowering
			setLoop:			lOneBoat
			setPri:			14
			posn:				251 76
			cycleSpeed:		5
			init:
		)
		((= aBoat5 (Prop new:))
			view: 			vLifeboatsLowering
			setLoop:			lOneBoat
			setPri:			14
			posn:				268 76
			cycleSpeed:		5
			init:
		)

		((= aEgoBoat (Actor new:))
			view: 			vLifeboatsLowering
			setLoop:			lEgoBoat
			setPri:			14
			setCycle:		Forward
			illegalBits:	0
			posn:				160 1133
			setStep:			1 1
			cycleSpeed:		1
			moveSpeed:		1
			init:
		)

		(ego
			posn:				123 999			;**	out of the way
		)
		(HandsOff)
		(= currentStatus LIFEBOATING)
		(self
			setRegions:		rgShip
			setScript:		rm131Script
		)
	)
)


(instance rm131Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (and	(ego has: iWig)
					(Said '(put<on), use, wear/wig'))
			(Print "Good idea. This would be a perfect time!")
			(if (not wearingWig)
				(theGame changeScore: 5)
				(= wearingWig TRUE)
			)
		)

		(if (and (ego has: iSpinachDip)
					(or	(Said '/dip<(overboard, (board<over))')
							(Said 'discard, drop, throw, dump/dip')))
			(theGame changeScore: 2)
			(Print "A good idea.")
			(Print "You toss the spinach dip as far over the ocean as you can.")
			(Print "(Which is not really that far.)" LOW)
			(ego put: iSpinachDip LIMBO)
		)

		(if (Said 'look>')
			(if (Said '/ship')
				(Print "It's getting smaller!")
			)

			(if (Said '[/boat, room]')
				(Print "This is not what you were expecting from a deluxe cruise!")
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(= seconds 2)
			)

			(deadInWater
				(Print "The lifeboat drill has brought the ship to a standstill.
					What have you done, Larry?")
				(= seconds 5)
			)

			(justMadeIt
				(Print "A siren wails a warning cry,
					and the lifeboat begins to shake.
					You made it to the lifeboat in the nick of time!")
					(theGame changeScore: 5)
				(= seconds 3)
			)

			(lowerBoats
				(aBoat0	setCycle:	EndLoop self)
				(aBoat1	setCycle:	EndLoop)
				(aBoat2	setCycle:	EndLoop)
				(aBoat3	setCycle:	EndLoop)
				(aBoat4	setCycle:	EndLoop)
				(aBoat5	setCycle:	EndLoop)
			)

			(boatsDown
				(aBoat1	stopUpd:)
				(aBoat2	stopUpd:)
				(aBoat3	stopUpd:)
				(aBoat4	stopUpd:)
				(aBoat5	stopUpd:)
				(aBoat0
					setLoop:			lEgoRope
					stopUpd:
				)
				(User
					canControl: 	FALSE
					canInput: 		TRUE
				)
				(aEgoBoat
					posn:				160 133
					setMotion:		MoveTo 123 146 self
				)
			)

			(egoBoatAway
				(Print "Slowly, you drift away from the cruise ship.
					You wonder what you forgot to pack.")
				(aEgoBoat
					setMotion:		MoveTo -12 201 self
				)
				(User canInput: 	TRUE)
			)

			(egoBoatOffScreen
				(curRoom newRoom: 138)
			)
		)
	)
)

