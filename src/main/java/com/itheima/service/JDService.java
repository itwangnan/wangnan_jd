package com.itheima.service;

import com.itheima.domain.ModelPage;
import com.itheima.domain.Product;

public interface JDService<T>  {

    ModelPage<T> select(String queryString, String catalog_name, String price,String sort,Integer curPage,Integer curCount) throws Exception;
}
