package com.xxx.user.controller;

import com.xxx.base.controller.BaseController;
import com.xxx.common.core.utils.BeanUtil;
import com.xxx.common.core.utils.ObjectUtils;
import com.xxx.user.provider.IUserProvider;
import com.xxx.user.response.UserDetail;
import com.xxx.user.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Tag(name = "前端接口")
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @DubboReference
    IUserProvider userProvider;

    @Operation(summary = "用户", description = "用户详情")
    @GetMapping("/info")
    @ResponseBody
    public Mono<UserDetail> info(
            ServerWebExchange exchange,
            @RequestParam(required = false) Long userId) {
        Long userIdTemp = userId;
        if (userIdTemp == null) {
            userIdTemp = getUserIdAsLong(exchange);
        }
        if (ObjectUtils.isNull(userIdTemp)) {
            return Mono.justOrEmpty(java.util.Optional.empty());
        }
        UserVO voParam = queryArgsToObj(UserVO.class);
        voParam.setId(userIdTemp);
        voParam.setJoinSumUserFund(true);
        UserVO userVO = userProvider.queryOne(voParam);
        UserDetail ret = BeanUtil.copyToNewBean(userVO, UserDetail.class);
        return Mono.justOrEmpty(ret);
    }

}