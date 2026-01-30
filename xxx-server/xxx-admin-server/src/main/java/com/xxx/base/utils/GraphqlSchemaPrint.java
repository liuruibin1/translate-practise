package com.xxx.base.utils;//package com.xxx.base.utils;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.xxx.admin.vo.AdminAllocateFeeVO;
//import com.xxx.admin.vo.AdminCollectionAccountVO;
//import com.xxx.admin.vo.AdminCryptoAccountFundVO;
//import com.xxx.admin.vo.AdminCryptoAccountVO;
//import com.xxx.chain.vo.ChainBlockHeightVO;
//import com.xxx.chain.vo.ChainRpcVO;
//import com.xxx.chain.vo.ChainVO;
//import com.xxx.country.vo.LanguageVO;
//import com.xxx.currency.vo.*;
//import com.xxx.game.vo.*;
//import com.xxx.notification.vo.*;
//import com.xxx.rollover.vo.RolloverBonusConfigVO;
//import com.xxx.rollover.vo.RolloverConfigVO;
//import com.xxx.stats.vo.*;
//import com.xxx.system.vo.*;
//import com.xxx.task.vo.TaskVO;
//import com.xxx.user.vo.*;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Modifier;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.math.BigDecimal;
//import java.util.*;
//
//public class GraphqlSchemaPrint {
//
//    private static final Map<Class<?>, String> typeMap = new HashMap<>();
//
//    private static final Class<?>[] ADMIN_CLASS_ARRAY = new Class[]{
//            AdminAllocateFeeVO.class,
//            AdminCollectionAccountVO.class,
//            AdminCryptoAccountVO.class,
//            AdminCryptoAccountFundVO.class
//    };
//
//    private static final Class<?>[] USER_CLASS_ARRAY = new Class[]{
//            //UserVO.class,
//            UserAnnouncementVO.class,
//            UserBonusVO.class,
//            UserCryptoAccountVO.class,
//            UserCryptoAccountFundVO.class,
//            UserCryptoDepositVO.class,
//            UserCryptoPaymentMethodVO.class,
//            UserCryptoWithdrawVO.class,
//            //UserDailyBetCommissionVO.class,
//            //UserDailyBetRebateVO.class,
//            UserDepositStatisticsVO.class,
//            UserFiatDepositVO.class,
//            UserFiatDepositCallbackVO.class,
//            UserFiatPaymentMethodVO.class,
//            UserFiatWithdrawVO.class,
//            UserFiatWithdrawCallbackVO.class,
//            //UserFundVO.class,
//            UserFundFlowVO.class,
//            UserFundFreezeVO.class,
//            UserFundFreezeDetailVO.class,
//            //UserFundHedgeVO.class,
//            UserGameFavoriteVO.class,
//            UserGameLikeVO.class,
//            UserGameOrderVO.class,
//            UserGameOrderNewGamingVO.class,
//            UserGameOrderNovaBetVO.class,
//            UserGameOrderQuickGamingVO.class,
//            UserGameRecentVO.class,
//            UserGameRecordLuckyMatchVO.class,
//            UserGameServiceVendorFundVO.class,
//            UserItemVO.class,
//            UserItemOrderVO.class,
//            UserItemUsageVO.class,
//            UserLoginLogVO.class,
//            //UserMetricsVO.class,
//            UserPreferenceVO.class,
//            UserRolloverVO.class,
//            UserRolloverDetailsVO.class,
//            UserSettingsVO.class,
//            UserTaskRecordVO.class,
//            UserTransferVO.class,
//            UserWeekLuckyRaffleVO.class,
//    };
//
//    private static final Class<?>[] CHAIN_CLASS_ARRAY = new Class[]{
//            ChainVO.class,
//            ChainBlockHeightVO.class,
//            ChainRpcVO.class
//    };
//
//    private static final Class<?>[] CURRENCY_CLASS_ARRAY = new Class[]{
//            CryptoCurrencyVO.class,
//            CurrencyVO.class,
//            CurrencyPriceVO.class,
//            CurrencyPriceHistoryVO.class,
//            FiatChannelVO.class,
//            FiatChannelServiceVendorVO.class,
//            FiatChannelServiceVendorApiLogVO.class,
//            FiatCurrencyVO.class,
//            FiatCurrencyQuickAmountVO.class,
//    };
//
//    private static final Class<?>[] GAME_CLASS_ARRAY = new Class[]{
//            GameVO.class,
//            GameBetOptionVO.class,
//            GameBetOptionGroupVO.class,
//            GameBetTypeVO.class,
//            GameCategoriesVO.class,
//            GameCategoryVO.class,
//            GameCategoryTranslationVO.class,
//            GameHotVO.class,
//            GameItemVO.class,
//            GameLevelLuckyMatchVO.class,
//            GameOddsVO.class,
//            GameProviderVO.class,
//            GameProviderTranslationVO.class,
//            GameRoundVO.class,
//            GameRoundPresetVO.class,
//            //GameDisplaySectionVO.class,
//            //GameDisplaySectionTranslationVO.class,
//            GameServiceVendorVO.class,
//            GameServiceVendorApiLogVO.class,
//            GameSourceCategoryVO.class,
//            GameSourceProviderVO.class,
//            GameSourceProviderCurrencyVO.class,
//            GameSourceProviderDeviceTypeVO.class,
//            GameSourceProviderLanguageVO.class,
//            GameTagVO.class,
//            GameTagTranslationVO.class,
//            GameTagsVO.class,
//            GameTranslationVO.class,
//            RoomVO.class,
//    };
//
//    private static final Class<?>[] COUNTRY_CLASS_ARRAY = new Class[]{LanguageVO.class};
//
//    private static final Class<?>[] NOTIFICATION_CLASS_ARRAY = new Class[]{
//            AnnouncementVO.class,
//            EmailSendLogVO.class,
//            EventVO.class,
//            NotificationVO.class,
//            SiteMessageVO.class,
//            SmsSendLogVO.class,
//            SmsVendorVO.class,
//            TelegramSendLogVO.class,
//            VerificationCodeVO.class
//    };
//
//    private static final Class<?>[] ROLLOVER_CLASS_ARRAY = new Class[]{RolloverBonusConfigVO.class, RolloverConfigVO.class};
//
//    private static final Class<?>[] TASK_CLASS_ARRAY = new Class[]{TaskVO.class};
//
//    private static final Class<?>[] STATS_CLASS_ARRAY = new Class[]{
//            StatsGameBetVO.class,
//            StatsUserBetVO.class,
//            StatsUserDailyBetDepositWithdrawVO.class,
//            StatsUserDailyBetVO.class,
//            StatsUserDailyDepositWithdrawVO.class};
//
//    private static final Class<?>[] SYSTEM_CLASS_ARRAY = new Class[]{
//            SysDeptVO.class,
//            SysOperationLogVO.class,
//            SysPermissionVO.class,
//            SysRoleVO.class,
//            SysRolePermissionVO.class,
//            //SysUserVO.class,
//            SysUserLoginLogVO.class,
//            SysUserRoleVO.class
//    };
//
//    static {
//        typeMap.put(int.class, "Int");
//        typeMap.put(Integer.class, "Int");
//        typeMap.put(long.class, "Long");
//        typeMap.put(Long.class, "Long");
//        typeMap.put(boolean.class, "Boolean");
//        typeMap.put(Boolean.class, "Boolean");
//        typeMap.put(String.class, "String");
//        typeMap.put(BigDecimal.class, "BigDecimal");
//        typeMap.put(Date.class, "Timestamp");
//    }
//
//    public static void printFragment(List<Field> fields, String name) {
//        System.out.println("type " + name + "F {");
//        for (Field field : fields) {
//            int modifiers = field.getModifiers();
//            if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) {
//                continue; // 跳过 static/final 字段
//            }
//            JsonIgnore jsonIgnore = field.getAnnotation(JsonIgnore.class);
//            if (jsonIgnore != null) {
//                continue;
//            }
//            JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
//            if (jsonProperty != null) {
//                JsonProperty.Access access = jsonProperty.access();
//                if (access == JsonProperty.Access.WRITE_ONLY) {
//                    continue;
//                }
//            }
//            String graphQLType = resolveGraphQLType(field);
//            System.out.println("    " + field.getName() + ": " + graphQLType);
//        }
//        System.out.println("}");
//    }
//
//    public static void printPageFragment(String name) {
//        System.out.println("type " + name + "PageF {");
//        System.out.println("    records: [" + name + "F]");
//        System.out.println("    total: Long");
//        System.out.println("    size: Long");
//        System.out.println("    current: Long");
//        System.out.println("}");
//    }
//
//    public static void printInputArgs(List<Field> fields, String name) {
//        System.out.println("input " + name + "I {");
//
//        for (Field field : fields) {
//            //Annotation[] annotations = field.getAnnotations();
//            //Optional<Annotation> optional = Arrays
//            //        .stream(annotations)
//            //        //.peek(i -> System.out.println(i.annotationType().getSimpleName()))
//            //        .filter(i -> i.annotationType().getSimpleName().equals("JsonIgnore"))
//            //        .findAny();
//            //if (optional.isPresent()) {
//            //    continue;
//            //}
//            int modifiers = field.getModifiers();
//            if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) {
//                continue; // 跳过 static/final 字段
//            }
//            JsonIgnore jsonIgnore = field.getAnnotation(JsonIgnore.class);
//            if (jsonIgnore != null) {
//                continue;
//            }
//            JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
//            if (jsonProperty != null) {
//                JsonProperty.Access access = jsonProperty.access();
//                if (access == JsonProperty.Access.READ_ONLY) {
//                    continue;
//                }
//            }
//            String graphQLType = resolveGraphQLType(field);
//            System.out.println("    " + field.getName() + ": " + graphQLType);
//        }
//
//        System.out.println("}");
//    }
//
//    /**
//     * 递归获取类及其父类的所有字段
//     */
//    private static Field[] getAllFields(Class<?> clazz) {
//        //List<Field> fields = new ArrayList<>();
//        //while (clazz != null && clazz != Object.class) {
//        //    fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
//        //    clazz = clazz.getSuperclass();
//        //}
//        //return fields;
//        return clazz.getDeclaredFields();
//        //return Arrays.asList(declaredFields);
//    }
//
//    private static String resolveGraphQLType(Field field) {
//        Class<?> fieldType = field.getType();
//
//        // 如果是 List<T>
//        if (Collection.class.isAssignableFrom(fieldType)) {
//            Type genericType = field.getGenericType();
//            if (genericType instanceof ParameterizedType) {
//                Type[] typeArgs = ((ParameterizedType) genericType).getActualTypeArguments();
//                if (typeArgs.length == 1) {
//                    Class<?> genericClass = (Class<?>) typeArgs[0];
//                    return "[" + mapToGraphQLType(genericClass) + "]";
//                }
//            }
//            return "[Object]";
//        }
//
//        return mapToGraphQLType(fieldType);
//    }
//
//    private static String mapToGraphQLType(Class<?> clazz) {
//        String name = clazz.getSimpleName();
//        if (name.endsWith("VO")) {
//            name = name.replace("VO", "F");
//        }
//        return typeMap.getOrDefault(clazz, name);
//    }
//
//    private static String toSnakeCase(String name) {
//        if (name == null || name.isEmpty()) return name;
//
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < name.length(); i++) {
//            char c = name.charAt(i);
//            if (Character.isUpperCase(c)) {
//                if (i > 0) sb.append('_');
//                sb.append(Character.toLowerCase(c));
//            } else {
//                sb.append(c);
//            }
//        }
//        return sb.toString();
//    }
//
//    private static void print(Class<?> voClass) {
//        String name = voClass.getSuperclass().getSimpleName();
//        if (name.equals("Object")) {
//            name = voClass.getSimpleName();
//        }
//        if (name.endsWith("VO")) {
//            name = name.replace("VO", "");
//        }
//        List<Field> allFields = new ArrayList<>();
//        allFields.addAll(List.of(getAllFields(voClass.getSuperclass())));
//        allFields.addAll(List.of(getAllFields(voClass)));
//        System.out.println("# begin " + toSnakeCase(name));
//        printFragment(allFields, name);
//        printPageFragment(name);
//        printInputArgs(allFields, name);
//        System.out.println("# end " + toSnakeCase(name));
//    }
//
//    private static void printArray(Class<?>[] classArray) {
//        for (Class<?> clazz : classArray) {
//            print(clazz);
//            System.out.println("\n\n");
//        }
//    }
//
//    public static void main(String[] args) {
////        printArray(ADMIN_CLASS_ARRAY);
//        printArray(USER_CLASS_ARRAY);
////        printArray(CHAIN_CLASS_ARRAY);
////        printArray(CURRENCY_CLASS_ARRAY);
////        printArray(GAME_CLASS_ARRAY);
////        printArray(COUNTRY_CLASS_ARRAY);
////        printArray(NOTIFICATION_CLASS_ARRAY);
////        printArray(ROLLOVER_CLASS_ARRAY);
////        printArray(TASK_CLASS_ARRAY);
////        printArray(STATS_CLASS_ARRAY);
////        printArray(SYSTEM_CLASS_ARRAY);
//    }
//
//}