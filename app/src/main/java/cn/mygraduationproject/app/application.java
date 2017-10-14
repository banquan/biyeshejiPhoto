package cn.mygraduationproject.app;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created by yu on 2017/8/10.
 */

public class application extends Application{

    /**
     * App有多少个进程，那么onCreate的方法就会执行多少次
     */
    @Override
    public void onCreate() {
        super.onCreate();
        initBomb();
    }


    private void initBomb() {
        Bmob.initialize(this,"cc6b20f0a8ad63b7a930cb97bdffd204");
    }


}
