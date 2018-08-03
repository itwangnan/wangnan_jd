package com.itheima.service.impl;

import com.itheima.dao.JDDao;
import com.itheima.domain.ModelPage;
import com.itheima.domain.Product;
import com.itheima.service.JDService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.Query;
@Service
public class JDServiceImpl implements JDService{
    @Resource
    private JDDao jdDao;


    @Override
    public ModelPage<Product> select(String queryString, String catalog_name, String price,String sort,Integer curPage,Integer curCount) throws Exception {
        SolrQuery solrQuery = new SolrQuery();
        //设置主查询条件
        if (queryString!=null && !"".equals(queryString.trim())) {
            solrQuery.setQuery("product_keywords:"+queryString);
        }else {
            solrQuery.setQuery("*:*");
        }
        //添加过滤查询条件(分类)
        if (catalog_name != null && !"".equals(catalog_name.trim())) {
            solrQuery.addFilterQuery("product_picture:" + catalog_name);
        }
        //添加过滤查询条件(价格范围)
        if (price != null && !"".equals(price.trim())) {
            String[] split = price.split("-");
            solrQuery.addFilterQuery("product_price:[" + split[0] + " TO " + split[1] + "]");
        }
        //设置排序
        if (sort.equals("1")){
            solrQuery.setSort("product_price", SolrQuery.ORDER.desc);
        }else {
            solrQuery.setSort("product_price", SolrQuery.ORDER.asc);
        }
        //设置分页
        solrQuery.setStart((curPage-1)*curCount);
        solrQuery.setRows(curCount);
        //设置默认查询域
//        solrQuery.set("df","product_name");
        //设置高亮
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("product_name");
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");

        ModelPage<Product> modelPage = jdDao.select(solrQuery);
        int totalPage = (int) Math.ceil(1.0 * modelPage.getTotalCount()/curCount);
        modelPage.setTotalPage(totalPage);
        modelPage.setCurCount(curCount);
        modelPage.setCurPage(curPage);
        return modelPage;
    }
}
