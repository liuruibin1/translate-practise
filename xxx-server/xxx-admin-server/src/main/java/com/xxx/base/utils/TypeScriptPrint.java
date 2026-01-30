package com.xxx.base.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xxx.mutation.vo.CacheVO;
import com.xxx.system.bo.ReactRouterBO;
import com.xxx.system.bo.ReactRouterMetaBO;
import com.xxx.system.bo.SysUserLoginBO;
import com.xxx.system.vo.*;
import com.xxx.user.vo.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class TypeScriptPrint {

    private static final Map<Class<?>, String> typeMap = new HashMap<>();

    private static final Class<?>[] ADMIN_CLASS_ARRAY = new Class[]{
            //AdminAllocateFeeVO.class,
            //AdminCollectionAccountVO.class,
    };

    private static final Class<?>[] USER_CLASS_ARRAY = new Class[]{
            UserVO.class,
    };

    private static final Class<?>[] MAINTAIN_CLASS_ARRAY = new Class[]{CacheVO.class};

    private static final Class<?>[] SYSTEM_CLASS_ARRAY = new Class[]{
            ReactRouterBO.class,
            ReactRouterMetaBO.class,
            SysUserLoginBO.class,
            SysDeptVO.class,
            SysOperationLogVO.class,
            SysPermissionVO.class,
            SysRolePermissionVO.class,
            SysRoleVO.class,
            SysUserLoginLogVO.class,
            SysUserRoleVO.class,
            SysUserVO.class,
    };

    static {
        typeMap.put(int.class, "number");
        typeMap.put(Integer.class, "number");
        typeMap.put(long.class, "number");
        typeMap.put(Long.class, "number");
        typeMap.put(boolean.class, "boolean");
        typeMap.put(Boolean.class, "boolean");
        typeMap.put(String.class, "string");
        typeMap.put(BigDecimal.class, "number");
        typeMap.put(Date.class, "number");
    }


    private static void writeTsTypeDefs(Path outFile, Class<?>[] classArray) throws IOException {
        Files.createDirectories(outFile.getParent());
        Files.writeString(
                outFile,
                buildAll(classArray),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    private static String buildAll(Class<?>[] classArray) {
        StringBuilder sb = new StringBuilder();
        for (Class<?> clazz : classArray) {
            sb.append(buildOne(clazz)).append("\n\n");
        }
        return sb.toString();
    }

    private static String buildOne(Class<?> voClass) {
        String name = voClass.getSuperclass().getSimpleName();
        if (name.equals("Object")) {
            name = voClass.getSimpleName();
        }
        if (name.endsWith("VO")) {
            name = name.replace("VO", "");
        } else if (name.endsWith("BO")) {
            name = name.replace("BO", "");
        }

        List<Field> allFields = new ArrayList<>();
        allFields.addAll(List.of(getAllFields(voClass.getSuperclass())));
        allFields.addAll(List.of(getAllFields(voClass)));

        StringBuilder sb = new StringBuilder();
        appendVOType(sb, allFields, name);
        appendDTOType(sb, allFields, name);
        return sb.toString();
    }

    private static void appendVOType(StringBuilder sb, List<Field> fields, String name) {
        sb.append("// begin ").append(name).append("\n");
        sb.append("export type ").append(name).append(" = {\n");
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) continue;

            JsonIgnore jsonIgnore = field.getAnnotation(JsonIgnore.class);
            if (jsonIgnore != null) continue;

            JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
            if (jsonProperty != null && jsonProperty.access() == JsonProperty.Access.WRITE_ONLY) continue;

            String graphQLType = resolveType(field);
            sb.append("  ").append(field.getName()).append("?: ").append(graphQLType).append("\n");
        }
        sb.append("}");
    }

    private static void appendDTOType(StringBuilder sb, List<Field> fields, String name) {
        sb.append("\n");
        sb.append("export type ").append(name).append("DTO = {\n");
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) continue;

            JsonIgnore jsonIgnore = field.getAnnotation(JsonIgnore.class);
            if (jsonIgnore != null) continue;

            JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
            if (jsonProperty != null && jsonProperty.access() == JsonProperty.Access.READ_ONLY) continue;

            String graphQLType = resolveType(field);
            sb.append("  ").append(field.getName()).append("?: ").append(graphQLType).append("\n");
        }
        sb.append("}\n// end ").append(name);
    }

    private static Field[] getAllFields(Class<?> clazz) {
        if (clazz == null || clazz == Object.class) return new Field[0];
        return clazz.getDeclaredFields();
    }

    private static String resolveType(Field field) {
        Class<?> fieldType = field.getType();
        if (Collection.class.isAssignableFrom(fieldType)) {
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType pt) {
                Type[] typeArgs = pt.getActualTypeArguments();
                if (typeArgs.length == 1) {
                    Type t = typeArgs[0];
                    if (t instanceof Class<?> genericClass) {
                        return mapToType(genericClass) + "[]";
                    }
                }
            }
            return "[Object]";
        }
        return mapToType(fieldType);
    }

    private static String mapToType(Class<?> clazz) {
        String name = clazz.getSimpleName();
        if (name.endsWith("VO")) {
            name = name.replace("VO", "");
        } else if (name.endsWith("BO")) {
            name = name.replace("BO", "");
        }
        return typeMap.getOrDefault(clazz, name);
    }

    private static void generateToTsFile(String baseRootDir,
                                         String subDir,
                                         String fileName,
                                         Class<?>[] classArray) {
        try {
            Path outFile = Paths.get(baseRootDir, subDir, fileName);

            // 1) 生成 SDL
            String sdl = buildAll(classArray);

            // 2) 包装成 TS
            String content = sdl + (sdl.endsWith("\n") ? "" : "\n");

            // 3) 创建目录 + 覆盖写入
            Files.createDirectories(outFile.getParent());
            Files.writeString(outFile, content, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("✅ Generated: " + outFile.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Generate type.ts failed: " + subDir + "/" + fileName, e);
        }
    }

    public static void main(String[] args) {
        String baseRootDir = "/Users/mac/Documents/project/385-game/code/game-admin/src/services";
        generateToTsFile(baseRootDir, "admin", "type.ts", ADMIN_CLASS_ARRAY);
        generateToTsFile(baseRootDir, "user", "type.ts", USER_CLASS_ARRAY);
        generateToTsFile(baseRootDir, "maintain", "type.ts", MAINTAIN_CLASS_ARRAY);
        generateToTsFile(baseRootDir, "sys", "type.ts", SYSTEM_CLASS_ARRAY);
    }

}