;**
;**	Logics for room 53 -- Customs
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 20, 1988
;**


(script#				53)

(public
	rm53				0
)


(define	nearAgent	171 143 198 149)

;(define	vEgoBomb						155)
	(define	lGrabBag			0)
	(define	lWalk				1)
	(define	lCleanSelf		2)

;(define	vCustoms						513)
	(define	lDoor					0)
	(define	lPlane				6)

;(define	vCustomsAgent				514)
	(define	lAgentNear			0)
	(define	lAgentFar			1)
	(define	lAgentSearching	2)
	(define	lWomanWalking		3)
	(define	lWomanSuitcase		4)
	(define	lManWalking			5)
	(define	lManSuitcase		6)


(local
	work
	aPlane
	aDoor
	aConveyor1
	aConveyor2
	aConveyor3
	aConveyor4
	aAgentFar
	aAgentNear
	aTraveler
)


(enum												;**	rmScript state values
	intro
	doCustomsGag
	startAgent
	excellentLikeness
	startTalking
	showAll
	showInventory
	startHim
	customsDone

	bombInit
	toAgent
	pastAgent
)

(enum												;**	traveler script
	travelerInit
	travelerStart
	travelerIn
	travelerSuitcaseUp
	searchTraveler
	searchOver
	travelerTakeSuitcase
	travelerLeaves
	travelerGone
)


(instance theSound of Sound
	(properties
		number:		sTicking
		loop:			-1
	)
)


(instance rm53 of Room
	(properties
		picture:		53
		horizon:		5
		east:			54
		west:			52
	)

	(method (init)
		(Load VIEW vCustoms)
		(Load VIEW vCustomsAgent)
		(Load VIEW vAirplane)

		(super init:)

		((View new:)							;**	advertisement
			view: 			vCustoms
			loop: 			1
			cel: 				0
			posn: 			25 158
			setPri: 			12
			addToPic:
		)
		((View new:)							;**	plants
			view: 			vCustoms
			loop: 			1
			cel: 				1
			posn: 			73 58
			setPri: 			10
			addToPic:
		)
		((View new:)							;**	painting in rm54
			view: 			vCustoms
			loop: 			6
			cel: 				0
			posn: 			301 118
			setPri: 			11
			addToPic:
		)

		((= aConveyor4 (Prop new:))
			view:				vCustoms
			setLoop:			5
			setPri:			3
			posn:				198 60
			cycleSpeed:		1
			setCycle:		Forward
			isExtra:			TRUE
			init:
		)
		((= aConveyor3 (Prop new:))
			view:				vCustoms
			setLoop:			4
			setPri:			4
			posn:				206 70
			cycleSpeed:		1
			setCycle:		Forward
			isExtra:			TRUE
			init:
		)
		((= aConveyor2 (Prop new:))
			view:				vCustoms
			setLoop:			3
			setPri:			4
			posn:				218 97
			cycleSpeed:		1
			setCycle:		Forward
			isExtra:			TRUE
			init:
		)
		((= aConveyor1 (Prop new:))
			view:				vCustoms
			setLoop:			2
			setPri:			10
			posn:				239 132
			cycleSpeed:		1
			setCycle:		Forward
			isExtra:			TRUE
			init:
		)
		((= aAgentNear (Prop new:))		;**	CustomsAgent for ego to deal with
			view:				vCustomsAgent
			setLoop:			lAgentNear
			setCel:			0
			setPri:			10
			posn:				187 124
			stopUpd:
			init:
		)
		((= aAgentFar (Prop new:))			;**	smallCustomsAgent in distance
			view:				vCustomsAgent
			setLoop:			lAgentFar
			setCel:			0
			setPri:			1
			posn:				184 47
			stopUpd:
			init:
		)
		((= aTraveler (Actor new:))		;**	male/female Walking
			view:				vCustomsAgent
			setLoop:			lWomanWalking
			setPri:			2
			posn:				128 37
			setStep:			1 1
			illegalBits:	0
			ignoreActors:
			init:
			hide:
		)

		((= aPlane (Airplane new:))
			view:				vAirplane
			setCel:			0
			startX:			208
			startY:			23
			endX:				1
			endY:				23
			init:
		)

		(cond
			((== prevRoomNum 54)		(ego	posn: 	316 154))
			((== prevRoomNum 52)		(ego	posn: 	51 120))
			(else							(ego	posn: 	51 120))
		)

		(self
			setRegions:		rgAirport
			setScript: 		rm53Script
		)

		(if (!= bombStatus TICKING)
			(NormalEgo)
		else
			(Load SOUND sTicking)
			(theSound		play:)
			(HandsOff)
			(= currentStatus BOMBING)
			(rm53Script changeState: bombInit)
		)

		(ego					init:)

		((= aDoor (AutoDoor new:))
			view:				vCustoms
			setLoop:			lDoor
			posn:				293 157
			setPri:			11
			roomCtrl:		0
			locked:			(if (== prevRoomNum 54) FALSE else TRUE)
			msgLook:			"There is a barely perceptible, blue gate in the
									east wall under that painting."
			msgLookLock:	"The gate is controlled by the Customs Official
									standing behind the counter."
			msgLocked:		"The gate is locked. Try talking to the friendly
									looking gentleman behind the counter."
			msgExcept:		"...except it's locked!"
			msgFunny:		"Most people never knock on a gate!"
			msgCloser:		"When the man releases the gate, it opens just by
									walking near it."
			init:
		)
	)
)


(instance rm53Script of Script
	(method (doit)
		(super doit:)

		(if (& (ego onControl: origin) cGreen)
			(curRoom newRoom: 52)
		)
	)


	(method (handleEvent event &tmp obj)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'give, show, throw, put, drop>')
			(= obj (inventory saidMe:))
			(cond
				((or	(not obj)
						(not (ego has: (inventory indexOf: obj))))
				 	(event claimed: FALSE)
				)
				((not (ego inRect: nearAgent))
					(NotClose)
				)
				((== (inventory indexOf: obj) iKnife)
					(Print "You'd better not let him see that!")
				)
				((!= (inventory indexOf: obj) iPassport)
					(Print "That won't help you with this guy.")
				)
				((== currentEgoView vEgoBlond)
					(Print "\"Here ya go, buddy,\" you tell him,
						\"but remember: this picture was taken before my
						recent hair rejuvenation.\"")
					(Print "\"I'm sorry, Senor,\" he responds,
						\"but this is not your passport.
						I'm afraid I cannot allow you to pass this point.\"")
				)
				((not seenCustomsJoke)
					(= seenCustomsJoke TRUE)
					(theGame changeScore: 5)
					(self changeState: doCustomsGag)
				)
				(else
					(if (ego has: iKnife)
						(Print "\"You again?!
							Did you get rid of that knife?\"")
						(Print "\"Of course, would I lie to you?\"
							you say unabashed.")
						(Print (Format @string
							"\"Ok, I've seen enough of your stuff!
							Go on through, but please remember:
							%s\"" tritePhrase))
					else
						(Print (Format @string
							"\"You again?! I've seen enough of your stuff!
							Just go on through; and remember:
							%s\"" tritePhrase))
					)
					(aDoor locked: FALSE)
				)
			)
		)

		(if (Said 'look>')
			(if (Said '/man, agent')
				(Print "A gentleman with a pleasant appearance and friendly
					smile waits by the only open Customs Inspection counter.")
			)

			(if (Said '/picture')
				(Print "Those raisins are EVERYWHERE!")
			)

			(if (Said '/belt, open, hole')
				(Print "The conveyor belts disappear through the wall
					to the next room, where they are thoroughly inspected by
					sharp-eyed Customs Agents.")
			)

			(if (Said '[/room, building, belt]')
				(Print "For such a small airport, they certainly have an
					impressive Customs Inspection station.")
				(Print "There is a barely perceptible gate in the far wall.")
			)
		)

		(if (or	(Said '(crawl<into), ride/open, hole, belt')
					(Said 'use, (climb<on), ride/belt'))
			(Print "You can't. The conveyor belts are for luggage only!")
		)

		(if (Said '/rope')
			(Print "The rope is firmly affixed, and won't help you get by here.")
		)

		(if (Said 'open/gate, door')
			(Print "You can't.
				It's controlled by the Customs Agent behind the counter.")
		)

		(if (Said 'talk/man, agent')
			(Print (Format @string
				"\"Good day, Senor,\" you say to the man.
				%s" laffer))
			(Print "\"Good day, Sir,\" he replies in like fashion.
				\"May I please see your passport?\"")
		)
	)


	(method (changeState newState &tmp i theObj)
		(switch (= state newState)

;**				(intro)

			(doCustomsGag
				(HandsOff)
				(ego
					setLoop:			facingNorth
				)
				(Print "You hand the agent your passport.")
				(= seconds 3)
			)

			(startAgent
				(aAgentNear setCycle: Forward)
				(= seconds 3)
			)

			(excellentLikeness
				(aAgentNear setCel: 0)
				(Print "\"This picture is an excellent likeness,\"
					says the Customs Agent.
					\"It must be printed on photographic paper by Kodork!\""
						draw:)
				(= seconds 3)
			)

			(startTalking
				(aAgentNear setCycle: Forward)
				(= seconds 3)
			)

			(showAll
				(aAgentNear setCel: 0)
				(Print "\"Now, I must inspect your possessions,\" he says.
					\"Please show me everything you are carrying.\"" draw:)
				(= seconds 3)
			)

			(showInventory
				(Print "\"Well, ok, I guess,\" you reply,
					\"although this is highly unusual in an adventure game...\"")
				(for 	((= i (inventory first:))) 
						i 
						((= i (inventory next: i)))
					(= theObj (NodeValue i))
					(if (theObj ownedBy: ego)
						(theObj showSelf:)
					)
				)
				(Print "\"There. Are you satisfied?\" you ask.")
				(= seconds 3)
			)

			(startHim
				(aAgentNear setCycle: Forward)
				(= seconds 3)
			)

			(customsDone
				(User
					canControl:		TRUE
					canInput:		TRUE
				)
				(aAgentNear
					setCel: 			0
					stopUpd:
				)
				(ego
					setLoop:			RELEASE
				)
				(if (ego has: iKnife)
					(Print "\"Yes, but I must warn you about carrying that
						knife on board any aircraft.\"")
					(Print "\"Ok,\" you lie. \"You have my word on it!\"")
				)
				(Print "\"All right,\" he says,
					unlocking the gate in the east wall,
					\"you may pass.\"" draw:)
				(Print "\"Hey, thanks a lot, buddy,\" you say,
					peeling a hundred off your wad of bills,
					\"and here's a little something for you and the missus!\"")
				(Print (Format @string
					"\"I'm not your buddy, you ugly Americano.
					%s\"" tritePhrase))
				(aDoor locked: FALSE)
			)


			;**	This path is used when he enters this room from rm54 carrying
			;**	the suitcase bomb.

			(bombInit
				(ego
					setStep:		4 3			;**	hump it out of here!
					setLoop:		lWalk
					setMotion:	MoveTo 222 154 self
				)
			)

			(toAgent
				(Print "\"LOOK OUT, I'VE GOT A BOMB!!!\" you shout.")
				(ego
					setMotion:	MoveTo 157 150 self
				)
			)

			(pastAgent
				(Print (Format @string
					"Of course, the agent casually replies,
					\"%s\"" tritePhrase))
				(ego
					setMotion:	MoveTo 43 127 self
				)
			)
		)
	)
)


(instance travelerScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(travelerInit						;**	agent sitting bored
				(= seconds 3)
			)

			(travelerStart
				(aTraveler
					posn:			128 37
					show:
					setCycle:	Walk
					setMotion:	MoveTo 182 37 self
				)
			)

			(travelerIn
				(aTraveler
					setLoop:		( + (aTraveler loop?) 1)
					cel:			0
					setCycle:	EndLoop self
				)
			)

			(travelerSuitcaseUp
				(aAgentFar
					setCycle:	EndLoop self
				)
			)

			(searchTraveler
				(aAgentFar
					setLoop:		lAgentSearching
					setCycle:	Forward
				)
				(= seconds 10)
			)

			(searchOver
				(aAgentFar
					setLoop:		lAgentFar
					setCel:		LastCel
					setCycle:	BegLoop self
				)
			)

			(travelerTakeSuitcase
				(aAgentFar		stopUpd:)
				(aTraveler
					setCycle:	BegLoop self
				)
			)

			(travelerLeaves
				(aTraveler
					setLoop:		( - (aTraveler loop?) 1)
					setCycle:	Walk
					setMotion:	MoveTo 203 37 self
				)
			)

			(travelerGone
				(aTraveler						;**	perform sex change!
					setLoop:		(if (== (aTraveler loop?) lWomanWalking)
										lManWalking
									else
										lWomanWalking
									)
				)
				(self changeState: travelerInit)
			)
		)
	)
)

