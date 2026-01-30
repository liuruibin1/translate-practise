package com.xxx.captcha.service;

import com.google.code.kaptcha.Producer;
import com.xxx.captcha.configuration.properties.CaptchaProperties;
import com.xxx.captcha.constants.CaptchaConstant;
import com.xxx.common.core.utils.uuid.UUIDUtil;
import com.xxx.common.core.vo.BaseResponse;
import com.xxx.framework.redis.service.RedisService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 验证码实现
 */
@Component
public class CaptchaCodeService {

    private final RedisService redisService;

    private final CaptchaProperties captchaProperties;

    //    @Resource(name = "captchaProducer")
    private final Producer captchaProducer;

    //    @Resource(name = "captchaProducerMath")
    private final Producer captchaProducerMath;

    public CaptchaCodeService(
            CaptchaProperties captchaProperties,
            RedisService redisService,
            Producer captchaProducer,
            Producer captchaProducerMath) {
        this.redisService = redisService;
        this.captchaProperties = captchaProperties;
        this.captchaProducer = captchaProducer;
        this.captchaProducerMath = captchaProducerMath;
    }

    /**
     * 生成验证码
     */
    public BaseResponse createCaptcha(String captchaCodeCachePrefix) throws RuntimeException {
        BaseResponse response = BaseResponse.success();
        boolean captchaEnabled = captchaProperties.getEnabled();
        response.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled) {
            return response;
        }
        // 保存验证码信息
        String uuid = UUIDUtil.randomUUID();
        String verifyKey = captchaCodeCachePrefix + uuid;
        String capStr, code = null;
        BufferedImage image = null;
        String captchaType = captchaProperties.getType();
        // 生成验证码
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }
        redisService.opsForValueAndSet(verifyKey, code, CaptchaConstant.EFFECTIVE_DURATION_MINUTES, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            assert image != null;
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return BaseResponse.error(e.getMessage());
        }
        response.put("uuid", uuid);
        byte[] bytes = os.toByteArray();
        response.put("img", "data:image/png;base64," + Base64.encodeBase64String(bytes));
        return response;
    }

    ///**
    // * 生成验证码
    // */
    //public BaseResponse createCaptcha() throws RuntimeException {
    //    BaseResponse response = BaseResponse.success();
    //    boolean captchaEnabled = applicationCaptchaProperties.getEnabled();
    //    response.put("captchaEnabled", captchaEnabled);
    //    if (!captchaEnabled) {
    //        return response;
    //    }
    //    //
    //    CCCaptcha specCaptcha = new CCCaptcha(57, 25, 4, new Font("Microsoft JhengHei", Font.BOLD, 14), new Color(0, 0, 0));
    //    specCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
    //    String code = specCaptcha.text().toLowerCase();
    //    //
    //    String uuid = UUIDUtil.simpleUUID();
    //    String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
    //    //
    //    redisService.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
    //    //
    //    response.put("uuid", uuid);
    //    response.put("img", specCaptcha.toBase64());
    //    return response;
    //}

    /**
     * 校验验证码
     */
    public BaseResponse checkCaptcha(String captchaCodeCachePrefix, String captchaCode, String uuid) throws RuntimeException {
        if (StringUtils.isEmpty(captchaCode)) {
            return BaseResponse.error("captcha code is required");
        }
        if (StringUtils.isEmpty(uuid)) {
            return BaseResponse.error("captcha code invalid");
        }
        String captchaCacheKey = captchaCodeCachePrefix + uuid;
        String captcha = redisService.opsForValueAndGet(captchaCacheKey);
        redisService.delete(captchaCacheKey);
        if (!captchaCode.equalsIgnoreCase(captcha)) {
            return BaseResponse.error("captcha code error");
        }
        return BaseResponse.success();
    }

}
