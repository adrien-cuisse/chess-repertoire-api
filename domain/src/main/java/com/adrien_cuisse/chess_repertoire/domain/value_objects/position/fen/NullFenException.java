
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position.fen;

public final class NullFenException extends IllegalArgumentException
{
	public NullFenException()
	{
		super("Fen is null");
	}
}
