package cn.xy.unittext;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xy.unittext.adapter.AddItemViewAdapter;
import cn.xy.unittext.adapter.AddressAdapter;
import cn.xy.unittext.bean.ConstantValue;
import cn.xy.unittext.bean.URLBean;
import cn.xy.unittext.bean.URLDao;
import cn.xy.unittext.util.NetWorkUtil;
import cn.xy.unittext.util.SharePrefrenceUtil;
import cn.xy.unittext.util.ToastUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AddressAdapter.recycViewItemClick, View.OnLongClickListener {

    @BindView(R.id.text_Internet)
    Button mTextInternet;
    @BindView(R.id.active_equipment)
    Button mActiveEquipment;
    @BindView(R.id.login_game)
    Button mLoginGame;
    @BindView(R.id.input_url)
    EditText mInputUrl;
    @BindView(R.id.get_history_ip)
    ImageButton mGetHistoryIp;
    @BindView(R.id.display_popView_local)
    LinearLayout mDisplayPopViewLocal;

    @BindView(R.id.input_username)
    EditText mInputUsername;
    @BindView(R.id.input_password)
    EditText mInputPassword;
    @BindView(R.id.llyout_User_psw)
    LinearLayout mLlyoutUserPsw;
    @BindView(R.id.sum_llyout_group)
    LinearLayout mSumLlyoutGroup;
    @BindView(R.id.fab_view)
    FloatingActionButton mFabView;
    @BindView(R.id.save_params)
    Button mSaveParams;

    RecyclerView mRecyclerViewLayoutPOP;
    @BindView(R.id.add_item_view)
    LinearLayout mRecyclerViewLayout;
    private AddressAdapter mAddressAdapter;
    private PopupWindow mPopupWindow;
    private int clickNumber;
    private int deleteNumber;
    private final int SINGLE_CLICK_TIME = 2000;
    private final int DOUBLE_CLICK_TIME = 200;
    private long lastClickTime = 0;
    private long lastClick = 4;
    private Map<String, String> mStringMap = new HashMap<>();
    private AddItemViewAdapter mAddItemViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mFabView.setOnLongClickListener(this);
    }

    @OnClick({R.id.text_Internet, R.id.active_equipment, R.id.login_game, R.id.fab_view, R.id.get_history_ip, R.id.save_params})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_Internet:
                boolean isNetWorkState = NetWorkUtil.checkNetStateInfo(this);
                if (isNetWorkState) {
                    //有网络，已做提示
                }
                break;
            case R.id.active_equipment:
                if (!NetWorkUtil.checkNetStateInfo(this)) {
                    return;
                }
                final String url = checkUpUrl();
                if (url == null) return;
                getHostPath(url);
                HttpStartModel.getActiveState(MainActivity.this, new HttpResultCallback(){

                    @Override
                    public void onSuccess(int code, String msg) {
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailed(int code, String msg) {
                        ToastUtil.showToast(MainActivity.this, msg, true);
                    }

                });
                URLBean urlBean = new URLBean();
                urlBean.setUrl(url);
                URLDao.insertURL(urlBean);
                break;
            case R.id.login_game:
                String userName = mInputUsername.getText().toString();
                String userPassWord = mInputPassword.getText().toString();
                Map<String, String> mapParams = new HashMap<>();
                mapParams.put("action", "login");
                if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(userPassWord)) {
                    mapParams.put("login_account", "A050676");
                    mapParams.put("password", "0697");
                } else if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPassWord)) {
                    mapParams.put("login_account", userName);
                    mapParams.put("password", userPassWord);
                } else {
                    ToastUtil.showToast(this, "请输入用户名或密码", true);
                    return;
                }
                final String loginurl = checkUpUrl();
                if (loginurl == null) return;
                getHostPath(loginurl);
                HttpStartModel.getLoginState(MainActivity.this, mapParams, new HttpResultCallback() {

                    @Override
                    public void onSuccess(int code, String msg) {
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailed(int code, String msg) {
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.fab_view:
                //单击
                //动态添加布局
                ToastUtil.showToast(this, "单击", true);
                clickNumber++;
                View addView = LayoutInflater.from(this).inflate(R.layout.add_float_layout, null);
                mRecyclerViewLayout.addView(addView);
                SharePrefrenceUtil.getInstance().saveAddItemView(this, clickNumber);
                break;
            case R.id.get_history_ip:
                showPopWindow();
                break;
            case R.id.save_params:
                saveParams();
                break;
        }
    }

    private void saveParams() {
        for (int i = 0; i < mRecyclerViewLayout.getChildCount(); i++) {
            View childAt = mRecyclerViewLayout.getChildAt(i);
            EditText editKey = childAt.findViewById(R.id.input_key);
            EditText editValue = childAt.findViewById(R.id.input_value);
            String skey = editKey.getText().toString();
            String sValue = editValue.getText().toString();
            if (!TextUtils.isEmpty(skey) && !TextUtils.isEmpty(sValue)) {
                mStringMap.put(skey, sValue);
            }
        }
        Log.e("tag", mStringMap.toString());
    }

    private void getHostPath(String url) {
        ConstantValue.URLHost = NetWorkUtil.getHost(url);
        ConstantValue.URLPath = NetWorkUtil.getURIPath(url);
    }

    @Nullable
    private String checkUpUrl() {
        final String url = mInputUrl.getText().toString();
        if (TextUtils.isEmpty(url)) {
            ToastUtil.showToast(this, "地址输入不能为空", true);
            return null;
        }
        return url;
    }

    private void showPopWindow() {
        View popView = getLayoutInflater().inflate(R.layout.popuwindow_layout_view, null);
        mRecyclerViewLayoutPOP = popView.findViewById(R.id.recyclerView_layout);
        LinearLayoutManager llManager = new LinearLayoutManager(this);
        mRecyclerViewLayoutPOP.setLayoutManager(llManager);
        List<URLBean> urlBeans = URLDao.queryAll();
        mAddressAdapter = new AddressAdapter(urlBeans);
        mAddressAdapter.setOnItemDeleteClickListener(this);
        mRecyclerViewLayoutPOP.setAdapter(mAddressAdapter);
        mRecyclerViewLayoutPOP.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mPopupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);
        boolean showing = mPopupWindow.isShowing();
        if (showing) {
            mPopupWindow.dismiss();
        } else {
            mPopupWindow.setFocusable(true);
            mPopupWindow.showAsDropDown(mDisplayPopViewLocal, 0, -1);
        }
    }

    @Override
    public void ClickImageButtonDel(URLBean urlBean) {
        URLDao.deleteURL(urlBean);
        mAddressAdapter.notifyDataSetChanged();
    }

    @Override
    public void ClickTextGetURl(String URL) {
        mInputUrl.setText(URL);
        mPopupWindow.dismiss();
    }

    @Override
    public boolean onLongClick(View v) {
        deleteNumber++;
        if (clickNumber - deleteNumber >= 0) {
            SharePrefrenceUtil.getInstance().saveAddItemView(this, clickNumber);
            //todo
            mRecyclerViewLayout.removeViewAt(clickNumber - deleteNumber);
        }
        return true;
    }
}
