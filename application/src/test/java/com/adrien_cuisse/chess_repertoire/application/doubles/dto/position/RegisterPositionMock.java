
package com.adrien_cuisse.chess_repertoire.application.doubles.dto.position;

import com.adrien_cuisse.chess_repertoire.application.dto.position.RegisterPositionCommand;

public final class RegisterPositionMock implements RegisterPositionCommand.IHandler
{
	private boolean wasExecuted = false;

	private RegisterPositionCommand receivedCommand = null;

	@Override
	public void execute(final RegisterPositionCommand command)
	{
		this.wasExecuted = true;
		this.receivedCommand = command;
	}

	public boolean wasExecuted()
	{
		return this.wasExecuted;
	}

	public RegisterPositionCommand receivedCommand()
	{
		return this.receivedCommand;
	}
}
