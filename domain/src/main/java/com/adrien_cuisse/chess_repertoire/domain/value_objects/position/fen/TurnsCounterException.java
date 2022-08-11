
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position.fen;

public final class TurnsCounterException extends IllegalArgumentException
{
	public TurnsCounterException(final String counter)
	{
		super("Turns counter must be an integer > 0, got '" + counter + "'");
	}
}
