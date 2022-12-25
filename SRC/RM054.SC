;**
;**	Logics for room 54 -- American Terroristor Luggage
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 20, 1988
;**


(script#				54)

(public
	rm54				0
)


(define	x-rayX 				90)
(define	beltY					124)
(define	bombCel				12)
(define	lastBagCel			12)
(define	maxBagDistance		23)			;**	distance to grab bag from belt

(define	grabBag
	"You nimbly grab someone's suitcase from the moving belt,
	force it open, and discover inside...")

;(define	vEgoBomb						155)
	(define	lGrabBag			0)
	(define	lWalk				1)
	(define	lCleanSelf		2)

;(define	vX-ray						515)
	(define	lX-ray			0)
	(define	lVisible			1)
	(define	lDoor				2)
	(define	lPerson1			3)
	(define	lPerson2			4)

;(define	vX-rayAgent					516)


(local
	bagNumber
	aPlane
	aBag
	aDoor
	aTraveler
)


(enum												;**	bagScript state values
	intro
	gotBag
	what'llHeDo
	bagDown
)

(enum												;**	travelers in distance
	travelerInit
	travelerStart
	travelerGone
)

(enum												;**	bagScript state values
	bagInit
	bagStart
	bagDone
)


(instance theSound of Sound
	(properties
		number:		sTicking
		loop:			-1
	)
)


(instance rm54 of Room
	(properties
		picture:		54
		horizon:		5
		east:			55
		west:			53
	)

	(method (init)
		(Load VIEW vX-ray)
		(Load VIEW vX-rayAgent)

		(super init:)

		((View new:)
			view: 			vX-rayAgent
			loop: 			0
			cel: 				0
			posn: 			109 158
			setPri: 			12
			addToPic:
		)

		(if ((inventory at: iSuitcase) ownedBy: curRoomNum)
			(Load VIEW vEgoBomb)
			(Load SOUND sTicking)
			(Load FONT HELVETICA18)
			(theSound		init:)
			((= aBag (Actor new:))
				view:			vX-ray
				setLoop:		lX-ray
				setCel:		0
				setPri:		5
				setStep:		2 1
				posn:			x-rayX beltY
				illegalBits:0
				ignoreActors:
				init:
				hide:
				setScript:	bagScript
			)
		)

		((= aPlane (Airplane new:))
			view:				vAirplane
			setCel:			0
			startX:			306
			startY:			22
			endX:				222
			endY:				22
			init:
		)

		((= aTraveler (Actor new:))		;**	male/female Walking
			view:				vX-ray
			setLoop:			lPerson1
			setPri:			2
			setStep:			1 1
			setCycle:		Walk
			posn:				230 37
			illegalBits:	0
			ignoreActors:
			init:
			hide:
		)

		(cond
			((== prevRoomNum 53)		(ego	posn:		  1 155))
			((== prevRoomNum 55)		(ego	posn:		316 155))
			(else							(ego	posn:		316 155))
		)
		(NormalEgo)
		(ego					init:)

		((= aDoor (AutoDoor new:))
			view:				vX-ray
			setLoop:			lDoor
			posn:				15 157
			setPri:			11
			doorCtrl:		cBlue
			doorBlock:		cYellow
			roomCtrl:		0
			msgLook:			"The low blue gate leads back to the Customs
									Inspection area."
			msgFunny:		"Knock. Knock. (No one's there!)"
			init:
		)

		(self
			setRegions:		rgAirport
			setScript: 		rm54Script
		)
	)
)


(instance rm54Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (and	(not (ego has: iSuitcase))
					(Said 'look/bag, suitcase'))
			(Print "Luggage passes by on the nearby conveyor belt.")
		)

		(if (Said 'look>')
			(if (Said '/conveyor, suitcase, bag, belt')
				(Print "Conveyors carrying luggage stretch off into the distance.")
			)

			(if (Said '/man, guard')
				(Print "He's sound asleep.
					Shhh!
					Don't wake him.")
			)

			(if (Said '/picture')
				(Print "Gawd, is that symbolic, or what?!")
				(if (> filthLevel DIRTY)
					(Print "(You fondly recall an old joke about a girl and
						her cherry.)" LOW)
				)
			)

			(if (Said '[/room]')
				(Print "A security guard snoozes beside his X-ray machine,
					allowing baggage to pass by uninspected.")
			)
		)

		(if (or	(Said 'wake<up')
					(Said 'wake, shake, hit, talk/man, guard'))
			(Print "\"Zzzzzzz.\"")
		)

		(if (Said 'grab, (pick<up), take/bag, suitcase')
			(cond
				((not ((inventory at: iSuitcase) ownedBy: curRoomNum))
					(Print "Where?")
				)
				((not (& (ego onControl:) cCyan))
					(NotClose)
				)
				(( > (ego distanceTo: aBag) maxBagDistance)
					(Print "You missed!")
				)
				(else
					(aBag hide:)

					(if (and (!= bagNumber 0)
								(!= bagNumber 8))
						(Print grabBag HIGH draw:)
					)

					(switch bagNumber 
						(0
							(Print "\"Paul is dead.\"")
						)
						(1
							(Print "...a disassembled hunting rifle.
								You decide it is of no use to you.")
						)
						(2
							(Print "...some dirty underwear.")
						)
						(3
							(Print "...a dead cat.")
							(Print "YUCK!")
						)
						(4
							(Print "...a pair of maces.
								You decide they are of no use to you.")
						)
						(5
							(Print "...a Tommy gun in a violin case.
								You decide it is of no use to you.")
						)
						(6
							(Print "...nothing of interest.")
						)
						(7
							(Print "...a computer.
								Searching through the disk storage box within, 
								you discover a complete set of Sierra adventure games.
								You decide they are of no use to you.")
							(Print "...because you're having plenty of trouble
								with THIS game!")
						)
						(8
							(Print "\"John 3:16\"")
						)
						(9
							(Print "...nothing of interest.")
						)
						(10
							(Print "...some blue, pin-striped suits.
								You decide they are of no use to you.")
						)
						(11
							(Print "...a fully-automatic machine gun and some
								clips of ammo.
								You realize you have no use for this until you
								purchase \"Police Quest II, The Vengeance.\"")
						)
						(12
							(self changeState: gotBag)
							(return)
						)
					)
					(Print "You sheepishly close it, 
						return it to the conveyor,
						and wonder if anyone noticed you." HIGH)
					(aBag show:)
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(intro)

			(gotBag
				(theGame changeScore: 5)
				(aBag dispose:)
				(bagScript dispose:)
				(theSound		play:)
				(Print "\n\ \ \ \ \ A BOMB!!\n\n" HIGH width: 222 font: HELVETICA18)
				(ego get: iSuitcase)
				(= bombStatus TICKING)
				(HandsOff)
				(ego
					view:			vEgoBomb
					setLoop:		lWalk
				)
				(Print "\"Let's see now,\" you think,
					\"I'm holding a suitcase containing a ticking bomb.
					What shall I do?\"" draw:)
				(= seconds 3)
			)

			(what'llHeDo
				(Print "\"Say,\" you decide.
					\"\"I think I'll get this thing outside... and FAST!")
				(ego
					setMotion:	MoveTo (ego x?) 154 self
				)
			)

			(bagDown
				(ego
					setStep:		4 3			;**	hump it out of here!
					setCycle:	Walk
					setMotion:	MoveTo 0 154 self
				)
			)
		)
	)
)


(instance travelerScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(travelerInit
				(= seconds 3)
			)

			(travelerStart
				(aTraveler
					posn:			230 37
					show:
					setMotion:	MoveTo 291 37 self
				)
			)

			(travelerGone
				(aTraveler						;**	perform sex change!
					setLoop:		(if (== (aTraveler loop?) lPerson1)
										lPerson2
									else
										lPerson1
									)
				)
				(self changeState: intro)
			)
		)
	)
)


(instance bagScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(bagInit
				(aBag
					posn:			x-rayX beltY
					setCel:		bagNumber
					setLoop:		lX-ray
					show:
					setMotion:	MoveTo 152 beltY self
				)
			)

			(bagStart
				(aBag
					setLoop:		lVisible
					setMotion:	MoveTo 303 beltY self
				)
			)

			(bagDone
				(if ( > (++ bagNumber) lastBagCel)
					(= bagNumber 0)
				)
				(aBag				hide:)
				(self changeState: bagInit)
			)
		)
	)
)

