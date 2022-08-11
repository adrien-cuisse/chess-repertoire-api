
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position.fen;

public final class ActivePlayerException extends IllegalArgumentException
{
	public ActivePlayerException(final String activePlayer)
	{
		super("Fen requires active player to be either 'w' or 'b', got '" + activePlayer + "'");
	}
}
