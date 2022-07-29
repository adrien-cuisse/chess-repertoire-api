
package com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.uuid;

public final class NullVariantException extends RuntimeException
{
	public NullVariantException()
	{
		super("Variant is null");
	}
}
