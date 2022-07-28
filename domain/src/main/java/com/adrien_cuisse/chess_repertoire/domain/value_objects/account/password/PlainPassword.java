
package com.adrien_cuisse.chess_repertoire.domain.value_objects.account.password;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;

import java.util.regex.Pattern;

public final class PlainPassword implements IPassword
{
    private static final int MINIMUM_LENGTH = 8;

    private static final int MAXIMUM_LENGTH = 64;

    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");

    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");

    private static final Pattern DIGITS_PATTERN = Pattern.compile("[0-9]");

    // OWASP strong password recommendation
    private static final Pattern SYMBOLS_PATTERN = Pattern.compile("[!~<>,;:_=?*+#.\"&§%°()|\\[\\]\\-$^@/]");

    private final String password;

    /**
     * @throws NullPasswordException - if password is null
     * @throws PasswordWithoutLowercaseException - if password doesn't contain lowercase
     * @throws PasswordWithoutUppercaseException - if password doesn't contain uppercase
     * @throws PasswordWithoutDigitsException - if password doesn't contain digits
     * @throws PasswordWithoutSymbolsException - if password doesn't contain symbols
     */
    public PlainPassword(final String password)
    {
        if (password == null)
            throw new NullPasswordException();

        this.password = password;

        if (password.length() < MINIMUM_LENGTH)
            throw new PasswordTooShortException(password, MINIMUM_LENGTH);
        else if (password.length() > MAXIMUM_LENGTH)
            throw new PasswordTooLongException(password, MAXIMUM_LENGTH);

        if (containsLowercase() == false)
            throw new PasswordWithoutLowercaseException(password);
        else if (containsUpperCase() == false)
            throw new PasswordWithoutUppercaseException(password);
        else if (containsDigits() == false)
            throw new PasswordWithoutDigitsException(password);
        else if (containsSymbols() == false)
            throw new PasswordWithoutSymbolsException(password);
    }

    public boolean isHashed()
    {
        return false;
    }

    public boolean equals(final IValueObject other)
    {
        if (other instanceof PlainPassword)
            return this.password.equals(((PlainPassword) other).password);
        return false;
    }

    public String toString()
    {
        return this.password;
    }

    private boolean containsLowercase()
    {
        return containsAnyCharacter(LOWERCASE_PATTERN);
    }

    private boolean containsUpperCase()
    {
        return containsAnyCharacter(UPPERCASE_PATTERN);
    }

    private boolean containsDigits()
    {
        return containsAnyCharacter(DIGITS_PATTERN);
    }

    private boolean containsSymbols()
    {
        return containsAnyCharacter(SYMBOLS_PATTERN);
    }

    private boolean containsAnyCharacter(final Pattern regex)
    {
        return regex.matcher(this.password).find();
    }
}
