
package com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.password;

public final class PasswordWithoutLowercaseException extends RuntimeException
{
	public PasswordWithoutLowercaseException(final String password)
	{
		super(String.format("Invalid password '%s', it doesn't contain lowercase", password));
	}
}
