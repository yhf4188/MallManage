package com.yhf.pointsmanage.service;

import com.yhf.pointsmanage.dao.MallDao;
import com.yhf.pointsmanage.entity.Mall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MallService {

    @Autowired
    private MallDao mallDao;

    public List<Mall> getAllMall()
    {
        try {
            return mallDao.getAllMall();
        } catch (Exception e)
        {
            throw e;
        }
    }
}
