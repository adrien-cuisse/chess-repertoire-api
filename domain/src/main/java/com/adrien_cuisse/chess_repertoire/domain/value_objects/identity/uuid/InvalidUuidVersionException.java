
package com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.uuid;

public final class InvalidUuidVersionException extends IllegalArgumentException
{
    public InvalidUuidVersionException(final String uuid, final int expectedVersion)
    {
        super("The uuid " + uuid + " doesn't match version " + expectedVersion);
    }
}
