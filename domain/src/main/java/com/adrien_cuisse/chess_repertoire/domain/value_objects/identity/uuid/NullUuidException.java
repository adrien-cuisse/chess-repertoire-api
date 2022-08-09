
package com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.uuid;

public final class NullUuidException extends IllegalArgumentException
{
	public NullUuidException()
	{
		super("Uuid is null");
	}
}
