
package com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.password;

public final class NullPasswordException extends IllegalArgumentException
{
	public NullPasswordException()
	{
		super("Password is null");
	}
}
