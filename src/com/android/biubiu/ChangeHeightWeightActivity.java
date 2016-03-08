package com.android.biubiu;

import com.android.biubiu.activity.BaseActivity;
import com.android.biubiu.activity.mine.ChangeConstellationActivity;
import com.android.biubiu.common.city.ArrayWheelAdapter;
import com.android.biubiu.common.city.OnWheelChangedListener;
import com.android.biubiu.common.city.WheelView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class ChangeHeightWeightActivity extends BaseActivity implements OnWheelChangedListener{
	private WheelView heightWheelView,weightWheelView;
	
	private String [] heights=new String[70],weights=new String[60];
//	private String heights[]={"170","171","172"},weights[]={"40","50","60"};
	private TextView nheightWeight;
	private int pHeightCurrent=0,pWeightCurrent=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_height_weight);
		loadDate();
		initView();
		
		setUpData();
		setUpListener();
	}
/**
 * 加载数据
 */
	private void loadDate() {
		
		for(int i=0;i<70;i++){
			heights[i]=""+(i+150)+"cm";
			
		}
		for(int j=0;j<60;j++){
			weights[j]=""+(j+40)+"kg";
			
		}
		
	}
	private void setUpData() {
		// initProvinceDatas();
	//	constellation.setText(mProvinceDatas[0]);
		heightWheelView.setViewAdapter(new ArrayWheelAdapter<String>(
				ChangeHeightWeightActivity.this, heights));
		weightWheelView.setViewAdapter(new ArrayWheelAdapter<String>(ChangeHeightWeightActivity.this, weights));

	//	Log.e("lucifer", "mProvinceDatas.length==" + mProvinceDatas.length);
		// 设置可见条目数量
		heightWheelView.setVisibleItems(7);
		weightWheelView.setVisibleItems(7);

	}

	private void initView() {
		// TODO Auto-generated method stub
		heightWheelView=(WheelView) findViewById(R.id.id_hight_myview);
		weightWheelView=(WheelView) findViewById(R.id.id_weight_myview);
		nheightWeight=(TextView) findViewById(R.id.height_weight_change_city_tv);
	}
	private void setUpListener() {
		// 添加change事件
		heightWheelView.addChangingListener(this);
		weightWheelView.addChangingListener(this);
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == heightWheelView) {

			pHeightCurrent = heightWheelView.getCurrentItem();
	
			nheightWeight.setText(""+heights[pHeightCurrent]+weights[pWeightCurrent]);
		} 
		if (wheel == weightWheelView) {

			pWeightCurrent = weightWheelView.getCurrentItem();
			nheightWeight.setText(""+heights[pHeightCurrent]+weights[pWeightCurrent]);
		
		} 
		
		
	}

	

}
