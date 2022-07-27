package com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.uuid;

public final class InvalidUuidBytesCountException extends RuntimeException
{
    public InvalidUuidBytesCountException(final byte[] bytes)
    {
        super(String.format("Expected 16 bytes, got %d", bytes.length));
    }
}
