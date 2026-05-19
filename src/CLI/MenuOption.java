package CLI;

public class MenuOption {
    
    private String key;
    private Action action;

    public MenuOption(String key, Action action){
        this.key = key;
        this.action = action;
    }

    public String getKey(){
        return this.key;
    }

    public Action getAction(){
        return this.action;
    }

}
