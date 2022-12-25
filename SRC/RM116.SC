;**
;**	Logics for room 116 -- clothing store interior
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 23, 1988
;**


(script#				116)

(public
	rm116				0
)


(define	nearCounter		120 130 192 140)
(define	nearSwimsuit	137	88 182	96)
(define	clerkY			106)
(define	clerkLeftX		138)
(define	clerkRightX		174)

;(define	vClothingInt				225)
;(define	vClothingClerk				226)
	(define	lDrumToWalk		2)				;**	0 and 1 are normal walkers
	(define	lDrumming		3)
	(define	lBigHand			4)
	(define	lBigClerk		5)


(local
	swimsuitHere
	seenMessage
	owesForSwimsuit
	aClerk
	aBigHand
)


(enum
	intro
	goWest
	waitAtWest
	drumFingers
	drumToWalker
	goEast
	waitAtEast

	buySwimsuit
	atEgo
	thatsAll
	yourChangeIs
	oneH
	twoH
	threeH
	fourH
	neverMind
	veryWell
	allOver
)


(instance rm116 of Room
	(properties
		picture:		116
		horizon:		5
	)

	(method (init)
		(Load VIEW vClothingInt)
		(Load VIEW vClothingClerk)

		(super init:)

((View new:) ignoreActors: view:225 loop:0 cel:0 posn:160	90 setPri: 5 addToPic:)
((View new:) ignoreActors: view:225 loop:1 cel:0 posn:238 151 setPri:11 addToPic:)
((View new:) ignoreActors: view:225 loop:1 cel:1 posn: 72	86 setPri: 5 addToPic:)
((View new:) ignoreActors: view:225 loop:2 cel:0 posn: 85 119 setPri: 8 addToPic:)
((View new:) ignoreActors: view:225 loop:2 cel:0 posn: 97 110 setPri: 7 addToPic:)
((View new:) ignoreActors: view:225 loop:2 cel:1 posn: 74 130 setPri: 9 addToPic:)
((View new:) ignoreActors: view:225 loop:2 cel:1 posn: 62 139 setPri:10 addToPic:)
((View new:) ignoreActors: view:225 loop:2 cel:2 posn:257 135 setPri: 9 addToPic:)
((View new:) ignoreActors: view:225 loop:2 cel:2 posn:250 132 setPri: 9 addToPic:)
((View new:) ignoreActors: view:225 loop:2 cel:2 posn:239 125 setPri: 8 addToPic:)
((View new:) ignoreActors: view:225 loop:2 cel:2 posn:264 140 setPri:10 addToPic:)
((View new:) ignoreActors: view:225 loop:2 cel:3 posn:216 109 setPri: 6 addToPic:)
((View new:) ignoreActors: view:225 loop:2 cel:3 posn:228 116 setPri: 7 addToPic:)

		(if (ego has: iMillion)
			(Load VIEW iMillion)
			(= swimsuitHere TRUE)
			((View new:)
				view:			225
				cel:			1
				posn:			157 66
				addToPic:
			)
		)

		(aBigClerk
			view:				vClothingClerk
			setLoop:			lBigClerk
			posn:				273 1059
			setPri:			15
			setCycle:		Forward
			init:
		)
		((= aBigHand (Prop new:))
			view:				vClothingClerk
			setLoop:			lBigHand
			setCel:			0
			setPri:			15
			posn:				29 1188
			setCycle:		Forward
			cycleSpeed:		2
			ignoreActors:
			init:
		)
		((= aClerk (Actor new:))
			view:				vClothingClerk
			setLoop:			3
			setCel:			0
			setPri:			9
			posn:				clerkRightX clerkY
			ignoreActors:
			setCycle:		Walk
			init:
		)

		(NormalEgo)
		(ego
			posn: 			161 162
			init:
		)

		(self
			setRegions:		rgLA
			setScript: 		rm116Script
		)
	)
)


(instance rm116Script of Script
	(method (doit)
		(super doit:)

		(if (& (ego onControl:) cBlue)
			(curRoom newRoom: 16)
		)

		(if (not (& (ego onControl:) cGreen))
			(= seenMessage FALSE)
		else
			(if (and owesForSwimsuit
						(not seenMessage))
				(= seenMessage	TRUE)
				(Print "\"Molto Lira's\" sophisticated, computerized, automatic,
					shoplifting prevention devices keep this door locked while
					customers carry merchandise for which they have not paid.")
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (and	(== swimsuitHere TRUE)
						(Said '/wall, sign'))
				(Print "\"Swimsuits\n\n1/2-PRICE!\"")
			)

			(if (Said '/rack, bikini, (suit<swim), swimsuit')
				(cond
					((not (ego inRect: nearSwimsuit))
						(Print "There is a rack of swimsuits against the rear wall.")
					)
					((ego has: iSwimsuit)	;**	he paid for it
						(Print "The rest of these suits appear to be too
							generously cut in the crotch region for a guy like you!")
					)
					((!= swimsuitHere TRUE)
						(Print "There's nothing in this place that you could
							afford!")
					)
					(else
						(Print "Wait a minute;
							these swimsuits are marked down to half-price,
							only $100,000.00.
							With your new-found wealth, you could afford
							that little, blue, bikini job hanging there.")
					)
				)
			)

			(if (Said '/suit')
				(Print "Never in your life have you seen a man's suit that
					required a home mortgage!")
				(Print "Isn't a million, five a little steep,
					even if the suit IS imported?")
			)

			(if (Said '/picture, ceiling')
				(Print "Evidently, while decorating this store,
					cost was no object.")
				(Print "(And also, no help!)" LOW)
			)

			(if (Said '/ring')
				(Print "It appears that you are boring the beautiful lady.")
			)

			(if (Said '/wall')
				(Print "Along the right and left walls are rows of expensive,
					Italian, hand-made virgin-wool suits.")
				(Print "From the prices here, they should have been hand-knit
					by virgins!")
			)

			(if (Said '/statue')
				(Print "What ARE they doing?")
			)

			(if (Said '/counter, hand, girl, clerk')
				(aBigClerk
					posn: 		159 59
					stopUpd:
				)
				(Print "Behind an unbelievably beautiful, imported,
					Italian-marble counter
					stands an unbelievably beautiful, imported, Italian clerk."
					draw:)
				(Timer setReal: aBigClerk 5)
				(HandsOff)
			)

			(if (Said '[/room, store, building, display, ceiling, floor]')
				(if (ego has: iSwimsuit)
					(Print "There's nothing else here you can afford.")
				else
					(Print "This place may be over-priced,
						but at least it's gaudy!")
					(if (== swimsuitHere TRUE)
						(Print "There is a sale sign on the rear wall.")
					)
				)
			)
		)


		(if (Said 'get/bikini, (suit<swim), swimsuit')
			(cond
				((not (ego inRect: nearSwimsuit))
					(Print "There are swimsuits for sale on the rack at the rear 
						of the store.
						Why not walk over there?")
				)
				((ego has: iSwimsuit)
					(Print "At these prices, one's enough!")
				)
				((!= swimsuitHere TRUE)
					(Print "Nah. No way!")
				)
				(else
					(Print "You select a reasonably tight Spandex job in blue.
						Although it's cut a little tight,
						perhaps that may work to your advantage on the ship.
						In fact, this may be the beginning of a
						whole new image for you, Larry!")
					(= owesForSwimsuit TRUE)
					(ego get: iSwimsuit)
					(theGame changeScore: 5)
					(ego observeControl: cBlue)
				)
			)
		)

		(if (or	(Said 'give, hand/clerk, person, girl/money')
					(Said 'give, hand/money')
					(Said 'pay'))
			(cond
				((not owesForSwimsuit)
					(Print "Presently, you don't owe any money.")
				)
				((not (ego inRect: nearCounter))
					(Print "\"I'm sorry,\" says the dark-haired, Italian beauty,
						\"I'm only allowed to accept purchases
						across the front of this counter!\"")
				)
				(else
					(self changeState: buySwimsuit)
				)
			)
		)

		(if (Said 'get/suit')
			(Print "You don't have $1,500,000!")
		)

		(if (Said 'talk/girl, person, clerk')
			(cond
				((or	(ego has: iMillion)
						(ego has: iMoney))
					(Print "\"Hey, my little Latino bambino!\" you say,
						\"Have I told you I'm now incredibly wealthy?\"")
					(Print "You're sure to endear yourself instantly
						to her with that pseudo-bi-lingualism.")
					(Print "\"Oh?\" she replies,
						\"How many shipping lines do you own?\"")
					(Print "(Ouch.)" LOW)
				)
				(else
					(Print "\"Hey, baby, you don't look like a
						California girl!\" you tell her.")
					(Print "She successfully ignores you.")
				)
			)
		)
	)


	(method (changeState newState &tmp work)
		(switch (= state newState)

			(intro
				(= seconds (Random 5 10))
			)

			(goWest
				(aClerk
					setLoop:			facingWest
					setMotion:		MoveTo clerkLeftX clerkY self
					setCycle:		Walk
				)
			)

			(waitAtWest
				(aClerk
					setCel:			0
				)
				(= seconds (Random 10 20))
			)

			(drumFingers
				(aClerk
					setLoop:			lDrumming
					setCel:			0
					setCycle:		Forward
					cycleSpeed:		2
				)
				(if ( > machineSpeed 30)
					(aBigHand
						setCel:			0
						setCycle:		Forward
						posn:				29 188
					)
				)
				(= seconds (Random 5 10))
			)

			(drumToWalker
				(aClerk
					setLoop:			lDrumToWalk
					setCel:			0
					setCycle:		EndLoop self
					cycleSpeed:		0
				)
				(aBigHand
					posn:				29 1188
				)
			)

			(goEast
				(aClerk
					setLoop: 		facingEast
					cycleSpeed:		1
					setMotion:		MoveTo clerkRightX clerkY self
					setCycle:		Walk
				)
			)

			(waitAtEast
				(= state intro)
				(aClerk
					setCel:			0
				)
				(= seconds (Random 20 30))
			)


			;**	The following section handles purchase of the swimsuit,
			;**	preventing additional actions on the part of the clerk,
			;**	and her big hand.

			(buySwimsuit
				(= seconds 0)
				(= swimsuitHere FALSE)
				(HandsOff)
				(Print "\"Excuse me, miss,\" you say hesitantly.
					\"Do you have change for a million?\""
					icon: iMillion 0 0
					)
				(aBigHand 			hide:)
				(= work (ego x?))				;**	get destination near ego
				(if ( < work clerkLeftX)	;**	handle out of bounds first
					(= work clerkLeftX))
				(if ( > work clerkRightX)
					(= work clerkRightX))
				(aClerk
					setLoop:			RELEASE
					setCycle:		Walk
					setMotion:		MoveTo work clerkY self
				)
			)

			(atEgo
				(aClerk
					setLoop:			2
					setCel:			2
				)
				(Print "\"Bella bueno,\" she smiles,
					\"but, of course.
					But, is that trivial, little, clearance item
					all you are going to purchase today?\"" draw:)
				(= seconds 3)
			)

			(thatsAll
				(Print "\"Yes, ma'am,\" you reply, handing over
					your lottery winnings.
					\"I've only got a million on me!\"")
				(ego put: iMillion LIMBO)
				(= seconds 3)
			)

			(yourChangeIs
				(Print "\"All right, that will be $106,500.00 including tax,
					your change is $893,500.00.\"")
				(= seconds 2)
			)

			(oneH
				(Print "\"That's one hundred dollars...\"")
				(= seconds 2)
			)

			(twoH
				(Print "\"two hundred dollars...\"")
				(= seconds 2)
			)

			(threeH
				(Print "\"three hundred dollars...\"")
				(= seconds 2)
			)

			(fourH
				(Print "\"four hundred dollars...\"")
				(= seconds 2)
			)

			(neverMind
				(Print "\"five hundred dollars...\"")
				(Print "\"STOP! Don't count any more,\" you shout,
					\"I don't have THAT long before my ship leaves!!\"")
				(= seconds 2)
			)

			(veryWell
				(Print (Format @string
					"\"Very well, sir,\" she concludes.
					\"%s\"" tritePhrase))
				(Print "How did you know she was going to say that?!")
				(= cycles 6)
			)

			(allOver
				(Print "You pocket your new, gigantic wad of hundred-dollar
					bills.")
				(NormalEgo)
				(ego get: iMoney)
				(= owesForSwimsuit FALSE)
				(theGame changeScore: 3)
			)
		)
	)
)

(instance aBigClerk of Prop
	(method (cue)
		(Print "You find Italian women sexy.")
		(Print "(But then, you find any woman sexy!)" at: 55 155 width: 210)
		(self posn:	273 1059)
		(HandsOn)
	)
)
