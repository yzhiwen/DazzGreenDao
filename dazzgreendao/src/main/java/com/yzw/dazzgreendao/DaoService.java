package com.yzw.dazzgreendao;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * 基于GreenDao的数据层服务接口
 * Created by yzw on 2016/7/11 0011.
 */
public interface DaoService {

    <T> void insert(T t);

    <T> void insert(List<T> t);

    <T> void update(T t);

    <T> void delete(T t);

    <T> void deleteAll(Class<T> t);

    <T> List<T> loadAll(Class<T> t);

    <T> QueryBuilder<T> query(Class<T> t);
}
