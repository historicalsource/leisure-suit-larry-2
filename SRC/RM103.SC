;**
;**	Logics for room 103 -- Dating Connection studio
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 22, 1988
;**


(script#				103)

(procedure
	StartTalk
	StopTalk
)


(public											;**	entry point to room
	rm103				0
	StartTalk		1
	StopTalk			2
)


(define	girlX				86)
(define	boy1X				170)
(define	larryX			205)
(define	boy3X				238)

(define	headY				66)
(define	cameramanY		144)
(define	doorwayY			93)

;(define	vMC							207)
;	(define	lPodium			0)
;	(define	lMCtalking		1)				;**	Cel 0 = mouth shut
;	(define	lShakeHands		2)				;**	Includes ego
;	(define	lGestureRight	3)
;	(define	lGestureLeft 	4)
;	(define	lApplause		5)

;(define	vDCpeople					208)
	(define	lStools			0)
		(define	cBoy1		0)
		(define	cBoy3		1)
		(define	cLarry	2)
		(define	cGirl		3)
		(define	cEmpty	4)

	(define	lHeadBoy1		1)
	(define	lHeadBoy3		2)
	(define	lHeadGirl		3)
	(define	lHeadLarry		4)
	(define	lSurprised		5)

;(define	vDCmonitor					209)
	(define	lMboy1Talk		0)
	(define	lMlarryTalk		1)
	(define	lMboy3Talk		2)
	(define	lMgirlTalk		3)
	(define	lMstatic			4)
	(define	lMmc				5)
	(define	lMcruiseShip1	6)
	(define	lMcruiseShip2	7)
		(define	cSurprised	13)

;(define	vDCstudio					210)
	(define	lSignEast		0)
	(define	lSignWest		1)
	(define	lCameraMonitor	2)

;(define	vCameraman					212)
	(define	lKneel			2)				;**	preceded by east & west loops


(local
	[userLine 40]
	aSignWest
	aSignEast
	aCameraMonitor
	aTV
	aMC
	aCameraman
	aGirl
	aBoy1
	aBoy3
	aLarry
	aStoolLarry
	aApplause
)


(enum												;**	minicamScript state values
	minicamInit
	kneel
	stopUpdate

	toGirl
	walkToGirl

	toBoy1
	walkToBoy1

	toLarry
	walkToLarry

	toBoy3
	walkToBoy3
)

(enum
	intro
	walkIn
	atStool
	onStool

	mcWelcome
	meetBarbara
	meetBoy1
	meetLarry
	meetLarry1
	meetBoy3
	nowLetsPlay

	mcQ1
	girlQ1
	boy1A1
	girlA1

	waitingForLarryA2
	noLarryA2
	larryA2
	girlA2

	girlQ3
	boy3A3
	girlA3

	mcQ4
	girlQ4
	boy1A4
	girlA4

	waitingForLarryA5
	noLarryA5
	larryA5
	girlA5

	girlQ6
	boy3A6
	girlA6

	mcMakeChoice
	girlPicks
	mcSurprised
	girlScreams
	mcDecisionFinal
	voiceOver1
	voiceOver2
	voiceOver3

	girlGripes
	showDone
	mcLarryGo

	offStool
	goToDoor
	atDoor
)


(instance theSound of Sound
	(properties
		number:			mDC
		priority:		100
		loop:				-1
	)
)


(instance rm103 of Room
	(properties
		picture:		103
		horizon:		1
	)

	(method (init)
		(Load VIEW vMC)
		(Load VIEW vCameraman)
		(Load VIEW vDCpeople)
		(Load VIEW vDCmonitor)
		(Load VIEW vDCstudio)
		(Load SOUND mDC)

		(super init:)

		(theSound	init:)

		((= aSignWest (Prop new:))
			view: 			vDCstudio
			loop: 			lSignWest
			cel: 				1
			posn: 			97 54
			priority: 		1
			ignoreActors:
			init:
			stopUpd:
		)
		((= aSignEast (Prop new:))
			view: 			vDCstudio
			loop: 			lSignEast
			cel: 				0
			posn: 			197 54
			priority: 		1
			ignoreActors:
			init:
			stopUpd:
		)
		((= aApplause (Prop new:))
			view:				vMC
			setLoop:			lApplause
			setCel:			0
			setPri:			15
			posn:				127 16
			init:
			stopUpd:
		)
		((= aTV (Prop new:))
			view:				vDCmonitor
			loop:				lMstatic
			cel:				0
			posn:				49 52
			setPri:			15
			ignoreActors:
			setCycle:		Forward
			init:
		)
		((= aCameraMonitor (Prop new:))
			view:				vDCstudio
			loop:				lCameraMonitor
			posn:				276 6
			setPri:			15
			ignoreActors:
			isExtra:			TRUE
			init:
			setCycle:		Forward
		)

		((View new:)							;**	MC bottom
			view: 			vMC
			loop: 			lPodium
			cel: 				0
			posn: 			31 93
			setPri:			5
			ignoreActors:
			addToPic:
		)
		((= aMC (Prop new:))					;**	MC head
			view:				vMC
			loop:				lMCtalking
			cel:				0
			posn:				31 78
			setPri:			5
			ignoreActors:
			stopUpd:
			init:
		)

		((View new:)							;**	girl bottom
			view:				vDCpeople
			loop: 			lStools
			cel: 				cGirl
			posn: 			girlX headY
			setPri: 			4
			ignoreActors:
			addToPic:
		)
		((= aGirl (Prop new:))				;**	girl head
			view:				vDCpeople
			setLoop:			lHeadGirl
			setCel:			0
			posn:				girlX headY
			setCycle:		Forward
			setPri:			6
			ignoreActors:
			stopUpd:
			init:
		)

		((View new:)							;**	boy1 bottom
			view:				vDCpeople
			loop: 			lStools
			cel: 				cBoy1
			posn: 			boy1X headY
			setPri: 			4
			ignoreActors:
			addToPic:
		)
		((= aBoy1 (Prop new:))				;**	boy1 head
			view:				vDCpeople
			setLoop:			lHeadBoy1
			setCel:			0
			posn:				boy1X headY
			setPri:			6
			ignoreActors:
			init:
			stopUpd:
		)

		((= aStoolLarry (Prop new:))		;**	larry bottom
			view:				vDCpeople
			setLoop:			lStools
			setCel: 			cEmpty
			posn:				larryX 88
			setPri: 			4
			ignoreActors:
			init:
			stopUpd:
		)
		((= aLarry (Prop new:))				;**	Larry head
			view:				vDCpeople
			setLoop:			lHeadLarry
			setCel:			0
			posn:				larryX headY
			setPri:			5
			ignoreActors:
			init:
			hide:
		)

		((View new:)							;**	boy3 bottom
			view:				vDCpeople
			loop: 			lStools
			cel: 				cBoy3
			posn: 			boy3X headY
			setPri: 			2
			ignoreActors:
			addToPic:
		)
		((= aBoy3 (Prop new:))				;**	boy3 head
			view:				vDCpeople
			setLoop:			lHeadBoy3
			setCel:			0
			posn:				boy3X headY
			setPri:			3
			ignoreActors:
			init:
			stopUpd:
		)

		((= aCameraman (Actor new:))
			view:				vCameraman
			loop:				facingEast
			cel:				0
			posn:				girlX cameramanY
			moveSpeed:		2
			cycleSpeed:		2
			setCycle:		Walk
			setStep:			3 2
			init:
		)

		(ego
			view: 			vEgo
			loop:				facingWest
			posn: 			291 doorwayY
			setCycle:		Walk
			setPri: 			5
			init:
		)
		(HandsOff)
		(= currentStatus IN_STUDIO)
		(self setScript: rm103Script)
		(aCameraman setScript: minicamScript)
	)
)


(instance rm103Script of Script
	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (event claimed?)			(return))

		(if (and
				(== (event type?) keyDown)
				(<= SPACEBAR (event message?) 127)	; only a typeable character
				(or	(== state waitingForLarryA2)
						(== state waitingForLarryA5)))
			(event claimed: TRUE)

			(if (!= (event message?) SPACEBAR) 		; space bar echoes the line
				(= userLine (event message?))
			)

			(GetInput @userLine 38 {Give her your best line...})
			(if ( < (StrLen @userLine) 7)
				(Format @userLine {I dunno.})
			)

			(if (== state waitingForLarryA2)
				(self changeState: larryA2)
			)

			(if (== state waitingForLarryA5)
				(self changeState: larryA5)
			)

			(return)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(Print "The control room talkback speaker crackles,
					\"Five seconds to air, boys and girls.
					Five seconds to air!
					Places, everybody!\"")
				(ego setMotion: FALSE)
				(= seconds 4)
			)

			(walkIn
				(Print "This doesn't look at all like
					you expected a lottery show to look.
					But, just in case, you decide to grab a seat
					on that empty stool over there.")
				(ego setMotion: MoveTo larryX doorwayY self)
				(minicamScript changeState: toLarry)
			)

			(atStool
				(= seconds 2)
			)

			(onStool
				(ego hide:)						;**	Turn ego into...
				(aStoolLarry					;**			...a stool and a...
					posn:				larryX headY
					setCel:			cLarry
					forceUpd:
				)
				(aLarry							;**						...talking head.
					show:
				)
				(= seconds 4)
			)

			(mcWelcome
				(theSound	play:)
				(Print "The voiceover announcer says,
					\"From Hollywood, it's the latest and greatest
					in embarrassment programming --
					the all new 'Dating Connection!'\"")
				(StartTalk	aMC lMmc)
				(Print "\"And here's your host, Biff Barf!\"" draw:)
				(aSignWest	setCycle:	Forward)
				(aSignEast	setCycle:	Forward)
				(aApplause	setCycle:	Forward)
				(minicamScript changeState: toGirl)
				(= seconds 4)
			)

			(meetBarbara
				(Print "\"Thank you and welcome; welcome, everybody!
					I'm so glad you're here for the all new
					'Dating Connection.'\"")
				(Print "\"We're just about ready to play our game,
					so let's meet our contestants.
					Who's going to make their big 'Dating Connection' today?\"")
				(StopTalk			aMC)
				(aTV
					view:				vDCmonitor
					setLoop:			lMgirlTalk
					setCel:			2
					stopUpd:
				)
				(Print "\"Biff, today's lovely bachelorette is Barbara Bimbo,
					of Airhead, California.\"" draw:)
				(Print "\"Barbara's hobbies are computer programming,
					creating unusual milkshakes,
					and tantalizing elderly men.
					She lists her turn-ons as
					industrial-grade blenders and
					RS-232 interfacing.\"")
				(Print "\"Turn-offs include
					international military conflicts
					and the aroma of rosin-core solder.\"")
				(= seconds 4)
			)

			(meetBoy1
				(minicamScript changeState: toLarry)
				(aTV
					view:				vDCmonitor
					setLoop:			lMboy1Talk
					setCel:			2
					forceUpd:
				)
				(Print "\"On the left is bachelor number one,
					a professional surfboard waxer from Gumbo, Missouri.
					His hobbies include
					collecting braunschweiger casings,
					speculating on the sexual preference of professional dancers,
					and watching televised opera with the sound off.\"" draw:)
				(Print "\"Meet Davie Blair!\"")
				(= seconds 4)
			)

			(meetLarry
				(aTV
					view:				vDCmonitor
					setLoop:			lMlarryTalk
					setCel:			2
					forceUpd:
				)
 				(Print "\"Bachelor number two is today's token intellectual.
					He is presently chairman of the Physics Department
					at our local university, F.U.\"" draw:)
				(Print "\"Originally from Pakistan, Ohio, meet
					Raguka Singh Soong.\"")
				(Print "Hey, that's not your name!")
				(= seconds 2)
			)

			(meetLarry1
				(Print (Format @string
					"\"Uh, excuse me,\" you shout.
					%s" laffer))
				(Print "\"Ok, Raguka Larry,\" responds Biff.
					\"Whatever you want to call yourself
					is ok with me!\"")
				(= seconds 3)
			)

			(meetBoy3
				(aTV
					view:				vDCmonitor
					setLoop:			lMboy3Talk
					setCel:			2
					forceUpd:
				)
				(Print "\"And, on the far right today is bachelor number three,
					a journalism major who quit college in order
					to pursue his dream job: fact-checker on the
					'National Enquiry' newspaper.\"" draw:)
				(Print "\"Currently unemployed, meet A. P. Wire!\"")
				(= seconds 2)
			)

			(nowLetsPlay
				(aSignWest	stopUpd:)
				(aSignEast	stopUpd:)
				(aApplause	stopUpd:)
				(theSound	stop:)
				(StartTalk	aMC lMmc)
				(= seconds 2)
			)

			(mcQ1
				(Print "\"And now, it's time to play 'The Dating Connection!'\"
					says Biff Barf.
					\"Barbara, may we have your first question please?\"")
				(StopTalk	aMC)
				(StartTalk	aGirl lMgirlTalk)
				(minicamScript changeState: toGirl)
				(= seconds 4)
			)

			(girlQ1
				(Print "\"Ok like thanks, Biff, ya know,\" says Barbara.
					\"Bachelor number one, like this one's for you!\"")
				(Print "\"If I was to go like out on a you know date with you
					and you was like you know all dressed up or sumpthin'
					and like the car busted and we had to walk for help
					and I like broke a you know heel
					like how would you fix it?\"")
				(Print "(\"Like you know\" it seems obvious her elevator
					doesn't reach her penthouse!)" LOW)
				(StopTalk	aGirl)
				(StartTalk	aBoy1 lMboy1Talk)
				(= seconds 4)
			)

			(boy1A1
				(Print "\"Why, beautiful, if you were with me you wouldn't
					have to worry about any ol' breakdown.
					We'd be cruisin' in my brand new Porsche,
					and you wouldn't have a care in the world.\"")
				(Print "\"Besides, if something did happen to your heel,
					I'd just sweep you up in these muscular arms
					and carry you wherever you wanted to go!
					When you're a top notch physical specimen like me,
					a light little feather like you would be a breeze.\"")
				(Print "(You're sure not even this mental midget would
					fall for a cornball line like that!)" LOW)
				(StopTalk	aBoy1)
				(StartTalk	aGirl lMgirlTalk)
				(= seconds 4)
			)

			(girlA1
				(Print "\"Geeeeez, how romantic!\" she gushes.
					\"What more could any girl want?\"")
				(Print "\"Well, bachelor, you're 'number one' with me!\"")
				(Print "(So much for your theory, Larry!)" LOW)
				(minicamScript changeState: toLarry)
				(= seconds 4)
			)

			(waitingForLarryA2
				(Print "\"And now, how about you, bachelor number two?\"
					she asks you. \"How would you solve this puzzle?\"")
				(StopTalk 			aGirl)
				(aTV
					view:				vDCmonitor
					setLoop:			lMlarryTalk
					setCel:			1
					stopUpd:
				)
				(aLarry
					setCel:			1
					forceUpd:
				)
				(Print "Ok, Larry, that's your cue. You're ON!" draw:)
				(User canInput:	TRUE)
				(= seconds 11)
			)

			(noLarryA2
				(Print "\"Well, Raguka Larry,\" prompts Biff Barf,
					\"we're waiting for your answer!\"")
				(= state waitingForLarryA2)
				(= seconds 11)
			)


			;**	El Dorko finally typed something in; let's make fun of him!

			(larryA2
				(= cycles 0)
				(User canInput:	FALSE)
				(Print (Format @string "\"Uhhh, well, er,\" you stumble,
					\"%s\"" @userLine))
				(StopTalk	aLarry)
				(StartTalk	aGirl lMgirlTalk)
				(= seconds 4)
			)

			(girlA2
				(Print "\"Who let in that jerk?!\" Barbara squeals.
					\"What a putz!\"")
				(= seconds 4)
			)

			(girlQ3
				(Print "Barbara attempts to assume an intelligent expression.
					\"Now how about you bachelor number you know three?\"")
				(minicamScript changeState: toBoy3)
				(StopTalk	aGirl)
				(StartTalk	aBoy3 lMboy3Talk)
				(= seconds 4)
			)

			(boy3A3
				(Print "\"Bachelorette Barbie, my deepest, personal feelings
					are that you are far too sexy for me ever to allow you
					to leave my highly-expensive, beachfront,
					swinging bachelor apartment.\"")
				(Print "\"You and I would spend all our time together alone,
					sharing each other in every way (if you know what I mean)!\"")
				(Print "(How's that for a way with words?)" LOW)
				(StopTalk	aBoy3)
				(StartTalk	aGirl lMgirlTalk)
				(= seconds 4)
			)

			(girlA3
				(minicamScript changeState: toGirl)
				(Print "\"Oooooh, how sexy!\" she gushes.
					\"What more could any girl want?\"")
				(Print "\"You may be bachelor number three on this program,
					but you're 'number one' in my heart!\"")
				(Print "(Do you think anyone's still watching this tripe?)" LOW)
				(StopTalk	aGirl)
				(StartTalk	aMC lMmc)
				(= seconds 4)
			)

			(mcQ4
				(Print "\"Barbara, in my opinion you've just asked
					one of our best questions ever,\" says Biff Barf.
					\"Now, do you think you could come up
					with another great question?\"")
				(StopTalk	aMC)
				(StartTalk	aGirl lMgirlTalk)
				(= seconds 4)
			)

			(girlQ4
				(minicamScript changeState: toBoy1)
				(Print "Once again, Barbara burns off a few million
					brain cells attempting to rise to the occasion.")
				(Print "\"Ok like thanks Biff ya know,\" says Barbara.
					\"Bachelor number one, like this one's for you!\"")
				(Print "\"Like if you was a you know insect and
					like I was a you know flower
					what kind of you know insect would you like be
					and like what kind of flower am I?\"")
				(Print "(Sigh.)" LOW)
				(StopTalk	aGirl)
				(StartTalk	aBoy1 lMboy1Talk)
				(= seconds 4)
			)

			(boy1A4
				(Print "\"Barbara,\" responds bachelor number one,
					\"I'd be a beautiful butterfly
					and you'd be my tender little buttercup.
					Together, we'd build a wonderful garden of love!\"")
				(Print "(At least his garden would be well-fertilized!)" LOW)
				(StartTalk	aGirl lMgirlTalk)
				(StopTalk	aBoy1)
				(= seconds 4)
			)

			(girlA4
				(Print "\"Like you ARE quite the charmer,
					aren't you, number one?\" she gushes.
					\"I'd love to sow a few seeds with you!\"")
				(= seconds 4)
			)

			(waitingForLarryA5
				(minicamScript changeState: toLarry)
				(Print "\"Biff,\" she says, \"MUST I like waste our time asking
					El Dorko this question?\"")
				(Print "Biff responds, \"Why, of course, Bachelorette Barbara.
					You must follow the rules!\"")
				(Print "\"Ok, bore number two,\" she says sarcastically,
					\"can you know even like remember the question?\"")
				(StopTalk 			aGirl)
				(aTV
					view:				vDCmonitor
					setLoop:			lMlarryTalk
					setCel:			1
					stopUpd:
				)
				(StopTalk 			aLarry 1)
				(Print "Come on, Larry. Fire your best shot!" draw:)
				(User canInput:	TRUE)
				(= seconds 11)
			)

			(noLarryA5
				(Print "\"Come on, El Do--, uh I mean Raguka Larry,\"
					stammers Biff Barf, \"we need your answer now!\"")
				(= state waitingForLarryA5)
				(= seconds 11)
			)


			;**	El Dorko finally typed something in; let's make fun of him!

			(larryA5
				(= cycles 0)
				(User canInput:	FALSE)
				(Print (Format @string "\"Er, um, I think, ah that is,\" you stumble,
					\"%s\"" @userLine))
				(StartTalk	aGirl lMgirlTalk)
				(StopTalk	aLarry)
				(= seconds 4)
			)

			(girlA5
				(Print "\"Like, what planet beamed this spook down?!\" she squeaks.
					\"I've heard better lines in a you know nursing home!\"")
				(= seconds 4)
			)

			(girlQ6
				(Print "\"Well, bachelor number three,\" she says,
					\"I can't you know wait to learn like what insect are you
					and like what flower am you know I?\"")
				(StopTalk	aGirl)
				(StartTalk	aBoy3 lMboy3Talk)
				(minicamScript changeState: toBoy3)
				(= seconds 4)
			)

			(boy3A6
				(Print "\"Barbie doll, you'd be my precious
					American Beauty rose and I'd be your little bumblebee.
					I know you'd enjoy rubbing a little of your
					pollen on MY stinger!\"")
				(Print "(Does this show supply antacids?)" LOW)
				(StartTalk	aGirl lMgirlTalk)
				(StopTalk	aBoy3)
				(= seconds 4)
			)

			(girlA6
				(Print "\"Well, number three,
					we certainly are the oversexed little devil, aren't we?!\"
					Barbara gushes.")
				(StartTalk	aMC lMmc)
				(StopTalk	aGirl)
				(= seconds 1)
			)

			(mcMakeChoice
				(Print "\"Hold it right there!\" interjects Biff,
					\"I'm afraid that's all the time we have for questions today.
					Bachelorette Barbie:
					it's time for you to make your 'Dating Connection!'\"")
				(minicamScript changeState: toLarry)
				(StopTalk	aMC)
				(StartTalk	aGirl lMgirlTalk)
				(= seconds 4)
			)

			(girlPicks
				(StopTalk	aGirl 2)
				(Print "Barbie smiles, \"Well, Biff,
					it's really difficult to choose;
					both men are really terrific!\"" draw:)
				(Print "(Something makes you feel the \"both\"
					doesn't include you!)" LOW)
				(Print "\"Yes, but I'm sure the audience has already
					made its decision,\" says Biff.")
				(Print "\"Yes,\" says Barbara,
					\"and I've made my decision, you know, too.\"")
				(theSound	
					loop:		-1
					play:
				)
				(StopTalk	aGirl)
				(StartTalk	aMC lMmc)
				(aSignWest	setCycle: Forward)
				(aSignEast	setCycle: Forward)
				(aApplause	setCycle: Forward)
				(theGame changeScore: 20)
				(= seconds 5)
			)

			(mcSurprised
				(Print "Biff looks surprised, \"Ok, bachelor TWO it is.
					But frankly, Bachelorette Barbara,
					I must admit I'm a little surprised!\"")
				(StopTalk 			aMC)
				(aLarry
					setLoop:			lSurprised
					stopUpd:
				)
				(aTV
					setLoop:			lMcruiseShip2
					setCel:			cSurprised
				)
				(Print "(Did he say bachelor number TWO?)" draw: LOW)
				(Print "\"Barbara, I think most of us expected a
					different choice!\" says Biff Barf.")
				(aGirl setCycle:	Forward)
				(= seconds 1)
			)

			(girlScreams
				(Print "\"WAIT, BIFF!\" shouts Barbara,
					\"I wanted bachelor number THREE!!!\"")
				(StartTalk	aMC lMmc)
				(StopTalk	aGirl 2)
				(= seconds 4)
			)

			(mcDecisionFinal
				(Print "\"I'm sorry, Barbara,\" says Biff,
					\"the rules clearly state your first decision is final.\"")
				(Print "\"Let's find out what they've won!\"")
				(StopTalk 			aMC)
				(aTV
					setLoop:			lMcruiseShip1
					setCel:			0
					setCycle:		EndLoop self
					cycleSpeed:		2
				)
			)

			(voiceOver1
				(Print "The voiceover announcer booms,
					\"It's a CRUISE!\"")
				(Print "(audience goes oooooh)" LOW)
				(Print "\"Yes, a romantic cruise of the South Pacific
					on the beautiful U.S.S. Love Tub.\"")
				(aTV
					setLoop:			lMcruiseShip2
					setCel:			0
					setCycle:		EndLoop self
				)
			)

			(voiceOver2
				(Print "\"You'll spend a solid MONTH together,
					exploring exotic ports of call
					courtesy of Wonder Cruise Lines.\"" draw:)
				(Print "\"Remember: if it's a good cruise,
					it's a Wonder!\"")
				(= seconds 4)
			)

			(voiceOver3
				(Print "\"And for our other contestants, why,
					you lucky fellows have won a year's supply of armadillo polish,
					twenty cases of black shoe laces,
					plus a copy of our home game.\"")
				(aLarry		setLoop: lHeadLarry)	;**	close your mouth, Larry!
				(StartTalk	aGirl lMgirlTalk)
				(= seconds 4)
			)

			(girlGripes
				(Print "\"Hey, man!
					There ain't no way I'm spendin' no month
					on a boat with this jerk,\"
					shouts the delicate Bachelorette Barbara.
					\"I wanna chance to hit on that number three guy!!\"")
				(StopTalk	aGirl 2)
				(StartTalk	aMC lMmc)
				(= seconds 4)
			)

			(showDone
				(Print "\"I'm sure she's really truly delighted, folks,\"
					says Biff attempting to cover,
					\"Now come on -- let's really hear it for our lucky couple.\"")
				(Print "(a smattering of applause)" LOW)
				(Print "The voiceover announcer says,
					\"Be sure to tune in tomorrow night, same time,
					same station, for the finest in embarrassment programming:
					the all new 'Dating Connection.' Good night.\"")
				(theSound	stop:)
				(StopTalk	aMC)
				(aSignWest	stopUpd:)
				(aSignEast	stopUpd:)
				(aApplause	stopUpd:)
				(aTV
					loop:				lMstatic
					setCycle:		Forward
				)
				(aCameraman
					setCycle:		BegLoop
				)
				(= seconds 2)
			)

			(mcLarryGo
				(Print "The control room speaker crackles,
					\"Ok, gang, that's another one in the can!
					Raguka Larry, please report to the green room to receive
					your prizes.
					The rest of you just stay where you are!\"")
				(= seconds 2)
			)

			(offStool
				(= seconds 2)
				(aLarry				hide:)	;**	his head
				(aStoolLarry					;**	his body
					posn:				larryX 88
					setCel:			cEmpty
					forceUpd:
				)
				(ego
					loop:				facingEast
					show:
				)
			)

			(goToDoor
				(ego	setMotion:	MoveTo 291 doorwayY self)
			)

			(atDoor
				(Print "Gosh, Larry, what good luck you have:
					winning a fun-filled, month-long cruise with that
					lovely bachelorette Barbara!")
				(curRoom newRoom: 102)
			)
		)
	)
)


(instance minicamScript of Script
	(method (changeState newState)
		(switch (= state newState)

;**				(minicamInit)

			(kneel
				(aCameraman
					loop:			lKneel
					setCycle:	EndLoop self
				)
			)

			(stopUpdate
				(aCameraman stopUpd:)
			)

			(toGirl
				(aCameraman
					setCycle:	BegLoop self
				)
			)
			(walkToGirl
				(aCameraman
					setCycle:	Walk
					setMotion:	MoveTo girlX cameramanY self
				)
				(= state minicamInit)
			)

			(toBoy1
				(aCameraman
					setCycle:	BegLoop self
				)
			)
			(walkToBoy1
				(aCameraman
					setCycle:	Walk
					setMotion:	MoveTo boy1X cameramanY self
				)
				(= state minicamInit)
			)

			(toLarry
				(aCameraman
					setCycle:	BegLoop self
				)
			)
			(walkToLarry
				(aCameraman
					setCycle:	Walk
					setMotion:	MoveTo larryX cameramanY self
				)
				(= state minicamInit)
			)

			(toBoy3
				(aCameraman
					setCycle:	BegLoop self
				)
			)

			(walkToBoy3
				(aCameraman
					setCycle:	Walk
					setMotion:	MoveTo boy3X cameramanY self
				)
				(= state minicamInit)
			)
		)
	)
)


(procedure (StartTalk who tvLoop)
	(aTV
		setLoop:			tvLoop
		setCel:			0
		cycleSpeed:		0
		setCycle:		Forward
	)
	(who
		setCel:			0
		cycleSpeed:		0
		setCycle:		Forward
	)
)


(procedure (StopTalk who cel)
	(who
		setCel:			(if (> argc 1)
								cel
							else
								2
							)
		stopUpd:
	)
)

