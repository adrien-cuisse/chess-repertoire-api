
package com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.uuid;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.IIdentity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public final class UuidV4Test
{
    public static byte[] nullBytes()
    {
        final byte[] bytes = new byte[16];
        Arrays.fill(bytes, (byte) 0);
        return bytes;
    }

    @Test
    public void is32HexDigitsLong()
    {
        // given an uuid
        final UuidV4 uuid = new UuidV4();

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
        final UuidV4 uuid = new UuidV4();

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
        final UuidV4 uuid = new UuidV4();

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
        final UuidV4 uuid = new UuidV4();
        final IUuid otherInstance = new UuidV4();
        final IUuid otherImplementation = new Uuid(nullBytes(), 0);
        final IUuid sameStringRepresentation = new UuidV4(uuid.toString());

        final IIdentity<String> otherIdentity = new IIdentity<String>() {
            public String toNative() { return "42"; }
            public boolean equals(final IValueObject other) { return false; }
        };

        final IValueObject otherValueObject = new IValueObject() {
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
        final UuidV4 first = new UuidV4();
        final UuidV4 second = new UuidV4();

        // when comparing them
        final boolean instancesAreTheSame = first.equals(second);

        // then they should be different
        assertFalse(
            instancesAreTheSame,
            "Each instance should not equal any other"
        );
    }
}
