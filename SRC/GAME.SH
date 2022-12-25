;**
;**	Game Defines
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 22, 1988
;**


(include views.sh)



;**	currentStatus constants
(enum								  				;**	These are ok to change rooms:
	NORMAL
	WALKING										;**	kinda "nearly normal"
	NOT_SEEN										;**	not on screen

	EGO_IS_HENCH								;**	rm15/115
	DEPARTING									;**	rm27/28
	LANDSCAPE									;**	rm40
	MAZING										;**	rm48 the resort cliff maze
	BOMBING										;**	rm52
	RIDING_SIDEWALK							;**	rm55/56/57
	GOING_TO_SEAT								;**	rm61/62
	PARACHUTING									;**	rm64/65
	INITIATION									;**	rm75,76,77,79
	FALLING										;**	rm71
	MAKING_BOMB									;**	rm82
	WICK_BURNED_DOWN							;**	rm82
	NO_WICK										;**	rm82
	ERUPTION										;**	rm82/83
	MATCHES										;**	rm0
	IN_STUDIO									;**	rm103/104
	DIVING										;**	rm134
	HANGING_IN_TREE							;**	rm70
	LIFEBOATING									;**	rm138
	CLOSING_CARTOON
	HEADING_FOR_DEATH_SCENE
)

(enum 1000										;**	BAD NEWS:  no room change allowed
	NEAR_DEATH									;**	Local rm tells rm0 he screwed up.
	DEAD											;**	He's dead, but doesn't know it.
	GAMEOVER										;**	Now, even he knows it's over.

	PISSING										;**	rm13
	COLLAPSING									;**	rm22, drinks KGB wine
	SLEEPING										;**	rm31/44
	SWIMMING										;**	rm34
	SWAM_IN_SUIT								;**	rm34
	DROWNING										;**	rm34
	SITTING										;**	rm43/rm102
	SITTING_AT_TABLE							;**	rm43
	STUNNED										;**	rm71
	CRAWLING										;**	rm71
	SWINGING1									;**	rm74
	SWINGING2									;**	rm74
	SWINGING3									;**	rm74
	WADING										;**	rm74
	SLIPPING										;**	rm81
	SLIDING										;**	rm81
	SPREADING									;**	rm81
)


(extern											;**	External procedures
	LookAt 			0 1
	NormalEgo 		0 2
	NearControl		0 3
	AddActorToPic 	0 4
	HandsOff			0 5
	HandsOn			0 6
	Notify 			0 7
	HaveMem			0 8
	RedrawCast		0 9
	SoundLoops 		0 10
	cls				0 11
	Ok					0 12
	ItIs				0 13
	YouAre			0 14
	NotNow			0 15
	NotClose			0 16
	AlreadyTook		0 17
	SeeNothing		0 18
	CantDo			0 19
	DontHave			0 20
	SetRgTimer 		0 21
;	LogIt				0 22
)


;*****   ROOM AND REGIONS   *******
;(define	rmMENU						001)	;**	output is script.997
(define	INVENTORY_TEXT				002)
(define	DOORS							003)
(define	BASS_SETTER					004)
(define	DEBUG							005)
(define	AIRPLANES					006)
(define	rgBarber						007)
(define	rgHenchette					008)
(define	rmBossKey					009)
(define	rgLA							200)
(define	rgShip						300)
(define	rgResort						400)
(define	rgResortBeach				401)
(define	rgAirport					500)
(define	rgAirplane					600)
(define	rgIsland						700)
;(define	MENU							997)



