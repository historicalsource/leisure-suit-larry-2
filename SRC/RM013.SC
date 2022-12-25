;**
;**	Logics for room 13 -- non-Dark Alley
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 19, 1988
;**


(script#				13)

(public
	rm13				0
)


(define	pissArea 		254 112 258 115)
(define	nearKnothole	192  96 203 108)
(define	nearDumpster	230 115 284 137)


;(define	vLightAlley					242)
	(define	lSign			0)
	(define	lHead			1)
	(define	lPuddle		2)


(local
	work
	beenInPissArea
	tookApiss
	aHead
	aPuddle
)


(enum												;**	headScript and jointScript values
	intro

	headInit
	drawHead
	eraseHead
	jointInit
	drawJoint
	eraseJoint
)

(enum												;**	pissScript state values
	pissInit
	pissDone
)


(instance rm13 of Room
	(properties
		picture:		13
		horizon:		50
		south:		17
		east:			14
		west:			12
	)

	(method (init)
		(Load VIEW vLightAlley)

		(super init:)

		((View new:)							;**	liquor sign
			view: 			vLightAlley
			loop: 			lSign
			cel: 				0
			posn: 			276 35
			setPri: 			1
			ignoreActors:
			addToPic:
		)

		(self
			setRegions:		rgLA
			setScript:		rm13Script
		)

		((= aPuddle (Prop new:))
			view:				vLightAlley
			setLoop:			lPuddle
			setCel:			0
			setPri:			brown
			cycleSpeed:		3
			posn:				243 1118
			ignoreActors:
			init:
		)

		(if (== 3 (Random 1 3))				;**	Head behind fence this time?
			(rm13Script changeState: headInit)
			((= aHead (Prop new:))
				view:				vLightAlley
				setLoop:			lHead
				setCel:			0
				setPri:			cyan
				posn:				198 1074
				cycleSpeed:		1
				init:
			)
		)

		(cond
			((== prevRoomNum	 0)		(ego posn: 198 188))
			((== prevRoomNum	17)		(ego posn: 198 188))
		)
		(NormalEgo)
		(ego					init:)
	)
)


(instance rm13Script of Script
	(method (doit)
		(super doit:)

		(if (and	(ego inRect: pissArea)
					(not tookApiss)
					(== (ego loop?) facingEast))
			(= tookApiss TRUE)
			(ego setScript: pissScript)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))
		(if (Said '(look<in), open, lift/lid, can, trash')
			(if (not (ego inRect: nearDumpster))
				(NotClose)
			else
				(Print "You quickly open the lid, search through all the rubbish,
					find nothing, and close the lid.")
				(Print "(What did you expect? A passport?)" LOW)
			)
		)

		(if (Said 'look/can, trash')
			(Print "There are insults written in many languages on the side
				of the garbage bin.")
		)

		(if (Said 'look/fence')
			(Print "There is nothing interesting about that fence.")
			(Print "(Unless you find that knothole interesting.)" LOW)
		)

		(if (or	(Said 'look<in, through/fence')
					(Said 'look/hole'))
			(if (not (ego inRect: nearKnothole))
				(NotClose)
			else
				(Print "You bend over and peer through the knothole in the fence.
					On the other side, you see people playing \"Police Quest!\"
					How you wish you were one of them!")
				(if (not scoredKnothole)
					(= scoredKnothole TRUE)
					(theGame changeScore: 1)
				)
			)
		)

		(if (or	(Said '//hole, fence')
					(Said '/hole, fence'))
			(Print "Stop spending so much time worrying about holes and
				get on with the game!")
		)

		(if (Said 'look>')
			(if (Said '/graffiti')
				(Print "It says a lot of dirty stuff.")
			)

			(if (Said '/sign')
				(Print "\"LIQUOR\"")
			)

			(if (Said '[/can, alley, garbage, building, room]')
				(Print "This looks exactly like the dark alley in
					\"Leisure Suit Larry in the Land of the Lounge Lizards.\"")
				(Print "(But where's your friend?)")
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(intro)

			(headInit
				(= seconds (Random 10 20))
			)
			(drawHead
				(aHead
					posn:			198 74
					setCycle:	EndLoop self
				)
			)
			(eraseHead
				(aHead
					setCel:		0
					posn:			198 1074
				)
				(= state intro)
				(= seconds (Random 30 90))
			)
		)
	)
)


(instance pissScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(pissInit
				(HandsOff)
				(aPuddle
					posn:				243 118
					setCycle:		EndLoop self
				)
			)

			(pissDone
				(NormalEgo)
				(aPuddle dispose:)
				(Print "Ahhh." draw:)
			)
		)
	)
)

