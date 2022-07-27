
package com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.uuid;

public class InvalidUuidFormatException extends RuntimeException
{
    InvalidUuidFormatException(final String uuid)
    {
        super("The UUID " + uuid + " doesn't match the RFC format 01234567-89ab-cdef-0123-456789abcdef");
    }
}
