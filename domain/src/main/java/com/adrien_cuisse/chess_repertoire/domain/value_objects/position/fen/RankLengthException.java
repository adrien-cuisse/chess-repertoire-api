
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position.fen;

public final class RankLengthException extends IllegalArgumentException
{
	public RankLengthException(final int length)
	{
		super("Fen ranks must have a length of 8, got " + length);
	}
}
