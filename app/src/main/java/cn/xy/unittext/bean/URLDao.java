package cn.xy.unittext.bean;

import java.util.List;

import cn.xy.unittext.MyApplication;
import cn.xy.unittext.gen.URLBeanDao;

/**
 * Created by p on 2018/5/29.
 * 增删查改
 */
public class URLDao {

    /**
     * 添加数据，如果有重复则覆盖
     */
    public static void insertURL(URLBean urlBean) {

        List<URLBean> listURLBeans = MyApplication.getInstance().getDaoInstant().queryBuilder(URLBean.class).where(URLBeanDao.Properties.Url.eq(urlBean.url)).list();
        if(listURLBeans.size() ==0) {
            MyApplication.getInstance().getDaoInstant().insertOrReplace(urlBean);
        }
    }

    /**
     * 删除数据
     */
    public static void deleteURL(URLBean urlBean) {
        MyApplication.getInstance().getDaoInstant().delete(urlBean);
    }

    /**
     * 查询数据
     */
    public static List<URLBean> queryAll() {
        return MyApplication.getInstance().getDaoInstant().loadAll(URLBean.class);
    }

    /**
     * 更新数据
     */
    public static void updateURL(URLBean urlBean) {
        MyApplication.getInstance().getDaoInstant().update(urlBean);
    }

    /**
     * 查询条件id在1到20之间的数据
     */
    public static List<URLBean> queryPartURL() {
        return MyApplication.getInstance().getDaoInstant().queryBuilder(URLBean.class).where(URLBeanDao.Properties.Id.between(1, 20)).list();
    }
}
