;**
;**	Script 1 -- Menu suite; outputs to script.997 (not 001 as supposed)
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 27, 1988
;**


;**	Break lines in "AddMenu" before a divider, for aesthetics
;**	= sets a menu item's starting value
;**	! makes the item non-selectable
;**	` denotes the following character is the key to press
;**	: separates menu items within a menu stack


(script# MENU)									;**	output to script.997


(include "menu.sh")

(local
	oldPause
)

(class TheMenuBar kindof MenuBar			;**	MENUBAR
	(method (init)
		(AddMenu { \01 }
			{About LSL2`^a:Help`#1}
		)

		(AddMenu { File }
			{Save Game`#5:Restore Game`#7:--!
			:Restart Game`#9:Quit`^q}
		)

		(AddMenu { Action }
			{Pause Game`^p:Inventory`^I:Retype`#3:--!
			:Boss Key`^b:Filth Level`^f:Trite Phrase`^t}
		)

		(AddMenu { Speed }
			{Change...`^s:--!:Faster`+:Normal`=:Slower`-}
		)

		(AddMenu { Sound }
			{Volume...`^v:Turn Off`#2=1}
		)

;**			(if debugging
;**			(AddMenu { Cheat }
;**				{Room num`@r:make Note`@n:Time left`@t:Input`@i:--!
;**				:Visual`@v:Priority`@p:Control`@c:--!
;**				TglDebug`@d:Memory`^h:QuickQuit\05`@z}
;**			)
;**	
;**			(AddMenu { Debug }
;**				{Ego view`@e:Game`^g:Cast`^c:Ego`^e:Add Grid`@o:WriteEgo`@w}
;**			)


		(SetMenu soundI p_text: 
			(if (DoSound SoundOn) 
				{Turn Off} 
			else 
				{Turn On}
			)
		)

		(SetMenu saveI			p_said: 'save[/game]')
		(SetMenu restoreI		p_said: 'restore[/game]')
		(SetMenu restartI		p_said: 'restart[/game]')
		(SetMenu quitI			p_said: 'quit[/game]')
		(SetMenu	pauseI		p_said: 'pause[/game]')
		(SetMenu invI			p_said: 'inventory')
	)


	(method (handleEvent event &tmp msg i [string2 100])
		(switch (= msg (super handleEvent: event))


			;**************		SIERRA MENU		**************

			(aboutI
				(= oldPause (Sound pause:TRUE))
				(Print
					"\"Leisure Suit Larry goes\n
						Looking for Love\n
						(in Several Wrong Places)\"\n
						\n
						Created, Designed,\n
						Written and Programmed by\n
						Al Lowe\n
						\n
						Graphics by\n
						Bill Skirvin,\n
						Bonnie Borucki\n
						and Douglas Herring
						"
					font:		smallFont
					mode:		teJustCenter
					title:	{An Al Lowe Production}
					icon:		vAuthors 0 0
				)
				(Print 
					(Format @string
						"\n
						Sierra's Creative Interpreter\n
						developed by Jeff Stephenson, Bob Heitman,\n
						Pablo Ghenis, and Stuart Goldstein\n
						\n
						Graphics Systems by Bob Heitman\n
						\n
						Music composed and performed by\n
						Al Lowe\n
						\n
						Version %s, 
						October 27, 1988
						\n
						\n
						"
						version
					)
					font:		smallFont
					mode:		teJustCenter
					at:		-1 50 
					width:	234
				)
				(Print 
					(Format @string
						"You've been playing for %d hours,\n
						%d minutes and %d seconds."
						gameHours gameMinutes gameSeconds
					)
					font:		smallFont
					mode:		teJustCenter
				)
				(Sound pause:oldPause)
			)

			(helpI
				(= oldPause (Sound pause:TRUE))
				(Print	"
					_____DURING THE GAME:\n
					Click at the top of the screen or
					press ESC to use the menus.
					Additional shortcuts are shown there.\n\n

					_____IN TYPING WINDOWS:\n
					Arrows, Home and End move the cursor,
					or click anywhere with the mouse.
					Ctrl-C clears the line.\n\n

					_____IN DIALOG WINDOWS:\n
					Enter selects the outlined item, or
					click on items with the mouse.
					Tab and Shift-Tab move between choices.
					ESC always cancels.
					"
						font:		smallFont
						WIDE
				)

				(Sound pause:oldPause)
			)


			;**************		FILE MENU		**************

			(saveI
				(theGame save:)
			)

			(restoreI
				(theGame restore:)
			)

			(restartI
				(= oldPause (Sound pause:TRUE))
				(if (Print 
					"You mean you\n
					want to start\n
					over from the\n
					very beginning?"
						title:	{Restart}
						icon:		vBEdismay 0 0
						font:		bigFont
						button:	{_Oops_} 	0
						button:	{Restart} 	1
					)
					(theGame restart:)
				)
				(Sound pause:oldPause)
			)

			(quitI
				(= oldPause (Sound pause:TRUE))
				(= quit (Print 
					"Are you just\n
					going to quit\n
					and leave me\n
					here all alone\n
					like this?"
						title:	{Quit}
						icon:		vBEchagrin 0 0
						font:		bigFont
						button:	{Quit} 1
						button:	{Oops} 0
					)
				)
				(Sound pause:oldPause)
			)


			;**************		ACTION MENU		**************

			(pauseI
				(= oldPause (Sound pause:TRUE))
				(Print 
					"Sure, you go ahead.\n
					I'll just wait in here\n
					until you get back..."
						title:	{This game is paused.}
						icon: 	vEgoPause 0 0
						font:		bigFont
						button:	{Ok. I'm back.} 1
				)
				(Sound pause:oldPause)
			)

			(invI
			 	(if (not (HaveMem 1024))
					(Print "Inventory cannot be displayed at this time.")
				else
					(inventory showSelf:	ego)
				)
			)

			(repeatI
				(event message: (User echo?))
				(User getInput: event)
			)

			(bossI
				(curRoom newRoom: 9)
			)

			(filthI
				(if ( < (MemoryInfo FreeHeap) 2048)
					(NotNow)
				else
					(= filthLevel ((Gauge new:)
							description:{Use the mouse or the left and right arrow keys.}
							text:		{Filth-O-Meter}
							higher:	{Dirtier}
							lower:	{Cleaner}
							doit:		filthLevel
						)
					)
					(cond
						((<= filthLevel CLEAN)
							(Print "Thank you, I hate it when you talk dirty!")
						)
						(( > filthLevel 14)
							(Print "Why you dirty little bastard!")
						)
						(else
							(Print "You're a dirty little som'bitch, ain'cha?")
						)
					)
					(DisposeScript GAUGE)
				)
			)

			(triteI
				(GetInput (Format @string tritePhrase) 38
					{Enter your favorite trite phrase:})
				(if ( > (StrLen @string) 4)
					(Format tritePhrase @string)
				)
			)


			;**************		SPEED MENU		**************

			(speedI
				(if (not (HaveMem 2048))
					(Print "Speed cannot be changed at this time.")
				else
					(= i
						((Gauge new:)
							description:{Use the mouse or the left and right arrow keys
										to select the speed of moving characters.}
							text:		{Animation Speed}
							normal:	10				;**	that's really (- 16 10)
							higher:	{Faster}
							lower:	{Slower}
							doit:		(- 16 speed)
						)
					)
					(theGame setSpeed:(- 16 i))
					(DisposeScript GAUGE)
				)
			)

			(fasterI
				(if (> speed (^ 1 (= i debugging))) ;This lets Al (only) haul ass!
					(theGame setSpeed: (-- speed))
				)
			)

			(normalI
				(theGame setSpeed: 6)
			)

			(slowerI
				(theGame setSpeed: (++ speed))
			)


			;**************		SOUND MENU		**************

			(volumeI
				(if (not (HaveMem 2048))
					(Print "Volume cannot be changed at this time.")
				else
					(= i
						((Gauge new:)
							description:{Use the mouse or the left and right arrow keys.}
							text:		{Sound Volume}
							normal:	15
							higher:	{Louder}
							lower:	{Softer}
							doit:		(DoSound ChangeVolume)
						)
					)
					(DoSound ChangeVolume i)
					(DoSound PauseSound oldPause)
					(DisposeScript GAUGE)
				)
			)

			(soundI
				(= i (DoSound SoundOn))
				(DoSound SoundOn (not i))	;**	Backwards (and proud of it)
				(if i
					(SetMenu soundI p_text:{Turn On})
					(Print "Sound is off.")
				else
					(SetMenu soundI p_text:{Turn Off})
					(Print "Sound is on.")
				)
			)

		
			(else
				(if debugMenu
					(debugMenu doit: msg)
				)
			)

		)
	)
)

