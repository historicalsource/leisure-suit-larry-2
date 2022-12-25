;**
;**	Logics for room 76 -- Chief's hut
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 15, 1988
;**


(script#				76)

(public
	rm76				0
)


;(define	vEgoKneeling				194)
;(define	vEgoTyping					195)
	(define	lEgoTyping			0)
	(define	lScreen1				1)
	(define	lScreen2				2)

;(define	vKalalau						704)
;(define	vComputerBearer			707)
	(define	lWalkEast			4)
	(define	lWalkWest			5)
	(define	lTerminal			6)

;(define	vChief						710)
	(define	lMouth				4)
		(define	mouthX	-1)
		(define	mouthY	-26)

;(define	vTribalInitiation			714)
	(define	lDrummerEast		0)
	(define	lDrummerWest		1)
	(define	lFamiliesEast		2)
	(define	lFamiliesWest		3)

;(define	mManhood						114)


(local
	aPedaler
	aBearer
	aBigEgo
	aBigScreen
	aDrummer
	aKalalau
	aChief
	aMouth
)


(enum												;**	rmScript state values
	intro

	letsParty
	showTime
	setScene
	outComesKen
	kensHere
	soYouWant
	iLoveHer
	soBeIt
	startBearer
	comeBack
	putItDown
	addPC
	PCdone
	bearerInPlace
	sitKid
	waddaIdo
	assembler
	moveLarry
	leastSignificant
	sitDown
	startPumping
	egoTypes

	opcode1
	opcode2
	opcode3
	opcode4
	opcode5
	opcode6
	opcode7

	showBig
	hideBigScreen
	egoUp
	readyForQA
	whatIsIt
	aProgram

	aName
	eunuchs
	tighten
	bottomOfStairs
	turnNorth
	nextRoom
)


(instance rm76 of Room
	(properties
		picture:		76
		horizon:		37
		north:		79
		east:			77
	)

	(method (init)
		(super init:)

		(NormalEgo)
		(self setScript: 	rm76Script)

		(if forceAtest
			(= islandStatus initiation)
		)

		(if (!= islandStatus initiation)
			(self setRegions:	rgIsland)
		else
			(= currentStatus INITIATION)
			(rm76Script changeState: letsParty)

			(Load VIEW vEgoKneeling)
			(Load VIEW vEgoTyping)
			(Load VIEW vKalalau)
			(Load VIEW vComputerBearer)
			(Load VIEW vChief)
			(Load VIEW vTribalInitiation)
			(Load SOUND mManhood)
			(theSound 	init:)

			((View new:)							;**	rock
				view: 			vComputerBearer
				ignoreActors:
				loop: 			6
				cel: 				3
				posn: 			208 94
				setPri:			5
				addToPic:
			)
			((View new:)							;**	palmLeft
				view: 			vComputerBearer
				ignoreActors:
				loop: 			6
				cel: 				2
				posn: 			190 97
				addToPic:
			)
			((View new:)							;**	palmRight
				view: 			vComputerBearer
				ignoreActors:
				loop: 			6
				cel: 				1
				posn: 			224 97
				addToPic:
			)
			((View new:)							;**	motherBaby
				view: 			vTribalInitiation
				ignoreActors:
				loop: 			lFamiliesEast
				cel: 				1
				posn: 			144 89
				addToPic:
			)
			((View new:)							;**	family
				view: 			vTribalInitiation
				ignoreActors:
				loop: 			lFamiliesWest
				cel: 				0
				posn: 			272 91
				addToPic:
			)
			((View new:)							;**	motherBaby #2
				view: 			vTribalInitiation
				ignoreActors:
				loop: 			lFamiliesWest
				cel: 				1
				posn: 			229 87
				addToPic:
			)
			((View new:)							;**	family #2
				view: 			vTribalInitiation
				ignoreActors:
				loop: 			lFamiliesEast
				cel: 				0
				posn: 			190 83
				addToPic:
			)

			((= aBigEgo (Prop new:))
				view:				vEgoTyping
				ignoreActors:
				setLoop:			0
				setPri:			15
				posn:				275 1040
				setCycle:		Forward
				init:
			)
			((= aBigScreen (Prop new:))
				view:				vEgoTyping
				ignoreActors:
				setLoop:			lScreen1
				setPri:			15
				posn:				275 1040
				setCycle:		Forward
				init:
			)
			((= aDrummer (Prop new:))
				view: 			vTribalInitiation
				ignoreActors:
				setLoop:			0
				posn:				92 161
				stopUpd:
				init:
			)
			((= aPedaler (Prop new:))
				view: 			vComputerBearer
				ignoreActors:
				setLoop:			7
				posn:				177 99
				stopUpd:
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
				posn:				-21 87
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

			((= aBearer (Actor new:))
				view:				vComputerBearer
				ignoreActors:
				illegalBits:	0
				setLoop:			lWalkWest
				setCycle:		Walk
				posn:				233 96
				stopUpd:
				init:
			)
			((= aKalalau (Actor new:))
				view:				vKalalau
				loop:				facingWest
				ignoreActors:
				illegalBits:	0
				posn:				300 132
				setCycle:		Walk
				init:
			)
		)

		(cond
			((== prevRoomNum 79)		(ego	posn: 	197	41))
			((== prevRoomNum 77)		(ego	posn: 	306 126))
			(else							(ego	posn: 	306 126))
		)
		(ego					init:)
	)
)


(instance rm76Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/door')
				(Print "All the doors here are closed.
					The natives enjoy their privacy.
					Leave them alone.")
			)

			(if (Said '[/room, village, hut]')
				(Print "The chief's hut is grand, but only in comparision
					to the other huts.")
				(Print "In the background, you can hear strains of
					old Olivia Newton-John tapes.")
			)
		)

		(if (or	(Said '(go<in), (walk<in), climb, enter/stair, hut')
					(Said 'look/window')
					(Said 'knock, open/door'))
			(Print "It would be better if you stayed out.")
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(intro)


		;**	Here we introduce Larry to Ken and the kids...

			(letsParty
				(= cycles 3)
			)

			(showTime
				(HandsOff)
				(ego
					setMotion:		MoveTo 145 126 self
				)
				(aKalalau
					setMotion:		MoveTo 138 132
				)
				(= cycles 10)
			)

			(setScene
				(Print "All of the village is gathered for this profound occasion.
					You and your beloved stride across the compound
					to wait the arrival of the chief." draw:)
			)

			(outComesKen
				(Print "\"Oh, Daddy!\" shouts Kalalau,
					\"please come out.
					There is someone here I would like you to meet!\"")
				(aKalalau
					setCel:			LastCel
					stopUpd:
				)
				(ego
					stopUpd:
				)
				(aChief
					setMotion:		MoveTo 81 91 self
				)
			)

			(kensHere
				(Print "\"Father, this is Larry; Larry Laffer,\" says Kalalau.
					\"We are in love, and wish to marry!\"")
				(aChief
					stopUpd:
				)
				(aMouth
					posn:				(+ (aChief x?) mouthX)
										(+ (aChief y?) mouthY)
				)
				(= seconds 3)
			)

			(soYouWant
				(aMouth 				hide:)
				(Print "\"So you want to marry my daughter?\" says
					Chief Keneewauwau, staring straight to the back of your skull.
					\"No one is worthy of her hand,
					unless he can prove himself a real man!\"" draw:)
				(= seconds 3)
			)

			(iLoveHer
				(Print "\"Oh, I'll do anything for the woman I love, sir,\"
					you tell him.
					\"No sacrifice is too great!\"")
				(aMouth				show:)
				(= seconds 3)
			)

			(soBeIt
				(aMouth 				hide:)
				(Print "\"So be it,\" he says softly.
					Then in a more commanding tone,
					\"Enter the sacrificial hut
					and bring forth the Sacred Peesea!\"" draw:)
				(Print "(villagers go ooooooh)" LOW)
				(= seconds 3)
			)

			(startBearer
				(aBearer
					setLoop:			lWalkEast
					setMotion:		MoveTo 444 96 self
				)
				(aDrummer
					setCycle:		Forward
				)
				(theSound			play:)
			)

			(comeBack
				(aBearer
					setLoop:			facingWest
					setMotion:		MoveTo 221 96 self
				)
			)

			(putItDown
				(aBearer
					setLoop:			(+ 2 facingWest)
					cel:				0
					setCycle:		CycleTo 7 FORWARD self
				)
			)

			(addPC
				((View new:)
					view: 			vComputerBearer
					ignoreActors:
					loop: 			6
					posn: 			207 82
					setPri:			magenta
					addToPic:
				)
				(aBearer
					setCycle:		EndLoop self
				)
			)

			(PCdone
				(aBearer
					setLoop:			(+ 4 facingEast)
					setMotion:		MoveTo 233 96 self
				)
			)

			(bearerInPlace
				(aBearer
					setLoop:			(+ 4 facingWest)
					forceUpd:
					stopUpd:
				)
				(aMouth				show:)
				(= cycles 20)
			)

			(sitKid
				(Print "\"Young man, sit before the Mighty Peesea!
					Prove your manhood by becoming its master and you
					will pass part one of this initiation to our tribe!\"")
				(aMouth 				hide:)
				(= seconds 3)
			)

			(waddaIdo
				(Print "\"But what is it I'm supposed to do?\" you ask.")
				(aMouth				show:)
				(= seconds 3)
			)

			(assembler
				(Print "\"Write a short program,\" he commands.
					\"Any subject or topic, any length...\"")
				(Print "\"No problem,\" you think,
					\"any good adventurer could do that!\"")
				(Print "\"...as long as it's in assembly language!\" he smiles,
					confident in the certainty of your failure!")
				(Print "(villagers gasp)" LOW)
				(aMouth 				hide:)
				(= cycles 20)
			)

			(moveLarry
				(ego
					setMotion:		MoveTo 206 100 self
				)
				(aKalalau
					setLoop:			facingNorth
				)
				(= cycles 10)
			)

			(leastSignificant
				(Print "With your heart in your throat you approach
					their sacred relic.
					\"Is it least significant byte first?\" you wonder.")
			)

			(sitDown
				(ego
					view:				vEgoKneeling
					posn: 			206 100
					cel:				0
					cycleSpeed:		1
					setCycle:		EndLoop self
				)
			)

			(startPumping
				(ego 					stopUpd:)
				(aPedaler
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(egoTypes
				(aBigEgo posn:		275 40)
				(aBigScreen
					posn:				275 40
					cel:				0
					setCycle:		EndLoop self
				)
				(= cycles 10)
			)

			(opcode1
				(aBigScreen
					setLoop:			lScreen2
					setCycle:		Forward
				)
				(cls)
				(Print "INT\ \ \ \ \ 21\n
					JB\ \ \ \ \ \ 0103\n" LOW dispose:)
				(= cycles (Random 20 30))
			)

			(opcode2
				(cls)
				(Print "MOV\ \ \ \ \ AH,4D\n
					INT\ \ \ \ \ 21\n
					CS:\n" LOW dispose:)
				(= cycles (Random 20 30))
			)

			(opcode3
				(cls)
				(Print "MOV\ \ \ \ \ [0BEA],AX\n
					JMP\ \ \ \ \ 02B2\n
					JMP\ \ \ \ \ 1451\n" LOW dispose:)
				(= cycles (Random 20 30))
			)

			(opcode4
				(cls)
				(Print "CS:\n
					TEST\ \ \ \ BYTE\ PTR\ [0C59],01\n
					JZ\ \ \ \ \ \ 0150\n" LOW dispose:)
				(= cycles (Random 20 30))
			)

			(opcode5
				(cls)
				(Print "CS:\n
					TEST\ \ \ \ BYTE\ PTR\ [0C59],02\n
					JZ\ \ \ \ \ \ 014F\n
					IRET\n" at: -1 140 dispose:)
				(= cycles (Random 20 30))
			)

			(opcode6
				(cls)
				(Print "INT\ \ \ \ \ 21\n
					JB\ \ \ \ \ \ 0103\n" LOW dispose:)
				(= cycles (Random 20 30))
			)

			(opcode7
				(cls)
				(Print "TEST\ \ \ \ BYTE\ PTR\ [0C59],04\n
					JZ\ \ \ \ \ \ 0169\n
					CMP\ \ \ \ \ AH,01\n
					JA\ \ \ \ \ \ 014F\n" at: -1 140 dispose:)
				(= cycles (Random 20 30))
			)

			(showBig
				(cls)
				(= seconds 3)
			)

			(hideBigScreen
				(aBigScreen			dispose:)
				(aBigEgo				dispose:)
				(aPedaler			stopUpd:)
				(ego
					setCycle:		BegLoop self
				)
			)

			(egoUp
				(ego
					view:				vEgo
					setCycle:		Walk
					setLoop:			RELEASE
					loop:				facingWest
					cycleSpeed:		0
				)
				(aDrummer			stopUpd:)
				(theSound			stop:)
				(= cycles 15)
			)

			(readyForQA
				(Print "\"Ok, all ready for Quality Assurance,\" you announce.")
				(aMouth				show:)
				(= seconds 3)
			)

			(whatIsIt
				(Print "\"What did you write?\" asks Chief Keneewauwau.")
				(aMouth 				hide:)
				(= seconds 3)
			)

			(aProgram
				(Print "\"A complete, multitasking,
					multiuser operating system that only runs on 8088 CPUs,\"
					you respond.")
				(aMouth				show:)
				(= seconds 3)
			)

			(aName
				(Print "\"Excellent. And do you have a name for this product?\"
					asks Chief Keneewauwau.")
				(aMouth 				hide:)
				(= seconds 3)
			)

			(eunuchs
				(Print "\"Why, Eunuchs, of course!\"")
				(aMouth				show:)
				(= seconds 3)
			)

			(tighten
				(Print "\"Perfect,\" says the Chief Keneewauwau,
					\"I'll tighten up your code later.
					Now follow me, and I'll lead you to the secret path:
					the way known only to card-carrying villagers,
					the path that will lead you to the sacred burial grounds
					of our ancestors,
					the one and only way to the top of Nontoonyt Volcano.\"")
				(Print "\"Rid this island of the evil Dr. Nonookee
					and Kalalau will be your wife!\"")
				(Print "(villagers go aaaahhhh)" LOW)
				(aMouth 				hide:)
				(aChief
					setMotion:		MoveTo 119 127 self
				)
			)

			(bottomOfStairs
				(aChief
					setPri:			RELEASE
					setMotion:		MoveTo 251 103 self
				)
			)

			(turnNorth
				(Print "\"Please, walk this way.\"")
				(aChief
					setMotion:		MoveTo 251 95 self
				)
				(ego
					setMotion:		MoveTo 240 100
				)
			)

			(nextRoom
				(curRoom newRoom: 79)
			)
		)
	)
)
(instance theSound of Sound
	(properties
	 	number:		mManhood
		loop:			-1
	)
)

