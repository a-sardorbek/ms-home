package com.system.uz.rest.service;

import com.system.uz.exceptions.BadRequestException;
import com.system.uz.exceptions.NotFoundException;
import com.system.uz.global.MessageKey;
import com.system.uz.global.Utils;
import com.system.uz.rest.domain.Contact;
import com.system.uz.rest.model.admin.contact.ContactCreateReq;
import com.system.uz.rest.model.admin.contact.ContactRes;
import com.system.uz.rest.model.admin.contact.ContactUpdateReq;
import com.system.uz.rest.model.contact.ContactWhiteRes;
import com.system.uz.rest.repository.ContactRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public void create(ContactCreateReq req) {

        long count = contactRepository.count();
        if (count > 0) {
            throw new BadRequestException(MessageKey.CONTACT_ALREADY_EXISTS);
        }

        Contact contact = new Contact();
        contact.setEmail(req.getEmail());
        contact.setFirstPhone(req.getFirstPhone());
        contact.setSecondPhone(req.getSecondPhone());
        contact.setMainOffice(req.getMainOfficeUz());
        contact.setMainOfficeRu(req.getMainOfficeRu());
        contact.setMainOfficeEng(req.getMainOfficeEng());
        contact.setProductionOffice(req.getProductionOfficeUz());
        contact.setProductionOfficeRu(req.getProductionOfficeRu());
        contact.setProductionOfficeEng(req.getProductionOfficeEng());
        contact.setContactId(Utils.generateToken());
        contact.setDescriptionUz(req.getDescriptionUz());
        contact.setDescriptionRu(req.getDescriptionRu());
        contact.setDescriptionEng(req.getDescriptionEng());
        contactRepository.save(contact);
    }

    public void update(ContactUpdateReq req) {
        Optional<Contact> optionalContact = contactRepository.findByContactId(req.getContactId());
        if (optionalContact.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        Contact contact = optionalContact.get();
        contact.setEmail(req.getEmail());
        contact.setFirstPhone(req.getFirstPhone());
        contact.setSecondPhone(req.getSecondPhone());
        contact.setMainOffice(req.getMainOfficeUz());
        contact.setMainOfficeRu(req.getMainOfficeRu());
        contact.setMainOfficeEng(req.getMainOfficeEng());
        contact.setProductionOffice(req.getProductionOfficeUz());
        contact.setProductionOfficeRu(req.getProductionOfficeRu());
        contact.setProductionOfficeEng(req.getProductionOfficeEng());
        contact.setDescriptionUz(req.getDescriptionUz());
        contact.setDescriptionRu(req.getDescriptionRu());
        contact.setDescriptionEng(req.getDescriptionEng());
        contactRepository.save(contact);
    }


    public ResponseEntity<List<ContactRes>> getList() {
        List<Contact> contacts = contactRepository.findAll();
        List<ContactRes> contactResList = new ArrayList<>();
        for (Contact contact : contacts) {
            contactResList.add(new ContactRes(
                    contact.getContactId(),
                    contact.getFirstPhone(),
                    contact.getSecondPhone(),
                    contact.getEmail(),
                    contact.getMainOffice(),
                    contact.getMainOfficeRu(),
                    contact.getMainOfficeEng(),
                    contact.getProductionOffice(),
                    contact.getProductionOfficeRu(),
                    contact.getProductionOfficeEng(),
                    contact.getDescriptionUz(),
                    contact.getDescriptionRu(),
                    contact.getDescriptionEng()
            ));
        }

        return ResponseEntity.ok(contactResList);
    }

    public ResponseEntity<ContactRes> getById(String contactId) {
        Optional<Contact> optionalContact = contactRepository.findByContactId(contactId);
        if (optionalContact.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }
        Contact contact = optionalContact.get();

        ContactRes contactRes = new ContactRes(
                contact.getContactId(),
                contact.getFirstPhone(),
                contact.getSecondPhone(),
                contact.getEmail(),
                contact.getMainOffice(),
                contact.getMainOfficeRu(),
                contact.getMainOfficeEng(),
                contact.getProductionOffice(),
                contact.getProductionOfficeRu(),
                contact.getProductionOfficeEng(),
                contact.getDescriptionUz(),
                contact.getDescriptionRu(),
                contact.getDescriptionEng()
        );

        return ResponseEntity.ok(contactRes);
    }

    public void delete(String contactId) {
        Optional<Contact> optionalContact = contactRepository.findByContactId(contactId);
        if (optionalContact.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }
        Contact contact = optionalContact.get();
        contact.setDeletedAt(LocalDateTime.now());
        contactRepository.save(contact);
    }

    // ==========================white==============================================

    public ResponseEntity<ContactWhiteRes> getWhiteContact() {
        List<Contact> contacts = contactRepository.findAll();
        Contact contact = contacts.get(0);
        ContactWhiteRes contactRes = new ContactWhiteRes(
                contact.getContactId(),
                contact.getFirstPhone(),
                contact.getSecondPhone(),
                contact.getEmail(),
                Utils.getLanguage(contact.getMainOffice(), contact.getMainOfficeRu(), contact.getMainOfficeEng()),
                Utils.getLanguage(contact.getProductionOffice(), contact.getProductionOfficeRu(), contact.getProductionOfficeEng()),
                Utils.getLanguage(contact.getDescriptionUz(),contact.getDescriptionRu(), contact.getDescriptionEng())
        );

        return ResponseEntity.ok(contactRes);
    }
}
