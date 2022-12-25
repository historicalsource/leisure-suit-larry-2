;**
;**	Logics for room 72 -- Monte, the jungle python
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 19, 1988
;**


(script#				72)

(public
	rm72				0
)


;(define	vEgoInSnake					172)
	(define	lWrap					0)
	(define	lFlick				1)
	(define	lSwallow				2)
	(define	lInside				3)
	(define	lBurp					4)

;(define	vEgoStoppingSnake			174)
	(define	lStickingStick		0)
	(define	lSnakeLeaving		1)
	(define	lSnakeDisappear	2)

;(define	vSnake						701)
	(define	lInTree				0)
	(define	lToGround			1)


(local
	usedStick
	aSnake
	aBurp
)


(enum 1											;**	snakeState values
	ATTACKING
	STICKED
	PAST_SNAKE
)

(enum												;**	rmScript state values
	intro

	cueTheSnake
	snake2
	wrapLarry
	i2i
	swallowLarry
	waitAsec
	snakeDone
	snakeBurps
	waitBurp
	deBurp
	UBdying

	stickInit
	stickIt
	stickIt2
	embarrassedSnake
	snakeGone
)


(instance rm72 of Room
	(properties
		picture:		72
		horizon:		56
		north:		71
		east:			73
	)

	(method (init)

		(super init:)

		(if (== snakeState FALSE)
			(Load VIEW vSnake)
			(Load VIEW vEgoStoppingSnake)
			(Load VIEW vEgoInSnake)
			(Load VIEW  iStick)

			((= aSnake (Actor new:))
				view:				vSnake
				illegalBits:	0
				ignoreActors:
				setLoop:			lInTree
				setCel:			0
				setPri:			14
				cycleSpeed:		1
				posn:				168 98
				stopUpd:
				init:
			)
			((= aBurp (Prop new:))
				view:				vEgoInSnake
				ignoreActors:
				setLoop:			lBurp
				setCel:			0
				setPri:			15
				posn:				220 152
				init:
				hide:
			)
		)

		(cond
			((== prevRoomNum 73)		(ego posn: 316 160))
			(else							(ego posn:	23	58))
		)
		(NormalEgo)
		(ego					init:)

		(self
			setRegions:		rgIsland
			setScript: 		rm72Script
		)
	)
)


(instance rm72Script of Script
	(method (doit)
		(super doit:)

		(if (and	(& (ego onControl:) cBlue)
					(== snakeState FALSE)
					(== currentStatus NORMAL))
			(self changeState: cueTheSnake)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (or	(Said 'get/stick<out')
					(Said 'get/stick<ready')
					(Said '(get<out), use, stick, hold, insert, put/stick'))
			(cond
				((not (ego has: iStick))	;**	If snake is already gone,
					(DontHave)					;**		you have no stick.
				)
				((!= currentStatus NEAR_DEATH)
					(NotClose)
				)
				(else
					(Ok)
					(Print "You carefully prepare yourself for the oncoming
						jaws with the stick.
						Will it work?" HIGH)
					(= usedStick TRUE)
				)
			)
		)

		(if (Said 'use, swing, get/vine')
			(Print "A good idea, but this is not the place.")
		)

		(if (not snakeState)
			(if (Said 'hit/snake/stick')
				(if (ego has: iStick)
					(Print "You're close, but the stick is not heavy enough to
						harm the anaconda.")
				else
					(DontHave)
				)
			)

			(if (Said 'hit/snake')
				(Print "You are mad enough to do that,
					but that's not the solution to your predicament.")
			)

			(if (Said 'crawl')
				(Print "Good idea, but that would just give the snake even more
					advantage. Just stick to using your brain.")
			)
		)

		(if (Said 'look>')
			(if (Said '/bush, tree, leaf')
				(if (== snakeState PAST_SNAKE)
					(Print "There are no more snakes around.")
				else
					(Print "There may be some movement in that center bush.")
				)
			)

			(if (Said '/ground, dirt, floor')
				(Print "This part of the jungle floor seems even more soft
					and mushy.")
			)

			(if (Said '/face, statue, mouth')
				(Print "It looks like a prehistoric punk rocker!")
			)

			(if (Said '/snake')
				(if (== snakeState PAST_SNAKE)
					(Print "There are no more snakes in this area.")
				else
					(Print "A giant anaconda lies waiting for you,
						coiled around a branch directly above the only exit
						from this part of the jungle.")
				)
			)

			(if (Said '[/room, jungle]')
				(Print "The jungle is dense and dark.
					High above you, monkeys dance through the treetops.")
				(if (== snakeState ATTACKING)
					(Print "A snake is in the process of devouring you!")
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(intro)

			(cueTheSnake
				(= currentStatus NEAR_DEATH)
				(= snakeState ATTACKING)
				(Print "You just attracted the attention of that giant anaconda
					by walking beneath his waiting coils.
					Perhaps if you hold very still he won't notice you!" HIGH)
				(User
					canControl:		FALSE
					canInput:		TRUE
				)
				(ego
					setMotion:		FALSE
					posn:				142 153
					setLoop:			facingSouth
				)
				(aSnake
					setCycle:		EndLoop self
				)
			)

			(snake2
				(if (!= usedStick TRUE)
					(Print "Ok, so he DID notice you!
						Perhaps he just finished a big meal and couldn't eat
						another bite!" HIGH draw:)
				)
				(aSnake
					posn:				130 145
					setLoop:			lToGround
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(wrapLarry
				(ego					hide:)
				(User
					canInput:		FALSE		;**	If no stick by now, dead meat.
				)
				(if usedStick
					(self changeState: stickInit)
				else
					(aSnake
						view:				vEgoInSnake
						setLoop:			lWrap
						posn:				138 153
						cycleSpeed:		2
						cel:				0
						setCycle:		EndLoop self
					)
				)
			)

			(i2i
				(aSnake
					setLoop:			lFlick
					posn:				140 153
					cel:				0
					cycleSpeed:		0
					setCycle:		Forward
				)
				(= seconds 5)
			)

			(swallowLarry
				(aSnake
					setLoop:			lSwallow
					cycleSpeed:		3
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(waitAsec
				(aSnake
					cycleSpeed:		2
					setLoop:			lInside
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(snakeDone
				(Print "Then again, perhaps he relishes the taste of polyester!")
				(= seconds 3)
			)

			(snakeBurps
				(aBurp
					show:
					setCycle:		EndLoop self
				)
			)

			(waitBurp
				(= cycles 10)
			)

			(deBurp
				(aBurp
					setCycle:		BegLoop self
				)
			)

			(UBdying
				(aBurp				dispose:)
				(Print "You feel you could have eventually escaped his coils
					if there was only some way to keep him from swallowing you."
					draw:)
				(= currentStatus DEAD)
			)


			;**	He cared enough to stick the very best...

			(stickInit
				(= snakeState STICKED)
				(aSnake
					view:				vEgoStoppingSnake
					setLoop:			lStickingStick
					cycleSpeed:		2
					posn:				139 146
					cel:				0
					setCycle:		EndLoop self
				)
				(Print "As the snake dislocates its lower jaw in preparation
					for another hearty, cholesterol-laden dinner of red meat
					and polyester, you carefully insert your stick into the
					distended opening!" HIGH draw:
					icon: iStick 0 0
				)
					;**	cholesterol spell-checked on August 17, 1988
			)

			(stickIt
				(delay 3)
			)

			(stickIt2
				(aSnake
					view:				vEgoStoppingSnake
					setLoop:			lSnakeLeaving
					cycleSpeed:		3
					cel:				0
					setCycle:		EndLoop self
				)
				(theGame changeScore: 10)
				(Print "The snake, unable to remove the stick from its jaws
					and embarrassed by being humiliated before the other
					jungle creatures, slithers away through the undergrowth to
					its lair (and an early retirement)." HIGH draw:)
			)

			(embarrassedSnake
				(ego
					posn:				146 160
					setLoop:			facingEast
					show:
					put:				iStick LIMBO
				)
				(aSnake
					setPri:			RELEASE
					setLoop:			lSnakeDisappear
					posn:				142 115
					cel:				0
					setCycle:		EndLoop self
				)
			)

			(snakeGone
				(aSnake				dispose:)
				(= snakeState PAST_SNAKE)
				(NormalEgo)
			)
		)
	)
)

