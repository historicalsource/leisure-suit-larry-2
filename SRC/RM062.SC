;**
;**	Logics for room 62 -- Larry's airplane seat
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 15, 1988
;**


(script#				62)

(public
	rm62				0
)


(define	nearSeat 		158 94 194 106)
(define	BORE_READING	255)				;**	boreStatus values

;(define	vBore							602)
	(define	lHead				0)
	(define	lTalk				1)
	(define	lNudge			2)
	(define	lBoreReading	3)

;(define	vHairTransplant			604)
;(define	vAirplaneCartRight		610)
;(define	vAirplaneCartLeft			611)

;(define	vEgoSkinny					163)
	(define	lGettingSkinny	0)
	(define	lWriggling		1)
	(define	lSittingSkinny	2)

;(define	vStewardess					600)
	(define	lSgesture		2)
	(define	lStalking		3)


(local
	cartsInAisle
	sawCartMessage
	firstTime

	aStewardess
	aBore
	aCartLeft
	aCartRight
)


(enum
	intro

	entrance
	entrancePunchLine
	lookingAtSeat
	saySeatSkinny
	egoGetSkinny
	egoWriggling
	egoSittingDown
	egoSeated
	enterStewardess
	StewardessTalking
	welcomeAboard
	waterLandingGag
	stewardessGesturing
	oxegenGag
	womenFirstGag
	stewardessOffScreen
	captainAnnouncement
	takeOff
	okToSmoke
	enjoyTrip

	dyingToMeetCha
	downNow
	startKGBcarts
	ubDying
)

(enum 1											;**	cartLeft/Right Script states
	moveCartOn
	cartIn
	cartOut
	moveCartOff
	cartOff
)

(enum 											;**	boreScript states
	boreInit
	mouthMoving
	punchLine
	gesture
	nextLine
	doTransplant
	transplant1
	transplant2
	transplant3
	shutUp
	shutBoreDown
)

(enum 1											;**	boreStatus values
	boreRemembersLarry
	boreAsksName
	rollingInIt
	hairTransplants
	itCouldBeLarry
	initTransplant
)

(enum	1											;**	sittingScript state values
	walkToSeat
	getSkinny
	wriggle
	sittingDown
	seated

	standingUp
	wriggleUp
	getNormal
	standing
)


(instance rm62 of Room
	(properties
		picture:		62
	)

	(method (init)
		(Load VIEW vAirplaneCartLeft)
		(Load VIEW vAirplaneCartRight)
		(Load VIEW vHairTransplant)
		(Load VIEW vMusicHench)

		(if (ego has: iPamphlet)
			(Load VIEW iPamphlet)
		)

	 	(Load SOUND mHenchette)

		(theSound init:)

		(super init:)

		((= aBore (Prop new:))
			view:				vBore
			setCel:			0
			setPri:			5
			posn:				175 86
		)

		(if (== boreStatus BORE_READING)	;**	He's been shut down.
			(aBore
				setLoop:			lBoreReading
				stopUpd:
				init:
			)
		else
			(aBore
				setLoop:			lHead
				stopUpd:
				init:
			)
		)
		((= aCartLeft (Actor new:))
			view:				vAirplaneCartLeft
			setLoop:			facingEast
			yStep:			8
			setCycle:		Walk
			posn:				23 1104
			init:
			setScript: 		cartLeftScript
		)
		((= aCartRight (Actor new:))
			view:				vAirplaneCartRight
			setLoop:			facingWest
			yStep:			8
			setCycle:		Walk
			posn:				308 1104
			init:
			setScript: 		cartRightScript
		)
		(self
			setRegions:		rgAirplane
			setScript:		rm62Script
		)

		(if (== currentStatus GOING_TO_SEAT)
			(= firstTime TRUE)
		else
			(NormalEgo)
		)

		(if firstTime
			(= firstTime FALSE)
			(HandsOff)
			(rm62Script changeState: entrance)
			((= aStewardess (Actor new:))
				view: 			vStewardess
				setLoop: 		facingEast
				posn: 			11 102
				init:
				setCycle:		Walk
			)
		)

		(cond
			((== prevRoomNum	63)		(ego posn: 298 102))
			(else								(ego posn:	37 102))
		)
		(ego					init:)
	)
)


(instance rm62Script of Script
	(method (doit)
		(super doit:)

		(if (== currentStatus NORMAL)
			(if (& (ego onControl:) cBlue)
				(curRoom newRoom: 61)
			)
			(if (& (ego onControl:) cGreen)
				(curRoom newRoom: 63)
			)
		)

		(if (== state takeOff)
			(ShakeScreen 1 (Random 1 3))
		)

		(if (and (==  currentTimer	AIRPLANE_ARRIVAL)
					(not rgTimer)
					(== currentStatus SITTING))
			(= currentStatus NEAR_DEATH)
			(= currentTimer FALSE)
			(rm62Script changeState: dyingToMeetCha)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look, call, ring, talk/stewardess, cart, woman')
			(if (== cartsInAisle FALSE)
				(Print "Whenever you need a stewardess, they're never around.")
			else
				(Print "Why is it they only serve drinks whenever you
					want to walk around the plane?")
			)
		)

		(if (Said 'push, move/cart')
			(if (== cartsInAisle FALSE)
				(Print "Where?")
			else
				(Print "There's no way you can affect them!")
			)
		)

		(if (Said 'look>')
			(if (Said '/pocket, seatback, seat')
				(if ((inventory at: iAirsickBag) ownedBy: curRoomNum)
					(Print "There's nothing there but an airsick bag.")
				else
					(Print "It's empty.")
				)
			)

			(if (Said '/ken')
				(if (== boreStatus BORE_READING)	;**	He's been shut down.
					(Print "He's fascinated by that pamphlet!")
				else
					(Print "He has a deadly glint in his eyes.")
				)
			)

			(if (Said '[/airplane, room]')
				(if ((inventory at: iAirsickBag) ownedBy: curRoomNum)
					(Print "There's not even a magazine to read in
						the seatback pocket in front of you;
						nothing but a slightly crumpled airsick bag.")
				else
					(Print "Boy, are these the cheap seats!")
				)
			)
		)

		(if (Said 'sit')
			(cond
				((== currentStatus SITTING)
					(YouAre)
				)
				((not (ego inRect: nearSeat))
					(NotClose)
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				(else
					(Ok)
					(ego setScript: sittingScript)
					(sittingScript changeState: walkToSeat)
				)
			)
		)

		(if (and (== currentStatus SITTING)
					(Said 'unfasten, fasten/belt'))
			(Ok)
		)

		(if (or	(Said 'rise, (get<up), (stand<up)')	
					(Said 'leave[/chair]'))
			(cond
				((== currentStatus NORMAL)
					(YouAre)
				)
				((!= currentStatus SITTING)
					(NotNow)
				)
				(else
					(Ok)
					(ego setScript: sittingScript)
					(sittingScript changeState: standingUp)
				)
			)
		)

		(if (Said 'buy, take, order/drink')
			(cond
				((not cartsInAisle)
					(Print "Where?")
				)
				((== currentStatus SITTING)
					(NotClose)
				)
				(else
					(Print "\"I'm sorry, sir,\" responds the stewardess,
						\"you must return to your seat
						before I can serve you.\"")
				)
			)
		)

		(if (Said 'talk/man, bore')
			(Print "You'd rather not.
				Besides, the only person he wants to hear is himself!")
		)

		(if (Said 'give, use/pamphlet')
			(cond
				((not (ego has: iPamphlet))
					(DontHave)
				)
				((!= currentStatus SITTING)
					(NotClose)
				)
				(else
					(ego put: iPamphlet LIMBO)
					(theGame changeScore: 8)
					(boreScript changeState: shutBoreDown)
					(Print "Instantly intrigued by the fascinating treatise,
						Ken finally leaves you alone."
						icon:	iPamphlet 0 0
						)
					(Print "The question is:
						have those stewardesses finished serving drinks?")
				)
			)
		)

		(if (Said 'get/bag')
			(cond
				((not ((inventory at: iAirsickBag) ownedBy: curRoomNum))
					(AlreadyTook)
				)
				((!= currentStatus SITTING)
					(NotClose)
				)
				(else
					(Print "You slowly slip your hand inside
						the slightly sticky seatback pocket
						and slide out the semi-soiled airsick bag.")
					(Print "(If you didn't need it before, you need it now!)" LOW)
					(ego get: iAirsickBag)
					(theGame changeScore: 5)
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(intro)

			(entrance
				(= seconds 3)
			)

			(entrancePunchLine
				(Print "Well. Things are slightly closer back here!")
				(ego setMotion: MoveTo 169 102 self)
			)

			(lookingAtSeat
				(= seconds 3)
			)

			(saySeatSkinny
				(Print "\"No way!\" you think to yourself.
					\"I can't possibly fit into such a skinny place.\"")
				(= seconds 3)
			)

			(egoGetSkinny
				(Print "\"Well, here goes...\"")
				(ego
					view:				vEgoSkinny
					setLoop:			lGettingSkinny
					illegalBits:	0
					setCel:			0
					cycleSpeed:		4
					posn:				166 98
					setCycle:		EndLoop self
				)
			)

			(egoWriggling
				(ego
					setLoop: 		lWriggling
					cycleSpeed:		2
					setCycle:		Forward
					moveSpeed:		10
					setStep:			1 1
					setPri: 			6
					setMotion:		MoveTo 167 94
				)
				(= cycles 40)
			)

			(egoSittingDown
				(ego
					setLoop: 		lSittingSkinny
					setCel: 			0
					posn: 			167 94
					cycleSpeed:		4
					setCycle: 		EndLoop self
				)
			)

			(egoSeated
				(ego
					stopUpd:
				)
				(Print "Whew!")
				(= seconds 4)
			)

			(enterStewardess
				(aStewardess
					posn: 			11 102
					setMotion:		MoveTo 50 102 self
				)
			)

			(StewardessTalking
				(aStewardess
					setLoop:			lStalking
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(welcomeAboard
				(Print "\"Good afternoon, ladies uh, er, gentlemen,\"
					says the cute stewardess.
					\"I'd like to welcome you aboard Flight 1.\"")
				(= seconds 3)
			)

			(waterLandingGag
				(Print "\"In the unlikely event of a water landing,
					there's no need to
					panic--because we'll all be dead anyway!\" she laughs.")
				(Print "\"Now don't get upset,
					that's just a little airline humor!\"")
				(= seconds 3)
			)

			(stewardessGesturing
				(aStewardess
					setLoop:			lSgesture
				)
				(= seconds 2)
			)

			(oxegenGag
				(Print "\"Oh, and if during our flight those cute
					little yellow masks happen to drop down from their overhead
					compartments... why, just ignore them.
					Lately, those practical jokers in maintenance have been
					substituting nitrous oxide for the oxygen again!\"")
				(aStewardess
					setLoop:			lStalking
				)
				(= seconds 3)
			)

			(womenFirstGag
				(Print "\"And remember: in case of an emergency,
					we women get to leave first!\"")
				(Print "\"Are there any questions?\" she asks, while leaving.")
				(aStewardess
					setLoop: 		facingWest
					setCycle:		Walk
					setMotion:		MoveTo 11 102 self
				)
			)

			(stewardessOffScreen
				(aStewardess		dispose:)
				(= seconds 5)
			)

			(captainAnnouncement
				(Print "A voice crackles through the loudspeakers,
					\"Please fasten your seatbelt and move your seatback and
					tray table to their fully upright and locked positions.
					We have just been cleared for takeoff.\"")
				(= seconds 5)
			)

			(takeOff
				;**	Although this state looks empty, the doit is providing
				;**	intricate and interesting variations on ShakeScreen here.
				(= seconds 3)					
			)

			(okToSmoke
				(Print "The captain's voice returns,
					\"We have reached our cruising altitude...\"")
				(Print "(Pretty fast takeoff, wasn't it?)" LOW)
				(Print "\"...of 500 feet!\"")
				(Print "\"Passengers in our rear
					smoking compartment may smoke 'em if you gottem.\"")
				(= seconds 6)
			)

			(enjoyTrip
				(Print "\"We hope you enjoy your flight.
					Our ETA is approximately ten minutes.
					Your lovely stewardesses will be serving drinks shortly.
					(But that's just because we couldn't hire any taller
					stewardesses!)\"")
				(Print "The man sitting next to you responds to the
					captain's lame quip with a hearty, \"Har, har, har!\"")
				(aBore setScript: boreScript)
				(ego setScript: sittingScript)
				(sittingScript changeState: seated)
			)


			;**	If he lets the airplane land, we do this:

			(dyingToMeetCha
				(HandsOff)
				(Print "The airplane's PA system again crackles to life,
					\"Ladies and gentlemen, we are starting our final descent
					at this time.\"")
				(= seconds 2)
			)

			(downNow
				(Print "\"Ok, we've landed!\"")
				(Print "(That was even faster than the takeoff!)" LOW)
				(Print "\"On behalf of the entire crew of Flight #1,
					we'd like to welcome you to Calwa
					where the local time is up!\"")
				(Print "\"Please remain in your seats,
					we have some members of the local police force
					coming aboard to check for contraband.\"")
				(= seconds 3)
			)

			(startKGBcarts
				(aCartLeft
					view:			vMusicHench
					posn:			20 104
					setLoop:		RELEASE
					setCycle:	Walk
					show:
					setMotion:	MoveTo 140 104
				)
				(aCartRight
					view:			vMusicHench
					posn:			308 104
					setLoop:		RELEASE
					setCycle:	Walk
					show:
					setMotion:	MoveTo 210 104 self
				)
				(theSound play:)
			)

			(ubDying
				(Print "Something makes you believe these two are no
					\"local police.\"")
				(Print "There must be some way you could escape from this 
					airplane before it lands!")
				(Print "\"Ah, ha! Ve ghot you again, monkey breath!\"
					shouts the KGB agent.")
				(Print "\"Hang on, Larry. Here we go again!!\"")
				(= currentStatus HEADING_FOR_DEATH_SCENE)
				(curRoom newRoom: 96)
			)
		)
	)
)


(instance sittingScript of Script
	(method (changeState newState)
		(switch (= state newState)

			;**	(0) is unused

			(walkToSeat
				(= cycles	0)
				(= seconds	0)
				(HandsOff)
				(cartLeftScript  changeState: moveCartOff)
				(cartRightScript changeState: moveCartOff)
				(ego
					setMotion:		MoveTo 169 98 self
				)
			)

			(getSkinny
				(ego
					view:				vEgoSkinny
					setLoop:			lGettingSkinny
					setCel:			0
					cycleSpeed:		4
					posn:				166 98
					setCycle:		EndLoop	self
				)
			)

			(wriggle
				(ego
					setLoop: 		lWriggling
					cycleSpeed:		2
					setCycle:		Forward
					setPri: 			6
					moveSpeed:		10
					setStep:			1 1
					setMotion:		MoveTo 166 94
					illegalBits:	0
				)
				(= seconds 3)
			)

			(sittingDown
				(ego
					setLoop: 		lSittingSkinny
					setCel: 			0
					posn: 			167 94
					cycleSpeed:		4
					setCycle: 		EndLoop self
				)
				(if (!= boreStatus BORE_READING)	;**	He's been shut down.
					(aBore setScript: boreScript)
				)
			)

			(seated
				(= cycles	0)
				(= seconds	0)
				(ego
					setCel:
					setMotion:		FALSE
					stopUpd:
				)
				(= currentStatus SITTING)
				(User
					canControl: 	FALSE
					canInput: 		TRUE
				)
			)


			;**	Here we wait for him to say \"Stand up.\"

			(standingUp
				(HandsOff)
				(= cycles	0)
				(= seconds	0)
				(ego
					cycleSpeed:		4
					setLoop: 		lSittingSkinny
					setCel:			LastCel
					setCycle: 		BegLoop self
					setMotion:		FALSE
				)
				(boreScript changeState: shutUp)
				(if (!= boreStatus BORE_READING)
					( cartLeftScript changeState: moveCartOn)
					(cartRightScript changeState: moveCartOn)
					(= cartsInAisle TRUE)
					(ego observeControl: cYellow)
				)
			)

			(wriggleUp
				(ego
					setLoop: 		lWriggling
					cycleSpeed:		2
					setCycle:		Forward
					posn: 			166 94
					setPri: 			RELEASE
					setMotion:		MoveTo 166 98
				)
				(= seconds 3)
			)

			(getNormal
				(ego
					setLoop:			lGettingSkinny
					setCel:			LastCel
					moveSpeed:		0
					setStep:			3 2
					setCycle:		BegLoop	self
					cycleSpeed:		4
				)
			)

			(standing
				(ego
					posn: 			169 98
				)
				(NormalEgo facingWest)
			)
		)
	)
)


(instance cartLeftScript of Script
	(method (changeState newState)
		(switch (= state newState)

			;**	(cartInit)	is unused

			(moveCartOn
				(aCartLeft
					posn:			20 104
					setMotion:	MoveTo 140 104 self
				)
			)

			(cartIn
				(aCartLeft
					setMotion:	MoveTo  30 104 self
				)
			)

			(cartOut
				(if (== currentStatus SITTING)
					(self changeState: moveCartOff)
				else
					(= state 0)
					(= cycles (Random 5 30))
				)
			)

			(moveCartOff
				(= cycles	0)
				(= seconds	0)
				(aCartLeft
					setMotion:	MoveTo  20 104 self
				)
			)

			(cartOff
				(= cartsInAisle FALSE)
				(aCartLeft 
					posn:			-1020 104	;**	note: x is way off here
				)
			)
		)
	)
)


(instance cartRightScript of Script
	(method (changeState newState)
		(switch (= state newState)

			;**	(cartInit)	is unused

			(moveCartOn
				(aCartRight
					posn:			308 104
					show:
					setMotion:	MoveTo 210 104 self
				)
				(Print "\"Drinks? Drinks, anyone?\"" draw:)
			)

			(cartIn
				(aCartRight
					setMotion:	MoveTo 308 104 self
				)
			)

			(cartOut
				(if (== currentStatus SITTING)
					(self changeState: moveCartOff)
				else
					(= state 0)
					(= cycles (Random 5 30))
				)
			)

			(moveCartOff
				(= cycles	0)
				(= seconds	0)
				(aCartRight
					setMotion:	MoveTo 308 104 self
				)
			)

			(cartOff
				(= cartsInAisle FALSE)
				(aCartRight 
					posn:			1308 104		;**	Note x is big here only.
				)
			)
		)
	)
)


(instance boreScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(boreInit
				(aBore
					init:
					setLoop:		lHead
					setCycle:	EndLoop self
				)
			)

			(mouthMoving
				(aBore
					setLoop:		lTalk
					setCycle:	Forward
				)
				(= seconds (Random 2 4))
			)

			(punchLine
				(switch (++ boreStatus)
					(boreRemembersLarry
						(Print "\"Hey,\" says the man beside you,
							in his too-loud voice,
							\"I remember you: you're that guy from Lefty's Bar!
							I haven't seen you in a long time.\"")
						(Print "\"Let me tell you this new joke I heard...\"")
						(Print "\"No way,\" you respond, \"no jokes!
							All you ever told me was punch lines!\"")
					)

					(boreAsksName
						(Print "Once again,
							the bore attempts to strike up a conversation.")
						(Print "\"Say, I don't believe we've ever met.
							My name is Ken; what's yours?\"")
						(Print laffer)
						(Print "\"Well, Larry,
							what kind of work are you in?\"")
						(Print "\"I'm not,\" you reply,
							\"I'm recently incredibly rich.\"")
						(Print "\"Oh.\"")
						(Print "(Well, that's one way to stifle him, Larry!)" LOW)
					)

					(rollingInIt
						(Print "\"Say, Larry,
							I'm expecting to be wealthy soon myself,\"
							he continues.
							\"Yep, just bought into a big, national franchise.
							Pretty soon I'll be rolling in it!\"")
						(Print "Define \"it,\" you think to yourself!")
					)

					(hairTransplants
						(Print "\"'Great market opportunity,' this guy told me.
							'Can't miss deal,' he said.
							'Always a steady stream of new customers,'
							he says.\"")
						(Print "\"Betcha you'll never guess my new business,\"
							he concludes.")
						(Print "\"You win.\"")
						(Print "\"HAIR TRANSPLANTS!\"")
						(Print "\"Yep, that's the ticket!
							You know, transplants are so easy nowadays that
							anybody can perform 'em,\" he spouts.
							\"Even a guy like me,
							with nothing but a correspondence course!\"")
						(Print "You scan the aircraft for empty seats.")
						(Print "You find none!")
					)

					(itCouldBeLarry
						(Print "\"You know lil' buddy, I'm lookin' to get my first
							real scalp under my belt right soon now!\"")
						(Print "(Why is this creep staring above your eyes?)" LOW)
					)

					(initTransplant
						(HandsOff)
						(Print "\"I'll help you out, Larry ol' buddy.\"
							he says, opening his briefcase!")
						(Print "\"Just relax.
							I've done this on fourteen dummies and
							haven't lost a one of 'em!\"")
						(Print "\"Har, har, har!!\"")
						(self changeState: doTransplant)
					)
				)
				(aBore
					setLoop:		lNudge
				)
				(= seconds (Random 2 5))
			)

			(gesture
				(aBore
					setLoop:		lHead
					setCel:		LastCel
					setCycle:	BegLoop
				)
				(switch boreStatus
					(boreRemembersLarry	(= seconds (Random 40 60)))
					(boreAsksName			(= seconds (Random 30 50)))
					(rollingInIt			(= seconds (Random 20 40)))
					(hairTransplants		(= seconds (Random 10 30)))
					(itCouldBeLarry		(= seconds (Random  0 20)))
				)
			)

			(nextLine
				(aBore
					setLoop:		lHead
					setCycle:	EndLoop
				)
				(self changeState: mouthMoving)
			)


			;**	We only get here if Larry sat too long without giving the
			;**	bore the pamphlet.

			(doTransplant
				(= cycles	0)
				(= seconds	0)
				(= currentStatus NEAR_DEATH)
				(ego hide:)
				(aBore
					view:			vHairTransplant
					setLoop:		0
					cycleSpeed:	2
					posn:			171 84
					cel:			0
					setCycle:	EndLoop self
				)
			)

			(transplant1
				(aBore
					setLoop:		1
					cel:			0
					setCycle:	EndLoop self
				)
			)

			(transplant2
				(= seconds 3)
			)

			(transplant3
				(Print "What a time for you to forget those complicated
					seat belt instructions!
					Maybe you could distract this guy with something..." draw:)
				(= currentStatus DEAD)
			)


			;**	We only do this when Larry stands up, turning off the
			;**	bore's mouth, but temporarily!

			(shutUp
				(= cycles	0)
				(= seconds	0)
				(if (!= boreStatus BORE_READING)
					(aBore
						setLoop:		lHead
						setCel:		0
						stopUpd:
					)
				)
			)


			;**	Larry gave the bore the pamphlet, turning off his mouth.

			(shutBoreDown
				(= cycles	0)
				(= seconds	0)
				(= boreStatus BORE_READING)
				(SetRgTimer AIRPLANE_ARRIVAL 3 33)
				(aBore
					setLoop:		lBoreReading
					setCel:		0
					stopUpd:
				)
			)
		)
	)
)


(instance theSound of Sound
	(properties
	 	number:	mHenchette
		loop:		1
	)
)


