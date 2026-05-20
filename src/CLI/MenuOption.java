package CLI;

import java.lang.Runnable;

public class MenuOption {
    
    private String key;
    private Runnable action;

    public MenuOption(String key, Runnable action){
        this.key = key;
        this.action = action;
    }

    public String getKey(){
        return this.key;
    }

    public Runnable getAction(){
        return this.action;
    }

}
