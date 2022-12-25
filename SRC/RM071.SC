;**
;**	Logics for room 71 -- Killer Bees
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 26, 1988
;**


(script#				71)

(public
	rm71				0
)


(define	nearStick		205 6 226 82)
(define	msgStick			"There is a stout stick lying under the tree.
									You feel fortunate you did not land on it!")


;(define	vEgoCrawling				105)
	(define	lSouth			0)
	(define	lNorth			1)
	(define	lGetDownSouth	2)
	(define	lGetDownNorth	3)

;(define	vBigEgo						110)
;(define	vBEstunned					114)

;(define	vEgoOutOfTree				171)
	(define	lFalling			0)
	(define	lPlop				1)
	(define	lGetUp			2)

;(define	vEgoKillerBees				173)
	(define	lSwarming		0)
	(define	lPickUp			1)
	(define	lCarriedAway	2)

;(define	vKillerBees					702)
	(define	lFromBush		0)
	(define	lOntoLarry		1)

;(define	vStick						721)

;(define	sFalling						1)
;(define	sStunned						2)
;(define	sBees							3)


(local
	stickHere
	aBigEgo
	aBigEgoFace
	aStick
	aSwarm
)


(enum												;**	rmScript state values
	intro

	comeOnDown
	hitBottom
	waitAsec
	showBigOne
	I'mOKnow
	he'sUp

	ohOh
	beesOut
	beesAtLarry
	biteLarry
	takeItAway
	takeOff
	swarmDone
	UBalmostDead
	UBdead

	startCrawlingSouth
	egoSouthDown
	egoSouthDone
	egoSouthUp

	startCrawlingNorth
	egoNorthDown
	egoNorthDone
	egoNorthUp
)


(instance theSound of Sound
)

(instance rm71 of Room
	(properties
		picture:		71
		horizon:		5
		south:		72
	)

	(method (init)
		(Load VIEW vEgoCrawling)
		(Load VIEW vEgoKillerBees)
		(Load VIEW vKillerBees)

		(super init:)

		((= aSwarm (Actor new:))
			view:				vKillerBees
			setLoop:			lFromBush
			setCel:			0
			setPri:			14
			setCycle:		Walk
			posn:				148 110
			illegalBits:	0
			ignoreActors:
			init:
		)

		(self
			setRegions:		rgIsland
			setScript:		rm71Script
		)

		(if ((inventory at: iStick) ownedBy: curRoomNum)
			(= stickHere TRUE)
			(Load VIEW vStick)
			((= aStick (View new:))
				view:				vStick
				posn:				216 72
				ignoreActors:
				stopUpd:
				init:
			)
		)

		(if (== prevRoomNum 72)
			(theSound
				number:			sBees
				init:
			)
			(NormalEgo)
			(ego
				posn:				142 186
				init:
			)
		else
			(Load VIEW vEgoOutOfTree)
			(Load VIEW vBigEgo)
			(Load VIEW vBEstunned)
			(Load SOUND sFalling)
			(Load SOUND sStunned)
			(theSound
				number:			sFalling
				loop:				1
				play:
			)
			((= aBigEgo (View new:))
				view:				vBigEgo
				ignoreActors:
				setPri:			14
				posn:				243 1081
				init:
			)
			((= aBigEgoFace (Prop new:))
				view:				vBEstunned
				ignoreActors:
				setPri:			15
				posn:				243 1081
				setCycle:		Forward
				init:
			)
			((inventory at: iSoap) moveTo: LIMBO)	;**	if missed in 48.
			(ego
				put:				iOnklunk LIMBO
				put:				iKnife LIMBO
				put:				iPassport LIMBO
				put:				iParachute LIMBO
				put:				iMoney LIMBO
				ignoreHorizon:
				illegalBits:	0
				view:				vEgoOutOfTree
				setLoop:			lFalling
				setCycle:		Forward
				setStep:			1 12
				setPri:			10
				posn:			193 -120
				init:
			)
			(HandsOff)
			(rm71Script changeState: comeOnDown)
			(= currentStatus FALLING)
		)
	)
)


(instance rm71Script of Script
	(method (doit)
		(super doit:)

		(if (== state hitBottom)
			(ShakeScreen 1 (Random 1 3))
		)

		(if (and (& (ego onControl:) cYellow)
					(== currentStatus NORMAL))
			(self changeState: ohOh)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look<down')
			(Print "The jungle floor is shrouded in darkness
				and covered with a viscous substance that
				you presume passes for dirt around here.")
			(if stickHere
				(Print msgStick)
			)
		)

		(if (Said 'look>')
			(if (Said '/ground, dirt')
				(Print "The jungle floor is shrouded in darkness
					and covered with a viscous substance that
					you presume passes for dirt around here.")
				(if stickHere
					(Print msgStick)
				)
			)

			(if (Said '/path')
				(Print "There might be a way out of here near that bush.")
			)

			(if (and stickHere
						(Said '/stick'))
				(Print msgStick)
			)

			(if (Said '/face, statue, mouth')
				(Print "It looks like a prehistoric Al Lowe!")
			)

			(if (Said '/bush')
				(Print "There are some bees resting on that bush over there.")
			)

			(if (Said '/bee')
				(Print "The swarm of bees are just resting on a branch of a bush.
					You remember reading something about a new strain of
					bees from this area.")
			)

			(if (Said '[/jungle, tree, vine, room]')
				(Print "It's like a jungle in here.")
				(Print "Could that be because you're stranded
					in the middle of a jungle on a tropical island?!")
				(Print "The ground here is unusually spongy.")
			)
		)

		(if (Said '(pick<up), get/stick')
			(cond
				((!= currentStatus NORMAL)
					(NotNow)
				)
				((not stickHere)
					(AlreadyTook)
				)
				((not (ego inRect: nearStick))
					(NotClose)
				)
				(else
					(Ok)
					(= stickHere FALSE)
					(ego get: iStick)
					(aStick hide:)
					(theGame changeScore: 4)
					(Print "You can never tell when a nice,
						stout stick like this one will come in handy!" draw:)
				)
			)
		)

		(if (or	(Said 'hit, poke, use/bush, branch, bee/stick')
					(Said 'shake, burn, hit, get/bee, branch, bush, honey'))
			(Print "You must be joking!")
		)

		(if (or	(Said 'pour, burn, put, use/tonic')
					(Said 'poke, wave, dig/ground, hole, dirt, stick')
					(Said 'burn/bush'))
			(Print "That wouldn't help.")
		)

		(if (Said 'jump, (step<over)')
			(Print "Jumping won't help you now.")
		)

		(if (Said 'crawl, (go<under)')
			(Print "Good idea.")
			(cond
				((& (ego onControl:) cBlue)
					(self changeState: startCrawlingSouth)
				)
				((& (ego onControl:) cGreen)
					(self changeState: startCrawlingNorth)
				)
				(else
					(Print "But first, move a little closer to the bush.")
				)
			)
		)

		(if (Said '/branch')
			(Print "The trees can't help you here.")
		)

		(if (Said 'climb/tree')
			(Print "Nah, you'd just wind up out on a limb again!")
		)

		(if (Said 'climb')
			(Print "That won't help here.")
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			;**	(intro)


			(comeOnDown
				(ego
					setMotion:		MoveTo 193 80 self
				)
			)

			(hitBottom
				(ego
					setMotion:		FALSE
					setStep:			3 2
					setLoop:			lPlop
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(waitAsec
				(Print "Unghhh!" draw:)
				(= seconds 3)
			)

			(showBigOne
				(aBigEgo
					posn:				143 81
					stopUpd:
				)
				(aBigEgoFace posn:143 81)
				(theSound			dispose:)
				(theSound
					number:			sStunned
					play:
				)
				(= seconds 5)
			)

			(I'mOKnow
				(aBigEgoFace		dispose:)
				(aBigEgo				dispose:)
				(theSound			dispose:)
				(ego
					setLoop:			lGetUp
					cel:				0
					setCycle:		EndLoop self
					cycleSpeed:		2
				)
			)

			(he'sUp
				(Print "A fall like that would be enough to break the average
					man's onklunk!
					Careful examination of yours reveals severe damage:
					the delicate instrument was smashed by the fall!
					You discard it here without learning of its dangerous cargo,
					nor realizing you have inadvertently 
					kept the world's most valuable secret
					from falling into enemy hands!" draw:)
				(NormalEgo facingSouth)
				(theSound
					number:			sBees
					loop:				-1
				)
			)


			;**	This is what happens when he's under the bee's bush
			;**	and isn't crawling on his belly like a reptile...

			(ohOh
				(= currentStatus NEAR_DEATH)
				(HandsOff)
				(ego
					illegalBits:	0
				)
				(Print "Oops!
					You just brushed a bush containing a resting swarm
					of killer bees.
					You are frozen in fear!
					Could the rumors of their incredible strength
					really be true?")
				(aSwarm
					show:
					setCycle:		EndLoop self
				)
				(theSound			play:)
			)

			(beesOut
				(aSwarm
					setLoop:			lOntoLarry
					cel:				0
					posn:				106 107
					setCycle:		CycleTo 2 FORWARD self
				)
			)

			(beesAtLarry
				(aSwarm
					setCycle:		EndLoop self
				)
				(ego
					view:				vEgoKillerBees
					setLoop:			lSwarming
					cel:				0
					posn:				98 121
					setPri:			11
					setCycle:		Forward
				)
			)

			(biteLarry
				(aSwarm				dispose:)
				(ego
					setLoop:			lPickUp
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(takeItAway
				(ego
					setLoop:			lCarriedAway
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(takeOff
				(ego
					setMotion:		MoveTo 195 39 self
				)
			)

			(swarmDone
				(Print "Yep.")
				(ego
					setPri:			9
					setMotion:		MoveTo 333 3 self
				)
			)

			(UBalmostDead
				(Print "Perhaps you'll enjoy your new life as the Queen Bee's
					personal love slave...")
				(= seconds 3)
			)

			(UBdead
				(Print "...nah. No way!")
				(theSound			dispose:)
				(= currentStatus	DEAD)
			)


			;**	This is where we crawlSouth

			(startCrawlingSouth
				(Print "You carefully lower yourself to the slimy ground and
					attempt to make your way past the dangerous swarm of killer
					bees!")
				(= currentStatus	CRAWLING)
				(HandsOff)
				(ego
					illegalBits:	0
					view:				vEgoCrawling
					setLoop:			lGetDownSouth
					cel:				0
					cycleSpeed:		1
					setCycle:		EndLoop self
				)
			)

			(egoSouthDown
				(ego
					moveSpeed:		1
					setLoop:			lSouth
					setCycle:		Walk
					setStep:			1 1
					setMotion:		MoveTo 92 157 self
				)
			)

			(egoSouthDone
				(ego
					setLoop:			lGetDownSouth
					setCel:			LastCel
					setCycle:		BegLoop self
				)
			)

			(egoSouthUp
				(NormalEgo facingSouth)
				(if ( > (++ pastBees) 1)
					(Print "You made it. Again!" draw:)
				else
					(Print "You successfully avoid the dangerous killer bees." draw:)
					(theGame changeScore: 6)
				)
			)


			;**	Crawl North starts here

			(startCrawlingNorth
				(Print "Once again, you lower yourself to the
					jungle floor and hope you can slide your way below
					the dangerous swarm of killer bees!" draw:)
				(= currentStatus	CRAWLING)
				(HandsOff)
				(ego
					illegalBits:	0
					view:				vEgoCrawling
					setLoop:			lGetDownNorth
					cel:				0
					cycleSpeed:		1
					setCycle:		EndLoop self
				)
			)

			(egoNorthDown
				(ego
					moveSpeed:		1
					setLoop:			lNorth
					setCycle:		Walk
					setStep:			1 1
					setMotion:		MoveTo 94 145 self
				)
			)

			(egoNorthDone
				(ego
					setLoop:			lGetDownNorth
					setCel:			LastCel
					setCycle:		BegLoop self
				)
			)

			(egoNorthUp
				(NormalEgo facingNorth)
				(Print "He walks!" draw:)
				(Print "He talks!")
				(Print "He wriggles on his belly like a reptile!")
			)
		)
	)
)
