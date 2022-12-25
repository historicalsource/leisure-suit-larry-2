;**
;**	Logics for room 82 -- Top of Volcano Fortress
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 26, 1988
;**


(script#				82)

(public
	rm82				0
)


(define	bottleOffsetX			7)
(define	bottleOffsetY			19)

(define	msgMoveCloser	"This area is not conducive to bombing.
									Try again somewhere else.")

;(define	vFortressExt				715)
	(define	lDoor					0)

;(define	vEgoDroppingTonic			184)
	(define	lTonicOut 			0)
	(define	lBagIntoBottle		1)
	(define	lLightingBag		2)
	(define	lBottleLit			3)
	(define	lBottleFalling		4)
	(define	lBottleFallingLit	5)

;(define	sBOOM							6)


(local
	bottleInHand
	bagInBottle
	bagBurning
	causedEruption
	safeX
	safeY
	aDoor
	aSteam1
	aSteam2
	aSteam3
	aBottle
)


(enum												;**	bottleScript state values
	bottleInit

	moveToSafe
	whipItOut
	bottleOut
)

(enum												;**	rmScript state values
	intro

	from181
	goDown
	egoIn

	startFall
	fallDone

	getBottleOut
	itIsOut

	stickBagIn
	bagIn

	lightBag
	bagLit
	bagBurnedDown

	dropBottle
	bottleAtHole
	bottleDropped
	rumbling
	doorIsOpen
	elevatorDoorOpen

	egoInShaft
	whatNoFloor
	nextRoom

	boomBoom

	bumDrop
)


(instance theSound of Sound
	(properties
		number:			sBOOM
		loop:				1
	)
)

(instance rm82 of Room
	(properties
		picture:		82
	)

	(method (init)
		(Load VIEW vFortressExt)
		(Load VIEW vEgoDroppingTonic)
		(Load VIEW vEgoFalling)
		(Load SOUND sFalling)
		(Load SOUND sBOOM)

		(super init:)

		(theSound			init:)

		((= aSteam3 (Prop new:))			;**	bottomSteam
			view:				vFortressExt
			loop: 			3
			posn: 			142 125
			setCycle:		Forward
			isExtra:			TRUE
			init:
		)
		((= aSteam2 (Prop new:))			;**	bottomSteam
			view:				vFortressExt
			loop: 			2
			posn: 			102 114
			setCycle:		Forward
			isExtra:			TRUE
			init:
		)
		((= aSteam1 (Prop new:))			;**	topSteam
			view:				vFortressExt
			loop: 			1
			posn: 			24 111
			setCycle:		Forward
			isExtra:			TRUE
			init:
		)
		((= aDoor (Prop new:))
			view:				vFortressExt
			setLoop:			lDoor
			posn:				75 147
			setPri:			ltCyan
			stopUpd:
			init:
		)

		(if debugging
			(ego
				get: iAirsickBag
				get: iHairRejuvenator
				get: iMatches
			)
		)

		(if (ego has: iHairRejuvenator)
			((= aBottle (Actor new:))
				view:				vEgoDroppingTonic
				ignoreActors:
				illegalBits:	0
				setLoop:			lBottleFalling
				setCycle:		Walk
				setStep:			3 9
				posn:				111 1111
				init:
				setScript:		bottleScript
			)
		)

		(ego
			posn: 		164 224
			setPri:		15
			init:
		)
		(self setScript: rm82Script)
	)
)


(instance rm82Script of Script
	(method (doit)
		(super doit:)

		(if (== state rumbling)
		 	(ShakeScreen 1 (Random 1 3))
		)

		(cond
			((and	(& (ego onControl:) cCyan)
					(== currentStatus NORMAL))
				(ego setPri: blue)
				(self changeState: startFall)
			)
			((and (& (ego onControl:) cGreen)
					(== currentStatus NORMAL))
				(self changeState: egoInShaft)
			)
			(else
				(if (and	debugging
							(== currentStatus NORMAL))
					(= safeX (ego x?))
					(= safeY (ego y?))
				)
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/crevice, hole, rock')
				(Print "As best you can tell from this angle,
					the crevice ends several hundred feet below you.")
				(Print "(Better save your game before walking closer!)" LOW)
			)

			(if (Said '/ice')
				(Print "It's not ice. It's a column of steam rising into the
					tropical sky.")
			)

			(if (Said '/door, elevator, box, button, dial, light')
				(if causedEruption
					(Print "The explosion has forced the door open.")
				else
					(Print "It's controlled from within
						and there is no way for you to get them to open it!")
				)
			)

			(if (Said '/ground')
				(Print "There is a major fissure very close to that elevator
					shaft.")
			)

			(if (Said '/sky')
				(Print "There's a 0% chance of rain in Sierra's adventure games.")
			)

			(if (Said '[/room, steam, volcano, mountain]')
				(Print "You have reached the summit of Nontoonyt volcano.
					An elevator incongruously awaits you.
					Steam rises from a volcanic crevice.
					The ground feels highly unstable.")
				(Print "(Fondly, you remember Los Angeles!)" LOW)
			)
		)

		(if (Said 'use/ash, sand')
			(Print "That won't help you now.")
		)

		(if (Said 'use, make/bomb')
			(Print "Good idea. The question is how.")
		)

		(if (Said 'push, knock, use, open/door, elevator, button')
			(Print "This elevator is controlled from within!")
		)

		(if (Said 'go, climb, use, enter/crevice, hole')
			(Print "Just walk closer.")
		)

		(if (and (ego has: iHairRejuvenator)
					(Said 'dump, pour/tonic'))
			(Print "You open the bottle of hair rejuvenator and pour
				it onto the ground by your feet.")
			(Print "(What a waste!)" LOW)
			(ego put: iHairRejuvenator LIMBO)
		)

		(if (Said 'drop, use, throw/tonic, bottle')
			(cond
				((not (ego has: iHairRejuvenator))
					(DontHave)
				)
				((and	(!= currentStatus NORMAL)
						(!= currentStatus MAKING_BOMB))
					(NotNow)
				)
				((not (& (ego onControl:) cLtMagenta))
					(Print msgMoveCloser)
				)
				(else
					(Ok)
					(self changeState: dropBottle)
				)
			)
		)

		(if (Said 'stick, stuff, push, put/tonic, bottle/bag')
			(cond
				((and (not (ego has: iHairRejuvenator))
						(not (ego has: iAirsickBag)))
					(DontHave)
				)
				((and	(!= currentStatus NORMAL)
						(!= currentStatus MAKING_BOMB))
					(NotNow)
				)
				(bagInBottle
					(Print "You can't.  The bag is securely stuffed into the
						neck of the bottle of hair rejuvenator.")
				)
				(else
					(Print "You slide the bottle of hair rejuvenator into your
						airsick bag.")
					(Print "Realizing this will accomplish nothing,
						you remove it again.")
				)
			)
		)
		(if (Said 'stick, push, stuff, put/bag/tonic, bottle')
			(cond
				((not (ego has: iHairRejuvenator))
					(DontHave)
				)
				((and	(!= currentStatus NORMAL)
						(!= currentStatus MAKING_BOMB))
					(NotNow)
				)
				((not (& (ego onControl:) cLtMagenta))
					(Print msgMoveCloser)
				)
				(bagInBottle
					(ItIs)
				)
				(else
					(Ok)
					(self changeState: stickBagIn)
				)
			)
		)

		(if (or	(Said 'put, drop/match/bottle')
					(Said 'use, light/match')
					(Said 'light, burn/bottle, tonic'))
			(cond
				((not (ego has: iMatches))
					(Print "You have no matches with which to light anything!")
				)
				((not (ego has: iHairRejuvenator))
					(Print "Do you have that?")
				)
				((and	(!= currentStatus NORMAL)
						(!= currentStatus MAKING_BOMB))
					(NotNow)
				)
				((!= bagInBottle TRUE)
					(self changeState: boomBoom)
				)
				(else
					(Ok)
					(self changeState: lightBag)
				)
			)
		)

		(if (Said 'light, burn/bag')
			(cond
				((not (ego has: iAirsickBag))
					(DontHave)
				)
				((not (ego has: iMatches))
					(Print "You have no matches with which to light anything!")
				)
				((and	(!= currentStatus NORMAL)
						(!= currentStatus MAKING_BOMB))
					(NotNow)
				)
				((!= bagInBottle TRUE)
					(Print "You strike a match and watch the airsick bag
						go up in flames. Whee!")
					(ego put: iMatches	LIMBO)
					(ego put: iAirsickBag LIMBO)
				)
				((not (& (ego onControl:) cLtMagenta))
					(Print msgMoveCloser)
				)
				(else
					(Ok)
					(self changeState: lightBag)
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(= cycles 5)
			)

			(from181
				(HandsOff)
				(ego
					illegalBits:0
					setLoop:		facingNorth
					setMotion:	MoveTo 164 180 self
				)
			)

			(goDown
				(ego
					setPri:		13
					setMotion:	MoveTo 164 188 self
				)
			)

			(egoIn
				(NormalEgo facingNorth)
				(ego observeControl: cYellow)
			)


			;**	here's what's left of ego climbing room.

			(startFall
				(HandsOff)
				(Print "Oh, shhhhhhhh..." HIGH dispose:)
				(= currentStatus FALLING)
				(theSound 
					number:		sFalling
					play:
				)
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
				(Print "You rapidly explore the interior of a volcanic crevice
					before discovering the pool of molten lava at its bottom.")
				(= currentStatus DEAD)
				(if (== debugging TRUE)
					(NormalEgo)
					(ego
						observeControl:cYellow
						posn: 			safeX safeY
					)
				)
			)


			;**	This makes Larry throw the bottle of hair rejuvenator
			;**	into the crevice.

			(getBottleOut
				(if bottleInHand
					(ItIs)
				else
					(bottleScript changeState: moveToSafe)
				)
			)

			(itIsOut
				(Print "You extract the bottle of hair rejuvenator from your
					pocket and remove the cap. Now what?" HIGH draw:)
				(User canInput:	TRUE)
				(if (not (ego has: iAirsickBag))
					(HandsOff)
					(Print "But, now you realize:
						there's something you've forgotten!")
					(Print "(But, what?!)" LOW)
					(= currentStatus DEAD)
				)
			)

			(stickBagIn
				(if (!= bottleInHand TRUE)
					(-- state)					;**	So we come back to here.
					(bottleScript changeState: moveToSafe)
				else
					(ego
						view:			vEgoDroppingTonic
						setLoop: 	lBagIntoBottle
						cel: 			0
						setCycle:	EndLoop self
					)
				)
			)

			(bagIn
				(theGame changeScore: 5)
				(Print "You insert the airsick bag into the neck of
					the bottle of hair rejuvenator." HIGH draw:)
				(if (not (ego has: iMatches))
					(HandsOff)
					(Print "But, now you realize:
						there's something you've forgotten!")
					(Print "(But, what?!)" LOW)
					(= currentStatus DEAD)
				else
					(= bagInBottle		TRUE)
					(User canInput:	TRUE)
				)
			)

			(lightBag
				(if (!= bottleInHand TRUE)
					(-- state)					;**	So we come back to here.
					(bottleScript changeState: moveToSafe)
				else
					(HandsOff)
					(ego
						view:				vEgoDroppingTonic
						setLoop: 		lLightingBag
						cel: 				0
						setCycle:		EndLoop self
					)
					(ego put: iMatches 	LIMBO)
					(ego put: iAirsickBag LIMBO)
				)
			)

			(bagLit
				(aBottle
					setPri:			(+ (ego priority?) 1)
					posn:				(+ (ego x?) bottleOffsetX)
										(- (ego y?) bottleOffsetY)
					setLoop:			lBottleLit
					setCycle:		Forward
				)
				(theGame changeScore: 5)
				(Print "You are now holding a bottle full of highly
					flammable liquid, with a burning wick made from an
					airsick bag." HIGH draw:)
				(Print "Yes, really!" LOW)
				(= bagBurning		TRUE)
				(User canInput:	TRUE)
				(= seconds 5)
			)

			(bagBurnedDown
				(HandsOff)
				(= currentStatus WICK_BURNED_DOWN)
				(curRoom newRoom: 152)
			)

			(dropBottle
				(if (!= bottleInHand TRUE)
					(-- state)					;**	So we come back to here.
					(bottleScript changeState: moveToSafe)
				else
					(if bagBurning
						(aBottle
							setLoop:		lBottleFallingLit
						)
						(Print "You dispatch your Larrytov cocktail on its merry
							mission with a hearty grunt!" HIGH)
					else
						(Print "You toss the bottle of hair rejuvenator into the
							deep crevice." HIGH)
					)
					(ego
						setMotion:		FALSE
						view:				vEgoDroppingTonic
						setLoop: 		lLightingBag
						setCel: 			LastCel
						put: iHairRejuvenator LIMBO
					)
					(aBottle
						posn:				(+ (ego x?) bottleOffsetX)
											(- (ego y?) bottleOffsetY)
						setPri:			0
						setMotion:		MoveTo 162 124 self
					)
					(= bagInBottle		FALSE)
					(= bottleInHand	FALSE)
				)
			)

			(bottleAtHole
				(if (not bagBurning)
					(= state (- bumDrop 1))
				)
				(= bagBurning			FALSE)
				(aBottle
					setMotion:			MoveTo 162 422 self
				)
			)

			(bottleDropped
				(NormalEgo facingEast)
				(aBottle				dispose:)
				(theGame changeScore: 10)
				(= seconds 3)
			)

			(rumbling
				(theSound 
					number:			sBOOM
					play:
				)
				(Print "You feel a deep rumbling as your homemade bomb 
					explodes, weakening an old lava tube deep within the earth.
					This could be extremely dangerous!" draw:)
				(= causedEruption TRUE)
				(aDoor
					setCycle:		EndLoop self
				)
			)

			(doorIsOpen
				(aDoor				stopUpd:)
				(= seconds 2)
			)

			(elevatorDoorOpen
				(Print "The force of the explosion has also caused
					the elevator door to open!" draw:)
			)


			;**	Door's open & Larry's inside, close door, new room...

			(egoInShaft
				(HandsOff)
				(= currentStatus CLOSING_CARTOON)
				(ego
					setLoop:		facingSouth
					setPri:		ltGreen
				)
				(= seconds 3)
			)

			(whatNoFloor
				(Print "As you start to push the \"Close Door\" button,
					you notice there is no \"Close Door\" button inside this
					elevator." HIGH )
				(Print "Say! There are no buttons at all inside this
					elevator." HIGH )
				(Print "In fact, there's not even a floor!" HIGH)
				(Print "Oh, shhhhhhhh..." HIGH dispose:)
				(theSound 
					number:		sFalling
					play:
				)
				(ego
					illegalBits:0
					ignoreActors:
					setStep:		1 15
					setMotion:	MoveTo (ego x?) ( + (ego y?) 200) self
				)
			)

			(nextRoom
				(cls)
				(curRoom newRoom: 83)
			)


			;**	Here are the various Larry screwups:

			(boomBoom
				(= currentStatus NO_WICK)
				(HandsOff)
				(Print "Bad idea.")
				(Print "You open the bottle of hair rejuvenator,
					light one of your matches,
					then drop it inside.")
				(Print "You wonder to yourself,
					\"Is that a disk drive I hear?\"")
				(curRoom newRoom: 152)
			)

			(bumDrop
				(Print "It finally reaches bottom and breaks harmlessly on
					the jagged rocks, making quite a mess
					but accomplishing nothing.")
				(Print "You just threw away the only hope you had of
					saving this island from the evil Doctor!")
				(= currentStatus DEAD)
			)
		)
	)
)


(instance bottleScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(moveToSafe
				(= currentStatus MAKING_BOMB)
				(HandsOff)
				(ego
					setMotion:		MoveTo 147 128 self
				)
			)

			(whipItOut
				(ego
					view:				vEgoDroppingTonic
					setLoop:			lTonicOut
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(bottleOut
				(= bottleInHand	TRUE)
				(rm82Script cue:)
			)
		)
	)
)

