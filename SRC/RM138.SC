;**
;**	Logics for room 138 -- Victory(?) At Sea
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 14, 1988
;**


(script#				138)

(public
	rm138				0
)


(define	daySunscreen	3)
(define	dayWig 			4)
(define	dayThirst		5)
(define	dayFood			6)
(define	dayDone			9)					;**	Now, go to resort.

(define	calendarX		220)
(define	calendarY		40)

;(define	vEgoInLifeboat				120)
	(define	lSurvives		0)
	(define	lWig				1)
	(define	lFlame			2)

;(define	vELBGreen					121)
;(define	vELBThirst					122)
;(define	vELBSunburn					123)
;(define	vELBHunger					124)

;(define	vLifeboat					326)
	(define	lShipReceding	0)
	(define	lCalendar		1)
	(define	lDate				2)
	(define	lPage				3)
	(define	lWave				4)


(local
	day
	aBigEgo
	aShip
	aCalendar
	aPage
	aDate
	aFlame
	aWave
)


(enum
	intro
	atHorizon
	belowHorizon
	startCalendar
	tearPages
	waitAwhile
	willyMakette
	yepFinishIt

	egoSunburn
	egoSunburn2
	egoSunburnDone

	egoFried										;**	No wig
	egoFried2
	egoFried3
	egoFryDone

	egoSick										;**	Brought spinach dip to the party
	egoSick2
	egoSickDone

	egoThirsty									;**	Has no soda
	egoThirsty2
	egoThirstyDone

	egoHungry									;**	Has NO food
	egoHungry2
	egoHungryDone

	noWeigh
	clearHisCalendar
	UBdying
)

(enum												;**	calendarScript state values
	calendarInit
	doPage
	fixDate
	pageFlying
	pageGone

	dummy
	eraseCalendar
)


(instance rm138 of Room
	(properties
		picture:		138
		horizon:		5
	)

	(method (init)
		(Load VIEW vEgoInLifeboat)
		(Load VIEW vLifeboat)
		(Load VIEW vELBGreen)
		(Load VIEW vELBThirst)
		(Load VIEW vELBSunburn)
		(Load VIEW vELBHunger)

		(super init:)

		((= aBigEgo (Prop new:))
			view:				vEgoInLifeboat
			setLoop:			lSurvives
			setPri:			3
			cel: 				0
			posn: 			116 68
			stopUpd:
			init:
		)

		(if (== wearingWig TRUE)
			((View new:)
				view:				vEgoInLifeboat
				loop:				lWig
				setPri:			5
				posn: 			116 68
				ignoreActors:
				stopUpd:
				init:
			)
		)

		((= aCalendar (Prop new:))
			view:				vLifeboat
			setLoop:			lCalendar
			setCel:			0
			setPri:			7
			posn:				calendarX calendarY
			setCycle:		Forward
			init:
			hide:
		)
		((= aDate (Prop new:))
			view:				vLifeboat
			setLoop:			lDate
			setCel:			0
			setPri:			9
			posn:				(+ calendarX 8) (+ calendarY 32)
			init:
			hide:
		)

		((= aPage (Actor new:))
			view:				vLifeboat
			setLoop:			lPage
			setCel:			0
			setPri:			11
			posn:				calendarX calendarY
			ignoreActors:
			ignoreHorizon:
			cycleSpeed:		0
			setStep:			10 10
			init:
			hide:
		)

		((= aShip (Actor new:))
			view:				vLifeboat
			setLoop:			lShipReceding
			setCel:			0
			setPri:			3
			setStep:			1 1
			moveSpeed:		8
			cycleSpeed:		8
			posn:				233 60	;**	60 is 9y below horizon; loop has 10 cels
			ignoreActors:
			ignoreHorizon:
			init:
		)

		((= aWave (Prop new:))
			view:				vLifeboat
			setLoop:			lWave
			setPri:			11
			posn:				115 156
			cycleSpeed:		2
			setCycle:		Forward
			isExtra:			TRUE
			init:
		)
		((= aFlame (Prop new:))
			view:				vEgoInLifeboat
			loop: 			lFlame
			posn: 			115 67
			setPri: 			5
			setCycle:		Forward
			init:
			hide:
		)

		(HandsOff)
		(= currentStatus LIFEBOATING)
		(self setScript: rm138Script)
	)
)


(instance rm138Script of Script
	(method (doit)
		(super doit:)
	)


	(method (changeState newState)
		(switch (= state newState)

			(intro
				(aShip
					setCycle:		EndLoop
					setMotion:		MoveTo 233 51 self
				)
			)

			(atHorizon
				(aShip
					setPri:			1
					setCel:
					moveSpeed:		13
					setMotion:		MoveTo 233 66 self
				)
				(Print "And as the good ship U.S.S. Love Tub
					slowly sinks behind the horizon,
					we rejoin our noble hero in his latest predicament.")
			)

			(belowHorizon
				(Print "Isn't it funny how time flies when you're having fun!")
				(aShip 				dispose:)
				(= seconds 3)
			)

			(startCalendar
				(aCalendar			show:)
				(aDate				show:)
				(= seconds 3)
			)

			(tearPages
				(if ( >= day dayDone)		;**	If we get through enough days...
					(Print "During a particularly rough storm,
						late in your tenth night at sea,
						your lifeboat crashes on a coral reef and shatters to bits.
						You grab the largest piece of wood you can,
						hold on tight and survive the storm.
						The dawn finds you crashing through an offshore reef,
						tumbling through the surf to a gorgeous beach.")
					(curRoom newRoom: 42)	;**	...we get to go to the beach.
				else
					(++ day)
					(calendarScript changeState: doPage)	;**	A day passes.
				)
			)

			(waitAwhile
				(= seconds 3)
			)

			(willyMakette
				(cond
					((== day daySunscreen)	;**	Did he use sunscreen?
						(Print "Boy, is that sun hot!" HIGH)
						(if (or	(== wearingSunscreen TRUE)
									(== wearingSunscreen ON_CLOTHES))
							(Print "It's a good thing you used that
								sunscreen to block those ultraviolets!
								The sun out here is hot enough to fry an egg." WIDE)
							(theGame changeScore: 5)
							(= seconds 1)
						else
							(self changeState: egoSunburn)
						)
					)

					((== day dayWig)			;**	Did he wear wig?
						(Print "You think the sun must be even hotter today!" HIGH)
						(if wearingWig
							(Print "It's a good thing you wore that silly wig.
								The sun out here is hot enough to fry your brains."
								HIGH)
							(theGame changeScore: 5)
							(= seconds 1)
						else
							(self changeState: egoFried)
						)
					)

					((== day dayThirst)		;**	Has he drunk?
						(Print "Your thirst becomes more and more intense
							with every passing day.
							Visions of pre-Classic Coke float before your eyes!"
								WIDE)
						(if (not (ego has: iGulp))
							(self changeState: egoThirsty)
						else
							(Print "You extract the Grotesque Gulp from your
								inside coat pocket and are surprised to discover
								it has retained its entire 32-gallon capacity.
								You now have enough fluid to last you for weeks;
								but you are worried about your blood sugar level!"
									WIDE)
							(theGame changeScore: 5)
							(ego put: iGulp LIMBO)
							(= seconds 1)
						)
					)

					((== day dayFood)			;**	Has he eaten?
						(Print "Your hunger grows intense with the passing
							of the days.
							Visions of Danny's Pizza float before your eyes!" WIDE)
						(cond
							((ego has: iSpinachDip)
								(Print "It's a good thing you brought that
									spinach dip along." HIGH)
								(ego put: iSpinachDip LIMBO)
								(self changeState: egoSick)
							)
							((ego has: iSewingKit)
								(Print "You cleverly open the sewing kit you stole
									from that Mother's nightstand, extract a safety
									pin and thread.
									You patiently fish for hours, but eventually
									catch your limit." WIDE)
								(Print "(Your limit for raw fish is quite low!)")
								(theGame changeScore: 10)
								(= seconds 1)
							)
							((ego has: iFruit)
								(Print "You were so clever to grab that basket of
									fruit from the nightstand." HIGH)
								(ego put: iFruit LIMBO)
								(theGame changeScore: 5)
								(= seconds 1)
							)
							(else
								(self changeState: egoHungry)
							)
						)
					)

					(else
						(= seconds 1)
					)
				)
			)

			(yepFinishIt
				(= state startCalendar)		;**	Loop back for another day
				(= seconds 1)
			)


			;**	Here are the death sequences:

			(egoSunburn
				(aBigEgo
					view:			vELBSunburn
					cel:			0
					cycleSpeed:	5
					setCycle:	EndLoop self
				)
			)

			(egoSunburn2
				(= seconds 3)
			)

			(egoSunburnDone
				(Print "Second-degree burns from the sun are no \"Laff-ing\"
					matter, Larry!" HIGH)
				(if (== wearingSunscreen WASHED_OFF)
					(Print "(Looks like that sunscreen is not waterproof!)" LOW)
				)
				(= state noWeigh)
				(= seconds 3)
			)

			(egoFried							;**	No wig
				(aFlame
					show:
				)
				(= seconds 5)
			)

			(egoFried2
				(aBigEgo
					view:			vELBSunburn
					cel:			0
					cycleSpeed:	5
					setCycle:	EndLoop self
				)
			)

			(egoFried3
				(= seconds 3)
			)

			(egoFryDone
				(Print "The tropical sun has turned your balded brain into
					an omelette cooked until Sunnyside Soft, Larry!" HIGH)
				(= state noWeigh)
				(= seconds 3)
			)

			(egoSick								;**	Brought spinach dip to the party
				(aBigEgo
					view:			vELBGreen
					cel:			0
					cycleSpeed:	5
					setCycle:	EndLoop self
				)
			)

			(egoSick2
				(= seconds 3)
			)

			(egoSickDone
				(Print "Unfortunately for you, the mayonnaise has spoiled in
					the hot, tropical sun!" HIGH)
				(= state noWeigh)
				(= seconds 3)
			)

			(egoThirsty							;**	Has no soda
				(aBigEgo
					view:			vELBThirst
					cel:			0
					cycleSpeed:	5
					setCycle:	EndLoop self
				)
			)

			(egoThirsty2
				(= seconds 3)
			)

			(egoThirstyDone
				(Print "Perhaps next time you'll pack a little something to
					drink before beginning your trans-oceanic voyage!" HIGH)
				(= state noWeigh)
				(= seconds 3)
			)


			(egoHungry							;**	Has NO food
				(aBigEgo
					view:			vELBHunger
					cel:			0
					cycleSpeed:	5
					setCycle:	EndLoop self
				)
			)

			(egoHungry2
				(= seconds 3)
			)

			(egoHungryDone
				(Print "You've thought of all your needs, except food.
					Come on, you can find something to eat somewhere,
					can't you?" HIGH)
				(= state noWeigh)
				(= seconds 3)
			)


			;**	all the death branches end up here

			(noWeigh
				(= seconds 3)
			)

			(clearHisCalendar
				(calendarScript changeState: eraseCalendar)
				(= seconds 3)
			)

			(UBdying
				(Print "Better luck next cruise, Larry!" HIGH)
				(= currentStatus DEAD)
			)
		)
	)
)


(instance calendarScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(doPage
				(aPage
					posn:			calendarX calendarY
					cel:			0
					setCycle:	CycleTo 2 FORWARD self
					show:
				)
			)

			(fixDate
				(aDate
					setCel:		(+ 1 (aDate cel?))
					forceUpd:
				)
				(aPage
					setCycle:	EndLoop self
				)
			)

			(pageFlying
				(aPage
					setMotion:	MoveTo (+ calendarX 140) (- calendarY 70) self
;**						setMotion:	MoveTo 340 -10 self
				)
			)

			(pageGone
				(aPage 			hide:)
				(rm138Script	cue:)
			)

			(eraseCalendar
				(aDate			dispose:)
				(aPage			dispose:)
				(aCalendar		dispose:)
			)
		)
	)
)


