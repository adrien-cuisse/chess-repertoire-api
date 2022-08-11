
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position.fen;

public final class CastlingException extends IllegalArgumentException
{
	public CastlingException(final String castling)
	{
		super("Castling possibilities must be white first and king first, or '-' if none, got '" + castling + "'");
	}
}
