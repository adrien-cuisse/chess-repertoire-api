
package com.adrien_cuisse.chess_repertoire.application.dto.account;

public record CredentialsDTO(
	String ownerIdentity,
	String nickname,
	String mailAddress,
	String hashedPassword
) { }
