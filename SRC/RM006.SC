;**
;**	Script 6 -- AIRPLANES
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	July 25, 1988
;**

;**	Make an airplane fly through a scene with (almost) no effort.


(script#	AIRPLANES)							;**	(define	AIRPLANES		6)

(public	airplaneScript			0)


(enum												;**	rmScript state values
	intro
	takeOff
	planeDone
)


(class Airplane of Actor
	(properties
		view 				vAirplane
		cel				0					;**	these are the default values
		startX			-60
		startY			22
		endX				380
		endY				22
		moveSpeed		1
		illegalBits		0
		delayMin 		20
		delayMax			66
	)

	(method (init)
		(super init:)
		(self
			setLoop:		RELEASE
			setCel:
			ignoreActors:
			ignoreHorizon:
			setScript:	(airplaneScript new:)
		)
	)
)


(instance airplaneScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(intro
				(= seconds (Random (client delayMin?) (client delayMax?)))
			)

			(takeOff
				(client
					posn:				(client startX?) (client startY?)
					show:
					setMotion:		MoveTo (client endX?) (client endY?) self
				)
			)

			(planeDone
				(client hide:)
				(self changeState: intro)
			)
		)
	)
)

