;**
;**	Logics for room 28 -- cruise ship departure
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 26, 1988
;**


(script#				28)

(public
	rm28				0
)


;(define	vSparkles					815)
	(define	lSparkle			0)

;(define	vShipDeparting				239)
	(define	lBird				0)
	(define	lLA				1)

;(define	mLoveTheme					116)


(local
	work
	aLA
	aBird
	aSparkle
)


(enum												;**	sparkleScript state values
	sparkleInit
	sparkleStart
	sparkleDone
)

(enum												;**	rmScript state values
	intro
	moveOut
	moveDock
	startBird
	birdDone
	shipDone
)


(instance theSound of Sound
	(properties
	 	number:		mLoveTheme
		loop:			1
	)
)


(instance rm28 of Room
	(properties
		picture:		28
		horizon:		1
	)

	(method (init)
		(Load VIEW vShipDeparting)
		(Load SOUND mLoveTheme)
		(theSound 	init:)

		(super init:)

		((= aSparkle (Prop new:))
			view:				vSparkles
			setLoop:			lSparkle
			setPri:			15
			posn:				222 111
			init:
			hide:
			setScript:		sparkleScript
		)

		((= aLA (Actor new:))
			view:				vShipDeparting
			setLoop:			lLA
			setPri:			1
			posn:				55 38
			setStep:			1 1
			moveSpeed:		2
			init:
		)
		((= aBird (Actor new:))
			view:				vShipDeparting
			setLoop:			lBird
			setPri:			4
			posn:				222 919
			ignoreActors:
			init:
			setCycle:		Forward
		)

		(HandsOff)
		(= currentStatus DEPARTING)
		(self setScript: rm28Script)
	)
)


(instance rm28Script of Script
	(method (doit)
		(super doit:)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(theSound		
					play: self
				)
				(aLA
					setMotion:		MoveTo -63 38 self
				)
				(= seconds 5)
			)

			(moveOut
				(Print "As Los Angeles slips out of view,
					your thoughts are on your search...")
				(Print "...will you find love?")
				(Print "(Or just keep looking in all the wrong places?)" LOW)
			)

			(moveDock
				(= seconds 2)
			)

			(startBird
				(aBird
					posn:				222 19
					setMotion:		MoveTo -23 19 self
				)
			)

			;**	(birdDone) is unused; we'll wait till music is done

			(shipDone
				(SetRgTimer CRUISE_BEDTIME 9 30)
				(curRoom newRoom: 31)
			)
		)
	)
)


(instance sparkleScript of Script
	(method (changeState newState &tmp sparkleX sparkleY)
		(switch (= state newState)

			(sparkleInit
				(= cycles (Random 3 10))
			)

			(sparkleStart
				(switch (Random 1 10)
					(1		(= sparkleX 212)	(= sparkleY	48))
					(2		(= sparkleX 269)	(= sparkleY	47))
					(3		(= sparkleX 193)	(= sparkleY	14))
					(4		(= sparkleX 239)	(= sparkleY 100))
					(5		(= sparkleX 238)	(= sparkleY 105))
					(6		(= sparkleX 198)	(= sparkleY 117))
					(7		(= sparkleX 318)	(= sparkleY 113))
					(8		(= sparkleX	43)	(= sparkleY 121))
					(9		(= sparkleX 163)	(= sparkleY 156))
					(else	(= sparkleX 169)	(= sparkleY	9))
				)
				(aSparkle
					posn:			sparkleX sparkleY
					cel:			0
					cycleSpeed:	(Random 0 2)
					show:
					setCycle:	EndLoop self
				)
			)

			(sparkleDone
				(aSparkle		hide:)
				(self changeState: sparkleInit)
			)
		)
	)
)

