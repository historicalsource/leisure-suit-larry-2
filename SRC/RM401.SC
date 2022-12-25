;**
;**	Logics for room 401 -- resort beach region
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	September 20, 1988
;**


(script#				401)

(public
	rm401				0
)


(local
	seenMessage
)


(instance rm401 of Region
	(method (init)
		(super init:)
	)


	(method (doit)
		(super doit:)
		(if (& (ego onControl:) cGray)
			(if (and	(== seenMessage FALSE)
						(!= currentStatus LIFEBOATING))
				(= seenMessage TRUE)
				(Print "You have no desire to return to the ocean
					after all you went through on that lifeboat, Larry!")
			)
		else
			(= seenMessage FALSE)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'talk')
			(Print "\"We're too busy recreating to talk to you, buddy.\"")
		)

		(if (or	(Said 'get/suntan')
					(Said 'lie<down'))
			(Print "You got plenty of sun on that lifeboat.")
		)

		(if (Said 'dig')
			(Print "Yeah! You can dig it!")
			(Print "(But not here.)" LOW)
		)

		(if (Said '/umbrella')
			(Print "The umbrellas are not important to your escape from this
				place.")
		)

		(if (Said '/rock')
			(Print "The rocks will not help you.")
		)

		(if (Said 'climb/tree')
			(Print "If you did climb a tree, you'd just see the airport,
				and long even more for escape from this place, and your freedom.
				So... don't!")
		)

		(if (Said 'look>')
			(if (Said '/up, overhead, sky')
				(Print "This sky could sunburn you in just a few seconds.")
				(Print "(It's a good thing you rubbed on that SPF-90 earlier.)"
					LOW)
			)

			(if (Said '/people, man, woman')
				(Print "Everyone here appears to be wearing even less
					clothing than you, Larry.")
			)

			(if (Said '/ocean, water, wave')
				(Print "The ocean certainly looks peaceful,
					but after what you've been through in that lifeboat
					you have no desire to return to it for some time.")
			)

			(if (Said '/beach, sand, ground')
				(Print "The hot sand burns your feet.")
			)

			(if (Said '/tree, bush, plant')
				(Print "Palm trees sway overhead, while tropical succulents
					suc away in the bushes.")
			)
		)
	)
)

