package com.xxx.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.base.enumerate.OperatorTypeEnum;
import com.xxx.base.service.BaseServiceImpl;
import com.xxx.common.core.utils.JSONUtil;
import com.xxx.common.core.utils.ObjectUtils;
import com.xxx.system.entity.SysOperationLog;
import com.xxx.system.enumerate.SysOperationLogActionTypeEnum;
import com.xxx.system.enumerate.SysOperationLogEntityEnum;
import com.xxx.system.mapper.SysOperationLogMapper;
import com.xxx.system.vo.SysOperationLogVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class SysOperationLogService extends BaseServiceImpl<SysOperationLog, SysOperationLogVO, SysOperationLogMapper> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysOperationLogService.class);

    private final SysOperationLogMapper mapper;

    public SysOperationLogService(SysOperationLogMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public IPage<SysOperationLogVO> queryPage(Page<SysOperationLogVO> pageParam, SysOperationLogVO voParam) {
        return mapper.queryPage(pageParam, voParam);
    }

    public void recordInsert(SysOperationLogEntityEnum entityName, OperatorTypeEnum operatorType, Long operatorId, Object data) {
        record(SysOperationLogActionTypeEnum.INSERT, entityName, operatorType, operatorId, data);
    }

    public void recordUpdate(SysOperationLogEntityEnum entityName, OperatorTypeEnum operatorType, Long operatorId, Object data) {
        record(SysOperationLogActionTypeEnum.UPDATE, entityName, operatorType, operatorId, data);
    }

    public void recordDelete(SysOperationLogEntityEnum entityName, OperatorTypeEnum operatorType, Long operatorId, Object data) {
        record(SysOperationLogActionTypeEnum.DELETE, entityName, operatorType, operatorId, data);
    }

    public void record(
            SysOperationLogActionTypeEnum actionTypeEnum,
            SysOperationLogEntityEnum entityName,
            OperatorTypeEnum operatorTypeEnum,
            Long operatorId,
            Object data) {
        try {
            //去掉null的字段
            Map<Object, Object> newObjectMap = trimNull(data);
            //
            SysOperationLog entity = new SysOperationLog();
            //entity.setId(simpleUUID());
            entity.setEntityId(entityName.getId());
            entity.setOperatorId(operatorId);
            entity.setOperatorType(operatorTypeEnum.getType());
            entity.setActionType(actionTypeEnum.getValue());
            entity.setData(JSONUtil.mapToString(newObjectMap));
            mapper.insert(entity);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 去掉null的字段
     *
     * @param data
     * @return
     */
    private Map<Object, Object> trimNull(Object data) {
        Map<Object, Object> retObjectMap = new HashMap<>();
        if (ObjectUtils.isNotNull(data)) {
            String objString = JSONUtil.objectToString(data);
            HashMap<?, ?> dataMap = JSONUtil.stringToObject(objString, HashMap.class);
            if (ObjectUtils.isNotNull(dataMap)) {
                //Map<String, Object> dataMap = Collections.unmodifiableMap((HashMap<String, Object>) data);
                Set<?> keySet = dataMap.keySet();
                for (Object key : keySet) {
                    Object value = dataMap.get(key);
                    if (value != null) {
                        retObjectMap.put(key, value);
                    }
                }
            }
        }
        return retObjectMap;
    }

}
