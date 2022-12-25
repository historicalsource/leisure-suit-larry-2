;**
;**	Logics for room 34 -- ship's pool
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 26, 1988
;**


(script#				34)

(public											;**	entry point to room
	rm34				0
)


(define	nearChaise 139 102 199 134)

;(define	vEgoSwimming				134)
;(define	vEgoDiving					135)
;(define	vEgoDrowningClothes		137)
;(define	vEgoDrowningSwimsuit		138)
;(define	vEgoShipPoolChaise		139)
	(define	lSitting			0)
	(define	lBurning			1)

;(define	vShipPoolHenchette1		311)
;(define	vShipPool					310)
	(define	lWake				0)
	(define	lDrain			1)
	(define	lGuys				2)
	(define	lHead2			3)				;**	Head of guy #2
	(define	lInnerTube		4)
	(define	lHead6			5)				;**	Head of guy #6
	(define	lHead7			6)				;**	Head of guy #7


(local
	work
	seenHenchette 
	aHead2
	aHead6
	aHead7
	aWake
	aDrain
	aMan

	goto95
	localBS										;**	bassSetter
	swamInSuit

	aHench
	henchInvited
	;**	henchOnScreen
)


(enum	1											;**	henchScript state values
	henchInit
	henchComing
	henchTurning
	henchAtEgo
	henchLeaving
	henchTurning2
	henchOffScreen
	henchAllGone
)

(enum												;**	detailScript state values
	detailInit
	pickAhead
	head2On
	head2Off
	head6On
	head6Off
	head7On
	head7Off
)

(enum												;**	rm34Script state values
	intro

	drowning
	deathOccurredSuddenly
	UBdying

	swimming

	diving
	diveDone

	climbingOut
	ahhhh

	gotoChaise
	gotoChaise2
	atChaise
	putItDown
	willyBurn
	UBdead

	leaveChaise
	outOfChaise
)


(instance rm34 of Room
	(properties
		picture:		34
		horizon:		5
	)

	(method (init)
		(Load VIEW vShipPool)
		(Load VIEW vEgoSwimming)
		(Load VIEW vEgoDiving)
		(Load VIEW vEgoDrowningClothes)
		(Load VIEW vEgoDrowningSwimsuit)
		(Load VIEW vEgoShipPoolChaise)

		(super init:)

		((View new:)							;**	Deck Chair1
			view: 			vShipPool
			loop: 			lGuys
			cel: 				6
			posn: 			11 137
			setPri: 			10
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	Deck Chair2
			view: 			vShipPool
			loop: 			lGuys
			cel: 				0
			posn: 			45 137
			setPri: 			10
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	Deck Chair3
			view: 			vShipPool
			loop: 			lGuys
			cel: 				1
			posn: 			80 137
			setPri: 			10
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	Deck Chair4
			view: 			vShipPool
			loop: 			lGuys
			cel: 				1
			posn: 			115 137
			setPri: 			10
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	Deck Chair5
			view: 			vShipPool
			loop: 			lGuys
			cel: 				2
			posn: 			177 137
			setPri: 			10
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	Deck Chair6
			view: 			vShipPool
			loop: 			lGuys
			cel: 				3
			posn: 			224 137
			setPri: 			10
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	Deck Chair7
			view: 			vShipPool
			loop: 			lGuys
			cel: 				4
			posn: 			263 137
			setPri: 			10
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	Deck Chair8
			view: 			vShipPool
			loop: 			lGuys
			cel: 				5
			posn: 			303 137
			setPri: 			10
			ignoreActors:
			addToPic:
		)

		((= aWake (Prop new:))
			view: 			vShipPool
			setLoop: 		lWake
			posn: 			128 54
			setPri: 			2
			setCycle:		Forward
			cycleSpeed:		4
			ignoreActors:
			isExtra:			TRUE
			init:
		)
		((= aDrain (Prop new:))
			view: 			vShipPool
			setLoop: 		lDrain
			posn: 			119 69
			setPri: 			3
			setCycle:		Forward
			cycleSpeed:		7
			ignoreActors:
			init:
		)

		((= aHead2 (Extra new:))			;**	connected to Deck Chair #2
			view: 			vShipPool
			setLoop: 		lHead2
			posn: 			49 112
			setPri: 			10
			cycleSpeed:		2
			minPause:		20
			maxPause:		30
			ignoreActors:
			isExtra:			TRUE
			init:
		)
		((= aHead6 (Extra new:))
			view: 			vShipPool
			setLoop: 		lHead6
			posn: 			228 107
			setPri: 			10
			cycleSpeed:		1
			minPause:		16
			maxPause:		40
			ignoreActors:
			init:
		)
		((= aHead7 (Extra new:))
			view: 			vShipPool
			setLoop: 		lHead7
			posn: 			259 118
			setPri: 			10
			cycleSpeed:		3
			minPause:		12
			maxPause:		31
			ignoreActors:
			init:
		)

		(self
			setRegions:		rgShip
			setScript: 		rm34Script
		)

		(if (== currentEgoView vEgoSwimsuit)
			(self setRegions: rgHenchette)
			(= currentHenchView vShipPoolHenchette)
			(Load VIEW currentHenchView)
			((= aHench (Actor new:))
				view:				currentHenchView
				posn:				155 234
				illegalBits:	cLtWhite
				observeControl:cLtBlack
				setCycle:		Walk
				init:
				setScript:		henchScript
			)
		)
		((= aMan (Actor new:))
			view: 			vShipPool
			setLoop:			lInnerTube
			;**	everything BUT ltBlue is illegal!
			illegalBits:	( & $FFFF ( ~ cLtBlue))
			posn:				194 86
			init:
			setMotion:		Wander
			moveSpeed:		5
			cycleSpeed:		5
			setStep:			1 1
		)

		(if (== prevRoomNum 134)			;**	Swim from underwater
			(= currentStatus SWIMMING)
			(ego
				view:				vEgoSwimming
				setLoop:			RELEASE
				setCycle:		Forward
				setPri:			RELEASE
				setMotion:		FALSE
				setStep:			3 2
				illegalBits:	$FFFF			;**	Stay on water.
				ignoreControl:	cLtBlue cLtBlack
				posn: 			157 84
				cycleSpeed:		1
				moveSpeed:		1
				baseSetter:		(= localBS BSetter)
				init:
			)
		else
			(NormalEgo facingNorth)
			(ego
				posn: 			157 184
				init:
			)
		)

		(User
			canControl:		TRUE
			canInput:		TRUE
		)
	)
)


(instance rm34Script of Script
	(method (doit)
		(super doit:)

		(if (and	(& (ego onControl:) cLtGreen)	;**	lower left corner of pool
					(== currentStatus NORMAL))
			(ego posn: 55 89)
			(self changeState: drowning)
		)

		(if (and	(& (ego onControl:) cLtRed)	;**	lower right corner of pool
					(== currentStatus NORMAL))
			(ego posn: 255 89)
			(self changeState: drowning)
		)

		(if (and	(& (ego onControl:) cLtBlack)
					(== currentStatus NORMAL))
			(self changeState: drowning)
		)

		(if (== SOUTH (ego edgeHit?))
			(if (== goto95 FALSE)
				(curRoom newRoom: 31)
			else
				(Print "You allow yourself to be seduced by the beautiful
					woman and quickly become close friends,
					then head for the ship's fantail where she parks her private
					helicopter.
					A short, romantic chopper ride across the South Pacific
					is followed by an intriguing look at her impressive
					garage door,
					hidden suspiciously by a mountain waterfall.")
				(Print "Later, that night..." draw:)
				(= currentStatus HEADING_FOR_DEATH_SCENE)
				(curRoom newRoom: 95)
			)
		)

		(if (and henchOnScreen
					henchInvited
					( > (ego y?) 181))
			(= henchInvited FALSE)
			(= goto95 TRUE)
			(curRoom south: 95)				;**	Too bad, Larry.
			(Print "\"Hey, baby,\" you cry, \"wait for me!!\"")
			(HandsOff)
			(ego
				setMotion:	MoveTo (ego x?) 234
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/man, people')
				(Print "They are all too busy sunbathing to talk to you.")
			)

			(if (Said '/lounge, chair')
				(if (!= currentStatus SITTING)
					(Print "There's only one deck chair left.")
				else
					(Print "The webbing has stretched considerably since you
						plopped down!")
				)
			)

			(if (Said '<under/lounge, chair')
				(SeeNothing)
			)

			(if (Said '/innertube')
				(Print "Just think how ugly he must look from underneath!")
			)

			(if (Said '/flower, tree')
				(Print "Your botanical investigation leads to nothing.")
			)

			(if (Said '[/pool, ship, room]')
				(cond
					((== currentStatus SWIMMING)
						(Print "Everything around you looks wet.")
					)
					((== currentStatus DROWNING)
						(Print "You're too busy watching your life pass before
							your eyes!")
					)
					((== currentStatus SITTING)
						(Print "You look straight up and see lots of blue sky.")
					)
					(else
						(Print "Doesn't that pool look refreshing?")
						(Print "There is one empty chaise lounge by the pool.")
					)
				)
			)
		)

		(if (or	(Said '(get<in), enter/water, pool')
					(Said 'swim'))
			(cond
				((== currentStatus SWIMMING)
					(Print "You are!")
				)
				((!= currentStatus DROWNING)
					(Print "Good idea, Larry.")
					(Print "Try stepping into the pool first!")
				)
				(else
					(Ok)
					(self changeState: swimming)
				)
			)
		)

		(if (or	(Said 'leave/pool, water')
					(Said 'get, climb<out')
					(Said 'use, climb/ladder'))
			(cond
				((!= currentStatus SWIMMING)
					(NotNow)
				)
				((not (& (ego onControl:) cLtBlack))
					(NotClose)
				)
				(else
					(Ok)
					(self changeState: climbingOut)
				)
			)
		)

		(if (or	(Said 'go, swim/underwater')
					(Said 'dive'))
			(cond
				((== currentStatus DROWNING)
					(Print "You can't. You're too busy drowning!")
				)
				((!= currentStatus SWIMMING)
					(Print "You try repeatedly to dive into the concrete pool
						deck with no success!")
				)
				((& (ego onControl:) cLtBlack)
					(Print "Not here! 
						You might hit the edge of the pool.")
				)
				(else
					(Ok)
					(self changeState: diving)
				)
			)
		)

		(if (or	(Said 'lie, go, sit[/bed, lounge, chair]')
					(Said 'sit[/down, lounge, chair]'))
			(cond
				((== currentStatus SITTING)
					(Print "This is too comfortable for you to change positions 
						now!")
				)
				((not (ego inRect: nearChaise))
					(NotClose)
				)
				((!= currentEgoView vEgoSwimsuit)
					(Print "You're not properly dressed for sunbathing;
						where's your swimming suit?")
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				(else
					(self changeState: gotoChaise)
				)
			)
		)

		(if (or	(Said 'rise, (stand[<up]), (get<up)')
					(Said 'leave[/seat]'))
			(cond
				((== currentStatus NORMAL)
					(Print "You are!")
				)
				((and	( <= (henchScript state?) henchAtEgo)
						(== henchOnScreen TRUE))
					(Print "Not now! There's someone coming.")
				)
				((!= currentStatus SITTING)
					(NotNow)
				)
				(else
					(self changeState: leaveChaise)
				)
			)
		)

		(if (or	(Said 'breathe')
					(Said 'take/breath'))
			(Ok)
		)

		(if (or	(Said 'roll, turn<over')
					(Said 'lie<on/front, stomach, face'))
			(Print "Nah. That's unnecessary.")
		)

		(if (Said '/girl>')
			(cond
				((Said 'talk/')
					(if henchOnScreen
						(Print (Format @string
							"\"Hi ya, beautiful,\" you tell her. %s" laffer))
					else
						(Print "To whom are you speaking? There are no girls here.")
					)
				)

				((Said 'look/')
					(if henchOnScreen
						(Print "You have always loved bathing suits with the 
							consistency of facial tissue!")
					else
						(Print "Where? There's nothing here but guys.")
					)
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(drowning
				(User canControl: FALSE)
				(= currentEgoView (ego view?))
				(= currentStatus DROWNING)
				(if (== currentEgoView vEgo)	;**	You're screwed!
					(= swamInSuit TRUE)
					(= work vEgoDrowningClothes)
					(User canInput:	FALSE)
				else
					(= work vEgoDrowningSwimsuit)
				)
				(ego
					view:				work
					illegalBits:	$FFFF		;**	Stay on water.
					ignoreControl:	cLtBlack cLtBlue
					cycleSpeed:		1
					moveSpeed:		1
					setStep:			2 2
					setCycle:		Forward
					setMotion:		Wander
				)
				(= seconds 5)
			)

			(deathOccurredSuddenly
				(= currentStatus NEAR_DEATH)
				(User canInput: 	FALSE)
				(ego
					cel:				0
					setCycle:		EndLoop self
					setMotion:		FALSE
				)
			)

			(UBdying
				(if (== swamInSuit TRUE)
					(Print "Next time, you might try swimming in something other
						than a thick polyester white leisure suit!")
				else
					(Print "Why jump in the water if you're not going to swim?")
					(Print "Surely you learned to swim
						when you were a little dorker?")
				)
				(= currentStatus DEAD)
				(ego hide:)
			)


			;**	He was drowning and typed "swim," so we do.

			(swimming
				(= cycles	0)
				(= seconds	0)
				(= currentStatus SWIMMING)
				(User canControl: TRUE)
				(ego
					view:				vEgoSwimming
					setStep:			3 2
					setLoop:			RELEASE
					setMotion:		FALSE
					baseSetter:		(= localBS BSetter)
				)
			)


			;**	He was swimming and typed "dive," so we do.

			(diving
				(= cycles	0)
				(= seconds	0)
				(HandsOff)
				(= currentStatus DIVING)
				(ego
					view:				vEgoDiving
					loop:				(if (or	(== (ego loop?) facingNorth)
													(== (ego loop?) facingWest))
											facingWest
										else
											facingEast
										)
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(diveDone
				(ego hide:)
				(curRoom newRoom: 134)
			)


			;**	He's done swimming, now "pop" him out of the water...

			(climbingOut
				(= cycles	0)
				(= seconds	0)
				(if (== wearingSunscreen TRUE)
					(= wearingSunscreen WASHED_OFF)
				)
				(ego
					view:				currentEgoView
					setLoop:			RELEASE
					setPri:			RELEASE
					setMotion:		FALSE
					setCycle:		Walk
					cycleSpeed:		0
					moveSpeed:		0
					ignoreActors:	FALSE
					illegalBits:	cLtBlack	;**	Stay off water.
					observeControl:cLtBlue
					;**	pop him off water and hopefully onto land?
					baseSetter:		0
				)
				(= cycles 12)
			)

			(ahhhh
				(Print "That was refreshing!")
				(NormalEgo)
			)

			;**	These path happens if he types "lie down" on the chaise
			;**	lounge closest to the exit.

			(gotoChaise
				(HandsOff)
				(if (not scoredChaise)
					(= scoredChaise TRUE)
					(theGame changeScore: 3)
				)
				(Ok)
				(if ( < (ego y?) 113)
					(ego setMotion: MoveTo 160 112 self)
				else
					(self changeState: gotoChaise2)
				)
			)

			(gotoChaise2
				(ego
					illegalBits:	0
					ignoreActors:
					setMotion: 		MoveTo 163 134 self
				)
			)

			(atChaise
				(ego
					view:				vEgoShipPoolChaise
					cycleSpeed:		1
					setMotion:		FALSE
					setPri:			ltGreen
					setLoop:			lSitting
					posn:				171 134
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(putItDown
				(= currentStatus SITTING)
				(Print "Ah! Seems like a lovely day to catch a few rays.")
				(= seconds 5)
			)

			(willyBurn
				(if (== wearingSunscreen TRUE)	;**	not applied to clothes
															;**	and not washed off.
					(User canInput:	TRUE)
					(Print "It's a good thing you applied that sunscreen!")

					(if (not seenHenchette)			;**	limit of 1 per visit
						(henchScript changeState: henchInit)
					)
				else
					(= currentStatus NEAR_DEATH)
					(ego
						setLoop:		lBurning
						setCel:		0
						cycleSpeed:	5
						setCycle:	EndLoop self
					)
				)
			)

			(UBdead
				(Print "Those tropical UVs can be deadly!" HIGH draw:)
				(= currentStatus DEAD)
			)


			;**	Here we wait for him to type "stand/up"

			(leaveChaise
				(= cycles	0)
				(= seconds	0)
				(Ok)
				(ego
					cycleSpeed:		1
					setCycle:		BegLoop self
				)
				(if (== henchInit (henchScript state?))
					(henchScript changeState: 255)	;**	kill running timer
				)
			)

			(outOfChaise
				(ego
					posn:				163 124
				)
				(NormalEgo)
			)
		)
	)
)


(instance henchScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(henchInit
				(= seenHenchette TRUE)
				(= seconds (Random 5 10))
			)

			(henchComing
				(if (!= currentStatus SITTING)
					(self changeState: henchInit)
				else
					(aHench
						setMotion: MoveTo 155 129 self
					)
					(= henchOnScreen TRUE)
					(Notify rgHenchette 1)
				)
			)

			(henchTurning
				(aHench setMotion: MoveTo 160 129 self)
			)

			(henchAtEgo
				(Print "\"Why, hello there, big fella,\"
					says the beautiful blond with the body like a brick shipyard,
					\"why are you hanging around here?
					I've been looking for someone just like you for a long time.
					Why don't you come back to my place,
					and you won't have to hang around all alone ever again!\"")
				(Print "(Geez, Larry.
					You've finally found yourself a live one!)" LOW)
				(= seconds 5)
			)

			(henchLeaving
				(Print "\"Come on, Samson,
					I just can't wait to get you all alone!\"")
				(aHench setMotion: MoveTo 155 129 self)
				(= henchInvited TRUE)
			)

			(henchTurning2
				(aHench setMotion: MoveTo 155 234 self)
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

(instance BSetter of Code
	(method (doit whom)
		(whom brBottom:	(+ (whom y?) 5))
		(whom brTop:		(- (whom y?) 3))
		(whom brLeft:		(- (whom x?) 10))
		(whom brRight:		(+ (whom x?) 10))
	)
)

