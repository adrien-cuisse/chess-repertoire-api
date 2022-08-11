
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position.fen;

public final class RankException extends IllegalArgumentException
{
	public RankException(final String rank)
	{
		super(
			"Fen contains an invalid rank '%s', it must be made of characters [RNBQKrnbqkPp]"
				+ " or empty squares [1-8] as a single digit, got '" + rank + "'"
		);
	}
}
