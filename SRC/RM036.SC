;**
;**	Logics for room 36 -- ship's bridge
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 15, 1988
;**


(script#				36)

(public
	rm36				0
)


(define	nearSwitch 	175 110 202 128)

;(define	vBridgeHench				321)
	(define	lFiringDart		2)
	(define	lDart				3)

;(define	vBridgeInt					322)
	(define	lDials1 			0)
	(define	lDials2 			1)
	(define	lDials3 			2)
	(define	lBoatSwitch		3)
	(define	lHorizonWest	4)
	(define	lHorizonEast	5)
	(define	lDials4 			6)
	(define	lDials5 			7)
	(define	lDials6 			8)
	(define	lWires			9)

;(define	vCaptain						323)
	(define	lCaptainSteering)
	(define	lWheel			1)

;(define	vCaptainDying				324)
	(define	lSalute			0)
	(define	lFall				1)
	(define	lStool			2)


(local
	work
	aDials1
	aDials2
	aDials3
	aDials4
	aDials5
	aDials6
	aLever
	aCaptain
	aWheel
	aHench
	aDart
	aHorizonEast
	aHorizonWest
)


(enum												;**	captainScript state values
	captainInit
	wheelLeft
	waitLeft
	wheelRight
	waitRight
)

(enum												;**	rm036Script values
	intro
	henchAppears
	henchRaisesGun
	henchFires
	captainHit
	waitAsec
	henchGone
	shipSpins
	youAreDead

	capSawLarry
	youDeadAgain
)


(instance rm36 of Room
	(properties
		picture:		36
		south:		31
	)

	(method (init)
		(Load VIEW currentEgoView)
		(Load VIEW vBridgeInt)
		(Load VIEW vCaptain)
		(Load VIEW vCaptainDying)
		(Load VIEW vBridgeHench)

		(super init:)

		((View new:)
			view: 			vBridgeInt
			loop: 			lDials1
			cel: 				0
			posn: 			80 112
			setPri: 			7
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vBridgeInt
			loop: 			lDials1
			cel: 				1
			posn: 			94 107
			setPri: 			7
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vBridgeInt
			loop: 			lDials1
			cel: 				1
			posn: 			82 112
			setPri: 			7
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vBridgeInt
			loop: 			lDials1
			cel: 				1
			posn: 			71 117
			setPri: 			8
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vBridgeInt
			loop: 			lDials1
			cel: 				2
			posn: 			144 85
			setPri: 			6
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vBridgeInt
			loop: 			lDials1
			cel: 				2
			posn: 			152 85
			setPri: 			6
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vBridgeInt
			loop: 			lDials1
			cel: 				2
			posn: 			117 94
			setPri: 			6
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vBridgeInt
			loop: 			lDials1
			cel: 				2
			posn: 			98 101
			setPri: 			6
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vBridgeInt
			loop: 			lWires
			cel: 				0
			posn: 			99 151
			setPri: 			15
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vBridgeInt
			loop: 			lWires
			cel: 				1
			posn: 			229 151
			setPri: 			15
			ignoreActors:
			addToPic:
		)

		((= aDials1 (Prop new:))
			view: 			vBridgeInt
			setLoop: 		lDials2
			cel: 				2
			posn: 			201 93
			setPri: 			6
			setCycle:		Forward
			cycleSpeed:		1
			ignoreActors:
			isExtra:			TRUE
			init:
		)
		((= aDials2 (Prop new:))
			view: 			vBridgeInt
			setLoop: 		lDials3
			cel: 				0
			posn: 			199 85
			setPri: 			6
			setCycle:		Forward
			cycleSpeed:		2
			ignoreActors:
			isExtra:			TRUE
			init:
		)
		((= aDials3 (Prop new:))
			view: 			vBridgeInt
			setLoop: 		lDials5
			cel: 				0
			posn: 			107 99
			setPri: 			8
			setCycle:		Forward
			cycleSpeed:		3
			ignoreActors:
			isExtra:			TRUE
			init:
		)
		((= aDials4 (Prop new:))
			view:				vBridgeInt
			setLoop:			lDials4
			setPri:			6
			posn:				101 105
			setCycle:		Forward
			cycleSpeed:		5
			isExtra:			TRUE
			init:
		)
		((= aDials5 (Prop new:))
			view: 			vBridgeInt
			setLoop: 		lDials5
			cel: 				0
			posn: 			128 91
			setPri: 			6
			setCycle:		Forward
			cycleSpeed:		5
			ignoreActors:
			isExtra:			TRUE
			init:
		)
		((= aDials6 (Prop new:))
			view: 			vBridgeInt
			setLoop: 		lDials6
			cel: 				0
			posn: 			217 102
			setPri: 			8
			setCycle:		Forward
			cycleSpeed:		6
			ignoreActors:
			isExtra:			TRUE
			init:
		)
		((= aHorizonEast (Prop new:))
			view:				vBridgeInt
			setLoop:			lHorizonEast
			setPri:			3
			posn:				181 69
			setCycle:		Forward
			cycleSpeed:		3
			isExtra:			TRUE
			init:
		)
		((= aHorizonWest (Prop new:))
			view:				vBridgeInt
			setLoop:			lHorizonWest
			setPri:			3
			posn:				68 69
			setCycle:		Forward
			cycleSpeed:		3
			isExtra:			TRUE
			init:
		)
		((= aLever (Prop new:))
			view:				vBridgeInt
			setLoop:			lBoatSwitch
			setCel:			(if loweredLifeboats LastCel else 0)
			setPri:			8
			posn:				190 103
			stopUpd:
			init:
		)
		((= aWheel (Prop new:))
			view: 			vCaptain
			setLoop: 		1
			cel: 				0
			posn: 			125 105
			setPri: 			6
			init:
		)

		((= aCaptain (Actor new:))
			view:				vCaptain
			setLoop:			0
			cel:				0
			posn:				140 116
			setPri:			8
			init:
			setScript:		captainScript
		)
		((= aHench (Actor new:))
			view:				vBridgeHench
			setLoop:			facingWest
			setPri:			5
			posn:				192 92			; hench hidden
			ignoreActors:
			illegalBits:	0
			setCycle:		Walk
			init:
			hide:
		)

		(NormalEgo facingNorth)
		(ego
			posn: 			164 152
			init:
		)
		(self
			setRegions:		rgShip
			setScript: 		rm36Script
		)
	)
)


(instance rm36Script of Script
	(method (doit)
		(super doit:)

		(cond
			((& (ego onControl:) cBlue)
				(curRoom newRoom: 31)
			)
			((and	( < state henchAppears)
					( < (ego y?) 111))
				(self changeState: henchAppears)
			)
			((and	( < state capSawLarry)
					( < (ego x?) 155))
				(self changeState: capSawLarry)
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/panel, control')
				(Print "There are many flashing dials, lights, video display
					terminals, etc.
					Mostly, they just confuse you.")
			)

			(if (Said '/dial, terminal, instrument, light, tv')
				(Print "The ship's flashing dials and lights reveal nothing,
					but there is a large switch on the center console,
					directly behind the Captain.")
			)

			(if (Said '/switch, lever, console')
				(Print "There is a large toggle switch labeled
					\"LIFEBOAT TIMER\" on the console directly behind the
					Captain.")
			)

			(if (Said '/man')
				(Print "He's intently steering this ship.
					Don't disturb him.")
			)

			(if (Said '[/bridge, cabin]')
				(Print "You have the distinct feeling that you should not
					be messing around on the bridge of a large, ocean-going
					vessel. So far, the Captain hasn't noticed you.")
				(Print "You'd better keep it that way.")
			)
		)

		(if (Said '/man, captain')
			(Print "No, better not. You're not supposed to be in here,
				and as of now, he hasn't seen you. Leave him alone.")
		)


		(if (Said 'throw, switch, pull/switch')
			(cond
				(loweredLifeboats
					(Print "Try as you might, you are unable to return
						the switch to its previous position.
						It is locked \"ON.\"
						Once a lifeboat drill begins, there is no turning back!")
				)
				((not (ego inRect: nearSwitch))
					(NotClose)
				)
				(else
					(= loweredLifeboats TRUE)
					(theGame changeScore: 8)
					(aLever
						setCycle:	EndLoop
					)
					(Print "Silently, cautiously,
						so as not to attract the Captain's attention,
						you move the large toggle switch to the
						\"START\" position.
						A faint ticking sound begins." draw: BIG)
					(SetRgTimer LIFEBOAT_DRILL 2 10)
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(henchAppears
				(= cycles	0)
				(= seconds	0)
				(HandsOff)
				(= currentStatus NEAR_DEATH)
				(aHench
					show:
					setMotion:		MoveTo 167 92 self
				)
				(captainScript dispose:)
				(ego stopUpd:)
			)

			(henchRaisesGun
				(aHench							;**	hench in doorway
					setLoop:			lFiringDart
					cel:				0
					cycleSpeed:		1
					setCycle:		EndLoop self
				)
			)

			(henchFires
				(aHench
					cycleSpeed:		0
					setCycle:		BegLoop
				)
				((= aDart (Actor new:))
					view:				vBridgeHench
					setLoop:			lDart
					posn:				169 77
					illegalBits:	0
					ignoreActors:
					setStep:			4 4
					init:
					setMotion:		MoveTo 146 95 self
				)
				(Print "Look out, Larry! He's firing a poisoned dart!!"
					HIGH draw:)
			)

			(captainHit
				(aDart				dispose:)
				(aHench
					setLoop:			facingEast
					setCycle:		Walk
					cycleSpeed:		0
					setMotion:		MoveTo 192 92
				)
				(aWheel
					setCycle:		EndLoop
				)
				(aCaptain
					view: 			vCaptainDying
					setLoop: 		lSalute
					setCel: 			0
					posn: 			140 116
					setPri: 			8
					ignoreActors:
					illegalBits:	0
					cycleSpeed:		1
					setCycle:		EndLoop self
				)
			)

			(waitAsec
				((View new:)
					view: 			vCaptainDying
					ignoreActors:
					setLoop: 		lStool
					posn: 			140 116
					setPri: 			7
					addToPic:
				)
				(aCaptain
					view: 			vCaptainDying
					setLoop: 		lFall
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(henchGone
				(Print "At least he missed you.
					However, the Captain lies dying on the deck,
					and the ship is drifting helplessly on the high seas!")
				(aCaptain		stopUpd:)
				(aWheel			stopUpd:)
				(= seconds 3)
			)

			(shipSpins
				(Print "Through your ineptitude
					you've allowed your Captain
					to be killed by a KGB poisoned dart.
					Unbeknown to the passengers and crew
					the ship now charts its own course, a course (of course)
					that courses straight for the nearest reef.")
				(Print "Perhaps you'll miss the reef and miss the grief;
					save the ship and save the day;
					perhaps you'll even escape safely!")
				(aHench		dispose:)
				(= seconds 3)
			)

			(youAreDead
				(ShakeScreen 8 (Random 1 3))
				(Print "Nah, no way!!")
				(Print "You knew better than that!")
				(= currentStatus DEAD)
			)


			;**	We get here if Larry walked within Captain's peripheral vision,
			;**	defined as left of 163x

			(capSawLarry
				(= cycles	0)
				(= seconds	0)
				(= currentStatus NEAR_DEATH)
				(HandsOff)
				(ego stopUpd:)
				(Print "\"Hold it right there, buddy!\" says the Captain.
					\"No one's allowed on MY bridge without permission!\"" draw:)
				(= seconds 3)
			)

			(youDeadAgain
				(ego hide:)
				(Print "The security guards take you away to the brig,
					which is located on a deck even lower than your cabin!
					The conditions there aren't that bad, although seepage
					does keep the floor covered with oily, slimy water.
					Unfortunately, during one particularly wet dream,
					you slide off your bunk and drown in bilge water." draw:)
				(= currentStatus DEAD)
			)
		)
	)
)


(instance captainScript of Script
	(method (changeState newState &tmp tmpSpeed tmpDelay)
		(switch (= state newState)

			(captainInit
				(= cycles (Random 5 10))
			)

			(wheelLeft
				(= tmpSpeed (Random 0 3))
				(aCaptain
					cycleSpeed:	tmpSpeed
					setCycle:	EndLoop
				)
				(aWheel
					cycleSpeed:	tmpSpeed
					setCycle:	EndLoop
				)
				(= cycles (Random 5 10))
			)

			(waitLeft
				(aCaptain 		setCel:)
				(aWheel			setCel:)
				(= cycles (Random 5 10))
			)

			(wheelRight
				(= tmpSpeed (Random 0 3))
				(aCaptain
					cycleSpeed:	tmpSpeed
					setCycle:	BegLoop
				)
				(aWheel
					cycleSpeed:	tmpSpeed
					setCycle:	BegLoop
				)
				(= cycles (Random 5 10))
			)

			(waitRight
				(aCaptain 		setCel:)
				(aWheel			setCel:)
				(self changeState: captainInit)
			)
		)
	)
)

