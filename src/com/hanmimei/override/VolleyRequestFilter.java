package com.hanmimei.override;

import com.android.volley.Request;
import com.android.volley.RequestQueue.RequestFilter;

public class VolleyRequestFilter implements RequestFilter{
	
	

	@Override
	public boolean apply(Request<?> arg0) {
		return true;
	}

}
