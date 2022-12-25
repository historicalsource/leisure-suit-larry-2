;**
;**	Logics for room 47 -- Gay Beach
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 23, 1988
;**


(script#				47)

(public
	rm47				0
)


(define	grabMe 		86 2 333 140)

(define	howDidYouKnow
	"\"How did you know it was me?\" you ask.")

;(define	vBeach						406)
;(define	vGayBeach					410)	;**	legs under umbrella
;(define	vGayHench					411)
;(define	vGayHench2					412)
;(define	vGayHenchGrabbing			413)
;(define	vGayHench2Grabbing		414)
;(define	vAirplane					511)


(local
	aPlane
	aWave
	henchStatus
	aHench1
	aHench2
	aWaveEast
)


(enum												;**	henchStatus values
	WAITING_TO_CHASE
	CHASING
	CAUGHT_HIM
	8
	NOT_THIS_TIME
	MADE_CRUDE_REMARK
)

(enum												;**	generic henchScript values
	henchChasingEgo
	henchAtEgo
	henchGrabDone
	caughtYou
	soNear
)

(enum												;**	individual henchScript values
	henchInit
	henchClear
)

(enum												;**	rmScript values
	intro
	youMadeIt
	offScreen
)


(instance rm47 of Room
	(properties
		picture:		47
		horizon:		5
		west:			42
		east: 		48
	)

	(method (init)
		(Load VIEW vGayBeach)
		(Load VIEW vGayHench)
		(Load VIEW vGayHenchGrabbing)
		(Load VIEW vAirplane)
		(Load VIEW vGayBeachWave)

		(super init:)

		((View new:)							;**	left umbrella
			view: 			vGayBeach
			loop: 			0
			cel: 				1
			posn: 			181 144
			setPri: 			10
			addToPic:
		)
		((View new:)							;**	right umbrella
			view: 			vGayBeach
			loop: 			1
			cel: 				0
			posn: 			282 125
			setPri: 			8
			addToPic:
		)
		((View new:)							;**	feet under red umbrella
			view: 			vGayBeach
			loop: 			0
			cel: 				0
			posn: 			200 126
			setPri: 			8
			ignoreActors:
			addToPic:
		)

		((= aWave (Prop new:))
			view:				vGayBeachWave
			ignoreActors:
			posn:				268 50
			setCycle:		Forward
			cycleSpeed:		6
			isExtra:			TRUE
			init:
		)

		((= aPlane (Actor new:))
			view:				vAirplane
			setLoop:			5
			setCel:			0
			setPri:			3
			posn:				322 25
			illegalBits:	0
			ignoreActors:
			setStep:			1 1
			init:
			hide:
		)
		((= aHench1 (Actor new:))
			view:				vGayHench
			setLoop:			facingWest
			setCycle:		Walk
			setStep:			4 3
			ignoreControl:	cYellow
			posn:				159 134
			init:
			setAvoider:		(Avoider new:)
		)
		((= aHench2 (Actor new:))
			view:				vGayHench
			setLoop:			facingEast
			setCycle:		Walk
			setStep:			4 3
			posn:				54 152
			init:
			setAvoider:		(Avoider new:)
		)

		(cond
			(( >	99 (ego y?))			(ego y:	99))
			(( < 134 (ego y?))			(ego y: 134))
		)
		(NormalEgo)
		(ego
			x:					3
			observeControl:cYellow
			init:
		)

		(if (and	bodyWaxed
					(== currentEgoView vEgoBikiniStuffed))
			(= henchStatus NOT_THIS_TIME)
		)

		(self
			setRegions:		rgResortBeach
			setScript: 		rm47Script
		)
	)
)


(instance rm47Script of Script
	(method (doit)
		(super doit:)

		(cond
			((and (== henchStatus WAITING_TO_CHASE)
					(ego inRect: grabMe))
				(= henchStatus CHASING)
				(aHench1	setScript: hench1Script)
				(aHench2	setScript: hench2Script)
			)
			((and (== henchStatus NOT_THIS_TIME)
					(ego inRect: grabMe))
				(= henchStatus MADE_CRUDE_REMARK)
				(Print "\"Whoa-aaah! Hellooo, baaabee,\" cries the KGB agent,
					\"Oooh, honey, I thinks I loves ya!!\"")
				(Print "(Vhat happened to his accent?)" LOW)
			)
			((== EAST (ego edgeHit?))
				(HandsOff)
				(theGame changeScore: 12)	;**	      getting past KGB
				(Print "You did it!
					You made it across the beach without being caught by the
					KGB agents!!")
				(Print "What lies ahead now?!")
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'talk/agent, man')
			(Print "Don't! They might recognize your voice.")
		)

		(if (Said 'look>')
			(if (Said '/man, agent')
				(Print "Two particularly interesting guys are wearing slouch
					hats and trench coats.")
			)

			(if (Said '[/room, bush]')
				(Print "Funny. This entire beach is filled with men.")
			)
		)
	)
)


(instance hench1Script of Script
	(method (changeState newState)
		(switch (= state newState)

			(henchInit
				(aHench1 setMotion:	MoveTo 138 134 self)
			)

			(henchClear
				(aHench1	setScript:	(henchScript new:))
			)
		)
	)
)

(instance hench2Script of Script
	(method (changeState newState)
		(switch (= state newState)

			(henchInit
				(aHench2 setMotion:	MoveTo 123 152 self)
			)

			(henchClear
				(aHench2	setScript:	(henchScript new:))
			)
		)
	)
)

(instance henchScript of Script
	(method (changeState newState &tmp tmpX tmpY)
		(switch (= state newState)

			(henchChasingEgo
				(client
					setLoop:		RELEASE
					setMotion:	Chase ego 11 self
				)
			)

			(henchAtEgo
				(if (== henchStatus CHASING)
					(= currentStatus NEAR_DEATH)
					(= henchStatus CAUGHT_HIM)
					(Print "\"I have him!\" cries the agent.")
					(HandsOff)
					(ego
						stopUpd:
					)
					(aPlane
						show:
						setMotion:		MoveTo 116 13
					)
					(= tmpY (- (ego y?) 1))
					(if ( < (ego x?) (client x?))
						(= tmpX ( + (ego x?) 11))
						(client setLoop: facingWest)
					else
						(= tmpX ( - (ego x?) 11))
						(client setLoop: facingEast)
					)
					(client
						view:				vGayHenchGrabbing
						ignoreActors:
						illegalBits:	0
						posn:				tmpX tmpY
						setMotion:		FALSE
						setPri:			(+ (ego priority?) 1)
						cel:				0
						setCycle:		EndLoop self
					)
				)
			)

			(henchGrabDone
				(= seconds 3)
				(cond
					((== currentEgoView vEgoBikiniStuffed)
						(Print "\"Ve caught ju, ju vicious villian of the
							People!\" shouts the KGB agent,
							by now growing quite hoarse.")
						(Print howDidYouKnow)
						(Print "\"Only in Russia do vomen have such body hair!\"
							he answers.")
					)
					((== currentEgoView vEgoBikini)
						(Print "\"Ve caught ju, ju pawn of the proletariat!\"
							screams the poorly disguised KGB agent.")
						(Print howDidYouKnow)
						(Print "\"Only in Russia do vomen have such flat chests!\"
							he responds.")
					)
					((== currentEgoView vEgoBlond)
						(Print "\"Ve caught ju, ju capitalist slime-sucking pig,\"
							shouts the KGB agent,
							\"we vill make you ze best-looking fashion model
							in all U.S.S.R!\"")
						(Print howDidYouKnow)
						(Print "\"Only in Russia do 
							women wear leisure suits to beach!\"
							he replies.")
					)
					((== currentEgoView vEgo)
						;**	spelling of "bourgeois" is correct, June 20, 1988
						(Print "\"Ve caught ju, ju capitalist bourgeois enlistee
							of the Evil Empire!\" shouts the KGB agent,
 							looking ill-at-ease in his ill-fitted trench coat
							and oversized Speedo.")
					)
					(else
						(Print "Got you!")
					)
				)
			)

			(caughtYou
				(Print "The KGB have recognized you, captured you,
					and will now take you to their headquarters
					for questioning...")
				(Print "...and other fun stuff!")
				(= seconds 4)
			)

			(soNear
				(Print "Too bad, too, since you were nearing the airport...
					and your freedom from this resort!")
				(= currentStatus HEADING_FOR_DEATH_SCENE)
				(curRoom newRoom: 96)
			)
		)
	)
)

