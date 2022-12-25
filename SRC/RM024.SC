;**
;**	Logics for room 24 -- Unreversal Studios
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	September 20, 1988
;**


(script#				24)

(public
	rm24				0
)


;(define	vUniversal					255)
	(define	lMan				0)
	(define	lWoman			1)
	(define	lGroup			2)
	(define	lGrope			3)


(local
	aPeople
)


(enum												;**	detailScript state values
	detailInit
	startMan
	manDone
	startWoman
	womanDone
	startGroup
	groupDone
	startGrope
	gropeDone
)

(enum												;**	rmScript state values
	intro
)


(instance rm24 of Room
	(properties
		picture:		24
		horizon:		5
		north:		20
		east:			25
		west:			23
	)

	(method (init)
		(Load VIEW vUniversal)

		(super init:)

		((= aPeople (Actor new:))
			view:				vUniversal
			setLoop:			0
			setStep:			1 1
			setCycle:		Forward
			illegalBits:	0
			ignoreHorizon:
			ignoreActors:
			posn:				-5 64
			init:
			setScript:		detailScript
		)

		(cond
			((== prevRoomNum	0)	(ego posn: 155 180))
			((== prevRoomNum	20)	(ego posn:	99 111))
		)
		(NormalEgo)
		(ego					init:)

		(self
			setRegions:		rgLA
			setScript: 		rm24Script
		)
	)
)


(instance rm24Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said '/film')
			(Print "From here you can just make out the promo board on the
				latest project now in production:
				\"Debbie Does Oakhurst!
				An extraordinary trip down mammary lane!
				A behind the scenes look when sex was still dirty.\"")
			(Print "(Must be a documentary!)" LOW)
		)

		(if (Said 'look>')
			(if (Said '[/building, unreversal, studio, room]')
				(Print "Why, that must be \"Unreversal Studios!\"")
			)

			(if (Said '/people, man, woman')
				(Print "They're on one of those famous
					\"Unreversal Studios Tours.\"")
			)
		)
	)
)


(instance detailScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(detailInit
				(= cycles 12)
			)

			(startMan
				(aPeople
					setPri: 			RELEASE
					setLoop:			lMan
					setMotion:		MoveTo 190 64 self
				)
			)

			(manDone
				(aPeople	hide:)
				(= seconds (Random 2 9))
			)

			(startWoman
				(aPeople
					setLoop:			lWoman
					posn:				-5 64
					show:
					setMotion:		MoveTo 190 64 self
				)
			)

			(womanDone
				(aPeople	hide:)
				(= seconds (Random 2 9))
			)

			(startGroup
				(aPeople
					setLoop:			lGroup
					posn:				-5 64
					show:
					setMotion:		MoveTo 210 64 self
				)
			)

			(groupDone
				(aPeople	hide:)
				(= seconds (Random 2 9))
			)

			(startGrope
				(aPeople
					setLoop:			lGrope
					posn:				299 104
					setPri: 			11
					show:
				)
				(= seconds (Random 5 15))
			)

			(gropeDone
				(aPeople	hide:)
				(self changeState: detailInit)
			)
		)
	)
)

