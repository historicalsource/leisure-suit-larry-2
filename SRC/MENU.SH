;**
;**	Menu.sh -- common header for rm000, rm001, and rm005 (debug menu)
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	October 1, 1988
;**


(enum
$100 sierraM
	aboutI
	helpI

$200 fileM
	saveI
	restoreI
		divider201I
	restartI
	quitI

$300 actionM
	pauseI
	invI
	repeatI
		divider301I
	bossI
	filthI
	triteI

$400 speedM
	speedI
		divider401I
	fasterI
	normalI
	slowerI

$500 soundM
	volumeI
	soundI

$600	cheatM
	roomI
	noteI
	rgTimeI
	inputI
		divider601I
	visualI
	priorityI
	controlI
		divider602I
	tglDebugI
	memI
	quickQuitI

$700	debugM
	clothingI
	gameI
	castI
	egoI
	gridI
	writeEgoI
)

