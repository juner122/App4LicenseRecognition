package com.eb.geaiche.activity;

import android.Manifest;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eb.geaiche.mvp.ActivateCardActivity;
import com.juner.mvp.Configure;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.GridImageAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.bean.AutoBrand;
import com.juner.mvp.bean.AutoModel;

import com.juner.mvp.bean.CarInfoRequestParameters;

import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.UpDataPicEntity;
import com.eb.geaiche.util.A2bigA;
import com.eb.geaiche.util.Auth;
import com.eb.geaiche.util.CommonUtil;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.FullyGridLayoutManager;
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

import io.reactivex.disposables.Disposable;


public class CarInfoInputActivity extends BaseActivity {

    private static final String TAG = "CarInfoInputActivity";
    private final static int requestCode1 = 1;
    private final static int requestCode2 = 2;
    private final static int requestCode3 = 3;


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

    @BindView(R.id.tv_car_vin)
    TextView tv_car_vin;

    @BindView(R.id.et_remarks)
    EditText et_remarks;
    @BindView(R.id.tv_car_mileage)
    EditText tv_car_mileage;

    @BindView(R.id.ll_check)
    View ll_check;


    @BindView(R.id.recycler1)
    RecyclerView recyclerView1;
    @BindView(R.id.recycler2)
    RecyclerView recyclerView2;
    @BindView(R.id.recycler3)
    RecyclerView recyclerView3;

    private GridImageAdapter adapter;
    private GridImageAdapter adapter2;
    private GridImageAdapter adapter3;

    int uploadTaskCount;//七牛上传图片完成计数

    PictureSelector pictureSelector;
    PictureSelector pictureSelector2;
    PictureSelector pictureSelector3;


    int type_action;//页面逻辑  1 添加车况 2修改车况
    CarInfoRequestParameters carEntity, carInfo;//
    int car_id, new_car_id;//上个页面转递


    boolean isrvShow1, isrvShow2, isrvShow3;

    @OnClick({R.id.tv_enter_order, R.id.tv_car_model, R.id.tv_car_vin, R.id.ll_rv_1, R.id.ll_rv_2, R.id.ll_rv_3, R.id.ll_check})
    public void onclick(View v) {
        switch (v.getId()) {

            case R.id.tv_car_model:

                toActivity(AutoBrandActivity.class);


                break;
            case R.id.ll_rv_1:

                if (isrvShow1) {
                    recyclerView1.setVisibility(View.GONE);
                    isrvShow1 = false;
                } else {
                    recyclerView1.setVisibility(View.VISIBLE);
                    isrvShow1 = true;
                }
                break;
            case R.id.ll_rv_2:

                if (isrvShow2) {
                    recyclerView2.setVisibility(View.GONE);
                    isrvShow2 = false;
                } else {
                    recyclerView2.setVisibility(View.VISIBLE);
                    isrvShow2 = true;
                }
                break;
            case R.id.ll_rv_3:

                if (isrvShow3) {
                    recyclerView3.setVisibility(View.GONE);
                    isrvShow3 = false;
                } else {
                    recyclerView3.setVisibility(View.VISIBLE);
                    isrvShow3 = true;
                }
                break;
            case R.id.tv_car_vin:

                toActivity(CarVinDISActivity.class);
                break;

            case R.id.tv_enter_order:

                if (TextUtils.isEmpty(tv_car_no.getText())) {
                    ToastUtils.showToast("请正确填写车牌号码！");
                    return;
                }

//                if (null == autoModel || autoModel.getName().equals("")) {
//                    ToastUtils.showToast("请选择车型！");
//                    return;
//                }

                if (isUpdata)
                    onAddCarInfoOfFixCarInfo();//接口提交
                else
                    ToastUtils.showToast("请等待图片上传完成");

                break;

            case R.id.ll_check:

                //车况检查表
                //车况检修记录列表
                Intent intent = new Intent(this, CarCheckResultListActivity.class);
                intent.putExtra(Configure.car_no, carEntity.getCarNo());
                intent.putExtra(Configure.car_id, car_id);
                intent.putExtra(Configure.isShow, 1);

                startActivity(intent);


                break;
        }

    }


