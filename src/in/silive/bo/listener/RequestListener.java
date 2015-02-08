package in.silive.bo.listener;

public interface RequestListener {

	public void preRequest();
	
	public void postRequest(String result);
}
