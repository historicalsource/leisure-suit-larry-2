;**
;**	Logics for room 181 -- Just a narrow walkway between 81 and 82
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 19, 1988
;**


(script#				181)

(public
	rm181				0
)


(local
	aWaterfall
	seenMessage
	safeX
	safeY
)


(enum												;**	rmScript state values
	intro
	startFalling
	fallDone
)


(instance theSound of Sound
	(properties
		number:			sFalling
		loop:				1
	)
)

(instance blockEast of Block
	(properties
		top:				1
		bottom:			222
		left:				328
		right:			333
	)
)

(instance blockSouth of Block
	(properties
		top:				192
		bottom:			222
		left:				99
		right:			333
	)
)


(instance rm181 of Room
	(properties
		picture:		181
		horizon:		33
		north:		82
	)

	(method (init)
		(Load VIEW vWaterfall181)
		(Load VIEW vEgoFalling)
		(Load SOUND sFalling)

		(super init:)

		(theSound			init:)

		((= aWaterfall (Prop new:))
			view:				vWaterfall181
			setLoop:			0
			posn:				27 189
			setCycle:		Forward
			setPri: 			3
			isExtra:			TRUE
			init:
		)

		(blockEast 			init:)
		(blockSouth			init:)

		(self
			setScript: 		rm181Script
		)

		(cond
			((== prevRoomNum	82)		(ego posn:	21	36))
			(else								(ego posn: 310 186))
		)

		(NormalEgo)
		(ego
			observeBlocks:	blockEast blockSouth
			init:
		)
	)
)


(instance rm181Script of Script
	(method (doit)
		(super doit:)

		(if (and	(== currentStatus NORMAL)
					(or	(== (ego edgeHit?) SOUTH)
							(== (ego edgeHit?) EAST)))
			(if (== seenMessage FALSE)
				(= seenMessage TRUE)
				(Print "You have no need to return that way.
					Your quest lies before you.
					Hurry, Larry!")
			)
		else
			(= seenMessage FALSE)
		)

		(if (== currentStatus NORMAL)
			(if ( < (ego y?) 50)
				(ego setPri: cyan)
			else
				(ego setPri: RELEASE)
			)
		)

		(cond
			((and	(== (ego edgeHit?) WEST)
					(== currentStatus NORMAL))
				(curRoom newRoom: 82)
			)
			((and	(& (ego onControl:) cBlue)
					(== currentStatus NORMAL))
				(ego setPri: ltBlack)
				(self changeState: startFalling)
			)
			((and	(& (ego onControl:) cGreen)
					(== currentStatus NORMAL))
				(ego setPri: black)
				(self changeState: startFalling)
			)
			((== currentStatus NORMAL)
				(= safeX (ego x?))
				(= safeY (ego y?))
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(if (Said '/hole, cloud, sky, (rock<through, in)')
				(Print "You look through the hole in the rock and see just
					a cloud.")
				(Print "(Why? Did you think you see something?)" LOW)
			)

			(if (Said '[/room, mountain, cliff, waterfall, water, river]')
				(Print "Sheer cliffs drop off on both sides.
					For a moment you consider walking very near the edge,
					just to see if you can fall off.
					Perhaps you won't.
					Perhaps you can fly!
					Perhaps the love of the beautiful Kalalau has given you
					amazing, magical powers that are only activated here on
					this enchanted island.")
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(intro)

			(startFalling
				(HandsOff)
				(Print "Oh, shhhhhhhh..." HIGH dispose:)
				(= currentStatus FALLING)
				(theSound play:)
				(ego
					view:			vEgoFalling
					illegalBits:0
					ignoreActors:
					ignoreBlocks: blockSouth blockEast
					posn:			(ego x?) (- (ego y?) 15)
					cel:			0
					setStep:		1 15
					setCycle:	Forward
					setMotion:	MoveTo (ego x?) ( + (ego y?) 200) self
				)
			)

			(fallDone
				(cls)
				(= currentStatus NEAR_DEATH)
				(Print "Perhaps Kalalau's love only SEEMED to give you wings!")
				(= currentStatus DEAD)
				(if debugging
					(NormalEgo)
					(ego
						observeBlocks:	blockEast blockSouth
						posn: 			safeX safeY
					)
					(self changeState: intro)	;**	back to normal
				)
			)
		)
	)
)

