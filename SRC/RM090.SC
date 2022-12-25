;**
;**	Logics for room 90 -- Banner Screen and Credits
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 13, 1988
;**


(script#				90)

(public
	rm90				0
)


(define	sparkleMax				10)		;**	number of sparkles on Title

;(define	vEgoTitle					194)
	(define	lPeekThrough		0)
	(define	lPopUp				1)
	(define	lFalling				2)

;(define	vTitle						800)
;(define	vCredits						801)
	(define	lCircles				0)			;**	cels 0-3
		(define	lastBall		8)
	(define	lGraphics			1)			;**	names cels 1-3
		(define	cBy			0)
		(define	cBill			1)
		(define	cBonnie		2)
		(define	cDoug			3)
	(define	lAlCredits			2)			;**	cycle
	(define	lAl					3)
	(define	lSCI					4)
;		(define	cBy			0)
		(define	cJeff			1)
		(define	cBob			2)
		(define	cPablo		3)
		(define	cStuart		4)


;(define	vTitleHenchette			809)
;(define	vSparkles					815)

;(define	mTitle						101)


(local
	aGirl
	aBall
	aName
	sparkleCount
	heardSong
)


(enum												;**	rmScript state values
	intro

	lookingFor									;**	1
	severalWrong
	doSparkle
	sparkleDone
	peekOut
	disappear
	pause1
	popUp
	goDown
	pause2
	dropHere
	fall

	doCredits									;**	13
	ballToGraphics
	graphicsBy
	waitBill
	ballToBonnie
	waitBonnie
	ballToDoug
	waitDoug

	ballToSCI									;**	21
	SCIby
	waitJeff
	ballToBob
	waitBob
	ballToPablo
	waitPablo
	ballToStuart
	waitStuart

	ballToAlBy									;**	30
	ballToAl
	waitAl
	waitUntilSongDone							;**	33
	startCartoon
)


(instance mThemeSong of Sound
	(properties
		number:			mTitle
		loop:				1
		priority:		20
	)
)


(instance rm90 of Room
	(properties
		picture:		90
		style:		WIPEDOWN
	)

	(method (init)

		(Load VIEW vTitle)
		(Load VIEW vEgoTitle)
		(Load VIEW vSparkles)
		(Load VIEW vCredits)
		(Load VIEW vTitleHenchette)
		(Load SOUND mTitle)
		(Load PICTURE 91)

		(StatusLine disable:)
		(TheMenuBar hide:)

		(super init:)

		(mThemeSong play:)

		((View new:)							;**	Leisure Suit Larry
			view: 			vTitle
			ignoreActors:
			posn: 			159 48
			setPri: 			8
			addToPic:
		)

		((= aGirl (Actor new:))
			view:				vTitleHenchette
			ignoreActors:
			illegalBits:	0
			setPri:			13
			setCycle:		Walk
			posn:				22 100
			cycleSpeed:		1
			moveSpeed:		1
			init:
			stopUpd:
		)

		(ego
			view:				vSparkles
			ignoreActors:
			illegalBits:	0
			setPri:			15
			moveSpeed:		0
			cycleSpeed:		0
			posn:				123 1123
			init:
		)

		(self setScript: rm90Script)
		(HandsOff)
		(theGame setSpeed: 6)
	)
)


(instance rm90Script of Script
	(method (doit)
		(super doit:)
		
		(if (== SND_DONE (mThemeSong prevSignal?))
			(mThemeSong prevSignal: FALSE)
			(= heardSong TRUE)
			(if (< state waitUntilSongDone)
				(mThemeSong play:)
			)
		)

		(if (and	heardSong
					(== state waitUntilSongDone))
			(curRoom newRoom: 91)
		)
	)

	(method (handleEvent event)
		(if (not (event claimed?))
			(theGame restart:)
		)
	)

	(method (changeState newState &tmp bx by ex ey)
		(switch (= state newState)

			(intro
				(= cycles 20)
			)

			(lookingFor
				((View new:)							;**	Looking For Love
					view: 			vTitle
					ignoreActors:
					loop: 			1
					posn: 			159 99
					setPri: 			10
					addToPic:
				)
				(= cycles 30)
			)

			(severalWrong
				((View new:)							;**	in Several Wrong Places
					view: 			vTitle
					ignoreActors:
					loop: 			2
					posn: 			162 135
					setPri: 			12
					addToPic:
				)
				(= cycles 20)
				(theGame setSpeed: 3)
			)

			(doSparkle
				(switch (Random 0 16)
					( 0	(= bx 149)	(= by	40))
					( 1	(= bx	64)	(= by	48))
					( 2	(= bx 161)	(= by	78))
					( 3	(= bx 234)	(= by	89))
					( 4	(= bx 114)	(= by 117))
					( 5	(= bx	91)	(= by	98))
					( 6	(= bx 179)	(= by	30))
					( 7	(= bx	61)	(= by	98))
					( 8	(= bx 255)	(= by	65))
					( 9	(= bx 130)	(= by	87))
					(10	(= bx 143)	(= by	81))
					(11	(= bx 212)	(= by	86))
					(12	(= bx	48)	(= by 108))
					(13	(= bx 205)	(= by 126))
					(14	(= bx 275)	(= by 122))
					(15	(= bx	92)	(= by 130))
					(16	(= bx 182)	(= by 133))
				)
				(ego
					posn:				bx by
					cel:				0
					setCycle:		EndLoop self
				)
				(if ( < (++ sparkleCount) sparkleMax)
					(= state (- doSparkle 1))
				)
			)

			(sparkleDone
				(ego					hide:)
				(theGame setSpeed: 6)
				(aGirl
					setMotion:		MoveTo 109 100 self
				)
			)

			(peekOut
				(aGirl
					setMotion:		MoveTo 175 100 self
				)
				(ego
					view: 			vEgoTitle
					loop: 			lPeekThrough
					cel: 				0
					posn: 			86 80
					setPri: 			15
					show:
					setCycle:		EndLoop self
				)
			)

			(disappear
				(ego
					setCycle:		BegLoop self
				)
			)

			(pause1
				(ego 					hide:)
			)

			(popUp
				(aGirl
					setMotion:		MoveTo 220 100 self
				)
				(ego
					loop: 			lPopUp
					cel: 				0
					posn: 			148 107
					show:
					setCycle:		EndLoop self
				)
			)

			(goDown
				(ego
					setCycle:		BegLoop self
				)
			)

			(pause2
				(ego 					hide:)
			)

			(dropHere
				(aGirl
					setMotion:		MoveTo 268 100 self
				)
				(ego
					loop: 			lFalling
					cel: 				0
					posn: 			199 58
					show:
					setCycle:		EndLoop self
				)
			)

			(fall
				(ego
					setCel:
					setStep:			1 15
					setMotion:		MoveTo 199 234
				)
			)

			(doCredits
				(ego					hide:)
				(aGirl				hide:)
				(= aBall ego)					;**	for documentation purposes only
				(= aName aGirl)
				(curRoom drawPic: 91 WIPEUP)
				(= cycles 10)
			)


			(ballToGraphics
				(theGame setSpeed: 3)
				(= bx (- (Random 0 420) 50))
				(aBall
					view:				vCredits
					setLoop:			lCircles
					setCycle:		FALSE
					setCel:			(Random 0 lastBall)
					posn:				bx 234
					setStep:			15 15
					illegalBits:	0
					ignoreActors:
					show:
					setMotion:		MoveTo 159 47 self
				)
			)

			(graphicsBy
				(= bx (- (Random 0 420) 50))
				(= ex (Random 50 270))
				(= ey (Random 70 180))
				(aBall
					setCel:			(Random 0 lastBall)
					posn:				bx -33
					setMotion:		MoveTo ex ey self
				)
				(aName
					view:				vCredits
					setLoop:			lGraphics
					setCycle:		FALSE
					setCel:			cBy
					setPri:			1
					setMotion:		FALSE
					ignoreActors:
					illegalBits:	0
					posn:				159 47
					show:
				)
			)

			(waitBill
				(aBall
					setLoop:			lGraphics
					setCel:			cBill
				)
				(= cycles 35)
			)

			(ballToBonnie
				(= bx (- (Random 0 420) 50))
				(= ex (Random 50 270))
				(= ey (Random 70 180))
				(aBall
					setLoop:			lCircles
					setCel:			(Random 0 lastBall)
					posn:				bx -33
					setMotion:		MoveTo ex ey self
				)
			)

			(waitBonnie
				(aBall
					setLoop:			lGraphics
					setCel:			cBonnie
				)
				(= cycles 35)
			)

			(ballToDoug
				(= bx (- (Random 0 420) 50))
				(= ex (Random 50 270))
				(= ey (Random 70 180))
				(aBall
					setLoop:			lCircles
					setCel:			(Random 0 lastBall)
					posn:				bx -33
					setMotion:		MoveTo ex ey self
				)
			)

			(waitDoug
				(aBall
					setLoop:			lGraphics
					setCel:			cDoug
				)
				(= cycles 35)
			)

			(ballToSCI
				(aName				hide:)
				(= bx (- (Random 0 420) 50))
				(aBall
					setLoop:			lCircles
					setCel:			(Random 0 lastBall)
					posn:				bx 234
					setMotion:		MoveTo 159 47 self
				)
			)

			(SCIby
				(= bx (- (Random 0 420) 50))
				(= ex (Random 50 270))
				(= ey (Random 70 180))
				(aBall
					setCel:			(Random 0 lastBall)
					posn:				bx -33
					setMotion:		MoveTo ex ey self
				)
				(aName
					setLoop:			lSCI
					setCel:			cBy
					posn:				159 47
					show:
				)
			)

			(waitJeff
				(aBall
					setLoop:			lSCI
					setCel:			cJeff
				)
				(= cycles 35)
			)

			(ballToBob
				(= bx (- (Random 0 420) 50))
				(= ex (Random 50 270))
				(= ey (Random 70 180))
				(aBall
					setLoop:			lCircles
					setCel:			(Random 0 lastBall)
					posn:				bx -33
					setMotion:		MoveTo ex ey self
				)
			)

			(waitBob
				(aBall
					setLoop:			lSCI
					setCel:			cBob
				)
				(= cycles 35)
			)

			(ballToPablo
				(= bx (- (Random 0 420) 50))
				(= ex (Random 50 270))
				(= ey (Random 70 180))
				(aBall
					setLoop:			lCircles
					setCel:			(Random 0 lastBall)
					posn:				bx -33
					setMotion:		MoveTo ex ey self
				)
			)

			(waitPablo
				(aBall
					setLoop:			lSCI
					setCel:			cPablo
				)
				(= cycles 35)
			)

			(ballToStuart
				(= bx (- (Random 0 420) 50))
				(= ex (Random 50 270))
				(= ey (Random 70 180))
				(aBall
					setLoop:			lCircles
					setCel:			(Random 0 lastBall)
					posn:				bx -33
					setMotion:		MoveTo ex ey self
				)
			)

			(waitStuart
				(aBall
					setLoop:			lSCI
					setCel:			cStuart
				)
				(= cycles 35)
			)


			(ballToAlBy
				(aName				hide:)
				(= bx (- (Random 0 420) 50))
				(aBall
					setLoop:			lCircles
					setCel:			(Random 0 lastBall)
					posn:				bx 234
					setMotion:		MoveTo 159 47 self
				)
			)

			(ballToAl
				(= bx (- (Random 0 420) 50))
				(= ex (Random 50 270))
				(= ey (Random 70 180))
				(aBall
					setLoop:			lCircles
					cel:				(Random 0 lastBall)
					posn:				bx -33
					setMotion:		MoveTo ex ey self
				)
				(aName
					setLoop:			lAlCredits
					cel:				0
					show:
				)
			)

			(waitAl
				(theGame setSpeed: 6)		;**	back to normal
				(aBall
					setLoop:			lAl
					setCel:			0
				)
				(aName
					cycleSpeed:		2
					setCycle:		Forward
				)
				(= cycles 72)
			)

;**				(waitUntilSongDone)

		)
	)
)

