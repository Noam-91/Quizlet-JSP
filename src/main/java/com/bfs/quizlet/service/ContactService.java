package com.bfs.quizlet.service;

import com.bfs.quizlet.dao.ContactDao;
import com.bfs.quizlet.domain.Contact;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service

public class ContactService {
    private final ContactDao contactDao;
    public ContactService(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    @Transactional
    public void addContact(String subject, String message, String email) {
        contactDao.addContact(subject, message, email);
    }

    public List<Contact> getAllContacts() {
        List<Contact>contacts = contactDao.getAllActiveContactsOrderByIsSolvedANDCreatedAt();
        contacts.forEach(contact -> {
            String messageSummary = contact.getMessage().length() > 50 ? contact.getMessage().substring(0, 50) + "..." : contact.getMessage();
            contact.setMessageSummary(messageSummary);
        });
        return contacts;
    }

    public Optional<Contact> getContactById(Long contactId) {
        return contactDao.findById(contactId);
    }

    @Transactional
    public void updateContact(Long contactId, boolean isActive, boolean isSolved, Long updatedBy){
        contactDao.updateContact(contactId, isActive, isSolved, updatedBy);
    }
}
