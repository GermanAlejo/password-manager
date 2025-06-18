package com.passwordmanager.password_manager.controller;

import com.passwordmanager.password_manager.dto.PasswordEntryDTO;
import com.passwordmanager.password_manager.exceptions.EncryptionException;
import com.passwordmanager.password_manager.exceptions.IllegalPasswordEntryException;
import com.passwordmanager.password_manager.exceptions.PasswordEntryNotFoundException;
import com.passwordmanager.password_manager.model.PasswordEntry;
import com.passwordmanager.password_manager.security.UserDetailsImpl;
import com.passwordmanager.password_manager.service.PasswordEntryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    //Maybe here makes more sense to request user?
    @GetMapping("list")
    public ResponseEntity<List<PasswordEntryDTO>> getEntriesForUser() throws PasswordEntryNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.isAuthenticated()) {
            log.error("User is not authenticated");
            throw new BadCredentialsException("Credentials expired for this session");
        }
        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
        List<PasswordEntry> allEntries = passwordEntryService.listEntriesByUserId(user.getUser().getId());
        List<PasswordEntryDTO> entryDTOList = allEntries.stream()
                .map(passwordEntry -> new PasswordEntryDTO(passwordEntry.getEntryName(), passwordEntry.getEncryptedPassword()))
                .toList();
        return ResponseEntity.ok(entryDTOList);
    }

  @PostMapping("createEntry")
  public ResponseEntity<PasswordEntryDTO> createNewEntry(@Valid @RequestBody PasswordEntryDTO passwordEntryDTO) throws IllegalPasswordEntryException, EncryptionException {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (!auth.isAuthenticated()) {
          log.error("User is not authenticated");
          throw new BadCredentialsException("Credentials expired for this session");
      }
      if(passwordEntryService.doesEntryExists(passwordEntryDTO.getEntryName())) {
          log.error("Entry already created");
          throw new IllegalPasswordEntryException("Entry already created");
      }
      UserDetailsImpl customUser = (UserDetailsImpl) auth.getPrincipal();
      PasswordEntry newEntry = passwordEntryService.createNewEntry(passwordEntryDTO, customUser.getUser());
      PasswordEntryDTO responseEntry = new PasswordEntryDTO(newEntry.getEntryName(), newEntry.getEncryptedPassword());
      return ResponseEntity.ok(responseEntry);
  }

  //TODO: Delete entry here

  //TODO: Edit entry
}
