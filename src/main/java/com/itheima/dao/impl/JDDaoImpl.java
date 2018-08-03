package com.itheima.dao.impl;

import com.itheima.dao.JDDao;
import com.itheima.domain.ModelPage;
import com.itheima.domain.Product;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Repository
public class JDDaoImpl implements JDDao {
    @Resource
    private SolrServer solrServer;

    public ModelPage<Product> select(SolrQuery query) throws Exception {
        SolrServer solrServer = new HttpSolrServer("http://localhost:6080/solr/wangnan");
        QueryResponse response = solrServer.query(query);
        SolrDocumentList results = response.getResults();

        ModelPage<Product> model = new ModelPage<>();
        List<Product> productList = new ArrayList<>();

        for (SolrDocument result : results) {
            Product product = new Product();
            //封装id
            String id = (String) result.get("id");
            product.setPid(id);
            //封装product_name并且高亮显示
            String product_name = (String) result.get("product_name");
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            Map<String, List<String>> stringListMap = highlighting.get(id);
            List<String> list = stringListMap.get("product_name");
            if (list!=null && list.size()>0){
                product_name = list.get(0);
            }
            product.setName(product_name);
            //封装product_price价格
            Float product_price = (Float) result.get("product_price");
            product.setPrice(product_price);
            //封装product_picture
            String product_picture = (String) result.get("product_picture");
            product.setPicture(product_picture);

            productList.add(product);
        }
        model.setProductList(productList);

        //开始封装总页
        SolrDocumentList solrDocuments = response.getResults();
        long totalCount = solrDocuments.getNumFound();
        model.setTotalCount((int) totalCount);
        return model;
    }
}
