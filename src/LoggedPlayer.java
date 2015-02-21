

/**
 * Created by Brettness on 2/19/15.
 */
public class LoggedPlayer {

    private String firstName;
    private String lastName;
    private String name;
    private String position;
    private String espnId;

    public LoggedPlayer(String name, String position, String espnId){
        this.position = position;
        this.espnId = espnId;
        this.name = name;
    }

    public String getEspnId(){
        return espnId;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }
}
