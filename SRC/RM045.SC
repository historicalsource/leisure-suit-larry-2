;**
;**	Logics for room 45 -- Resort barber shop
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 16, 1988
;**


(script#				45)

(public
	rm45				0
)


(define	nearChair	148 117 180 127)
(define	chairPosition			167 116)
(define	headPosition			165	86)
(define	waxPosition				165	82)

;(define	vBarberInt					232)	;**	various addToPic of shop interior
	(define	lEgoSitting			2)

;(define	vResortBarber				430)
	(define	lOffCounter			0)
	(define	lToChair				1)
	(define	lBehindChair		2)
	(define	lGeneric				3)
	(define	lPouring				4)
	(define	lBlue2black			5)
	(define	lBlack2blond		6)
	(define	lLeavingChairBlond 7)

;(define	vBarberWaxing				431)
	(define	lLeavingChairBikini 0)
;	(define	lToChair				1)
	(define	lTiltChair			2)
	(define	lPourWax				3)
	(define	lOpenWax				4)
	(define	lDiscardWax			5)
	(define	lFlashingLarry		6)
	(define	lMisc					7)
		(define	cChairBase	0)
		(define	cHoriLarry	1)

;(define	vBarberBleaching			437)


(local
	work
	aBarber
	aChair
	talkedToBarber
)


(enum
	intro
	sayHello

	bleachInit
	egoAtChair
	barberWalking
	barberWalking2
	barberAtChair
	sprinklePowder
	black2blond
	waitAsec
	growThatHair
	hairLong
	startBleaching
	bleachingDone
	ohGreat
	standUp
	allDone
	haveA

	waxInit
	egoAtChair2
	barberWalking3
	barberWalking4
	barberAtChair2
	tiltChair
	readyToRaiseVial
	raiseVial
	vialInAir
	pourWax
	waxOn
	allSet
	openWax
	waxOpen
	egoOnCeiling
	yeeeeeeOwwwwww
	fallFromCeiling
	downToChair
	punchLine
	raiseChair
	standUpWax
	allDoneWax
	haveAday
)


(instance rm45 of Room
	(properties
		picture:		125
		south:		40
	)

	(method (init)
		(Load VIEW currentEgoView)
		(Load VIEW vBarberInt)
		(Load VIEW vResortBarber)
		(Load VIEW vEgoBlond)

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
			view:				vResortBarber
			loop:				lOffCounter
			ignoreActors:
			illegalBits:	0
			posn:				184 92
;**				stopUpd:
			init:
		)

		(NormalEgo)
		(ego
			posn: 			160 160
			init:
		)

		(self
			setRegions:		rgBarber rgResort 
			setScript: 		rm45Script
		)
	)
)


