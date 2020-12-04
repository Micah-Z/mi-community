package org.mi.gateway.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: eladmin-cloud
 * @description: 路由策略
 * @author: Micah
 * @create: 2020-08-15 21:18
 **/
@Data
public class Predicate implements Serializable {

    private static final long serialVersionUID = 1331709878647977544L;

    @TableId(value = "id",type = IdType.AUTO)
    private Long predicateId;


    private String predicateName;

    private String predicateVal;


}
