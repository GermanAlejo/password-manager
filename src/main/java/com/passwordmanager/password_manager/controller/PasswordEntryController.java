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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passwords")
public class PasswordEntryController {

  private static final Logger log = LoggerFactory.getLogger(PasswordEntryController.class);

  private final PasswordEntryService passwordEntryService;

  public PasswordEntryController(PasswordEntryService passwordEntryService) {
    this.passwordEntryService = passwordEntryService;
  }

  @GetMapping("list")
  public ResponseEntity<List<PasswordEntryDTO>> getEntriesForUser(@RequestHeader("Authorization") String authHeader) throws PasswordEntryNotFoundException {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (!auth.isAuthenticated()) {
      log.error("User is not authenticated");
      throw new BadCredentialsException("Credentials expired for this session");
    }
    //extract the token from the request
    String jwt = authHeader.substring(7); // Bearer <token>
    UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
    List<PasswordEntryDTO> allEntries = passwordEntryService.listEntriesByUserId(user.getUser().getId(), jwt);
    return ResponseEntity.ok(allEntries);
  }

  @PostMapping("createEntry")
  public ResponseEntity<PasswordEntryDTO> createNewEntry(@Valid @RequestBody PasswordEntryDTO passwordEntryDTO, @RequestHeader("Authorization") String authHeader)
      throws IllegalPasswordEntryException, EncryptionException {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (!auth.isAuthenticated()) {
      log.error("User is not authenticated");
      throw new BadCredentialsException("Credentials expired for this session");
    }
    if (passwordEntryService.doesEntryExists(passwordEntryDTO.getEntryName())) {
      log.error("Entry already created");
      throw new IllegalPasswordEntryException("Entry already created");
    }
    log.info("Calling service");
    //extract the token from the request
    String jwt = authHeader.substring(7); // Bearer <token>
    UserDetailsImpl customUser = (UserDetailsImpl) auth.getPrincipal();
    PasswordEntry newEntry = passwordEntryService.createNewEntry(passwordEntryDTO, customUser.getUser(), jwt);
    PasswordEntryDTO responseEntry = new PasswordEntryDTO(newEntry.getEntryName(), newEntry.getEncryptedPassword());
    return ResponseEntity.ok(responseEntry);
  }

  @PostMapping("deleteEntry")
  public ResponseEntity<?> deleteEntry(@Valid @RequestBody PasswordEntryDTO dto, @RequestHeader("Authorization") String authHeader)
      throws IllegalPasswordEntryException {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if(!auth.isAuthenticated()) {
      log.error("User is not authenticated");
      throw new BadCredentialsException("Credentials expired for this session");
    }
    if (!passwordEntryService.doesEntryExists(dto.getEntryName())) {
      log.error("Entry does not exists");
      throw new IllegalPasswordEntryException("Entry not found");
    }
    passwordEntryService.deleteEntry(dto.getEntryName());
    return ResponseEntity.noContent().build(); // 204 No Content
  }

  //TODO: Edit entry


}
