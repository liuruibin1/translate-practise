package com.xxx.auth.controller;

import com.xxx.auth.response.LoginResponse;
import com.xxx.common.core.utils.ObjectUtils;
import com.xxx.common.core.utils.PasswordUtil;
import com.xxx.user.entity.User;
import com.xxx.user.provider.IUserProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.xxx.common.core.enumerate.BizErrorEnum._10_001;

@Tag(name = "前端接口")
@RestController
@RequestMapping("/auth")
public class AuthController extends BaseAuthController {

    @DubboReference
    IUserProvider userProvider;

    @Operation(summary = "用户登录", description = "用户名/邮箱/手机号方式登录")
    @GetMapping("/login")
    @ResponseBody
    public Mono<LoginResponse> login(@RequestParam String username, @RequestParam String password) {
        //判断出账户是 邮件，手机号，用户名
        //User dbUser = userProvider.queryOneByEmail(account);
//        User dbUser = userProvider.queryOneByUsername(username);
//        if (ObjectUtils.isNull(dbUser)) {
//            return Mono.justOrEmpty(LoginResponse.error(_10_001));
//        }
//        if (!PasswordUtil.verifyPassword(password, dbUser.getPassword(), dbUser.getSalt())) {
//            return Mono.justOrEmpty(LoginResponse.error("Invalid account or password"));
//        }
//        return Mono.justOrEmpty(afterLogin(dbUser));
        return null;
    }

    @Operation(summary = "用户注册", description = "注册")
    @GetMapping("/sign-up")
    @ResponseBody
    public Mono<LoginResponse> signUp(
            @RequestParam String username,
            @RequestParam String password
    ) {
        if (password == null || password.isEmpty()) {
            return Mono.justOrEmpty(LoginResponse.error("Password is required"));
        }
        if (password.length() < 8) {
            return Mono.justOrEmpty(LoginResponse.error("Password length should be 8 characters"));
        }
//        if (StringUtils.isNotEmpty(email)) {
//            return signUpEmail(
//                    email,
//                    username,
//                    password,
//                    verificationCode,
//                    referralCode);
        return null;
//        } else {
//            return Mono.justOrEmpty(LoginResponse.error("Delivery method is required"));
//        }
    }

}