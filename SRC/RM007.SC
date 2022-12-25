;**
;**	Logics for room 7 -- generic Barber shop init
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 12, 1988
;**


(script#				7)

(public
	rm7				0
)

;(define	mBarber						107)


(instance rm7 of Region
	(method (init)
		(super init:)

		((View new:)							;**	left seating
			view: 			vBarberInt
			loop: 			0
			cel: 				0
			posn: 			102 147
			setPri: 			8
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	right seating
			view: 			vBarberInt
			loop: 			0
			cel: 				1
			posn: 			220 147
			setPri: 			8
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	left table
			view: 			vBarberInt
			loop: 			1
			cel: 				2
			posn: 			119 124
			setPri: 			8
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	right table
			view: 			vBarberInt
			loop: 			1
			cel: 				3
			posn: 			203 124
			setPri: 			8
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	coat rack
			view: 			vBarberInt
			loop: 			1
			cel: 				1
			posn: 			127 112
			setPri: 			7
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	left painting
			view: 			vBarberInt
			loop: 			0
			cel: 				2
			posn: 			100 102
			setPri: 			1
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	right calender
			view: 			vBarberInt
			loop: 			0
			cel: 				3
			posn: 			208 106
			setPri: 			1
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	stuff counter
			view: 			vBarberInt
			loop: 			1
			cel: 				4
			posn: 			162 93
			setPri: 			1
			ignoreActors:
			addToPic:
		)
		(Load SOUND mBarber)
		(theSound		play:)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/barber, man, woman, person')
				(Print "Nah. You've seen one barber, you've seen 'em all!")
			)

			(if (Said '/chair')
				(Print "It's a barber chair. Try sitting in it.")
			)

			(if (Said '/table')
				(Print "The tables have nothing but old, 
					dirty magazines lying on them.")
			)

			(if (Said '/magazine')
				(Print "Don't look. They're filled with pictures of naked women.")
			)

			(if (Said '/floor')
				(Print "You see lots of hair.")
			)

			(if (Said '/art, girl, calendar, wall')
				(Print "You don't know much about art,
					but you know what you like!")
			)

			(if (Said '/mirror')
				(Print "You see a barber shop.\n\n
							.pohs rebrab a ees uoY")
			)

			(if (Said '[/shop, building, counter, floor, ceiling, room]')
				(Print "Don't all barber shops look the same?")
			)
		)
	)
)
(instance theSound of Sound
	(properties
	 	number:		mBarber
		loop:			1
	)
)



