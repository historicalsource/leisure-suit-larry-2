;**
;**	Logics for room 57 -- Airport waiting room
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 15, 1988
;**


(script#				57)

(public
	rm57				0
)


(define	nearGateAttendant		185 140 244 152)
(define	nearPamphlet			159 140 195 152)

;(define	vAirplane					511)
;(define	vWaiting						525)
	(define	lAddToPix		0)
	(define	lKillerKids 	1)
	(define	lJumpingJack 	2)
	(define	lLine				3)
	(define	lTumblingEast	4)
	(define	lTumblingWest	5)
	(define	lDoor				6)


(local
	seenMessage
	aSidewalkNorth
	aSidewalkSouth
	aPlane
	aKid1
	aKid2
	aKid3
	aDoor
)


(enum												;**	tumbleScript state values
	tumbleInit
	tumbleEast
	rest
	tumbleWest
	tumbleDone
)

(enum												;**	rmScript state values
	intro
	offBeltway

	startSliding
	slideDone

	startAirlineTicket
	pastDesk
	inDoor
	ticketDone
)


(instance rm57 of Room
	(properties
		picture:		57
		horizon:		1
	)

	(method (init)
		(Load VIEW vWaiting)
		(Load VIEW vAirplane)

		(super init:)

		((View new:)							;**	mom
			view: 			vWaiting
			loop: 			lAddToPix
			cel: 				0
			posn: 			112 141
			setPri: 			10
			addToPic:
		)
		((View new:)							;**	skater
			view: 			vWaiting
			loop: 			lAddToPix
			cel: 				1
			posn: 			234 161
			setPri: 			11
			addToPic:
		)
		((View new:)							;**	counterPerson
			view: 			vWaiting
			loop: 			lAddToPix
			cel: 				3
			posn: 			196 131
			setPri: 			10
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	monitors
			view: 			vWaiting
			loop: 			lAddToPix
			cel: 				2
			posn: 			197 112
			setPri: 			10
			ignoreActors:
			addToPic:
		)

		((= aPlane (Airplane new:))
			view:				vAirplane
			setLoop:			2
			setCel:			0
			startX:			-100
			startY:			14
			endX:				555
			endY:				14
			init:
		)

		((= aDoor (Door new:))
			view:				vWaiting
			setLoop:			lDoor
			posn:				163 138
			setPri:			9
			entranceTo:		0
			doorCtrl:		0
			roomCtrl:		0
			init:
			locked:			TRUE
			doorState:		doorClosed
			msgLook:			"This door is controlled by the gentleman behind
									the counter.
									He'll unlock it for you if you'll show him
									a confirmed ticket for the next flight."
			msgLookLock:	"Right now, it's locked up tight!"
			msgLocked:		"This door is controlled by the gentleman behind
									the counter.
									He'll unlock it for you if you'll show him
									a confirmed ticket for the next flight."
			msgExcept:		"Have the man at the desk open it for you!"
		)

		((= aKid1 (Extra new:))				;**	JumpingJack
			view:				vWaiting
			loop: 			lJumpingJack
			posn: 			92 131
			setPri: 			10
			cycleSpeed:		1
			minPause:		20
			maxPause:		30
			minCycles:		11
			maxCycles:		33
			init:
		)
		((= aKid2 (Extra new:))				;**	killerKids
			view:				vWaiting
			loop: 			lKillerKids
			posn: 			127 140
			setPri: 			10
			cycleSpeed:		2
			minPause:		29
			maxPause:		100
			minCycles:		20
			maxCycles:		30
			init:
		)

		((= aKid3 (Prop new:))				;**	rollingKid
			view:				vWaiting
			setLoop: 		lTumblingEast
			posn: 			100 158
			setPri: 			ltRed
			cycleSpeed:		0
			init:
			setScript:		tumbleScript
		)

		((= aSidewalkNorth (Actor new:))	;**	moving sidewalk going away
			view:				vWaiting
			setLoop:			lLine
			setCel:			3
			setPri:			0
			setStep:			1 1
			posn:				192 181
			illegalBits:	0
			ignoreActors:
			init:
			setScript:		sidewalkNorthScript
		)
		((= aSidewalkSouth (Actor new:))	;**	moving sidewalk coming towards us
			view:				vWaiting
			setLoop:			lLine
			setCel:			3
			setPri:			0
			setStep:			1 1
			posn:				135 179
			illegalBits:	0
			ignoreActors:
			init:
			setScript:		sidewalkSouthScript
		)

		(= currentStatus RIDING_SIDEWALK)
		(HandsOff)
		(ego
			illegalBits:	0
			setPri:			RELEASE
			setLoop:			facingNorth
			setCycle:		FALSE
			setStep:			1 1
			posn:				196 234
			setCel:			0
			init:
		)

		(self
			setRegions:		rgAirport
			setScript: 		rm57Script
		)
	)
)


(instance rm57Script of Script
	(method (doit)
		(super doit:)

		(cond
			((& (ego onControl:) cRed)
				(self changeState: startSliding)
			)
			((& (ego onControl:) cCyan)
				(if (and	(== currentStatus NORMAL)
							(== seenMessage FALSE))
					(= seenMessage TRUE)
					(Print "\"Hey! Stay off that!\" shouts the gate attendant.")
				)
			)
			(else
				(= seenMessage FALSE)
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (or	(Said 'give, hand, show/ticket/man, agent')
					(Said 'give, hand, show/agent, man/ticket')
					(Said 'give, hand, show, use/ticket'))
			(cond
				((not (ego has: iAirlineTicket))
					(DontHave)
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				((not (ego inRect: nearGateAttendant))
					(NotClose)
				)
				(else
					(Print "\"Here's my ticket,\" you say, handing your
						ticket folder to the gate attendant,
						\"is this Gate #1?\"")

					(if (== missedPlane TRUE)
						(Print "\"It certainly is,\" he replies,
							\"but this ticket is for a flight that took off
							30 seconds ago. Too bad you missed it!\"")
						(Print (Format @string
							"\"Oh, well,\" says the gate attendant.
							\"%s\"" tritePhrase))
					else
						(self changeState: startAirlineTicket)
					)
				)
			)
		)

		(if (and	(ego inRect: nearGateAttendant)
					(Said 'talk/man, clerk'))
			(Print (Format @string
				"\"Good day, sir,\" you tell the gate attendant.
				%s" laffer))

			(if (not (ego has: iAirlineTicket))
				(Print "\"May I please purchase a ticket?\"")
				(Print "He responds,
					\"Tickets are only available at the front desk.\"")
			else
				(Print "\"How nice for you,\" he responds.
					\"Are you going to give me your ticket or did you ride that 
					walkway just so we could have this stimulating conversation?\"")
			)
		)

		(if (Said 'get/pamphlet')
			(cond
				((!= currentStatus NORMAL)
					(NotNow)
				)

				((not ((inventory at: iPamphlet) ownedBy: curRoomNum))
					(Print "Nah. One of these is enough!")
				)

				((not (ego inRect: nearPamphlet))
					(NotClose)
				)

				(else
					(ego get: iPamphlet)
					(theGame changeScore: 11)
					(Print "That's a good idea.
						Take a little free reading material to help you wile
						away your flight time!
						You grab a brochure from the display and
						shove it in your pocket, promising to read it when you
						get to your seat.
						It seems the perfect choice for a long, boring flight.")
				)
			)
		)

		(if (Said 'look>')
			(if (Said '/man, clerk')
				(Print "The man by the window is totally absorbed by
					the fascinating baggage handlers.")
				(Print "But... behind the counter waits the man you came to
					see: the gate attendant.")
			)

			(if (Said '/counter')
				(if ((inventory at: iPamphlet) ownedBy: curRoomNum)
					(Print "There is a computer terminal that is out of order
						and a display of religious pamphlets on the left end of
						the counter.")
				else
					(Print "There is nothing else of interest on the counter.")
				)
			)

			(if (Said '/pamphlet')
				(Print "There is a display on the left end of the counter
					filled with religious tracts.")
			)

			(if (Said '/kid, woman')
				(if (> filthLevel CLEAN)
					(Print "That woman looks like
						she almost screwed herself out of a seat!")
				else
					(Print "The woman seems quite busy with her children.")
				)
			)

			(if (Said '/screen')
				(Print "All flights are on time.")
				(Print "(Remember: this IS a fantasy!)" LOW)
			)

			(if (Said '[/room]')
				(Print "There is an airline attendant waiting behind the counter,
					and a locked door that you presume leads to the jetway --
					and your escape!")
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro								;**	Larry's entrance from rm56.
				(ego
					setCel:		0
					setMotion:	MoveTo 186 175 self
				)
			)

			(offBeltway
				(NormalEgo facingNorth)
				(ego
					observeControl:cGreen cYellow
				)
			)


			;**	Oops; he's chosen to return to the snack bar...

			(startSliding
				(= currentStatus RIDING_SIDEWALK)
				(HandsOff)
				(ego
					illegalBits:	0
					setLoop:			facingSouth
					setCel:			4
					setStep:			1 1
					setMotion:		MoveTo 129 234 self
				)
			)

			(slideDone
				(curRoom newRoom: 55)
			)


			;**	Give agent the ticket, and head for the plane...

			(startAirlineTicket
				(HandsOff)
				(theGame changeScore: 3)
				(Print "\"It certainly is,\" says the gate attendant,
					\"but if you want to take the flight listed on this ticket,
					you'd better hurry.
					I've already announced the final boarding call.\"")
				(Print (Format @string
					"\"The stewardess onboard will give you your seat assignment,\"
					says the gate attendant.
					\"%s\"" tritePhrase))
				(aDoor
					locked:			FALSE
					force:			TRUE
					open:
				)
				(SetRgTimer 0 0 0)
				(ego
					setMotion:		MoveTo 151 145 self
				)
			)

			(pastDesk
				(ego
					setMotion:		MoveTo 151 133 self
				)
			)

			(inDoor
				(aDoor
					setCycle:		BegLoop
				)
				(ego
					illegalBits:	0
					setPri:			5
					setMotion:		MoveTo 199 133 self
				)
			)

			(ticketDone
				(curRoom newRoom: 58)
			)
		)
	)
)


(instance sidewalkNorthScript of Script
	(method (changeState newState)
		(switch (= state newState)
			(0	(aSidewalkNorth	setMotion:	MoveTo 192 179 self))
			(1	(aSidewalkNorth	posn:			192 222)
				(self changeState: 0))
		)
	)
)


(instance sidewalkSouthScript of Script
	(method (changeState newState)
		(switch (= state newState)
			(0	(aSidewalkSouth	setMotion:	MoveTo 135 202 self))
			(1	(aSidewalkSouth	posn:			135 179)
				(self changeState: 0))
		)
	)
)


(instance tumbleScript of Script
	(method (changeState newState &tmp tumbleX tumbleY)
		(switch (= state newState)

			(tumbleInit
				(aKid3
					setLoop:			lTumblingEast
					cel:				0
				)
				(= seconds (Random 2 5))
			)

			(tumbleEast
				(aKid3
					cycleSpeed:		(Random 0 2)
					setCycle:		EndLoop self
				)
			)

			(rest
				(aKid3
					setLoop:			lTumblingWest
					cel:				0
				)
				(= seconds (Random 2 5))
			)

			(tumbleWest
				(aKid3
					cycleSpeed:		(Random 0 2)
					setCycle:		EndLoop self
				)
			)

			(tumbleDone
				(self changeState: tumbleInit)
			)
		)
	)
)

