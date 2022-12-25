;**
;**	Logics for room 99 -- speed check upon restart
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 19, 1988
;**


(script#				99)

(public
	rm99				0
)

(local
	doneTime
)

;(define	vGarage						253)


(instance rm99 of Room
	(properties
		picture:		178
		style:		IRISIN 
	)

	(method (init)
		(HandsOff)
		(super init:)
		(ego
			view:				vGarage
			setLoop:			2
			posn:				20 100
			setStep:			1 1
			setMotion:		MoveTo 300 100 
			setCycle:		Forward
			init:
		)

		(theGame setSpeed: 0)
	)

	(method (doit)
		(super doit:)

		(++ machineSpeed)
		(if (== machineSpeed 1)
			(= doneTime (+ 60 (GetTime)))
		)
		(if ( < doneTime (GetTime))
			(if logging
				(Print (Format @string "Attention QA: this machine has an Al Lowe
					speed rating of %d. Please write down the number and the
					type of machine you're on." machineSpeed))
				(Print "(Note: your mileage may vary.)" LOW)
			)	
			(theGame setSpeed: 6)
			(TheMenuBar	draw:)
			(StatusLine	enable:)
			(curRoom newRoom: 23)
		)
	)
)

