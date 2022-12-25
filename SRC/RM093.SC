;**
;**	Logics for room 93 -- Dr. Nonookee setup opening cartoon
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 2, 1988
;**


(script#				93)

(public
	rm93				0
)


;(define	vFortressOpening			816)
	(define	lSparks			0)
	(define	lDial				1)
	(define	lLightsWest		2)
	(define	lLightsEast		3)
	(define	lDoor				4)
	(define	lStuff			5)

;(define	vDrNonookee					818)
	;(loops 0-3 are regular old walking around loops)
	(define	lSitting			4)
	(define	lOnTheThrone	5)

;(define	vFanningHenchette			819)
;(define	vFeedingHenchette			820)
	(define	lFeedEast		0)
	(define	lFeedWest		1)
	(define	lHeadEast		2)
	(define	lHeadWest		3)
	(define	lDr'sBody		4)

;(define	mFortress					110)


(local
	work
	aGenerator
	aRadio
	aRightPanel1
	aRightPanel2
	aLeftPanel1
	aLeftPanel2
	aDoor
	aFanEast
	aFanWest
	aGrapeEast
	aGrapeWest
)


(enum												;**	rmScript state values
	intro
	closeTheDoor
	path1
	wait1
	path2
	wait2
	path3
	path4
	path5
	wait3
	path6
	path7
	path8
	path9
	wait4
	sit
	sitDone

	orderGrapes
	eatEast
	headEast
	chewEast
	eatEastDone
	eatWest
	headWest
	chewWest
	eatWestDone
	nextRoom
)


(instance theSound of Sound
	(properties
		number:			mFortress
		loop:				-1
		priority:		30
	)
)


(instance rm93 of Room
	(properties
		picture:		93
		style:		(+ IRISOUT BLACK)
	)

	(method (init)
		(Load VIEW vFortressOpening)
		(Load VIEW vDrNonookee)
		(Load VIEW vFanningHenchette)
		(Load VIEW vFeedingHenchette)
		(Load SOUND mFortress)

		(super init:)

		(theSound play:)

		((View new:)							;**	photo on wall
			view: 			vFortressOpening
			loop: 			lStuff
			cel: 				0
			posn: 			39 86
			addToPic:
		)
		((View new:)							;**	greenTurbine1
			view: 			vFortressOpening
			ignoreActors:
			loop: 			lStuff
			cel: 				1
			posn: 			251 108
			setPri: 			10
			addToPic:
		)
		((View new:)							;**	greenTurbine2
			view: 			vFortressOpening
			loop: 			lStuff
			cel: 				2
			posn: 			291 108
			setPri: 			10
			addToPic:
		)
		((View new:)							;**	microphone
			view: 			vFortressOpening
			loop: 			lStuff
			cel: 				3
			posn: 			253 74
			setPri: 			magenta
			addToPic:
		)
		((View new:)							;**	detail1
			view: 			vFortressOpening
			ignoreActors:
			loop: 			lStuff
			cel: 				4
			posn: 			23 140
			setPri: 			15
			addToPic:
		)
		((View new:)							;**	detail2
			view: 			vFortressOpening
			ignoreActors:
			loop: 			lStuff
			cel: 				5
			posn: 			294 140
			setPri: 			15
			addToPic:
		)

		((= aGenerator (Extra new:))		;**	generator
			view:				vFortressOpening
			setLoop: 		lSparks
			posn: 			258 102
			setPri: 			7
			init:
			cycleSpeed:		0
			setCel:			0
		)
		((= aRadio (Extra new:))			;**	radioView
			view:				vFortressOpening
			setLoop:			lDial
			posn: 			263 76
			setPri:			4
			init:
			cycleSpeed:		0
		)
		((= aRightPanel1 (Extra new:))	;**	rightPanel
			view:				vFortressOpening
			ignoreActors:
			setLoop: 		lLightsEast
			posn: 			267 164
			setPri: 			15
			pauseCel:		-1
			init:
			cycleSpeed:		0
		)
		((= aRightPanel2 (Extra new:))	;**	rightPanel
			view: 			vFortressOpening
			ignoreActors:
			loop: 			lLightsEast
			posn: 			283 152
			setPri: 			15
			pauseCel:		-1
			init:
			cycleSpeed:		1
		)
		((= aLeftPanel1 (Extra new:))		;**	leftPanel
			view: 			vFortressOpening
			ignoreActors:
			loop: 			lLightsWest
			cel: 				1
			posn: 			35 152
			setPri: 			15
			pauseCel:		-1
			init:
			cycleSpeed:		1
		)
		((= aLeftPanel2 (Extra new:))		;**	leftPanel
			view:				vFortressOpening
			ignoreActors:
			setLoop: 		lLightsWest
			posn: 			50 164
			setPri: 			15
			pauseCel:		-1
			init:
			cycleSpeed:		1
		)
		((= aDoor (Prop new:))
			view:				vFortressOpening
			loop:				lDoor
			setCel:			LastCel
			setPri:			magenta
			posn:				96 85
			stopUpd:
			init:
		)

		((= aGrapeWest (Prop new:))
			view:				vFeedingHenchette
			ignoreActors:
			setLoop:			lFeedEast
			setCycle:		Forward
			setPri:			10
			posn:				142 99
			cycleSpeed:		1
			stopUpd:
			init:
		)
		((= aGrapeEast (Prop new:))
			view:				vFeedingHenchette
			ignoreActors:
			setLoop:			lFeedWest
			setCycle:		Forward
			setPri:			10
			posn:				179 99
			cycleSpeed:		1
			stopUpd:
			init:
		)

		((= aFanWest (Prop new:))
			view:				vFanningHenchette
			ignoreActors:
			setLoop:			facingEast
			posn:				132 85
			setCycle:		Forward
			cycleSpeed:		2
			stopUpd:
			init:
		)
		((= aFanEast (Prop new:))
			view:				vFanningHenchette
			ignoreActors:
			setLoop:			facingWest
			posn:				191 85
			setCycle:		Forward
			cycleSpeed:		3
			stopUpd:
			init:
		)

		(NormalEgo facingSouth)
		(ego
			view:			vDrNonookee
			posn:			84 83
			init:
		)
		(self	setScript: rm93Script)
	)
)


(instance rm93Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (not (event claimed?))
			(theGame restart:)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(ego
					setMotion:	MoveTo 86 127 self
				)
				(= cycles 10)
			)

			(closeTheDoor
				(aDoor
					setCycle:	BegLoop
				)
			)

			(path1
				(aDoor			stopUpd:)
				(ego
					setMotion:	MoveTo 66 160 self
				)
			)

			(wait1
				(Print "\"Where is that woman?
					Why is she so slow?\" shouts Dr. Nonookee.
					\"She should be here by now!\"" BIG time: 10)
				(= cycles 30)
			)

			(path2
				(ego
					setMotion:	MoveTo 249 160 self
				)
			)

			(wait2
				(Print "\"Sir,\" crackles the radio,
					\"shipment incoming!\"" BIG time: 5)
				(Print "\"Ah, ha! Here she is. That's more like it!\" he says."
					HIGH time: 5)
				(= cycles 30)
			)

			(path3
				(ego
					setMotion:	MoveTo 225 160 self
				)
			)

			(path4
				(ego
					setMotion:	MoveTo 225	97 self
				)
			)

			(path5
				(ego
					setMotion:	MoveTo 243	93 self
				)
			)

			(wait3
				(Print "\"Calling LA. Calling LA.\" he says into the microphone."
					HIGH time: 5)
				(Print "\"Yes, sir,\" responds a female voice on the radio."
					HIGH time: 4)
				(Print "\"Is everything in place for the transfer?\" he asks."
					BIG time: 5)
				(Print "\"Everything!\" she snaps back."
					HIGH time: 4)
				(Print "He smiles a broad smile." HIGH time: 4)
				(Print "\"Excellent, LA. Keep me informed,\" he concludes,
					\"and remember: no mistakes!\""
					BIG time: 10)
				(= cycles 30)
			)

			(path6
				(ego
					setMotion:	MoveTo 219	99 self
				)
			)

			(path7
				(ego
					setMotion:	MoveTo 219 135 self
				)
			)

			(path8
				(ego
					setMotion:	MoveTo 161 135 self
				)
			)

			(path9
				(ego
					setMotion:	MoveTo 161	98 self
				)
			)

			(wait4
				(Print "Evidently the bad Doctor is planning something to do
					with Los Angeles and some sort of transfer.
					What could it be?" BIG time: 8)
				(= cycles 6)
			)

			(sit
				(ego
					illegalBits:0
					ignoreActors:
					setLoop: 	lSitting
					posn: 		160 101
					cycleSpeed:	2
					cel: 			0
					setCycle:	EndLoop self
				)
			)

			(sitDone
				(Print "\"FAN ME,\" he shouts, \"AND NOW!\"" HIGH time: 4 draw:)
				((View new:)
					view: 			vFeedingHenchette
					ignoreActors:
					loop: 			lDr'sBody
					posn: 			161 97
					setPri: 			6
					addToPic:
				)
				(ego
					view: 			vFeedingHenchette
					setLoop:			lHeadEast
					setPri:			8
					posn:				161 72
					cycleSpeed:		1
				)
				(aFanEast
					setCycle:		Forward
				)
				(aFanWest
					setCycle:		Forward
				)
				(= cycles 30)
			)

			(orderGrapes
				(Print "\"Now, feed me grapes,\" he orders with a sinister smile,
					\"and keep 'em coming!\"" BIG time: 5)
				(= cycles 10)
			)

			(eatEast
				(aGrapeEast
					setCycle:		CycleTo 4 FORWARD self
				)
			)

			(headEast
				(ego 			setCycle:	EndLoop)
				(aGrapeEast setCycle:	EndLoop self)
			)

			(chewEast
				(ego 			setCycle:	BegLoop)
				(aGrapeEast setCycle:	BegLoop self)
			)

			(eatEastDone
				(= cycles 10)
			)

			(eatWest
				(aGrapeWest
					setCycle:		CycleTo 4 FORWARD self
				)
			)

			(headWest
				(ego
					setLoop:		lHeadWest
					setCycle:	EndLoop
				)
				(aGrapeWest setCycle:	EndLoop self)
			)

			(chewWest
				(ego 			setCycle:	BegLoop)
				(aGrapeWest setCycle:	BegLoop self)
			)

			(eatWestDone
				(= cycles 30)
			)

			(nextRoom
				(Print "Gosh, Larry;
					let's hope you never end up on the bad side of this character!"
					BIG time: 6)
				(curRoom newRoom: 90)
			)
		)
	)
)

