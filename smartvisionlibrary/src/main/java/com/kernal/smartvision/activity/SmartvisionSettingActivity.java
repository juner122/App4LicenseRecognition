/**
 * 
 */
package com.kernal.smartvision.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.kernal.smartvision.adapter.SetDocTypeAdapter;
import com.kernal.smartvision.ocr.OCRConfigParams;
import com.kernal.smartvision.utils.PermissionUtils;
import com.kernal.smartvision.utils.WriteToPCTask;
import com.kernal.smartvisionocr.model.TempleModel;
import com.kernal.smartvisionocr.utils.KernalLSCXMLInformation;
import com.kernal.smartvisionocr.utils.SharedPreferencesHelper;
import com.kernal.smartvisionocr.utils.Utills;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 项目名称：SmartVisionOCR 类名称：SmartvisionSettingActivity 类描述： 创建人：张志朋 创建时间：2016-5-3 下午3:33:16
 * 修改人：user 修改时间：2016-5-3 下午3:33:16 修改备注：
 * 
 * @version
 * 
 */
public class SmartvisionSettingActivity extends Activity   implements ActivityCompat.OnRequestPermissionsResultCallback{
	private DisplayMetrics displayMetrics = new DisplayMetrics();
	private ListView listview_setting;
	private int srcWidth, srcHeight;
	private SetDocTypeAdapter adapter;
	private KernalLSCXMLInformation wlci_Landscape, wlci_Portrait;
	private View view1, view2;
	private RelativeLayout layout_set, layout_menu;
	private Button btn_setting_back;
	private ViewPager mTabPager;
    private TextView setting_upload, Document_formats;
    private int currIndex = 0;
    private Boolean isDocTypeSetting = true;
    private TextView tv_set_upload_ip;
    private EditText et_set_upload_ip;
    private CheckBox cb_isupload;
    private int selectType ;

	List<TempleModel> TempListData =  new ArrayList<TempleModel>();

	public  static final String[] PERMISSION = new String[]{
			Manifest.permission.WRITE_EXTERNAL_STORAGE,// 写入权限
			Manifest.permission.READ_EXTERNAL_STORAGE // 读取权限
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		if (Build.VERSION.SDK_INT >= 23) {
			permission();
		} else {
			initView();
		}

	}

