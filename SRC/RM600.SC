;**
;**	Logics for room 600 -- airplane region (Al's first region!)
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 19, 1988
;**


(script#				600)

(public
	rm600				0
)


(instance rm600 of Region
	(method (init)
		(super init:)
	)


	(method (doit)
		(super doit:)

		(if (and (== currentTimer	AIRPLANE_ARRIVAL)
					(not rgTimer)
					( > secondsInRoom 5)
					(== currentStatus NORMAL))
			(= currentStatus DEAD)
			(= currentTimer FALSE)
			(ego hide:)
			(Print "The airplane comes to an abrupt landing,
				without you in your seat!" draw:)
			(Print "You suffer major injuries, too numerous to mention here.
				Next time, listen to your pilot!")
		)

		(if (and (== currentTimer	AIRPLANE_ARRIVAL)
					(== rgTimer	300))
			(-- rgTimer)						
			(Print "The airplane's PA system crackles to life,
				\"Ladies and gentlemen, we are approaching our destination.
				Please return to your seat, fasten your seatbelt, and
				return your tray table to its upright and fully locked position.
				Thank you.\"")
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

;**	From here to next marker, all this code is duplicated in rgAirport...
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

		(if (and (ego has: iAirsickBag)
					(or	(Said 'vomit')
							(Said 'use/bag')))
			(Ok)
			(Print "You do so and immediately discard the now-soiled
				airsick bag.")
			(ego put: iAirsickBag LIMBO)
			(theGame changeScore: -2)
		)
;**	Down to here...


		(if (Said 'look>')
			(if (Said '/up, overhead, ceiling')
				(Print "The ceiling is overhead.")
			)

			(if (Said '/stewardess')
				(Print "In spite of her sarcastic introduction, you
					think you are in love again!")
			)

			(if (Said '/seat')
				(if (== currentStatus SITTING)
					(Print "The guy behind you has stuffed his carry on bag under
						your seat, and he doesn't want you to muck with it!")
				else
					(Print "It's difficult to see the bottom of your trousers
						in your current position!")
				)
			)

			(if (Said '[/room]')
				(Print "From your current viewpoint, you can only see sky.")
			)
		)

		(if (Said 'talk, call/stewardess')
			(Print "She can't help you solve this flight!")
		)

		(if (Said 'grab, touch, feel/stewardess')
			(Print "Whee!")
		)

		(if (Said 'talk/people, man')
			(if (Random 0 1)
				(Print (Format @string
					"You proudly announce,
					%s" laffer))
				(Print "They're underwhelmed!")
			else
				(Print "All businessmen look alike to you.")
			)
		)
	)
)

