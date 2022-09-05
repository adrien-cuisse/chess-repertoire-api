
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position.name;

public class EmptyPositionNameException extends IllegalArgumentException
{
	public EmptyPositionNameException()
	{
		super("Position name is empty");
	}
}
