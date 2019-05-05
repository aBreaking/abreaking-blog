package com.abreaking.office.resource;

import java.io.InputStream;

/**
 * 获取某个资源，转为流的形式
 * 资源一般都是某个配置文件，或者某个需要解析的Inparam文件
 */
public interface Resource {

    InputStream getInputStream() ;

    String name();

}
