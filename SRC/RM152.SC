;**
;**	Logics for room 152 -- The Boom Boom Room!!
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 13, 1988
;**


(script#				152)

(public
	rm152				0
)


(enum												;**	rmScript state values
	intro
	UBdying
)

(instance theSound of Sound
	(properties
		number:		sBOOM
		loop:			1
	)
)

(instance rm152 of Room
	(properties
		picture:		152
		horizon:		1
		style:		WIPEUP
	)

	(method (init)
		(super init:)
		(self setScript: rm152Script)
		(HandsOff)
		(ego
			posn: 		99 1099
		)
		(if (== prevRoomNum 52)
			(Load SOUND sBOOM)
			(theSound	play:)
		else
			(Load SOUND sVolcanoEruption)
			(theSound
				number:		sVolcanoEruption
				play:
			)
		)
	)
)


(instance rm152Script of Script
	(method (doit)
		(super doit:)

		(if (== state intro)
			(ShakeScreen 1 (Random 1 3))
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(= seconds 5)
			)

			(UBdying
				(cond
					((== currentStatus NO_WICK)
						(= currentStatus DEAD)
						(Print "The next time you make a Molotov cocktail,
							why not try using a wick?!")
					)

					((== currentStatus WICK_BURNED_DOWN)
						(= currentStatus DEAD)
						(Print "(No one has ever accused you of having a long fuse,
							Larry!)" LOW)
					)

					((== currentStatus ERUPTION)
						(= currentStatus DEAD)
						(Print "The volcano eruption has interrupted your wedding
							plans in a most insistent manner!
							Perhaps this is one time you'll
							require outside assistance?")
					)

					((== currentStatus BOMBING)
						(if (== (theSound state?) SND_ACTIVE)
							(-- state)
							(= cycles 3)
						else
							(= bombStatus EXPLODED)
							(curRoom newRoom: 52)
						)
					)

					((== currentStatus MATCHES)
						(= currentStatus DEAD)
						(Print "Next time, 
							you'd better read the label on that bottle of
							hair rejuvenator!")
					)

					(else
						(= currentStatus DEAD)
						(Print "Oops. You're dead.")
					)
				)
			)
		)
	)
)

