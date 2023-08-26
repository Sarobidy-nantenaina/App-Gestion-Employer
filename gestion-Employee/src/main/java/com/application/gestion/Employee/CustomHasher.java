package com.application.gestion.Employee;

import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomHasher {
  private final int hashIterations; // Le nombre d'itérations pour le hachage
  private final Logger logger = LoggerFactory.getLogger(CustomHasher.class);

  @Autowired
  public CustomHasher(@Value("${custom.hasher.iterations}") int hashIterations) {
    this.hashIterations = hashIterations;
    logger.info("CustomHasher bean created with hashIterations: {}", hashIterations);
  }

  public Hash hash(Object source, Object salt) {
    logger.info("Hashing source: {} with salt: {}", source, salt);
    return new SimpleHash("SHA-256", source, salt, hashIterations);
  }
}