
package com.adrien_cuisse.chess_repertoire.domain.value_objects.identity;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;

/**
 * A unique identity, which makes entities what they are
 */
public interface IIdentity<T> extends IValueObject
{
    /**
     * @return T - the native representation of the identity
     */
    T toNative();

    String toString();
}
