;**
;**	Logics for room 41 -- Nude Beach
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 23, 1988
;**


(script#				41)

(public
	rm41				0
)


(define	nearBikini	76 179 106 189)
(define	nearGirl		75 150 160 189)

;(define	vBikiniBottom				400)
;(define	vNudeGirl					401)
;(define	vNudeHenchette				402)
;(define	vNudeMan						403)


(local
	work
	bikiniHere
	henchInvited
	goto95
	aBikini
	aHench
)


(enum												;**	henchScript state values
	waitAsec
	startAction
	endAction
	talkToLarry
	henchAtEgo
	henchLeaving
	henchTurning2
	henchOffScreen
	henchAllGone
)


(instance rm41 of Room
	(properties
		picture:		41
		horizon:		5
	)

	(method (init)
		(Load VIEW vNudeGirl)
		(Load VIEW vNudeMan)

		(super init:)

		((View new:)
			view: 			vNudeGirl
			loop: 			0
			cel: 				0
			posn: 			34 111
			setPri: 			7
			addToPic:
		)
		((View new:)
			view: 			vNudeGirl
			loop: 			1
			cel: 				0
			posn: 			77 148
			setPri: 			11
			addToPic:
		)
		((View new:)
			view: 			vNudeGirl
			loop: 			2
			cel: 				0
			posn: 			119 104
			setPri: 			6
			addToPic:
		)
		((View new:)
			view: 			vNudeGirl
			loop: 			2
			cel: 				1
			posn: 			88 82
			setPri: 			4
			addToPic:
		)
		((View new:)
			view: 			vNudeGirl
			loop: 			3
			cel: 				1
			posn: 			10 80
			setPri: 			4
			addToPic:
		)
		((View new:)
			view: 			vNudeGirl
			loop: 			3
			cel: 				0
			posn: 			41 109
			setPri: 			7
			addToPic:
		)
		((View new:)
			view: 			vNudeMan
			loop: 			0
			cel: 				0
			posn: 			86 136
			setPri: 			9
			addToPic:
		)
		((View new:)
			view: 			vNudeMan
			loop: 			0
			cel: 				1
			posn: 			58 70
			setPri: 			3
			addToPic:
		)
		((View new:)
			view: 			vNudeMan
			loop: 			1
			cel: 				0
			posn: 			22 109
			setPri: 			7
			addToPic:
		)
		((View new:)
			view: 			vNudeMan
			loop: 			2
			cel: 				0
			posn: 			94 99
			setPri: 			6
			addToPic:
		)

		(if (< (ego y?) 165)
			(ego y: 165))
		(NormalEgo)
		(ego					init:)

		(self
			setRegions:		rgResortBeach
			setScript: 		rm41Script
		)

		(if (and	((inventory at: iBikiniBottom) ownedBy: curRoomNum)
					(== currentEgoView vEgoBlond))
			(= bikiniHere TRUE)
			(Load VIEW vBikiniBottom)
			((= aBikini (View new:))
				view:			vBikiniBottom
				ignoreActors:
				posn:			88 159
				stopUpd:
				init:
			)
		else
			(if (== currentEgoView vEgo)
				(self setRegions: rgHenchette)
				(= henchOnScreen TRUE)
				(= currentHenchView vNudeHenchette)
				(Load VIEW currentHenchView)
				((= aHench (Actor new:))
					view:				currentHenchView
					illegalBits:	0
					setLoop:			4
					setPri:			12
					posn:				106 156
					cycleSpeed:		2
					init:
					setScript:		henchScript
				)
				(Notify rgHenchette 1)		;**	play music 1x
			)
		)
	)
)


(instance rm41Script of Script
	(method (doit)
		(super doit:)

		(if (== EAST (ego edgeHit?))
			(if (== goto95 FALSE)
				(curRoom newRoom: 42)
			else
				(Print "You quickly seduce the beautiful, naked woman,
					then head for the resort parking lot
					where she leaves her personal helicopter.
					A short, romantic, low-altitude chopper ride across
					the South Pacific is followed by..." WIDE)
				(= currentStatus HEADING_FOR_DEATH_SCENE)
				(curRoom newRoom: 95)
			)
		)

		(if (and henchOnScreen
					henchInvited
					( > (ego x?) 300))
			(= henchInvited FALSE)
			(= goto95 TRUE)
			(curRoom east: 95)				;**	Too bad, Larry.
			(Print "\"Hey, baby,\" you cry, \"wait for me!!\"")
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'climb[<over]/rock')
			(Print "That won't help you.")
		)

		(if (Said 'look>')
			(if (and	henchOnScreen
						(Said '/woman'))
				(Print "Suddenly, you start to perspire!")
			)

			(if (Said '/people, man, blanket, woman')
				(Print "(blush)" LOW font: smallFont)
			)

			(if (Said '/tree')
				(Print "The large, straight trunk rises high into the sky
					above the beautiful sand beach.")
				(if ( > filthLevel 13)
					(Print "(Or is that the guy with the good tan?)" LOW)
				)
			)

			(if (Said '/rock')
				(Print "They effectively prevent you from joining
					the \"beautiful people.\"")
				(if bikiniHere
					(Print "There's a discarded bikini bottom lying that rock
						at the eastern edge of the sand.")
				)
			)

			(if (Said '[/room, rock, beach]')
				(Print "Say! These people have no clothes on!
					What kind of place is this, anyway?")
				(if bikiniHere
					(Print "There's a discarded bikini bottom lying that rock
						at the eastern edge of the sand.")
				)
			)
		)

		(if henchOnScreen
			(if (Said 'get/towel')
				(Print "She needs it.")
			)

			(if (Said 'talk/woman')
				(if (not (ego inRect: nearGirl))
					(NotClose)
				else
					(Print (Format @string
						"\"Hi ya, baby,\" you tell her.
						%s" laffer))
					(if (not (henchScript state?))	;**	only 1 per visit
						(henchScript changeState: henchAtEgo)
					)
				)
			)
		)

		(if (or	(Said 'wear, (change<into), (put<on)/swimsuit, (suit<swim), bikini')
					(Said 'change, (take<off), remove/suit, clothes')
					(Said 'wear, (put<on)/swimsuit, top, bottom, (suit<swim), bikini')
					(Said 'get<naked')
					(Said 'undress')
					(Said 'change, (take<off), remove/suit, clothes'))
			(Print "There's already enough skin visible here!")
			(Print "Try that elsewhere.")
		)

		(if (Said 'get/bottom, bikini, (bottom<bikini)')
			(cond
				((not bikiniHere)
					(Print "Where?")
				)
				((not (ego inRect: nearBikini))
					(NotClose)
				)
				(else
					(Print "It looks a little small for you,
						but what the heck...")
					(= bikiniHere FALSE)
					(ego get: iBikiniBottom)
					(aBikini dispose:)
					(theGame changeScore: 4)
				)
			)
		)
	)
)


(instance henchScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(waitAsec
				(= cycles (Random 30 50))
			)

			(startAction
				(aHench setCycle: Forward)
				(= cycles (Random 5 22))
			)

			(endAction
				(aHench setCel:)
				(= seconds 5)
			)

			(talkToLarry
				(switch (Random 1 3)
					(1	(Print "\"Oooh, I find clothed men so appealing!\""))
					(2 (Print "\"Isn't polyester comfortable?\""))
					(3	(Print "\"Are you new around here, good lookin'?\""))
				)
				(Print "(Was that naked woman talking to you?)" LOW)
				(self changeState: waitAsec)
			)

			(henchAtEgo
				(= cycles 0)
				(aHench setCel: 0)
				(Print "\"Well, hello yourself, big boy,\"
					says the totally naked woman lying on the rock.
					\"Aren't you the overdressed one?
					Say, since you're all dressed and I was just thinking
					of leaving, why don't we go to my place together.
					I know a wonderful spot near here where we can be alone.
					Come on, Larry, just follow me!\"" WIDE draw:)
				(= seconds 5)
			)

			(henchLeaving
				(aHench
					setAvoider: (Avoider new:)
					setStep:		3 2
					setLoop:		RELEASE
					setPri:		RELEASE
					cycleSpeed:	0
					loop:			facingEast
					posn:			105 183
					setCycle:	Walk
					setAvoider:	Avoider
					setMotion:	MoveTo 333 183 self
				)
				(= cycles 20)
			)

			(henchTurning2
				(if ( > (aHench x?) (ego x?))
					(Print "\"Well, Tiger? What are you waiting for?
						Follow me!\"")
				else
					(Print "\"C'mon Larry; let's go to my vehicle!\"")
				)
				(= henchInvited TRUE)
			)

			(henchOffScreen
				(= seconds 10)
			)

			(henchAllGone
				(aHench		dispose:)
				(= currentHenchView FALSE)
				(= henchOnScreen FALSE)
				(= henchInvited FALSE)
			)
		)
	)
)


