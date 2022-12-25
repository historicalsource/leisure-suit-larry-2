;**
;**	Logics for room 37 -- ship's barber
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 16, 1988
;**


(script#				37)

(public
	rm37				0
)


(define	nearChair	148 117 180 127)

;(define	vEgoWig						133)
;(define	vBarberInt					232)	;**	various addToPic of shop interior
	(define	lEgoSitting			2)

;(define	vShipBarberInt				302)
	(define	lEgoSittingWig		0)
	(define	lRemoveWig			1)

;(define	vShipBarber					303)
	(define	lMopping				0)
	(define	lRaiseMop			1)
	(define	lPullMop				2)			;**	cycle a while
	(define	lRemoveMop			3)
	(define	lWalkNorth			4)
	(define	lWigOnLarry			5)
	(define	lAdjustWig			6)
	(define	lReadjustWig		7)
	(define	lYankWig				8)
	(define	lTurnAndSlide		9)


(local
	work
	aBarber
	aChair
	talkedToBarber
)


(enum
	intro
	sayHello
	wigInit
	egoAtChair
	pullMop
	removeMop
	mopOffHandle
	gotoChair
	sitDown
	wigIsOn
	adjustWig
	yankMeRealHard
	stepAsideBigBoy
	moveOver
	wigOn
	standUp
	wigDone
	headForDoor
	haveA
	seeMeInMirror
	nextRoom
)


(instance rm37 of Room
	(properties
		picture:		125
		south:		31
	)

	(method (init)
		(Load VIEW currentEgoView)
		(Load VIEW vBarberInt)
		(Load VIEW vShipBarber)
		(Load VIEW vShipBarberInt)

		(super init:)

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
			view:				vShipBarber
			setLoop:			lMopping
			posn:				134 146
			setCycle:		Walk
			observeControl:cYellow cLtWhite
			moveSpeed:		6
			cycleSpeed:		3
			setMotion:		Wander
			init:
		)

		(NormalEgo)
		(ego
			posn: 			160 160
			init:
		)

		(self
			setRegions:		rgBarber rgShip 
			setScript: 		rm37Script
		)
	)
)


