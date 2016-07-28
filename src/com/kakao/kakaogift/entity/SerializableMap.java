/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-18 下午4:42:44 
**/
package com.kakao.kakaogift.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * @author eric
 *
 */
/**
 * 序列化map供Bundle传递map使用
 * Created  on 13-12-9.
 */
public class SerializableMap implements Serializable {

    private Map<String,String> map;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> data) {
        this.map = data;
    }
}
