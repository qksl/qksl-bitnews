package io.bitnews.passport.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Created by ywd on 2019/7/5.
 */
@RestController
@RequestMapping("/v1/passport/authentication")
public class AuthenticationController {

    @GetMapping("current")
    public Principal getUser(Principal principal) {
        return principal;
    }
}
