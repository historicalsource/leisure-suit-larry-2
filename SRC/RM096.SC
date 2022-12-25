;**
;**	Logics for room 96 -- KGB Death
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 9, 1988
;**


(script#				96)

(public
	rm96				0
)


;(define	vHenchDeath					830)


(enum												;**	rmScript state values
	intro
	where
	howMany
	UBdying
)


(instance rm96 of Room
	(properties
		picture:		96
	)

	(method (init)
		(Load VIEW vHenchDeath)

		(Load SOUND mKGB)

		(super init:)

		(theSound play:)

		((View new:)
			view: 			vHenchDeath
			ignoreActors:
			posn: 			110 186
			addToPic:
		)

		(self setScript: rm96Script)
	)
)


(instance rm96Script of Script
	(method (doit)
		(super doit:)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(= seconds 3)
			)

			(where
				(Print "You are quickly taken to a local office of
					the KGB where a specialist in onklunk extraction is
					busy giving you the third degree!" BIG)
				(= seconds 3)
			)

			(howMany
				(Print "\"And so, my leettle white-suited capitalist swine,\"
					says the KGB agent,
					\"you vill now tell us zee location of zat onklunk
					or I vill be forced to run zeese alto saxophone reeds
					under your fingernails
					until you're zinging like zee Bird!\"" BIG)
				(= seconds 3)
			)

			(UBdying
				(Print "Things don't look good, Larry.
					Maybe next time a different approach would be better..." BIG)
				(= currentStatus DEAD)
			)
		)
	)
)



(instance theSound of Sound
	(properties
	 	number:		mKGB
		loop:			1
	)
)

