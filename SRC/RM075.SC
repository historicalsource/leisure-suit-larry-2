;**
;**	Logics for room 75 -- Kalalau Beach
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 20, 1988
;**


(script#				75)

(public
	rm75				0
)


;(define	vLoveCliches				703)
	(define	lCupidWest			0)
	(define	lCupidEast			1)
	(define	lHearts				2)
	(define	lHeartThumping		3)
	(define	lEgoHeart			4)
	(define	lEgoHeartbeat		5)
	(define	lKalalauHeart	6)
	(define	lKalalauHeartbeat 7)
	(define	lFireworksEast		8)
	(define	lFireworksWest		9)

;(define	vKalalau						704)
;(define	vKalalauWading				705)
	(define	lRaiseArm			0)
	(define	lWave					1)
	(define	lLeaveWater			2)

;(define	vKalalauKissing			706)
	(define	lTheKiss				0)
	(define	lTheKissWest		1)

;(define	vCopterTail					807)

;(define	mLoveTheme					116)


(local
	seenMessage
	aKalalau
	aCupidWest
	aCupidEast
	aCopter
)


(enum												;**	rmScript state values
	intro

	loveInBloom
	theGlance
	theWave
	heNotices
	geographic
	sheMoves
	beached
	it'sLove

	lookAtHer
	showBigK
	wow
	backTo75

	cliche1
	cliche1pause
	cliche1done

	cliche2
	cliche2done

	cliche3
	cliche3beat
	cliche3shrink
	cliche3done

	cliche4
	startSecondFirework
	stopFirstFirework
	cliche4done

	moveItLarry
	theKiss
	holdIt
	breakLips
	kissDone

	plotExposition
	I'mYourMan
	takeOff
	clearOfLarry
	larryAlso

	startLarry
	moveLarry
	startGirl
	theKiss2
	holdIt2
	breakLips2
	kissDone2
	iDidIt
)


(instance theSound of Sound
	(properties
		loop:			-1
	)
)


(instance rm75 of Room
	(properties
		picture:		75
		horizon:		5
		south:		74
		west:			77
	)

	(method (init)
		(super init:)

		(NormalEgo)

		(self setScript: 	rm75Script)

		(if forceAtest
			(= islandStatus wedding)
		)

		(cond
			((== islandStatus FALSE)
				(= islandStatus metGirl)		;**	This is it!
				(= currentStatus INITIATION)
				(rm75Script changeState: loveInBloom)

				(Load VIEW vLoveCliches)
				(Load VIEW vKalalau)
				(Load VIEW vKalalauWading)
				(Load VIEW vKalalauKissing)
				(Load FONT HELVETICA18)
				(Load PICTURE  99)
				(Load SOUND mLoveTheme)
				
				(theSound 	
				 	number:			mLoveTheme
					init:
				)

				((= aKalalau (Actor new:))
					view:				vKalalauWading
					loop:				lRaiseArm
					ignoreActors:
					illegalBits:	0
					posn:				190 125
					init:
				)
				((= aCupidEast (Prop new:))
					view:				vLoveCliches
					ignoreActors:
					setLoop:			lCupidEast
					posn:				217 127
					setCycle:		Forward
					init:
					hide:
				)
				((= aCupidWest (Prop new:))
					view:				vLoveCliches
					ignoreActors:
					setLoop:			lCupidWest
					cel:				3
					posn:				84 127
					setCycle:		Forward
					init:
					hide:
				)
				(HandsOff)
				(ego	posn: 	115 186)
			)

			((== islandStatus onBeach)
				(= islandStatus getDaddy)
				(= currentStatus CLOSING_CARTOON)
				(rm75Script changeState: startLarry)

				(Load VIEW vKalalau)
				(Load VIEW vKalalauKissing)
				(Load VIEW vCopterTail)
				(Load SOUND sCopter)

				(theSound 	
				 	number:			sCopter
					play:
				)

				((= aCopter (Prop new:))
					view:				vCopterTail
					posn:				319 137
					setCycle:		Forward
					init:
				)
				((= aKalalau (Actor new:))
					view:				vKalalau
					loop:				facingSouth
					ignoreActors:
					illegalBits:	0
					posn:				-11 146
					setCycle:		Walk
					init:
				)
				(HandsOff)
				(ego posn: 			327 187)
			)

			(else
				(self setRegions:	rgIsland)
				(if (== prevRoomNum 77)
					(ego	posn: 	2 136)
				else
					(ego	posn: 	115 186)
				)
			)
		)

		(ego		init:)
	)
)


(instance rm75Script of Script
	(method (doit)
		(super doit:)

		(if (& (ego onControl:) cGray)
			(if (== seenMessage FALSE)
				(= seenMessage TRUE)
				(Print "You have no desire to return to the ocean
					after all you went through on that lifeboat, Larry!")
			)
		else
			(= seenMessage FALSE)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/water, ocean')
				(Print "The spectacular lagoon is crystal clear.")
				(Print "Too bad you didn't bring that swimming suit!")
			)

			(if (Said '[/room, mountain]')
				(Print "In the distance you see the island's volcano
					ringed by a glacier,
					not at the top as you'd expect,
					but near the bottom of some vertical cliffs.")
				(Print "But here a beautiful, tropical beach
					wraps its glorious splendor around a crystalline lagoon.
					What a wonderful place to vacation!")
				(Print "(You wonder why it isn't covered with lots of
					cheap motels?!)" LOW)
			)
		)

		(if (and (not (ego has: iSand))
					(Said 'look/sand'))
			(Print "This sandy beach is beautiful.")
		)

		(if (Said 'play, dig/sand')
			(Print "Whee!")
		)

		(if (Said 'get/sand')
			(cond
				((!= currentStatus NORMAL)
					(NotNow)
				)
				((not ((inventory at: iSand) ownedBy: curRoomNum))
					(AlreadyTook)
				)
				((not (& (ego onControl: origin) cYellow))
					(NotClose)
				)
				(else
					(ego get: iSand)
					(theGame changeScore: 3)
					(Print "You bend over and scoop up a large handful of
						beach sand and put it in your pockets.")
					(if ( > filthLevel DIRTY)
						(Print "(At last your pants are bulging!)" LOW)
					)
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(intro)


			;**	Here's where our two lovers meet, and fall in it...

			(loveInBloom
				(= seconds 3)
			)

			(theGlance
				(aKalalau
					cycleSpeed:	1
					setCycle:	EndLoop self
				)
			)

			(theWave
				(aKalalau
					setLoop:		lWave
					setCycle:	Forward
				)
				(= seconds 3)
			)

			(heNotices
				(Print "Larry, look!
					Out in the surf!
					That beautiful native girl is waving at you, and..." HIGH)
				(Print "\n\ SHE'S TOPLESS!!\n\n"
					font: HELVETICA18 HIGH width: 222)
				(theSound		play:)
				(= seconds 3)
			)

			(geographic
				(Print "\"Gee,\" you think to yourself,
					\"I thought girls like that only existed in
					'National Geographic!'\"")
				(aKalalau
					setLoop:		lRaiseArm
					setCel:		LastCel
					setCycle:	BegLoop self
				)
			)

			(sheMoves
				(aKalalau
					cycleSpeed:	0
					setLoop:		lLeaveWater
					cel:			0
					setMotion:	MoveTo 190 145
					setCycle:	EndLoop self
				)
			)

			(beached
				(aKalalau
					view:			vKalalau
					setLoop:		RELEASE
					setCycle:	Walk
					setMotion:	MoveTo 190 164 self
				)
			)

			(it'sLove
				(aKalalau
					loop:			facingWest
				)
				(ego
					setMotion:	MoveTo 115 164 self
				)
				(Print "It's love at first sight." draw:)
				(Print "(And also, second sight!)" LOW)
			)

			(lookAtHer
				(ego
					setLoop:		facingEast
				)
				(= cycles 10)
			)

			(showBigK
				(ego
					posn:			1115 164
				)
				(aKalalau
					posn:			1190 164
				)
				(DrawPic 99 DISSOLVE)
				(= seconds 6)
			)

			(wow
				(Print "You think, \"This is the girl I've dreamed of;
					the woman I've longed for;
					the moment I've waited for!
					Is this the love I've been looking for?\"" BIG)
				(= seconds 5)
			)

			(backTo75
				(DrawPic 75 DISSOLVE)
				(ego
					posn:			115 164
				)
				(aKalalau
					posn:			190 164
				)
				(= seconds 3)
			)

			(cliche1
				(ego
					view:			vLoveCliches
					loop:			lEgoHeart
					cel:			0
					setCycle:	EndLoop
				)
				(aKalalau
					view:			vLoveCliches
					loop:			lKalalauHeart
					cel:			0
					setCycle:	EndLoop self
				)
			)

			(cliche1pause
				(ego
					loop:			lEgoHeartbeat
					setCycle:	Forward
				)
				(aKalalau
					loop:			lKalalauHeartbeat
					setCycle:	Forward
				)
				(= seconds 3)
			)

			(cliche1done
				(ego
					loop:			lEgoHeart
					setCel:		LastCel
					setCycle:	BegLoop
				)
				(aKalalau
					loop:			lKalalauHeart
					setCel:		LastCel
					setCycle:	BegLoop self
				)
			)

			(cliche2
				(aCupidWest
					show:
					setCycle:	Forward
				)
				(aCupidEast
					show:
					setCycle:	Forward
				)
				(ego
					view:			vEgo
					loop:			facingEast
					setLoop:		RELEASE
					setCycle:	Walk
				)
				(aKalalau
					view:			vKalalau
					loop:			facingWest
					setLoop:		RELEASE
					setCycle:	Walk
				)
				(= seconds 4)
			)

			(cliche2done
				(aCupidEast		hide:)
				(aCupidWest		hide:)
				(= cycles 20)
			)

			(cliche3
				(aCupidWest
					loop:			lHearts
					cel:			0
					setCycle:	EndLoop
					posn:			84 127
					show:
				)
				(aCupidEast
					loop:			lHearts
					cel:			0
					setCycle:	EndLoop self
					posn:			217 127
					show:
				)
			)

			(cliche3beat
				(aCupidWest
					loop:			lHeartThumping
					cel:			0
					setCycle:	Forward
				)
				(aCupidEast
					loop:			lHeartThumping
					cel:			1
					setCycle:	Forward
				)
				(= seconds 3)
			)

			(cliche3shrink
				(aCupidWest
					loop:			lHearts
					setCel:		LastCel
					setCycle:	BegLoop
				)
				(aCupidEast
					loop:			lHearts
					setCel:		LastCel
					setCycle:	BegLoop self
				)
			)

			(cliche3done
				(aCupidEast		hide:)
				(aCupidWest		hide:)
				(= cycles 20)
			)

			(cliche4
				(aCupidWest
					loop:			lFireworksWest
					cel:			0
					setCycle:	Forward
					posn:			116 50
					show:
				)
				(= cycles 5)					;**	start number two 5 cycles later
			)

			(startSecondFirework
				(aCupidEast
					loop:			lFireworksEast
					cel:			0
					setCycle:	Forward
					posn:			236 53
					show:
				)
				(= cycles (- (* 16 3) 7))	;**	3 is number of explosions; why 7?
			)

			(stopFirstFirework
				(aCupidEast		hide:)
				(= cycles 5)
			)

			(cliche4done
				(aCupidWest		hide:)
				(= cycles 20)
			)

			(moveItLarry
				(Print "Well, Larry? Are you just going to stand there?
					You've finally met the girl of your dreams,
					now's the time to take some action!")
				(ego
					setMotion:	MoveTo 142 164
				)
				(aKalalau
					setMotion:	MoveTo 158 164 self
				)
			)

			(theKiss
				(aKalalau		hide:)
				(ego
					view:			vKalalauKissing
					setLoop:		lTheKiss
					cel:			0
					posn:			151 164
					setCycle:	EndLoop self
				)
			)

			(holdIt
				(= seconds 5)
			)

			(breakLips
				(ego
					setCycle:	BegLoop self
				)
			)

			(kissDone
				(Print "You're limp. You've never been kissed like that before!
					Could it be?
					Is \"Leisure Suit Larry\" in love?" draw:)
				(Print "(Again?)" LOW)
				(aKalalau
					show:
					ignoreActors:FALSE
				)
				(ego
					view:			currentEgoView
					setLoop:		RELEASE
					loop:			facingEast
					posn:			142 164
					setCycle:	Walk
				)
				(= seconds 3)
			)

			(plotExposition
				(Print "You recover enough to speak,
					\"Ohmigosh, you're beautiful! You're wonderful!
						Do you understand English?
						What's your name?
						Where are you from?
						Are you busy tonight?\"")
				(Print "\"Of course I understand English, you silly;
					all of us here on Nontoonyt Island do.
					We live in a small village just off the beach.
					My name is Kalalau; what's yours?\"")
				(Print laffer)
				(Print "\"Of course; it only stands to reason that
					such a lovely man would also have such a lovely name,\"
					Kalalau's smile beams, melting your heart.
					\"But, as to your question concerning my availability tonight,
					I am afraid I have disappointing information for you:
					as enjoyable as I feel sure an evening with someone like
					you must be,
					customs here on the island forbid pre-marital dating.
					On Nontoonyt Island, all women save themselves for marriage.\"" WIDE)
				(Print "\"Well, that's ok,\" you volunteer,
					\"why don't we just get married instead!\"")
				(Print "She smiles in agreement,
					\"Wouldn't that be wonderful!
					But, unfortunately our tribal elders have forbidden any new
					marriages until our island has been freed from its
					present scourge.
					Please permit me to elucidate:
					recently an abhorrent man took our island from us.
					He claimed our sacred ancestral burial grounds at the top
					of our volcano,
					and refuses to allow us to visit.\"" WIDE)
				(Print "She continues,
					\"Soon thereafter, our perpetually blue skies turned to fog.
					While none of us had ever seen snow or ice,
					suddenly a glacier appeared on the slopes of the volcano,
					preventing our passage to our sacred lands.
					A treacherous river then appeared,
					gouging a formidable canyon just behind our village.
					Then (most odious of all)
					he hypnotized our most beautiful women,
					enslaving them in his mountaintop fortress,
					forcing them to do his every whim,
					no matter how sick or repulsive.\"" WIDE)
				(Print "\"What a drag!\" you sympathize.")
				(Print "\"Yes,\" she agrees,
					\"but now, the worst of all:
					because of all this,
					he prevented my people from completing a megabuck deal with a
					large, multi-national hotel conglomerate who were prepared to
					turn this place into next year's trendiest tourist trap!
					We had it bagged: they were going to build a huge,
					casino/resort/hotel complex right here on this lagoon,
					provide employment for our lazy men in the growing field of
					slot-machine repair, and even fund a day-care center so,
					(even though we women would continue to work our normal,
					16-hour days in the taro patches)
					at least we wouldn't have to carry our children on our
					backs!\"" WIDE)
				(if ( > filthLevel CLEAN)
					(Print "\"But did it happen? Hell, NO!
						And all because of that evil Dr. Nonookee!\"
						she cries, suddenly turning quite sensitive.
						\"Oh, if only some great hero could rid this island of
						his evilness, we could all live here happily ever after!\"")
				else
					(Print "\"But did it happen? Oh my gracious, NO!
						And all because of that evil Dr. Nonookee!\"
						she cries, suddenly turning quite sensitive.
						\"Oh, if only some great hero could rid this island of
						his evilness, we could all live here happily ever after!\"")
				)
				(Print "(You're no fool, Larry. That sounds like a cue!)" LOW)
				(= seconds 3)
			)

			(I'mYourMan
				(theSound		dispose:)
				(Print "\"Say, I've got an idea,\" you tell her.
					\"What if I stop this guy? Then could we get married?\"")
				(theGame changeScore: 10)
				(Print "\"But, of course.
					Why didn't I think of that?
					Why, this is every little girl's fairy tale:
					a hero comes in a white suit to save my
					island and its people!\"")
				(Print "Sigh." LOW)
				(= seconds 3)
			)

			(takeOff
				(Print "\"Come on, Larry. Let's go!\"")
				(aKalalau
					setMotion:		MoveTo (aKalalau x?) (- (aKalalau y?) 5) self
				)
			)

			(clearOfLarry
				(aKalalau
					setMotion:		MoveTo -20 143
				)
				(= cycles 20)
			)

			(larryAlso
				(ego
					setMotion:		MoveTo 0 143 self
				)
			)


			;**	This is for the end of the game, when the copter returns
			;**	Larry to the village...


			(startLarry
				(= seconds 3)
			)

			(moveLarry
				(Print "\"Hey, thanks for the lift, guys!
					Want to stick around for my wedding?\"")
				(Print "\"Sure, why not? We'll grab the camcorder and
					be right there. Watch out for that blade!\"")
				(ego
					illegalBits:0
					setMotion:	MoveTo 181 167 self
				)
			)

			(startGirl
				(aKalalau
					setMotion:	MoveTo 76 156
				)
				(ego
					setMotion:	MoveTo 92 158 self
				)
			)

			(theKiss2
				(Print "\"Hi, Larry; what took you so long?\"")
				(Print "\"I ran into a little trouble with
					an artificial glacier!\"")
				(aKalalau		hide:)
				(ego
					view:			vKalalauKissing
					setLoop:		lTheKissWest
					cel:			0
					posn:			83 157
					setCycle:	EndLoop self
				)
			)

			(holdIt2
				(= seconds 5)
			)

			(breakLips2
				(ego
					setCycle:	BegLoop self
				)
			)

			(kissDone2
				(aKalalau
					show:
					ignoreActors:FALSE
				)
				(ego
					view:			currentEgoView
					setLoop:		RELEASE
					loop:			facingWest
					posn:			92 158
					setCycle:	Walk
				)
				(= seconds 3)
			)

			(iDidIt
				(Print "\"I did it, Kalalau! I really did it!!
					Dr. Nonookee is no more, his evil spell is broken,
					the glacier is melting away, and your women are again free!
					Once again, peace reigns over this beautiful island!
					Let's find your father, and get married immediately!\"")
				(ego
					setMotion:		MoveTo 0 143 self
				)
				(aKalalau
					setMotion:		MoveTo -20 143
				)
			)
		)
	)
)

