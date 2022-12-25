;**
;**	Logics for room 70 -- hanging in the Parachute Tree
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 15, 1988
;**


(script#				70)

(public
	rm70				0
)


;(define	vEgoInTree					170)
	(define	lHanging				0)
	(define	lReaching			1)
	(define	lFalling				2)
	(define	lSparkle				3)
	(define	lCutting				4)

;(define	sFalling						1)


(local
	aBigEgo
	aBigEgoBottom
	aSparkle
)


(enum												;**	rmScript state values
	intro
	oopsAgain
	UBdying

	reachUp
	cuttingHarness
	startFall
	startTwinkle
	sparkleDone
)


(instance theSound of Sound
	(properties
	 	number:		sFalling
		loop:			1
	)
)


(instance rm70 of Room
	(properties
		picture:		70
		horizon:		1
	)

	(method (init)
		(Load VIEW vEgoInTree)
		(Load SOUND sFalling)
		(theSound 	init:)

		(super init:)

		((= aSparkle (Prop new:))
			view:				vEgoInTree
			setLoop:			lSparkle
			setPri:			15
			posn:				120 124
			init:
			hide:
		)

		((= aBigEgo (Actor new:))
			view:				vEgoInTree
			posn:				120 104
			setPri:			14
			illegalBits:	0
			init:
		)
		((= aBigEgoBottom (View new:))
			view:				vEgoInTree
			cel:				1
			posn:				120 134
			init:
		)

		(ego
			setMotion:		FALSE				;**	Don't init!
		)
		(User
			canControl:		FALSE
			canInput:		TRUE
		)
		(= currentStatus HANGING_IN_TREE)
		(self
			setRegions:		rgIsland
			setScript: 		rm70Script
		)
	)
)


(instance rm70Script of Script
	(method (doit)
		(super doit:)
	)

	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/parachute')
				(Print "It's caught in the tree, and you're caught in it!")
			)

			(if (Said '[/room, limb, tree, jungle, bush]')
				(Print "Now you're REALLY out on a limb!")
				(Print "The ground is far below you, and you're stuck
					in a parachute you bought from a vending machine!")
			)
		)

		(if (Said 'swing')
			(Print "\"Doobee doobee do.\"")
		)

		(if (Said 'remove, (take<off), untie, release, open, 
							pull/ripcord, parachute, harness')
			(Print "You try your best, but the parachute harness' buckles are
				jammed.")
		)

		(if (Said 'jump, grab, climb')
			(Print "You try, but the parachute harness holds you safely
				in place.")
		)

		(if (or	(Said 'use/knife')
					(Said 'cut/parachute, harness, rope'))
			(Print "A good idea...")
			(if (ego has: iKnife)
				(self changeState: reachUp)
			else
				(Print "But, with what?")
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(if (not (ego has: iKnife))
					(= seconds 10)
				)
			)

			(oopsAgain
				(Print "Struggling to free yourself from the parachute harness,
					you realize the buckles are hopelessly jammed!")
				(Print "Try as you might,
					you are unable to fix them, or free yourself.")
				(= seconds 4)
			)

			(UBdying
				(Print "Although you often have fun by just hanging around,
					this time you'd prefer to cut yourself down.")
				(Print "That's what you get for buying a parachute from a
					vending machine!")
				(= currentStatus DEAD)
			)


			;**	Here's the path we take only after he figures out he's
			;**	supposed to cut the parachute harness with the knife.

			(reachUp
				(User	canInput: FALSE)
				(aBigEgo
					cycleSpeed:		2
					setLoop:			lReaching
					setCycle:		EndLoop self
				)
			)

			(cuttingHarness
				(aBigEgo
					cycleSpeed:		1
					setLoop:			lCutting
					setCycle:		Forward
				)
				(= cycles 40)
			)

			(startFall
				(theGame changeScore: 8)	
				(Print "You saw away at the parachute harness with the
					dull cheese knife, eventually turning it into a mere thread
					supporting your entire body weight.
					Suddenly, you look down, and consider the consequences of
					falling to the jungle floor from this height!" BIG)
				(Print "But you don't consider it for long..." HIGH)
				(aBigEgoBottom		dispose:)
				(theSound			play:)
				(aBigEgo
					setStep:			1 10
					setLoop:			lFalling
					cel:				0
					setCycle:		EndLoop
					setMotion:		MoveTo (aBigEgo x?) 255 self
				)
			)

			(startTwinkle
				(aSparkle
					show:
					setCycle:		EndLoop self
				)
			)

			(sparkleDone
				(aSparkle			dispose:)
				(curRoom newRoom: 71)
			)
		)
	)
)

