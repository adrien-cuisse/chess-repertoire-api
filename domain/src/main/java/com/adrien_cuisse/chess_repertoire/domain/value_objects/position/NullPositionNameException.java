
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position;

public final class NullPositionNameException extends RuntimeException
{
	public NullPositionNameException()
	{
		super("Name is null");
	}
}
