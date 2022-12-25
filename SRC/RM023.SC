;**
;**	Logics for room 23 -- Eve's House
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 23, 1988
;**


(script#				23)

(public
	rm23				0
)

(define	nearPassport	81 148 144 172)

;(define	vGarage						253)


(local
	work
	askedAlready
	trashHere
	garageClosed
)


(instance rm23 of Room
	(properties
		picture:		23
		horizon:		107
		north:		19
		east:			24
	)

	(method (init)
		(Load VIEW vGarage)

		(if (== prevRoomNum 99)
			(self style: IRISOUT)
		)
		(super init:)

		(if (not ((inventory at: iDollar) ownedBy: curRoomNum))
			(= garageClosed TRUE)
			((View new:)						;**	right garage door is down
				view: 			vGarage
				loop: 			0
				cel: 				1
				posn: 			93 136
				setPri: 			9
				addToPic:
			)
			(ego
				observeControl:cYellow
			)
		else
			((View new:)						;**	garage inside wall
				view: 			vGarage
				loop: 			0
				cel: 				0
				posn: 			95 131
				setPri: 			1
				ignoreActors:
				addToPic:
			)
		)

		((View new:)							;**	flowerPot1 on right
			view: 			vGarage
			loop: 			0
			cel: 				2
			posn: 			136 140
			setPri: 			9
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	flowerPot2
			view: 			vGarage
			loop: 			0
			cel: 				2
			posn: 			49 139
			setPri: 			9
			addToPic:
		)
		((View new:)							;**	flowerPot3
			view: 			vGarage
			loop: 			0
			cel: 				2
			posn: 			35 144
			setPri: 			9
			addToPic:
		)
		((View new:)							;**	streetSign
			view: 			vGarage
			loop: 			0
			cel: 				3
			posn: 			149 111
			setPri: 			13
			addToPic:
		)
		((View new:)							;**	left garage door is always down
			view: 			vGarage
			loop: 			0
			cel: 				1
			posn: 			15 136
			setPri: 			1
			addToPic:
		)

		(if (and	garageClosed
					(ego has: iCruiseTicket)
					((inventory at: iPassport) ownedBy: curRoomNum))
			(= trashHere TRUE)
			((View new:)						;**	1st trash can
				view: 			vGarage
				loop: 			1
				cel: 				0
				posn: 			101 168
				setPri: 			12
				addToPic:
			)
			((View new:)						;**	2nd trash can
				view: 			vGarage
				loop: 			1
				cel: 				1
				posn: 			122 168
				setPri: 			12
				brRight:			128
				addToPic:
			)
		)

		(if (== prevRoomNum 19)
			(ego posn: 299 111)
		else
			(if (!= prevRoomNum 24)
				(TheMenuBar draw:)
				(StatusLine enable:)
				(ego
					loop:			facingWest
					posn:			135 152
				)
			)
		)
		(NormalEgo)
		(ego					init:)

		(self
			setRegions:		rgLA
			setScript:		rm23Script
		)
	)
)


(instance rm23Script of Script
	(method (doit)
		(super doit:)

		(if (or	(& (ego onControl:) cBlue)
					(& (ego onControl:) cCyan))
			(ego setPri: 2)
		else
			(ego setPri: RELEASE)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said '(look<in), search/pocket, pants')
			(cond
				((not ((inventory at: iDollar) ownedBy: curRoomNum))
					(AlreadyTook)
				)
				((& (ego onControl:) cBlue)
					(Ok)
					(Print "You carefully go through Eve's pants and
						find nothing but a crumpled-up dollar bill.")
				)
				(else
					(event claimed: FALSE)	;**	give to rm0
				)
			)
		)

		(if (Said 'look/pants')
			(cond
				((not ((inventory at: iDollar) ownedBy: curRoomNum))
					(Print "Except for that dollar bill,
						her pants contain nothing to hold your interest.")
					(if ( > filthLevel CLEAN)
						(Print "(At least, now that she's not in them!)" LOW)
					)
				)
				((not (& (ego onControl:) cBlue))
					(NotClose)
				)
				(else
					(Ok)
					(Print "You carefully go through Eve's pants and
						find nothing but a crumpled-up dollar bill.")
				)
			)
		)

		(if (Said 'get/dollar, bill, (bill<dollar)')
			(cond
				((not ((inventory at: iDollar) ownedBy: curRoomNum))
					(AlreadyTook)
				)
				((not (& (ego onControl:) cBlue))
					(NotClose)
				)
				(else
					(Ok)
					(Print "You briefly consider the morality of this move,
						but after all she hasn't done for you, 
						you decide it's probably the least she can do!")
					(ego get: iDollar)
					(theGame changeScore: 3)
				)
			)
		)

		(if (Said 'get/pants')
			(Print "It's not her pants that you want!")
		)

		(if (or	(Said 'search, look, go/garbage, can, (barrel<garbage)')
					(and	askedAlready
							(Said 'yes, me')))
			(cond
				((not trashHere)
					(Print "Where?!")
				)
				((not (ego inRect: nearPassport))
					(NotClose)
				)
				(((inventory at: iPassport) ownedBy: curRoomNum)
					(if (not askedAlready)
						(= askedAlready TRUE)
						(Print "Who'd want to search through Eve's can?")
					else
						(Print "Ok, ok!")
						(Print "Searching through the trash until your
							suit turns off-white, you discover your passport
							stuffed between the pages of an old \"Cosmo.\"")
					)
				)
				(else
					(Print "You find nothing else of interest.")
				)
			)
		)

		(if (Said '/balcony')
			(Print "What is this -- \"Romeo and Juliet?!\"")
		)

		(if (Said 'enter, open, (go<into)/house')
			(Print "You'd better not. She said she wanted you out of here!")
		)

		(if (Said 'get/(bench<work), magazine, trash, can, mower, shelf, 
																bench, table')
			(Print "That will not help you.")
		)

		(if (Said 'break/window')
			(Print "Larry!")
			(Print "(Really!)" LOW)
		)

		(if (Said 'shut, open, close, unlock, lock/door')
			(Print "There is no need to fool around with Eve's garage door.")
		)

		(if (Said '/flower')
			(Print "They smell lovely, but reveal nothing.")
		)

		(if (Said 'look, (look<in), (look<through), open>')
			(if (and trashHere
						(Said '/trash, can, (barrel<garbage)'))
				(Print "They are open.")
			)

			(if (Said '/window')
				(Print "Eve keeps her windows locked.")
			)

			(if (Said '/door<garage')
				(Print "The garage door is locked.")
			)

			(if (Said '/door')
				(Print "There are no doors here except the garage door.")
			)
		)

		(if (Said 'search, look>')
			(if (Said '/pole, sign')
				(Print "Eve lives at the corner of Ascot Place and Ball Road.")
			)

			(if (Said '/(bench<work), mower, shelf, bench, table')
				(Print "You see nothing that will help you.")
			)

			(if (Said '/grass, ground')
				(Print "Part of the lawn looks freshly mowed!")
			)

			(if (Said '/calendar')
				(Print "You love auto parts calendars.")
			)

			(if (Said '[/room, floor, house, garage]')
				(cond
					((& (ego onControl:) cCyan)
						(Print "You search carefully,
							but find nothing in this part of the garage.")
					)
					((not (& (ego onControl:) cBlue))
						(Print "You were so sure this would be the location of
							your future, happy life with Eve.
							But, some things are not to be.")
					)
					(((inventory at: iDollar) ownedBy: curRoomNum)
						(Print "Hey! What's this?
							Eve left a dollar bill stuffed in this old pair of
							pants!")
					)
					(else
						(Print "You carefully search the garage
							and find nothing but junk, which is of no use to you.")
					)
				)
			)
		)

		(if (Said 'get/passport')
			(cond
				((not trashHere)
					(Print "Where?!")
				)
				((not (ego inRect: nearPassport))
					(NotClose)
				)
				(((inventory at: iPassport) ownedBy: curRoomNum)
					(Ok)
					(ego get: iPassport)
					(theGame changeScore: 5)
					(Print "You now smell like a combination of
						cheap department store credit card bills
						and road kill!")
				)
				(else
					(AlreadyTook)
				)
			)
		)
	)
)
