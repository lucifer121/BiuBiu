package com.android.biubiu.activity.biu;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.android.biubiu.BaseActivity;
import com.android.biubiu.R;
import com.android.biubiu.activity.mine.AboutMeActivity;
import com.android.biubiu.activity.mine.ChangeBrithdayActivity;
import com.android.biubiu.activity.mine.ChangeCityActivity;
import com.android.biubiu.activity.mine.ChangeConstellationActivity;
import com.android.biubiu.activity.mine.ChangeHeightWeightActivity;
import com.android.biubiu.activity.mine.ChangeHomeTwonActivity;
import com.android.biubiu.activity.mine.ChangeIdentityProfessionActivity;
import com.android.biubiu.activity.mine.ChangeNameActivity;
import com.android.biubiu.activity.mine.ChangeSchoolActivity;
import com.android.biubiu.activity.mine.InterestLabelActivity;
import com.android.biubiu.activity.mine.PersonalityTagActivity;
import com.android.biubiu.activity.mine.ScanUserHeadActivity;
import com.android.biubiu.adapter.UserInterestAdapter;
import com.android.biubiu.adapter.UserPagerPhotoAdapter;
import com.android.biubiu.adapter.UserPagerTagAdapter;
import com.android.biubiu.bean.InterestByCateBean;
import com.android.biubiu.bean.InterestTagBean;
import com.android.biubiu.bean.PersonalTagBean;
import com.android.biubiu.bean.UserInfoBean;
import com.android.biubiu.bean.UserPhotoBean;
import com.android.biubiu.utils.Constants;
import com.android.biubiu.utils.DensityUtil;
import com.android.biubiu.utils.HttpContants;
import com.android.biubiu.utils.LogUtil;
import com.android.biubiu.utils.SharePreferanceUtils;
import com.android.biubiu.view.MyGridView;
import com.avos.avoscloud.LogUtil.log;
import com.google.gson.Gson;

