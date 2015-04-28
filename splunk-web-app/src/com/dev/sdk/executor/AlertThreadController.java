package com.dev.sdk.executor;

public class AlertThreadController implements Runnable {
	private String alertType;
	
	public AlertThreadController(String ipType){
		this.alertType = ipType;
	}
	
	@Override
	public void run() {
		try{
			if("ACCESS_LOG_ALERT".equalsIgnoreCase(this.alertType)){
				System.out.println("AlertThreadController - " + Thread.currentThread().getName() + " started processing for " + this.alertType);
				AccesslogAlertExecutor.execute();
			}else if("SYS_LOG_ALERT".equalsIgnoreCase(this.alertType)){
				System.out.println("AlertThreadController - " + Thread.currentThread().getName() + " started processing for " + this.alertType);
				SyslogAlertExecutor.execute();
			}else{
				System.out.println("Invalid Alert Type : " + this.alertType);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}