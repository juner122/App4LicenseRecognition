package com.frank.plate.activity;

import android.Manifest;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.frank.plate.R;
import com.frank.plate.adapter.GridImageAdapter;
import com.frank.plate.api.MySubscriber;
import com.frank.plate.api.SubscribeOnNextListener;
import com.frank.plate.bean.NullDataEntity;
import com.frank.plate.view.FullyGridLayoutManager;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.tamic.novate.Throwable;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CarInfoInputActivity extends BaseActivity {


    private final static int requestCode1 = 1001;
    private final static int requestCode2 = 1002;
    private final static int requestCode3 = 1003;
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<LocalMedia> selectList2 = new ArrayList<>();
    private List<LocalMedia> selectList3 = new ArrayList<>();

    private int maxSelectNum = 5;

    @BindView(R.id.tv_car_no)
    EditText tv_car_no;
    @BindView(R.id.tv_car_model)
    TextView tv_car_model;
    @BindView(R.id.et_remarks)
    EditText et_remarks;


    @BindView(R.id.recycler1)
    RecyclerView recyclerView1;
    @BindView(R.id.recycler2)
    RecyclerView recyclerView2;
    @BindView(R.id.recycler3)
    RecyclerView recyclerView3;

    private GridImageAdapter adapter;
    private GridImageAdapter adapter2;
    private GridImageAdapter adapter3;


    @OnClick({R.id.tv_enter_order})
    public void onclick() {
        Api().saveCarInfo(new MySubscriber<>(this, new SubscribeOnNextListener<NullDataEntity>() {
            @Override
            public void onNext(NullDataEntity o) {
                Toast.makeText(CarInfoInputActivity.this, "保存车况成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(CarInfoInputActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }), tv_car_no.getText().toString(), "23", tv_car_model.getText().toString(), et_remarks.getText().toString());
    }

    @Override
    protected void init() {

        tv_title.setText("车况录入");


        FullyGridLayoutManager manager = new FullyGridLayoutManager(CarInfoInputActivity.this, 5, GridLayoutManager.VERTICAL, false);
        FullyGridLayoutManager manager2 = new FullyGridLayoutManager(CarInfoInputActivity.this, 5, GridLayoutManager.VERTICAL, false);
        FullyGridLayoutManager manager3 = new FullyGridLayoutManager(CarInfoInputActivity.this, 5, GridLayoutManager.VERTICAL, false);
        recyclerView1.setLayoutManager(manager);
        recyclerView2.setLayoutManager(manager2);
        recyclerView3.setLayoutManager(manager3);

        adapter = new GridImageAdapter(CarInfoInputActivity.this, onAddPicClickListener, requestCode1);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView1.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(CarInfoInputActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList);
                            break;

                    }
                }
            }
        });
        adapter2 = new GridImageAdapter(CarInfoInputActivity.this, onAddPicClickListener, requestCode2);
        adapter2.setList(selectList2);
        adapter2.setSelectMax(maxSelectNum);
        recyclerView2.setAdapter(adapter2);
        adapter2.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList2.size() > 0) {
                    LocalMedia media = selectList2.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(CarInfoInputActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList2);
                            break;
                    }
                }
            }
        });
        adapter3 = new GridImageAdapter(CarInfoInputActivity.this, onAddPicClickListener, requestCode3);
        adapter3.setList(selectList3);
        adapter3.setSelectMax(maxSelectNum);
        recyclerView3.setAdapter(adapter3);
        adapter3.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList3.size() > 0) {
                    LocalMedia media = selectList3.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(CarInfoInputActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList3);
                            break;
                    }
                }
            }
        });


        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(CarInfoInputActivity.this);
                } else {
                    Toast.makeText(CarInfoInputActivity.this,
                            getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(java.lang.Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });

    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {

        }

        @Override
        public void onAddPicClick(int requestCode) {

            PictureSelector.create(CarInfoInputActivity.this)
                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                    .previewImage(true)// 是否可预览图片
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                    //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径

                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
                    //.compressSavePath(getPath())//压缩图片保存地址
                    //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽

                    .selectionMedia(selectList)// 是否传入已选图片
                    //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                    //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                    .minimumCompressSize(50)// 小于100kb的图片不压缩
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled(true) // 裁剪是否可旋转图片
                    //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()//显示多少秒以内的视频or音频也可适用
                    //.recordVideoSecond()//录制视频秒数 默认60s
                    .forResult(requestCode);//结果回调onActivityResult code


        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case requestCode1:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
                        Log.i("图片1-----》", media.getPath());
                    }
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
                case requestCode2:
                    // 图片选择结果回调
                    selectList2 = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList2) {
                        Log.i("图片2-----》", media.getPath());
                    }
                    adapter2.setList(selectList2);
                    adapter2.notifyDataSetChanged();
                    break;
                case requestCode3:
                    // 图片选择结果回调
                    selectList3 = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList3) {
                        Log.i("图片3-----》", media.getPath());
                    }
                    adapter3.setList(selectList3);
                    adapter3.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_car_status_entry;
    }


}
