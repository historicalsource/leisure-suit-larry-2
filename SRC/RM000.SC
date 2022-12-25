;**
;**	Logics for room 0 -- "'cause Al used to be a room zero-kind of guy!"
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 25, 1988
;**


(script#				0)

(include	"menu.sh")

(procedure
	LookAt
	NormalEgo
	NearControl
	AddActorToPic
	HandsOff
	HandsOn
	Notify
	HaveMem
	RedrawCast
	SoundLoops
	cls
	Ok
	ItIs
	YouAre
	NotNow
	NotClose
	AlreadyTook
	SeeNothing
	CantDo
	DontHave
	SetRgTimer 
;	LogIt
)

(public
	LL2				0
	LookAt 			1
	NormalEgo 		2
	NearControl		3
	AddActorToPic 	4
	HandsOff			5
	HandsOn			6
	Notify 			7
	HaveMem			8
	RedrawCast		9
	SoundLoops 		10
	cls				11
	Ok					12
	ItIs				13
	YouAre			14
	NotNow			15
	NotClose			16
	AlreadyTook		17
	SeeNothing		18
	CantDo			19
	DontHave			20
	SetRgTimer 		21
;	LogIt				22
)


(instance LL2 of Game
	(properties
		name				"LSL2"
	)

	(method (init &tmp testRoom)
		(super init:)

		;**	Comment out these lines (and the ones below) before shipping:
;		(= debugging		TRUE)
;		(= logging			TRUE)

		(= volume			15)
		(DoSound ChangeVolume volume)
		(StatusLine			code: statusCode)
		(TheMenuBar  		init:)
		(scoreSnd 			init:)
		(deadSnd  			init:)
		(User echo:			SPACEBAR)

		(= bigFont 			0)
		(= possibleScore	500)
		(= currentEgoView vEgo)
		(= filthLevel		CLEAN)
		(= rank				"Novice")
		(= laffer			"\"My name is Larry; Larry Laffer.\"")
		(= tritePhrase 	(Format @tpBuffer "Have a nice day."))
		(= version 			{x.yyy.zzz})

		(Load FONT smallFont)				;**	Gonna use anyway!
		(Load FONT  userFont)
		(Load FONT   bigFont)
		(Load CURSOR normalCursor)
		(Load CURSOR   waitCursor)
		(ego view:vEgo setCycle:Walk)		;**	bring in motion

		(Inventory
			empty:			"Your leisure suit is empty!"
			add:
				dummy
				dollar
				lotteryTicket
				cruiseTicket
				million
				swimsuit
				money
				passport
				gulp
				sunscreen
				onklunk
				fruit
				sewingKit
				spinachDip
				wig
				bikiniTop
				bikiniBottom
				knife
				soap
				matches
				flower
				hairRejuvenator
				suitcase
				airlineTicket
				parachute
				bobbyPin
				pamphlet
				airsickBag
				stick
				vine
				ashes
				sand
		)

		;**	Comment out these lines before shipping:
;**			(if logging
;**				(File name: "input.log" write: "\r\r" version "\r" close:)
;**				(File name: "note.log"  write: "\r\r" version "\r" close:)
;**			)

		(if (GameIsRestarting)
			(StatusLine	disable:)
			(TheMenuBar	hide:)
			(self	newRoom:	99)
		else
			;**	swap out the next line with the following 8 for testing.
			(self	newRoom:	10)

;**				(if debugging (= testRoom (GetNumber {Starting Room})))
;**				(if (<= testRoom 0)
;**					(= testRoom 90)
;**				else
;**					(TheMenuBar draw:)
;**					(StatusLine enable:)
;**				)
;**				(self newRoom: testRoom)

		)
	)


	(method (replay)
		(TheMenuBar	draw:)
		(StatusLine	enable:)
		(SetMenu	soundI text: (if (DoSound SoundOn) {Turn Off} else {Turn On}))
		(super replay:)
	)


	(method (changeScore n)
		(if ( > n 25)
			(= rank "Big Hero")
		else
			(if ( > n 0)
				(scoreSnd play:)
			)
			(switch (Random 1 22)
				( 1	(= rank "Novice"))
				( 2	(= rank "Kumquat"))
				( 3	(= rank "Dork"))
				( 4	(= rank "Putz"))
				( 5	(= rank "Lame-o"))
				( 6	(= rank "Schmuck"))
				( 7	(= rank "Pinhead"))
				( 8	(= rank "Dweeb"))
				( 9	(= rank "Nerd"))
				(10	(= rank "Minion"))
				(11	(= rank "Low-life"))
				(12	(= rank "Sleeze"))
				(13	(= rank "Slug"))
				(14	(= rank "Cretin"))
				(15	(= rank "Dullard"))
				(16	(= rank "Schlemiel"))
				(17	(= rank "Boor"))
				(18	(= rank "Creep"))
				(19	(= rank "Dim Bulb"))
				(20	(= rank "Jerk"))
				(21	(= rank "Hoser"))
				(22	(= rank "Nimrod"))
			)
		)
		(super changeScore: n)
	)


	(method (newRoom n)
;		(timers eachElementDo: #dispose:)
;		(sounds eachElementDo: #dispose:)
;		(DisposeScript FILE)
		(DisposeScript JUMP)					;**	should be done where used.
		(DisposeScript EXTRA)
		(DisposeScript DOORS)				;**	clean up old details
		(DisposeScript AIRPLANES)
		(DisposeScript BASS_SETTER)

		(= henchOnScreen FALSE)
		(= showStyle (Random 0 5))
		(= secondsInRoom 0)
		(super newRoom: n)
		(if debugging
			(curRoom setLocales: DEBUG)
		)
	)


	(method (startRoom n	&tmp newRegion)
;**			(if loadDebugNext 				
;**				(=	loadDebugNext FALSE)
;**				((= debugMenu (ScriptID	DEBUG_MENU)) init:)
;**			)
;**			(=	newRegion
;**				(switch n
;**					(23	(if (or	( > prevRoomNum 24) 			;**	From cartoons
;**										( < prevRoomNum 11))			;**	From a restart
;**								rgLA))
;**					(31	(if (== prevRoomNum 28)
;**								rgShip))
;**					(50	(if (== prevRoomNum 48)
;**								rgAirport))
;**					(61	(if (== prevRoomNum 58)
;**								rgAirplane))
;**					(70	rgIsland)
;**				)
;**	 		)
;**			(if newRegion
;**				(regions eachElementDo: #keep: FALSE)
;**				((ScriptID newRegion) init: keep: TRUE)
;**			)
		(super startRoom: n)
	)


	(method (doit &tmp systime)
		(super doit:)

		(if (!= oldTime (= systime (GetTime TRUE)))
			(= oldTime systime)
			(++ secondsInRoom)

			(if (== 60 (++ gameSeconds))
				(= gameSeconds 0)
				(if (== 60 (++ gameMinutes))
					(= gameMinutes 0)
					(++ gameHours)
				)
			)
		)

		(if (and currentTimer				;**	A global timer that kinda works.
					( > rgTimer 0))
			(-- rgTimer)
		)

		(if (== currentStatus DEAD)
			(curRoom setScript: dyingScript)
		)
	)


	(method (wordFail word input)
		(Print (Format @string "Don't you ever say \"%s\" to me again!" word))
;**			(if logging
;**				(Format @string
;**					"[r%3d v%3d %3dx/%3dy CS%-5d] Word \"%s\" in \"%s\""
;**						curRoomNum (ego view?) (ego x?) (ego y?) 
;**						currentStatus word input)
;**				(File name: "input.log" write: @string "\r" close:)
;**			)
	)


	(method (syntaxFail input)
		(Print "Perhaps you can understand that sentence,
			but I have graphics to worry about!")
;		(LogIt)
	)


	(method (pragmaFail input)
		(if ( <= filthLevel CLEAN)
			(Print "The heck you say!")
		else
			(Print "The hell you say!")
		)
;		(LogIt)
	)


	(method (handleEvent event &tmp i theObj xyWindow evt fd)
;**			(if (and debugging
;**						(not (event claimed?))
;**						(== mouseDown (event type?)))
;**				(cond
;**					((& (event modifiers?) ctrlDown)
;**						(event claimed: TRUE)
;**						(User canControl:	TRUE)
;**						(while (!= mouseUp ((= evt (Event new:)) type?))
;**							(GlobalToLocal evt)
;**							(ego posn: (evt x?) (evt y?) setMotion: 0)
;**							(RedrawCast)
;**							(evt dispose:)
;**						)
;**						(evt dispose:)
;**					)
;**					((& (event modifiers?) shiftDown)
;**						(event claimed:TRUE)
;**						(=	xyWindow (Print (Format 
;**							@string "%d/%d"	(event x?) (event	y?))
;**							at: 150 100
;**							font:999
;**							dispose:
;**							)
;**						)
;**						(while (!= mouseUp ((= evt	(Event new:)) type?))
;**							(evt dispose:)
;**						)
;**						(xyWindow dispose:)
;**						(evt dispose:)
;**					)
;**				)
;**				(if (event claimed?)
;**					(return TRUE)
;**				)
;**			)

		(super handleEvent: event)

		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))
	
		(if (Said 'praise/lord')
			(^= debugging TRUE)
			(if debugging
				(Print "Amen, brother!")
			else
				(Print "Go in peace.")
			)
		)

;**			(if debugging
;**				(if (Said 'get>')
;**					(if (= theObj (inventory saidMe:))
;**						(event claimed: TRUE)
;**						(if (theObj ownedBy: ego)
;**							(Print "You already have it, Al.")
;**						else
;**							(Print "Ok, Al.")
;**							(theObj moveTo: ego)
;**						)
;**					)
;**				)
;**	
;**				(if (Said 'show/memory')
;**					(theGame showMem:)
;**				)
;**	
;**				(if (Said 'show/frag')
;**					(Print "Ok, Al.")
;**					(^= debugOn TRUE)
;**				)
;**	
;**				(if (Said 'load/menu<debug')
;**					(=	loadDebugNext TRUE)
;**					(Print "Next room will have Cheat menu.")
;**				)
;**			)


		;**	Now, handle the known objects in the game:

		(cond
			((and	(ego has: iMoney)
					(Said 'count/money'))
				(Print "You have more money than you can count!")
			)

			((or	(Said 'give/bill, wad, million, money')
					(Said 'bribe'))
				(cond
					((ego has: iMoney)
						(Print "\"Here. Take some money,\" 
							you say in a vain attempt to buy friendship!")
						(Print "(It doesn't work.)" LOW)
					)
					((ego has: iDollar)
						(Print "\"Want a dollar?\" you ask.")
						(Print "There is no reply.")
					)
					((ego has: iMillion)
						(Print "\"Hey, have you got change for a million?\" 
							you ask.")
						(Print "(Evidently not.)" LOW)
					)
					(else
						(DontHave)
					)
				)
			)

			((and	(or	(ego has: iSwimsuit)
							(ego has: iBikiniTop)
							(ego has: iBikiniBottom))
					(or	(Said 'get/dressed')
							(Said 'get<dressed')
							(Said 'wear, change, (take<off), remove,
											(put<on)/swimsuit, clothes, suit, bikini')))
				(Print "You can't change here!")
			)

			((and (ego has: iPassport)
					(Said 'show/passport/anyword'))
				(Print "You're the only one interested in your passport photo!")
			)

			((and (ego has: iGulp)
					(Said 'drink, use/soda, gulp'))
				(Print "Ok. As you slowly drain the entire 32-gallon contents of
					your Grotesque Gulp, you begin searching feverishly
					for a fire hydrant.")
				(ego hide: put: iGulp LIMBO)
				(Print "Your exploding bladder prevents you from finding one!"
					draw:)
				(= currentStatus DEAD)
			)

			((and (ego has: iFruit)
					(Said 'read, look/note, fruit'))
				(fruit showSelf:)
			)

			((and	(ego has: iOnklunk)
					(Said 'give/onklunk'))
				(Print "You easily give away the hopes of the free world!
					Way to go, traitor!
					Next time, hang onto your onklunk!")
				(= currentStatus DEAD)
			)

			((and	(ego has: iOnklunk)
					(Said 'play, use/music, song, onklunk'))
				(Print "You beat your onklunk in a frenzied rendition of
					an old country classic!")
				(Print "(But, which country?)" LOW)
			)

			((and (ego has: iKnife)
					(or	(Said 'use/knife')
							(Said 'stab')))
				(Print "Hey! We want our R rating for sex, not violence!")
			)

			((and (ego has: iMatches)
					(Said 'use, light/match'))
				(if (ego has: iHairRejuvenator)
					(HandsOff)
					(Ok)
					(= currentStatus MATCHES)
					(curRoom newRoom: 152)
				else
					(Print "You light the book of matches and watch
						it go up in flames. Whee!")
					(ego put: iMatches LIMBO)
					(theGame changeScore: -2)
				)
			)

			((and (ego has: iHairRejuvenator)
					(Said 'put, (put<on), pour, use, drop, rub, wear/tonic>'))
				(cond
					((Said '[/!*]')
						(Print "On what?")
					)
					((Said '/self, head, hair')
						(Print "You've had enough problems with your hair.
							You don't need to wear explosives, too!")
					)
					(else
						(event claimed: TRUE)
						(Print "Ok. It's gone now.")
						(ego put: iHairRejuvenator LIMBO)
						(theGame changeScore: -2)
					)
				)
			)

			((and (ego has: iHairRejuvenator)
					(ego has: iAirsickBag)
					(or	(Said 'put/bag/bottle')
							(Said 'put/bottle/bag')))
				(Print "Good idea. But this is not the place.")
			)

			((and (ego has: iHairRejuvenator)
					(Said 'light, burn/bottle, tonic'))
				(if (ego has: iMatches)
					(HandsOff)
					(Ok)
					(= currentStatus MATCHES)
					(curRoom newRoom: 152)
				else
					(Print "Fortunately, you have no matches with which to
						immolate yourself!")
				)
			)

			((Said 'open, (look<in)>')
				(=	theObj (inventory	saidMe:))
				(cond
					((Said '[/!*]')
						(Print "What do you want to inspect?")
					)
					((not	theObj)
						(event claimed: TRUE)
						(CantDo)
					)
					((not	(theObj ownedBy: ego))
						(DontHave)
					)
					(else
						(switch (inventory indexOf: theObj)
							(iCruiseTicket
								(Print "\"If it's a good cruise,
									it's a Wonder!\"")
							)
							(iSwimsuit
								(if (== currentEgoView vEgoSwimsuit)
									(Print "Pervert!")
								else
									(Print "\"Hand wash only.\"")
								)
							)
							(iMoney
								(Print "You only find more money.")
							)
							(iPassport
								(Print "Nice photo, Larry!"
									icon: iPassport 1 0)
							)
							(iOnklunk
								(Print "You can't find the microfiche in there.")
								(Print "(It's a secret and you don't know about it!)" 
									LOW)
							)
							(iSewingKit
								(Print "Nothing in the sewing kit is of use to you.")
							)
							(iWig
								(Print "\"Made in Taiwan\"")
							)
							(iBikiniTop
								(Print "\"40D\"")
							)
							(iBikiniBottom
								(Print "\"XL\"")
							)
							(iHairRejuvenator
								(Print "Slosh.")
								(Print "Slosh.")
							)
							(iAirlineTicket
								(Print "\"You have no rights. (Just ask us!)\"")
							)
							(iParachute
								(Print "You open the parachute and it spills out
									everywhere. Since you know nothing about 
									parachute folding, you throw it away.")
								(ego put: iParachute LIMBO)
								(theGame changeScore: -3)
							)
							(iPamphlet
								(Print "\"Repent!\"")
								(Print "\"And send money.\"")
							)
							(iAirsickBag
								(Print "You look inside the bag and see nothing.")
							)
							(else
								(SeeNothing)
;								(LogIt)
							)
						)
					)
				)
			)

			;**	Do the silly stuff now:

			((Said 'hello')
				(Print "\"Hi.\"")
			)

			((or	(Said '/bye')
					(Said 'bye'))
				(Print "\"Goodbye.\"")
			)

			((Said 'thank')
				(Print "\"You're welcome.\"")
			)

			((Said 'knock')
				(Print "\"Knock, knock.\"")
				(Print "(Nobody wants to play now, Larry!)" LOW)
			)

			((Said 'hit')
				(Print "Hey! We want our R rating for sex, not violence!")
			)

			((or	(Said 'go/toilet')
					(Said 'urinate')
					(Said 'take/urinate'))
				(if (== vEgo (ego view?))
					(Print "A warm feeling spreads down your leg.
						Your suit is not as white as it was!")
				else
					(NotNow)
				)
			)


			((Said 'climb>')
				(if (Said '/wall, building')
					(Print "Climbing the walls already?")
				else
					(Print "That would be unbecoming a man of leisure.")
					(event claimed: TRUE)
				)
;				(LogIt)
			)

			((Said 'jump')
				(Print "Whee!")
			)

			((Said 'yell')
				(Print "\"HELP!\"")
			)

			((Said 'dream')
				(Print "Zzzzzz.")
			)

			((Said 'steal')
				(Print "That's against the law!")
			)

			((Said 'cheat')
				(Print "Ok, you win.\05")
				(Print "(Game over.)" LOW)
				(= quit 1)
			)

			((Said '(put<on), wear>')
				(cond
					((Said '[/!*]')
						(Print "What do you want to wear?")
					)
					((= theObj (inventory saidMe:))
						(if (not	(ego has: (inventory	indexOf:	theObj)))
							(DontHave)
						else
							(Print "You'd look better if you didn't!")
						)
					)
					(else
						(Print "You can't wear what is not yours.")
						(event claimed: TRUE)
;						(LogIt)
					)
				)	
			)

			((Said 'drop>')
				(cond
					((Said '[/!*]')
						(Print "What do you want to drop?")
					)
					((= theObj (inventory saidMe:))
						(if (not	(ego has: (inventory	indexOf:	theObj)))
							(DontHave)
						else
							(Print "Nah. Better not!")
						)
					)
					(else
						(Print "It's not yours to drop.")
						(event claimed: TRUE)
;						(LogIt)
					)
				)	
			)

			((Said 'throw>')
				(cond
					((Said '[/!*]')
						(Print "What do you want to throw?")
					)
					((= theObj (inventory saidMe:))
						(if (not	(ego has: (inventory	indexOf:	theObj)))
							(DontHave)
						else
							(Print "Nah. Better not!")
						)
					)
					(else
						(Print "That doesn't belong to you.")
						(event claimed: TRUE)
;						(LogIt)
					)
				)	
			)

			((Said 'smell')
				(Print "Sniff, sniff.")
			)

			((Said 'wear/crown')
				(Print "You'll have to buy \"King's Quest IV\" to do that!")
			)

			((Said '/microfiche')
				(Print "Shh! You're not supposed to know about that.")
			)

			((Said 'whistle')
				(Print "You whistle a happy tune.")
				(Print "(You envision Opie on a dirt road.)" LOW)
			)

			((Said 'laugh')
				(Print "\"Ha, ha!\"")
			)

			((Said 'eat')
				(Print "Once you tasted it, you wouldn't do that any more!")
			)

			((Said 'lie, rest, sleep')
				(Print "There's no time!")
			)

			((Said 'help')
				(Print "Don't whine!")
			)

			((Said 'search>')
				(if (Said '/pocket, self')
					(Print "Searching through your pockets you find...")
					(inventory showSelf:	ego)
				else
					(event claimed: TRUE)
					(Print "You find nothing.")
				)
			)

			((or	(Said '/key')
					(Said 'unlock, lock'))
				(Print "You can't. There's no key in this game!")
			)


			;**	Now handle the potty-mouths:

			((or	(Said '/hump/you')
					(Said 'hump/you'))
				(Print "Same to you, buddy!")
			)

			((Said 'hump/anyword')
				(Print "Yeah, you probably would, too!")
			)

			((Said 'masturbate')
				(Print "Aren't your palms hairy enough?")
			)

			((Said 'touch, stroke, kiss, look/pussy')
				(Print "Obviously, restraint is no problem for you, Larry.")
			)

			((Said 'lick/girl')
				(Print "That comes later.")
			)

			((Said 'drink')
				(Print "Nobody's THAT thirsty!")
			)

			((Said 'kiss')
				(Print "Wouldn't you rather just remain friends?")
			)

			((or	(Said 'suck, hump/me')
					(Said 'crack, crap, piss, hump, fart, tit, ass, cunnilingus'))
				(Print "Tsk, tsk.")
			)

			((Said '/hell')
				(Print "That's probably where you'll end up!")
			)

			((Said '/heck')
				(Print "Aren't we the little prissy one!")
			)



			;**	Now handle the TRULY stupid answers:


			;**	Start with 'look'

			((Said 'look>')
				(cond
					((Said '/anyword/man, woman, person>')
						(= theObj (inventory saidMe:))
						(cond
							((not theObj)
								(event claimed: TRUE)
								(Print "It's not yours.")
							)
							((not	(theObj ownedBy: ego))
								(DontHave)
							)
							(else
								(Print "You	have no reason	to	do	that.")
								(event claimed: TRUE)
;								(LogIt)
							)
						)
					)

					((Said '/suit, larry')
						(if (== vEgo (ego view?))
							(Print "You're wearing the latest in fashion.")
							(Print "(If you consider 1971 \"late\")!" LOW)
						)
						(Print "Searching through your pockets you find...")
						(inventory showSelf:	ego)
					)

					((Said '/wallet, (spray[<breath]), (lint[<pocket]), (watch[<wrist])')
						(Print "Oops, wrong game! 
							If you want to see that,
							you'll have to buy \"Lounge Lizards.\"")
					)

					((Said '/bush, tree')
						(Print "You see many leaves.")
					)

					((Said '/man, woman, people')
						(Print "Where?")
					)

					((Said '/wall, building')
						(Print "Staring at the walls already?")
					)

					((Said '/ground, down')
						(Print "It just lies there, under your feet.")
					)

					((Said '/sky, ceiling')
						(Print "It's still up there!")
					)

					((Said '<in/window')
						(Print "This game does not encourage voyeurs!")
									;**	\"voyeurs\" spellchecked September 3, 1988
					)


					;**	Now for the tricky stuff.	(Who designed this game, anyway?)

					((Said '/bikini')
						(if (or	(ego has: iBikiniTop)
									(ego has: iBikiniBottom))
							(if (ego has: iBikiniTop)
								(bikiniTop showSelf:)
							)
							(if (ego has: iBikiniBottom)
								(bikiniBottom showSelf:)
							)
						else
							(DontHave)
						)
					)

					((Said '/bill, million, money, wad')
						(cond
							((ego has: iDollar)
								(dollar showSelf:)
							)
							((ego has: iMillion)
								(million showSelf:)
							)
							((ego has: iMoney)	;	Clean up for stupid object naming
								(money showSelf:) ;	and selection by game designer!!
							)
							(else
								(DontHave)
							)
						)
					)

					((Said '/ticket')
						(cond
							((ego has: iLotteryTicket)
								(lotteryTicket showSelf:)
							)
							((ego has: iCruiseTicket)
								(cruiseTicket showSelf:)
							)
							((ego has: iAirlineTicket)
								(airlineTicket showSelf:)
							)
							(else
								(DontHave)
							)
						)
					)

					((= theObj (inventory saidMe:))
						(if (theObj ownedBy: ego)
							(theObj showSelf:)
						else
							(DontHave)
						)
					)

					(else
						(switch (Random 42 44)
							(42	(Print "It's just as it appears."))
							(43	(Print "It doesn't look interesting."))
							(44	(SeeNothing))
						)
						(event claimed: TRUE)
;						(LogIt)
					)
				)
			)

			;**	END 'look'


			((or	(Said 'use, spend/bill, million, money, wad')
					(Said 'buy'))
				(if (or	(ego has: iDollar)
							(ego has: iMoney)
							(ego has: iMillion))
					(Print "There's nothing worth buying here.")
				else
					(Print "You're broke!")
				)
			)

			((Said 'use>')
				(=	theObj (inventory	saidMe:))
				(event claimed: FALSE)
				(cond
					((Said '/ticket')
						(if (or	(ego has: iLotteryTicket)
									(ego has: iCruiseTicket)
									(ego has: iAirlineTicket))
							(Print "There's no need to use the ticket here.")
						else
							(Print "What ticket?")
						)
					)
					((Said '[/!*]')
						(Print "What do you want to use?")
					)
					((not	theObj)
						(event claimed: TRUE)
						(CantDo)
					)					
					((not	(theObj ownedBy: ego))
						(DontHave)
					)			
					(else
						(Print "There's no need to use it here.")
						(event claimed: TRUE)
;						(LogIt)
					)
				)
			)

			((Said 'give>')
				(= theObj (inventory saidMe:))
				(event claimed: FALSE)
				(cond
					((Said '/*[/!*]')
						(Print "To whom?")
					)
					((Said '[/!*]')
						(Print "What do you want to give?")
					)
					((not theObj)
						(event claimed: TRUE)
						(Print "It's not yours to give.")
					)
					((not	(theObj ownedBy: ego))
						(DontHave)
					)
					(else
						(Print "You	have no reason	to	do	that.")
						(event claimed: TRUE)
;						(LogIt)
					)
				)
			)

			((Said 'get>')
				(cond
					((Said '[/!*]')
						(Print "What do you want to take?")
					)
					((and (= theObj (inventory saidMe:))
							(theObj ownedBy: ego))
						(Print "You already have it.")
					)
					(else
						(switch (Random 33 35)	;**	Larry 1 memorial print numbers
							(33 (Print "It's of no use."))
							(34 (Print "You don't need it."))
							(35 (Print "There's no reason to take it."))
						)
						(event claimed: TRUE)
;						(LogIt)
					)
				)
			)

			((= theObj (inventory saidMe:))
				(if (not (theObj ownedBy: ego))
					(DontHave)
				else
					(Print "You can't do that now.")
				)
			)

			((Said 'talk>')
				(if (Said '[/!*]')
					(Print "To whom?")
				else
					(Print "\"Hello,\" you say.")
					(Print "(There is no response.)" LOW)
				 	(event claimed: TRUE)
				)
;				(LogIt)
			)

			((or	(Said '/pussy, crap, pee, hump, asshole, tit, ass, cunnilingus')
					(Said '//pussy, crap, pee, hump, asshole, tit, ass, cunnilingus'))
				(Print "Ok, but I don't usually do that!")
			)

			((or	(Said '//woman')
					(Said '/woman/')
					(Said '/woman'))
				(Print "She's not interested in that!")
			)
		)
	)
)


;**	Define LL2 style of inventory...

(class Iitem kindof InvItem
	(method (showSelf)
		(Print INVENTORY_TEXT view
			title:	name
			icon:		view 0 0
		)
	)
)

;**	Add all the inventory items.	Their view numbers were assigned to
;**	match their inventory numbers, so only the "i" type define is needed.

(instance dummy of Iitem
	(properties
		owner:	0
		name:		{NoInv}
	)
)
(instance dollar of Iitem
	(properties
		view:		iDollar
		owner:	23
		name:		{Dollar Bill}
		said:		'/(bill<dollar)'
	)
)
(instance lotteryTicket of Iitem
	(properties
		view:		iLotteryTicket
		owner:	114
		name:		{Lottery Ticket}
		said:		'/ticket[<lottery]'
	)
)
(instance cruiseTicket of Iitem
	(properties
		view:		iCruiseTicket
		owner:	103
		name:		{Cruise Ticket}
		said:		'/ticket[<cruise]'
	)
)
(instance million of Iitem
	(properties
		view:		iMillion
		owner:	104
		name:		{Million Dollar Bill}
		said:		'/((bill<dollar)<million), (dollar<million)'
	)
)
(instance swimsuit of Iitem
	(properties
		view:		iSwimsuit
		owner:	116
		name:		{Swimsuit}
		said:		'/swimsuit'
	)
)
(instance money of Iitem
	(properties
		view:		iMoney
		owner:	116
		name:		{Wad O' Dough}
		said:		'/money, wad'
	)
)
(instance passport of Iitem
	(properties
		view:		iPassport
		owner:	23
		name:		{Passport}
		said:		'/passport'
	)
)
(instance gulp of Iitem
	(properties
		view:		iGulp
		owner:	114
		name:		{Grotesque Gulp}
		said:		'/gulp[<grotesque]'
	)
)
(instance sunscreen of Iitem
	(properties
		view:		iSunscreen
		owner:	118
		name:		{Sunscreen}
		said:		'/sunscreen'
	)
)
(instance onklunk of Iitem
	(properties
		view:		iOnklunk
		owner:	115
		name:		{Onklunk}
		said:		'/onklunk'
	)
)
(instance fruit of Iitem
	(properties
		view:		iFruit
		owner:	32
		name:		{Fruit}
		said:		'/fruit'
	)
)
(instance sewingKit of Iitem
	(properties
		view:		iSewingKit
		owner:	33
		name:		{Sewing Kit}
		said:		'/kit[<sew]'
	)
)
(instance spinachDip of Iitem
	(properties
		view:		iSpinachDip
		owner:	35
		name:		{Spinach Dip}
		said:		'/dip[<spinach]'
	)
)
(instance wig of Iitem
	(properties
		view:		iWig
		owner:	37
		name:		{Wig}
		said:		'/wig'
	)
)
(instance bikiniTop of Iitem
	(properties
		view:		iBikiniTop
		owner:	134
		name:		{Bikini Top}
		said:		'/top[<bikini]'
	)
)
(instance bikiniBottom of Iitem
	(properties
		view:		iBikiniBottom
		owner:	41
		name:		{Bikini Bottom}
		said:		'/bottom[<bikini]'
	)
)
(instance knife of Iitem
	(properties
		view:		iKnife
		owner:	43
		name:		{Knife}
		said:		'/knife'
	)
)
(instance soap of Iitem
	(properties
		view:		iSoap
		owner:	44
		name:		{Soap}
		said:		'/soap'
	)
)
(instance matches of Iitem
	(properties
		view:		iMatches
		owner:	44
		name:		{Matches}
		said:		'/match'
	)
)
(instance flower of Iitem
	(properties
		view:		iFlower
		owner:	40
		name:		{Flower}
		said:		'/flower'
	)
)
(instance hairRejuvenator of Iitem
	(properties
		view:		iHairRejuvenator
		owner:	45
		name:		{Hair Rejuvenator}
		said:		'/rejuvenator[<hair]'
	)
)
(instance suitcase of Iitem
	(properties
		view:		iSuitcase
		owner:	54
		name:		{Suitcase}
		said:		'/suitcase'
	)
)
(instance airlineTicket of Iitem
	(properties
		view:		iAirlineTicket
		owner:	52
		name:		{Airline Ticket}
		said:		'/ticket[<airplane, airline]'
	)
)
(instance parachute of Iitem
	(properties
		view:		iParachute
		owner:	55
		name:		{Parachute}
		said:		'/parachute'
	)
)
(instance bobbyPin of Iitem
	(properties
		view:		iBobbyPin
		owner:	55
		name:		{Bobby Pin}
		said:		'/pin[<bobby]'
	)
)
(instance pamphlet of Iitem
	(properties
		view:		iPamphlet
		owner:	57
		name:		{Pamphlet}
		said:		'/pamphlet'
	)
)
(instance airsickBag of Iitem
	(properties
		view:		iAirsickBag
		owner:	62
		name:		{Airsick Bag}
		said:		'/bag[<airsick]'
	)
)
(instance stick of Iitem
	(properties
		view:		iStick
		owner:	71
		name:		{Stout Stick}
		said:		'/stick'
	)
)
(instance vine of Iitem
	(properties
		view:		iVine
		owner:	74
		name:		{Vine}
		said:		'/vine'
	)
)
(instance ashes of Iitem
	(properties
		view:		iAshes
		owner:	77
		name:		{Ashes}
		said:		'/ash'
	)
)
(instance sand of Iitem
	(properties
		view:		iSand
		owner:	75
		name:		{Sand}
		said:		'/sand'
	)
)


(instance dyingScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(0
				(= currentStatus GAMEOVER)
				(HandsOff)
				(Load SOUND mDead)
				(= seconds 3)
			)

			(1
				(sounds eachElementDo: #dispose:)
				(deadSnd play:)
				(if (Print 
					"Well, Larry, you've\n
					screwed up again!\n\n
					What'dya say we do it\n
					just one more time?"
						title:	{Oh, no! Not again?!}
						font: 	bigFont
						icon: 	vBEdismay 0 0
						button:	{Keep On Muddling} 0
						button:	{Order A Hintbook} 1
						)
					(Print "Dial (209) 683-6858 from 8-5 Pacific Time.
						Have your credit card handy.")
				)
				(repeat
					(switch
						(Print "Remember\n
							save early, save often!"
							title:	{Al says:}
							font: 	bigFont
							button:	{Restore}	1
							button:	{Restart}	2
							button:	{__Quit__}	3
						)
						(1	(theGame restore:))
						(2	(theGame restart:))
						(3	(= quit TRUE)
							(break))
					)
				)
			)
		)
	)
)


(instance statusCode of Code
	(method (doit str)
		(Format str " Score: %d of %d%15s Rank: %s%10s"
			score possibleScore " " rank " ")
	)
)


;**	Makes actor1 face actor2.	Assumes actor1 has four loops.	If a third
;**	argument is sent, actor2 also faces actor 1.

(procedure (LookAt actor1 actor2 both)
	(DirLoop actor1 (GetAngle (actor1 x?) (actor1 y?) (actor2 x?) (actor2 y?)))
	(if (== argc 3)
		(DirLoop actor2 (GetAngle (actor2 x?) (actor2 y?) (actor1 x?) (actor1 y?)))
	)
)


;**	Search in the direction the actor is facing for a control.	If distance
;**	is not specified, search 5 pixels.

(procedure (NearControl	actor	distance )
	(if (< argc	2)
		(=	distance 5)
	)
	(switch (actor	loop?)
		(0	(return (OnControl (actor x?) (actor y?)
				(+ (actor x?) distance) (+	(actor y?) 1))))
		(1	(return(OnControl (-	(actor x?) distance) (actor y?)
				(actor x?) (+	(actor y?) 1))))
		(2	(return(OnControl (actor x?) (actor y?)
				(+	(actor x?) 1) (+	(actor y?) distance))))
		(3	(return(OnControl (actor x?) (-	(actor y?) distance)
				(+	(actor x?) 1) (actor y?))))
	)
)


;**	Take any known instance of ego-jockeying, and return him to his normal,
;**	wonderful self.

(procedure (NormalEgo theLoop)
	(if ( > argc 0)
		(ego loop:		theLoop)
	)
	(ego
		view:				currentEgoView
		setLoop:			RELEASE
		setPri:			RELEASE
		setMotion:		FALSE
		setStep:			3 2
		setCycle:		Walk
		illegalBits:	cLtWhite
		cycleSpeed:		0
		moveSpeed:		0
		ignoreActors:	FALSE
	)
	(= currentStatus NORMAL)
	(User
		canControl:		TRUE
		canInput:		TRUE
	)
)


;**	This will add an actor to the picture at his current location and
;**	appearance, then position him below the screen so he can be used 
;**	again as someone else.

(procedure (AddActorToPic whom)
	(if whom
		((View new:)
			view:			(whom view?)
			loop:			(whom loop?)
			cel:			(whom cel?)
			priority:	(whom priority?)
			posn:			(whom x?) (whom y?)
			addToPic:
		)
		(whom
			posn:			(whom x?) ( + 1000 (whom y?))
		)
	)
)

(instance scoreSnd of Sound
	(properties
		number:			sScored
		loop:				1
		owner:			-1
		priority:		-10
	)
)

(instance deadSnd of Sound
	(properties
		number: 			mDead 
		loop:				1
		priority:		255
	)
)

(procedure (HandsOff)
	(User
		canControl:		FALSE
		canInput:		FALSE
	)
	(ego setMotion:	FALSE)
)

(procedure (HandsOn)
	(User
		canControl:		TRUE
		canInput:		TRUE
	)
	(ego setMotion:	FALSE)
)

(procedure (Notify whom)
	(= whom (ScriptID whom))
	(whom notify: &rest)
)

(procedure (HaveMem howMuch)
	(return (> (MemoryInfo FreeHeap)	howMuch))
)

(procedure (RedrawCast)
	(Animate	(cast	elements?) FALSE)
)

(procedure (SoundLoops who	howMany)
	(who loop:howMany changeState:)
)

(procedure (cls)
	(if modelessDialog
		(modelessDialog dispose:)
	)
)

(procedure	(Ok)					(Print "Ok."))
(procedure	(ItIs)				(Print "It is."))
(procedure	(YouAre)				(Print "You are."))
(procedure	(NotNow)				(Print "Not now!"))
(procedure	(NotClose)			(Print "You're	not close enough."))
(procedure	(AlreadyTook)		(Print "You	already took it."))
(procedure	(SeeNothing)		(Print "You	see nothing	special."))
(procedure	(CantDo)				(Print "You	can't	do	that here; at least, not now."))
(procedure	(DontHave)			(Print "You	don't	have it."))

(procedure	(SetRgTimer name minutes seconds)
	(= currentTimer name)
	(= rgTimer (* 10 (+ seconds (* minutes 60))))
)


;**	(procedure (LogIt)
;**		(if logging
;**			(Format @string
;**				"[r%3d v%3d %3dx/%3dy CS%-5d] Said \"%s\""
;**					curRoomNum (ego view?) (ego x?) (ego y?) 
;**					currentStatus (User inputLineAddr?))
;**			(File name: "input.log" write: @string "\r" close:)
;**		)
;**	)


;**	(procedure (HandsOff)
;**		(if handsOff
;**			(return FALSE)
;**		else
;**			; here insert standard handsoff code
;**			(User canControl: FALSE canInput: FALSE)
;**			(ego setMotion:0)
;**			(return (= handsOff TRUE))
;**		)
;**	)
;**	
;**	(procedure (HandsOn)
;**		; here insert standard handson code
;**		(User canControl: TRUE canInput: TRUE)
;**		(= handsOff FALSE)
;**	)
;**	
;**	 return true if inventory item (ENUM VALUE) is in room
;**	(procedure (InRoom objEnum rm)
;**		(return (== ((inventory at: objEnum) owner?) (if (== argc 1) curRoomNum else rm)))
;**	)
;**	
;**	(procedure (Put objEnum rm)
;**		((inventory at: objEnum) owner:	(if (== argc 1) curRoomNum else rm))
;**	)
;**	
;**	(procedure (setFlag flagEnum)
;**		(|= [gameFlags (/ flagEnum 16)] (>> $8000 (mod flagEnum 16)))
;**	)
;**	
;**	(procedure (clearFlag flagEnum)
;**		(&= [gameFlags (/ flagEnum 16)] (~ (>> $8000 (mod flagEnum 16))))
;**	)
;**	
;**	(procedure (testFlag flagEnum)
;**		(return (& [gameFlags (/ flagEnum 16)] (>> $8000 (mod flagEnum 16))))
;**	)

