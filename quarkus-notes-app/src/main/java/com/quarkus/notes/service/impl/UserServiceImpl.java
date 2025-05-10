package com.quarkus.notes.service.impl;

import com.quarkus.notes.model.User;
import com.quarkus.notes.repository.UserRepository;
import com.quarkus.notes.service.IUserService;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

@RequiredArgsConstructor
@ApplicationScoped
public class UserServiceImpl implements IUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String TAG_NAME = "UserServiceImpl";

    final UserRepository userRepository;

    @Override
    public String authenticate(String username, String password) {
        LOGGER.info("{} :: inside authenticate : username :: password", TAG_NAME);
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(password)) {
            try {
                return generateJwt(optionalUser.get());
            } catch (Exception e) {
                LOGGER.info("{} :: inside authenticate : username :: password : User not authenticated! : ", TAG_NAME, e);
                return null;
            }
        }
        return null;
    }

    private String generateJwt(User user) {
        LOGGER.info("{} :: inside generateJwt : User ", TAG_NAME);
        try {
            PrivateKey privateKey = getPrivateKey();

            return Jwt.claims()
                    .issuer("quarkus-notes-app")
                    .subject(user.getUsername())
                    .groups(Set.of("user"))
                    .expiresAt(System.currentTimeMillis() + 10 * 60 * 1000)
                    .sign(privateKey);
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException("Error generating JWT token", e);
        }
    }

    private PrivateKey getPrivateKey() throws GeneralSecurityException, IOException {
        LOGGER.info("{} :: inside getPrivateKey ", TAG_NAME);
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("privateKey.pem")) {
            if (is == null) {
                throw new FileNotFoundException("privateKey.pem not found in classpath");
            }

            String pem = new String(toByteArray(is), StandardCharsets.UTF_8)
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            byte[] decoded = Base64.getDecoder().decode(pem);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        }
    }

    private byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int nRead;
        while ((nRead = input.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }
}
