;**
;**	Logics for room 42 -- On The Beach
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 20, 1988
;**


(script#				42)

(public
	rm42				0
)


;(define	vEgoWashingOntoBeach		144)

;(define	vBeach						406)
;(define	vBeachSports				407)
	(define	lParasail			0)
	(define	lSailboardEast		1)
	(define	lTractor				2)
	(define	lSailboardWest1	3)
	(define	lBanana				4)
	(define	lSailboardWest2	5)
	(define	lWaterSki			6)
	(define	lUmbrellas			7)

;(define	vBeachStud					408)
;(define	vBeachStudKickingSand	409)


(local
	aSport
	aCreep
)


(enum												;**	waterSports script values
	sportsInit
	sport1Start
	sport1Done
	sport2Start
	sport2Done
	sport3Start
	sport3Done
	sport4Start
	sport4Done
	sport5Start
	sport5Done
	sport6Start
	sport6Done
	sport7Start
	sport7Done
)

(enum												;**	rmScript state values
	intro

	driftingAtSea
	it'sYou
	washingAshore
	doLoop1
	doLoop2
	doLoop3
	waitAsec

	hereComesCreep
	helpIsHere
	kickSand
	hereGoesCreep
	whatAnAsshole

	doLoop4
	delaySomeMore
	dustOff
	egoAshore
	polyester
	whereAmI
)


(instance rm42 of Room
	(properties
		picture:		42
		horizon:		5
		east:			47
		west:			41
		south:		40
	)

	(method (init)
		(Load VIEW vBeachSports)

		(super init:)

		((View new:)							;**	west umbrella
			view: 			vBeachSports
			loop: 			lUmbrellas
			cel: 				1
			posn: 			76 173
			setPri: 			13
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	center umbrella
			view: 			vBeachSports
			loop: 			lUmbrellas
			cel: 				0
			posn: 			206 160
			setPri: 			12
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	east umbrella
			view: 			vBeachSports
			loop: 			lUmbrellas
			cel: 				2
			posn: 			294 175
			setPri: 			13
			ignoreActors:
			addToPic:
		)

		(self
			setRegions:		rgResortBeach
			setScript: 		rm42Script
		)

		(if (or	(== prevRoomNum 138)
					(== prevRoomNum 10))
			(Load VIEW vEgoWashingOntoBeach)
			(Load VIEW vBeachStud)
			(Load VIEW vBeachStudKickingSand)
			(ego
				view:				vEgoWashingOntoBeach
				setLoop:			0
				illegalBits:	0
				ignoreActors:
				cycleSpeed:		1
				moveSpeed:		1
				setStep:			1 1
				posn:				-8 55
				setCycle:		Forward
				init:
				put: iSwimsuit		LIMBO
				put: iSunscreen	LIMBO
				put: iWig			LIMBO
				put: iSewingKit	LIMBO
				put: iFruit 		LIMBO
				;**	This is all he gets to keep:
				;**		iMoney
				;**		iPassport
				;**		iOnklunk
				;**		iBikiniTop
			)
			((= aCreep (Actor new:))
				view:				vBeachStud
				ignoreActors:
				illegalBits:	0
				setCycle:		Walk
				posn:				-12 150
				init:
			)
			(HandsOff)
			(rm42Script changeState: driftingAtSea)
			(= currentStatus LIFEBOATING)
			(curRoom west: 0)
		else
			((= aSport (Actor new:))
				view:				vBeachSports
				setCycle:		Forward
				setPri:			1
				posn:				-40 80
				illegalBits:	0
				ignoreActors:
				init:
				hide:
				setScript:		sportsScript
			)
			(if (or	(== prevRoomNum 0)
						(== prevRoomNum 40))
				(ego posn: 155 186)
			)
			(NormalEgo)
			(ego					init:)
		)
	)
)


(instance rm42Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/umbrella')
				(Print "It looks as though they are all unoccupied.")
			)

			(if (Said '[/room, tree, bush]')
				(Print "You are on the beach of a beautiful resort hotel,
					a tropical paradise for the rich and famous.
					In the distance you see people enjoying water
					sports of all kinds.")
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(intro)


			;**	This is the script that makes him wash ashore from lifeboat.

			(driftingAtSea
				(ego
					setMotion:	MoveTo 3 61 self
				)
			)

			(it'sYou
				(Print "Hey, Larry! That's you out there in the surf!")
				(Print "\"LAND HO!!\" you cry.")
				(= seconds 3)
			)

			(washingAshore
				(ego
					setMotion:	MoveTo 70 95 self
				)
			)

			(doLoop1
				(ego
					cycleSpeed:	2
					setLoop:		1
					cel:			0
					posn:			72 97
					setStep:		3 2
					setCycle:	EndLoop self
				)
			)

			(doLoop2
				(ego
					setLoop: 	2
					posn: 		84 112
					cel: 			0
					setCycle:	EndLoop self
				)
			)

			(doLoop3
				(ego
					cycleSpeed:	1
					setLoop: 	3
					posn:			92 125
					cel: 			0
					setCycle:	EndLoop self
				)
			)

			(waitAsec
				(= seconds 5)
			)

			(hereComesCreep
				(aCreep
					setMotion:	MoveTo 84 150 self
				)
				(= cycles 15)
			)

			(helpIsHere
				(Print "Oh, good; here comes someone to help you.")
			)

			(kickSand
				(aCreep
					view:			vBeachStudKickingSand
					cel:			0
					setCycle:	Forward
				)
				(= cycles 18)
			)

			(hereGoesCreep
				(aCreep
					view:			vBeachStud
					setCycle:	Walk
					setMotion:	MoveTo 333 120 self
				)
				(= cycles 25)
			)

			(whatAnAsshole
				(if ( > filthLevel 10)
					(Print "(Asshole!)" LOW)
				)
			)

			(doLoop4
				(aCreep dispose:)
				(Print "Congratulations, Larry!
					You have survived weeks adrift on the high seas with nothing
					but your courage, perseverance and a few humble provisions;
					endured tropical storms, vicious winds and high seas
					in just a tiny lifeboat;
					prevailed over the surf, off-shore barrier reef,
					and razor-sharp coral;
					escaped the dangerous Mother;
					avoided the vicious KGB and the tempting enticements of
					the evil Dr. Nonookee's henchette hordes,
					and withstood that creep's audacious humor..." BIG draw:)
				(ego
					cycleSpeed:	1
					setLoop:		4
					cel:			0
					posn:			101 151
					setCycle:	EndLoop self
				)
			)

			(delaySomeMore
				(Print "...and you're still as good as new!" HIGH draw:)
				(Print "(But boy, is your suit a mess!!)" LOW)
				(= seconds 3)
			)

			(dustOff
				(ego
					setLoop: 	5
					cel: 			0
					setCycle:	EndLoop self
				)
			)

			(egoAshore
				(= seconds 3)
			)

			(polyester
				(Print "(Don't you just love a good polyester?)" LOW)
				(= seconds 3)
			)

			(whereAmI
				(Print "But now you wonder, \"Where am I?\"")
				(Print "And what possessions survived your disastrous journey?")
				(NormalEgo facingSouth)
				(rm42 west: 41)
			)
		)
	)
)


(instance sportsScript of Script
	(method (changeState newState &tmp sportsX sportsY)
		(switch (= state newState)

			(sportsInit
				(if (!= prevRoomNum 138)
					(= seconds (Random 3 5))
				)
			)

			(sport1Start
				(aSport
					setLoop: 		lParasail
					cel: 				0
					posn: 			-30 50
					setStep:			5 2
					cycleSpeed:		3
					show:
					setMotion:		MoveTo 360 50 self
				)
			)

			(sport1Done
				(aSport
					hide:
				)
				(= seconds (Random 2 5))
			)

			(sport2Start
				(aSport
					setLoop: 		lSailboardEast
					posn: 			-24 108
					setStep:			3 1
					cycleSpeed:		4
					show:
					setMotion:		MoveTo 343 64 self
				)
			)

			(sport2Done
				(aSport
					hide:
				)
				(= seconds (Random 2 5))
			)

			(sport3Start
				(aSport
					setLoop: 		lTractor
					posn: 			-29 58
					setStep:			1 1
					cycleSpeed:		0
					moveSpeed:		1
					show:
					setMotion:		MoveTo 337 58 self
				)
			)

			(sport3Done
				(aSport
					hide:
				)
				(= seconds (Random 2 5))
			)

			(sport4Start
				(aSport
					setLoop: 		lSailboardWest1
					posn: 			-10 48
					setStep:			3 1
					cycleSpeed:		4
					moveSpeed:		0
					show:
					setMotion:		MoveTo 333 48 self
				)
			)

			(sport4Done
				(aSport
					hide:
				)
				(= seconds (Random 2 5))
			)

			(sport5Start
				(aSport
					setLoop: 		lBanana
					posn: 			-35 80
					setStep:			2 1
					cycleSpeed:		1
					show:
					setMotion:		MoveTo 334 55 self
				)
			)

			(sport5Done
				(aSport
					hide:
				)
				(= seconds (Random 2 5))
			)

			(sport6Start
				(aSport
					setLoop: 		lSailboardWest2
					posn: 			358 77
					setStep:			3 1
					cycleSpeed:		4
					show:
					setMotion:		MoveTo -30 113 self
				)
			)

			(sport6Done
				(aSport
					hide:
				)
				(= seconds (Random 2 5))
			)

			(sport7Start
				(aSport
					setLoop: 		lWaterSki
					posn: 			-23 74
					setStep:			5 2
					cycleSpeed:		3
					show:
					setMotion:		MoveTo 377 48 self
				)
			)

			(sport7Done
				(aSport
					hide:
				)
				(self changeState: sportsInit)
			)
		)
	)
)

