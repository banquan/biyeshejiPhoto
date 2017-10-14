package cn.mygraduationproject.presenter;

import java.util.List;

import cn.mygraduationproject.model.ImageItemViewInfo;

/**
 * Created by yu on 2017/10/9.
 */

public interface BtnAddAlbumPresenter {


    void UploadToBmob(String title);


    List<ImageItemViewInfo> getList();

    void isExistedAlbumName(String s);
}
