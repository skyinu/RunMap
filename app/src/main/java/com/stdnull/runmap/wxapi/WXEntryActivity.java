package com.stdnull.runmap.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.stdnull.runmap.R;
import com.stdnull.runmap.common.RMConfiguration;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信分享的回调Activity
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
    private IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, RMConfiguration.WEIXIN_APP_ID, false);
		try {
			api.handleIntent(getIntent(), this);
		} catch (Exception e) {
			e.printStackTrace();
		}

    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}


	@Override
	public void onReq(BaseReq baseReq) {

	}

	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			break;
		case BaseResp.ErrCode.ERR_UNSUPPORT:
			result = R.string.errcode_unsupported;
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		finish();
	}
	

}