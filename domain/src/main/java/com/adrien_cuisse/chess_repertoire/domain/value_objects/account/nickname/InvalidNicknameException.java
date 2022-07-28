
package com.adrien_cuisse.chess_repertoire.domain.value_objects.account.nickname;

public final class InvalidNicknameException extends RuntimeException
{
    public InvalidNicknameException(final String nickname)
    {
        super(String.format(
            "Invalid nickname '%s', it can't contain symbols other than '-', '_', '.' and must start with alphanumeric character",
            nickname
        ));
    }
}
