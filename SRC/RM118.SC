;**
;**	Logics for room 118 -- drug store
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 13, 1988
;**


(script#				118)

(public
	rm118				0
)


(define	nearCounter		216 118 237 134)
(define	seesSunscreen	  0 115 105 138)
(define	nearSunscreen	 66 115	90 134)

;(define	vDrugInt						228)
;(define	vDrugClerk					229)
	(define	lNormal				0)
	(define	lPickingNose		1)
	(define	lCountingMoney		2)
	(define	lTalk					3)
	(define	lBigClerk			4)


(local
	work
	seenMessage
	owesForSunscreen
	aClerk
)

(enum
	intro

	picket
	normalClerk

	talkToClerk
	clerkSpeaks

	buySunscreen
	clerkResponds
	countingDone
)


(instance rm118 of Room
	(properties
		picture:		118
		horizon:		5
	)

	(method (init)
		(Load VIEW vDrugClerk)
		(Load VIEW vDrugInt)

		(super init:)

		((View new:)							;**	precription sign
			view: 			vDrugInt
			loop: 			0
			cel: 				0
			posn: 			144	37
			setPri:			0
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	prescription counter
			view: 			vDrugInt
			loop: 			0
			cel: 				1
			posn: 			144	84
			setPri:			4
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	lf display
			view: 			vDrugInt
			loop: 			1
			cel: 				0
			posn:				90 136
			setPri: 			10
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	rf display
			view: 			vDrugInt
			loop: 			1
			cel: 				0
			posn: 			171 136
			setPri: 			10
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	counter display
			view: 			vDrugInt
			loop: 			1
			cel: 				1
			posn: 			283 114
			setPri:			7
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	sale sign
			view: 			vDrugInt
			loop: 			1
			cel: 				2
			posn: 			279 137
			setPri:			6
			ignoreActors:
			addToPic:
		)

		(aBigClerk
			view:				vDrugClerk
			setLoop:			lBigClerk
			posn:				273 1059
			setPri:			15
			setCycle:		Forward
			init:
		)
		((= aClerk (Prop new:))
			view:				vDrugClerk
			setLoop:			0
			setPri:			2
			posn:				261 108
			init:
		)

		(NormalEgo)
		(ego
			posn: 			216 162
			init:
		)

		(self
			setRegions:		rgLA
			setScript:		rm118Script
		)
	)
)


(instance rm118Script of Script
	(method (doit)
		(super doit:)

		(if (& (ego onControl: origin) cBlue)
			(if timer (timer dispose:))
			(curRoom newRoom: 18)
		)

		(if (not (& (ego onControl:) cGreen))
			(= seenMessage FALSE)
		else
			(if (and	owesForSunscreen
						(not seenMessage))
				(= seenMessage TRUE)
				(Print "Nice guy!
					Trying to shoplift from this nice, young clerk.")
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/wall, ceiling')
				(Print "The walls are covered with many items,
					none of which you need.")
			)

			(if (Said '/sign')
				(Print "\"Just Say No!\"")
			)
													 
			(if (Said '/counter')
				(Print "Behind the counter sits the epitome of the surfer culture.")
			)

			(if (Said '/clerk, person, boy')
				(aBigClerk posn: 273 59)
				(HandsOff)
				(Timer setReal: aBigClerk 5)
			)

			(if (Said '/sale')
				(Print "Something is always for sale here!")
			)

			(if (Said '/shelf, bottle, counter, cabinet')
				(cond
					((ego has: iSunscreen)
						(Print "There is nothing more of interest here.")
					)
					((not (ego inRect: seesSunscreen))
						(Print "There are shelves filled with sundries,
							but you see nothing that seems useful.")
					)
					((not (ego has: iCruiseTicket))
						(Print "You might be able to use some of that sunscreen.")
					)
					(else
						(Print "Say, a bottle of that
							sunscreen might be handy in the South Pacific!")
					)
				)
			)

			(if (Said '[/room, building, store]')
				(Print "By the sign on the druggist's counter,
					you presume the druggist is out.
					Only a clerk remains, and he doesn't look too bright.")
			)
		)


		(if (Said 'get/sunscreen, (lotion<suntan)')
			(cond
				((not (ego inRect: nearSunscreen))
					(NotClose)
				)
				((ego has: iSunscreen)
					(Print "One bottle is enough!")
				)
				(else
					(if (ego has: iCruiseTicket)
						(Print "You grab a bottle of SPF 90 while you picture
							Bachelorette Barbara rubbing it all over you!")
					else
						(Print "You take a bottle of sunscreen.")
					)
					(= owesForSunscreen TRUE)
					(ego get: iSunscreen)
					(ego observeControl: cBlue)
				)
			)
		)

		(if (or	(Said 'put/sunscreen, (lotion<suntan)/shelf<back')
					(Said 'put, give/sunscreen, (lotion<suntan)/back')
					(Said 'put, give/sunscreen, (lotion<suntan)<back')
					(Said 'return, put/sunscreen, (lotion<suntan)'))
			(cond
				((not (ego inRect: nearSunscreen))
					(Print "Put it back where you got it.")
				)
				((not (ego has: iSunscreen))
					(DontHave)
				)
				(else
					(Ok)
					(= owesForSunscreen FALSE)
					(ego put: iSunscreen curRoomNum)
					(ego ignoreControl: cBlue)
				)
			)
		)

		(if (or	(Said 'give, hand/clerk, person, man/money')
					(Said 'give/money')
					(Said 'pay'))
			(cond
				((not owesForSunscreen)
					(Print "You don't owe for anything.")
				)
				((not (ego inRect: nearCounter))
					(NotClose)
				)
				(else
					(self changeState: buySunscreen)
				)
			)
		)

		(if (Said 'search, find/sunscreen, (lotion<suntan)')
			(Print "You feel sure it's in here somewhere.")
		)

		(if (Said '/lubber')
			(Print "You must obtain those in the \"Land of the Lounge Lizards!\"")
		)

		(if (Said 'talk/boy, surf, person, clerk')
			(self changeState: talkToClerk)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(= seconds (Random 10 40))
			)

			(picket
				(aClerk
					setLoop:			lPickingNose
					cel:				0
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(normalClerk
				(aClerk
					setLoop:			lNormal
					cel:				0
				)
				(= seconds (Random 10 20))
				(= state intro)
			)


			;**	This part handles him talking.

			(talkToClerk
				(= cycles	0)
				(if (or	(ego has: iMillion)
							(ego has: iMoney))
					(Print "\"Hey, buddy!\" you say,
						\"Have you heard I'm now incredibly wealthy?\"")
				else
					(Print "\"Do you have anything here that costs
						a dollar or less?\" you ask him.")
				)
				(aClerk
					setLoop:			lTalk
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(clerkSpeaks
				(if (or	(ego has: iMillion)
							(ego has: iMoney))
					(Print "\"Gosh,\" he replies, \"I'm so impressed.\"")
				else
					(Print "\"Nope,\" he responds.")
				)
				(self changeState: normalClerk)
			)


			;**	This path is for when ego pays for sunscreen.

			(buySunscreen
				(= cycles	0)
				(= seconds	0)
				(HandsOff)
				(Print "\"I'd like to pay for this,\" you tell the clerk.")
				(cond
					((ego has: iMillion)
						(Print "\"Do you have change for a million-dollar bill?\"")
					)
					((ego has: iMoney)
						(Print "\"Do you have change for a hundred?\"")
					)
					((ego has: iDollar)
						(Print "\"But all I have is a dollar.\"")
					)
					(else
						(Print "\"But I have no money,\" you whine.
							\"Could you find it in your heart to let me
							have a little bottle of sunscreen to protect
							my skin from the ozone-depleted atmosphere?\"")
					)
				)
				(aClerk
					setLoop:			lTalk
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(clerkResponds
				(cond
					((ego has: iMillion)
						(Print "\"Yeah, sure,\" he says,
							\"do I look like they'd trust me with a million in
							change?\"")
						(aClerk
							setLoop:			lNormal
						)
					)
					((ego has: iMoney)
						(Print "\"No,\" he says,
							\"But feel free to give me a big tip!\"")
						(Print "\"But, of course, my good man,\"
							you tell him, suddenly putting on airs,
							\"have a hundred; in fact, have two.
							Money means nothing to me!\"")
						(aClerk
							setLoop:			lCountingMoney
							cycleSpeed:		1
							cel:				0
							setCycle:		EndLoop
						)
					)
					(else
						(Print "\"No way, dude!\"")
						(aClerk
							setLoop:			lNormal
						)
					)
				)
				(= seconds 4)
			)

			(countingDone
				(NormalEgo)
				(if (ego has: iMoney)
					(Print "\"Like, thanks, dude,\" he tells you,
						\"now let me go back to sleep.\"")
					(Print (Format @string
						"He concludes with a familiar,
						\"%s\"" tritePhrase))
					(= owesForSunscreen FALSE)
					(if (not scoredSunscreen)
						(= scoredSunscreen TRUE)
						(theGame changeScore: 9)
					)
				else
					(Print "Looks like you're out of luck here, Larry.")
					(ego observeControl: cBlue)
				)
				(self changeState: normalClerk)
			)
		)
	)
)


(instance aBigClerk of Prop
	(method (cue)
		(Print "You find women sexy!")
		(Print "(How you wish this were one!)" at: 75 155 width: 170)
		(self posn: 273 1059)
		(HandsOn)
	)
)

