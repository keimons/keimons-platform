package com.keimons.platform.test;

import com.keimons.platform.annotation.AProcessor;
import com.keimons.platform.process.BaseProcessor;
import com.keimons.platform.process.ThreadLevel;

@AProcessor(MsgCode = 1001, Interval = 200, Desc = "登录协议", ThreadLevel = ThreadLevel.AUTO)
public class LoginProcessor1001 extends BaseProcessor {


}