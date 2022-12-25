;**
;**	Logics for room 114 -- Quickie mart interior
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 14, 1988
;**


(script#				114)

(public
	rm114				0
)


(define	nearLotto		0	0	51 126)
(define	nearClerk 		0 105	90 120)
(define	nearGulp		110 128 170 138)

(define	NO_SODA	"There's not one single cup left in the whole joint!")

;(define	vQuikiInt					218)
;(define	vQuikiClerk					219)
	(define	lTalking			0)
	(define	lFiring			1)
	(define	lEgoShot			2)
	(define	lBigClerk		3)


(local
	work
	owesForSoda
	girlTalk										;**	times said "talk girl"
	aGulpCup
	aSpigots
	aClerk
)


(enum												;**	girlScript state values
	nothing
	girl1a
	girl1b
	girl1c
	girl2a
	girl2b
	girl3a
	girl3b
)

(enum
	intro
	gulpInit
	egoInPlace
	after5
	after10
	after15
	after19
	inPocket

	gunfightInit
	egoShot
	egoDown
	UBdead

	lotteryInit
	turnCorner
	atMachine
	enterNumbers
)


(instance rm114 of Room
	(properties
		picture:		114
		horizon:		5
		south:		14
	)

	(method (init)
		(Load VIEW vQuikiInt)
		(Load VIEW vQuikiClerk)

		(if (ego has: iDollar)
			(Load VIEW iDollar)
		)
	
		(super init:)

		((View new:)							;**	lotto machine
			view: 			vQuikiInt
			loop: 			0
			cel: 				2
			posn: 			40 101
			setPri: 			6
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	liquor sign
			view: 			vQuikiInt
			loop: 			1
			cel: 				0
			posn: 			167 46
			setPri: 			1
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	mag rack
			view: 			vQuikiInt
			loop: 			0
			cel: 				1
			posn: 			59 84
			setPri: 			0
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	the soda machine
			view: 			vQuikiInt
			loop: 			0
			cel: 				0
			posn: 			140 129
			setPri: 			9
			ignoreActors:
			addToPic:
		)

		((= aSpigots (Prop new:))			;**	two streams of soda from spigots
			view:				vQuikiInt
			setLoop:			2
			setCel:			0
			setPri:			9
			posn:				140 108
			setCycle:		Forward
			init:
			hide:
		)

		(if ((inventory at: iGulp) ownedBy: curRoomNum)
			((= aGulpCup (View new:))
				view:				vQuikiInt
				ignoreActors:
				setLoop:			0
				setCel:			3
				setPri:			9
				posn:				140 130		;**	includes 5y for sci priority bug
				init:
				stopUpd:
			)
		)

		((= aClerk (Prop new:))
			view:				vQuikiClerk
			setLoop:			lTalking
			cel:				0
			setPri:			5
			posn:				53 93
			stopUpd:
			init:
			setScript:		girlScript
		)

		(aBigClerk
			view:				vQuikiClerk
			setLoop:			lBigClerk
			posn:				273 1039
			setPri:			15
			init:
		)

		(NormalEgo)
		(ego
			posn: 			35 155
			init:
		)

		(self
			setRegions:		rgLA
			setScript: 		rm114Script
		)
	)
)


(instance rm114Script of Script
	(method (doit)
		(super doit:)

		(if (& (ego onControl:) cBlue)
			(cond
				((and owesForSoda
						(ego has: iGulp))
					(if ( < state gunfightInit)
						(self changeState: gunfightInit)
					)
				)

				(else
					(curRoom newRoom: 14)
				)
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/soda, cup, (dispenser<soda), (machine<soda)')
				(if (not ((inventory at: iGulp) ownedBy: curRoomNum))
					(Print NO_SODA)
				else
					(Print "There's only one soda cup left in the place,
						the speciality of the house: a \"Grotesque Gulp.\"")
					(Print "GG's are legendary in the Los Angeles area --
						it's the only soft drink served in a painted
						32-gallon trash can!")
				)
			)

			(if (Said '/lottery, ticket, (machine<lottery)')
				(cond
					((ego has: iLotteryTicket)
						(event claimed: FALSE)
						(return)
					)
					((not ((inventory at: iLotteryTicket) ownedBy: curRoomNum))
						(Print "There are no more tickets available here.")
					)
					(else
						(Print "\"You too can be rich and famous!\n\n
							Play the Lucky Life Lottery game!\n\n
							Luck-O Buck-O Tickets available here!\"")
					)
				)
			)

			(if (Said '/machine')
				(Print "There are two machines here: a Luck-O Buck-O lottery
					ticket machine and a soda dispenser.")
			)

			(if (Said '/clerk, person, girl')
				(aBigClerk
					posn:			273 39
					stopUpd:
				)
				(Timer setReal: aBigClerk 5)
				(HandsOff)
			)

			(if (Said '[/store, case, counter, shelf, building, room]')
				(Print "This Quikie Mart has many items for sale.
					A clerk lounges sleepily behind the counter near
					a lottery ticket machine.
					There is a soda dispenser near the front window.")
			)
		)

		(if (Said 'talk/clerk, person, girl')
			(girlScript cue:)
		)

		(if (and	(ego has: iGulp)
					(or	(Said 'return, put/soda/shelf<back')
							(Said 'return, put/soda/back')
							(Said 'return, put/soda<back')
							(Said 'return, put/soda/machine')))
			(Print "You try as hard as you can, but you just can't force that
				soda back into the nozzles!")
		)

		(if (and	(ego has: iGulp)
					(or	(Said 'return, give, put/soda/counter')
							(Said 'return, give/soda')
							(Said 'return, give/soda/clerk, woman')))
			(Print "\"Hey, you can't do that!\" orders the clerk.
				\"Once you pour 'em, you gotta keep 'em!\"")
		)

		(if (or	(Said 'ask/clerk, person, girl/ticket')
					(Said 'use, play/(machine<lottery), lottery')
					(Said 'ask, get, buy/ticket'))
			(cond
				((not ((inventory at: iLotteryTicket) ownedBy: curRoomNum))
					(Print "\"Sorry, pardner,\" says the clerk,
						\"we're all out of Luck-O Buck-Os for this week!\"")
				)
				((not (ego inRect: nearClerk))
					(Print "Move closer to the clerk to buy a ticket.")
				)
				((ego has: iDollar)
					(self changeState: lotteryInit)
				)
				(else
					(Print "\"May I have a free lottery ticket, please?
						I have no money!\"")
					(Print "The clerk responds,
						\"We ain't runnin' no charity here, cowboy.
						You wanna lottery ticket?
						You gotta pay for it.\"")
					(Print "\"That'll be a buck, please,\" she says.")
				)
			)
		)

		(if (Said 'show/ticket')
			(cond
				((not (ego has: iLotteryTicket))
					(DontHave)
				)
				((not (ego inRect: nearClerk))
					(NotClose)
				)
				(else
					(Print "You show your lottery ticket to the clerk.")
					(Print "\"I donno if it's any good or not.
						We don't cash 'em; we just sell 'em!\"")
				)
			)
		)

		(if (Said 'get/cup')
			(if (not ((inventory at: iGulp) ownedBy: curRoomNum))
				(Print NO_SODA)
			else
				(Print "Perhaps you should use the soda dispenser.")
			)
		)

		(if (or	(Said 'pour, fill/cup, soda')
					(Said 'use/machine<soda')
					(Said 'get/gulp, soda'))
			(cond
				((not ((inventory at: iGulp) ownedBy: curRoomNum))
					(Print NO_SODA)
				)
				((not (ego inRect: nearGulp))
					(NotClose)
				)
				(else
					(self changeState: gulpInit)
				)
			)
		)

		(if (or	(Said 'give, hand/clerk, person, girl')
					(Said 'hand, give/money')
					(Said 'pay'))
			(cond
				((not owesForSoda)
					(Print "Why pay? You don't owe any money.")
				)
				((not (ego inRect: nearClerk))
					(NotClose)
				)
				((ego has: iMillion)
					(Print "The clerk just laughs,
						\"Do I look like I could break a million-dollar bill?\"")
				)
				((ego has: iLotteryTicket)
					(Print "\"I can't give you credit on that ticket, pardner.\"
						says the clerk.
						\"You're gonna havta come up with some real money
						to pay for a soda like that!\"")
				)
				((ego has: iDollar)
					(Print "\"Here's a dollar for my 'Grotesque Gulp'\"
						you offer.")
					(Print "The clerk responds,
						\"We ain't runnin' no charity here, cowboy.
						You wanna soda?
						You gotta pay for it.\"")
					(Print "\"That'll be thirty-two bucks, please,\" she says.")
					(Print "That's a good value, Larry --
						only a dollar a gallon!
						Too bad you only have one dollar on you.")
				)
				((not (ego has: iMoney))
					(Print "You have no money to give her.")
				)
				(else
					(Print "\"Here's a hundred-dollar bill,\" you offer.")
					(Print "\"Ok, pardner,\" says the clerk.
						\"Too bad we don't keep any change at this time of
						night!\"")
					(Print "\"But it's broad daylight,\" you protest!")
					(Print (Format @string "\"%s\" she concludes." tritePhrase))
					(theGame changeScore: 3)
					(= owesForSoda FALSE)
				)
			)
		)

		(if (Said '/lubber')
			(Print "You must obtain those in the \"Land of the Lounge Lizards!\"")
		)

		(if (Said 'buy, open, (ask<for)/door, anyword')
			(Print "You don't need to do that.")
		)
	)


	(method (changeState newState &tmp i)
		(switch (= state newState)

;**				(intro)


			;**	This lets Larry get the big cup of soda.

			(gulpInit
				(HandsOff)
				(Ok)
				(ego
					setMotion:		MoveTo 155 133 self
				)
			)

			(egoInPlace
				(ego
					setLoop:			facingNorth
				)
				(aSpigots
					show:
					setCycle:	Forward
				)
				(= seconds 6)
			)

			(after5
				(Print "A cup that large might take a long time to fill.")
				(= seconds 6)
			)

			(after10
				(Print "A cup that large might take FOREVER to fill!")
				(= seconds 6)
			)

			(after15
				(aSpigots hide:)
				(Print "Finally, you top it off, while
					you carefully consider how you're going to pick it up." draw:)
				(= seconds 3)
			)

			(after19
				(if (> filthLevel CLEAN)
					(Print "Ah, what the hell.
						This isn't real life... but merely an incredible simulation!\n\n
						You decide to put it in your pocket,
						along with everything else.")
				else
					(Print "Ah, shucks!
						This isn't real life... but merely an incredible simulation!\n\n
						You decide to put it in your pocket,
						along with everything else.")
				)
				(ego
					view:				vQuikiInt
					setLoop:			3
					cel:				0
					setCycle:		EndLoop self
					cycleSpeed:		2
				)
				(aGulpCup dispose:)
			)

			(inPocket
				(NormalEgo facingWest)
				(ego get: iGulp)
				(theGame changeScore: 5)
				(= owesForSoda TRUE)
				(= state 0)
			)


			;**	This section handles ego leaving without paying for his soda.

			(gunfightInit
				(= cycles	0)
				(= seconds	0)
				(= currentStatus NEAR_DEATH)
				(Print "Suddenly the woman behind the counter comes to life!")
				(Print "\"Hold it right there, pardner,\" she cries,
					\"you ain' a gonna rustle no soda offn me!\"")
				(HandsOff)
				(aClerk
					setLoop:			lFiring
					cel:				0
					cycleSpeed:		1
					setCycle:		EndLoop self
				)
			)

			(egoShot
				(aClerk
					setCycle:		CycleTo 8 -1
				)
				(ego
					view:				vQuikiClerk
					setLoop:			lEgoShot
					cel:				0
					posn:				30 150
					illegalBits:	0
					setPri:			3
					cycleSpeed:		1
					setCycle:		EndLoop self
					setMotion:		MoveTo 23 184
				)
			)

			(egoDown
				(ego stopUpd:)
				(aClerk stopUpd:)
				(= seconds 3)
			)

			(UBdead
				(Print "\"Yeeeee haaaaaa,\" screams the clerk,
					\"you're one dead Coke sucker!\"")
				(Print "Enunciate carefully, please!")
				(= currentStatus DEAD)
			)


			;**	This handles ego buying a Luck-O Buck-O ticket.

			(lotteryInit
				(HandsOff)
				(Print "\"Here's my last dollar,\" you say,
					handing it to the cute clerk.
					\"How about selling me one of those
					Luck-O Buck-O Lottery tickets?\""
					icon: iDollar 0 0
					)
				(Print "\"Ok, pardner,\" says the clerk, \"here ya go.
					Just stick this here ticket in that machine
					on the end of the counter.\"")
				(Print (Format @string
					"\"Good luck!\" she concludes.
					\"%s\"" tritePhrase))
				(ego
					put: 			iDollar LIMBO
					setMotion:	MoveTo (ego x?) 123 self
				)
			)

			(turnCorner
				(ego
					setMotion:	MoveTo 42 123 self
				)
			)

			(atMachine
				(ego
					loop:			facingNorth
				)
				(Print "You insert the paper ticket into the Luck-O Buck-O
					machine." draw:)
				(= seconds 3)
			)

			(enterNumbers						;**	Jerk him off a little
				(Print "The Luck-O Buck-O Lottery machine's
					video display screen flashes,
					\"Please enter your six numbers between 100 and 999 now.\""
					icon: 	vQuikiInt 0 2
					HIGH
					dispose:
				)

				(= i 0)
				(while (or 	( < i 100)
								( > i 999))
					(= i (GetNumber {Pick #1}))
				)
				(= i 0)
				(while (or 	( < i 100)
								( > i 999))
					(= i (GetNumber {Pick #2}))
				)
				(= i 0)
				(while (or 	( < i 100)
								( > i 999))
					(= i (GetNumber {Pick #3}))
				)
				(= i 0)
				(while (or 	( < i 100)
								( > i 999))
					(= i (GetNumber {Pick #4}))
				)
				(= i 0)
				(while (or 	( < i 100)
								( > i 999))
					(= i (GetNumber {Pick #5}))
				)
				(= i 0)
				(while (or 	( < i 100)
								( > i 999))
					(= i (GetNumber {Pick #6}))
				)

				(cls)
				(for ((= i 0)) (< i 3) ((++ i))
					(Print "\"Processing...\"" icon: vQuikiInt 0 2)
				)
				(Print "Out pops your Luck-O Buck-O lottery ticket.
					You take it and wonder, \"Will I be a winner?\"")
				(ego get: iLotteryTicket)
				(theGame changeScore: 3)
				(= state 0)
				(HandsOn)
			)
		)
	)
)


(instance girlScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(girl1a
				(User canInput: 	FALSE)
				(Print (Format @string
					"\"Hello, baby!\" you tell her.
					%s" laffer))
				(aClerk
					setLoop:			lTalking
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(girl1b
				(aClerk
					setLoop:			lTalking
					setCel:			0
				)
				(Print "\"Hi, y'all,\" she replies in a charming drawl.
					\"Ya shore have a white suit.\"")
				(= seconds 3)
			)

			(girl1c
				(Print "You have always admired Southern girls.")
				(User canInput:	TRUE)
			)


			(girl2a
				(User canInput: 	FALSE)
				(Print "You vow to try again.
					\"That's really a charming Southern accent,\" you say.
					\"I'll bet you're from the South.\"")
				(aClerk
					setLoop:			lTalking
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(girl2b
				(User canInput:	TRUE)
				(aClerk
					setLoop:			lTalking
					setCel:			0
				)
				(Print "\"Why, shucks,\" she replies, \"you're right!
					Howdja know I was from Orange County?\"")
			)


			(girl3a
				(User canInput: 	FALSE)
				(Print "\"What do you say, baby?\"
					you turn on your charm afterburner,
					\"How's about you and me gettin' it on!\"")
				(aClerk
					setLoop:			lTalking
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(girl3b
				(User canInput:	TRUE)
				(Print "\"Gettin' it on what?\" she asks.")
				(aClerk
					setLoop:			lTalking
					setCel:			0
				)
				(= state girl3a)
				(-- state)						;**	ready for next line
			)
		)
	)
)


(instance aBigClerk of Prop
	(method (cue)
		(Print "You find Southern women sexy.")
		(Print "(But then, you find any woman sexy!)" at: 55 155 width: 210)
		(self posn: 273 1039)
		(HandsOn)
	)
)
