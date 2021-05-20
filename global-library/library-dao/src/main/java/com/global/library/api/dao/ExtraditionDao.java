package com.global.library.api.dao;

import com.global.library.entity.Extradition;
import org.springframework.stereotype.Repository;

@Repository
public class ExtraditionDao extends AGenericDao<Extradition> implements IAGenericDao<Extradition> {
    public ExtraditionDao() {
        super(Extradition.class);
    }
}
