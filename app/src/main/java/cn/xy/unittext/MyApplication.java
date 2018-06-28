package cn.xy.unittext;

import android.app.Application;
import android.content.Context;

import cn.xy.unittext.bean.ConstantValue;
import cn.xy.unittext.gen.DaoMaster;
import cn.xy.unittext.gen.DaoSession;
import cn.xy.unittext.gen.URLBeanDao;

/**
 * Created by p on 18/5/27.
 */
public class MyApplication extends Application {

    private static MyApplication INSTANCE;
    private DaoSession mDaoSession;
    private DaoMaster mDaoMaster;
    public static URLBeanDao mDao;
    private DBHelper mDbHelper;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public static MyApplication getInstance() {
        if (INSTANCE == null) {
            synchronized (MyApplication.class) {
                INSTANCE = new MyApplication();
            }
        }
        return INSTANCE;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        INSTANCE = this;
        //配置数据库
        setupDatabase();
    }

    /**
     * 配置数据库
     */
    private void setupDatabase() {
        //未涉及到数据库的升级时的写法
      /*  //创建数据库urlAddress.db"
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ConstantValue.URLAdress, null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        mDaoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        mDaoSession = mDaoMaster.newSession();
        mDao = (URLBeanDao) mDaoSession.getDao(URLBean.class);*/

      //数据库的升级的写法
        mDbHelper = new DBHelper(this, ConstantValue.URLAdress, null);
    }

    public DaoSession getDaoInstant() {
       // return mDaoSession;
      return   new DaoMaster(mDbHelper.getWritableDatabase()).newSession();
    }

    public DaoMaster getmDaoMaster() {
        return mDaoMaster;
    }

}
