;**
;**	Logics for room 85 -- the SI copter rescue Closing Cartoon
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 2, 1988
;**


(script#				85)

(public
	rm85				0
)


;(define	vEgoFortress				197)
	(define	lBalling				0)
	(define	lUnwinding			1)
	(define	lMountingLamp		2)
	(define	lOnLamp				3)
	(define	lFalling				4)
	(define	lLanding				5)
	(define	lHighFive			6)

;(define	vLaserDeath					829)
	(define	lChain			0)
	(define	lLaser			1)
	(define	lAcid				2)
	(define	lBeam				3)
	(define	lAcidDoor		4)
	(define	lLogo				5)
	(define	lDoor				6)
	(define	lSmpte			7)
	(define	lSteps			8)
	(define	lBed				9)


(local
	work
	aDoor
	a2
	a3
)


(enum												;**	rmScript state values
	intro
	doneBalling
	toChopper
	toSteps
	enterChopper
	inChopper
	nextRoom
)


(instance rm85 of Room
	(properties
		picture:		95
		horizon:		1
	)

	(method (init)
		(Load VIEW vLaserDeath)
		(Load VIEW vEgoFortress)

		(super init:)

		((View new:)							;**	copter steps
			view: 			vLaserDeath
			ignoreActors:
			loop: 			lSteps
			posn: 			35 174
			setPri:			11
			addToPic:
		)

		((View new:)
			view: 			vLaserDeath
			loop: 			lLogo
			cel: 				1
			posn: 			65 151			;**	Al's SI logo
			setPri: 			13
			addToPic:
		)
		((View new:)
			view: 			vLaserDeath
			loop: 			lSmpte
			posn: 			33 69
			setPri: 			blue
			addToPic:
		)
		((View new:)
			view: 			vLaserDeath
			loop: 			lSmpte
			posn: 			58 69
			setPri: 			blue
			addToPic:
		)
		((View new:)
			view:				vLaserDeath
			loop:				lBed
			setPri:			5			   
			posn:				214 106
			addToPic:
		)
		((= aDoor (Actor new:))
			view:				vLaserDeath
			ignoreActors:
			illegalBits:	0
			setLoop:			lDoor
			setStep:			1 1
			setPri:			14
			posn:				9 138
			stopUpd:
			init:
		)

		(= currentStatus CLOSING_CARTOON)
		(ego
			view:				vEgoFortress
			setLoop:			lBalling
			posn: 			48 -1
			illegalBits:	0
			setStep:			5 5
			ignoreActors:
			setCycle:		Forward
			init:
		)
		(HandsOff)
		(self
			setScript: 		rm85Script
		)
	)
)


(instance rm85Script of Script
	(method (doit)
		(super doit:)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(ego
					setPri:		RELEASE
					setMotion:	MoveTo 149 144 self
				)
			)

			(doneBalling
				(ego
					setLoop:		lUnwinding
					setCel:		0
					setCycle:	EndLoop self
				)
			)

			(toChopper
				(ego
					view:			vEgo
					loop:			facingSouth
					setLoop:		RELEASE
					setCycle:	Walk
					setStep:		3 2
					setMotion:	MoveTo 144 183 self
				)
				(Print "\"Thanks a lot for dropping me down the stairs!\" 
					you cry to the native women.
					\"Hurry and get in the helicopter!\"" draw:)
				(Print "\"No, thanks,\" they yell down the stairs,
					\"we'll use the back door and take our chances with 
					the glacier!\"")
			)

			(toSteps
				(Print "\"Come on, Big Laffer,\" says the pilot.
					\"Climb aboard!\"")
 				(ego
					setMotion:	MoveTo 35 183 self
				)
			)

			(enterChopper
				(ego
					setMotion:	MoveTo 32 153 self
				)
			)

			(inChopper
				(aDoor
					setMotion:	MoveTo 35 138 self
				)
			)

			(nextRoom
				(Print "\"Sorry, I had a little trouble with that fog machine!\"
					you tell him.")
				(= islandStatus leavingVolcano)
				(curRoom newRoom: 92)
			)
		)
	)
)

