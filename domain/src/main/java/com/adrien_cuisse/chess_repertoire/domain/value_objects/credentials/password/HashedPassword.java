
package com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.password;

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

    @Override
    public boolean isHashed()
    {
        return true;
    }

    @Override
    public boolean equals(final IValueObject other)
    {
        if (other instanceof HashedPassword otherInstance)
            return this.hash.equals(otherInstance.hash);
        return false;
    }

    public String toString()
    {
        return this.hash;
    }
}
