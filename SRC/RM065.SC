;**
;**	Logics for room 65 -- parachuting to the island
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 21, 1988
;**


(script#				65)

(public
	rm65				0
)


;(define	vEgoParachute				162)
	(define	lFalling			0)
	(define	lParachuting	1)

;(define	vParachuting				607)
	(define	lOceanSplash	0)
	(define	lIsland			1)

;(define	vClouds						620)	;**	Loop0 contains Cels0-10


(local
	work
	numClouds
)


(enum												;**	rm65Script states
	falling
	splashing
	splashDone
	punchLine1

	missedIsland
	punchLine2

	landing
	delaySome
	changeRooms
)


(instance rm65 of Room
	(properties
		picture:		65
		horizon:		5
	)

	(method (init &tmp i cloud)
		(Load VIEW vParachuting)
		(Load VIEW vClouds)

		(super init:)

		((View new:)							;**	tiny island
			view: 			vParachuting
			loop:				lIsland
			cel:				0
			posn:				0 180
			setPri:			15
			ignoreActors:
			addToPic:
		)

		(= numClouds (Random 2 6))
		(for ((= i 0)) ( < i numClouds) (( ++ i))		;**	Create clouds 
			((View new:)
				view:				vClouds
				cel:				(Random 0 10)
				ignoreActors:
				posn: 			(Random -10 330) (Random 5 120)
				addToPic:
			)
		)

		(self setScript:	rm65Script)

		(ego
			ignoreHorizon:
			posn:			144 -22
		)

		(if (== currentStatus FALLING)
			(ego
				loop: 		lFalling
				setStep:		3 4
				init:
			)
			(rm65Script changeState:	falling)
		else
			(= currentStatus PARACHUTING)
			(ego
				loop: 		lParachuting
				setStep:		1 1
				cycleSpeed:	4
				init:
			)
			(rm65Script changeState:	landing)
		)

		(User
			canInput: 		TRUE
			canControl: 	FALSE
		)
	)
)


(instance rm65Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/volcano, island, mountain')
				(if (== currentStatus FALLING)
					(Print "How you hope you will NOT land there!")
				else
					(Print "How you hope you will land there!")
				)
			)

			(if (Said '/water, ocean, wave')
				(Print "It appears to be approaching!")
			)

			(if (Said '/cloud')
				(Print "They appear to be receding!")
			)

			(if (Said '[/room, around, sky]')
				(if (== currentStatus FALLING)
					(Print "The view from this height is spectacular,
						but rather wobbly.")
				else
					(Print "The view from this height is spectacular.")
				)
			)
		)

		(if (Said 'use, open, pull/ripcord, parachute')
			(if (!= currentStatus FALLING)
				(Print "Again?")
			else
				(Print "Good idea, Larry.")
				(if (== wearingParachute TRUE)
					(rm65Script changeState: missedIsland)
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

			;**	This script handles him falling unimpeded.

			(falling
				(ego
					setMotion:	MoveTo 144 177 self
				)
			)

			(splashing
				(ego
					view:			vParachuting
					setLoop:		lOceanSplash
					cel:			0
					cycleSpeed:	1
					setCycle:	EndLoop self
				)
			)

			(splashDone
				(ego hide:)
				(= seconds 3)
			)

			(punchLine1
				(Print "Larry, you must remember the old saying...")
				(if (Random 0 1)
					(Print "\"Skydivers are good to the very last drop!\"")
				else
					(Print "\"It don't mean a thing,
						if you don't pull that string!\"")
				)
				(= currentStatus DEAD)
			)


			;**	If he pulls ripcord in this room, we do this:

			(missedIsland
				(= currentStatus PARACHUTING)
				(= cycles	0)
				(= seconds	0)
				(User canInput: FALSE)
				(Print "The blessed parachute blossoms above you,
					jerking you around a lot,
					a feeling not wholly unfamiliar to you!")
				(ego
					loop: 		lParachuting
					setStep:		1 1
					cycleSpeed:	4
					setMotion:	MoveTo (- (ego x?) 20) 147 self
				)
			)

			(punchLine2
				(Print "Unfortunately, you pulled a little too late to
					hit that island over there!")
				(ego
					setMotion:	MoveTo (- (ego x?) 10) 177 self
				)
				(= state falling)				;**	to cue the splash
			)


			;**	This script only happens if he pulled ripcord before
			;**	entering this room:

			(landing
				(= cycles	0)
				(= seconds	0)
				(= currentStatus PARACHUTING)
				(User canInput: FALSE)
				(ego
					loop: 		lParachuting
					setStep:		1 1
					setMotion:	MoveTo 17 178 self
					setPri:		2
				)
			)

			(delaySome
				(ego hide:)
				(= seconds 3)
			)

			(changeRooms
				(curRoom newRoom: 70)
			)
		)
	)
)

