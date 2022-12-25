;**
;**	Logics for room 500 -- airport region
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 19, 1988
;**


(script#				500)

(public
	rm500				0
)


(instance rm500 of Region
	(method (init)
		(super init:)
	)


	(method (doit)
		(super doit:)

		(if (and (== currentTimer	AIRPLANE_DEPARTURE)
					(not rgTimer)
					(!= curRoomNum 57)
					( > secondsInRoom 5)
					(or	(== currentStatus NORMAL)
							(== currentStatus RIDING_SIDEWALK)))
			(= currentStatus DEAD)
			(= currentTimer FALSE)
			(Print "The airport's PA system crackles to life,
				\"Ladies and gentlemen, we are pleased to announce the departure
				of Flight #1 from Gate #1.
				Thank you.")
			(Print "Way to go, Larry.
				You just missed your last chance at escape from this place!")
			(Print "The next time you buy a airplane ticket,
				you'd better hotfoot it to Gate #1!")
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))


;**	From here to next marker, all this code is duplicated in rgAirplane...
		(if (and (ego has: iParachute)
					(Said '(put<on), use, wear/parachute'))
			(Print "Not yet!")
		)

		(if (and (ego has: iBobbyPin)
					(Said 'wear, put, use/pin'))
			(Print "You'd wear the bobby pin if you just had the hair!")
		)

		(if (and (ego has: iPamphlet)
					(Said '(look<in), read/pamphlet'))
			(Print "\"Repent!\"")
			(Print "\"And send money.\"")
		)
;**	Down to here...


		(if (and (ego has: iAirlineTicket)
					(Said 'look<inside/ticket<airline'))
			(Print "It says \"Your flight is about to leave!\"")
		)

		(if (Said 'look>')
			(if (Said '/up, overhead, ceiling')
				(Print "Looking up, your mind wanders home to your
					beloved collection of asbestos ceiling tiles!")
			)

			(if (Said '/art')
				(Print "For such a primitive country,
					they have an appreciation for art.")
			)

			(if (Said '/people, man, woman')
				(Print "They look like they're going someplace.")
			)

			(if (Said '/airplane')
				(Print "They have a lot of air traffic here for such a small
					airport.")
			)

			(if (Said '/wall')
				(Print "For a small, tropical, resort airport they
					certainly have a lot of public artwork.")
			)
		)
	)
)

