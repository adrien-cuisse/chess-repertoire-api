
package com.adrien_cuisse.chess_repertoire.application.dto.account;

import java.util.Optional;

public record FindAccountByMailAddressQuery(String mailAddress)
{
    public interface IHandler
    {
        Optional<Account> execute(final FindAccountByMailAddressQuery query);
    }
}
