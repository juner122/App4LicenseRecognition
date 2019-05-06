package com.eb.geaiche.mvp.presenter;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.eb.geaiche.activity.CustomRecordsActivity;
import com.eb.geaiche.bean.Meal2;
import com.eb.geaiche.bean.MealEntity;
import com.eb.geaiche.view.CardInputConfirmDialog;
import com.eb.geaiche.view.MyTimePickerView;
import com.juner.mvp.Configure;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.RemakeActCard;
import com.juner.mvp.bean.SaveUserAndCarEntity;
import com.eb.geaiche.mvp.contacts.ActivityCardContacts;
import com.eb.geaiche.mvp.model.ActivateCardMdl;
import com.juner.mvp.bean.UserEntity;

import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.eb.geaiche.util.DateUtil.getFormatedDateTime;

public class ActivateCardPtr extends BasePresenter<ActivityCardContacts.ActivityCardUI> implements ActivityCardContacts.ActivityCardPtr {

    private ActivityCardContacts.ActivityCardMdl mdl;
    private Activity context;

    List<RemakeActCard> list;//接口请求参数对象
    List<MealEntity> goodsList;//套卡商品列表
    private int user_id;//用户id
    private String car_no = "";//车牌
    private String card_sn = "";//卡号

    private long dataStart = -1;//开始时间
    private long dataEnd = -1;//结束时间

    private String actName;//套卡名

    MyTimePickerView pvTimeStart, pvTimeEnd;//时间选择器


    CardInputConfirmDialog confirmDialog;//信息确认Dialog

    public ActivateCardPtr(@NonNull ActivityCardContacts.ActivityCardUI view) {
        super(view);
        context = view.getSelfActivity();
        mdl = new ActivateCardMdl(context);
        list = new ArrayList<>();

        pvTimeStart = new MyTimePickerView(context);
        pvTimeEnd = new MyTimePickerView(context);
        dataStart = System.currentTimeMillis();
        dataEnd = dataStart;
    }

    @Override
    public void checkMember(String phone, String name) {

        if (phone.equals("")) {
            getView().showToast("手机号码不能为空");
            return;
        }

        mdl.checkMember(phone, name, new RxSubscribe<SaveUserAndCarEntity>(context, true) {
            @Override
            protected void _onNext(SaveUserAndCarEntity s) {
                user_id = s.getUser_id();
                new AppPreferences(context).put(Configure.user_id, user_id);//保存检测到的用户id

                if (s.getUser_id() == 0) {//录入不成功
                    Toast.makeText(context, "会员尚未注册，请完善注册信息", Toast.LENGTH_SHORT).show();
                    getView().showCheckView();

                } else {
                    getView().etsetFocusable(false);
                    getView().showView();
                    getView().hideCheckView();
                    getView().setCarName(s.getUser_name());


                    if (null == s.getUser_name() || s.getUser_name().equals("")) {
                        getView().etnamesetFocusable(true);
                    } else
                        getView().etnamesetFocusable(false);

                    if (null != s.getCarList()) {//新车 老会员
                        getView().setCarList(s.getCarList());//车辆列表
                    }

                }


            }

            @Override
            protected void _onError(String message) {
                getView().showToast(message);
            }
        });

    }

    @Override
    public void confirmInput(int position) {

        refreshList();

        if (position == 1) {
            mdl.confirmInput(list, new RxSubscribe<NullDataEntity>(context, true) {
                @Override
                protected void _onNext(NullDataEntity n) {

                    getView().showToast("录入成功！");

                    context.finish();

                    context.startActivity(new Intent(context, CustomRecordsActivity.class));
                }

                @Override
                protected void _onError(String message) {
                    getView().showToast("录入失败:" + message);
                }
            });
        }
        if (position == 0) {

            mdl.confirmAdd(list, new RxSubscribe<NullDataEntity>(context, true) {
                @Override
                protected void _onNext(NullDataEntity n) {

                    getView().showToast("新增成功！");

                    context.finish();
                    context.startActivity(new Intent(context, CustomRecordsActivity.class));
                }

                @Override
                protected void _onError(String message) {
                    getView().showToast("新增失败:" + message);
                }
            });
        }


    }

    @Override
    public void setMealList(Meal2 meal2) {

        goodsList = meal2.getGoodsList();
        actName = meal2.getActName();
        list.clear();//清空
        getView().setMealInfoList(goodsList, meal2.getActName());
        for (MealEntity m : goodsList) {
            RemakeActCard rac = new RemakeActCard();
            rac.setActivityId(meal2.getActivityId());
            rac.setActivityName(meal2.getActName());
            rac.setDetailsId(meal2.getId());
            rac.setGoodsId(m.getId());
            rac.setGoodsName(m.getName());
            rac.setGoodsNum(m.getNumber());
            list.add(rac);
        }

    }

    private void refreshList() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setActivitySn(card_sn);
            list.get(i).setAddTime(dataStart);
            list.get(i).setCarNo(car_no);
            list.get(i).setEndTime(dataEnd);
            list.get(i).setUserId(user_id);
        }

    }


    private boolean judgeNull(int position) {
//        if (car_no.equals("")) {
//            getView().showToast("请选择车辆！");
//            return false;
//        }

        if (null == goodsList) {
            getView().showToast("请选择一张套卡！");
            return false;

        }
        if (goodsList.size() == 0) {
            getView().showToast("套卡商品列表为0！");
            return false;

        }


        if (card_sn.equals("") && position != 0) {
            getView().showToast("卡号不能为空！");
            return false;
        }


        if (dataStart == -1 || dataEnd == -1) {
            getView().showToast("请选择套卡有效期！");
            return false;
        }
        return true;
    }


    @Override
    public void setCarNo(String carNo) {
        car_no = carNo;
    }

    @Override
    public void setCardSn(String cardSn) {
        card_sn = cardSn;
    }

    @Override
    public void setStartData(View v) {

        pvTimeStart.init(Calendar.getInstance(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                ((TextView) v).setText(getFormatedDateTime(date));
                dataStart = date.getTime();
            }
        });
        pvTimeStart.show(v);
    }

    @Override
    public void setEndData(View v) {
        pvTimeEnd.init(Calendar.getInstance(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                ((TextView) v).setText(getFormatedDateTime(date));
                dataEnd = date.getTime();
            }
        });
        pvTimeEnd.show(v);
    }

    @Override
    public void showConfirmDialog(int position) {
        if (!judgeNull(position)) return;

        confirmDialog = new CardInputConfirmDialog(context, goodsList, actName, card_sn, position);
        confirmDialog.setClicklistener(new CardInputConfirmDialog.ClickListenerInterface() {
            @Override
            public void doConfirm(String sn) {
                card_sn = sn;
                getView().onShowConfirmDialog();
            }

            @Override
            public void doCancel() {
                confirmDialog.dismiss();
            }
        });

        confirmDialog.show();
    }

    @Override
    public void getInfo() {
        mdl.getInfo(new RxSubscribe<UserEntity>(context, true) {
            @Override
            protected void _onNext(UserEntity userEntity) {

                //设置录卡人
                getView().setUserName(userEntity.getUsername());
            }

            @Override
            protected void _onError(String message) {

                getView().showToast("录卡人获取失败:" + message);

            }
        });
    }


}