(instance rm37Script of Script
	(method (doit)
		(super doit:)

		(if (& (ego onControl:) cBlue)
			(if (== currentEgoView vEgoWig)
				(if ( < state haveA)
					(self changeState: haveA)
				)
			else
				(curRoom newRoom: 31)
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/window, porthole, ocean, water')
				(Print "You can't see any from here.")
			)

			(if (Said '/up, overhead')
				(Print "You love the warm glow of florescent lights.")
			)

			(if (Said '[/ship, cloud]')
				(Print "Don't all barber shops look alike?")
			)

			(if (Said '/man')
				(Print "He looks friendly enough.")
			)
		)

		(if (or	(Said 'sit, fix, yes, cut')
					(Said '(get<in)/chair')
					(Said 'get/haircut, (cut<hair)'))
			(= talkedToBarber TRUE)
			(cond
				((not (ego inRect: nearChair))
					(Print "Walk over to the front of the 
						barber's chair and sit down.")
				)
				((not ((inventory at: iWig) ownedBy: curRoomNum))
					(Print "\"I'm sorry, Mister,\"
						says the barber rudely,
						\"I have no more appointments left today!\"")
					(Print "\"In fact, I'd appreciate it if you'd leave now
						before I have to call the Ship Police!\"")
				)
				((== currentStatus SITTING)
					(YouAre)
				)
				(else
					(self changeState: wigInit)
				)
			)
		)

		(if (Said 'talk/man')
			(= talkedToBarber TRUE)
			(if (not ((inventory at: iWig) ownedBy: curRoomNum))
				(Print "\"You look much better to me, monsieur!\"")
			else
				(Print "\"I have no time for idle conversation,\"
					responds the barber.
					\"It's bad enough I have to talk to customers while
					I cut their hair,
					I certainly don't want to talk when I'm NOT cutting hair!\"")
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(if ((inventory at: iWig) ownedBy: curRoomNum)
					(= seconds 10)
				)
			)

			(sayHello
				(if (not talkedToBarber)
					(Print "\"Good day, Mister,\" says the barber.
						\"May I help you?\"")
					(= seconds (Random 12 25))
					(= state intro)
				)
			)


			;**	Here we sell him the wig:

			(wigInit
				(= cycles	0)
				(= seconds	0)
				(HandsOff)
				(Print "\"Is there anything you can do for this hairline of
					mine,\" you ask the barber." HIGH)
				(Print "\"But, of course, Mister,\" says the barber,
					\"I've got exactly what you need.\"" HIGH)
				(Print "\"Just have a seat in the chair, please.\"" HIGH)
				(ego
					illegalBits:0
					ignoreActors:
					setMotion:	MoveTo 157 120 self
				)
				(aBarber
					setLoop:		lRaiseMop
					moveSpeed:	0
					cycleSpeed:	0
					setCycle:	EndLoop
					setMotion:	FALSE
				)
			)

			(egoAtChair
				(aChair 			hide:)
				(ego
					view:			vBarberInt
					setLoop:		lEgoSitting
					cel:			0
					posn:			167 116
					setCycle:	EndLoop self
				)
			)

			(pullMop
				(aBarber
					setLoop:		lPullMop
					setCycle:	Forward
				)
				(= seconds 3)
			)

			(removeMop
				(aBarber
					setLoop:		lRemoveMop	;**	toss away handle
					setCycle:	EndLoop self
				)
			)

			(mopOffHandle
				(= seconds 3)
			)

			(gotoChair
				(aBarber
					setLoop:		lWalkNorth
					setCycle:	Walk
					setMotion:	MoveTo 166 117 self
					illegalBits:0
				)
			)

			(sitDown
				(aBarber
					setLoop:		lWigOnLarry
					setCycle:	EndLoop self
				)
				(Print "\"Yes, I have exactly what you need,\" he tells you.
					\"It's all the rage; the latest thing.
					You see it everywhere these days.
					In fact, guys make a lot of money with one of these!\""
					HIGH draw:)
				(Print "\"I call it 'The Jimmy' model,
					and I'm sure you'll like it just fine!\"" HIGH)
			)

			(wigIsOn
				(aBarber
					setLoop:		lAdjustWig
					setCycle:	Forward
				)
				(Print "He places \"The Jimmy\" carefully on your head."
					HIGH draw:)
				(= seconds 4)
			)

			(adjustWig
				(aBarber
					setLoop:		lReadjustWig
					setCycle:	EndLoop self
				)
			)

			(yankMeRealHard
				(aBarber
					setLoop:		lYankWig
					setCycle:	Forward
				)
				(= seconds 4)
			)

			(stepAsideBigBoy
				(ego
					view:			vShipBarberInt
					setLoop:		lEgoSittingWig
					cel:			0
				)
				(aBarber
					setLoop:		lTurnAndSlide
					setCycle:	CycleTo 6 FORWARD self
				)
			)

			(moveOver
				(aBarber
					setCycle:	EndLoop self
					setMotion:	MoveTo 176 118
				)
			)

			(wigOn
				(Print "\"Impeccable fit!\" he cries,
					\"And (for you) the price is right -- only $10,000.00!!
					But I like to consider it more of an investment than
					an expense!\"" HIGH)
				(Print "(10K for a wig?)" LOW)

				(if (> filthLevel CLEAN)
					(Print "Oh, what the hell -- it's not like it's your money;
						why not let those suckers who paid for all those
						Luck-o Buck-o tickets treat you to a new look!" HIGH)
				else
					(Print "Oh, well, it's not like it's your money;
						why not let those suckers who paid for all those
						Luck-o Buck-o tickets treat you to a new look!" HIGH)
				)

				(Print "\"I'll take it,\" you tell him, and peel off a
					hundred hundreds from your rapidly depleting wad." HIGH)
				(ego get: iWig)
				(theGame changeScore: 3)
				(= seconds 3)
			)

			(standUp
				(ego
					cycleSpeed:		1
					setCycle:		EndLoop self	;**	get out of chair
				)
			)

			(wigDone
				(ego posn:			159 120)
				(= currentEgoView vEgoWig)
				(NormalEgo facingNorth)
				(aChair				show:)
				(HandsOff)
				(= seconds 3)
			)

			(headForDoor
				(ego setMotion:		MoveTo 159 199 self)
			)

			;**	This happens when Larry hits the changeRoom control:

			(haveA
				(Print (Format @string
					"The barber bids you farewell with a sweet,
					\"%s\"" tritePhrase))
				(HandsOff)
				(ego
					setLoop:			facingNorth
				)
				(= cycles 20)
			)

			(seeMeInMirror
				(Print "You catch a good look at yourself in the mirror
					and slip the wig into your inner jacket pocket.
					You tell the barber,
					\"Thanks a lot, but I think I'll just save this until
					my cable channel application is approved!\"" HIGH)
				(ego
					view:				vShipBarberInt
					cycleSpeed:		2
					setLoop:			lRemoveWig
					setCel:			0
					setCycle:		EndLoop self
				)
			)

			(nextRoom
				(= currentEgoView vEgo)
				(NormalEgo facingSouth)
			)
		)
	)
)

