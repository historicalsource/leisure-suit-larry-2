;**
;**	Logics for room 77 -- native village central
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	September 29, 1988
;**


(script#				77)

(public
	rm77				0
)


(define	nearAshes			144 126 186 148)

;(define	vCampfire					708)
	(define	lFire				0)
	(define	lAshes			1)

;(define	vVillagersWorking			709)
	(define	lLadyAtFire		0)
	(define	lArmTurning		1)
	(define	lSpit				2)
	(define	lBasketEast		3)
	(define	lBasketDown		4)
	(define	lWomanWest		5)
	(define	lBasket			6)
	(define	lWeaver			7)

(local
	work
	aCampfire
	aArm
	aKalalau
	aBearer
	aWeaver
)


(enum
	intro

	letsParty
	fetchTheKids
	heresLarry
	basketDown
	standUp
	walkBack
	bearerGone

	iWillBaby
	letsGo
	nextRoom

	fromChopper
	I'llGetKen
	heyYou
	offToMarry
)


(instance rm77 of Room
	(properties
		picture:		77
		horizon:		50
		south:		75
		east:			78
		west:			76
	)

	(method (init)
		(super init:)

		(NormalEgo)
		(self setScript: 	rm77Script)

		(Load VIEW vCampfire)

		(if forceAtest
			(= islandStatus wedding)
		)

		(cond
			((== islandStatus getDaddy)
				(= islandStatus wedding)
				(= currentStatus CLOSING_CARTOON)
				(rm77Script changeState: fromChopper)

				(Load VIEW vKalalau)
				((View new:)
					view:				vCampfire
					setLoop:			lAshes
					posn:				165 143
					addToPic:
				)
				((= aKalalau (Actor new:))
					view:				vKalalau
					ignoreActors:
					illegalBits:	0
					loop:				facingNorth
					posn:				145 184
					setCycle:		Walk
					init:
				)
				(HandsOff)
			)

			((== islandStatus metGirl)
				(= islandStatus initiation)
				(= currentStatus INITIATION)
				(rm77Script changeState: letsParty)

				(Load VIEW vVillagersWorking)
				((= aCampfire (Prop new:))
					view:				vCampfire
					setLoop:			lFire
					setPri:			10
					posn:				165 143
					setCycle:		Forward
					init:
				)
				((View new:)							;**	woman
					view: 			vVillagersWorking
					loop: 			lLadyAtFire
					posn: 			124 138
					addToPic:
				)
				((View new:)							;**	spit
					view: 			vVillagersWorking
					ignoreActors:
					loop: 			lSpit
					posn: 			160 136
					setPri: 			(CoordPri 138)
					addToPic:
				)
				((= aArm (Extra new:))
					view: 			vVillagersWorking
					ignoreActors:
					loop: 			lArmTurning
					posn: 			118 124
					setPri: 			(CoordPri 138)
					cycleSpeed:		5
					minPause:		2
					maxPause:		10
					isExtra:			TRUE
					init:
				)
				((= aWeaver (Extra new:))
					view:				vVillagersWorking
					loop: 			lWeaver
					posn: 			153 108
					cycleSpeed:		1
					init:
				)
				((= aBearer (Actor new:))
					view:				vVillagersWorking
					ignoreActors:
					illegalBits:	0
					setLoop:			lBasketEast
					setCycle:		Walk
					posn:				68 145
					init:
				)
				((= aKalalau (Actor new:))
					view:				vKalalau
					ignoreActors:
					illegalBits:	0
					loop:				facingNorth
					posn:				145 184
					setCycle:		Walk
					init:
				)
				(HandsOff)
			)

			(else
				((View new:)
					view:				vCampfire
					setLoop:			lAshes
					posn:				165 143
					addToPic:
				)
				(self setRegions:	rgIsland)
			)
		)

		(cond
			((== prevRoomNum	76)			(ego posn:	3 144))
			((== prevRoomNum	78)			(ego posn: 318 135))
			(else
				(ego
					loop:		facingNorth
					posn: 	165 188
				)
			)
		)
		(ego		init:)
	)
)


(instance rm77Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/fire, spit')
				(Print "The village campfire has burned down to just a few
					cool ashes.")
			)

			(if (Said '/ash')
				(if ((inventory at: iAshes) ownedBy: curRoomNum)
					(Print "Some ashes might come in handy later in the game.")
				else
					(Print "There are only a few left; not enough to worry about!")
				)
			)

			(if (Said '/door')
				(Print "All the doors here are closed.
					The natives enjoy their privacy.
					Leave them alone.")
			)

			(if (Said '[/room, step, village, hut]')
				(if ((inventory at: iAshes) ownedBy: curRoomNum)
					(Print "The campfire made lots of ashes.")
				else
					(Print "The huts are filled with the joy of aerobics.
						Don't disturb them.")
				)
			)
		)

		(if (or	(Said '(go<in), (walk<in), climb, enter/stair, hut')
					(Said 'look/window')
					(Said 'knock, open/door'))
			(Print "It would be better if you stayed out.")
		)

		(if (Said 'get/ash')
			(cond
				((!= currentStatus NORMAL)
					(NotNow)
				)

				((not ((inventory at: iAshes) ownedBy: curRoomNum))
					(AlreadyTook)
				)

				((not (ego inRect: nearAshes))
					(NotClose)
				)

				(else
					(ego get: iAshes)
					(theGame changeScore: 6)
					(Print "You bend over the firepit and scoop up huge handfuls
						of ashes, 
						completely filling all of your pockets with them!")
				)
			)
		)

	)


	(method (changeState newState)
		(switch (= state newState)

			;**	(intro)

			(letsParty
				(= cycles 3)
			)

			(fetchTheKids
				(aBearer
					setMotion:		MoveTo 123 145 self
				)
				(= cycles 15)
			)

			(heresLarry
				(Print "\"Attention, please!\" cries Kalalau.
					\"Gather all the villagers before the Chief's hut.
					We must have a celebration.\"")
			)

			(basketDown
				(aBearer
					setLoop:			lBasketDown
					cel:				0
					setCycle:		CycleTo 7 FORWARD self
				)
			)

			(standUp
				((View new:)					;**	add basket to pic
					view: 			vVillagersWorking
					ignoreActors:
					loop: 			lBasket
					posn: 			137 145
					addToPic:
				)
				(aBearer
					setCycle:		EndLoop self
				)
			)

			(walkBack
				(aBearer
					setLoop:			lWomanWest
					setCycle:		Walk
					setMotion:		MoveTo -33 145 self
				)
				(Print "\"Wait here while I tell everyone,\" says the
					basket carrier." draw:)
			)

			(bearerGone
				(Print "\"Oh, Larry, I so hope you'll be able to convince
					my father, the Chief, that we should be married,\"
					says Kalalau.
					\"Sometimes Daddy can have a strange sense of humor!\"")
				(= seconds 3)
			)

			(iWillBaby
				(Print "\"Don't worry, baby,\" you reassure her. \"For you
					I would cross the burning sands of the Sahara.
					I would climb the highest peaks of the Himalayas.
					I would swim the shark-infested waters of the ocean.
					Why, I'd even learn desktop publishing software!\"")
				(= seconds 3)
			)

			(letsGo
				(Print "\"My hero!\"")
				(Print "\"Come on, let's go meet Daddy!\"")
				(aKalalau
					setMotion:	MoveTo 4 148 self
				)
				(ego
					setMotion:	MoveTo 4 138
				)
			)

			(nextRoom
				(curRoom newRoom: 76)
			)


			;**	This is the path if Larry has slain the evil Doctor...


			(fromChopper
				(= seconds 3)
			)

			(I'llGetKen
				(Print "\"I'll go get Daddy; you notify the village.
					Have everyone assemble before the witch doctor's hut!
					Oh, Larry; I'm so excited!!\"")
				(Print "\"Just think; I'll be Mrs. Larry; Mrs. Larry Laffer!\"")
				(aKalalau
					setMotion:		MoveTo -5 132 self
				)
			)

			(heyYou
				(Print "\"Friends, natives, countrymen: lend me your ears.\"")
				(Print "(You're off to an original start!)" LOW)
				(Print "\"People of Nontoonyt,
					the evil Doctor Nonookee is gone forever,
					your women have been freed and are returning here now,
					and you are again free to travel wherever you wish!
					Since I have met all the conditions of Chief Keneewauwau,
					Kalalau and I will be married!
					Please assemble for the wedding!!\"" WIDE)
				(Print "(You hear the muffled sounds of cheers.)" LOW)
				(curRoom east:	0)
				(ego
					setMotion:		MoveTo 345 124 self
				)
			)

			(offToMarry
				(Print "Later that day..." HIGH)
				(curRoom newRoom: 78)
			)
		)
	)
)

