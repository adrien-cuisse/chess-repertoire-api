
package com.adrien_cuisse.chess_repertoire.domain.value_objects.account.password;

public final class NullPasswordException extends RuntimeException
{
	public NullPasswordException()
	{
		super("Password is null");
	}
}
