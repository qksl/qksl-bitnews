package io.bitnews.model.external;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailLoginVo {
    String email;
    String password;
}