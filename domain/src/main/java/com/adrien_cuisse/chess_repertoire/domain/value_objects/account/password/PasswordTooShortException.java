
package com.adrien_cuisse.chess_repertoire.domain.value_objects.account.password;

public final class PasswordTooShortException extends RuntimeException
{
	public PasswordTooShortException(final String password, final int minimumLength)
	{
		super(String.format("Invalid password '%s', it must be at least %d characters long", password, minimumLength));
	}
}
