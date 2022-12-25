;**
;**	Logics for room 700 -- island region
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 3, 1988
;**


(script#				700)

(public
	rm700				0
)


(instance rm700 of Region
	(method (init)
		(super init:)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (and (ego has: iStick)
					(Said 'use, throw/stick'))
			(Print "You hurl the stick through the jungle as hard as you can.
				\"Whee,\" you shout.")
			(ego put: iStick LIMBO)
		)

		(if (ego has: iVine)
			(if (Said 'smoke/vine')
				(Ok)
				(Print "You haven't done this since you were a kid.")
				(Print "(You didn't like it then, either!)" LOW)
				(ego put: iVine LIMBO)
			)

			(if (Said 'throw, use/vine')
				(Print "This is not the place.")
			)
		)

		(if (and (ego has: iAshes)
					(Said 'smell, taste, throw, drop, look/ash'))
			(Print "The ashes have a distinct aroma of burnt iguana flesh!")
		)

		(if (and (ego has: iAshes)
					(Said 'spread, throw, drop, use/ash'))
			(Print "You throw the ashes into the wind and watch them
				disappear forever.")
			(ego put: iAshes LIMBO)
		)

		(if (Said 'look>')
			(if (Said '/up, overhead, sky')
				(Print "The warm tropical sun basks you in
					its amber glow while the clear blue air refreshes
					your lungs.")
			)

			(if (Said '/monkey')
				(Print "You bet they are up there somewhere!")
			)

			(if (Said '/rock, head, art')
				(Print "Evidently, some of the island's rocks were part of an
					ancient fertility ceremony.")
			)

			(if (Said '/tree')
				(Print "The dense undergrowth of the jungle brings a sense
					of foreboding to your heart!")
			)
		)
	)
)

