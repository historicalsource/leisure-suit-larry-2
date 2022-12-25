;**
;**	Logics for room 104 -- Lucky Life Lottery studio
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 26, 1988
;**


(script#				104)

(procedure
	StartMC
	StopMC
)

(public
	rm104				0
	StartMC			1
	StopMC			2
)


;(define	vMC							207)
;	(define	lPodium			0)
;	(define	lMCtalking		1)				;**	Cel 0 = mouth shut
;	(define	lShakeHands		2)				;**	Includes ego
;	(define	lGestureRight	3)
;	(define	lGestureLeft 	4)
;	(define	lApplause		5)

;(define	vCameraman					212)
;(define	vLLLwheel					213)
	(define	lWheel			0)
	(define	lEgoSpinning	1)

;(define	vLLLstudio					214)

(define	lSign				0)
	(define	lCards			1)

;(define	vHostess						215)	;**	with check
	(define	lWalking			0)
	(define	lPresenting		1)
	(define	lEgoPocketing	2)

;(define	vLLLmonitor					216)
	(define	lCameraMonitor	0)
	(define	lLarryShocked	1)
	(define	lLarrySpinning	2)
	(define	lWheelOnly		3)
	(define	lFlapper			4)
	(define	lLuckySign		5)
	(define	lMillionSign	6)
	(define	lMC				7)
	(define	lMstatic			8)

;(define	sDrumRoll					9)
;(define	mLLL							109)



(enum
	intro
	hereHeIs
	someApplause
	startMCtalking
	atStage
	gestureDone
	atMC
	stopTalking
	atWheel
	wait1
	spinWheel
	slow1
	slow2
	slow3
	win1
	win2
	win3
	heyYouComeHere
	startCheck
	makeHostessCome
	hostessThruDoor
	checkAtLarry
	hereTis
	checkInPocket
	shakeHands
	getCheck
	heWon
	showsOver
	thatsAwrap
	leaveStage
	atDoor
	nextRoom
	punchLine
)


(local
	aSign
	aCameraMonitor
	aTV
	aMC
	aWheel
	aCameraman
	aLana
	aApplause
)

(instance mDrumRoll of Sound
	(properties
		number:		sDrumRoll
		priority:	100
		loop:			-1
	)
)

(instance mThemeSong of Sound
	(properties
		number:		mLLL
		priority:	100
		loop:			-1
	)
)


(instance rm104 of Room
	(properties
		picture:		104
		horizon:		1
	)

	(method (init)
		(Load VIEW vMC)
		(Load VIEW vHostess)
		(Load VIEW vCameraman)
		(Load VIEW vLLLwheel)
		(Load VIEW vLLLstudio)
		(Load VIEW vLLLmonitor)
		(Load SOUND mLLL)
		(Load SOUND sDrumRoll)

		(super init:)

		(mDrumRoll			init:)
		(mThemeSong			init:)

		((= aApplause (Prop new:))			;**	the Applause sign
			view:				vMC
			setLoop:			lApplause
			setCel:			0
			setPri:			15
			posn:				156 21
			init:
			stopUpd:
		)
		((= aSign (Prop new:))				;**	the LL sign behind MC's head
			view: 			vLLLstudio
			loop: 			lSign
			cel: 				0
			posn: 			163 55
			priority: 		2
			init:
			stopUpd:
		)
		((View new:)							;**	the 4 aces behind MC
			view:				vLLLstudio
			loop: 			lCards
			cel: 				0
			posn: 			166 78
			setPri: 			2
			ignoreActors:
			addToPic:
		)
		((= aMC (Prop new:))
			view:				vMC
			loop:				lMCtalking
			cel:				0
			posn:				164 79
			setPri:			5
			ignoreActors:
			setCycle:		Forward
			init:
		)
		((View new:)
			view: 			vMC
			loop: 			lPodium
			cel: 				0
			posn: 			164 94
			setPri:			5
			ignoreActors:
			addToPic:
		)
		((= aCameraMonitor (Prop new:))
			view:				vLLLmonitor
			loop:				lCameraMonitor
			posn:				276 6
			setPri:			15
			ignoreActors:
			isExtra:			TRUE
			setCycle:		Forward
			init:
		)
		((= aTV (Prop new:))
			view:				vLLLmonitor
			loop:				lMC
			cel:				0
			posn:				49 52
			setPri:			15
			ignoreActors:
			setCycle:		Forward
			init:
		)
		((= aWheel (Prop new:))
			view:				vLLLwheel
			loop:				lWheel
			cel:				0
			posn:				250 97
			setPri:			9
			ignoreActors:
			stopUpd:
			init:
		)
		((= aCameraman (Actor new:))
			view:				vCameraman
			loop:				facingWest
			posn:				90 132
			moveSpeed:		1
			cycleSpeed:		1
			init:
		)
		((= aLana (Actor new:))
			name:				"lana"
			view:				vHostess
			loop:				lWalking
			posn: 			19 1092
			ignoreActors:
			setCycle:		Walk
			illegalBits:	0
			init:
		)

		(ego
			view: 			vEgo
			setLoop:			RELEASE
			posn: 			33 92
			setCycle:		Walk
			init:
			illegalBits:	0
		)

		(HandsOff)
		(= currentStatus IN_STUDIO)
		(self setScript: rm104Script)
	)
)


(instance rm104Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(return)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(= seconds 2)
			)

			(hereHeIs
				(Print "\"Ladies and gentlemen, we've finally found him!\"
					says the master of ceremonies.
					\"Yes, we've found our last Lucky Life Lottery
					contestant of this week's show!\"")
				(Print "\"And here he is now... Mr. Larry uh,\"
					he double-checks the teleprompter,
					\"Laffer? Yeah, Mr. Larry Laffer!\"")
				(aApplause setCycle:	Forward)
				(= seconds 1)
			)

			(someApplause
				(Print "(a little applause)" LOW)
				(StopMC)
				(ego setMotion: MoveTo 77 92 self)
			)

			(startMCtalking
				(StartMC)
				(aApplause stopUpd:)
				(ego setMotion: MoveTo 99 92 self)
			)

			(atStage
				(Print "\"Mr. Laffer, we don't have time for our
					normal introductory chit-chat.
					Just step right up to the wheel and give 'er a big spin!\"")
				(StopMC)
				(aMC
					setLoop:			lGestureRight
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(gestureDone
				(ego setMotion: 	MoveTo 144 98 self)
			)

			(atMC
				(Print "Under his breath, the MC admonishes you:
					\"(Hurry up, Laffer!
					We're already runnin' late because we couldn't find you!)\"")
				(aCameraman
					setMotion:		MoveTo 222 132
					setCycle:		Walk
				)
				(ego setMotion: MoveTo 234 101 self)
				(= cycles 30)
			)

			(stopTalking
				(aMC setLoop:		lMCtalking)
				(StopMC)
			)

			(atWheel
				(mDrumRoll			play:)
				(= seconds 2)
			)

			(wait1
				(Print "Nervously, you reach for the wheel,
					knowing full well that the pleasure of America's
					3-D, graphic, animated, adventure game players
					is riding on the luck of your pull!")
				(aCameraman		stopUpd:)
				(ego
					view: 		vLLLwheel
					loop: 		lEgoSpinning
					cel: 			0
					cycleSpeed:	1
					setCycle:	EndLoop self
				)
				(aWheel
					cel: 			0
					cycleSpeed:	0
					setCycle:	Forward
				)
				(aTV
					setLoop:		lLarrySpinning
					cel: 			0
					cycleSpeed:	1
					setCycle:	Forward
				)
			)

			(spinWheel
				(ego
					view: 		vEgo
					loop: 		facingEast
					cel: 			0
					cycleSpeed:	0
					setMotion:	0
					stopUpd:
				)
				(aTV
					setLoop:		lWheelOnly
					cel: 			0
					cycleSpeed:	0
				)
				(= seconds 3)
			)

			(slow1
				(aWheel			cycleSpeed:	1)
				(aTV				cycleSpeed:	1)
				(= seconds 2)
			)

			(slow2
				(aTV
					setLoop:		lFlapper
					cycleSpeed:	2
				)
				(aWheel			cycleSpeed:	2)
				(= seconds 2)
			)

			(slow3
				(aWheel			cycleSpeed:	4)
				(aTV				cycleSpeed:	4)
				(= seconds 2)
			)

			(win1
				(aTV
					setLoop:		lLarryShocked
					cel:			0
					cycleSpeed:	1
					setCycle:	EndLoop
				)
				(aWheel			stopUpd:)
				(mDrumRoll		dispose:)
				(mThemeSong		play:)
				(aSign			setCycle: Forward)
				(aApplause 		setCycle: Forward)
				(= seconds 4)
			)

			(win2
				(StartMC)
				(theGame changeScore: 12)
				(= seconds 1)
			)

			(win3
				(Print "\"He did it!
					HE DID IT!
					He's won the big one!!\n\n
					Ladies and gentlemen,
					Larry Laffer just won the largest prize in the
					history of the Lottery --
					a million dollars a year for life!!\"")
				(Print "(applause)" LOW)
				(Print "(tons o' applause)" LOW)
				(= seconds 3)
			)

			(heyYouComeHere
				(Print "\"Come on over here, Lucky Larry.\"")
				(ego
					setCycle:	Walk
					setMotion:	MoveTo 149 98 self
				)
				(StopMC)
				(aTV
					setLoop:		lLuckySign
					setCycle:	Forward
				)
				(aCameraman 	setMotion:	MoveTo 161 132)
			)

			(startCheck
				(aApplause 		stopUpd:)
				(aSign			stopUpd:)
				(aCameraman 	stopUpd:)
				(ego 				setMotion:	MoveTo 149 95)
				(aTV
					setLoop:		lMillionSign
					stopUpd:
				)
				(aMC
					setLoop:		lGestureLeft
					cel:			0
					setCycle:	EndLoop self
				)
				(Print "\"Here comes lovely Lanna Light,
					the Lucky Life Lottery lady,
					with Larry Laffer's first year's winnings:
					the U. S. Treasury Department's
					first one million dollar bill!\"" draw:)
			)

			(makeHostessCome
				(aMC
					setLoop:		lMCtalking
					forceUpd:
				)
				(StopMC)
				(aLana
					posn: 		19 92
					setMotion:	MoveTo 82 92 self
				)
			)

			(hostessThruDoor
				(aLana			setMotion:	MoveTo 117 94 self)
				(ego
					loop:			facingWest
					stopUpd:
				)
			)

			(checkAtLarry
				(aLana
					setLoop:		lPresenting
					setCel:		0
					setCycle:	FALSE
				)
				(ego
					setCycle:	FALSE
				)
				(aCameraman 	setMotion:	MoveTo 161 132)
				(= seconds 2)
			)

			(hereTis
				(Print "\"On behalf of the Lucky Life Lottery,
					I'm pleased to present you our Grand Prize,
					one million dollars!\" says the MC.")
				(aLana
					setCel:		0
					cycleSpeed:	1
					setCycle:	EndLoop
				)
				(ego
					view:			vHostess
					setLoop:		lEgoPocketing
					setCel:		0
					cycleSpeed:	1
					setCycle:	EndLoop self
				)
			)

			(checkInPocket
				(aLana			stopUpd:)
				(theGame changeScore: 7)
				(ego get: iMillion)
				(= seconds 3)
			)

			(shakeHands
				(Print "\"Congratulations, Mr. Laffer,\" says the MC,
					\"you're a lucky man!
					Too bad we don't have time for a speech!\"")
				(ego					hide:)
				(aMC
					setLoop:		lShakeHands
					cycleSpeed:	1
					setCycle:	Forward
				)
				(= seconds 4)
			)

			(getCheck
				(aMC
					setLoop:		lMCtalking
				)
				(ego
					view:			vEgo
					loop:			facingEast
					show:
				)
				(StartMC)
				(= seconds 3)
			)

			(heWon
				(StopMC)
				(Print "\"Let's hear it for him,
					ladies and gentlemen:
					Mr. Larry Laffer,
					the luckiest guy in Los Angeles!!\"" draw:)
				(Print "(even more applause)" LOW)
				(aTV
					setLoop:		lLuckySign
					setCycle:	Forward
				)
				(= seconds 3)
			)

			(showsOver
				(Print "The voiceover announcer says,
					\"That's it for this week's show;
					be sure to tune in next time for the 'Lucky Life Lottery'
					show, live from Hollywood!\"")
				(Print "\"Good night.\"")
				(aTV
					setLoop:		lMillionSign
					setCycle:	Forward
				)
				(aSign
					setCycle:	Forward
				)
				(= seconds 5)
			)

			(thatsAwrap
				(mThemeSong		stop:)
				(aSign			stopUpd:)
				(aTV
					setLoop:		lMstatic
				)
				(Print "The control room talkback speaker crackles,
					\"Ok, kids, there's another one
					that's good enough for who it's for!\"" draw:)
				(Print "\"Mr. Laffer, please exit through the door to the left;
					there will be plenty of reporters and photographers waiting
					for you!\"")
				(= seconds 2)
			)

			(leaveStage
				(ego
					cycleSpeed:	0
					setLoop:		RELEASE
					setCycle:	Walk
					setMotion:	MoveTo 18 112 self
				)
			)

			(atDoor
				(ego				setMotion:	MoveTo -12 112 self)
			)

			(nextRoom
				(Print "Gee, Larry, things are finally beginning to go your way!
					First, you win a month-long ocean voyage with the lovely
					bachelorette,
					and now you win the biggest lottery prize in history.")
				(= seconds 2)
			)

			(punchLine
				(Print "(Something tells you this is too good to last!)" LOW)
				(curRoom newRoom: 101)
			)
		)
	)
)


(procedure (StartMC)
	(aTV
		setLoop:			lMC
		setCel:			0
		cycleSpeed:		0
		setCycle:		Forward
	)
	(aMC
		setCel:			0
		cycleSpeed:		0
		setCycle:		Forward
	)
)


(procedure (StopMC)
	(aMC
		setCel:			0
		stopUpd:
	)
	(aTV
		setCel:			0
		stopUpd:
	)
)

