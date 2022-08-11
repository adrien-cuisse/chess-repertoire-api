
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position.fen;

public final class MissingKingException extends IllegalArgumentException
{
	public MissingKingException(final String color, final String kingLabel)
	{
		super("Fen doesn't contain a " + color + " king '" + kingLabel + "'");
	}
}
