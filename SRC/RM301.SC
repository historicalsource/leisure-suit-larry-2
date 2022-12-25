;**
;**	Logics for room 301 -- henchette witty repartee region
;**
;**	Leisure Suit Larry goes "Looking for Love" (in Several Wrong Places)
;**		by Al Lowe
;**
;**	Last Update:	August 1, 1988
;**


(script#				301)

(public
	rm301				0
)


(local
	seenMessage
)


(instance rm301 of Region
	(method (init)
		(super init:)
	)


	(method (doit)
		(super doit:)
	)


	(method (handleEvent event)
		(if (or	(!= (event type?) saidEvent)
					(== henchOnScreen FALSE)
					(event claimed?))
			(return))

		(if (Said 'talk')
			(Print "\"Ooh, Larry. I think I love you.
				Why don't you come with me and we'll head back to my place!\"")
			(Print "(You wonder how she knows your name.)" LOW)
		)

		(if (Said 'hump, (sleep<with), (love<make), (bone<jump), (mount<up)')
			(Print "\"I could be interested in that;
				but only with you, my studly hero!\"")
		)

		(if (Said 'look>')
			(if (Said '/woman')
				(Print "She's a knockout, isn't she, Larry?")
			)

			(if (Said 'tit')
				(Print "What a pair, eh?")
			)

			(if (Said '/butt')
				(Print "It's tight and firm; not at all like yours, Larry.")
			)

			(if (Said '/eye')
				(Print "Her eyes bring fire to your heart.")
			)

			(if (Said '/dress')
				(Print "It's almost as beautiful as what's in it.")
			)
		)
	)
)

