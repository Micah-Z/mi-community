package org.common.mp.component;

import java.util.List;

/**
 * @program: eladmin-cloud
 * @description: 映射基本接口
 * @author: Micah
 * @create: 2020-07-30 15:34
 **/
public interface BaseMapStruct<D, E> {
    /**
     * DTO转Entity
     * @param dto /
     * @return /
     */
    E toEntity(D dto);

    /**
     * Entity转DTO
     * @param entity /
     * @return /
     */
    D toDto(E entity);

    /**
     * DTO集合转Entity集合
     * @param dtoList /
     * @return /
     */
    List<E> toEntity(List<D> dtoList);

    /**
     * Entity集合转DTO集合
     * @param entityList /
     * @return /
     */
    List <D> toDto(List<E> entityList);
}