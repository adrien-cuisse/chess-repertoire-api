
package com.adrien_cuisse.chess_repertoire.domain.value_objects.account.password;

public final class PasswordTooLongException extends RuntimeException
{
	public PasswordTooLongException(final String password, final int maximumLength)
	{
		super(String.format("Invalid password '%s', it must be at most %d characters long", password, maximumLength));
	}
}
