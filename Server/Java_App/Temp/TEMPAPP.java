package Server.Java_App.Temp;

import java.util.concurrent.TimeUnit;

public class TEMPAPP{
    public static void main(String[] args) {
        int i = 0;
        for(;;i++){
            System.out.println(i);
            try{
            TimeUnit.SECONDS.sleep(1);}
            catch(Exception e){}
        }
    }
}