(instance rm45Script of Script
	(method (doit)
		(super doit:)

		(if (& (ego onControl:) cBlue)
			(curRoom newRoom: 40)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (or	(Said 'sit, fix, yes, cut')
					(Said 'remove/hair')
					(Said '(get<in)/chair')
					(Said 'get/haircut, (cut<hair)'))
			(= talkedToBarber TRUE)
			(cond
				((not (ego inRect: nearChair))
					(Print "Walk over to the front of the 
						barber's chair and sit down.")
				)
				((== currentStatus SITTING)
					(YouAre)
				)
				((and blondHair bodyWaxed)
					(Print "\"I'm sorry, Meester,\"
						says the barber,
						\"I've donna all I canna do to, er, for you.
						Enjoy your new look!\"")
				)
				((not blondHair)
					(self changeState: bleachInit)
				)
				((and	(!= currentEgoView vEgoBikini)
						(!= currentEgoView vEgoBikiniStuffed))
					(Print "\"I'm sorry, Meester,\"
						says the barber,
						\"I've gotta no more 'pointments today!\"")
				)
				(else
					(self changeState: waxInit)
				)
			)
		)

		(if (Said 'look/barber')
			(Print "Funny. This guy doesn't look Italian!")
		)

		(if (Said 'talk/man')
			(= talkedToBarber TRUE)
			(if (!= currentEgoView vEgo)
				(Print "\"You looka mucho better to me, buddy!\"")
			else
				(Print "\"I gotta no time for a ticky-talky,\"
					responds the barber.")
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(if (!= currentEgoView vEgoBikiniStuffed)
					(= seconds 10)
				)
			)

			(sayHello
				(if (not talkedToBarber)
					(Print "\"Goodda daya, Meester,\" says the barber.
						\"Canna I helpa you?\"")
					(= seconds (Random 12 25))
					(= state intro)
				)
			)


			;**	Here we do the bleach job:

			(bleachInit
				(= cycles	0)
				(= seconds	0)
				(= currentStatus SITTING)
				(HandsOff)
				(Print "\"Is there anything you can do for my receding hairline?\"
					you ask the barber.")
				(Print "\"Butta course, Mister,\" says the barber,
					\"I gotta jus' ah whacha need.\"")
				(Print "\"Jus' havva seat inna da chair, please.\"")
				(ego
					illegalBits:0
					ignoreActors:
					setMotion:	MoveTo 157 120 self
				)
				(aBarber
					setCycle:	EndLoop
				)
			)

			(egoAtChair
				(aChair 			hide:)
				(ego
					view:			vBarberInt
					setLoop:		lEgoSitting
					cel:			0
					posn: 		chairPosition
					setCycle:	EndLoop self
				)
			)

			(barberWalking
				(aBarber
					setLoop:		lToChair
					cel: 			0
					setCycle:	EndLoop self
				)
			)

			(barberWalking2
				(ego				hide:)		;**	Together, at last!
				(aBarber
					view:			vResortBarber
					setLoop:		lBehindChair
					cel: 			0
					posn: 		chairPosition
					setCycle:	EndLoop self
				)
			)

			(barberAtChair
				(Print "\"Fora youa today, I gotta special deal,\"
					he begins. \"Imma gonna try somma my oxydol.\"" HIGH draw:)
				(Print "\"Don't you mean 'Minoxidol?'\" you ask." HIGH)
				(Print "\"No,\" he says, \"I dunno whata dat is;
					Imma gonna rubba you head with somma dis liquid
					laundry detergent!\"" HIGH)
				(Print "(Too late to leave now, Larry. He's got you!)" LOW)
				(= seconds 3)
			)

			(sprinklePowder
				(aBarber
					setLoop:			lPouring
					cel:				0
					posn: 			headPosition
					cycleSpeed:		1
					setPri:			9
					setCycle:		EndLoop self
				)
				(aChair
					view:				vResortBarber
					setLoop:			lGeneric
					cel:				0
					show:
				)
			)

			(black2blond
				(aBarber
					setLoop:			lBlue2black
					cel:				0
					cycleSpeed:		2
					setCycle:		CycleTo 4 FORWARD self
				)
			)

			(waitAsec
				(Print "\"Dere, alla set,\" says the barber,
					\"now let'sa justa give it a second ta work.\"" HIGH draw:)
				(Print "\"How ridiculous,\" you think,
					\"I just let some guy I don't even know
					pour laundry detergent on my head!\"" HIGH)
				(= seconds 3)
			)

			(growThatHair
				(aBarber
;**				setLoop:			lBlue2black
					setCycle:		EndLoop self
				)
			)

			(hairLong
				(Print "\"WOW!\" you exclaim,
					\"Not only does that stuff really work...
					it's fast, too!\"" HIGH draw:)
				(Print "\"I knew you'da be delighted,\" responds the barber,
					\"comma back anda see me any time, Blondie!\"" HIGH)
				(Print "\"Why do you call me that?\" you ask." HIGH)
				(Print "\"Oh, no reason,\" he says." HIGH)
				(= seconds 3)
			)

			(startBleaching
				(aBarber
					setLoop:			lBlack2blond
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(bleachingDone
				(= seconds 3)
			)

			(ohGreat
				(Print "\"Oh, great!\" you cry,
					\"now what am I supposed to do?\"" HIGH)
				(Print "\"Hava more fun,\" he answers." HIGH)
				(= seconds 3)
			)

			(standUp
				(aBarber
					setLoop:			lToChair
					setCel:			0
					setPri:			RELEASE
					posn:				167 92
				)
				(aChair
					setLoop:			lLeavingChairBlond
					cel:				0
					cycleSpeed:		1
					setCycle:		EndLoop self
				)
			)

			(allDone
				(= blondHair TRUE)
				(theGame changeScore: 3)
				(aChair
					view: 			vBarberInt
					setLoop: 		1
					setCel: 			0
				)
				(= currentEgoView vEgoBlond)
				(NormalEgo facingNorth)
				(ego
					posn:				157 120
					show:
				)
				(Print "\"Well, at least my bald spot is gone,\" you say." draw:)
				(= cycles 5)
			)

			(haveA
				(Print (Format @string
					"\"Along with your self-respect,\" says the barber.
					\"%s\"" tritePhrase))
				(aBarber				stopUpd:)
				(aChair				stopUpd:)
			)


			;**	Here we do the body wax:

			(waxInit
				(= cycles	0)
				(= seconds	0)
				(User
					canControl: 	FALSE
					canInput: 		FALSE
				)
				(Print "\"Is there anything you can do about my
					excess body hair?\" you ask the barber.")
				(Print "\"Butta course, Meester,\" says the barber,
					\"I gotta jus' whacha need.\"")
				(Print "\"Justa hava seat inna da chair, please.\"")
				(= currentEgoView (ego view?))
				(ego
					illegalBits:0
					ignoreActors:
					setMotion:	MoveTo 157 120 self
				)
				(aBarber
					illegalBits:0
					setCycle:	EndLoop
				)
			)

			(egoAtChair2
				(aChair 			hide:)
				(ego
					view:			vBarberWaxing
					setLoop:		lLeavingChairBikini
					cel:			7
					posn:			chairPosition
					setCycle:	BegLoop self
				)
			)

			(barberWalking3
				(aBarber
					setLoop:		lToChair
					cel: 			0
					setCycle:	EndLoop self
				)
			)

			(barberWalking4
				(ego				hide:)		;**	Together, at last!
				(aBarber
					view:			vBarberWaxing
					setLoop:		lToChair
					cel: 			0
					posn: 		chairPosition
					setCycle:	EndLoop self
				)
			)

			(barberAtChair2
				(Print "\"Fora youa, today onaly, I gotta special deal,\"
					he begins." HIGH draw:)
				(Print "\"Somehow I wonder,\" you reply." HIGH)
				(Print "\"No,\" he says, \"this'll worka reala good.\"" HIGH)
				(Print "\"What are you going to do to me this time?\"" HIGH)
				(Print "\"BODY WAXING!\"" HIGH)
				(Print "(Once again, he's got you trapped in the chair.
					You hope this time will turn out better!)" LOW)
				(= seconds 3)
			)

			(tiltChair
				(aBarber
					setLoop:			lTiltChair
					cel:				0
					cycleSpeed:		2
					setCycle:		EndLoop self
				)
			)

			(readyToRaiseVial
				(= seconds 3)
			)

			(raiseVial
				(aBarber
					setLoop:			lPourWax
					cel:				0
					posn: 			waxPosition
					setCycle:		CycleTo 1 FORWARD self
				)
				(aChair
					view:				vBarberWaxing
					setLoop:			lMisc
					cel:				cChairBase
					posn:				chairPosition
					show:
				)
			)

			(vialInAir
				(= seconds 3)
			)

			(pourWax
				(aBarber
					setCycle:		EndLoop self
				)
			)

			(waxOn
				(= seconds 3)
			)

			(allSet
				(Print "\"Dere, alla set,\" says the barber,
					\"now justa lay dere and let it geta hard.\"" HIGH)
				(Print "\"How could I be so stupid,\" you think,
					\"I have wax hardening all over my naked body!\"" HIGH)
				(= seconds 3)
			)

			(openWax
				(aBarber
					setLoop:			lOpenWax
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(waxOpen
				(Print "\"Yeeeeee yoooowwwwww!\"" LOW draw:)
				(aBarber
					setLoop:			lDiscardWax
					cel:				0
					setCycle:		EndLoop
				)
				(ego
					view:				vBarberWaxing
					posn: 			157 94
					setLoop:			lFlashingLarry
					setCycle:		Forward
					setStep:			1 10
					setPri:			9
					setMotion:		MoveTo 157 57 self
					show:
				)
			)

			(egoOnCeiling
				(= seconds 4)
			)

			(yeeeeeeOwwwwww
				(ego
					setCel:			0
				)
				(= seconds 3)
			)

			(fallFromCeiling
				(ego
					setMotion:		MoveTo 157 94 self
				)
			)

			(downToChair
				(ego					hide:)
				(aChair				hide:)
				(aBarber
					view:				vBarberWaxing
					setLoop:			lTiltChair
					setCel:			5
					posn: 			chairPosition
				)
				(= seconds 3)
			)

			(punchLine
				(Print "\"Oh, gawd!\" you cry, \"was it good for you?\"" HIGH)
				(Print "\"I donna think that could be good for anybody!\"
					he answers." HIGH)
				(= seconds 3)
			)

			(raiseChair
				(aBarber
					setCycle:		BegLoop self
				)
			)

			(standUpWax
				(aBarber
					view:				vResortBarber
					setLoop:			lToChair
					setCel:			0
					setPri:			RELEASE
					posn:				167 92
				)
				(aChair
					view:				vBarberWaxing
					setLoop:			lLeavingChairBikini
					cel:				0
					setCycle:		EndLoop self
					show:
				)
			)

			(allDoneWax
				(= bodyWaxed TRUE)
				(theGame changeScore: 3)
				(aChair
					view: 			vBarberInt
					setLoop: 		1
					setCel: 			0
				)
				(NormalEgo facingNorth)
				(ego
					posn:				157 120
					show:
				)
				(Print "\"Well, perhaps the pain was worth it;
					I DO like this 'clean-shaven' look!\" you tell him." draw:)
				(= seconds 2)
			)

			(haveAday
				(Print (Format @string
					"\"You're the only one,\" concludes the barber.
					\"%s\"" tritePhrase))
			)
		)
	)
)

