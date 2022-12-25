;**
;**	Logics for room 11 -- outside the tv studio:	KROD (get it?)
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 25, 1988
;**


(script#				11)

(public
	rm11				0
)


;(define	vTVext						200)
;(define	vShowBizGuy					204)
;(define	vShowBizDoll				205)


(local
	aDoor
	aShowBizType
	manOnScreen
)


(enum												;**	rm11Script state values
	intro
	moveGuy
	hideGuy
	moveDoll
	hideDoll
)


(instance rm11 of Room
	(properties
		picture:		11
		horizon:		2
		south:		15
		east:			12
		west:			15
	)

	(method (init)
		(Load VIEW vTVext)
		(Load VIEW vShowBizGuy)
		(Load VIEW vShowBizDoll)

		(super init:)

		((View new:)							;**	Girl behind desk
			view: 			vTVext
			loop: 			1
			cel: 				0
			posn: 			160 31
			setPri: 			1
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	studio sign
			view: 			vTVext
			loop: 			1
			cel: 				1
			posn: 			283 147
			setPri: 			10
			ignoreActors:
			addToPic:
		)

		((= aShowBizType (Actor new:))
			view:				vShowBizGuy
			setLoop:			facingEast
			posn:				-20 133
			illegalBits:	0
			setPri:			3
			setCycle:		Walk
			ignoreActors:
			init:
		)

		(cond
			((== prevRoomNum 101)	(ego posn: 161	 61))
			((== prevRoomNum	12)	(ego posn: 316 169))
			(else							(ego posn:	21 188))
		)
		(NormalEgo)
		(ego					init:)

		((= aDoor (AutoDoor new:))
			view:				vTVext
			setLoop:			0
			posn:				161 63
			setPri:			2
			entranceTo:		101
			msgLook:			"Inside, a woman sits behind a desk."
			init:
		)

		(self
			setRegions:		rgLA
			setScript:		rm11Script
		)
	)
)


(instance rm11Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/sign')
				(Print "A sophisticated sign identifies this as the home of
					KROD Television.")
			)

			(if (Said '/antenna, dish')
				(Print "You presume the large dish behind the studio is used
					to transmit their programming to a satellite
					in geosynchronous orbit, but you've been wrong before!")
			)

			(if (Said '/man, woman, people')
				(if manOnScreen
					(Print "Is that someone famous?")
				else
					(Print "Where?")
				)
			)

			(if (Said '[/studio, building, tv, station, (station<tv), room]')
				(Print "KROD originates many of your favorite television
					shows, including the big \"Lucky Life Lottery.\""
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(= seconds (Random 3 13))
			)

			(moveGuy
				(= manOnScreen TRUE)
				(aShowBizType
					view:				vShowBizGuy
					setLoop:			facingEast
					setMotion: 		MoveTo 340 133 self
				)
			)

			(hideGuy
				(= manOnScreen FALSE)
				(= seconds (Random 1 11))
			)

			(moveDoll
				(= manOnScreen TRUE)
				(aShowBizType					;**	Now he becomes a gal.
					view:				vShowBizDoll
					setLoop:			facingWest
					setMotion: 		MoveTo -20 133 self
				)
			)

			(hideDoll
				(self changeState: intro)
			)
		)
	)
)

