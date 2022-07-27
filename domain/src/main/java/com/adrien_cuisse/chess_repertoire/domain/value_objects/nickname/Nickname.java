
package com.adrien_cuisse.chess_repertoire.domain.value_objects.nickname;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;

public final class Nickname implements IValueObject
{
    private static final int MINIMUM_LENGTH = 2;

    private static final int MAXIMUM_LENGTH = 16;

    private final String nickname;

    /**
     * @throws NullNicknameException - if nickname is null
     * @throws NicknameTooShortException - if nickname is too short
     * @throws NicknameTooLongException - if nickname is too long
     * @throws InvalidNicknameException - if the nickname contains symbols other than '-', '_', '.' and alphanumeric
     */
    public Nickname(final String nickname)
    {
        if (nickname == null)
            throw new NullNicknameException();

        this.nickname = nickname.replaceAll("\\s", "");

        if (this.nickname.length() < MINIMUM_LENGTH)
            throw new NicknameTooShortException(this.nickname, MINIMUM_LENGTH);
        else if (this.nickname.length() > MAXIMUM_LENGTH)
            throw new NicknameTooLongException(this.nickname, MINIMUM_LENGTH);
        else if (this.nickname.matches("([a-zA-Z0-9][a-zA-Z0-9\\-_.]*)") == false)
            throw new InvalidNicknameException(this.nickname);
    }

    public boolean equals(final IValueObject other)
    {
        if (other instanceof Nickname)
            return this.nickname.equals(((Nickname) other).nickname);
        return false;
    }

    public String toString()
    {
        return this.nickname;
    }
}

