;**
;**	Logics for room 200 -- LA region
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 25, 1988
;**


(script#				200)

(public
	rm200				0
)


(instance rm200 of Region
	(method (init)
		(super init:)
	)


	(method (doit)
		(super doit:)

		(if (and (== currentTimer	CRUISE_DEPARTURE)
					(not rgTimer)
					( < curRoomNum	26)
					( > secondsInRoom 5)
					(== currentStatus NORMAL))
			(= currentStatus DEAD)
			(= currentTimer FALSE)
			(Print "You have fooled around too long!
				In the distance you hear a foghorn announcing the
				departure of your cruise ship.")
			(Print "This is the story of your life.
				Once again, Larry, you have \"missed the boat!\"")
		)

		(if (and ( > gameMinutes 9)
					(== gameSeconds 1)
					( > secondsInRoom 8)
					((inventory at: iDollar) ownedBy: 23))
			(++ gameSeconds)
			(Print "You're getting off to a slow start, Larry.
				Perhaps you should dig out the documentation that came with 
				this game and read through the Walk-Thru that begins on page 12.")
			(Print "(This is merely a friendly suggestion from your 
				designer and programmer, Al Lowe.)" LOW)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'talk/man, person, woman')
			(Print "No one here is interested in talking to you.")
		)

		(if (and	(ego has: iLotteryTicket)
					(Said 'scratch, mark, use/ticket'))
			(Print "You scratch it until you remove the numbers
				and invalidate it.
				Since it's no good now, you toss it away!")
			(ego put: iLotteryTicket LIMBO)
			(theGame changeScore: -2)
		)

		(if (and (ego has: iSunscreen)
					(Said 'wear, rub, put, use/sunscreen, (lotion<suntan)'))
			(Print "There's no need. No sun can pierce that smoggy sky.")
		)

		(if (Said '/taxi')
			(Print "There are no taxis in this \"Leisure Suit Larry.\"")
		)

		(if (Said 'look>')
			(if (Said '/up, overhead, sky')
				(Print "During a Stage III smog alert, breathing is
					optional (although still permitted).")
			)

			(if (Said '/traffic, street, city')
				(Print "Formerly, you thought LA was filled with cars.")
			)

			(if (Said '/ground')
				(if ( > curRoomNum 30)
					(Print "It just lies there under your feet.")
				else
					(Print "Is this whole city paved?")
				)
			)

			(if (Said '/tree, foliage')
				(Print "All the plants here are made of poly-vinyl chloride
					or another monoxide-breathing substance.")
			)
		)
	)
)

