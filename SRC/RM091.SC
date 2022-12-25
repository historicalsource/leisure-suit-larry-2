;**
;**	Logics for room 91 -- Eve dumps Larry opening cartoon
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 1, 1988
;**


(script#				91)

(public
	rm91				0
)


;(define	vEgoMowing					192)
;(define	vGarage						253)
;(define	vEve'sCar					802)
	(define	lCar				0)
	(define	lMouth			1)

;(define	vDog							803)
;(define	vDogPissing					804)
	; loops 0 and 1 are Walking east and west respectively
	(define	lPissingEast	2)
	(define	lPissingWest	3)

;(define	mTitle						101)


(local
	work
	aMouth
	aCar
	aDog
)


(enum												;**	rmScript state values
	intro
	startMow2
	startMow3
	startCar
	carDone
	eveBaby
	whoRU
	it'sMe
	vaguely
	moveIn
	dogJumps
	dogOutOfCar
	familiar
	Bback
	dogAtEgo
	dogPissing
	dogDone
	badLuck
	whatWill
)


(instance theSound of Sound
	(properties
		number:			mTitle
		loop:				-1
		priority:		10
	)
)


(instance rm91 of Room
	(properties
		picture:		23
		style:		(+ IRISOUT BLACK)
	)

	(method (init)
		(Load VIEW vEgoMowing)
		(Load VIEW vGarage)
		(Load VIEW vEve'sCar)
		(Load VIEW vDog)
		(Load VIEW vDogPissing)
		(Load SOUND mTitle)

		(super init:)

		(theSound	play:)

		((View new:)							;**	garage inside wall
			view: 			vGarage
			loop: 			0
			cel: 				0
			posn: 			95 131
			setPri: 			1
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	flowerPot1 on right
			view: 			vGarage
			loop: 			0
			cel: 				2
			posn: 			136 140
			setPri: 			9
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	flowerPot2
			view: 			vGarage
			loop: 			0
			cel: 				2
			posn: 			49 139
			setPri: 			9
			addToPic:
		)
		((View new:)							;**	flowerPot3
			view: 			vGarage
			loop: 			0
			cel: 				2
			posn: 			35 144
			setPri: 			9
			addToPic:
		)
		((View new:)							;**	streetSign
			view: 			vGarage
			loop: 			0
			cel: 				3
			posn: 			149 111
			setPri: 			13
			addToPic:
		)
		((View new:)						;**	left garage door is always down
			view: 			vGarage
			loop: 			0
			cel: 				1
			posn: 			15 136
			setPri: 			1
			addToPic:
		)

		((= aMouth (Prop new:))
			view:				vEve'sCar
			ignoreActors:
			setLoop:			lMouth
			setCel:			0
			setPri:			15
			posn:				3 1152
			setCycle:		Forward
			init:
		)

		((= aCar (Actor new:))
			view:				vEve'sCar
			ignoreActors:
			illegalBits:	0
			setLoop:			lCar
			setCel:			0
			setPri:			13
			posn:				(-	0 (* 8 8))	;**		0 is MoveTo x, 8 is xStep
								(+ 183 (* 8 5))	;**	183 is MoveTo y, 5 is yStep
			init:
		)
		((= aDog (Actor new:))
			view:				vDog
			ignoreActors:
			illegalBits:	0
			setLoop:			0
			setCel:			0
			setPri:			14
			posn:				31 151
			init:
			hide:
		)

		(ego
			view:				vEgoMowing
			ignoreActors:
			illegalBits:	0
			posn: 			119 146
			setCel:			0
			setCycle:		Walk
			setPri:			RELEASE
			setStep:			3 2
			init:
		)
		(self	setScript: 	rm91Script)
	)
)


(instance rm91Script of Script
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
					setMotion:		MoveTo 185 146 self
				)
			)

			(startMow2
				(ego
					loop: 			1
					posn: 			179 148
					setMotion:		MoveTo 116 148 self
				)
			)

			(startMow3
				(ego
					loop: 			0
					posn: 			110 152
					setMotion:		MoveTo 167 152 self
				)
			)

			(startCar
				(ego
					setLoop:			facingWest
					posn:				155 156
					setCel:			0
					stopUpd:
				)
				(aCar
					setMotion:		MoveTo 0 183 self
				)
			)

			(carDone
				(= seconds 3)
			)

			(eveBaby
				(Print "\"Eve, baby,\"
					you greet your woman with love in your voice,
					\"you're finally home!
					I've been worried sick about you.\"" BIG time: 8)
				(aMouth
					posn:				3 152
				)
				(= seconds 3)
			)

			(whoRU
				(Print "\"Who are you?\" asks the voluptuous woman in the
					the magnificent red sports car.
					\"And, why are you mowing my lawn?\"" time: 7)
				(aMouth
					posn:				3 1152
				)
				(= seconds 3)
			)

			(it'sMe
				(Print "\"Why, Eve! Don't you remember me?
					It's me, Larry; Larry Laffer!\"" BIG time: 6)
				(Print "\"We met in that hot tub in Lost Wages...
					in your luxury penthouse apartment...
					in \"The Land of the Lounge Lizards...\"
					just before my big finale (wink, wink)...
					at the end of the game, remember?\"" BIG time: 12)
				(aMouth
					posn:				3 152
				)
				(= seconds 2)
			)

			(vaguely
				(Print "\"Vaguely,\" she says. \"So why are you here?\""
					time: 5)
				(aMouth
					posn:				3 1152
				)
				(= seconds 3)
			)

			(moveIn
				(Print "\"Why, I, er, ah, that is,\" you stammer,
					\"why, I just kinda assumed when two people are as deeply
					in love as we are, uh, er,
					the natural thing to do to move in together, and,
					so here I am...\"" BIG time: 10)
				(Print "\"Move in?! You creep!
					You've got exactly five minutes
					to get everything out of my house,
					and out of my life.\"" time: 9)
				(Print "\"Brutus,\" she commands her dog,
					\"ON GUARD!\"" time: 4)
				(aDog					show:)
				(= seconds 3)
			)

			(dogJumps
				(aDog
					setCycle:		EndLoop self
				)
			)

			(dogOutOfCar
				(= cycles 5)
			)

			(familiar
				(aDog
					view:				vDogPissing
					posn: 			52 167
					setPri:			RELEASE
					setLoop:			facingEast
					setCycle:		Walk
					setMotion:		MoveTo 193 161 self
				)
				(Print "\"Hmm,\" you think to yourself,
					\"that dog looks rather familiar.\"" BIG time: 7)
				(aMouth
					posn:				3 152
				)
				(= cycles 30)
			)

			(Bback
				(Print "\"I'll be back here in five minutes,\" Eve shouts,
					\"and you WON'T!!\"" time: 8)
				(aMouth				dispose:)
				(aCar
					setMotion:		MoveTo -99 288
				)
			)

			(dogAtEgo
				(aDog
					setLoop:			lPissingEast
					setCycle:		Forward
				)
				(= cycles 30)
			)

			(dogPissing
				(Print "(Sigh.)" LOW time: 3)
				(= cycles 30)
			)

			(dogDone
				(aDog
					setLoop:			facingWest
					setCycle:		Walk
					setMotion:		MoveTo 123 161 self
				)
			)

			(badLuck
				(Print "Gee, Larry;
					looks like things are just the way they used to be.
					You thought your life was complete:
					you had found true love with a beautiful woman,
					with a beautiful car and a beautiful home,
					all in beautiful Los Angeles..." BIG time: 13)
				(Print "...but instead, you're out on the streets again!"
					BIG time: 4)
				(aDog
					setMotion:		MoveTo -22 151
				)
				(= seconds 3)
			)

			(whatWill
				(Print "What will you do?" time: 3)
				(curRoom newRoom: 92 (+ IRISOUT BLACK))
			)
		)
	)
)

