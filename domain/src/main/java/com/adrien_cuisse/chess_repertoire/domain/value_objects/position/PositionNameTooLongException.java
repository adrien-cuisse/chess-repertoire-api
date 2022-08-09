
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position;

public final class PositionNameTooLongException extends IllegalArgumentException
{
	public PositionNameTooLongException(final String name, final int maximumLength)
	{
		super(String.format(
			"Invalid position name '%s', it must be at most %d characters long",
			name,
			maximumLength
		));
	}
}
