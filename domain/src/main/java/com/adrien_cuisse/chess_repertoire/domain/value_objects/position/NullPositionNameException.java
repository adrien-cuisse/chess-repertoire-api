
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position;

public final class NullPositionNameException extends IllegalArgumentException
{
	public NullPositionNameException()
	{
		super("Position name is null");
	}
}
