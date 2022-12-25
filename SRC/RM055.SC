;**
;**	Logics for room 55 -- Snack Bar
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 25, 1988
;**


(script#				55)

(public
	rm55				0
)


(define	nearCandy		272 178 287 186)
(define	nearParachute	248 150 275 172)
(define	nearCounter		35 181 151 189)
(define	waitressY		175)
(define	waitressLeftX	-6)
(define	waitressRightX	153)

;(define	vCafe							519)
	(define	lLine				1)
	(define	lDoor				2)
	(define	lParachute		3)

;(define	vWaitress					520)
	(define	lTurnEast		2)
	(define	lTurnWest		3)
	(define	lGrabAss			4)
	(define	lScratchAss		5)
	(define	lGrabHair		6)
	(define	lScratchHair	7)


(local
	waitressX
	aWaitress
	aSidewalk
	aPlane
	aDoor
	aParachute
	aPlate
	aSidewalkNorth
	aSidewalkSouth
	orderedSpecial
	seenMessage
)


(enum	1											;**	"orderedSpecial" values
	ORDERED
	DELIVERED
	PIN_GONE
)

(enum												;**	waitressScript state values
	waitressInit
	waitressTurn
	waitressStart
	waitressArrive
	faceCustomers
	waitressReach
	waitressScratch
	scratchDone
	waitAwhile
	waitressDone

	getPlate
	comeBack
	plateAtEgo
)

(enum												;**	rmScript state values
	intro

	comingHome
	offBeltway
	startSliding
	slideDone
	startChute
	startChuteDoor
	doorDone
	chuteDone
	waitAsec
	punchLine
	gimmeAspecial
	bluePateToYou
	yo
	specialDone
	ateSpecial
	UBdying
)


(instance rm55 of Room
	(properties
		picture:		55
		horizon:		5
		west:			54
	)

	(method (init)
		(Load VIEW vAirplane)
		(Load VIEW vCafe)
		(Load VIEW vWaitress)

		(super init:)

		((View new:)							;**	vendingMachine
			view: 			vCafe
			loop: 			0
			cel: 				1
			posn: 			279 167
			setPri: 			ltCyan
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	otherVendingMachine
			view: 			vCafe
			loop: 			0
			cel: 				2
			posn: 			301 188
			setPri: 			ltCyan
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	specialSign
			view: 			vCafe
			loop: 			0
			cel: 				0
			posn: 			19 161
			setPri: 			12
			ignoreActors:
			addToPic:
		)

		((= aPlate (View new:))
			view: 			vCafe
			loop: 			0
			cel: 				3
			posn: 			999 999
			setPri: 			15
			ignoreActors:
			init:
		)

		((= aDoor (Prop new:))
			view:				vCafe
			setLoop:			lDoor
			setCel:			0
			setPri:			ltCyan
			posn:				257 128
			cycleSpeed:		1
			ignoreActors:
			stopUpd:
			init:
		)
		((= aParachute (Prop new:))
			view:				vCafe
			setLoop:			lParachute
			setCel:			0
			setPri:			ltCyan
			posn:				264 1147
			cycleSpeed:		1
			setCycle:		Forward
			ignoreActors:
			init:
		)

		((= aPlane (Airplane new:))
			view:				vAirplane
			setCel:			0
			startX:			306
			startY:			22
			endX:				22
			endY:				22
			init:
		)

		((= aSidewalkNorth (Actor new:))	;**	moving sidewalk going away
			view:				vCafe
			setLoop:			lLine
			setCel:			0
			setPri:			0
			setStep:			1 1
			illegalBits:	0
			posn:				187 99
			ignoreActors:
			init:
			setScript:		sidewalkNorthScript
		)
		((= aSidewalkSouth (Actor new:))	;**	moving sidewalk coming towards us
			view:				vCafe
			setLoop:			lLine
			setCel:			0
			setPri:			0
			setStep:			1 1
			illegalBits:	0
			posn:				127 66
			ignoreActors:
			init:
			setScript:		sidewalkSouthScript
		)
		((= aWaitress (Actor new:))		;**	waitress behind snack bar
			view:				vWaitress
			loop:				facingNorth
			setPri:			15
			posn:				52 174
			ignoreActors:
			illegalBits:	0
			init:
			setCycle:		Walk
			setScript:		waitressScript
		)

		(self
			setRegions:		rgAirport
			setScript: 		rm55Script
		)

		(if (== prevRoomNum 57)
			(= currentStatus RIDING_SIDEWALK)
			(HandsOff)
			(ego
				illegalBits:	cLtWhite
				setPri:			RELEASE
				setLoop:			facingSouth
				setCycle:		FALSE
				setCel:			0
				setStep:			1 1
				posn:				141 88
				init:
			)
			(rm55Script changeState: comingHome)
		else
			(NormalEgo)
			(ego
				observeControl:cGreen
				posn:				33 146
				init:
			)
		)
	)
)


(instance rm55Script of Script
	(method (doit)
		(super doit:)

		(cond
			((and (& (ego onControl:) cBlue)
					(== currentStatus NORMAL))
				(curRoom newRoom: 54)
			)
			((and	(& (ego onControl:) cRed)
					(== currentStatus NORMAL))
				(self changeState: startSliding)
			)
			((& (ego onControl:) cCyan)
				(if (and	(== currentStatus NORMAL)
							(not seenMessage))
					(= seenMessage TRUE)
					(Print "\"Hey! Stay outta there!\" shouts the waitress.
						\"You tryin' to get yourself killed?\"")
				)
			)
			(else
				(= seenMessage FALSE)
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'search, (look<in)/food, plate, gravy, special')
			(cond
				((!= orderedSpecial DELIVERED)
					(DontHave)
				)
				((not (ego inRect: nearCounter))
					(NotClose)
				)
				(else
					(Print "Say! There's a bobby pin buried in the gravy.")
				)
			)
		)

		(if (Said 'look>')
			(if (Said '/food, gravy, special')
				(cond
					((!= orderedSpecial DELIVERED)
						(DontHave)
					)
					((not (ego inRect: nearCounter))
						(NotClose)
					)
					(else
						(Print "It looks like it might be edible.")
					)
				)
			)

			(if (Said '/sidewalk')
				(Print "The moving sidewalks are a refreshingly modern touch
					in such a small airport,
					but boy, is it a long way to the gates!")
			)

			(if (Said '/machine')
				(Print "The one to your right is labeled: \"Brokin\" (sic).
					The machine to the left is labeled:
					\"Flight Insurance, $100.\"")
				(Print "(It's a little large for an insurance machine,
					isn't it?)" LOW)
			)

			(if (Said '/counter, table, bar')
				(Print "It's just a cheap plastic laminate,
					with a delicate little design that's too small
					to show up at this resolution!")
			)

			(if (Said '/girl, waitress')
				(Print "Nah. You'd rather not.")
				(Print "(Trust me on this one!)" LOW)
			)

			(if (Said '/menu, sign')
				(Print "\n
					\ \ MENU\n\n
					\ Today only:\n\n
					Blue P\ ate Special\n\n
					\ \ $1.00\n\n"
					mode:	teJustCenter
					)
				(Print "At least the price is reasonable.")
			)

			(if (Said '[/room, wall]')
				(Print "A waitress cruises behind the snack bar
					while a pair of moving sidewalks
					disappear into the distance.
					There are some vending machines along the east wall
					and near the snack bar is a sign.")
			)
		)

		(if (Said 'ask, request/menu')
			(Print "\"Pardon me, ma'am,\" you say,
				\"but do you have a menu?\"")
			(Print "\"Ain'cha got no eyes, fella?
				What's that big thing hanging on the wall there?\"")
		)

		(if (Said 'talk/woman, waitress')
			(cond
				((ego has: iBobbyPin)
					(Print "\"Ha, ha, I fooled you,\" you tell the waitress.
						\"I'm not eating that stuff again.\"")
					(Print "\"Why not?\" she asks,
						\"we say it's just like momma used to make!\"")
				)
				((== orderedSpecial DELIVERED)
					(Print "\"Say, is this stuff good to eat?\" you ask.")
					(Print "(She does not respond.)" LOW)
				)
				((and	orderedSpecial
						(< orderedSpecial DELIVERED))
					(Print "\"Hey, mama, hurry up with that chow,\" you order.")
					(Print "(It affects her speed not in the least!)" LOW)
				)
				(else
					(Print "\"Lovely weather, eh?\" you ask the waitress.")
					(Print "\"Yep.\"")
				)
			)
		)

		(if (Said 'buy, order/special, food')
			(cond
				((or	orderedSpecial 
						(not ((inventory at: iBobbyPin) ownedBy: curRoomNum)))
					(Print "You already did that.")
					(Print "(And you don't want another!)" LOW)
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				((not (ego inRect: nearCounter))
					(NotClose)
				)
				(else
					(self changeState: gimmeAspecial)
				)
			)
		)

		(if (Said 'get/special, food')
			(cond
				((!= currentStatus NORMAL)
					(NotNow)
				)
				(( < orderedSpecial DELIVERED)
					(Print "You must have some food before you can take it.")
				)
				((not (ego inRect: nearCounter))
					(NotClose)
				)
				(else
					(Print "Nah. The plate is stuck to the counter!")
				)
			)
		)

		(if (Said 'eat/special, food')
			(cond
				((!= currentStatus NORMAL)
					(NotNow)
				)
				((== orderedSpecial PIN_GONE)
					(Print "No way.
						You feel that you've tasted that stuff in an earlier
						lifetime, and that time was enough!")
				)
				((!= orderedSpecial DELIVERED)
					(Print "You must have some food before you can eat it.
						Try ordering something.")
				)
				((not (ego inRect: nearCounter))
					(NotClose)
				)
				(else
					(self changeState: ateSpecial)
				)
			)
		)

		(if (Said 'eat/noword')
			(Print "Eat what?")
		)

		(if (Said 'search, find, get/gravy, special, pin')
			(cond
				((not ((inventory at: iBobbyPin) ownedBy: curRoomNum))
					(AlreadyTook)
				)
				((!= orderedSpecial DELIVERED)
					(Print "Where?")
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				((not (ego inRect: nearCounter))
					(NotClose)
				)
				(else
					(ego get: iBobbyPin)
					(= orderedSpecial PIN_GONE)
					(aPlate posn: 999 999)
					(theGame changeScore: 7)
					(Print "This time, you decide to save yourself from
						a fate worse than
						(well, ok, actually quite similar to;
						no, indeed, I suppose it would be identical to)
						death by fishing
						the bobby pin out of the \"Blue Pate Special's\"
						gravy-like substance instead of swallowing it."
						draw:)
				)
			)
		)

		(if (or	(Said 'buy, get/insurance, parachute')
					(Said 'use/machine')
					(Said 'put/money/machine'))
			(cond
				((!= currentStatus NORMAL)
					(NotNow)
				)
				((ego inRect: nearCandy)
					(Print "This machine is clearly labeled, \"Brokin!\"")
				)
				((not (ego inRect: nearParachute))
					(NotClose)
				)
				((not ((inventory at: iParachute) ownedBy: curRoomNum))
					(Print "It's empty now.")
					(Print "(But it did swallow another of your hundred dollar
						bills!)" LOW)
				)
				(else
					(self changeState: startChute)
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(intro)


			;**	Here's Larry's return from rm57.

			(comingHome
				(ego
					illegalBits:	0
					setCel:			0
					setMotion:		MoveTo 125 114 self
				)
			)

			(offBeltway
				(NormalEgo facingSouth)
				(ego observeControl: cGreen)
			)

			(startSliding
				(= currentStatus RIDING_SIDEWALK)
				(HandsOff)
				(ego
					illegalBits:	0
					setLoop:			facingNorth
					setCel:			4
					setStep:			1 1
					setMotion:		MoveTo 174 88 self
				)
			)

			(slideDone
				(curRoom newRoom: 56)
			)


			;**	Here's where he buys the parachute...

			(startChute
				(HandsOff)
				(Print "You peel off another hundred-dollar bill from your wad of
					money and insert it into the slot;
					the machine slurps it up, digests it,
					considers its authenticity, finds it valid,
					then begins to whir and shake.")
				(= seconds 3)
			)

			(startChuteDoor
				(aDoor setCycle:	EndLoop self)
			)

			(doorDone
				(aParachute
					posn:				264 147
					setCycle:		EndLoop self
				)
			)

			(chuteDone
				(aDoor setCycle:	BegLoop self)
			)

			(waitAsec
				(= seconds 3)
			)

			(punchLine
				(aParachute			dispose:)
				(ego get: iParachute)
				(theGame changeScore: 3)
				(Print "What kind of airline is this, any way?
					Well, you suppose a parachute would be pretty good insurance,
					so you pick it up off the floor and hide it in your
					inner suitcoat pocket!" draw:)
				(User
					canControl:		TRUE
					canInput:		TRUE
				)
			)


			;**	This is the path if he orders the Blue Pate Special...

			(gimmeAspecial
				(= orderedSpecial TRUE)
				(HandsOff)
				(Print "\"Hey, baby!\" you shout.
					\"Gimme one o' them 'Blue Plate Specials!'\"")
				(= seconds 3)
			)

			(bluePateToYou
				(Print "\"That's 'Blue PATE' to you, baldie!\" she retorts.")
				(= seconds 3)
			)

			(yo
				(Print "\"Yo,\" she yells back into the kitchen,
					\"slop me up another bald one!\"")
				(waitressScript changeState: getPlate)
			)


			;**	Here we pause while waitressScript walks her to kitchen & back.

			(specialDone
				(User
					canControl:		TRUE
					canInput:		TRUE
				)
				(Print "\"Here ya go, big boy,\" she says with a toothy grin,
					\"one 'Big Blue' comin' atcha.\"")
				(Print "She slings the plate on the counter before you.")
				(aPlate
					posn:		(ego x?) 177
				)
				(= orderedSpecial DELIVERED)
			)


			;**	This is if he eats the food...

			(ateSpecial
				(= currentStatus NEAR_DEATH)
				(HandsOff)
				(Ok)
				(Print "You quickly wolf down the \"Blue Pate Special\"
					as if you had not eaten for an entire adventure game.
					\"Say,\" you tell the waitress, \"this is pretty good stuff.
					Why, this tastes just like the meatloaf Mama Laffer
					usggheddf dfgggfssef pssqfufufaaf arrrrgggggggghhhhh!\"")
				(ego hide:)
				(Print "You drop to the floor near the counter while
					making clever, creative sounds with your trachea." draw:)
					;**	"trachea" spelling checked:	July 2, 1988
				(= seconds 3)
			)

			(UBdying
				(Print "You now remember
					what it was you hated about Mama Laffer's cooking...")
				(Print "\"She had a habit of losing her bobby pins in the
					gravy,\" you think,
					while you frantically make the universal sign for choking.")
				(Print "(What a shame the waitress never heard of
					Dr. Heimlich!)" LOW)
				(= currentStatus DEAD)
			)
		)
	)
)


(instance waitressScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(waitressInit
				(= seconds (Random 1 3))
			)

			(waitressTurn
				(= waitressX (Random 5 150))
				(aWaitress
					setLoop:			(if ( > waitressX (aWaitress x?))
											lTurnEast
										else
											lTurnWest
										)
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(waitressStart
				(aWaitress
					setCycle:		Walk
					setLoop:			RELEASE
					setMotion:		MoveTo waitressX waitressY self
				)
			)

			(waitressArrive
				(aWaitress
					setLoop:			(+ (aWaitress loop?) 2)
					cel:				3
					setCycle:		BegLoop self
				)
			)

			(faceCustomers
				(= seconds (Random 2 5))
			)

			(waitressReach
				(switch (Random 1 7)
					(1
						(aWaitress
							cycleSpeed:	1
							setLoop:		lGrabAss
							cel:			0
							setCycle:	EndLoop self
						)
					)
					(2
						(aWaitress
							cycleSpeed:	1
							setLoop:		lGrabHair
							cel:			0
							setCycle:	EndLoop self
						)
					)
					(else
						(self changeState: waitAwhile)
					)
				)
			)

			(waitressScratch
				(aWaitress
					setLoop:		(+ (aWaitress loop?) 1)
					setCycle:	Forward
				)
				(= cycles (Random 30 50))
			)

			(scratchDone
				(aWaitress
					setLoop:		(- (aWaitress loop?) 1)
					cel:			3
					setCycle:	BegLoop self
				)
			)

			(waitAwhile
				(aWaitress
					setLoop:		lTurnEast
					setCel:		0
					cycleSpeed:	0
					setCycle:	Walk
				)
				(= seconds (Random 1 3))
			)

			(waitressDone
				(self changeState: waitressInit)
			)


			;**	This makes her get Larry's food from the kitchen,
			;**	and deliver it to him.

			(getPlate
				(= cycles	0)
				(= seconds	0)
				(aWaitress
					setCycle:		Walk
					setLoop:			RELEASE
					setMotion:		MoveTo -222 waitressY self
				)
			)

			(comeBack
				(aWaitress
					setMotion:		MoveTo (ego x?) waitressY self
				)
			)

			(plateAtEgo
				(rm55Script cue:)
				(self changeState: waitressArrive)
			)
		)
	)
)


(instance sidewalkNorthScript of Script
	(method (changeState newState)
		(switch (= state newState)
			(0	(aSidewalkNorth	setMotion:	MoveTo 184 66 self))
			(1	(aSidewalkNorth	posn:			187 123)
				(self changeState: 0))
		)
	)
)


(instance sidewalkSouthScript of Script
	(method (changeState newState)
		(switch (= state newState)
			(0	(aSidewalkSouth	setMotion:	MoveTo 124 118 self))
			(1	(aSidewalkSouth	posn:			127 66)
				(self changeState: 0))
		)
	)
)

