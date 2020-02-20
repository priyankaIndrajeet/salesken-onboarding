package ai.salesken.onboarding.utils.Validation;

import java.sql.Timestamp;
import java.util.Date;


public class ValidateDate {
    public boolean validEventDate(Timestamp eventTimestamp){
        if (eventTimestamp.after(new Date(System.currentTimeMillis() - 10 * 60 * 1000))){
            return true;
        }
        return false;
    }
}
