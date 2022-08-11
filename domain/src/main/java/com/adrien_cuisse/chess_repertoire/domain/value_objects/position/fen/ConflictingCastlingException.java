
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position.fen;

public final class ConflictingCastlingException extends IllegalArgumentException
{
	public ConflictingCastlingException(
		final String color,
		final String sideOrPlayer,
		final String movedPiece,
		final String movedSquare
	) {
		super(String.format(
			"Castling possibilities contain %s %s, but %s moved from square %s",
			color,
			sideOrPlayer,
			movedPiece,
			movedSquare
		));
	}
}
