;**
;**	Logics for room 44 -- a room at the resort
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 25, 1988
;**


(script#				44)

(public
	rm44				0
)


(define	nearSoap			222 131 243 145)
(define	nearMatches		 70 127 103 139)
(define	nearBed			100 120 127 130)	;**	for screwing
(define	nearBedSouth	104 127 122 131)	;**	for sleeping
(define	nearToilet		227 142 259 149)

(define	inDressingArea	176 100 199 115)
(define	offScreen		160 234)


;(define	vEgoOnBed					152)
;(define	vEgoBikini					150)
;(define	vEgoBikiniStuffed			151)

;(define	vResortRoom					416)
	(define	lStuff				0)
		(define	cTable1		0)
		(define	cLamp			1)
		(define	cTable2		2)
		(define	cBath			3)
		(define	cSoap			4)
		(define	cPainting	5)
	(define	lBoats			1)

;(define	vMaid							417)
;(define	vMaidInBed					418)
	(define	lMaid					0)
	(define	lLarry				1)
	(define	lBothUnder			2)
	(define	lMaidOut				3)
	(define	lLarryOut			4)
	(define	lBothOut				5)
	(define	lCaught				6)

;(define	vMaid'sBrother				419)
	(define	lEnter				0)
	(define	lRaiseGun			1)

;(define	vBigMaid						436)
	(define	lBigFace		0)

;(define	sDrumFill					12)


(local
	work
	aSoap
	aBoat
	aMaid
	aBrother
	maidInRoom
	oldEgoX
	oldEgoY
)


(enum												;**	brotherScript state values
	brotherInit
	brotherStart
	brotherInDoorway
	brotherIn
	brotherDone
)

(enum												;**	boatScript state values
	boatInit
	boatStart
	boatRight
	goBack
	boatLeft
)

(enum												;**	rmScript state values
	intro
	maidEnters
	maidInPlace
	maidTalks
	turnToGo
	maidGone

	headForBed
	maidOnBed
	larryAtBed
	roomService
	gopherIt
	kissAwhile
	underCover
	maidOut
	maidIn
	larryOut
	larryIn
	bothOut
	bothUnder
	here'sBrother
	bothCaught
	UBdying
)


(instance rm44 of Room
	(properties
		picture:		44
		horizon:		1
	)

	(method (init)
		(Load VIEW vEgoOnBed)
		(Load VIEW vResortRoom)

		(if (and (ego has: iBikiniTop)
					(ego has: iBikiniBottom))
			(Load VIEW vEgoBikini)
			(Load VIEW vEgoBikiniStuffed)
		)

		(Load SOUND sDrumFill)
		(theSound 	init:)

		(super init:)

		((View new:)							;**	table
			view: 			vResortRoom
			ignoreActors:
			loop:				lStuff
			cel: 				cTable1
			posn: 			81 133
			setPri: 			ltBlue
			addToPic:
		)
		((View new:)							;**	lamp
			view: 			vResortRoom
			ignoreActors:
			loop:				lStuff
			cel: 				cLamp
			posn: 			110 97
			setPri: 			4
			addToPic:
		)
		((View new:)							;**	smallTable
			view: 			vResortRoom
			ignoreActors:
			loop:				lStuff
			cel: 				cTable2
			posn: 			108 107
			setPri: 			3
			addToPic:
		)
		((View new:)							;**	bathroom
			view: 			vResortRoom
			ignoreActors:
			loop:				lStuff
			cel: 				cBath
			posn: 			241 127
			setPri: 			9
			addToPic:
		)
		((View new:)							;**	painting
			view: 			vResortRoom
			ignoreActors:
			loop:				lStuff
			cel: 				cPainting
			posn: 			82 98
			setPri: 			4
			addToPic:
		)

		(if ((inventory at: iSoap) ownedBy: curRoomNum)
			((= aSoap (View new:))
				view:				vResortRoom
				ignoreActors:
				loop:				lStuff
				cel: 				cSoap
				posn: 			256 123
				setPri: 			10
				stopUpd:
				init:
			)
		)

		((= aBoat (Actor new:))
			view:				vResortRoom
			setLoop:			lBoats
			setCel:			(Random 0 4)
			setPri:			0
			setStep:			1 1
			moveSpeed:		2
			illegalBits:	0
			ignoreActors:
			posn:				179 85
			init:
			setScript:		boatScript
		)

		(NormalEgo)
		(ego
			posn: 			159 163
			init:
		)

		(if (== currentEgoView vEgo)
			(Load VIEW vMaid)
			(Load VIEW vMaidInBed)
			(Load VIEW vMaid'sBrother)
			(Load VIEW vBigMaid)
			((= aMaid (Actor new:))
				view:				vMaid
				illegalBits:	0
				loop:				facingNorth
				posn:				offScreen
				setCycle:		Walk
				stopUpd:
				init:
			)
			(aBigFace
				view:				vBigMaid
				setLoop:			lBigFace
				posn:				149 1060
				setPri:			15
				init:
			)
			((= aBrother (Actor new:))
				view:				vMaid'sBrother
				ignoreActors:
				illegalBits:	0
				setLoop:			lEnter
				posn:				offScreen
				setCycle:		Walk
				init:
			)
		)
		(self
			setRegions:		rgResort
			setScript: 		rm44Script
		)
	)
)


(instance rm44Script of Script
	(method (doit)
		(super doit:)

		(if (& (ego onControl:) cGreen)
			(curRoom newRoom: 40)
		)

		(if (and	(== state maidOnBed)
					(ego inRect: nearBed))
			(self cue:)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look<under/bed')
			(Print "Pretty clever!")
			(Print "But, you find only dust.")
			(Print "(The maid probably has other chores to accomplish!)" LOW)
		)

		(if (Said '/lamp')
			(Print "It's just a lamp.")
		)

		(if (Said 'look>')
			(if (Said '/girl')
				(cond
					((not maidInRoom)
						(Print "Where?")
					)
					((!= currentStatus NORMAL)
						(NotNow)
					)
					(else
						(aBigFace posn: 149 60)
						(Timer setReal: aBigFace 5)
						(HandsOff)
					)
				)
			)

			(if (Said '/closet, shower')
				(Print "The room is very comfortable.")
			)

			(if (Said '/mirror')
				(Print "Will you ever be handsome?")
			)

			(if (Said '/stool, toilet')
				(Print "The toilet is on the south wall of the bathroom.")
			)

			(if (Said '/(room<rest), sink, cabinet, bowl, bathroom, (room<bath)')
				(if ((inventory at: iSoap) ownedBy: curRoomNum)
					(Print "A bowl filled with complimentary soap is
						near the sink.")
				else
					(Print "A bowl that formerly contained soap is
						near the sink.")
				)
			)

			(if (Said '/drawer')
				(Print "There isn't one.")
			)

			(if (Said '/bed')
				(Print "Why? You're not sleepy.")
			)

			(if (Said '/picture')
				(Print "\"Starving Artists' Corp.\"")
			)

			(if (Said '<behind/picture')
				(Print "\"Made in Taiwan\"")
			)

			(if (or	(Said '/mirror')
						(and	(or	(== currentEgoView vEgoBikini)
										(== currentEgoView vEgoBikiniStuffed))
								(Said '/bikini')))
				(cond
					((== currentEgoView vEgoBikini)
						(Print "What a shame your figure hasn't yet filled out.")
					)
					((== currentEgoView vEgoBikiniStuffed)
						(Print "There are few people who can fill a bikini like you,
							Larry!")
					)
					((== currentEgoView vEgoBlond)
						(Print "Who says blondes have more fun, Larry?")
					)
					(else
						(Print "\"Hey, look!\" you cry, \"It's me, Larry.
							Larry Laffer!\"")
					)
				)
			)

			(if (Said '/nightstand, table')
				(if ((inventory at: iMatches) ownedBy: curRoomNum)
					(Print "There is a book of complimentary matches lying
						on the nightstand near the bed.")
				else
					(Print "There's nothing left on the nightstand but a lamp,
						which you don't need.")
				)
			)

			(if (Said '/window')
				(Print "There is a beach outside filled with people having fun.")
				(Print "(Unlike you, Larry!)" LOW)
			)

			(if (Said '/beach')
				(Print "The people are having a \"beach\" of a good time!")
			)

			(if (Said '[/room]')
				(Print "This room is considerably larger than your
					stateroom on board the cruise ship!
					It has a picture window overlooking the beach.")
				(if (& (ego onControl:) cBlue)	;**	in bathroom
					(if ((inventory at: iSoap) ownedBy: curRoomNum)
						(Print "A bowl filled with complimentary soap is
							near the sink.")
					else
						(Print "A bowl that formerly contained soap is
							near the sink.")
					)
				else
					(Print "There is a restroom, plus a dressing area
						near the picture window.")
					(if ((inventory at: iMatches) ownedBy: curRoomNum)
						(Print "There is a book of complimentary matches lying
							on the nightstand near the bed.")
					)
				)
			)
		)

		(if (Said 'get/bowl')
			(Print "Leave the toilet where it is!")
		)

		(if (or	(Said 'shit, piss')
					(Said '/shit, piss')
					(Said '//shit, piss')
					(Said '/toilet'))
			(if (ego inRect: nearToilet)
				(ego
					loop: 		facingSouth
					setMotion:	FALSE
				)
				(Print "Ahhhh." draw:)
			else
				(NotClose)
			)
		)

		(if (and	(== currentEgoView vEgoBikiniStuffed)
					(Said 'remove/soap, money/top[<bikini]'))
			(Print "No, better leave it there.")
		)

		(if (Said 'wear, use/flower')
			(Print "You would look sweet, but decide to wait until later.")
		)

		(if (Said '(turn<on), use/water, sink')
			(Print "You'd like to wash your hands of this whole affair!;")
		)

		(if (Said 'close, pull/window, curtain')
			(Print "It's too bad they don't close,
				but the darkened glass prevents anyone outside
				from looking in and seeing you in the dressing area.")
		)

		(if maidInRoom
			(if (Said 'no')
				(Print "\"No, everything is NOT ok,\" you tell the maid.")
				(Print "\"Well, sir; perhaps you'd like something that is not on
					the room service menu?\" she asks with a smile.")
			)

			(if (Said 'talk/girl')
				(Print (Format @string
					"\"Hello, my little native beauty,\" you tell her.
					%s" laffer))
				(Print "\"Hello, sir,\" she smiles.
					\"I would like to help you in any way I can.\"")
			)

			(if (Said '(love<make), (sex<have), hump/girl')
				(cond
					((!= currentStatus NORMAL)
						(NotNow)
					)
					(( > state maidGone)		;**	She's already heading for bed.
						(Print "\"Ok. I'm ready,\" she says with a smile.
							\"How about you?\"")
					)
					(else
						(Ok)
						(self changeState: headForBed)
					)
				)
			)
		)

		(if (or	(Said '(go<to), (get<into), enter/bed, sleep')
					(Said '(lie<down), lie, sleep'))
			(cond
				((== (ego view?) vEgoOnBed)
					(YouAre)
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				((!= currentEgoView vEgo)
					(Print "Not dressed like that!")
				)
				(( < (ego y?) 121)			;**	middle of bed
					(Print "First, move to the other side of the bed.")
				)
				((not (ego inRect: nearBedSouth))
					(NotClose)
				)
				(else
					(= currentStatus SLEEPING)
					(Ok)
					(User
						canControl: 	FALSE
						canInput: 		TRUE
					)
					(= currentEgoView (ego view?))
					(ego
						view: 			vEgoOnBed
						setLoop: 		0
						cel:				0
						setCycle:		Forward
						cycleSpeed:		5
						setPri:			8
						setMotion:		FALSE
						illegalBits:	0
						posn: 			111 111
					)
				)
			)
		)

		(if (or	(Said 'leave/bed')
					(Said 'rise, (wake, stand, get<up)'))
			(cond
				((!= (ego view?) vEgoOnBed)
					(YouAre)
				)
				((!= currentStatus SLEEPING)
					(NotNow)
				)
				(else
					(Ok)
					(ego posn: 110 130)
					(NormalEgo facingNorth)
				)
			)
		)

		(if (or	(Said 'wear, (change<into), (put<on)/swimsuit, 
														(suit<swim), top, bottom, bikini')
					(Said 'change, (take<off), remove/suit, clothes'))
			(cond
				((!= currentStatus NORMAL)
					(NotNow)
				)
				(maidInRoom
					(Print "Not while there's a lady present!")
				)
				((or	(== currentEgoView vEgoBikini)
						(== currentEgoView vEgoBikiniStuffed))
					(Print "You are!")
				)
				((and (ego has: iBikiniTop)
						(ego has: iBikiniBottom))
					(if (ego inRect: inDressingArea)
						(Ok)
						(= currentEgoView vEgoBikini)
						(ego view: currentEgoView)
						(theGame changeScore: 5)
						(Print "(Boy, do you look sweet!)" LOW)
					else
						(Print "Have you no modesty?
							At least move to the dressing area behind the restroom.")
					)
				)
				((or	(ego has: iBikiniTop)
						(ego has: iBikiniBottom))
					(Print "Nah. Why would you want to wear half a bikini?")
				)
				(else
					(DontHave)
				)
			)
		)

		(if (or	(Said 'change, wear, (put<on)/suit, clothes')
					(Said 'change, remove, (take<off)/(suit<swim), swimsuit'))
			(if (or	(== currentEgoView vEgoBikini)
						(== currentEgoView vEgoBikiniStuffed))
				(Print "You'd better stay in the bikini.
					It's your only hope of escape.")
			else
				(Print "What do you want to wear?")
			)
		)

		(if (Said 'take/shower, bath')
			(Print "There's no time.")
		)

		(if (Said 'get/match')
			(cond
				((not ((inventory at: iMatches) ownedBy: curRoomNum))
					(AlreadyTook)
				)
				((not (ego inRect: nearMatches))
					(NotClose)
				)
				(else
					(Ok)
					(ego get: iMatches)
					(theGame changeScore: 2)
					(Print "You slip a book of matches into your pocket.")
					(Print "(Gee, Larry; at last you've found your match!)" LOW)
					(theSound play:)
					(Print "(Drum fill)")
				)
			)
		)

		(if (Said 'get/soap')
			(cond
				((not ((inventory at: iSoap) ownedBy: curRoomNum))
					(AlreadyTook)
				)
				((not (ego inRect: nearSoap))
					(NotClose)
				)
				(else
					(Ok)
					(ego get: iSoap)
					(theGame changeScore: 2)
					(aSoap hide:)
					(Print "You grab the bowl full of tiny bars of soap
						and dump it into your pockets thinking,
						\"When I get home, I can carefully wet these,
						and glue them all together to make at least one
						regular-sized bar. What a great deal!\""
							WIDE draw:)
					(Print "(You have always been terminally cheap!)" LOW)
				)
			)
		)

		(if (or	(Said 'put, stuff/top/money, bill')
					(Said 'put, stuff/money, bill/top'))
			(cond
				((!= currentStatus NORMAL)
					(NotNow)
				)
				(maidInRoom
					(Print "Not while there's a lady present!")
				)
				((== currentEgoView vEgoBikiniStuffed)
					(Print "Your bra is full already!")
				)
				((!= currentEgoView vEgoBikini)
					(Print "You aren't wearing a bikini.")
				)
				((ego inRect: inDressingArea)
					(Ok)
					(= braContents iMoney)
					(= currentEgoView vEgoBikiniStuffed)
					(NormalEgo)
					(theGame changeScore: 12)
					(Print "(Are you sure you want to do this?)" LOW)
				)
				(else
					(Print "Have you no modesty?
						At least move to the room's dressing area
						behind the restroom.")
				)
			)
		)

		(if (or	(Said 'put, stuff/top/soap')
					(Said 'put, stuff/soap/top'))
			(cond
				((!= currentStatus NORMAL)
					(NotNow)
				)
				(maidInRoom
					(Print "Not while there's a lady present!")
				)
				((== currentEgoView vEgoBikiniStuffed)
					(Print "Your bra is full already!")
				)
				((!= currentEgoView vEgoBikini)
					(Print "You aren't wearing a bikini.")
				)
				((not (ego has: iSoap))
					(Print "You have no soap.")
				)
				((ego inRect: inDressingArea)
					(Ok)
					(= braContents iSoap)
					(= currentEgoView vEgoBikiniStuffed)
					(NormalEgo)
					(theGame changeScore: 12)
					(Print "(Are you sure you want to do this?)" LOW)
				)
				(else
					(Print "Have you no modesty?
						At least move to the room's dressing area
						behind the restroom.")
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(if (and (== currentStatus NORMAL)
							(== currentEgoView vEgo))
					(= seconds 10)
				)
			)

			(maidEnters
				(if (and (== currentStatus NORMAL)
							(== currentEgoView vEgo))
					(= maidInRoom TRUE)
					(aMaid
						setMotion:	MoveTo 160 132 self
					)
				)
			)

			(maidInPlace
				(LookAt aMaid ego 1)
				(= seconds 3)
			)

			(maidTalks
				(Print "\"Good morning, sir!
					I'm your customer service representative in charge of
					horizontal surface consistency and custodial maintenance.
					I trust you are finding everything satisfactory?\" she asks."
					BIG)
				(Print "(She says she's the maid!)" LOW)
				(= seconds 8)
			)

			(turnToGo
				(Print (Format @string
					"\"Ok,\" says the native maid coyly,
					\"%s\"" tritePhrase))
				(aMaid
					setMotion:	MoveTo offScreen self
				)
			)

			(maidGone
				(= maidInRoom FALSE)
				(aMaid	dispose:)
			)


			;**	This path occurs if user enters some pickup line
			;**	while maidInRoom:

			(headForBed
				(= cycles	0)
				(= seconds	0)
				(Print "\"Oh, sir,\" she smiles demurely,
					\"I just love Americano men;
					they are so (how you say) 'financial.'\"" HIGH)
				(aMaid
					setMotion:	MoveTo 130 130 self
				)
			)

			(maidOnBed
				(aMaid
					illegalBits:0
					ignoreActors:
					view:			vMaidInBed
					loop:			lMaid
					cel:			0
					setPri:		ltBlue
					posn:			127 111
				)
				(Print (Format @string
					"\"Well, come on, big boy! What are you waiting for?
					Don't you want to '%s'\"" tritePhrase) HIGH draw:)
			)


			;**	Here we wait for Larry to join her on the bed...

			(larryAtBed
				(HandsOff)
				(= currentStatus NEAR_DEATH)
				(ego
					illegalBits:0
					ignoreActors:
					view:			vMaidInBed
					loop:			lLarry
					cel:			0
					posn:			111 120
					setPri:		ltBlack
				)
				(= seconds 3)
			)

			(roomService
				(Print "\"Gee,\" you tell her,
					\"is this room service, or what?\"" HIGH draw:)
				(= seconds 3)
			)

			(gopherIt
				(ego
					cycleSpeed:	1
					setCycle:	EndLoop self
				)
				(aMaid
					cycleSpeed:	1
					setCycle:	EndLoop
				)
			)

			(kissAwhile
				(= seconds 4)
			)

			(underCover
				(ego				hide:)
				(aMaid
					setLoop:		lBothUnder
					cel:			0
					setCycle:	Forward
					posn:			137 105
					setPri:		ltBlack
				)
				(= seconds 3)
			)

			(maidOut
				(aMaid
					setLoop:		lMaidOut
				)
				(= seconds 2)
			)

			(maidIn
				(aMaid
					setLoop:		lBothUnder
				)
				(= seconds 4)
			)

			(larryOut
				(aMaid
					setLoop:		lLarryOut
				)
				(= seconds 2)
			)

			(larryIn
				(aMaid
					setLoop:		lBothUnder
				)
				(= seconds 4)
			)

			(bothOut
				(aMaid
					setLoop:		lBothOut
				)
				(= seconds 2)
			)

			(bothUnder
				(aMaid
					setLoop:		lBothUnder
				)
				(aBrother
					setScript:	brotherScript
				)
			)


			;**	Here we pause while big brother walks in...

			(here'sBrother
				(aBrother
					setCycle:	EndLoop self
				)
				(if ( > filthLevel DIRTY)
					(Print "\"Seester Maria! 
						How many times I gotta tell you?
						Lay off the white meat!\"" HIGH draw:)
				else
					(Print "\"Sister! How many times shall I shoot him?\"")
				)
				(= seconds 3)
			)

			(bothCaught
				(aMaid
					setLoop:		lCaught
				)
				(= seconds 4)
			)

			(UBdying
				(if ( > filthLevel DIRTY)
					(Print "Bad choice, Larry!
						Remember: there's no such thing as a free lunch!")
				else
					(Print "Bad choice, Larry!")
				)
				(= currentStatus DEAD)
			)
		)
	)
)


(instance brotherScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(brotherInit
				(= seconds 5)
			)

			(brotherStart
				(aBrother
					setMotion:	MoveTo 160 160 self
				)
			)

			(brotherInDoorway
				(Print "While you are indisposed,
					through the doorway marches the maid's older brother,
					Carlos.")
				(Print "Thanks to U.S. foreign aid,
					Carlos has many extra bullets that he enjoys firing
					for amusement!")
				(aBrother
					setMotion:	MoveTo 150 122 self
				)
			)

			(brotherIn
				(aBrother
					setLoop:		lRaiseGun
					cel:			0
				)
				(= seconds 3)
			)

			(brotherDone
				(rm44Script cue:)
				(self dispose:)
			)
		)
	)
)


(instance boatScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(boatInit
				(= seconds (Random 5 15))
			)

			(boatStart
				(aBoat			setMotion:	MoveTo 98 85 self)
			)

			(boatRight
				(aBoat			stopUpd:)
				(= seconds (Random 5 15))
			)

			(goBack
				(aBoat
					setCel:		(Random 0 4)
					setMotion:	MoveTo 179 85 self
				)
			)

			(boatLeft
				(aBoat
					setCel:		(Random 0 4)
					stopUpd:
				)
				(self changeState: boatInit)
			)
		)
	)
)


(instance aBigFace of Prop
	(method (cue)
		(Print "You find native women sexy." draw:)
		(Print "(But then, you find any woman sexy!)" at: 55 155 width: 210)
		(self posn: 149 1060)
		(HandsOn)
	)
)

(instance theSound of Sound
	(properties
	 	number:		sDrumFill
		loop:			1
	)
)

