package io.bitnews.model.external;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MobileLoginVo {
    String phoneNumber;
    String password;
}