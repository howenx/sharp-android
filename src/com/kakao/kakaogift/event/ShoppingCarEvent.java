/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-9-1 下午3:12:28 
**/
package com.kakao.kakaogift.event;

/**
 * @author vince
 *
 */
public class ShoppingCarEvent {
	public static final int ADD = 1;
	public static final int SETBOTTOM = 2;
	public static final int SETCUSTOMSTATE = 3;
	private int handleCode;  //1 加入购物车  2 setBottom  3 setCustomState
	private int nums;

	public ShoppingCarEvent(int  handleCode,int nums) {
		this.handleCode = handleCode;
		this.nums = nums;
	}
	public ShoppingCarEvent(int  handleCode) {
		this.handleCode = handleCode;
	}

	public int getNums() {
		return nums;
	}

	public void setNums(int nums) {
		this.nums = nums;
	}

	public int getHandleCode() {
		return handleCode;
	}

	public void setHandleCode(int handleCode) {
		this.handleCode = handleCode;
	}
	
	
}
