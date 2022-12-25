;**
;**	Logics for room 400 -- resort region
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 19, 1988
;**


(script#				400)

(public
	rm400				0
)


(instance rm400 of Region
	(method (init)
		(super init:)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (and (ego has: iSoap)
					(or	(Said 'wash, clean')
							(Said 'use/soap')))
			(Print "You're already clean.")
			(Print "(It's your mind that's dirty!)" LOW)
		)

		(if (and (ego has: iFlower)
					(Said 'smell/plant, flower'))
			(Print "It smells of native, ornithological, organic fertilizers.")
		)

		(if (Said 'look>')
			(if (Said '/up, overhead, sky')
				(Print "Isn't the air here wonderful?")
			)

			(if (Said '/people, man, woman')
				(Print "Everyone here appears to be even more wealthy
					than you, Larry.")
			)

			(if (Said '/tree, bush, plant')
				(Print "They are everywhere. Lush growth. Huge flowers.")
				(Print "This resort is really a beautiful place.")
			)
		)
	)
)

