;**
;**	Logics for room 48 -- Kalalau cliffs
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 26, 1988
;**


(script#				48)

(public
	rm48				0
)


(define	msgLookOut		"Larry! LOOK OUT!!")
(define	msgTooClose		"That was close. You'd better be more careful!")

;(define	vEgoBikiniStuffed			151)
;(define	vEgoCliffs1					156)
;(define	vEgoCliffs2					157)
;(define	vEgoCliffs3					158)
;(define	vEgoCliffs4					159)

;(define	s1Boing						13)
;(define	s2Boing						14)
;(define	s3Boing						15)

(local
	loopCount
	safeX
	safeY
	oldScore
	localBS										;**	bassSetter
)

(procedure (TripDone)
	(theSound stop:)
	(cls)
	(+= score 1)
	(StatusLine doit:)
	(Print msgTooClose HIGH time: 5 draw:)
)

(instance theSound of Sound
	(properties
		number: 		s1Boing
		loop:			1
	)
)


(enum												;**	rmScript state values
	intro
	onTrailBlue

	touchedLtBlue
	ltBluePatch
	aroundLtBlue
	onTrailGreen

	touchedLtGreen
	aroundLtGreen
	onTrailCyan

	touchedLtCyan
	aroundLtCyan
	onTrailRed

	touchedLtRed
	aroundLtRed
	nextRoom

	touchedBlue
	blueFall1
	blueFall2

	touchedGreen
	greenFall1
	greenFall2

	touchedCyan
	cyanFall1
	cyanFall2
	cyanFall3

	touchedRed
	redFall1
	redFall2
	redFall3
)


(instance rm48 of Room
	(properties
		picture:		48
		horizon:		1
	)

	(method (init)
		(Load VIEW vEgoCliffs1)
		(Load VIEW vEgoCliffs2)
		(Load VIEW vEgoCliffs3)
		(Load VIEW vEgoCliffs4)
		(Load VIEW vEgoBikiniStuffed)

		(Load SOUND s1Boing)
		(Load SOUND s2Boing)
		(Load SOUND s3Boing)

		(super init:)

		(theSound	init:)

		(self
			setScript: 		rm48Script
		)

		(= oldScore score)

		(= currentEgoView vEgoBikiniStuffed)
		(NormalEgo)
		(ego
			posn: 			312 84
			setPri:			11
			setStep:			3 1
			loop:				facingWest
			init:
			baseSetter:		(= localBS (BassSetter new:))	;**	add to actor
		)
		(localBS				radii: 6)
		(HandsOff)
	)
)


(instance rm48Script of Script
	(method (doit)
		(super doit:)

		(cond
			((and (& (ego onControl:) cLtRed)
					(== state onTrailRed))
				(self changeState: touchedLtRed)
			)
			((and (& (ego onControl:) cLtCyan)
					(== state onTrailCyan))
				(self changeState: touchedLtCyan)
			)
			((and (& (ego onControl:) cLtGreen)
					(== state onTrailGreen))
				(self changeState: touchedLtGreen)
			)
			((and (& (ego onControl:) cLtBlue)
					(== state onTrailBlue))
				(self changeState: touchedLtBlue)
			)
			((and (& (ego onControl:) cRed)
					(== state onTrailRed))
				(self changeState: touchedRed)
			)
			((and (& (ego onControl:) cCyan)
					(== state onTrailCyan))
				(self changeState: touchedCyan)
			)
			((and (& (ego onControl:) cGreen)
					(== state onTrailGreen))
				(self changeState: touchedGreen)
			)
			((and (& (ego onControl:) cBlue)
					(== state onTrailBlue))
				(self changeState: touchedBlue)
			)
			(else
				(if (or	(== state onTrailBlue)
							(== state onTrailGreen)
							(== state onTrailCyan)
							(== state onTrailRed))
					(= safeX (ego x?))
					(= safeY (ego y?))
				)
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/self')
				(Print "You've always been afraid of heights.")
			)

			(if (Said 'dive, jump')
				(Print "You die on the rocks below.")
				(Print "(The idea is to NOT do that!)" LOW)
			)

			(if (Said '[/room, mountain, cliff]')
				(Print "The cliffs drop precipitously off into the pounding
					surf directly below.
					Be careful, Larry: a fall from this height could be fatal!")
			)
		)

		(if (or	(Said 'change, wear, (put<on)/suit, clothes')
					(Said 'change, remove, (take<off)/(suit<swim), bikini, swimsuit')
					(Said 'get<dressed'))
			(if (!= state aroundLtRed)
				(Print "Not now. Maybe later!")
			else
				(theGame changeScore: 6)
				(+= oldScore 6)
				(if (== braContents iMoney)
					(Print "Ok. You transfer your money from the bikini top
						to your leisure suit,
						then toss the bikini far over the cliff.
						As you slip into your leisure suit you realize
						you were beginning to enjoy wearing women's clothing!")
				else
					((inventory at: iSoap) moveTo: LIMBO)
					(Print "Ok. You slip back into your leisure suit and
						toss the bikini and soap far over the cliff.
						Too bad, as you were beginning to enjoy wearing
						women's clothing!")
				)
				(Print "(At least you still have your beautiful,
					long blond hair!)" LOW)
				(= currentEgoView vEgoBlond)
				(= braContents FALSE)
				(ego view: currentEgoView)
				((inventory at: iBikiniTop) moveTo: LIMBO)
				((inventory at: iBikiniBottom) moveTo: LIMBO)
				(= cycles 5)					;**	let's trip that timer!
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(= seconds 3)
			)

			(onTrailBlue
				(User
					canControl:		TRUE
					canInput:		TRUE
				)
				(Print "Ohmigosh! How will a guy as clumsy as you are,
					ever manage to hike a trail as narrow as this?")
			)

			(touchedLtBlue
				(HandsOff)
				(theSound			dispose:)
				(ego
					illegalBits:	0
					setPri:			10
					setMotion:		MoveTo 220 95 self
				)
			)

			(ltBluePatch
				(ego
					setPri:			9
					setMotion:		MoveTo 270 95 self
				)
			)

			(aroundLtBlue
				(= currentEgoView vEgoCliffs2)
				(ego
					view:				currentEgoView
					setPri:			9
					setStep:			2 1
					setMotion:		MoveTo 203 106 self
				)
				(localBS				radii: 5)
			)

			(onTrailGreen
				(User
					canControl:		TRUE
					canInput:		TRUE
				)
				(ego
					illegalBits:	cLtWhite
				)
			)

			(touchedLtGreen
				(HandsOff)
				(theSound			dispose:)
				(ego
					illegalBits:	0
					setPri:			8
					setMotion:		MoveTo (+ (ego x?) 44) (ego y?) self
				)
			)

			(aroundLtGreen
				(= currentEgoView vEgoCliffs3)
				(ego
					view:				currentEgoView
					setPri:			7
					setStep:			1 1
					posn:				(ego x?) 100
					setMotion:		MoveTo 132 100 self
				)
				(localBS				radii: 4)
			)

			(onTrailCyan
				(User
					canControl:		TRUE
					canInput:		TRUE
				)
				(ego
					illegalBits:	cLtWhite
				)
			)

			(touchedLtCyan
				(HandsOff)
				(theSound			dispose:)
				(ego
					illegalBits:	0
					setPri:			6
					setMotion:		MoveTo (+ (ego x?) 12) (ego y?) self
				)
			)

			(aroundLtCyan
				(= currentEgoView vEgoCliffs4)
				(ego
					view:				currentEgoView
					setPri:			5
					posn:				99 124
					setMotion:		MoveTo 65 123 self
				)
				(localBS				radii: 3)
			)

			(onTrailRed
				(User
					canControl:		TRUE
					canInput:		TRUE
				)
				(ego
					illegalBits:	cLtWhite
				)
			)

			(touchedLtRed
				(HandsOff)
				(ego
					illegalBits:	0
					setPri:			4
					setMotion:		MoveTo (+ (ego x?) 22) (ego y?) self
				)
			)

			(aroundLtRed
				(User
					canControl:		FALSE
					canInput:		TRUE
				)
				(= currentEgoView vEgoBikiniStuffed)
				(ego
					hide:
					view:				currentEgoView
					baseSetter:		0			;**	dispose of BS
				)
				(if localBS (localBS dispose:))
				(Print "Pausing a moment, you catch your breath before
					crossing the airport parking lot to the terminal building...")
				(Print "...and your escape from that wonderful tropical resort!")
				(= cycles 50)
			)

			(nextRoom
				(= cycles 0)
				(Print "Well rested, you stride boldly across the dirt parking
					lot to the front door of the main terminal building.")
				
				;**	Give him his old score back again, plus six points.

				(if (!= score oldScore)
					(= score oldScore)			;**	sneaky patch
					(StatusLine doit:)
					(Print "(By the way, you didn't think you were going to 
						keep all those free points, did you?)" LOW)
				)
				(curRoom newRoom: 50)
			)


			;**	Here he almost buys the farm...

			(touchedBlue
				(HandsOff)
				(ego
					view:				vEgoCliffs1
					illegalBits:	0
					setLoop:			0
					cel:				0
					setCycle:		EndLoop self
				)
				(Print msgLookOut HIGH dispose: draw:)
			)

			(blueFall1
				(ego
					setCycle:		BegLoop self
				)
			)

			(blueFall2
				(NormalEgo facingWest)
				(ego
					posn:				safeX safeY
					setPri:			11
					setStep:			3 1
				)
				(= state onTrailBlue)
				(TripDone)
			)

			(touchedGreen
				(HandsOff)
				(theSound 
					number:			s1Boing
					play:
				)
				(ego
					illegalBits:	0
					setLoop:			4
					cel:				0
					setCycle:		EndLoop self
				)
				(Print msgLookOut HIGH dispose: draw:)
			)

			(greenFall1
				(ego
					setLoop:			5
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(greenFall2
				(NormalEgo facingWest)
				(ego
					posn:				safeX safeY
					setPri:			9
					setStep:			2 1
				)
				(= state onTrailGreen)
				(TripDone)
			)

			(touchedCyan
				(HandsOff)
				(ego
					illegalBits:	0
					setLoop:			4
					cel:				0
					setCycle:		EndLoop self
				)
				(theSound 
					number:			s2Boing
					play:
				)
				(Print msgLookOut HIGH dispose: draw:)
			)

			(cyanFall1
				(ego
					setLoop:			5
					cel:				0
					setCycle:		Forward
				)
				(= cycles 22)					;**	make a multiple of 11 (# of cels)
			)

			(cyanFall2
				(ego
					setLoop:			6
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(cyanFall3
				(NormalEgo facingWest)
				(ego
					posn:				safeX safeY
					setPri:			7
					setStep:			1 1
				)
				(= state onTrailCyan)
				(TripDone)
			)

			(touchedRed
				(HandsOff)
				(ego
					illegalBits:	0
					setLoop:			4
					cel:				0
					setCycle:		EndLoop self
				)
				(theSound 
					number:			s3Boing
					play:
				)
				(= loopCount 0)
				(Print msgLookOut HIGH dispose: draw:)
			)

			(redFall1
				(ego
					setLoop:			5
					cel:				0
					setCycle:		EndLoop self
				)
				(if ( > 3 (++ loopCount))
					(-- state)
				)
			)

			(redFall2
				(ego
					setLoop:			4
					setCel:			LastCel
					setCycle:		BegLoop self
				)
			)

			(redFall3
				(NormalEgo facingWest)
				(ego
					posn:				safeX safeY
					setPri:			5
					setStep:			1 1
				)
				(= state onTrailRed)
				(TripDone)
			)
		)
	)
)
