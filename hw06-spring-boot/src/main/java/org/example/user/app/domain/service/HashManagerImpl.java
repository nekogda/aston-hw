package org.example.user.app.domain.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.user.infra.Log;
import org.example.user.infra.LogLevel;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
@Slf4j
public class HashManagerImpl implements HashManager {

    @Override
    @SneakyThrows
    @Log(LogLevel.DEBUG)
    public String toHash(String data) {
        MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        final byte[] hashBytes = digest.digest(
                data.getBytes(StandardCharsets.UTF_8));
        return toHex(hashBytes);
    }

    private String toHex(byte[] hash) {
        StringBuilder sb = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}