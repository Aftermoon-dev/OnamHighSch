package darkhost.onamhighsch;

/**
 * Created by 민재 on 2016-04-02.
 */
public class SchItem {
    int date;
    String sch;

    int getDate(){
        return this.date;
    }
    String getSch(){
        return this.sch;
    }

    SchItem(int date, String sch){
        this.date=date;
        this.sch=sch;
    }
}
