;**
;**	Logics for room 901 -- Bill's Palette
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	August 30, 1988
;**


(script#				901)

(public
	rm901				0
)


(define	x1		113)
(define	x2		132)
(define	x3		151)
(define	x4		170)
(define	x5		189)

(define	y1		59)
(define	y2		70)
(define	y3		81)
(define	y4		92)
(define	y5		103)
(define	y6		114)
(define	y7		125)


(local
	aBox
	lclWindow

	boxView
	boxLoop
	boxCel
	boxX
	boxY

	oldBoxX
	oldBoxY
	clickX
	clickY
)


(enum												;**	rmScript state values
	intro
	changeLocation
	changeCel
)


(instance rm901 of Room
	(properties
		picture:		901
		horizon:		1
	)

	(method (init)
		(Load VIEW 901)
		(Load VIEW 902)
		(Load VIEW 903)
		(Load VIEW 904)

		(super init:)

		((View new:) view: 901 loop: 0 cel:	0 posn: x1 y7 addToPic:)
		((View new:) view: 901 loop: 0 cel:	1 posn: x2 y7 addToPic:)
		((View new:) view: 901 loop: 0 cel:	2 posn: x3 y7 addToPic:)
		((View new:) view: 901 loop: 0 cel:	3 posn: x4 y7 addToPic:)
		((View new:) view: 901 loop: 0 cel:	4 posn: x5 y7 addToPic:)
		((View new:) view: 901 loop: 0 cel:	5 posn: x1 y6 addToPic:)
		((View new:) view: 901 loop: 0 cel:	6 posn: x2 y6 addToPic:)
		((View new:) view: 901 loop: 0 cel:	7 posn: x3 y6 addToPic:)
		((View new:) view: 901 loop: 0 cel:	8 posn: x4 y6 addToPic:)
		((View new:) view: 901 loop: 0 cel:	9 posn: x5 y6 addToPic:)
		((View new:) view: 901 loop: 0 cel: 10 posn: x1 y5 addToPic:)
		((View new:) view: 901 loop: 0 cel: 11 posn: x2 y5 addToPic:)
		((View new:) view: 901 loop: 0 cel: 12 posn: x3 y5 addToPic:)
		((View new:) view: 901 loop: 0 cel: 13 posn: x4 y5 addToPic:)
		((View new:) view: 901 loop: 0 cel: 14 posn: x5 y5 addToPic:)
		((View new:) view: 901 loop: 0 cel: 15 posn: x1 y4 addToPic:)
		((View new:) view: 901 loop: 1 cel:	0 posn: x2 y4 addToPic:)
		((View new:) view: 901 loop: 1 cel:	1 posn: x3 y4 addToPic:)
		((View new:) view: 901 loop: 1 cel:	2 posn: x4 y4 addToPic:)
		((View new:) view: 901 loop: 1 cel:	3 posn: x5 y4 addToPic:)
		((View new:) view: 901 loop: 1 cel:	4 posn: x1 y3 addToPic:)
		((View new:) view: 901 loop: 1 cel:	5 posn: x2 y3 addToPic:)
		((View new:) view: 901 loop: 1 cel:	6 posn: x3 y3 addToPic:)
		((View new:) view: 901 loop: 1 cel:	7 posn: x4 y3 addToPic:)
		((View new:) view: 901 loop: 1 cel:	8 posn: x5 y3 addToPic:)
		((View new:) view: 901 loop: 1 cel:	9 posn: x1 y2 addToPic:)
		((View new:) view: 901 loop: 1 cel: 10 posn: x2 y2 addToPic:)
		((View new:) view: 901 loop: 1 cel: 11 posn: x3 y2 addToPic:)
		((View new:) view: 901 loop: 1 cel: 12 posn: x4 y2 addToPic:)
		((View new:) view: 901 loop: 1 cel: 13 posn: x5 y2 addToPic:)
		((View new:) view: 901 loop: 1 cel: 14 posn: x1 y1 addToPic:)
		((View new:) view: 901 loop: 1 cel: 15 posn: x2 y1 addToPic:)
		((View new:) view: 901 loop: 2 cel:	0 posn: x3 y1 addToPic:)
		((View new:) view: 901 loop: 2 cel:	1 posn: x4 y1 addToPic:)
		((View new:) view: 901 loop: 2 cel:	2 posn: x5 y1 addToPic:)

		(= boxX 		0)
		(= boxY 		0)
		(= boxView 901)
		(= boxLoop 	0)
		(= boxCel 	0)
		(= oldBoxX	x1)
		(= oldBoxY	y7)
;**			((= aBox (Actor new:))
		(ego
			view:				boxView
			setLoop:			boxLoop
			setCel:			boxCel
			setPri:			15
			illegalBits:	0
			ignoreActors:
			posn:				oldBoxX oldBoxY
			init:
		)
		(self
			setScript: 		rm901Script
		)
	)
)


(instance rm901Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (== (event type?) direction)
			(event claimed: TRUE)
			(switch (event message?)
				(dirN
					(if (> boxLoop 0)
						(-- boxLoop)
						(self changeState: changeCel)
					)
				)
				(dirNE
					(if (> boxView 901)
						(-- boxView)
						(self changeState: changeCel)
					)
				)
				(dirE
					(if (< boxCel 15)
						(++ boxCel)
						(self changeState: changeCel)
					)
				)
				(dirSE
					(if (< boxView 904)
						(++ boxView)
						(self changeState: changeCel)
					)
				)
				(dirS
					(if ( < boxLoop 3)
						(++ boxLoop)
						(self changeState: changeCel)
					)
				)
				(dirSW
					(if (< boxView 904)
						(++ boxView)
						(self changeState: changeCel)
					)
				)
				(dirW
					(if (> boxCel 0)
						(-- boxCel)
						(self changeState: changeCel)
					)
				)
				(dirNW
					(if (> boxView 901)
						(-- boxView)
						(self changeState: changeCel)
					)
				)
			)
		)

		(if (and (== mouseDown (event type?))
					(not (event claimed?)))
			(event claimed: TRUE)
			(= clickX (event x?))
			(= clickY (event y?))
			(self changeState: changeLocation)
		)

		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(Print	"Click on a color.\n\n
							LEFT decreases cel#\n
							RIGHT increases cel#\n
							UP decreases loop#\n
							DOWN increases loop#\n
							PgUp decreases view#\n
							PgDown increases view#")
				(self changeState: changeCel)
			)

			(changeLocation
				(cond
					((and (> clickX (- x1 10))	(< clickX (+ x1 9)))	(= boxX x1))
					((and (> clickX (- x2 10))	(< clickX (+ x2 9)))	(= boxX x2))
					((and (> clickX (- x3 10))	(< clickX (+ x3 9)))	(= boxX x3))
					((and (> clickX (- x4 10))	(< clickX (+ x4 9)))	(= boxX x4))
					((and (> clickX (- x5 10))	(< clickX (+ x5 9)))	(= boxX x5))
				)

				(cond
					((and (> clickY (- y1 10))	(< clickY (+ y1 1)))	(= boxY y1))
					((and (> clickY (- y2 10))	(< clickY (+ y2 1)))	(= boxY y2))
					((and (> clickY (- y3 10))	(< clickY (+ y3 1)))	(= boxY y3))
					((and (> clickY (- y4 10))	(< clickY (+ y4 1)))	(= boxY y4))
					((and (> clickY (- y5 10))	(< clickY (+ y5 1)))	(= boxY y5))
					((and (> clickY (- y6 10))	(< clickY (+ y6 1)))	(= boxY y6))
					((and (> clickY (- y7 10))	(< clickY (+ y7 1)))	(= boxY y7))
				)

				(if (or boxX boxY)
					((View new:)
						view: 	boxView
						loop: 	boxLoop
						cel:		boxCel
						posn: 	oldBoxX oldBoxY
						ignoreActors:
						addToPic:
					)
					(= oldBoxX boxX)
					(= oldBoxY boxY)
					(ego
						posn:		boxX boxY
					)
					(= boxX 0)
					(= boxY 0)
				)
			)

			(changeCel
				(ego
					view:				boxView
					setLoop:			boxLoop
					setCel:			boxCel
				)
				(if lclWindow
					(lclWindow dispose:)
				)
				(= lclWindow (Print
					(Format @string "%d\ \ %d\ \ %d" boxView boxLoop boxCel)
					back: cyan at: 20 20 dispose:))
			)
		)
	)
)

