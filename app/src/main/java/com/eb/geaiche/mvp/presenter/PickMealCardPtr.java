package com.eb.geaiche.mvp.presenter;

import android.app.Activity;
import androidx.annotation.NonNull;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.MealPickListAdapter;
import com.eb.geaiche.bean.Meal2;
import com.eb.geaiche.bean.MealEntity;
import com.eb.geaiche.bean.MealL0Entity;
import com.eb.geaiche.mvp.contacts.PickMealCardContacts;
import com.eb.geaiche.mvp.model.PickMealCardMdl;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

public class PickMealCardPtr extends BasePresenter<PickMealCardContacts.PickMealCardUI> implements PickMealCardContacts.PickMealCardPtr {

    private PickMealCardContacts.PickMealCardMdl mdl;
    private Activity context;


    public PickMealCardPtr(@NonNull PickMealCardContacts.PickMealCardUI view) {
        super(view);
        context = view.getSelfActivity();
        mdl = new PickMealCardMdl(context);
    }

    @Override
    public void getMealList() {

        mdl.getMealList(new RxSubscribe<List<Meal2>>(context, true) {
            @Override
            protected void _onNext(List<Meal2> list) {


                getView().setMealList(generateData(list));

            }

            @Override
            protected void _onError(String message) {
                getView().showToast(message);
            }
        });

    }

    @Override
    public void setOnItemChildClickListener(final MealPickListAdapter a) {


        a.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {

                    case R.id.ll_item:
                        MealL0Entity m = (MealL0Entity) adapter.getData().get(position);

                        if (m.isExpanded()) {//是否打开
                            adapter.collapse(position);
                            getView().setPickMealName("");
                        } else {
                            int size = adapter.getData().size();
                            int e = 0;
                            for (int i = 0; i < size; i++) {
                                if (adapter.getData().get(i) instanceof MealL0Entity && ((MealL0Entity) adapter.getData().get(i)).isExpanded()) {
                                    if (position > i) //点击比当前展开项后面的项才查找
                                        e = ((MealL0Entity) adapter.getData().get(i)).getSubItems().size();//先查找出展开项的子项数量
                                    break;
                                }
                            }
                            //收起所有子项
                            for (int i = 0; i < size; i++) {
                                adapter.collapse(i);
                            }
//                            adapter.expand(position);//打开列表
                            adapter.expand(position - e);//
                            getView().setPickMealName(m.getActivityName());
                        }
                        break;

                    case R.id.ib_plus:
                        int max = ((MealEntity) adapter.getData().get(position)).getMaxNum();
                        int now = ((MealEntity) adapter.getData().get(position)).getNumber();

                        if (max <= now)
                            getView().showToast("不能超过最大数量");
                        else
                            ((MealEntity) adapter.getData().get(position)).setNumber(++now);
                        adapter.notifyDataSetChanged();

                        break;

                    case R.id.ib_reduce:


                        int now_reduce = ((MealEntity) adapter.getData().get(position)).getNumber();

                        if (now_reduce == 0)
                            getView().showToast("数量不能少于零");
                        else
                            ((MealEntity) adapter.getData().get(position)).setNumber(--now_reduce);
                        adapter.notifyDataSetChanged();
                        break;


                }
            }
        });


    }

    @Override
    public void getMeal2(List<MultiItemEntity> data) {


        if (null == transformationMeal2(data)) {
            getView().showToast("请选择一个套卡！");
            return;

        }

        getView().onConfirmPick(transformationMeal2(data));
    }


    private Meal2 transformationMeal2(List<MultiItemEntity> list) {


        List<MealL0Entity> mealL0s = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof MealL0Entity) {
                mealL0s.add((MealL0Entity) list.get(i));
            }
        }

        for (MealL0Entity m : mealL0s) {
            if (m.isExpanded()) {
                return d(m);
            }
        }


        return null;
    }

    private Meal2 d(MealL0Entity m) {

        Meal2 meal2 = new Meal2();

        meal2.setGoodsList(m.getSubItems());
        meal2.setActivityId(m.getActivityId());
        meal2.setActName(m.getActivityName());
        meal2.setId(m.getId());


        return meal2;
    }


    private List<MultiItemEntity> generateData(List<Meal2> list) {


        List<MultiItemEntity> res = new ArrayList<>();
        if (null == list || list.size() == 0) {
            return res;

        }

        for (int i = 0; i < list.size(); i++) {
            if (null != list.get(i).getGoodsList() && list.get(i).getGoodsList().size() > 0) {
                MealL0Entity lv0 = new MealL0Entity();
                lv0.setId(list.get(i).getId());
                lv0.setActivityId(list.get(i).getActivityId());
                lv0.setActivityName(list.get(i).getActName());
                for (MealEntity entity : list.get(i).getGoodsList()) {
                    if (null != entity) {
                        entity.setMaxNum(entity.getNumber());//设置最大值
                        lv0.addSubItem(entity);
                    }

                }

                if (null != lv0.getSubItems())
                    res.add(lv0);
            }

        }
        return res;
    }
}
