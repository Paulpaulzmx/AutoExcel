package com.z5n.autoexcel.service.base;

import com.z5n.autoexcel.exception.BusinessException;
import com.z5n.autoexcel.repository.BaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @program: autoexcel
 * @ClassName: AbstractCurdService
 * @Description: AbstractCurdService
 * @Author: chen qi
 * @Date: 2019/12/22 9:59
 * @Version: 1.0
 **/
@Slf4j
public abstract class AbstractCurdService<ENTITY, ID> implements CurdService<ENTITY, ID> {

    private final String entityName;

    private final BaseRepository<ENTITY, ID> repository;

    protected AbstractCurdService(BaseRepository<ENTITY, ID> repository) {
        this.repository = repository;

        Class<ENTITY> entityClass = (Class<ENTITY>) fetchType(0);
        this.entityName = entityClass.getSimpleName();
    }

    /**
     * 获取实际的泛型类型。
     * @param index 泛型索引
     * @return 返回真正的泛型类型
     */
    private Type fetchType(int index) {
        Assert.isTrue(index >= 0 && index <= 1, "type index must be between 0 to 1");

        return ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[index];
    }

    /**
     *  列出所有
     * @return
     */
    @Override
    public List<ENTITY> listAll() {
        return repository.findAll();
    }

    /**
     *   根据id集合查询
     * @param ids ids
     * @return List
     */
    @Override
    public List<ENTITY> listAllByIds(Collection<ID> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : repository.findAllById(ids);
    }

    /**
     *  根据id获取实体
     * @param id id
     * @return Optional
     */
    @Override
    public Optional<ENTITY> fetchById(ID id) {
        Assert.notNull(id, entityName + " id 不能为空");

        return repository.findById(id);
    }

    /**
     * 根据主键查询
     * @param id id
     * @return entity
     */
    @Override
    public ENTITY getById(ID id) {

        //todo 全局异常捕获
        return fetchById(id).orElseThrow(() -> new BusinessException(entityName + "没有找到"));
//        return null;
    }

    /**
     * 根据主键查询
     * @param id id
     * @return 允许为空
     */
    @Override
    public ENTITY getByIdOfNullable(ID id) {
        return fetchById(id).orElse(null);
    }

    /**
     *  count
     */
    @Override
    public long count() {
        return repository.count();
    }

    /**
     * 创建
     * @param entity entity
     * @return entity
     */
    @Override
    public ENTITY create(ENTITY entity) {
        Assert.notNull(entity, entityName + " data不能为空");

        return repository.save(entity);
    }

    /**
     *  批量创建
     * @param entities entities
     * @return List
     */
    @Override
    public List<ENTITY> createInBatch(Collection<ENTITY> entities) {
        return CollectionUtils.isEmpty(entities) ? Collections.emptyList() : repository.saveAll(entities);
    }

    /**
     *  修改
     * @param entity entity
     * @return entity
     */
    @Override
    public ENTITY update(ENTITY entity) {
        Assert.notNull(entity, entityName + " data不能为空");

        return repository.saveAndFlush(entity);
    }

    /**
     *  批量修改
     * @param entities entities
     * @return List
     */
    @Override
    public List<ENTITY> updateInBatch(Collection<ENTITY> entities) {
        return CollectionUtils.isEmpty(entities) ? Collections.emptyList() : repository.saveAll(entities);
    }

    /**
     *   主键删除
     * @param id id
     * @return entity
     */
    @Override
    public ENTITY removeById(ID id) {
        ENTITY entity = getById(id);

        remove(entity);

        return entity;
    }

    /**
     *  删除
     * @param id id
     * @return  允许为空
     */
    @Override
    public ENTITY removeByIdOfNullable(ID id) {
        return fetchById(id).map(entity -> {
            remove(entity);
            return entity;
        }).orElse(null);
    }

    /**
     * 删除
     * @param entity entity
     */
    @Override
    public void remove(ENTITY entity) {
        repository.delete(entity);
    }

    /**
     * 根据id批量删除
     * @param ids ids
     */
    @Override
    public void removeInBatch(Collection<ID> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            log.warn(entityName + " id collection is empty");
            return;
        }
        repository.deleteByIdIn(ids);
    }

    /**
     * 根据实体集合删除
     * @param entities entities
     */
    @Override
    public void removeAll(Collection<ENTITY> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            log.warn(entityName + " entities collection is empty");
            return;
        }
        repository.deleteInBatch(entities);
    }

    /**
     * 删除所有
     */
    @Override
    public void removeAll() {
        repository.deleteAll();
    }
}
