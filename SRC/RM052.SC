;**
;**	Logics for room 52 -- Aeroporto Ticket Counter
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 25, 1988
;**


(script#				52)

(public
	rm52				0
)


(define	msgShift		"The line quickly shifts around you, 
								ignoring your attempt to butt in.")

(define	gotoRm53		279 100 333 137)	;**	change rooms to right
(define	nearAgent	154 118 181 129)

(define	blueX					109)			;**	leftmost X of this line
(define	greenX				151)
(define	cyanX					197)
(define	lineY					141)
(define	turnHereX			220)

(define	offset1				10)			;**	lineX +x for walker approach
(define	offset2				13)			;**	walker at front of line
(define	offset3				26)			;**	walker comes forward

(define	startingY			228)			;**	walkers in Y's
(define	decisionY			166)
(define	endOfLineY			149)
(define	walkPastY			154)
(define	counterY				125)

(define	doorwayX				282)
(define	doorwayY				129)

;(define	vEgoBomb						155)
	(define	lGrabBag			0)
	(define	lWalk				1)
	(define	lCleanSelf		2)

;(define	vTickets						508)
;(define	vTicketAgents				509)
;(define	vTicketLines				510)
;(define	vTicketExplosion			512)
;(define	vPassenger					608)


(local
	work
	aBlueAgent
	aGreenAgent
	aCyanAgent
	aBlueLine
	aGreenLine
	aCyanLine
	aCustomer

	activeLine
	agentHere
)


(enum												;**	ticketScript state values
	ticketInit
	walkOneIn
	pickAline

	doGreenLine
	atGreenLine
	greeniesShift
	greenLeaving
	greenLeaving2
	greenLeaving3

	doBlueLine
	atBlueLine
	blueiesShift
	blueLeaving
	blueLeaving2
	blueLeaving3

	doCyanLine
	atCyanLine
	cyaniesShift
	cyanLeaving
	headingForHallway
	allDone
)

(enum												;**	rmScript state values
	intro

	bombInit
	lookOut
	nobodyMoves
	walkOff
	boom

	justBombed
	punchLine
	bombDone

	buyTicket
	whereTo
	anywhereBut
	youAreInLuck
	continueNow
	agentTalks1
	smokingOr
	agentTalks2
	touchee
)


(instance theSound of Sound
	(properties
		number:		sTicking
		loop:			-1
	)
)


(instance rm52 of Room
	(properties
		picture:		52
		horizon:		5
		south:		50
		east:			53
		west:			51
	)

	(method (init)
		(Load VIEW vTickets)
		(Load VIEW vTicketAgents)

		(super init:)

		((View new:)							;**	leftComputer
			view: 			vTickets
			loop: 			0
			cel: 				1
			posn: 			105 100
			setPri: 			8
			addToPic:
		)
		((View new:)							;**	centerComputer
			view: 			vTickets
			loop: 			0
			cel: 				1
			posn: 			157 100
			setPri: 			8
			addToPic:
		)
		((View new:)							;**	rightComputer
			view: 			vTickets
			loop: 			0
			cel: 				0
			posn: 			210 100
			setPri: 			8
			addToPic:
		)
		((View new:)							;**	counter1
			view: 			vTickets
			loop: 			0
			cel: 				2
			posn: 			131 100
			setPri: 			8
			addToPic:
		)
		((View new:)							;**	counter2
			view: 			vTickets
			loop: 			0
			cel: 				3
			posn: 			185 100
			setPri: 			8
			addToPic:
		)
		((View new:)							;**	monitor1
			view: 			vTickets
			loop: 			1
			cel: 				0
			posn: 			128 65
			setPri: 			5
			addToPic:
		)
		((View new:)							;**	monitor2
			view: 			vTickets
			loop: 			1
			cel: 				0
			posn: 			149 65
			setPri: 			4
			addToPic:
		)
		((View new:)							;**	monitor3
			view: 			vTickets
			loop: 			1
			cel: 				0
			posn: 			171 65
			setPri: 			3
			addToPic:
		)
		((View new:)							;**	monitor4
			view: 			vTickets
			loop: 			1
			cel: 				0
			posn: 			192 65
			setPri: 			2
			addToPic:
		)
		((View new:)							;**	customsSign
			view: 			vTickets
			loop: 			1
			cel: 				1
			posn: 			249	63
			setPri: 			10
			addToPic:
		)
		((View new:)							;**	plants
			view: 			vTickets
			loop: 			1
			cel: 				3
			posn: 			247	55
			setPri: 			11
			addToPic:
		)
		((View new:)							;**	raisins
			view: 			vTickets
			loop: 			1
			cel: 				2
			posn: 			295 149
			setPri: 			10
			addToPic:
		)
		((View new:)							;**	tickets1
			view: 			vTickets
			loop: 			2
			cel: 				0
			posn: 			92	104
			setPri: 			8
			addToPic:
		)
		((View new:)							;**	tickets2
			view: 			vTickets
			loop: 			2
			cel: 				0
			posn: 			125 104
			setPri: 			8
			addToPic:
		)
		((View new:)							;**	tickets3
			view: 			vTickets
			loop: 			2
			cel: 				0
			posn: 			157 104
			setPri: 			8
			addToPic:
		)
		((View new:)							;**	tickets4
			view: 			vTickets
			loop: 			2
			cel: 				0
			posn: 			189 104
			setPri: 			8
			addToPic:
		)
		((View new:)							;**	tickets5
			view: 			vTickets
			loop: 			2
			cel: 				0
			posn: 			221 104
			setPri: 			8
			addToPic:
		)

		(cond
			((== prevRoomNum	53)		(ego posn: 273 129))
			((== prevRoomNum	50)		(ego posn: 133 186))
			((== prevRoomNum	 0)		(ego posn: 133 186))
		)

		(self
			setRegions:		rgAirport
			setScript: 		rm52Script
		)
		
		(if ((inventory at: iAirlineTicket) ownedBy: curRoomNum)
			(= agentHere TRUE)
		)

		(if ( < bombStatus EXPLODED)		;**	The first condition of room...
			(Load VIEW vTicketLines)
			(Load VIEW vPassenger)
			((= aBlueAgent (Extra new:))	;**	BlueAgent servicing Blue Line
				view:				vTicketAgents
				setLoop: 		0
				posn: 			116 95
				init:
			)
			((= aGreenAgent (Extra new:))	;**	GreenAgent ditto
				view:				vTicketAgents
				setLoop: 		2
				posn: 			167 94
				init:
			)
			((= aCyanAgent (Extra new:))	;**	CyanAgent ditto
				view:				vTicketAgents
				setLoop: 		1
				posn: 			194 96
				init:
			)
			((= aBlueLine (Prop new:))		;**	The line of people behind cBlue
				view:				vTicketLines
				loop: 			0
				cel: 				4
				posn: 			blueX lineY
				cycleSpeed:		1
				setPri: 			10
				stopUpd:
				init:
			)
			((= aGreenLine (Prop new:))
				view:				vTicketLines
				loop: 			1
				cel: 				4
				posn: 			greenX lineY
				cycleSpeed:		1
				setPri: 			10
				stopUpd:
				init:
			)
			((= aCyanLine (Prop new:))
				view:				vTicketLines
				loop: 			0
				cel: 				4
				posn: 			cyanX lineY
				setPri: 			10
				cycleSpeed:		1
				stopUpd:
				init:
			)

			(if ( < bombStatus TICKING)		;**	The normal condition
				(NormalEgo)
				(ego observeControl:	cYellow)
				((= aCustomer (Actor new:))
					view:				vPassenger
					posn:				(+ greenX offset1) startingY
					setCycle:		Walk
					setStep:			3 2
					illegalBits:	0
					init:
					setScript:		ticketScript
				)
			else
				(Load SOUND sTicking)
				(theSound			play:)
				(= currentStatus BOMBING)
				(HandsOff)
				(rm52Script changeState: bombInit)
			)
		else										;**	He DID explode the bomb:
			(if (== bombStatus EXPLODED)
				(= bombStatus GONE)
				(HandsOff)
				(ego								;**	Set up for the blackface schtick
					view:				vEgoBomb
					setLoop:			lCleanSelf
					cel:				0
					cycleSpeed:		2
					posn:				159 188
					setStep:			3 2
				)
				(rm52Script changeState: justBombed)
			else
				(NormalEgo)
			)

			(if agentHere
				((= aGreenAgent (Prop new:))	;**	no guy if no tickets to sell
					view:				vTicketAgents
					setLoop: 		2
					setCel:			0
					posn: 			167 94
					stopUpd:
					init:
				)
			)
		)

		(ego	init:)
	)
)


(instance rm52Script of Script
	(method (doit)
		(super doit:)

		(if (ego inRect: gotoRm53)
			(curRoom newRoom: 53)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/man, people')
				(cond
					(aBlueLine
						(Print "The men are too busy waiting in line to talk to 
							you.")
					)
					(agentHere
						(Print "He looks like he's ready to help you.
							What do you want?")
					)
					(else
						(Print "This is where you bought your ticket.
							Shouldn't you be hurrying toward your plane?")
					)
				)
			)

			(if (and agentHere
						(Said '/woman, clerk'))
				(Print "Perhaps you might want to purchase an airline ticket.")
			)

			(if (Said '/monitor, schedule')
				(Print "Every single flight is listed as \"ON TIME!\"")
				(Print "(Obviously, this is a fantasy game!)" LOW)
			)

			(if (Said '/raisin, (poster<raisin)')
				(Print "The delicate interplay of colored light is
					fascinating.")
				(Print "(Oh, did you mean that colored light sculpture?)" LOW)
			)

			(if (Said '/window, sculpture')
				(Print "The delicate interplay of colored light is
					fascinating.")
				(Print "(Oh, did you mean that raisin poster?)" LOW)
			)

			(if (Said '[/room, counter]')
				(cond
					(aBlueLine
						(Print "There are three ticket agents with lines of
							customers queued before them.
							Perhaps you should stand in line?")
					)
					(agentHere
						(Print "Since you cleared away the crowds,
							you are all alone with the sole remaining ticket agent.")
					)
					(else
						(Print "This is where you bought your ticket.
							Shouldn't you be hurrying toward your plane?")
					)
				)
			)
		)

		(if (or	(Said 'talk/girl, clerk')
					(Said 'get, buy/ticket'))
			(cond
				((not agentHere)
					(Print "Where?")
				)
				((!= bombStatus GONE)
					(Print "\"Hey,\" shouts a man waiting in line,
						\"who do you think you are?
						Stand in line like everyone else!\"")
				)
				((not ((inventory at: iAirlineTicket) ownedBy: curRoomNum))
					(Print "\"Sorry, but I'm all out of tickets.\"")
				)
				((not (ego inRect: nearAgent))
					(NotClose)
				)
				(else
					(self changeState: buyTicket)
				)
			)
		)

		(if (Said 'talk/man, people')
			(cond
				((not agentHere)
					(Print "Where?")
				)
				((!= bombStatus GONE)
					(Print (Format @string "\"Hi,\" you say to the men in line,
						\"%s\"" laffer))
					(if (> filthLevel DIRTY)
						(Print "\"Back of the line, butthole!\"")
					else
						(Print "\"Back of the line, buddy!\"")
					)
				)
				((ego inRect: nearAgent)
					(Print (Format @string
						"\"Hello,\" you say to the ticket agent.
						%s" laffer))
					(Print "\"Would you care to buy a ticket?\"
						says the clerk, obviously unimpressed.")
				)
				(else
					(NotClose)
				)
			)
		)

		(if (and (!= bombStatus GONE)
					(Said '/line'))
			(Print "This is just like the grocery store:
				no matter which line you stand in, it's always the slowest.")
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(intro)


			;**	This path is when Larry carries the suitcase bomb in from
			;**	room 53...

			(bombInit
				(ego setMotion: MoveTo 255 129 self)
			)

			(lookOut
				(Print "\"LOOK OUT, I'VE GOT A BOMB!\" you shout,
					\"Get out of here -- QUICK!!\"")
				(Print "(That was certainly subtle, Larry.
					You wouldn't want to create a panic!)" LOW)
				(= seconds 9)
			)

			(nobodyMoves
				(Print "It seems you're the only one panicked around here, Larry!")
				(Print "Ignoring their apathy you vow,
					\"I'll rush this outside and throw it safely over those cliffs,
					protecting these innocent bystanders from harm
					while making myself a hero in the process!
					Fame and recognition will surely follow;
					I'll be invited to the White House,
					where I'll be acclaimed a national treasure.
					Shortly thereafter,
					I'll write a titillating, best-selling autobiography
					in which I reveal everything,
					guaranteeing appearances on \"Donahue\" and \"Carson,\"
					and a seven-figure movie deal,
					signed over a power lunch at Spago's in spite
					of the protestations of my agent and attorneys,
					which will go on to make me even richer and more famous,
					before retiring to a quiet,
					unassuming life as a gentleman farmer
					in the foothills of eastern Madera County.\"" BIG)
				(Print "(Or, then again, maybe not!)" LOW)
				(ego
					ignoreControl:cYellow
					setMotion: MoveTo 184 178 self
				)
			)

			(walkOff
				(Print "Hurry, Larry, it sounds like it's about to go off!!")
				(ego setMotion: MoveTo 159 188 self)
			)

			(boom
				(ego put: iSuitcase LIMBO)
				(curRoom newRoom: 152)
			)


			;**	This path happens after the bomb goes off, and we've
			;**	returned from room 152...

			(justBombed
				(= seconds 5)
			)

			(punchLine
				(theGame changeScore: 15)
				(Print "That was certainly ONE way to clear out a crowd!")
				(Print "But now look at your suit!")
				(ego
					setCycle:		EndLoop self
				)
			)

			(bombDone
				(Print "Oh, well, at least no one was hurt;
					but why did they all run away from a little firecracker
					like that?" draw:)
				(NormalEgo facingSouth)
			)


			;**	Let's purchase a ticket...

			(buyTicket
				(HandsOff)
				(ego
					loop:				facingNorth
				)
				(Print "\"May I please purchase a ticket on the next
					available flight,\" you tell the agent." draw:)
				(aGreenAgent setCycle: Forward)
				(= seconds 3)
			)

			(whereTo
				(Print "\"Your destination, Sir?\" he asks." HIGH)
				(aGreenAgent setCel: 0)
				(= seconds 3)
			)

			(anywhereBut
				(Print "\"Anywhere but here!\" you respond.")
				(aGreenAgent setCycle: Forward)
				(= seconds 3)
			)

			(youAreInLuck
				(Print "\"You're in luck,\" says the ticket agent,
					\"we have exactly one seat available
					in the next two weeks and it's on the next departing
					flight.\"" HIGH)
				(Print "\"I'll take it,\" you blurt.
					\"Say, if you have no seats available,
					why were all those men in line?\"")
				(Print "\"They were waiting for the keys to the restroom!\"" HIGH)
				(aGreenAgent setCel: 0)
				(= seconds 3)
			)

			(continueNow
				(Print "You presume that explains their sudden departure
					following the loud noise!")
				(= seconds 3)
			)

			(agentTalks1
				(aGreenAgent setCycle: Forward)
				(= seconds 3)
			)

			(smokingOr
				(Print "\"Would you prefer smoking or non-smoking?\"" HIGH)
				(Print "\"Non.\"")
				(Print "\"Aisle or window?\"" HIGH)
				(Print "\"Aisle.\"")
				(Print "\"Any carry-on baggage?\"" HIGH)
				(Print "\"None.\"")
				(Print "\"Luggage to check through?\"" HIGH)
				(Print "\"No.\"")
				(Print "\"Will you require a bassinet?\"" HIGH)
				(Print "\"No.\"")
				(Print "\"Traveling with children or elderly?\"" HIGH)
				(Print "\"No.\"")
				(Print "\"Bringing a pet?\"" HIGH)
				(Print "\"No.\"")
				(Print "\"Do you have a passport or visa?\"" HIGH)
				(Print "\"Passport.\"")
				(Print "\"Care for baggage insurance?\"" HIGH)
				(Print "\"No.\"")
				(Print "\"First class, business class or coach?\"" HIGH)
				(Print "\"You said there was only one seat.\"")
				(Print "\"Will you be staying 14 to 21 days?\"" HIGH)
				(Print "\"Forever.\"")
				(Print "\"Are you a member of any frequent flyer programs?\"" HIGH)
				(Print "\"No.\"")
				(Print "\"Will you be bringing a laptop computer on-board?\"" HIGH)
				(Print "\"No.\"")
				(Print "\"Would you like a pillow?\"" HIGH)
				(Print "\"Yes.\"")
				(Print "\"Blanket?\"" HIGH)
				(Print "\"No.\"")
				(Print "\"Movie?\"" HIGH)
				(Print "\"No.\"")
				(Print "\"Stereo headphones?\"" HIGH)
				(Print "\"I suppose.\"")
				(Print "\"Drinking or non-drinking?\"" HIGH)
				(Print "\"Drinking.\"")
				(Print "\"Cash or credit card?\"" HIGH)
				(Print "\"Cash.\"")
				(Print "\"Telephone privileges in flight?\""HIGH)
				(Print "\"No.\"")
				(Print "\"Kosher or Mexican?\"" HIGH)
				(Print "\"Armenian!\"")
				(Print "\"3-1/2 inch or 5-1/4 inch?\"" HIGH time: 4)
				(Print "\"8-bit or 16-bit?\"" HIGH time: 3)
				(Print "\"Stewardess fondling or non-fondling?\"" HIGH time: 2 dispose:)

				(if ( > filthLevel CLEAN)
					(Print "\"JUST GIVE ME THE DAMN TICKET!!\"")
				else
					(Print "\"JUST GIVE ME THE GOL-DARNED, DAB-NATTED,
						GOLLY-GEE-WHIZ-BANGED TICKET!!\"")
				)
				(aGreenAgent setCel: 0)
				(= seconds 3)
			)

			(agentTalks2
				(aGreenAgent setCycle: Forward)
				(= seconds 3)
			)

			(touchee
				(Print "\"Boy, are you Americanos touchy!\" he snarls,
					\"Ok, here's your ticket:
					one adult, ultra-saver, non-smoking, window,
					Deluxe-Business Class, Express,
					APEX, excursion, Gold Label, y-fare,
					non-refundable, maxifare ticket for
					Calwa, with pillow, blanket, movie, and stereo headphones.\""
					BIG)
				(Print "\"Your flight is scheduled to depart from Gate #1
					in exactly one minute.\"" HIGH)
				(Print (Format @string
					"\"Too bad you spent so much time here;
					you might have made it!\" concludes the agent,
					\"%s\"" tritePhrase) HIGH)
				(aGreenAgent
					setCel:			0
					stopUpd:
				)
				(ego get: iAirlineTicket)
				(theGame changeScore: 5)
				(NormalEgo facingNorth)
				(SetRgTimer AIRPLANE_DEPARTURE 5 10)
			)
		)
	)
)


(instance ticketScript of Script
	(method (changeState newState &tmp done)
		(switch (= state newState)

			(ticketInit
				(= seconds (Random 2 5))
			)

			(walkOneIn
				(aCustomer
					posn:				(+ greenX offset1) startingY
					show:
					setMotion:		MoveTo (+ greenX offset1) decisionY self
				)
			)

			(pickAline
				(= done FALSE)
				(while (not done)
					(switch (++ activeLine)
						(blue
							(if (not (& (ego onControl:) cBlue))
								(self changeState: doBlueLine)
								(= done TRUE)
							)
						)
						(green
							(if (not (& (ego onControl:) cGreen))
								(self changeState: doGreenLine)
								(= done TRUE)
							)
						)
						(cyan
							(if (not (& (ego onControl:) cCyan))
								(self changeState: doCyanLine)
								(= done TRUE)
							)
						)
						(else
							(= activeLine 0)	;**	try again next "while"
						)
					)
				)
			)


			;**	GREEN LINE:

			(doGreenLine
				(aCustomer
					setMotion:		MoveTo (+ greenX offset1) endOfLineY self
				)
			)

			(atGreenLine
				(aCustomer			loop: facingNorth)
				(= seconds 3)
			)

			(greeniesShift
				(if (& (ego onControl:) cGreen)
					(-- state)
					(= cycles 10)				;**	try again leter
				else
					(aCustomer
						posn:				(+ greenX offset2) counterY
						setMotion:		MoveTo (+ greenX offset3) counterY self
					)
					(aGreenLine
						cel:				0
						setCycle:		EndLoop
					)
				)
			)

			(greenLeaving
				(aCustomer
					setMotion:		MoveTo (+ greenX offset3) walkPastY self
				)
			)

			(greenLeaving2
				(aGreenLine			stopUpd:)
				(aCustomer
					setMotion:		MoveTo turnHereX walkPastY self
				)
			)

			(greenLeaving3
				(aCustomer
					setMotion:		MoveTo doorwayX doorwayY self
				)
				(= state headingForHallway)
			)


			;**	BLUE LINE:

			(doBlueLine
				(aCustomer
					setMotion:		MoveTo (+ blueX offset1) endOfLineY self
				)
			)

			(atBlueLine
				(aCustomer			loop: facingNorth)
				(= seconds 3)
			)

			(blueiesShift
				(if (& (ego onControl:) cBlue)
					(-- state)
					(= cycles 10)				;**	try again leter
				else
					(aCustomer
						posn:				(+ blueX offset2) counterY
						setMotion:		MoveTo (+ blueX offset3) counterY self
					)
					(aBlueLine
						cel:				0
						setCycle:		EndLoop
					)
				)
			)

			(blueLeaving
				(aCustomer
					setMotion:		MoveTo (+ blueX offset3) walkPastY self
				)
			)

			(blueLeaving2
				(aBlueLine			stopUpd:)
				(aCustomer
					setMotion:		MoveTo turnHereX walkPastY self
				)
			)

			(blueLeaving3
				(aCustomer
					setMotion:		MoveTo doorwayX doorwayY self
				)
				(= state headingForHallway)
			)


			;**	CYAN LINE:

			(doCyanLine
				(aCustomer
					setMotion:		MoveTo (+ cyanX offset1) endOfLineY self
				)
			)

			(atCyanLine
				(aCustomer			loop: facingNorth)
				(= seconds 3)
			)

			(cyaniesShift
				(if (& (ego onControl:) cCyan)
					(-- state)
					(= cycles 10)				;**	try again leter
				else
					(aCustomer
						posn:				(+ cyanX offset2) counterY
						setMotion:		MoveTo (+ cyanX offset3) counterY self
					)
					(aCyanLine
						cel:				0
						setCycle:		EndLoop
					)
				)
			)

			(cyanLeaving
				(aCustomer
					setMotion:		MoveTo doorwayX doorwayY self
				)
				(= state headingForHallway)
			)


			;**	(headingForHallway) is unused, except as place marker.

			(allDone
				(aCustomer		hide:)
				(self changeState: ticketInit)
			)
		)
	)
)

