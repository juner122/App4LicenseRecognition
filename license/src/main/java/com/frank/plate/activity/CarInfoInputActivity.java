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

import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.adapter.GridImageAdapter;
import com.frank.plate.bean.UpDataPicEntity;
import com.frank.plate.util.Auth;
import com.frank.plate.util.CommonUtil;
import com.frank.plate.util.ImageUtil;
import com.frank.plate.view.FullyGridLayoutManager;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import net.grandcentrix.tray.AppPreferences;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CarInfoInputActivity extends BaseActivity {

    private static final String TAG = "CarInfoInputActivity";
    private final static int requestCode1 = 1001;
    private final static int requestCode2 = 1002;
    private final static int requestCode3 = 1003;


    //图片本地路径
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<LocalMedia> selectList2 = new ArrayList<>();
    private List<LocalMedia> selectList3 = new ArrayList<>();


    //图片云路径
    private List<UpDataPicEntity> upDataPicEntities = new ArrayList<>();


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

    int uploadTaskCount1;//七牛上传图片完成计数
    int uploadTaskCount2;//七牛上传图片完成计数
    int uploadTaskCount3;//七牛上传图片完成计数

    PictureSelector pictureSelector;
    PictureSelector pictureSelector2;
    PictureSelector pictureSelector3;

    @OnClick({R.id.tv_enter_order})
    public void onclick() {
//        Api().saveCarInfo(new MySubscriber<>(this, new SubscribeOnNextListener<NullDataEntity>() {
//            @Override
//            public void onNext(NullDataEntity o) {
//                Toast.makeText(CarInfoInputActivity.this, "保存车况成功", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Toast.makeText(CarInfoInputActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }), tv_car_no.getText().toString(), "23", tv_car_model.getText().toString(), et_remarks.getText().toString());


    }

    @Override
    protected void init() {

        String car_no = getIntent().getStringExtra(Configure.car_no);


        if ("".equals(car_no)) {

            tv_car_no.setFocusable(false);
        } else {
            tv_car_no.setText(car_no);
            tv_car_no.setFocusable(true);
        }

        tv_title.setText("车况录入");

        pictureSelector = PictureSelector.create(CarInfoInputActivity.this);
        pictureSelector2 = PictureSelector.create(CarInfoInputActivity.this);
        pictureSelector3 = PictureSelector.create(CarInfoInputActivity.this);


        FullyGridLayoutManager manager = new FullyGridLayoutManager(CarInfoInputActivity.this, 5, GridLayoutManager.VERTICAL, false);
        FullyGridLayoutManager manager2 = new FullyGridLayoutManager(CarInfoInputActivity.this, 5, GridLayoutManager.VERTICAL, false);
        FullyGridLayoutManager manager3 = new FullyGridLayoutManager(CarInfoInputActivity.this, 5, GridLayoutManager.VERTICAL, false);
        recyclerView1.setLayoutManager(manager);
        recyclerView2.setLayoutManager(manager2);
        recyclerView3.setLayoutManager(manager3);

        adapter = new GridImageAdapter(CarInfoInputActivity.this, onAddPicClickListener, requestCode1, pictureSelector);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView1.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    // 预览图片 可自定长按保存路径
                    //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                    pictureSelector.themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList);

                }
            }
        });

        adapter2 = new GridImageAdapter(CarInfoInputActivity.this, onAddPicClickListener, requestCode2, pictureSelector2);
        adapter2.setList(selectList2);
        adapter2.setSelectMax(maxSelectNum);
        recyclerView2.setAdapter(adapter2);
        adapter2.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList2.size() > 0) {
                    // 预览图片 可自定长按保存路径
                    //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                    pictureSelector2.themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList2);
                }
            }
        });

        adapter3 = new GridImageAdapter(CarInfoInputActivity.this, onAddPicClickListener, requestCode3, pictureSelector3);
        adapter3.setList(selectList3);
        adapter3.setSelectMax(maxSelectNum);
        recyclerView3.setAdapter(adapter3);
        adapter3.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList3.size() > 0) {
                    // 预览图片 可自定长按保存路径
                    //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                    pictureSelector3.themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList3);

                }
            }
        });


        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).

                subscribe(new Observer<Boolean>() {
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
        public void onAddPicClick(int requestCode, PictureSelector pictureSelector, List<LocalMedia> list) {
            pictureSelector.openGallery(PictureMimeType.ofImage())
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .selectionMedia(list)// 是否传入已选图片
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .minimumCompressSize(50)// 小于100kb的图片不压缩
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                    .previewImage(true)// 是否可预览图片
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
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
                    selectList = pictureSelector.obtainMultipleResult(data);
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

                    //上传到七牛

                    uploadTaskCount1 = 0;
                    uploadImg2QiNiu(ImageUtil.toListString(selectList), requestCode1);

                    break;
                case requestCode2:
                    // 图片选择结果回调
                    selectList2 = pictureSelector2.obtainMultipleResult(data);
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


                    uploadTaskCount2 = 0;
                    uploadImg2QiNiu(ImageUtil.toListString(selectList2), requestCode2);

                    break;
                case requestCode3:
                    // 图片选择结果回调
                    selectList3 = pictureSelector3.obtainMultipleResult(data);
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

                    uploadTaskCount3 = 0;
                    uploadImg2QiNiu(ImageUtil.toListString(selectList3), requestCode3);

                    break;
            }
        }
    }

    private void uploadImg2QiNiu(final List<String> list, final int requestCode) {

        if (list == null || list.size() == 0)
            return;
        shwoProgressBar();

        UploadManager uploadManager = new UploadManager();

        // 设置图片名字

        for (String picPath : list) {
            String key = "pic_" + CommonUtil.getTimeStame();
            Log.i(TAG, "picPath: " + picPath);
            uploadManager.put(picPath, key, Auth.create(Configure.accessKey, Configure.secretKey).uploadToken(Configure.bucket), new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {
                            // info.error中包含了错误信息，可打印调试
                            // 上传成功后将key值上传到自己的服务器
                            if (info.isOK()) {
                                Log.i(TAG, "ResponseInfo: " + info + "\nkey::" + key);
                                UpDataPicEntity upDataPicEntity = new UpDataPicEntity();
                                switch (requestCode) {

                                    case requestCode1:
                                        upDataPicEntity.setType("1");
                                        break;

                                    case requestCode2:
                                        upDataPicEntity.setType("2");
                                        break;
                                    case requestCode3:
                                        upDataPicEntity.setType("3");
                                        break;
                                }
                                upDataPicEntity.setImageUrl(Configure.Domain + key);
                                upDataPicEntities.add(upDataPicEntity);

                            } else {
                                Log.i(TAG, "info:error====> " + info.error);
                            }


                        }
                    }, new UploadOptions(null, null, false,
                            new UpProgressHandler() {
                                public void progress(String key, double percent) {
                                    Log.i(TAG, key + ": " + "上传进度:" + percent);//上传进度
                                    if (percent == 1.0)//上传进度等于1.0说明上传完成,通知 完成任务+1
                                    {
                                        sendMsg(list.size(), requestCode);
                                    }

                                }
                            }, null)
            );
        }
    }


    @Override
    protected void msgManagement(int what, int tag) {
        super.msgManagement(what, tag);

        switch (tag) {
            case requestCode1:
                uploadTaskCount1++;
                if (uploadTaskCount1 == what) {//容器中图片全部上传完成
                    hideProgressBar();
                    Toast.makeText(CarInfoInputActivity.this, "图片1全部上传完成", Toast.LENGTH_SHORT).show();
                }
                break;
            case requestCode2:
                uploadTaskCount2++;
                if (uploadTaskCount2 == what) {//容器中图片全部上传完成
                    hideProgressBar();
                    Toast.makeText(CarInfoInputActivity.this, "图片2全部上传完成", Toast.LENGTH_SHORT).show();
                }
                break;
            case requestCode3:
                uploadTaskCount3++;
                if (uploadTaskCount3 == what) {//容器中图片全部上传完成
                    hideProgressBar();
                    Toast.makeText(CarInfoInputActivity.this, "图片3全部上传完成", Toast.LENGTH_SHORT).show();
                }
                break;
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
