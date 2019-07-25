/**
 * 
 */
package com.kernal.smartvision.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**   
 *    
 * 项目名称：SmartVisionOCRV1.0.0.3  
 * 类名称：VinParseResultAdapter  
 * 类描述：   
 * 创建人：user  
 * 创建时间：2016-9-27 下午3:28:19  
 * 修改人：user  
 * 修改时间：2018年12月5日10:50:52
 * 修改备注：   
 * @version    
 *    
 */
public class VinParseResultAdapter extends BaseAdapter {
	private Context context;
	private ViewHolder holder = null;
	private LayoutInflater inflater;
	public List<HashMap<String, String>> recogResult;
	private int width, height;
	private String keyName="";
	public VinParseResultAdapter(Context context, List<HashMap<String, String>> recogResult, int width, int height){
		this.inflater = LayoutInflater.from(context);
		this.recogResult = recogResult;
		this.context = context;
		this.width = width;
		this.height = height;
	}
	@Override
	public int getCount() {
		return recogResult.size();
	}

	@Override
	public Object getItem(int position) {
		return recogResult.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	public void SetData(List<HashMap<String, String>> recogResult){
		this.recogResult = recogResult;
	}
	public static class ViewHolder {	
		private TextView tv_result = null;
		private RelativeLayout vin_parse_showResult = null;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					context.getResources().getIdentifier(
							"activity_vinparse_list_result", "layout",
							context.getPackageName()), null);
			
			holder.tv_result =  (TextView) convertView.findViewById(context.getResources().getIdentifier("tv_result",
	                "id", context.getPackageName()));
			holder.vin_parse_showResult = (RelativeLayout) convertView
					.findViewById(context.getResources().getIdentifier("vin_parse_showResult",
			                "id", context.getPackageName()));
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (width * 0.82),
					(int) (height * 0.06));
			params.leftMargin = (int) (width * 0.04);
			holder.tv_result.setLayoutParams(params);
			
			params = new RelativeLayout.LayoutParams(width,
					(int) (height * 0.05));
			params.leftMargin = 0;
			holder.vin_parse_showResult.setLayoutParams(params);
			
			 convertView.setTag(holder);
		} else {
				holder = (ViewHolder) convertView.getTag();
				
		}
		
		keyName=recogResult.get(position).keySet().iterator().next();
		holder.tv_result.setText(keyName+":"+recogResult.get(position).get(keyName));
		return convertView;
	}

}
