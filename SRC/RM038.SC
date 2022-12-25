;**
;**	Logics for room 38 -- ship's lifeboat
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 20, 1988
;**


(script#				38)

(public
	rm38				0
)


(define	nearBoat		128 99 217 122)

;(define	vEgoIntoLifeboat			141)
	(define	lStretching			0)
	(define	lWindingUp			1)
	(define	lJumping				2)

;(define	vShipLifeboat				325)
;(define	vClouds						620)	;**	Loop0 contains Cels0-10


(local
	seenMessage
	numClouds
)


(enum												;**	cloudScript state values
	cloudInit
	repoCloud
	moveCloud
)

(enum
	intro
	lifeboatLine

	moveToChain
	overChain
	struggle
	downIntoBoat
	changePri
	sitting
	punchLine
	punchLine2
	outOfBoat

	downTheChain
	lifeboatDone
)


(instance rm38 of Room
	(properties
		picture:		38
		east:			31
	)

	(method (init &tmp i cloud)
		(Load VIEW currentEgoView)
		(Load VIEW vEgoIntoLifeboat)
		(Load VIEW vClouds)

		(super init:)

		(cond
			(( > machineSpeed 60)	(= numClouds 3))
			(( > machineSpeed 40)	(= numClouds 2))
			(( > machineSpeed 20)	(= numClouds 1))
		)	
		(if (not loweredLifeboats)
			(for ((= i 0)) ( < i numClouds) (( ++ i))		;**	Create clouds 
				((Actor new:)
					view:				vClouds
					setLoop:			0
					setPri:			1
					setStep:			1 1
					cel:				(Random 0 10)
					ignoreHorizon:
					ignoreActors:
					illegalBits:	0
					setScript:		(cloudScript new:)
				)
			)
		)

		(NormalEgo)
		(ego
			posn: 			318 108
			init:
		)

		(self
			setRegions:		rgShip
			setScript: 		rm38Script
		)
	)
)


(instance rm38Script of Script
	(method (doit)
		(super doit:)

		(if (and loweredLifeboats
					( < (ego x?) 300)
					(not seenMessage))
			(= seenMessage TRUE)
			(ego observeControl: cYellow)
			(Print "Look! The ship has come to a standstill in the water.
				They must be preparing for the lifeboat drill!")
			(Print "You'd better not leave this area!")
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'search, (look<in)/lifeboat')
			(cond
				((== currentStatus SITTING)
					(Print "You delve through the lifeboat's contents, but find
						nothing that will help you through your current
						predicament.")
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				(else
					(Print "You are too far from the lifeboat to examine it
						carefully. Perhaps you should climb in.")
				)
			)
		)

		(if (or	(Said 'cut/rope')
					(Said 'use/apparatus')
					(Said 'launch, lower/lifeboat'))
			(Print "A good idea.")
			(Print "But you find nothing here to help you do that.")
		)

		(if (Said 'look>')
			(if (Said '/ocean, water, wave')
				(Print "While no whitecaps are visible here,
					the rolling motion of the ship makes you
					briefly consider throwing something overboard.")
				(Print "(Where \"something\" is defined as your lunch!)" LOW)
			)

			(if (Said '/chain')
				(Print "It's there to prevent people like you from falling
					overboard.")
			)

			(if (Said '/lifeboat, mooring')
				(Print "It is firmly attached to its mooring apparatus
					by ropes.
					There is a chain preventing you from falling over
					the side of the ship.")
			)

			(if (Said '[/room]')
				(Print "The upper deck of the ship stretches away in all
					directions.
					There is a lifeboat hanging around here.")
			)
		)

		(if (Said 'get, move, (take<off), remove/gate, chain')
			(Print "No way! You might fall overboard.
				You're not coordinated enough to balance on this heaving deck.")
		)

		(if (Said 'crawl/chain, railing')
			(Print "Good idea.")
			(ego hide:)
			(Print "You crawl under the chain.
				As you plummet to the water you think to yourself,
				\"Gee, aren't we hundreds of miles from shore?\"" draw:)
			(Print "(Yes!)" LOW)
			(= currentStatus DEAD)
		)

		(if (Said '(jump<off), dive, jump/overboard, ship')
			(Print "Good idea.")
			(ego hide:)
			(Print "You rapidly race for the railing and fling yourself over
				the ship's railing.
				As you plummet to the water you think to yourself,
				\"Gee, aren't we hundreds of miles from shore?\"" draw:)
			(Print "(Yes!)" LOW)
			(= currentStatus DEAD)
		)

		(if (and	(== currentStatus SITTING)
					(or	(Said 'jump, rise, stand[/down, boat, chair]')
							(Said 'leave, go')
							(Said 'climb, rise, get<out, up')))
			(Ok)
			(self changeState: outOfBoat)
		)

		(if (or	(Said 'jump, sit[/down, boat, chair]')
					(Said 'climb/chain')
					(Said 'get, climb<in/boat')
					(Said 'enter/boat'))
			(cond
				((!= currentEgoView vEgo)
					(Print "Not dressed like that!")
				)
				((== currentStatus SITTING)
					(Print "You are already in the lifeboat.")
				)
				((!= currentStatus NORMAL)
					(NotNow)
				)
				((not (ego inRect: nearBoat))
					(NotClose)
				)
				(else
					(if (not scoredLifeboat)
						(= scoredLifeboat TRUE)
						(theGame changeScore: 2)
					)
					(Ok)
					(self changeState: moveToChain)
				)
			)
		)

		(if (Said 'get/lifeboat')
			(Print "Right. You'll never get THAT thing to fold up into your
				pocket!")
		)
	)


	(method (changeState newState)
		(switch (= state newState)

;**				(intro


			;**	Handle ego climbing into lifeboat.

			(moveToChain
				(HandsOff)
				(ego
					illegalBits:	0
					setMotion:		MoveTo 165 112 self
				)
			)

			(overChain
				(ego
					view: 			vEgoIntoLifeboat
					setLoop: 		lStretching
					cel: 				0
					setCycle:		Forward
				)
				(= cycles (* 3 (NumCels ego)))
			)

			(struggle
				(ego
					setLoop:			lWindingUp
					setCycle:		Forward
				)
				(= cycles (* 3 (NumCels ego)))
			)

			(downIntoBoat
				(ego
					setLoop:			lJumping
					cel:				0
					posn:				157 80
					setCycle:		CycleTo 3 FORWARD self
				)
			)

			(changePri
				(ego
					setPri:			0
					setCycle:		EndLoop self
				)
			)

			(sitting
				(if loweredLifeboats
					(curRoom newRoom: 131)	;**	longshot of lifeboats lowering
				else
					(User canInput: 	TRUE)
					(= currentStatus SITTING)
					(= seconds 7)
				)
			)

			(punchLine
				(Print "(Gee. This is exciting.)" LOW)
				(= seconds 7)
			)

			(punchLine2
				(Print "(Are we having fun yet?)" LOW)
			)

			(outOfBoat
				(User
					canInput: 		FALSE
				)
				(= seconds	0)
				(= cycles	0)
				(ego
					setCel:			LastCel
					setCycle:		CycleTo 3 REVERSE self
				)
			)

			(downTheChain
				(ego
					setPri:			RELEASE
					setCycle:		BegLoop self
				)
			)

			(lifeboatDone
				(ego
					posn:				165 112
				)
				(NormalEgo facingNorth)
			)
		)
	)
)


(instance cloudScript of Script
	(method (changeState newState &tmp tmpY tmpCel)
		(switch (= state newState)

			(cloudInit
				(client
					posn: 		(Random 0 270) (Random 3 35)
					init:
				)
				(self changeState: moveCloud)
			)

			(repoCloud
				(= tmpY			(Random 3 35))
				(= tmpCel		(Random 0 10))
				(client
					setCel:		tmpCel
					posn: 		321 tmpY
				)
				(self changeState: moveCloud)
			)

			(moveCloud
				(client
					moveSpeed:	(Random 0 3)
					setMotion:	MoveTo ( - 1 (CelWide vClouds 0 tmpCel)) (client y?) self
				)
				(= state cloudInit)
			)
		)
	)
)

