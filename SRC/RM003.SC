;**
;**	Script 3 -- DOORS
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 12, 1988
;**

;**	Al's first class.	Handles regular & automatic doors.


(script#				DOORS)


;**	Door states (from "game.sh")
;**	(enum
;**		doorClosed
;**		doorOpening
;**		doorOpen
;**		doorClosing
;**	)


(class Door of Prop
	(properties
		cycleSpeed		1
		entranceTo 		-1
		locked 			FALSE
		openSnd 			0
		closeSnd 		0
		doorState 		doorClosed
		doorCtrl 		cBlue
		doorBlock		cYellow
		roomCtrl			cGreen
		code 				0
		illegalBits 	0
		force				0
		notify			0
		msgLook			"Yep; it's a door."
		msgLookLock		"And... it's locked."
		msgLocked		"This door is locked."
		msgExcept		"...except it's locked!"
		msgFunny			"Funny. No response."
		msgCloser		"Just walk near it."
	)


	(methods
		open
		close
	)


	(method (init)
		(if (== prevRoomNum entranceTo)
			(= doorState doorOpen)
		)

		(if (== doorState doorClosed)
			(= cel 0)
			(ego observeControl: doorBlock)
		else
			(= cel (- (NumCels self) 1))
			(= locked FALSE)
			(ego ignoreControl: doorBlock)
		)

		(super init:)
		(self
			stopUpd:
			ignoreActors:
		)
	)


	(method (doit)
		(super doit:)
		(if (and ( > roomCtrl 0)
					(& (ego onControl:) roomCtrl))
			(curRoom newRoom: entranceTo)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) 
					(== curRoomNum 33)		;**	pretty fucking object oriented!!
					(== curRoomNum 15)		;**	pretty fucking object oriented!!
					(event claimed?)) (return))
		(cond
			((Said 'open/door')
				(self open:)
			)

			((Said 'close/door')
				(self close:)
			)

			((Said 'look/door')
				(Print msgLook)
				(if locked
					(Print msgLookLock)
				)
			)

			((Said 'knock/door')
				(Print msgFunny)
			)
		)
	)


	(method (open)
		(cond
			((and	(not force)
					(!= (ego onControl: origin) doorCtrl))
				(NotClose)
			)

			(locked
				(Print msgLocked)
			)

			((or	(== doorState doorOpening)
					(== doorState doorOpen))
				(ItIs)
			)

			(else
				(= doorState doorOpening)
				(self setCycle: EndLoop self)
				(if openSnd
					(openSnd doit:)
				)
			)
		)
	)


	(method (close)
		(cond
			((and (not force)
					(!= (ego onControl: origin) doorCtrl))
				(NotClose)
			)

			(locked
				(Print msgLocked)
			)

			((or	(== doorState doorClosing)
					(== doorState doorClosed))
				(ItIs)
			)

			((& (ego onControl:) doorBlock)
				(if ( > filthLevel CLEAN)
					(Print "Get your fat ass out of the doorway first.")
				else
					(Print "Please move out of the doorway first.")
				)
			)

			(else
				(= doorState doorClosing)
				(self setCycle: BegLoop self)
				(if closeSnd
					(closeSnd doit:)
				)
			)
		)
	)


	(method (cue)
		(= doorState
			(if (== doorState doorClosing)
				doorClosed
			else
				doorOpen
			)
		)

		(if (== doorState doorOpen)
			(ego ignoreControl: doorBlock)
		else
			(ego observeControl: doorBlock)
		)

		(self stopUpd:)
		(if notify
			(notify cue:)
			(= notify 0)
		)
	)
)


(class AutoDoor of Door						;**	properties same as regular doors
	(methods
		open
		close
	)

	(method (init)
		(super init:)
	)

	(method (doit)
		(super doit:)

		(if code
			(if (code doit: self)
				(self open:)
			else
				(self close:)
			)
		else
			(if (& (ego onControl:) doorCtrl)
				(self open:)
			else
				(self close:)
			)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))
		(cond
			((Said 'open/door')
				(Print msgCloser)
				(if locked
					(Print msgExcept)
				)
			)

			((Said 'close/door')
				(Print msgCloser)
				(if locked
					(Print msgExcept)
				)
			)

			((Said 'look/door')
				(Print msgLook)
				(if locked
					(Print msgLookLock)
				)
			)

			((Said 'knock/door')
				(Print msgFunny)
			)
		)
	)


	(method (open)
		(if (and
				(not locked)
				(!= doorState doorOpening)
				(!= doorState doorOpen))
			(= doorState doorOpening)
			(self setCycle: EndLoop self)
			(if openSnd
				(openSnd doit:)
			)
		)
	)


	(method (close)
		(if (and
				(!= doorState doorClosing)
				(!= doorState doorClosed))
			(= doorState doorClosing)
			(self setCycle: BegLoop self)
			(if closeSnd
				(closeSnd doit:)
			)
		)
	)
)

