;**
;**	Logics for room 33 -- Mother's cabin
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 20, 1988
;**


(script#				33)

(public
	rm33				0
)


(define	NOT_NOW			"Not now! Mother would be displeased.")

(define	nearBed				172 100 222 118)
(define	nearHallDoor		127 117 333 222)


;(define	vMother'sCabin				305)
	(define	lPainting		0)
	(define	lWave				1)
	(define	lDrawer			2)
	(define	lCloset			3)
	(define	lMamaOnBed		4)
	(define	lPorthole		5)

;(define	vMother						306)
;(define	vMotherGrabbing			307)
	(define	lGrabbing			0)
	(define	lThrowingEgo		1)
	(define	lEgoFlying			2)
	(define	lEgoWrithing		3)

;(define	vMotherWhippingEgo		308)
	(define	lWalking				0)
	(define	lWhipping			1)

;(define	vBigMother					313)


(local
	mamaInRoom
	aPorthole
	aMama
	aCloset
	aDrawer
	drawerState
	closetState
	debugMsg
)


(enum												;**	drawer states
	drawerClosed
	drawerOpen
)

(enum												;**	closet states
	closetClosed
	closetOpen
)

(enum												;**	mamaInRoom values
	notThisTime
	hisFirstTime
	waitingOnBed
	caughtLarry
)

(enum												;**	rmScript state values
	noMama
	whatAplace

	firstTimeInRoom
	helloSonny
	she'sReady

	waitingForMama
	mamaChasingEgo
	mamaInPlace
	mamaGrabbing
	throwingEgo
	egoAirborne
	changeClothes
	mamaWhipping
	UBdead

	isMamaInRoom
	hiHoney
	showBigMama
	mamaHere
)

(enum												;**	whipScript state values
	whipInit
	egoInPlace
	hideEgo
	egoFlyingUp
	egoAtBed
	egoWrithing
	alterWrithing
)


(instance rm33 of Room
	(properties
		picture:		33
	)

	(method (init)
		(Load VIEW vEgo)
		(Load VIEW vMother)
		(Load VIEW vMotherGrabbing)
		(Load VIEW vMotherWhippingEgo)
		(Load VIEW vMother'sCabin)
		(Load VIEW vBigMother)

		(Load SCRIPT JUMP)

		(super init:)

		((View new:)
			view:				vMother'sCabin
			loop: 			lPorthole
			cel: 				0
			posn: 			172 78
			setPri: 			3
			ignoreActors:
			addToPic:
		)
		((View new:)
			view:				vMother'sCabin
			loop: 			lPainting
			cel: 				0
			posn: 			222 87
			setPri: 			5
			ignoreActors:
			addToPic:
		)

		(aBigMama
			view:				vBigMother
			posn:				273 1059
			setPri:			15
			setCycle:		Forward
			init:
		)

		((= aPorthole (Prop new:))
			view:				vMother'sCabin
			loop:				lWave
			setPri:			4
			posn:				172 75
			setCycle:		Forward
			cycleSpeed:		10
			ignoreActors:
			isExtra:			TRUE
			init:
		)

		(NormalEgo)
		(ego
			posn: 			109 113
			loop: 			facingEast
			init:
		)

		(self
			setRegions:		rgShip
			setScript: 		rm33Script
		)

		(if (== currentEgoView vEgo)
			(if (== 1 (++ timesInRm33))	;**	It's his first time!
				(= mamaInRoom hisFirstTime)
				(= debugMsg 1)
				((= aMama (Extra new:))
					view:				vMother'sCabin
					setLoop:			lMamaOnBed
					setPri: 			7
					posn: 			201 111
					cycleSpeed:		1
					minPause:		20
					maxPause:		50
					init:
				)
				(rm33Script changeState: firstTimeInRoom)
			else
				(if ( > 3 (Random 1 5))
					(= debugMsg 0)
				else
					(if ( > 3 (Random 1 5))
						(= debugMsg 2)
						((= aMama (Extra new:))
							view:				vMother'sCabin
							setLoop:			lMamaOnBed
							setPri: 			7
							posn: 			201 111
							cycleSpeed:		1
							minPause:		20
							maxPause:		50
							init:
						)
						(rm33Script changeState: isMamaInRoom)
					else
						(= debugMsg 3)
						((= aMama (Actor new:))
							view:				vMother
							loop:				facingNorth
							posn: 			154 126
							illegalBits:	0
							ignoreActors:
							setCycle:		Walk
							init:
							hide:
						)
						(rm33Script changeState: waitingForMama)
					)
				)
			)
		)

		((= aCloset (Door new:))
			view:				vMother'sCabin
			loop:				lCloset
			setPri:			6
			posn:				137 97
			doorCtrl:		cRed
			doorBlock:		0
			roomCtrl:		0
			init:
		)
		((= aDrawer (Door new:))
			view:				vMother'sCabin
			setLoop:			lDrawer
			setPri:			5
			posn:				161 89
			doorCtrl:		cMagenta
			doorBlock:		0
			roomCtrl:		0
			init:
		)

		(if debugging 
			(RedrawCast)
			(switch debugMsg
				(1 	(Print "His first Mama."			HIGH))
				(2 	(Print "Here's Mama." 				HIGH))
				(3 	(Print "Mama's coming... later!" HIGH))
				(else	(Print "Not this time." 			HIGH))
			)
		)
	)
)


(instance rm33Script of Script
	(method (doit)
		(super doit:)

		(if (& (ego onControl:) cGreen)
			(curRoom newRoom: 32)
		)

		(if (and
				(== state waitingForMama)
				(& (ego onControl: origin) cMagenta))
			(self changeState: mamaChasingEgo)
		)

		(if (and
				(== state mamaInPlace)
				(== (whipScript state?) egoInPlace))
			(self cue:)
		)

		(if (and	mamaInRoom
					(== currentStatus NORMAL)
					( > (ego x?) 170))
			(= currentStatus NEAR_DEATH)
			(HandsOff)
			(aMama
				view:				vMotherWhippingEgo
				posn:				173 105
			)
			(Print "\"Wow, sonny,\" shrieks big Mama,
				I thought you were never gonna come over here!
				Sit here on the bed while
				I slip into something more comfortable.
				I bet you'd like this little
				mink-lined leather special, wouldn't ya?
				Now you get comfortable in those chromium
				chains and handcuffs conveniently attached to the four
				corners of the bed.\"")
			(self changeState: mamaWhipping)
			(whipScript changeState: egoWrithing)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look<under/bed')
			(Print "You find plenty of dust.")
		)

		(if (Said 'look>')
			(if (Said '/(door<closet), closet')
				(cond
					((== closetState closetClosed)
						(Print "It's not open.")
					)
					(mamaInRoom
						(Print NOT_NOW)
					)
					(else
						(Print "You see lots of unusual garments,
							many made of leather, with fur linings.
							Mother must be a policewoman, as she also
							has handcuffs, whips, chains and other
							paraphernalia that you assume would only be useful
							in the field of law enforcement.")
					)
				)
			)

			(if (Said '/door')
				(Print "There is an open door here leading back to your room. 
					The door to the hall is locked.
					There is also a closet and a drawer in the nightstand, 
					either of which may be opened.")
			)

			(if (Said '/drawer, nightstand, table, dresser')
				(cond
					((not (& (ego onControl: origin) cMagenta))
						(NotClose)
					)
					((== drawerState drawerClosed)
						(Print "It's not open.")
					)
					(mamaInRoom
						(Print NOT_NOW)
					)
					(else
						(Ok)
						(if ((inventory at: iSewingKit) ownedBy: curRoomNum)
							(Print "You see lots of unmentionables,
								which will remain unmentioned,
								plus Mother's sewing kit.")
						else
							(Print "You see nothing of further interest to you.")
						)
					)
				)
			)

			(if (Said '/bed')
				(Print "It's small and hard.")
				(if ( > filthLevel DIRTY)
					(Print "(A condition not unknown to you!)" LOW)
				)
			)

			(if (Said '/woman')
				(cond
					((== (User canControl?) FALSE)
						(Print NOT_NOW)
					)
					((not mamaInRoom)
						(Print "Where?")
					)
					(else
						(Print "Must we? Oh, ok.")
						(aBigMama posn: 273 59)
						(Timer setReal: aBigMama 5)
						(HandsOff)
					)
				)
			)

			(if (Said '/paint, wall')
				(Print "The painting on the aft wall is
					from the Starving Sailors' School of art.")
			)

			(if (Said '[/cabin, room]')
				(Print "There is a door on the forward wall that leads back
					to your room.
					The door to the hall is locked.
					A large closet and a nightstand
					are against the starboard wall.")
				(if mamaInRoom
					(Print "And a large, ugly woman is looking back at you!")
				)
			)
		)

		(if (Said 'open>')
			(if (Said '/(door<closet), door, closet')
				(cond
					((& (ego onControl:) cBlue)
						(Print "The door to your room IS open!")
					)
					((ego inRect: nearHallDoor)
						(Print "The door to the hallway is locked.")
					)
					((not (& (ego onControl: origin) cRed))
						(NotClose)
					)
					((== closetState closetOpen)
						(ItIs)
					)
					(mamaInRoom
						(Print NOT_NOW)
					)
					(else
						(Ok)
						(= closetState closetOpen)
						(aCloset
							force:	TRUE
							open:
						)
					)
				)
			)

			(if (Said '/drawer, nightstand, table, dresser')
				(cond
					((not (& (ego onControl: origin) cMagenta))
						(NotClose)
					)
					((== drawerState drawerOpen)
						(ItIs)
					)
					(mamaInRoom
						(Print NOT_NOW)
					)
					(else
						(Ok)
						(= drawerState drawerOpen)
						(aDrawer
							force:	TRUE
							open:
						)
					)
				)
			)
		)

		(if (Said 'close>')
			(if (Said '/(door<closet), door, closet')
				(cond
					((& (ego onControl:) cBlue)
						(Print "You don't want to close yourself in here!")
					)
					((ego inRect: nearHallDoor)
						(Print "The door to the hallway IS closed.")
					)
					((not (& (ego onControl: origin) cRed))
						(NotClose)
					)
					((== closetState closetClosed)
						(ItIs)
					)
					(mamaInRoom
						(Print "It's too late now!")
					)
					(else
						(Ok)
						(= closetState closetClosed)
						(aCloset
							force:	TRUE
							close:
						)
					)
				)
			)

			(if (Said '/drawer, nightstand, table, dresser')
				(cond
					((not (& (ego onControl: origin) cMagenta))
						(NotClose)
					)
					((== drawerState drawerClosed)
						(ItIs)
					)
					(mamaInRoom
						(Print "It's too late now!")
					)
					(else
						(Ok)
						(= drawerState drawerClosed)
						(aDrawer
							force:	TRUE
							close:
						)
					)
				)
			)
		)

		(if (Said 'get/unmentionable, dust')
			(Print "You have better things to do. Try to find one.")
		)

		(if (Said 'get/kit')
			(cond
				((not (& (ego onControl: origin) cMagenta))
					(NotClose)
				)
				((not ((inventory at: iSewingKit) ownedBy: curRoomNum))
					(AlreadyTook)
				)
				((!= drawerState drawerOpen)
					(Print "Where?")
				)
				((or	mamaInRoom
						(!= currentStatus NORMAL))
					(NotNow)
				)
				(else
					(Ok)
					(ego get: iSewingKit)
					(theGame changeScore: 6)
				)
			)
		)

		(if (or	(Said '(get<on, in), lie, go/bed')
					(Said '(take<off), remove/clothes')
					(Said 'yes, play, (get<undress)')
					(Said 'get/undress')
					(Said 'touch, (make<love), hug, kiss/woman')
					(Said 'sit, (sit<with), (sleep<with), hump/bed, woman'))
			(cond
				((not mamaInRoom)
					(Print "You should not do that here.")
				)
				(else
					(Print "\"Come on over, Larry,\" says Mother.
						\"Sit here on the bed with me.\"")
				)
			)
		)

		(if (Said 'talk/woman')
			(cond
				((== (User canControl?) FALSE)
					(Print NOT_NOW)
				)
				((not mamaInRoom)
					(Print "Where?")
				)
				(else
					(Print (Format @string
						"\"Gosh, I'm real sorry I busted into your room
						like this,\" you tell the woman.
						%s" laffer))
					(Print "\"Yes, I know, big boy,\" says Mama Bimbo.
						\"I was hopin' you'd come next door!\"")
				)
			)
		)

		(if (Said 'get')
			(if mamaInRoom
				(Print NOT_NOW)
			else
				(Print "There's nothing in there that you know how to use!")
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(noMama)						;**	for descriptive room conditions


			;**	This takes care of the plot exposition, so user knows that
			;**	Bachelorette Barbara stiffed him, and sent Big Momma on the
			;**	cruise instead.

			(firstTimeInRoom
				(HandsOff)
				(= mamaInRoom hisFirstTime)
				(= seconds 3)
			)

			(helloSonny
				(Print "\"Well, well, well; what have we here?\"
					says the woman on the bed." HIGH)
				(Print "Uh, oh, Larry.
					You've been caught entering someone else's stateroom.")
				(Print "\"Why, I do believe it's that darling young man
					that my daughter won on TV!\" says the woman." HIGH)
				(Print "Good lord! She's Bachelorette Barbara's mother!")
				(Print "\"Poor little Barbie got to feeling poorly just as
					the cruise was about to start,
					so I decided to 'borrow' her ticket
					and come along instead!\"" HIGH)
				(Print "Your mind quickly envisions \"Paradise Lost.\"")
				(Print "\"I hope you don't mind:
					I convinced the purser that it was in his best interests
					to assign us to adjoining cabins!
					I'm sure I can make your voyage enjoyable!\"" HIGH)
				(= seconds 3)
			)

			(she'sReady
				(Print "\"I really want to get to 'know' you
					(if you get my drift).
					After I saw you win that Lottery show,
					I thought you were just about the cutest thing I'd
					ever seen!\"" HIGH)
				(Print "\"Except for my Mister, of course (may he rest in peace).
					He was a good man, and usually quite sturdy,
					but one day his heart just couldn't take it any more!\"" HIGH)
				(Print "You wonder why it is necessary for you to learn this.")
				(Print "\"Honey, any time you want to come over and see Momma,
					you just feel free to pop on through that doorway,
					ya hear?\"" HIGH)
				(Print "She attempts to give you a cute, girlish smile.")
				(Print "(She fails.)" LOW)
				(HandsOn)
			)


			;**	This path means Mama's gonna catch Larry in her room.

			(mamaChasingEgo
				(= seconds 0)
				(= currentStatus NEAR_DEATH)
				(= mamaInRoom caughtLarry)
				(aMama
					loop:				facingNorth
					show:
					setMotion:		MoveTo 108 113 self
				)
				(ego
					setScript:		whipScript
				)
				(Print "\"Say, look who's back!\" says the Mother.
					\"It's my little friend from next door!
					Came over for a little action, did we?
					Well, you've come to the right place, big boy!\"" BIG draw:)
				(Print "You've been caught in someone else's stateroom!")
				(Print "You decide to make a run for the door!")
			)

			(mamaInPlace
				(aMama
					loop:				facingEast
				)
				(Print "\"Yep, I've been lookin' for a man like you ever since
					my Mister had his big one.
					Whaddaya say we have a little fun?
					You do like havin' fun, doncha?\"" BIG draw:)
			)

			(mamaGrabbing
				(aMama
					view:				vMotherGrabbing
					setLoop:			lGrabbing
					posn: 			107 113
					setPri: 			12
					cel:				0
					cycleSpeed:		2
					setCycle:		EndLoop self
				)
			)

			(throwingEgo
				(aMama
					setLoop:			lThrowingEgo
					cel:				0
					posn: 			105 113
					setPri: 			7
					cycleSpeed:		1
					setCycle:		CycleTo 6 FORWARD self
				)
				(whipScript changeState: hideEgo)
				(Print "\"Hop over there on the bed, baby.\"" HIGH)
			)

			(egoAirborne
				(aMama
					setLoop:			lThrowingEgo
					setCel:			LastCel
				)
				(whipScript changeState: egoFlyingUp)
			)

			(changeClothes
				(aCloset
					setCel:			LastCel
				)
				(aMama
					view:				vMotherWhippingEgo
					setLoop:			lWalking
					setCycle:		Walk
					cycleSpeed:		0
					setMotion:		MoveTo 167 105 self
				)
				(Print "\"Now you just get comfortable in those chromium
					chains and handcuffs while I slip into something
					a little more comfortable myself,\"
					she says, opening her closet.
					\"Let's see, possibly a little something in mink-lined leather
					would be nice...\"" draw:)
			)

			(mamaWhipping
				(aMama
					setLoop:			lWhipping
					setCycle:		Forward
					cycleSpeed:		1
				)
				(= seconds 7)
			)

			(UBdead
				(Print "Mama proceeds to have her way with you repeatedly.")
				(Print "Too bad that \"her way\" is not \"your way!\"")
				(Print "(You can see why this broad's a widow!)" LOW)
				(= currentStatus DEAD)
			)


			;**	This path is if Mama is in her room when ego walks in.

			(isMamaInRoom
				(= mamaInRoom waitingOnBed)
				(= seconds 5)
			)

			(hiHoney
				(Print "\"Hi, sweetie!\" says Mama.")
				(Print "\"Back to play?\"")
			)
		)
	)
)


(instance whipScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(whipInit
				(HandsOff)
				(ego
					loop:				facingSouth
					ignoreActors:
					illegalBits:	0
					setMotion:		MoveTo 119 113 self
				)
			)

			(egoInPlace							;**	ego here for grabbing
				(ego
					setLoop:			facingWest
				)
			)

			(hideEgo
				(ego hide:)
			)

			(egoFlyingUp
				(ego
					view:				vMotherGrabbing
					illegalBits:	0
					ignoreActors:
					loop: 			lEgoFlying
					cel: 				0
					posn: 			117 86
					setPri: 			9
					setStep:			5 5
					show:
					setMotion:		JumpTo 194 94 self
				)
			)

			(egoAtBed
				(rm33Script changeState: changeClothes)
				(self changeState: egoWrithing)
			)

			(egoWrithing
				(ego
					view:				vMotherGrabbing
					illegalBits:	0
					ignoreActors:
					setLoop:			lEgoWrithing
					posn:				184 85
					setPri: 			5
					setCycle:		Forward
					setMotion:		FALSE
				)
				(= cycles (Random 2 11))
			)

			(alterWrithing
				(ego cycleSpeed:		(Random 0 7))
				(= cycles (Random 6 22))
				(= state egoWrithing)
			)
		)
	)
)

(instance aBigMama of Prop
	(method (cue)
		(Print "You find women sexy.")
		(Print "(But in this case, you make an exception!)"
			at: 66 155 width: 190)
		(self posn: 273 1059)
		(HandsOn)
	)
)

