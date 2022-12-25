;**
;**	Logics for room 32 -- Larry's cabin
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 23, 1988
;**


(script#				32)

(public
	rm32				0
)


(define	nearBed 		150 96 168 110)
(define	nappypoo		1)						;**	causeOfDeath values
(define	nightfall	2)

;(define	vEgoOnBunk					131)
	(define	lWhiteSuit		0)
	(define	lWhiteSuitFlat	1)
	(define	lSwimsuit		2)
	(define	lSwimsuitFlat	3)

;(define	vLarryCabin					304)
	(define	lDoor				0)
	(define	lPorthole		1)
	(define	lFruit			2)

;(define	vMotherOnEgo				309)
	(define	lWalking				0)
	(define	lRunning				1)
	(define	lJumping				2)
	(define	lSplat				3)


(local
	oldEgoX
	oldEgoY
	causeOfDeath
	aPorthole
	aDoor
	aMama
	aFruit
)


(enum												;**	rm32Script state values
	intro

	nightieNight
	toCenter
	toBed
	atBed

	bedInit
	soundAsleep
	doorOpeningFinished
	mamaInit
	mamaRunning1
	MR2
	MR3
	MR4
	mamaJumping
	MJ2
	MJ3
	MJ4
	MJ5
	MJ6
	MJ7
	mamaOnEgo
	mamaOffEgo
	UBdying
)


(instance theSound of Sound
)


(instance rm32 of Room
	(properties
		picture:		32
	)

	(method (init)
		(Load VIEW vLarryCabin)
		(Load VIEW vEgoOnBunk)

		(if (ego has: iSwimsuit)
			(Load VIEW vEgoSwimsuit)
		)

		(super init:)

		(NormalEgo)

		(self
			setRegions:		rgShip
			setScript: 		rm32Script
		)

		(if ((inventory at: iFruit) ownedBy: curRoomNum)
			((= aFruit (View new:))
				view:				vLarryCabin
				setLoop:			lFruit
				setPri:			4
				posn:				156 84
				init:
			)
		)

		((= aPorthole (Prop new:))
			view:				vLarryCabin
			setLoop:			lPorthole
			setCel:			0
			setPri:			4
			posn:				148 76
			setCycle:		Forward
			cycleSpeed:		5
			isExtra:			TRUE
			init:
		)

		(if timesInRm33
			(Load VIEW vMotherOnEgo)
			(Load SOUND sBOOM)

			(theSound
				number:			sBOOM
				init:
			)
			((= aMama (Actor new:))
				view:				vMotherOnEgo
				ignoreActors:
				illegalBits:	0
				setLoop:			lWalking
				cel:				3			;**	so she hits 202 111 on cel 0
				setPri:			7
				setStep:			3 2
				setCycle:		Forward
				posn:				217 111
				init:
			)

			(if (and (not rgTimer)
						(== currentTimer	CRUISE_BEDTIME))
				(= currentStatus NEAR_DEATH)
				(rm32Script changeState: nightieNight)
			)
		)

		((= aDoor (Door new:))
			view:				vLarryCabin
			setLoop:			lDoor
			posn:				207 115
			setPri:			7
			entranceTo:		33
			doorCtrl:		cGreen
			roomCtrl:		cCyan
			msgLook:			"This door should be locked, as it leads to someone
									else's cabin. (But, it's not!)"
			msgFunny:		"You may have heard a soft, \"Come in.\"
									(But you're not sure.)"
			msgCloser:		"Just walk near it."
			init:
		)

		(cond
			((== prevRoomNum 33)				(ego posn: 200 114))
			(else									(ego posn: 156 122))
		)
		(ego					init:)
	)
)


(instance rm32Script of Script
	(method (doit)
		(super doit:)

		(if (== state mamaOnEgo)
			(ShakeScreen 1 (Random 1 3))
		)

		(if (& (ego onControl:) cBlue)
			(curRoom newRoom: 31)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'open, look/closet')
			(if (not (ego inRect: nearBed))
				(NotClose)
			else
				(Print "You quickly open the cramped little closet
					and look inside.
					You find absolutely nothing,
					so you return the doors to their closed position.")
			)
		)

		(if (Said 'look<on, over, above/closet[<top]')
			(Print "You feel nothing but dust.")
			(Print "(But remember this if you ever play \"King's Quest III.\")" 
				LOW)
		)

		(if (Said 'look<under/bed')
			(Print "You are so clever to think to look under the bed!")
			(Print "You find nothing.")
		)

		(if (Said 'look>')
			(if (Said '/bed')
				(Print "It's small and hard.")
				(if ( > filthLevel DIRTY)
					(Print "(A condition not unknown to you!)" LOW)
				)
			)

			(if (Said '/nightstand, dresser, table')
				(cond
					((not (ego inRect: nearBed))
						(NotClose)
					)
					((not ((inventory at: iFruit) ownedBy: curRoomNum))
						(Print "You wonder what brand of furniture polish the
							ship's employees use to bring out
							the natural grain of that fine plywood.")
					)
					(else
						(Print "There is a bowl of fruit sitting on the
							nightstand, with a note attached.")
						(Print "You read the note...")
						(Print "\"Bon voyage, Larry!\n\n
							Love,\n
							Mrs. Bimbo\"")
						(if timesInRm33
							(Print "Egad! She's sending you gifts!")
						else
							(Print "Who's \"Mrs. Bimbo?!\"")
						)
					)
				)
			)

			(if (and	((inventory at: iFruit) ownedBy: curRoomNum)
						(Said '/fruit, basket'))
				(if (not (ego inRect: nearBed))
					(NotClose)
				else
					(Print "There is a bowl of fruit sitting on the
						nightstand, with a note attached.")
					(Print "You read the note...")
					(Print "\"Bon voyage, Larry!\n\n
						Love,\n
						Mrs. Bimbo\"")
					(if timesInRm33
						(Print "Egad! She's sending you gifts!")
					else
						(Print "Who's \"Mrs. Bimbo?!\"")
					)
				)
			)

			(if (Said '[/cabin, room]')
				(Print "No one on \"The Dating Connection\"
					promised you a deluxe cabin.
					In fact, this is surely \"un-luxe!\"
					Located at the rear of the ship's lowest deck,
					the noise from the engine compartment is deafening.")
				(Print "You have a bed, a nightstand, and a small closet.
					There is a door in the aft wall.")
			)
		)

		(if (Said '(get, climb<in), enter, go/closet')
			(Print "It's too small to hold you.")
			(Print "(Bet you were expecting another cheap \"out of the closet\"
				joke!)" LOW)
		)

		(if (Said 'get/dust')
			(Print "Nah. You're allergic.")
		)

		(if (or	(Said 'get/dressed')
					(Said 'get<dressed')
					(Said 'wear, change, (take<off), remove, 
											(put<on)/swimsuit, clothes, suit, bikini'))
			(cond
				((== currentEgoView vEgoSwimsuit)
					(= currentEgoView vEgo)
					(Ok)
					(ego view: vEgo)
				)
				((not (ego has: iSwimsuit))
					(if (ego has: iBikiniTop)
						(Print "Nah. Who'd wear half a swimsuit?")
					else
						(DontHave)
					)
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				((not timesInRm33)
					(Print "Don't you want to see what's behind that door to  
						the right?")
				)
				(else
					(= currentEgoView vEgoSwimsuit)
					(Print "Ok. This IS a good place to change clothes.")
					(ego view: vEgoSwimsuit)
				)
			)
		)

		(if (Said 'open/drawer, stand, nightstand')
			(Print "You open the nightstand's sole drawer and
				inspect it thoroughly.")
			(Print "Nothing.")
			(Print "Zip.")
			(Print "Nada.")
			(Print "You close the drawer.")
		)

		(if (and (not (ego has: iFruit))
					((inventory at: iFruit) ownedBy: curRoomNum)
					(Said 'eat, use/fruit'))
			(Print "Ok. You feel refreshed and fruitless.")
			((inventory at: iFruit) moveTo: LIMBO)
			(theGame changeScore: -2)
		)

		(if (Said 'get/fruit, basket')
			(cond
				((not ((inventory at: iFruit) ownedBy: curRoomNum))
					(AlreadyTook)
				)
				((not (ego inRect: nearBed))
					(NotClose)
				)
				(else
					(Ok)
					(aFruit dispose:)
					(ego get: iFruit)
					(theGame changeScore: 3)
				)
			)
		)

		(if (Said 'lie, go, sit[/bed, sleep, chair]')
			(cond
				((== (ego view?) vEgoOnBunk)
					(YouAre)
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				((not (ego inRect: nearBed))
					(NotClose)
				)
				(else
					(= currentStatus SLEEPING)
					(Print "Ok.
						Thinking a little shut-eye might do you some good,
						you collapse on the bed, and immediately begin snoring.")
					(User
						canControl: 	FALSE
						canInput: 		TRUE
					)
					(= oldEgoX (ego x?))
					(= oldEgoY (ego y?))
					(= currentEgoView (ego view?))
					(= causeOfDeath nappypoo)
					(ego
						view:				vEgoOnBunk
						setLoop:			(if (== currentEgoView vEgo)
												lWhiteSuit
											else
												lSwimsuit
											)
						cel:				0
						setCycle:		Forward
						cycleSpeed:		2
						setPri:			9
						illegalBits:	0
						posn:				124 99
						setMotion:		FALSE
					)
					(if timesInRm33
						(self changeState: bedInit)
					)
				)
			)
		)

		(if (or	(Said 'rise, get, wake, stand[/up]')
					(Said '(get<out), leave/bed')
					(Said 'stand, get<up'))
			(cond
				((!= (ego view?) vEgoOnBunk)
					(Print "You're not down.")
				)
				((!= currentStatus SLEEPING)
					(NotNow)
				)
				(else
					(Ok)
					(ego posn: oldEgoX oldEgoY)
					(NormalEgo facingEast)
					(if (== (aDoor doorState?) doorClosed)
						(ego observeControl: cYellow)
					)
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			;**	(intro)


			;**	This path is taken when he screws around too long and doesn't
			;**	solve the timer puzzle.	Night falls, he gets tired and heads
			;**	for bed, triggering a Mama attack.

			(nightieNight
				(= currentStatus NEAR_DEATH)
				(= causeOfDeath nightfall)
				(HandsOff)
				(= cycles 5)
			)

			(toCenter
				(Print "Night has fallen on the good ship U.S.S. Love Tub,
					so you head for your bed and a few hours of relaxing sleep.")
				(ego
					setMotion:		MoveTo 159 112 self
				)
			)

			(toBed
				(ego
					setMotion:		MoveTo 155 103 self
				)
			)

			(atBed
				(= currentStatus SLEEPING)
				(ego
					view:				vEgoOnBunk
					setLoop:			(if (== currentEgoView vEgo)
											lWhiteSuit
										else
											lSwimsuit
										)
					cel:				0
					setCycle:		Forward
					cycleSpeed:		2
					setPri:			9
					illegalBits:	0
					posn:				124 99
					setMotion:		FALSE
				)
				(= seconds 5)
			)


			;**	The following block of code handles Mama jumping on Larry.

			(bedInit
				(= currentStatus NEAR_DEATH)
				(HandsOff)
				(= seconds 5)
			)

			(soundAsleep
				(if (!= (aDoor doorState?) doorClosed)
					(self changeState: doorOpeningFinished)
				else
					(Print "Unbeknownst to our \"little boy blue,\"
						the door to the adjoining cabin swings open.")
					(aDoor
						locked:			FALSE
						notify:			self
						force:			TRUE
						open:
					)
				)
			)

			(doorOpeningFinished
				(Print "Suddenly a deep rumbling shakes your cabin,
					not the steady roar of the ship's engines
					(to which you've now grown accustomed),
					this sounds more like an onrushing steam locomotive
					encountering a steep gradient while the fireman
					pours on the coal!")
				(= seconds 2)
			)

			(mamaInit
				(aMama
					show:
					setMotion:		MoveTo 181 111 self
				)
			)

			(mamaRunning1						;**	She's at 181 111 now.
				(aMama
					setLoop:			lRunning
					setCel:			0
					posn:				177 111
					setPri:			13
				)
				(= cycles 2)
			)
			(MR2
				(aMama
					posn:				171 109
					setCel:			1
				)
				(= cycles 2)
			)
			(MR3
				(aMama
					posn:				165 107
					setCel:			2
				)
				(= cycles 2)
			)
			(MR4
				(aMama
					posn:				162 105
					setCel:			3
				)
				(= cycles 2)
			)

			(mamaJumping
				(Print "IT'S MAMA!!!!" HIGH)
				(aMama
					setLoop:			lJumping
					posn:				162 105
					setCel:			0
				)
				(= cycles 2)
			)
			(MJ2
				(aMama
					posn:				162 105
					setCel:			1
				)
				(= cycles 2)
			)
			(MJ3
				(aMama
					posn:				162 105
					setCel:			2
				)
				(= cycles 2)
			)
			(MJ4
				(aMama
					posn:				160 103
					setCel:			3
				)
				(= cycles 2)
			)
			(MJ5
				(aMama
					posn:				158 99
					setCel:			4
				)
				(= cycles 2)
			)
			(MJ6
				(aMama
					posn:				154 93
					setCel:			5
				)
				(= cycles 2)
			)
			(MJ7
				(aMama
					posn:				158 90
					setCel:			5
				)
				(= cycles 2)
			)

			(mamaOnEgo
				(theSound 			play:)
				(aMama
					setLoop:			lSplat
					setMotion:		0
					setCycle:		Forward
					posn:				129 107
				)
				(ego
					setLoop:			(if (== currentEgoView vEgo)
											lWhiteSuitFlat
										else
											lSwimsuitFlat
										)
					posn:				125 94
				)
				(= seconds 4)
			)

			(mamaOffEgo
				(aMama
					setLoop:			lWalking
					setCel:			0			;**	No cycling.
					posn:				155 104
				)
				(Print "\"Oops,\" says the big Mother,
					\"I didn't mean ta hurt 'cha, lil' fella!\"" BIG draw:)
				(Print "\"Shooooot, an' he was such a cute 'n, too!\"
					she moans." BIG)
				(= seconds 1)
			)

			(UBdying
				(= currentStatus DEAD)
				(if (== causeOfDeath nightfall)
					(Print "Now it becomes clear to you.
						You must escape from this vessel before darkness falls
						or you'll continue to be the main course at a Mama party!")
				else
					(Print "Too bad, Larry, but it seems your bed just isn't the
						place to be, if you want to be alone!" BIG)
				)
			)
		)
	)
)

