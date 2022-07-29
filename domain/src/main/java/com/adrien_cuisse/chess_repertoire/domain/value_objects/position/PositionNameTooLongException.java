
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position;

public final class PositionNameTooLongException extends RuntimeException
{
	public PositionNameTooLongException(final String name, final int maximumLength)
	{
		super(String.format(
			"Invalid name '%s', it must be at most %d characters long",
			name,
			maximumLength
		));
	}
}
