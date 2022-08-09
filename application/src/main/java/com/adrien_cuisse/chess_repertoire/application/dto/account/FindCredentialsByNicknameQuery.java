
package com.adrien_cuisse.chess_repertoire.application.dto.account;

import java.util.Optional;

public record FindCredentialsByNicknameQuery(String nickname)
{
    public interface IHandler
    {
        Optional<CredentialsDTO> execute(final FindCredentialsByNicknameQuery query);
    }
}
