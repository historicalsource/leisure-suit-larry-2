;**
;**	Logics for room 143 -- Resort Restaurant addToPix
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	September 30, 1988
;**


(script#				143)

(public
	rgRestaurant	0
)


;(define	vEgoSitting					101)
;(define	vRestaurantWaiter			432)
	(define	lGetTable		2)
	(define	lGetChair		3)

;(define	vRestaurant					433)	;**	if changes, change rm8 too
	(define	lDoor				1)
	(define	lPaintings		2)
	(define	lTables			3)				;**	cels 0 & 1
	(define	lLarryChair		3)				;**	cel2
	(define	lRope				4)
	(define	lChair			5)
	(define	lTray				6)
	(define	lRopeDone		7)

;(define	vRestaurantMaitreD		434)
	(define	lMovingRope		4)
	(define	lTalk				5)

;(define	vRestaurantMan				435)
;(define	vRestaurantWoman			438)
	(define	lSitting			4)


(procedure (rgRestaurant)
		((View new:)							;**	cheese
			view: 			vRestaurant
			posn: 			22 135
			setPri: 			6
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	salad
			view: 			vRestaurant
			cel: 				1
			posn: 			63 122
			setPri: 			6
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	otherFood
			view: 			vRestaurant
			cel: 				2
			posn: 			37 164
			setPri: 			13
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	T1
			view: 			vRestaurant
			loop: 			lTables
			cel: 				1
			posn: 			116 82
			setPri: 			3
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	T2
			view: 			vRestaurant
			loop: 			lTables
			posn: 			65 120
			setPri: 			7
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	T3
			view: 			vRestaurant
			loop: 			lTables
			cel: 				1
			posn: 			178 101
			setPri: 			5
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	T4
			view: 			vRestaurant
			loop: 			lTables
			cel: 				1
			posn: 			242 101
			setPri: 			5
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	T5
			view: 			vRestaurant
			loop: 			lTables
			posn: 			168 144
			setPri: 			9
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	larryChair
			view: 			vRestaurant
			loop: 			lLarryChair
			cel: 				2
			posn: 			230 172
			setPri: 			13
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	west picture
			view: 			vRestaurant
			loop: 			lPaintings
			cel: 				1
			posn: 			53 78
			setPri: 			4
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	east picture
			view: 			vRestaurant
			loop: 			lPaintings
			posn: 			178 56
			setPri: 			2
			ignoreActors:
			addToPic:
		)
)

