;**
;**	Logics for room 79 -- Chasm of Doom
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 20, 1988
;**


(script#				79)

(public
	rm79				0
)


;(define	vEgoSwingingChasm			179)
	(define	lThrowingVine		0)
	(define	lSwingingAcross	1)
	(define	lVineShortening	2)

;(define	vChief						710)
	(define	lMouth				4)
		(define	mouthX	-1)
		(define	mouthY	-26)

;(define	vChasm						725)
	(define	lHelicopter		0)
	(define	lWaterTop		1)
	(define	lWaterBottom	2)
	(define	lLimb				3)


(local
	work
	aWater1
	aWater2
	aSteam
	aPlane
	aLimb
	restoreX
	restoreY
	aChief
	aMouth
)


(enum												;**	rmScript state values
	intro

	heresKen
	kenAtBrink
	goodBalance
	kenSpeaks
	thisSecret
	kenLeaves
	kenGone

	throwVine
	vineUp
	vineDown

	tarzanIt
	vineWrapped
	onOtherSide
	fallLimb

	vineGone
	walkOff
	walkDown
	changeRooms

	startFall
	fallDone
)


(instance theSound of Sound
)

(instance rm79 of Room
	(properties
		picture:		79
		horizon:		5
	)

	(method (init)
		(Load VIEW vChasm)
		(Load VIEW vEgoFalling)
		(Load VIEW vEgoSwingingChasm)
		(Load SOUND sFalling)

		(super init:)

		(theSound
			number:			sFalling
			init:
		)

		((= aWater1 (Prop new:))			;**	water1
			view:				vChasm
			setLoop: 		lWaterBottom
			posn: 			27 189
			setPri: 			gray
			cycleSpeed:		1
			setCycle:		Forward
			init:
		)
		((= aWater2 (Prop new:))			;**	water2
			view:				vChasm
			setLoop: 		lWaterTop
			posn: 			37 175
			setPri: 			gray
			setCycle:		Forward
			init:
		)
		((= aPlane (Airplane new:))		;**	really a chopper
			view:				vChasm
			posn:				444 44
			setPri:			magenta
			setStep:			2 1
			startX:			444
			startY:			44
			endX:				-44
			endY:				44
			init:
			setLoop:			lHelicopter
			setCycle:		Forward
		)

		((= aLimb (Actor new:))
			view:				vChasm
			setLoop:			lLimb
			setPri:			yellow
			setStep:			1 9
			posn:				202 60
			cycleSpeed:		2
			illegalBits:	0
			stopUpd:
			ignoreActors:
			init:
		)

		(self
			setScript: 		rm79Script
		)

		(NormalEgo)
		(if (== islandStatus initiation)
			(= islandStatus exploring)
			(= currentStatus INITIATION)
			(Load VIEW vChief)
			((= aMouth (Prop new:))
				view:				vChief
				ignoreActors:
				setLoop:			lMouth
				posn:				999 999
				setCycle:		Forward
				setPri:			14
				init:
			)
			((= aChief (Actor new:))
				view:				vChief
				setCycle:		Walk
				cycleSpeed:		1
				moveSpeed:		1
				posn:				210 189
				init:
			)
			(rm79Script changeState: heresKen)
		)
		(ego
			loop:				facingNorth
			posn: 			250 186
			init:
		)
	)
)


(instance rm79Script of Script
	(method (doit)
		(super doit:)

		(cond
			((and	(== SOUTH (ego edgeHit?))
					(== currentStatus NORMAL))
				(curRoom newRoom: 76)
			)
			((and	(& (ego onControl:) cBlue)
					(== currentStatus NORMAL))
				(ego setPri: yellow)
				(self changeState: startFall)
			)
			((and	(& (ego onControl:) cGreen)
					(== currentStatus NORMAL))
				(ego setPri: gray)
				(self changeState: startFall)
			)
			((and debugging
					(== currentStatus NORMAL))
				(= restoreX (ego x?))
				(= restoreY (ego y?))
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/tree')
				(Print "A dead tree is conveniently located just out of your
					reach on the other side of the chasm.")
				(Print "One of its branches hangs out over the chasm.")
			)

			(if (Said '/fern, leaves, plant')
				(Print "It's a member of the fern family
					and one of the first true trees.
					Its species is over 300-million years old.
					Since most of the jokes in this game come from the same era,
					it seems appropriate.")
			)

			(if (Said '/chopper')
				(Print "There's a lot of air traffic here, 
					for a remote, tropical island.")
			)

			(if (Said '[/room, chasm, mountain, path, valley, rock]')
				(Print "A terribly deep chasm extends around the tongue of
					rock upon which you are standing.
					If you didn't know better,
					you'd think a guy could get hurt around here!")
				(Print "In the distance you see the island's volcano,
					ringed with a glacier,
					not at the top as you'd expect,
					but near the bottom of some vertical cliffs.")
			)
		)

		(if (Said '/branch')
			(Print "You can't reach it from here, but it looks sturdy.")
		)

		(if (Said 'jump')
			(Print "There's no way you could make it across that chasm
				standing way back here!")
			(Print "(Heh, heh, heh.)" LOW)
		)

		(if (Said 'fill/bag/sand')
			(Print "A good idea, but unnecessary here.")
		)

		(if (or	(Said 'tie/vine/bag')
					(Said 'tie/bag/vine'))
			(Print "A good idea, but unnecessary here.")
		)

		(if (Said 'use, throw/vine')
			(cond
				((not (ego has: iVine))
					(DontHave)
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				(else
					(self changeState: throwVine)
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(intro)


			;**	An intro to the chasm by Ken

			(heresKen
				(HandsOff)
				(aChief
					setMotion:		MoveTo 127 134 self
				)
			)

			(kenAtBrink
				(aChief
					loop:				facingEast
				)
				(= seconds 3)
			)

			(goodBalance
				(Print "(The old boy's got some sense of balance!)" LOW)
				(aMouth
					posn:				(+ (aChief x?) mouthX)
										(+ (aChief y?) mouthY)
				)
				(= seconds 3)
			)

			(kenSpeaks
				(Print "\"Here we are, son,\" says Chief Kenawauwau,
					\"this is the secret of my people;
					the way to our sacred volcano;
					right across this chasm!\"")
				(= seconds 3)
			)

			(thisSecret
				(Print "(Some secret?! Any fool could see that!)" LOW)
				(Print "Kenawauwau continues, 
					\"Of course, any fool could see that;
					what's important is:
					you must cross this chasm,
					climb the recent icy glacier,
					walk the treacherous paths,
					and penetrate the impenetrable fortress of
					the evil Dr. Nonookee!\"")
				(Print "\"By the way, did I mention that my daughter is
					a virgin?\"")
				(theGame changeScore: 25)
				(= seconds 3)
			)

			(kenLeaves
				(Print "\"Ok, catch you later!\" he says,
					\"I must go lead the village's afternoon aerobics class!\"")
				(Print (Format @string
					"\"%s\"" tritePhrase))
				(aMouth				dispose:)
				(aChief
					setMotion:		MoveTo 270 234 self
				)
			)

			(kenGone
				(aChief				dispose:)
				(NormalEgo)
				(++ islandStatus)
			)


			;**	Throw the vine, then check to see if we hit anything.

			(throwVine
				(HandsOff)
				(ego
					view:				vEgoSwingingChasm
					loop:				lThrowingVine
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(vineUp
				(if (& (ego onControl: origin) cCyan)		;**	IT'S A HIT!
					(self changeState: tarzanIt)
				else
					(ego
						setCycle:	BegLoop self				;**	he missed.
					)
				)
			)

			(vineDown
				(NormalEgo facingNorth)
			)


			;**	We only get here if he hit the tree with his vine.

			(tarzanIt
				(= currentStatus WALKING)	;**	Should have a "swinging" state...
				(ego
					loop:				lSwingingAcross
					cel:				0
					posn:				174 56
					setPri: 			ltGreen
					illegalBits:	0
					ignoreControl:	cBlue cGreen
					cycleSpeed:		1
					put: 				iVine LIMBO
				)
				(aLimb
					setCycle:		EndLoop self	;**	break limb
				)
			)

			(vineWrapped
				(ego
					setCycle:		EndLoop self
				)
			)

			(onOtherSide
				(Print "You made it!" HIGH draw:)
				(theGame changeScore: 11)
				(= seconds 3)
			)

			(fallLimb
				(ego
					setLoop:			lVineShortening
					cel:				0
					posn: 			171 106
					setPri: 			brown
					setCycle:		EndLoop self
				)
				(aLimb
					setPri: 			gray
					setMotion:		MoveTo (aLimb x?) 234 self
				)
			)

			(vineGone
				(ego
					view:				vEgo
					setLoop:			facingSouth
					cycleSpeed:		0
					setCycle:		Walk
				)
			)

			(walkOff
				(Print "You set forth on your mission to explore the unknown
					mountain before you, knowing full well that you will
					never be able to return this way again!" draw:)
				(Print "What lies before you?")
				(Print "Will you be able to climb the icy glacier?")
				(Print "Will you find the evil Doctor's mountaintop fortress?")
				(Print "Will you destroy the evil Dr. Nonookee?")
				(Print "Will you return to your beloved Kalalua?")
				(Print "Will you find love?")
				(Print "Will you find happiness?")
				(Print "(Will you stop asking all these
					questions and get on with the game?!)" LOW)
				(ego
					setLoop:			facingNorth
					setMotion:		MoveTo 171 104 self
				)
			)

			(walkDown
				(ego
					setPri:			green
					setMotion:		MoveTo 171 140 self
				)
			)

			(changeRooms
				(curRoom newRoom: 80)
			)


			;**	here's what's left of ego climbing room.

			(startFall
				(HandsOff)
				(Print "Oh, shhhhhhhh..." HIGH dispose:)
				(= currentStatus FALLING)
				(theSound play:)
				(ego
					view:			vEgoFalling
					illegalBits:0
					ignoreActors:
					posn:			(ego x?) (- (ego y?) 15)
					cel:			0
					setStep:		1 15
					setCycle:	Forward
					setMotion:	MoveTo (ego x?) ( + (ego y?) 200) self
				)
			)

			(fallDone
				(cls)
				(= currentStatus NEAR_DEATH)
				(Print "Treacherous little first step, isn't it?")
				(= currentStatus DEAD)
				(if (== debugging TRUE)
					(NormalEgo)
					(ego
						posn: 		restoreX restoreY
					)
					(self changeState: vineDown)	;**	back to normal
				)
			)
		)
	)
)

