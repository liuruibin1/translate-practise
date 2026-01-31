package com.xxx.controller;

import com.xxx.base.controller.BaseController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {


//    @GetMapping("/info")
//    @ResponseBody
//    public Mono<UserDetail> info(
//            ServerWebExchange exchange,
//            @RequestParam(required = false) Long userId) {
//        Long userIdTemp = userId;
//        if (userIdTemp == null) {
//            userIdTemp = getUserIdAsLong(exchange);
//        }
//        if (ObjectUtils.isNull(userIdTemp)) {
//            return Mono.justOrEmpty(java.util.Optional.empty());
//        }
//        UserVO voParam = queryArgsToObj(UserVO.class);
//        voParam.setId(userIdTemp);
//        voParam.setJoinSumUserFund(true);
//        UserVO userVO = userProvider.queryOne(voParam);
//        UserDetail ret = BeanUtil.copyToNewBean(userVO, UserDetail.class);
//        return Mono.justOrEmpty(ret);
//    }

}