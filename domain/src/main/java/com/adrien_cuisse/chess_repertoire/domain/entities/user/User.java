
package com.adrien_cuisse.chess_repertoire.domain.entities.user;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.IIdentity;

public final class User
{
    private final IIdentity<?> identity;

    public User(final IIdentity<?> identity)
    {
        this.identity = identity;
    }

    public IIdentity<?> identity()
    {
        return this.identity;
    }
}
