package cn.mygraduationproject;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.mygraduationproject.adapter.AlbumAdpater;
import cn.mygraduationproject.model.AllPicInfo;
import cn.mygraduationproject.presenter.BtnAddAlbumPresenter;
import cn.mygraduationproject.presenter.impl.BtnAddAlbumPresenterImpl;
import cn.mygraduationproject.view.BtnAddAlbumView;


public class MainActivity extends AppCompatActivity implements BtnAddAlbumView {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.btn_add_album)
    Button mBtnAddAlbum;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private AlbumAdpater mAlbumAdpater;
    private BtnAddAlbumPresenter mBtnAddAlbumPresenter;
    private boolean isExistAlbumName = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
        ininRecyclerView();



    }

    private void init() {
        mBtnAddAlbumPresenter = new BtnAddAlbumPresenterImpl(this);
        initSwipeRefreshLayout();


        //侧滑删除
        //initSlideDelete();

    }

    private void initSlideDelete() {
        ItemTouchHelper helper = new ItemTouchHelper(new MyItemTouchCallback(mAlbumAdpater));
        helper.attachToRecyclerView(mRecyclerView);

    }


    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.qq_blue, R.color.colorAccent, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Toast.makeText(MainActivity.this, "刷新啦~~~", Toast.LENGTH_SHORT).show();
            mAlbumAdpater.refreshAlbumAdpater(mBtnAddAlbumPresenter.getList());
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    private void ininRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAlbumAdpater = new AlbumAdpater(this, mBtnAddAlbumPresenter.getList());
        mRecyclerView.setAdapter(mAlbumAdpater);
    }

    @OnClick(R.id.btn_add_album)
    public void onClick() {
        showAlbumNameInputDialog(mBtnAddAlbumPresenter);
    }

    protected void showAlbumNameInputDialog(final BtnAddAlbumPresenter mBtnAddAlbumPresenter) {
        final EditText et_name = new EditText(MainActivity.this);
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("aaaaaaaa", "beforeTextChanged: " + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("aaaaaaaa", "onTextChanged: " + s);
                //监听输入框的文字变化，然后判断是否与已在相册同名
                mBtnAddAlbumPresenter.isExistedAlbumName(String.valueOf(s));

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("aaaaaaaa", "afterTextChanged: " + s);

            }
        });
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Write You AlbumName")
                .setPositiveButton("Sure", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int which) {
                        if (isExistAlbumName){
                            isExistAlbumName = false;
                            return;
                        }
                        dialog.dismiss();
                        String title = et_name.getText().toString();
//                        Toast.makeText(MainActivity.this, name, Toast.LENGTH_LONG).show();
                        mBtnAddAlbumPresenter.UploadToBmob(title);

                        mAlbumAdpater.refreshAlbumAdpater(mBtnAddAlbumPresenter.getList());

                    }
                })
                .setView(et_name)//Set a custom view to be the contents of the Dialog
                .create()
                .show();

    }

    @Override
    public void UploadToBmobSuccess() {
        Toast.makeText(this, "数据添加成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void UploadToBmobFailed() {
        Toast.makeText(this, "数据添加失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getListSuccess() {
        Toast.makeText(this, "数据获取成功", Toast.LENGTH_SHORT).show();
        mAlbumAdpater.notifyDataSetChanged();
    }

    @Override
    public void showExistAlbumName() {
        Toast.makeText(this, "您创建的相册名字已存在，请更改~~~", Toast.LENGTH_SHORT).show();
            isExistAlbumName = true;
    }

    @Override
    public void getListFailed() {
        Toast.makeText(this, "数据获取失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity", "onStart");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MainActivity", "onRestart");
        mAlbumAdpater.refreshAlbumAdpater(mBtnAddAlbumPresenter.getList());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume");


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "onDestroy");
    }



    public class MyItemTouchCallback extends ItemTouchHelper.Callback{

        private AlbumAdpater adapter;

        public MyItemTouchCallback(AlbumAdpater adapter) {
            this.adapter = adapter;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlag;
            int swipeFlag;
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager){
                dragFlag = ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                swipeFlag = 0;
            }else{
                dragFlag = ItemTouchHelper.DOWN | ItemTouchHelper.UP;
                swipeFlag = ItemTouchHelper.END | ItemTouchHelper.START;
            }

            return makeMovementFlags(dragFlag,swipeFlag);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            if (fromPosition < toPosition ){
                for (int i = fromPosition ;i<toPosition ;i++){
                    Collections.swap(adapter.getDataList(),i,i+1);
                }
            }else{
                for (int i= fromPosition; i>toPosition; i--){
                    Collections.swap(adapter.getDataList(),i ,i-1);
                }
            }
            recyclerView.getAdapter().notifyItemMoved(fromPosition,toPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();

//            String objectId = adapter.getDataList().get(position).getObjectId();
//            String albumName = adapter.getDataList().get(position).getTitle();
//            queryAllPicInfo(albumName);

//            删除相册条目的表中内容。
//            删除AllPicInfo表中符合的内容
//            ImageItemViewInfo iivi = new ImageItemViewInfo();
//            iivi.setObjectId(objectId);
//            iivi.delete(new UpdateListener() {
//
//                @Override
//                public void done(BmobException e) {
//                    if(e==null){
//                        Toast.makeText(MainActivity.this, "ImageItemViewInfo", Toast.LENGTH_SHORT).show();
//                    }else{
//                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
//                    }
//                }
//            });




            if (direction == ItemTouchHelper.END | direction==ItemTouchHelper.START){
                adapter.getDataList().remove(position);
                adapter.notifyItemRemoved(position);
            }
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (actionState==ItemTouchHelper.ACTION_STATE_DRAG){
                viewHolder.itemView.setBackgroundColor(Color.BLUE);
            }

        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(0);
        }
    }




    private void queryAllPicInfo(String albumName) {
        BmobQuery<AllPicInfo> query = new BmobQuery<AllPicInfo>();

        query.addWhereEqualTo("albumName", albumName);
        //执行查询方法
        query.findObjects(new FindListener<AllPicInfo>() {
            @Override
            public void done(List<AllPicInfo> object, BmobException e) {
                if(e==null){
                    for (AllPicInfo one : object) {
                        String objectId = one.getObjectId();

                        //删除AllPicInfo表中符合的内容
                        AllPicInfo gameScore = new AllPicInfo();
                        gameScore.setObjectId(objectId);
                        gameScore.delete(new UpdateListener() {

                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Toast.makeText(MainActivity.this, "AllPicInfo", Toast.LENGTH_SHORT).show();
                                }else{
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });



                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }


}
