package org.example.user.rest.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class ChangePasswordRequest {
    String oldPassword;
    String newPassword;
}