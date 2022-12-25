;**
;**	Logics for room 78 -- Witch Doctor's hut
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 17, 1988
;**


(script#				78)

(public
	rm78				0
)



(define	nearWoodchopper 0	0	91 120)
(define	nearDoctor		0	0 191 140)
(define	nearLog			34 100	43 107)

;(define	vCameraman					212)
	(define	lKneel			2)				;**	preceded by east & west loops

	(define	cameramanY		180)
	(define	girlX				180)
	(define	chiefX			120)

;(define	vKalalau						704)
;(define	vKalalauKissing			706)
	(define	lTheKiss				0)
	(define	lTheKissWest		1)

;(define	vComputerBearer			707)
;(define	vChief						710)
	(define	lMouth				4)
		(define	mouthX	-1)
		(define	mouthY	-25)

;(define	vWitchDoctor				711)
;(define	vWitchDoctorTreating		713)
;(define	vTribalInitiation			714)
	(define	lDrummerEast		0)
	(define	lDrummerWest		1)
	(define	lFamiliesEast		2)
	(define	lFamiliesWest		3)

;(define	vWoodchopper				716)
;(define	vWedding						822)
	(define	lStartWhirling		0)
	(define	lWhirling			1)
	(define	lStanding			2)
	(define	lHearts				3)
	(define	lCupid1				4)
	(define	lCupid2				5)
	(define	lCake					6)

;(define	vPhotographer				823)
	(define	lHulaHoop			0)
	(define	lMaleDancer			1)
	(define	lPhotographer		2)
	(define	lRaiseCamera		3)
	(define	lFlash				4)
	(define	lAddToPix			5)
		(define	cHulaHoop	0)

;(define	vHenchetteStrippers		833)

;(define	mWedding						111)


(local
	aWoodchopper
	pauseCycles

	aHulaHooper
	aDancer
	aPhotographer
	aDrummer
	aPedaler
	aDoctor
	aChief
	aMouth
	aKalalau
	aCameraman
	aCupidWest
	aCupidEast
	aHeart
	aFlash
)


(enum												;**	woodScript state values
	woodInit
	woodStart
	woodDone
)

(enum												;**	minicamScript state values
	minicamInit
	kneel
	stopUpdate

	toGirl
	walkToGirl

	toChief
	walktoChief
)

(enum												;**	rmScript state values
	intro

	hereComesTheDork
	startHula
	goodTurnout
	hulaDone
	dancerDone
	egoDone
	drummer
	startGirl

	waitForSlowestOne

	kInPlace
	lInPlace
	meDo
	theRing
	otherGame
	wellOK
	twirlPlease
	twirling

	speedUp
	goFastAwhile
	slowDown

	pieInFace
	doCake
	cakeDone
	nowMoonwalk
	necessary
	walkWest
	walkEast
	kissHer
	theKiss
	holdIt
	breakLips
	kissDone
	inLaws

	photogInPlace
	cameraUp
	growAheart
	addWings
	flasher
	removeFlash
	cameraDown
	shrinkHeart
	removeHeart

	comeInsideBoy
	besideEgo
	jumpStartEgo
	toTheHut

	removeToe
	UBdying
)


(instance rm78 of Room
	(properties
		picture:		78
		horizon:		5
		west:			77
	)

	(method (init)
		(if (== islandStatus wedding)
			(self style: DISSOLVE)
		)

		(super init:)

		(NormalEgo)
		(ego
			posn: 			9 134
			init:
		)

		(self setScript: 	rm78Script)

		(if forceAtest
			(= islandStatus wedding)
		)

		(if (!= islandStatus wedding)
			(self setRegions: rgIsland)

			(Load VIEW vWoodchopper)
			((= aWoodchopper (Prop new:))
				view:				vWoodchopper
				posn:				55 103
				cycleSpeed:		1
				init:
				setScript:		woodScript
			)
		else
			(HandsOff)
			(rm78Script changeState: hereComesTheDork)

			(Load VIEW vHenchetteStrippers)
			(Load VIEW vCameraman)
			(Load VIEW vWitchDoctor)
			(Load VIEW vChief)
			(Load VIEW vTribalInitiation)
			(Load VIEW vWitchDoctorTreating)
			(Load VIEW vComputerBearer)
			(Load VIEW vKalalau)
			(Load VIEW vKalalauKissing)
			(Load VIEW vWedding)
			(Load VIEW vPhotographer)
			(Load SOUND mWedding)
			(theSound 	init:)

			((View new:)
				view:				vHenchetteStrippers
				ignoreActors:
				loop:				0
				setCel:			LastCel
				posn:				90 123
				addToPic:
			)
			((View new:)
				view:				vHenchetteStrippers
				ignoreActors:
				loop:				1
				setCel:			LastCel
				posn:				105 120 
				addToPic:
			)
			((View new:)
				view:				vHenchetteStrippers
				ignoreActors:
				loop:				2
				setCel:			LastCel
				posn:				120 117
				addToPic:
			)
			((View new:)
				view:				vHenchetteStrippers
				ignoreActors:
				loop:				3
				setCel:			LastCel
				posn:				135 114
				addToPic:
			)

			((View new:)							;**	motherBaby
				view: 			vTribalInitiation
				ignoreActors:
				loop: 			lFamiliesWest
				cel: 				1
				posn: 			67 106
				addToPic:
			)
			((View new:)							;**	motherBaby #2
				view: 			vTribalInitiation
				ignoreActors:
				loop: 			lFamiliesEast
				cel: 				1
				posn: 			206 117
				addToPic:
			)
			((View new:)							;**	family
				view: 			vTribalInitiation
				ignoreActors:
				loop: 			lFamiliesWest
				cel: 				0
				posn: 			16 109
				addToPic:
			)

			((= aDrummer (Prop new:))
				view: 			vTribalInitiation
				ignoreActors:
				setLoop:			lDrummerWest
				posn:				233 146
				stopUpd:
				init:
			)
			((= aPedaler (Prop new:))
				view: 			vComputerBearer
				ignoreActors:
				setLoop:			7
				posn:				212 164
				stopUpd:
				init:
			)
			((= aDoctor (Actor new:))
				view:				vWitchDoctor
				loop:				facingNorth
				posn:				159 172
				setPri: 			14
				setCycle:		Walk
				illegalBits:	0
				stopUpd:
				init:
			)
			((= aHulaHooper (Actor new:))
				view:				vPhotographer
				ignoreActors:
				illegalBits:	0
				setLoop:			lHulaHoop
				setCycle:		Walk
				setStep:			1 1
				posn:				146 129
				stopUpd:
				init:
			)
			((= aDancer (Actor new:))
				view:				vPhotographer
				ignoreActors:
				illegalBits:	0
				setLoop:			lMaleDancer
				setCycle:		Walk
				setStep:			2 1
				posn: 			118 145
				stopUpd:
				init:
			)

			((= aKalalau (Actor new:))
				view:				vKalalau
				loop:				facingWest
				ignoreActors:
				illegalBits:	0
				posn:				-28 142
				setCycle:		Walk
				setStep:			3 2
				cycleSpeed:		1
				moveSpeed:		1
				init:
			)
			((= aChief (Actor new:))
				view:				vChief
				ignoreActors:
				illegalBits:	0
				setPri:			9
				setCycle:		Walk
				cycleSpeed:		1
				moveSpeed:		1
				posn:				-22 138
				init:
			)
			((= aMouth (Prop new:))
				view:				vChief
				ignoreActors:
				setLoop:			lMouth
				setPri:			10
				posn:				999 999
				setCycle:		Forward
				init:
			)

			((= aCameraman (Actor new:))
				view:				vCameraman
				ignoreActors:
				illegalBits:	0
				loop:				facingEast
				cel:				0
				posn:				99 cameramanY
				moveSpeed:		2
				cycleSpeed:		2
				setCycle:		Walk
				setStep:			3 2
				init:
				setScript:		minicamScript
			)
			((= aPhotographer (Actor new:))
				view:				vPhotographer
				ignoreActors:
				illegalBits:	0
				setLoop:			lPhotographer
				setCel:			5				;**	legs together
				setCycle:		Walk
				setPri:			ltMagenta
				posn:				56 170
				stopUpd:
				init:
			)
		)
	)
)


(instance rm78Script of Script
	(method (doit)
		(super doit:)

		(if (and	(!= islandStatus wedding)
					(== (ego loop?) facingNorth)
					(== 6 (aWoodchopper cel?))	;**	6 is in air; 7 is last cel
					(ego inRect: nearLog)
					(== currentStatus NORMAL))
			(self changeState: removeToe)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/man, native')
				(Print "He seems to be slowing down, doesn't he?")
			)

			(if (Said '/door')
				(Print "All the doors here are closed.
					The natives enjoy their privacy.
					Leave them alone.")
			)

			(if (Said '[/room, village, hut]')
				(Print "This must be the witch doctor's hut.")
				(Print "A native man chops wood near his hut.")
			)
		)

		(if (or	(Said '(go<in), (walk<in), climb, enter/stair, hut')
					(Said 'look/window')
					(Said 'knock, open/door'))
			(Print "It would be better if you stayed out.")
		)

		(if (Said 'talk/man, native')
			(if (ego inRect: nearWoodchopper)
				(Print "\"Grunt.\"")
				(Print "(Not very talkative, this one!)" LOW)
			else
				(NotClose)
			)
		)

		(if (Said '/man, native')
			(Print "You'd better not get too close to this guy!")
		)

		(if (Said '/axe')
			(Print "It's his axe. Leave it alone.")
		)

		(if (Said '/wood')
			(Print "It's his wood. 
				Boy! You try to take everything that's not nailed down!")
		)
	)


	(method (changeState newState &tmp i)
		(switch (= state newState)

;**				(intro)

			;**	SHOWTIME!

			(hereComesTheDork
				(= cycles 2)
			)

			(startHula
				(Print "The entire village has turned out for the wedding of
					their princess, Kalalau, to the white-suited stranger.")
				(= cycles 20)
			)

			(goodTurnout
				(theSound			play:)
				(Print "The drummer begins his special, stylized rendition
					of the theme from Wagner's \"Lonegrin.\"")
				(aDrummer
					setCycle:		Forward
				)
				(aHulaHooper
					setMotion:		MoveTo 222 123 self
				)
				(aDancer
					setMotion:		MoveTo 46 111 self
				)
			)

			;**	(hulaDone)

			(dancerDone
				(Print "The witch doctor is waiting;
					you move into position to await the entrance of your
					betrothed.")
				(aHulaHooper
					setLoop:			lAddToPix
					cel:				cHulaHoop
					stopUpd:
				)
				(aDancer
					cel:				0
					stopUpd:
				)
				(ego
					setMotion:		MoveTo 163 134 self
				)
			)

			(egoDone
				(ego
					setLoop:			facingSouth
					stopUpd:
				)
				(= seconds 3)
			)

			(drummer
				(Print "In comes your beloved Kalalau on the arm of her father.")
				(= seconds 3)
			)

			(startGirl
				(aKalalau
					setMotion:		MoveTo 182 152 self
				)
				(aChief
					setMotion:		MoveTo 100 138 self
				)
				(aPedaler
					setCycle:		Forward
				)
				(minicamScript changeState: toChief)
			)


			;**	(waitForSlowestWalker)

			(kInPlace
				(aKalalau
					setLoop:			facingWest
				)
				(ego
					setMotion:		MoveTo 165 152 self
				)
			)

			(lInPlace
				(aKalalau			hide:)
				(ego
					view:				vWedding
					setLoop:			lStartWhirling
					cel:				0
					posn:				172 152
				)
				(Print "\"Who gives this woman to marry this man?\"" draw:)
				(aChief				stopUpd:)
				(aMouth
					posn:				(+ (aChief x?) mouthX)
										(+ (aChief y?) mouthY)
				)
				(= seconds 3)
			)

			(meDo
				(aMouth
					posn:				666 666
				)
				(= aFlash aMouth)				;**	let's just reuse everybody!
				(= aCupidEast aChief)
				(ego					stopUpd:)
				(Print "\"That'd be me,\" says Chief Keneewauwau." draw:)
				(AddActorToPic aChief)
				(aCupidEast posn:	888 888)
				(= seconds 3)
			)

			(theRing
				(Print "\"Very well.\"
					Turning to you, the witch doctor asks,
					\"And, do you have a ring?\"")
				(minicamScript changeState: toGirl)
				(= seconds 3)
			)

			(otherGame
				(Print "\"You know,
					there was a ring in The Land of the Lounge Lizards,\"
					you tell him,
					\"but I never thought to bring it along here!\"")
				(= seconds 3)
			)

			(wellOK
				(Print "\"Never mind. Let's begin.\"")
				(theSound			stop:)
				(AddActorToPic aDrummer)
				(= aHeart aDrummer)			;**	drummer becomes a heart
				(aHeart posn:		777 777)
				(Print "\"Following the traditions of our peoples,
					you must now spin in a circle until you are dizzy and
					throw up.\"")
				(= seconds 3)
			)

			(twirlPlease
				(ego
					cycleSpeed:		2
					setCycle:		EndLoop self
				)
			)

			(twirling
				(ego
					cycleSpeed:		6
					setLoop:			lWhirling
					setCel:			0
					setCycle:		Forward
				)
				(= cycles 12)
			)

			(speedUp
				(= i (ego cycleSpeed?))
				(if i
					(-- i)
					(ego cycleSpeed: i)
					(-- state)
				)
				(= cycles 8)
			)

			(goFastAwhile
				(= seconds 6)
			)

			(slowDown
				(= i (ego cycleSpeed?))
				(if ( < i 8)
					(++ i)
					(ego cycleSpeed: i)
					(-- state)
				)
				(= cycles 8)
			)

			(pieInFace
				(ego
					setLoop:			lStanding
				)
				(Print "\"Now smash this wedding cake into your beloved's
					face.\"")
				(= cycles 10)
			)

			(doCake
				(ego
					cycleSpeed:		2
					setLoop:			lCake
					setCycle:		EndLoop self
				)
			)

			(cakeDone
				(= cycles 20)
			)

			(nowMoonwalk
				(Print "\"And finally, moonwalk across this dirt compound!\"")
				(= cycles 22)
			)

			(necessary
				(Print "(Is this really necessary?)" LOW)
				(= cycles 22)
			)
			
			(walkWest
				(aKalalau
					show:
					setLoop:			facingEast
					cycleSpeed:		0
					moveSpeed:		0
					setCycle:		Walk
					setMotion:		MoveTo 62 152 
				)
				(ego
					view:				vEgo
					setLoop:			facingEast
					cycleSpeed:		0
					moveSpeed:		0
					setCycle:		Walk
					setMotion:		MoveTo 45 152 self
				)
			)
			
			(walkEast
				(aKalalau
					setLoop:			facingWest
					setMotion:		MoveTo 189 152 
				)
				(ego
					setLoop:			facingWest
					setMotion:		MoveTo 172 152 self
				)
			)

			(kissHer
				(ego
					setLoop:			facingEast
				)
				(Print "\"You may kiss the bride!\"" draw:)
				(= cycles 10)
			)

			(theKiss
				(aKalalau			hide:)
				(ego
					view:				vKalalauKissing
					setLoop:			lTheKiss
					cel:				0
					cycleSpeed:		1
					setCycle:		EndLoop self
				)
			)

			(holdIt
				(= seconds 5)
			)

			(breakLips
				(Print "(villagers go aaaaahhh)" LOW)
				(ego
					setCycle:		BegLoop self
				)
			)

			(kissDone
				(ego
					view:				vWedding
					setLoop:			lStanding
					cycleSpeed:		0
					setCycle:		Walk
				)
				(= seconds 3)
			)

			(inLaws
				(Print "\"Now, Larry, we'd like to welcome you to our family.
					Look at it this way:
					you now have a villageful of in-laws!\"")
				(AddActorToPic aPedaler)
				(= aCupidWest aPedaler)
				(aCupidWest posn:	999 999)
				(aPhotographer
					setMotion:		MoveTo 132 160 self
				)
			)

			(photogInPlace
				(aPhotographer
					setLoop:			lRaiseCamera
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(cameraUp
				(aDoctor
					view:				vWitchDoctorTreating
					setLoop:			1
					setCycle:		Forward
				)
				(= cycles 50)
			)

			(growAheart
				(aHeart
					view:				vWedding
					loop:				3
					posn:				170 139
					cel:				0
					cycleSpeed:		2
					setPri:			ltMagenta
					setCycle:		EndLoop self
				)
			)

			(addWings
				(aDoctor
					view:				vWitchDoctor
					setLoop:			facingNorth
					setCycle:		Walk
					stopUpd:
				)
				(aCupidWest
					view:				vWedding
					setLoop:			lCupid1
					posn:				154 106
					setCycle:		Forward
				)
				(aCupidEast
					view:				vWedding
					setLoop:			lCupid2
					posn:				186 106
					setCycle:		Forward
				)
				(= cycles 30)
			)

			(flasher
				(aFlash
					view:				vPhotographer
					setLoop:			lFlash
					setPri:			ltRed
					cel:				0
					posn:				(+ (aPhotographer x?) 5)
										(- (aPhotographer y?) 35)
					cycleSpeed:		0
					setCycle:		EndLoop self
				)
			)

			(removeFlash
				(aFlash				dispose:)
				(aPhotographer
					setCycle:		BegLoop self
				)
			)

			(cameraDown
				(= cycles 10)
			)

			(shrinkHeart
				(aHeart
					setCycle:		BegLoop self
				)
				(aCupidWest			dispose:)
				(aCupidEast			dispose:)
			)

			(removeHeart
				(aHeart				dispose:)
				(= seconds 3)
			)

			(comeInsideBoy
				(Print "\"Larry, my son, at this time,
					I would like to give you a special wedding present in my hut.\"")
				(Print "\"Please, walk this way.\"")
				(aKalalau		
					setLoop:			facingWest
					show:
				)
				(ego
					view:				vEgo
					setPri:			RELEASE
					setLoop:			RELEASE
					loop:				facingSouth
					posn:				165 152
				)
				(aDoctor
					setPri:			RELEASE
					setMotion:		MoveTo 150 152 self
				)
			)

			(besideEgo
				(aDoctor
					setMotion:		MoveTo 165 116 self
				)
				(= cycles 5)
			)

			(jumpStartEgo
				(Print "You think, \"If I walked that way, I'd be a maitre'd!\"")
				(ego
					setMotion:		MoveTo 171 120
				)
			)

			(toTheHut
				(curRoom newRoom: 178)
			)


			;**	Larry goes in for a little podiatric work...

			(removeToe
				(HandsOff)
				(= currentStatus NEAR_DEATH)
				(aWoodchopper
					setScript:		FALSE
					setCel:			LastCel
					stopUpd:
				)
				(Print "\"OW!\"" draw:)
				(Print "You scream in anguish as the woodchopper
					misses the log, and removes your foot!")
				(= seconds 3)
			)

			(UBdying
				(= currentStatus DEAD)
				(Print "Next time, better leave this guy alone!")
			)
		)
	)
)

(instance woodScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(woodInit
				(= cycles 8)
			)

			(woodStart
				(aWoodchopper
					setCycle:		EndLoop self
				)
			)

			(woodDone
				(= cycles (++ pauseCycles))
				(= state woodInit)
			)
		)
	)
)


(instance minicamScript of Script
	(method (changeState newState)
		(switch (= state newState)

;**				(minicamInit)

			(kneel
				(aCameraman
					loop:				lKneel
					setCycle:		EndLoop self
				)
			)

			(stopUpdate
				(aCameraman 		stopUpd:)
			)

			(toGirl
				(aCameraman
					setCycle:		BegLoop self
				)
			)
			(walkToGirl
				(aCameraman
					setCycle:		Walk
					setMotion:		MoveTo girlX cameramanY self
				)
				(= state minicamInit)
			)

			(toChief
				(aCameraman
					setCycle:		BegLoop self
				)
			)
			(walktoChief
				(aCameraman
					setCycle:		Walk
					setMotion:		MoveTo chiefX cameramanY self
				)
				(= state minicamInit)
			)
		)
	)
)
(instance theSound of Sound
	(properties
	 	number:		mWedding
		loop:			-1
	)
)


