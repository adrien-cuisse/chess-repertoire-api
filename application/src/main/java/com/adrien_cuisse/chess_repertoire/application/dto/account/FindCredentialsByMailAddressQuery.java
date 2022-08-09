
package com.adrien_cuisse.chess_repertoire.application.dto.account;

import java.util.Optional;

public record FindCredentialsByMailAddressQuery(String mailAddress)
{
    public interface IHandler
    {
        Optional<CredentialsDTO> execute(final FindCredentialsByMailAddressQuery query);
    }
}
