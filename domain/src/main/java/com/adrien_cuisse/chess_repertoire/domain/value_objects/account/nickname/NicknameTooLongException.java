
package com.adrien_cuisse.chess_repertoire.domain.value_objects.account.nickname;

public final class NicknameTooLongException extends RuntimeException
{
	public NicknameTooLongException(final String nickname, final int maximumLength)
	{
		super(String.format(
			"Invalid nickname '%s', it mustn't be longer than %d characters long",
			nickname,
			maximumLength
		));
	}
}
