
package com.adrien_cuisse.chess_repertoire.application.doubles.dto.account;

import com.adrien_cuisse.chess_repertoire.application.dto.account.CredentialsDTO;
import com.adrien_cuisse.chess_repertoire.application.dto.account.FindCredentialsByMailAddressQuery;

import java.util.Optional;

public final class FindCredentialsByTakenMailAddressFake implements FindCredentialsByMailAddressQuery.IHandler
{
	public Optional<CredentialsDTO> execute(final FindCredentialsByMailAddressQuery query)
	{
		if (query.mailAddress().equals("taken@email.org"))
			return Optional.of(new CredentialsDTO("", "", "taken@email.org", ""));
		return Optional.empty();
	}
}
