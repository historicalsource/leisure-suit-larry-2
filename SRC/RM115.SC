;**
;**	Logics for room 115 -- Ethnomusicology Shoppe Interior
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 15, 1988
;**


(script#				115)

(public
	rm115				0
)


(define	nearClerk	61 139 76 157)

;(define	vMusicInt					221)
	(define	lDoor				0)
	(define	lBell				1)

;(define	vMusicClerk					222)
	(define	lStanding		0)
	(define	lBigClerk		1)
	(define	lTalk				2)
	(define	lGetOnklunk		3)
	(define	lOnklunk			4)


(local
	work
	aClerk
	aBell
	aOnklunk
)


(enum												;**	bellScript state values
	bellInit
	bellSlow
	bellSlower
	bellSlowest
	bellDone
)

(enum												;**	rmScript state values
	intro

	myNameIsLarry
	clerkTalks1
	larryTalks1
	whaleBoobs
	clerkTalks2
	larryTalks2
	clerkTalks3
	drawOnklunk
	larryTalks3
	whatHappened
	egoBeLeavin
	egoAtDoor

	kgbInit
	kgbIsYou
	kgbAtCounter
	i'mSorry
	whatWillIDo
	okI'llDoIt
	kgbBeLeavin
	kgbAtDoor
)


(instance rm115 of Room
	(properties
		picture:		115
		horizon:		5
		south:		15
	)

	(method (init)
		(Load VIEW vMusicInt)
		(Load VIEW vMusicClerk)

		(super init:)

		((View new:)							;**	drums
			view: 			vMusicInt
			loop: 			2
			cel: 				3
			posn: 			225 64
			addToPic:
		)
		((View new:)							;**	moreDrums
			view: 			vMusicInt
			loop: 			2
			cel: 				4
			posn: 			267 88
			addToPic:
		)
		((View new:)							;**	stillMoreDrums
			view: 			vMusicInt
			loop: 			2
			cel: 				5
			posn: 			149 46
			addToPic:
		)
		((View new:)							;**	floorDrums
			view: 			vMusicInt
			loop: 			3
			cel: 				4
			posn: 			102 100
			addToPic:
		)
		((View new:)							;**	moreFloorDrums
			view: 			vMusicInt
			loop: 			3
			cel: 				4
			posn: 			120 97
			addToPic:
		)
		((View new:)							;**	andMoreFloorDrums
			view: 			vMusicInt
			loop: 			3
			cel: 				3
			posn: 			134 101
			addToPic:
		)
		((View new:)							;**	andYetMoreFloorDrums
			view: 			vMusicInt
			loop: 			3
			cel: 				3
			posn: 			145 101
			addToPic:
		)
		((View new:)							;**	marshallAmp
			view: 			vMusicInt
			loop: 			2
			cel: 				0
			posn: 			162 102
			addToPic:
		)
		((View new:)							;**	anotherAmp
			view: 			vMusicInt
			loop: 			2
			cel: 				0
			posn: 			186 102
			addToPic:
		)
		((View new:)							;**	andAnotherAmp
			view: 			vMusicInt
			loop: 			2
			cel: 				0
			posn: 			156 110
			addToPic:
		)
		((View new:)							;**	andStillAnotherAmp
			view: 			vMusicInt
			loop: 			2
			cel: 				0
			posn: 			191 110
			addToPic:
		)
		((View new:)							;**	marshallAmp behind chinese gogong
			view: 			vMusicInt
			loop: 			2
			cel: 				0
			posn: 			90 102
			addToPic:
		)
		((View new:)							;**	frenchHorn
			view: 			vMusicInt
			loop: 			3
			cel: 				0
			posn: 			69 152
			setPri: 			15
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	wallInstruments
			view: 			vMusicInt
			loop: 			3
			cel: 				2
			posn: 			23 104
			addToPic:
		)
		((View new:)							;**	andAnother
			view: 			vMusicInt
			loop: 			3
			cel: 				2
			posn: 			0 114
			addToPic:
		)
		((View new:)							;**	moreWallStuff
			view: 			vMusicInt
			loop: 			3
			cel: 				1
			posn: 			31 80
			addToPic:
		)
		((View new:)							;**	andAnother
			view: 			vMusicInt
			loop: 			3
			cel: 				1
			posn: 			41 87
			addToPic:
		)
		((View new:)							;**	chinese instrument on top of amp
			view: 			vMusicInt
			loop: 			2
			cel: 				1
			posn: 			172 78
			setPri: 			6
			addToPic:
		)
		((View new:)							;**chinese guitar in front of leftamp
			view: 			vMusicInt
			loop: 			2
			cel: 				2
			posn: 			89 105
			setPri: 			7
			addToPic:
		)
		((View new:)							;**	musicRack
			view: 			vMusicInt
			loop: 			3
			cel: 				5
			posn: 			209 136
			ignoreActors:
			addToPic:
		)
		((View new:)							;**	herald trumpet
			view: 			vMusicInt
			loop: 			3
			cel: 				7
			posn: 			140 71
			addToPic:
		)
		((View new:)							;**	counterDisplay
			view: 			vMusicInt
			loop: 			3
			cel: 				6
			posn: 			42 119
			setPri: 			13
			ignoreActors:
			addToPic:
		)
		((View new:)
			view:				vMusicInt
			loop:				lDoor
			posn:				67 106
			setPri:			7
			ignoreActors:
			addToPic:
		)

		((= aBell (Prop new:))
			view:				vMusicInt
			setLoop:			lBell
			posn:				63 63
			setCycle:		Forward
			init:
			setScript:		bellScript
		)
		(aBigClerk
			view:				vMusicClerk
			setLoop:			lBigClerk
			posn:				273 1059
			setPri:			15
			setCycle:		Forward
			init:
		)

		((= aClerk (Actor new:))
			view:				vMusicClerk
			setLoop:			0
			setPri:			14
			illegalBits:	0
			posn:				41 153
			stopUpd:
			ignoreActors:
			init:
		)

		(self
			setRegions:		rgLA
			setScript: 		rm115Script
		)

		(if (== currentStatus EGO_IS_HENCH)
			(NormalEgo facingEast)
			(= currentStatus EGO_IS_HENCH)
			(HandsOff)
			(rm115Script changeState: kgbInit)
		else
			(NormalEgo facingEast)
			(Load SOUND mHenchette)

			(theSound init:)
		)
		(ego
			posn: 			64 116
			init:
		)
	)
)


(instance rm115Script of Script
	(method (doit)
		(super doit:)

		(if (& (ego onControl:) cBlue)
			(curRoom newRoom: 15)
		)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent) (event claimed?)) (return))

		(if (Said 'look>')
			(cond
				((Said '/instrument')
					(Print "There are instruments here from many diverse
						ethnic cultures.
						The studios at the rear of the store are filled with
						people plucking their charangos!")
				)

				((Said '/counter')
					(Print "A beautiful Latin lady waits behind the counter,
						watching your every move.")
				)

				((Said '/carpet')
					(Print "At last, a carpet! 
						You thought this whole city was paved.")
				)

				((Said '/door')
					(Print "The door beneath the bell is clearly labeled \"IN\" and
						thus should not be used. However, the door in the south wall
						is labeled: \"OUT.\"")
					(Print "Along the walls are locked practice rooms.")
				)

				((Said '/floor')
					(Print "It's a pretty carpet, isn't it?")
				)

				((Said '/clerk, person, girl')
					(aBigClerk
						posn: 	273 59
						stopUpd:
					)
					(Timer setReal: aBigClerk 5)
					(HandsOff)
				)

				((Said '[/room, store, rack, shelf, stand, music]')
					(Print "\"Ye Olde Ethno-Musicology Shoppe\" is filled with
						unusual instruments from the four corners of the world.")
					(Print "(You wonder which ethnic subculture makes
						such extensive use of electric amplifiers and drum sets!)" LOW)
				)
			)
		)

		(if (Said 'use, take, play/instrument')
			(Print "Perhaps you should talk to the clerk first to see if
				it's all right for you to mess around with the instruments.")
		)

		(if (Said 'buy')
			(cond
				((ego has: iMillion)
					(Print "\"Do you have change for a million?\"
						you ask the clerk.")
					(Print "\"Non habla anglais,\" replies the clerk.")
				)
				(else
					(Print "\"May I buy something here?\" you ask.")
					(Print "\"Non habla anglais,\" replies the clerk.")
				)
			)
		)

		(if (Said 'open/door')
			(Print "The door beneath the bell is clearly labeled \"IN\" and
				thus should not be used. However, the door in the south wall
				is labeled: \"OUT.\"")
			(Print "Along the walls are locked practice rooms.")
		)

		(if (Said 'talk/girl, person, clerk')
			(cond
				((not (ego inRect: nearClerk))
					(NotClose)
				)
				((not ((inventory at: iOnklunk) ownedBy: curRoomNum))
					(AlreadyTook)
				)
				((== LAhaircut FALSE)
					(Print (Format @string
						"\"Hello, beautiful,\" you tell her.
						%s" laffer))
					(Print "\"Buenos dias, Senor.\" she responds
						with a blank stare.")
					(Print "(Apparently she doesn't speak English.)" LOW)
				)
				(else
					(self changeState: myNameIsLarry)
				)
			)
		)
	)


	(method (changeState newState)
		(switch (= state newState)


			;**	This path gets ego his Onker:

			(myNameIsLarry
				(HandsOff)
				(Print (Format @string
					"\"Hi, beautiful,\"
						you attempt to lay a little smooth-mouth
						on the lovely Latin lady.
						%s" laffer))
				(aClerk
					setLoop:			lTalk
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(clerkTalks1
				(aClerk
					setLoop:			lStanding
					setCel:			0
				)
				(Print "\"Buenos dias, Senor
					Dos assistanta bello bono?\" la senorita
					el talkos."
					HIGH font: userFont dispose:)
				(Print "(\"Good day, Sir. How may I help you?\")"
					LOW font: smallFont)
				(cls)
				(= seconds 3)
			)

			(larryTalks1
				(Print "Suddenly, she smiles broadly...
					as if she recognizes you!")
				(Print "Her smile makes you glad you took Spanish in high school.")
				(Print "(Too bad you slept through it!)")
				(= seconds 3)
			)

			(whaleBoobs
				(if (> filthLevel CLEAN)
					(Print "Aw, what the hell.
						You decide to brush the dust off your Spanish
						by attempting a conversation with the lovely senorita.")
				else
					(Print "Aw, what the heck.
						You decide to brush the dust off your Spanish
						by attempting a conversation with the lovely senorita.")
				)
				(Print "Perhaps you could begin by complimenting
					her on her extremely good looks...")
				(Print "\"El pesto la guardia say spermo boobitos,\"
					you tell her."
					HIGH font: userFont dispose:)
				(Print "(\"Your ears remind me of whale breasts.\")"
					LOW font: smallFont)
				(cls)
				(aClerk
					setLoop:			lTalk
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(clerkTalks2
				(aClerk
					setLoop:			lStanding
					setCel:			0
				)
				(Print "Her face brightens further --
					she seems genuinely interested in you.
					Your Spanish must not be so bad after all!")
				(Print "\"Si, sa la luna el gross
					et poopie-doggie la bambino,\" la senorita reporitos."
					HIGH font: userFont dispose:)
				(Print "(\"Yes, but the moon is full, and
					you are a chihuahua.\")"
					LOW font: smallFont)
				(cls)
				(= seconds 3)
			)

			(larryTalks2
				(Print "Whoa!
					Speaking is one thing, but understanding is another!
					Her response baffles you completely.
					You have no idea what she means.")
				(Print "Of course, that's never stopped you before...")
				(Print "\"El pencilo es tubular, rigido 
					amarillo,\" you tell her." HIGH font: userFont dispose:)
				(Print "(\"My pencil is long, hard and yellow.\")"
					LOW font: smallFont)
				(cls)
				(aClerk
					setLoop:			lTalk
					setCycle:		Forward
				)
				(= seconds 3)
			)

			(clerkTalks3
				(aClerk
					setLoop:			lStanding
					setCel:			0
				)
				(Print "\"El hombre del na nostra
					donde esta majora signale.
					La writo de stupido sententates,\" la senorita charma."
					HIGH font: userFont dispose:)
				(Print "(\"So you're finally here!
					I thought you'd never show up.
					By the way, who writes these secret phrases, anyway?\")"
					LOW font: smallFont)
				(cls)
				(Print "\"La microfiche la enigm
					big dealo onklunk no touche, Dr. Nonookee el commie.\""
					HIGH font: userFont dispose:)
				(Print "(\"The microfiche containing the secrets to the recent
					United States' superconductor research breakthrough
					is hidden inside this rare, Peruvian onklunk.
					Don't let anyone touch it before you personally deliver it to
					Dr. Nonookee.
					And (I don't need to tell you) be careful:
					he, and he alone, has the authority to deliver it to the
					USSR agents!\")"
					at: 20 130 width: 270 font: smallFont)
				(cls)
				(aClerk
					setLoop:			lGetOnklunk
					setCel:			0
					cycleSpeed:		1
					setCycle:		EndLoop self
				)
			)

			(drawOnklunk
				(aClerk
					setLoop:			lStanding
					cycleSpeed:		0
					setCel:			0
				)
				((= aOnklunk (View new:))
					view:				vMusicClerk
					setLoop:			lOnklunk
					setPri:			14
					posn:				51 133
					ignoreActors:
					stopUpd:
					init:
				)
				(= seconds 3)
			)

			(larryTalks3
				(Print "\"Mucho gracias, la Senorita,\
					say el gringo."
					HIGH font: userFont dispose:)
				(Print "(\"Thanks, I guess.\")"
					LOW font: smallFont)
				(cls)
				(= seconds 3)
			)

			(whatHappened
				(Print "As best you can tell,
					she wants you to keep that unusual musical
					instrument on the counter.
					So, rather than risk offending her generosity, you take it.")
				(ego get: iOnklunk)
				(theGame changeScore: 7)
				(aOnklunk dispose:)
				(= seconds 3)
			)

			(egoBeLeavin
				(Print "\"Have zeee nice dayo.\""
					HIGH font: userFont dispose:)
				(Print (Format @string
					"(\"%s\")" tritePhrase)
					LOW font: smallFont)
				(cls)
				(ego setMotion:	MoveTo 220 166 self)
			)

			(egoAtDoor
				(ego setMotion:	MoveTo 222 182)	;**	He'll leave via cBlue
			)


			;**	This path brings in the KGB ego, to discover that Larry
			;**	has already absconded with the goods!

			(kgbInit
				(ego
					setMotion:		MoveTo 73 141 self
				)
				(= cycles 6)
			)

			(kgbIsYou
				(Print "\"You! What are you doing back here?!\" says the clerk,
					skipping the foreign language subtitles completely.
					\"You'd better get on with the mission before
					Dr. Nonookee prevents you from ever missioning again!\"")
			)

			(kgbAtCounter
				(ego	setLoop:		facingWest)
				(Print "\"What are you talking about?
					I've never seen you before in my entire life!\"
					says the dorky-looking guy,
					\"Besides, you're not supposed to talk to me like that
					before we exchange our cute, little, secret phrases!\"" draw:)
				(Print "\"What do you mean,\" she responds,
					\"you just walked out that other door over there!\"")
				(Print "\"Not me!\" he exclaims.
					\"Hey!
					Wait a minute!
					Don't tell me you gave the goods to someone else?\"")
				(theSound play:)
				(Print "\"I, eh, uh,\" she stammers,
					\"I'm not sure, that is, there may be a...
					I may have...\"")
				(Print "Her voice trembles with fear!")
				(= seconds 3)
			)

			(i'mSorry
				(Print "\"I'm sorry, he fit your description perfectly!
					And he said the secret code phrases as well!!
					Although, come to think of it,
					he DID have a terrible accent.
					I thought the bad accent might
					have been part of your disguise.\"")
				(= seconds 3)
			)

			(whatWillIDo
				(Print "\"What will I do?\" she continues,
					\"Dr. Nonookee doesn't tolerate mistakes!\"")
				(Print "Her eyes flash with an idea,
					\"Of course,
					if you promise to help me,
					I won't tell him how you lost the microfiche!\"")
				(Print "\"Why, you...\" he threatens,
					\"if you know what's good for you,
					you'll shut down this joint,
					contact Nontoonyt Island,
					and get the troops started finding this guy.
					They'll guarantee he tells no tales about your little
					indiscretion!
					If you can retrieve the microfiche before he discovers
					what he has,
					Dr. Nonookee might even allow you to live a few extra days!\"")
				(= seconds 3)
			)

			(okI'llDoIt
				(Print "She deliberates just a moment
					before making her decision.")
				(Print "\"You're right, of course.
					You see if you can follow him,
					while I contact the island by radiotelephone.\"")
				(= seconds 2)
			)

			(kgbBeLeavin
				(ego
					setLoop:		RELEASE
					setMotion:	MoveTo 220 166 self)
			)

			(kgbAtDoor
				(ego
					loop:			facingSouth
					setMotion:	FALSE
				)
				(Print "Well, Larry, this is certainly another
					fine mess you've gotten us into!" draw:)
				(Print "Not only is the KGB after you for grabbing
					what was supposed to be theirs,
					but Dr. Nonookee's beautiful army of henchettes,
					game show hostesses, church secretaries, and bimbos
					is now hot on your tail!")
				(Print "Since we've now learned the worst,
					we'll rejoin you as you wander through Los Angeles...")
				(SetRgTimer CRUISE_DEPARTURE 4 30)
				(curRoom newRoom: 21)
			)
		)
	)
)


(instance bellScript of Script
	(method (changeState newState)
		(switch (= state newState)

			(bellInit
;**	(sBell play:)
				(= cycles 5)
			)

			(bellSlow
				(aBell cycleSpeed: 1)
				(= cycles 7)
			)

			(bellSlower
				(aBell cycleSpeed: 2)
				(= cycles 9)
			)

			(bellSlowest
				(aBell cycleSpeed: 3)
				(= cycles 11)
			)

			(bellDone
				(aBell
					setCel: 2
					stopUpd:
				)
			)
		)
	)
)


(instance aBigClerk of Prop
	(method (cue)
		(Print "You find Latin American women sexy.")
		(Print "(But then, you find any woman sexy!)" at: 55 155 width: 210)
		(self posn: 273 1059)
		(HandsOn)
	)
)

(instance theSound of Sound
	(properties
	 	number:	mHenchette
		loop:		1
	)
)


