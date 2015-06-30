package com.timer_task;

import java.util.TimerTask;

import com.shot_img.ShotHandler;

public class DelTempDirTimerTask extends TimerTask {

	@Override
	public void run() {
		try {
			System.out.println("执行定时删临时文件夹开始");
			new ShotHandler().delTempDir();
			System.out.println("执行定时删临时文件夹完成");
		} catch (Exception e) {
			System.out.println("执行定时删临时文件夹失败");
			e.printStackTrace();
		}
	}

}
