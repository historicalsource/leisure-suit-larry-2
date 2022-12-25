;**
;**	Logics for room 27 -- gangplank
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	September 3, 1988
;**


(script#				27)

(public
	rm27				0
)


;(define	vConfetti					238)


(local
	confettiFalling
	numConfetti
)


(enum												;**	confettiScript state values
	confettiInit
	confettiDone
)


(instance rm27 of Room
	(properties
		picture:		27
		horizon:		0
		east:			28
	)

	(method (init)
		(Load VIEW vConfetti)

		(super init:)

		(NormalEgo facingEast)
		(ego
			posn: 			8 135
			setPri:			13
			init:
		)

		(self
			setScript: 		rm27Script
		)
	)
)


(instance rm27Script of Script
	(method (doit &tmp i)
		(super doit:)

		(if (and
				(not confettiFalling)
				( > (ego x?) 160))
			(Print "It looks like you just made it;
				the ship is almost ready to leave!")
			(= confettiFalling TRUE)

			(cond
				(( > machineSpeed 80)	(= numConfetti 5))
				(( > machineSpeed 60)	(= numConfetti 4))
				(( > machineSpeed 40)	(= numConfetti 3))
				(( > machineSpeed 20)	(= numConfetti 2))
				(else							(= numConfetti 1))
			)	
			(= numConfetti 4)
			(for ((= i 0)) ( < i numConfetti) (( ++ i))
				((Actor new:)
					view:				vConfetti
					setPri: 			14
					illegalBits:	0
					setStep:			1 5
					ignoreActors:
					ignoreHorizon:
					setCycle:		Forward
					init:
					setScript:		(confettiScript new:)
				)
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (and	confettiFalling
						(Said '/confetti'))
				(Print "Isn't it pretty!")
			)

			(if (Said '/hull, ship, boat, up')
				(Print "The ship rises beside you.")
			)

			(if (Said '/porthole, hole')
				(Print "Inside the ship, people are overeating.")
			)

			(if (Said '[/room, gangplank]')
				(Print
					"The massive hull of the ocean liner rises beside the gangplank.
						Above you, people are in a festive mood, anticipating
						the impending departure of the U.S.S. Love Tub.")
				(if confettiFalling
					(Print "Somebody up there is littering!")
				)
			)
		)
	)
)


(instance confettiScript of Script
	(method (changeState newState &tmp tmpX tmpLoop tmpCel)
		(switch (= state newState)

			(confettiInit
				(= tmpX			(Random 2 300))
				(= tmpLoop		(Random 0 7))
				(= tmpCel		(Random 0 1))
				(client
					setPri:		14
					setLoop:		tmpLoop
					cel:			tmpCel
					setStep:		-1 (Random 5 12)
					posn: 		tmpX (- 0 (CelHigh vConfetti tmpLoop tmpCel))
					show:
					setMotion:	MoveTo tmpX 180 self
				)
			)

			(confettiDone
				(client			hide:)
				(self changeState: confettiInit)
			)
		)
	)
)

