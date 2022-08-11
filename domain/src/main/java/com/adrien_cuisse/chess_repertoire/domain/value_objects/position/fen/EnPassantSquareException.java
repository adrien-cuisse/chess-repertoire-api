
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position.fen;

public final class EnPassantSquareException extends IllegalArgumentException
{
	public EnPassantSquareException(final String enPassantSquare)
	{
		super("Fen must contain a valid en-passant square, or '-' if none, got '" + enPassantSquare + "'");
	}
}