public class MyPagerActivity extends BaseActivity implements OnClickListener{
	private static final int UPDATE_INFO = 1001;
	private static final int UPDATE_PHOTOS = 1002;
	private static final int UPDATE_PERSONAL_TAG = 1003;
	private static final int UPDATE_INTEREST_TAG = 1004;
	private static final int UPDATE_HEAD = 1005;
	private ImageView userheadImv;
	private TextView usernameTv;
	private ImageView addPhotoImv;
	private ViewPager photoPager;
	private TextView userInfoTv;
	private TextView userInfoBigTv;
	private TextView userOpenTv;
	private LinearLayout nicknameLinear;
	private TextView nicknameTv;
	private LinearLayout sexLinear;
	private TextView sexTv;
	private LinearLayout birthdayLinear;
	private TextView birthdayTv;
	private LinearLayout starSignLinear;
	private TextView starSignTv;
	private LinearLayout cityLinear;
	private TextView cityTv;
	private LinearLayout hometownLinear;
	private TextView hometownTv;
	private LinearLayout heightWeightLinear;
	private TextView heightWeightTv;
	private LinearLayout identityLinear;
	private TextView identityTv;
	private LinearLayout schoolLinear;
	private TextView schoolTv;
	private LinearLayout personalTagLinear;
	private MyGridView personalTagGv;
	private LinearLayout interestTagLinear;
	private MyGridView interestTagGv;
	private RelativeLayout backRl;
	private RelativeLayout userInfoLinear;
	private UserInfoBean infoBean ;
	ImageOptions imageOptions;
	private ArrayList<View> photoPageViews = new ArrayList<View>();
	private UserPagerPhotoAdapter photoAdapter;
	//标记个人描述是否已经展开
	private boolean isOpen = false;
	private UserPagerTagAdapter personalAdapter;
	private UserInterestAdapter interestAdapter;
	ArrayList<UserPhotoBean> photoList = new ArrayList<UserPhotoBean>();
	ArrayList<PersonalTagBean> personalTagList = new ArrayList<PersonalTagBean>();
	ArrayList<InterestTagBean> interestTagList = new ArrayList<InterestTagBean>();
	//上传文件相关
	String accessKeyId = "";
	String accessKeySecret = "";
	String securityToken = "";
	String expiration = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_pager_layout);
		initView();
		TestUserBean();
		setUserInfoView(infoBean);
		setUserPhotos(photoList);
		setPersonalTags(personalTagList);
		setInterestTags(interestTagList);
	}

	private void TestUserBean() {
		// TODO Auto-generated method stub
		infoBean = new UserInfoBean();
		String photoStr = "http://ac-tcd4rj3s.clouddn.com/EE4DqTAx7ZFWaMUq1bQqfXDWFW0n1TehKAW5bCQk.jpeg";
		infoBean.setNickname("小强");
		infoBean.setSex("男");
		infoBean.setIconOrign(photoStr);
		infoBean.setIconCircle(photoStr);
		infoBean.setBirthday("2006-01-01");
		infoBean.setCity("北京");
		infoBean.setHeight(0);
		infoBean.setHomeTown("北京");
		infoBean.setSchool("幼儿园");
		infoBean.setStar("小强座");
		ArrayList<UserPhotoBean> photos = new ArrayList<UserPhotoBean>();
		ArrayList<InterestTagBean> tags = new ArrayList<InterestTagBean>();
		ArrayList<PersonalTagBean> tags2 = new ArrayList<PersonalTagBean>();
		for (int i = 0; i < 6; i++) {
			UserPhotoBean phBean = new UserPhotoBean();
			phBean.setPhotoOrign(photoStr);
			photos.add(phBean);
			InterestTagBean bean = new InterestTagBean();
			bean.setName("标签"+i);
			tags.add(bean);
			PersonalTagBean bean2 = new PersonalTagBean();
			bean2.setTag("标签"+i);
			tags2.add(bean2);
		}
		//log.d("mytest", beanList.toString());
		//Gson gson = new Gson();
		//log.d("mytest", ""+gson.fromJson(beanList.toString(), TagBean[].class));
		infoBean.setUserPhotos(photos);
		infoBean.setPersonalTags(tags2);
		infoBean.setInterestTags(tags);
		infoBean.setCareer("lala");
		infoBean.setCompany("lala");
		infoBean.setAboutMe(getResources().getString(R.string.page_test));
		infoBean.setIsStudent(Constants.IS_STUDENT_FLAG);

		photoList.addAll(infoBean.getUserPhotos());
		interestTagList.addAll(infoBean.getInterestTags());
		personalTagList.addAll(infoBean.getPersonalTags());
	}

	private void initView() {
		// TODO Auto-generated method stub
		userheadImv = (ImageView) findViewById(R.id.userhead_imv);
		userheadImv.setOnClickListener(this);
		usernameTv = (TextView) findViewById(R.id.username_tv);
		addPhotoImv = (ImageView) findViewById(R.id.add_userphoto_imv);
		addPhotoImv.setOnClickListener(this);
		photoPager = (ViewPager) findViewById(R.id.userphoto_pager);
		userInfoTv = (TextView) findViewById(R.id.userinfo_tv);
		userInfoBigTv = (TextView) findViewById(R.id.userinfo_big_tv);
		userOpenTv = (TextView) findViewById(R.id.open_tv);
		userOpenTv.setOnClickListener(this);
		userInfoLinear = (RelativeLayout) findViewById(R.id.userinfo_linear);
		userInfoLinear.setOnClickListener(this);
		nicknameLinear = (LinearLayout) findViewById(R.id.nickname_linear);
		nicknameLinear.setOnClickListener(this);
		nicknameTv = (TextView) findViewById(R.id.nickname_tv);
		sexLinear = (LinearLayout) findViewById(R.id.sex_linear);
		sexTv = (TextView) findViewById(R.id.sex_tv);
		birthdayLinear = (LinearLayout) findViewById(R.id.birthday_linear);
		birthdayLinear.setOnClickListener(this);
		birthdayTv = (TextView) findViewById(R.id.birthday_tv);
		starSignLinear = (LinearLayout) findViewById(R.id.starsign_linear);
		starSignLinear.setOnClickListener(this);
		starSignTv = (TextView) findViewById(R.id.starsign_tv);
		cityLinear = (LinearLayout) findViewById(R.id.city_linear);
		cityLinear.setOnClickListener(this);
		cityTv = (TextView) findViewById(R.id.city_tv);
		hometownLinear = (LinearLayout) findViewById(R.id.hometown_linear);
		hometownLinear.setOnClickListener(this);
		hometownTv = (TextView) findViewById(R.id.hometown_tv);
		heightWeightLinear = (LinearLayout) findViewById(R.id.height_weight_linear);
		heightWeightLinear.setOnClickListener(this);
		heightWeightTv = (TextView) findViewById(R.id.height_weight_tv);
		identityLinear = (LinearLayout) findViewById(R.id.identity_linear);
		identityLinear.setOnClickListener(this);
		identityTv = (TextView) findViewById(R.id.identity_tv);
		schoolLinear = (LinearLayout) findViewById(R.id.school_linear);
		schoolLinear.setOnClickListener(this);
		schoolTv = (TextView) findViewById(R.id.school_tv);
		personalTagLinear = (LinearLayout) findViewById(R.id.personal_tag_linear);
		personalTagLinear.setOnClickListener(this);
		personalTagGv = (MyGridView) findViewById(R.id.personal_tag_gv);
		interestTagLinear = (LinearLayout) findViewById(R.id.interest_tag_linear);
		interestTagLinear.setOnClickListener(this);
		interestTagGv = (MyGridView) findViewById(R.id.interest_tag_gv);
		backRl = (RelativeLayout) findViewById(R.id.back_rl);
		backRl.setOnClickListener(this);

		imageOptions = new ImageOptions.Builder()
		.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
		.setLoadingDrawableId(R.drawable.ic_launcher)
		.setFailureDrawableId(R.drawable.ic_launcher)
		.build();

		photoPager.setOffscreenPageLimit(3);
		photoPager.setPageMargin(DensityUtil.dip2px(getApplicationContext(), 10));
	}
	private void setUserInfoView(UserInfoBean bean) {
		// TODO Auto-generated method stub
		x.image().bind(userheadImv, bean.getIconCircle(), imageOptions);
		usernameTv.setText(bean.getNickname());
		nicknameTv.setText(bean.getNickname());
		sexTv.setText(bean.getSex());
		birthdayTv.setText(bean.getBirthday());
		starSignTv.setText(bean.getStar());
		cityTv.setText(bean.getCity());
		hometownTv.setText(bean.getHomeTown());
		heightWeightTv.setText(bean.getHeight()+","+bean.getWeight());
		if(bean.getIsStudent().equals(Constants.IS_STUDENT_FLAG)){
			identityTv.setText("学生");
			schoolTv.setText(bean.getSchool());
		}else{
			identityTv.setText(bean.getCareer());
			schoolTv.setText(bean.getCompany());
		}

		userInfoTv.setText(R.string.page_test);
		userInfoBigTv.setText(R.string.page_test);
	}
	private void setUserPhotos(ArrayList<UserPhotoBean> photos) {
		// TODO Auto-generated method stub
		photoPageViews.clear();
		for(int i=0;i<photos.size();i++){
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.userpager_photo_item, null);
			photoPageViews.add(view);
		}
		photoAdapter = new UserPagerPhotoAdapter(getApplicationContext(), photos, imageOptions, photoPageViews);
		photoPager.setAdapter(photoAdapter);
	}
	private void setInterestTags(ArrayList<InterestTagBean> tags) {
		// TODO Auto-generated method stub
		interestAdapter = new UserInterestAdapter(getApplicationContext(), tags);
		interestTagGv.setAdapter(interestAdapter);
	}
	private void setPersonalTags(ArrayList<PersonalTagBean> tags) {
		// TODO Auto-generated method stub
		personalAdapter = new UserPagerTagAdapter(getApplicationContext(), tags);
		personalTagGv.setAdapter(personalAdapter);
	}
	private void getUserInfo(){
		RequestParams params = new RequestParams(HttpContants.HTTP_ADDRESS+HttpContants.REGISTER_METHOD);
		JSONObject requestObject = new JSONObject();
		try {
			requestObject.put("token",SharePreferanceUtils.getInstance().getToken(getApplicationContext(), SharePreferanceUtils.TOKEN, ""));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		params.addBodyParameter("data",requestObject.toString());
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(String arg0) {
				// TODO Auto-generated method stub

			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.userhead_imv:
			Intent headIntent = new Intent(MyPagerActivity.this,ScanUserHeadActivity.class);
			headIntent.putExtra("userhead", infoBean.getIconCircle());
			headIntent.putExtra("isMyself", true);
			startActivity(headIntent);
			break;
		case R.id.open_tv:
			if(isOpen){
				userInfoBigTv.setVisibility(View.GONE);
				userInfoTv.setVisibility(View.VISIBLE);
				userOpenTv.setText("展开");
				isOpen = false;
			}else{
				userInfoTv.setVisibility(View.GONE);
				userInfoBigTv.setVisibility(View.VISIBLE);
				userOpenTv.setText("收起");
				isOpen = true;
			}
			break;
		case R.id.add_userphoto_imv:
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					"image/*");
			startActivityForResult(intent, UPDATE_PHOTOS);
			break;
		case R.id.userinfo_linear:
			Intent aboutIntent = new Intent(MyPagerActivity.this,AboutMeActivity.class);
			aboutIntent.putExtra("userInfoBean", infoBean);
			startActivityForResult(aboutIntent, UPDATE_INFO);
			break;
		case R.id.nickname_linear:
			Intent nicknameIntent = new Intent(MyPagerActivity.this,ChangeNameActivity.class);
			startActivity(nicknameIntent);
			break;
		case R.id.birthday_linear:
			Intent birthIntent = new Intent(MyPagerActivity.this,ChangeBrithdayActivity.class);
			startActivity(birthIntent);
			break;
		case R.id.starsign_linear:
			Intent starIntent = new Intent(MyPagerActivity.this,ChangeConstellationActivity.class);
			startActivity(starIntent);
			break;
		case R.id.city_linear:
			Intent cityIntent = new Intent(MyPagerActivity.this,ChangeCityActivity.class);
			startActivity(cityIntent);
			break;
		case R.id.hometown_linear:
			Intent homeIntent = new Intent(MyPagerActivity.this,ChangeHomeTwonActivity.class);
			startActivity(homeIntent);
			break;
		case R.id.height_weight_linear:
			Intent tallIntent = new Intent(MyPagerActivity.this,ChangeHeightWeightActivity.class);
			startActivity(tallIntent);
			break;
		case R.id.identity_linear:
			Intent identityIntent = new Intent(MyPagerActivity.this,ChangeIdentityProfessionActivity.class);
			startActivity(identityIntent);
			break;
		case R.id.school_linear:
			Intent schoolIntent = new Intent(MyPagerActivity.this,ChangeSchoolActivity.class);
			startActivity(schoolIntent);
			break;
		case R.id.personal_tag_linear:
			Intent personalTagIntent=new Intent(MyPagerActivity.this,PersonalityTagActivity.class);
			startActivity(personalTagIntent);
			break;
		case R.id.interest_tag_linear:
			Intent interestLableIntent=new Intent(MyPagerActivity.this,InterestLabelActivity.class);
			startActivity(interestLableIntent);
			break;
		case R.id.back_rl:
			finish();
			break;
		default:
			break;
		}
	}
	//鉴权
	public void getOssToken(final String path){
		RequestParams params = new RequestParams(HttpContants.HTTP_ADDRESS+"app/auth/getOSSSecurityToken");
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Throwable ex, boolean arg1) {
				// TODO Auto-generated method stub
				LogUtil.d("mytest", "error--"+ex.getMessage());
				LogUtil.d("mytest", "error--"+ex.getCause());
			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(String arg0) {
				// TODO Auto-generated method stub
				try {
					JSONObject jsonObjs = new JSONObject(arg0);
					JSONObject obj = jsonObjs.getJSONObject("data");
					accessKeyId = obj.getString("accessKeyId");
					accessKeySecret = obj.getString("accessKeySecret");
					securityToken = obj.getString("securityToken");
					expiration = obj.getString("expiration");
					//上传到阿里云
					asyncPutObjectFromLocalFile(path);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	// 从本地文件上传，使用非阻塞的异步接口
	public void asyncPutObjectFromLocalFile(String path) {
		String endpoint = HttpContants.A_LI_YUN;
		//OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("XWp6VLND94vZ8WNJ", "DSi9RRCv4bCmJQZOOlnEqCefW4l1eP");
		OSSCredentialProvider credetialProvider = new OSSFederationCredentialProvider() {
			@Override
			public OSSFederationToken getFederationToken() {

				return new OSSFederationToken(accessKeyId, accessKeySecret, securityToken, expiration);
			}
		};
		ClientConfiguration conf = new ClientConfiguration();
		conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
		conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
		conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
		conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
		OSSLog.enableLog();
		OSS oss = new OSSClient(getApplicationContext(), endpoint, credetialProvider, conf);
		String deviceId = SharePreferanceUtils.getInstance().getDeviceId(getApplicationContext(), SharePreferanceUtils.DEVICE_ID, "");
		final String fileName = "profile/"+System.currentTimeMillis()+deviceId+".png";
		// 构造上传请求
		PutObjectRequest put = new PutObjectRequest("protect-app",fileName, path);

		// 异步上传时可以设置进度回调
		put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
			@Override
			public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
				Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
			}
		});
		OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
			@Override
			public void onSuccess(PutObjectRequest request, PutObjectResult result) {
				Log.d("PutObject", "UploadSuccess");
				Log.d("ETag", result.getETag());
				Log.d("RequestId", result.getRequestId());
				LogUtil.d("mytest", result.getServerCallbackReturnBody().toString());
				//上传照片成功，调用修改头像接口
				uploadPhoto(fileName);
			}
			@Override
			public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
				// 请求异常
				if (clientExcepion != null) {
					// 本地异常如网络异常等
					clientExcepion.printStackTrace();
				}
				if (serviceException != null) {
					// 服务异常
					Log.e("ErrorCode", serviceException.getErrorCode());
					Log.e("RequestId", serviceException.getRequestId());
					Log.e("HostId", serviceException.getHostId());
					Log.e("RawMessage", serviceException.getRawMessage());
				}
			}
		});
	}
	//将图片上传到后台
	protected void uploadPhoto(String fileName) {
		// 上传成功后更新界面
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case UPDATE_INFO:
			if(resultCode != RESULT_OK){
				return;
			}
			if(data == null ){
				return;
			}
			UserInfoBean bean = (UserInfoBean) data.getSerializableExtra("userInfoBean");
			setUserInfoView(bean);
			break;
		case UPDATE_PHOTOS:
			if(null == data){
				return;
			}
			Bitmap bm = null;
			//外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
			ContentResolver resolver = getContentResolver();
			//获得图片的uri 
			Uri originalUri = data.getData();      
			//显得到bitmap图片这里开始的第二部分，获取图片的路径：
			try {
				bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
				String[] proj = {MediaStore.Images.Media.DATA};
				//好像是android多媒体数据库的封装接口，具体的看Android文档
				Cursor cursor = managedQuery(originalUri, proj, null, null, null); 
				//按我个人理解 这个是获得用户选择的图片的索引值
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				//将光标移至开头 ，这个很重要，不小心很容易引起越界
				cursor.moveToFirst();
				//最后根据索引值获取图片路径
				String path = cursor.getString(column_index);
				//上传图片鉴权
				getOssToken(path);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			break;
		case UPDATE_HEAD:
			if(null == data){
				return;
			}
			break;
		case UPDATE_INTEREST_TAG:
			if(null == data){
				return;
			}
			break;
		case UPDATE_PERSONAL_TAG:
			if(null == data){
				return;
			}
			break;
		default:
			break;
		}
	}
}
