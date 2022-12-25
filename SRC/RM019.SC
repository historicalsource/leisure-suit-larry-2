;**
;**	Logics for room 19 -- Disneyland
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 25, 1988
;**


(script#				19)

(public
	rm19				0
)


;(define	vDisneyland					254)
	(define	lChairlift			0)
	(define	lMonorail			1)
	(define	lMatterhorn			2)
	(define	lRollerCoaster		3)
	(define	lFlags				4)


(local
	work
	aChairlift
	aMonorail
	aMatterhorn
	aRollerCoaster
	aFlags
)


(enum												;**	monoScript state values
	monoInit
	monoStart
	monoDone
)

(enum												;**	coasterScript state values
	coasterInit
	coasterStart
	coasterDone
)

(enum												;**	rmScript state values
	intro
)


(instance rm19 of Room
	(properties
		picture:		19
		horizon:		106
		north:		15
		south:		23
		west:			23
	)

	(method (init)
		(Load VIEW vDisneyland)

		(super init:)

		((View new:)							;**	Matterhorn
			view: 			vDisneyland
			loop: 			2
			cel: 				0
			posn: 			144 32
			addToPic:
		)

		((= aChairlift (Prop new:))
			view: 			vDisneyland
			setLoop:			0
			posn:				60 16
			setCycle:		Forward
			cycleSpeed:		2
			isExtra:			TRUE
			init:
		)
		((= aRollerCoaster (Prop new:))
			view: 			vDisneyland
			setLoop:			3
			setCel:			LastCel
			posn:				149 14
			init:
			hide:
			setScript:		coasterScript
		)
		((= aFlags (Prop new:))
			view: 			vDisneyland
			setLoop:			4
			posn:				239 15
			setCycle:		Forward
			cycleSpeed:		4
			isExtra:			TRUE
			init:
		)

		((= aMonorail (Actor new:))
			view: 			vDisneyland
			setLoop:			1
			setPri:			2
			setStep:			4 1
			ignoreHorizon: 
			ignoreActors:	
			illegalBits:	0
			posn:				-940 53
			init:
			setScript:		monoScript
		)

		(cond
			((== prevRoomNum	 0)	(ego posn: 318 161))
			((== prevRoomNum	15)	(ego posn: 306 109))
			((== prevRoomNum	20)	(ego posn: 318 161))
			((== prevRoomNum	23)	(ego posn:	 5 188))
		)
		(NormalEgo)
		(ego					init:)

		(self
			setRegions:		rgLA
			setScript: 		rm19Script
		)
	)
)


(instance rm19Script of Script
	(method (doit)
		(super doit:)

		(if (== EAST (ego edgeHit?))
			(if ( < (ego y?) 159)
				(curRoom newRoom: 15)
			else
				(curRoom newRoom: 20)
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look<over, under/wall, fence')
			(Print "You don't have time for that.")
		)

		(if (Said '/wall, fence')
			(Print "That wall is designed to keep undesirables like you out!")
		)

		(if (Said 'look[/room]')
			(Print "Hey! Is that Disneyland?!")
			(Print "Nah! Not in an Al Lowe game!!")
		)
	)
)


(instance monoScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(monoInit
				(= seconds (Random 2 21))
			)

			(monoStart
				(aMonorail
					posn:				-59 53
					setMotion:		MoveTo 380 53 self
				)
			)

			(monoDone
				(aMonorail			
					posn:				-945 53
				)
				(self changeState:monoInit)
			)
		)
	)
)


(instance coasterScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(coasterInit
				(= seconds (Random 2 10))
			)

			(coasterStart
				(aRollerCoaster
					cel:				0
					show:
					setCycle:		EndLoop self
				)
			)

			(coasterDone
				(aRollerCoaster	hide:)
				(self changeState: coasterInit)
			)
		)
	)
)

