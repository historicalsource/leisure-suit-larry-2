;**
;**	Logics for room 83 -- Dr. Nonookee's elevator shaft
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 19, 1988
;**


(script#				83)

(public
	rm83				0
)


;(define	vEgoElevatorShaft			185)
;(define	sFallingLong				11)


(enum												;**	rmScript state values
	intro
	shafted
	doneIt
)

(instance theSound of Sound
	(properties
		number:			sFallingLong
	)
)

(instance rm83 of Room
	(properties
		picture:		178
	)

	(method (init)
		(Load VIEW vEgoElevatorShaft)
		(Load SOUND sFallingLong)

		(super init:)

		(theSound			play:)

		(HandsOff)
		(ego
			view:				vEgoElevatorShaft
			loop:				0
			cel:				0
			posn:				159 88
			setCycle:		Forward
			init:
		)

		(self
			setScript: 		rm83Script
		)
	)
)


(instance rm83Script of Script
	(method (changeState newState)
		(switch (= state newState)

			(intro
				(= seconds 5)
			)

			(shafted
				(Print "Once again, Larry, you're getting the shaft!" LOW 
					time: 5 dispose:)
				(= seconds 5)
			)

			(doneIt
				(curRoom newRoom: 84)
			)
		)
	)
)

