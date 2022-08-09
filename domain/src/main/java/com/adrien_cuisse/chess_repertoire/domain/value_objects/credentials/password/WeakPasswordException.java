
package com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.password;

public class WeakPasswordException extends RuntimeException
{
	public WeakPasswordException(final String password)
	{
		super(String.format(
			"Password '%s' is too weak, it must contain lowercase, uppercase, digits and symbols",
			password
		));
	}
}
