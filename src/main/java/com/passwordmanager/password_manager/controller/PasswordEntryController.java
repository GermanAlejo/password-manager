package com.passwordmanager.password_manager.controller;

import com.passwordmanager.password_manager.dto.PasswordEntryDTO;
import com.passwordmanager.password_manager.exceptions.EncryptionException;
import com.passwordmanager.password_manager.exceptions.IllegalPasswordEntryException;
import com.passwordmanager.password_manager.exceptions.PasswordEntryNotFoundException;
import com.passwordmanager.password_manager.model.PasswordEntry;
import com.passwordmanager.password_manager.security.EncryptionService;
import com.passwordmanager.password_manager.service.PasswordEntryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/passwords")
public class PasswordEntryController {

  private static final Logger log = LoggerFactory.getLogger(PasswordEntryController.class);

    private final PasswordEntryService passwordEntryService;

    public PasswordEntryController(PasswordEntryService passwordEntryService) {
        this.passwordEntryService = passwordEntryService;
    }

    //TODO: Get entry for user
    //Maybe here makes more sense to request user?
    @GetMapping("list")
    public ResponseEntity<List<PasswordEntryDTO>> getEntriesForUser(@Valid @RequestBody String userId) throws PasswordEntryNotFoundException {
        List<PasswordEntry> allEntries = passwordEntryService.listEntriesByUser(userId);
        List<PasswordEntryDTO> entryDTOList = allEntries.stream()
                .map(passwordEntry -> new PasswordEntryDTO(passwordEntry.getEntryName(), passwordEntry.getEncryptedPassword()))
                .toList();
        return ResponseEntity.ok(entryDTOList);
    }

  //TODO: Create new entry her
  @PostMapping("createEntry")
  public ResponseEntity<PasswordEntryDTO> createNewEntry(@Valid @RequestBody PasswordEntryDTO passwordEntryDTO) throws IllegalPasswordEntryException, EncryptionException {
      PasswordEntry newEntry = passwordEntryService.createNewEntry(passwordEntryDTO);
      PasswordEntryDTO responseEntry = new PasswordEntryDTO(newEntry.getEntryName(), newEntry.getEncryptedPassword());
      return ResponseEntity.ok(responseEntry);
  }

  //TODO: Delete entry here

  //TODO: Edit entry
}