	private void initView(){
		setContentView(getResources().getIdentifier("activity_setting", "layout", getPackageName()));
		Utills.copyFile(this.getApplicationContext());
		srcWidth = displayMetrics.widthPixels;
		srcHeight = displayMetrics.heightPixels;
		wlci_Landscape =  Utills.parseXmlFile(this,"appTemplateConfig.xml",
				false);
		wlci_Portrait =  Utills.parseXmlFile(this,"appTemplatePortraitConfig.xml", false);
		TempListData.addAll(wlci_Landscape.template);
		adapter = new SetDocTypeAdapter(getApplicationContext(), srcWidth, srcHeight, TempListData,wlci_Portrait.template);
		LayoutInflater mLi = LayoutInflater.from(this);
		view1 = mLi.inflate(getResources().getIdentifier(
				"activity_set_upload", "layout", this.getPackageName()), null);
		view2 = mLi.inflate(getResources().getIdentifier(
				"activity_set_doctype", "layout", this.getPackageName()), null);
		findTotalView();
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}
			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
			@Override
			public void finishUpdate(View arg0) {
			}
			@Override
			public void restoreState(Parcelable arg0, ClassLoader arg1) {
			}
			@Override
			public Parcelable saveState() {
				return null;
			}
			@Override
			public void startUpdate(View arg0) {
			}
		};

		mTabPager.setAdapter(mPagerAdapter);
		isDocTypeSetting = SharedPreferencesHelper.getBoolean(
				getApplicationContext(), "isDocTypeSetting", true);
		if (SharedPreferencesHelper.getBoolean(getApplicationContext(), "isDocTypeSetting", true)) {
			setting_upload.setBackgroundResource(getResources().getIdentifier("unselect_settype",
					"drawable", this.getPackageName()));
			Document_formats
					.setBackgroundResource(getResources().getIdentifier("set_select_backgroud",
							"drawable", this.getPackageName()));

			mTabPager.setCurrentItem(1);
		} else {
			setting_upload
					.setBackgroundResource(getResources().getIdentifier("set_select_backgroud",
							"drawable", this.getPackageName()));
			Document_formats.setBackgroundResource(getResources().getIdentifier("unselect_settype",
					"drawable", this.getPackageName()));

			mTabPager.setCurrentItem(0);
		}
		findUploadView();
		findDocTypeView();
	}

	/**
	 * 权限申请
	 */
	private void  permission(){
		boolean isgranted = true;
		for (int i = 0; i < PERMISSION.length; i++) {
			if (ContextCompat.checkSelfPermission(this, PERMISSION[i]) != PackageManager.PERMISSION_GRANTED) {
				isgranted = false;
				break;
			}
		}
		if(!isgranted){
			//没有授权
			PermissionUtils.requestMultiPermissions(this,mPermissionGrant);
		}else{
			initView();
		}
	}

	private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
		@Override
		public void onPermissionGranted(int requestCode) {
			switch (requestCode) {
				case PermissionUtils.CODE_MULTI_PERMISSION:
					initView();
					break;
				default:
					break;
			}
		}
	};

	/**
	 * Callback received when a permissions request has been completed.
	 */
	@Override
	public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
	}


	private void findUploadView(){
		tv_set_upload_ip=(TextView)view1.findViewById(getResources().getIdentifier("tv_set_upload_ip", "id", getPackageName()));
		et_set_upload_ip=(EditText)view1.findViewById(getResources().getIdentifier("et_set_upload_ip", "id", getPackageName()));
		cb_isupload=(CheckBox)view1.findViewById(getResources().getIdentifier("cb_isupload", "id", getPackageName()));
		LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.leftMargin=(int) (srcWidth*0.01);
		params.topMargin=(int) (srcHeight*0.05);
		tv_set_upload_ip.setLayoutParams(params);
		params=new LayoutParams((int) (srcWidth*0.8),(int) (srcHeight*0.05));
		params.leftMargin=(int) (srcWidth*0.12);
		params.topMargin=(int) (srcHeight*0.045);
		et_set_upload_ip.setLayoutParams(params);
		et_set_upload_ip.setText(SharedPreferencesHelper.getString(
					getApplicationContext(), "URL", WriteToPCTask.httpPath));
		params=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);;
		params.topMargin=(int) (srcHeight*0.15);
		cb_isupload.setLayoutParams(params);
		if(SharedPreferencesHelper.getBoolean(getApplicationContext(), "upload",
					false)){
			cb_isupload.setChecked(true);
		}else{
			cb_isupload.setChecked(false);
		}
	}
	//设置界面总UI布局
	private void findTotalView() {
		 layout_menu = (RelativeLayout) this.findViewById(getResources().getIdentifier("layout_menu",
	                "id", this.getPackageName()));
	        btn_setting_back = (Button) this.findViewById(getResources().getIdentifier("btn_setting_back",
	                "id", this.getPackageName()));
	        btn_setting_back.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(cb_isupload.isChecked())
						{
							SharedPreferencesHelper.putBoolean(getApplicationContext(), "upload",
				                    true);	
						}else{
							SharedPreferencesHelper.putBoolean(getApplicationContext(), "upload",
				                    false);	
						}
						if(et_set_upload_ip.getText().toString()!=null){
							WriteToPCTask.httpPath=et_set_upload_ip.getText().toString();
						}
						SharedPreferencesHelper.putBoolean(getApplicationContext(),
				                "isDocTypeSetting", isDocTypeSetting);
					//保存结果；

					if (adapter.listData.size() >= 2){
						if (adapter.listData.get(0).isSelected && adapter.listData.get(1).isSelected ){
							selectType = 0;
						}else if (	adapter.listData.get(0).isSelected ){
							//vin
							selectType =  1;
						}else {
							//手机号
							selectType = 2;
						}
						SharedPreferencesHelper.putInt(getApplicationContext(), "SettingSelectType", selectType);
					}
					Utills.updateXml(SmartvisionSettingActivity.this,wlci_Landscape,"appTemplateConfig.xml");
					Utills.updateXml(SmartvisionSettingActivity.this,wlci_Portrait,"appTemplatePortraitConfig.xml");
						finish();
				}
			});
	        layout_set = (RelativeLayout) this.findViewById(getResources().getIdentifier("layout_set",
	                "id", this.getPackageName()));
	        mTabPager = (ViewPager) this.findViewById(getResources().getIdentifier("tabpager",
	                "id", this.getPackageName()));
	        mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
	        setting_upload = (TextView) this.findViewById(getResources().getIdentifier("setting_upload",
	                "id", this.getPackageName()));
	        Document_formats = (TextView) this.findViewById(getResources().getIdentifier("Document_formats",
	                "id", this.getPackageName()));
	        setting_upload.setOnClickListener(new MyOnClickListener(0));
	        Document_formats.setOnClickListener(new MyOnClickListener(1));
	        LayoutParams layoutParams = new LayoutParams(
	        		srcWidth, (int) (srcHeight * 0.05));
	        layoutParams.topMargin = 0;
	        layout_set.setLayoutParams(layoutParams);
	        layoutParams = new LayoutParams((int) (srcHeight * 0.1),
	                (int) (srcHeight * 0.03));
	        layoutParams.leftMargin = (int) (srcWidth * 0.02);
	        layoutParams.topMargin = (int) (srcHeight * 0.01);
	        btn_setting_back.setLayoutParams(layoutParams);
	        layoutParams = new LayoutParams((int) (srcWidth * 0.5),
	                (int) (srcHeight * 0.05));
	        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
	                RelativeLayout.TRUE);
	        setting_upload.setLayoutParams(layoutParams);
	        layoutParams = new LayoutParams((int) (srcWidth * 0.5),
	                (int) (srcHeight * 0.05));
	        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
	                RelativeLayout.TRUE);
	        layoutParams.leftMargin = srcWidth / 2;
	        Document_formats.setLayoutParams(layoutParams);
	        layoutParams = new LayoutParams(srcWidth, (int) (srcHeight * 0.05));
	        layoutParams.topMargin = (int) (srcHeight * 0.05);
	        layout_menu.setLayoutParams(layoutParams);
	}


	/**
	 * @Title: findView
	 * @Description: 设置模板界面UI布局
	 * @return void 返回类型
	 * @throws
	 */
	private void findDocTypeView() {
		listview_setting = (ListView) view2.findViewById(getResources().getIdentifier("listview_setting", "id", getPackageName()));
		LinearLayout.LayoutParams LinearParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearParams.leftMargin = (int) (srcWidth * 0.07);
		LinearParams.rightMargin = (int) (srcWidth * 0.07);
		LinearParams.topMargin = (int) (srcHeight * 0.05);
		listview_setting.setLayoutParams(LinearParams);
		listview_setting.setAdapter(adapter);


		if (adapter.listData != null && adapter.listData.size() > 1){
			adapter.listData.get(0).isSelected = true;
			adapter.listData.get(1).isSelected = true;

			if (OCRConfigParams.OcrType == 0){
				selectType = OCRConfigParams.getOcrType(this);
			    if (selectType == 1){
			    	//vin
					adapter.listData.get(1).isSelected = false;
				}else if (selectType == 2){
			    	// 手机号
					adapter.listData.get(0).isSelected = false;
				}


			}else if (OCRConfigParams.OcrType == 1){
				adapter.listData.remove(1);
				//adapter.listData.get(0).isSelected = true;
			}else  if ( OCRConfigParams.OcrType == 2){
				adapter.listData.remove(0);
			//	adapter.listData.get(1).isSelected = true;
			}
			adapter.notifyDataSetChanged();

        }
	}

	 public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
	        @Override
	        public void onPageSelected(int arg0) {
	            Animation animation = null;
	            switch (arg0) {
	            case 0:
	                if (currIndex == 1) {
	                    setting_upload.setBackgroundResource(getResources().getIdentifier("set_select_backgroud",
	                                    "drawable", SmartvisionSettingActivity.this.getPackageName()));
	                    Document_formats.setBackgroundResource(getResources().getIdentifier("unselect_settype",
	                                    "drawable", SmartvisionSettingActivity.this.getPackageName()));
	                    isDocTypeSetting = false;
	                }
	                break;
	            case 1:
	                if (currIndex == 0) {
	                    setting_upload.setBackgroundResource(getResources().getIdentifier("unselect_settype",
	                                    "drawable", SmartvisionSettingActivity.this.getPackageName()));
	                    Document_formats.setBackgroundResource(getResources().getIdentifier("set_select_backgroud",
	                                    "drawable", SmartvisionSettingActivity.this.getPackageName()));
	                    isDocTypeSetting = true;
	                }
	                break;
	            }
	            currIndex = arg0;
	        }

	        @Override
	        public void onPageScrolled(int arg0, float arg1, int arg2) {
	        }

	        @Override
	        public void onPageScrollStateChanged(int arg0) {
	        }
	    }

	   
	    public class MyOnClickListener implements OnClickListener {
	        private int index = 0;

	        public MyOnClickListener(int i) {
	            index = i;
	        }

	        @Override
	        public void onClick(View v) {
	            mTabPager.setCurrentItem(index);
	            if (index == 1) {
	                isDocTypeSetting = true;
	            } else if (index == 0) {
	                isDocTypeSetting = false;
	            }
	        }
	    }

	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			SharedPreferencesHelper.putBoolean(getApplicationContext(), "isDocTypeSetting", isDocTypeSetting);
			if(et_set_upload_ip.getText().toString()!=null){
				WriteToPCTask.httpPath=et_set_upload_ip.getText().toString();
			}
			if(cb_isupload.isChecked())
			{
				SharedPreferencesHelper.putBoolean(getApplicationContext(), "upload", true);
				
			}else{
				SharedPreferencesHelper.putBoolean(getApplicationContext(), "upload", false);
			}
			// 保存结果：
			if (adapter.listData.size() >= 2){
				if (adapter.listData.get(0).isSelected && adapter.listData.get(1).isSelected ){
					selectType = 0;
				}else if (	adapter.listData.get(0).isSelected ){
					//vin
					selectType =  1;
				}else {
					//手机号
					selectType = 2;
				}
				SharedPreferencesHelper.putInt(getApplicationContext(), "SettingSelectType", selectType);
			}
			Utills.updateXml(SmartvisionSettingActivity.this,wlci_Landscape,"appTemplateConfig.xml");
			Utills.updateXml(SmartvisionSettingActivity.this,wlci_Portrait,"appTemplatePortraitConfig.xml");
			finish();
		}
		return super.onKeyDown(keyCode, event);

	}

}
