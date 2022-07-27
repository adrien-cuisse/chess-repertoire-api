
package com.adrien_cuisse.chess_repertoire.domain.value_objects.nickname;

public final class NicknameTooShortException extends RuntimeException
{
	public NicknameTooShortException(final String nickname, final int minimumLength)
	{
		super(String.format(
			"Invalid nickname '%s', it mustn't be longer than %d characters long",
			nickname,
			minimumLength
		));
	}
}
