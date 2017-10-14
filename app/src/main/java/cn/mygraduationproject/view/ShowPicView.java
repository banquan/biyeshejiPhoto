package cn.mygraduationproject.view;

import java.util.ArrayList;

import cn.mygraduationproject.model.AllPicInfo;

/**
 * Created by yu on 2017/10/11.
 */

public interface ShowPicView {

    void uploadPicSuccess();

    void saveAllPicInfoSuccess();


    void getAllPicInfoListSuccess(ArrayList<AllPicInfo> arr);

    void getListSuccess(ArrayList<AllPicInfo> arr);

    void uploadPicProgress(Integer value);

    void showProgressBar();
}
