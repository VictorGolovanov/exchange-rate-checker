package app;

public class Gif {
    public Data data;

    public void setData(Data data) {
        this.data = data;
    }

}
class Data {
    private String id;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
