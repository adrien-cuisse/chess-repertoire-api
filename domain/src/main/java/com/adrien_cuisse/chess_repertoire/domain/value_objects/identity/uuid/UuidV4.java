
package com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.uuid;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;

import java.util.Random;

public final class UuidV4 implements IUuid
{
    private static final Random generator = new Random();

    private final Uuid uuid;

    public UuidV4()
    {
        this.uuid = new Uuid(randomBytes(), 4, Variant.RFC_VARIANT);
    }

    /**
     * @throws InvalidUuidFormatException - if uuid isn't RFC compliant
     * @throws InvalidUuidVersionException - if uuid is not version 4
     */
    public UuidV4(final String uuid)
    {
        this.uuid = new Uuid(uuid, 4);
    }

    public boolean equals(final IValueObject other)
    {
        return this.uuid.equals(other);
    }

    public int version()
    {
        return 4;
    }

    public Variant variant()
    {
        return this.uuid.variant();
    }

    public String toNative()
    {
        return this.uuid.toNative();
    }

    public String toString()
    {
        return this.uuid.toString();
    }

    private static byte[] randomBytes()
    {
        final byte[] randomBytes = new byte[16];
        generator.nextBytes(randomBytes);
        return randomBytes;
    }
}
