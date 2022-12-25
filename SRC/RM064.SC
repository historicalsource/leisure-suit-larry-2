;**
;**	Logics for room 64 -- fresh out of the airplane
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 21, 1988
;**


(script#				64)

(public
	rm64				0
)


;(define	vEgoParachute				162)
	(define	lFalling			0)
	(define	lParachuting	1)

;(define	vClouds						620)	;**	Loop0 contains Cels0-10


(local
	numClouds
	aPlane
)


(enum												;**	rm64 state
	falling
	changeRoomsFalling
	floating
	changeRoomsFloating
)


(instance rm64 of Room
	(properties
		picture:		64
		horizon:		5
	)

	(method (init &tmp i cloud)
		(Load VIEW vEgoParachute)
		(Load VIEW vAirplane)
		(Load VIEW vClouds)

		(super init:)

		((= aPlane (Actor new:))			;**	tiny plane
			view:				vAirplane
			setLoop:			5
			setCel:			0
			posn:				155 8
			illegalBits:	0
			setStep:			3
			setMotion:		MoveTo -20 8	;**	fly off screen to the left
			init:
		)

		(= numClouds (Random 2 6))
		(for ((= i 0)) ( < i numClouds) (( ++ i))		;**	Create clouds 
			((View new:)
				view:				vClouds
				cel:				(Random 0 10)
				ignoreActors:
				posn: 			(Random -10 330) (Random 5 188)
				addToPic:
			)
		)

		(ego
			view: 			vEgoParachute
			posn:				160 9
			setCycle:		Forward
			loop: 			lFalling
			setStep:			-1 3
			init:
		)

		(= currentStatus FALLING)
		(self setScript:	rm64Script)
		(User
			canInput: 		TRUE
			canControl: 	FALSE
		)
	)
)


(instance rm64Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look')
			(if (== currentStatus FALLING)
				(Print "The view from this height is spectacular,
					but wobbly.")
			else
				(Print "The view from this height is spectacular.")
			)
		)

		(if (Said 'use, open, pull/ripcord, parachute')
			(if (!= currentStatus FALLING)
				(Print "Again?")
			else
				(Print "Good idea, Larry!")
				(if (== wearingParachute TRUE)
					(self changeState: floating)
				else
					(Print "But you aren't wearing a parachute!")
				)
			)
		)

		(if (Said '(put<on), wear, fasten, strap, tie/parachute')
			(cond
				((== wearingParachute TRUE)
					(Print "You are!")
				)
				((ego has: iParachute)
					(Print "You valiently struggle with the balky straps.
						However even a macho stud like you is
						incapable of donning a parachute
						while plummeting through the atmosphere
						at terminal velocity.")
				)
				(else
					(Print "You don't seem to have one!")
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(falling
				(ego
					setMotion:	MoveTo 144 208 self
				)
			)

			(changeRoomsFalling
				(curRoom newRoom: 65)
			)

			(floating
				(ego
					loop: 		lParachuting
					setStep:		-1 1
					cycleSpeed:	4
					setMotion:	MoveTo 144 208 self
				)
				(= currentStatus PARACHUTING)
				(Print "The blessed parachute blossoms above you,
					jerking you around a lot,
					a feeling not wholly unfamiliar to you!" draw:)
			)

			(changeRoomsFloating
				(curRoom newRoom: 65)
			)
		)
	)
)

