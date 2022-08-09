
package com.adrien_cuisse.chess_repertoire.application.dto.account;

public record RegisterAccountCommand(
	String ownerIdentity,
	String nickname,
	String mailAddress,
	String hashedPassword
) {
	public interface IHandler
	{
		void execute(final RegisterAccountCommand command);
	}
}