;*********   GLOBAL VARS  *********
(global
	debugging					100
	currentStatus				101
	currentEgoView				102
	currentHenchView			103
	gameSeconds					104
	gameMinutes					105
	gameHours					106
	rank							107
	rgTimer						108
	currentTimer				110
		(enum 									;**	currentTimer values (what 
			1										;**	rgTimer is presently timing.
			CRUISE_DEPARTURE
			CRUISE_BEDTIME
			LIFEBOAT_DRILL
			AIRPLANE_DEPARTURE
			AIRPLANE_ARRIVAL
			VOLCANO_ERUPTION
		)
	forceAtest					111
;	notMovedCycles				112
	oldTime						113
;	nearPerson					114
;		(enum 1											;**	nearPerson values
;			man
;			woman
;			child
;			person
;		)
	secondsInRoom				115
	loadDebugNext				116
	debugMenu					117
	showFrag						118
	logging						119
	machineSpeed 				120
		(enum 
			10 pc8088							;**	if < 30 it's a PC
			30 pcAT								;**	if < 60 it's an AT
			60 pc386								;**	if > 60 it's a 386
		)
	LAhaircut					121
	LAhenchAfterEgo			122
	filthLevel					123
	scoredSunscreen			124

	wearingSunscreen			125
		(enum	2 WASHED_OFF ON_CLOTHES)
	loweredLifeboats			126
	henchOnScreen				127
	timesInRm33					128
	wearingWig					129
	
	blondHair					130
	bodyWaxed					131
	rmAfter40 					132
	braContents					133
	timesInRm40					134
	talkedToMD					135
	
	confusedKrishnas			136
	seenCustomsJoke			137
	bombStatus					138
		(enum 1											;**	bombStatus values
			TICKING
			EXPLODED
			GONE
		)
	missedPlane					139
	timesInRm50					140
	
	boreStatus					141
	wearingParachute			142
	airplaneDoorStatus		143
	
	pastBees						144
	snakeState					145
	pastQuicksand				146
	pastPiranha					147
	islandStatus				148
		(enum 1											;**	islandStatus values
			metGirl
			initiation
			exploring
				100
			copterComing
			enteringCopter
			leavingVolcano
			onBeach
			getDaddy
			wedding
			hairfix
			dreamScene
		)
	tpBuffer						150			;**	20 words = 40 characters
	tritePhrase					169
	string						170			;**	300 words = 600 characters
	laffer						470
	scoredKnothole				471
	scoredChaise				472
	scoredLifeboat				473
	scoredRm102Sit				474
	scoredRm43Sit				475
	scoredJogger 				476
	scoredWoreSunscreen		477
	scoredRosella				478
	reappliedSunscreen		479
)


;*********   INVENTORY   (Show Object views match Inventory ID #'s)
(enum
	iDummy					;**	0
	iDollar
	iLotteryTicket
	iCruiseTicket
	iMillion
	iSwimsuit				;**	5
	iMoney
	iPassport
	iGulp
	iSunscreen
	iOnklunk					;**	10
	iFruit
	iSewingKit
	iSpinachDip
	iWig
	iBikiniTop				;**	15
	iBikiniBottom
	iKnife
	iSoap
	iMatches
	iFlower					;**	20
	iHairRejuvenator
	iSuitcase
	iAirlineTicket
	iParachute
	iBobbyPin				;**	25
	iPamphlet
	iAirsickBag
	iStick
	iVine
	iAshes					;**	30
	iSand
)



;*********   CONSTANTS   **********
(enum												;**	Standard loops
	facingEast
	facingWest
	facingSouth
	facingNorth
)
(enum												;**	Door states
	doorClosed
	doorOpening
	doorOpen
	doorClosing
)
(enum												;**	Priorities
	black
	blue
	green
	cyan
	red
	magenta
	brown
	white
	ltBlack
	ltBlue
	ltGreen
	ltCyan
	ltRed
	ltMagenta
	ltBrown
	ltWhite
)
(define	gray			ltBlack)				;**	Synonyms
(define	grey			ltBlack)
(define	pink			ltRed)
(define	yellow		ltBrown)


(define	cBlack		%0000000000000001) ;**	Bit mapped controls
(define	cBlue			%0000000000000010)
(define	cGreen		%0000000000000100)
(define	cCyan			%0000000000001000)
(define	cRed			%0000000000010000)
(define	cMagenta		%0000000000100000)
(define	cBrown		%0000000001000000)
(define	cWhite		%0000000010000000)
(define	cLtBlack		%0000000100000000)
(define	cLtBlue		%0000001000000000)
(define	cLtGreen		%0000010000000000)
(define	cLtCyan		%0000100000000000)
(define	cLtRed		%0001000000000000)
(define	cLtMagenta	%0010000000000000)
(define	cLtBrown		%0100000000000000)
(define	cLtWhite		%1000000000000000)

(define	cGray			cLtBlack)			;**	Synonyms
(define	cGrey			cLtBlack)
(define	cYellow		cLtBrown)


(define	LIMBO				-1)				;**	Cut-rate defines.
(define	RELEASE			-1)
(define	delay				= seconds)
(define	LastCel			255)
(define	FORWARD			1)					;**	for CycleTo stuff
(define	REVERSE		  -1)
(define	HIGH			at: -1 20)
(define	LOW			at: -1 152)
(define	BIG		   at: -1 15 width: 280)
(define	WIDE		at: 15 -1 width: 280)
(define	upDown			1)					;**	shake screen constants
(define	leftRight		2)
(define	diagonal			3)

(define	DIRTY				10)				;**	filthLevel constants
(define	CLEAN				4)

(define	BIRD				995)				;**	Al's personal Cursor



;************   FONTS   *************
(define	CHICAGO12			0)
(define	NEWYORK12			1)
(define	GENEVA12				2)
(define	SMALL9				3)
(define	HELVETICA18 		7)
(define	GENEVA7	 			999)
;(define	SYSFONT				0)
;(define	USERFONT				1)



;********   SOUND EFFECTS   *********
(define	sFalling						1)
(define	sStunned						2)
(define	sBees							3)
(define	sVolcanoEruption			4)
(define	sTicking						5)
(define	sBOOM							6)
(define	sScored						7)
(define	sBirds						8)
(define	sDrumRoll					9)
(define	sKilledDrNo					10)
(define	sFallingLong				11)
(define	sDrumFill					12)
(define	s1Boing						13)
(define	s2Boing						14)
(define	s3Boing						15)
(define	sHarp							16)
(define	sCopter						17)


;************   MUSIC   *************
(define	mTitle						101)
(define	mIsland						102)
(define	mDead							103)
(define	mDrNo							104)	;**	mDrNoFeelings
(define	mHenchette					105)
(define	mHench						106)
(define	mBarber						107)
(define	mDC							108)
(define	mLLL							109)
(define	mFortress					110)
(define	mWedding						111)
(define	mChase						112)
(define	mJaws							113)
(define	mManhood						114)
(define	mKGB 							115)
(define	mLoveTheme					116)
(define	mKGBishna					117)

