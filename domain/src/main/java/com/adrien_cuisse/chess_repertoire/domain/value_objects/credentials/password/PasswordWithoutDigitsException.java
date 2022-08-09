
package com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.password;

public final class PasswordWithoutDigitsException extends RuntimeException
{
	public PasswordWithoutDigitsException(final String password)
	{
		super(String.format("Invalid password '%s', it doesn't contain digits", password));
	}
}
