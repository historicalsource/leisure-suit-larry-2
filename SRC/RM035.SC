;**
;**	Logics for room 35 -- ship's bar
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 21, 1988
;**


(script#				35)

(public
	rm35				0
)


(define	nearSpinachDip		 73 100 106 104)
(define	nearStool			 64 107 285 128)
(define	alsoNearStool		148 103 222 109)
(define	nearGirls			200 100 300 123)

(define	sitFirst	"You'd better sit down first.")

;(define	vEgoShipBar					104)
	(define	lSitting				0)
	(define	lGetDrink			1)
	(define	lDrinking			2)
	(define	lFalling				3)
	(define	lSpinning			4)

;(define	vShipBar						315)
	(define	lStuff				0)
		(define	cSpinachDip	2)
	(define	lGirl1drinking		1)
	(define	lTitFeeler			2)
	(define	lGirl2drinking		3)
	(define	lGirl3drinking		4)
	(define	lManDrinking		5)
	(define	lStools				6)
	(define	lTV					7)

;(define	vShipBarHenchette			316)
;(define	vShipBartender				319)
	(define	lShaking				0)
	(define	lPouring				1)
	(define	lHandingToEgo		2)


(local
	work
	aSpinachDip
	dipHere
	;**	henchOnScreen
	goto95
	aBartender
	aTV
	aGirl1drinking
	aTitFeeler
	aGirl2drinking
	aGirl3drinking
	aManDrinking
	aShip
	aHench
	henchInvited
)


(enum	1											;**	henchScript state values
	henchInit
	henchComing
	henchAtEgo
	henchLeaving
	henchOffScreen
	henchAllGone
)

(enum
	intro

	gotoStool
	sitDown
	sitting

	standUp
	standing

	gimmeAdrink
	shaking
	pouring
	waitAsec
	handing
	swallowing
	falling
	spinning
	gotcha
	UBdying
)


(instance rm35 of Room
	(properties
		picture:		35
		horizon:		5
		south:		31
	)

	(method (init)
		(Load VIEW vShipBar)
		(Load VIEW vShipBartender)

		(super init:)

		(self
			setRegions:		rgShip
			setScript: 		rm35Script
		)

		(if ((inventory at: iSpinachDip) ownedBy: curRoomNum)
			(= dipHere TRUE)
			((= aSpinachDip (View new:))
				ignoreActors:
				view:				vShipBar
				setLoop:			lStuff
				setCel:			cSpinachDip
				setPri:			4
				posn: 			94 80
				stopUpd:
				init:
			)
		else
			(self setRegions: rgHenchette)
			(= currentHenchView vShipBarHenchette)
			(Load VIEW currentHenchView)
			((= aHench (Actor new:))
				view:				currentHenchView
				posn:				155 234
				illegalBits:	cLtWhite
				setCycle:		Walk
				init:
				setScript:		henchScript
			)
		)

		((View new:)							;**	girl1body
			view: 			vShipBar
			ignoreActors:
			loop: 			0
			cel: 				5
			posn: 			252 145
			setPri: 			11
			addToPic:
		)
		((View new:)							;**	girl2body
			view: 			vShipBar
			ignoreActors:
			loop: 			0
			cel: 				5
			posn: 			319 124
			setPri: 			11
			addToPic:
		)
		((View new:)							;**	man1body
			view: 			vShipBar
			ignoreActors:
			loop: 			0
			cel: 				6
			posn: 			201 148
			setPri: 			11
			addToPic:
		)
		((View new:)							;**	man2body
			view: 			vShipBar
			ignoreActors:
			loop: 			0
			cel: 				6
			posn: 			277 138
			setPri: 			11
			addToPic:
		)
		((View new:)							;**	boatFrontStart
			view: 			vShipBar
			ignoreActors:
			loop: 			6
			cel: 				0
			posn: 			-7 63
			setPri: 			0
			addToPic:
		)
		((View new:)							;**	moreGirl
			view: 			vShipBar
			ignoreActors:
			loop: 			4
			cel: 				0
			posn: 			0 128
			setPri: 			11
			addToPic:
		)
		((View new:)							;**	moreManDrinking
			view: 			vShipBar
			ignoreActors:
			loop: 			5
			cel: 				0
			posn: 			39 133
			setPri: 			11
			addToPic:
		)
		((View new:)							;**	stool
			view: 			vShipBar
			ignoreActors:
			loop: 			0
			cel: 				1
			posn: 			165 105
			setPri: 			6
			addToPic:
		)
		((View new:)							;**	barStuff
			view: 			vShipBar
			ignoreActors:
			loop: 			0
			cel: 				0
			posn: 			165 80
			setPri: 			2
			addToPic:
		)
		((View new:)							;**	bargirl1
			view: 			vShipBar
			ignoreActors:
			loop: 			0
			cel: 				3
			posn: 			200 104
			setPri: 			6
			addToPic:
		)
		((View new:)							;**	bargirl2
			view: 			vShipBar
			ignoreActors:
			loop: 			0
			cel: 				4
			posn: 			227 102
			setPri: 			6
			addToPic:
		)

		((= aBartender (Prop new:))
			view:				vShipBartender
			ignoreActors:
			setLoop:			0
			setPri:			3
			posn:				183 69
			stopUpd:
			init:
		)
		((= aTV (Prop new:))
			view:				vShipBar
			ignoreActors:
			setLoop:			lTV
			setPri:			3
			posn:				162 43
			setCycle:		Forward
			isExtra:			TRUE
			init:
		)

		((= aGirl1drinking (Extra new:))	;**	girl1drinking
			view:				vShipBar
			loop: 			lGirl1drinking
			cel: 				0
			posn: 			313 101
			setPri: 			7
			minPause:		20
			maxPause:		40
			minCycles:		5
			maxCycles:		10
			init:
		)
		((= aTitFeeler (Extra new:))		;**	TitFeeler
			view:				vShipBar
			loop: 			lTitFeeler
			cel: 				0
			posn: 			121 105
			setPri: 			6
			minPause:		20
			maxPause:		30
			minCycles:		5
			maxCycles:		8
			init:
		)
		((= aGirl2drinking (Extra new:))	;**	girl2drinking
			view:				vShipBar
			loop: 			lGirl2drinking
			cel: 				0
			posn: 			19 89
			setPri: 			7
			minPause:		22
			maxPause:		50
			minCycles:		7
			maxCycles:		12
			init:
		)
		((= aGirl3drinking (Extra new:))	;**	girl3drinking
			view:				vShipBar
			loop: 			lGirl3drinking
			cel: 				0
			posn: 			66 145
			setPri: 			11
			init:
		)
		((= aManDrinking (Extra new:))	;**	manDrinking
			view:				vShipBar
			loop: 			lManDrinking
			cel: 				0
			posn: 			113 148
			setPri: 			11
			minPause:		32
			maxPause:		70
			minCycles:		5
			maxCycles:		10
			init:
		)

		((= aShip (Actor new:))
			view:				vShipBar
			illegalBits:	0
			ignoreActors:
			setLoop:			6
			setCel:			0
			setPri:			0
			setStep:			1 1
			moveSpeed:		1
			posn:				999 999
			init:
			setScript:		shipScript
		)

		(NormalEgo)
		(ego
			posn: 			157 158
			init:
		)
	)
)


(instance rm35Script of Script
	(method (doit)
		(super doit:)

		(if (& (ego onControl:) cBlue)
			(if (== goto95 FALSE)
				(curRoom newRoom: 31)
			else
				(Print "You meet the lovely blond on the ship's stairs,
					quickly become dear friends,
					then head for the fantail where she parks her private
					helicopter.
					A romantic, moonlight chopper ride across the South Pacific
					is followed by an intriguing look at her impressive
					garage door, which looks suspiciously like a
					mountaintop waterfall.")
				(Print "Later, that night...")
				(= currentStatus HEADING_FOR_DEATH_SCENE)
				(curRoom newRoom: 95)
			)
		)

		(if (and henchOnScreen
					henchInvited
					( > (ego y?) 146))
			(= henchInvited FALSE)
			(= currentStatus HEADING_FOR_DEATH_SCENE)
			(= goto95 TRUE)
			(curRoom south: 95)				;**	Too bad, Larry.
			(Print "\"Hey, baby,\" you cry, \"wait for me!
				I think I love ya!\"")
		)
	)


	(method (handleEvent event &tmp)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (and	henchOnScreen
					( < (henchScript state?) henchOffScreen)
					(Said '/girl>'))
			(cond
				((Said 'talk/')
					(Print (Format @string
						"\"Hi ya, beautiful,\" you tell her. %s
						Where'dja get that dress?" laffer))
				)
				((Said 'look/')
					(Print "Too bad she couldn't afford to buy a complete dress!")
				)
				(else
					(Print "She looks like she has something you need, Larry.")
					(Print "(And vice versa.)" LOW)
				)
			)
		)

		(if (Said 'look>')
			(if (and henchOnScreen
						(Said '/woman'))
				(if ( < (henchScript state?) henchAtEgo)
					(Print "A beautiful woman is entering the bar,
						and it looks like she's heading for you, Larry")
				else
					(Print "BOING!")
				)
			)

			(if (Said '/bartender')
				(Print "He looks extremely bored.")
			)

			(if (Said '/table, man, woman, people')
				(Print "All of the tables are occupied by people enjoying
					themselves.")
				(Print "(And they don't want to be bothered by you!)" LOW)
			)

			(if (Said '/window, ship')
				(Print "There's something moving out there!")
				(Print "Oh, wait. That's just the ship; this room is revolving!")
			)

			(if (Said '/light')
				(Print "You've always loved black lights.")
			)

			(if (Said '/screen, tv')
				(Print "Shouldn't you turn off this computer if you're going
					to watch television?")
			)

			(if (Said '/bottle, bar')
				(if (== currentStatus SITTING)
					(Print "The bartender is looking at you, expecting you to
						order a drink.")
				else
					(Print "There is a handsome bartender behind the bar and
						one empty stool in front of it.")
				)
				(if dipHere
					(Print "There is a complimentary bowl of spinach dip at the
						left end of the bar.")
				)
			)

			(if (and	dipHere
						(Said '/bowl, dip'))
				(Print "The spinach dip resides in a bowl made of sourdough
					bread, and looks delicious.")
			)

			(if (Said '[/room]')
				(Print "The ship's revolving nightclub has a television set
					behind the bar and only one empty stool.")
				(if dipHere
					(Print "There is a complimentary bowl of spinach dip at the
						left end of the bar.")
				)
			)
		)

		(if (Said 'sit[/down, chair]')
			(cond
				((== currentStatus SITTING)
					(YouAre)
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				((and	(not (ego inRect: alsoNearStool))
						(not (ego inRect: nearStool)))
					(Print "There's only one seat available to you, Larry:
						that empty stool at the bar.")
				)
				(else
					(self changeState: gotoStool)
				)
			)
		)

		(if (or	(Said 'rise, (get<up), stand[/up]')
					(Said 'leave[/seat]'))
			(cond
				((== currentStatus NORMAL)
					(Print sitFirst)
				)
				((!= currentStatus SITTING)
					(NotNow)
				)
				(else
					(self changeState: standUp)
				)
			)
		)

		(if (Said 'talk/bartender')
			(cond
				((== currentStatus NORMAL)
					(Print sitFirst)
				)
				((!= currentStatus SITTING)
					(NotNow)
				)
				(else
					(Print (Format @string
						"\"Hi,\" you say to the bartender with a smile.
						%s" laffer))
					(Print "\"Thanks for telling me,\" he responds sarcastically.")
					(Print "(Well, that didn't work Larry!)" LOW)
				)
			)
		)

		(if (Said 'talk/girl')
			(cond
				((== currentStatus NORMAL)
					(Print sitFirst)
				)
				((!= currentStatus SITTING)
					(NotNow)
				)
				(else
					(Print (Format @string
						"\"Hi, you bevy of beautiful babes,\"
						you say to the women at the bar, flashing your best smile.
						%s" laffer))
					(Print "\"Why don't you join hands
						and try communicating with the living?\"
						she asks sarcastically.")
					(if ( > filthLevel 10)
						(Print "\"What an asshole,\" she tells her friend.")
					)
				)
			)
		)

		(if (Said 'talk')
			(Print "There's no one here interested in talking to you.
				Perhaps you could talk to the bartender.")
		)

		(if (or	(Said 'give/me/beer')
					(Said 'order/beer'))
			(cond
				((== currentStatus NORMAL)
					(Print sitFirst)
				)
				((!= currentStatus SITTING)
					(NotNow)
				)
				(else
					(Print "\"We're all out of that,\" says the bartender.
						\"Would you like a mixed drink?\"")
				)
			)
		)

		(if (or	(Said 'give/me/drink')
					(Said 'order/drink'))
			(cond
				((== currentStatus NORMAL)
					(Print sitFirst)
				)
				((!= currentStatus SITTING)
					(NotNow)
				)
				(else
					(self changeState: gimmeAdrink)
				)
			)
		)

		(if (and	dipHere
					(Said 'eat/dip'))
			(cond
				((!= currentStatus NORMAL)
					(NotNow)
				)
				((not (ego inRect: nearSpinachDip))
					(NotClose)
				)
				(else
					((inventory at: iSpinachDip) moveTo: LIMBO)
					(aSpinachDip dispose:)
					(= dipHere FALSE)
					(theGame changeScore: -5)
					(Print "Ok. You dip a little sourdough bread into the
						mayonnaise-rich dip. Say! This is tasty.
						You ravenously devour the entire bowl,
						consuming approximately a quart of mayonnaise in the process
						and blowing your diet for the next month.
						Geez, Larry; you could have left a little for the other
						people here!" WIDE draw:)
				)
			)
		)

		(if (Said 'get/dip')
			(cond
				((== dipHere FALSE)
					(AlreadyTook)
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				((not (ego inRect: nearSpinachDip))
					(NotClose)
				)
				(else
					(Print "You grab the loaf of french bread
						filled with spinach, mayonnaise and spices.
						Umm, good!")
					(ego get: iSpinachDip)
					(theGame changeScore: 2)
					(aSpinachDip dispose:)
					(= dipHere FALSE)
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(intro)

			;**	This handles sitting down and getting up off the barstool.

			(gotoStool
				(= currentStatus WALKING)
				(HandsOff)
				(Ok)
				(ego
					setMotion:		MoveTo 166 107 self
				)
			)

			(sitDown
				(ego
					view:				vEgoShipBar
					ignoreActors:
					illegalBits:	0
					setMotion:		FALSE
					posn:				163 96
					setLoop:			lSitting
					setPri:			brown
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(sitting
				(= currentStatus SITTING)
				(if (and (== dipHere FALSE)
							(not (henchScript state?)))	;**	1/customer
					(henchScript changeState: henchInit)
				)
				(User
					canInput:		TRUE
				)
			)

			(standUp
				(Ok)
				(= currentStatus WALKING)
				(HandsOff)
				(ego
					setLoop:			lSitting
					setMotion:		FALSE
					setCel:			LastCel
					setCycle:		BegLoop self
				)
			)

			(standing
				(ego	posn:			164 107)
				(NormalEgo facingNorth)
			)


			;**	This handles the bartender slipping Larry a Mickey.

			(gimmeAdrink
				(= currentStatus NEAR_DEATH)
				(HandsOff)
				(henchScript changeState: 255)
				(Print "\"Do you have any drinks with parasols and fruit?\"
					you ask the bartender.")
				(= seconds 3)
			)

			(shaking
				(Print "\"But, of course,\" he replies,
					\"allow me to fix you the speciality of the house...\"")
				(Print "\"Heavy on the pineapple, ok?\" you ask.")
				(Print "\"No problem,\" he says.
					\"Somehow, I would have guessed that!\"")
				(aBartender
					setLoop:			lShaking
					setCycle:		Forward
				)
				(= seconds 5)
			)

			(pouring
				(aBartender
					setLoop:			lPouring
					cel:				0
					cycleSpeed:		1
					setCycle:		EndLoop self
				)
			)

			(waitAsec
				(= seconds 2)
			)

			(handing
				(aBartender
					setLoop:			lHandingToEgo
					cel:				0
					cycleSpeed:		0
					setCycle:		EndLoop self
				)
				(ego
					setLoop:			lGetDrink
					cycleSpeed:		0
					cel:				0
					setCycle:		EndLoop
				)
			)

			(swallowing
				(aBartender			stopUpd:)
				(Print "\"Here's to ya,\" you say to no one in particular.")
				(ego
					setLoop:			lDrinking
					cel:				0
					setCycle:		EndLoop
				)
				(= seconds 3)
			)

			(falling
				(ego
					cycleSpeed:		1
					setLoop:			lFalling
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(spinning
				(ego
					cycleSpeed:		0
					setLoop:			lSpinning
					cel:				0
					setCycle:		Forward
				)
				(= seconds 5)
			)

			(gotcha
				(Print "\"Ah, ha!
					I gotcha, you Vicious Devil-Enemy of the People!!\"
					cries the bartender,
					subtlely revealing himself as a member of the KGB.
					\"How's that for 'extra pineapple,' you sap?\"")
				(= seconds 3)
			)

			(UBdying
				(Print "The bartender's mickey has the desired effect upon your
					central nervous system.
					You give the KGB no problems during transit to their
					local headquarters.
					We'll drop by there, just to see how you and they are
					getting along...")
				(= currentStatus HEADING_FOR_DEATH_SCENE)
				(curRoom newRoom: 96)
			)
		)
	)
)


(instance shipScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(0
				(aShip
					setCel:			(if (aShip cel?) 0 else 1)
					posn:				-30 64
					setMotion:		MoveTo 333 64 self
				)
			)

			(1
				(= cycles (Random 30 50))
				(= state -1)
			)
		)
	)
)


(instance henchScript of Script
	(method (changeState newState)

		(if (== currentStatus NEAR_DEATH)
			(return)
		)

		(switch (= state newState)

			(henchInit
				(= cycles (Random 50 100))
			)

			(henchComing
				(if (!= currentStatus SITTING)
					(-- state)					;**	try again later
					(= cycles (Random 50 100))
				else
					(aHench
						setMotion: MoveTo 157 107 self
					)
					(Notify rgHenchette 1)			;**	play music 1x
					(= henchOnScreen TRUE)
				)
			)

			(henchAtEgo
				(aHench loop: facingEast)
				(Print "\"Hello, studly,\"
					says the beautiful blond,
					\"looking for a good time?
					Want to go to my place,
					instead of just hanging around here all alone?\"" draw: HIGH)
				(Print "(Geez, Larry. A real woman! Talking to you!!)" LOW)
				(= seconds 5)
			)

			(henchLeaving
				(Print "\"Come on, sweetheart,
					let's go someplace more private!\"")
				(aHench setMotion: MoveTo 155 234 self)
				(= henchInvited TRUE)
			)

			(henchOffScreen
				(= seconds 10)
			)

			(henchAllGone
				(aHench		dispose:)
				(= currentHenchView FALSE)
				(= henchOnScreen FALSE)
				(= henchInvited FALSE)
			)
		)
	)
)

