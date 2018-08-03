package com.itheima.dao;

import com.itheima.domain.ModelPage;
import org.apache.solr.client.solrj.SolrQuery;

public interface JDDao<T> {

    ModelPage<T> select(SolrQuery query) throws Exception;

}
