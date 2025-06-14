package com.passwordmanager.password_manager;

import com.passwordmanager.password_manager.model.PasswordEntry;
import com.passwordmanager.password_manager.model.User;
import com.passwordmanager.password_manager.repository.PasswordRepository;
import com.passwordmanager.password_manager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;
import java.util.Optional;
@SpringBootApplication()
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})//This is used to the app don't crash when mongo is not configured
@EnableMongoRepositories("com.passwordmanager.password_manager.repository") // Keep this
public class PasswordManagerBackendApplication {

	private static final Logger log = LoggerFactory.getLogger(PasswordManagerBackendApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PasswordManagerBackendApplication.class, args);
	}

	/*
	This is a testing method
	 */
	//@Bean
	public CommandLineRunner demo(UserRepository repository, PasswordRepository repository2) {
		return (args) -> {
			// save a few customers
			//repository.save(new User("Jack", "test1@mail.com", "123"));
			//repository.save(new User("Chloe", "test2@mail.com", "321"));
			//repository2.save(new PasswordEntry("pass1", "1234"));
			//repository2.save(new PasswordEntry("pass2", "1234"));

			// fetch all customers
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			repository.findAll().forEach(customer -> log.info(customer.toString()));
			log.info("");

			// fetch an individual customer by ID
			Optional <User> result = repository.findById("1L");
			result.ifPresent(user -> {
				log.info("User found with findById(1L):");
				log.info("--------------------------------");
				log.info(user.toString());
				log.info("");
			});

			Optional <User> result2 = repository.findByUsername("Chloe");
			result2.ifPresent(user -> {
				log.info("User found with Username:");
				log.info("--------------------------------");
				log.info(user.getUsername());
				log.info("");
			});

			// fetch customers by username
			List<User> allList = repository.findAll();
			log.info("Users found with findByLastName('Bauer'): ");
			log.info("--------------------------------------------");
			allList.forEach(user -> log.info(user.toString()));
			log.info("");

			//password test
			//fetch all entries
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			repository2.findAll().forEach(passwordEntry -> log.info(passwordEntry.toString()));
			log.info("");

			// fetch an entry by ID
			Optional <PasswordEntry> result3 = repository2.findById("1L");
			result3.ifPresent(pass -> {
				log.info("Entry found with findById(1L):");
				log.info("--------------------------------");
				log.info(pass.toString());
				log.info("");
			});

			//Fetch by entry name
			Optional<PasswordEntry> result4 = repository2.findByEntryName("pass1");
			result4.ifPresent(passwordEntry -> {
				log.info("Entry found with findById(1L):");
				log.info("--------------------------------");
				log.info(passwordEntry.toString());
				log.info("");
			});
		};
	}

}
