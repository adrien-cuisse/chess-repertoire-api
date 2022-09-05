
package com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.password;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;

public final class PlainPassword implements IPassword
{
    private final String password;

    /**
     * @throws NullPasswordException - if password is null
     * @throws EmptyPasswordException - if password is empty
     */
    public PlainPassword(final String password)
    {
        if (password == null)
            throw new NullPasswordException();

        this.password = password;
        if (this.password.equals(""))
            throw new EmptyPasswordException();
    }

    @Override
    public boolean isHashed()
    {
        return false;
    }

    @Override
    public boolean equals(final IValueObject other)
    {
        if (other instanceof PlainPassword otherInstance)
            return this.password.equals(otherInstance.password);
        return false;
    }

    public String toString()
    {
        return this.password;
    }
}
