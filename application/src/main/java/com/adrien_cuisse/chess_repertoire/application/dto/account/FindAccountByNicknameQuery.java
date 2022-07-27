
package com.adrien_cuisse.chess_repertoire.application.dto.account;

import java.util.Optional;

public record FindAccountByNicknameQuery(String nickname)
{
    public interface IHandler
    {
        Optional<Account> execute(final FindAccountByNicknameQuery query);
    }
}
