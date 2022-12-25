;**
;**	Logics for room 102 -- Green Room
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 13, 1988
;**


(script#				102)

(public
	rm102				0
)


(define	nearSeat			101 90 255 99)
(define	nearDoorSouth 	0 151 screenRight screenBottom)

;(define	vEgoSitting					101)
	(define	lSitting				0)
	(define	lArmMove				1)
	(define	lLegCross			2)

;(define	vGreenRoom					202)
	(define	lReception		0)
	(define	lDC				1)
	(define	lLLL				2)

;(define	vDCasstProducer			206)
;	(define	facingEast		0)				;**	standard loop numbers
;	(define	facingWest		1)
	(define	lSweaterToss	2)
	(define	lExtendArm		3)
	(define	lTalkWithWrist	4)
	(define	lTurnToLeave	5)

;(define	vLLLasstProducer			217)
;	(define	facingEast		0)				;**	standard loop numbers
;	(define	facingWest		1)
	(define	lTalkingEast	2)
	(define	lTalkingWest	3)


(local
	work
	doorIsOpen 
	aProducer
	aDoorWest
	aDoorEast
	aTVwest
	aTVeast
)


(enum												;**	for room script
	intro
	waitingForProducerDC
	startDCtimer
	openDoorDC
	walkingInDC
	flipSweaterDC
	producerInDC
	producerTalkingDC
	comeOnLarryDC
	hurryDC
	producerLeavingDC
	producerGoneDC

	enteringFrom103
	egoIn
	flipSweater
	producerIn
	producerTalking
	youWonIGuess
	producerLeaving
	producerGone

	waitingForProducerLLL
	startLLLtimer
	openDoorLLL
	walkingInLLL
	producerTalkingLLL
	comeOnLarryLLL
	producerGoneLLL
)

(enum												;**	sittingScript state values
	walkToSeat
	walkCloser
	sittingDown
	seated
	fidgeting
	standingUp
	standing
)


(instance rm102 of Room
	(properties
		picture:		102
	)

	(method (init)
		(Load VIEW vGreenRoom)
		(Load VIEW vDCasstProducer)
		(Load VIEW vLLLasstProducer)

		(super init:)

		((View new:)							;**	painting on west wall
			view:				vGreenRoom
			loop: 			4
			cel: 				0
			posn: 			59 127
			setPri: 			9
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	painting on East wall
			view:				vGreenRoom		;**	painting on North wall is in pic
			loop: 			4
			cel: 				1
			posn: 			262 129
			setPri: 			9
			ignoreActors:
			addToPic:
		)

		((= aTVwest (Prop new:))
			view:				vGreenRoom
			setLoop:			2
			setCel:			0
			posn:				96 95
			setPri:			4
			setCycle:		Forward
			isExtra:			TRUE
			init:
		)
		((= aTVeast (Prop new:))
			view:				vGreenRoom
			setLoop:			3
			setCel:			1
			posn:				228 96
			setPri:			4
			setCycle:		Forward
			isExtra:			TRUE
			init:
		)

		(self setScript: rm102Script)

		(NormalEgo)
		(ego
			observeControl:cYellow cLtMagenta
		)
		((= aDoorWest (Prop new:))
			view:				vGreenRoom
			posn:				83 116
			setPri:			4
			init:
		)
		((= aDoorEast (Prop new:))
			view:				vGreenRoom
			setLoop:			1
			posn:				250 117
			setPri:			4
			init:
		)

		(if (== prevRoomNum 103)			;**	prev room is Dating Connection
			(aDoorWest
				setCel:		LastCel
			)
			(ego
				posn:			67 123
				observeControl:cLtMagenta
				ignoreActors:
			)
			(rm102Script changeState: enteringFrom103)
		else
			(ego
				posn: 		163 154
			)
			(rm102Script changeState: waitingForProducerDC)
		)
		(ego					init:)
	)
)


(instance rm102Script of Script
	(method (doit)
		(super doit:)

		(if (and (== currentStatus SITTING)
					(or	(== state waitingForProducerLLL)
							(== state waitingForProducerDC)))
			(self cue:)
		)

		(if (== doorIsOpen cLtMagenta)
			(ego ignoreControl: cLtMagenta)
		)

		(if (== doorIsOpen cYellow)
			(ego ignoreControl: cYellow)
		)

		(if (& (ego onControl:) cGreen)
			(curRoom newRoom: 103)
		)

		(if (& (ego onControl:) cMagenta)
			(curRoom newRoom: 104)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'open/door')
			(if doorIsOpen 
				(ItIs)
			else
				(Print "You can't. They are all locked. And, there's no key!")
				(Print "Just be patient. Have a seat. Kick back. Relax.")
			)
		)

		(if (Said 'look>')
			(if (Said '/art, wall')
				(Print "That Skirvin guy sure can draw horses!")
			)

			(if (Said '/door')
				(if doorIsOpen 
					(Print "There's one door that's open.")
					(Print "(Why aren't you walking through it?)" LOW)
				else
					(Print "There are doors to the east, west and south,
						which are all locked. Tight. And, there's no key.")
					(Print "(Looks like you're trapped in here until someone
						comes to your rescue, Larry!)" LOW)
				)
			)

			(if (Said '/tv, lady, monitor')
				(Print "One monitor shows a lovely young lady;
					the other, just static.")
			)

			(if (Said '/bench')
				(Print "It looks comfortable.")
			)

			(if (Said '[/room, studio, lobby, building]')
				(Print "You wonder why the receptionist called this a green room,
					when there's no green in it at all!
					There are two television monitors, a bench, some art posters,
					a lovely modern painting and three doors.")
			)
		)

		(if (Said 'sit')
			(cond
				((== currentStatus SITTING)
					(YouAre)
				)
				((not (ego inRect: nearSeat))
					(NotClose)
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				(else
					(if (not scoredRm102Sit)
						(= scoredRm102Sit TRUE)
						(theGame changeScore: 1)
					)
					(Ok)
					(ego setScript: sittingScript)
					(sittingScript changeState: walkToSeat)
				)
			)
		)

		(if (or	(Said 'rise, (stand<up), (get<up)')
					(Said 'leave[/chair]'))
			(cond
				((== currentStatus NORMAL)
					(YouAre)
				)
				((!= currentStatus SITTING)
					(NotNow)
				)
				(else
					(Ok)
					(ego setScript:	sittingScript)
					(sittingScript changeState: standingUp)
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(intro) is unused!

			(waitingForProducerDC
				(HandsOn)
				(ego
					observeControl:cYellow cLtMagenta
				)
			)


			;**	Larry's entered this room for the first time.	Make him wait
			;**	around a while until the fag comes to get him!

			(startDCtimer
				(= seconds (Random 10 30))
			)

			(openDoorDC
				(if (!= currentStatus SITTING)
					(-- state)
					(= seconds 3)
				else
					(HandsOff)
					(= doorIsOpen cYellow)
					(aDoorWest
						setCycle:		EndLoop self
					)
				)
			)

			(walkingInDC
				((= aProducer (Actor new:))
					view:				vDCasstProducer
					loop:				facingEast
					illegalBits:	0
					ignoreActors:	FALSE
					setCycle:		Walk
					posn:				60 120
					init:
					setMotion:		MoveTo 80 120 self
				)
			)

			(flipSweaterDC
				(aProducer
					setLoop:			lSweaterToss
					cel:				0
					setCycle:		EndLoop
					setMotion:		MoveTo 97 120 self
				)
			)

			(producerInDC
				(aProducer
					setLoop:			lExtendArm
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(producerTalkingDC
				(aProducer
					setLoop:			lTalkWithWrist
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(comeOnLarryDC
				(Print "\"Oh, thay, you! Poo Poo,\" cries the man,
					\"You're late!
					Where have you been, you thilly little dickenth?!
					We've justh been worried thick about you.\"" HIGH)
				(= seconds 2)
			)

			(hurryDC
				(Print "Before you can answer him, he tells you:
					\"Hurry up, Honey, you're ON!!\"" HIGH)
				(aProducer
					setLoop:			lTurnToLeave
					cycleSpeed:		2
					setCycle:		EndLoop self
				)
			)

			(producerLeavingDC
				(User canInput: 	TRUE)
				(if (!= currentStatus SITTING)
					(User canControl: TRUE)	
				)
				(aProducer
					setLoop:			facingWest
					setCycle:		Walk
					cycleSpeed:		0
					ignoreActors:	TRUE
					setMotion:		MoveTo 60 120
				)
			)

			(producerGoneDC
				(aProducer dispose:)
				;**	Here we wait for Larry to walk out the doorway.
			)


			;**	This path through the script is only taken if Larry just
			;**	finished "The Dating Connection" and is waiting for the
			;**	"Lucky Life Lottery" show.		i.e. PrevRoom == 103

			(enteringFrom103
				(HandsOff)
				(ego
					illegalBits:	0
					setMotion:		MoveTo 120 123 self
				)
			)

			(egoIn
				((= aProducer (Actor new:))
					view:				vDCasstProducer
					loop:				facingEast
					setCycle:		Walk
					illegalBits:	0
					ignoreActors:
					posn:				60 120
					init:
					setMotion:		MoveTo 80 120 self
				)
				(ego
					illegalBits:	cLtWhite
					observeControl:cYellow cLtMagenta
					loop:				facingWest
				)
			)

			(flipSweater
				(aProducer
					setLoop:			lSweaterToss
					cel:				0
					setCycle:		EndLoop
					setMotion:		MoveTo 97 120 self
				)
			)

			(producerIn
				(aProducer
					setLoop:			lExtendArm
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(producerTalking
				(aProducer
					setLoop:			lTalkWithWrist
					cycleSpeed:		1
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(youWonIGuess
				(Print "The assistant producer looks at you with disdain,
					\"Well, Laffer, I musth admit none of usth in the
					control room exthpected you to win.
					I'm thure you're not proud of the outcome,
					but, ruleths are ruleths I thuppoth.\"" BIG)
				(Print "\"Here is your cruithe thip ticket.
					All of usth here at 'The Dating Connecthion' wisth you
					the besth of luck.\"" HIGH)
				(Print "\"You'll need it!\"" HIGH)
				(ego get: iCruiseTicket)
				(theGame changeScore: 6)
				(aProducer
					setLoop:			lTurnToLeave
					setCycle:		EndLoop self
				)
			)

			(producerLeaving
				(aProducer
					setLoop:			facingWest
					setCycle:		Walk
					cycleSpeed:		0
					setMotion:		MoveTo 36 120 self
				)
			)

			(producerGone
				(aProducer 			
					posn: 			264 122
				)
				(aDoorWest
					setCycle:		BegLoop self
				)
			)

			(waitingForProducerLLL
				(HandsOn)
				(aDoorWest			stopUpd:)
				(= currentStatus 	NORMAL)
			)


			;**	Now we wait for Larry to sit down, before starting the timer
			;**	that brings the girl producer in to summon Larry to
			;**	the Lucky Life Lottery show.

			(startLLLtimer
				(= seconds (Random 11 33))
			)

			(openDoorLLL
				(if (!= currentStatus SITTING)
					(-- state)
					(= seconds 3)
				else
					(HandsOff)
					(= doorIsOpen cLtMagenta)
					(aDoorEast
						setCycle:		EndLoop self
					)
				)
			)

			(walkingInLLL
				(aProducer
					view:				vLLLasstProducer
					setCycle:		Walk
					setMotion:		MoveTo 219 122 self
				)
			)

			(producerTalkingLLL
				(aProducer
					loop:				(+ (aProducer loop?) 2)
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(comeOnLarryLLL
				(Print "\"Hey, you!\" cries the woman,
					\"Where have you been?
					We've been looking all over for you.\"")
				(Print "\"I just won 'The Dating Connection,'\" you reply." HIGH)
				(Print "\"Big deal.
					You were supposed to wait here.
					Now hurry and follow me!\"")
				(aProducer
					loop:				facingEast
					setMotion:		MoveTo 264 122 self
				)
			)

			(producerGoneLLL
				(User canInput:	TRUE)
				(if (== currentStatus NORMAL)
					(User canControl: TRUE)	
				)
				(aProducer 			dispose:)
				(Print "(Oh no, Larry. Here we go again!)" LOW)
				;**	Here we wait for Larry to walk out the doorway.
			)
		)
	)
)


(instance sittingScript of Script
	(method (changeState newState)
		(switch (= state newState)

			;**	(0) is unused

			(walkToSeat
				(HandsOff)
				(= cycles	0)
				(= seconds	0)
				(cond
					(( < (ego x?) 123)
						(ego
							setMotion:		MoveTo 123 (ego y?) self
						)
					)
					(( > (ego x?) 200)
						(ego
							setMotion:		MoveTo 200 (ego y?) self
						)
					)
					(else
						(self cue:)
					)
				)
			)

			(walkCloser
				(ego
					setMotion:		MoveTo (ego x?) 94 self
				)
			)

			(sittingDown
				(ego
					view:				vEgoSitting
					setLoop: 		0
					setCel: 			0
					cycleSpeed:		2
					setCycle: 		EndLoop self
				)
			)

			(seated
				(ego
					setCel:
					setMotion:		FALSE
					stopUpd:
				)
				(= currentStatus SITTING)
				(User
					canInput:		TRUE
;					canControl: 	FALSE
				)
				(= seconds (Random 2 6))
			)


			;**	Here we fidget while waiting for him to say \"Stand up.\"

			(fidgeting
				(ego setLoop: (Random 1 2))
				(if (== (ego cel?) 0)
					(ego setCycle: EndLoop)
				else
					(ego setCycle: BegLoop)
				)
				(-- state)
				(= seconds (Random 2 6))
			)

			(standingUp
				(HandsOff)
				(= cycles	0)
				(= seconds	0)
				(ego
					setLoop: 		0
					setCel:			LastCel
					setCycle: 		BegLoop self
					setMotion:		FALSE
				)
			)

			(standing
				(NormalEgo facingSouth)
				(ego
					observeControl:cYellow cLtMagenta
				)
			)
		)
	)
)

