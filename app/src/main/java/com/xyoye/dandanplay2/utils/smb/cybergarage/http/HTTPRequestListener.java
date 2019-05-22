/******************************************************************
*
*	CyberHTTP for Java
*
*	Copyright (C) Satoshi Konno 2002
*
*	File: HTTPRequestListener.java
*
*	Revision;
*
*	12/13/02
*		- first revision.
*	
******************************************************************/

package com.xyoye.dandanplay2.utils.smb.cybergarage.http;

/** HTTP 请求监听 */
public interface HTTPRequestListener
{
	/** http请求接收 */
    void httpRequestRecieved(HTTPRequest httpReq);
}
