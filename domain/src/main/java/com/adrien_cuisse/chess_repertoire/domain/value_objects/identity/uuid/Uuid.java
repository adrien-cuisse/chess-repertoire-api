
package com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.uuid;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;

import java.util.Arrays;
import java.util.List;

final class Uuid implements IUuid
{
    private final byte[] bytes;

    /**
     * @throws NullUuidException - if bytes are null
     * @throws InvalidUuidBytesCountException - if bytes count isn't 16
     */
    Uuid(final byte[] bytes)
    {
        if (bytes == null)
            throw new NullUuidException();
        if (bytes.length != 16)
            throw new InvalidUuidBytesCountException(bytes);

        this.bytes = bytes;
    }

    /**
     * @throws NullUuidException - if bytes are null
     * @throws InvalidUuidBytesCountException - if bytes count isn't 16
     */
    Uuid(final byte[] bytes, final int version)
    {
        this(interlopeVersionInTimestampHigh(bytes, version));
    }

    /**
     * @throws NullUuidException - if bytes are null
     * @throws InvalidUuidBytesCountException - if bytes count isn't 16
     * @throws NullVariantException - if variant is null
     */
    Uuid(final byte[] bytes, final int version, final Variant variant)
    {
        this(interlopeVariantInClockSequenceHigh(bytes, variant), version);
    }

    /**
     * @throws NullUuidException - if uuid is null
     * @throws InvalidUuidFormatException - if uuid isn't RFC compliant
     * @throws InvalidUuidVersionException - if version mismatches with string
     */
    Uuid(final String uuid, final int expectedVersion)
    {
        if (uuid == null)
            throw new NullUuidException();

        final String trimmed = uuid.replace(" ", "");
        if (!trimmed.matches("[a-f0-9]{8}-([a-f0-9]{4}-){3}[a-f0-9]{12}"))
            throw new InvalidUuidFormatException(uuid);

        final String digits = trimmed.replace("-", "");

        final String versionDigit = digits.substring(12, 13);
        final int actualVersion = Integer.parseInt(versionDigit, 16);
        if (actualVersion != expectedVersion)
            throw new InvalidUuidVersionException(uuid, expectedVersion);

        this.bytes = new byte[digits.length() / 2];

        for (int processedDigits = 0; processedDigits < digits.length(); processedDigits += 2)
        {
            String hexOctet = digits.substring(processedDigits, processedDigits + 2);
            this.bytes[processedDigits / 2] = (byte) Integer.parseInt(hexOctet, 16);
        }
    }

    @Override
    public boolean equals(final IValueObject other)
    {
        if (other instanceof IUuid otherInstance)
            return this.toNative().equals(otherInstance.toNative());
        return false;
    }

    @Override
    public int version()
    {
        return (this.bytes[6] & 0b1111_1111) >> 4;
    }

    @Override
    public Variant variant()
    {
        return Variant.matchFromByte(this.bytes[8]);
    }

    @Override
    public String toNative()
    {
        return this.toString();
    }

    @Override
    public String toString()
    {
        final var builder = new StringBuilder();
        final List<Integer> dashesPosition = Arrays.asList(4, 6, 8, 10);

        for (int bytesCount = 0; bytesCount < this.bytes.length; bytesCount++)
        {
            if (dashesPosition.contains(bytesCount))
                builder.append("-");

            final String hexOctet = String.format("%02x", this.bytes[bytesCount]);
            builder.append(hexOctet);
        }

        return builder.toString();
    }

    /**
     * @throws NullUuidException - if bytes are null
     * @throws InvalidUuidBytesCountException - if bytes count isn't 16
     */
    private static byte[] interlopeVersionInTimestampHigh(final byte[] bytes, final int version)
    {
        if (bytes == null)
            throw new NullUuidException();
        else if (bytes.length != 16)
            throw new InvalidUuidBytesCountException(bytes);

        final int lowNibbleBitMask = 0x0f;

        final byte highNibble = (byte) ((version & lowNibbleBitMask) << 4);
        final byte lowNibble = (byte) (bytes[6] & lowNibbleBitMask);

        final byte[] bytesWithInterlopedVersion = bytes.clone();
        bytesWithInterlopedVersion[6] = (byte) (highNibble | lowNibble);

        return bytesWithInterlopedVersion;
    }

    /**
     * @throws NullUuidException - if bytes are null
     * @throws InvalidUuidBytesCountException - if bytes count isn't 16
     * @throws NullVariantException - if variant is null
     */
    private static byte[] interlopeVariantInClockSequenceHigh(final byte[] bytes, final Variant variant)
    {
        if (bytes == null)
            throw new NullUuidException();
        else if (bytes.length != 16)
            throw new InvalidUuidBytesCountException(bytes);
        else if (variant == null)
            throw new NullVariantException();

        final byte[] bytesWithInterlopedVariant = bytes.clone();
        bytesWithInterlopedVariant[8] = (byte) (variant.bits() | (bytes[8] & variant.unusedBitsMask()));
        return bytesWithInterlopedVariant;
    }
}
