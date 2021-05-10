package com.global.library.api.dao;

import com.global.library.entity.Publisher;

public interface IPublisherDao extends IAGenericDao<Publisher>{

    Publisher getByName(String name);

    boolean isPublisherExist(String name);
}
