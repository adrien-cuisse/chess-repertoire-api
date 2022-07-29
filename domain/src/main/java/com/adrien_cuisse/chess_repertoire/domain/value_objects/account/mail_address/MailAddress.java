
package com.adrien_cuisse.chess_repertoire.domain.value_objects.account.mail_address;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;

import java.util.regex.Pattern;

public final class MailAddress implements IValueObject
{
    private final static Pattern VALIDATION_PATTERN = Pattern.compile("^(.+)@(\\S+)$");

    private final String mailAddress;

    /**
     * @throws NullMailAddressException - if mailAddress is null
     * @throws InvalidMailAddressException - if mailAddress is invalid
     */
    public MailAddress(final String mailAddress)
    {
        if (mailAddress == null)
            throw new NullMailAddressException();

        this.mailAddress = mailAddress.replace(" ", "");

        if (VALIDATION_PATTERN.matcher(this.mailAddress).find() == false)
            throw new InvalidMailAddressException(this.mailAddress);
    }

    public boolean equals(final IValueObject other)
    {
        if (other instanceof MailAddress)
            return this.mailAddress.equals(((MailAddress) other).mailAddress);
        return false;
    }

    public String toString()
    {
        return this.mailAddress;
    }
}
