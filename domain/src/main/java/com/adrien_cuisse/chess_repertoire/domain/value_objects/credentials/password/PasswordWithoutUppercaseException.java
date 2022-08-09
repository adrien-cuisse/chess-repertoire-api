
package com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.password;

public final class PasswordWithoutUppercaseException extends RuntimeException
{
	public PasswordWithoutUppercaseException(final String password)
	{
		super(String.format("Invalid password '%s', it doesn't contain uppercase", password));
	}
}
