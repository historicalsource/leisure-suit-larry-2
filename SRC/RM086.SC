;**
;**	Logics for room 86 -- consummation
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 22, 1988
;**


(script#				86)

(public
	rm86				0
)


;(define	vBEhappy						116)
	(define	lHeadOnly			0)
	(define	lBigMouth			1)
	(define	lBigEyes				2)

;(define	vBeachThoughts				718)
	(define	lBoth					0)
	(define	lLarry				1)
	(define	lKalalau				2)
	(define	lBush					3)

;(define	vKalalauAndEgoHumping	824)
	(define	lAssumeThePosition 0)
	(define	lWink					1)
	(define	lHumping				2)

;(define	vMoonOverVolcano			825)
	(define	lMoon					0)
	(define	lReflection			1)

;(define	vNightVolcanoEruptions	827)
	(define	lStartEruption		0)
	(define	lEruption			1)
	(define	lSmoke				2)
	(define	lStartLava			3)
	(define	lLava					4)

;(define	mLoveTheme					116)


(local
	work
	aVolcano
	aSmoke
	aLava
	aReflection
	aBigEgo
	aBigEgoEyes
	aBigEgoMouth
	aKalalau
)


(enum												;**	rmScript state values
	intro
	bonnie
	outOfBubble
	inTheBush
	erupt
	addSmoke
	addLava
	cycleLava
	gameEnds
	doWink
	addBigOne
	doBigWink
	humpMore
)


(instance theSound of Sound
	(properties
		number:			mLoveTheme
		loop:				1
	)
)

(instance rm86 of Room
	(properties
		picture:		86
		horizon:		1
		style: 		(+ IRISOUT BLACKOUT)
	)

	(method (init)
		(Load VIEW vBEhappy)
		(Load VIEW vBeachThoughts)
		(Load VIEW vKalalauAndEgoHumping)
		(Load VIEW vMoonOverVolcano)
		(Load VIEW vNightVolcanoEruptions)
		(Load SOUND mLoveTheme)

		(super init:)

		(theSound			init:)

		((View new:)
			view: 			vBeachThoughts
			ignoreActors:
			loop: 			lBush
			posn: 			213 180
			setPri: 			14
			addToPic:
		)
		((View new:)
			view:				vMoonOverVolcano
			posn:				257 48
			addToPic:
		)

		((= aBigEgo (View new:))		;**	His empty head.
			view:				vBEhappy
			ignoreActors:
			setLoop:			lHeadOnly
			setPri:			14
			posn:				204 1095
			init:
		)
		((= aBigEgoEyes (Prop new:))	;**	facial details
			view:				vBEhappy
			setLoop:			lBigEyes
			ignoreActors:
			setPri:			15
			posn:				202 1085
			init:
		)
		((= aBigEgoMouth (Prop new:))	;**	facial details
			view:				vBEhappy
			setLoop:			lBigMouth
			ignoreActors:
			setPri:			15
			posn:				202 1107
			init:
		)

		((= aReflection (Prop new:))
			view:				vMoonOverVolcano
			ignoreActors:
			setLoop:			lReflection
			posn:				262 135
			setCycle:		Forward
			cycleSpeed:		5
			isExtra:			TRUE
			init:
		)
		((= aVolcano (Prop new:))
			view:				vNightVolcanoEruptions
			ignoreActors:
			setLoop:			lStartEruption
			setPri:			2
			cel:				0
			posn:				147 30
			cycleSpeed:		1
			init:
			hide:
		)
		((= aSmoke (Prop new:))
			view:				vNightVolcanoEruptions
			ignoreActors:
			setLoop:			lSmoke
			setPri:			1
			posn:				147 30
			cycleSpeed:		3
			init:
			hide:
		)
		((= aLava (Prop new:))
			view:				vNightVolcanoEruptions
			ignoreActors:
			setLoop:			lStartLava
			setPri:			3
			cel:				0
			posn:				147 30
			cycleSpeed:		4
			init:
			hide:
		)

		((= aKalalau (Actor new:))
			view:				vBeachThoughts
			ignoreActors:
			illegalBits:	0
			setLoop: 		lKalalau
			posn: 			174 134
			setPri: 			9
			setCycle:		Walk
			setStep:			4 3
			cycleSpeed:		2
			moveSpeed:		2
			init:
			hide:
		)

		(ego
			view: 			vBeachThoughts
			illegalBits:	0
			ignoreActors:
			loop: 			lBoth
			cel: 				0
			posn: 			176 109
			setMotion:		FALSE
			setCycle:		Walk
			setStep:			4 3
			cycleSpeed:		2
			moveSpeed:		2
			init:
		)
		(HandsOff)
		(self
			setScript: 		rm86Script
		)
	)
)


(instance rm86Script of Script
	(method (doit)
		(super doit:)

		(if (and ( > state inTheBush)
					( < state cycleLava))
			(if ( > 50 (Random 0 75))
				(ShakeScreen 1 (Random 1 3))
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(= cycles 10)
			)

			(bonnie
				(ego
					setCycle:	EndLoop self
				)
			)

			(outOfBubble
				(aKalalau
					show:
					setMotion:	MoveTo 210 161 self
				)
				(ego
					setLoop: 	lLarry
					cel: 			0
					posn: 		156 136
					setCycle:	Walk
					setMotion:	MoveTo 200 169
				)
			)

			(inTheBush
				(theSound		play:)
				(ego				hide:)
				(aKalalau
					view:			vKalalauAndEgoHumping
					setLoop:		lAssumeThePosition
					cel:			0
					posn:			204 167
					setCycle:	EndLoop self
				)
			)

			(erupt
				(aVolcano
					show:
					setCycle:	EndLoop self
				)
			)

			(addSmoke
				(aVolcano
					setLoop: 	lEruption
					cycleSpeed:	3
					setCycle:	Forward
				)
				(aSmoke
					show:
					setCycle:	Forward
				)
				(= cycles 10)
			)

			(addLava
				(aLava
					show:
					setCycle:	EndLoop self
				)
			)

			(cycleLava
				(aLava
					setLoop:		lLava
					setCycle:	Forward
				)
				(= seconds 5)
			)

			(gameEnds
				(Print "As we leave our hero,
					cavorting in the sand with his new love,
					we ask ourselves the burning question..." draw:)
				(Print "Is women's lib really dead?")
				(Print "Is there still a feminist movement?")
				(Print "Is there a market for
					\"Polyester Patty in Pursuit of the Pulsating Pectorals?\"")
				(Print "...or will Al Lowe have to write yet another
					of these Silly Sin-phonies?")
				(= seconds 3)
			)

			(doWink
				(aKalalau
					setLoop:		lWink
					cel:			0
					setCycle:	EndLoop self
				)
			)

			(addBigOne
				(aBigEgo
					posn:			204 95
					stopUpd:
				)
				(aBigEgoEyes
					posn:			202 85
				)
				(aBigEgoMouth
					posn:			202 107
				)
				(= seconds 3)
			)

			(doBigWink
				(aBigEgoEyes
					setCycle:	EndLoop self
				)
				(aBigEgoMouth
					setCycle:	EndLoop
				)
			)

			(humpMore
				(aKalalau
					setLoop:		lHumping
					setCycle:	Forward
				)
			)
		)
	)
)
