
package com.adrien_cuisse.chess_repertoire.application.doubles.dto.account;

import com.adrien_cuisse.chess_repertoire.application.dto.account.CredentialsDTO;
import com.adrien_cuisse.chess_repertoire.application.dto.account.FindCredentialsByMailAddressQuery;

import java.util.Optional;

public final class FindCredentialsByTakenMailAddressFake implements FindCredentialsByMailAddressQuery.IHandler
{
	private static final String MATCHING_MAIL_ADDRESS = "taken@email.org";

	@Override
	public Optional<CredentialsDTO> execute(final FindCredentialsByMailAddressQuery query)
	{
		if (query.mailAddress().equals(MATCHING_MAIL_ADDRESS))
			return Optional.of(new CredentialsDTO("", "", MATCHING_MAIL_ADDRESS, ""));

		return Optional.empty();
	}

	public String matchingMailAddress()
	{
		return MATCHING_MAIL_ADDRESS;
	}
}
