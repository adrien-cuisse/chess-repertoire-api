
package com.adrien_cuisse.chess_repertoire.domain.value_objects.account.password;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;

public final class HashedPassword implements IPassword
{
    private final String hash;

    public HashedPassword(final String hashedPassword)
    {
        if (hashedPassword == null)
            throw new NullPasswordException();

        this.hash = hashedPassword;
    }

    public boolean isHashed()
    {
        return true;
    }

    public boolean equals(final IValueObject other)
    {
        if (other instanceof HashedPassword)
            return this.hash.equals(((HashedPassword) other).hash);
        return false;
    }

    public String toString()
    {
        return this.hash;
    }
}
