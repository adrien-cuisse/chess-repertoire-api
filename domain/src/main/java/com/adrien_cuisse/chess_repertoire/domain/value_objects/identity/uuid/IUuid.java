
package com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.uuid;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.IIdentity;

/**
 * An RFC-4122 compliant UUID
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc4122">RFC specifications</a>
 */
public interface IUuid extends IIdentity<String>
{
    int version();

    Variant variant();

    enum Variant
    {
        APOLLO_NCS_VARIANT(0x00, 0x7f),
        RFC_VARIANT(0x80, 0x3f),
        MICROSOFT_VARIANT(0xc0, 0x1f),
        FUTURE_VARIANT(0xe0, 0x1f);

        private final byte bits;

        private final byte unusedBitsMask;

        Variant(final int bits, final int unusedBitsMask)
        {
            this.bits = (byte) bits;
            this.unusedBitsMask = (byte) unusedBitsMask;
        }

        public final byte bits()
        {
            return this.bits;
        }

        public final byte unusedBitsMask()
        {
            return this.unusedBitsMask;
        }

        public static Variant matchFromByte(final byte octet)
        {
            final int bits = (octet & 0b1110_0000);

            if (bits >= 0xe0)
                return FUTURE_VARIANT;
            if (bits >= 0xc0)
                return MICROSOFT_VARIANT;
            if (bits >= 0x80)
                return RFC_VARIANT;

            return APOLLO_NCS_VARIANT;
        }
    }
}
