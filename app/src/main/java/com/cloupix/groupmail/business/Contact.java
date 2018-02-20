package com.cloupix.groupmail.business;

/**
 * Created by alonsoapp on 23/07/16.
 *
 */
public class Contact {

    private long contactId;
    private String email;

    public Contact() {
    }

    public Contact(long contactId, String email) {
        this.contactId = contactId;
        this.email = email;
    }

    public Contact(String email) {
        this.email = email;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean hasVerifiedEmail() {
        // TODO Implementar un verificador de emails true si est'a bien y false si est'a mal
        return true;
    }
}
