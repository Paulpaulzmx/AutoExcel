package com.z5n.autoexcel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

/**
 * @program: autoexcel
 * @Interface: BaseRepository
 * @Description: BaseRepository基类仓库   公共repository 不创建spring实例 @NoRepositoryBean
 * @Author: chen qi
 * @Date: 2019/12/22 10:01
 * @Version: 1.0
 **/
@NoRepositoryBean
public interface BaseRepository<ENTITY, ID> extends JpaRepository<ENTITY, ID> {

    /**
     *  根据id批量删除
     * @param ids
     * @return long
     */
    long deleteByUuidIn(@NonNull Iterable<ID> ids);
}
