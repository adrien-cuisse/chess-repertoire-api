
package com.adrien_cuisse.chess_repertoire.application.dto.position;

public record RegisterPositionCommand(
	String identity,
	String ownerIdentity,
	String name,
	String fen
) {
	public interface IHandler
	{
		void execute(final RegisterPositionCommand command);
	}
}
