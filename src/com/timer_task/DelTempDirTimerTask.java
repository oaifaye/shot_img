package com.timer_task;

import java.util.TimerTask;

import com.shot_img.ShotHandler;

public class DelTempDirTimerTask extends TimerTask {

	@Override
	public void run() {
		try {
			System.out.println("ִ�ж�ʱɾ��ʱ�ļ��п�ʼ");
			new ShotHandler().delTempDir();
			System.out.println("ִ�ж�ʱɾ��ʱ�ļ������");
		} catch (Exception e) {
			System.out.println("ִ�ж�ʱɾ��ʱ�ļ���ʧ��");
			e.printStackTrace();
		}
	}

}
