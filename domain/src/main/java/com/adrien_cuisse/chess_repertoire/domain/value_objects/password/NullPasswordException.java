
package com.adrien_cuisse.chess_repertoire.domain.value_objects.password;

public final class NullPasswordException extends RuntimeException
{
	public NullPasswordException()
	{
		super("Password is null");
	}
}
