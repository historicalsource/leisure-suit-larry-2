;**
;**	Logics for room 10 -- copy check
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 22, 1988
;**


(script#				10)

(public
	rm10 				0
)


;(define	vCopyCheck					060)
	(define	lBody				0)
	(define	lFace				1)
	(define	lHair				2)
	(define	lEars				3)

(local
	girl
	body
	face
	hair
	ear
	page
	phone
	room

	testing
	testRoom

	aBody
	aFace
	aHair
	aEar
)



(instance rm10 of Room
	(properties
		picture:		10
		style:		DISSOLVE
	)
	
	(method (init &tmp i)
		(HandsOff)

;**			(= debugging TRUE)
;**			(= testing TRUE)

		(if debugging (= testRoom (GetNumber {Starting Room}))
			(if (<= testRoom 0)
				(= testRoom 90)
			else
				(TheMenuBar draw:)
				(StatusLine enable:)
			)
		)				

		(Load VIEW vCopyCheck)

		(super init:)

		((View new:)							;**	Sierra death-star 
			view: 			vCopyCheck
			ignoreActors:
			loop: 			4 
			cel: 				0 
			posn: 			72 52 
			setPri: 			1
			addToPic:
		)
		((View new:)							;**	S
			view: 			vCopyCheck
			ignoreActors:
			loop: 			4 
			cel: 				1 
			posn: 			122 36 
			setPri: 			0
			addToPic:
		)
		((View new:)							;**	I
			view: 			vCopyCheck
			ignoreActors:
			loop: 			4 
			cel: 				2 
			posn: 			141 35 
			setPri: 			0
			addToPic:
		)
		((View new:)							;**	E
			view: 			vCopyCheck
			ignoreActors:
			loop: 			4 
			cel: 				3 
			posn: 			161 35 
			setPri: 			0
			addToPic:
		)
		((View new:)							;**	R
			view: 			vCopyCheck
			ignoreActors:
			loop: 			4 
			cel: 				4 
			posn: 			190 35 
			setPri: 			0
			addToPic:
		)
		((View new:)							;**	R
			view: 			vCopyCheck
			ignoreActors:
			loop: 			4 
			cel: 				4 
			posn: 			221 35 
			setPri: 			0
			addToPic:
		)
		((View new:)							;**	A
			view: 			vCopyCheck
			ignoreActors:
			loop: 			4 
			cel: 				5 
			posn: 			252 35 
			setPri: 			0
			addToPic:
		)
		((View new:)							;**	left fake picture 
			view: 			vCopyCheck
			ignoreActors:
			loop: 			5 
			cel: 				0 
			posn: 			13 113 
			setPri: 			7
			addToPic:
		)
		((View new:)							;**	right fake picture
			view: 			vCopyCheck
			ignoreActors:
			loop: 			6 
			cel: 				0 
			posn: 			306 113 
			setPri: 			7
			addToPic:
		)

		((= aBody (View new:))
			view: 			vCopyCheck
			loop:				lBody
			cel:				0
			setPri:			1
			posn:				154 981
			init:
		)
		((= aFace (View new:))
			view: 			vCopyCheck
			loop:				lFace
			cel:				5
			setPri:			2
			posn:				154 981
			init:
		)
		((= aHair (View new:))							
			view: 			vCopyCheck
			loop:				lHair
			cel:				1
			setPri:			3
			posn:				154 981
			init:
		)
		((= aEar (View new:))
			view: 			vCopyCheck
			loop:				lEars
			cel:				4
			setPri:			4
			posn:				154 981
			init:
		)

		(= girl (Random 1 16))			;** decide which girl it's going to be:
		(switch girl
			(1	 (= body 0) (= face 5) (= hair 1) (= ear 4) (= page  7) (= phone {555-7448}))
			(2	 (= body 1) (= face 5) (= hair 2) (= ear 4) (= page  6) (= phone {555-5968}))
			(3	 (= body 2) (= face 5) (= hair 3) (= ear 1) (= page  8) (= phone {555-3425}))
			(4	 (= body 1) (= face 2) (= hair 2) (= ear 4) (= page  8) (= phone {555-8487}))
			(5	 (= body 2) (= face 1) (= hair 1) (= ear 2) (= page 11) (= phone {555-3787}))
			(6	 (= body 0) (= face 4) (= hair 2) (= ear 4) (= page  6) (= phone {555-2737}))
			(7	 (= body 0) (= face 3) (= hair 3) (= ear 1) (= page 12) (= phone {555-2867}))
			(8	 (= body 1) (= face 1) (= hair 1) (= ear 4) (= page 11) (= phone {555-3825}))
			(9	 (= body 2) (= face 0) (= hair 0) (= ear 3) (= page 14) (= phone {555-2868}))
			(10 (= body 1) (= face 3) (= hair 2) (= ear 4) (= page 12) (= phone {555-3425}))
			(11 (= body 0) (= face 5) (= hair 0) (= ear 0) (= page  6) (= phone {555-3642}))
			(12 (= body 2) (= face 2) (= hair 2) (= ear 3) (= page  8) (= phone {555-5464}))
			(13 (= body 1) (= face 1) (= hair 3) (= ear 5) (= page 14) (= phone {555-6262}))
			(14 (= body 0) (= face 1) (= hair 1) (= ear 0) (= page 14) (= phone {555-8627}))
			(15 (= body 2) (= face 4) (= hair 0) (= ear 2) (= page  6) (= phone {555-5633}))
			(16 (= body 1) (= face 1) (= hair 3) (= ear 2) (= page 13) (= phone {555-5834}))
		)


		;**	Build the girl from the pre-selected body parts:

		(aBody 
			posn:				154 81
			setLoop:			lBody
			setCel:			body
		)
		(aFace 
			posn:				154 81
			setLoop:			lFace
			setCel:			face
		)
		(aHair 
			posn:				154 81
			setLoop:			lHair
			setCel:			hair
		)
		(aEar 
			posn:				154 81
			setLoop:			lEars
			setCel:			ear
		)

		(RedrawCast)

		(Display "\04 1988 by Sierra On-Line, Inc." 
			p_at:				60 176
			p_color:			1
			p_back:			3
		)
	)

	
	(method (doit &tmp i)
		(if debugging
			(if (!= testRoom 10)
				(self newRoom: testRoom)
				(return)
			)
		)

		
;**			(if debugging
;**				(++ girl)
;**			else
;**				(= girl (Random 1 16))			;** decide which girl it's going to be:
;**			)

;**			(switch girl
;**				(1	 (= body 0) (= face 5) (= hair 1) (= ear 4) (= page  7) (= phone {555-7448}))
;**				(2	 (= body 1) (= face 5) (= hair 2) (= ear 4) (= page  6) (= phone {555-5968}))
;**				(3	 (= body 2) (= face 5) (= hair 3) (= ear 1) (= page  8) (= phone {555-3425}))
;**				(4	 (= body 1) (= face 2) (= hair 2) (= ear 4) (= page  8) (= phone {555-8487}))
;**				(5	 (= body 2) (= face 1) (= hair 1) (= ear 2) (= page 11) (= phone {555-3787}))
;**				(6	 (= body 0) (= face 4) (= hair 2) (= ear 4) (= page  6) (= phone {555-2737}))
;**				(7	 (= body 0) (= face 3) (= hair 3) (= ear 1) (= page 12) (= phone {555-2867}))
;**				(8	 (= body 1) (= face 1) (= hair 1) (= ear 4) (= page 11) (= phone {555-3825}))
;**				(9	 (= body 2) (= face 0) (= hair 0) (= ear 3) (= page 14) (= phone {555-2868}))
;**				(10 (= body 1) (= face 3) (= hair 2) (= ear 4) (= page 12) (= phone {555-3425}))
;**				(11 (= body 0) (= face 5) (= hair 0) (= ear 0) (= page  6) (= phone {555-3642}))
;**				(12 (= body 2) (= face 2) (= hair 2) (= ear 3) (= page  8) (= phone {555-5464}))
;**				(13 (= body 1) (= face 1) (= hair 3) (= ear 5) (= page 14) (= phone {555-6262}))
;**				(14 (= body 0) (= face 1) (= hair 1) (= ear 0) (= page 14) (= phone {555-8627}))
;**				(15 (= body 2) (= face 4) (= hair 0) (= ear 2) (= page  6) (= phone {555-5633}))
;**				(16 (= body 1) (= face 1) (= hair 3) (= ear 2) (= page 13) (= phone {555-5834}))
;**			)


		;**	Build the girl from the pre-selected body parts:

;**			(aBody 
;**				posn:				154 81
;**				setLoop:			lBody
;**				setCel:			body
;**			)
;**			(aFace 
;**				posn:				154 81
;**				setLoop:			lFace
;**				setCel:			face
;**			)
;**			(aHair 
;**				posn:				154 81
;**				setLoop:			lHair
;**				setCel:			hair
;**			)
;**			(aEar 
;**				posn:				154 81
;**				setLoop:			lEars
;**				setCel:			ear
;**			)

;**			(if testing
;**				(Print "Hi, QA-ers!\n\n
;**					1 = opening/LA\n
;**					2 = Love Tub\n
;**					3 = Resort\n
;**					4 = Airport\n
;**					5 = Airplane\n
;**					6 = Island\n
;**					7 = closing"
;**					dispose:
;**					font:		999
;**					at:		203 -1
;**				)
;**	
;**	
;**				(Display (Format @string "Girl %d_" girl) p_at:	40 55 p_color: 0 p_back: 3)
;**				(Display (Format @string "Page %d_" page) p_at:	40 75 p_color: 0 p_back: 3)
;**				(Display (Format @string "%s_" phone)		p_at:	40 95 p_color: 0 p_back: 3)
;**			)
		
;		(RedrawCast)
		(Format @string "555-")
		(Print "Please find this girl's picture in your little black book,
			then type her telephone number here:"
			at: 		-1 144 
			width:	248
			font:		smallFont
			edit: 	@string 11
		)							  
		

		(cls)
		(= i (ReadNumber @string))


		(cond
;**				((and 	i
;**							( < i 8))
;**					(switch i
;**						(1
;**							(= i 90)
;**						)
;**						(2
;**							(= i 28)
;**							(ego
;**								get:	iMoney
;**								get:	iPassport
;**								get:	iOnklunk
;**								get:	iSunscreen
;**								get:	iGulp
;**								get:	iSwimsuit
;**							)
;**						)
;**						(3
;**							(= i 42)
;**							(ego
;**								get:	iMoney
;**								get:	iPassport
;**								get:	iOnklunk
;**								get:	iBikiniTop
;**							)
;**						)
;**						(4
;**							(= i 50)
;**							(= currentEgoView vEgoBlond)
;**							(ego
;**								get:	iMoney
;**								get:	iPassport
;**								get:	iOnklunk
;**								get:	iFlower
;**							)
;**						)
;**						(5
;**							(= i 58)
;**							(ego
;**								get:	iAirlineTicket
;**								get:	iMoney
;**								get:	iPassport
;**								get:	iOnklunk
;**								get:	iPamphlet
;**								get:	iParachute
;**								get:	iBobbyPin
;**							)
;**						)
;**						(6
;**							(= i 70)
;**							(ego
;**								get:	iKnife
;**								get:	iAirsickBag
;**								get:	iHairRejuvenator
;**								get:	iMatches
;**							)
;**						)
;**						(7
;**							(= i 83)
;**						)
;**					)
;**					(TheMenuBar draw:)
;**					(StatusLine enable:)
;**					(curRoom newRoom: i)
;**					; avoid further doits
;**					(return)
;**				)
;**	
;**				((not (StrCmp @string {reebok})) 
;**					(= debugging TRUE)
;**					(curRoom newRoom: 23)
;**				)
			((not (StrCmp @string phone))
				(curRoom newRoom: 90)
			)
			(else				
				(Print "Sorry, but you need to spend more time staring at
					beautiful women!
					In order to play this game, 
					you must have the original documentation.
					If you've lost your little black book,
					please telephone Sierra's Customer Support Department
					at the number printed on your disks.")
				(if testing
					(Print "Well, ok. Since it's you, you get another chance.")
				else
					(= quit 1)
				)
			)
		)
	)
)

