package com.ot.cm.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Types;
import java.util.Map;

@Service
public class IMDataServiceImpl implements IMDataService {

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Transactional
    public int getFilter(String buid, String filterCateg_ID) {
        int id = 0;
        try {
            id = jdbcTemplate.queryForObject(
                    "SELECT FILTER_ID FROM GDS_CATEGORIES_FILTERS WHERE BU_ID = ? and CATEGORY_ID= ?",
                    new Object[]{
                            buid,
                            filterCateg_ID
                    }, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return id;
    }


    @Transactional
    public Map<String, Object> getFile(int filterId) {
        Map<String, Object> map = null;
        try {
            map = jdbcTemplate.queryForMap(
                    "SELECT EXTENDED_ID,KEY FROM GDS_FILTERS_EXTENDED WHERE FILTER_ID= ?",
                    new Object[]{
                            filterId
                    }, new int[]{Types.INTEGER});
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }

        return map;
    }

    @Override
    public Map<String, Object> getDocumentMetadata(String buId, String filterCategoryId) {
        int filterId = getFilter(buId, filterCategoryId);
        Map<String, Object> map = getFile(filterId);
        return map;
    }

    @Override
    public Map<String, Object> downloadFile(String extendedId) {
        Map<String, Object> map = null;
        try {
            map = jdbcTemplate.queryForMap(
                    "SELECT KEY,VALUE FROM GDS_FILTERS_EXTENDED WHERE EXTENDED_ID= ?",
                    new Object[]{
                            extendedId
                    }, new int[]{Types.INTEGER});
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return map;
    }


}

