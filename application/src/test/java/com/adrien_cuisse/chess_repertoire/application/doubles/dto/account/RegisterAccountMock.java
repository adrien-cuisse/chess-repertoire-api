
package com.adrien_cuisse.chess_repertoire.application.doubles.dto.account;

import com.adrien_cuisse.chess_repertoire.application.dto.account.RegisterAccountCommand;

public final class RegisterAccountMock implements RegisterAccountCommand.IHandler
{
	private boolean wasExecuted = false;

	private RegisterAccountCommand receivedCommand = null;

	@Override
	public void execute(final RegisterAccountCommand command)
	{
		this.wasExecuted = true;
		this.receivedCommand = command;
	}

	public boolean wasExecuted()
	{
		return this.wasExecuted;
	}

	public RegisterAccountCommand receivedCommand()
	{
		return this.receivedCommand;
	}
}
