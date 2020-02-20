package ai.salesken.onboarding.utils;

import java.util.ArrayList;

public interface URLUtil {

	public String sendPostForm(String url, ArrayList<String[]> formData);

	public String sendGet(String url) throws Exception;

}
