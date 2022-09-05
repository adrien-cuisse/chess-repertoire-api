
package com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.nickname;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;

public final class Nickname implements IValueObject
{
    private final String nickname;

    /**
     * @throws NullNicknameException - if nickname is null
     * @throws EmptyNicknameException - if nickname is empty
     */
    public Nickname(final String nickname)
    {
        if (nickname == null)
            throw new NullNicknameException();

        this.nickname = nickname.replaceAll("\s+", "\s").trim();
        if (this.nickname.equals(""))
            throw new EmptyNicknameException();
    }

    @Override
    public boolean equals(final IValueObject other)
    {
        if (other instanceof Nickname otherInstance)
            return this.nickname.equals(otherInstance.nickname);
        return false;
    }

    public String toString()
    {
        return this.nickname;
    }
}

