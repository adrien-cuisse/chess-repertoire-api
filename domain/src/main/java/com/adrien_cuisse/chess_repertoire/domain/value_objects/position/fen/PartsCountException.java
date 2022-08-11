
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position.fen;

public final class PartsCountException extends IllegalArgumentException
{
	public PartsCountException(final int partsCount)
	{
		super("Fen requires 6 space-delimited parts, got " + partsCount);
	}
}
