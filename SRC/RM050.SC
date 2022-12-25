;**
;**	Logics for room 50 -- Aeroporto exterior
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 25, 1988
;**


(script#				50)

(public
	rm50				0
)


(define	nearDoor			121 127 202 133)
(define	nearKrishna 	121 111 205 149)
(define	nearCopWest		  9 156	46 183)
(define	nearCopEast		273 156 312 183)

;(define	vEgoBikiniStuffed			151)

;(define	vAirportExt					500)
;(define	vMP1							501)
;(define	vMP2							502)
;(define	vKrishna1					503)
;(define	vKrishna2					504)
	(define	lNormal			2)				;**	this loop in 501-504
	(define	lGrabbingEast	3)				;**		"	"	"	"	"
	(define	lGrabbingWest	4)				;**		"	"	"	"	"
	(define	lConfused		5)				;**		"	"	" only 503 & 504
	(define	lBigFace			6)				;**		"	"	" only 503

;(define	mKGBishna					117)


(local
	currentHench
	seenMessage
	henchStatus
	aDoor
	aRadar
	aHench1
	aHench2
)


(enum												;**	currentHench values
	nobody
	cop
	krishna
)

(enum												;**	henchStatus values
	WAITING_TO_CHASE
	CHASING
	CAUGHT_HIM

	NOT_THIS_TIME
	GAVE_FLOWER
)

(enum 											;**	copScript values
	copChasingEgo
	copAtEgo
	copGrabDone
)

(enum 											;**	krishnaScript values
	krishnaChasingEgo
	krishnaAtEgo
	krishnaGrabDone
)

(enum												;**	rmScript state values
	intro
	firstTime

	startFlower
	confusedKGB
	niceJob
)


(instance theSound of Sound
	(properties
	 	number:		mKGBishna
		loop:			-1
	)
)


(instance rm50 of Room
	(properties
		picture:		50
		horizon:		1
	)

	(method (init)
		(Load VIEW vAirportExt)
		(Load VIEW vMP1)
		(Load VIEW vMP2)

		(super init:)

		((View new:)							;**	"aeroporto" sign
			view: 			vAirportExt
			loop: 			0
			cel: 				0
			posn: 			155 63
			setPri: 			2
			addToPic:
		)
		((View new:)							;**	"eat" sign
			view: 			vAirportExt
			loop: 			0
			cel: 				1
			posn: 			11 64
			setPri: 			3
			addToPic:
		)
		((View new:)							;**	"shit" sign
			view: 			vAirportExt
			loop: 			0
			cel: 				2
			posn: 			308 63
			setPri: 			2
			addToPic:
		)

		((= aRadar (Prop new:))				;**	radar antenna
			view:				vAirportExt
			setLoop:			2
			setPri:			0
			posn:				285 3
			cycleSpeed:		1
			setCycle:		Forward
			isExtra:			TRUE
			init:
		)

		(= currentHench nobody)

		(if (== forceAtest TRUE)
			(= currentEgoView vEgoBikiniStuffed)
		)

		(NormalEgo)
		(cond
			((== prevRoomNum 52)		(ego	posn: 	159 122))
			(else							(ego	posn: 	159 186))
		)
		(ego
			init:
		)

		((= aDoor (AutoDoor new:))
			view:				vAirportExt
			setLoop:			1
			posn:				160 123
			setPri:			8
			entranceTo:		52
			doorCtrl:		cBlue
			doorBlock:		cYellow
			roomCtrl:		cGreen
			msgLook:			"Inside you see lines of businessmen waiting to buy
									tickets."
			init:
		)

		(self
			setRegions:		rgAirport
			setScript: 		rm50Script
		)

		(if (== currentEgoView vEgoBikiniStuffed)
			(= currentStatus NEAR_DEATH)
			(= henchStatus WAITING_TO_CHASE)
			(= currentHench cop)

			((= aHench1 (Actor new:))	;**		These will chase you.
				view:				vMP1
				loop:				lNormal
				posn:				22 165
				illegalBits:	cLtWhite
				setStep:			4 3
				init:
				setAvoider:		(Avoider new:)
			)
			((= aHench2 (Actor new:))
				view:				vMP2
				loop:				lNormal
				posn:				298 165
				illegalBits:	cLtWhite
				setStep:			4 3
				stopUpd:
				init:
				setAvoider:		(Avoider new:)
			)
		else
			((View new:)						;**		These cops are just addtopics.
				view:				vMP1
				loop:				lNormal
				posn:				22 165
				addToPic:
			)
			((View new:)
				view:				vMP2
				loop:				lNormal
				posn:				298 165
				addToPic:
			)
			(if (!= confusedKrishnas TRUE)
				(= henchStatus WAITING_TO_CHASE)
				(= currentHench krishna)
				
				(Load VIEW vKrishna1)
				(Load VIEW vKrishna2)
				(Load VIEW iFlower)
				(Load SOUND 		mKGBishna)
				(theSound			play:)

				((= aHench1 (Actor new:))
					view:				vKrishna1
					setLoop:			lNormal
					posn:				141 126
					illegalBits:	cLtWhite
					setCycle:		Forward
					init:
					setAvoider:		(Avoider new:)
				)
				((= aHench2 (Actor new:))
					view:				vKrishna2
					setLoop:			lNormal
					posn:				181 126
					illegalBits:	cLtWhite
					setCycle:		Forward
					init:
					setAvoider:		(Avoider new:)
				)
				(aBigFace
					view:				vKrishna1
					setLoop:			lBigFace
					posn:				160 1055
					setPri:			15
					setCycle:		Forward
					init:
				)
			)
		)
		(if currentHench
			(aDoor
				locked:	TRUE
			)
		)
	)
)


(instance rm50Script of Script
	(method (doit)
		(super doit:)

		(cond
			((and	(== henchStatus WAITING_TO_CHASE)
					(== currentHench cop)
					( > 180 (ego y?)))
				(= henchStatus CHASING)		;**	bring on cops
				(aHench1	setScript: (copScript new:))
				(aHench2	setScript: (copScript new:))
				(Print "\"HALT,\" cries the military policeman,
					\"or I'll shoot!!\"" HIGH)
			)
			((and	(== henchStatus WAITING_TO_CHASE)
					(== currentHench krishna)
					(& (ego onControl:) cBlue))
				(= henchStatus CHASING)		;**	bring on Krishnas
				(aHench1	setScript: (krishnaScript new:))
				(aHench2	setScript: (krishnaScript new:))
			)
			((& (ego onControl:) cCyan)
				(if (== seenMessage FALSE)
					(= seenMessage TRUE)
					(Print "Have you forgotten your vow already?
						You swore you'd never go that way again!")
				)
			)
			(else
				(= seenMessage FALSE)
			)
		)
	)


	(method (handleEvent event &tmp obj)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(cond
			((Said 'look>')
				(cond
					((Said '/man')
						(cond
							((== currentHench krishna)
								(Print "There are several men here:
									two airport security guards
									and two \"other.\"")
							)
							((== currentHench cop)
								(Print "These airport policemen look capable of almost
									anything... except intelligent thought!")
							)
							(else
								(Print "The guards are as silent as statues.")
							)
						)
					)

					((Said '/krishna, other')
						(if (== currentHench krishna)
							(aBigFace posn: 160 55)
							(Timer setReal: aBigFace 5)
							(HandsOff)
						else
							(Print "Where?")
						)
					)

					((Said '/guard')
						(if (== currentHench cop)
							(Print "These airport policemen look capable of almost
								anything... except intelligent thought!")
						else
							(Print "The guards are as silent as statues.")
						)
					)

					((Said '[/room, building]')
						(Print "At last, you've reach the resort airport, and...
							what a disappointment.")
						(Print "There are two security guards patrolling here.")
						(if (!= confusedKrishnas TRUE)
							(Print "Near the door, there are two men wearing robes.")
						)
					)
				)
			)

			((Said 'talk>')
				(cond
					((Said '/man')
						(cond
							((== currentHench krishna)
								(Print "There are several men here:
									two airport security guards
									and two \"other.\"")
							)
							((== currentHench cop)
								(Print "These airport policemen look capable of almost
									anything... except intelligent thought!")
							)
							(else
								(Print "The guards are as silent as statues.")
							)
						)
					)

					((Said '/krishna, other')
						(if (== currentHench krishna)
							(Print "\"Hey, flower child,\" you shout,
								\"don't you love my long hair?\"")
							(Print "They respond, \"Hmmmmmmmmmmmmm.\"")
						else
							(Print "Where?")
						)
					)

					((Said '/guard')
						(cond
							((== currentHench cop)
								(Print "\"Oh, officer!\" you yell.
									\"How do you like men in bikinis?\"")
							)
							((or	(ego inRect: nearCopWest)
							 		(ego inRect: nearCopEast))
								(Print "\"Say, can you tell me how to buy an airline
									ticket?\" you ask him. 
									\"Does that line never end?\"")
								(Print "Believing this to be an audition for duty
									at Buckingham Palace, 
									he stares back at you with no response.")
							)
							(else
								(NotClose)
							)
						)
					)
				)
			)

			((Said 'wear, change, (take<off), remove, 
											(put<on)/swimsuit, clothes, suit, bikini')
				(Print "You can't change here! 
					You should have thought of that earlier.")
			)

			((Said 'play/music, onklunk')
				(Print "\"Want to hear a favorite from the Peruvian Top 40?\"
					you ask them.")
				(Print "Evidently not, as there is no response!")
			)

			((Said 'give, throw, bribe, hand, put, pay, pay, drop>')
				(= obj (inventory saidMe:))
				(cond
					((!= currentStatus NORMAL)
						(NotNow)
					)
					((or	(not obj)
							(not (ego has: (inventory indexOf: obj))))
					 	(event claimed: FALSE)
					)
					((and (!= confusedKrishnas TRUE)
							( < 25 (ego distanceTo: aHench1))
							( < 25 (ego distanceTo: aHench2)))
						(NotClose)
					)
					((== (inventory indexOf: obj) iMoney)
						(Print "They're not interested in your money.
							Be more creative.")
					)
					((!= (inventory indexOf: obj) iFlower)	;**	not interested
						(Print "They're not interested in that.")			
					)
					(else							;**	a hit!
						(Ok)
						(self changeState: startFlower)
					)
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(++ timesInRm50)
				(if (== timesInRm50 1)
					(= cycles 5)
				else
					(= seconds (Random 3 12))
				)
			)

			(firstTime
				(switch timesInRm50
					(1 	(Print "Boy are you happy to have made it past those
								cliffs. You swear never to return THAT way!"))
					(2		(Print "\"Mr. Skirvin. White courtesy phone, please,\"
								says the PA announcer."))
					(3		(Print "\"Paging Ms. Borucki; Ms. Bonnie Barucki,
								please,\" booms over the PA system."))
					(4		(Print "\"Mr. Herring; your prescription is ready!\""))
					(else	(Print "\"The white zone is for passenger loading and
								unloading only.\""))
				)
			)


			;**	This handles giving the flower to the Krishna Kids...

			(startFlower
				(= currentHench nobody)
				(= henchStatus GAVE_FLOWER)
				(= confusedKrishnas TRUE)
				(theSound stop:)
				(aHench1
					setCel:			0
				)
				(aHench2	
					setCel:			0
				)
				(HandsOff)
				(Print "\"Here, my little flower child,\"
					you tell the KGBishna,
					\"make love, not money!!\"" HIGH draw:
					icon: iFlower 0 0
				)
				(ego
					put: iFlower 	LIMBO
				)
				(= seconds 3)
			)

			(confusedKGB
				(aHench1
					illegalBits:	0
					setCycle:		Walk
					setLoop:			lConfused
					setMotion:		MoveTo 333 (aHench1 y?)
				)
				(aHench2
					illegalBits:	0
					setCycle:		Walk
					setLoop:			lConfused
					setMotion:		MoveTo 333 (aHench2 y?)	self
				)
				(Print "This so confuses the KGBishna agents that
					they stumble off towards the native rental car agencies,
					contemplating the beauty of a single flower and their navels!"
						icon: vKrishna2 lNormal 0 HIGH draw: dispose:)
				(theGame changeScore: 7)
				(aDoor	locked:	FALSE)
			)

			(niceJob
				(cls)
				(Print "(Nice going, Larry.
					You've succeeded in bypassing the KGBers again!)" LOW draw:)
				(User
					canControl:		TRUE
					canInput:		TRUE
				)
			)
		)
	)
)


(instance copScript of Script
	(method (changeState newState &tmp tmpX tmpY)
		(switch (= state newState)

			(copChasingEgo
				(client
					setLoop:		RELEASE
					setMotion:	Chase ego 11 self
					setCycle:	Walk
				)
				(HandsOff)
			)

			(copAtEgo
				(if (== henchStatus CHASING)
					(Print "\"All right, you pervert!\" cries the MP.
						\"We've got laws against people like you!
						We don't want no cross-dressers
						hanging around our airport!\"" HIGH)
					(= currentStatus NEAR_DEATH)
					(= henchStatus CAUGHT_HIM)
					(= tmpY (- (ego y?) 1))
					(if ( < (ego x?) (client x?))
						(= tmpX ( + (ego x?) 19))
					else
						(= tmpX ( - (ego x?) 19))
					)
					(client
						ignoreActors:
						posn:				tmpX tmpY
					)
					(ego
						setMotion:		FALSE
						setLoop:			(if (> tmpX (ego x?))
												facingEast
											else
												facingWest
											)
						ignoreActors:
						stopUpd:
					)
					(= seconds 3)
				)
			)

			(copGrabDone
				(Print "Next time, you'd better dress a little more
					formally for a visit to the airport, Larry!")
				(= currentStatus DEAD)
			)
		)
	)
)


(instance krishnaScript of Script
	(method (changeState newState &tmp tmpX tmpY)
		(switch (= state newState)

			(krishnaChasingEgo
				(client
					setLoop:		RELEASE
					setMotion:	Chase ego 11 self
					setCycle:	Walk
				)
				(HandsOff)
			)

			(krishnaAtEgo
				(if (== henchStatus CHASING)
					(= currentStatus NEAR_DEATH)
					(= henchStatus CAUGHT_HIM)
					(HandsOff)
					(= tmpY (- (ego y?) 2))
					(if ( < (ego x?) (client x?))
						(= tmpX ( + (ego x?) 9))
					else
						(= tmpX ( - (ego x?) 9))
					)
					(client
						loop:				(+ 3 (client loop?))
						cel:				0
						ignoreActors:
						posn:				tmpX tmpY
						setCycle:		EndLoop self
						setStep:			4 3
					)
					(ego
						setLoop:			(if (> tmpX (ego x?))
												facingEast
											else
												facingWest
											)
						stopUpd:
					)
					(Print "\"Hmmmmmmmmmmmmmmm,\" chants the
						strangely dressed KGB agent,
						\"have we got a cult for you!!\"" HIGH draw:)
				)
			)

			(krishnaGrabDone
				(Print "If only you could offer them some token of peace
					and beauty!" draw:)
				(Print "Once again you've allowed the KGB agents to recognize
					and capture you.
					Now we'll run by their place to see some of the fun things
					that you'll get to enjoy..." HIGH draw:)
				(= currentStatus HEADING_FOR_DEATH_SCENE)
				(curRoom newRoom: 96)
			)
		)
	)
)

(instance aBigFace of Prop
	(method (cue)
		(Print "You find women sexy." draw:)
		(Print "(But what is this?!)" LOW)
		(self posn: 160 1055)
		(HandsOn)
	)
)
