		(if (== (User controls?) FALSE)
			(self setCursor: waitCursor TRUE)
		else
			(self setCursor: normalCursor (HaveMouse))
		)
	