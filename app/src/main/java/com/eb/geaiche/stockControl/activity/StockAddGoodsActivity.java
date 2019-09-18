package com.eb.geaiche.stockControl.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;


import com.eb.geaiche.adapter.GridImageAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.stockControl.adapter.PickBrandListAdapter;
import com.eb.geaiche.stockControl.adapter.PickCategoryListAdapter;
import com.eb.geaiche.stockControl.adapter.StandardsListAdapter;
import com.eb.geaiche.util.Auth;
import com.eb.geaiche.util.CommonUtil;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.FullyGridLayoutManager;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.Goods;
import com.juner.mvp.bean.GoodsBrand;
import com.juner.mvp.bean.GoodsCategory;
import com.juner.mvp.bean.NullDataEntity;


import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StockAddGoodsActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    TextView name;
    @BindView(R.id.tv_type)
    TextView tv_type;//分类
    @BindView(R.id.tv_brand)
    TextView tv_brand;//品牌
    @BindView(R.id.sw)
    Switch sw;//是否推广
    @BindView(R.id.num)
    TextView num;//

    @BindView(R.id.ll_add_standards)
    View ll_add_standards;//

    @BindView(R.id.et_remarks)
    EditText et_remarks;//

    GoodsBrand brand = new GoodsBrand();//商品品牌
    String firstCategoryId;//分类id
    String firstCategoryTitle;//分类名

    @BindView(R.id.rv)
    RecyclerView rv;//
    @BindView(R.id.rv2)
    RecyclerView rv2;//

    @BindView(R.id.rv_standards)
    RecyclerView rv_standards;
    StandardsListAdapter standardsListAdapter;

    @BindView(R.id.cv)
    CardView cv;//


    @BindView(R.id.rv_category)
    RecyclerView rv_category;//分类和品牌列表

    GridImageAdapter headAdapter;//商品头部图片
    GridImageAdapter infoAdapter;//商品详情图片

    PictureSelector pictureSelector;
    PictureSelector pictureSelector2;

    private final static int requestCode1 = 1;
    private final static int requestCode2 = 2;
    //图片本地路径   要上传的
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<LocalMedia> selectList2 = new ArrayList<>();

    //图片网络路径  之前上传成功
    private List<LocalMedia> netList = new ArrayList<>();
    private List<LocalMedia> netList2 = new ArrayList<>();

    //图片路径   回到页面要显示的 所有
    private List<LocalMedia> showlist = new ArrayList<>();
    private List<LocalMedia> showlist2 = new ArrayList<>();
    boolean isUpdata = true;//图片上传完成，是否可以确认信息，
    boolean isrvShow1, isrvShow2;

    private int maxSelectNum = 5;

    PickCategoryListAdapter aba;//分类列表
    PickBrandListAdapter aba_brand;//品牌列表

    int goodId;//商品id

    @OnClick({R.id.enter, R.id.ll_pick_brand, R.id.ll_pick_category, R.id.ll_add_standards})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.enter://新增商品

                if (!isUpdata) {
                    ToastUtils.showToast("请等待图片上传完成");
                    return;
                }


                if (goodId != -1)
                    fixGood();
                else
                    addGood();


                break;
            case R.id.ll_pick_brand://选择品牌

                showRvBrand();
                break;
            case R.id.ll_pick_category://选择分类

                showRvCategory();
                break;
            case R.id.ll_add_standards://新增规格

                Intent intent = new Intent(this, StockAddStandardsActivity.class);
                intent.putExtra("goodsId", goodId);
                intent.putExtra("goodsTitle", name.getText().toString());

                startActivity(intent);
                break;

        }
    }

    private Goods getGoods() {

        Goods goods = new Goods();

        goods.setGoodsTitle(name.getText().toString());
        goods.setGoodsBrandId(brand.getBrandId());
        goods.setGoodsBrandTitle(brand.getBrandTitle());

        goods.setFirstCategoryId(firstCategoryId);
        goods.setFirstCategoryTitle(firstCategoryTitle);
        goods.setType(4);//固定为 4 零件
        goods.setWxType(sw.isChecked() ? 1 : 2);//是否微信上架1是2否
        goods.setRemindLimit(Integer.valueOf(num.getText().toString()));//库存提醒设置数字
        goods.setRemarks(et_remarks.getText().toString());//库存备注
        if (goodId != -1) {
            goods.setId(goodId);
        }


        return goods;
    }

    private void addGood() {
        if (TextUtils.isEmpty(name.getText())) {
            ToastUtils.showToast("商品名称不能为空！");
            return;
        }
        if (null == brand.getBrandId()) {
            ToastUtils.showToast("商品品牌不能为空！");
            return;
        }
        if (null == firstCategoryId) {
            ToastUtils.showToast("商品分类不能为空！");
            return;
        }

        Api().addGoods(getGoods()).subscribe(new RxSubscribe<Integer>(this, true) {
            @Override
            protected void _onNext(Integer i) {
                goodId = i;
                ToastUtils.showToast("操作成功,现可添加规格！");
                initViewType();

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("操作失败！" + message);
            }
        });

    }

    //编辑商品
    private void fixGood() {
        Api().fixGoods(getGoods()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity i) {
                ToastUtils.showToast("操作成功！");
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("操作失败！" + message);
            }
        });


    }


    //显示分类列表
    private void showRvCategory() {

        showRv();

        aba = new PickCategoryListAdapter(null, this);
        rv_category.setAdapter(aba);
        aba.setOnItemClickListener((a, view, position) -> {//选择分类
            firstCategoryId = aba.getData().get(position).getCategoryId();
            firstCategoryTitle = aba.getData().get(position).getCategoryTitle();
            tv_type.setText(firstCategoryTitle);//分类
            hideRv();
        });

        //获取分类
        Api().queryShopcategoryAll(String.valueOf(Configure.Goods_TYPE_4)).subscribe(new RxSubscribe<List<GoodsCategory>>(this, true) {
            @Override
            protected void _onNext(List<GoodsCategory> list) {
                if (null == list || list.size() == 0) {
                    ToastUtils.showToast("暂无分类！");
                    return;
                }
                aba.setNewData(list);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("获取分类失败" + message);
            }
        });
    }

    //显示品牌列表
    private void showRvBrand() {
        showRv();
        aba_brand = new PickBrandListAdapter(null, this);
        rv_category.setAdapter(aba_brand);

        aba_brand.setOnItemClickListener((a, view, position) -> {//选择品牌
            brand = aba_brand.getData().get(position);
            tv_brand.setText(brand.getBrandTitle());
            hideRv();
        });

        //获取品牌
        Api().brandGoodsList().subscribe(new RxSubscribe<List<GoodsBrand>>(this, true) {
            @Override
            protected void _onNext(List<GoodsBrand> list) {
                if (null == list || list.size() == 0) {
                    ToastUtils.showToast("暂无品牌！");
                    return;
                }
                aba_brand.setNewData(list);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("获取分类失败" + message);
            }
        });

    }

    //隐藏分类和品牌列表
    private void hideRv() {
        TranslateAnimation hideAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        hideAnim.setDuration(300);
        cv.startAnimation(hideAnim);
        cv.setVisibility(View.GONE);
    }

    //显示分类和品牌列表
    private void showRv() {
        TranslateAnimation hideAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        hideAnim.setDuration(300);
        cv.startAnimation(hideAnim);
        cv.setVisibility(View.VISIBLE);
    }


    @Override
    protected void init() {
        goodId = getIntent().getIntExtra("goodsId", -1);


        initPic();
    }


    //初始化图片上传模块
    private void initPic() {


        pictureSelector = PictureSelector.create(this);
        pictureSelector2 = PictureSelector.create(this);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        FullyGridLayoutManager manager2 = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(manager);
        rv2.setLayoutManager(manager2);
        headAdapter = new GridImageAdapter(this, onAddPicClickListener, requestCode1, pictureSelector, onItemDeleteListener, true);
        headAdapter.setList(showlist);
        headAdapter.setSelectMax(maxSelectNum);
        rv.setAdapter(headAdapter);
        headAdapter.setOnItemClickListener((position, v) -> {
            if (showlist.size() > 0) {
                // 预览图片 可自定长按保存路径
                //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                try {
                    pictureSelector.themeStyle(R.style.picture_default_style).openExternalPreview(position, showlist);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        infoAdapter = new GridImageAdapter(this, onAddPicClickListener, requestCode2, pictureSelector2, onItemDeleteListener, true);
        infoAdapter.setList(showlist2);
        infoAdapter.setSelectMax(maxSelectNum);
        rv2.setAdapter(infoAdapter);
        infoAdapter.setOnItemClickListener((position, v) -> {
            if (showlist2.size() > 0) {
                // 预览图片 可自定长按保存路径
                //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                pictureSelector2.themeStyle(R.style.picture_default_style).openExternalPreview(position, showlist2);
            }
        });

    }

    @Override
    protected void setUpView() {
        standardsListAdapter = new StandardsListAdapter(null, this);

        rv_standards.setLayoutManager(new LinearLayoutManager(this));
        rv_standards.setAdapter(standardsListAdapter);
        standardsListAdapter.setOnItemClickListener((adapter, view, position) -> {

            Intent intent = new Intent(this, StockAddStandardsActivity.class);
            intent.putExtra("goodsStandardId", standardsListAdapter.getData().get(position).getId());
            intent.putExtra("goodsTitle", name.getText().toString());
            startActivity(intent);

        });
    }

    @Override
    protected void setUpData() {

        rv_category.setLayoutManager(new LinearLayoutManager(this));
        initViewType();
    }


    private void initViewType() {
        if (goodId == -1) {
            tv_title.setText("新增商品");
            ll_add_standards.setVisibility(View.GONE);
        } else {
            tv_title.setText("商品详情");
            ll_add_standards.setVisibility(View.VISIBLE);
            getGoodInfo();
        }
    }

    //获取商品信息
    private void getGoodInfo() {

        //查询商品
        Api().xgxshopgoodsInfo(goodId).subscribe(new RxSubscribe<Goods>(this, true) {
            @Override
            protected void _onNext(Goods goods) {
                name.setText(goods.getGoodsTitle());
                tv_type.setText(goods.getFirstCategoryTitle());//分类
                tv_brand.setText(goods.getGoodsBrandTitle());//品牌
                et_remarks.setText(goods.getRemarks());
                num.setText(String.valueOf(null == goods.getRemindLimit() ? 0 : goods.getRemindLimit()));
                sw.setChecked(null != goods.getWxType() && goods.getWxType() == 1);
                brand.setBrandId(goods.getGoodsBrandId());
                brand.setBrandTitle(goods.getGoodsBrandTitle());
                firstCategoryId = goods.getFirstCategoryId();
                firstCategoryTitle = goods.getFirstCategoryTitle();

                standardsListAdapter.setNewData(goods.getXgxGoodsStandardPojoList());

                for (Goods.GoodsPic pic : goods.getGoodsDetailsPojoList()) {
                    LocalMedia localMedia = new LocalMedia();
                    localMedia.setPath(pic.getImage());
                    localMedia.setId(pic.getId());
                    localMedia.setDate(String.valueOf(System.currentTimeMillis()));
                    netList.add(localMedia);
                }

                for (Goods.GoodsPic pic : goods.getGoodsInfoPicList()) {
                    LocalMedia localMedia = new LocalMedia();
                    localMedia.setPath(pic.getImage());
                    localMedia.setId(pic.getId());
                    localMedia.setDate(String.valueOf(System.currentTimeMillis()));
                    netList2.add(localMedia);
                }


                showlist.addAll(netList);
                showlist2.addAll(netList2);

                headAdapter.setList(showlist);
                headAdapter.notifyDataSetChanged();

                infoAdapter.setList(showlist2);
                infoAdapter.notifyDataSetChanged();

                if (netList.size() > 0) {
                    rv.setVisibility(View.VISIBLE);
                    isrvShow1 = true;
                }
                if (netList2.size() > 0) {
                    rv2.setVisibility(View.VISIBLE);
                    isrvShow2 = true;
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_stock_add_goods;
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
    private GridImageAdapter.OnItemDeleteListener onItemDeleteListener = id -> delete(id);

    //删除图片
    private void delete(int id) {
        Api().deleteDetails(String.valueOf(id)).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                ToastUtils.showToast("图片删除成功！");
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("图片添删除失败！");
            }
        });

    }


    int uploadTaskCount;//七牛上传图片完成计数

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                    headAdapter.setList(showlist);
                    headAdapter.notifyDataSetChanged();

                    uploadImg2QiNiu2(selectList, 1, "商品头图");
                    break;
                case requestCode2:
                    // 图片选择结果回调
                    uploadTaskCount = 0;
                    selectList2 = pictureSelector2.obtainMultipleResult(data);//

                    for (LocalMedia media : selectList2) {
                        Log.i("添加的图片2-----》", media.getPath());
                    }

                    showlist2.addAll(selectList2);
                    infoAdapter.setList(showlist2);
                    infoAdapter.notifyDataSetChanged();
                    uploadImg2QiNiu2(selectList2, 2, "商品详情图");
                    break;
            }

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

    private void uploadImg2QiNiu2(final List<LocalMedia> upList, final int type, final String tag) {


        if (upList.size() == 0) {
            return;
        }
        isUpdata = false;

        shwoProgressBar();
        UploadManager uploadManager = new UploadManager();
        UploadOptions uploadOptions = new UploadOptions(null, null, false,
                (key, percent) -> {
                    if (percent == 1.0)//上传进度等于1.0说明上传完成,通知 完成任务+1
                    {
                        sendMsg(upList.size(), tag);
                    }
                }, null);

        for (int i = 0; i < upList.size(); i++) {
            String key = "pic_" + CommonUtil.getTimeStame();
            String path = upList.get(i).getPath();

            final int finalI = i;

            uploadManager.put(path, key, Auth.create(Configure.accessKey, Configure.secretKey).uploadToken(Configure.bucket), (key1, info, res) -> {
                        // info.error中包含了错误信息，可打印调试
                        // 上传成功后将key值上传到自己的服务器
                        if (info.isOK()) {

                            Goods.GoodsPic goodsPic = new Goods.GoodsPic();
                            goodsPic.setType(type);
                            goodsPic.setImage(Configure.Domain + key1);
                            goodsPic.setGoodsId(goodId);
                            addDetails(goodsPic);

                        } else {
                            ToastUtils.showToast("上传失败！");
                        }
                    }, uploadOptions
            );
        }
    }

    //添加商品图片
    private void addDetails(Goods.GoodsPic goodsPic) {
        Api().addDetails(goodsPic).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                ToastUtils.showToast("图片添加成功！");
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("图片添加失败！");
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        getGoodInfo();
    }
}
