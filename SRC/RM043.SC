;**
;**	Logics for room 43 -- Resort Restaurant
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 26, 1988
;**


(script#				43)

(public
	rm43				0
)


(define	nearKnife		 32 142	55 161)
(define	seeKnife			 30 130	80 176)
(define	nearChair		175 160 241 189)
(define	nearTable		266 111 296 124)
(define	nearTableChair 268 111 290 116)
(define	nearPodium		170 155 194 179)

(define	manX			188)					;**	x-coordinate where they begin
(define	womanX		204)
(define	coupleY		234)					;**	y-coordinate where they begin
(define	coupleMin	2)
(define	coupleMax	4)

(define	aroundT5		209 139)				;**	clear of first table
(define	topCenter	209 102)				;**	from here; turn to tables 1-4
(define	bottomStep	140 101)
(define	topStep		122  89)


;(define	vEgoSitting					101)
;(define	vRestaurantWaiter			432)
	(define	lGetTable		2)
	(define	lGetChair		3)

;(define	vRestaurant					433)	;**	if changes, change rm8 too
	(define	lDoor				1)
	(define	lPaintings		2)
	(define	lTables			3)				;**	cels 0 & 1
	(define	lLarryChair		3)				;**	cel2
	(define	lRope				4)
	(define	lChair			5)
	(define	lTray				6)
	(define	lRopeDone		7)

;(define	vRestaurantMaitreD		434)
	(define	lMovingRope		4)
	(define	lTalk				5)

;(define	vRestaurantMan				435)
;(define	vRestaurantWoman			438)
	(define	lSitting			4)


(local
	knifeHere
	tookKnifeThisTime 
	preventInput
	MDatPodium
)


;**	(enum)		rmScript state values are wired

(enum												;**	SITscript state values
	SITinit
	SITgoToChair
	SIT2
	SIT3
	SITfidget
	SITstand
	SITstanding
)

(enum												;**	TSscript values
	TSgoToChair
	TSsit
	TSsit3
	TSstand
	TSstanding
)

(enum 											;**	MDscript state values
	1	MDstart
	8	MDreturn
	12	MDdone
)


(instance rm43 of Room
	(properties
		picture:		43
		horizon:		5
		south:		40
	)

	(method (init)
		(Load VIEW vRestaurant)
		(super init:)

		(if ((inventory at: iKnife) ownedBy: curRoomNum)
			(= knifeHere TRUE)
		)
	
		((View new:)							;**	cheese
			view: 			vRestaurant
			posn: 			22 135
			setPri: 			6
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	salad
			view: 			vRestaurant
			cel: 				1
			posn: 			63 122
			setPri: 			6
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	otherFood
			view: 			vRestaurant
			cel: 				2
			posn: 			37 164
			setPri: 			13
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	T1
			view: 			vRestaurant
			loop: 			lTables
			cel: 				1
			posn: 			116 82
			setPri: 			3
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	T2
			view: 			vRestaurant
			loop: 			lTables
			posn: 			65 120
			setPri: 			7
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	T3
			view: 			vRestaurant
			loop: 			lTables
			cel: 				1
			posn: 			178 101
			setPri: 			5
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	T4
			view: 			vRestaurant
			loop: 			lTables
			cel: 				1
			posn: 			242 101
			setPri: 			5
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	T5
			view: 			vRestaurant
			loop: 			lTables
			posn: 			168 144
			setPri: 			9
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	larryChair
			view: 			vRestaurant
			loop: 			lLarryChair
			cel: 				2
			posn: 			230 172
			setPri: 			13
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	west picture
			view: 			vRestaurant
			loop: 			lPaintings
			cel: 				1
			posn: 			53 78
			setPri: 			4
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	east picture
			view: 			vRestaurant
			loop: 			lPaintings
			posn: 			178 56
			setPri: 			2
			ignoreActors:
			addToPic:
		)

		
		(aChair
			setPri:			7
			init:
			hide:
		)
		(aTable
			setPri:			8
			init:
			hide:
		)
		(Load VIEW vEgoSitting)
		(Load VIEW vRestaurantMaitreD)
		(Load VIEW vRestaurantMan)
		(Load VIEW vRestaurantWoman)
		(Load VIEW vRestaurantWaiter)

		(aRope
			setPri:			12
			ignoreActors:
			stopUpd:
			init:
		)
		(aMaitreD
			setCycle:		Walk
			ignoreActors:
			stopUpd:
			init:
		)
		(aMan
			setCycle:		Walk
			ignoreActors:
			init:
		)
		(aWoman
			setCycle:		Walk
			ignoreActors:
			init:
		)

		(= MDatPodium TRUE)
		(NormalEgo facingNorth)
		(ego
			posn: 			195 179
			observeControl:cYellow
			init:
		)

		(self
			;** DO NOT	setRegions:		rgResort
			setScript: 		rm43Script
		)
	)
)


(instance rm43Script of Script
	(method (doit)

		(super doit:)

		(if (and	(== currentStatus SITTING)
					(== 0 (mod state 20)))
			(self cue:)
		)

		(if preventInput
			(User canInput: FALSE)
		else
			(User canInput: TRUE)
		)

		(if ( < state 101)
			(ego observeControl: cYellow)
		)

		(if (& (ego onControl:) cBlue)
			(curRoom newRoom: 40)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look<under/table')
			(Print "You stick your head under the table nearest you.")
			(Print "(You see nothing you wish to share at this time!)" LOW)
		)

		(if (Said 'look>')
			(if (Said '/food, plate, table')
				(cond
					((ego inRect: seeKnife)
						(if knifeHere
							(Print "Why look!
								There's a knife lying next to the cheese plate.")
						else
							(Print "There's nothing left but food.")
						)
					)
					((== currentStatus SITTING_AT_TABLE)
						(Print "Your table conveniently arrived with food
							attached!")
					)
					((ego inRect: nearTable)
						(Print "It's not much, but it's yours!")
					)
					(( < state 3)
						(Print "They aren't crowded!
							Perhaps you should talk to the Maitre d' about
							dining here.")
					)
					(else
						(Print "There are people sitting at the tables.")
					)
				)
			)

			(if (Said '/chair')
				(Print "It's a nice chair.")
			)

			(if (Said '/man, waiter, woman, people')
				(cond
					((not state)
						(Print "There's no one here except that snooty Maitre d'.")
					)
					(( < state 15)
						(Print "They make a lovely couple.")
					)
					(else
						(Print "They certainly sit still!")
						(Print "And, what a dress code!!")
					)
				)
			)

			(if (Said '/crackers, silver, dish')
				(Print "Nothing is too expensive for this place.")
			)

			(if (Said '[/room, restaurant]')
				(Print "Expensive linen, crystal and silver weigh down the tables.
					An appetizer buffet fills one corner.")
			)
		)

		(if (Said '/rope')
			(Print "It's just hanging around.")
		)

		(if (or	(Said 'talk, ask/maitre')
					(Said 'talk, ask/man/table')
					(Said 'talk, ask/man<table')
					(Said 'talk, ask/table<man')
					(Said 'talk, ask'))
			(cond
				(( > state 100)
					(Print "\"No one at this table is interested in speaking
						with you.\"")
				)
				((!= currentEgoView vEgo)
					(Print "The Maitre d' stops you by growling angrily,
						\"Sir! Our strict dress code forbids us serving anyone
						that looks like you! Please leave -- NOW!\"")
				)
				((== knifeHere FALSE)
					(Print "\"We have a limit of one TV tray of food per
						customer,\" says the Maitre d' snootily.
						\"Please leave... NOW!\"")
				)
				((== MDatPodium FALSE)
					(Print "Wait until the maitre d' returns to his position.")
				)
				(else
					(Print "\"Uh, excuse me, sir,\" you say hesitantly,
						\"I was wondering if I might get a bite to eat?\"")
					(Print "The Maitre d' slowly diverts his gaze toward you.")

					(if (!= currentEgoView vEgo)
						(Print "\"Certainly not!\" he sputters,
							\"appropriate dress is always required here!\"")
					else
						(= talkedToMD TRUE)
						(Print "\"Your name, please?\" he asks.")
						(Print laffer)
						(Print "\"Well, I suppose I could try to find a place
							for you on our busy schedule,\" he replies disdainfully.
							\"Just have a seat there in the chair while
							I prepare a table for you.\"")
					)
				)
			)
		)

		(if (Said 'get, (pick<up), eat/food')
			(cond
				((ego inRect: seeKnife)
					(if knifeHere
						(Print "Grabbing the knife lying beside the
							cheese platter, you carve yourself off a huge
							hunk of cheddar and pop it into your mouth.")
						(Print "Not bad.")
					else
						(Print "You glom up a cracker full of brie,
							and slip it into your mouth.")
						(Print "You've never liked brie.")
					)
				)
				((== currentStatus SITTING_AT_TABLE)
					(self changeState: 121)
				)
				((ego inRect: nearTable)
					(Print "You reach over to your table, and grab a
						handful of oyster crackers, popping them into your mouth.
						(You've loved these things ever since you were a kid!)")
				)
				(( < state 3)
					(Print "A good idea.
						Perhaps you should talk to the Maitre d'.")
				)
				(else
					(Print "There's nothing nearby to eat.")
				)
			)
		)

		(if (Said '(pick<up), get/crackers, silver, dish')
			(Print "That won't help you.")
		)

		(if (Said '(pick<up), get/knife')
			(cond
				((== knifeHere FALSE)
					(AlreadyTook)
				)
				((not (ego inRect: nearKnife))
					(NotClose)
				)
				(else
					(Print "The Maitre d' DID say to help yourself to the
						buffet, but this is rather borderline behavior, Larry!")
					(Print "Oh, ok; you surreptitiously slip the slicer
						from the serving surface and sequester it in your suit.")
					(ego get: iKnife)
					(= knifeHere FALSE)
					(= tookKnifeThisTime TRUE)
					(theGame changeScore: 3)
				)
			)
		)

		(if (Said 'sit')
			(cond
				((or	(== currentStatus SITTING)
						(== currentStatus SITTING_AT_TABLE))
					(Print "You are!")
				)
				((!= currentEgoView vEgo)
					(Print "The Maitre d' stops you by growling angrily,
						\"Sir! Our strict dress code forbids us serving anyone
						that looks like you! Please leave... NOW!\"")
				)
				((and	(== knifeHere FALSE)
						(== tookKnifeThisTime FALSE))
					(Print "\"We have a limit of one TV tray of food per
						customer,\" says the Maitre d' snootily.
						\"Please leave... NOW!\"")
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				((ego inRect: nearTable)
					(if (not (ego inRect: nearTableChair))
						(Print "Please move between the table and chair if you
							want to sit down at your table.")
					else
						(Ok)
						(ego setScript:	TSscript)
						(TSscript changeState: TSgoToChair)
					)
				)
				((not (ego inRect: nearChair))
					(NotClose)
				)
				((not talkedToMD)
					(Print "Perhaps you should talk to the man at the podium
						first.")
				)
				(else
					(Ok)
					(if (not scoredRm43Sit)
						(= scoredRm43Sit TRUE)
						(theGame changeScore: 1)
					)
					(ego setScript:	SITscript)
					(SITscript changeState: SITgoToChair)
				)
			)
		)

		(if (Said 'rise, (stand<up), (get<up)')
			(cond
				((== currentStatus NORMAL)
					(YouAre)
				)
				((== currentStatus SITTING)
					(Ok)
					(ego setScript:	SITscript)
					(SITscript changeState: SITstand)
				)
				((== currentStatus SITTING_AT_TABLE)
					(Ok)
					(ego setScript:	TSscript)
					(TSscript changeState: TSstand)
				)
				(else
					(Print "Not now!")
				)
			)
		)

		(if (and	( < state 101)
					(ego inRect: nearPodium)
					(== MDatPodium TRUE)
					(or	(Said 'give/man/money')
							(Said 'tip, bribe/waiter, man')))
			(Print "\"Here's a hundred dollars,\"
				you inform the Maitre d'.
				\"May I please have a nice table and something to eat?\"")
			(Print "\"Thank you, Sir.
				Just have a seat in the chair.\"")
		)

		(if (Said 'drink')
			(Print "That Grotesque Gulp was enough to quench your
				thirst for the rest of this game!")
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(intro)


			;**	Larry asks to eat; man says sit down. Bring in the bigwigs.

			(1
				(aMan setScript: enterScript)
				(enterScript changeState: 1)
			)

			(2
				(Print 
					"\"Why, Mr. and Mrs. Rich,\" says the Maitre d'. \"What 
					a pleasure to see
					you again. Of course I have a table for you!\"")
			)

			(3
				(Print "Well, that was certainly rude! You were here before
								them. You feel yourself becoming perturbed!")
				(aMaitreD	setScript:	(to1Script new:))
				(= preventInput FALSE)
				(= cycles 15)
				(= state 8)
			)

			(9
				(aWoman		setScript:	(to1Script new:))
				(= cycles 7)
			)

			(10
				(aMan			setScript:	(to1Script new:))
			)

			(11
				(groupScript init:)
				(aMaitreD	setMotion:	MoveTo 80 82 groupScript)
			)

			(12
				(aWoman		setMotion:	MoveTo 95 77 groupScript)
			)

			(13
				(aMan			setMotion:	MoveTo 135 77 groupScript)
			)

			(14
				(aMaitreD	loop:			facingEast)
				(aWoman		loop:			facingEast)
				(aMan			loop:			facingWest)
				(= seconds 3)
			)

			(15
				(aMan			hide:)
				(aWoman		hide:)
				((View new:)
					view:		vRestaurantWoman
					loop:		lSitting
					posn:		99 74				;**	womanOffset -17/-8
					setPri:	4
					ignoreActors:
					addToPic:
				)
				((View new:)
					view:		vRestaurantMan
					loop:		lSitting
					posn:		135 74			;**	manOffset	+19/-8
					setPri:	4
					ignoreActors:
					addToPic:
				)
				(Print "\"I hope you find this satisfactory, Sir,\"
					grovels the Maitre d' with his palm extended." draw:)
				(= seconds 3)
			)

			(16
				(aMaitreD	setMotion:	MoveTo topStep self)
			)

			(17
				(aMaitreD	setMotion:	MoveTo bottomStep self)
			)

			(18
				(MDscript changeState: MDreturn)
			)

			(19
				(= seconds (Random coupleMin coupleMax))	;**	will bump to 20
			)


			;**	Now we're readyForT2, if Larry is still sitting down.

			(21
				(aMan setScript: enterScript)
				(enterScript changeState: 1)
			)

			(22
				(Print 
					"\"Why, Mr. and Mrs. Famous,\" says the Maitre d'. 
					\"What a pleasure to see
					you again. Of course I have a table for you!\"")
			)

			(23
				(Print "Well, that was certainly rude! You were here before
								them. You feel yourself becoming annoyed!")
				(aMaitreD	setScript:	(to2Script new:))
				(= cycles 15)
				(= state 27)
			)

			(28
				(aWoman		setScript:	(to2Script new:))
				(= cycles 7)
			)

			(29
				(aMan			setScript:	(to2Script new:))
			)

			(30
				(groupScript init:)
				(aMaitreD	setMotion:	MoveTo 68 90 groupScript)
			)

			(31
				(aWoman		setMotion:	MoveTo 47 105 groupScript)
			)

			(32
				(aMan			setMotion:	MoveTo 90 105 groupScript)
			)

			(33
				(aMaitreD	loop:			facingEast)
				(aWoman		loop:			facingEast)
				(aMan			loop:			facingWest)
				(= seconds 3)
			)

			(34
				(aMan			hide:)
				(aWoman		hide:)
				((View new:)
					view:		vRestaurantWoman
					loop:		lSitting
					posn:		44 111			;**	womanOffset -17/-9
					setPri:	8
					ignoreActors:
					addToPic:
				)
				((View new:)
					view:		vRestaurantMan
					loop:		lSitting
					posn:		87 110			;**	manOffset	+22/-10
					setPri:	8
					ignoreActors:
					addToPic:
				)
				(Print "\"I hope you find this satisfactory, Sir,\"
					grovels the Maitre d' with his palm extended." draw:)
				(= seconds 3)
			)

			(35
				(aMaitreD	setMotion:	MoveTo topStep self)
			)

			(36
				(aMaitreD	setMotion:	MoveTo bottomStep self)
			)

			(37
				(MDscript changeState: MDreturn)
			)

			(38
				(= state 39)
				(= seconds (Random coupleMin coupleMax))	;**	will bump to 40
			)


			;**	Now we're readyForT3, if Larry is still sitting down.

			(41
				(aMan setScript: enterScript)
				(enterScript changeState: 1)
			)

			(42
				(Print 
					"\"Why, Mr. and Mrs. Gates,\" says the Maitre d'. 
					\"What a pleasure to see
					you again. Of course I have a table for you!\"")
			)

			(43
				(Print "Well, that was certainly rude! You were here before
								them. You feel yourself becoming indignant!")
				(aMaitreD	setScript:	(to3Script new:))
				(= preventInput FALSE)
				(= cycles 15)
				(= state 47)
			)

			(48
				(aWoman		setScript:	(to3Script new:))
				(= cycles 7)
			)

			(49
				(aMan			setScript:	(to3Script new:))
			)

			(50
				(groupScript init:)
				(aMaitreD	setMotion:	MoveTo 147 101 groupScript)
			)

			(51
				(aWoman		setMotion:	MoveTo 158 96 groupScript)
			)

			(52
				(aMan			setMotion:	MoveTo 202 97 groupScript)
			)

			(53
				(aMaitreD	loop:			facingEast)
				(aWoman		loop:			facingEast)
				(aMan			loop:			facingWest)
				(= seconds 3)
			)

			(54
				(aMan			hide:)
				(aWoman		hide:)
				((View new:)
					view:		vRestaurantWoman
					loop:		lSitting
					posn:		161 93			;**	womanOffset -17/-8
					setPri:	6
					ignoreActors:
					addToPic:
				)
				((View new:)
					view:		vRestaurantMan
					loop:		lSitting
					posn:		197 93			;**	manOffset	+19/-8
					setPri:	6
					ignoreActors:
					addToPic:
				)
				(Print "\"I hope you find this satisfactory, Sir,\"
					grovels the Maitre d' with his palm extended." draw:)
				(= seconds 3)
			)

			(55
				(MDscript changeState: MDreturn)
			)

			(56
				(= state 59)
				(= seconds (Random coupleMin coupleMax))
			)


			;**	Now we're readyForT4, if Larry is still sitting down.

			(61
				(aMan setScript: enterScript)
				(enterScript changeState: 1)
			)

			(62
				(Print 
					"\"Why, Mr. Leach,\" says the Maitre d'. 
					\"What a pleasure to see
					you again. Of course I have a table for you!\"")
			)

			(63
				(Print "Well, that was certainly rude! You were here before
								them. You feel yourself becoming incensed!")
				(aMaitreD	setScript:	(to4Script new:))
				(= cycles 15)
				(= preventInput FALSE)
				(= state 67)
			)

			(68
				(aWoman		setScript:	(to4Script new:))
				(= cycles 7)
			)

			(69
				(aMan			setScript:	(to4Script new:))
			)

			(70
				(groupScript init:)
				(aMaitreD	setMotion:	MoveTo 277 100 groupScript)
			)

			(71
				(aWoman		setMotion:	MoveTo 220 96 groupScript)
			)

			(72
				(aMan			setMotion:	MoveTo 263 97 groupScript)
			)

			(73
				(aMaitreD	loop:			facingWest)
				(aWoman		loop:			facingEast)
				(aMan			loop:			facingWest)
				(= seconds 3)
			)

			(74
				(aMan			hide:)
				(aWoman		hide:)
				((View new:)
					view:		vRestaurantWoman
					loop:		lSitting
					posn:		225 93			;**	womanOffset -17/-8
					setPri:	6
					ignoreActors:
					addToPic:
				)
				((View new:)
					view:		vRestaurantMan
					loop:		lSitting
					posn:		261 93			;**	manOffset	+19/-8
					setPri:	6
					ignoreActors:
					addToPic:
				)
				(Print "\"I hope you find this satisfactory, Sir,\"
					grovels the Maitre d' with his palm extended." draw:)
				(= seconds 3)
			)

			(75
				(MDscript changeState: MDreturn)
			)

			(76
				(= state 79)
				(= seconds (Random coupleMin coupleMax))
			)


			;**	Now we're readyForT5, if Larry is still sitting down.

			(81
				(aMan setScript: enterScript)
				(enterScript changeState: 1)
			)

			(82
				(Print 
					"\"Why, Mr. and Mrs. Williams,\" says the Maitre d'. 
					\"What a pleasure to see
					you again. Of course I have a table for you!\"")
			)

			(83
				(Print "Well, that was certainly rude! You were here before
								them. You feel yourself becoming enraged!")
				(aMaitreD	setMotion:	MoveTo 190 142 self)
				(= preventInput FALSE)
				(= state 87)
			)

			(88
				(aMaitreD	setMotion:	MoveTo 134 139 self)
			)

			(89
				(aMaitreD	loop:			facingEast)
				(aWoman		setMotion:	MoveTo 190 142 self)
			)

			(90
				(aWoman		setMotion:	MoveTo 149 136 self)
			)

			(91
				(aWoman		loop:			facingEast)
				(aMan			setMotion:	MoveTo 190 142 self)
			)

			(92
				(aMan			setMotion:	MoveTo 189 136 self)
			)

			(93
				(aMan			loop:			facingWest)
				(= seconds 3)
			)

			(94
				(aMan			hide:)			;**	he becomes a waiter later
				(aWoman		dispose:)
				((View new:)
					view:		vRestaurantWoman
					loop:		lSitting
					posn:		151 135			;**	womanOffset -17/-9
					setPri:	10
					ignoreActors:
					addToPic:
				)
				((View new:)
					view:		vRestaurantMan
					loop:		lSitting
					posn:		190 134			;**	manOffset	+22/-10
					setPri:	10
					ignoreActors:
					addToPic:
				)
				(Print "\"I hope you find this satisfactory, Sir,\"
					grovels the Maitre d' with his palm extended." draw:)
				(= seconds 3)
			)

			(95
				(aMaitreD	setMotion:	MoveTo 154 167 self)
			)

			(96
				(aMaitreD	loop:	facingEast)
				(= MDatPodium TRUE)
				(= state 99)
				(= seconds 3)
			)


			;**	Now we're readyForLarry, if he's still sitting down.

			(101
				(= preventInput TRUE)
				(User
					canControl: 	FALSE
				)
				(= seconds 0)
				(Print "\"All right, sir,\" the Maitre d' says begrudgingly,
					\"I'll show you to your table now.\"")
				(Print "(Sure, now that there are no tables left!)" LOW)
				(MDscript changeState: MDstart)
				(ego
					setScript:		FALSE
					setLoop: 		0
					setCel:			LastCel
					setCycle: 		BegLoop self
					setMotion:		FALSE
				)
			)

			(102
				(aRope
					setLoop:			lRopeDone
					stopUpd:
				)
				(ego
					view:				vEgo
					setLoop:			facingSouth
					setLoop:			RELEASE
					cycleSpeed:		0
					setCycle:		Walk
					illegalBits:	0
					ignoreActors:
					setMotion:		MoveTo 193 172 self
				)
			)

			(103
				(ego	setMotion:	MoveTo 193 150 self)
			)

			(104
				(= seconds 2)
			)

			(105
				(Print "\"Please, walk this way.\"")
				(Print "You think to yourself,
					\"If I walked that way, I'd use talcum powder!\"")
				(aMaitreD	setScript:	(toLarryScript new:))
				(= cycles 10)
			)

			(106
				(ego			setScript:	(toLarryScript new:))
			)

			(107
				(aMaitreD	setMotion:	MoveTo 253 105 self)
			)

			(108
				(ego			setMotion:	MoveTo 240 113 self)
			)

			(109
				(ego setLoop:		facingEast)
				(Print "\"Oh, boy,\" says the Maitre d',
					sharply clapping his hands,
					\"please set up a table for Mr. Laffer!\"" draw:)
				(= seconds 3)
			)

			(110
				(aMan
					view:				vRestaurantWaiter
					setLoop:			RELEASE
					loop:				facingWest
					posn: 			333 110
					setPri:			4
					show:
					setMotion:		MoveTo 275 110 self
				)
			)

			(111
				(aMan
					loop:				facingEast	;**	w/o table, chair
				)
				(= cycles 5)
			)

			(112
				(aChair				show:)
				(aTable				show:
					setCycle:		EndLoop self)
			)

			(113
				(aChair
					setCycle:		EndLoop self
				)
			)

			(114
				(Print "\"Be seated, Mr. Laffer,\" says the Maitre d'
					as he escapes to the kitchen.
					\"Feel free to help yourself at our appetizer bar,
					and, oh yes, I almost forgot:\"" HIGH draw:)
				(theGame changeScore: 1)
				(Print (Format @string "\"%s\"" tritePhrase) HIGH)
				(aTable				stopUpd:)
				(aChair				stopUpd:)
				(aMan
					setPri:			6
					setMotion:		MoveTo 311 110
				)
				(aMaitreD
					setPri:			6
					setMotion:		MoveTo 311 110 self
				)
			)

			(115
				(aMan					dispose:)
				(aMaitreD			dispose:)
				((View new:)							;**	formerly a Door
					ignoreActors:
					view:				vRestaurant
					loop:				lDoor
					posn: 			287 64
					setPri: 			4
					addToPic:
				)
				(Print "(Choice location, Larry.)" LOW draw:)
				(NormalEgo)
				(= preventInput FALSE)
			)


			;**	This path happens if Larry tries some of the local cuisine.

			(121
				(= preventInput TRUE)
				(User
					canControl: 	FALSE
				)
				(= currentStatus NEAR_DEATH)
				(ego
					setMotion:		FALSE
				)
				(Print "Ummm.")
				(Print "Yummy.")
				(= seconds 3)
			)

			(122
				(Print "Bad idea, Larry.")
				(Print "For a while, you are afraid you are going to die.
					Then you are afraid you are NOT going to die!")
				(= seconds 3)
			)

			(123
				(Print "Your last thoughts are,
					\"Maybe it was something I ate...\"")
				(= currentStatus DEAD)
			)
		)
	)
)


(instance SITscript of Script
	(method (changeState newState)
		(switch (= state newState)

			(SITgoToChair
				(if seconds (= seconds 0))
				(= preventInput TRUE)
				(User
					canControl: 	FALSE
				)
				(if ( < (ego y?) 170)
					(ego setMotion:	MoveTo 200 170 self)
					(-- state)
				else
					(ego setMotion:	MoveTo 227 172 self)
				)
			)

			(SIT2
				(ego
					view:				vEgoSitting
					setLoop: 		0
					setCel: 			0
					cycleSpeed:		2
					setMotion:		FALSE
					setCycle: 		EndLoop self
				)
			)

			(SIT3
				(= currentStatus SITTING)
				(= preventInput FALSE)
				(= seconds (Random 2 6))
			)


			;**	Here we fidget while waiting for him to say \"Stand up.\"

			(SITfidget
				(ego setLoop: (Random 1 2))
				(if (== (ego cel?) 0)
					(ego setCycle: EndLoop)
				else
					(ego setCycle: BegLoop)
				)
				(-- state)
				(= seconds (Random 2 6))
			)

			(SITstand
				(if seconds (= seconds 0))
				(ego
					setLoop: 		0
					setCel:			LastCel
					setCycle: 		BegLoop self
					setMotion:		FALSE
				)
				(= preventInput TRUE)
			)

			(SITstanding
				(NormalEgo facingSouth)
				(= preventInput FALSE)
			)
		)
	)
)


(instance TSscript of Script
	(method (changeState newState)
		(switch (= state newState)

			(TSgoToChair
				(if seconds (= seconds 0))
				(= preventInput TRUE)
				(User
					canControl: 	FALSE
				)
				(ego
					illegalBits:	0
					ignoreActors:
					setMotion:		MoveTo 283 115 self
				)
			)

			(TSsit
				(ego
					view:				vEgoSitting
					setLoop: 		0
					setCel: 			0
					cycleSpeed:		2
					setMotion:		FALSE
					setCycle: 		EndLoop self
				)
			)

			(TSsit3
				(= currentStatus SITTING_AT_TABLE)
				(= preventInput FALSE)
			)


			;**	Here we wait for him to say \"Stand up.\"

			(TSstand
				(ego
					setLoop: 		0
					setCel:			LastCel
					setCycle: 		BegLoop self
					setMotion:		FALSE
				)
				(= preventInput TRUE)
			)

			(TSstanding
				(NormalEgo facingSouth)
				(= preventInput FALSE)
			)
		)
	)
)


(instance MDscript of Script
	(method (changeState newState)
		(switch (= state newState)

			(MDstart
				(= MDatPodium FALSE)
				(aMaitreD	setLoop:		RELEASE
								setCycle:	Walk
								setMotion:	MoveTo 172 155 self))
			(2	(aMaitreD	setMotion:	MoveTo 189 155 self))
			(3	(aMaitreD	cel:			0
								setMotion:	MoveTo 210 155 self)
				(aRope		setCycle:	EndLoop))
			(4	(rm43Script cue:))

			(MDreturn
				(aMaitreD	setMotion:	MoveTo topCenter self))
			(9	(aMaitreD	setMotion:	MoveTo aroundT5 self))
			(10 (aMaitreD	setMotion:	MoveTo 172 155 self))
			(11 (aMaitreD	setMotion:	MoveTo 154 167 self))
			(MDdone
				(aMaitreD	setLoop:		facingSouth)
				(= MDatPodium TRUE)
				(rm43Script cue:))
		)
	)
)

(instance to1Script of Script
	(method (changeState newState)
		(switch (= state newState)
			(0	(client	setMotion:	MoveTo aroundT5 self))
			(1	(client	setMotion:	MoveTo topCenter self))
			(2	(client	setMotion:	MoveTo bottomStep self))
			(3	(client	setMotion:	MoveTo topStep self))
			(4	(client	setMotion:	MoveTo 116 86 self))
			(5	(rm43Script cue:)
				(client	setScript:	FALSE))
		)
	)
)

(instance to2Script of Script
	(method (changeState newState)
		(switch (= state newState)
			(0	(client	setMotion:	MoveTo aroundT5 self))
			(1	(client	setMotion:	MoveTo topCenter self))
			(2	(client	setMotion:	MoveTo bottomStep self))
			(3	(client	setMotion:	MoveTo topStep self))
			(4	(client	setMotion:	MoveTo 112 86 self))
			(5	(rm43Script cue:)
				(client	setScript:	FALSE))
		)
	)
)

(instance to3Script of Script
	(method (changeState newState)
		(switch (= state newState)
			(0	(client	setMotion:	MoveTo aroundT5 self))
			(1	(client	setMotion:	MoveTo topCenter self))
			(2	(client	setMotion:	MoveTo 200 102 self))
			(3	(rm43Script cue:)
				(client	setScript:	FALSE))
		)
	)
)

(instance to4Script of Script
	(method (changeState newState)
		(switch (= state newState)
			(0	(client	setMotion:	MoveTo aroundT5 self))
			(1	(client	setMotion:	MoveTo topCenter self))
			(2	(client	setMotion:	MoveTo 208 102 self))
			(3	(rm43Script cue:)
				(client	setScript:	FALSE))
		)
	)
)

(instance toLarryScript of Script
	(method (changeState newState)
		(switch (= state newState)
			(0	(client 	setMotion:	MoveTo aroundT5 self))
			(1	(client 	setMotion:	MoveTo 241 115 self))
			(2	(rm43Script cue:)
				(client	setScript:	FALSE))
		)
	)
)

(instance groupScript of Script
	(method (init)
		(= state 0)
	)

	(method (changeState newState)
		(switch (= state newState)
			(3	(rm43Script cue:)
			)
		)
	)
)

(instance aChair		of Prop
	(properties
			view:				vRestaurant
			loop:				5
			x:					287 
			y:					112
			cycleSpeed:		2
	)
)
(instance aTable		of Prop
	(properties
			view:				vRestaurant
			loop:				6
			x:					272 
			y:					116
			cycleSpeed:		2
	)
)
(instance aRope		of Prop
	(properties
			view:				vRestaurant
			loop:				lRope
			x:					225 
			y:					144
	)
)
(instance aMaitreD	of Actor
	(properties
			view:				vRestaurantMaitreD
			illegalBits:	0
			x:					154 
			y:					167
	)
)
(instance aMan			of Actor
	(properties
			view:				vRestaurantMan
			loop:				facingNorth
			x: 				manX 
			y:					coupleY
			illegalBits:	0
	)
)
(instance aWoman		of Actor
	(properties
			view:				vRestaurantWoman
			loop:				facingNorth
			x: 				womanX 
			y:					coupleY
			illegalBits:	0
	)
)

(instance enterScript of Script
	(method (changeState newState &tmp enterX enterY [str 100])
		(switch (= state newState)

;**				(0)

			(1
				(if (!= currentStatus SITTING)
					(-- state)
					(= seconds (Random coupleMin coupleMax))	
				else
					(= seconds 0)
					(= preventInput TRUE)
					(User
						canControl: 	FALSE
					)
					(aMan
						posn: 			manX coupleY
						setCycle:		Walk
						show:
						setMotion:		MoveTo manX 175 self
					)
					(aWoman
						posn: 			womanX coupleY
						setCycle:		Walk
						show:
						setMotion:		MoveTo womanX 175
					)
				)
			)

			(2
				(= MDatPodium FALSE)
				(aMaitreD	
					setLoop:		RELEASE
					setCycle:	Walk
					setMotion:	MoveTo 172 155 self
				)
			)

			(3	
				(aMaitreD	
					setMotion:	MoveTo 189 155 self
				)
			)

			(4	
				(aMaitreD	
					cel:			0
					setMotion:	MoveTo 210 155 self
				)
				(aRope		
					setCycle:	EndLoop
				)
			)

			(5	
				(rm43Script cue:)
				(= cycles 10)
			)

			(6
				(aWoman		setMotion:	MoveTo 193 165 self)
			)

			(7
				(aWoman		setMotion:	MoveTo 193 149)
				(aMan			setMotion:	MoveTo 193 165 self)
			)

			(8
				(aMan			setMotion:	MoveTo 193 152 self)
			)

			(9	
				(aMaitreD	
					setMotion:	MoveTo 185 155 self
				)
				(aRope		
					setCycle:	BegLoop
				)
			)

			(10	
				(aMaitreD	
					setMotion:	MoveTo 210 155 self
				)
				(aRope			stopUpd:)
			)
			
			(11	
				(Print "\"Please, walk this way.\"")
				(= preventInput FALSE)
				(rm43Script cue:)
			)
		)
	)
)


