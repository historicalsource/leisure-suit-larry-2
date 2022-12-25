;**
;**	Logics for room 125 -- inside LA barber shop
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 22, 1988
;**


(script#				125)

(public
	rm125				0
)


(define	nearChair	148 117 180 127)

;(define	vBarberInt					232)	;**	various addToPic of shop interior
	(define	lEgoSitting		2)

;(define	vLAbarber					233)
;(define	vLAhaircut					244)
	(define	lApron			0)
	(define	lTiltChair		1)
	(define	lWashHair		2)
	(define	lRaiseChair		3)
	(define	lCutHair			4)

;(define	vLAhaircutThoughts		245)	;**	Either dirty, 
;(define	vLAhaircutCleanThoughts	248)	;**		or clean.
	(define	lBalloon			0)
	(define	lNakedCouple	1)
	(define	lLarryHead		2)
	(define	lDogPissing		3)

;(define	vBigMirror					246)
	(define	lMirror			0)
		(define	cMirror	0)
		(define	cHandle	1)
	(define	lEgo				1)


(local
	aBarber
	aChair
	aThoughtBalloon
	aThought
	aHeads
	aHandle

	cycSpeed
	thoughtView
	talkedToBarber
)


(enum
	intro
	sayHello

	weaveInit
	egoAtChair
	barberWalking
	barberWalking2
	barberShowsMirror
	youThink
	gopherIt
	sitDown
	doApron
	tiltChair
	wetHair
	think
	growBubble
	growHead
	shrinkHead
	growHead2
	shrinkHead2
	growHead3
	shrinkHead3
	headJobDone
	cueTheCouple
	coupleDone
	goodDreams
	cueDog
	dogPissing
	endDog
	dogDone
	raiseChair
	cuttingAlittleHair
	wakeUp
	drawMirror
	okIguess
	itsOK
	whatsAhaircut
	punchLine
	adjustChair
	removeApron
	standUp
	weaveDone
	haveA
)


(instance rm125 of Room
	(properties
		picture:		125
		horizon:		5
		south:		25
	)

	(method (init)
		(Load VIEW vLAbarber)
		(Load VIEW vBarberInt)
		(Load VIEW vLAhaircut)
		(Load VIEW vBigMirror)

		(if ( > filthLevel CLEAN)
			(= thoughtView vLAhaircutThoughts)
		else
			(= thoughtView vLAhaircutCleanThoughts)
		)
		(Load VIEW thoughtView)

		(super init:)

		(cond
			(( > machineSpeed 60)	(= cycSpeed 3))
			(( > machineSpeed 40)	(= cycSpeed 2))
			(( > machineSpeed 20)	(= cycSpeed 1))
		)	

		((= aThoughtBalloon (Prop new:))
			view:				thoughtView
			ignoreActors:
			posn:				160 1128
			setPri:			14
			init:
		)
		((= aThought (Prop new:))
			view:				thoughtView
			ignoreActors:
			posn:				161 1120
			setPri:			15
			init:
		)
		((= aHandle (View new:))
			view:				vBigMirror
			setCel:			cHandle
			setPri:			14
			ignoreActors:
			posn:				160 1129
			init:
		)
		((= aHeads (Prop new:))
			view:				vLAhaircut
			ignoreActors:
			init:
			hide:
		)

		((= aChair (View new:))				;**	barber chair
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
			view:				vLAbarber
			loop:				facingSouth
			posn:				113 153			;**	looking out window
			setCycle:		Walk
			illegalBits:	cLtWhite
			init:
		)

		(NormalEgo facingNorth)
		(ego
			posn: 			160 160
			init:
		)

		(self
			setRegions:		rgBarber rgLA 
			setScript:		rm125Script
		)
	)
)


(instance rm125Script of Script
	(method (doit)
		(super doit:)

		(if (& (ego onControl:) cBlue)
			(curRoom newRoom: 25)
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
				((ego has: iMillion)
					(Print "\"Do you have change for a million-dollar bill?\"
						you ask the barber.")
					(Print "\"Get lost,\" replies the barber,
						\"I don't appreciate practical jokes.\"")
				)
				((not (ego has: iMoney))
					(Print "You'd like to get a haircut,
						but you just don't have enough money!")
				)
				(LAhaircut
					(Print "\"I'm sorry, monsieur,\"
						says the barber rudely,
						\"I have no more appointments left today!\"")
					(Print "\"In fact, I'd appreciate it if you'd leave now
						before I have to call the Hair Police!\"")
				)
				((== currentStatus SITTING)
					(YouAre)
				)
				(else
					( = LAhaircut TRUE)
					(self changeState: weaveInit)
				)
			)
		)

		(if (Said 'talk/man')
			(= talkedToBarber TRUE)
			(if (== LAhaircut TRUE)
				(Print "\"You look much better to me, monsieur!\"")
			else
				(Print "\"I have no time for idle conversation,\"
					responds the barber.
					\"It's bad enough I have to talk to customers while
					I cut their hair,
					I certainly don't want to talk when I'm NOT cutting hair!\"")
				(Print "\"If you want me to work on that problem head of yours,
					you'd better sit down in the chair.\"")
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(if (not LAhaircut)
					(= seconds 10)
				)
			)

			(sayHello
				(if (not talkedToBarber)
					(Print "\"Good day, monsieur,\" says the barber.
						\"May I help you?\"")
					(= seconds (Random 12 25))
					(= state intro)
				)
			)


			;**	This does the haircut:

			(weaveInit
				(= cycles	0)
				(= seconds	0)
				(= currentStatus SITTING)
				(HandsOff)
				(Print "\"How about a quick styling?\" you ask.")
				(Print "\"Oui, monsieur, but of course,\" replies the barber,
					\"allow me to correct that receding hairline of yours with
					my patented, special, proprietary technique...
					macrobiotic styling reweaving!\"")
				(Print "\"Just have a seat in the chair, please.\"")
				(ego
					illegalBits:0
					ignoreActors:
					setMotion:	MoveTo 157 120 self
				)
				(aBarber
					setMotion:	MoveTo 185 119	self
				)
			)

			(egoAtChair
				(aChair 			hide:)
				(ego
					view:			vBarberInt
					setLoop:		lEgoSitting
					cel:			0
					posn:			167 116
					setCycle:	EndLoop
				)
			)

			(barberWalking
				(aBarber
					setMotion:	MoveTo 179 111 self
				)
			)

			(barberWalking2
				(aBarber
					setMotion:	MoveTo 166 111 self
					illegalBits:0
				)
			)

			(barberShowsMirror
				(Print "\"Here,\" says the barber,
					\"take a look in this mirror and remember what you look like.
					When I'm done, you won't be able to recognize your own
					hairline!
					Why, you'll be a different man!\"")
				(aThoughtBalloon
					view:			vBigMirror
					setCel:		cMirror
					posn:			160 128
					setPri:		14
					stopUpd:
				)
				(aHandle
					posn:			160 129
					stopUpd:
				)
				(aThought
					view:			vBigMirror
					setLoop:		lEgo
					cel:			0
					posn:			161 120
					setPri:		15
					setCycle:	Forward
					cycleSpeed:	(* 2 cycSpeed)
				)
				(= seconds 6)
			)

			(youThink
				(Print "You think to yourself,
					\"Yeah, this is exactly what I need...
					a new look.
					I'll make Bachelorette Barbie change her tune!\"" BIG)
				(= seconds 3)
			)

			(gopherIt
				(aThought
					posn:			161 1120
				)
				(aHandle
					posn:			160 1129
				)
				(aThoughtBalloon
					posn:			160 1128
				)
				(Print "\"Ok, go for it!\" you tell the barber." HIGH draw:)
				(= seconds 3)
			)

			(sitDown
				(ego 				hide:)
				(aBarber
					view:			vLAhaircut
					setLoop:		lApron		;**	flip apron
					posn:			167 116
					setCel:		0
				)
				(= cycles 30)
			)

			(doApron
				(aBarber
					cycleSpeed:	1
					setCycle:	EndLoop self
				)
			)

			(tiltChair
				(Print "\"Allow me to begin with a thorough cleansing
					to rid your hair and scalp of any possible pollutants,\"
					says the barber.
					\"I assure you, I use only the trendiest products,
					all organically grown and available only in undersized,
					overpriced, biodegradable bottles!\"" draw:)
				(aBarber
					setLoop:		lTiltChair
					cel:			0
					cycleSpeed:	2
					setCycle:	EndLoop self
				)
			)

			(wetHair
				(aHeads
					posn:			166 101
					setLoop:		lWashHair		;**	washingHair headsOnly
					cel:			0
					setPri:		9
					cycleSpeed:	2
					show:
					setCycle:	Forward
				)
				(aBarber
					stopUpd:
				)
				(= seconds 5)
			)

			(think
				(Print "You wonder to yourself,
					\"What will I look like when he's done?\"")
				(Print "\"Will I be handsome?\"")
				(Print "\"Will I ever find true love?\"")
				(= seconds 5)
			)

			(growBubble
				(aThoughtBalloon
					view:			thoughtView
					setLoop:		lBalloon
					cel:			0
					setPri:		14
					posn:			154 92
					cycleSpeed:	cycSpeed
					setCycle:	EndLoop self
				)
			)

			(growHead
				(aThoughtBalloon
					stopUpd:
				)
				(aThought
					view:			thoughtView
					setLoop:		lLarryHead
					setPri:		15
					setCel:		0
					posn:			110 46
					cycleSpeed:	cycSpeed
					setCycle:	EndLoop self
				)
			)

			(shrinkHead
				(aThought
					setCycle:	BegLoop self
				)
			)

			(growHead2
				(aThought
					setCycle:	EndLoop self
				)
			)

			(shrinkHead2
				(aThought
					setCycle:	BegLoop self
				)
			)

			(growHead3
				(aThought
					setCycle:	EndLoop self
				)
			)

			(shrinkHead3
				(aThought
					setCycle:	BegLoop self
				)
			)

			(headJobDone
				(aThought
					posn:			161 1120
				)
				(= seconds 5)
			)

			(cueTheCouple
				(aThought
					setLoop: 	lNakedCouple
					posn:			112 41
					cel: 			0
					setPri: 		15
					setCycle:	EndLoop self
				)
			)

			(coupleDone
				(aThought
					posn:			161 1120
				)
				(= seconds 3)
			)

			(goodDreams
				(Print "\"You certainly have interesting daydreams!\"
					says the barber.")
				(= seconds 3)
			)

			(cueDog
				(aThought
					setLoop:		lDogPissing
					cel: 			0
					posn: 		103 42
					cycleSpeed:	cycSpeed
					setCycle:	CycleTo 10 FORWARD self
				)
			)

			(dogPissing
				(Print "Hey, Brutus! Get outta here!!" draw:)
				(= cycles 8)
			)

			(endDog
				(aThought
					setCycle:	EndLoop self
				)
			)

			(dogDone
				(aThought
					posn:			161 1120
				)
				(aThoughtBalloon
					posn:			160 1128
				)
				(= seconds 6)
			)

			(raiseChair
				(aHeads
					hide:
				)
				(aBarber
					setLoop:		lRaiseChair
					cel:			0
					cycleSpeed:	cycSpeed
					setCycle:	EndLoop self
				)
				(Print "\"Your hair is clean and conditioned,\" says the barber,
					\"now for the special styling.\"" draw:)
				(Print "\"However, in the future, may I recommend 10w40 and
					no more than 3,000 miles between oil changes!\"")
			)

			(cuttingAlittleHair
				(aHeads
					posn:			166 95
					setLoop:		lCutHair
					cel:			0
					setPri:		14
					setCycle:	Forward
					show:
				)
				(aBarber
					stopUpd:
				)
				(= seconds 11)
			)

			(wakeUp
				(aHeads
					dispose:
				)
				(Print "\"All done,\" says the barber.
					\"Take a look in this mirror... what do you think?\"" draw:)
				(= seconds 3)
			)

			(drawMirror
				(aThoughtBalloon
					view:			vBigMirror
					setCel:		cMirror
					posn:			160 128
					setPri:		14
					stopUpd:
				)
				(aHandle
					posn:			160 129
					stopUpd:
				)
				(aThought
					view:			vBigMirror
					setLoop:		lEgo
					cel:			0
					setPri:		15
					posn:			161 120
					setCycle:	Forward
					cycleSpeed:	cycSpeed
				)
				(= seconds 6)
			)

			(okIguess
				(Print "Oh, well. You can't judge a book by its cover!"
					HIGH draw:)
				(= seconds 3)
			)

			(itsOK
				(aThought			dispose:)
				(aHandle				dispose:)
				(aThoughtBalloon	dispose:)
				(Print "\"That'll be fine,\" you tell the barber." HIGH draw:)
				(= seconds 3)
			)

			(whatsAhaircut
				(Print "\"Say,\" you ask him, \"exactly how could I know
					I was getting a macrobiotic styling?\"")
				(= seconds 3)
			)

			(punchLine
				(Print "\"By the price!\"")
				(= seconds 2)
			)

			(adjustChair
				(aBarber
					setLoop:			lRaiseChair
					setCel:			LastCel
					setCycle:		BegLoop self
				)
			)

			(removeApron
				(aBarber
					setLoop:			lApron
					setCel:			LastCel
					setCycle:		BegLoop self
				)
			)

			(standUp
				(aBarber
					view:				vLAbarber
					posn:				182 115
					loop:				facingSouth
					setCycle:		Walk
					illegalBits:	cLtWhite
				)
				(ego
					view:				vBarberInt
					setLoop:			lEgoSitting
					setCel:			LastCel
					posn:				167 116
					setCycle:		BegLoop self
					show:
				)
			)

			(weaveDone
				(NormalEgo facingSouth)
				(ego ignoreActors: FALSE)
				(aChair show:)
				(theGame changeScore: 3)
				(= seconds 2)
			)

			(haveA
				(Print "\"That'll be a hundred bucks,\" says the barber.")
				(Print "\"Thanks, I guess,\" you say,
					flipping the barber one of your C-notes.")
				(Print (Format @string
					"The barber replies,
					\"%s\"" tritePhrase))
			)
		)
	)
)

