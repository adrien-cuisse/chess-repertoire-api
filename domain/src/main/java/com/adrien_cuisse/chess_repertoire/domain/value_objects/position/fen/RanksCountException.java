
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position.fen;

public final class RanksCountException extends IllegalArgumentException
{
	public RanksCountException(final int ranksCount)
	{
		super("Fen requires 8 ranks, got " + ranksCount);
	}
}