    AutoBrand selectAutoBrand;//选中的品牌
    AutoModel autoModel;//选中的型号


    boolean isUpdata = true;//图片上传完成，是否可以确认信息，


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        if (null != intent.getParcelableExtra("vinInfo")) {
            carInfo = intent.getParcelableExtra("vinInfo");//车况对象
            if (null == selectAutoBrand) {
                selectAutoBrand = new AutoBrand();
            } else {
                selectAutoBrand.setId(0);
            }
            selectAutoBrand.setName(carInfo.getBrand());

            if (null == autoModel) {
                autoModel = new AutoModel();
            } else {
                autoModel.setId(0);
                autoModel.setBrandId(0);
            }
            autoModel.setName(carInfo.getName());
            tv_car_vin.setText(carInfo.getVin());
        } else {

            selectAutoBrand = intent.getParcelableExtra(Configure.brand);
            autoModel = intent.getParcelableExtra(Configure.brandModdel);

        }

        tv_car_model.setText(selectAutoBrand.getName() + "\t" + autoModel.getName());

    }


    private void showCarInfo(int car_id) {

        Api().showCarInfo(car_id).subscribe(new RxSubscribe<CarInfoRequestParameters>(this, true) {
            @Override
            protected void _onNext(CarInfoRequestParameters o) {
                carEntity = o;
                tv_car_no.setText(carEntity.getCarNo());
                tv_car_model.setText(o.getBrand() + "\t" + o.getName());
                if ("".equals(o.getPostscript()))
                    et_remarks.setHint("暂无备注");
                else
                    et_remarks.setText(o.getPostscript());

                selectAutoBrand = new AutoBrand(o.getBrandId(), o.getBrand());
                autoModel = new AutoModel(o.getNameId(), o.getName());

                tv_car_vin.setText(null != carEntity.getVin() || !TextUtils.isEmpty(carEntity.getVin()) ? carEntity.getVin() : "扫描识别车架号");


                tv_car_mileage.setText(null == carEntity.getMileage() ? "0" : carEntity.getMileage());

                for (UpDataPicEntity picEntity : o.getImagesList()) {

                    LocalMedia localMedia = new LocalMedia();
                    localMedia.setPath(picEntity.getImageUrl());
                    localMedia.setId(picEntity.getId());
                    localMedia.setDate(picEntity.getCarateTime4Date());


                    switch (picEntity.getType()) {
                        case 1:
                            netList.add(localMedia);
                            break;
                        case 2:
                            netList2.add(localMedia);
                            break;
                        case 3:
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

                if (netList.size() > 0) {
                    recyclerView1.setVisibility(View.VISIBLE);
                    isrvShow1 = true;
                }
                if (netList2.size() > 0) {
                    recyclerView2.setVisibility(View.VISIBLE);
                    isrvShow2 = true;
                }
                if (netList3.size() > 0) {
                    recyclerView3.setVisibility(View.VISIBLE);
                    isrvShow3 = true;
                }

            }

            @Override
            protected void _onError(String message) {
                Toast.makeText(CarInfoInputActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void init() {


        car_id = getIntent().getIntExtra(Configure.CARID, 0);
        new_car_id = getIntent().getIntExtra("new_car_id", 0);

        tv_car_no.setTransformationMethod(new A2bigA());
        tv_car_vin.setTransformationMethod(new A2bigA());

        if (0 == car_id) {
            tv_car_no.setFocusable(true);
            tv_title.setText("车况录入");
            type_action = 1;
            tv_car_no.setText(getIntent().getStringExtra(Configure.car_no));

            ll_check.setVisibility(View.GONE);//车况检查表

            if (new_car_id != 0) {
                showCarInfo(new_car_id);
                ll_check.setVisibility(View.VISIBLE);//车况检查表
            }
        } else {

            tv_car_no.setFocusable(false);
            tv_title.setText("车况确认");
            type_action = 2;

            showCarInfo(car_id);
            ll_check.setVisibility(View.VISIBLE);//车况检查表


        }

        pictureSelector = PictureSelector.create(CarInfoInputActivity.this);
        pictureSelector2 = PictureSelector.create(CarInfoInputActivity.this);
        pictureSelector3 = PictureSelector.create(CarInfoInputActivity.this);


        FullyGridLayoutManager manager = new FullyGridLayoutManager(CarInfoInputActivity.this, 3, GridLayoutManager.VERTICAL, false);
        FullyGridLayoutManager manager2 = new FullyGridLayoutManager(CarInfoInputActivity.this, 3, GridLayoutManager.VERTICAL, false);
        FullyGridLayoutManager manager3 = new FullyGridLayoutManager(CarInfoInputActivity.this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView1.setLayoutManager(manager);
        recyclerView2.setLayoutManager(manager2);
        recyclerView3.setLayoutManager(manager3);

        adapter = new GridImageAdapter(CarInfoInputActivity.this, onAddPicClickListener, requestCode1, pictureSelector, onItemDeleteListener, true);
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

        adapter2 = new GridImageAdapter(CarInfoInputActivity.this, onAddPicClickListener, requestCode2, pictureSelector2, onItemDeleteListener, true);
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

        adapter3 = new GridImageAdapter(CarInfoInputActivity.this, onAddPicClickListener, requestCode3, pictureSelector3, onItemDeleteListener, true);
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


    private GridImageAdapter.OnItemDeleteListener onItemDeleteListener = new GridImageAdapter.OnItemDeleteListener() {
        @Override
        public void onItemDelete(int id) {

            delete(id);

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case requestCode1:
                    uploadTaskCount = 0;
                    // 图片选择结果回调
                    selectList = pictureSelector.obtainMultipleResult(data);

                    for (LocalMedia media : selectList) {
                        Log.i("添加的图片1-----》", media.getPath());
                    }

                    showlist.addAll(selectList);
                    adapter.setList(showlist);
                    adapter.notifyDataSetChanged();

                    uploadImg2QiNiu2(selectList, 1, "仪表记录");
                    break;
                case requestCode2:
                    // 图片选择结果回调
                    uploadTaskCount = 0;
                    selectList2 = pictureSelector2.obtainMultipleResult(data);//

                    for (LocalMedia media : selectList2) {
                        Log.i("添加的图片2-----》", media.getPath());
                    }

                    showlist2.addAll(selectList2);
                    adapter2.setList(showlist2);
                    adapter2.notifyDataSetChanged();
                    uploadImg2QiNiu2(selectList2, 2, "内饰记录");
                    break;
                case requestCode3:
                    // 图片选择结果回调
                    uploadTaskCount = 0;
                    selectList3 = pictureSelector3.obtainMultipleResult(data);

                    for (LocalMedia media : selectList3) {
                        Log.i("添加的图片3-----》", media.getPath());
                    }
                    showlist3.addAll(selectList3);
                    adapter3.setList(showlist3);
                    adapter3.notifyDataSetChanged();
                    uploadImg2QiNiu2(selectList3, 3, "外观记录");
                    break;
            }

        }
    }

    private void uploadImg2QiNiu2(final List<LocalMedia> upList, final int type, final String tag) {
        Log.i(TAG, "上传总数=" + upList.size());

        if (upList.size() == 0) {
            return;
        }
        isUpdata = false;

        shwoProgressBar();
        UploadManager uploadManager = new UploadManager();
        UploadOptions uploadOptions = new UploadOptions(null, null, false,
                new UpProgressHandler() {
                    public void progress(String key, double percent) {
                        Log.i(TAG, key + ": " + "上传进度:" + percent);//上传进度
                        if (percent == 1.0)//上传进度等于1.0说明上传完成,通知 完成任务+1
                        {
                            sendMsg(upList.size(), tag);
                        }
                    }
                }, null);

        for (int i = 0; i < upList.size(); i++) {
            String key = "pic_" + CommonUtil.getTimeStame();
            String path = upList.get(i).getPath();
            Log.i(TAG, "picPath: " + path);
            final int finalI = i;


            uploadManager.put(path, key, Auth.create(Configure.accessKey, Configure.secretKey).uploadToken(Configure.bucket), new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {
                            // info.error中包含了错误信息，可打印调试
                            // 上传成功后将key值上传到自己的服务器
                            if (info.isOK()) {
                                Log.i(TAG, "upList      ResponseInfo: " + info + "\nkey::" + key);
                                UpDataPicEntity upDataPicEntity = new UpDataPicEntity();
                                upDataPicEntity.setType(type);
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
    protected void msgManagement(int what, String tag) {
        super.msgManagement(what, tag);


        uploadTaskCount++;
        if (uploadTaskCount == what) {//容器中图片全部上传完成
            hideProgressBar();
            ToastUtils.showToast(tag + "图片上传成功");
            isUpdata = true;

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

        if (type_action == 1) {
            Api().addCarInfo(makeParameters()).subscribe(new RxSubscribe<Integer>(this, true) {
                @Override
                protected void _onNext(Integer nullDataEntity) {

                    new AppPreferences(getApplicationContext()).put(Configure.car_no, "");
                    ToastUtils.showToast("操作成功");
                    int result_code = getIntent().getIntExtra("result_code", 0);

                    if (result_code == 1) {//用户信息页面传过来
                        setResult(RESULT_OK);
                        finish();
                    } else if (result_code == 2) {

                        toActivity(ActivateCardActivity.class, Configure.act_tag, 101);
                    } else if (result_code == 999) {//检修单详情页面
                        finish();
                    } else {
                        toActivity(MemberInfoInputActivity.class, Configure.car_no, tv_car_no.getText().toString());
                    }
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.showToast(message);

                }
            });

        } else {
            Api().fixCarInfo(makeParameters()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
                @Override
                protected void _onNext(NullDataEntity nullDataEntity) {

                    new AppPreferences(getApplicationContext()).put(Configure.car_no, "");
                    ToastUtils.showToast("操作成功");
                    int result_code = getIntent().getIntExtra("result_code", 0);

                    if (result_code == 1) {//用户信息页面传过来
                        setResult(RESULT_OK);
                        finish();
                    } else if (result_code == 2) {

                        toActivity(ActivateCardActivity.class, Configure.act_tag, 101);
                    } else if (result_code == 999) {//检修单详情页面
                        finish();
                    } else {
                        toActivity(MemberInfoInputActivity.class, Configure.car_no, tv_car_no.getText().toString());
                    }
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.showToast(message);

                }
            });


        }


    }


    private CarInfoRequestParameters makeParameters() {
        CarInfoRequestParameters parameters = new CarInfoRequestParameters();

        if (null == carEntity) {
            parameters.setUserId(new AppPreferences(this).getString(Configure.user_id, ""));
            parameters.setCarNo(tv_car_no.getText().toString().toUpperCase());
        } else {

            if (new_car_id != 0)//旧车 绑定新用户
                parameters.setUserId(new AppPreferences(this).getString(Configure.user_id, ""));
            else {
                parameters.setUserId(carEntity.getUserId());
                parameters.setId(carEntity.getId());
            }

            parameters.setCarNo(carEntity.getCarNo());
        }

        parameters.setBrandId(null != selectAutoBrand ? selectAutoBrand.getId() : -1);
        parameters.setBrand(null != selectAutoBrand ? selectAutoBrand.getName() : "");
        parameters.setName(null != autoModel ? autoModel.getName() : "");
        parameters.setNameId(null != autoModel ? autoModel.getId() : -1);
        parameters.setPostscript(et_remarks.getText().toString());
        parameters.setImagesList(upDataPicEntities);//添加的图片
        parameters.setMileage(tv_car_mileage.getText().toString());


        if (null != carInfo) {
            parameters.setVin(carInfo.getVin());
            parameters.setAllJson(carInfo.getAllJson());
            parameters.setYear(carInfo.getYear());
            parameters.setGuidingPrice(carInfo.getGuidingPrice());
            parameters.setEffluentStandard(carInfo.getEffluentStandard());
            parameters.setCarType(carInfo.getCarType());
            parameters.setSaleName(carInfo.getSaleName());
            parameters.setOutputVolume(carInfo.getOutputVolume());
            parameters.setEngineSn(carInfo.getEngineSn());

        }

        Log.d("CarInfoInputActivity", "请求参数:CarInfoRequestParameters==" + parameters.toString());
        return parameters;
    }


    private void delete(int id) {


        Api().delete(id).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {

                ToastUtils.showToast("删除成功");
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("删除失败");
            }
        });
    }


}
