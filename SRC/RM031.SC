;**
;**	Logics for room 31 -- ship map
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 7, 1988
;**


(script#				31)

(public
	rm31				0
)


(define	rmLarry			32)
(define	rmPool			34)
(define	rmBar				35)
(define	rmBridge			36)
(define	rmBarber			37)
(define	rmLifeboat		38)

;(define	vEgoShipMapSwimsuit		129)
;(define	vEgoShipMap					130)

;(define	vShipMap						300)	;**	pertinent views
	(define	lBar				0)
	(define	lFlag				1)
	(define	lWakeRear		2)
	(define	lWakeFront		3)
	(define	lFlagLimp		4)
	(define	lHorizonBow		5)
	(define	lHorizonStern	6)

;(define	vLifeboatsLowering		301)
	(define	lOneBoat			0)
	(define	lEgoRope			1)
	(define	lEgoBoat			2)

;(define	vClouds						620)	;**	Loop0 contains Cels0-10


(local
	aBar
	aFlag
	aWakeFront
	aWakeRear
	aHorizonBow
	aHorizonStern
	numClouds
	seenMessage
)


(enum												;**	cloudScript state values
	cloudInit
	repoCloud
	moveCloud
)


(instance rm31 of Room
	(properties
		picture:		31
		horizon:		1
	)

	(method (init &tmp i cloud)
		(Load VIEW vShipMap)
		(Load VIEW vEgoShipMap)
		(Load VIEW vLifeboatsLowering)
		(Load VIEW vClouds)

		(super init:)

		((View new:)
			view: 			vLifeboatsLowering
			setLoop:			lOneBoat
			setCel:			0
			setPri:			14
			posn:				159 76
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vLifeboatsLowering
			setLoop:			lOneBoat
			setCel:			0
			setPri:			14
			posn:				199 76
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vLifeboatsLowering
			setLoop:			lOneBoat
			setCel:			0
			setPri:			14
			posn:				217 76
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vLifeboatsLowering
			setLoop:			lOneBoat
			setCel:			0
			setPri:			14
			posn:				234 76
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vLifeboatsLowering
			setLoop:			lOneBoat
			setCel:			0
			setPri:			14
			posn:				251 76
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vLifeboatsLowering
			setLoop:			lOneBoat
			setCel:			0
			setPri:			14
			posn:				268 76
			ignoreActors:
			addToPic:
		)

		;**	The following props are all unnecessary (isExtra: TRUE),
		((= aHorizonBow (Prop new:))
			view:				vShipMap
			setLoop:			lHorizonBow
			setPri:			3
			posn:				8 103
			setCycle:		Forward
			cycleSpeed:		2
			isExtra:			TRUE
			init:
		)
		((= aHorizonStern (Prop new:))
			view:				vShipMap
			setLoop:			lHorizonStern
			setPri:			3
			posn:				317 103
			setCycle:		Forward
			cycleSpeed:		2
			isExtra:			TRUE
			init:
		)
		((= aBar (Prop new:))				; revolvingBar
			view:				vShipMap
			setLoop:			lBar
			setCel:			0
			setPri:			15
			posn:				216 45
			setCycle:		Forward
			cycleSpeed:		30
			isExtra:			TRUE
			init:
		)
		((= aFlag (Prop new:))
			view:				vShipMap
			setLoop:			lFlag
			setPri:			1
			posn:				99 23
			setCycle:		Forward
			cycleSpeed:		3
			isExtra:			TRUE
			init:
		)

		((= aWakeRear (Prop new:))
			view:				vShipMap
			setLoop:			lWakeRear
			setPri:			10
			posn:				267 119
			setCycle:		Forward
			cycleSpeed:		3
			isExtra:			TRUE
			init:
		)
		((= aWakeFront (Prop new:))
			view:				vShipMap
			setLoop:			lWakeFront
			setPri:			10
			posn:				108 120
			setCycle:		Forward
			cycleSpeed:		4
			isExtra:			TRUE
			init:
		)

		(cond
			(( > machineSpeed 60)	(= numClouds 3))
			(( > machineSpeed 40)	(= numClouds 2))
			(( > machineSpeed 20)	(= numClouds 1))
		)	
		(for ((= i 0)) ( < i numClouds) (( ++ i))	;** Create clouds 1 through ??
			((Actor new:)
				view:				vClouds
				ignoreHorizon:
				ignoreActors:
				illegalBits:	0
				setScript:		(cloudScript new:)
			)
		)

		(= currentEgoView (ego view?))	;**	save old view
		(if (== currentEgoView vEgoSwimsuit)
			(ego	view:		vEgoShipMapSwimsuit)
		else
			(ego	view:		vEgoShipMap)
		)
		(ego
			setLoop:			0
			setPri:			14
			setCycle:		Walk
			setStep:			1 1
			illegalBits:	cLtWhite
		)
		(cond
			((== prevRoomNum rmLifeboat)	(ego posn: 179	74))		;**	lifeboat
			((== prevRoomNum rmBarber)		(ego posn:	96	86))		;**	barber
			((== prevRoomNum rmBridge)		(ego posn: 111	63))		;**	bridge
			((== prevRoomNum rmBar)			(ego posn: 215	51))		;**	bar
			((== prevRoomNum rmPool)		(ego posn: 299	86))		;**	pool
			(else									(ego posn: 280 114))		;**	Larry
		)
		(ego init:)
		(User
			canControl:		TRUE
			canInput:		TRUE
		)
		(= currentStatus NORMAL)

		(self
			setRegions:		rgShip
			setScript: 		rm31Script
		)
	)
)


(instance rm31Script of Script
	(method (doit)
		(super doit:)

		(cond
			((and (== currentEgoView vEgoSwimsuit)
					;**	cMagenta	cBrown cWhite
					(& (ego onControl:) %0000000011100000))
				(if (== seenMessage FALSE)
					(Print "Swimwear is not allowed there. Try the pool instead.")
					(= seenMessage TRUE)
				)
			)
			((& (ego onControl:) cGray)		(curRoom newRoom: rmLifeboat))
			((& (ego onControl:) cWhite)		(curRoom newRoom: rmBarber))
			((& (ego onControl:) cBrown)		(curRoom newRoom: rmBridge))
			((& (ego onControl:) cMagenta)	(curRoom newRoom: rmBar))
			((& (ego onControl:) cRed)			(curRoom newRoom: rmPool))
			((& (ego onControl:) cBlue)		(curRoom newRoom: rmLarry))
			(else
				(= seenMessage FALSE)
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'climb/stair')
			(Print "I suggest using the cursor keys for that!")
		)

		(if (or	(Said 'open/door')
					(Said 'enter, (go<to), find/cabin, room'))
			(Print "Your cabin is all the way to the rear of the ship,
				on the lowest level... remember?")
		)

		(if (Said 'look>')
			(if (Said '/hall')
				(Print "The hallways aren't that large; it's you that's small!")
			)

			(if (Said '/stair')
				(Print "They are perfect for moving between decks.")
			)

			(if (Said '/flag')
				(Print "\"Oh-ho, say, can you sea?\"")
			)

			(if (Said '[/ship, boat, cloud, room]')
				(Print "Scud scuds past the ship,
					messing up an otherwise clear blue sky.")
			)
		)
	)
)


(instance cloudScript of Script
	(method (changeState newState &tmp tmpY tmpCel)
		(switch (= state newState)

			(cloudInit
				(client
					posn: 		(Random 0 270) (Random 10 65)
					init:
				)
				(self changeState: moveCloud)
			)

			(repoCloud
				(= tmpY			(Random 1 80))
				(= tmpCel		(Random 0 10))
				(client
					setCel:		tmpCel
					posn: 		(- 1 (CelWide vClouds 0 tmpCel)) tmpY
				)
				(self changeState: moveCloud)
			)

			(moveCloud
				(client
					setStep:		(Random 1 3) -1
					setMotion:	MoveTo 321 (client y?) self
				)
				(= state cloudInit)
			)
		)
	)
)

