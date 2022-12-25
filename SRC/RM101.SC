;**
;**	Logics for room 101 -- TV studio reception area
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 17, 1988
;**


(script#				101)

(public
	rm101				0
)


(define	frontOfDesk		133)
(define	atFrontDoor		156 180)
(define	atDoorEast 		258 122)
(define	atDoorWest		 62 118)
(define	atDoorNorth		161  82)
(define	nearDesk			134 145 185 152)
(define	triggerDoll		 91  80 109	 87)
(define	triggerBoy		214  80 229  87)

(define	msgHey	"Hey! Was that somebody famous?")

;(define	vTVreception				201)
;	(define	lSigns				0)
;	(define	lGreenRoomDoor		1)
;	(define	lDCdoor				2)
;	(define	lLLLdoor				3)

;(define	vReceptionist				203)
	(define	lSmile				0)
	(define	lTalk					1)
	(define	lLiftPhone			2)
	(define	lTalkPhone			3)
	(define	lPreBubble			4)
	(define	lBlowBubble			5)
	(define	lPhone				6)
	(define	lBigClerk			7)
	(define	lBigChewing			8)
	(define	lBigBubbles			9)

;(define	vShowBizGuy					204)
;(define	vShowBizDoll				205)


(local
	aReceptionist
	aGuy
	aDoll
	aDoorWest
	aDoorEast
	aDoorNorth
	hisNum1
	hisNum2
	hisNum3
	hisNum4
	hisNum5
	hisNum6
	myNum1
	myNum2
	myNum3
	myNum4
	myNum5
	myNum6
)


(enum												;**	rm101Script state values
	intro

	showedTicket
	waitingForNumber
	processNumber
	gotoGreenRoom
	atEndOfCounter
	atRearOfCounter
	atDoor
	doorDone
	nextRoom
	noWayPirate
)

(enum												;**	receptionistScript state values
	girlInit
	preBubble
	blowBubble1
	blowBubble2
	postBubble
	bubbleDone
	getPhone
	talk1
	listen1
	talk2
	listen2
	talk3
	putPhone
	phoneDone
	talkingToLarry
	waitingToTalkToLarry
)

(enum												;**	guyScript/dollScript state values
	guyInit
	doDoor
	guyDoorOpen
	walkGuy
	lockDeDoor
	walkOffScreen
	eraseGuy
)


(instance rm101 of Room
	(properties
		picture:		101
		south:		11
	)

	(method (init)
		(Load VIEW vTVreception)
		(Load VIEW vReceptionist)
		(Load VIEW vShowBizGuy)
		(Load VIEW vShowBizDoll)

		(if (ego has: iLotteryTicket)
			(Load VIEW iLotteryTicket)
		)

		(super init:)

		((View new:)							;**	First, do the signs over doors.
			view: 			vTVreception
			loop: 			0
			cel: 				0
			posn: 			51 72
			setPri: 			10
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vTVreception
			loop: 			0
			cel: 				1
			posn: 			266 72
			setPri: 			10
			ignoreActors:
			addToPic:
		)
		((View new:)
			view: 			vReceptionist
			loop: 			lPhone
			cel: 				0
			setPri: 			10
			posn: 			168 120
			ignoreActors:
			addToPic:
		)

		(aBigClerk
			view:				vReceptionist
			setLoop:			lBigClerk
			posn:				223 1042
			setPri:			14
			init:
		)
		(aBigMouth
			view:				vReceptionist
			posn:				223 1042
			setLoop:			lBigBubbles		;**	lBigChewing
			setPri:			15
			setCycle:		Forward
			cycleSpeed:		3
			init:
		)

		((= aGuy (Actor new:))				;**	Guy that occasionally walks out
			view:				vShowBizGuy
			posn:				33 1118
			illegalBits:	0
			setCycle:		Walk
			init:
			setScript:		guyScript
		)
		((= aDoll (Actor new:))				;**	Girl that (dittos.)
			view:				vShowBizDoll
			posn:				288 1118
			illegalBits:	0
			setCycle:		Walk
			init:
			setScript:		dollScript
		)
		((= aReceptionist (Prop new:))	;**	Girl behind desk.
			view:				vReceptionist
			setLoop:			lSmile
			posn:				158 119
			cycleSpeed:		1
			setPri:			9
			ignoreActors:
			init:
			setScript:		receptionistScript
		)

		(NormalEgo)

		((= aDoorWest (Prop new:))			;**	left door
			view:				vTVreception
			ignoreActors:
			setLoop:			1
			posn:				58 107
			setPri:			7
			stopUpd:
			init:
		)
		((= aDoorEast (Prop new:))			;**	right door
			view:				vTVreception
			ignoreActors:
			setLoop:			2
			posn:				259 107
			setPri:			7
			stopUpd:
			init:
		)
		((= aDoorNorth (Prop new:))		;**	rear door
			view:				vTVreception
			ignoreActors:
			setLoop:			3
			posn:				145 80
			setPri:			4
			stopUpd:
			init:
		)

		(self	setScript: rm101Script)

		(if (== prevRoomNum 104)
			(ego
				posn: 			atDoorEast
				loop:				facingWest
				init:
			)
		else										;**	can't enter from other 2 doors
			(ego
				posn: 			atFrontDoor
				init:
			)
		)
	)
)


(instance rm101Script of Script
	(method (doit)
		(super doit:)

		(cond
			((& (ego onControl:) cCyan)
				(curRoom newRoom: 11)
			)
			((and	(ego inRect: triggerBoy)	;**	If in right-rear corner...
					(not (guyScript state?)))
				(guyScript changeState: doDoor)
			)
			((and (ego inRect: triggerDoll)	;**	if in left-rear corner...
					(not (dollScript state?)))
				(dollScript changeState: doDoor)
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'give, hand, show/ticket')
			(cond
				((ego has: iCruiseTicket)
					(event claimed: FALSE)
					(return)
				)
				((not (ego has: iLotteryTicket))
					(DontHave)
				)
				((not (ego inRect: nearDesk))
					(NotClose)
				)
				(else
					(self changeState: showedTicket)
				)
			)
		)

		(if (Said 'look>')
			(if (Said '/door')
				(Print "Every door here is locked,
					except the doors leading back to the street.
					There are signs over the doors.")
			)

			(if (Said '/lamp')
				(Print "It's just a modern lighting fixture,
					not a trapeze upon which she performs!")
			)

			(if (Said '/wall, sign')
				(Print "The sign over the west door says \"The Dating Connection,
					CLOSED SET.\"")
				(Print "The sign over the east door says \"Lucky Life Lottery,
					CLOSED SET.\"")
				(Print "The sign over the north door says \"Green Room.\"")
				(Print "The sign over the south door says \"EXIT.\"")
			)

			(if (Said '/picture')
				(Print "Don't you love modern art.")
			)

			(if (Said '/window')
				(Print "She has a nice pair of skylights, eh?")
			)

			(if (Said '/girl, skylight, desk')
				(aBigClerk
					posn:				223 42
					stopUpd:
				)
				(aBigMouth
					posn:				223 42
				)
				(receptionistScript changeState: bubbleDone)
				(Timer setReal: aBigClerk 5)
				(Timer setReal: aBigMouth 5)
				(HandsOff)
			)

			(if (Said '[/room, studio, lobby, building, wall]')
				(Print "The television studio lobby is lavishly decorated
					in the latest trend.
					A receptionist sits in the center of the room.
					There are doors in every wall.")
			)
		)

		(if (Said 'open/door')
			(Print "The only door you can open is the door you used to
				enter from the street.")
		)

		(if (Said '/reporter')
			(Print "Guess there must have been a fast-breaking story
				somewhere else!")
		)

		(if (Said 'talk, ask/girl')
			(Print (Format @string
				"\"Hello, big, blonde and beautiful,\" you say.
				%s" laffer))
			(Print "\"Hiya,\" she blurts between bubbles,
				\"kin I helpya?\"")
		)

		(if (Said 'yes')
			(Print "\"Sure you can help me,\" you tell the girl, 
				\"can you get out from behind that counter?\"")
			(Print "(She ignores your feeble attempt at humor.)" LOW)
		)

	)


	(method (changeState newState)
;**	(Print (Format @string "newState = %d" newState))
		(switch (= state newState)

			;**	(intro)

			(showedTicket
				(HandsOff)
				(receptionistScript changeState: talkingToLarry)
				(Print "\"Say,\" you ask the receptionist,
					\"is this lottery ticket any good?\""
					icon: iLotteryTicket 0 0
				)
				(= seconds 4)
			)

			(waitingForNumber
				(= myNum1 (Random 100 999))
				(= myNum2 (Random 100 999))
				(= myNum3 (Random 100 999))
				(= myNum4 (Random 100 999))
				(= myNum5 (Random 100 999))
				(= myNum6 (Random 100 999))
				(Print 
					(Format @string 
						"\"I don't know,\" she replies, \"I've misplaced my glasses!
						As best I can remember, 
						this week's Lucky Life Lottery Luck-O Buck-O numbers are: 
						%d, %d, %d, %d, %d, and %d.
						What six numbers do you have?\"" 
					myNum1
					myNum2
					myNum3
					myNum4
					myNum5
					myNum6
					)
				)
				(= string 0)
				(= hisNum1 0)
				(while (or 	( < hisNum1 100)
								( > hisNum1 999))
					(= hisNum1 (GetNumber {Pick #1}))
				)
				(= hisNum2 0)
				(while (or 	( < hisNum2 100)
								( > hisNum2 999))
					(= hisNum2 (GetNumber {Pick #2}))
				)
				(= hisNum3 0)
				(while (or 	( < hisNum3 100)
								( > hisNum3 999))
					(= hisNum3 (GetNumber {Pick #3}))
				)
				(= hisNum4 0)
				(while (or 	( < hisNum4 100)
								( > hisNum4 999))
					(= hisNum4 (GetNumber {Pick #4}))
				)
				(= hisNum5 0)
				(while (or 	( < hisNum5 100)
								( > hisNum5 999))
					(= hisNum5 (GetNumber {Pick #5}))
				)
				(= hisNum6 0)
				(while (or 	( < hisNum6 100)
								( > hisNum6 999))
					(= hisNum6 (GetNumber {Pick #6}))
				)
				(= seconds 3)
			)

			(processNumber
				(if (or	(!= myNum1 hisNum1)
							(!= myNum2 hisNum2)
							(!= myNum3 hisNum3)
							(!= myNum4 hisNum4)
							(!= myNum5 hisNum5)
							(!= myNum6 hisNum6))
					(self changeState: noWayPirate)
				else
					(theGame changeScore: 10)
					(Print "\"Why, that's correct!\" she replies excitedly.
						\"You ARE a lucky guy!
						The last Lucky Life Lottery show of the season is
						being taped RIGHT NOW!
						I'll notify the director that you're here;
						I'm sure you'll be called immediately.\"")
					(Print "\"I'll unlock the door to the green room,
						so you can wait there.\"")	 
					(= seconds 2)
					(ego put: iLotteryTicket LIMBO)
				)
			)

			(gotoGreenRoom
				(ego setMotion:	MoveTo 228 146 self)
			)

			(atEndOfCounter
				(Print "\"Oh, by the way,\" says the receptionist,
					\"don't get nervous just because you're on live television
					and being watched by millions of people!\"")
				(Print "She chuckles softly under her breath.")
				(ego setMotion:	MoveTo 228 117 self)
			)

			(atRearOfCounter
				(ego setMotion:	MoveTo 162 82 self)
			)

			(atDoor
				(aDoorNorth
					setCycle:		EndLoop self
				)
			)

			(doorDone
				(ego
					illegalBits:	0
					setMotion:		MoveTo 162 77 self
				)
			)

			(nextRoom
				(curRoom newRoom: 102)
			)


			;**	We jump to here if Larry didn't match the lottery number:

			(noWayPirate
				(= cycles	0)
				(= seconds	0)
				(Print "\"Well, that's too bad,\" she replies. \"I'm sorry, 
					but you cannot get on the big TV show unless
					you have all six lucky numbers.\"")
				(Print "\"Why don't you try again when you're a winner!\"")
				(User
					canControl:		TRUE
					canInput:		TRUE
				)
				(receptionistScript changeState: girlInit)
			)
		)
	)
)


(instance receptionistScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(girlInit
				(= cycles	0)
				(= seconds (Random 3 7))
			)

			(preBubble
				(aReceptionist
					setLoop:		lPreBubble
					cel:			0
					setCycle:	EndLoop self
				)
			)

			(blowBubble1
				(aReceptionist
					setLoop:		lBlowBubble
					cel:			0
					setCycle:	EndLoop self
				)
			)

			(blowBubble2
				(aReceptionist
					cel:			0
					setCycle:	EndLoop self
				)
			)

			(postBubble
				(aReceptionist
					setLoop:		lPreBubble
					setCel:		LastCel
					setCycle:	BegLoop self
				)
			)

			(bubbleDone
				(= cycles	0)
				(= seconds	0)
				(aReceptionist
					setLoop:		lSmile
				)
				(if (> (Random 1 3) 1)
					(self changeState: girlInit)
				else
					(= seconds (Random 3 7))
				)
			)

			(getPhone
				(aReceptionist
					setLoop:		lLiftPhone
					cel:			0
					setCycle:	EndLoop self
				)
			)

			(talk1
				(aReceptionist
					setLoop:		lTalkPhone
					setCycle: 	Forward
				)
				(= seconds (Random 2 4))
			)

			(listen1
				(aReceptionist
					setCel:		0
				)
				(= seconds (Random 3 7))
			)

			(talk2
				(aReceptionist
					setLoop:		lTalkPhone
					setCycle: 	Forward
				)
				(= seconds (Random 2 4))
			)

			(listen2
				(aReceptionist
					setCel:		0
				)
				(= seconds (Random 3 7))
			)

			(talk3
				(aReceptionist
					setLoop:		lTalkPhone
					setCycle: 	Forward
				)
				(= seconds (Random 1 3))
			)

			(putPhone
				(aReceptionist
					setLoop:		lLiftPhone
					setCel:		LastCel
					setCycle:	BegLoop self
				)
			)

			(phoneDone
				(self changeState: girlInit)
			)

			(talkingToLarry
				(= cycles	0)
				(aReceptionist
					setLoop:		lTalk
					setCycle:	Forward
				)
				(= seconds (Random 2 4))
			)

			(waitingToTalkToLarry
				(aReceptionist
					setLoop:		lSmile
				)
			)
		)
	)
)


(instance guyScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(doDoor
				(aDoorWest
					setCycle:		EndLoop self
				)
			)

			(guyDoorOpen
				(aGuy
					posn:			33 118
					setMotion:	MoveTo 72 118 self
				)
			)

			(walkGuy
				(aGuy setMotion:	MoveTo 132 182 self)
				(aDoorWest
					setCycle:		BegLoop self
				)
			)

			(lockDeDoor
				(aDoorWest			stopUpd:)
			)

			(walkOffScreen
				(aGuy setMotion: MoveTo 132 232 self)
			)

			(eraseGuy
				(aGuy dispose:)
				(Print msgHey)
			)
		)
	)
)


(instance dollScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(doDoor
				(aDoorEast
					setCycle:		EndLoop self
				)
			)

			(guyDoorOpen
				(aDoll
					posn:			288 118
					setMotion:	MoveTo 258 118 self
				)
			)

			(walkGuy
				(aDoll setMotion:	MoveTo 162 182 self)
				(aDoorEast
					setCycle:		BegLoop self
				)
			)

			(lockDeDoor
				(aDoorEast			stopUpd:)
			)

			(walkOffScreen
				(aDoll setMotion: MoveTo 162 232)
			)

			(eraseGuy
				(aDoll dispose:)
				(Print msgHey)
			)
		)
	)
)


(instance aBigClerk of Prop
	(method (cue)
		(Print "You find dull women sexy.")
		(Print "(But then, you find any woman sexy!)" at: 55 155 width: 210)
		(self posn:	223 1042)
	)
)


(instance aBigMouth of Prop
	(method (cue)
		(self posn:	223 1042)
		(HandsOn)
	)
)

