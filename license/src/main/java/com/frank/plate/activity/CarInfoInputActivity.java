package com.frank.plate.activity;

import android.Manifest;
import android.annotation.SuppressLint;
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
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.CarEntity;
import com.frank.plate.bean.CarInfoEntity;
import com.frank.plate.bean.CarInfoRequestParameters;
import com.frank.plate.bean.NullDataEntity;
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
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CarInfoInputActivity extends BaseActivity {

    private static final String TAG = "CarInfoInputActivity";
    private final static int requestCode1 = 1001;
    private final static int requestCode2 = 1002;
    private final static int requestCode3 = 1003;


    //图片本地路径   要上传的
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<LocalMedia> selectList2 = new ArrayList<>();
    private List<LocalMedia> selectList3 = new ArrayList<>();

    //图片网络路径  之前上传成功
    private List<LocalMedia> netList = new ArrayList<>();
    private List<LocalMedia> netList2 = new ArrayList<>();
    private List<LocalMedia> netList3 = new ArrayList<>();

    //图片路径   回到页面要显示的 所有
    private List<LocalMedia> showlist = new ArrayList<>();
    private List<LocalMedia> showlist2 = new ArrayList<>();
    private List<LocalMedia> showlist3 = new ArrayList<>();


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

    int uploadTaskCount;//七牛上传图片完成计数

    PictureSelector pictureSelector;
    PictureSelector pictureSelector2;
    PictureSelector pictureSelector3;


    int type_action;//页面逻辑  1 添加车况 2修改车况
    CarEntity carEntity;//上个页面转递


    @OnClick({R.id.tv_enter_order, R.id.tv_car_model})
    public void onclick(View v) {
        switch (v.getId()) {

            case R.id.tv_car_model:

                toActivity(AutoBrandActivity.class);


                break;

            case R.id.tv_enter_order:

                uploadImg2QiNiu2();
                break;
        }

    }


    @Override
    protected void init() {


        carEntity = getIntent().getParcelableExtra(Configure.CARINFO);

        if (null == carEntity) {

            tv_car_no.setFocusable(true);
            tv_title.setText("车况录入");
            type_action = 1;
            tv_car_no.setText(new AppPreferences(this).getString(Configure.car_no, ""));
        } else {
            tv_car_no.setText(carEntity.getCarNo());
            tv_car_no.setFocusable(false);
            tv_title.setText("车况确认");
            type_action = 2;
            Toast.makeText(CarInfoInputActivity.this, "carEntity.getid==" + carEntity.getId(), Toast.LENGTH_SHORT).show();

            Api().showCarInfo(carEntity.getId()).subscribe(new RxSubscribe<CarInfoRequestParameters>(this, true) {
                @Override
                protected void _onNext(CarInfoRequestParameters o) {
                    tv_car_model.setText(o.getBrand());
                    for (UpDataPicEntity picEntity : o.getImagesList()) {

                        LocalMedia localMedia = new LocalMedia();
                        localMedia.setPath(picEntity.getImageUrl());

                        switch (picEntity.getType()) {
                            case "1":
                                netList.add(localMedia);
                                break;
                            case "2":
                                netList2.add(localMedia);
                                break;
                            case "3":
                                netList3.add(localMedia);
                                break;
                        }
                    }

                    showlist.addAll(netList);
                    showlist2.addAll(netList2);
                    showlist3.addAll(netList3);
                    adapter.setList(showlist);
                    adapter.notifyDataSetChanged();
                    adapter2.setList(showlist2);
                    adapter2.notifyDataSetChanged();
                    adapter3.setList(showlist3);
                    adapter3.notifyDataSetChanged();
                }

                @Override
                protected void _onError(String message) {
                    Toast.makeText(CarInfoInputActivity.this, message, Toast.LENGTH_SHORT).show();

                }
            });


        }


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
        adapter.setList(showlist);
        adapter.setSelectMax(maxSelectNum);
        recyclerView1.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (showlist.size() > 0) {
                    // 预览图片 可自定长按保存路径
                    //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                    pictureSelector.themeStyle(R.style.picture_default_style).openExternalPreview(position, showlist);

                }
            }
        });

        adapter2 = new GridImageAdapter(CarInfoInputActivity.this, onAddPicClickListener, requestCode2, pictureSelector2);
        adapter2.setList(showlist2);
        adapter2.setSelectMax(maxSelectNum);
        recyclerView2.setAdapter(adapter2);
        adapter2.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (showlist2.size() > 0) {
                    // 预览图片 可自定长按保存路径
                    //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                    pictureSelector2.themeStyle(R.style.picture_default_style).openExternalPreview(position, showlist2);
                }
            }
        });

        adapter3 = new GridImageAdapter(CarInfoInputActivity.this, onAddPicClickListener, requestCode3, pictureSelector3);
        adapter3.setList(showlist3);
        adapter3.setSelectMax(maxSelectNum);
        recyclerView3.setAdapter(adapter3);
        adapter3.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (showlist3.size() > 0) {
                    // 预览图片 可自定长按保存路径
                    //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                    pictureSelector3.themeStyle(R.style.picture_default_style).openExternalPreview(position, showlist3);

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
                    .maxSelectNum(maxSelectNum - list.size())// 最大图片选择数量
//                    .selectionMedia(list)// 是否传入已选图片
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

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case requestCode1:
                    // 图片选择结果回调
                    selectList = pictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectList) {
                        Log.i("添加的图片1-----》", media.getPath());
                    }

                    showlist.addAll(selectList);
                    adapter.setList(showlist);
                    adapter.notifyDataSetChanged();

                    break;
                case requestCode2:
                    // 图片选择结果回调
                    selectList2 = pictureSelector2.obtainMultipleResult(data);//
                    for (LocalMedia media : selectList2) {
                        Log.i("添加的图片2-----》", media.getPath());
                    }

                    showlist2.addAll(selectList2);
                    adapter2.setList(showlist2);
                    adapter2.notifyDataSetChanged();
                    break;
                case requestCode3:
                    // 图片选择结果回调
                    selectList3 = pictureSelector3.obtainMultipleResult(data);

                    for (LocalMedia media : selectList3) {
                        Log.i("添加的图片3-----》", media.getPath());
                    }


                    showlist3.addAll(selectList3);
                    adapter3.setList(showlist3);
                    adapter3.notifyDataSetChanged();
                    break;
            }
        }
    }

    private void uploadImg2QiNiu2() {

        // 设置图片名字
        final int allup_size = selectList.size() + selectList2.size() + selectList3.size();//所有要上传的图片
        Log.i(TAG, "上传总数=" + allup_size + "\nselectList1.size()=" + selectList.size() + "\nselectList3.size()=" + selectList2.size() + "\nselectList3.size()=" + selectList3.size());//上传进度

        if (allup_size == 0) {

            onAddCarInfoOfFixCarInfo();
            return;
        }


        shwoProgressBar();
        UploadManager uploadManager = new UploadManager();
        UploadOptions uploadOptions = new UploadOptions(null, null, false,
                new UpProgressHandler() {
                    public void progress(String key, double percent) {
                        Log.i(TAG, key + ": " + "上传进度:" + percent);//上传进度
                        if (percent == 1.0)//上传进度等于1.0说明上传完成,通知 完成任务+1
                        {
                            sendMsg(allup_size);
                        }
                    }
                }, null);

        for (int i = 0; i < selectList.size(); i++) {
            String key = "pic_" + CommonUtil.getTimeStame();
            String path = selectList.get(i).getPath();
            Log.i(TAG, "picPath: " + path);
            final int finalI = i;
            uploadManager.put(path, key, Auth.create(Configure.accessKey, Configure.secretKey).uploadToken(Configure.bucket), new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {
                            // info.error中包含了错误信息，可打印调试
                            // 上传成功后将key值上传到自己的服务器
                            if (info.isOK()) {
                                Log.i(TAG, "selectList      ResponseInfo: " + info + "\nkey::" + key);
                                UpDataPicEntity upDataPicEntity = new UpDataPicEntity();
                                upDataPicEntity.setType("1");
                                upDataPicEntity.setImageUrl(Configure.Domain + key);
                                upDataPicEntity.setSort(finalI);
                                upDataPicEntities.add(upDataPicEntity);
                            } else {
                                Log.i(TAG, "info:error====> " + info.error);
                            }
                        }
                    }, uploadOptions
            );
        }
        for (int i = 0; i < selectList2.size(); i++) {
            String key = "pic_" + CommonUtil.getTimeStame();
            String path = selectList2.get(i).getPath();
            Log.i(TAG, "picPath: " + selectList2.get(i).getPath());
            final int finalI = i;
            uploadManager.put(path, key, Auth.create(Configure.accessKey, Configure.secretKey).uploadToken(Configure.bucket), new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {
                            // info.error中包含了错误信息，可打印调试
                            // 上传成功后将key值上传到自己的服务器
                            if (info.isOK()) {
                                Log.i(TAG, "selectList2     ResponseInfo: " + info + "\nkey::" + key);
                                UpDataPicEntity upDataPicEntity = new UpDataPicEntity();
                                upDataPicEntity.setType("2");
                                upDataPicEntity.setImageUrl(Configure.Domain + key);
                                upDataPicEntity.setSort(finalI);
                                upDataPicEntities.add(upDataPicEntity);
                            } else {
                                Log.i(TAG, "info:error====> " + info.error);
                            }
                        }
                    }, uploadOptions
            );
        }
        for (int i = 0; i < selectList3.size(); i++) {
            String key = "pic_" + CommonUtil.getTimeStame();
            String path = selectList3.get(i).getPath();
            Log.i(TAG, "picPath: " + path);
            final int finalI = i;
            uploadManager.put(path, key, Auth.create(Configure.accessKey, Configure.secretKey).uploadToken(Configure.bucket), new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {
                            // info.error中包含了错误信息，可打印调试
                            // 上传成功后将key值上传到自己的服务器
                            if (info.isOK()) {
                                Log.i(TAG, "selectList3      ResponseInfo: " + info + "\nkey::" + key);
                                UpDataPicEntity upDataPicEntity = new UpDataPicEntity();
                                upDataPicEntity.setType("3");
                                upDataPicEntity.setImageUrl(Configure.Domain + key);
                                upDataPicEntity.setSort(finalI);
                                upDataPicEntities.add(upDataPicEntity);
                            } else {
                                Log.i(TAG, "info:error====> " + info.error);
                            }
                        }
                    }, uploadOptions
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
    protected void msgManagement(int what) {
        super.msgManagement(what);


        uploadTaskCount++;
        if (uploadTaskCount == what) {//容器中图片全部上传完成
            hideProgressBar();
            Toast.makeText(CarInfoInputActivity.this, "图片全部上传完成", Toast.LENGTH_SHORT).show();
            onAddCarInfoOfFixCarInfo();//接口提交
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

    private void onAddCarInfoOfFixCarInfo() {

        Observable<NullDataEntity> observable;
        if (type_action == 1) {
            observable = Api().addCarInfo(makeParameters());
        } else {
            observable = Api().fixCarInfo(makeParameters());
        }


        observable.subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                Toast.makeText(CarInfoInputActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                toActivity(MemberInfoInputActivity.class);
            }

            @Override
            protected void _onError(String message) {
                Toast.makeText(CarInfoInputActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });


    }


    private CarInfoRequestParameters makeParameters() {
        CarInfoRequestParameters parameters = new CarInfoRequestParameters();

        if (null == carEntity) {
            parameters.setUserId(new AppPreferences(this).getString(Configure.user_id, ""));
            parameters.setCarNo(tv_car_no.getText().toString());
        } else {

            parameters.setUserId(carEntity.getUserId());
            parameters.setId(carEntity.getId());
            parameters.setCarNo(carEntity.getCarNo());
        }

        parameters.setBrandId("");
        parameters.setBrand(tv_car_model.getText().toString());
        parameters.setName("");
        parameters.setNameId("");
        parameters.setPostscript("");
        parameters.setImagesList(upDataPicEntities);

        Log.d("CarInfoInputActivity", "请求参数:CarInfoRequestParameters==" + parameters.toString());
        return parameters;
    }
}
