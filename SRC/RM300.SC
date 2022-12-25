;**
;**	Logics for room 300 -- the Cruise Ship region
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 19, 1988
;**


(script#				300)

(public
	rm300				0
)


(instance rm300 of Region
	(method (init)
		(super init:)
	)


	(method (doit)
		(super doit:)

		(if (and (== currentTimer	LIFEBOAT_DRILL)
					(not rgTimer)
					(!= curRoomNum 38)
					(!= curRoomNum 31)
					(!= curRoomNum 131)
					(!= curRoomNum 138)		;**	just for safety
					( > secondsInRoom 5)
					(== currentStatus NORMAL))
			(= currentStatus DEAD)
			(= currentTimer FALSE)
			(Print "The ship's PA system crackles to life,
				\"We are sorry, ladies and gentlemen, but due to unknown reasons
				our lifeboats have inadvertently been lowered into the water.
				We regret this inconvenience.\"")
			(Print "You just missed your last chance at escape!")
			(Print "The next time you start something, you'd better be prepared
				to finish it!")
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(cond
			((and (ego has: iFruit)
					(Said 'eat, use/fruit'))
				(Print "Ok. You feel refreshed, but fruitless.")
				(ego put: iFruit LIMBO)
				(theGame changeScore: -2)
			)

			((and (ego has: iSpinachDip)
					(Said 'eat, use/dip'))
				(Print "Ok. You rapidly glom down a quart of spinach dip and
					a whole loaf of sourdough bread.")
				(ego put: iSpinachDip LIMBO)
				(theGame changeScore: -5)
			)

			((and (ego has: iWig)
						(Said 'wear, use, (put<on)/wig'))
				(NotNow)
			)

			((and (ego has: iSewingKit)
						(Said 'sew, repair, use/kit'))
				(Print "You have nothing which needs mending.")
			)

			((and (ego has: iSunscreen)
						(Said 'wear, rub, put, use/sunscreen, (lotion<suntan)'))
				(cond
					((!= currentEgoView vEgoSwimsuit)
						(Print "You apply the sunscreen to your neck and hands.")
						(= wearingSunscreen ON_CLOTHES)
						(if (not scoredWoreSunscreen)
							(= scoredWoreSunscreen TRUE)
							(theGame changeScore: 1)
						)
					)
					((== wearingSunscreen WASHED_OFF)
						(Print "You are clever to reapply the sunscreen after your
							swim washed it off!")
						(= wearingSunscreen TRUE)
						(if (not reappliedSunscreen)
							(= reappliedSunscreen TRUE)
							(theGame changeScore: 3)
						)
					)
					(else
						(Print "Ok. You carefully rub the sunscreen over every exposed
							part of your body.")
						(= wearingSunscreen TRUE)
						(if (not scoredWoreSunscreen)
							(= scoredWoreSunscreen TRUE)
							(theGame changeScore: 3)
						)
					)
				)
			)

			((Said 'look>')
				(cond
					((Said '/window, down, porthole, ocean, water')
						(Print "You look to sea to see what you can see,
							but soon you see that all you can see is sea.")
					)

					((Said '/up, overhead, sky')
						(Print "The bright tropical sun smiles down from an azure sky.
							Ah! There's nothing like a sea voyage to freshen
							the lungs of a lounge lizard!")
					)

					((Said '[/ship, cloud]')
						(Print "Scud scuds past the ship,
							messing up an otherwise clear blue sky.")
					)

					((Said '/people, man, woman')
						(Print "Everyone here ALSO has a better tan than you,
							Larry.")
					)

					((Said '/ground, floor, deck')
						(Print "Sway.")
						(Print "Sway.")
						(Print "Sway.")
					)

					((Said '/tree, bush, plant')
						(Print "Where?")
					)
				)
			)
		)
	)
)

