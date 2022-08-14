
package com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.uuid;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.IIdentity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public final class UuidV4Test
{
    public static byte[] nullBytes()
    {
        final var bytes = new byte[16];
        Arrays.fill(bytes, (byte) 0);
        return bytes;
    }

    @Test
    public void is32HexDigitsLong()
    {
        // given an uuid
        final var uuid = new UuidV4();

        // when checking how many digits are composing its string representation
        final int digitsCount = uuid.toString().replace("-", "").length();

        // then there should be 32
        assertEquals(
            32,
            digitsCount,
            "Uuid should be made of 32 hex-digits"
        );
    }

    @Test
    public void isVersion4()
    {
        // given a new uuid
        final var uuid = new UuidV4();

        // when checking its version
        final int version = uuid.version();

        // then it should be 4
        assertEquals(
            4,
            version,
            "Version of UuidV4 should be 4"
        );
    }

    @Test
    public void isRfcVariant()
    {
        // given a new uuid
        final var uuid = new UuidV4();

        // when checking its variant
        final IUuid.Variant variant = uuid.variant();

        // then it should be RFC
        assertEquals(
            IUuid.Variant.RFC_VARIANT,
            variant,
            "UuidV4 should be RFC variant"
        );
    }

    public static Object[][] equality() // throws InvalidUuidException, InvalidUuidV4Exception
    {
        final var uuid = new UuidV4();
        final var otherInstance = new UuidV4();
        final var otherImplementation = new Uuid(nullBytes(), 0);
        final var sameStringRepresentation = new UuidV4(uuid.toString());

        final var otherIdentity = new IIdentity<String>() {
            public String toNative() { return "42"; }
            public boolean equals(final IValueObject other) { return false; }
        };

        final var otherValueObject = new IValueObject() {
            public boolean equals(IValueObject other) { return false; }
        };

        return new Object[][] {
            { uuid, otherImplementation, false },
            { uuid, otherInstance, false },
            { uuid, uuid, true },
            { uuid, sameStringRepresentation, true },
            { uuid, otherIdentity , false },
            { uuid, otherValueObject, false }
        };
    }

    @ParameterizedTest
    @MethodSource("equality")
    public void matchesOnlyExactString(final UuidV4 uuid, final IValueObject other, final boolean expectedEquality)
    {
        // given 2 uuids

        // when comparing them
        final boolean actualEquality = uuid.equals(other);

        // then it should be the expected equality
        assertEquals(
            expectedEquality,
            actualEquality,
            "Uuids should equal only if they represent the same value"
        );
    }

    @Test
    public void isUnique()
    {
        // given 2 uuids built the same way
        final var first = new UuidV4();
        final var second = new UuidV4();

        // when comparing them
        final boolean instancesAreTheSame = first.equals(second);

        // then they should be different
        assertFalse(
            instancesAreTheSame,
            "Each instance should not equal any other"
        );
    }

    @Test
    public void isNotNull()
    {
        // given a null UUID
        final String uuid = null;

        // when trying to create an instance from it
        final Executable instantiation = () -> new UuidV4(uuid);

        // then it should throw an exception
        assertThrows(
            NullUuidException.class,
            instantiation,
            "Uuid shouldn't be null"
        );
    }

    @Test
    public void isTrimmed()
    {
        // given a uuid with useless spaces
        final var uuid = new UuidV4("   00 000000-000 0-40 00-00 00-000 000 00000 0   ");

        // when checking its format
        final String format = uuid.toString();

        // then it shouldn't contain spaces
        assertEquals(
            "00000000-0000-4000-0000-000000000000",
            format,
            "String representation shouldn't contain spaces"
        );
    }
}
