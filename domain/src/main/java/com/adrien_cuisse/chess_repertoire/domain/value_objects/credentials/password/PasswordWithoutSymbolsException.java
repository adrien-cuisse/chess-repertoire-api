
package com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.password;

public final class PasswordWithoutSymbolsException extends RuntimeException
{
	public PasswordWithoutSymbolsException(final String password)
	{
		super(String.format("Invalid password '%s', it doesn't contain symbols", password));
	}
}