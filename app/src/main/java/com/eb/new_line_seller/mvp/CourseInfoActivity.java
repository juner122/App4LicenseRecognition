package com.eb.new_line_seller.mvp;

import android.support.v7.widget.RecyclerView;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.mvp.contacts.CourseInfoContacts;
import com.eb.new_line_seller.mvp.presenter.CourseInfoPtr;
import com.juner.mvp.bean.Courses;

import butterknife.BindView;
import butterknife.OnClick;


//课程信息页面
public class CourseInfoActivity extends BaseActivity<CourseInfoContacts.CourseInfoPtr> implements CourseInfoContacts.CourseInfoUI {



    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.tv_text)
    TextView tv_text;


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_course_info2;
    }


    @Override
    protected void init() {
        tv_title.setText("哥爱车学院");
        getPresenter().getInfo();
        getPresenter().initRecyclerView(rv);

    }

    @Override
    public CourseInfoContacts.CourseInfoPtr onBindPresenter() {
        return new CourseInfoPtr(this);
    }

    @Override
    public void setInfo(Courses courses) {
        title.setText(courses.getCourseName());
        tv_price.setText(String.format("￥%s", courses.getCoursePrice()));
        tv_type.setText(String.format("适用人群：%s", courses.getCourseType()));
        tv_number.setText(String.format("学习人次：%s人", courses.getPageView()));
        tv_text.setText(courses.getCourseMarke());
    }
}
