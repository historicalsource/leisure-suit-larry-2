;**
;**	Logics for room 178 -- witch doctor Barber Shop
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 2, 1988
;**


(script#				178)

(public
	rm178				0
)


;(define	vEgoBarberIsland			196)
	(define	lSmileWag		8)

;(define	vBarberInt					232)	;**	various addToPic of shop interior
;(define	vWitchDoctor				711)
;(define	vIslandBarber				712)
;(define	vWitchDoctorTreating		713)
;(define	vIslandHaircutThoughts	717)
	(define	lBalloon				0)
	(define	lCoupleIn			1)
	(define	lCoupleOut			2)
	(define	lMirror				3)
		(define	cMirror		0)
		(define	cHandle		1)
		(define	cLarry		2)


(local
	work
	aBarber
	aChair
	aThoughtBalloon
	aThought
	aHandle
)


(enum												;**	rmScript state values
	intro
	dontAll
	startDoc
	barberSitsDown
	doc2
	doc3
	rollBowl
	comeHere
	moveLarry
	faceSouth
	turnUpsideDown
	dunkin
	backToFeet
	toBunny
	isAbunny
	toTiny
	toDonkey
	buckAwhile
	smile
	smileAwhile
	smile2
	buckAwhile2
	isTiny2
	toTiny2
	isAbunny2
	toBunny2
	whew
	barberShowsMirror
	youDidIt
	removeMirror
	great
	doBubble
	growBubble
	shrinkBubble
	coupleDone
)


(instance rm178 of Room
	(properties
		picture:		125
	)

	(method (init)
		(Load VIEW vEgoBarberIsland)
		(Load VIEW vWitchDoctor)
		(Load VIEW vIslandBarber)
		(Load VIEW vWitchDoctorTreating)
		(Load VIEW vBarberInt)
		(Load PICTURE 178)

		(super init:)

		((= aThoughtBalloon (Prop new:))
			view:				vIslandHaircutThoughts
			setLoop:			3
			setCel:			0
			setPri:			14
			ignoreActors:
			posn:				160 1128
			setPri:			14
			init:
		)
		((= aHandle (View new:))
			view:				vIslandHaircutThoughts
			setLoop:			3
			setCel:			1
			ignoreActors:
			posn:				160 1129
			setPri:			14
			init:
		)
		((= aThought (Prop new:))
			view:				vIslandHaircutThoughts
			setLoop:			3
			setCel:			2
			ignoreActors:
			posn:				161 1120
			setPri:			15
			init:
		)
		((= aChair (Prop new:))
			view: 			vBarberInt
			loop: 			1
			cel: 				0
			posn: 			164 119
			setPri: 			8
			ignoreActors:
			stopUpd:
			init:
		)

		((= aBarber (Actor new:))
			view:				vWitchDoctor
			loop:				facingNorth
			setCycle:		Walk
			illegalBits:	0
			posn:				151 162
			ignoreActors:
			init:
		)

		(ego
			posn:				171 162
			illegalBits:	0
			ignoreActors:
			init:
		)

		(self
			setRegions:		rgBarber
			setScript: 		rm178Script
		)
		(HandsOff)
	)
)


(instance rm178Script of Script
	(method (doit)
		(super doit:)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(ego
					setMotion:		MoveTo 181 132
				)
				(aBarber
					setMotion:		MoveTo 159 122 self
				)
			)

			(dontAll
				(ego loop: facingNorth)
				(Print "(Don't all witch doctor huts look alike?)" LOW draw:)
				(= seconds 3)
			)

			(startDoc
				(Print "\"Just a moment, Larry, while I ready this special
					preparation!\"")
				(aBarber
					view:				vWitchDoctorTreating
					setCycle:		Forward
				)
				(= cycles 50)
			)

			(barberSitsDown
				(aChair							;**	the chair is now the barber!
					view:				vIslandBarber
					setLoop:			0
					setCycle:		Forward
				)
				(aBarber
					view:				vIslandBarber
					posn:				157 1120
				)
				(= cycles 50)
			)

			(doc2
				(aChair
					setLoop:			1
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(doc3
				(aBarber							;**	Now becomes the ball.
					view:				vIslandBarber
					setLoop:			2
					posn:				157 122
					cel:				0
					setCycle:		EndLoop self
					show:
				)
			)

			(rollBowl
				((View new:)					;**	add the ball of crap to floor
					view:				vIslandBarber
					ignoreActors:
					posn:				162 112
					loop:				2
					cel:				2
					setPri:			ltBlack
					addToPic:
				)
				(aBarber
					setLoop:			3
					setStep:			3 3
					posn:				144 136
					setMotion:		MoveTo 130 159 self
					forceUpd:
				)
			)

			(comeHere
				(Print "\"All right, son; come on over here.\"" draw:)
				(= cycles 10)
			)

			(moveLarry
				(ego
					setMotion:		MoveTo 157 122 self
				)
			)

			(faceSouth
				(ego
					setLoop:			facingSouth
				)
				(= cycles 10)
			)

			(turnUpsideDown
				(aChair
					view: 			vBarberInt
					loop: 			1
					cel: 				0
					posn: 			164 119
					show:
				)
				(ego
					view:				vEgoBarberIsland
					posn: 			157 121
					setLoop:			0
					cel:				0
					cycleSpeed:		1
					setCycle:		EndLoop self
				)
			)

			(dunkin
				(aChair				stopUpd:)
				(ego
					setLoop:			1
					setCycle:		Forward
				)
				(= cycles 20)
			)

			(backToFeet
				(ego
					setLoop:			0
					setCel:			LastCel
					setCycle:		BegLoop self
				)
			)

			(toBunny
				(aBarber
					view:				vWitchDoctor
					setLoop:			facingSouth
					posn: 			159 122
				)
				(ego
					setLoop:			2
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(isAbunny
				(aBarber				stopUpd:)
				(ego
					setLoop:			3
					cel:				0
					setCycle:		Forward
				)
				(= cycles 30)
			)

			(toTiny
				(ego
					setLoop:			4
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(toDonkey
				(ego
					setLoop:			5
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(buckAwhile
				(ego
					setLoop:			6
					cel:				0
					setCycle:		Forward
				)
				(= cycles 32)
			)

			(smile
				(ego
					setLoop:			7
					cycleSpeed:		1
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(smileAwhile						;**	smile and wag
				(ego
					setLoop:			8
					setCycle:		Forward
				)
				(= cycles 30)
			)

			(smile2
				(ego
					setLoop:			7
					setCel:			LastCel
					setCycle:		BegLoop self
				)
			)

			(buckAwhile2
				(ego
					setLoop:			6
					cycleSpeed:		0
					cel:				0
					setCycle:		Forward
				)
				(= cycles 30)
			)

			(isTiny2
				(ego
					setLoop:			5
					setCel:			LastCel
					setCycle:		BegLoop self
				)
			)

			(toTiny2
				(ego
					setLoop:			4
					setCel:			LastCel
					setCycle:		BegLoop self
				)
			)

			(isAbunny2
				(ego
					setLoop:			3
					cel:				0
					setCycle:		Forward
				)
				(= cycles 30)
			)

			(toBunny2
				(ego
					setLoop:			2
					setCel:			LastCel
					setCycle:		BegLoop self
				)
			)

			(whew
				(aBarber				hide:)
				(ego
					setLoop:			9
				)
				(Print "\"Geez,\" you exclaim,
					\"that's some powerful stuff!\"" HIGH draw:)
				(= seconds 3)
			)

			(barberShowsMirror
				(Print "\"Here,\" says the witch doctor,
					\"take a look in this mirror and tell me what you think.\"")
				(ego					stopUpd:)
				(aThoughtBalloon
					posn:				160 128
					stopUpd:
				)
				(aHandle
					posn:				160 129
					stopUpd:
				)
				(aThought
					posn:				161 120
				)
				(= seconds 2)
			)

			(youDidIt
				(Print "\"YOU DID IT! YOU REALLY DID IT!!\"" HIGH)
				(= seconds 5)
			)

			(removeMirror
				(aThoughtBalloon
					posn:				160 1128
				)
				(aHandle
					posn:				160 1229
				)
				(aThought
					posn:				161 1320
				)
				(= seconds 3)
			)

			(great
				(Print "\"This is great!
					I look like a young man again;
					I'm stranded in paradise, broke but happy;
					and the most beautiful woman I've ever seen is
					madly in love with me!!\"")
				(Print "\"My dreams have all come true!\"")
				(= seconds 3)
			)

			(doBubble
				(aThoughtBalloon
					setLoop:			0
					cel:				0
					posn:				154 92
					cycleSpeed:		3
					setCycle:		EndLoop self
				)
			)

			(growBubble
				(aThoughtBalloon
					stopUpd:
				)
				(aThought
					posn:				115 48
					cycleSpeed:		3
					setLoop: 		1
					cel: 				0
					setCycle:		EndLoop self
				)
			)

			(shrinkBubble
				(aThoughtBalloon
					posn:				154 1092
				)
				(aThought
					posn:				154 92
					setLoop:			2
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(coupleDone
				(curRoom drawPic: 178 (+ IRISOUT BLACKOUT))
				(aChair	posn:		999 999)
				(ego 		posn:		999 999)
				(aThought			forceUpd:)
				(curRoom newRoom: 86)		;**	This is it!
			)
		)
	)
)

