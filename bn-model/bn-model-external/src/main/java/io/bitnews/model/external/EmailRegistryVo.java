package io.bitnews.model.external;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailRegistryVo {
    String email;
    String code;
    String password;
}