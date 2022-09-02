
package com.adrien_cuisse.chess_repertoire.application.use_cases.position.creation;

public record PositionCreationRequest(String authenticationToken, String name, String fen)
{
}
