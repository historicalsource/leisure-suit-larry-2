;**
;**	Logics for room 56 -- riding the airport moving sidewalk
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 3, 1988
;**


(script#				56)

(public
	rm56				0
)


(define	lineStartY				169)
(define	lineStopY					3)

(define	eastLineStartX			95)
(define	eastLineStopX			204)
(define	westLineStartX			78)
(define	westLineStopX			-58)

;(define	vTunnel						522)
	(define	lLine				0)
		(define	cEast		0)
		(define	cWest		1)
	(define	lMind				1)


(local
	aMind
	lineNum										;**	line counter
	lineMax										;**	max number lines for this machine
	lineMaxY										;**	Pixels of vertical skylight
	eastLineMaxX
	westLineMaxX
)


(enum												;**	rmScript state values
	intro
	sureLong
	sureIs
	showMind
	removeMind
	sidewalkDone
)

(enum
	lineIntro
	moveLine
	repoLine
)


(instance rm56 of Room
	(properties
		picture:		56
		horizon:		1
	)

	(method (init)
		(Load VIEW vTunnel)

		(super init:)

		(= lineMaxY			(- lineStartY lineStopY))
		(= eastLineMaxX	(- eastLineStopX	eastLineStartX))
		(= westLineMaxX	(- westLineStartX westLineStopX))

		(cond
			(( > machineSpeed 60)	(= lineMax 4))
			(( > machineSpeed 40)	(= lineMax 3))
			(( > machineSpeed 20)	(= lineMax 2))
			(else							(= lineMax 1))
		)	
		(for ((= lineNum 1)) ( <= lineNum lineMax) (( ++ lineNum))
			((Actor new:)
				setScript:		(eastLineScript new:)
			)
			((Actor new:)
				setScript:		(westLineScript new:)
			)
		)

		((= aMind (Actor new:))
			view:				vTunnel
			setLoop:			lMind
			setPri:			14
			setStep:			4 4
			illegalBits:	cRed
			posn:				175 1042
			ignoreActors:
			init:
		)

		(HandsOff)
		(= currentStatus RIDING_SIDEWALK)
		(self
			setRegions:		rgAirport
			setScript: 		rm56Script
		)
	)
)


(instance rm56Script of Script
	(method (doit)
		(super doit:)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(= seconds 10)
			)

			(sureLong
				(Print "This is certainly a long terminal.")
				(= seconds 10)
			)

			(sureIs
				(Print "Your mind wanders...")
				(= seconds 3)
			)

			(showMind
				(aMind
					posn:			175 42
					setMotion:	Wander 99
				)
				(= seconds 10)
			)

			(removeMind
				(aMind			
					ignoreControl: cRed
					setMotion:		MoveTo (aMind x?) -20 
				)
				(Print "Previously,
					you believed that was just a figure of speech!" draw:)
				(= seconds 10)
			)

			(sidewalkDone
				(Print "Finally, you reach the end of the moving sidewalk,
					and enter the waiting room for Gate #1.")
				(curRoom newRoom: 57)
			)
		)
	)
)


(instance eastLineScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(lineIntro
				(client
					view:				vTunnel
					setLoop:			lLine
					setCel:			cEast
					setPri:			5
					setStep:			1 1
					illegalBits:	0
					ignoreHorizon:
					ignoreActors:
					posn:				(- eastLineStopX (* lineNum (/ eastLineMaxX lineMax)))
										(+ lineStopY (* lineNum (/ lineMaxY lineMax)))
					init:
				)
				(self changeState: moveLine)
			)

			(moveLine
				(client
					setMotion:	MoveTo eastLineStopX lineStopY self
				)
			)

			(repoLine
				(client
					posn:			eastLineStartX lineStartY
				)
				(self changeState: moveLine)
			)
		)
	)
)


(instance westLineScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(lineIntro
				(client
					view:				vTunnel
					setLoop:			lLine
					setCel:			cWest
					setPri:			5
					setStep:			1 1
					illegalBits:	0
					ignoreHorizon:
					ignoreActors:
					posn:				(+ westLineStopX (* lineNum (/ westLineMaxX lineMax)))
										(+ lineStopY (* lineNum (/ lineMaxY lineMax)))
					init:
				)
				(self changeState: moveLine)
			)

			(moveLine
				(client
					setMotion:	MoveTo westLineStopX lineStopY self
				)
			)

			(repoLine
				(client
					posn:			westLineStartX lineStartY
				)
				(self changeState: moveLine)
			)
		)
	)
)

