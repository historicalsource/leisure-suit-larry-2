;**
;**	Logics for room 40 -- resort landscape maze
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 26, 1988
;**


(script#				40)

(public
	rm40				0
)


(define	nearFlower	128 150 160 167)
(define	msgFlower	"There is one beautiful flower growing directly under
							that parrot sitting in the palm tree.")

;(define	vEgoLandscape				145)
;	(define	East,West,North,South 0,1,2,3)
	(define	lUpTree			4)
	(define	lGopher			5)
	(define	lMonkey			6)
	(define	lThruBush		7)

;(define	vEgoLandscape				145)
;(define	vEgoLandscapeBlond		146)
;(define	vEgoLandscapeBikini		147)
;(define	vEgoLandscapeStuffed		157)	;**	same as cliffs #2
;(define	vEgoBlond					149)
;(define	vEgoBikini					150)
;(define	vEgoBikiniStuffed			151)

;(define	vPeacock						412)
	(define	lSpreading		0)
	(define	lWalking			1)

;(define	vKGBjaws						414)

;(define	vLandscapeMaze				415)
	(define	lPottedPlant	0)
	(define	lFlower			1)

;(define	vJungleBirds				700)
	(define	lWhite			0)
	(define	lRed				1)

;(define	sBirds						8)


(local
	work
	lclEgoView
	flowerHere
	aHench
	aFlower
	aPottedPlant
	aBird1
	aBird2
)


(enum												;**	rmScript state values
	startBit1
	walkToBit1
	face1N
	face1S
	face1E
	return1
	bit1Done

	startBit2
	walkToBit2
	face2N
	face2S
	face2E
	return2
	bit2Done

	startBit3
	walkToBit3
	face3N
	face3S
	face3E
	return3
	bit3Done

	startBit4
	bit4Done

	startTreeBit
	tree1
	tree2
	treeDone

	startBushBit
	bushParted
	bushClosing
	bushDone

	startPlantBit
	plantRaised
	lowerLarry
	lowerPlant
	plantDone

	startFallBit
	fallDelay
	fallSlips
	fallFromTree
	fallBitDone

	startPeacock
	movePeacock
	peacockSpreadsIt
	peacockPause
	putLarryBehindPeacock
	peacockLeaves
	egoGoesAway
	peacockGone
	peacockDone

	startShark
	larryTurns
	sharkTurns
	larryRuns
	larryGone
	sharkTake1
	sharkTake2
	sharkDone

	startBorderBit
	borderParted
	reverseBorder
	borderDone

	shortCut
	mazeDone
)


(instance theSound of Sound
	(properties
	 	number:		sBirds						
		loop:			-1
	)
)


(instance rm40 of Room
	(properties
		picture:		40
		horizon:		5
	)

	(method (init)
		(cond
			((== currentEgoView vEgoBlond)
				(= lclEgoView vEgoLandscapeBlond)
			)
			((== currentEgoView vEgoBikini)
				(= lclEgoView vEgoLandscapeBikini)
			)
			((== currentEgoView vEgoBikiniStuffed)
				(= lclEgoView vEgoLandscapeStuffed)
			)
			(else
				(= lclEgoView vEgoLandscape)
			)
		)

		(Load VIEW lclEgoView)
		(Load VIEW vLandscapeMaze)
		(Load VIEW vJungleBirds)
		(Load VIEW vKGBjaws)
		(Load VIEW vPeacock)

		(Load SOUND mJaws)
		(Load SOUND mHench)
		(Load SOUND sBirds)

		(super init:)

		(theSound		play:)

		(++ timesInRm40)

		(if forceAtest
				(= timesInRm40 257)
		)

		(= flowerHere ((inventory at: iFlower) ownedBy: curRoomNum))
		((= aFlower (View new:))
			view:				vLandscapeMaze
			ignoreActors:
			setLoop:			lFlower
			setCel:			flowerHere
			posn:				139 157
			stopUpd:
			init:
		)

		((= aPottedPlant (Actor new:))
			view:				vLandscapeMaze
			ignoreActors:
			setLoop:			lPottedPlant
			setCel:			0
			setStep:			1 1
			posn:				103 161
			setPri:			12
			init:
		)

		((= aBird1 (Extra new:))
			view:				vJungleBirds
			ignoreActors:
			setLoop:			lWhite
			posn:				279 59
			setPri:			15
			minPause:		22
			maxPause:		44
			minCycles:		11
			maxCycles:		22
			init:
		)
		((= aBird2 (Extra new:))
			view:				vJungleBirds
			ignoreActors:
			setLoop:			lRed
			posn:				140 50
			setPri:			15
			minPause:		22
			maxPause:		55
			minCycles:		11
			maxCycles:		22
			init:
		)

		(if (== lclEgoView vEgoLandscape)
			((= aHench (Actor new:))
				view:				vPeacock
				ignoreActors:
				illegalBits:	0
				setLoop:			lWalking
				setCycle:		Walk
				posn:				222 1222
				init:
			)
		)

		(NormalEgo facingEast)
		(ego
			ignoreActors:
			illegalBits:	0
			posn:				44 189
			init:
		)
		(User
			canControl:		FALSE
		)
		(= currentStatus LANDSCAPE)
		(self
			setRegions:		rgResort
			setScript: 		rm40Script
		)
	)
)


(instance rm40Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/ground')
				(Print "The grounds of the resort are covered with
					elaborate, hand-carved and inlaid patterns,
					none of which show up on the cheap graphics board
					and monitor that you're using!")
			)

			(if (and flowerHere
						(Said '/flower'))
				(Print msgFlower)
			)

			(if (Said '<under/bush')
				(Print "You discover Jim Walls testing \"Police Quest II!\"
					Jim! You were supposed to be on vacation!")
			)

			(if (Said '/peacock')
				(Print "The peacock is beautiful, but of no use to you.")
			)

			(if (Said '/parrot')
				(Print "You hesitate to look up, but grab a quick peek.
					They are beautiful birds, but unfortunately
					they cannot tell you how to escape this place!")
			)

			(if (Said '/shark')
				(if (and ( >= state startShark)
							( <= state sharkDone))
					(Print "LAND SHARK!!")
				else
					(Print "Where?")
				)
			)

			(if (Said '[/room, tree]')
				(Print "This resort has lovely landscaping,
					but it's so dense that you cannot find anything.
					You wander about aimlessly,
					searching for a break in the vegetation
					to return you to civilization!")
			)
		)

		(if (and ( >= state startShark)
					( <= state sharkDone)
					(Said 'talk/man'))
			(Print "He's not interested in talk.")
		)

		(if (Said 'climb/tree')
			(Print "Sit back; relax; enjoy.")
		)

		(if (Said 'get/coconuts')
			(Print "You have all the nuts you need for this game.")
		)

		(if (Said 'get/feather, shark, peacock, bird')
			(Print "That won't help you.")
		)

		(if (Said 'pick, cut, get/flower')
			(cond
				((!= currentStatus LANDSCAPE)
					(NotNow)
				)
				((not flowerHere)
					(AlreadyTook)
				)
				((not (ego inRect: nearFlower))
					(NotClose)
				)
				(else
					(ego get: iFlower)
					(= flowerHere FALSE)
					(theGame changeScore: 3)
					(aFlower
						setCel:		flowerHere
						stopUpd:
					)
					(Print "You reach over and break off the beautiful flower
						at its base.
						\"Aw,\" you rationalize,
						\"they'll never miss just one flower!\"" HIGH draw:)
				)
			)
		)
	)


	(method (changeState newState)
;**			(if ( > newState startShark)
;**				(Print (Format @string "newState = %d" newState))
;**			)

		(switch (= state newState)

			(startBit1
				(ego
					posn: 		16 171
					setMotion:	MoveTo 91 171 self
				)
			)

			(walkToBit1						;**	pause, turn all ways, pause
				(if (== timesInRm40 257)
					(= state shortCut)
				)
				(= cycles 15)
			)

			(face1N
				(ego
					setLoop:		facingNorth
				)
				(= cycles 15)
			)

			(face1S
				(ego
					setLoop:		facingSouth
				)
				(= cycles 15)
			)

			(face1E
				(ego
					setLoop:		facingEast
				)
				(= cycles 15)
			)

			(return1
				(ego
					setLoop: 	RELEASE
					setMotion:	MoveTo 19 175 self
				)
			)

			(bit1Done
				(= cycles 30)
			)

			(startBit2
				(ego
					posn: 		249 160
					setMotion:	MoveTo 157 160 self
				)
			)

			(walkToBit2
				(= cycles 15)
			)

			(face2N
				(ego
					setLoop:		facingNorth
				)
				(= cycles 15)
			)

			(face2S
				(ego
					setLoop:		facingSouth
				)
				(= cycles 15)
			)

			(face2E
				(ego
					setLoop:		facingEast
				)
				(= cycles 15)
			)

			(return2
				(ego
					setLoop: 	RELEASE
					setMotion:	MoveTo 324 160 self
				)
			)

			(bit2Done
				(= cycles 30)
				(if ( > timesInRm40 8)
					(= state shortCut)
				)
			)

			(startBit3
				(ego
					view:			lclEgoView
					setLoop: 	RELEASE
					setPri: 		RELEASE
					setStep: 	2 1
					posn: 		228 136
					setMotion:	MoveTo 180 136 self
				)
			)

			(walkToBit3
				(= cycles 15)
			)

			(face3N
				(ego
					setLoop:		facingNorth
				)
				(= cycles 15)
			)

			(face3S
				(ego
					setLoop:		facingSouth
				)
				(= cycles 15)
			)

			(face3E
				(ego
					setLoop:		facingEast
				)
				(= cycles 15)
			)

			(return3
				(ego
					setLoop: 	RELEASE
					setMotion:	MoveTo 318 136 self
				)
			)

			(bit3Done
				(= cycles 30)
			)

			(startBit4
				(ego
					posn: 		119 143
					setMotion:	MoveTo 2 143 self
				)
			)

			(bit4Done
				(= cycles 30)
				(if (or	(!= lclEgoView vEgoLandscape)
							( > timesInRm40 7))
					(= state shortCut)
				)
			)

			(startTreeBit
				(ego
					setLoop: 	lUpTree
					setPri: 		12
					posn: 		117 26
					setCel:		LastCel
					setCycle:	Reverse
					setMotion:	MoveTo 117 63 self
				)
			)

			(tree1
				(ego setCycle:	Walk)
				(= cycles 30)
			)

			(tree2
				(ego
					setCycle:	Forward
					setMotion:	MoveTo 117 26 self
				)
			)

			(treeDone
				(= cycles 50)
				(if ( > timesInRm40 6)
					(= state shortCut)
				)
			)

			(startBushBit
				(ego
					setLoop: 	lThruBush
					cel: 			0
					posn: 		243 141
					setPri: 		15
					setCycle:	EndLoop self
				)
			)

			(bushParted
				(= cycles 30)
			)

			(bushClosing
				(ego
					setCycle:	BegLoop self
				)
			)

			(bushDone
				(ego				hide:)
				(= cycles 30)
				(if ( > timesInRm40 5)
					(= state shortCut)
				)
			)

			(startPlantBit
				(aPottedPlant
					setMotion:	MoveTo 103 151
				)
				(ego
					setLoop: 	lGopher
					cel: 			0
					posn: 		102 164
					setPri: 		14
					show:
					cycleSpeed:	0
					setCycle:	EndLoop self
				)
			)

			(plantRaised
				(= cycles 30)
			)

			(lowerLarry
				(ego
					setCycle:	CycleTo 5 REVERSE self
				)
			)

			(lowerPlant
				(aPottedPlant
					setMotion:	MoveTo 103 161
				)
				(ego
					setCycle:	BegLoop self
				)
			)

			(plantDone
				(ego				hide:)
				(= cycles 30)
				(if ( > timesInRm40 4)
					(= state shortCut)
				)
			)

			(startFallBit
				(ego
					setLoop: 	lMonkey
					cel: 			0
					posn: 		218 89
					setPri: 		10
					show:
					setCycle:	CycleTo 5 FORWARD self
				)
			)

			(fallDelay
				(= cycles 7)
			)

			(fallSlips
				(ego
					setCycle:	EndLoop self
				)
			)

			(fallFromTree
				(ego
					setCel:
					setStep:		1 6
					setMotion:	MoveTo 218 143 self
				)
			)

			(fallBitDone
				(= cycles 30)
				(if ( > timesInRm40 3)
					(= state shortCut)
				)
			)

			(startPeacock
				(aHench
					view: 		vPeacock
					setLoop: 	lWalking
					setStep:		1 1
					posn: 		177 136
					setCycle:	Walk
					setMotion:	MoveTo 162 138 self
				)
			)

			(movePeacock
				(aHench
					setMotion:	MoveTo 162 155 self
				)
			)

			(peacockSpreadsIt
				(aHench
					setLoop:		lSpreading
					cel:			0
					setCycle:	EndLoop self
				)
			)

			(peacockPause
				(= cycles 30)
			)

			(putLarryBehindPeacock
				(ego
					loop: 		facingSouth
					setLoop:		RELEASE
					setPri:		RELEASE
					setCycle:	Walk
					setStep:		2 1
					posn: 		163 152
				)
				(aHench
					setCycle:	BegLoop self
				)
			)

			(peacockLeaves
				(aHench
					setLoop:		lWalking
					setCycle:	Walk
					setMotion:	MoveTo 162 196 self
				)
			)

			(egoGoesAway
				(ego
					setMotion:	MoveTo 163 147 self
				)
			)

			(peacockGone
				(ego
					setMotion:	MoveTo 0 147 self
				)
				(aHench
					hide:
				)
			)

			(peacockDone
				(= cycles 30)
				(if ( > timesInRm40 2)
					(= state shortCut)
				)
			)

			(startShark
				(ego
					view:			lclEgoView
					setLoop: 	RELEASE
					setPri:		RELEASE
					posn: 		321 147
					setCycle:	Walk
					setMotion:	MoveTo 214 147 self
				)
			)

			(larryTurns
				(ego
					setLoop:		facingEast
					setMotion:	MoveTo 170 147 self
				)
			)

			(sharkTurns
				(aHench
					view: 		vKGBjaws
					setLoop: 	RELEASE
					setPri:		RELEASE
					setCycle:	Walk
					setStep:		2 1
					posn: 		249 153
					setMotion:	MoveTo 214 153 self
					show:
				)
				(theSound		dispose:)
				(theSound 
		 			number:		mJaws
					loop:			1
					play:
				)
			)

			(larryRuns
				(ego
					setLoop:		RELEASE
					setMotion:	MoveTo 117 147 self
				)
			)

			(larryGone
				(aHench
					setMotion:	MoveTo 170 153 self
				)
			)

			(sharkTake1
				(aHench
					loop:			facingEast
				)
				(theSound		dispose:)
				(theSound 
					number: 		mHench 
					loop: 		1 
					play:	
				)
				(= cycles 15)
			)

			(sharkTake2
				(aHench
					loop:			facingWest
				)
				(= cycles 15)
			)

			(sharkDone
				(aHench
					setMotion:	MoveTo 333 153 self
				)
				(if ( > timesInRm40 1)
					(= state shortCut)
				)
			)

			(startBorderBit
				(theSound		dispose:)
				(theSound 
					number:		sBirds
					loop:			-1
					play:
				)
				(ego
					loop: 		lThruBush
					setLoop: 	RELEASE
					cel: 			0
					posn: 		264 17
					setPri: 		15
					cycleSpeed:	1
					setCycle:	EndLoop self
				)
			)

			(borderParted
				(= cycles 30)
			)

			(reverseBorder
				(ego
					setCycle:	BegLoop self
				)
			)

			(borderDone
				(ego
					cycleSpeed:	1
					hide:
				)
				(= cycles 30)
			)

			(shortCut
				(= cycles 30)
				(= seconds 0)
			)

			(mazeDone
				(if (== rmAfter40 0)
					(= rmAfter40 43)
				else
					(if ( > (++ rmAfter40) 45)
						(= rmAfter40 42)
					)
				)

;**					(if ( > timesInRm40 255) (= rmAfter40 42))

				(if (!= lclEgoView vEgoLandscape)
					(Print "Say, you're getting faster at this!")
				else
					(Print "At last you find an exit.")
					(Print "(But where does it lead?)" LOW)
					(switch rmAfter40
						(42
							(Print "Why, here you are back at the beach!")
						)
						(43
							(Print "Why, you've found a lovely restaurant!")
						)
						(44
							(Print "Why, you've found an empty guest room!")
						)
						(45
							(Print "Why, you've found the resort's barber shop!")
						)
					)
				)
				(curRoom newRoom: rmAfter40)
			)
		)
	)
)
