package com.eb.geaiche.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eb.geaiche.R;
import com.eb.geaiche.bean.AutoBrand;

import java.util.HashMap;
import java.util.List;

public class AutoBrandSelectorAdapter extends BaseAdapter implements Indexer {

    private final LayoutInflater mInflate;
    private List<AutoBrand> mCitys;
    private Context mContext;
    private HashMap<String, Integer> indexMap = new HashMap<>();

    public AutoBrandSelectorAdapter(Context context, List<AutoBrand> citys) {
        mCitys = citys;
        mContext = context;
        mInflate = LayoutInflater.from(mContext);
        // 列表特征和分组首项进行关联
        for (int i = 0; i < mCitys.size(); i++) {
            AutoBrand city = mCitys.get(i);
            String type = city.getType();
            if (type == null || "".equals(type)) continue;
            if (!indexMap.containsKey(type)) {
                indexMap.put(type, i);
            }
        }
    }

    public int getCount() {
        return mCitys.size();
    }

    public Object getItem(int position) {
        return mCitys.get(position);
    }

    public long getItemId(int position) {
        return mCitys.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflate.inflate(R.layout.activity_auto_brand, parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.name);
            holder.tvSection = (TextView) convertView.findViewById(R.id.tvSection);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            holder.ll = convertView.findViewById(R.id.ll);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AutoBrand city = mCitys.get(position);
        holder.tvName.setText(city.getName());

        Glide.with(mContext)
                .load(city.getImage())
                .into(holder.iv);

        String type = city.getType();
        if (position == 0) {
            holder.ll.setVisibility(View.VISIBLE);
            setIndex(holder.tvSection, type);
        } else {
            String preLabel = mCitys.get(position - 1).getType();

            if (!type.equals(preLabel)) { // diff group
                setIndex(holder.tvSection, String.valueOf(type));
                holder.ll.setVisibility(View.VISIBLE);
            } else { // same group
                holder.ll.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    private void setIndex(TextView section, String str) {
        section.setVisibility(View.VISIBLE);
//        if ("#".equals(str)) section.setText("当前城市");
//        else
            section.setText(str);
    }

    @Override
    public int getStartPositionOfSection(String section) {
        if (indexMap.containsKey(section))
            return indexMap.get(section);
        return -1;
    }

    static class ViewHolder {
        TextView tvName;
        TextView tvSection;
        ImageView iv;
        View ll;
    }
}
