;**
;**	Logics for room 5 -- an old-fashioned debugger
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 13, 1988
;**

;**	Add this code to rm0's newRoom, just below the (super newRoom: n):
;**			(if debugging
;**				(curRoom setLocales: DEBUG)
;**			)
;**	

;**	Add this code to rm0's Said event handler:
;**			(if (Said 'debug/larry') ;or your favorite shipable password
;**				(^= debugging TRUE)
;**				(if debugging
;**					(Print "Ok. Now change rooms.")
;**				else
;**					(Print "Bye, bye.")
;**				)
;**			)


(script# 	DEBUG)							;**	where DEGUB is 5

(public
	rm5				0
)

(define	ALT_I		 5888)
(define	ALT_Z		11264)
(define	ALT_C		11776)
(define	ALT_F		 8448)
(define	ALT_M		12800)
(define	ALT_P		 6400)
(define	ALT_R		 4864)
(define	ALT_V		12032)


(instance rm5 of Locale
	(method (handleEvent event &tmp i theObj xyWindow evt fd [string2 50])
		(if (and debugging
					(not (event claimed?))
					(== keyDown (event type?)))
			(switch (event message?)
				(ALT_I
					(User canInput: TRUE)
				)
				(ALT_Z
					(= quit 1)
				)
				(ALT_C
					(Show CMAP)
					(Print "" at: 0 0)
					(Show VMAP)
				)
				(ALT_F
					(Print "Debug will pop up on next newRoom.")
					(^= debugOn TRUE)
				)
				(ALT_M
					(theGame showMem:)
				)
				(ALT_P
					(Show PMAP)
				)
				(ALT_R
					(Print (Format @string "This is room %d." curRoomNum))
				)
				(ALT_V
					(Show VMAP)
				)
			)
		)


		(if (and debugging
					(not (event claimed?))
					(== mouseDown (event type?)))
			(cond
				((& (event modifiers?) ctrlDown)
					(event claimed: TRUE)
					(User canControl:	TRUE)
					(while (!= mouseUp ((= evt (Event new:)) type?))
						(GlobalToLocal evt)
						(ego posn: (evt x?) (evt y?) setMotion: 0)
						(RedrawCast)
						(evt dispose:)
					)
					(evt dispose:)
				)
				((& (event modifiers?) shiftDown)
					(event claimed:TRUE)
					(=	xyWindow (Print (Format 
						@string "%d/%d"	(event x?) (event	y?))
						at: 150 100
						font:999
						dispose:
						)
					)
					(while (!= mouseUp ((= evt	(Event new:)) type?))
						(evt dispose:)
					)
					(xyWindow dispose:)
					(evt dispose:)
				)
			)
			(if (event claimed?)
				(return TRUE)
			)
		)


		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))
	
		(if (Said 'pitch>')
			(if (= theObj (inventory saidMe:))
				(event claimed: TRUE)
				(if (not (theObj ownedBy: ego))
					(Print "You already pitched it.")
				else
					(Print "Ok, Al.")
					(theObj moveTo: LIMBO)
				)
			)
		)

		(if (Said 'get>')
			(if (= theObj (inventory saidMe:))
				(event claimed: TRUE)
				(if (theObj ownedBy: ego)
					(Print "You already have it, Al.")
				else
					(Print "Ok, Al.")
					(theObj moveTo: ego)
				)
			)
		)

		(if (Said 'help')
			(Print "You can now...\n
				tp\n
				get (name of object)\n
				pitch (name of object)\n
				make note\n
				show timer\n
				show ego\n
				show grid\n
				\n
				Alt-C show control\n
				Alt-F show fragmenting\n
				Alt-I regain typing\n
				Alt-M show memory\n
				Alt-P show priority\n
				Alt-R show room number\n
				Alt-V show normal\n
				Alt-Z quits\n
				Control-click moves ego\n
				Shift-click shows mouse location\n
			"
			font: 999)
		)

		(if (Said 'tp')
			(regions eachElementDo: #keep: FALSE)
			(NormalEgo)
			(curRoom newRoom: (GetNumber {Teleport to:}))
		)

		(if (Said 'show/memory')
			(theGame showMem:)
		)

		(if (Said 'show/frag')
			(Print "Debug will pop up on next newRoom.")
			(^= debugOn TRUE)
		)

		(if (Said 'room/number')
			(Print (Format @string "This is room %d." curRoomNum))
		)

		(if (Said 'make/note')
			(= string	0)
			(= string2	0)
			(if (GetInput @string2 50 {Writing to \"note.log\"})
				(Format @string
					"[r%3d v%3d %3dx/%3dy CS%-5d] Note "
					curRoomNum (ego view?) (ego x?) (ego y?) currentStatus
				)
				(File name: "note.log" write: @string @string2 "\r" close:)
			)
		)

		(if (Said 'show/timer')
			(Print (Format @string "%2d:%2d" (/ rgTimer 600) (/ (mod rgTimer 600) 10)))
		)

		(if (Said 'show/priority')
			(Show PMAP)
		)

		(if (Said 'show/control')
			(Show CMAP)
			(Animate (cast elements?))
			(while (== 0 ((= event (Event new:(- allEvents mouseUp))) type?))
				(event dispose:))
			(event dispose:)
			(Show VMAP)
		)

		(if (Said 'show/normal')
			(Show VMAP)
		)

		(if (Said 'show/ego')
			(Print (Format @string {view: %d loop: %d cel: %d posn: %d %d
				pri: %d OnControl: $%x Origin on: $%x}
				(ego view?) (ego loop?) (ego cel?) (ego x?) (ego y?)
				(ego priority?) (ego onControl:) (ego onControl: origin))
				icon: (ego view?) (ego loop?) (ego cel?))
		)

		(if (Said 'show/grid')
			(if (Load PICTURE 999)
				(DrawPic 999 1 1)
			)
		)

;**			(if (Said 'write/ego')
;**				(= string	0)
;**				(= string2	0)
;**				(if (GetInput @string 50 {Writing to \"ego.log\"})
;**					(Format @string2
;**						"(ego room: %d view: %d loop: %d cel: %d setPri: %d
;**						posn: %d %d)\r"
;**						curRoomNum (ego view?) (ego loop?) (ego cel?) (ego priority?)
;**						(ego x?) (ego y?)
;**					)
;**					(File name: "ego.log" write: @string @string2 "\r" close:)
;**				)
;**			)
	)
)



			;**************		DEBUG MENU		**************

;**				(clothingI
;**					(= currentEgoView (GetNumber {New Ego View #:}))
;**					(NormalEgo)
;**				)
;**	
;**				(gameI
;**					(theGame showSelf:)
;**				)
;**	
;**				(castI
;**					(cast showSelf:)
;**				)
;**	
;**				(egoI
;**					(Print (Format @string {view: %d loop: %d cel: %d posn: %d %d
;**						pri: %d OnControl: $%x Origin on: $%x}
;**						(ego view?) (ego loop?) (ego cel?) (ego x?) (ego y?)
;**						(ego priority?) (ego onControl:) (ego onControl: origin))
;**						icon: (ego view?) (ego loop?) (ego cel?))
;**				)
;**	
;**				(gridI
;**					(if (Load PICTURE 999)
;**						(DrawPic 999 1 1)
;**					)
;**				)
;**	
;**				(writeEgoI
;**					(= string	0)
;**					(= string2	0)
;**					(if (GetInput @string 50 {Writing to \"ego.log\"})
;**						(Format @string2
;**							"(ego room: %d view: %d loop: %d cel: %d setPri: %d
;**							posn: %d %d)\r"
;**							curRoomNum (ego view?) (ego loop?) (ego cel?) (ego priority?)
;**							(ego x?) (ego y?)
;**						)
;**						(File name: "ego.log" write: @string @string2 "\r" close:)
;**					)
;**				)




