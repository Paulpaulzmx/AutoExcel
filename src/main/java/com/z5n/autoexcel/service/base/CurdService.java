package com.z5n.autoexcel.service.base;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @program: autoexcel
 * @Interface: CurdService  ENTITY实体  ID 主键
 * @Description: curd接口类
 * @Author: chen qi
 * @Date: 2019/12/22 9:37
 * @Version: 1.0
 **/
public interface CurdService<ENTITY, ID> {

    /**
     *  List All
     * @return List
     */
    @NonNull
    List<ENTITY> listAll();

    /**
     * List all by ids
     *
     * @param ids ids
     * @return List
     */
    @NonNull
    List<ENTITY> listAllByIds(@Nullable Collection<ID> ids);

    /**
     *  通过id查询
     * @param id id
     * @return Optional
     */
    @NonNull
    Optional<ENTITY> fetchById(@NonNull ID id);

    /**
     * id查询
     * @param id id
     * @return ENTITY
     * @throws NotFoundException 不存在
     */
    @NonNull
    ENTITY getById(@NonNull ID id);

    /**
     * 查询允许为空
     * @param id id
     * @return ENTITY
     */
    @Nullable
    ENTITY getByIdOfNullable(@NonNull ID id);

    /**
     * count
     * @return long
     */
    long count();


    // todo 异常类未指定
    /**
     * create
     * @param entity entity
     * @return ENTITY
     */
    @NonNull
    @Transactional
    ENTITY create(@NonNull ENTITY entity);

    /**
     * save list
     * @param entities entities
     * @return List
     */
    @NonNull
    @Transactional
    List<ENTITY> createInBatch(@NonNull Collection<ENTITY> entities);

    /**
     * Updates
     * @param entity entity
     * @return ENTITY
     */
    @NonNull
    @Transactional
    ENTITY update(@NonNull ENTITY entity);


    /**
     * Updates by entities
     *
     * @param entities entities
     * @return List
     */
    @NonNull
    @Transactional
    List<ENTITY> updateInBatch(@NonNull Collection<ENTITY> entities);

    /**
     * Removes by id
     *
     * @param id id
     * @return ENTITY
     * @throws NotFoundException If the specified id does not exist
     */
    @NonNull
    @Transactional
    ENTITY removeById(@NonNull ID id);

    /**
     * Removes by id if present.
     *
     * @param id id
     * @return ENTITY
     */
    @Nullable
    @Transactional
    ENTITY removeByIdOfNullable(@NonNull ID id);

    /**
     * Remove by entity
     *
     * @param entity entity
     */
    @Transactional
    void remove(@NonNull ENTITY entity);

    /**
     * Remove by ids
     *
     * @param ids ids
     */
    @Transactional
    void removeInBatch(@NonNull Collection<ID> ids);

    /**
     * Remove all by entities
     *
     * @param entities entities
     */
    @Transactional
    void removeAll(@NonNull Collection<ENTITY> entities);

    /**
     * Remove all
     */
    @Transactional
    void removeAll();
}
