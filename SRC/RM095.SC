;**
;**	Logics for room 95 -- Henchette death
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	September 27, 1988
;**


(script#				95)

(public
	rm95				0
)


;(define	vBigEgo						110)
;(define	vBElasered					113)
;(define	vEgoPrisoner				191)
	(define	lBloodyBed		0)
	(define	lSitting			1)
	(define	lReclining		2)
	(define	lCuffing			3)
	(define	lBedRising		4)
	(define	lGroinGone		5)
	(define	lEgoToAcid		6)
	(define	lSquirming		7)

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
	(define	lBedDown			9)

(define	chainWestX			218)
(define	chainEastX			250)


(local
	work
	aHench
	aChainEast
	aChainWest
	aDoor
	aLaser
	aBeam
	aAcid
	aBed
	aBigEgo
	aBigEgoFace
)


(enum												;**	henchScript state values
	henchInit
	henchStart
	henchMiddle
	outOfCopter
	henchFrontOfCopter
	atPanel
	henchDone
)

(enum												;**	rmScript state values
	intro

	openCopterDoor
	copterDoorOpen
	leaveChopper
	egoOutOfCopter
	frontOfCopter
	atBed
	onBed
	reclines
	comfy
	raiseHandcuffs
	squirm
	heyWhat
	noNotMe

	lowerChains
	hookUpChains
	raiseChains
	raiseBed
	openDoors
	hydrofloric
	terror
	lowerLaser
	startBeam
	egoGroin
	byeByeBigGuy
	bodyGone
	egoOnAcid
	egoInAcid
	punch
)


(instance rm95 of Room
	(properties
		picture:		95
		horizon:		1
	)

	(method (init)
		(Load VIEW vEgoPrisoner)
		(Load VIEW vLaserDeath)
 		(Load VIEW vBigEgo)
 		(Load VIEW vBElasered)

		(Load SOUND mHenchette)

		(super init:)

		(theSound play:)

		((View new:)							;**	copter steps
			view: 			vLaserDeath
			ignoreActors:
			loop: 			8
			posn: 			35 176
			setPri: 			ltBlue
			addToPic:
		)
		((View new:)
			view: 			vLaserDeath
			ignoreActors:
			loop: 			lLogo
			posn: 			9 138
			setPri: 			13
			addToPic:
		)
		((View new:)
			view: 			vLaserDeath
			ignoreActors:
			loop: 			lSmpte
			posn: 			33 69
			setPri: 			blue
			addToPic:
		)
		((View new:)
			view: 			vLaserDeath
			ignoreActors:
			loop: 			lSmpte
			posn: 			58 69
			setPri: 			blue
			addToPic:
		)
		((= aBed (View new:))
			view:				vLaserDeath
			ignoreActors:
			loop:				lBedDown
			setPri:			5
			posn:				214 106
			stopUpd:
			init:
		)
		((= aBigEgo (View new:))
			view:				vBigEgo
			ignoreActors:
			setPri:			14
			posn:				172 1039
			init:
		)
		((= aBigEgoFace (Prop new:))
			view:				vBElasered
			ignoreActors:
			cycleSpeed:		5
			setPri:			15
			posn:				172 1039
			setCel:			0
			init:
		)
		((= aAcid (Prop new:))
			view:				vLaserDeath
			ignoreActors:
			setLoop:			lAcidDoor
			setPri:			1
			posn:				235 137
			cycleSpeed:		2
			init:
			hide:
		)

		((= aDoor (Actor new:))
			view:				vLaserDeath
			ignoreActors:
			illegalBits:	0
			setLoop:			lDoor
			setStep:			1 1
			setPri:			14
			posn:				35 138
			stopUpd:
			init:
		)
		((= aChainWest (Actor new:))
			view:				vLaserDeath
			ignoreActors:
			ignoreHorizon:
			illegalBits:	0
			setLoop:			0
			setPri:			4
			posn:				chainWestX -5
			setStep:			1 3
			init:
		)
		((= aChainEast (Actor new:))
			view:				vLaserDeath
			ignoreActors:
			ignoreHorizon:
			illegalBits:	0
			setLoop:			0
			setPri:			4
			posn:				chainEastX -5
			setStep:			1 3
			init:
		)
		((= aLaser (Actor new:))
			view:				vLaserDeath
			ignoreActors:
			ignoreHorizon:
			illegalBits:	0
			setLoop:			lLaser
			setPri:			13
			posn:				234 -5
			moveSpeed:		1
			setStep:			1 1
			init:
		)
		((= aBeam (Actor new:))
			view:				vLaserDeath
			ignoreActors:
			ignoreHorizon:
			illegalBits: 	0
			setLoop:			lBeam
			setPri:			12
			setCycle:		Walk
			posn:				234 28
			moveSpeed:		1
			setStep:			1 1
			init:
			hide:
		)

		(if (== currentHenchView FALSE)
			(= currentHenchView vShowBizDoll)
		)
		((= aHench (Actor new:))
			view:				currentHenchView
			ignoreActors:
			illegalBits:	0
			loop:				facingSouth
			setCycle:		Walk
			posn:				36 155
			init:
			setScript:		henchScript
		)

		(self
			setScript: 		rm95Script
		)

		(= currentEgoView vEgo)
		(NormalEgo)
		(ego
			loop:				facingSouth
			posn: 			32 151
			init:
		)
		(HandsOff)
	)
)


(instance rm95Script of Script
	(method (doit)
		(super doit:)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(= seconds 3)
			)

			(openCopterDoor
				(Print "You are so impressed!
					You've never had a girl invite you to her pad before --
					not to mention her landing pad!")
				(aDoor
					setMotion:	MoveTo 9 138 self
				)
			)

			(copterDoorOpen
				(aDoor
					stopUpd:
				)
				(henchScript cue:)
			)

			(leaveChopper
 				(ego
					setMotion:	MoveTo 31 183 self
				)
			)

			(egoOutOfCopter
				(ego
					setMotion:	MoveTo 163 183 self
				)
			)

			(frontOfCopter
				(ego
					setMotion:	MoveTo 163 111 self
				)
			)

			(atBed
				(ego
					setMotion:	MoveTo 209 111 self
				)
			)

			(onBed
				(ego
					view:			vEgoPrisoner
					setLoop:		lSitting
					setPri:		11
					posn:			235 96
					cycleSpeed:	1
					cel:			0
					setCycle:	EndLoop self
				)
			)

			(reclines
				(ego
					cycleSpeed:	2
					setLoop:		lReclining
					cel:			0
					posn:			235 106
					setCycle:	EndLoop self
				)
			)

			(comfy
				(Print "\"Hey, baby,\" you say,
					\"What a great, kinky bed!\"" draw:)
				(= seconds 3)
			)

			(raiseHandcuffs
				(ego
					cycleSpeed:	1
					setLoop:		lCuffing
					cel:			0
					setCycle:	EndLoop self
				)
			)

			(squirm
				(ego
					setLoop:		lSquirming
					setCycle:	Forward
				)
				(= cycles 20)
			)

			(heyWhat
				(Print "\"Hey! What's going on here?\" you shout.
					\"Get these ankle cuffs off me!
					And, why the handcuffs?\"" HIGH draw:)
				(delay 3)
			)

			(noNotMe
				(Print "\"Oh, I'm just into mechanical devices,\"
					she replies with a smile.
					\"Hold still, darling, this will only take a second.\"" HIGH)
				(delay 3)
			)

			(lowerChains
				(aChainWest
					setMotion:	MoveTo chainWestX 90 self
				)
				(aChainEast
					setMotion:	MoveTo chainEastX 90
				)
			)

			(hookUpChains
				(delay 3)
			)

			(raiseChains
				(aBed				hide:)
				(ego
					setLoop:		lBedRising
					cel:			0
					posn:			214 106
					cycleSpeed:	1
					setCycle:	EndLoop self
				)
				(aChainWest
					setStep:		1 2
					setMotion:	MoveTo chainWestX 45
				)
				(aChainEast
					setStep:		1 2
					setMotion:	MoveTo chainEastX 45
				)
			)

			(raiseBed
				(ego
					setStep:		1 2
					setMotion:	MoveTo 214 89 self
				)
			)

			(openDoors
				(ego				stopUpd:)
				(aChainWest		
					setMotion:	FALSE
					stopUpd:
				)
				(aChainWest		
					setMotion:	FALSE
					stopUpd:
				)
				(aAcid	
					show:
					setCycle:	EndLoop self
				)
			)

			(hydrofloric
				(aAcid
					setLoop:		lAcid
					cycleSpeed:	0
					setCycle:	Forward
				)
				(= cycles 15)
			)

			(terror
				(Print "\"Why does that look like hydrofloric acid below me?\"
					you shout in terror." HIGH)
				(Print "\"Because it is, silly,\" she responds." HIGH)
				(delay 3)
			)

			(lowerLaser
				(aLaser
					setMotion:	MoveTo 234 29 self
				)
			)

			(startBeam
				(aBigEgo
					posn:				172 39
					stopUpd:
				)
				(aBigEgoFace
					posn:				172 39
					setCycle:		EndLoop
				)
				(aLaser
					setStep:		1 1
					moveSpeed:	1
					setMotion:	MoveTo 234 -2
				)
				(aBeam
					posn:			234 28
					setStep:		1 1
					cycleSpeed:	0
					moveSpeed:	1
					show:
					setMotion:	MoveTo 234 21 self
				)
			)

			(egoGroin
				(Print "Well, Larry; this brings a whole new meaning to the
					term \"dismemberment!\"" LOW)
				(aBeam
					setMotion:	MoveTo 234 0 self
				)
				(aBed
					view:			vEgoPrisoner
					loop:			lBloodyBed
					posn:			214 89
					show:
				)
				(ego
					setLoop:		lGroinGone
					posn:			(+ (ego x?) 8) (- (ego y?) 5)
					cel:			0
					cycleSpeed:	6
					setCycle:	EndLoop
				)
				(= cycles 10)
			)

			(byeByeBigGuy
				(aBigEgoFace		dispose:)
				(aBigEgo				dispose:)
			)

			(bodyGone
				(aLaser			dispose:)
				(aBeam			dispose:)
				(ego
					setLoop:		lEgoToAcid
					setCel:		0
					setStep:		1 7
					posn:			235 91
					setMotion:	MoveTo 235 134 self
				)
			)

			(egoOnAcid
				(ego
					setMotion:	FALSE
					cycleSpeed:	0
					setCycle:	EndLoop self
				)
			)

			(egoInAcid
				(= seconds 3)
			)

			(punch
				(= currentStatus DEAD)
			)
		)
	)
)


(instance henchScript of Script
	(method (changeState newState)
		(switch (= state newState)

;**				(henchInit)

			(henchStart
				(aHench
					setMotion:	MoveTo 37 169 self
				)
			)

			(henchMiddle
				(aHench
					setMotion:	MoveTo 39 181 self
				)
				(rm95Script cue:)
			)

			(outOfCopter
				(aHench
					setMotion:	MoveTo 139 181 self
				)
			)

			(henchFrontOfCopter
				(Print "\"You just wait over there on the bed,
					while I slip something,\" she says with a smile.")
				(aHench
					setMotion:	MoveTo 139 131 self
				)
			)

			(atPanel
				(Print "\"Perhaps if things go well tonight,\"
					you think to yourself,
					\"I could give her a subscription to 'Popular Mechanics!'\"")
				(aHench
					setMotion:	MoveTo 49 91 self
				)
			)

			(henchDone
				(aHench
					setLoop:		facingNorth
					stopUpd:
				)
				(self dispose:)
			)
		)
	)
)

(instance theSound of Sound
	(properties
	 	number:	mHenchette
		loop:		1
	)
)

