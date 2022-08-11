
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position.fen;

public final class PliesCounterException extends IllegalArgumentException
{
	public PliesCounterException(final String counter)
	{
		super("Plies counter must be an integer >= 0, got '" + counter + "'");
	}
}
