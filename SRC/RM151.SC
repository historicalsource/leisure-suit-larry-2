;**
;**	Logics for room 151 -- Airport Barber Shop
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 22, 1988
;**


(script#				151)

(public
	rm151				0
)


(define	nearChair	 148 117 180 127)
(define	chairPosition			167 116)
(define	headPosition			165  86)
(define	waxPosition				165  82)

;(define	vBarberInt					232)	;**	various addToPic of shop interior
	(define	lEgoSitting		2)

;(define	vAirportBarber				507)
;	(define	loops	0, 1, 2, 3 	are just Rosie walkin'
	(define	lGeneric			4)
	(define	lRaiseArm		5)
	(define	lSprinkle		6)
	(define	lPoof				7)
	(define	lLeavingChair	8)				;**	same as v430, l7
	(define	lBigFace			9)

(local
	aBarber
	aChair
	talkedToBarber
)


(enum
	intro
	sayHello

	startScene
	egoAtChair
	barberWalking
	barberWalking2
	sitDown
	doApron
	cuttingAlittleHair
	wakeUp
	itsOK
	standUp
	haircutDone
	haveA
)


(instance rm151 of Room
	(properties
		picture:		125
		horizon:		5
		south:		51
	)

	(method (init)
		(Load VIEW vAirportBarber)
		(Load VIEW vBarberInt)

		(super init:)

		((= aChair (Prop new:))				;**	barber chair
			view: 			vBarberInt
			loop: 			1
			cel: 				0
			posn: 			chairPosition
			setPri: 			8
			ignoreActors:
;**				stopUpd:
			init:
		)

		((= aBarber (Actor new:))
			view:				vAirportBarber
			loop:				facingSouth
			posn:				144 153			;**	looking out window
			setCycle:		Walk
			illegalBits:	cLtWhite
			init:
		)

		(aBigFace
			view:				vAirportBarber
			setLoop:			lBigFace
			posn:				36 1118
			setPri:			15
			init:
		)

		(NormalEgo facingNorth)
		(ego
			posn: 			160 160
			init:
		)

		(self
			setRegions:		rgBarber rgAirport 
			setScript: 		rm151Script
		)
	)
)


(instance rm151Script of Script
	(method (doit)
		(super doit:)

		(if (& (ego onControl:) cBlue)
			(curRoom newRoom: 51)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (or	(Said 'sit, fix, yes, cut')
					(Said '(get<in)/chair')
					(Said 'get/haircut, (cut<hair)'))
			(= talkedToBarber TRUE)
			(cond
				((not (ego inRect: nearChair))
					(Print "Walk over to the front of the 
						barber's chair and sit down.")
				)
				((!= currentEgoView vEgoBlond)
					(Print "\"I'm sorry, sir,\"
						says the barber sweetly,
						\"I have no more appointments left today!\"")
				)
				((== currentStatus SITTING)
					(YouAre)
				)
				(else
					(= blondHair FALSE)
					(self changeState: startScene)
				)
			)
		)

		(if (Said 'look/barber, girl, person')
			(aBigFace
				posn:				36 118
				stopUpd:
			)
			(if (not scoredRosella)
				(= scoredRosella TRUE)
				(theGame changeScore: 3)
			)
			(Timer setReal: aBigFace 5)
			(HandsOff)
		)

		(if (Said 'talk/barber, person, girl')
			(= talkedToBarber TRUE)
			(if (== currentEgoView vEgo)
				(Print "\"You look much better to me, sir!\"")
			else
				(Print "\"Just have a seat in the chair there,\"
					responds the barber.")
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(if ((inventory at: iHairRejuvenator) ownedBy: curRoomNum)
					(= seconds 10)
				)
			)

			(sayHello
				(if (not talkedToBarber)
					(Print "\"Good day, sir,\" says the female barber with the long,
						beautiful, blond hair. 
						\"May I help you in your quest?\"")
					(= seconds (Random 12 25))
					(= state intro)
				)
			)


			;**	This does the hair exchange:

			(startScene
				(= cycles	0)
				(= seconds	0)
				(= currentStatus SITTING)
				(HandsOff)
				(Print "\"Could I just get a slight trim?\" you ask.
					\"My hair is ridiculously long!")
				(Print "\"Oh, oh!\" she replies, \"I'll be happy to.
					Just plop it in my chair there, Goldilocks,
					I'll take care of those curly locks for you!\"")
				(Print "\"Gosh, please be careful,\" you tell her,
					\"I'd hate to lose my full head of hair.
					Just take a little off the back!\"")
				(ego
					illegalBits:	0
					ignoreActors:
					setMotion:		MoveTo 157 120 self
				)
				(aBarber
					setMotion:		MoveTo 185 119	self
				)
			)

			(egoAtChair
				(ego					hide:)
				(aChair
					view:				vAirportBarber
					setLoop:			lLeavingChair
					setCel:			LastCel
					setCycle:		BegLoop
				)
			)

			(barberWalking
				(aBarber
					setMotion:		MoveTo 179 113 self
				)
			)

			(barberWalking2
				(aBarber
					setMotion:		MoveTo 169 113 self
					illegalBits:	0
					ignoreActors:
				)
			)

			(sitDown
				(aChair
					view:				vAirportBarber
					setLoop:			lGeneric
					setCycle:		FALSE
				)
				(aBarber
					view:				vAirportBarber
					setLoop:			lRaiseArm
					posn:				headPosition
					setCel:			0
					setCycle:		EndLoop self
					setPri:			9
				)
			)

			(doApron
				(aChair
					stopUpd:
				)
				(aBarber
					setLoop:			lSprinkle
					cycleSpeed:		1
					cel:				0
					setCycle:		Forward
				)
				(= seconds 5)
			)

			(cuttingAlittleHair
				(aBarber
					setLoop:			lPoof
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(wakeUp
				(aChair
					view:				vBarberInt
					setLoop:			lEgoSitting
					setCel:			LastCel
				)
				(aBarber
					loop:				facingSouth
					setLoop:			RELEASE
					setPri:			7
					posn:				169 113
					setCycle:		Walk
					ignoreActors:	FALSE
				)
				(Print "\"All done,\" says the barber.
					\"What do you think?\"" HIGH draw:)
				(= seconds 3)
			)

			(itsOK
				(Print "\"MY HAIR!\" you cry. \"IT'S GONE!\"" HIGH)
				(Print "She replies,
					\"It serves you right; you should never trust a barber with
					longer, prettier, blonder hair than yours!\"" HIGH)
				(= seconds 3)
			)

			(standUp
				(aChair
					setCycle:		BegLoop self
				)
			)

			(haircutDone
				(= currentEgoView vEgo)
				(ego
					view:				vEgo
					setLoop:			RELEASE
					loop:				facingNorth
					ignoreActors:	FALSE
					show:
				)
				(aChair
					view: 			vBarberInt
					loop: 			1
					cel: 				0
				)
				(= seconds 2)
			)

			(haveA
				(aChair				stopUpd:)
				(aBarber				stopUpd:)
				(Print "\"Well, at least I look like my passport picture now,\"
					you sigh, \"although that seems small consolation.\"" draw:)
				(Print "\"I'm sorry you're not satisfied,\" says the barber.
					\"Here. Take a bottle of this month's special.
					When you have time,
					rub a teaspoon on your hair every day for a month.
					Soon your hair will be full and rich.\"")
				(Print (Format @string
					"\"But be careful,\" she concludes, \"it's highly inflammatory.
					%s\"" tritePhrase))
				(NormalEgo)
				(theGame changeScore: 3)
				(ego 
					get: iHairRejuvenator
					setMotion:		MoveTo 157 222 self
				)
			)
		)
	)
)

(instance aBigFace of Prop
	(method (cue)
		(Print "You find Daventry women sexy.")
		(Print "(But then, you find any woman sexy!)" at: 55 155 width: 210)
		(self posn: 36 1118)
		(Print "\"By the way, have you played 'King's Quest IV' yet?\" 
			asks the barber." HIGH draw:)
		(Print "\"Why, no, I haven't,\" you reply. \"Is it good?\"")
		(Print "\"Well, I certainly think so,\" she concludes,
			\"maybe it's just me!\"" HIGH)
		(HandsOn)
	)
)

