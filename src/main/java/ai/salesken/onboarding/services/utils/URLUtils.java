package ai.salesken.onboarding.services.utils;

 
import java.util.ArrayList;

public interface URLUtils {
	public String sendGet(String url) throws Exception;

	public String sendPostForm(String url, ArrayList<String[]> formData);
}
