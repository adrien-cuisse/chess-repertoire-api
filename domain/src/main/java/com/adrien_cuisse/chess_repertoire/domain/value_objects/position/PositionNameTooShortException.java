
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position;

public final class PositionNameTooShortException extends RuntimeException
{
	public PositionNameTooShortException(final String name, final int minimumLength)
	{
		super(String.format(
			"Invalid name '%s', it must be at least %d characters long",
			name,
			minimumLength
		));
	}
}
